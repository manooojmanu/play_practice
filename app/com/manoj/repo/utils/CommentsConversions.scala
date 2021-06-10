package com.manoj.repo.utils

import com.manoj.models.Comment
import com.manoj.repo.entity.CommentsEntity

import java.util.UUID

object CommentsConversions {

  def toCommentsEntity(comment: Comment): CommentsEntity = {
    CommentsEntity(comment.uuid, comment.postId, comment.name, comment.email, comment.body)
  }

  def toComments(commentsEntity: CommentsEntity) = {
    Comment(commentsEntity.uuid, commentsEntity.postId, commentsEntity.name, commentsEntity.email, commentsEntity.body)
  }
}
