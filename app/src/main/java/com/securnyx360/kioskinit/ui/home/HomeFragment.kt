package com.securnyx360.kioskinit.ui.home

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.securnyx360.kioskinit.KioskActivity
import com.securnyx360.kioskinit.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    var welcomeMessage = "Welcome to the cat bonanza!"
    var isImageModeEnabled = true

    var imageModeButton: Button? = null

    var startImageActivityIntent: Intent? = null

    var myRestrictionsManager: RestrictionsManager? = null
    var restrictionsReceiver: BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startImageActivityIntent = Intent(context, KioskActivity::class.java)

        val welcomeButton = view.findViewById(R.id.welcomeButton) as Button
        welcomeButton.setOnClickListener { view ->
            Snackbar.make(
                view,
                welcomeMessage,
                Snackbar.LENGTH_LONG
            ).show()
        }

        imageModeButton = view.findViewById(R.id.startImageModeButton) as Button
        imageModeButton?.setOnClickListener { startActivity(startImageActivityIntent) }

        myRestrictionsManager =
            context?.getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager

        val restrictionsFilter = IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED)

        restrictionsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val appRestrictions = myRestrictionsManager?.applicationRestrictions
                if (appRestrictions?.size()!! > 0) {
                    welcomeMessage = appRestrictions.getString("welcomeButtonMessage")!!
                    isImageModeEnabled = appRestrictions.getBoolean("isImageModeEnabled")
                }
                imageModeButton?.post { imageModeButton?.isEnabled = isImageModeEnabled }
            }
        }

        context?.registerReceiver(restrictionsReceiver, restrictionsFilter)
    }

    override fun onResume() {
        super.onResume()
        val appRestrictions = myRestrictionsManager?.applicationRestrictions
        if (appRestrictions?.size()!! > 0) {
            welcomeMessage = appRestrictions.getString("welcomeButtonMessage")!!
            isImageModeEnabled = appRestrictions.getBoolean("isImageModeEnabled")
        }
        imageModeButton?.isEnabled = isImageModeEnabled
    }

    override fun onDestroy() {
        context?.unregisterReceiver(restrictionsReceiver)
        super.onDestroy()
    }
}