package emasher.sockets;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.*;
import emasher.core.EmasherCore;
import emasher.core.item.ItemEmasherGeneric;
import emasher.sockets.items.*;
import emasher.sockets.modules.*;
import emasher.sockets.pipes.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mod( modid = "eng_toolbox", name = "Engineer's Toolbox", version = "1.2.0.4", dependencies = "required-after:emashercore" )
public class SocketsMod {
	@Instance( "Sockets" )
	public static SocketsMod instance;
	
	@SidedProxy( clientSide = "emasher.sockets.client.ClientProxy", serverSide = "emasher.sockets.CommonProxy" )
	public static CommonProxy proxy;
	
	//Blocks
	public static Block socket;
	public static Block tempRS;
	public static Block paintedPlanks;
	public static Block groundLimestone;
	public static Block blockSlickwater;
	
	public static Block blockStartPipe;
	public static Block blockFluidPipe;
	public static Block blockEnergyPipe;
	
	public static Block miniPortal;
	public static Block directionChanger;
	public static Block frame;
	
	//Fluids
	
	public static Fluid fluidSlickwater;
	
	//Items
	
	public static Item module;
	public static Item remote;
	public static Item blankSide;
	public static Item engWrench;
	public static Item rsWand;
	public static Item handboiler;
	public static ItemPaintCan[] paintCans = new ItemPaintCan[16];
	public static Item dusts;
	public static Item slickBucket;
	public static Item rsIngot;
	public static Item nutBucket;
	public static boolean cbTextures;
	public static boolean smeltSand;
	public static boolean enableMiniPortal;
	public static boolean miniPortalLava;
	public static int RFperMJ;
	public static int RFperEU;
	public static Map<String, IIcon> innerTextures;
	public static String[] colours = new String[]
			{
					"Black",
					"Red",
					"Green",
					"Brown",
					"Blue",
					"Purple",
					"Cyan",
					"Light Gray",
					"Gray",
					"Pink",
					"Lime",
					"Yellow",
					"Light Blue",
					"Magenta",
					"Orange",
					"White"
			};
	public static Object PREF_BLUE = EnumChatFormatting.BLUE;
	public static Object PREF_GREEN = EnumChatFormatting.GREEN;
	public static Object PREF_RED = EnumChatFormatting.RED;
	public static Object PREF_DARK_PURPLE = EnumChatFormatting.DARK_PURPLE;
	public static Object PREF_YELLOW = EnumChatFormatting.YELLOW;
	public static Object PREF_AQUA = EnumChatFormatting.AQUA;
	public static Object PREF_WHITE = EnumChatFormatting.WHITE;
	public static SimpleNetworkWrapper network;
	public static CreativeTabs tabSockets = new CreativeTabs( "tabSockets" ) {
		@Override
		public Item getTabIconItem() {
			return new ItemEngWrench();
		}

		public ItemStack getIconItemStack() {
			return new ItemStack( rsWand );
		}
	};
	public int slickwaterAmount;
	public boolean enableGrinder;
	public boolean enableSolars;
	public boolean enableWaterIntake;
	public boolean enableHydro;
	public boolean enablePiezo;
	public boolean enableMultiSmelter;
	public boolean enableKiln;
	public boolean enableCentrifuge;
	public boolean enableHusher;
	String[] dyes =
			{
					"dyeBlack",
					"dyeRed",
					"dyeGreen",
					"dyeBrown",
					"dyeBlue",
					"dyePurple",
					"dyeCyan",
					"dyeLightGray",
					"dyeGray",
					"dyePink",
					"dyeLime",
					"dyeYellow",
					"dyeLightBlue",
					"dyeMagenta",
					"dyeOrange",
					"dyeWhite"
			};

