package emasher.microcontrollers

import java.io.InputStream

import emasher.tileentities.TileSocket
import org.mod.luaj.vm2.compiler.LuaC
import org.mod.luaj.vm2.lib._
import org.mod.luaj.vm2.lib.jse.{JseMathLib, JseBaseLib}
import org.mod.luaj.vm2._

class LuaScript( chunk: LuaValue, setHook: LuaValue, globals: Globals ) {
  def run(): Unit = {
    val thread = new LuaThread( globals, chunk )
    setHook.invoke( LuaValue.varargsOf( Array[LuaValue]( thread, LuaScript.instructionLimitHook, LuaValue.EMPTYSTRING,
      LuaValue.valueOf( LuaScript.MAX_INSTRUCTIONS ) ) ) )
    val result = thread.resume( LuaValue.NIL )
    println( result )
  }
}

object LuaScript {
  val runGlobals = new Globals
  final val MAX_INSTRUCTIONS = 128
  val instructionLimitHook = new ZeroArgFunction {
    override def call(): LuaValue = {
      throw new Error( "Too Many Instructions In Script" )
    }
  }

  def init( te: TileSocket ): Unit = {
    runGlobals.load( new JseBaseLib )
    runGlobals.load( new PackageLib )
    runGlobals.load( new StringLib )
    runGlobals.load( new JseMathLib )
    LoadState.install( runGlobals )
    LuaC.install( runGlobals )
    LuaString.s_metatable = new ReadOnlyLuaTable().init( LuaString.s_metatable )
  }

  def createFromStream( stream: InputStream, entryPoint: String, tileEntity: TileSocket ): LuaScript = {
    val globals = new Globals

    globals.load( new JseBaseLib )
    globals.load( new PackageLib )
    globals.load( new StringLib )
    globals.load( new JseMathLib )
    globals.load( new TableLib )
    val socketLib = new SocketLib
    socketLib.init( tileEntity )
    globals.load( socketLib )

    globals.load( new DebugLib )
    val setHook = globals.get( "debug" ).get( "sethook" )
    globals.set( "debug", LuaValue.NIL )
    val chunk = runGlobals.load( stream, entryPoint, "t", globals )
    new LuaScript( chunk, setHook, globals )
  }

  class ReadOnlyLuaTable extends LuaTable {
    def init( table: LuaValue ): ReadOnlyLuaTable = {
      presize( table.length, 0 )
      var n = table.next( LuaValue.NIL )
      while( ! n.arg1.isnil ) {
        val key = n.arg1
        val value = n.arg( 2 )

        val realValue = if( value.istable ) {
          new ReadOnlyLuaTable().init( value )
        } else {
          value
        }

        super.rawset( key, realValue)

        n = table.next( n.arg1 )
      }

      this
    }

    override def setmetatable( metatable: LuaValue ) = throw new LuaError( "Table is Read Only" )
    override def set( key: Int, value: LuaValue ) = throw new LuaError( "Table is Read Only" )
    override def rawset( key: Int, value: LuaValue ) = throw new LuaError( "Table is Read Only" )
    override def rawset( key: LuaValue, value: LuaValue ) = throw new LuaError( "Table is Read Only" )
    override def remove( pos: Int ) = throw new LuaError( "Table is Read Only" )
  }
}