package emasher.gas;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.*;
import emasher.core.EmasherCore;
import emasher.core.item.ItemEmasherGeneric;
import emasher.gas.block.*;
import emasher.gas.fluids.FluidGas;
import emasher.gas.item.*;
import emasher.gas.tileentity.TileDuct;
import emasher.gas.tileentity.TileGas;
import emasher.gas.tileentity.TileShaleResource;
import emasher.gas.worldgen.WorldGenGas;
import emasher.gas.worldgen.WorldGenGasVent;
import emasher.gas.worldgen.WorldGenerationUpdater;
import emasher.sockets.SocketsMod;
import emasher.sockets.items.ItemDusts;
import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;


@Mod( modid = "gascraft", name = "GasCraft", version = "2.1.0.0", dependencies = "required-after:eng_toolbox" )
public class EmasherGas {
	@Instance( "gascraft" )
	public static EmasherGas instance;
	
	@SidedProxy( clientSide = "emasher.gas.client.ClientProxy", serverSide = "emasher.gas.CommonProxy" )
	public static CommonProxy proxy;
	
	//Blocks
	
	public static BlockGasGeneric naturalGas;
	public static BlockGasGeneric propellent;
	public static BlockGasGeneric hydrogen;
	public static BlockGasGeneric smoke;
	public static BlockGasGeneric toxicGas;
	public static BlockGasGeneric neurotoxin;
	public static BlockGasGeneric corrosiveGas;
	public static BlockGasGeneric plasma;
	
	public static Block shaleResource;
	public static Block chimney;
	
	public static Block gasPocket;
	public static Block plasmaPocket;
	
	//Items
	
	public static Item vial;
	public static Item vialFilled;
	public static Item gasMask;
	public static Item smokeGrenade;
	public static Item ash;
	
	//Fluids
	
	public static Fluid fluidNaturalGas;
	public static Fluid fluidPropellent;
	public static Fluid fluidHydrogen;
	public static Fluid fluidSmoke;
	public static Fluid fluidToxicGas;
	public static Fluid fluidNeurotoxin;
	public static Fluid fluidCorrosiveGas;
	public static Fluid fluidPlasma;
	
	public static WorldGenGas gasGenerator = new WorldGenGas();
	public static WorldGenGasVent gasVentGenerator = new WorldGenGasVent();
	public static boolean smeltSand;
	public static boolean spawnMineGas;
	public static boolean flatBedrock;
	public static boolean flatNetherBedrock;
	public static int flatBedrockTop;
	public static int flatNetherBedrockTop;
	
	public static int maxGasInVent;
	public static int minGasInVent;
	public static boolean infiniteGasInVent;

	public static boolean gasBlocksInWorld;
	public static boolean gasBlocksCanExplode;

	public static CreativeTabs tabGasCraft = new CreativeTabs( "tabGasCraft" ) {
		@Override
		public Item getTabIconItem() {
			return new ItemStack( gasMask ).getItem();
		}

		public ItemStack getIconItemStack() {
			return new ItemStack( gasMask );
		}
	};
	
	public static String[] dyes =
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
	
