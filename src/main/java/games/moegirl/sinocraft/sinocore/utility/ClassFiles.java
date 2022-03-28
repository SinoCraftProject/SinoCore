package games.moegirl.sinocraft.sinocore.utility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.stream.Stream;

/**
 * A class to get classes jar or directory.
 */
public class ClassFiles {

    // forge "union" scheme
    private static final FileSystemProvider UFSP = FileSystemProvider.installedProviders().stream()
            .filter(f -> "union".equalsIgnoreCase(f.getScheme())).findFirst().orElseThrow();

    private static boolean isInitialized = false;
    private static FileSystem SOURCE_FS;

    /**
     * Get classes path
     * @return path
     * @throws URISyntaxException if this URL is not formatted strictly according to
     *                            RFC2396 and cannot be converted to a URI.
     */
    public static Path getSource() throws URISyntaxException {
        if (!isInitialized) {
            URI CLASSES_URI = ClassFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            if ("file".equalsIgnoreCase(CLASSES_URI.getScheme())) {
                SOURCE_FS = FileSystems.getFileSystem(CLASSES_URI);
            } else {
                SOURCE_FS = UFSP.getFileSystem(CLASSES_URI);
            }
            isInitialized = true;
        }
        return SOURCE_FS.getPath("/");
    }

    /**
     * Return a stream contains all files in the package
     * @param packageName package name
     * @return the {@link Stream} of {@link Path}
     * @throws IOException if an I/O error is thrown when accessing the starting file.
     * @throws URISyntaxException if this URL is not formatted strictly according to
     *                            RFC2396 and cannot be converted to a URI.
     */
    public static Stream<Path> forPackage(String packageName) throws IOException, URISyntaxException {
        return Files.walk(getSource().resolve("/" + packageName.replace(".", "/")));
    }

    /**
     * Return a stream contains all files in the package
     * @param packageName package name
     * @param depth the maximum number of directory levels to visit
     * @return the {@link Stream} of {@link Path}
     * @throws IOException if an I/O error is thrown when accessing the starting file.
     * @throws URISyntaxException if this URL is not formatted strictly according to
     *                            RFC2396 and cannot be converted to a URI.
     */
    public static Stream<Path> forPackage(String packageName, int depth) throws IOException, URISyntaxException {
        return Files.walk(getSource().resolve("/" + packageName.replace(".", "/")), depth);
    }
}
