package emasher.gas;

import emasher.api.IModuleRegistrationManager;
import emasher.api.ModuleRegistry;
import emasher.gas.modules.*;

public class GasModuleRegistrationManager implements IModuleRegistrationManager {
	public GasModuleRegistrationManager() {
	}

	@Override
	public void registerModules() {
		ModuleRegistry.registerModule( new ModFracker( 100 ) );
		ModuleRegistry.registerModule( new ModPhotobioReactor( 101 ) );
		ModuleRegistry.registerModule( new ModFan( 102 ) );
		ModuleRegistry.registerModule( new ModGasVent( 103 ) );
		ModuleRegistry.registerModule( new ModExhaust( 104 ) );
		ModuleRegistry.registerModule( new ModGasGenerator( 105 ) );
		ModuleRegistry.registerModule( new ModGasTurbine( 106 ) );
		ModuleRegistry.registerModule( new ModRefinery( 107 ) );
		ModuleRegistry.registerModule( new ModPlasmaReactor( 108 ) );
	}
}
