package uk.gov.hmrc.sdeccustomerfrontend.forms

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.test.Injecting
import uk.gov.hmrc.sdeccustomerfrontend.models.CheckYourAnswers

class CheckYourAnswersFormProviderSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite with Injecting {

  private val fieldName            = "declarationCheck"
  private val requiredErrorMessage = "Select yes to make your declaration"

  private lazy val messages: Messages = inject[MessagesApi].preferred(Seq(Lang.defaultLang))

  private val checkYourAnswersForm = new CheckYourAnswersFormProvider().apply()
  private def message(error: FormError): String = messages(error.message)
  private def bind(value:    Boolean) = checkYourAnswersForm.bind(Map(fieldName -> value.toString))

  "CheckYourAnswersFormProviderSpec" must {

    "successfully bind when the declaration is ticked" in {
      val result = bind(true)
      result.errors mustBe empty
      result.value mustBe Some(CheckYourAnswers(declarationCheck = true))
    }

    "fail to bind when the declaration is not ticked" in {
      checkYourAnswersForm
        .bind(Map.empty[String, String])
        .errors
        .map(message) mustBe Seq(requiredErrorMessage)
    }

    "fail to bind when the declaration is false" in {
      bind(false).errors.map(message) mustBe Seq(requiredErrorMessage)
    }

  }

}
