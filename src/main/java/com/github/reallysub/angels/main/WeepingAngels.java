package com.github.reallysub.angels.main;

import com.github.reallysub.angels.common.structures.WorldGenCatacombs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import com.github.reallysub.angels.common.WAObjects;
import com.github.reallysub.angels.common.capability.CapabilityAngelSickness;
import com.github.reallysub.angels.common.capability.IAngelSickness;
import com.github.reallysub.angels.common.events.CommonEvents;
import com.github.reallysub.angels.common.network.MessageSicknessUpdate;
import com.github.reallysub.angels.main.config.Config;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid = WeepingAngels.MODID, name = WeepingAngels.NAME, version = WeepingAngels.VERSION, updateJSON = "https://raw.githubusercontent.com/ReallySub/Weeping-Angels-Mod/master/update.json")
@Mod.EventBusSubscriber
public class WeepingAngels {
	public static final String MODID = "weeping-angels";
	public static final String NAME = "Weeping Angels";
	public static final String VERSION = "7.0";
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.init(new Configuration(event.getSuggestedConfigurationFile()));
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		
		WAObjects.setUpSpawns();
		
		CapabilityManager.INSTANCE.register(IAngelSickness.class, new CapabilityAngelSickness.Storage(), IAngelSickness.class);
		
		NETWORK.registerMessage(MessageSicknessUpdate.Handler.class, MessageSicknessUpdate.class, 0, Side.CLIENT);
		
		GameRegistry.registerWorldGenerator(new WorldGenCatacombs(), 8);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		// I'll fix it later
		ModelResourceLocation loc = new ModelResourceLocation(WAObjects.angelPainting.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(WAObjects.angelPainting, 0, loc);
		
		ModelResourceLocation loc2 = new ModelResourceLocation(WAObjects.angelArmItem.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(WAObjects.angelArmItem, 0, loc2);
	}
	
}
