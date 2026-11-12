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
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import uk.gov.hmrc.sdeccustomerfrontend.base.ViewSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.forms.ThreadReferenceFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.NormalMode
import uk.gov.hmrc.sdeccustomerfrontend.views.html.EnterThreadReferenceView

class EnterThreadReferenceViewSpec extends ViewSpecBase with Matchers with GuiceOneAppPerSuite {

  private lazy val formProvider = app.injector.instanceOf[ThreadReferenceFormProvider]

  private lazy val page: EnterThreadReferenceView = inject[EnterThreadReferenceView]
  private lazy val view: Document = Jsoup.parse(page(formProvider(), NormalMode)(request, messages).toString())

  val pageTitle = "Enter the thread reference number"

  "EnterThreadReferenceView" must {
    "have the correct title" in {
      view.title() mustBe s"$pageTitle - Share Files Securely with HMRC - GOV.UK"
    }

    "have a unique H1 heading that matches the page title" in {
      val h1Elements: Elements = view.getElementsByTag("h1")
      h1Elements.size() mustBe 1
      h1Elements.get(0).ownText() mustBe pageTitle // caption is in a child span
    }

    "have the correct heading caption" in {
      val headingCaption: Elements = view.select("h1 .govuk-caption-l")
      headingCaption.text() mustBe "Share Files Securely with HMRC"
    }

    "have the correct paragraphs" in {
      val paragraphs: Elements = view.getElementsByTag("p")
      paragraphs.size() mustBe 3
      paragraphs
        .get(0)
        .text() mustBe "This can be found on the email you received from HMRC, inviting you to use the service."
      paragraphs
        .get(1)
        .text() mustBe "The number is 14 digits long and contains letters and numbers, for example KL8978654322."
      paragraphs
        .get(2)
        .text() mustBe "If you cannot locate your thread reference number, contact the person who invited you to use the service for help."
    }

    "have the thread-reference form field and a 'Continue' button" in {
      val input = view.select("input[name=thread-reference]")
      input.size() mustBe 1
      input.attr("type") mustBe "text"

      view.select("label[for=thread-reference]").text() mustBe pageTitle

      val buttons: Elements = view.getElementsByClass("govuk-button")
      buttons.size() mustBe 1
      buttons.first().text() mustBe "Continue"
    }

    "have the 'Still having issues' detail component" in {
      val details = view.select("details.govuk-details")
      details.size() mustBe 1
      details.select(".govuk-details__summary-text").text() mustBe "Still having issues?"
      details.select(".govuk-details__text").text() mustBe
        "If you cannot find your thread reference number, contact the person who invited you to the service for help."
    }
  }

}
