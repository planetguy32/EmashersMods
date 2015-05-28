package emasher.api;

import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

public class GeneratorFuelRegistry {
	private static HashMap<String, FuelProperties> registry;
	
	static {
		registry = new HashMap<String, FuelProperties>();
	}
	
	public static void registerFuel( FluidStack fluid, int burnTime, int enPerTick, boolean smoke ) {
		if( registry.get( fluid.getFluid().getName() ) == null ) {
			FuelProperties fp = new FuelProperties( fluid, burnTime, enPerTick, smoke );
			registry.put( fluid.getFluid().getName(), fp );
		} else {
			System.out.println( "[GasCraft] Fuel already registered" );
		}
	}
	
	public static int getBurnTime( String key ) {
		FuelProperties fp = registry.get( key );
		if( fp != null ) {
			return fp.bt;
		}

		return 0;
	}
	
	public static int getEnergyPerTick( String key ) {
		FuelProperties fp = registry.get( key );
		if( fp != null ) {
			return fp.mjt;
		}

		return 0;
	}
	
	public static FluidStack getFuel( String key ) {
		FuelProperties fp = registry.get( key );
		if( fp != null ) {
			return fp.f;
		}

		return null;
	}
	
	public static boolean isFuel( String key ) {
		return registry.get( key ) != null;
	}
	
	public static boolean producesSmoke( String key ) {
		FuelProperties fp = registry.get( key );
		if( fp != null ) {
			return fp.s;
		}

		return false;
	}
	
	private static class FuelProperties {
		FluidStack f;
		int bt;
		int mjt;
		boolean s;

		public FuelProperties( FluidStack fluid, int burnTime, int enPerTick, boolean smoke ) {
			f = fluid;
			bt = burnTime;
			mjt = enPerTick;
			s = smoke;
		}
	}
}
