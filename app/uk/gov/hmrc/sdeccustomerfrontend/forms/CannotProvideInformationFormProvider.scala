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

import play.api.data.Form
import play.api.data.Forms.mapping
import uk.gov.hmrc.sdeccustomerfrontend.forms.mappings.Mappings
import uk.gov.hmrc.sdeccustomerfrontend.forms.models.CannotProvideInformationForm

import javax.inject.Inject

class CannotProvideInformationFormProvider @Inject() extends Mappings {

  private val MaximumLength = 500

  def apply(): Form[CannotProvideInformationForm] =
    Form(
      mapping(
        "message" ->
          text("label.enter_a_message")
            .verifying(
              firstError(
                maxLength(
                  MaximumLength,
                  "label.reason_must_be_500_characters_or_less"
                )
              )
            )
      )(CannotProvideInformationForm.apply)(form => Some(form.message))
    )

}
