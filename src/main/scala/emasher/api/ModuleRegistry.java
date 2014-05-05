package emasher.api;

import java.util.ArrayList;


public class ModuleRegistry
{
	public static SocketModule[] moduleList;
	public static ArrayList<IModuleRegistrationManager> registers;
	public static int numModules = 1024;
	
	static
	{
		moduleList = new SocketModule[numModules];
		registers = new ArrayList<IModuleRegistrationManager>();
	}
	
	public static void registerModule(SocketModule module)
	{
		if(moduleList[module.moduleID] == null) moduleList[module.moduleID] = module;
	}
	
	public static SocketModule getModule(int id)
	{
		return moduleList[id];
	}
	
	public static void addModuleRegistrationManager(IModuleRegistrationManager manager)
	{
		registers.add(manager);
	}
	
}
