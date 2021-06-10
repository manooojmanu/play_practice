package com.manoj.gatling

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GetCommentsSimulation extends Simulation {
  val theHttpProtocolBuilder = http.baseUrl("http://localhost:9000")

  val theScenarioBuilder = scenario("GetComments").during(50 seconds) {
    exec(
      http("Fetch Comments request")
        .get("/actor/comments")
        .check(status.is(200))

      /*
       * Check the response of this request. It should be a HTTP status 200.
       * Since the expected result is 200, the request will be verified as being OK
       * and the simulation will thus succeed.
       */

    )
  }

  setUp(
    theScenarioBuilder.inject(rampUsers(30).during(50.seconds))
  )

    /*
     * This asserts that, for all the requests in all the scenarios in the simulation
     * the maximum response time should be less than 50 ms.
     * If this is not the case when the simulation runs, the simulation will considered to have failed.
     */
    .assertions(
      global.responseTime.max.lt(1000),
      forAll.failedRequests.count.lt(5),
      details("Search Request").successfulRequests.percent.gt(90)
    )
    .protocols(theHttpProtocolBuilder)
}
