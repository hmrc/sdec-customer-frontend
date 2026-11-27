package uk.gov.hmrc.sdeccustomerfrontend.controllers

import org.jsoup.Jsoup
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.sdeccustomerfrontend.base.ControllerSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.fixtures.ThreadFixtures.{threadId, threadWith}
import uk.gov.hmrc.sdeccustomerfrontend.forms.CheckYourAnswersFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.Thread
import uk.gov.hmrc.sdeccustomerfrontend.services.ThreadServiceAlgebra
import uk.gov.hmrc.sdeccustomerfrontend.views.html.{CheckYourAnswersView, ThreadView}

import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersControllerSpec extends ControllerSpecBase {

  val pageTitle = "Check your answers"

  private lazy val controller = new CheckYourAnswersController(
    messagesControllerComponents,
    fakeIdentifyExternalUser,
    new CheckYourAnswersFormProvider,
    inject[CheckYourAnswersView]
  )(using inject[ExecutionContext])

  private def getRequest = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad().url)

  "CheckYourAnswersController.onPageLoad" must {
    "return OK and render the Check your answers page" in {
      val result = controller.onPageLoad()(getRequest)
      status(result) mustBe OK

      val document = Jsoup.parse(contentAsString(result))
      document.select("h1").text() mustBe pageTitle
    }

    "show the 'Send reply' button" in {
      val document = Jsoup.parse(contentAsString(controller.onPageLoad()(getRequest)))
      val button = document.select(".govuk-main-wrapper button.govuk-button")
      button.size() mustBe 1
      button.text() mustBe "Send reply"
    }

  }

}
