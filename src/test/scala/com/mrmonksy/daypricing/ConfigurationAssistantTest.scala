package com.mrmonksy.daypricing

import java.io.FileNotFoundException

import com.mrmonksy.daypricing.config.{RateItem, RatesCollection}
import com.mrmonksy.daypricing.datamodifiers.BadDayException
import com.typesafe.config.{ConfigException, ConfigFactory}
import org.joda.time.{DateTimeConstants, LocalTime}
import org.scalatest.{Matchers, WordSpec}

class ConfigurationAssistantTest extends WordSpec with Matchers {

  "ConfigurationAssistantTest::grabConfigurationAndConvert" should {
    "Handle the standard configuration" in {
      val expected =
        List(
          new PriceDataItem(DateTimeConstants.MONDAY, 1500, new LocalTime(6, 0), new LocalTime(18, 0)),
          new PriceDataItem(DateTimeConstants.TUESDAY, 1500, new LocalTime(6, 0), new LocalTime(18, 0))
        )

      val instance = new ConfigurationAssistant(Some("simpleconfig.json"))
      val actual = instance.grabConfigurationAndConvert()

      actual should be(expected)
    }

    "Handle the expect an exception for a file that doesn't exist" in {
      val instance = new ConfigurationAssistant(Some("doesnotexistapplication.json"))
      an[RateParsingError] should be thrownBy instance.grabConfigurationAndConvert()
    }

    "Handle the expect an exception for an empty config" in {
      val instance = new ConfigurationAssistant(Some("EmptyConfig.json"))
      an[ConfigException.Parse] should be thrownBy instance.grabConfigurationAndConvert()
    }


    "Handle the expect an exception for a rate that doesn't have a valid day" in {
      val instance = new ConfigurationAssistant(Some("BadConfig.json"))
      an[BadDayException] should be thrownBy instance.grabConfigurationAndConvert()
    }

    "expect an exception for a file that has a bad syntax" in {
      val instance = new ConfigurationAssistant(Some("BadSyntax.json"))
      an[ConfigException.Parse] should be thrownBy instance.grabConfigurationAndConvert()
    }


    "Handle the expect an exception for a rate that has missing items" in {
      val instance = new ConfigurationAssistant(Some("MissingRateinfo.json"))
      an[RateParsingError] should be thrownBy instance.grabConfigurationAndConvert()
    }


  }

}
