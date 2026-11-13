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

package uk.gov.hmrc.sdeccustomerfrontend.repositories

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.Configuration
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.sdeccustomerfrontend.config.FrontendAppConfig
import uk.gov.hmrc.sdeccustomerfrontend.models.UserAnswers

import java.time.{Clock, Instant, ZoneOffset}
import scala.concurrent.ExecutionContext.Implicits.global

class SessionRepositorySpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with DefaultPlayMongoRepositorySupport[UserAnswers] {

  private val clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

  override protected val databaseName: String = "test-sdec-customer-frontend-SessionRepositorySpec"

  private val configuration = Configuration("mongodb.timeToLiveInSeconds" -> 900)

  override protected val repository: SessionRepository =
    SessionRepository(mongoComponent, FrontendAppConfig(configuration, ServicesConfig(configuration)), clock)

  "SessionRepository" should {
    "save and read back answers" in {
      repository.set(UserAnswers("id1")).futureValue
      repository.get("id1").futureValue.map(_.id) shouldBe Some("id1")
    }

    "clear answers" in {
      repository.set(UserAnswers("id2")).futureValue
      repository.clear("id2").futureValue
      repository.get("id2").futureValue shouldBe None
    }
  }
}
