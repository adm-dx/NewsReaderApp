package com.example.newsreaderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var vText:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vText = findViewById<TextView>(R.id.act1_text)
        vText.setTextColor(0xFFFF0000.toInt())
        vText.setOnClickListener {
            Log.e("tag", "Text has been pushed")
            val i = Intent(this, SecondActivity::class.java)
            i.putExtra("tag1", vText.text)
            startActivityForResult(i, 0)
        }
        Log.v("tag", "Step onCreate has been passed")
        //Log.v("tag", "text")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            val str = data.getStringExtra("tag2")
            vText.text=str
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
}
