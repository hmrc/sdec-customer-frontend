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
import play.api.data.FormError
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.test.Injecting
import uk.gov.hmrc.sdeccustomerfrontend.models.CheckYourAnswers

class CheckYourAnswersFormProviderSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite with Injecting {

  private val fieldName            = "declarationCheck"
  private val requiredErrorMessage = "Select yes to make your declaration"

  private lazy val messages: Messages = inject[MessagesApi].preferred(Seq(Lang.defaultLang))

  private val checkYourAnswersForm = new CheckYourAnswersFormProvider().apply()
  private def message(error: FormError): String = messages(error.message)
  private def bind(value:    Boolean) = checkYourAnswersForm.bind(Map(fieldName -> value.toString))

  "CheckYourAnswersFormProviderSpec" must {

    "successfully bind when the declaration is ticked" in {
      val result = bind(true)
      result.errors mustBe empty
      result.value mustBe Some(CheckYourAnswers(declarationCheck = true))
    }

    "fail to bind when the declaration is not ticked" in {
      checkYourAnswersForm
        .bind(Map.empty[String, String])
        .errors
        .map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the declaration is false" in {
      bind(false).errors.map(message) mustBe Seq(requiredErrorMessage)
    }

  }

}
