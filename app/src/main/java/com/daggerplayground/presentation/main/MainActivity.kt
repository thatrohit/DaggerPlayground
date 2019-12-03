package com.daggerplayground.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.daggerplayground.R
import com.daggerplayground.data.local.PostDb
import com.daggerplayground.data.local.PostsDbController
import com.daggerplayground.domain.Post
import com.daggerplayground.util.BaseScheduler
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IMain {

    private lateinit var vm: MainViewModel
    private lateinit var sch: BaseScheduler
    private var adapter: MainAdapter? = null
    private var posts = ArrayList<Post>()
    private lateinit var db: PostDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        vm = ViewModelProviders.of(this)[MainViewModel::class.java]
        sch = vm.sch.scheduler()
        db = PostsDbController.buildDatabase(this)
        getPostsFromDb()
        getPosts()
    }

    private fun getPostsFromDb() {
        vm.getPostsFromDb(sch, db).subscribe {
            posts.clear()
            posts.addAll(it)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onClickPost(post: Post) {
        Toast.makeText(this, post.title, Toast.LENGTH_SHORT).show()
    }

    override fun onToggleLike(post: Post) {
        post.isLiked = !(post.isLiked?:false)
        vm.updatePostToDb(post, sch, db).subscribe()
        adapter?.notifyItemChanged(posts.indexOf(post))
    }

    private fun initRecyclerView() {
        adapter = MainAdapter(posts, this)
        rvPost.adapter = adapter
        rvPost.layoutManager = LinearLayoutManager(this)
    }

    private fun getPosts() {
       vm.getPosts(sch).observe(this, Observer {
           posts.clear()
           posts.addAll(it)
           adapter?.notifyDataSetChanged()
           insertPostsToDb()
       })
    }

    private fun insertPostsToDb() {
        Observable.concat(vm.deletePostsFromDb(sch, db), vm.insertPostsToDb(posts, sch, db)).subscribe()
    }
}
