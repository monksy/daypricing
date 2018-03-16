package com.mrmonksy.daypricing.config

import com.typesafe.config.Config
import scala.collection.JavaConversions._

case class RatesCollection(rates: List[RateItem])
object RatesCollection {
  def apply(config: Config): RatesCollection = {
    val configItems = config.getConfigList("rates")
    val items = configItems.map(a=> RateItem(a))

    RatesCollection(items.toList)
  }
}

case class RateItem(days: String, times: String, price: Long)
object RateItem {
  def apply(config: Config): RateItem = {
    RateItem(config.getString("days"), config.getString("times"), config.getLong("price"))
  }
}


//TODO: Write a class to turn a rate item into a price data item