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
import uk.gov.hmrc.sdeccustomerfrontend.forms.models.ThreadReferenceForm

class ThreadReferenceFormProviderSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite with Injecting {

  private val threadReferenceForm: Form[ThreadReferenceForm] = new ThreadReferenceFormProvider().apply()

  private val fieldName            = "thread-reference"
  private val validThreadReference = "KL897865432211"

  private val requiredErrorMessage = "Enter the thread reference number"
  private val invalidErrorMessage  = "The thread reference contains 14 characters using A - Z and 0 - 9 only"

  private lazy val messages: Messages = inject[MessagesApi].preferred(Seq(Lang.defaultLang))

  private def bind(value: String) = threadReferenceForm.bind(Map(fieldName -> value))

  private def message(error: FormError): String = messages(error.message)

  "ThreadReferenceFormProvider" must {
    "successfully bind a valid 14 character alphanumeric reference" in {
      bind(validThreadReference).value mustBe Some(ThreadReferenceForm(validThreadReference))
    }

    "fail to bind when the reference is blank" in {
      bind("").errors.map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the reference is only spaces" in {
      bind("   ").errors.map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the field is missing" in {
      threadReferenceForm.bind(Map.empty[String, String]).errors.map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the reference is too short" in {
      bind("KL8978654").errors.map(message) mustBe Seq(invalidErrorMessage)
    }

    "fail to bind when the reference is too long" in {
      bind(validThreadReference + "1").errors.map(message) mustBe Seq(invalidErrorMessage)
    }

    "fail to bind when the reference contains invalid character" in {
      bind("KL89786543$$$$").errors.map(message) mustBe Seq(invalidErrorMessage)
    }
  }

}
