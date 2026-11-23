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

package uk.gov.hmrc.sdeccustomerfrontend.forms

import play.api.data.Forms.*
import play.api.data.format.Formatter
import play.api.data.validation.Constraint
import play.api.data.{Form, FormError, Mapping}
import uk.gov.hmrc.sdeccustomerfrontend.forms.mappings.Mappings
import uk.gov.hmrc.sdeccustomerfrontend.models.{ReplyOption, ReplyOptions}

import javax.inject.Inject

class ReplyOptionsFormProvider @Inject extends Mappings {

  def apply(): Form[ReplyOption] = Form(
    mapping(
      "replyOption" -> enumerable[ReplyOptions](requiredKey =
        "label.select_whether_you_can_provide_the_requested_information"
      )
    )(ReplyOption.apply)(replyOption =>
      Some(
        replyOption.options
      )
    )
  )

}
