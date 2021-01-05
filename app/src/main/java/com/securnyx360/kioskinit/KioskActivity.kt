package com.securnyx360.kioskinit

import android.content.*
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class KioskActivity : AppCompatActivity() {

    var imageUrl =
        "http://wisdompets.com/wp-content/uploads/2015/10/cat-scratching-post-97572046-262x300.jpg"

    var fullScreenImage: ImageView? = null

    var myRestrictionsManager: RestrictionsManager? = null
    var restrictionsReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kiosk)

        fullScreenImage = findViewById(R.id.fullScreenImage)

        myRestrictionsManager = getSystemService(RESTRICTIONS_SERVICE) as RestrictionsManager

        val restrictionsFilter = IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED)

        restrictionsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val appRestrictions = myRestrictionsManager?.applicationRestrictions
                if (appRestrictions?.size()!! > 0) {
                    imageUrl = appRestrictions.getString("imageUrl")!!
                    if (URLUtil.isValidUrl(imageUrl)) {
                        fullScreenImage?.apply {
                            post {
                                Glide.with(applicationContext)
                                    .load(imageUrl)
                                    .centerCrop()
                                    .into(this)
                            }
                        }
                    }
                }
            }
        }

        registerReceiver(restrictionsReceiver, restrictionsFilter)
    }

    override fun onResume() {
        super.onResume()
        val appRestrictions = myRestrictionsManager!!.applicationRestrictions
        if (appRestrictions.size() > 0) {
            imageUrl = appRestrictions.getString("imageUrl")!!
            if (URLUtil.isValidUrl(imageUrl)) {
                Glide.with(applicationContext)
                    .load(imageUrl)
                    .centerCrop()
                    .into(fullScreenImage!!)
            }
        }
        startLockTask()
    }

    override fun onDestroy() {
        unregisterReceiver(restrictionsReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {}

}