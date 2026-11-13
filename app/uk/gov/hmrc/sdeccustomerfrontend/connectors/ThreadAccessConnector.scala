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

package uk.gov.hmrc.sdeccustomerfrontend.connectors

import play.api.libs.json.Json
import play.api.libs.ws.JsonBodyWritables.*
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps, UpstreamErrorResponse}
import uk.gov.hmrc.sdeccustomerfrontend.config.FrontendAppConfig
import uk.gov.hmrc.sdeccustomerfrontend.models.thread.ThreadAccessRequest

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ThreadAccessConnector @Inject() (
  httpClient: HttpClientV2,
  appConfig:  FrontendAppConfig
)(using ec: ExecutionContext) {

  def requestAccess(
    request: ThreadAccessRequest
  )(using HeaderCarrier): Future[Either[UpstreamErrorResponse, HttpResponse]] =
    httpClient
      .post(url"${appConfig.sdecCustomerApiUrl}/thread-access")
      .withBody(Json.toJson(request))
      .execute[Either[UpstreamErrorResponse, HttpResponse]]

}
