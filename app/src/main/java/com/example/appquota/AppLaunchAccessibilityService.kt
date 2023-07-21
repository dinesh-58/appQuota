package com.example.appquota

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

var firstLaunch = true
class AppLaunchAccessibilityService : AccessibilityService() {
    private lateinit var serviceContext: Context
    private var lastPackageName: String? = null
    private var blockedApp: String? = null
    //    private var remainingBlockTime: Long = 0
    // TODO rename this to blockTime cause needs to be constant value instead of changing?
    private var blockTimer: CountDownTimer? = null
    private lateinit var quotaTimer: CountDownTimer
    private var blockTimeFinished = false
    private var intent: Intent? = null

    companion object {
        private val REQUEST_CODE_SET_QUOTA = 1000
    }

    override fun onServiceConnected() {
        serviceContext = this
        // This method is called when the accessibility service is connected.
        // You need to enable the service in the device's accessibility settings
        // for this method to be called and for your service to start working.
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // This event is triggered whenever the foreground app or activity changes.
            val packageName = event.packageName?.toString()
            if (packageName != lastPackageName) {
                // done because TYPE_WINDOW_STATE_CHANGED also triggers when activity changed in the
                // same app & we only need to detect app switching
                lastPackageName = packageName
                runBlocking { blockedApp = serviceContext.dataStore.data.first()[BLOCKED_APP_KEY] }

                if (blockedApp.equals(packageName)) {
                    Log.d("DataStoreExample", "If you see this, then fuck yeah")
                    Toast.makeText(this, "switch to blocked app detected", Toast.LENGTH_SHORT)
                        .show()
//                    intent = Intent(this, AppBlockedActivity::class.java)
                    if (firstLaunch) {
                        var setQuotaIntent = Intent(this, SetQuotaActivity::class.java)
                        val tempContext = this
                        setQuotaIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(setQuotaIntent)
//                        ActivityResultContracts.StartActivityForResult(setQuotaIntent, )
                        Toast.makeText(this, "value of current quota is: " + SetQuotaActivity.currentQuotaMillis, Toast.LENGTH_SHORT).show()
                        quotaTimer = object : CountDownTimer(SetQuotaActivity.currentQuotaMillis, 1000) {
                            // TODO activity is started parallely or something so currentQuotaMillis is accessed(0) before we can set it
                            override fun onTick(millisUntilFinished: Long) {
//                                TODO display constantly updating notification like StayFocused?
                            }

                            override fun onFinish() {
                                firstLaunch = true
                                Toast.makeText(tempContext, "Time's up", Toast.LENGTH_SHORT).show()
                            }
                        }
//                    intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

//                    TODO move intent initialization to onCreate instead? because this runs on every switch to blocked app
//                    intent?.putExtra("block_time_finished", blockTimeFinished)
//                    startActivity(intent) // TODO launch this only when countdown finishes
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        // This method is called when the accessibility service is interrupted or disabled.
    }

//    private fun startTimer(time: Long) {
//        // Cancel any existing timer before starting a new one
//        cancelTimer()
//
//        countDownTimer = object : CountDownTimer(remainingBlockTime, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
////                intent?.putExtra("remaining_seconds", millisUntilFinished / 1000)
//                // You could update UI with the remaining time if needed
//            }
//
//            override fun onFinish() {
//                // Time's up, hide the activity with the text
//                hideRestrictedActivity()
//                // TODO in the end, if hideRestrictedActivity doesn't have much code, move it here & delete function
//            }
//        }.start()
//    }
//
//    private fun cancelTimer() {
//        countDownTimer?.cancel()
//        countDownTimer = null
//    }
//
//    private fun hideRestrictedActivity() {
//        blockTimeFinished = true
//        // Implement the logic to hide the activity with the text here
//        // For example, you can use the WindowManager to remove the view or finish the activity
//        // The exact implementation depends on how you are displaying the restricted activity.
//    }
}