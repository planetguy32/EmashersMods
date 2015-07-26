package emasher.microcontrollers

import java.io.InputStream

import emasher.tileentities.TileSocket
import net.minecraft.nbt._
import org.mod.luaj.vm2.compiler.LuaC
import org.mod.luaj.vm2.lib._
import org.mod.luaj.vm2.lib.jse.{JseMathLib, JseBaseLib}
import org.mod.luaj.vm2._

class LuaScript( chunk: LuaValue, setHook: LuaValue, globals: Globals ) {
  def run: Boolean = {
    val thread = new LuaThread( globals, chunk )
    setHook.invoke( LuaValue.varargsOf( Array[LuaValue]( thread, LuaScript.instructionLimitHook, LuaValue.EMPTYSTRING,
      LuaValue.valueOf( LuaScript.MAX_INSTRUCTIONS ) ) ) )
    val result = thread.resume( LuaValue.NIL )
    result.arg1().checkboolean
  }

  def runWithIndexAndState( name: String, index: Int, state: Boolean ): Boolean = {
    globals.set( name + "Index", index )
    globals.set( name + "State", state match {
      case true => 1
      case false => 0
    })

    run
  }

  def saveGlobalsToNBT( nbt: NBTTagCompound ): Unit = {
    val boolData = new NBTTagList
    val intData = new NBTTagList
    val doubleData = new NBTTagList
    val stringData = new NBTTagList
    val namesData = new NBTTagList

    var n = globals.next( LuaValue.NIL )
    while( ! n.arg1.isnil ) {
      val key = n.arg1
      val value = n.arg( 2 )

      if( value.isboolean() ) {
        val theBool = value.checkboolean()
        namesData.appendTag( new NBTTagString( "[bool]" + key.checkjstring() ) )
        boolData.appendTag( new NBTTagString( theBool match {
          case true => "true"
          case _ => "false"
        } ) )
      } else if( value.isnil() ) {
        namesData.appendTag( new NBTTagString( "[nil]" + key.checkjstring() ) )
      } else if( value.isnumber() ) {
        value.checknumber() match {
          case theInt: LuaInteger =>
            namesData.appendTag( new NBTTagString( "[int]" + key.checkjstring() ) )
            intData.appendTag( new NBTTagString( "" + theInt.checkint() ) )
          case theDouble: LuaDouble =>
            namesData.appendTag( new NBTTagString( "[double]" + key.checkjstring() ) )
            doubleData.appendTag( new NBTTagDouble( theDouble.checkdouble() ) )
          case _ =>
        }
      } else if( value.isstring() ) {
        val theString = value.checkstring().checkjstring()
        namesData.appendTag( new NBTTagString( "[string]" + key.checkjstring ) )
        stringData.appendTag( new NBTTagString( theString ) )
      }

      n = globals.next( n.arg1 )
    }

    nbt.setTag( "luaNames", namesData )
    nbt.setTag( "luaBools", boolData )
    nbt.setTag( "luaInts", intData )
    nbt.setTag( "luaDoubles", doubleData )
    nbt.setTag( "luaStrings", stringData )
  }

  def readGlobalsFromNBT( nbt: NBTTagCompound ): Unit = {
    if( nbt.hasKey( "luaNames" ) ) {
      val namesData = nbt.getTagList( "luaNames", 8 )

      val boolData = nbt.getTagList( "luaBools", 8 )
      val intData = nbt.getTagList( "luaInts", 8 )
      val doubleData = nbt.getTagList( "luaDoubles", 6 )
      val stringData = nbt.getTagList( "luaStrings", 8 )

      var currentBool = 0
      var currentInt = 0
      var currentDouble = 0
      var currentString = 0

      for( i <- 0 to namesData.tagCount() - 1 ) {
        val name = namesData.getStringTagAt( i )
        name match {
          case n: String if n startsWith "[bool]" =>
            val theBoolString = boolData.getStringTagAt( currentBool )
            currentBool += 1
            val theBool = theBoolString match {
              case "true" => LuaValue.TRUE
              case "false" => LuaValue.FALSE
            }
            globals.set( n.stripPrefix( "[bool]" ), theBool )
          case n: String if n startsWith "[nil]" =>
            globals.set( n.stripPrefix( "[nil]" ), LuaValue.NIL )
          case n: String if n startsWith "[int]" =>
            val theIntString = intData.getStringTagAt( currentInt )
            currentInt += 1
            val theInt = theIntString.toInt
            globals.set( n.stripPrefix( "[int]" ), LuaInteger.valueOf( theInt ) )
          case n: String if n startsWith "[double]" =>
            val theDouble = doubleData.func_150309_d( currentDouble )
            currentDouble += 1
            globals.set( n.stripPrefix( "[double]" ), LuaDouble.valueOf( theDouble ) )
          case n: String if n startsWith "[string]" =>
            val theString = stringData.getStringTagAt( currentString )
            currentString += 1
            globals.set( n.stripPrefix( "[string]" ), LuaString.valueOf( theString ) )
          case _ =>
        }
      }
    }
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

  def init(): Unit = {
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

    val socketData = new LuaUserdata( tileEntity )
    globals.set( "socketObject", socketData )

    val socketLib = new SocketLib
    socketLib.install( globals )

    globals.load( new DebugLib )
    val setHook = globals.get( "debug" ).get( "sethook" )
    globals.set( "debug", LuaValue.NIL )
    globals.set( "require", LuaValue.NIL )
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