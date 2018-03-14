package com.mrmonksy.daypricing

import org.joda.time.{DateTime, DateTimeConstants, Interval, LocalTime}
import org.scalatest.{FlatSpec, FunSuite, Matchers}

class PriceDataItemTest extends FlatSpec with Matchers {
  "PriceDataItem::getIntervalForDate" should "Produce an interval with the respective date times." in {
    val timeOne = new LocalTime(0, 0)
    val timeTwo = new LocalTime(23, 0)
    val dateOne = new DateTime().withDate(2018, 3, 12).withTime(11, 12, 0, 0)
    val instance = new PriceDataItem(DateTimeConstants.MONDAY, 500, timeOne, timeTwo)

    val expected = new Interval(dateOne.withTime(timeOne), dateOne.withTime(timeTwo))

    val actual = instance.getIntervalForDate(dateOne)

    actual should be(expected)
  }

}
