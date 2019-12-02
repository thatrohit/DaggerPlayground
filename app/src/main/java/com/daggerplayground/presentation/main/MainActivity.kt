package com.daggerplayground.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.daggerplayground.R

class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = DaggerMainViewModelFactory.create().vm()
        getPosts()
        getPost()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.stop()
    }

    private fun getPosts() {
       vm.getPosts(vm.sch.scheduler()).observe(this, Observer {
           Log.d("API_RES", it.toString())
       })
    }

    private fun getPost() {
        vm.getPost(4, vm.sch.scheduler()).observe(this, Observer {
            Log.d("API_RES", it.toString())
        })
    }
}
