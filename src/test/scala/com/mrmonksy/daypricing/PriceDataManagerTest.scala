package com.mrmonksy.daypricing


import org.joda.time.{DateTime, DateTimeConstants, LocalTime}
import org.scalatest.{FlatSpec, Matchers}

class PriceDataManagerTest extends FlatSpec with Matchers {

  "PriceDataManager::findPriceBetween" should "Should not find a price when the price exceeds the upper bounds" in {
    //Monday
    val dateOne = new DateTime().withDate(2018, 3, 12).withTime(11, 0, 0, 0)
    val dateTwo = new DateTime().withDate(2018, 3, 12).withTime(12, 12, 0, 0)

    val data = List(
      new PriceDataItem(DateTimeConstants.MONDAY, 500, new LocalTime(11, 0), new LocalTime(11, 30)),
      new PriceDataItem(DateTimeConstants.SUNDAY, 2500, new LocalTime(11, 0), new LocalTime(23, 0))

    )
    val instance = new PriceDataManager(data)
    val expected = None
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }

  "PriceDataManager::findPriceBetween" should "Should not find a price when the price exceeds the lower bounds" in {
    //Monday
    val dateOne = new DateTime().withDate(2018, 3, 12).withTime(11, 0, 0, 0)
    val dateTwo = new DateTime().withDate(2018, 3, 12).withTime(12, 12, 0, 0)

    val data = List(
      new PriceDataItem(DateTimeConstants.MONDAY, 500, new LocalTime(12, 0), new LocalTime(23, 30)),
      new PriceDataItem(DateTimeConstants.SUNDAY, 2500, new LocalTime(11, 0), new LocalTime(23, 0))

    )
    val instance = new PriceDataManager(data)
    val expected = None
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }



  "PriceDataManager::findPriceBetween" should "not find a price for an item that doesn't exist on the same date" in {
    //This date is a tuesday
    val dateOne = new DateTime().withDate(2018, 3, 13).withTime(11, 12, 0, 0)
    val dateTwo = new DateTime().withDate(2018, 3, 13).withTime(12, 12, 0, 0)

    val data = List(
      new PriceDataItem(DateTimeConstants.SUNDAY, 500, new LocalTime(11, 0), new LocalTime(23, 0))
    )
    val instance = new PriceDataManager(data)
    val expected = None
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }

  "PriceDataManager::findPriceBetween" should "find a price for an item that is on the same day with wide open availbity" in {
    //This is a monday
    //WOULDLIKE: I would like a generator to pick out a date that fits the day of week
    val dateOne = new DateTime().withDate(2018, 3, 12).withTime(11, 12, 0, 0)
    val dateTwo = new DateTime().withDate(2018, 3, 12).withTime(12, 12, 0, 0)

    val data = List(
      new PriceDataItem(DateTimeConstants.MONDAY, 500, new LocalTime(0, 0), new LocalTime(23, 0)),
      new PriceDataItem(DateTimeConstants.SUNDAY, 2500, new LocalTime(11, 0), new LocalTime(23, 0))

    )
    val instance = new PriceDataManager(data)
    val expected = Some(new PriceDataItem(DateTimeConstants.MONDAY, 500, new LocalTime(0, 0), new LocalTime(23, 0)))
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }

  "PriceDataManager::findPriceBetween" should "find example for \u20292015-07-01T07:00:00Z to 2015-07-01T12:00:00Z should yield 1500\n2015-07-04T07:00:00Z to 2015-07-04T12:00:00Z should yield 2000" in {
    val dateOne = new DateTime().withDate(2015, 7, 1).withTime(7, 0, 0, 0)
    val dateTwo = new DateTime().withDate(2015, 7, 1).withTime(12, 0, 0, 0)


    val itemOne = new PriceDataItem(DateTimeConstants.WEDNESDAY, 1750, new LocalTime(6, 0), new LocalTime(18, 0))
    val itemTwo = new PriceDataItem(DateTimeConstants.WEDNESDAY, 1000, new LocalTime(1, 0), new LocalTime(5, 0))
    val data = List(
      itemOne,
      itemTwo

    )
    val instance = new PriceDataManager(data)
    val expected = Some(itemOne)
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }



  "PriceDataManager::findPriceBetween" should "find example for 2015-07-04T07:00:00Z to 2015-07-04T20:00:00Z should be none " in {
    val dateOne = new DateTime().withDate(2015, 7, 4).withTime(7, 0, 0, 0)
    val dateTwo = new DateTime().withDate(2015, 7, 4).withTime(12, 0, 0, 0)



    val itemOne = new PriceDataItem(DateTimeConstants.SATURDAY, 2000, new LocalTime(9, 0), new LocalTime(21, 0))
    val itemTwo = new PriceDataItem(DateTimeConstants.SATURDAY, 1000, new LocalTime(1, 0), new LocalTime(5, 0))
    val data = List(
      itemOne,
      itemTwo
    )
    val instance = new PriceDataManager(data)
    val expected = None
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }





  "PriceDataManager::findPriceBetween" should "Don't find a price for an item that is on the same day but it doesn't have the availablity" in {
    val dateOne = new DateTime().withDate(2018, 3, 12).withTime(11, 12, 0, 0)
    val dateTwo = new DateTime().withDate(2018, 3, 12).withTime(12, 12, 0, 0)

    val data = List(
      new PriceDataItem(DateTimeConstants.MONDAY, 500, new LocalTime(16, 0), new LocalTime(23, 0)),
      new PriceDataItem(DateTimeConstants.SUNDAY, 2500, new LocalTime(11, 0), new LocalTime(23, 0))

    )
    val instance = new PriceDataManager(data)
    val expected = None
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }

  "PriceDataMangaer::findPriceBetween" should "not find a price in a dataset that doesn't exist" in {
    val dateOne = new DateTime().withDate(2018, 3, 13).withTime(11, 12, 0, 0)
    val dateTwo = new DateTime().withDate(2018, 3, 13).withTime(12, 12, 0, 0)

    val instance = new PriceDataManager(List())
    val expected = None
    val actual = instance.findPriceBetween(dateOne, dateTwo)

    actual should be(expected)
  }
}
