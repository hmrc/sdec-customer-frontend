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
import uk.gov.hmrc.sdeccustomerfrontend.base.ViewSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.controllers.routes
import uk.gov.hmrc.sdeccustomerfrontend.fixtures.ThreadFixtures.*
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.{Attachment, Thread, ThreadStatus}
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ThreadView

class ThreadViewSpec extends ViewSpecBase {

  private lazy val page: ThreadView = inject[ThreadView]

  private def render(thread: Thread): Document =
    Jsoup.parse(page(thread)(request, messages).toString())

  private lazy val view: Document = render(threadWith())

  private val replyButton = ".govuk-main-wrapper a.govuk-button"

  "ThreadView" must {
    "have the correct title" in {
      view.title() mustBe "Thread details - Share Files Securely with HMRC - GOV.UK"
    }

    "have a single H1 with the recipient's name" in {
      view.getElementsByTag("h1").size() mustBe 1
      view.select("h1").text() mustBe "John Doe"
    }

    "show 'Name not available' when the recipient has no name" in {
      val noName = defaultRecipient.copy(firstName = "", lastName = "")
      render(threadWith(recipient = noName)).select("h1").text() mustBe "Name not available"
    }

    "show the thread reference in the caption" in {
      view.select(".govuk-caption-l").text() mustBe s"Thread reference number: $threadId"
    }

    Seq(
      ThreadStatus.Draft    -> "HMRC has not sent this request yet.",
      ThreadStatus.Active   -> "HMRC is waiting for your reply.",
      ThreadStatus.Closed   -> "This thread is closed. You can still view it, but you cannot reply.",
      ThreadStatus.Archived -> "This thread has been archived."
    ).foreach { case (status, expected) =>
      s"show the correct inset text for a $status thread" in {
        val document = render(threadWith(status = status))
        document.select(".govuk-inset-text").text() mustBe expected
        document.select("[role=status]").text() mustBe expected
      }
    }

    "have a timeline heading and a single timeline event" in {
      view.select("h2.govuk-heading-m").text() mustBe "Timeline"
      view.select("ol.hmrc-timeline > li.hmrc-timeline__event").size() mustBe 1
    }

    "show the event date and time" in {
      val time = view.select("time.hmrc-timeline__event-meta")
      time.attr("datetime") mustBe "2026-06-14T10:15"
      time.text() mustBe "14 June 2026 at 10:15am"
    }

    "title the event 'File(s) sent and reply requested' when files are requested" in {
      view.select(".hmrc-timeline__event-title").text() mustBe "File(s) sent and reply requested by HMRC"
    }

    "title the event 'Message sent' when no files are requested" in {
      render(threadWith(requestedFiles = Seq.empty)).select(".hmrc-timeline__event-title").text() mustBe
        "Message sent by HMRC"
    }

    "show the requested files box" in {
      val box = view.select(".sdec-requested-files")
      box.size() mustBe 1

      val paragraphs = box.select("p")
      paragraphs.size() mustBe 2
      paragraphs.get(0).text() mustBe "You must provide the following by 27 October 2026"
      paragraphs.get(0).select("strong").text() mustBe "27 October 2026"
      paragraphs.get(1).text() mustBe
        "If you cannot provide any or all of the file(s) requested, press Reply and select the option which applies to you."

      val items = box.select("ul.govuk-list--bullet > li")
      items.size() mustBe 2
      items.get(0).text() mustBe "Payslip for June 2026"
      items.get(1).text() mustBe "Proof of address"
    }

    "not show the requested files box when no files are requested" in {
      render(threadWith(requestedFiles = Seq.empty)).select(".sdec-requested-files").size() mustBe 0
    }

    "show each paragraph of the message" in {
      val paragraphs = view.select(".hmrc-timeline__event-content > p")
      paragraphs.size() mustBe 2
      paragraphs.get(0).text() mustBe "First paragraph."
      paragraphs.get(1).text() mustBe "Second paragraph."
    }

    "show 'Message not available' when the message is blank" in {
      render(threadWith(message = "  ")).select(".hmrc-timeline__event-content > p").text() mustBe
        "Message not available"
    }

    "show the attachments with a link each" in {
      view.select(".hmrc-timeline__event-content h3").text() mustBe "Attachments"

      val links = view.select(".hmrc-timeline__event-content ul.govuk-list a.govuk-link")
      links.size() mustBe 1
      links.text() mustBe "Proof of Address Guidance Sheet.docx"
      links.attr("href") mustBe routes.DevelopmentInProgressController.onPageLoad().url
    }

    "not show the attachments section when there are no attachments" in {
      val document = render(threadWith(attachments = Seq.empty))
      document.select(".hmrc-timeline__event-content h3").size() mustBe 0
    }

    "escape HTML in attachment file names" in {
      val document = render(threadWith(attachments = Seq(Attachment("<b>evil</b>.docx"))))
      document.select(".hmrc-timeline__event-content a.govuk-link b").size() mustBe 0
      document.select(".hmrc-timeline__event-content a.govuk-link").text() mustBe "<b>evil</b>.docx"
    }

    "show the Reply button only when the thread is Active" in {
      val button = view.select(replyButton)
      button.size() mustBe 1
      button.text() mustBe "Reply"
      button.attr("href") mustBe routes.DevelopmentInProgressController.onPageLoad().url

      Seq(ThreadStatus.Draft, ThreadStatus.Closed, ThreadStatus.Archived).foreach { status =>
        render(threadWith(status = status)).select(replyButton).size() mustBe 0
      }
    }
  }
}
