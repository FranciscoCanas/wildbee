package observers

import models.traits._
import java.util.UUID

//Might have to use an Actor
case class Bugzilla() extends Observer with ObserverCommands {
  override def update(s: Observable, id: UUID, command: ObserverCommand){
    println(s"I $name have observed a change in the '$s', and received command $command")
    command match {
      case Delete => this.untrack(id)
      case Dummy =>  println("Dummy Command");
      case _ => println(s"ERROR $command")
    }

  }
}

