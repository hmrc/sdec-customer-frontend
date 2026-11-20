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

package uk.gov.hmrc.sdeccustomerfrontend.services

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.ThreadStatus

import java.time.{LocalDate, LocalDateTime}

class ThreadServiceSpec extends AnyWordSpec with Matchers with ScalaFutures {

  private given HeaderCarrier = HeaderCarrier()

  private val service = new ThreadService

  "ThreadService.getThread" must {
    "return a thread with the requested id" in {
      service.getThread("KL897865432211").futureValue.id mustBe "KL897865432211"
    }

    "return an Active thread" in {
      service.getThread("KL897865432211").futureValue.status mustBe ThreadStatus.Active
    }

    "return a message that was sent in the past with a response date in the future" in {
      val thread = service.getThread("KL897865432211").futureValue

      thread.createdTimeStamp.isBefore(LocalDateTime.now()) mustBe true
      thread.threadDetails.responseDate.isAfter(LocalDate.now()) mustBe true
    }

    "return requested files, a message and an attachment" in {
      val details = service.getThread("KL897865432211").futureValue.threadDetails

      details.requestedFiles must not be empty
      details.message.trim   must not be empty
      details.attachments    must not be empty
    }
  }
}
