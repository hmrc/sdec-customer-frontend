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

package uk.gov.hmrc.sdeccustomerfrontend.views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import uk.gov.hmrc.sdeccustomerfrontend.base.ViewSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.controllers.routes
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ReplySentConfirmationView

class ReplySentConfirmationViewSpec extends ViewSpecBase {

  private val threadUrl   = routes.DevelopmentInProgressController.onPageLoad().url
  private val feedbackUrl = routes.DevelopmentInProgressController.onPageLoad().url

  private lazy val page: ReplySentConfirmationView = inject[ReplySentConfirmationView]
  private lazy val view: Document = Jsoup.parse(page(threadUrl, feedbackUrl)(request, messages).toString())

  val pageTitle = "Reply sent"

  "ReplySentConfirmationView" must {

    "have the correct title" in {
      view.title() mustBe s"$pageTitle - Share Files Securely with HMRC - GOV.UK"
    }

    "have a unique H1 heading that matches the page title" in {
      val h1Elements: Elements = view.getElementsByTag("h1")
      h1Elements.size() mustBe 1
      h1Elements.get(0).ownText() mustBe pageTitle
    }

    "have the correct h2 subheading" in {
      val h2Elements = view.select("#main-content h2")
      h2Elements.size() mustBe 1
      h2Elements.get(0).ownText() mustBe "What happens next"
    }

    "have the correct paragraphs" in {
      val paragraphs: Elements = view.getElementsByTag("p")
      paragraphs.size() mustBe 4
      paragraphs
        .get(0)
        .text() mustBe "You have been sent a confirmation email."
      paragraphs
        .get(1)
        .text() mustBe "Your reply has been sent to HMRC - we will review your reply and will message to advise you of any next steps."
      paragraphs
        .get(2)
        .text() mustBe "You can view your reply in the thread."
      paragraphs
        .get(3)
        .text() mustBe "What did you think of this service? (takes 30 seconds)"
    }

    "have the correct hyperlinks for 'in the thread' and 'What did you think of this service?'" in {
      val hyperlinks = view.select("#main-content a")
      hyperlinks.size() mustBe 2
      hyperlinks.get(0).attr("href") mustBe threadUrl
      hyperlinks.get(0).text() mustBe "in the thread."
      hyperlinks.get(1).attr("href") mustBe feedbackUrl
      hyperlinks.get(1).text() mustBe "What did you think of this service?"
    }
  }

}
