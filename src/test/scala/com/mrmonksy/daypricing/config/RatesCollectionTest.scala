package com.mrmonksy.daypricing.config

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class RatesCollectionTest extends FlatSpec with Matchers {
  "A sample config" should "turn into an object" in {
    val config = ConfigFactory.load("application.json")

    val expected = RatesCollection(List(RateItem("mon,tues,wed,thurs,fri", "0600-1800", 1500),
      RateItem("sat,sun", "0600-2000", 2000)
    ))
    val actual = RatesCollection(config)

    actual should be (expected)
  }
}
