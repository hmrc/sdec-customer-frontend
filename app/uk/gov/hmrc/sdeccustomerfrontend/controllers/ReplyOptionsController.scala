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
import play.api.mvc.*
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.sdeccustomerfrontend.controllers.actions.IdentifyExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.forms.ReplyOptionsFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.ReplyOption
import uk.gov.hmrc.sdeccustomerfrontend.views.html.ReplyOptionsView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReplyOptionsController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identifyExternalUser:     IdentifyExternalUser,
  replyOptionsFormProvider: ReplyOptionsFormProvider,
  replyOptionsView:         ReplyOptionsView
)(using ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  private val replyOptionsForm: Form[ReplyOption] = replyOptionsFormProvider()

  def onPageLoad: Action[AnyContent] =
    identifyExternalUser { request =>
      given Request[AnyContent] = request
      Ok(replyOptionsView(replyOptionsForm))
    }

  def onSubmit: Action[AnyContent] =
    identifyExternalUser.async { request =>
      logger.info(s"Handling Reply Option Submission")
      given Request[AnyContent] = request
      replyOptionsForm
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(
              BadRequest(replyOptionsView(formWithErrors))
            ),
          _ => Future.successful(Redirect(routes.DevelopmentInProgressController.onPageLoad()))
        )
    }

}
