package com.daggerplayground.presentation.main

import android.util.Log
import androidx.lifecycle.*
import com.daggerplayground.data.remote.ApiFactory
import com.daggerplayground.data.remote.DaggerApiFactory
import com.daggerplayground.domain.Post
import com.daggerplayground.util.BaseScheduler
import com.daggerplayground.util.DaggerSchedulerFactory
import com.daggerplayground.util.SchedulerFactory
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Component
interface MainViewModelFactory {
    fun vm(): MainViewModel
}

class MainViewModel @Inject constructor() : LifecycleOwner {

    var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    private val disposable = CompositeDisposable()
    private var apiFactory: ApiFactory = DaggerApiFactory.builder().build()
    var sch: SchedulerFactory = DaggerSchedulerFactory.builder().build()

    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun stop() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    fun getPosts(sch: BaseScheduler): MutableLiveData<List<Post>> {
        val postsLiveData = MutableLiveData<List<Post>>()
        disposable.add(
            apiFactory.api().getPosts().subscribeOn(sch.io()).observeOn(sch.ui()).subscribe({
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    postsLiveData.value = it
                    Log.i("RX_SUC", "Success")
                }
            }, {
                Log.e("RX_ERR", it.message.toString())
            })
        )
        return postsLiveData
    }

    fun getPost(postId: Int, sch: BaseScheduler): MutableLiveData<Post> {
        val postLiveData = MutableLiveData<Post>()
        disposable.add(
            apiFactory.api().getPostsById(postId).subscribeOn(sch.io()).observeOn(sch.ui()).subscribe({
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    postLiveData.value = it
                    Log.i("RX_SUC", "Success")
                }
            }, {
                Log.e("RX_ERR", it.message.toString())
            })
        )
        return postLiveData
    }
}
