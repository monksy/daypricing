package com.mrmonksy.daypricing.datamodifiers

import org.joda.time.DateTimeConstants

object DateAndTimeHandling {
  val dayMappings = Map(
    "sun" -> DateTimeConstants.SUNDAY,
    "mon" -> DateTimeConstants.MONDAY,
    "tues" -> DateTimeConstants.TUESDAY,
    "wed" -> DateTimeConstants.WEDNESDAY,
    "thurs" -> DateTimeConstants.THURSDAY,
    "fri" -> DateTimeConstants.FRIDAY,
    "sat" -> DateTimeConstants.SATURDAY
  )

  def convertToDay(day: String): Int = {
    dayMappings.get(day) match {
      case Some(v) => v
      case None => throw new BadDayException(day)
    }
  }

}

case class BadDayException(day: String) extends Exception(s"The day $day does not have a mapping.")
