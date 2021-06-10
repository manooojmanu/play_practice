package com.manoj.client

import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SubscriberClient @Inject() (ws: WSClient) {

  private val eventJson = Json.parse(
    """
      |{
      |  "value" : [ {
      |    "subscriptionId" : "b4e80e97-bff1-427a-96f9-b064d626ff99",
      |    "clientState" : "SecretClientStat",
      |    "changeType" : "updated",
      |    "resource" : "communications/presences?$filter=id+in+(%27ec12b2fe-4f61-42cf-8b4a-d42174e137db%27%2c%272ffaaf66-4421-4776-a294-c48790fc3c3a%27)",
      |    "subscriptionExpirationDateTime" : "2021-06-04T04:52:52.1843119-07:00",
      |    "resourceData" : {
      |      "@odata.type" : "#Microsoft.Graph.presence",
      |      "@odata.id" : "communications/presences?$filter=id+in+(%27ec12b2fe-4f61-42cf-8b4a-d42174e137db%27%2c%272ffaaf66-4421-4776-a294-c48790fc3c3a%27)",
      |      "id" : "ec12b2fe-4f61-42cf-8b4a-d42174e137db",
      |      "activity" : "Offline",
      |      "availability" : "Offline"
      |    },
      |    "tenantId" : "aeeb40cd-4a4e-4666-ad36-544f07ed8939"
      |  } ]
      |}
      |""".stripMargin
  )

  def sendEvent(): Future[String] = {

    ws.url("http://localhost:9000/home")
      .addHttpHeaders("Accept" -> "application/json")
      .post(eventJson)
      .map(s => s.body[String])
  }
}
