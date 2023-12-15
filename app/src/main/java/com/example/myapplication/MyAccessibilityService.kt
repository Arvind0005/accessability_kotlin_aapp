package com.example.myapplication

import HighlightOverlay
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.ActivityManager
//import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.AlertDialog
import android.content.SharedPreferences


class MyAccessibilityService<AccessibilityNodeInfo> : AccessibilityService() {
//    private var broadcastReceiver: BroadcastReceiver? = null
    private val capturedTextLiveData = MutableLiveData<String>()
    private var highlightOverlay: HighlightOverlay? = null
    private var youtube=false;
    lateinit var sharedPreferences:SharedPreferences;
    lateinit var webIntent: Intent

    var caption="";

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)

        for (service in runningServices) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    override fun onCreate() {
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        var serviceStarted = sharedPreferences.getBoolean("serviceStarted", false)
        val editor = sharedPreferences.edit()
        super.onCreate()
        highlightOverlay = HighlightOverlay(this)
    }
    override fun onInterrupt() {}
    override fun onServiceConnected() {
        super.onServiceConnected()
        println("Accessibility was connected!")
    }

        @SuppressLint("SuspiciousIndentation")
        override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val eventType = event?.eventType

        val text = event?.text;

        println("heeeeeeeeeeeeelo");
        println(text.toString());
        println(event?.packageName);
        println(event);
        val isFloatingWindowServiceRunning = isServiceRunning(this, FloatingWindowGFG::class.java)
        if((text?.toString()?.length !!>10 && event?.packageName.toString()=="com.android.chrome") ||(text?.toString()?.length !!>10 && event?.packageName.toString()=="com.whatsapp")) {
            println("im insideeeeeeeeeeeeeeee"+isFloatingWindowServiceRunning.toString());
            if (!isFloatingWindowServiceRunning) {
                println("heeeeeeeeeeeeeeeelooooooooooodnsj");
                val intent = Intent(this, FloatingWindowGFG::class.java)
              //  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startService(intent)
                println(text);
            }
        }
        val source = event?.source

//        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
//            // Call the HighlightOverlay to show the overlay on the clicked view
//            val clickedView = event.source as View?
//            applyGreenBorder(clickedView);
//            println("clickkkkkkkkkkkkkkkkkkkkkkkkkkkkked");
//
//            // Apply green border to the clicked view
//
//            // Apply green border to the clicked view
//
//
//            // Other handling logic
//        }
         if (eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
         //   System.out.println("yesssssssssssssssssss");
            // Handle accessibility focus event
            // Detect double clicks and select content
        }
        if(text?.size!! >4) {
            println(text);
            if (event != null) {

//                System.out.println(
//                    String.format(
//                        "onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]",
//                        event, event.getClassName(), event.getPackageName(),
//                        event.getEventTime(), event.text
//                    )
               // )
            };

//                val broadcastIntent = Intent("READING_TEXT");
//                broadcastIntent.putExtra("READING_TEXT", text.toString());
//                sendBroadcast(broadcastIntent);
        }

//        broadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                val readingText = intent.getStringExtra("READING_TEXT")
//                System.out.println("cccccccccccccccccccccccccc");
//                System.out.println(readingText);
//                // Process the reading text here
//            }
//        }
//        val intentFilter = IntentFilter("READING_TEXT")
//        registerReceiver(broadcastReceiver, intentFilter);
//        when (eventType) {
//            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
//                //System.out.println("testing");
//                val text = event.text?.joinToString("\n") { it.toString() }
//                //System.out.println(text);
//               // capturedTextLiveData.postValue(text)
//                // Update UI or perform actions based on the captured text
//            }
//            // Handle other evvent types...
//        }
    }


    override fun onKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        val keyCode = event.keyCode
        // the service listens for both pressing and releasing the key
        // so the below code executes twice, i.e. you would encounter two Toasts
        // in order to avoid this, we wrap the code inside an if statement
        // which executes only when the key is released
        if (action == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                Log.d("Check", "KeyUp")
                Toast.makeText(this, "KeyUp", Toast.LENGTH_SHORT).show()
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                Log.d("Check", "KeyDown")
                Toast.makeText(this, "KeyDown", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onKeyEvent(event)
    }
}