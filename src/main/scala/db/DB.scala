package db

import scalikejdbc._
import org.joda.time._


object DB extends App {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

  implicit val session = AutoSession

  val createTable = {
    println("EXECUTED")
    sql"""
       create table members (
       id serial not null primary key,
       name varchar(64),
       created_at timestamp not null
       )
     """.execute.apply()

  }

  val populateDB = Seq("Alice", "Bob", "Chris") foreach {
    name =>
      sql"insert into members (name, created_at) values (${name}, current_timestamp)".update().apply()
  }


  val entities: List[Map[String, Any]] = sql"select * from members".map(_.toMap).list.apply()

  case class Member(id: Long, name: Option[String], createdAt: DateTime)

  object Member extends SQLSyntaxSupport[Member] {
    override val tableName = "members"

    def apply(rs: WrappedResultSet): Member = new Member(
      rs.long("id"), rs.stringOpt("name"), rs.jodaDateTime("created_at")
    )
  }

  val members = sql"select * from members".map(rs => Member(rs)).list.apply()


  createTable
  populateDB
  entities.foreach(println)
  members.foreach(println)
  session.close()
}
