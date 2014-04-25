package net.benhowell.diminutives.core

import com.typesafe.config.{ConfigFactory, Config}
import scala.collection.JavaConversions._

/**
 * Created by Ben Howell [ben@benhowell.net] on 01-Apr-2014.
 */
object Configuration {
  def loadConfig(): Config = {
    return ConfigFactory.load()
  }

  def getConfigString(config: Config, item: String): String = {
    return config.getString(item)
  }

  def getConfigList(config: Config, item: String): List[Config] = {
    return config.getConfigList(item).toList
  }

  def getStringList(config: Config, item: String): List[String] = {
    return config.getStringList(item).toList
  }

}
