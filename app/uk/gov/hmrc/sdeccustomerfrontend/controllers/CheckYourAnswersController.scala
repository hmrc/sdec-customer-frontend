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
import uk.gov.hmrc.sdeccustomerfrontend.forms.CheckYourAnswersFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.ReplyDetails
import uk.gov.hmrc.sdeccustomerfrontend.viewmodels.checkyouranswers.ReplyDetailsSummary
import uk.gov.hmrc.sdeccustomerfrontend.viewmodels.govuk.all.SummaryListViewModel
import uk.gov.hmrc.sdeccustomerfrontend.views.html.CheckYourAnswersView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersController @Inject() (
  val controllerComponents:     MessagesControllerComponents,
  identifyExternalUser:         IdentifyExternalUser,
  checkYourAnswersFormProvider: CheckYourAnswersFormProvider,
  checkYourAnswersView:         CheckYourAnswersView
)(using ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  private val checkYourAnswersForm = checkYourAnswersFormProvider()

  // TODO - this will be a function not a val and will grab from the sessionRepo
  private val replyDetails: ReplyDetails =
    ReplyDetails(
      replyOption = "I can provide all :)",
      supportingFiles = Some(
        Seq(
          ("hello", "hi"),
          ("foo", "bar")
        )
      ),
      message = "message"
    )

  def onPageLoad: Action[AnyContent] =
    identifyExternalUser { request =>
      given Request[AnyContent] = request
      Ok(
        checkYourAnswersView(
          checkYourAnswersForm,
          SummaryListViewModel(ReplyDetailsSummary.rows(replyDetails))
        )
      )
    }

  def onSubmit: Action[AnyContent] =
    identifyExternalUser.async { request =>
      given Request[AnyContent] = request
      checkYourAnswersForm
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(
              BadRequest(
                checkYourAnswersView(formWithErrors, SummaryListViewModel(ReplyDetailsSummary.rows(replyDetails)))
              )
            ),
          _ => Future.successful(Redirect(routes.DevelopmentInProgressController.onPageLoad()))
        )
    }

}
