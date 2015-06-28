package emasher.defense;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod( modid = "emasherdefense", name = "ET Defense", version = "1.2.1.1", dependencies = "required-after:emashercore" )
public class EmasherDefense {

	// The instance of your mod that Forge uses.
	@Instance( "EmasherDefense" )
	public static EmasherDefense instance;
	
	public static Block chainFence;
	public static Block sandbag;
	public static Block emeryTile;
	public static Block deflectorBase;
	public static Block deflector;
	
	public static Item chainSheet;
	public static Item fenceWire;
	
	public static CreativeTabs tabDefense = new CreativeTabs( "tabDefense" ) {
		@Override
		public Item getTabIconItem() {
			return new ItemStack( deflectorBase, 1, 0 ).getItem();
		}

		public ItemStack getIconItemStack() {
			return new ItemStack( deflectorBase, 1, 0 );
		}
	};

	@SidedProxy( clientSide = "emasher.defense.client.ClientProxy", serverSide = "emasher.defense.CommonProxy" )
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event ) {
	}
	
	@EventHandler
	public void load( FMLInitializationEvent event ) {
		GameRegistry.registerTileEntity( TileDeflectorGen.class, "DeflectorGen" );
		
		chainFence = ( new BlockThin( Material.iron ) )
				.setHardness( 5.0F ).setStepSound( Block.soundTypeMetal )
				.setBlockName( "chainFence" );
		
		sandbag = new BlockSandBag( Material.cloth )
				.setHardness( 2.0F ).setResistance( 20.0F )
				.setStepSound( Block.soundTypeCloth ).setBlockName( "sandbag" );
		
		emeryTile = new BlockEmeryTile( Material.rock )
				.setHardness( 2.0F ).setResistance( 20.0F )
				.setStepSound( Block.soundTypeStone ).setBlockName( "emeryTile" );
		
		deflectorBase = new BlockDeflectorGen( Material.iron )
				.setHardness( 50.0F ).setResistance( 2000.0F )
				.setStepSound( Block.soundTypeMetal ).setBlockName( "deflectorGenerator" );

		deflector = new BlockDeflector()
				.setBlockUnbreakable().setStepSound( Block.soundTypeGlass )
				.setBlockName( "deflector" );
		
		chainSheet = new ItemChainSheet();
		GameRegistry.registerItem( chainSheet, "chainSheet", "emasherdefense" );
		fenceWire = new ItemFenceWire();
		GameRegistry.registerItem( fenceWire, "fenceWire", "emasherdefense" );

		GameRegistry.registerBlock( chainFence, ItemBlockThin.class, "chainFence" );
		GameRegistry.registerBlock( sandbag, "sandbag" );
		GameRegistry.registerBlock( emeryTile, "emeryTile" );
		GameRegistry.registerBlock( deflectorBase, "deflectorBase" );
		GameRegistry.registerBlock( deflector, "deflector" );


		addRecipies();
		
		proxy.registerRenderers();
		
		
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event ) {
	}
	
	private void addRecipies() {
		//Chain Sheet
		GameRegistry.addRecipe( new ItemStack( chainSheet, 6 ), new Object[]
				{
						"# #", " # ", "#  ", Character.valueOf( '#' ), Items.iron_ingot
				} );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( chainSheet, 6 ), "# #", " # ", "#  ", Character.valueOf( '#' ), "ingotAluminum" ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( chainSheet, 6 ), "# #", " # ", "#  ", Character.valueOf( '#' ), "ingotTin" ) );
		
		//Fence Wire
		GameRegistry.addRecipe( new ItemStack( fenceWire, 6 ), new Object[]
				{
						"###", " B ", Character.valueOf( '#' ), Items.iron_ingot,
						Character.valueOf( 'B' ), Blocks.wooden_button
				} );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( fenceWire, 6 ), "###", " B ", Character.valueOf( '#' ), "ingotAluminum", Character.valueOf( 'B' ), Blocks.wooden_button ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( fenceWire, 6 ), "###", " B ", Character.valueOf( '#' ), "ingotTin", Character.valueOf( 'B' ), Blocks.wooden_button ) );
		
		//Chain Link Fence
		GameRegistry.addRecipe( new ItemStack( chainFence, 16, 0 ), new Object[]
				{
						"###", "###", Character.valueOf( '#' ), chainSheet
				} );
		
		//Chain Link Fence Post
		
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 1 ), "ingotAluminum", new ItemStack( chainFence, 1, 0 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 1 ), "ingotTin", new ItemStack( chainFence, 1, 0 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 1 ), Items.iron_ingot, new ItemStack( chainFence, 1, 0 ) ) );
		
		//Barbed Wire Fence
		GameRegistry.addRecipe( new ItemStack( chainFence, 8, 2 ), new Object[]
				{
						" I ", "###", Character.valueOf( 'I' ), Items.iron_ingot,
						Character.valueOf( '#' ), fenceWire
				} );
		
		//Barbed Wire Fence Post
		
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 4 ), "ingotAluminum", new ItemStack( chainFence, 1, 2 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 4 ), "ingotTin", new ItemStack( chainFence, 1, 2 ) ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 4 ), Items.iron_ingot, new ItemStack( chainFence, 1, 2 ) ) );
		
		
		//Barbed Wire Fence Wood Post
		
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( chainFence, 1, 3 ), Blocks.fence, new ItemStack( chainFence, 1, 2 ) ) );
		
		//Barbed Wire Fence
		GameRegistry.addRecipe( new ItemStack( chainFence, 8, 2 ), new Object[]
				{
						"###", " I ", Character.valueOf( 'I' ), Items.iron_ingot,
						Character.valueOf( '#' ), fenceWire
				} );
		
		//Razor Wire Fence
		GameRegistry.addRecipe( new ItemStack( chainFence, 8, 5 ), new Object[]
				{
						"II ", "###", " II", Character.valueOf( 'I' ), Items.iron_ingot,
						Character.valueOf( '#' ), fenceWire
				} );
		
		//Razor Wire Fence
		GameRegistry.addRecipe( new ItemStack( chainFence, 8, 5 ), new Object[]
				{
						" II", "###", "II ", Character.valueOf( 'I' ), Items.iron_ingot,
						Character.valueOf( '#' ), fenceWire
				} );
		
		//Chain Armour
		GameRegistry.addRecipe( new ItemStack( Items.chainmail_helmet ), new Object[]
				{
						"###", "# #", Character.valueOf( '#' ), chainSheet
				} );
		GameRegistry.addRecipe( new ItemStack( Items.chainmail_chestplate ), new Object[]
				{
						"# #", "###", "###", Character.valueOf( '#' ), chainSheet
				} );
		GameRegistry.addRecipe( new ItemStack( Items.chainmail_leggings ), new Object[]
				{
						"###", "# #", "# #", Character.valueOf( '#' ), chainSheet
				} );
		GameRegistry.addRecipe( new ItemStack( Items.chainmail_boots ), new Object[]
				{
						"# #", "# #", Character.valueOf( '#' ), chainSheet
				} );
		
		//Sandbag
		GameRegistry.addRecipe( new ItemStack( sandbag, 8 ), new Object[]
				{
						"www", "wsw", "www", Character.valueOf( 'w' ), Blocks.wool, Character.valueOf( 's' ), Blocks.sand
				} );
		
		//Emery Tile
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emeryTile, 1 ), "##", "##", Character.valueOf( '#' ), "gemEmery" ) );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapelessOreRecipe( new ItemStack( EmasherCore.gem, 4, 0 ), emeryTile ) );
		
		//Deflector
		GameRegistry.addRecipe( new ItemStack( deflectorBase, 4 ), new Object[]
				{
						"odo", "clc", "omo", Character.valueOf( 'o' ), Blocks.obsidian, Character.valueOf( 'd' ), Items.diamond,
						Character.valueOf( 'c' ), EmasherCore.circuit, Character.valueOf( 'l' ), Blocks.redstone_lamp,
						Character.valueOf( 'm' ), EmasherCore.machine
				} );
	}
}
