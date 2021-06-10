package com.manoj.repo.entity

import java.util.UUID

case class CommentsEntity(uuid: UUID, postId: Int, name: String, email: String, body: String)
