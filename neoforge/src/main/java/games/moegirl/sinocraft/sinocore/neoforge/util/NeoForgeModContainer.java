package games.moegirl.sinocraft.sinocore.neoforge.util;

import games.moegirl.sinocraft.sinocore.utility.modloader.IModContainer;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLLoader;

import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class NeoForgeModContainer implements IModContainer {
    private static final FileSystemProvider UNION_FILE_SYSTEM_PROVIDER = FileSystemProvider.installedProviders()
            .stream()
            .filter(f -> "union".equalsIgnoreCase(f.getScheme()))
            .findFirst()
            .orElseThrow();

    private final ModContainer container;

    public NeoForgeModContainer(ModContainer container) {
        this.container = container;
    }

    @Override
    public String getId() {
        return container.getModId();
    }

    @Override
    public String getName() {
        return container.getModInfo().getDisplayName();
    }

    @Override
    public String getVersion() {
        return container.getModInfo().getVersion().toString();
    }

    @Override
    public Path findModFile(String... subPaths) {
        return container.getModInfo().getOwningFile().getFile().findResource(subPaths);
    }

    @Override
    public Object getModContainer() {
        return container;
    }

    @Override
    public List<Path> getRootFiles() {
        var layer = FMLLoader.getGameLayer().findModule(container.getModInfo().getOwningFile().moduleName()).orElseThrow();
        return container.getModInfo().getOwningFile().getFile().getScanResult().getClasses()
                .stream()
                .map(d -> Class.forName(layer, d.clazz().getClassName()))
                .map(this::getPathByClass)
                .toList();
    }

    private Path getPathByClass(Class<?> mainClass) {
        try {
            var uri = mainClass.getProtectionDomain().getCodeSource().getLocation().toURI();
            FileSystem fs;
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                fs = FileSystems.getFileSystem(uri);
            } else {
                fs = UNION_FILE_SYSTEM_PROVIDER.getFileSystem(uri);
            }
            return fs.getPath("/");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Path getResourcePathOnly(ModContainer container) {
        return container.getModInfo().getOwningFile().getFile().getFilePath();
    }
}
