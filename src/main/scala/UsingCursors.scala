import io.circe.Json
import io.circe.syntax._
import io.circe.parser._
import cats.syntax.either._
import io.circe._, io.circe.parser._

object UsingCursors {


def transformJson(s: String ) = {
  val json = parse(s).getOrElse(throw new RuntimeException("parsing error"))
  val cursor = json.hcursor

  val res: Option[Json] = {
    for {
      d <- cursor.downField("data").focus
      et <- cursor.downField("eventType").focus
      dataObj <- d.asObject
      content = dataObj.add("eventType", et)
    } yield content.asJson
  }
  res.getOrElse(throw new RuntimeException("malformed data")).toString()
}


//  val json: Json = parse(input).getOrElse(throw new RuntimeException("parsing error"))
//  val cursor = json.hcursor
//  val data = cursor.downField("data").focus.get.asObject.get
//  val eventType : Json = cursor.downField("eventType").focus.get
//
//  data.add("eventType", eventType).asJson

}
