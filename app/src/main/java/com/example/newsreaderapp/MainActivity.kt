package com.example.newsreaderapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var vText: TextView
    lateinit var vRecView: RecyclerView
    var request: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vRecView = findViewById<RecyclerView>(R.id.act1_rec_view)
        val o =
            createRequest("http://newsapi.org/v2/top-headlines?country=us&pageSize=60&apiKey=00db6d7e181649daa5aa4e7a8ab39ee7")
                .map { Gson().fromJson(it, Feed::class.java) }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = o.subscribe({
            showRecView(it.articles)
        }, {
            Log.e("test", "", it)
        })
    }

    fun showRecView(feedList: ArrayList<FeedItem>) {
        vRecView.adapter = RecAdapter(feedList)
        vRecView.layoutManager = LinearLayoutManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val str = data.getStringExtra("tag2")
            vText.text = str
        }
    }

    override fun onDestroy() {
        request?.dispose()
        super.onDestroy()
    }
}

class Feed(
    val articles: ArrayList<FeedItem>
)

class FeedItem(
    val title: String,
    val url: String,
    val urlToImage: String,
    val description: String
)

class RecAdapter(val items: ArrayList<FeedItem>) : RecyclerView.Adapter<RecHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}

class RecHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: FeedItem) {
        val vTitle = itemView.findViewById<TextView>(R.id.item_title)
        val vDesc = itemView.findViewById<TextView>(R.id.item_description)
        val vThumb = itemView.findViewById<ImageView>(R.id.item_thumb)
        vTitle.text = item.title
        vDesc.text = item.description
        Picasso.with(vThumb.context).load(item.urlToImage).into(vThumb)
        itemView.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.url)
            vThumb.context.startActivity(i)
        }
    }
}