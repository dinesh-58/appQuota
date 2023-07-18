package com.example.appquota

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AppLaunchAccessibilityService : AccessibilityService() {
    private lateinit var serviceContext: Context
    private var lastPackageName: String? = null
    private var blockedApp: String? = null

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
//                Toast.makeText(this, "app switch detected", Toast.LENGTH_SHORT).show()
                runBlocking {
                    blockedApp = serviceContext.dataStore.data.first()[BLOCKED_APP_KEY]
                }
                if (blockedApp.equals(packageName)) {
                    Log.d("DataStoreExample", "If you see this, then fuck yeah")
                    Toast.makeText(this, "switch to blocked app detected", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, AppBlockedActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    // Add any extra data to the intent if needed
                    // intent.putExtra("key", value)

                    startActivity(intent)
                }
            }
        }
    }

    override fun onInterrupt() {
        // This method is called when the accessibility service is interrupted or disabled.
    }
}