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

package uk.gov.hmrc.sdeccustomerfrontend.viewmodels.checkyouranswers

import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{SummaryListRow, Value}
import uk.gov.hmrc.sdeccustomerfrontend.controllers.routes as appRoutes
import uk.gov.hmrc.sdeccustomerfrontend.viewmodels.govuk.all.*

object SummaryRow {

  def apply(labelKey: String, value: String)(using messages: Messages): SummaryListRow =
    build(labelKey, ValueViewModel(Text(value)))

  def preservingLineBreaks(labelKey: String, value: String)(using messages: Messages): SummaryListRow =
    build(
      labelKey,
      ValueViewModel(
        HtmlContent(
          HtmlFormat.raw(
            s"""<span class="sdec-preserve-line-breaks">${HtmlFormat.escape(value).body}</span>"""
          )
        )
      )
    )

  def numberedList(labelKey: String, items: Seq[(String, String)])(using messages: Messages): SummaryListRow =
    val listItems = items.map { (name, url) =>
      s"""<li><a class="govuk-link" href="${HtmlFormat.escape(url).body}">${HtmlFormat.escape(name).body}</a></li>"""
    }.mkString

    build(
      labelKey,
      ValueViewModel(
        HtmlContent(
          HtmlFormat.raw(s"""<ol class="gouk-list govuk-list--number">$listItems</ol>""")
        )
      )
    )

  private def build(labelKey: String, value: Value)(using messages: Messages): SummaryListRow =
    SummaryListRowViewModel(
      key = KeyViewModel(Text(messages(labelKey))),
      value = value,
      actions = Seq(
        ActionItemViewModel(Text(messages("label.change")), appRoutes.DevelopmentInProgressController.onPageLoad().url)
          .withVisuallyHiddenText(messages(labelKey))
      )
    )

}
