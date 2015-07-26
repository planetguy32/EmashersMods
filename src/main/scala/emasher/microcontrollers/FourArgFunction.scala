package emasher.microcontrollers

import org.mod.luaj.vm2.{Varargs, LuaValue}
import org.mod.luaj.vm2.lib.LibFunction

abstract class FourArgFunction extends LibFunction {
  def call( arg1: LuaValue, arg2: LuaValue, arg3: LuaValue, arg4: LuaValue ): LuaValue

  override final def call = call( LuaValue.NIL, LuaValue.NIL, LuaValue.NIL, LuaValue.NIL )
  override final def call( arg1: LuaValue ) = call( arg1, LuaValue.NIL, LuaValue.NIL, LuaValue.NIL )
  override def call( arg1: LuaValue, arg2: LuaValue ) = call( arg1, arg2, LuaValue.NIL, LuaValue.NIL )
  override def call( arg1: LuaValue, arg2: LuaValue, arg3: LuaValue ) = call( arg1, arg2, arg3, LuaValue.NIL )
  override def invoke( varargs: Varargs ) = call( varargs.arg1, varargs.arg( 2 ), varargs.arg( 3 ), varargs.arg( 4 ) )
}