	@EventHandler
	public void preInit( FMLPreInitializationEvent event ) {
		Configuration config = new Configuration( event.getSuggestedConfigurationFile() );
		
		config.load();

		enableGrinder = config.get( Configuration.CATEGORY_GENERAL, "Enable Grinder Module", true ).getBoolean( true );
		enableKiln = config.get( Configuration.CATEGORY_GENERAL, "Enable Kiln Module", true ).getBoolean( true );
		enableMultiSmelter = config.get( Configuration.CATEGORY_GENERAL, "Enable Multi Smelter Module", true ).getBoolean( true );
		enableCentrifuge = config.get( Configuration.CATEGORY_GENERAL, "Enable Centrifuge Module", true ).getBoolean( true );
		enableSolars = config.get( Configuration.CATEGORY_GENERAL, "Enable Solar Panel Modules", true ).getBoolean( true );
		enableHydro = config.get( Configuration.CATEGORY_GENERAL, "Enable Hydroelectric Turbines", true ).getBoolean( true );
		enablePiezo = config.get( Configuration.CATEGORY_GENERAL, "Enable Piezo Electric Tiles", true ).getBoolean( true );
		enableWaterIntake = config.get( Configuration.CATEGORY_GENERAL, "Enable Water Intake", true ).getBoolean( true );
		enableHusher = config.get( Configuration.CATEGORY_GENERAL, "Enable Husher", true ).getBoolean( true );
		cbTextures = config.get( Configuration.CATEGORY_GENERAL, "Enable Colour Blind Mode", false ).getBoolean( false );
		smeltSand = config.get( Configuration.CATEGORY_GENERAL, "Hand boiler smelts sand", false ).getBoolean( false );
		RFperMJ = config.get( Configuration.CATEGORY_GENERAL, "RF per MJ", 10 ).getInt();
		RFperEU = config.get( Configuration.CATEGORY_GENERAL, "RF per EU", 4 ).getInt();
		enableMiniPortal = config.get( Configuration.CATEGORY_GENERAL, "Enable Fluidic Nether Portal", true ).getBoolean( true );
		miniPortalLava = config.get( Configuration.CATEGORY_GENERAL, "Allow Lava In Fluidic Nether Portal", true ).getBoolean( true );
		slickwaterAmount = config.get( Configuration.CATEGORY_GENERAL, "Amount of slickwater produced per operation (mb)", 1000 ).getInt();

		if( slickwaterAmount > 32000 || slickwaterAmount <= 0 ) {
			System.err.println( "[Engineer's Toolbox] slickwaterAmount is not between (0..32000]" );
			slickwaterAmount = 1000;
		}

		if( config.hasChanged() )
			config.save();
		
		if( cbTextures ) {
			PREF_BLUE = "Blue: ";
			PREF_GREEN = "Green: ";
			PREF_RED = "Red: ";
			PREF_DARK_PURPLE = "Purple: ";
			PREF_YELLOW = "MO: ";
			PREF_AQUA = "EN: ";
			PREF_WHITE = "GEN: ";
		}
		
		proxy.registerRenderers();

		network = NetworkRegistry.INSTANCE.newSimpleChannel( "engineers_toolbox" );
		proxy.registerMessages();
	}
	
