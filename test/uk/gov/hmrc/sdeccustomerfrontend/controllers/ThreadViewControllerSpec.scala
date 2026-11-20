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
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.sdeccustomerfrontend.base.ControllerSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.fixtures.ThreadFixtures.{threadId, threadWith}
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.Thread
import uk.gov.hmrc.sdeccustomerfrontend.services.ThreadServiceAlgebra
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ThreadView

import scala.concurrent.{ExecutionContext, Future}

class ThreadViewControllerSpec extends ControllerSpecBase {

  private lazy val controller = new ThreadViewController(
    messagesControllerComponents,
    fakeIdentifyExternalUser,
    new ThreadServiceAlgebra {
      override def getThread(threadId: String)(using HeaderCarrier): Future[Thread] =
        Future.successful(threadWith().copy(id = threadId))
    },
    inject[ThreadView]
  )(using inject[ExecutionContext])

  private def getRequest = FakeRequest(GET, routes.ThreadViewController.onPageLoad(threadId).url)

  "ThreadViewController.onPageLoad" must {
    "return OK and render the thread page" in {
      val result = controller.onPageLoad(threadId)(getRequest)

      status(result) mustBe OK

      val document = Jsoup.parse(contentAsString(result))
      document.select("h1").text() mustBe "John Doe"
      document.select(".govuk-caption-l").text() mustBe s"Thread reference number: $threadId"
      document.select(".govuk-inset-text").text() mustBe "HMRC is waiting for your reply."
    }

    "show the thread reference from the URL" in {
      val result   = controller.onPageLoad("ABC123")(getRequest)
      val document = Jsoup.parse(contentAsString(result))

      document.select(".govuk-caption-l").text() mustBe "Thread reference number: ABC123"
    }

    "render the requested files, the message and the attachment" in {
      val document = Jsoup.parse(contentAsString(controller.onPageLoad(threadId)(getRequest)))

      document.select(".sdec-requested-files li").size() mustBe 2
      document.select(".hmrc-timeline__event-content > p").size() mustBe 2
      document.select(".hmrc-timeline__event-content a.govuk-link").text() mustBe
        "Proof of Address Guidance Sheet.docx"
    }

    "show the Reply button" in {
      val document = Jsoup.parse(contentAsString(controller.onPageLoad(threadId)(getRequest)))
      val button   = document.select(".govuk-main-wrapper a.govuk-button")

      button.size() mustBe 1
      button.text() mustBe "Reply"
    }
  }
}
