package com.daggerplayground.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daggerplayground.domain.Post

@Database(entities = [Post::class], version = 1)
abstract class PostDb: RoomDatabase() {
    abstract fun postDao(): PostDAO
}

object PostsDbController {

    private const val db_name = "posts.db"

    fun buildDatabase(context: Context) = Room
        .databaseBuilder(context.applicationContext, PostDb::class.java, db_name)
        .fallbackToDestructiveMigration()
        .build()
}
