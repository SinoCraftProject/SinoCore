package games.moegirl.sinocraft.sinocore.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import games.moegirl.sinocraft.sinocore.api.gui.layout.LayoutManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class LayoutCommand {
    public static final String ID = "id";

    public static final LiteralArgumentBuilder<CommandSourceStack> LAYOUT_COMMAND = literal("layout")
            .requires(s -> s.hasPermission(2))
            .then(literal("reload")
                    .then(argument(ID, ResourceLocationArgument.id())
                            .executes(LayoutCommand::onReloadId)
                    )
                    .executes(LayoutCommand::onReloadAll)
            );

    private static int onReloadAll(CommandContext<CommandSourceStack> context) {
        LayoutManager.reloadAllLayouts();
        return 1;
    }

    private static int onReloadId(CommandContext<CommandSourceStack> context) {
        var id = ResourceLocationArgument.getId(context, ID);
        LayoutManager.reloadLayout(id);
        return 1;
    }
}
