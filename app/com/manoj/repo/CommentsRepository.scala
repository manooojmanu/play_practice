package com.manoj.repo

import com.manoj.models.Comment
import com.manoj.repo.entity.CommentsEntity
import com.manoj.repo.utils.CommentsConversions._
import com.typesafe.scalalogging.LazyLogging
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.ast.BaseTypedType
import slick.jdbc.{ JdbcProfile, JdbcType }
import slick.jdbc.MySQLProfile.api._
import java.util.UUID

import javax.inject.Inject

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CommentsRepository @Inject() (val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  implicit val uuidColumnType: JdbcType[UUID] with BaseTypedType[UUID] =
    MappedColumnType.base(_.toString, UUID.fromString)

  class CommentsTable(tag: Tag) extends Table[CommentsEntity](tag, "comments") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def postId: Rep[Int] = column[Int]("postId")
    def uuid: Rep[UUID] = column[UUID]("uuid")
    def name: Rep[String] = column[String]("name")
    def email: Rep[String] = column[String]("email")
    def body: Rep[String] = column[String]("body")

    override def * = (uuid, postId, name, email, body) <> (CommentsEntity.tupled, CommentsEntity.unapply)
  }
  val comments = TableQuery[CommentsTable]

  var commentsList: ListBuffer[Comment] = ListBuffer()

  def save(comment: Comment): Future[Int] = {
    val commentsEntity = toCommentsEntity(comment)
    db.run(comments += commentsEntity)
  }

  def bulkSave(list: List[Comment]): Future[Int] = {
    val grouped = list.grouped(100)
    Future
      .sequence(grouped.map { list =>
//        logger.info("saving comments to database ---------------------------")
        val entities = list.map(toCommentsEntity)
        db.run(comments ++= entities)
      })
      .map(_.flatten.sum)
  }

  def list(): Future[Seq[Comment]] = {
    db.run(comments.result).map { s => s.map(toComments) }
  }

  def callDBForFetch(): Future[Seq[Comment]] = {
    logger.info("Calling db for list comments ---------------------------")
    db.run(comments.result).map { s =>
      val res = s.map(toComments)
      commentsList ++= res
      res
    }
  }

  def byUUID(uuid: UUID): Future[Comment] = {
    db.run(comments.filter(_.uuid === uuid).result.headOption).map {
      case Some(value) => toComments(value)
      case None =>
        logger.error(s"No comment fount for the uuid :$uuid")
        throw new NoSuchElementException(s"No comment fount for the uuid :$uuid")
    }
  }

}
