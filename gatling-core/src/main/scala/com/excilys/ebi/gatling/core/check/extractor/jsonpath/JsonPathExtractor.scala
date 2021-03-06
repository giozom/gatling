/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.core.check.extractor.jsonpath

import scala.collection.JavaConversions.asScalaBuffer
import scala.util.Try

import com.excilys.ebi.gatling.core.check.extractor.Extractor

/**
 * A built-in extractor for extracting values with  Xpath like expressions for Json
 *
 * @constructor creates a new JsonPathExtractor
 * @param textContent the text where the search will be made
 */
class JsonPathExtractor(textContent: Array[Byte]) extends Extractor {

	val json: Option[JsonNode] = Try(Json.parse(textContent)).toOption

	/**
	 * @param occurrence
	 * @param expression
	 * @return extract the occurrence of the given rank matching the expression
	 */
	def extractOne(occurrence: Int)(expression: String): Option[String] = extractMultiple(expression) match {
		case Some(results) if (results.isDefinedAt(occurrence)) => results(occurrence)
		case _ => None
	}

	/**
	 * @param expression
	 * @return extract all the occurrences matching the expression
	 */
	def extractMultiple(expression: String): Option[Seq[String]] = json.map(new JaxenJackson(expression).selectNodes(_).map(_.asInstanceOf[JsonText].value))

	/**
	 * @param expression
	 * @return count all the occurrences matching the expression
	 */
	def count(expression: String): Option[Int] = extractMultiple(expression).map(_.size)
}