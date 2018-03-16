package com.mrmonksy.daypricing.datamodifiers

import org.joda.time.DateTimeConstants

object DateAndTimeHandling {
  def convertToDay: PartialFunction[String, Int] = {
    case "sun" => DateTimeConstants.SUNDAY
    case "mon" => DateTimeConstants.MONDAY
    case "tues" => DateTimeConstants.TUESDAY
    case "wed" => DateTimeConstants.WEDNESDAY
    case "thurs" => DateTimeConstants.THURSDAY
    case "fri" => DateTimeConstants.FRIDAY
    case "sat" => DateTimeConstants.SATURDAY
  }

}
