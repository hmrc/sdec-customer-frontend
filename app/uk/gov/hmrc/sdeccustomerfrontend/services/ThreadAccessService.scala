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

import play.api.Logging
import play.api.http.Status.{FORBIDDEN, NOT_FOUND}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.sdeccustomerfrontend.connectors.ThreadAccessConnector
import uk.gov.hmrc.sdeccustomerfrontend.models.ExternalUser
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.{ThreadAccessRequest, ThreadAccessResult}
import uk.gov.hmrc.sdeccustomerfrontend.services.ThreadAccessServiceAlgebra

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ThreadAccessService @Inject() (
  connector: ThreadAccessConnector
)(using ec: ExecutionContext)
    extends ThreadAccessServiceAlgebra
    with Logging {

  override def requestAccess(
    threadReference: String,
    user:            ExternalUser
  )(using HeaderCarrier): Future[ThreadAccessResult] =
    connector
      .requestAccess(ThreadAccessRequest.from(threadReference, user))
      .map {
        case Right(_)    => ThreadAccessResult.Found
        case Left(error) =>
          error.statusCode match {
            case NOT_FOUND => ThreadAccessResult.NotFound
            case FORBIDDEN => ThreadAccessResult.Denied
            case _         => ThreadAccessResult.Failed
          }
      }
      .recover { case ex =>
        logger.error("Thread access request failed", ex)
        ThreadAccessResult.Failed
      }
}

object ThreadAccessService {
  val ConfidenceLevelThreshold: Int = 250
}
