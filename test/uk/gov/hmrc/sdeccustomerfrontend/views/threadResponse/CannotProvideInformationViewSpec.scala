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

package uk.gov.hmrc.sdeccustomerfrontend.views.threadResponse

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import uk.gov.hmrc.sdeccustomerfrontend.base.ViewSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.forms.CannotProvideInformationFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.NormalMode
import uk.gov.hmrc.sdeccustomerfrontend.views.html.threadResponse.CannotProvideInformationView

class CannotProvideInformationViewSpec extends ViewSpecBase with Matchers with GuiceOneAppPerSuite {

  private lazy val formProvider = app.injector.instanceOf[CannotProvideInformationFormProvider]
  private lazy val page: CannotProvideInformationView = inject[CannotProvideInformationView]
  private lazy val view: Document = Jsoup.parse(page(formProvider(), NormalMode)(request, messages).toString())

  private val pageTitle = "Tell us why you cant provide the files HMRC has asked for"

  "CannotProvideInformationView" must {

    "have the correct title" in {
      view.title() mustBe s"$pageTitle - Share Files Securely with HMRC - GOV.UK"
    }

    "have a unique H1 heading that matches the page title" in {
      val h1Elements: Elements = view.getElementsByTag("h1")

      h1Elements.size() mustBe 1
      h1Elements.get(0).text() mustBe pageTitle
    }

    "have the correct caption" in {
      val captions = view.getElementsByClass("govuk-caption-l")
      captions.size() mustBe 1
      captions.first().text() mustBe "Step 2 of 3"
    }

    "have the explanatory paragraph" in {
      val paragraphs = view.getElementsByTag("p")

      paragraphs
        .get(0)
        .text() mustBe "Explain why you cant send any file(s) so HMRC can further help you with the request"
    }

    "display the files requested banner" in {
      val banner = view.select(".sdec-files-requested-banner")

      banner.size() mustBe 1
      banner.select("h2").text() mustBe "HMRC asked for the following file(s):"
    }

    "display the requested files" in {
      val files = view.select(".sdec-files-requested-banner li")

      files.size() mustBe 2

      files.get(0).text() mustBe "Proof of address"
      files.get(1).text() mustBe "Completed lamp tax form"
    }

    "display the message field" in {
      val input = view.select("textarea[name=message]")

      input.size() mustBe 1
    }

    "display the warning text" in {
      val warning = view.select(".govuk-warning-text")

      warning.size() mustBe 1
      warning.text() must include("HMRC will not tolerate rude, abusive, or threatening language")
    }

    "display the Continue button" in {
      val buttons: Elements = view.getElementsByClass("govuk-button")

      buttons.size() mustBe 1
      buttons.first().text() mustBe "Continue"
    }
  }
}
