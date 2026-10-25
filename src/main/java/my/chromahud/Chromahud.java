package my.chromahud;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chromahud implements ModInitializer {
    public static final String MOD_ID = "chromahud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("ChromaHud initialized!");
    }
}
