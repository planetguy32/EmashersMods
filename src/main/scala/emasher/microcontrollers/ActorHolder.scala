package emasher.microcontrollers

import akka.actor.Props

class ActorHolder {

}

object ActorHolder {
  def controllerProps = Props[ControllerActor]
}