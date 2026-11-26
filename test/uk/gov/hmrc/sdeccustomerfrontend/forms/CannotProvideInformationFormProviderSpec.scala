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

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.test.Injecting
import uk.gov.hmrc.sdeccustomerfrontend.forms.models.CannotProvideInformationForm

class CannotProvideInformationFormProviderSpec
    extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with Injecting {

  private val cannotProvideInformationForm: Form[CannotProvideInformationForm] =
    new CannotProvideInformationFormProvider().apply()

  private val fieldName    = "message"
  private val validMessage = "I am unable to provide the requested files."

  private val maxLengthMessage     = "a" * 500
  private val overMaxLengthMessage = "a" * 501

  private val requiredErrorMessage  = "Enter a message"
  private val maxLengthErrorMessage = "Reason must be 500 characters or less"

  private lazy val messages: Messages = inject[MessagesApi].preferred(Seq(Lang.defaultLang))

  private def bind(value: String) = cannotProvideInformationForm.bind(Map(fieldName -> value))

  private def message(error: FormError): String = messages(error.message)

  "CannotProvideInformationFormProvider" must {
    "successfully bind a valid message" in {
      bind(validMessage).value mustBe Some(CannotProvideInformationForm(validMessage))
    }

    "successfully bind a message with exactly 500 characters" in {
      bind(maxLengthMessage).value mustBe Some(CannotProvideInformationForm(maxLengthMessage))
    }

    "fail to bind when the message is blank" in {
      bind("").errors.map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the message is only spaces" in {
      bind("   ").errors.map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the field is missing" in {
      cannotProvideInformationForm
        .bind(Map.empty[String, String])
        .errors
        .map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the message exceeds 500 characters" in {
      bind(overMaxLengthMessage).errors.map(message) mustBe Seq(maxLengthErrorMessage)
    }
  }
}
