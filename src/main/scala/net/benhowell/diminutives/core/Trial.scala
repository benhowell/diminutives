/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ben Howell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.benhowell.diminutives.core

import com.typesafe.config.Config
import scala.util.Random
import scala.collection.mutable

/**
 * Created by Ben Howell [ben@benhowell.net] on 09-Mar-2014.
 */
object Trial{

  def loadExampleRun(config: Config, item: String): Vector[Map[String,String]] = {
    val es = Vector[mutable.Map[String,String]]() ++ Configuration.getConfigList(config, item)
      .map(e => collection.mutable.Map(createTrial(e).toSeq : _*) )
    es.map(m => (m.toMap)).toVector
  }

  def getTrialBlocks(config: List[Config]): Vector[Vector[Map[String, String]]] = {
    return config.map(createRandomTrialBlock(_)).toVector
  }

  def createRandomTrialRun(config: Config, item: String): Vector[Map[String,String]] = {
    val blocks = getTrialBlocks(Configuration.getConfigList(config, item))
    var list = Vector[Map[String,String]]()
    list ++= Random.shuffle(blocks.flatMap(_.toList))
    list
  }

  def createRandomTrialBlock(config: Config): Vector[Map[String, String]] = {
    val c = config.getString("category")
    val ns = Vector[String]() ++ Random.shuffle(Configuration.getStringList(config, "names"))
    val ts = Vector[mutable.Map[String,String]]() ++ Random.shuffle(Configuration.getConfigList(config, "trials")
      .map(e => collection.mutable.Map(createTrial(e).toSeq : _*) ))
    (ts,ns).zipped foreach { (t,n) => {
      t("name") = n
      t("category") = c
    }}
    ts.map(m => (m.toMap)).toVector
  }

  def createTrial(config: Config): Map[String,String] = {
    val m = new mutable.HashMap[String, String]()
    m.put("id", config.getString("id"))
    m.put("type", config.getString("type"))
    m.put("imagePath", getClass().getResource("/img/").toString)
    m.put("imageName", config.getString("image"))
    m.put("text", config.getString("text"))
    m.put("name", config.getString("name"))
    Map() ++ m
  }

  def composeTrial(trial: Map[String, String]): (String, String, String) = {
    val id = trial("id")
    val text = trial("text").replace("${name}", trial("name"))
    val imgPath = trial("imagePath") + trial("imageName")
    (id, text, imgPath)
  }
}
