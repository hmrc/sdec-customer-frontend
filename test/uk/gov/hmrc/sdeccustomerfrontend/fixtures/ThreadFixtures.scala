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

package uk.gov.hmrc.sdeccustomerfrontend.fixtures

import uk.gov.hmrc.sdeccustomerfrontend.models.thread.*

import java.time.{LocalDate, LocalDateTime}

object ThreadFixtures {

  val threadId: String = "KL897865432211"

  val sentAt:  LocalDateTime = LocalDateTime.of(2026, 6, 14, 18, 15)
  val dueDate: LocalDate     = LocalDate.of(2026, 10, 27)

  val defaultRecipient: RecipientDetails = RecipientDetails(
    firstName = "John",
    lastName = "Doe",
    email = "johndoe@example.com",
    phoneNumber = "07123456789",
    nationalInsuranceNumber = "AA000000A",
    hasRelatedCase = false,
    caseReferenceNumber = None
  )

  def threadWith(
    status:         ThreadStatus = ThreadStatus.Active,
    message:        String = "First paragraph.\nSecond paragraph.",
    requestedFiles: Seq[String] = Seq("Payslip for June 2026", "Proof of address"),
    attachments:    Seq[Attachment] = Seq(Attachment("Proof of Address Guidance Sheet.docx")),
    recipient:      RecipientDetails = defaultRecipient
  ): Thread =
    Thread(
      id = threadId,
      status = status,
      createdTimeStamp = sentAt,
      lastUpdatedTimeStamp = sentAt,
      threadExpiryDate = LocalDate.of(2026, 12, 12),
      associatedCaseReference = "CASE-001",
      recipientDetails = recipient,
      threadDetails = ThreadDetails(
        message = message,
        responseDate = dueDate,
        requestedFiles = requestedFiles,
        attachments = attachments
      )
    )
}
