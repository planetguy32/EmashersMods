package emasher.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.HashMap;

public class Registry {
	public static HashMap<String, Item> items = new HashMap<String, Item>();
	public static HashMap<String, Block> blocks = new HashMap<String, Block>();
	
	public static void addItem( String key, Item item ) {
		if( items.get( key ) != null ) System.out.println( "[Emasher API] ERROR: Key \"" + key + "\" already exists" );
		else items.put( key, item );
	}
	
	public static Item getItem( String key ) {
		Item result = items.get( key );
		if( result == null ) {
			System.out.println( "[Emasher API] ERROR: Could not find item with key \"" + key + "\"" );
			return null;
		}
		
		return result;
	}
	
	public static void addBlock( String key, Block block ) {
		if( blocks.get( key ) != null ) System.out.println( "[Emasher API] ERROR: Key \"" + key + "\" already exists" );
		else blocks.put( key, block );
	}
	
	public static Block getBlock( String key ) {
		Block result = blocks.get( key );
		if( result == null ) {
			System.out.println( "[Emasher API] ERROR: Could not find block with key \"" + key + "\"" );
			return null;
		}
		
		return result;
	}
}
