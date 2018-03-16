package com.mrmonksy.daypricing.datamodifiers

import com.mrmonksy.daypricing.PriceDataItem
import com.mrmonksy.daypricing.config.RateItem
import org.joda.time.LocalTime

object PriceDataUtility {
  val TIME_PATTERN = raw"(\d\d)(\d\d)-(\d\d)(\d\d)".r

  def from(rateItem: RateItem): List[PriceDataItem] = {

    val TIME_PATTERN(startHour, startMinute, endHour, endMinute) = rateItem.times
    val startTime = new LocalTime(startHour.toInt, startMinute.toInt)
    val endTime = new LocalTime(endHour.toInt, endMinute.toInt)
    val price = rateItem.price

    rateItem.days.split(",").map(_.trim).map(DateAndTimeHandling.convertToDay).map(
      d => new PriceDataItem(d, price, startTime, endTime)
    ).toList
  }
}
