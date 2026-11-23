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
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.test.FakeRequest
import uk.gov.hmrc.sdeccustomerfrontend.forms.ReplyOptionsFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.ReplyOption
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ReplyOptionsView

import scala.jdk.CollectionConverters.*

class ReplyOptionsViewSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  private val view         = app.injector.instanceOf[ReplyOptionsView]
  private val request      = FakeRequest("GET", "/sdec/reply-options")
  private val messages     = app.injector.instanceOf[MessagesApi].preferred(request)
  private val formProvider = app.injector.instanceOf[ReplyOptionsFormProvider]

  private def form: Form[ReplyOption] = formProvider()

  private lazy val page: Document =
    Jsoup.parse(view(form)(using request, messages).body)

  val pageTitle   = "Can you provide the information HMRC has requested?"
  val serviceName = "Share Files Securely with HMRC"

  "ReplyOptionsView" should {
    "have the correct title" in {
      page.title() shouldBe s"$pageTitle - $serviceName - GOV.UK"
    }

    "have a unique H1 heading, matching the page title" in {
      val h1Elements: Elements = page.getElementsByTag("h1")
      h1Elements.size() shouldBe 1
      h1Elements.text() shouldBe pageTitle
    }

    "show a radio selection for selecting the Reply Option" in {
      val radioButtons = page.select("#main-content .govuk-radios")
      radioButtons.size() shouldBe 1
      val radioOptions = radioButtons.select(".govuk-radios__item")

      radioOptions.select(".govuk-radios__label").eachText().asScala.toSeq shouldBe Seq(
        "Yes, I can provide all the information",
        "No, I can only provide some of the information",
        "No, I cannot provide any of the information"
      )
    }

    "show a hint for the Reply Option selection" in {
      page.select("#main-content #replyOption-hint").text() shouldBe
        "Select an option. Don't worry if you cannot provide all the information HMRC has asked for. You can tell us about what you're providing and if you can't add something."
    }

    "show a Continue button" in {
      val button = page.select("#main-content .govuk-button")
      button.size()               shouldBe 1
      button.first().text()       shouldBe "Continue"
      button.first().attr("type") shouldBe "submit"
    }

  }

}
