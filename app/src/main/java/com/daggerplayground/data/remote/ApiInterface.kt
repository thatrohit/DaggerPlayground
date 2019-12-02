package com.daggerplayground.data.remote

import com.daggerplayground.domain.Post
import dagger.Component
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("/posts")
    fun getPosts(): Observable<List<Post>>

    @GET("posts/{id}")
    fun getPostsById(@Path("id") id: Int): Observable<Post>
}