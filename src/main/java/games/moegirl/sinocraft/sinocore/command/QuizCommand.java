package games.moegirl.sinocraft.sinocore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import games.moegirl.sinocraft.sinocore.api.capability.IQuizzingPlayer;
import games.moegirl.sinocraft.sinocore.api.capability.SCCapabilities;
import games.moegirl.sinocraft.sinocore.capability.QuizzingPlayer;
import games.moegirl.sinocraft.sinocore.config.QuizQuestionsConfig;
import games.moegirl.sinocraft.sinocore.config.model.QuizModel;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class QuizCommand {
    public static LiteralCommandNode<CommandSourceStack> QUIZ = literal("quiz")
            .requires(s -> s.hasPermission(2))
            .then(literal("refetch")
                    .executes(QuizCommand::reFetch)
            )
            .then(literal("start")
                    .then(argument("max_stage", IntegerArgumentType.integer(1, QuizQuestionsConfig.MAX_STAGE.get()))
                            .suggests(QuizCommand::onStartSuggest)
                            .executes(QuizCommand::onStart)
                    ))
            .then(literal("next")
                    .then(argument("answer", StringArgumentType.string())
                            .suggests(QuizCommand::onNextSuggest)
                            .executes(QuizCommand::onNext)
                    )
            )
            .then(literal("succeed")
                    .executes(QuizCommand::onSucceed)
            )
            .then(literal("fail")
                    .executes(QuizCommand::onFail)
            )
            .build();

    public static int reFetch(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(new TextComponent("ReFetching..."), true);

        if (!QuizQuestionsConfig.ENABLED.get()) {
            context.getSource().sendSuccess(new TextComponent("Not enabled feature."), true);
            return 0;
        }

        QuizQuestionsConfig.QUESTIONS.doReFetch();
        return 1;
    }


    public static CompletableFuture<Suggestions> onStartSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        builder.suggest("<Max stages>");
        return builder.buildFuture();
    }

    public static int onStart(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            makeNotPlayer(context.getSource());
            return 0;
        }

        if (!QuizQuestionsConfig.ENABLED.get()) {
            makeNotEnabled(player);
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
        var maxStage = context.getArgument("max_stage", Integer.class);

        doStart(player, quiz, maxStage);
        return Command.SINGLE_SUCCESS;
    }

    public static CompletableFuture<Suggestions> onNextSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        builder.suggest("<Answer>");
        return builder.buildFuture();
    }

    public static int onNext(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            makeNotPlayer(context.getSource());
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
        var answer = context.getArgument("answer", String.class);

        doNext(player, quiz, answer);

        return Command.SINGLE_SUCCESS;
    }

    public static int onFail(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            makeNotPlayer(context.getSource());
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());

        var result = doFail(player, quiz);
        return result ? 0 : Command.SINGLE_SUCCESS;
    }

    private static int onSucceed(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            makeNotPlayer(context.getSource());
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());

        var result = doSucceed(player, quiz);
        return result ? Command.SINGLE_SUCCESS : 0;
    }

    public static QuizModel.Question nextQuestion() {
        return QuizQuestionsConfig.QUESTIONS.random();
    }

    public static void doStart(Player player, IQuizzingPlayer quiz, int maxStage) {
        quiz.deserializeNBT(new QuizzingPlayer().serializeNBT());

        quiz.setQuizzing(true);
        quiz.setSucceed(false);
        quiz.setMaxQuizStage(maxStage);
        quiz.setQuizStage(0);

        makeStarted(player);

        doNext(player, quiz);
    }

    public static boolean isCorrect(Player player, IQuizzingPlayer quiz, String answer) {
        return quiz.isCorrect(answer);
    }

    public static void doNext(Player player, IQuizzingPlayer quiz) {
        quiz.clearAnswers();

        var question = nextQuestion();
        quiz.setQuizStage(quiz.getQuizStage() + 1);
        quiz.setQuestion(question.question());

        var res = shuffle(player, question.answers());

        // Todo: qyl27: we have 4 answers for now.
        if (res.size() != 4) {
            throw new RuntimeException("Not 4 answers!");
        }

        quiz.addAnswer(res.get(0).answer(), "A", res.get(0).isCorrect());
        quiz.addAnswer(res.get(1).answer(), "B", res.get(1).isCorrect());
        quiz.addAnswer(res.get(2).answer(), "C", res.get(2).isCorrect());
        quiz.addAnswer(res.get(3).answer(), "D", res.get(3).isCorrect());

        makeQuestion(player, quiz);
    }

    public static List<QuizModel.Question.Answer> shuffle(Player player, List<QuizModel.Question.Answer> answers) {
        var rand = player.getRandom();

        var length = answers.size();

        for (int i = 0; i < length; i++) {
            var randIndex = rand.nextInt(0, length);
            var temp = answers.get(i);
            answers.set(i, answers.get(randIndex));
            answers.set(randIndex, temp);
        }

        return answers;
    }

    public static void doNext(Player player, IQuizzingPlayer quiz, String answer) {
        if (!quiz.isQuizzing()) {
            makeWrongState(player);
            return;
        }

        if (!isCorrect(player, quiz, answer) && !isEnded(player, quiz)) {
            makeWrongAnswer(player);

            player.getCommandSenderWorld()
                    .getServer()
                    .getCommands()
                    .performCommand(player.createCommandSourceStack(), "/sinocore quiz fail");

            return;
        }

        if (isEnded(player, quiz)) {
            player.getCommandSenderWorld()
                    .getServer()
                    .getCommands()
                    .performCommand(player.createCommandSourceStack(), "/sinocore quiz succeed");

            return;
        }

        makeCorrectAnswer(player);

        doNext(player, quiz);
    }

    public static boolean hasReachedMaxStage(Player player, IQuizzingPlayer quiz) {
        return quiz.getQuizStage() >= quiz.maxQuizStage();
    }

    public static boolean isEnded(Player player, IQuizzingPlayer quiz) {
        return hasReachedMaxStage(player, quiz) || !quiz.isQuizzing();
    }

    public static boolean doFail(Player player, IQuizzingPlayer quiz) {
        if (isEnded(player, quiz)) {
            makeNotStarted(player);
            return false;
        }

        quiz.setSucceed(false);
        quiz.setQuizzing(false);

        if (quiz.isSucceed()) {
            makeWrongState(player);
            return false;
        }

        makeFail(player);
        return true;
    }

    public static boolean doSucceed(Player player, IQuizzingPlayer quiz) {
        if (!isEnded(player, quiz)) {
            makeNotStarted(player);

            return false;
        }

        quiz.setSucceed(true);
        quiz.setQuizzing(false);

        if (!quiz.isSucceed()) {
            makeWrongState(player);
            return false;
        }

        makeSucceed(player);
        return true;
    }

    public static final String MESSAGE_NOT_PLAYER = "sinocore.command.quiz.not_player";
    public static final String MESSAGE_NOT_STARTED = "sinocore.command.quiz.not_started";
    public static final String MESSAGE_NOT_ENABLED = "sinocore.command.quiz.not_enabled";
    public static final String MESSAGE_SUCCEED = "sinocore.command.quiz.succeed";
    public static final String MESSAGE_BROADCAST_SUCCEED = "sinocore.command.quiz.broadcast_succeed";
    public static final String MESSAGE_FAIL = "sinocore.command.quiz.fail";
    public static final String MESSAGE_WRONG_STATE = "sinocore.command.quiz.wrong_state";
    public static final String MESSAGE_STARTED = "sinocore.command.quiz.started";
    public static final String MESSAGE_QUESTION = "sinocore.command.quiz.question";
    public static final String MESSAGE_QUESTION_LAST = "sinocore.command.quiz.question_last";
    public static final String MESSAGE_ANSWER_MARKED = "sinocore.command.quiz.answer_marked";
    public static final String MESSAGE_ANSWER_WRONG = "sinocore.command.quiz.answer_wrong";
    public static final String MESSAGE_ANSWER_RIGHT = "sinocore.command.quiz.answer_right";
    public static final String MESSAGE_ANSWER_A = "sinocore.command.quiz.answer_a";
    public static final String MESSAGE_ANSWER_B = "sinocore.command.quiz.answer_b";
    public static final String MESSAGE_ANSWER_C = "sinocore.command.quiz.answer_c";
    public static final String MESSAGE_ANSWER_D = "sinocore.command.quiz.answer_d";

    public static void makeNotPlayer(CommandSourceStack source) {
        source.sendFailure(new TranslatableComponent(MESSAGE_NOT_PLAYER));
    }

    public static void makeNotStarted(Player player) {
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_NOT_STARTED)
                .withStyle(ChatFormatting.RED), true);
    }

    public static void makeSucceed(Player player) {
        broadcast(player, new TranslatableComponent(MESSAGE_BROADCAST_SUCCEED, player.getDisplayName()).withStyle(ChatFormatting.GREEN));
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_SUCCEED).withStyle(ChatFormatting.GREEN), true);
    }

    public static void makeFail(Player player) {
        player.createCommandSourceStack().sendFailure(new TranslatableComponent(MESSAGE_FAIL)
                .withStyle(ChatFormatting.RED));
    }

    public static void makeWrongState(Player player) {
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_WRONG_STATE)
                .withStyle(ChatFormatting.RED), true);
    }

    public static void makeWrongAnswer(Player player) {
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_ANSWER_WRONG)
                .withStyle(ChatFormatting.RED), true);
    }

    public static void makeNotEnabled(Player player) {
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_NOT_ENABLED)
                .withStyle(ChatFormatting.RED), true);
    }

    public static void makeCorrectAnswer(Player player) {
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_ANSWER_RIGHT)
                .withStyle(ChatFormatting.LIGHT_PURPLE), true);
    }

    public static void makeStarted(Player player) {
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_STARTED)
                .withStyle(ChatFormatting.LIGHT_PURPLE), true);
    }

    public static void makeQuestion(Player player, IQuizzingPlayer quiz) {
        var component = new TranslatableComponent(MESSAGE_QUESTION, quiz.getQuestion()).withStyle(ChatFormatting.AQUA).append("\n");

        for (var q : quiz.getAnswers()) {
            component.append(new TranslatableComponent(MESSAGE_ANSWER_MARKED, q.getA()).withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent(q.getB()).withStyle(ChatFormatting.GREEN)).append("\n");
        }

        component.append(new TranslatableComponent(MESSAGE_QUESTION_LAST).withStyle(ChatFormatting.AQUA));

        player.createCommandSourceStack().sendSuccess(component, true);
    }

    public static void broadcast(Player player, Component message) {
        if (player.level.isClientSide) {
            return;
        }

        var players = player.level.getServer().getPlayerList().getPlayers();
        for (var p : players) {
            if (p.getUUID().equals(player.getUUID())) {
                continue;
            }
            p.sendMessage(message, Util.NIL_UUID);
        }
    }
}
