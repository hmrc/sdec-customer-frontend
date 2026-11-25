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

package uk.gov.hmrc.sdeccustomerfrontend.utils

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.sdeccustomerfrontend.utils.DateTimeFormats.{toDateAndTime12hFormat, toDateAndTimeFormat, toDateFormat}

import java.time.{LocalDate, LocalDateTime}

class DateTimeFormatsSpec extends AnyWordSpec with Matchers {

  "toDateFormat" must {
    "format as 'd MMMM yyyy'" in {
      LocalDate.of(2011, 12, 3).toDateFormat mustBe "3 December 2011"
    }

    "not zero-pad the day" in {
      LocalDate.of(2026, 6, 5).toDateFormat mustBe "5 June 2026"
    }
  }

  "toDateAndTime12Format" must {
    "format as 'd MMMM yyyy at h:mm am/pm'" in {
      LocalDateTime.of(2011, 12, 3, 12, 42).toDateAndTime12hFormat mustBe "3 December 2011 at 12:42pm"
    }

    "not zero-pad the hour" in {
      LocalDateTime.of(2026, 6, 5, 9, 30).toDateAndTime12hFormat mustBe "5 June 2026 at 9:30am"
    }

    "show just after midnight as 12am" in {
      LocalDateTime.of(2026, 6, 14, 0, 5).toDateAndTime12hFormat mustBe "14 June 2026 at 12:05am"
    }
  }

  "toDateAndTimeFormat (24h format)" must {
    "format as 'd MMMM yyyy at H:mm'" in {
      LocalDateTime.of(2011, 12, 23, 21, 42).toDateAndTimeFormat mustBe "23 December 2011 at 21:42"
    }

    "not zero-pad the hour" in {
      LocalDateTime.of(2026, 11, 28, 9, 30).toDateAndTimeFormat mustBe "28 November 2026 at 9:30"
    }

    "show just after midnight as 0:0x" in {
      LocalDateTime.of(2026, 6, 27, 0, 5).toDateAndTimeFormat mustBe "27 June 2026 at 0:05"
    }
  }
}
