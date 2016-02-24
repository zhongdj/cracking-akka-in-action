package net.imadz.faulttolerance

import net.imadz.common.StopSystemAfterAll
import org.scalatest._
import akka.testkit.TestKit
import akka.actor.ActorSystem
/**
  * Created by Barry on 2/24/16.
  */
class LifecycleTest extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "Lifecycle Hooks" must {
    "invoke stop while sending stop to actor" in {
      val lifecycle = system.actorOf(ActorLifecycleHooks.props)
      lifecycle ! "stop"
    }

    "invoke restart while sending restart to actor" in {
      val lifecycle = system.actorOf(ActorLifecycleHooks.props)
      lifecycle ! "restart"
    }
  }
}
