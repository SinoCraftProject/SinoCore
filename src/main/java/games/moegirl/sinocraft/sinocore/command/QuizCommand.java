package games.moegirl.sinocraft.sinocore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import games.moegirl.sinocraft.sinocore.api.capability.SCCapabilities;
import games.moegirl.sinocraft.sinocore.capability.QuizzingPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.targets.FMLServerLaunchHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class QuizCommand {
    public static LiteralCommandNode<CommandSourceStack> QUIZ = literal("quiz")
            .then(literal("start")
                    .then(argument("max_stage", IntegerArgumentType.integer(1, 25))
                            .suggests(QuizCommand::onStartSuggest)
                            .executes(QuizCommand::onStart)
                    ))
            .then(literal("next")
                    .then(argument("answer", StringArgumentType.string())
                            .suggests(QuizCommand::onNextSuggest)
                            .executes(QuizCommand::onNext)
                    )
            )
            .then(literal("fail")
                    .executes(QuizCommand::onFail)
            )
            .build();


    public static CompletableFuture<Suggestions> onStartSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        builder.suggest("<Max stages>");
        return builder.buildFuture();
    }

    public static int onStart(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
        var maxStage = context.getArgument("max_stage", Integer.class);

        quiz.setMaxQuizStage(maxStage);

        var next = nextQuestion();
        quiz.setQuestion(next.getA());
        for (var entry : next.getB().entrySet()) {
            quiz.addAnswer(entry.getKey(), entry.getValue());
        }
        quiz.setQuizzing(true);

        return Command.SINGLE_SUCCESS;
    }

    public static CompletableFuture<Suggestions> onNextSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        builder.suggest("<Answer>");
        return builder.buildFuture();
    }

    public static int onNext(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());
        var maxStage = quiz.maxQuizStage();

        var answer = context.getArgument("answer", String.class);
        if (!quiz.isCorrect(answer)) {
            player.getCommandSenderWorld()
                    .getServer()
                    .getCommands()
                    .performCommand(player.createCommandSourceStack(), "/sinocore quiz fail");
            return Command.SINGLE_SUCCESS;
        }

        if (maxStage >= quiz.getQuizStage()) {
            quiz.setSucceed(true);
        }

        var next = nextQuestion();
        quiz.setQuestion(next.getA());
        for (var entry : next.getB().entrySet()) {
            quiz.addAnswer(entry.getKey(), entry.getValue());
        }
        quiz.setQuizStage(quiz.getQuizStage() + 1);

        return Command.SINGLE_SUCCESS;
    }

    public static int onFail(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getEntity();

        if (!(source instanceof Player player)) {
            return 0;
        }

        var quiz = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY).orElse(new QuizzingPlayer());

        quiz.setQuizzing(false);
        quiz.setSucceed(false);

        return Command.SINGLE_SUCCESS;
    }

    public static Tuple<String, Map<String, Boolean>> nextQuestion() {
        var map = new HashMap<String, Boolean>();

        // Todo: qyl27: waiting for impl.
        var question = "";

        return new Tuple<String, Map<String, Boolean>>(question, map);
    }
}
