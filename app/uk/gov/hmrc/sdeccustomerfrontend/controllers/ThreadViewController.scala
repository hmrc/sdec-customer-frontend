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

package uk.gov.hmrc.sdeccustomerfrontend.controllers

import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.sdeccustomerfrontend.controllers.actions.IdentifyExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.*
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ThreadView

import java.time.{LocalDate, LocalDateTime}
import javax.inject.Inject

class ThreadViewController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identifyExternalUser:     IdentifyExternalUser,
  threadView:               ThreadView
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(threadId: String): Action[AnyContent] = identifyExternalUser { request =>
    given Request[AnyContent] = request
    Ok(threadView(dummyThread(threadId)))
  }

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
