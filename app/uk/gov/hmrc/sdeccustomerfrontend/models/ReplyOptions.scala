/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.sdeccustomerfrontend.models

import play.api.i18n.Messages
import play.api.libs.json.*
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

enum ReplyOptions(val messageKey: String) {
  case All
      extends ReplyOptions(
        "label.yes_i_can_provide_all_the_information"
      )
  case Partial
      extends ReplyOptions(
        "label.no_i_can_only_provide_some_of_the_information"
      )
  case Nothing
      extends ReplyOptions(
        "label.no_i_cannot_provide_any_of_the_information"
      )
}

object ReplyOptions {

  def options(using messages: Messages): Seq[RadioItem] =
    values.toSeq.zipWithIndex.map { (option, index) =>
      RadioItem(
        id = Some(if index == 0 then "replyOption" else s"replyOption_$index"),
        value = Some(option.toString),
        content = Text(messages(option.messageKey))
      )
    }

  def parseReplyOption(value: String): Option[ReplyOptions] =
    ReplyOptions.values.find(_.toString.equalsIgnoreCase(value))

  given Enumerable[ReplyOptions] = Enumerable(
    ReplyOptions.values.map(status => status.toString -> status).toSeq*
  )

  given Format[ReplyOptions] = Format(
    Reads {
      case JsString(s) =>
        parseReplyOption(s)
          .map(JsSuccess(_))
          .getOrElse(JsError("Invalid value"))
      case _ => JsError("Expected JSON String")
    },
    Writes(o => JsString(o.toString))
  )

}
