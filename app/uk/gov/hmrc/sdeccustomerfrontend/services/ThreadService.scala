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

package uk.gov.hmrc.sdeccustomerfrontend.services

import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.*

import java.time.{LocalDate, LocalDateTime}
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ThreadService @Inject() () extends ThreadServiceAlgebra {

  // TODO: replace with a call to the backend once the connector is wired in
  override def getThread(threadId: String)(using HeaderCarrier): Future[Thread] =
    Future.successful(dummyThread(threadId))

  private def dummyThread(threadId: String): Thread = {
    val sent = LocalDateTime.now().minusDays(5)

    Thread(
      id = threadId,
      status = ThreadStatus.Active,
      createdTimeStamp = sent,
      lastUpdatedTimeStamp = sent,
      threadExpiryDate = LocalDate.now().plusDays(28),
      associatedCaseReference = "CASE-001",
      recipientDetails = RecipientDetails(
        firstName = "John",
        lastName = "Doe",
        email = "johndoe@example.com",
        phoneNumber = "07123456789",
        nationalInsuranceNumber = "AA000000A",
        hasRelatedCase = false,
        caseReferenceNumber = None
      ),
      threadDetails = ThreadDetails(
        message = Seq(
          "Please provide your payslip for June 2026 and a proof of address.",
          "Proof of address can include a range of documents including a recent bank or building society " +
            "statement, utility bill, Council Tax bill, mortgage statement, P45 or P60, benefit entitlement " +
            "letter, pension or financial statement, bank account opening letter, or a driving licence " +
            "showing their current address.",
          "Any document provided should clearly show your full name and current address."
        ).mkString("\n"),
        responseDate = LocalDate.now().plusDays(14),
        requestedFiles = Seq("Payslip for June 2026", "Proof of address"),
        attachments = Seq(Attachment("Proof of Address Guidance Sheet.docx"))
      )
    )
  }
}
