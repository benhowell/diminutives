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
import scala.collection.mutable

/**
 * Created by Ben Howell [ben@benhowell.net] on 03-May-2014.
 */
object Intro {

  def load(config: Config, item: String): Vector[Map[String,String]] = {
    val es = Vector[mutable.Map[String,String]]() ++ Configuration.getConfigList(config, item)
      .map(e => collection.mutable.Map(createIntro(e).toSeq : _*) )
    es.map(m => (m.toMap)).toVector
  }

  def createIntro(config: Config): Map[String,String] = {
    val m = new mutable.HashMap[String, String]()
    m.put("id", config.getString("id"))
    m.put("label", config.getString("label"))
    m.put("text", config.getString("text"))
    Map() ++ m
  }

  def compose(intro: Map[String, String]): (String, String, String) = {
    val id = intro("id")
    val label = intro("label")
    val text = intro("text")
    (id, label, text)
  }

}
