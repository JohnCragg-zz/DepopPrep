import java.time.Instant

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.parser._
import org.scalatest.{FunSuite, Matchers}
import io.circe.optics.JsonPath._

class CircePlayTest extends FunSuite with Matchers {

  import CircePlayTestData._


  test("Given a json representation of notification of any type," +
    "I can serialize the json into a tuple of eventType and a case class") {

    implicit val likeDecoder: Decoder[Like] = deriveDecoder
    implicit val commentDecoder: Decoder[Comment] = deriveDecoder
    val (actualLikeEventType, actualLike) = testAnyNotification[Like](rawLike)
    val (actualCommentEventType, actualComment) = testAnyNotification[Comment](rawComment)
    actualLike shouldBe aLike
    actualLikeEventType shouldBe likeEventType
    actualComment shouldBe aComment
    actualCommentEventType shouldBe commentEventType
  }

  test("Given a notification of any type I can transform the json into a notification container" +
    "so that I can store it into the database in a generic way") {
    implicit val likeEncoder: ObjectEncoder[Like] = deriveEncoder
    implicit val commentEncoder: ObjectEncoder[Comment] = deriveEncoder
    NotificationTransformer.toNotificationContainer(likeEventType, aLike) shouldEqual  NotificationContainer(userId, Option(productId), likeDatabaseContent)
    NotificationTransformer.toNotificationContainer(commentEventType, aComment) shouldEqual NotificationContainer(userId, Option(productId), commentDatabaseContent)
  }

  def testAnyNotification[A <: Notification](raw: String)(implicit decoder: Decoder[A]) = {
    val (eventType, event) = NotificationTransformer.extractEventTypeAndDataFromJson(raw)
    val toEvent = event.as[A]
    (eventType, toEvent.getOrElse(""))
  }


}

object CircePlayTestData {

  val likeEventType = "like"
  val commentEventType = "comment"
  val commentContent = "aComment"
  val userId = 1
  val productId = 2
  val receiverId = 3
  val timestamp = Instant.now.toEpochMilli
  val rawLike =
    s"""{
       |"eventType":"$likeEventType",
       |"data":{
       |         "userId":$userId,
       |         "productId":$productId,
       |         "timestamp":$timestamp
       |         }
       |}
      """.stripMargin

  val rawComment =
    s"""{
       |"eventType":"$commentEventType",
       |"data":{
       |        "userId":$userId,
       |         "productId":$productId,
       |         "receiverId":$receiverId,
       |         "comment":"$commentContent",
       |         "timestamp":$timestamp
       |         }
       | }
     """.stripMargin

  val likeDatabaseContent =
    s"""{
       |"userId":$userId,
       |"productId":$productId,
       |"timestamp":$timestamp,
       |"eventType":"$likeEventType"
       |}""".stripMargin

  val commentDatabaseContent =
    s"""{
       |"userId":$userId,
       |"productId":$productId,
       |"receiverId":$receiverId,
       |"comment":"$commentContent",
       |"timestamp":$timestamp,
       |"eventType":$commentEventType"
       |}""".stripMargin

  val aLike = Like(userId, productId, timestamp)
  val aComment = Comment(userId, timestamp, commentContent, Option(receiverId), Option(productId))

}
