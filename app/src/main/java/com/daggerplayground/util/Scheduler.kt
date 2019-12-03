package com.daggerplayground.util

import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import javax.inject.Inject

@Component(modules = [MySchedulers::class, TestSchedulers::class])
interface SchedulerFactory {
    fun scheduler(): MySchedulers
    fun testScheduler(): TestSchedulers
}

interface BaseScheduler {
    fun ui(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler
}

@Module
class MySchedulers @Inject constructor(): BaseScheduler {
    @Provides
    override fun ui() = AndroidSchedulers.mainThread()

    @Provides
    override fun io() = Schedulers.io()

    @Provides
    override fun computation() = Schedulers.computation()
}

@Module
class TestSchedulers @Inject constructor(): BaseScheduler {
    @Provides
    override fun ui(): Scheduler =
        Schedulers.trampoline()

    @Provides
    override fun io(): Scheduler =
        Schedulers.trampoline()

    @Provides
    override fun computation(): Scheduler =
        Schedulers.trampoline()

}