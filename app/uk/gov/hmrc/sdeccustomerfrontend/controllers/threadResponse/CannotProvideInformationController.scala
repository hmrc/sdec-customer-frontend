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

package uk.gov.hmrc.sdeccustomerfrontend.controllers.threadResponse

import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.sdeccustomerfrontend.controllers.actions.IdentifyExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.controllers.routes
import uk.gov.hmrc.sdeccustomerfrontend.forms.CannotProvideInformationFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.Mode
import uk.gov.hmrc.sdeccustomerfrontend.views.html.threadResponse.CannotProvideInformationView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class CannotProvideInformationController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identifyExternalUser:     IdentifyExternalUser,
  formProvider:             CannotProvideInformationFormProvider,
  view:                     CannotProvideInformationView
)(using ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  private val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] =
    identifyExternalUser { request =>
      given Request[AnyContent] = request

      Ok(view(form, mode))
    }

  def onContinue(mode: Mode): Action[AnyContent] =
    identifyExternalUser { request =>
      given Request[AnyContent] = request

      form
        .bindFromRequest()
        .fold(
          formWithErrors => BadRequest(view(formWithErrors, mode)),
          _ => Redirect(routes.DevelopmentInProgressController.onPageLoad())
        )
    }
}
