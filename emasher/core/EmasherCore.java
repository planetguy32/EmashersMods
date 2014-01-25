package emasher.core;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.*;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.Mod;

import emasher.api.Registry;
import emasher.core.block.BlockLimestone;
import emasher.core.block.BlockMachine;
import emasher.core.block.BlockMetal;
import emasher.core.block.BlockMixedDirt;
import emasher.core.block.BlockMixedSand;
import emasher.core.block.BlockNormalCube;
import emasher.core.block.BlockOre;
import emasher.core.block.BlockPondScum;
import emasher.core.block.BlockRedSandstone;
import emasher.core.hemp.*;
import emasher.core.item.ItemBlockMetal;
import emasher.core.item.ItemBlockNormalCube;
import emasher.core.item.ItemBlockOre;
import emasher.core.item.ItemCircuit;
import emasher.core.item.ItemEmasherGeneric;
import emasher.core.item.ItemGem;
import emasher.core.item.ItemIngot;
import emasher.core.item.ItemPondScum;

import buildcraft.api.recipes.*;

import java.util.*;

/*import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.Smeltery;*/


@Mod(modid="emashercore", name="Emasher Resource", version="1.2.1.8")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class EmasherCore 
{
	// The instance of your mod that Forge uses.
	@Instance("EmasherResource")
	public static EmasherCore instance;
	
	public static WorldGenMine gen = new WorldGenMine();
	
	public static Block mixedDirt;
	public static Block mixedSand;
	public static Block pondScum;
	public static Block machine;
	public static Block normalCube;
	public static Block redSandStone;
	public static Block limestone;
	
	public static Block metal;
	public static Block ore;
	
	public static ItemCircuit circuit;
	public static Item psu;
	public static Item ingot;
	public static Item gem;
	
	int mixedDirtID;
	int mixedSandID;
	int pondScumID;
	int machineID;
	int circuitID;
	int psuID;
	
	int metalID;
	int oreID;
	int gemID;
	int ingotID;
	
	int normalCubeID;
	int redSandStoneID;
	int limestoneID;
	
	int hempBlockID;
	
	int hempPlantID;
	int hempSeedsID;
	int hempOilID;
	
	int hempCapID;
	int hempTunicID;
	int hempPantsID;
	int hempShoesID;
	
	//Hemp stuff
	
	public static Block hemp;
	
	public static Item hempPlant;
	public static Item hempSeeds;
	public static Item hempOil;
	
	public static Item hempCap;
	public static Item hempTunic;
	public static Item hempPants;
	public static Item hempShoes;
	
	static EnumArmorMaterial enumArmorMaterialHemp = EnumHelper.addArmorMaterial("Hemp", 5, new int[]{1, 3, 2, 1}, 15);
	
	public static boolean retroGen;
	
	public static boolean spawnAlgae;
	public static boolean spawnHemp;
	public static int algaeDepth;
	
	public static boolean spawnLimestone;
	public static boolean spawnRedSandstone;
	public static boolean spawnBauxite;
	public static boolean spawnCassiterite;
	public static boolean spawnEmery;
	public static boolean spawnGalena;
	public static boolean spawnNativeCopper;
	public static boolean spawnPentlandite;
	public static boolean spawnRuby;
	public static boolean spawnSapphire;
	
	public static int limestonePerChunk;
	public static int redSandstonePerChunk;
	public static int bauxitePerChunk;
	public static int cassiteritePerChunk;
	public static int emeryPerChunk;
	public static int galenaPerChunk;
	public static int nativeCopperPerChunk;
	public static int pentlanditePerChunk;
	public static int rubyPerChunk;
	public static int sapphirePerChunk;
	
	public static int redSandstonePerVein;
	public static int limestonePerVein;
	public static int bauxitePerVein;
	public static int cassiteritePerVein;
	public static int emeryPerVein;
	public static int galenaPerVein;
	public static int nativeCopperPerVein;
	public static int pentlanditePerVein;
	public static int rubyPerVein;
	public static int sapphirePerVein;
	
	public static int redSandstoneMinHeight;
	public static int limestoneMinHeight;
	public static int bauxiteMinHeight;
	public static int cassiteriteMinHeight;
	public static int emeryMinHeight;
	public static int galenaMinHeight;
	public static int nativeCopperMinHeight;
	public static int pentlanditeMinHeight;
	public static int rubyMinHeight;
	public static int sapphireMinHeight;
	
	public static int redSandstoneMaxHeight;
	public static int limestoneMaxHeight;
	public static int bauxiteMaxHeight;
	public static int cassiteriteMaxHeight;
	public static int emeryMaxHeight;
	public static int galenaMaxHeight;
	public static int nativeCopperMaxHeight;
	public static int pentlanditeMaxHeight;
	public static int rubyMaxHeight;
	public static int sapphireMaxHeight;
	
	public static String limestoneBiomes;
	public static String redSandstoneBiomes;
	public static String bauxiteBiomes;
	public static String cassiteriteBiomes;
	public static String emeryBiomes;
	public static String galenaBiomes;
	public static String nativeCopperBiomes;
	public static String pentlanditeBiomes;
	public static String rubyBiomes;
	public static String sapphireBiomes;
	
	public static ItemStack aluminiumStack;
	public static ItemStack bronzeStack;
	public static ItemStack copperStack;
	public static ItemStack leadStack;
	public static ItemStack nickelStack;
	public static ItemStack platinumStack;
	public static ItemStack silverStack;
	public static ItemStack steelStack;
	public static ItemStack tinStack;
	
	public static ItemStack emeryStack;
	public static ItemStack rubyStack;
	public static ItemStack sapphireStack;
	
	public static WorldGenPondScum scumGenerator = new WorldGenPondScum();
	public static WorldGenHemp generateHemp = new WorldGenHemp();
	
	public static CreativeTabs tabEmasher = new CreativeTabs("tabEmasher")
	{
		public ItemStack getIconItemStack()
		{
			return new ItemStack(gem, 1, 0);
		}
	};
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="emasher.core.client.ClientProxy", serverSide="emasher.core.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		Property test = config.get(Configuration.CATEGORY_GENERAL, "A:", false);
		test.comment = "Red Sandstone, Limestone, Mixed Dirt, and Mixed Sand Must have IDs below 256!";
		
		Property prop = config.get(Configuration.CATEGORY_BLOCK,  "Mixed Dirt ID", 198);
		//prop.comment = "Must be 255 or lower!";
		mixedDirtID = prop.getInt();
		
		prop = config.get(Configuration.CATEGORY_BLOCK,  "Mixed Sand ID", 197);
		//prop.comment = "Must be 255 or lower!";
		mixedSandID = prop.getInt();
		
		normalCubeID = config.get(Configuration.CATEGORY_BLOCK, "Normal Cube ID", 199).getInt();
		redSandStoneID = config.get(Configuration.CATEGORY_BLOCK, "Red Sandstone ID", 196).getInt();
		limestoneID = config.get(Configuration.CATEGORY_BLOCK, "Limestone ID", 195).getInt();
		
		pondScumID = config.get(Configuration.CATEGORY_BLOCK, "Pond Scum ID", 3290).getInt();
		machineID = config.get(Configuration.CATEGORY_BLOCK, "Machine Chassis ID", 3172).getInt();
		circuitID = config.get(Configuration.CATEGORY_ITEM, "Control Circuit ID", 13173).getInt();
		psuID = config.get(Configuration.CATEGORY_ITEM, "PSU ID", 13174).getInt();
		oreID = config.get(Configuration.CATEGORY_BLOCK, "Ore Block ID", 3173).getInt();
		metalID = config.get(Configuration.CATEGORY_BLOCK, "Metal Block ID", 3174).getInt();
		gemID = config.get(Configuration.CATEGORY_ITEM, "Gem ID", 13175).getInt();
		ingotID = config.get(Configuration.CATEGORY_ITEM, "Ingot ID", 3176).getInt();
		
		
		hempBlockID = config.get(Configuration.CATEGORY_BLOCK,  "Hemp Block ID", 2055).getInt();
		hempPlantID = config.get(Configuration.CATEGORY_ITEM, "Hemp Plant ID", 3035).getInt();
		hempSeedsID = config.get(Configuration.CATEGORY_ITEM, "Hemp Seeds ID", 3036).getInt();
		hempOilID = config.get(Configuration.CATEGORY_ITEM, "Hemp Oil ID", 3039).getInt();
		hempCapID = config.get(Configuration.CATEGORY_ITEM, "Hemp Cap ID",3042).getInt();
		hempTunicID = config.get(Configuration.CATEGORY_ITEM, "Hemp Tunic ID",3043).getInt();
		hempPantsID = config.get(Configuration.CATEGORY_ITEM, "Hemp Pants ID",3044).getInt();
		hempShoesID = config.get(Configuration.CATEGORY_ITEM, "Hemp Shoes ID",3045).getInt();
		
		retroGen = config.get(Configuration.CATEGORY_GENERAL, "A: Retro Gen Ores", false).getBoolean(false);
		algaeDepth = config.get(Configuration.CATEGORY_GENERAL, "A: Max Water Depth Alage Can Grow In", 3).getInt();
		
		spawnAlgae = config.get(Configuration.CATEGORY_GENERAL, "C: Generate Algae", true).getBoolean(true);
		spawnHemp = config.get(Configuration.CATEGORY_GENERAL, "C: Generate Hemp", true).getBoolean(true);
		
		spawnLimestone = config.get(Configuration.CATEGORY_GENERAL, "C: Generate Limestone", true).getBoolean(true);
		spawnRedSandstone = config.get(Configuration.CATEGORY_GENERAL, "C: Generate Red Sandstone", true).getBoolean(true);
		spawnBauxite = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Bauxite Ore", true).getBoolean(true);
		spawnCassiterite = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Cassiterite Ore", true).getBoolean(true);
		spawnEmery = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Emery Ore", true).getBoolean(true);
		spawnGalena = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Galena Ore", true).getBoolean(true);
		spawnNativeCopper = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Native Copper Ore", true).getBoolean(true);
		spawnPentlandite = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Pentlandite Ore", true).getBoolean(true);
		spawnRuby = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Ruby Ore", true).getBoolean(true);
		spawnSapphire = config.get(Configuration.CATEGORY_GENERAL, "D: Generate Sapphire Ore", true).getBoolean(true);
		
		limestonePerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Limestone Per Chunk", 20).getInt();
		redSandstonePerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Red SandStone Per Chunk", 20).getInt();
		bauxitePerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Bauxite Ore Per Chunk", 6).getInt();
		cassiteritePerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Cassiterite Ore Per Chunk", 6).getInt();
		emeryPerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Emery Ore Per Chunk", 6).getInt();
		galenaPerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Galena Ore Per Chunk", 6).getInt();
		nativeCopperPerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Native Copper Ore Per Chunk", 12).getInt();
		pentlanditePerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Pentlandite Ore Per Chunk", 4).getInt();
		rubyPerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Ruby Ore Per Chunk", 2).getInt();
		sapphirePerChunk = config.get(Configuration.CATEGORY_GENERAL, "E: Sapphire Ore Per Chunk", 2).getInt();
		
		limestonePerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Limestone Per Vein", 32).getInt();
		redSandstonePerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Red Sandstone Per Vein", 32).getInt();
		bauxitePerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Bauxite Ore Per Vein", 8).getInt();
		cassiteritePerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Cassiterite Ore Per Vein", 16).getInt();
		emeryPerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Emery Ore Per Vein", 16).getInt();
		galenaPerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Galena Ore Per Vein", 8).getInt();
		nativeCopperPerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Native Copper Ore Per Vein", 16).getInt();
		pentlanditePerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Pentlandite Ore Per Vein", 4).getInt();
		rubyPerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Ruby Ore Per Vein", 4).getInt();
		sapphirePerVein = config.get(Configuration.CATEGORY_GENERAL, "F: Sapphire Ore Per Vein", 4).getInt();
		
		limestoneMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Limestone Min Height", 32).getInt();
		redSandstoneMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Red Sandstone Min Height", 32).getInt();
		bauxiteMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Bauxite Ore Min Height", 0).getInt();
		cassiteriteMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Cassiterite Ore Min Height", 16).getInt();
		emeryMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Emery Ore Min Height", 32).getInt();
		galenaMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Galena Ore Min Height", 12).getInt();
		nativeCopperMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Native Copper Ore Min Height", 32).getInt();
		pentlanditeMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Pentlandite Ore Min Height", 0).getInt();
		rubyMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Ruby Ore Min Height", 0).getInt();
		sapphireMinHeight = config.get(Configuration.CATEGORY_GENERAL, "G: Sapphire Ore Min Height", 0).getInt();
		
		limestoneMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Limestone Max Height", 127).getInt();
		redSandstoneMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Red Sandstone Max Height", 127).getInt();
		bauxiteMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Bauxite Ore Max Height", 50).getInt();
		cassiteriteMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Cassiterite Ore Max Height", 32).getInt();
		emeryMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Emery Ore Max Height", 127).getInt();
		galenaMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Galena Ore Max Height", 32).getInt();
		nativeCopperMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Native Copper Ore Max Height", 127).getInt();
		pentlanditeMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Pentlandite Ore Max Height", 24).getInt();
		rubyMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Ruby Ore Max Height", 16).getInt();
		sapphireMaxHeight = config.get(Configuration.CATEGORY_GENERAL, "H: Sapphire Ore Max Height", 16).getInt();
		
		limestoneBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Limestone Biomes", "PLAINS,FOREST,HILLS,MOUNTAIN,WATER").getString();
		redSandstoneBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Red Sandstone Biomes", "DESERT").getString();
		bauxiteBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Bauxite Ore Biomes", "PLAINS,JUNGLE,DESERT").getString();
		cassiteriteBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Cassiterite Ore Biomes", "").getString();
		emeryBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Emery Ore Biomes", "DESERT,HILLS,MOUNTAIN").getString();
		galenaBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Galena Ore Biomes", "").getString();
		nativeCopperBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Native Copper Ore Biomes", "").getString();
		pentlanditeBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Pentlandite Ore Biomes", "HILLS,MOUNTAIN,FOREST").getString();
		rubyBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Ruby Ore Biomes", "JUNGLE").getString();
		sapphireBiomes = config.get(Configuration.CATEGORY_GENERAL, "I: Sapphire Ore Biomes", "DESERT").getString();
		
		config.save();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		mixedDirt = (new BlockMixedDirt(mixedDirtID, Material.ground))
				.setHardness(0.5F).setStepSound(Block.soundSandFootstep).setUnlocalizedName("mixedDirt");
		mixedSand = (new BlockMixedSand(mixedSandID, Material.sand))
				.setHardness(0.5F).setStepSound(Block.soundSandFootstep).setUnlocalizedName("mixedsand");
		machine = new BlockMachine(machineID).setHardness(1.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("machine");
		
		redSandStone = (new BlockRedSandstone(redSandStoneID, Material.rock))
				.setHardness(1.5F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("redSandstone");
		
		GameRegistry.registerBlock(redSandStone, "redSandstone");
		LanguageRegistry.addName(redSandStone, "Red Sandstone");
		
		limestone = (new BlockLimestone(limestoneID))
				.setHardness(1.5F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("limestone");
		
		GameRegistry.registerBlock(limestone, "limestone");
		LanguageRegistry.addName(limestone, "Limestone");
		
		normalCube = (new BlockNormalCube(normalCubeID, 0, Material.rock))
				.setHardness(1.5F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("normalCube");
		
		
		Item.itemsList[normalCube.blockID] = new ItemBlockNormalCube(normalCube.blockID - 256);
		LanguageRegistry.instance().addStringLocalization("tile.normalCube.litchenStone.name", "Lichen Stone");
		LanguageRegistry.instance().addStringLocalization("tile.normalCube.redSandstoneBricks.name", "Red Sandstone Bricks");
		LanguageRegistry.instance().addStringLocalization("tile.normalCube.limestoneBricks.name", "Limestone Bricks");
		LanguageRegistry.instance().addStringLocalization("tile.normalCube.roadWay.name", "Roadway");
		LanguageRegistry.instance().addStringLocalization("tile.normalCube.dirtyCobblestone.name", "Dirty Cobblestone");
		
		metal = (new BlockMetal(metalID, 0, Material.iron))
				.setHardness(2.0F).setStepSound(Block.soundMetalFootstep)
				.setUnlocalizedName("e_metal");
		
		Item.itemsList[metal.blockID] = new ItemBlockMetal(metal.blockID - 256);
		
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockAluminium.name", "Aluminium Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockBronze.name", 	"Bronze Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockCopper.name", 	"Copper Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockLead.name", "Lead Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockNickel.name", "Nickel Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockPlatinum.name", "Platinum Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockSilver.name", "Silver Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockSteel.name", "Steel Block");
		LanguageRegistry.instance().addStringLocalization("tile.e_metal.e_blockTin.name", "Tin Block");
		
		OreDictionary.registerOre("blockAluminum", new ItemStack(metal, 1, 0));
		//OreDictionary.registerOre("blockAluminium", new ItemStack(metal, 1, 0));

		OreDictionary.registerOre("blockBronze", new ItemStack(metal, 1, 1));
		OreDictionary.registerOre("blockCopper", new ItemStack(metal, 1, 2));
		OreDictionary.registerOre("blockLead", new ItemStack(metal, 1, 3));
		OreDictionary.registerOre("blockNickel", new ItemStack(metal, 1, 4));
		OreDictionary.registerOre("blockPlatinum", new ItemStack(metal, 1, 5));
		OreDictionary.registerOre("blockSilver", new ItemStack(metal, 1, 6));
		OreDictionary.registerOre("blockSteel", new ItemStack(metal, 1, 7));
		OreDictionary.registerOre("blockTin", new ItemStack(metal, 1, 8));
		
		MinecraftForge.setBlockHarvestLevel(metal, "pickaxe", 2);
		
		ore = (new BlockOre(oreID, 0, Material.rock))
				.setHardness(1.8F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("e_ore");
		
		Item.itemsList[ore.blockID] = new ItemBlockOre(ore.blockID - 256);
		
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreBauxite.name", "Bauxite Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreCassiterite.name", "Cassiterite Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreEmery.name", "Emery Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreGalena.name", "Galena Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreNativeCopper.name", "Native Copper Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_orePentlandite.name", "Pentlandite Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreRuby.name", "Ruby Ore");
		LanguageRegistry.instance().addStringLocalization("tile.e_ore.e_oreSapphire.name", "Sapphire Ore");
		
		OreDictionary.registerOre("oreAluminum", new ItemStack(ore, 1, 0));
		//OreDictionary.registerOre("oreAluminium", new ItemStack(ore, 1, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(ore, 1, 1));
		OreDictionary.registerOre("oreEmery", new ItemStack(ore, 1, 2));
		OreDictionary.registerOre("oreLead", new ItemStack(ore, 1, 3));
		OreDictionary.registerOre("oreCopper", new ItemStack(ore, 1, 4));
		OreDictionary.registerOre("oreNickel", new ItemStack(ore, 1, 5));
		OreDictionary.registerOre("oreRuby", new ItemStack(ore, 1, 6));
		OreDictionary.registerOre("oreSapphire", new ItemStack(ore, 1, 7));
		
		MinecraftForge.setBlockHarvestLevel(ore, "pickaxe", 2);
		
		ingot = new ItemIngot(ingotID);
		ingot.setUnlocalizedName("e_ingot");
		
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotAluminium.name", "Aluminium Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotBronze.name", 	"Bronze Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotCopper.name", 	"Copper Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotLead.name", "Lead Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotNickel.name", "Nickel Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotPlatinum.name", "Platinum Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotSilver.name", "Silver Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotSteel.name", "Steel Ingot");
		LanguageRegistry.instance().addStringLocalization("item.e_ingot.e_ingotTin.name", "Tin Ingot");
		
		OreDictionary.registerOre("ingotAluminum", new ItemStack(ingot, 1, 0));
		//OreDictionary.registerOre("ingotAluminium", new ItemStack(ingot, 1, 0));
		OreDictionary.registerOre("ingotBronze", new ItemStack(ingot, 1, 1));
		OreDictionary.registerOre("ingotCopper", new ItemStack(ingot, 1, 2));
		OreDictionary.registerOre("ingotLead", new ItemStack(ingot, 1, 3));
		OreDictionary.registerOre("ingotNickel", new ItemStack(ingot, 1, 4));
		OreDictionary.registerOre("ingotPlatinum", new ItemStack(ingot, 1, 5));
		OreDictionary.registerOre("ingotSilver", new ItemStack(ingot, 1, 6));
		OreDictionary.registerOre("ingotSteel", new ItemStack(ingot, 1, 7));
		OreDictionary.registerOre("ingotTin", new ItemStack(ingot, 1, 8));
		
		aluminiumStack = new ItemStack(ingot, 1, 0);
		bronzeStack = new ItemStack(ingot, 1, 1);
		copperStack = new ItemStack(ingot, 1, 2);
		leadStack = new ItemStack(ingot, 1, 3);
		nickelStack = new ItemStack(ingot, 1, 4);
		platinumStack = new ItemStack(ingot, 1, 5);
		silverStack = new ItemStack(ingot, 1, 6);
		steelStack = new ItemStack(ingot, 1, 7);
		tinStack = new ItemStack(ingot, 1, 8);
		
		gem = new ItemGem(gemID);
		gem.setUnlocalizedName("e_gem");
		
		LanguageRegistry.instance().addStringLocalization("item.e_gem.e_gemEmery.name", "Emery");
		LanguageRegistry.instance().addStringLocalization("item.e_gem.e_gemRuby.name", 	"Ruby");
		LanguageRegistry.instance().addStringLocalization("item.e_gem.e_gemSapphire.name", 	"Sapphire");
		
		OreDictionary.registerOre("gemEmery", new ItemStack(gem, 1, 0));
		OreDictionary.registerOre("gemRuby", new ItemStack(gem, 1, 1));
		OreDictionary.registerOre("gemSapphire", new ItemStack(gem, 1, 2));
		
		circuit = new ItemCircuit(circuitID);
		psu = new ItemEmasherGeneric(psuID, "EmasherCore:psu", "psu");
		
		proxy.registerRenderers();
		GameRegistry.registerBlock(mixedDirt, "mixedDirt");
		GameRegistry.registerBlock(mixedSand, "mixedSand");
		GameRegistry.registerBlock(machine, "machine");
		
		LanguageRegistry.addName(mixedDirt, "Mixed Dirt");
		LanguageRegistry.addName(mixedSand, "Mixed Sand");
		LanguageRegistry.addName(machine, "Machine Chassis");
		
		MinecraftForge.setBlockHarvestLevel(mixedDirt, "shovel", 2);
		MinecraftForge.setBlockHarvestLevel(mixedSand, "shovel", 2);
		
		pondScum = new BlockPondScum(pondScumID).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("pondScum");
		Item.itemsList[pondScum.blockID] = new ItemPondScum(pondScum.blockID - 256);
		LanguageRegistry.addName(pondScum, "Algae");
		LanguageRegistry.addName(machine, "Machine Chasis");
		LanguageRegistry.addName(circuit, "Control Circuit");
		LanguageRegistry.addName(psu, "PSU");
		
		GameRegistry.addShapelessRecipe(new ItemStack(EmasherCore.mixedDirt, 2), new Object[]{Block.dirt, Block.gravel});
		GameRegistry.addShapelessRecipe(new ItemStack(EmasherCore.mixedSand, 2), new Object[]{Block.sand, Block.gravel});
		
		//Machine Chasis
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), Item.goldNugget, Character.valueOf('i'), "ingotAluminum"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), Item.goldNugget, Character.valueOf('i'), "ingotTin"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), Item.goldNugget, Character.valueOf('i'), Item.ingotIron));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), "ingotAluminum"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), "ingotTin"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), Item.ingotIron));
		
		//PSU
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'), Item.goldNugget, Character.valueOf('i'), "ingotLead", Character.valueOf('r'), Item.redstone));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'), Item.goldNugget, Character.valueOf('i'), Item.netherQuartz, Character.valueOf('r'), Item.redstone));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'),"ingotCopper", Character.valueOf('i'), "ingotLead", Character.valueOf('r'), Item.redstone));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'),"ingotCopper", Character.valueOf('i'), Item.netherQuartz, Character.valueOf('r'), Item.redstone));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(circuit, 2), "rrr", "ggg", Character.valueOf('r'), Item.redstone, Character.valueOf('g'), Item.goldNugget));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(circuit, 2), "rrr", "ggg", Character.valueOf('r'), Item.redstone, Character.valueOf('g'), "ingotCopper"));
		
		/*if(Loader.isModLoaded("BuildCraft|Silicon"))
		{
			AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {new ItemStack(Item.redstone, 1), new ItemStack(Item.goldNugget, 1)}, 20000, new ItemStack(circuit, 4)));
			AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {new ItemStack(Item.redstone, 1), copperStack}, 20000, new ItemStack(circuit, 4)));

		}*/
		
		//Stone Recipes
		
		GameRegistry.addRecipe(new ItemStack(normalCube, 4, 1), new Object[]
				{
					"##", "##", Character.valueOf('#'), redSandStone
				});
		
		GameRegistry.addRecipe(new ItemStack(normalCube, 4, 2), new Object[]
				{
					"##", "##", Character.valueOf('#'), limestone
				});
		
		GameRegistry.addShapelessRecipe(new ItemStack(normalCube, 2, 3), new Object[]
				{
					new ItemStack(normalCube, 1, 2), EmasherCore.mixedSand
				});
		
		GameRegistry.addShapelessRecipe(new ItemStack(normalCube, 2, 4), new Object[]
				{
					Block.cobblestone, Block.dirt
				});
		
		GameRegistry.addShapelessRecipe(new ItemStack(normalCube, 2, 4), new Object[]
				{
					Block.cobblestone, EmasherCore.mixedDirt
				});
		
		//Metal Recipes
		
		for(int i = 0; i < 9; i++)
		{
			GameRegistry.addRecipe(new ItemStack(metal, 1, i), new Object[] {
				"iii", "iii", "iii", Character.valueOf('i'), new ItemStack(ingot, 1, i)
			});
			
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(ingot, 9, i), new ItemStack(metal, 1, i)));
		}
		
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 0, new ItemStack(ingot, 1, 0), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 1, new ItemStack(ingot, 1, 8), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 2, new ItemStack(gem, 1, 0), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 3, new ItemStack(ingot, 1, 3), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 4, new ItemStack(ingot, 1, 2), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 5, new ItemStack(ingot, 1, 4), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 6, new ItemStack(gem, 1, 1), 1.0F);
		FurnaceRecipes.smelting().addSmelting(ore.blockID, 7, new ItemStack(gem, 1, 2), 1.0F);
		
		//TC Support
		/*if(Loader.isModLoaded("TConstruct"))
		{
			ItemStack al = TConstructRegistry.getItemStack("ingotAluminum");
			FluidStack l = Smeltery.getSmelteryResult(al);
			l.amount = 144 * 2;
			Smeltery.addMelting(new ItemStack(ore, 1, 0), 400, l);
			l = l.copy();
			l.amount = 144;
			Smeltery.addMelting(new ItemStack(ingot, 1, 0), metal.blockID, 0, 400, l);
			l = l.copy();
			l.amount = 144 * 9;
			Smeltery.addMelting(new ItemStack(metal, 1, 0), 400, l);
		}*/
		
			
		if(this.spawnAlgae) GameRegistry.registerWorldGenerator(scumGenerator);
		
		if(retroGen)
		{
			MinecraftForge.EVENT_BUS.register(new CoreWorldGenUpdater());
		}
		else
		{
			GameRegistry.registerWorldGenerator(gen);
		}
		
		registerInRegistry();
		
		
		registerOreGen();
		
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabEmasher", "en_US", "Emasher Resource");
		
		initHemp();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
	
	private void initHemp()
	{
		hemp = new BlockHemp(hempBlockID, 0).setStepSound(Block.soundGrassFootstep)
				.setHardness(0.0F).setResistance(0.0F)
				.setLightValue(0.0F)
				.setUnlocalizedName("Hemp");
		
		hempPlant = new ItemHempPlant(hempPlantID);
		hempSeeds = new ItemHempSeeds(hempSeedsID, hemp);
		hempOil = new ItemHempOil(hempOilID);
		
		hempCap = new ItemHempCap(hempCapID, enumArmorMaterialHemp, 0, 0);
		hempTunic = new ItemHempTunic(hempTunicID, enumArmorMaterialHemp, 0, 1);
		hempPants = new ItemHempPants(hempPantsID, enumArmorMaterialHemp, 0, 2);
		hempShoes = new ItemHempShoes(hempShoesID, enumArmorMaterialHemp, 0, 3);
		
		LanguageRegistry.addName(hemp, "Hemp Block");
		GameRegistry.registerBlock(hemp, "hemp");
		
		if(this.spawnHemp)
		{
			GameRegistry.registerWorldGenerator(generateHemp);
		}
		
		LanguageRegistry.addName(hempPlant, "Hemp");
		LanguageRegistry.addName(hempSeeds, "Hemp Seeds");

		LanguageRegistry.addName(hempOil, "Hemp Seed Oil");
		
		LanguageRegistry.addName(hempCap, "Hemp Cap");
		LanguageRegistry.addName(hempTunic, "Hemp Tunic");
		LanguageRegistry.addName(hempPants, "Hemp Pants");
		LanguageRegistry.addName(hempShoes, "Hemp Shoes");
		
		GameRegistry.addShapelessRecipe(new ItemStack(hempSeeds, 1), new Object[]{hempPlant});
		GameRegistry.addShapelessRecipe(new ItemStack(hempOil, 1), new Object[]{hempSeeds, Item.bowlEmpty});
		
		
		GameRegistry.registerFuelHandler((ItemHempOil)hempOil);
		
		Registry.addItem("hemp", hempPlant);
		
		GameRegistry.addRecipe(new ItemStack(Item.silk, 3), new Object[]
		{
			"#  ", " # ", "  #", Character.valueOf('#'), hempPlant
		});
		GameRegistry.addRecipe(new ItemStack(Item.paper, 3), new Object[]
		{
			"###", Character.valueOf('#'), hempPlant
		});
		
		
		GameRegistry.addRecipe(new ItemStack(hempCap, 1), new Object[]
		{
			"###", "# #", Character.valueOf('#'), hempPlant
		});
		
		GameRegistry.addRecipe(new ItemStack(hempTunic, 1), new Object[]
		{
			"# #", "###", "###", Character.valueOf('#'), hempPlant
		});
		
		GameRegistry.addRecipe(new ItemStack(hempPants, 1), new Object[]
		{
			"###", "# #", "# #", Character.valueOf('#'), hempPlant
		});
		
		GameRegistry.addRecipe(new ItemStack(hempShoes, 1), new Object[]
		{
			"# #", "# #", Character.valueOf('#'), hempPlant
		});
		
		GameRegistry.addSmelting(this.hempPlant.itemID, new ItemStack(Item.dyePowder, 1, 2), 0.1F);
	}
	
	private void registerOreGen()
	{
		WorldGenMinableSafe oreGen;
		WorldGenMinableWrap container;
		
		if(this.spawnLimestone)
		{
			oreGen = new WorldGenMinableSafe(limestone.blockID, limestonePerVein);
			container = new WorldGenMinableWrap(oreGen, limestonePerChunk, limestoneMinHeight, limestoneMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(limestoneBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnRedSandstone)
		{
			oreGen = new WorldGenMinableSafe(redSandStone.blockID, redSandstonePerVein);
			container = new WorldGenMinableWrap(oreGen, redSandstonePerChunk, redSandstoneMinHeight, redSandstoneMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(redSandstoneBiomes)) container.add(bType);
			gen.add(container);
		}
		
		//public WorldGenMinableSafe(int id, int meta, int number, int target)
		
		if(this.spawnBauxite)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 0, bauxitePerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, bauxitePerChunk, bauxiteMinHeight, bauxiteMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(bauxiteBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnCassiterite)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 1, cassiteritePerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, cassiteritePerChunk, cassiteriteMinHeight, cassiteriteMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(cassiteriteBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnEmery)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 2, emeryPerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, emeryPerChunk, emeryMinHeight, emeryMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(emeryBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnGalena)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 3, galenaPerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, galenaPerChunk, galenaMinHeight, galenaMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(galenaBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnNativeCopper)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 4, nativeCopperPerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, nativeCopperPerChunk, nativeCopperMinHeight, nativeCopperMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(nativeCopperBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnPentlandite)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 5, pentlanditePerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, pentlanditePerChunk, pentlanditeMinHeight, pentlanditeMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(pentlanditeBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnRuby)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 6, rubyPerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, rubyPerChunk, rubyMinHeight, rubyMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(rubyBiomes)) container.add(bType);
			gen.add(container);
		}
		
		if(this.spawnSapphire)
		{
			oreGen = new WorldGenMinableSafe(ore.blockID, 7, sapphirePerVein, Block.stone.blockID);
			container = new WorldGenMinableWrap(oreGen, sapphirePerChunk, sapphireMinHeight, sapphireMaxHeight);
			for(BiomeDictionary.Type bType : parseBiomeList(sapphireBiomes)) container.add(bType);
			gen.add(container);
		}
	}
	
	public ArrayList<BiomeDictionary.Type> parseBiomeList(String s)
	{
		ArrayList<BiomeDictionary.Type> result = new ArrayList<BiomeDictionary.Type>();
		String temp;
		String temp2;
		BiomeDictionary.Type temp3;
		temp = s.concat("");
		int loc;
		
		if(temp.length() == 0) return result;
		
		while(temp.indexOf(",") != -1)
		{
			loc = temp.indexOf(",");
			temp2 = temp.substring(0, loc);
			temp3 = BiomeDictionary.Type.valueOf(temp2);
			result.add(temp3);
			temp = temp.substring(loc + 1);
			//System.out.println("test");
		}
		
		//Add the last one
		result.add(BiomeDictionary.Type.valueOf(temp));
		
		return result;
	}
	
	private void registerInRegistry()
	{
		Registry.addBlock("limestone", limestone);
		Registry.addBlock("redSandstone", redSandStone);
		Registry.addBlock("mixedDirt", mixedDirt);
		Registry.addBlock("mixedSand", mixedSand);
		Registry.addBlock("normalCube", normalCube);
		Registry.addBlock("machine", machine);
		Registry.addBlock("ore", ore);
		Registry.addBlock("metal", metal);
		Registry.addBlock("algae", pondScum);
		
		Registry.addItem("ingot", ingot);
		Registry.addItem("circuit", circuit);
		Registry.addItem("PSU", psu);
		Registry.addItem("gem", gem);
	}
	
	
}
