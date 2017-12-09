import io.circe.Json
import io.circe.parser._
import io.circe.optics.JsonPath._
import io.circe.syntax._
import io.circe._
case class NotificationContainer(userId: Long, productId: Option[Long] , content: String)

sealed trait Notification{
  def userId : Long
  def productIdToStore : Option[Long]
}

case class Like(userId: Long, productId: Long, timestamp: Long) extends Notification {
  val productIdToStore = Option(productId)
}

case class Comment(userId: Long, timestamp: Long, comment: String, receiverId: Option[Long], productId: Option[Long]) extends Notification{
  val productIdToStore = productId
}

object NotificationTransformer {
  def extractEventTypeAndDataFromJson(s : String): (String, Json) = {
    val asJson = parse(s).getOrElse(Json.Null)
    val eventType = root.eventType.string.getOption(asJson).getOrElse(throw new RuntimeException("malformed"))
    val data = root.data.json.getOption(asJson).getOrElse(Json.Null)
    (eventType, data)
  }

  def toNotificationContainer[A <: Notification](eventType : String, notification : A)(implicit encoder : ObjectEncoder[A]) : NotificationContainer = {
    // converting from json object to json doesn't preserve order of fields
    val content = notification.asJsonObject.add("eventType", eventType.asJson).asJson.toString()
    NotificationContainer(notification.userId, notification.productIdToStore, content)
  }
}