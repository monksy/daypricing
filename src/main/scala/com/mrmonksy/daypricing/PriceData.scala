package com.mrmonksy.daypricing


import org.joda.time.{DateTime, Interval, LocalTime}

class PriceDataManager(prices: List[PriceDataItem]) {
  /**
    * This searches for the best interval within the parameters. Note: this is inclusive of the startTime and exclusive of the end time.
    * @param startTime A date and time representing the start of a the time that we're requesting.
    * @param endTime A date and time representing the end of the time that we're representing.
    * @return Returns the pricing information. If it exists.
    */
  def findPriceBetween(startTime: DateTime, endTime: DateTime): Option[PriceDataItem] = {
    assume(startTime.dayOfWeek() == endTime.dayOfWeek(), "It was assumed that the start and end time were on the same day. ")

    val intervalComparison = new Interval(startTime, endTime)

    //Narrow it down to the day of the week and then find by the times.
    //Sense the dates don't exceed a day, the start and the end are the same day
    prices.filter(test => startTime.dayOfWeek().get() == test.day).
      find(_.getIntervalForDate(startTime).contains(intervalComparison))
  }


}


/**
  * This defines the price for the time period, the start time and the end time.
  *
  * @param day   day that this price represents. This is based off of the DateTimeCOnstnats for the day
  * @param price The price of the time period.
  * @param start The start time of the pricing period. The only thing that matters in this intstant is the hours.
  * @param end   The end time of the pricing period. The only thing that matters in this intstant is the hours.
  */
class PriceDataItem(val day: Int, val price: Long, val start: LocalTime, val end: LocalTime) {
  //Ensure the the start is earlier than the end time.
  require(start.compareTo(end) == -1, "The start time must be earlier than the end time")

  /**
    * This produces a JodaTime interval representing the time periods for a specific date.
    * @param dateTime The date that we're looking for.
    * @return Returns an interval between the times on that date.
    */
  def getIntervalForDate(dateTime: DateTime): Interval = {
    val startDate = dateTime.withTime(start)
    val endDate = dateTime.withTime(end)

    new Interval(startDate, endDate)
  }

  //Hashcode and equals does not check for price becuase it's not considered as part of the identity per spects (you won't have overlapping prices)
  def canEqual(other: Any): Boolean = other.isInstanceOf[PriceDataItem]

  override def equals(other: Any): Boolean = other match {
    case that: PriceDataItem =>
      (that canEqual this) &&
        day == that.day &&
        start == that.start &&
        end == that.end
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(day, start, end)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
