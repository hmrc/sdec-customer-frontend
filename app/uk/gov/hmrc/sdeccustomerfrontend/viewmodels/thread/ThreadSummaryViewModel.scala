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

import play.api.i18n.Messages
import play.twirl.api.Html
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.{Thread, ThreadStatus}
import uk.gov.hmrc.sdeccustomerfrontend.utils.DateTimeFormats.{toDateAndTimeFormat, toDateFormat}

import java.time.LocalDateTime

object ThreadSummaryViewModel {

  private def fullName(firstName: String, lastName: String): String =
    s"$firstName $lastName"

  private def orUnavailable(value: String)(using messages: Messages): String =
    Option(value).map(_.trim).filter(_.nonEmpty).getOrElse(messages("label.name_not_available"))

  def formatDateTime(value: LocalDateTime): String = value.toDateAndTimeFormat

  def referenceOrUnavailable(thread: Thread)(using messages: Messages): String =
    orUnavailable(thread.id)

  def nameOrUnavailable(thread: Thread)(using messages: Messages): String =
    orUnavailable(fullName(thread.recipientDetails.firstName, thread.recipientDetails.lastName))

  def paragraphsOrUnavailable(thread: Thread)(using messages: Messages): Seq[String] = {
    val paragraphs = thread.threadDetails.message.split("\n").map(_.trim).filter(_.nonEmpty).toSeq
    if paragraphs.nonEmpty then paragraphs
    else Seq(messages("label.message_not_available"))
  }

  def insetKey(thread: Thread): String =
    thread.status match {
      case ThreadStatus.Draft    => "label.hmrc_has_not_sent_this_request_yet"
      case ThreadStatus.Active   => "label.hmrc_is_waiting_for_your_reply"
      case ThreadStatus.Closed   => "label.this_thread_is_closed_"
      case ThreadStatus.Archived => "label.this_thread_has_been_archived"
    }

  def canReply(thread: Thread): Boolean =
    thread.status == ThreadStatus.Active

  def messageTitleKey(thread: Thread): String =
    if thread.threadDetails.requestedFiles.nonEmpty then "label.files_sent_and_reply_requested"
    else "label.message_sent"

  def requestedFilesIntro(thread: Thread)(using messages: Messages): Html =
    Html(
      messages(
        "label.you_must_provide_the_following_by_",
        s"<strong>${thread.threadDetails.responseDate.toDateFormat}</strong>"
      )
    )
}
