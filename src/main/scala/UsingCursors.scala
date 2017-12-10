import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

object UsingCursors {


  def transformJson(s: String) = {
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
}
