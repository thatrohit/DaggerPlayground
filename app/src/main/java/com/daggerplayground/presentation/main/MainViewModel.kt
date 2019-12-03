package com.daggerplayground.presentation.main

import android.util.Log
import androidx.lifecycle.*
import com.daggerplayground.data.local.PostDb
import com.daggerplayground.data.local.PostsDbController
import com.daggerplayground.data.remote.ApiFactory
import com.daggerplayground.data.remote.DaggerApiFactory
import com.daggerplayground.domain.Post
import com.daggerplayground.util.BaseScheduler
import com.daggerplayground.util.DaggerSchedulerFactory
import com.daggerplayground.util.SchedulerFactory
import dagger.Component
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class MainViewModel : ViewModel() {

    private val disposables = CompositeDisposable()
    private var apiFactory: ApiFactory = DaggerApiFactory.builder().build()
    var sch: SchedulerFactory = DaggerSchedulerFactory.builder().build()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getPosts(sch: BaseScheduler): MutableLiveData<List<Post>> {
        val postsLiveData = MutableLiveData<List<Post>>()
        disposables.add(
            apiFactory.api().getPosts().subscribeOn(sch.io()).observeOn(sch.ui()).subscribe({
                postsLiveData.value = it
                Log.i("RX_SUC", "Success")
            }, {
                Log.e("RX_ERR", it.message.toString())
            })
        )
        return postsLiveData
    }

    fun insertPostsToDb(posts: List<Post>, sch: BaseScheduler, db: PostDb) =
        Observable
            .fromCallable { db.postDao().insertPosts(posts) }
            .subscribeOn(sch.io())
            .observeOn(sch.ui())

    fun getPostsFromDb(sch: BaseScheduler, db: PostDb) =
        Observable
            .fromCallable { db.postDao().getPosts() }
            .subscribeOn(sch.io())
            .observeOn(sch.ui())

    fun deletePostsFromDb(sch: BaseScheduler, db: PostDb) =
        Observable
            .fromCallable { db.postDao().deletePosts() }
            .subscribeOn(sch.io())
            .observeOn(sch.ui())

    fun updatePostToDb(post: Post, sch: BaseScheduler, db: PostDb) =
        Observable
            .fromCallable { db.postDao().updatePost(post) }
            .subscribeOn(sch.io())
            .observeOn(sch.ui())

    fun updatePostsToDb(posts: List<Post>, sch: BaseScheduler, db: PostDb) =
        Observable
            .fromCallable { db.postDao().updatePosts(posts) }
            .subscribeOn(sch.io())
            .observeOn(sch.ui())
}
