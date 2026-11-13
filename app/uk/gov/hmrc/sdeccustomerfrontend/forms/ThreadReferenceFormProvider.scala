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
import uk.gov.hmrc.sdeccustomerfrontend.forms.mappings.ThreadReferenceMapping
import uk.gov.hmrc.sdeccustomerfrontend.forms.models.ThreadReferenceForm
import uk.gov.hmrc.sdeccustomerfrontend.forms.validators.Validation

import javax.inject.Inject

class ThreadReferenceFormProvider @Inject() extends ThreadReferenceMapping {

  def apply(): Form[ThreadReferenceForm] = Form(
    mapping(
      "thread-reference" ->
        text("label.enter_the_thread_reference_number")
          .verifying(
            firstError(
              regexp(
                Validation.ThreadReferencePattern,
                "label.the_thread_reference_contains_14_characters_using_"
              )
            )
          )
    )(ThreadReferenceForm.apply)(threadReferenceForm => Some(threadReferenceForm.threadReference))
  )

}
