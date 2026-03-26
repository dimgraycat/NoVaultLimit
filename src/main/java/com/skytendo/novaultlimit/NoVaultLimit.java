package com.skytendo.novaultlimit;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoVaultLimit implements ModInitializer {
	public static final String MOD_ID = "novaultlimit";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialized NoVaultLimit although there really isn't anything to initialize.");
	}
}
