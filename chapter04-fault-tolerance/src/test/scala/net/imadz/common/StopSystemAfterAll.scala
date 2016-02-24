package net.imadz.common

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

/**
  * Created by Barry on 2/24/16.
  */
trait StopSystemAfterAll extends BeforeAndAfterAll {

  this: TestKit with Suite =>

  override def afterAll = {
    system.shutdown
    super.afterAll
  }
}
