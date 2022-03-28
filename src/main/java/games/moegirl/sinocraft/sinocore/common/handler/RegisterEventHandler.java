package games.moegirl.sinocraft.sinocore.common.handler;

import games.moegirl.sinocraft.sinocore.common.util.ClassFiles;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * All other register events
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterEventHandler {

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        try {
            ClassFiles.forPackage("games.moegirl.sinocraft.sinocore.api.capability", 1)
                    .filter(Files::isRegularFile)
                    .map(f -> f.getFileName().toString())
                    .filter(fn -> fn.startsWith("I"))
                    .filter(fn -> fn.endsWith(".class"))
                    .map(fn -> fn.substring(1, fn.length() - 6))
                    .forEach(n -> registerCapability(n, event));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void registerCapability(String className, RegisterCapabilitiesEvent event) {
        try {
            String classPath = "games.moegirl.sinocraft.sinocore.api.capability." + className;
            Class<?> aClass = RegisterEventHandler.class.getClassLoader().loadClass(classPath);
            event.register(aClass);
        } catch (Exception ignored) {
        }
    }
}
