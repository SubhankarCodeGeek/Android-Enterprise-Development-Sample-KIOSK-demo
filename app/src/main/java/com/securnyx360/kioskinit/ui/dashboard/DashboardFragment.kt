package com.securnyx360.kioskinit.ui.dashboard

import android.app.ActivityOptions
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.securnyx360.kioskinit.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var dpm: DevicePolicyManager

    // Allowlist two apps.
    private val kioskPackage = "com.securnyx360.kioskinit"
    private val appPackage = arrayOf(kioskPackage)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_start_lock_task).setOnClickListener {
//            startLockTaskMode(appPackage, kioskPackage)
        }

        view.findViewById<Button>(R.id.btn_end_lock_task).setOnClickListener {
            activity?.stopLockTask()
        }
    }

//    // In our Fragment subclass.
//    override fun onResume() {
//        super.onResume()
//
//        val dpm = context?.getSystemService(Context.DEVICE_POLICY_SERVICE)
//                as DevicePolicyManager
//        val adminName = activity?.componentName
//        if (adminName != null) {
//            dpm.setLockTaskPackages(adminName, appPackage)
//        }
//
//        // First, confirm that this package is allowlisted to run in lock task mode.
//        if (dpm.isLockTaskPermitted(context?.packageName)) {
//            activity?.startLockTask()
//        } else {
//            // Because the package isn't allowlisted, calling startLockTask() here
//            // would put the activity into screen pinning mode.
//        }
//    }
//
//    // Called just after entering lock task mode.
//    override fun onLockTaskModeEntering(context: Context, intent: Intent) {
//        val dpm = getManager(context)
//        val admin = getWho(context)
//
//        dpm.addUserRestriction(admin, UserManager.DISALLOW_CREATE_WINDOWS)
//    }
//
//    private fun startLockTaskMode(
//        appPackage: Array<String>,
//        kioskPackage: String
//    ) {
//        activity?.apply {
//            // Set an option to turn on lock task mode when starting the activity.
//            val options = ActivityOptions.makeBasic()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                options.setLockTaskEnabled(true)
//            }
//
//            // Start our kiosk app's main activity with our lock task mode option.
//            val packageManager = this.packageManager
//            val launchIntent = packageManager.getLaunchIntentForPackage(kioskPackage)
//            if (launchIntent != null) {
//                this.startActivity(launchIntent, options.toBundle())
//            }
//        }
//    }
}