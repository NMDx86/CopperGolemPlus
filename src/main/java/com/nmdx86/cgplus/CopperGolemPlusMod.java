package com.nmdx86.cgplus;

import com.nmdx86.cgplus.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopperGolemPlusMod implements ModInitializer {
	public static final String MOD_ID = "reinfgolem";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ModConfig config;

	@Override
	public void onInitialize() {
		// Register config
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		LOGGER.info("Reinforced Copper Golem integration loaded!");
	}

	public static ModConfig getConfig() {
		return config;
	}
}