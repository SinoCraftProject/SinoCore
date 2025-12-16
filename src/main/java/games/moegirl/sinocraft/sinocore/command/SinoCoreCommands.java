package games.moegirl.sinocraft.sinocore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.registry.ICommandRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.RegistryManager;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public class SinoCoreCommands {
    public static final ICommandRegistry REGISTRY = RegistryManager.createCommand(SinoCore.MODID);

    public static void register() {
        REGISTRY.register(new BaseCommand());

        REGISTRY.register();
    }

    public static class BaseCommand implements ICommandRegistry.SimpleCommand {


        private static final LiteralArgumentBuilder<CommandSourceStack> BASE_COMMAND = literal("sinocore")
                .then(LayoutCommand.LAYOUT_COMMAND);

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(BASE_COMMAND);
        }
    }
}
