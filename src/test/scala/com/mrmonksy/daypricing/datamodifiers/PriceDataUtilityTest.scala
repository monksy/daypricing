package com.mrmonksy.daypricing.datamodifiers

import com.mrmonksy.daypricing.PriceDataItem
import com.mrmonksy.daypricing.config.RateItem
import org.joda.time.LocalTime
import org.scalatest.{FlatSpec, Matchers}

class PriceDataUtilityTest extends FlatSpec with Matchers {


  "PriceDataUtility::from" should "be able to convert a sample rate item" in {
    val input = RateItem("sat,sun", "0600-2000", 2000)
    val expected = List(
      new PriceDataItem(6, 2000, new LocalTime(6, 0), new LocalTime(20, 0)),
      new PriceDataItem(7, 2000, new LocalTime(6, 0), new LocalTime(20, 0))
    )

    val actual = PriceDataUtility.from(input)

    actual should be(expected)
  }

}
