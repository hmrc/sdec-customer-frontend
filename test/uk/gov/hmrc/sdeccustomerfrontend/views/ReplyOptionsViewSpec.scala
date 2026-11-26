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
import uk.gov.hmrc.sdeccustomerfrontend.forms.ReplyOptionsFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ReplyOptionsView

import scala.jdk.CollectionConverters.*

class ReplyOptionsViewSpec extends ViewSpecBase {

  private lazy val formProvider = app.injector.instanceOf[ReplyOptionsFormProvider]
  private lazy val page: ReplyOptionsView = inject[ReplyOptionsView]
  private lazy val view: Document         = Jsoup.parse(page(formProvider())(request, messages).toString())

  val pageTitle   = "Can you provide the information HMRC has requested?"
  val serviceName = "Share Files Securely with HMRC"

  "ReplyOptionsView" must {
    "have the correct title" in {
      view.title() mustBe s"$pageTitle - $serviceName - GOV.UK"
    }

    "have a unique H1 heading, matching the page title" in {
      val h1Elements: Elements = view.getElementsByTag("h1")
      h1Elements.size() mustBe 1
      h1Elements.text() mustBe pageTitle
    }

    "show a radio selection for selecting the Reply Option" in {
      val radioButtons = view.select("#main-content .govuk-radios")
      radioButtons.size() mustBe 1
      val radioOptions = radioButtons.select(".govuk-radios__item")

      radioOptions.select(".govuk-radios__label").eachText().asScala.toSeq mustBe Seq(
        "Yes, I can provide all the information",
        "No, I can only provide some of the information",
        "No, I cannot provide any of the information"
      )
    }

    "show a hint for the Reply Option selection" in {
      view.select("#main-content #replyOption-hint").text() mustBe
        "Select an option. Don’t worry if you cannot provide all the information HMRC has asked for. You can tell us about what you’re providing and if you can’t add something."
    }

    "show a Continue button" in {
      val button = view.select("#main-content .govuk-button")
      button.size() mustBe 1
      button.first().text() mustBe "Continue"
      button.first().attr("type") mustBe "submit"
    }

  }

}
