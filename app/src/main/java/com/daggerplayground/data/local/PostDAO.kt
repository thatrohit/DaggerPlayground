package com.daggerplayground.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.daggerplayground.domain.Post

@Dao
interface PostDAO {
    @Query("select * from post")
    fun getPosts(): List<Post>

    @Insert
    fun insertPost(post: Post)

    @Insert
    fun insertPosts(posts: List<Post>)

    @Update
    fun updatePost(post: Post)

    @Update
    fun updatePosts(posts: List<Post>)

    @Query("delete from post")
    fun deletePosts()
}