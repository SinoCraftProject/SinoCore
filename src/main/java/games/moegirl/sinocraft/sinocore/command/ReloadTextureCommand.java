package games.moegirl.sinocraft.sinocore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import games.moegirl.sinocraft.sinocore.api.command.CommandBuilder;
import games.moegirl.sinocraft.sinocore.api.utility.texture.TextureMap;
import games.moegirl.sinocraft.sinocore.api.utility.texture.TextureParser;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ReloadTextureCommand {
    public static LiteralCommandNode<CommandSourceStack> RELOAD = literal("reload")
            .requires(s -> s.hasPermission(2))
            .then(literal("texture")
                    .then(argument("texture_name", ResourceLocationArgument.id())
                            .suggests(ReloadTextureCommand::onSuggests)
                            .executes(ReloadTextureCommand::onReload)
                    ))
            .build();

    public static CompletableFuture<Suggestions> onSuggests(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        TextureParser.names()
                .stream()
                .map(ResourceLocation::toString)
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int onReload(CommandContext<CommandSourceStack> context) {
        ResourceLocation name = ResourceLocationArgument.getId(context, "texture_name");
        TextureMap map = TextureParser.get(name);
        if (map == null) {
            context.getSource().sendFailure(new TextComponent("Not found texture map named " + name));
        } else {
            map.reload();
            context.getSource().sendSuccess(new TextComponent("Reload succeed"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
