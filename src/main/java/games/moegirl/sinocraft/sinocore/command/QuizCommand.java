package games.moegirl.sinocraft.sinocore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import games.moegirl.sinocraft.sinocore.api.capability.IQuizzingPlayer;
import games.moegirl.sinocraft.sinocore.api.capability.SCCapabilities;
import games.moegirl.sinocraft.sinocore.capability.QuizzingPlayer;
import games.moegirl.sinocraft.sinocore.config.QuizModelConfig;
import games.moegirl.sinocraft.sinocore.config.model.QuizConstants;
import games.moegirl.sinocraft.sinocore.config.model.QuizModel;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
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
            .then(literal("start")
                    .requires(s -> s.hasPermission(2))
                    .then(argument("max_stage", IntegerArgumentType.integer(1, 50))
                            .then(argument("player", EntityArgument.player())
                                    .executes(QuizCommand::onStart))
//                            .suggests(QuizCommand::onStartSuggest)
                    ))
            .then(literal("next")
                    .then(argument("answer", StringArgumentType.string())
                            .then(argument("player", EntityArgument.player())
                                    .executes(QuizCommand::onNext)
                            )
                    )
            )
//            .then(literal("succeed")
//                    .requires(s -> s.hasPermission(2))
//                    .then(argument("player", EntityArgument.player())
//                            .executes(QuizCommand::onSucceed)
//                    )
//            )
//            .then(literal("fail")
//                    .requires(s -> s.hasPermission(2))
//                    .then(argument("player", EntityArgument.player())
//                            .executes(QuizCommand::onFail)
//                    )
//            )
            .then(literal("reload")
                    .requires(s -> s.hasPermission(2))
                    .executes(QuizCommand::onReload)
            )
            .then(literal("load")
                    .requires(s -> s.hasPermission(2))
                    .then(argument("url", StringArgumentType.greedyString())
                            .executes(QuizCommand::onLoad)
                    )
            )
            .build();

//    public static CompletableFuture<Suggestions> onStartSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
//        builder.suggest("<Max stages>");
//        return builder.buildFuture();
//    }

    public static int onStart(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var selector = context.getArgument("player", EntitySelector.class);
        var player = selector.findSinglePlayer(context.getSource());

        if (!QuizModelConfig.CONFIG.ENABLED.get()) {
            makeNotEnabled(player);
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
        var maxStage = context.getArgument("max_stage", Integer.class);

        doStart(player, quiz, maxStage);
        return Command.SINGLE_SUCCESS;
    }

    public static int onNext(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var selector = context.getArgument("player", EntitySelector.class);
        var player = selector.findSinglePlayer(context.getSource());

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
        var answer = context.getArgument("answer", String.class);

        doNext(player, quiz, answer);

        return Command.SINGLE_SUCCESS;
    }

//    public static int onFail(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
//        var selector = context.getArgument("player", EntitySelector.class);
//        var player = selector.findSinglePlayer(context.getSource());
//
//        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
//
//        var result = doFail(player, quiz);
//        return result ? 0 : Command.SINGLE_SUCCESS;
//    }
//
//    public static int onSucceed(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
//        var selector = context.getArgument("player", EntitySelector.class);
//        var player = selector.findSinglePlayer(context.getSource());
//
//        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
//
//        var result = doSucceed(player, quiz);
//        return result ? Command.SINGLE_SUCCESS : 0;
//    }

    public static int onReload(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(new TextComponent("Reloading...")
                .withStyle(ChatFormatting.DARK_AQUA), true);

        if (!QuizModelConfig.CONFIG.ENABLED.get()) {
            context.getSource().sendSuccess(new TextComponent("Not enabled feature."), true);
            return 0;
        }

        QuizModel.getInstance().reFetch(true);
        return 1;
    }

    public static int onLoad(CommandContext<CommandSourceStack> context) {
        var url = context.getArgument("url", String.class);

        context.getSource().sendSuccess(new TextComponent("Temporarily set Quiz URL to: " + url)
                        .withStyle(ChatFormatting.DARK_AQUA), true);

        QuizConstants.URL = url;

        return onReload(context);
    }

    public static QuizModel.Question nextQuestion() {
        return QuizModel.getInstance().random();
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

    public static boolean doNext(Player player, IQuizzingPlayer quiz, String answer) {
        if (!quiz.isQuizzing()) {
            makeWrongState(player);
            return false;
        }

        quiz.setQuizStage(quiz.getQuizStage() + 1);

        if (!isCorrect(player, quiz, answer) && !isEnded(player, quiz)) {
            makeWrongAnswer(player);
//            player.getCommandSenderWorld()
//                    .getServer()
//                    .getCommands()
//                    .performCommand(player.createCommandSourceStack(), "/sinocore quiz fail @s");
            doFail(player, quiz);
            return true;
        }

        makeCorrectAnswer(player);

        if (hasReachedMaxStage(player, quiz)) {
//            player.getCommandSenderWorld()
//                    .getServer()
//                    .getCommands()
//                    .performCommand(player.createCommandSourceStack(), "/sinocore quiz succeed @s");
            doSucceed(player, quiz);
            return true;
        } else {
            doNext(player, quiz);
            return true;
        }
    }

    public static boolean hasReachedMaxStage(Player player, IQuizzingPlayer quiz) {
        return quiz.getQuizStage() >= quiz.maxQuizStage();
    }

    public static boolean isEnded(Player player, IQuizzingPlayer quiz) {
        return hasReachedMaxStage(player, quiz) || !quiz.isQuizzing();
    }

    public static boolean doFail(Player player, IQuizzingPlayer quiz) {
        if (!quiz.isQuizzing()) {
            makeWrongState(player);

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
        if (!isEnded(player, quiz) && !quiz.isQuizzing()) {
            makeWrongState(player);
            return false;
        }

        if (!quiz.isQuizzing()) {
            makeWrongState(player);
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
        broadcast(player, new TranslatableComponent(MESSAGE_BROADCAST_SUCCEED, player.getDisplayName())
                .withStyle(ChatFormatting.GREEN)
                .withStyle(ChatFormatting.BOLD));
        player.createCommandSourceStack().sendSuccess(new TranslatableComponent(MESSAGE_SUCCEED)
                .withStyle(ChatFormatting.GREEN)
                .withStyle(ChatFormatting.BOLD), true);
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

    public static void broadcast(Player self, Component message) {
        if (self.level.isClientSide) {
            return;
        }

        var server = self.level.getServer();
        server.createCommandSourceStack().sendSuccess(message, true);

        var players = server.getPlayerList().getPlayers();
        for (var p : players) {
            if (p.getUUID().equals(self.getUUID())) {
                continue;
            }
            p.sendMessage(message, Util.NIL_UUID);
        }
    }
}
