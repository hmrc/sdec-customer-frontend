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

package uk.gov.hmrc.sdeccustomerfrontend.base

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.*
import play.api.test.Injecting
import uk.gov.hmrc.sdeccustomerfrontend.controllers.actions.IdentifyExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.fixtures.ExternalUserFixtures
import uk.gov.hmrc.sdeccustomerfrontend.models.requests.ExternalUserRequest

import scala.concurrent.{ExecutionContext, Future}

trait ControllerSpecBase extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  protected lazy val messagesControllerComponents: MessagesControllerComponents =
    inject[MessagesControllerComponents]

  protected lazy val fakeIdentifyExternalUser: IdentifyExternalUser = {
    val bodyParser = inject[BodyParsers.Default]
    val ec         = inject[ExecutionContext]

    new IdentifyExternalUser {
      override def parser: BodyParser[AnyContent] = bodyParser

      override protected def executionContext: ExecutionContext = ec

      override def invokeBlock[A](
        request: Request[A],
        block:   ExternalUserRequest[A] => Future[Result]
      ): Future[Result] =
        block(ExternalUserRequest(request, ExternalUserFixtures.externalUser))
    }
  }
}
