package emasher.defense;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.*;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import emasher.core.*;

@Mod(modid="emasherdefense", name="Defense", version="1.2.1.1", dependencies = "required-after:emashercore")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class EmasherDefense
{

    // The instance of your mod that Forge uses.
	@Instance("EmasherDefense")
	public static EmasherDefense instance;
	
	public static Block chainFence;
	public static Block sandbag;
	public static Block emeryTile;
	public static Block deflectorBase;
	public static Block deflector;
	
	public static Item chainSheet;
	public static Item fenceWire;
	
	int chainFenceID;
	int chainSheetID;
	int fenceWireID;
	int sandbagID;
	int emeryTileID;
	int deflectorBaseID;
	int deflectorID;
	
	public static CreativeTabs tabDefense = new CreativeTabs("tabDefense")
	{
		public ItemStack getIconItemStack()
		{
			return new ItemStack(deflectorBase, 1, 0);
		}
	};
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="emasher.defense.client.ClientProxy", serverSide="emasher.defense.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		chainFenceID = config.get(Configuration.CATEGORY_BLOCK,  "Chain Fence ID", 2036).getInt();
		chainSheetID = config.get(Configuration.CATEGORY_ITEM, "Chain Sheet ID", 2037).getInt();
		fenceWireID = config.get(Configuration.CATEGORY_ITEM, "Fence Wire ID", 2038).getInt();
		sandbagID = config.get(Configuration.CATEGORY_BLOCK, "Sandbag ID", 2045).getInt();
		emeryTileID = config.get(Configuration.CATEGORY_BLOCK, "Emery Tile ID", 2046).getInt();
		deflectorBaseID = config.get(Configuration.CATEGORY_BLOCK, "Deflector Generator ID", 3170).getInt();
		deflectorID = config.get(Configuration.CATEGORY_BLOCK, "Deflector ID", 3171).getInt();

		if(config.hasChanged())
			config.save();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabDefense", "en_US", "Defense");
		
		GameRegistry.registerTileEntity(TileDeflectorGen.class, "DeflectorGen");
		
		chainFence = (new BlockThin(chainFenceID, Material.iron))
				.setHardness(5.0F).setStepSound(Block.soundMetalFootstep)
				.setUnlocalizedName("chainFence");
		
		sandbag = new BlockSandBag(sandbagID, Material.cloth)
				.setHardness(2.0F).setResistance(20.0F)
				.setStepSound(Block.soundClothFootstep).setUnlocalizedName("sandbag");
		
		emeryTile = new BlockEmeryTile(emeryTileID, Material.rock)
			.setHardness(2.0F).setResistance(20.0F)
			.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("emeryTile");
		
		deflectorBase = new BlockDeflectorGen(deflectorBaseID, Material.iron)
				.setHardness(50.0F).setResistance(2000.0F)
				.setStepSound(Block.soundMetalFootstep).setUnlocalizedName("deflectorGenerator");
		deflector = new BlockDeflector(deflectorID)
				.setBlockUnbreakable().setStepSound(Block.soundGlassFootstep)
				.setUnlocalizedName("deflector");
		
		chainSheet = new ItemChainSheet(chainSheetID);
		fenceWire = new ItemFenceWire(fenceWireID);
		
		LanguageRegistry.addName(chainSheet, "Chain Sheet");
		LanguageRegistry.addName(fenceWire, "Fence Wire");
		LanguageRegistry.addName(emeryTile, "Emery Tile");
		LanguageRegistry.addName(deflectorBase, "Deflector Generator");
		LanguageRegistry.addName(deflector, "Deflector");
		
		Item.itemsList[chainFence.blockID] = new ItemBlockThin(chainFence.blockID - 256);
		GameRegistry.registerBlock(sandbag, "sandbag");
		GameRegistry.registerBlock(emeryTile, "emeryTile");
		GameRegistry.registerBlock(deflectorBase, "deflectorBase");
		GameRegistry.registerBlock(deflector, "deflector");		

		LanguageRegistry.instance().addStringLocalization("tile.chainFence.chainFence.name", "Chain Link Fence");
		LanguageRegistry.instance().addStringLocalization("tile.chainFence.chainPost.name", "Chain Link Fence Post");
		LanguageRegistry.instance().addStringLocalization("tile.chainFence.barbWireFence.name", "Barbed Wire Fence");
		LanguageRegistry.instance().addStringLocalization("tile.chainFence.barbPostWood.name", "Barbed Wire Fence Wood Post");
		LanguageRegistry.instance().addStringLocalization("tile.chainFence.barbPost.name", "Barbed Wire Fence Post");
		LanguageRegistry.instance().addStringLocalization("tile.chainFence.razorWireFence.name", "Razor Wire Fence");
		LanguageRegistry.addName(sandbag, "Sandbags");

		addRecipies();
		
		proxy.registerRenderers();
		
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		// Stub Method
	}
	
	private void addRecipies()
	{
		//Chain Sheet
		GameRegistry.addRecipe(new ItemStack(chainSheet, 6), new Object[]
				{
					"# #", " # ", "#  ", Character.valueOf('#'), Item.ingotIron
				});
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(chainSheet, 6), "# #", " # ", "#  ", Character.valueOf('#'), "ingotAluminum"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(chainSheet, 6), "# #", " # ", "#  ", Character.valueOf('#'), "ingotTin"));
		
		//Fence Wire
		GameRegistry.addRecipe(new ItemStack(fenceWire, 6), new Object[]
				{
					"###", " B ", Character.valueOf('#'), Item.ingotIron,
					Character.valueOf('B'), Block.woodenButton
				});
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(fenceWire, 6), "###", " B ", Character.valueOf('#'), "ingotAluminum", Character.valueOf('B'), Block.woodenButton));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(fenceWire, 6), "###", " B ", Character.valueOf('#'), "ingotTin", Character.valueOf('B'), Block.woodenButton));
		
		//Chain Link Fence
		GameRegistry.addRecipe(new ItemStack(chainFence, 16, 0), new Object[]
				{
					"###", "###", Character.valueOf('#'), chainSheet
				});
		
		//Chain Link Fence Post
		
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 1), "ingotAluminum", new ItemStack(chainFence, 1, 0)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 1), "ingotTin", new ItemStack(chainFence, 1, 0)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 1), Item.ingotIron, new ItemStack(chainFence, 1, 0)));
		
		//Barbed Wire Fence
		GameRegistry.addRecipe(new ItemStack(chainFence, 8, 2), new Object[]
				{
					" I ", "###", Character.valueOf('I'), Item.ingotIron,
					Character.valueOf('#'), fenceWire
				});
		
		//Barbed Wire Fence Post
		
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 4), "ingotAluminum", new ItemStack(chainFence, 1, 2)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 4), "ingotTin", new ItemStack(chainFence, 1, 2)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 4), Item.ingotIron, new ItemStack(chainFence, 1, 2)));
		
		
		//Barbed Wire Fence Wood Post
		
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 3), Block.fence, new ItemStack(chainFence, 1, 2)));
		
		//Barbed Wire Fence
		GameRegistry.addRecipe(new ItemStack(chainFence, 8, 2), new Object[]
				{
					"###", " I ", Character.valueOf('I'), Item.ingotIron,
					Character.valueOf('#'), fenceWire
				});
		
		//Razor Wire Fence
		GameRegistry.addRecipe(new ItemStack(chainFence, 8, 5), new Object[]
				{
					"II ", "###", " II", Character.valueOf('I'), Item.ingotIron,
					Character.valueOf('#'), fenceWire
				});
		
		//Razor Wire Fence
		GameRegistry.addRecipe(new ItemStack(chainFence, 8, 5), new Object[]
				{
					" II", "###", "II ", Character.valueOf('I'), Item.ingotIron,
					Character.valueOf('#'), fenceWire
				});
		
		//Chain Armour
		GameRegistry.addRecipe(new ItemStack(Item.helmetChain), new Object[]
				{
					"###", "# #", Character.valueOf('#'), chainSheet
				});
		GameRegistry.addRecipe(new ItemStack(Item.plateChain), new Object[]
				{
					"# #", "###", "###", Character.valueOf('#'), chainSheet
				});
		GameRegistry.addRecipe(new ItemStack(Item.legsChain), new Object[]
				{
					"###", "# #", "# #", Character.valueOf('#'), chainSheet
				});
		GameRegistry.addRecipe(new ItemStack(Item.bootsChain), new Object[]
				{
					"# #", "# #", Character.valueOf('#'), chainSheet
				});
		
		//Sandbag
		GameRegistry.addRecipe(new ItemStack(sandbag, 8), new Object[]
				{
					"www", "wsw", "www", Character.valueOf('w'), Block.cloth, Character.valueOf('s'), Block.sand
				});
		
		//Emery Tile
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(emeryTile, 1), "##", "##", Character.valueOf('#'), "gemEmery"));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(EmasherCore.gem, 4, 0), emeryTile));
		
		//Deflector
		GameRegistry.addRecipe(new ItemStack(deflectorBase, 4), new Object[]
				{
					"odo", "clc", "omo", Character.valueOf('o'), Block.obsidian, Character.valueOf('d'), Item.diamond,
					Character.valueOf('c'), EmasherCore.circuit, Character.valueOf('l'), Block.redstoneLampIdle,
					Character.valueOf('m'), EmasherCore.machine
				});
	}
}
