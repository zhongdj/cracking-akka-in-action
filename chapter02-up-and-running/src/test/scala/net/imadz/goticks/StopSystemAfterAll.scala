package net.imadz.goticks

import akka.testkit.TestKit
import org.scalatest.{Suite, BeforeAndAfterAll}

/**
  * Created by Barry on 2/1/16.
  */
trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>

  override protected def afterAll(): Unit = {
    super.afterAll()
    system.shutdown()
  }
}
