package uk.gov.hmrc.sdeccustomerfrontend.forms

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.test.Injecting
import uk.gov.hmrc.sdeccustomerfrontend.models.ReplyOption
import uk.gov.hmrc.sdeccustomerfrontend.models.ReplyOptions

class ReplyOptionsFormProviderSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite with Injecting {

  private val fieldName            = "replyOption"
  private val requiredErrorMessage = "Select an option to reply"
  private val validReplyOptionAll:     ReplyOptions = ReplyOptions.All
  private val validReplyOptionPartial: ReplyOptions = ReplyOptions.Partial
  private val validReplyOptionNothing: ReplyOptions = ReplyOptions.Nothing

  private lazy val messages: Messages = inject[MessagesApi].preferred(Seq(Lang.defaultLang))

  private val replyOptionsForm:          Form[ReplyOption] = new ReplyOptionsFormProvider().apply()
  private def message(error: FormError): String            = messages(error.message)
  private def bind(value:    ReplyOptions) = replyOptionsForm.bind(Map(fieldName -> value.toString))

  "ReplyOptionsFormProvider" must {
    "successfully bind for valid Reply Options" in {
      bind(validReplyOptionAll).value mustBe Some(ReplyOption(validReplyOptionAll))
      bind(validReplyOptionPartial).value mustBe Some(ReplyOption(validReplyOptionPartial))
      bind(validReplyOptionNothing).value mustBe Some(ReplyOption(validReplyOptionNothing))
    }

    "fail to bind when nothing selected" in {
      replyOptionsForm.bind(Map.empty[String, String]).errors.map(message) mustBe Seq(requiredErrorMessage)
    }

  }
}
