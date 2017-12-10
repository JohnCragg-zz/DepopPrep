import CircePlayTestData.{likeEventType, productId, timestamp, userId}
import org.scalatest.{FunSuite, Matchers}
import io.circe.Json
import io.circe.syntax._
import io.circe.parser._
import cats.syntax.either._
import io.circe._, io.circe.parser._

class UsingCursorsTest extends FunSuite with Matchers {
  test("can surface all the information from the data object and remove unwanted fields") {
    UsingCursors.transformJson(input) shouldBe expected
  }

  val input =
    s"""{
       |"eventType":"$likeEventType",
       |"something":"else",
       |"data":{
       |         "userId":$userId,
       |         "productId":$productId,
       |         "timestamp":$timestamp
       |         }
       |}
      """.stripMargin

  val expected =
    s"""{
       |"eventType":"$likeEventType",
       |"userId":$userId,
       |"productId":$productId,
       |"timestamp":$timestamp
       |}
     """.stripMargin
}
