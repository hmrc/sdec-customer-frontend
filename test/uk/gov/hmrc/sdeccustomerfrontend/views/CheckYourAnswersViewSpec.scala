package uk.gov.hmrc.sdeccustomerfrontend.views

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import uk.gov.hmrc.sdeccustomerfrontend.base.ViewSpecBase
import uk.gov.hmrc.sdeccustomerfrontend.forms.CheckYourAnswersFormProvider
import uk.gov.hmrc.sdeccustomerfrontend.models.ReplyDetails
import uk.gov.hmrc.sdeccustomerfrontend.viewmodels.checkyouranswers.ReplyDetailsSummary
import uk.gov.hmrc.sdeccustomerfrontend.viewmodels.govuk.all.SummaryListViewModel
import uk.gov.hmrc.sdeccustomerfrontend.views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewSpecBase {

  private val replyDetailsNoFiles: ReplyDetails =
    ReplyDetails(
      replyOption = "No, I cannot provide any of the information",
      message = "Example message"
    )

  private val replyDetailsFiles: ReplyDetails =
    ReplyDetails(
      replyOption = "Yes, I can provide all the files",
      supportingFiles = Some(
        Seq("hello", "foo")
      ),
      message = "Example message"
    )

  private lazy val formProvider = app.injector.instanceOf[CheckYourAnswersFormProvider]
  private lazy val page: CheckYourAnswersView = inject[CheckYourAnswersView]

  private lazy val view = Jsoup.parse(
    page(
      formProvider(),
      SummaryListViewModel(ReplyDetailsSummary.rows(replyDetailsNoFiles))
    )(request, messages).toString()
  )

  private lazy val viewWithFiles = Jsoup.parse(
    page(
      formProvider(),
      SummaryListViewModel(ReplyDetailsSummary.rows(replyDetailsFiles))
    )(request, messages).toString()
  )

  private lazy val viewWithErrors = Jsoup.parse(
    page(
      formProvider().bind(Map.empty[String, String]),
      SummaryListViewModel(ReplyDetailsSummary.rows(replyDetailsFiles))
    )(request, messages).toString()
  )

  val pageTitle   = "Check your answers"
  val serviceName = "Share Files Securely with HMRC"

  "CheckYourAnswersView" must {
    "have the correct title" in {
      view.title() mustBe s"$pageTitle - $serviceName - GOV.UK"
    }

    "have a unique H1 heading, matching the page title" in {
      val h1Elements = view.getElementsByTag("h1")
      h1Elements.size() mustBe 1
      h1Elements.text() mustBe pageTitle
    }

    "have the correct paragraphs" in {
      val paragraphs: Elements = view.getElementsByTag("p")
      paragraphs.size() mustBe 3
      paragraphs
        .get(0)
        .text() mustBe "Use this section to make any changes to your reply before sending it."
      paragraphs
        .get(1)
        .text() mustBe "By sending this reply, you confirm that the information you have given is complete and correct to the best of your knowledge and belief."
      paragraphs
        .get(2)
        .text() mustBe "If you make a false statement then you may:"
    }

    "show the 'Reply details' H2" in {
      view.select("#main-content h2").get(0).text() mustBe "Reply details"
    }

    "show the 'Declaration' heading" in {
      view.select("#main-content form h2").text() mustBe "Declaration"
    }

    "show 2 summary rows when no files are provided" in {
      val rows = view.select("#main-content .govuk-summary-list__row")
      rows.size() mustBe 2
      rows.get(0).select(".govuk-summary-list__key").text() mustBe "File(s) provided"
      rows.get(1).select(".govuk-summary-list__key").text() mustBe "Your message to HMRC"
    }

    "show the supporting files rows when files are provided" in {
      val rows = viewWithFiles.select("#main-content .govuk-summary-list__row")
      rows.size() mustBe 3
      rows.get(1).select("li").eachText() mustBe java.util.List.of("hello", "foo")
    }

    "show a single unchecked declaration checkbox" in {
      val checkbox = view.getElementById("declarationCheck")
      checkbox.attr("type") mustBe "checkbox"
      checkbox.attr("name") mustBe "declarationCheck"
      checkbox.hasAttr("checked") mustBe false

      view.select("#main-content .govuk-checkboxes__label").text() mustBe
        "I agree the information I have sent is complete and correct to the best of my knowledge and belief."
    }

    "show the error summary and inline error when the declaration is submitted unchecked" in {
      val summary = viewWithErrors.select(".govuk-error-summary")
      summary.size() mustBe 1
      summary.select(".govuk-error-summary__title").text() mustBe "There is a problem"
      summary.select("a").text() mustBe "Select yes to make your declaration"
      summary.select("a").attr("href") mustBe "#declarationCheck"

      viewWithErrors.select(".govuk-form-group--error .govuk-error-message").first().ownText() mustBe
        "Select yes to make your declaration"
    }

  }

}
