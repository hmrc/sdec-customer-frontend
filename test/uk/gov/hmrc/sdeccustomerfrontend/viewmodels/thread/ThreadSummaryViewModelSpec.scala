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

package uk.gov.hmrc.sdeccustomerfrontend.viewmodels.thread

import uk.gov.hmrc.sdeccustomerfrontend.base.ViewSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.fixtures.ThreadFixtures.*
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.ThreadStatus

class ThreadSummaryViewModelSpec extends ViewSpecBase {

  "referenceOrUnavailable" must {
    "return the thread id" in {
      ThreadSummaryViewModel.referenceOrUnavailable(threadWith()) mustBe threadId
    }
  }

  "nameOrUnavailable" must {
    "return the full name" in {
      ThreadSummaryViewModel.nameOrUnavailable(threadWith()) mustBe "John Doe"
    }

    "return only the first name when there is no last name" in {
      val thread = threadWith(recipient = defaultRecipient.copy(lastName = ""))
      ThreadSummaryViewModel.nameOrUnavailable(thread) mustBe "John"
    }

    "return 'Name not available' when both names are blank" in {
      val thread = threadWith(recipient = defaultRecipient.copy(firstName = " ", lastName = ""))
      ThreadSummaryViewModel.nameOrUnavailable(thread) mustBe "Name not available"
    }
  }

  "paragraphsOrUnavailable" must {
    "split the message on new lines" in {
      ThreadSummaryViewModel.paragraphsOrUnavailable(threadWith(message = "One\nTwo")) mustBe Seq("One", "Two")
    }

    "trim paragraphs and drop blank lines" in {
      val thread = threadWith(message = "  One  \n\n Two \r\n")
      ThreadSummaryViewModel.paragraphsOrUnavailable(thread) mustBe Seq("One", "Two")
    }

    "return 'Message not available' when the message is blank" in {
      ThreadSummaryViewModel.paragraphsOrUnavailable(threadWith(message = " \n ")) mustBe
        Seq("Message not available")
    }
  }

  "insetKey" must {
    "map each status to its message key" in
      Seq(
        ThreadStatus.Draft    -> "label.hmrc_has_not_sent_this_request_yet",
        ThreadStatus.Active   -> "label.hmrc_is_waiting_for_your_reply",
        ThreadStatus.Closed   -> "label.this_thread_is_closed_",
        ThreadStatus.Archived -> "label.this_thread_has_been_archived"
      ).foreach { case (status, key) =>
        ThreadSummaryViewModel.insetKey(threadWith(status = status)) mustBe key
      }
  }

  "canReply" must {
    "be true only for an Active thread" in {
      ThreadSummaryViewModel.canReply(threadWith(status = ThreadStatus.Active)) mustBe true

      Seq(ThreadStatus.Draft, ThreadStatus.Closed, ThreadStatus.Archived).foreach { status =>
        ThreadSummaryViewModel.canReply(threadWith(status = status)) mustBe false
      }
    }
  }

  "messageTitleKey" must {
    "be 'files sent and reply requested' when files are requested" in {
      ThreadSummaryViewModel.messageTitleKey(threadWith()) mustBe "label.files_sent_and_reply_requested"
    }

    "be 'message sent' when no files are requested" in {
      ThreadSummaryViewModel.messageTitleKey(threadWith(requestedFiles = Seq.empty)) mustBe "label.message_sent"
    }
  }

  "formatDateTime" must {
    "format as 'd MMMM yyyy at H:mm'" in {
      ThreadSummaryViewModel.formatDateTime(sentAt) mustBe "14 June 2026 at 18:15"
    }
  }

  "requestedFilesIntro" must {
    "embed the due date in bold" in {
      ThreadSummaryViewModel.requestedFilesIntro(threadWith()).toString mustBe
        "You must provide the following by <strong>27 October 2026</strong>"
    }
  }
}
