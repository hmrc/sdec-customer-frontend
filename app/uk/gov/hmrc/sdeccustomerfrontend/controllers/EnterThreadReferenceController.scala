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

import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.sdeccustomerfrontend.controllers.actions.IdentifyExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.forms.ThreadReferenceFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.forms.models.ThreadReferenceForm
import uk.gov.hmrc.sdeccustomerfrontend.models.Mode
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.ThreadAccessResult
import uk.gov.hmrc.sdeccustomerfrontend.services.ThreadAccessServiceAlgebra
import uk.gov.hmrc.sdeccustomerfrontend.views.html.EnterThreadReferenceView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class EnterThreadReferenceController @Inject() (
  val controllerComponents:    MessagesControllerComponents,
  identifyExternalUser:        IdentifyExternalUser,
  threadReferenceFormProvider: ThreadReferenceFormProvider,
  threadAccessService:         ThreadAccessServiceAlgebra,
  enterThreadReferenceView:    EnterThreadReferenceView
)(using ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  private val form: Form[ThreadReferenceForm] = threadReferenceFormProvider()

  def onPageLoad(mode: Mode, threadReferenceForm: Form[ThreadReferenceForm] = form): Action[AnyContent] =
    identifyExternalUser { request =>
      given Request[AnyContent] = request
      Ok(enterThreadReferenceView(threadReferenceForm, mode))
    }

  def onContinue(mode: Mode): Action[AnyContent] =
    identifyExternalUser.async { request =>
      given Request[AnyContent] = request

      val boundForm = threadReferenceFormProvider().bindFromRequest()

      boundForm.fold(
        formWithErrors => Future.successful(BadRequest(enterThreadReferenceView(formWithErrors, mode))),
        threadReferenceForm =>
          threadAccessService
            .requestAccess(
              threadReference = threadReferenceForm.threadReference,
              user = request.externalUser
            )
            .map {
              case ThreadAccessResult.Found =>
                Redirect(routes.DevelopmentInProgressController.onPageLoad())
              case ThreadAccessResult.NotFound | ThreadAccessResult.Denied =>
                BadRequest(
                  enterThreadReferenceView(
                    boundForm.withError("thread-reference", "label.enter_a_valid_reference_number"),
                    mode
                  )
                )
              case ThreadAccessResult.Failed =>
                InternalServerError(
                  enterThreadReferenceView(
                    boundForm.withGlobalError("site.sorry_there_is_a_problem_with_the_service"),
                    mode
                  )
                )
            }
      )
    }

}
