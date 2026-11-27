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

package uk.gov.hmrc.sdeccustomerfrontend.controllers

import org.jsoup.Jsoup
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.sdeccustomerfrontend.base.ControllerSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.forms.CheckYourAnswersFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class CheckYourAnswersControllerSpec extends ControllerSpecBase {

  val pageTitle = "Check your answers"

  private lazy val controller = new CheckYourAnswersController(
    messagesControllerComponents,
    fakeIdentifyExternalUser,
    new CheckYourAnswersFormProvider,
    inject[CheckYourAnswersView]
  )(using inject[ExecutionContext])

  private def getRequest = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad().url)

  "CheckYourAnswersController.onPageLoad" must {
    "return OK and render the Check your answers page" in {
      val result = controller.onPageLoad()(getRequest)
      status(result) mustBe OK

      val document = Jsoup.parse(contentAsString(result))
      document.select("h1").text() mustBe pageTitle
    }

    "show the 'Send reply' button" in {
      val document = Jsoup.parse(contentAsString(controller.onPageLoad()(getRequest)))
      val button   = document.select(".govuk-main-wrapper button.govuk-button")
      button.size() mustBe 1
      button.text() mustBe "Send reply"
    }

  }

}
