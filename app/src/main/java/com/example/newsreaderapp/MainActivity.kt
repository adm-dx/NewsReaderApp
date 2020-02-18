package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var vText: TextView
    lateinit var vList: LinearLayout
    var request: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vList = findViewById<LinearLayout>(R.id.act1_list)
        val o =
            createRequest("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.yahoo.com%2Fnews%2Frss%2Fworld")
                .map { Gson().fromJson(it, Feed::class.java) }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = o.subscribe({
            showLinearLayout(it.items)
        }, {
            Log.e("test", "", it)
        })
    }

    fun showLinearLayout(feedList: ArrayList<FeedItem>) {
        val inflater = layoutInflater
        for (f in feedList) {
            val view = inflater.inflate(R.layout.list_item, vList,false)
            val vTitle = view.findViewById<TextView>(R.id.item_title)
            vTitle.text = f.title
            vList.addView(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val str = data.getStringExtra("tag2")
            vText.text = str
        }
    }

    override fun onStart() {
        super.onStart()
        //is used only for two displays one time
    }

    override fun onResume() {
        super.onResume()
        //to start animations and processes. first application view after launch
    }

    override fun onPause() {
        super.onPause()
        //may be the last step of lifecycle, system can shutdown it here
    }

    override fun onDestroy() {
        request?.dispose()
        super.onDestroy()
    }
}

class Feed(
    val items: ArrayList<FeedItem>
)

class FeedItem(
    val title: String,
    val link: String,
    val thumbnail: String,
    val description: String
)