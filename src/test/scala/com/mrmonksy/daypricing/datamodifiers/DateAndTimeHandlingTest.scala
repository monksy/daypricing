package com.mrmonksy.daypricing.datamodifiers

import java.lang.IllegalArgumentException

import org.scalatest.{FlatSpec, Matchers}

class DateAndTimeHandlingTest extends FlatSpec with Matchers {

  //This would be much easier with a parameterized test, this was doable in junit 4.10+

  "DateAndTimeHandlingTest::convertToDay" should "Should handle sun" in {
    val day = "sun"
    val expected = 7
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }

  "DateAndTimeHandlingTest::convertToDay" should "Should handle a bad value" in {
    val day = "montag"

    an [BadDayException] should be thrownBy  DateAndTimeHandling.convertToDay(day)
  }


  "DateAndTimeHandlingTest::convertToDay" should "Should handle monday" in {
    val day = "mon"
    val expected = 1
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }

  "DateAndTimeHandlingTest::convertToDay" should "Should handle tuesday" in {
    val day = "tues"
    val expected = 2
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }

  "DateAndTimeHandlingTest::convertToDay" should "Should handle wednesday" in {
    val day = "wed"
    val expected = 3
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }

  "DateAndTimeHandlingTest::convertToDay" should "Should handle thursday" in {
    val day = "thurs"
    val expected = 4
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }

  "DateAndTimeHandlingTest::convertToDay" should "Should handle friday" in {
    val day = "fri"
    val expected = 5
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }

  "DateAndTimeHandlingTest::convertToDay" should "Should handle saturday" in {
    val day = "sat"
    val expected = 6
    val actual = DateAndTimeHandling.convertToDay(day)

    actual should be (expected)
  }



}
