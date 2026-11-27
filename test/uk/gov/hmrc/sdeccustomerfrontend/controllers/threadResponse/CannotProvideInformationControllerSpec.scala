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

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.*
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.auth.core.{ConfidenceLevel, Enrolments}
import uk.gov.hmrc.sdeccustomerfrontend.controllers.actions.IdentifyExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.controllers.routes
import uk.gov.hmrc.sdeccustomerfrontend.forms.CannotProvideInformationFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.*
import uk.gov.hmrc.sdeccustomerfrontend.models.requests.ExternalUserRequest
import uk.gov.hmrc.sdeccustomerfrontend.views.html.threadResponse.CannotProvideInformationView

import scala.concurrent.{ExecutionContext, Future}

class CannotProvideInformationControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  given ExecutionContext = scala.concurrent.ExecutionContext.global

  private val fakeIdentifyExternalUser = new IdentifyExternalUser {

    override def parser: BodyParser[AnyContent] = stubBodyParser()

    override protected def executionContext: ExecutionContext = summon[ExecutionContext]

    override def invokeBlock[A](
      request: Request[A],
      block:   ExternalUserRequest[A] => Future[Result]
    ): Future[Result] = {

      val fakeUser = ExternalUser(
        id = CustomerIdentifier(
          IdentityProvider.GovernmentGateway,
          "test-user"
        ),
        name = None,
        email = None,
        nino = None,
        enrolments = Enrolments(Set.empty),
        affinityGroup = None,
        confidenceLevel = ConfidenceLevel.L50
      )

      block(
        ExternalUserRequest(
          request,
          fakeUser
        )
      )
    }
  }

  private val controllerComponents = stubMessagesControllerComponents()

  private val view = app.injector.instanceOf[CannotProvideInformationView]

  private val controller =
    new CannotProvideInformationController(
      controllerComponents,
      fakeIdentifyExternalUser,
      new CannotProvideInformationFormProvider(),
      view
    )

  "onPageLoad" should {
    "return 200" in {
      val request = FakeRequest(GET, "/cannot-provide-information")

      val result = controller.onPageLoad(NormalMode)(request)

      status(result) mustBe OK
    }

    "return HTML" in {

      val request = FakeRequest(GET, "/cannot-provide-information")

      val result = controller.onPageLoad(NormalMode)(request)

      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }
  }

  "onContinue" should {

    "return BAD_REQUEST when the form is invalid" in {

      val request = FakeRequest(POST, "/cannot-provide-information")
        .withFormUrlEncodedBody()

      val result = controller.onContinue(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Development In Progress when the form is valid" in {

      val request = FakeRequest(POST, "/cannot-provide-information")
        .withFormUrlEncodedBody("message" -> "I cannot provide the requested files.")

      val result = controller.onContinue(NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(
        routes.DevelopmentInProgressController
          .onPageLoad()
          .url
      )
    }
  }
}
