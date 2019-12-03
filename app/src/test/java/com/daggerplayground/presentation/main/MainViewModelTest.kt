package com.daggerplayground.presentation.main

import android.os.Looper
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.daggerplayground.domain.Post
import io.mockk.MockKAnswerScope
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelTest {

    private lateinit var vm: MainViewModel

    /*for synchronous execution*/
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Before
    fun buildUp() {
        initMocks()
        vm = MainViewModel()
    }

    /* initialize log class mocks */
    private fun initMocks() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } answers { mockedLog() }
        every { Log.e(any(), any()) } answers { mockedLog() }
    }

    private fun MockKAnswerScope<Int, Int>.mockedLog(): Int {
        println("${firstArg<String>()}: ${secondArg<String>()}")
        return 0
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getPosts() {
        vm.getPosts(vm.sch.testScheduler()).observeForever {
            assert(it.size == 100)
        }
    }
}