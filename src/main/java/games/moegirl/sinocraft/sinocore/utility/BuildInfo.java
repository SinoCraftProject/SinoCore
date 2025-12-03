package games.moegirl.sinocraft.sinocore.utility;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.ZonedDateTime;

public class BuildInfo {
    public static final String MOD_VERSION;
    public static final @Nullable ZonedDateTime BUILD_TIME;
    public static final String COMMIT_ID;
    public static final boolean IS_RELEASE;

    private static final Logger LOGGER = LogUtils.getLogger();

    static {
        String mod_version;
        ZonedDateTime buildTime;
        String commitId;
        boolean release;

        try {
            var properties = new java.util.Properties();
            properties.load(BuildInfo.class.getResourceAsStream("/build_info.properties"));
            mod_version = properties.getProperty("mod_version");
            buildTime = ZonedDateTime.parse(properties.getProperty("build_time"));
            commitId = properties.getProperty("commit_id");
            release = "true".equalsIgnoreCase(properties.getProperty("release"));
        } catch (Exception ignored) {
            mod_version = "Unknown";
            buildTime = null;
            commitId = "uncommited";
            release = false;
        }

        MOD_VERSION = mod_version;
        BUILD_TIME = buildTime;
        COMMIT_ID = commitId;
        IS_RELEASE = release;
    }

    public static void printBuildInfo() {
        LOGGER.info("SinoCore Version: {} (Commit {}), Build at: {}", MOD_VERSION, COMMIT_ID, BUILD_TIME != null ? BUILD_TIME : "B.C. 3200");
    }
}
