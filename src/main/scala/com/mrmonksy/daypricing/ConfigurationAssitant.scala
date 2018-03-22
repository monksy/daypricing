package com.mrmonksy.daypricing

import java.io.FileNotFoundException

import com.mrmonksy.daypricing.config.RatesCollection
import com.mrmonksy.daypricing.datamodifiers.{BadDayException, PriceDataUtility}
import com.typesafe.config.{Config, ConfigException, ConfigFactory}

/**
  * This is a manager to handle the configuration details.
  *
  * @param fileName This is only a value used for testing. If there is no value set here. Then the default is to let Typesafe config handle the resolution.
  *                 Typically the best way to redefine where the configuration lives is the System property config.file. See https://github.com/lightbend/config#standard-behavior.
  */
class ConfigurationAssistant(fileName: Option[String]) {

  def grabConfigurationAndConvert(): List[PriceDataItem] = {
    val config = fileName.map(f => ConfigFactory.load(f)).getOrElse(ConfigFactory.defaultApplication())

    try {
      val baseObject = RatesCollection(config)
      baseObject.rates.flatMap(PriceDataUtility.from)
    } catch {
      case e@(_: ConfigException.Parse | _: ConfigException.Missing) => throw new RateParsingError(config, e)

    }
  }

  def makeNiceErrorMessage(error: Throwable): String = {
    error match {
      case p: ConfigException.Parse => s"The configuration file was malformed JSON/HOCON: ${fileName.orElse(Option(System.getProperty("config.file"))).getOrElse("application.json")}"
      case p: ConfigException.IO => s"There was an issue with accessing config (${fileName.orElse(Option(System.getProperty("config.file"))).getOrElse("application.json")})"
      case p: RateParsingError => s"There was an attempt to parse the config (${fileName.orElse(Option(System.getProperty("config.file"))).getOrElse("application.json")}), but it was invalid. Config was: ${p.config.toString}"
      case b: BadDayException => s"There was a day (${b.day}) that was in the configuration but does not exist in the system."
      case _ => "No nice error could be displayed."
    }
  }
}

case class RateParsingError(config: Config, e: Throwable) extends Exception(e)