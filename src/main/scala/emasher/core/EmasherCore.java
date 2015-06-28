package emasher.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.Registry;
import emasher.core.block.*;
import emasher.core.hemp.*;
import emasher.core.item.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;

@Mod( modid = "emashercore", name = "ET Resources", version = "1.2.1.0" )
public class EmasherCore {
	// The instance of your mod that Forge uses.
	@Instance( "EmasherResource" )
	public static EmasherCore instance;
	
	public static WorldGenMine gen = new WorldGenMine();
	
	public static Block mixedDirt;
	public static Block mixedSand;
	public static Block algae;
	public static Block deadAlgae;
	public static Block superAlgae;
	public static Block machine;
	public static Block normalCube;
	public static Block redSandStone;
	public static Block limestone;
	public static Block nutrientWater;
	
	public static Block metal;
	public static Block ore;
	
	public static ItemCircuit circuit;
	public static Item psu;
	public static Item ingot;
	public static Item gem;
	public static Item bluestone;

	public static Fluid nutrientWaterFluid;
	
	//Hemp stuff
	
	public static Block hemp;
	
	public static Item hempPlant;
	public static Item hempSeeds;
	public static Item hempOil;
	
	public static Item hempCap;
	public static Item hempTunic;
	public static Item hempPants;
	public static Item hempShoes;
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
	public static CreativeTabs tabEmasher = new CreativeTabs( "tabEmasher" ) {
		@Override
		public Item getTabIconItem() {
			return new ItemCircuit();
		}

		public ItemStack getIconItemStack() {
			return new ItemStack( circuit );
		}
	};
	@SidedProxy( clientSide = "emasher.core.client.ClientProxy", serverSide = "emasher.core.CommonProxy" )
	public static CommonProxy proxy;
	static ItemArmor.ArmorMaterial enumArmorMaterialHemp = EnumHelper.addArmorMaterial( "Hemp", 5, new int[] {1, 3, 2, 1}, 15 );
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event ) {
		Configuration config = new Configuration( event.getSuggestedConfigurationFile() );
		
		config.load();

		retroGen = config.get( Configuration.CATEGORY_GENERAL, "A: Retro Gen Ores", false ).getBoolean( false );
		algaeDepth = config.get( Configuration.CATEGORY_GENERAL, "A: Max Water Depth Alage Can Grow In", 3 ).getInt();
		
		spawnAlgae = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Algae", true ).getBoolean( true );
		spawnHemp = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Hemp", true ).getBoolean( true );
		
		spawnLimestone = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Limestone", true ).getBoolean( true );
		spawnRedSandstone = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Red Sandstone", true ).getBoolean( true );
		spawnBauxite = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Bauxite Ore", true ).getBoolean( true );
		spawnCassiterite = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Cassiterite Ore", true ).getBoolean( true );
		spawnEmery = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Emery Ore", true ).getBoolean( true );
		spawnGalena = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Galena Ore", true ).getBoolean( true );
		spawnNativeCopper = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Native Copper Ore", true ).getBoolean( true );
		spawnPentlandite = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Pentlandite Ore", true ).getBoolean( true );
		spawnRuby = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Ruby Ore", true ).getBoolean( true );
		spawnSapphire = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Sapphire Ore", true ).getBoolean( true );
		
		limestonePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Limestone Per Chunk", 20 ).getInt();
		redSandstonePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Red SandStone Per Chunk", 20 ).getInt();
		bauxitePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Bauxite Ore Per Chunk", 6 ).getInt();
		cassiteritePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Cassiterite Ore Per Chunk", 6 ).getInt();
		emeryPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Emery Ore Per Chunk", 6 ).getInt();
		galenaPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Galena Ore Per Chunk", 6 ).getInt();
		nativeCopperPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Native Copper Ore Per Chunk", 12 ).getInt();
		pentlanditePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Pentlandite Ore Per Chunk", 4 ).getInt();
		rubyPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Ruby Ore Per Chunk", 2 ).getInt();
		sapphirePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Sapphire Ore Per Chunk", 2 ).getInt();
		
		limestonePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Limestone Per Vein", 32 ).getInt();
		redSandstonePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Red Sandstone Per Vein", 32 ).getInt();
		bauxitePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Bauxite Ore Per Vein", 8 ).getInt();
		cassiteritePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Cassiterite Ore Per Vein", 16 ).getInt();
		emeryPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Emery Ore Per Vein", 16 ).getInt();
		galenaPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Galena Ore Per Vein", 8 ).getInt();
		nativeCopperPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Native Copper Ore Per Vein", 16 ).getInt();
		pentlanditePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Pentlandite Ore Per Vein", 4 ).getInt();
		rubyPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Ruby Ore Per Vein", 4 ).getInt();
		sapphirePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Sapphire Ore Per Vein", 4 ).getInt();
		
		limestoneMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Limestone Min Height", 32 ).getInt();
		redSandstoneMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Red Sandstone Min Height", 32 ).getInt();
		bauxiteMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Bauxite Ore Min Height", 0 ).getInt();
		cassiteriteMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Cassiterite Ore Min Height", 16 ).getInt();
		emeryMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Emery Ore Min Height", 32 ).getInt();
		galenaMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Galena Ore Min Height", 12 ).getInt();
		nativeCopperMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Native Copper Ore Min Height", 32 ).getInt();
		pentlanditeMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Pentlandite Ore Min Height", 0 ).getInt();
		rubyMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Ruby Ore Min Height", 0 ).getInt();
		sapphireMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Sapphire Ore Min Height", 0 ).getInt();
		
		limestoneMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Limestone Max Height", 127 ).getInt();
		redSandstoneMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Red Sandstone Max Height", 127 ).getInt();
		bauxiteMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Bauxite Ore Max Height", 50 ).getInt();
		cassiteriteMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Cassiterite Ore Max Height", 32 ).getInt();
		emeryMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Emery Ore Max Height", 127 ).getInt();
		galenaMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Galena Ore Max Height", 32 ).getInt();
		nativeCopperMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Native Copper Ore Max Height", 127 ).getInt();
		pentlanditeMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Pentlandite Ore Max Height", 24 ).getInt();
		rubyMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Ruby Ore Max Height", 16 ).getInt();
		sapphireMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Sapphire Ore Max Height", 16 ).getInt();
		
		limestoneBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Limestone Biomes", "PLAINS,FOREST,HILLS,MOUNTAIN,WATER" ).getString();
		redSandstoneBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Red Sandstone Biomes", "DESERT" ).getString();
		bauxiteBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Bauxite Ore Biomes", "PLAINS,JUNGLE,DESERT" ).getString();
		cassiteriteBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Cassiterite Ore Biomes", "" ).getString();
		emeryBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Emery Ore Biomes", "DESERT,HILLS,MOUNTAIN" ).getString();
		galenaBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Galena Ore Biomes", "" ).getString();
		nativeCopperBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Native Copper Ore Biomes", "" ).getString();
		pentlanditeBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Pentlandite Ore Biomes", "HILLS,MOUNTAIN,FOREST" ).getString();
		rubyBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Ruby Ore Biomes", "JUNGLE" ).getString();
		sapphireBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Sapphire Ore Biomes", "DESERT" ).getString();

		if( config.hasChanged() )
			config.save();
	}
	
	@EventHandler
	public void load( FMLInitializationEvent event ) {
		mixedDirt = ( new BlockMixedDirt( Material.ground )
				.setHardness( 0.5F ).setStepSound( Block.soundTypeSand ).setBlockName( "mixedDirt" ) );
		mixedSand = ( new BlockMixedSand( Material.sand ) )
				.setHardness( 0.5F ).setStepSound( Block.soundTypeSand ).setBlockName( "mixedsand" );
		machine = new BlockMachine().setHardness( 1.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "machine" );
		
		redSandStone = ( new BlockRedSandstone( Material.rock ) )
				.setHardness( 1.5F ).setStepSound( Block.soundTypeStone )
				.setBlockName( "redSandstone" );
		GameRegistry.registerBlock( redSandStone, "redSandstone" );
		
		limestone = ( new BlockLimestone() )
				.setHardness( 1.5F ).setStepSound( Block.soundTypeStone )
				.setBlockName( "limestone" );
		
		GameRegistry.registerBlock( limestone, "limestone" );
		
		normalCube = new BlockNormalCube( 0, Material.rock )
				.setHardness( 1.5F ).setStepSound( Block.soundTypeStone )
				.setBlockName( "normalCube" );

		GameRegistry.registerBlock( normalCube, ItemBlockNormalCube.class, "normalCube" );

		metal = ( new BlockMetal( Material.iron ) )
				.setHardness( 2.0F ).setStepSound( Block.soundTypeMetal )
				.setBlockName( "e_metal" );

		GameRegistry.registerBlock( metal, ItemBlockMetal.class, "metal" );
		
		OreDictionary.registerOre( "blockAluminum", new ItemStack( metal, 1, 0 ) );
		OreDictionary.registerOre( "blockBronze", new ItemStack( metal, 1, 1 ) );
		OreDictionary.registerOre( "blockCopper", new ItemStack( metal, 1, 2 ) );
		OreDictionary.registerOre( "blockLead", new ItemStack( metal, 1, 3 ) );
		OreDictionary.registerOre( "blockNickel", new ItemStack( metal, 1, 4 ) );
		OreDictionary.registerOre( "blockPlatinum", new ItemStack( metal, 1, 5 ) );
		OreDictionary.registerOre( "blockSilver", new ItemStack( metal, 1, 6 ) );
		OreDictionary.registerOre( "blockSteel", new ItemStack( metal, 1, 7 ) );
		OreDictionary.registerOre( "blockTin", new ItemStack( metal, 1, 8 ) );

		metal.setHarvestLevel( "pickaxe", 2, 0 );
		metal.setHarvestLevel( "pickaxe", 2, 1 );
		metal.setHarvestLevel( "pickaxe", 2, 2 );
		metal.setHarvestLevel( "pickaxe", 2, 3 );
		metal.setHarvestLevel( "pickaxe", 2, 4 );
		metal.setHarvestLevel( "pickaxe", 2, 5 );
		metal.setHarvestLevel( "pickaxe", 2, 6 );
		metal.setHarvestLevel( "pickaxe", 3, 7 );
		metal.setHarvestLevel( "pickaxe", 2, 8 );
		
		ore = ( new BlockOre( 0, Material.rock ) )
				.setHardness( 1.8F ).setStepSound( Block.soundTypeStone )
				.setBlockName( "e_ore" );

		GameRegistry.registerBlock( ore, ItemBlockOre.class, "ore" );
		
		OreDictionary.registerOre( "oreAluminum", new ItemStack( ore, 1, 0 ) );
		OreDictionary.registerOre( "oreTin", new ItemStack( ore, 1, 1 ) );
		OreDictionary.registerOre( "oreEmery", new ItemStack( ore, 1, 2 ) );
		OreDictionary.registerOre( "oreLead", new ItemStack( ore, 1, 3 ) );
		OreDictionary.registerOre( "oreCopper", new ItemStack( ore, 1, 4 ) );
		OreDictionary.registerOre( "oreNickel", new ItemStack( ore, 1, 5 ) );
		OreDictionary.registerOre( "oreRuby", new ItemStack( ore, 1, 6 ) );
		OreDictionary.registerOre( "oreSapphire", new ItemStack( ore, 1, 7 ) );

		ore.setHarvestLevel( "pickaxe", 2, 0 );
		ore.setHarvestLevel( "pickaxe", 1, 1 );
		ore.setHarvestLevel( "pickaxe", 1, 2 );
		ore.setHarvestLevel( "pickaxe", 2, 3 );
		ore.setHarvestLevel( "pickaxe", 1, 4 );
		ore.setHarvestLevel( "pickaxe", 2, 5 );
		ore.setHarvestLevel( "pickaxe", 2, 6 );
		ore.setHarvestLevel( "pickaxe", 2, 7 );

		nutrientWaterFluid = new FluidNutrientWater();
		FluidRegistry.registerFluid( nutrientWaterFluid );

		nutrientWater = new BlockNutrientWater( nutrientWaterFluid );
		GameRegistry.registerBlock( nutrientWater, "nutrientWater" );
		
		ingot = new ItemIngot();
		ingot.setUnlocalizedName( "e_ingot" );

		GameRegistry.registerItem( ingot, "ingot", "emashercore" );
		
		OreDictionary.registerOre( "ingotAluminum", new ItemStack( ingot, 1, 0 ) );
		OreDictionary.registerOre( "ingotBronze", new ItemStack( ingot, 1, 1 ) );
		OreDictionary.registerOre( "ingotCopper", new ItemStack( ingot, 1, 2 ) );
		OreDictionary.registerOre( "ingotLead", new ItemStack( ingot, 1, 3 ) );
		OreDictionary.registerOre( "ingotNickel", new ItemStack( ingot, 1, 4 ) );
		OreDictionary.registerOre( "ingotPlatinum", new ItemStack( ingot, 1, 5 ) );
		OreDictionary.registerOre( "ingotSilver", new ItemStack( ingot, 1, 6 ) );
		OreDictionary.registerOre( "ingotSteel", new ItemStack( ingot, 1, 7 ) );
		OreDictionary.registerOre( "ingotTin", new ItemStack( ingot, 1, 8 ) );
		
		aluminiumStack = new ItemStack( ingot, 1, 0 );
		bronzeStack = new ItemStack( ingot, 1, 1 );
		copperStack = new ItemStack( ingot, 1, 2 );
		leadStack = new ItemStack( ingot, 1, 3 );
		nickelStack = new ItemStack( ingot, 1, 4 );
		platinumStack = new ItemStack( ingot, 1, 5 );
		silverStack = new ItemStack( ingot, 1, 6 );
		steelStack = new ItemStack( ingot, 1, 7 );
		tinStack = new ItemStack( ingot, 1, 8 );
		
		gem = new ItemGem();
		gem.setUnlocalizedName( "e_gem" );

		GameRegistry.registerItem( gem, "gem", "emashercore" );
		
		OreDictionary.registerOre( "gemEmery", new ItemStack( gem, 1, 0 ) );
		OreDictionary.registerOre( "gemRuby", new ItemStack( gem, 1, 1 ) );
		OreDictionary.registerOre( "gemSapphire", new ItemStack( gem, 1, 2 ) );
		
		circuit = new ItemCircuit();
		psu = new ItemEmasherGeneric( "EmasherCore:psu", "psu" );

		GameRegistry.registerItem( circuit, "circuit", "emashercore" );
		GameRegistry.registerItem( psu, "psu", "emashercore" );
		
		proxy.registerRenderers();
		GameRegistry.registerBlock( mixedDirt, "mixedDirt" );
		GameRegistry.registerBlock( mixedSand, "mixedSand" );
		GameRegistry.registerBlock( machine, "machine" );
		
		mixedDirt.setHarvestLevel( "shovel", 2 );
		mixedSand.setHarvestLevel( "shovel", 2 );
		
		algae = new BlockPondScum().setHardness( 0.0F ).setStepSound( Block.soundTypeGrass ).setBlockName( "algae" );
		GameRegistry.registerBlock( algae, ItemPondScum.class, "algae", ( Object ) "emashercore:algae" );

		superAlgae = new BlockSuperAlgae().setHardness( 0.0F ).setStepSound( Block.soundTypeGrass ).setBlockName( "superAlgae" );
		GameRegistry.registerBlock( superAlgae, ItemPondScum.class, "superAlgae", ( Object ) "emashercore:superAlgae" );

		deadAlgae = new BlockDeadAlgae().setHardness( 0.0F ).setStepSound( Block.soundTypeGrass ).setBlockName( "deadAlgae" );
		GameRegistry.registerBlock( deadAlgae, ItemPondScum.class, "deadAlgae", ( Object ) "emashercore:deadAlgae" );

		bluestone = new ItemBluestone();
		GameRegistry.registerItem( bluestone, "bluestone", "emashercore" );
		
		GameRegistry.addShapelessRecipe( new ItemStack( EmasherCore.mixedDirt, 2 ), new Object[] {Blocks.dirt, Blocks.gravel} );
		GameRegistry.addShapelessRecipe( new ItemStack( EmasherCore.mixedSand, 2 ), new Object[] {Blocks.sand, Blocks.gravel} );
		
		//Machine Chasis
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( machine, 2 ), "gig", "i i", "gig",
				Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'i' ), "ingotAluminum" ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( machine, 2 ), "gig", "i i", "gig",
				Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'i' ), "ingotTin" ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( machine, 2 ), "gig", "i i", "gig",
				Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'i' ), Items.iron_ingot ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( machine, 2 ), "gig", "i i", "gig",
				Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'i' ), "ingotAluminum" ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( machine, 2 ), "gig", "i i", "gig",
				Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'i' ), "ingotTin" ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( machine, 2 ), "gig", "i i", "gig",
				Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'i' ), Items.iron_ingot ) );
		
		//PSU
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( psu, 2 ), "igi", "rrr", "igi", Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'i' ), "ingotLead", Character.valueOf( 'r' ), Items.redstone ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( psu, 2 ), "igi", "rrr", "igi", Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'i' ), Items.quartz, Character.valueOf( 'r' ), Items.redstone ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( psu, 2 ), "igi", "rrr", "igi", Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'i' ), "ingotLead", Character.valueOf( 'r' ), Items.redstone ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( psu, 2 ), "igi", "rrr", "igi", Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'i' ), Items.quartz, Character.valueOf( 'r' ), Items.redstone ) );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( circuit, 2 ), "rrr", "ggg", Character.valueOf( 'r' ), Items.redstone, Character.valueOf( 'g' ), Items.gold_nugget ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( circuit, 2 ), "rrr", "ggg", Character.valueOf( 'r' ), Items.redstone, Character.valueOf( 'g' ), "ingotCopper" ) );
		
		/*if(Loader.isModLoaded("BuildCraft|Silicon"))
		{
			AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {new ItemStack(Items.redstone, 1), new ItemStack(Items.gold_nugget, 1)}, 20000, new ItemStack(circuit, 4)));
			AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {new ItemStack(Items.redstone, 1), copperStack}, 20000, new ItemStack(circuit, 4)));

		}*/
		
		//Stone Recipes
		
		GameRegistry.addRecipe( new ItemStack( normalCube, 4, 1 ), new Object[]
				{
						"##", "##", Character.valueOf( '#' ), redSandStone
				} );
		
		GameRegistry.addRecipe( new ItemStack( normalCube, 4, 2 ), new Object[]
				{
						"##", "##", Character.valueOf( '#' ), limestone
				} );
		
		GameRegistry.addShapelessRecipe( new ItemStack( normalCube, 2, 3 ), new Object[]
				{
						new ItemStack( normalCube, 1, 2 ), EmasherCore.mixedSand
				} );
		
		GameRegistry.addShapelessRecipe( new ItemStack( normalCube, 2, 4 ), new Object[]
				{
						Blocks.cobblestone, Blocks.dirt
				} );
		
		GameRegistry.addShapelessRecipe( new ItemStack( normalCube, 2, 4 ), new Object[]
				{
						Blocks.cobblestone, EmasherCore.mixedDirt
				} );
		
		//Metal Recipes
		
		for( int i = 0; i < 9; i++ ) {
			GameRegistry.addRecipe( new ItemStack( metal, 1, i ), new Object[] {
					"iii", "iii", "iii", Character.valueOf( 'i' ), new ItemStack( ingot, 1, i )
			} );
			
			CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( ingot, 9, i ), new ItemStack( metal, 1, i ) ) );
		}
		
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 0 ), new ItemStack( ingot, 1, 0 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 1 ), new ItemStack( ingot, 1, 8 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 2 ), new ItemStack( gem, 1, 0 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 3 ), new ItemStack( ingot, 1, 3 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 4 ), new ItemStack( ingot, 1, 2 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 5 ), new ItemStack( ingot, 1, 4 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 6 ), new ItemStack( gem, 1, 1 ), 1.0F );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( ore, 1, 7 ), new ItemStack( gem, 1, 2 ), 1.0F );
		OreDictionary.registerOre( "limestone", limestone );

		//TC Support
		/*if(Loader.isModLoaded("TConstruct"))
		{
			ItemStack al = TConstructRegistry.getItemStack("ingotAluminum");
			FluidStack l = Smeltery.getSmelteryResult(al);
			l.amount = 144 * 2;
			Smeltery.addMelting(new ItemStack(ore, 1, 0), 400, l);
			l = l.copy();
			l.amount = 144;
			Smeltery.addMelting(new ItemStack(ingot, 1, 0), metal, 0, 400, l);
			l = l.copy();
			l.amount = 144 * 9;
			Smeltery.addMelting(new ItemStack(metal, 1, 0), 400, l);
		}*/
		

		if( spawnAlgae ) GameRegistry.registerWorldGenerator( scumGenerator, 1 );
		
		if( retroGen ) {
			MinecraftForge.EVENT_BUS.register( new CoreWorldGenUpdater() );
		} else {
			GameRegistry.registerWorldGenerator( gen, 1 );
		}
		
		registerInRegistry();
		
		registerOreGen();
		
		initHemp();
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event ) {
	}
	
	private void initHemp() {
		hemp = new BlockHemp().setStepSound( Block.soundTypeGrass )
				.setHardness( 0.0F ).setResistance( 0.0F )
				.setBlockName( "Hemp" );
		
		hempPlant = new ItemHempPlant();
		hempSeeds = new ItemHempSeeds( hemp );
		hempOil = new ItemHempOil();
		
		hempCap = new ItemHempCap( enumArmorMaterialHemp, 0, 0 );
		hempTunic = new ItemHempTunic( enumArmorMaterialHemp, 0, 1 );
		hempPants = new ItemHempPants( enumArmorMaterialHemp, 0, 2 );
		hempShoes = new ItemHempShoes( enumArmorMaterialHemp, 0, 3 );
		GameRegistry.registerItem( hempPlant, "hempPlant", "emashercore" );
		GameRegistry.registerItem( hempSeeds, "hempSeeds", "emashercore" );
		GameRegistry.registerItem( hempOil, "hempOil", "emashercore" );
		GameRegistry.registerItem( hempCap, "hempCap", "emashercore" );
		GameRegistry.registerItem( hempTunic, "hempTunic", "emashercore" );
		GameRegistry.registerItem( hempPants, "hempPants", "emashercore" );
		GameRegistry.registerItem( hempShoes, "hempShoes", "emashercore" );

		GameRegistry.registerBlock( hemp, "hemp" );
		
		if( spawnHemp ) {
			GameRegistry.registerWorldGenerator( generateHemp, 1 );
		}
		
		GameRegistry.addShapelessRecipe( new ItemStack( hempSeeds, 1 ), new Object[] {hempPlant} );
		GameRegistry.addShapelessRecipe( new ItemStack( hempOil, 1 ), new Object[] {hempSeeds, Items.bowl} );
		
		
		GameRegistry.registerFuelHandler( ( ItemHempOil ) hempOil );
		
		Registry.addItem( "hemp", hempPlant );
		
		GameRegistry.addRecipe( new ItemStack( Items.string, 3 ), new Object[]
				{
						"#  ", " # ", "  #", Character.valueOf( '#' ), hempPlant
				} );
		GameRegistry.addRecipe( new ItemStack( Items.paper, 3 ), new Object[]
				{
						"###", Character.valueOf( '#' ), hempPlant
				} );
		
		
		GameRegistry.addRecipe( new ItemStack( hempCap, 1 ), new Object[]
				{
						"###", "# #", Character.valueOf( '#' ), hempPlant
				} );
		
		GameRegistry.addRecipe( new ItemStack( hempTunic, 1 ), new Object[]
				{
						"# #", "###", "###", Character.valueOf( '#' ), hempPlant
				} );
		
		GameRegistry.addRecipe( new ItemStack( hempPants, 1 ), new Object[]
				{
						"###", "# #", "# #", Character.valueOf( '#' ), hempPlant
				} );
		
		GameRegistry.addRecipe( new ItemStack( hempShoes, 1 ), new Object[]
				{
						"# #", "# #", Character.valueOf( '#' ), hempPlant
				} );

		FurnaceRecipes.smelting().func_151396_a( hempPlant, new ItemStack( Items.dye, 1, 2 ), 0.1F );
	}
	
	private void registerOreGen() {
		WorldGenMinableSafe oreGen;
		WorldGenMinableWrap container;
		
		if( this.spawnLimestone ) {
			oreGen = new WorldGenMinableSafe( limestone, limestonePerVein );
			container = new WorldGenMinableWrap( oreGen, limestonePerChunk, limestoneMinHeight, limestoneMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( limestoneBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnRedSandstone ) {
			oreGen = new WorldGenMinableSafe( redSandStone, redSandstonePerVein );
			container = new WorldGenMinableWrap( oreGen, redSandstonePerChunk, redSandstoneMinHeight, redSandstoneMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( redSandstoneBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnBauxite ) {
			oreGen = new WorldGenMinableSafe( ore, 0, bauxitePerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, bauxitePerChunk, bauxiteMinHeight, bauxiteMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( bauxiteBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnCassiterite ) {
			oreGen = new WorldGenMinableSafe( ore, 1, cassiteritePerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, cassiteritePerChunk, cassiteriteMinHeight, cassiteriteMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( cassiteriteBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnEmery ) {
			oreGen = new WorldGenMinableSafe( ore, 2, emeryPerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, emeryPerChunk, emeryMinHeight, emeryMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( emeryBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnGalena ) {
			oreGen = new WorldGenMinableSafe( ore, 3, galenaPerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, galenaPerChunk, galenaMinHeight, galenaMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( galenaBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnNativeCopper ) {
			oreGen = new WorldGenMinableSafe( ore, 4, nativeCopperPerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, nativeCopperPerChunk, nativeCopperMinHeight, nativeCopperMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( nativeCopperBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnPentlandite ) {
			oreGen = new WorldGenMinableSafe( ore, 5, pentlanditePerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, pentlanditePerChunk, pentlanditeMinHeight, pentlanditeMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( pentlanditeBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnRuby ) {
			oreGen = new WorldGenMinableSafe( ore, 6, rubyPerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, rubyPerChunk, rubyMinHeight, rubyMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( rubyBiomes ) ) container.add( bType );
			gen.add( container );
		}
		
		if( this.spawnSapphire ) {
			oreGen = new WorldGenMinableSafe( ore, 7, sapphirePerVein, Blocks.stone );
			container = new WorldGenMinableWrap( oreGen, sapphirePerChunk, sapphireMinHeight, sapphireMaxHeight );
			for( BiomeDictionary.Type bType : parseBiomeList( sapphireBiomes ) ) container.add( bType );
			gen.add( container );
		}
	}
	
	public ArrayList<BiomeDictionary.Type> parseBiomeList( String s ) {
		ArrayList<BiomeDictionary.Type> result = new ArrayList<BiomeDictionary.Type>();
		String temp;
		String temp2;
		BiomeDictionary.Type temp3;
		temp = s.concat( "" );
		int loc;
		
		if( temp.length() == 0 ) return result;
		
		while( temp.indexOf( "," ) != -1 ) {
			loc = temp.indexOf( "," );
			temp2 = temp.substring( 0, loc );
			temp3 = BiomeDictionary.Type.valueOf( temp2 );
			result.add( temp3 );
			temp = temp.substring( loc + 1 );
		}
		
		//Add the last one
		result.add( BiomeDictionary.Type.valueOf( temp ) );
		
		return result;
	}
	
	private void registerInRegistry() {
		Registry.addBlock( "limestone", limestone );
		Registry.addBlock( "redSandstone", redSandStone );
		Registry.addBlock( "mixedDirt", mixedDirt );
		Registry.addBlock( "mixedSand", mixedSand );
		Registry.addBlock( "normalCube", normalCube );
		Registry.addBlock( "machine", machine );
		Registry.addBlock( "ore", ore );
		Registry.addBlock( "metal", metal );
		Registry.addBlock( "algae", algae );
		
		Registry.addItem( "ingot", ingot );
		Registry.addItem( "circuit", circuit );
		Registry.addItem( "PSU", psu );
		Registry.addItem( "gem", gem );
	}
	
	
}
