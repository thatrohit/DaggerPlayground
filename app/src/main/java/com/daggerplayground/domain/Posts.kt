package com.daggerplayground.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey
    var id: Int?,
    var body: String?,
    var title: String?,
    var userId: Int?,
    var isLiked: Boolean?
)