	@EventHandler
	public void load( FMLInitializationEvent event ) {
		MinecraftForge.EVENT_BUS.register( new Util() );
		MinecraftForge.EVENT_BUS.register( new BucketEventHandler() );

		innerTextures = new HashMap<>();

		GameRegistry.registerTileEntity( TileStartPipe.class, "emasherstartpipe" );
		GameRegistry.registerTileEntity( TileFluidPipe.class, "emasherfluidpipe" );
		GameRegistry.registerTileEntity( TileEnergyPipe.class, "emasherenergypipe" );
		GameRegistry.registerTileEntity( TileSocket.class, "modular_socket" );
		GameRegistry.registerTileEntity( TileTempRS.class, "TempRS" );
		GameRegistry.registerTileEntity( TilePipeBase.class, "emasherbasepipe" );
		GameRegistry.registerTileEntity( TileMiniPortal.class, "emasherminiportal" );
		GameRegistry.registerTileEntity( TileDirectionChanger.class, "emasherdirectionchanger" );
		GameRegistry.registerTileEntity( TileFrame.class, "emasherframe" );

		ModuleRegistry.registerModule( new ModBlank( 0 ) );
		ModuleRegistry.registerModule( new ModItemInput( 1 ) );
		ModuleRegistry.registerModule( new ModItemOutput( 2 ) );
		ModuleRegistry.registerModule( new ModItemExtractor( 3 ) );
		ModuleRegistry.registerModule( new ModFluidInput( 4 ) );
		ModuleRegistry.registerModule( new ModFluidOutput( 5 ) );
		ModuleRegistry.registerModule( new ModFluidExtractor( 6 ) );
		ModuleRegistry.registerModule( new ModEnergyInput( 7 ) );
		ModuleRegistry.registerModule( new ModEnergyOutput( 8 ) );
		ModuleRegistry.registerModule( new ModMultiInput( 9 ) );
		ModuleRegistry.registerModule( new ModMultiOutput( 10 ) );
		ModuleRegistry.registerModule( new ModItemDetector( 11 ) );
		ModuleRegistry.registerModule( new ModFluidDetector( 12 ) );
		ModuleRegistry.registerModule( new ModItemDistributor( 13 ) );
		ModuleRegistry.registerModule( new ModFluidDistributor( 14 ) );
		ModuleRegistry.registerModule( new ModItemEjector( 15 ) );
		ModuleRegistry.registerModule( new ModRSInput( 16 ) );
		ModuleRegistry.registerModule( new ModRSOutput( 17 ) );
		ModuleRegistry.registerModule( new ModRSAND( 18 ) );
		ModuleRegistry.registerModule( new ModRSOR( 19 ) );
		ModuleRegistry.registerModule( new ModRSNOT( 20 ) );
		ModuleRegistry.registerModule( new ModRSNAND( 21 ) );
		ModuleRegistry.registerModule( new ModRSNOR( 22 ) );
		ModuleRegistry.registerModule( new ModRSXOR( 23 ) );
		ModuleRegistry.registerModule( new ModRSXNOR( 24 ) );
		ModuleRegistry.registerModule( new ModLatchToggle( 32 ) );
		ModuleRegistry.registerModule( new ModLatchSet( 33 ) );
		ModuleRegistry.registerModule( new ModLatchReset( 34 ) );
		ModuleRegistry.registerModule( new ModTimer( 35 ) );
		ModuleRegistry.registerModule( new ModDelayer( 36 ) );
		ModuleRegistry.registerModule( new ModStateCell( 37 ) );
		ModuleRegistry.registerModule( new ModPressurePlate( 38 ) );
		ModuleRegistry.registerModule( new ModSpinningWheel( 39 ) );
		ModuleRegistry.registerModule( new ModHinge( 40 ) );
		ModuleRegistry.registerModule( new ModEnderHinge( 41 ) );
		ModuleRegistry.registerModule( new ModLazySusan( 42 ) );
		ModuleRegistry.registerModule( new ModElevator( 43 ) );
		ModuleRegistry.registerModule( new ModInternalClock( 44 ) );
		ModuleRegistry.registerModule( new ModTrack( 45 ) );
		ModuleRegistry.registerModule( new ModAccelerometer( 46 ) );
		ModuleRegistry.registerModule( new ModMagnet( 47, "sockets:magnet" ) );
		ModuleRegistry.registerModule( new ModMagnetInput( 48 ) );
		ModuleRegistry.registerModule( new ModMagnetOutput( 49 ) );
		ModuleRegistry.registerModule( new ModBurner( 64 ) );
		ModuleRegistry.registerModule( new ModBreaker( 65 ) );
		if( enableWaterIntake ) ModuleRegistry.registerModule( new ModOsPump( 66 ) );
		ModuleRegistry.registerModule( new ModSpring( 67 ) );
		ModuleRegistry.registerModule( new ModDFBlade( 68 ) );
		ModuleRegistry.registerModule( new ModVacuum( 69 ) );
		ModuleRegistry.registerModule( new ModAdvancedBreaker( 70 ) );
		ModuleRegistry.registerModule( new ModFurnace( 71 ) );
		if( enableGrinder ) ModuleRegistry.registerModule( new ModGrinder( 72 ) );
		ModuleRegistry.registerModule( new ModEnergyIndicator( 73 ) );
		ModuleRegistry.registerModule( new ModEnergyExpansion( 74 ) );
		if( enableSolars ) ModuleRegistry.registerModule( new ModSolar( 75 ) );
		ModuleRegistry.registerModule( new ModButton( 76 ) );
		ModuleRegistry.registerModule( new ModBUD( 78 ) );
		ModuleRegistry.registerModule( new ModAdvancedEnergyExpansion( 79 ) );
		ModuleRegistry.registerModule( new ModWaterCooler( 80 ) );
		ModuleRegistry.registerModule( new ModFreezer( 81 ) );
		ModuleRegistry.registerModule( new ModLavaIntake( 82 ) );
		if( enablePiezo ) ModuleRegistry.registerModule( new ModPiezo( 83 ) );
		if( enableHydro ) ModuleRegistry.registerModule( new ModWaterMill( 84 ) );
		ModuleRegistry.registerModule( new ModSelfDestruct( 85 ) );
		ModuleRegistry.registerModule( new ModItemDisplay( 86 ) );
		ModuleRegistry.registerModule( new ModTankDisplay( 87 ) );
		ModuleRegistry.registerModule( new ModCharger( 88 ) );
		ModuleRegistry.registerModule( new ModBlockPlacer( 89 ) );
		ModuleRegistry.registerModule( new ModMachineOutput( 90 ) );
		if( enableKiln ) ModuleRegistry.registerModule( new ModKiln( 91 ) );
		if( enableMultiSmelter ) ModuleRegistry.registerModule( new ModMultiSmelter( 92 ) );
		if( enableCentrifuge ) ModuleRegistry.registerModule( new ModCentrifuge( 93 ) );
		ModuleRegistry.registerModule( new ModMixer( 94 ) );
		ModuleRegistry.registerModule( new ModPressurizer( 95 ) );
		ModuleRegistry.registerModule( new ModRangeSelector( 96 ) );
		if( enableHusher ) ModuleRegistry.registerModule( new ModHusher( 97 ) );
		ModuleRegistry.registerModule( new ModStirlingGenerator( 98 ) );
		ModuleRegistry.registerModule( new ModPump( 99 ) );

		//Register 3rd party modules
		for( IModuleRegistrationManager reg : ModuleRegistry.registers ) {
			reg.registerModules();
		}

		socket = new BlockSocket().setResistance( 8.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "modular_socket" );
		GameRegistry.registerBlock( socket, ItemBlockSocket.class, "modular_socket" );

		tempRS = new BlockTempRS().setBlockUnbreakable();
		GameRegistry.registerBlock( tempRS, "tempRS" );

		blockStartPipe = new BlockStartPipe().setResistance( 8.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "start_pipe" );
		GameRegistry.registerBlock( blockStartPipe, "start_pipe" );
		blockStartPipe.setCreativeTab( tabSockets );

		blockFluidPipe = new BlockFluidPipe().setResistance( 8.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "fluid_pipe" );
		GameRegistry.registerBlock( blockFluidPipe, "fluid_pipe" );
		blockFluidPipe.setCreativeTab( tabSockets );

		blockEnergyPipe = new BlockEnergyPipe().setResistance( 8.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "energy_pipe" );
		GameRegistry.registerBlock( blockEnergyPipe, "energy_pipe" );
		blockEnergyPipe.setCreativeTab( tabSockets );

		rsIngot = new ItemRSIngot();
		GameRegistry.registerItem( rsIngot, "rsIngot", "eng_toolbox" );

		if( enableMiniPortal ) {
			miniPortal = new BlockMiniPortal().setResistance( 8.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeStone )
					.setBlockName( "emasher_mini_portal" );

			GameRegistry.registerBlock( miniPortal, "emasher_mini_portal" );

			GameRegistry.addRecipe( new ItemStack( miniPortal ), "ooo", "oso", "ooo", 'o', Blocks.obsidian, 's', rsIngot );
		}

		directionChanger = new BlockDirectionChanger().setResistance( 8.0F ).setHardness( 2.0F )
				.setStepSound( Block.soundTypeGlass ).setBlockName( "emasher_direction_changer" );
		GameRegistry.registerBlock( directionChanger, "emasher_direction_changer" );

		GameRegistry.addShapelessRecipe( new ItemStack( directionChanger, 4 ), EmasherCore.machine, Items.glowstone_dust,
				new ItemStack( EmasherCore.gem, 1, 0 ) );

		frame = new BlockFrame().setResistance( 8.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "emasher_frame" );
		GameRegistry.registerBlock( frame, "emasher_frame" );


		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( frame, 4 ),
				"s s",
				" s ",
				"s  ",
				's', "ingotSteel" ) );


		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( frame, 4 ),
				"s s",
				" s ",
				"s  ",
				's', "ingotBronze" ) );


		paintedPlanks = ( new BlockPaintedWood( 0, Material.wood ) )
				.setHardness( 2.0F ).setResistance( 5.0F ).setStepSound( Block.soundTypeWood )
				.setBlockName( "paintedPlanks" );

		GameRegistry.registerBlock( paintedPlanks, ItemBlockPaintedWood.class, "paintedPlanks" );
		Blocks.fire.setFireInfo( paintedPlanks, 5, 20 );

		for( int i = 0; i < 16; i++ ) {
			OreDictionary.registerOre( "plankWood", new ItemStack( paintedPlanks, 1, i ) );
		}

		groundLimestone = new BlockGroundLimestone().setHardness( 0.6F ).setStepSound( Block.soundTypeGravel ).setBlockName( "groundLimestone" );
		GameRegistry.registerBlock( groundLimestone, "groundLimestone" );

		handboiler = new ItemHandboiler( "", "" );
		GameRegistry.registerItem( handboiler, "handBoiler" );

		for( int i = 0; i < 16; i++ ) {
			paintCans[i] = new ItemPaintCan( i );
			GameRegistry.registerItem( paintCans[i], "item.paintCan." + colours[i] + ".name", colours[i] + " Spray Paint" );

		}

		remote = new ItemSocketRemote();
		GameRegistry.registerItem( remote, "Socket Remote", "eng_toolbox" );

		rsWand = new ItemRSWand();
		GameRegistry.registerItem( rsWand, "Redstone Wand", "eng_toolbox" );

		fluidSlickwater = new FluidSlickwater();
		FluidRegistry.registerFluid( fluidSlickwater );

		blockSlickwater = new BlockSlickwater( fluidSlickwater );
		GameRegistry.registerBlock( blockSlickwater, "slickwater" );

		slickBucket = new ItemSlickBucket();
		slickBucket.setMaxStackSize( 1 );
		slickBucket.setCreativeTab( tabSockets );
		GameRegistry.registerItem( slickBucket, "Slickwater Bucket", "eng_toolbox" );

		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidSlickwater, FluidContainerRegistry.BUCKET_VOLUME ), new ItemStack( slickBucket ), new ItemStack( Items.bucket ) );

		nutBucket = new ItemNutrientBucket();
		nutBucket.setMaxStackSize( 1 );
		nutBucket.setCreativeTab( tabSockets );
		GameRegistry.registerItem( nutBucket, "Nutrient Water Bucket", "eng_toolbox" );

		FluidContainerRegistry.registerFluidContainer( new FluidStack( EmasherCore.nutrientWaterFluid, FluidContainerRegistry.BUCKET_VOLUME ), new ItemStack( nutBucket ), new ItemStack( Items.bucket ) );

		blankSide = new ItemEmasherGeneric( "sockets:blankmod", "blankSide" );
		blankSide.setCreativeTab( tabSockets );
		GameRegistry.registerItem( blankSide, "Blank Module", "eng_toolbox" );

		module = new ItemModule();

		GameRegistry.registerItem( module, "Module", "eng_toolbox" );

		engWrench = new ItemEngWrench();
		GameRegistry.registerItem( engWrench, "Engineer's Wrench", "eng_toolbox" );

		dusts = new ItemDusts();
		for( int i = 0; i < ItemDusts.NUM_ITEMS; i++ ) {
			OreDictionary.registerOre( ItemDusts.ORE_NAMES[i], new ItemStack( dusts, 1, i ) );
		}
		GameRegistry.registerItem( dusts, "Dust", "eng_toolbox" );

		registerOreRecipes();

		GameRegistry.addRecipe( new ItemStack( socket ), " b ", "pmc", " h ", 'm', EmasherCore.machine, 'h', Blocks.chest,
				'b', Items.bucket, 'p', EmasherCore.psu, 'i', Items.iron_ingot, 'c', EmasherCore.circuit );

		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( blankSide, 12 ),
				"ggg", "reb", "sms", 'm', EmasherCore.machine, 'g', Blocks.glass_pane, 's', Items.glowstone_dust,
				'r', "dyeRed", 'e', "dyeGreen", 'b', "dyeBlue" ) );

		GameRegistry.addRecipe( new ItemStack( remote ), "e", "c", "s", 'e', Items.ender_pearl, 'c', EmasherCore.circuit,
				's', blankSide );

		GameRegistry.addRecipe( new ItemStack( engWrench ), " i ", "ii ", "  b", 'i', Items.iron_ingot, 'b', Blocks.stone_button );

		GameRegistry.addRecipe( new ItemStack( rsWand ), "rt ", "tc ", "  w", 'r', Blocks.redstone_block, 't', Blocks.redstone_torch,
				'c', EmasherCore.circuit, 'w', engWrench );

		if( !Loader.isModLoaded( "gascraft" ) ) for( int i = 0; i < 16; i++ ) {
			CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( paintCans[i], 1 ),
							"i", "p", "d", 'i', Blocks.stone_button, 'p', Items.glass_bottle, 'd', dyes[i] )
			);
		}

		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( blockFluidPipe, 16 ),
						"sss", "ccc", "sss", 's', Blocks.stone, 'c', "ingotCopper" )
		);

		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( blockEnergyPipe, 16 ),
						"sgs", "rcr", "sgs", 's', Blocks.stone, 'g', Blocks.glass_pane, 'c', "ingotCopper",
						'r', new ItemStack( rsIngot ) )
		);

		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( blockEnergyPipe, 16 ),
						"sgs", "rcr", "sgs", 's', Blocks.stone, 'g', Blocks.glass_pane, 'c', Items.gold_nugget,
						'r', new ItemStack( rsIngot ) )
		);

		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( blockStartPipe, 1 ),
						"sss", " l ", "sss", 's', Blocks.stone_slab, 'l', Blocks.lever )
		);

		GameRegistry.addRecipe( new ItemStack( handboiler, 1 ), "bbb", "ici", " n ", 'b', Items.blaze_rod, 'c', Items.fire_charge,
				'i', Items.iron_ingot, 'n', EmasherCore.circuit );

		for( int i = 0; i < ModuleRegistry.numModules; i++ ) {
			if( ModuleRegistry.getModule( i ) != null ) {
				ModuleRegistry.getModule( i ).addRecipe();
			}
		}

		registerInRegistry();
	}
	
	private void registerOreRecipes() {
		//Grinder
		
		GrinderRecipeRegistry.registerRecipe( new ItemStack( EmasherCore.limestone ), new ItemStack( groundLimestone ) );
		
		GrinderRecipeRegistry.registerRecipe( "oreGold", new ItemStack( dusts, 1, ItemDusts.Const.groundGold.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreIron", new ItemStack( dusts, 1, ItemDusts.Const.groundIron.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreAluminum", new ItemStack( dusts, 1, ItemDusts.Const.groundBauxite.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreAluminium", new ItemStack( dusts, 1, ItemDusts.Const.groundBauxite.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreTin", new ItemStack( dusts, 1, ItemDusts.Const.groundCassiterite.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreCopper", new ItemStack( dusts, 1, ItemDusts.Const.groundNativeCopper.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreNickel", new ItemStack( dusts, 1, ItemDusts.Const.groundPentlandite.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreLead", new ItemStack( dusts, 1, ItemDusts.Const.groundGalena.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreSilver", new ItemStack( dusts, 1, ItemDusts.Const.groundSilver.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreCobalt", new ItemStack( dusts, 1, ItemDusts.Const.groundCobalt.ordinal() ) );
		GrinderRecipeRegistry.registerRecipe( "oreArdite", new ItemStack( dusts, 1, ItemDusts.Const.groundArdite.ordinal() ) );
		
		GrinderRecipeRegistry.registerRecipe( "oreLapis", new ItemStack( Items.dye, 16, 4 ) );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Blocks.coal_ore ), new ItemStack( Items.coal, 2 ) );
		GrinderRecipeRegistry.registerRecipe( "oreDiamond", new ItemStack( Items.diamond, 2 ) );
		GrinderRecipeRegistry.registerRecipe( "oreEmerald", new ItemStack( Items.emerald, 2 ) );
		GrinderRecipeRegistry.registerRecipe( "oreRedstone", new ItemStack( Items.redstone, 8 ) );
		GrinderRecipeRegistry.registerRecipe( "oreQuartz", new ItemStack( Items.quartz, 2 ) );
		GrinderRecipeRegistry.registerRecipe( "oreEmery", new ItemStack( EmasherCore.gem, 4, 0 ) );
		GrinderRecipeRegistry.registerRecipe( "oreRuby", new ItemStack( EmasherCore.gem, 2, 1 ) );
		GrinderRecipeRegistry.registerRecipe( "oreSapphire", new ItemStack( EmasherCore.gem, 2, 2 ) );

		GrinderRecipeRegistry.registerRecipe( new ItemStack( Blocks.cobblestone ), new ItemStack( Blocks.sand ) );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Blocks.stone ), new ItemStack( Blocks.gravel ) );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Items.blaze_rod ), new ItemStack( Items.blaze_powder, 5 ) );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Items.bone ), new ItemStack( Items.dye, 5, 3 ) );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Blocks.obsidian ), "dustObsidian" );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Blocks.netherrack ), "dustNetherrack" );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Items.coal ), "dustCoal" );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Items.coal, 1 ), "dustCharcoal" );
		GrinderRecipeRegistry.registerRecipe( new ItemStack( Items.clay_ball ), "dustClay" );

		//Multi Smelter
		
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundGold", new ItemStack( dusts, 3, ItemDusts.Const.impureGoldDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundIron", new ItemStack( dusts, 3, ItemDusts.Const.impureIronDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundAluminum", new ItemStack( dusts, 3, ItemDusts.Const.impureAluminiumDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundTin", new ItemStack( dusts, 3, ItemDusts.Const.impureTinDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundCopper", new ItemStack( dusts, 3, ItemDusts.Const.impureCopperDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundNickel", new ItemStack( dusts, 3, ItemDusts.Const.impureNickelDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundLead", new ItemStack( dusts, 3, ItemDusts.Const.impureLeadDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundSilver", new ItemStack( dusts, 3, ItemDusts.Const.impureSilverDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundCobalt", new ItemStack( dusts, 3, ItemDusts.Const.impureCobaltDust.ordinal() ) );
		MultiSmelterRecipeRegistry.registerRecipe( "dustQuicklime", "groundArdite", new ItemStack( dusts, 3, ItemDusts.Const.impureArditeDust.ordinal() ) );
		
		MultiSmelterRecipeRegistry.registerRecipe( "ingotCopper", "ingotTin", new ItemStack( EmasherCore.ingot, 1, 1 ) );
		MultiSmelterRecipeRegistry.registerRecipe( new ItemStack( Items.redstone ), new ItemStack( Items.sugar ), new ItemStack( rsIngot ) );
		MultiSmelterRecipeRegistry.registerRecipe( "ingotCopper", new ItemStack( Items.gunpowder ), new ItemStack( EmasherCore.bluestone, 2 ) );
		
		
		//Centrifuge
		
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureGold", new ItemStack( dusts, 1, ItemDusts.Const.pureGoldDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureSilverDust.ordinal() ), 5 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureIron", new ItemStack( dusts, 1, ItemDusts.Const.pureIronDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureIronDust.ordinal() ), 33 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureAluminum", new ItemStack( dusts, 1, ItemDusts.Const.pureAluminiumDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureIronDust.ordinal() ), 5 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureTin", new ItemStack( dusts, 1, ItemDusts.Const.pureTinDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureTinDust.ordinal() ), 33 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureCopper", new ItemStack( dusts, 1, ItemDusts.Const.pureCopperDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureCopperDust.ordinal() ), 33 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureNickel", new ItemStack( dusts, 1, ItemDusts.Const.pureNickelDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.purePlatinumDust.ordinal() ), 20 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureLead", new ItemStack( dusts, 1, ItemDusts.Const.pureLeadDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureSilverDust.ordinal() ), 50 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureSilver", new ItemStack( dusts, 1, ItemDusts.Const.pureSilverDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureLeadDust.ordinal() ), 10 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureCobalt", new ItemStack( dusts, 1, ItemDusts.Const.pureCobaltDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureArditeDust.ordinal() ), 10 );
		CentrifugeRecipeRegistry.registerRecipe( "dustImpureArdite", new ItemStack( dusts, 1, ItemDusts.Const.pureArditeDust.ordinal() ), new ItemStack( dusts, 1, ItemDusts.Const.pureCobaltDust.ordinal() ), 10 );
		
		//Furnace
		ItemStack cobalt = null;
		ItemStack ardite = null;
		ArrayList<ItemStack> list = OreDictionary.getOres( "ingotCobalt" );
		if( list.size() > 0 ) cobalt = list.get( 0 );
		list = OreDictionary.getOres( "ingotArdite" );
		if( list.size() > 0 ) ardite = list.get( 0 );

		if( cobalt != null ) {
			FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundCobalt.ordinal() ), new ItemStack( cobalt.getItem(), 2, cobalt.getItemDamage() ), 1 );
			FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureCobaltDust.ordinal() ), new ItemStack( cobalt.getItem(), 1, cobalt.getItemDamage() ), 1 );
			FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureCobaltDust.ordinal() ), new ItemStack( cobalt.getItem(), 1, cobalt.getItemDamage() ), 1 );
		}

		if( ardite != null ) {
			FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundArdite.ordinal() ), new ItemStack( ardite.getItem(), 2, ardite.getItemDamage() ), 1 );
			FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureArditeDust.ordinal() ), new ItemStack( ardite.getItem(), 1, ardite.getItemDamage() ), 1 );
			FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureArditeDust.ordinal() ), new ItemStack( ardite.getItem(), 1, ardite.getItemDamage() ), 1 );
		}

		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundGold.ordinal() ), new ItemStack( Items.gold_ingot, 2 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundIron.ordinal() ), new ItemStack( Items.iron_ingot, 2 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundBauxite.ordinal() ), new ItemStack( EmasherCore.ingot, 2, 0 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundCassiterite.ordinal() ), new ItemStack( EmasherCore.ingot, 2, 8 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundNativeCopper.ordinal() ), new ItemStack( EmasherCore.ingot, 2, 2 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundPentlandite.ordinal() ), new ItemStack( EmasherCore.ingot, 2, 4 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundGalena.ordinal() ), new ItemStack( EmasherCore.ingot, 2, 3 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.groundSilver.ordinal() ), new ItemStack( EmasherCore.ingot, 2, 6 ), 1 );

		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureGoldDust.ordinal() ), new ItemStack( Items.gold_ingot ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureIronDust.ordinal() ), new ItemStack( Items.iron_ingot ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureAluminiumDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 0 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureTinDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 8 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureCopperDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 2 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureNickelDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 4 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureLeadDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 3 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.impureSilverDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 6 ), 1 );

		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureGoldDust.ordinal() ), new ItemStack( Items.gold_ingot ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureIronDust.ordinal() ), new ItemStack( Items.iron_ingot ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureAluminiumDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 0 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureTinDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 8 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureCopperDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 2 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureNickelDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 4 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureLeadDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 3 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.pureSilverDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 6 ), 1 );
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( dusts, 1, ItemDusts.Const.purePlatinumDust.ordinal() ), new ItemStack( EmasherCore.ingot, 1, 5 ), 1 );
		
		//Mixer
		
		MixerRecipeRegistry.registerRecipe( new ItemStack( EmasherCore.mixedDirt ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( fluidSlickwater, slickwaterAmount ) );
		MixerRecipeRegistry.registerRecipe( new ItemStack( EmasherCore.mixedSand ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( fluidSlickwater, slickwaterAmount ) );
		MixerRecipeRegistry.registerRecipe( new ItemStack( Blocks.sand ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( fluidSlickwater, slickwaterAmount ) );
		MixerRecipeRegistry.registerRecipe( new ItemStack( groundLimestone ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( fluidSlickwater, slickwaterAmount ) );
		MixerRecipeRegistry.registerRecipe( new ItemStack( Items.dye, 1, 15 ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( EmasherCore.nutrientWaterFluid, 1000 ) );
		
	}
	
	private void registerInRegistry() {
		Registry.addBlock( "socket", socket );
		Registry.addBlock( "colouredPlanks", paintedPlanks );
		Registry.addBlock( "groundLimestone", groundLimestone );
		Registry.addBlock( "Slickwater", blockSlickwater );
		
		Registry.addItem( "module", module );
		Registry.addItem( "blankModule", blankSide );
		Registry.addItem( "socketRemote", remote );
		Registry.addItem( "wrench", engWrench );
		Registry.addItem( "redstoneWand", rsWand );
		Registry.addItem( "handBoiler", handboiler );
		for( int i = 0; i < 16; i++ ) {
			Registry.addItem( "paint" + colours[i], paintCans[i] );
		}
		Registry.addItem( "dust", dusts );
		Registry.addItem( "slickwaterBucket", slickBucket );
	}
}
