package emasher.microcontrollers

import akka.actor.Props

class ActorHolder {
  val
}

object ActorHolder {
  def controllerProps = Props[ControllerActor]
}