	static ItemArmor.ArmorMaterial enumArmorMaterialGas = EnumHelper.addArmorMaterial( "Gas", 5, new int[] {1, 3, 2, 1}, 15 );
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event ) {
		Configuration config = new Configuration( event.getSuggestedConfigurationFile() );
		
		config.load();
		
		maxGasInVent = config.get( Configuration.CATEGORY_GENERAL, "Max Fluid In Shale Resources (In Buckets)", 50000 ).getInt();
		minGasInVent = config.get( Configuration.CATEGORY_GENERAL, "Min Fluid In Shale Resources (In Buckets)", 5000 ).getInt();
		infiniteGasInVent = config.get( Configuration.CATEGORY_GENERAL, "Infinite Gas In Vents", false ).getBoolean( false );
		spawnMineGas = config.get( Configuration.CATEGORY_GENERAL, "Spawn Gas Pockets in the world", false ).getBoolean( false );
		flatBedrock = config.get( Configuration.CATEGORY_GENERAL, "Flat Bedrock Compatibility Mode", false ).getBoolean( false );
		flatNetherBedrock = config.get( Configuration.CATEGORY_GENERAL, "Nether Flat Bedrock Compatibility Mode", false ).getBoolean( false );
		flatBedrockTop = config.get( Configuration.CATEGORY_GENERAL, "Flat Bedrock Top Layer", 0 ).getInt();
		flatNetherBedrockTop = config.get( Configuration.CATEGORY_GENERAL, "Flat Nether Bedrock Top Layer", 0 ).getInt();
		gasBlocksInWorld = config.get( Configuration.CATEGORY_GENERAL, "Allow gas blocks", true ).getBoolean( true );
		gasBlocksCanExplode = config.get( Configuration.CATEGORY_GENERAL, "Allow gas blocks to explode", true ).getBoolean( true );

		if( config.hasChanged() )
			config.save();
		
		ModuleRegistry.addModuleRegistrationManager( new GasModuleRegistrationManager() );

		registerItems();
		registerBlocks();
		registerFluids();

		registerTileEntities();
		registerEvents();
		registerWorldGen();

		proxy.registerRenderers();

		registerInRegistry();
	}
	
	@EventHandler
	public void load( FMLInitializationEvent event ) {
		registerRecipes();
	}
	
	private void registerTileEntities() {
		GameRegistry.registerTileEntity( TileShaleResource.class, "shaleResource" );
		GameRegistry.registerTileEntity( TileGas.class, "gas" );
		GameRegistry.registerTileEntity( TileDuct.class, "chimney" );
		
		EntityRegistry.registerModEntity( EntitySmokeBomb.class, "smokeGrenade", 1, this, 80, 3, true );
	}

	
	private void registerBlocks() {
		gasPocket = ( new BlockMineGas() ).setHardness( 1.5F ).setResistance( 10.0F ).setStepSound( Block.soundTypeStone ).setBlockName( "gasPocket" );
		plasmaPocket = new BlockNetherGas().setHardness( 0.4F ).setResistance( 10.0F ).setStepSound( Block.soundTypeStone ).setBlockName( "plasmaPocket" );
		
		naturalGas = ( BlockGasGeneric ) new BlockNaturalGas().setBlockUnbreakable().setBlockName( "naturalGas" ).setResistance( 0.0F );
		propellent = ( BlockGasGeneric ) new BlockPropellent().setBlockUnbreakable().setBlockName( "propellent" ).setResistance( 0.0F );
		hydrogen = ( BlockGasGeneric ) new BlockHydrogen().setBlockUnbreakable().setBlockName( "hydrogen" ).setResistance( 0.0F );
		smoke = ( BlockGasGeneric ) new BlockSmoke().setBlockUnbreakable().setBlockName( "smoke" ).setResistance( 0.0F );
		toxicGas = ( BlockGasGeneric ) new BlockWeaponizedGas().setBlockUnbreakable().setBlockName( "toxicGas" ).setResistance( 0.0F );
		neurotoxin = ( BlockGasGeneric ) new BlockNeurotoxin().setBlockUnbreakable().setBlockName( "neurotoxin" ).setResistance( 0.0F );
		corrosiveGas = ( BlockGasGeneric ) new BlockCorrosiveGas().setBlockUnbreakable().setBlockName( "corrosiveGas" ).setResistance( 0.0F );
		plasma = ( BlockGasGeneric ) new BlockPlasma().setBlockUnbreakable().setBlockName( "plasma" ).setResistance( 0.0F );
		
		shaleResource = new BlockShaleResource();

		GameRegistry.registerBlock( shaleResource, ItemBlockShaleResource.class, "shaleResource" );
		
		chimney = new BlockDuct().setResistance( 5.0F ).setHardness( 2.0F ).setStepSound( Block.soundTypeMetal ).setBlockName( "chimney" );
		GameRegistry.registerBlock( chimney, "chimney" );

		GameRegistry.registerBlock( naturalGas, "naturalGas" );
		GameRegistry.registerBlock( propellent, "propellent" );
		GameRegistry.registerBlock( hydrogen, "hydrogen" );
		GameRegistry.registerBlock( smoke, "smoke" );
		GameRegistry.registerBlock( toxicGas, "weaponizedGas" );
		GameRegistry.registerBlock( neurotoxin, "neurotoxin" );
		GameRegistry.registerBlock( corrosiveGas, "corrosiveGas" );
		GameRegistry.registerBlock( gasPocket, "gasPocket" );
		GameRegistry.registerBlock( plasma, "plasma" );
	}
	
	private void registerFluids() {
		fluidNaturalGas = new FluidGas( "gasCraft_naturalGas", naturalGas, 0 );
		fluidPropellent = new FluidGas( "gasCraft_propellent", propellent, 1 );
		fluidHydrogen = new FluidGas( "gasCraft_hydrogen", hydrogen, 2 );
		fluidSmoke = new FluidGas( "gasCraft_smoke", smoke, 3 );
		fluidToxicGas = new FluidGas( "gasCraft_toxicGas", toxicGas, 4 );
		fluidNeurotoxin = new FluidGas( "gasCraft_neurotoxin", neurotoxin, 5 );
		fluidCorrosiveGas = new FluidGas( "gasCraft_corrosiveGas", corrosiveGas, 6 );
		fluidPlasma = new FluidGas( "gasCraft_plasma", plasma, 7 ).setLuminosity( 15 ).setTemperature( 2600 );
		
		FluidRegistry.registerFluid( fluidNaturalGas );
		FluidRegistry.registerFluid( fluidPropellent );
		FluidRegistry.registerFluid( fluidHydrogen );
		FluidRegistry.registerFluid( fluidSmoke );
		FluidRegistry.registerFluid( fluidToxicGas );
		FluidRegistry.registerFluid( fluidNeurotoxin );
		FluidRegistry.registerFluid( fluidCorrosiveGas );
		FluidRegistry.registerFluid( fluidPlasma );

		naturalGas.blocksFluid = fluidNaturalGas;
		propellent.blocksFluid = fluidPropellent;
		hydrogen.blocksFluid = fluidHydrogen;
		smoke.blocksFluid = fluidSmoke;
		toxicGas.blocksFluid = fluidToxicGas;
		neurotoxin.blocksFluid = fluidNeurotoxin;
		corrosiveGas.blocksFluid = fluidCorrosiveGas;
		plasma.blocksFluid = fluidPlasma;
		
		registerFluidContainers();
	}
	
	private void registerFluidContainers() {
		vial = new ItemGasVial();
		vialFilled = new ItemGasVialFilled();

		GameRegistry.registerItem( vial, "vial" );
		GameRegistry.registerItem( vialFilled, "vialFilled" );
		
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidNaturalGas, 4000 ), new ItemStack( vialFilled, 1, 0 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidPropellent, 4000 ), new ItemStack( vialFilled, 1, 1 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidHydrogen, 4000 ), new ItemStack( vialFilled, 1, 2 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidSmoke, 4000 ), new ItemStack( vialFilled, 1, 3 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidToxicGas, 4000 ), new ItemStack( vialFilled, 1, 4 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidNeurotoxin, 4000 ), new ItemStack( vialFilled, 1, 5 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidCorrosiveGas, 4000 ), new ItemStack( vialFilled, 1, 6 ), new ItemStack( vial ) );
		FluidContainerRegistry.registerFluidContainer( new FluidStack( fluidPlasma, 4000 ), new ItemStack( vialFilled, 1, 7 ), new ItemStack( vial ) );
	}
	
	private void registerItems() {
		gasMask = new ItemGasMask( enumArmorMaterialGas, 0, 0 );
		smokeGrenade = new ItemSmokeGrenade();
		ash = new ItemEmasherGeneric( "gascraft:ash", "ash" );

		ash.setCreativeTab( tabGasCraft );
		GameRegistry.registerItem( gasMask, "gasMask" );
		GameRegistry.registerItem( smokeGrenade, "smokeGrenade" );
		GameRegistry.registerItem( ash, "ash" );

		OreDictionary.registerOre( "dustAsh", ash );
	}
	
	private void registerRecipes() {
		PhotobioReactorRecipeRegistry.registerRecipe( new ItemStack( EmasherCore.algae ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( fluidHydrogen, 1000 ) );
		PhotobioReactorRecipeRegistry.registerRecipe( new ItemStack( EmasherCore.superAlgae ), new FluidStack( FluidRegistry.WATER, 1000 ), new FluidStack( fluidHydrogen, 3000 ) );
		PhotobioReactorRecipeRegistry.registerRecipe( new ItemStack( EmasherCore.superAlgae ), new FluidStack( fluidToxicGas, 1000 ), new FluidStack( fluidNeurotoxin, 500 ) );
		
		MixerRecipeRegistry.registerRecipe( new ItemStack( Items.gunpowder ), new FluidStack( fluidPropellent, 1000 ), new FluidStack( fluidToxicGas, 500 ) );
		MixerRecipeRegistry.registerRecipe( new ItemStack( SocketsMod.dusts, 1, ItemDusts.Const.lime.ordinal() ), new FluidStack( fluidPropellent, 100 ), new FluidStack( fluidCorrosiveGas, 100 ) );
		
		FuelManager.addBoilerFuel( fluidNaturalGas, 20000 );
		FuelManager.addBoilerFuel( fluidHydrogen, 20000 );
		FuelManager.addBoilerFuel( fluidPlasma, 100000 );
		
		//IronEngineFuel.addFuel(fluidNaturalGas, 5, 40000);
		//IronEngineFuel.addFuel(fluidHydrogen, 5, 40000);
		
		GeneratorFuelRegistry.registerFuel( new FluidStack( fluidNaturalGas, 1000 ), 500, 60, true );
		GeneratorFuelRegistry.registerFuel( new FluidStack( fluidHydrogen, 1000 ), 500, 60, false );
		
		if( Loader.isModLoaded( "BuildCraft|Core" ) && FluidRegistry.getFluid( "fuel" ) != null ) {
			Fluid fuel = FluidRegistry.getFluid( "fuel" );
			GeneratorFuelRegistry.registerFuel( new FluidStack( fuel, 1000 ), 1000, 60, true );
		}
		
		//TE
		//Natural Gas
		NBTTagCompound toSend = new NBTTagCompound();
		toSend.setString( "fluidName", "gasCraft_naturalGas" );
		toSend.setInteger( "energy", 30000 );
		FMLInterModComms.sendMessage( "ThermalExpansion", "CompressionFuel", toSend );
		//Natural Gas
		toSend = new NBTTagCompound();
		toSend.setString( "fluidName", "gasCraft_hydrogen" );
		toSend.setInteger( "energy", 30000 );
		FMLInterModComms.sendMessage( "ThermalExpansion", "CompressionFuel", toSend );

//        if(BuildcraftRecipes.refinery != null) {
//            BuildcraftRecipes.refinery.addRecipe(new FluidStack(fluidNaturalGas, 2), new FluidStack(fluidPropellent, 1), 1, 1);
//        }
		FurnaceRecipes.smelting().func_151394_a( new ItemStack( vialFilled, 0, 1 ), new ItemStack( vialFilled, 1, 1 ), 1.0F );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( vial, 8 ), "s", "g", "g", Character.valueOf( 'g' ), Blocks.glass, Character.valueOf( 's' ), Items.string ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( gasMask ), "lll", "lgl", "vwv", Character.valueOf( 'g' ), Blocks.glass, Character.valueOf( 'l' ), Items.leather,
				Character.valueOf( 'v' ), vial, Character.valueOf( 'w' ), Blocks.wool ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( smokeGrenade ), " i ", "isi", " i ", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 's' ), new ItemStack( vialFilled, 1, 3 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( smokeGrenade ), " i ", "isi", " i ", Character.valueOf( 'i' ), "ingotAluminum", Character.valueOf( 's' ), new ItemStack( vialFilled, 1, 3 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( smokeGrenade ), " i ", "isi", " i ", Character.valueOf( 'i' ), "ingotTin", Character.valueOf( 's' ), new ItemStack( vialFilled, 1, 3 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( chimney ), " i ", "i i", " i ", Character.valueOf( 'i' ), Items.brick ) );
		
		for( int i = 0; i < 16; i++ ) {
			CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( SocketsMod.paintCans[i], 1 ),
							"i", "p", "d", Character.valueOf( 'i' ), Blocks.stone_button, Character.valueOf( 'p' ), new ItemStack( vialFilled, 1, 1 ), Character.valueOf( 'd' ), dyes[i] )
			);
		}
	}

	private void registerEvents() {
		MinecraftForge.EVENT_BUS.register( new WorldGenerationUpdater() );
	}
	
	private void registerWorldGen() {
		if( this.spawnMineGas ) GameRegistry.registerWorldGenerator( gasGenerator, 1 );
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event ) {
		FluidStack ethanol = FluidRegistry.getFluidStack( "bioethanol", 1000 );
		if( ethanol != null ) GeneratorFuelRegistry.registerFuel( ethanol, 750, 40, false );
	}
	
	private void registerInRegistry() {
		Registry.addBlock( "shaleResource", this.shaleResource );
		Registry.addBlock( "chimney", this.chimney );
		Registry.addBlock( "naturalGas", this.naturalGas );
		Registry.addBlock( "propellent", this.propellent );
		Registry.addBlock( "hydrogen", this.hydrogen );
		Registry.addBlock( "smoke", this.smoke );
		Registry.addBlock( "toxicGas", this.toxicGas );
		Registry.addBlock( "neurotoxin", this.neurotoxin );
		Registry.addBlock( "plasma", this.plasma );
		
		Registry.addItem( "vialEmpty", this.vial );
		Registry.addItem( "vialFilled", this.vialFilled );
		Registry.addItem( "gasMask", this.gasMask );
		Registry.addItem( "smokeGrenade", this.smokeGrenade );
		Registry.addItem( "ash", this.ash );
		
		
	}
	
	
}