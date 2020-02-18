package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var vText: TextView
    var request: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vText = findViewById<TextView>(R.id.act1_text)
        vText.setTextColor(0xFFFF0000.toInt())
        vText.setOnClickListener {
            Log.e("tag", "Text has been pushed")
//            val i = Intent(this, SecondActivity::class.java)
//            i.putExtra("tag1", vText.text)
//            startActivityForResult(i, 0)

            val o =
                createRequest("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.yahoo.com%2Fnews%2Frss%2Fworld")
                    .map { Gson().fromJson(it, Feed::class.java) }
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            request = o.subscribe({
                for (item in it.items)
                    Log.w("test", "title: ${item.title}")
            }, {
                Log.e("test", "", it)
            })
        }
        Log.v("tag", "Step onCreate has been passed")
        //Log.v("tag", "text")
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