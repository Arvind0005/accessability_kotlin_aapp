package com.example.myapplication//package com.example.myapplication
//

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.accessibility.AccessibilityManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.MediaController
import android.widget.ScrollView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
import java.util.Objects


class MainActivity: AppCompatActivity(){
    var app_names = arrayOf("whatsapp,youtube,browser")
    val NEW_SPINNER_ID = 1
    private val USAGE_ACCESS_REQUEST_CODE = 1
    private lateinit var accessibilityService: MyAccessibilityService<Any?>
    private var tv_Speech_to_text: TextView? = null



    val str:String=""
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var webView: WebView
    private var alertDialog: AlertDialog? = null
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "ResourceType")

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_page)


        val permissionText=findViewById<TextView>(R.id.recentapps_text)
        val recentapps=findViewById<LinearLayout>(R.id.recentapps);


        val text1: TextView =findViewById(R.id.apptext0);
        val text2: TextView =findViewById(R.id.apptext1);
        val text3: TextView =findViewById(R.id.apptext2);
        val text4: TextView =findViewById(R.id.apptext3);
        val text5: TextView =findViewById(R.id.apptext4);
        val text6: TextView =findViewById(R.id.apptext5);


        val ic_1: ImageView =findViewById(R.id.app_ic0);
        val ic_2: ImageView =findViewById(R.id.app_ic1);
        val ic_3: ImageView =findViewById(R.id.app_ic2);
        val ic_4: ImageView =findViewById(R.id.app_ic3);
        val ic_5: ImageView =findViewById(R.id.app_ic4);
        val ic_6: ImageView =findViewById(R.id.app_ic5);

        //home
        val pm = packageManager
        val scrollview = findViewById<ScrollView>(R.id.scrollView)
        val listView = findViewById<ListView>(R.id.list)
        val searchview:SearchView =findViewById(R.id.searchView);
        val scale = resources.displayMetrics.density



        listView.setOnTouchListener(OnTouchListener { v, event ->
            scrollview.requestDisallowInterceptTouchEvent(true)
            val action = event.actionMasked
            when (action) {
                MotionEvent.ACTION_UP -> scrollview.requestDisallowInterceptTouchEvent(false)
            }
            false
        })


        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        val nonSystemApppackagenames = mutableListOf<String>()
        val appnames = mutableListOf<String>()
        val images = mutableListOf<Icon>();
        val appinfos = mutableListOf<AppInfo>()
        var sortedAppInfos = mutableListOf<AppInfo>()

        for (packageInfo in packages) {
            val isChromeOrYouTube = packageInfo.packageName == "com.android.chrome" ||
                    packageInfo.packageName == "com.google.android.youtube"
            if ((packageInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || isChromeOrYouTube) {
                // The app is not a system app
                val appinfo =AppInfo(packageInfo.loadLabel(packageManager).toString(),packageInfo.packageName,packageInfo.loadIcon(packageManager))
                appinfos.add(appinfo);
                appnames.add(packageInfo.loadLabel(packageManager).toString())
                val appIcon: Drawable = packageInfo.loadIcon(packageManager)
                println("heeeeeeeeeeelo");
                println(appIcon);
                nonSystemApppackagenames.add(packageInfo.packageName)
            }
        }
        sortedAppInfos = appinfos.sortedBy { it.appName }.toMutableList()
        val adapter =  MyArrayAdapter(this,R.id.rowtextview,sortedAppInfos);
        fun otherApps()
        {
            listView.adapter=adapter;
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val selectedPackageName = adapter.getItem(position)?.packageName;
                    if (selectedPackageName != null) {
                        openApp(selectedPackageName)
                    }
                }
        }
        otherApps();

        fun getAppName(packageName: String): String {
            val packageManager = packageManager
            return try {
                val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                packageManager.getApplicationLabel(applicationInfo).toString()
            } catch (e: PackageManager.NameNotFoundException) {


                // Handle exception or return the package name as a fallback
                packageName
            }
        }



        fun getAppIcon(packageName: String): Drawable? {
            return try {
                val packageManager = packageManager
                val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                packageManager.getApplicationIcon(applicationInfo)
            } catch (e: PackageManager.NameNotFoundException) {
                println("errrrrrrrrrrrrrrrrreeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrr");
                // Handle exception or return null as a fallback
                null
            }
        }

        fun recentappsPermission()
        {
            if (hasUsageAccessPermission()) {
                val usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
                val currentTime = System.currentTimeMillis()
                val startTime = currentTime - 24 * 60 * 60 * 1000 // 24 hours ago
                val packman:PackageManager = getPackageManager();
                val usageStatsList = usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY, startTime, currentTime
                )
                recentapps.visibility=View.VISIBLE;
                permissionText.visibility=View.GONE;
                var nonSystemApps = usageStatsList.filter { usageStats ->
                    val packageInfo = packageManager.getPackageInfo(usageStats.packageName, 0)

                    // Exclude Chrome and YouTube from system app filtering
                    val isChromeOrYouTube = usageStats.packageName == "com.android.chrome" ||
                            usageStats.packageName == "com.google.android.youtube"

                    // Include the app in nonSystemApps if it's not a system app or it's Chrome/YouTube
                    !((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) || isChromeOrYouTube
                }
                text1.text = getAppName(nonSystemApps[0].packageName.toString());
                text2.text = getAppName(nonSystemApps[1].packageName.toString());
                text3.text = getAppName(nonSystemApps[2].packageName.toString());
                text4.text = getAppName(nonSystemApps[3].packageName.toString());
                text5.text = getAppName(nonSystemApps[4].packageName.toString());
                text6.text = getAppName(nonSystemApps[5].packageName.toString());

                text1.setOnClickListener {
                    openApp(nonSystemApps[0].packageName.toString())
                }

                text2.setOnClickListener {
                    openApp(nonSystemApps[1].packageName.toString())
                }
                text3.setOnClickListener {
                    openApp(nonSystemApps[2].packageName.toString())
                }

                text4.setOnClickListener {
                    openApp(nonSystemApps[3].packageName.toString())
                }
                text5.setOnClickListener {
                    openApp(nonSystemApps[4].packageName.toString())
                }
                text6.setOnClickListener {
                    openApp(nonSystemApps[5].packageName.toString())
                }

                ic_1.setImageDrawable(getAppIcon(nonSystemApps[0].packageName.toString()));
                ic_2.setImageDrawable(getAppIcon(nonSystemApps[1].packageName.toString()));
                ic_3.setImageDrawable(getAppIcon(nonSystemApps[2].packageName.toString()));
                ic_4.setImageDrawable(getAppIcon(nonSystemApps[3].packageName.toString()));
                ic_5.setImageDrawable(getAppIcon(nonSystemApps[4].packageName.toString()));
                ic_6.setImageDrawable(getAppIcon(nonSystemApps[5].packageName.toString()));


                println("heeeeeeeeeeeeeeeeeeeeloooooooooooooo");
                for (usageStats in nonSystemApps) {
                    val packageName = usageStats.packageName
                    println(packageName);
                    // Display or store the package name as needed
                }


            }
            else
            {
                recentapps.visibility=View.GONE;
                permissionText.visibility=View.VISIBLE;
            }
        }
        val overlayStatusTextView: TextView = findViewById(R.id.overlay_permissionText)
        fun checkpermission() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                // Overlay permission not granted
                overlayStatusTextView.text = "Not Granted"
                overlayStatusTextView.setTextColor(Color.parseColor("#FF0000"))

                // Open settings to grant overlay permission

            } else {
                // Overlay permission granted
                overlayStatusTextView.text = "Granted"
                overlayStatusTextView.setTextColor(Color.parseColor("#00FF00"))

            }
        }
        checkpermission();
        recentappsPermission();
        tv_Speech_to_text = findViewById<TextView>(R.id.webview_text);

        val handler = Handler()
        val receivedIntent = intent
        var sharedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT)

        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val accessibilityServiceString = "$packageName/${MyAccessibilityService::class.java.canonicalName}"
        val isServiceEnabled = enabledServices?.contains(accessibilityServiceString) == true

        val MainserviceIntent = Intent(this, MyAccessibilityService::class.java)
        startService(MainserviceIntent);

        accessibilityService = MyAccessibilityService();


        if (sharedText != null) {
            val intent4 = Intent(this@MainActivity, FloatingWindowGFG::class.java);
            intent4.putExtra("url", sharedText.toString());
            startService(intent4);
            finish();
        };


        fun checkUsageAccessPermission() {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (alertDialog == null && !hasUsageAccessPermission() && !hasOverlayPermission()) {
                        requestUsageAccessPermission();
//                        if(hasUsageAccessPermission() && hasOverlayPermission())
//                            showAllPermissionsGrantedDialog();
                    } else {
                        // If the dialog is open or permission is granted, delay the next check

                    }
//                    if(alertDialog!=null && hasUsageAccessPermission() && hasOverlayPermission())
//                        showAllPermissionsGrantedDialog();
                    handler.postDelayed(this, 100)
                }
            }, 0)

        }
        checkUsageAccessPermission()



        val dropdownbutton0: LinearLayout =findViewById(R.id.dropdown_button);
        val permisionText0: LinearLayout=findViewById(R.id.permission_description);

        val gestureButton: LinearLayout =findViewById(R.id.gesturesDropown);
        val gestureDescription: LinearLayout=findViewById(R.id.GesturesDescription);

        val yt_usage_button:LinearLayout =findViewById(R.id.dropdownusage);
        val yt_description: LinearLayout=findViewById(R.id.Youtube_description);

        val wts_usage_button:LinearLayout=findViewById(R.id.dropdownusagewhatsapp);
        val wts_description:LinearLayout =findViewById(R.id.whatsapp_description);

        val chrome_usage_button:LinearLayout=findViewById(R.id.dropdownusagechrome);
        val chrome_description:LinearLayout=findViewById(R.id.chrome_description);

        val helppagebutton:ImageView=findViewById<ImageView>(R.id.help_ic)
        val helpage:LinearLayout =findViewById(R.id.help_page);


        val homeButton:ImageView =findViewById(R.id.helpthome);
        val homepage:LinearLayout=findViewById(R.id.home_page);

        val homeselection:View =findViewById(R.id.selectionhome);
        val helpselction:View=findViewById(R.id.selectionhelp);
        val micselection:View=findViewById(R.id.mic_page);
        val micbar:View=findViewById(R.id.selectionmic);

        val searchButton:ImageView =findViewById(R.id.search_ic);
        val micButton:ImageView=findViewById(R.id.mic0_ic);
        val appbarbackButton:androidx.cardview.widget.CardView =findViewById(R.id.AppBarBack)
        val searchSelection:LinearLayout =findViewById(R.id.search_bar)
        val appbar:LinearLayout =findViewById(R.id.Appbar)

        val settingsIc: ImageView = findViewById(R.id.settings_ic)

        settingsIc.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }



        val videoView: VideoView = findViewById(R.id.yt_video)
        val whatsapp_video: VideoView = findViewById(R.id.whatsapp_video)
        val chrome_video: VideoView = findViewById(R.id.chrome_video)

// Set the path of the video file (res/raw/sample.mp4)
        val path = "android.resource://" + packageName + "/" + R.raw.yt_video
        val pathw = "android.resource://" + packageName + "/" + R.raw.whatspp_video
        val pathc = "android.resource://" + packageName + "/" + R.raw.chrome_video

// Set up a MediaController to control playback
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        val mediaController0 = MediaController(this)
        mediaController0.setAnchorView(whatsapp_video)

        val mediaController1 = MediaController(this)
        mediaController1.setAnchorView(chrome_video)

// Set the Uri of the video file
        val uri = Uri.parse(path)
        videoView.setVideoURI(uri)

        val uri0 = Uri.parse(pathw)
        whatsapp_video.setVideoURI(uri0)

        val uri1 = Uri.parse(pathc)
        chrome_video.setVideoURI(uri1) // Set the correct URI for chrome_video

// Set the MediaController for the VideoView
        videoView.setMediaController(mediaController)
        whatsapp_video.setMediaController(mediaController0)
        chrome_video.setMediaController(mediaController1)

        videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.seekTo(0) // Seek to the beginning
            mediaPlayer.start() // Start the video again
        }

        whatsapp_video.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }

        chrome_video.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }

// Start playing the video
        videoView.start()
        whatsapp_video.start()
        chrome_video.start()




        val accessibilityStatusTextView: TextView = findViewById(R.id.access_permission)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (accessibilityManager.isEnabled) {
                // Accessibility granted
                accessibilityStatusTextView.text = "Granted"
                accessibilityStatusTextView.setTextColor(Color.parseColor("#00FF00"))
            } else {
                // Accessibility not granted
                accessibilityStatusTextView.text = "Not Granted"
                accessibilityStatusTextView.setTextColor(Color.parseColor("#FF0000"))
            }
        }

//        val overlayPermissionStatusTextView: TextView = findViewById(R.id.overlay_permissionText)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//            // Overlay permission not granted
//            overlayPermissionStatusTextView.text = "Not Granted"
//            overlayPermissionStatusTextView.setTextColor(Color.parseColor("#00FF00"))
//
//            // Open settings to grant overlay permission
//
//        } else {
//            // Overlay permission granted
//            overlayPermissionStatusTextView.text = "Granted"
//            overlayPermissionStatusTextView.setTextColor(Color.parseColor("#FF0000"))
//        }



//
//        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//        startActivity(intent)


        val usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val startTime = currentTime - 24 * 60 * 60 * 1000 // 24 hours ago
        val packman:PackageManager = getPackageManager();

        val pks: List<ApplicationInfo> =
            packman.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in pks) {
            println( "Installed package :" + packageInfo.packageName)
            println("Source dir : " + packageInfo.sourceDir)
            println("Launch Activity :" + packman.getLaunchIntentForPackage(packageInfo.packageName))
        }

        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, currentTime
        )


        webView = findViewById(R.id.micView)

        // Enable JavaScript in the WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Load the URL
        val userurl = "https://letstalksign.org/extension/page1.html"
        webView.webViewClient = MyWebViewClient()

        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = "useragentstring"
        webView.getSettings().setSupportMultipleWindows(true);

        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.evaluateJavascript("console.log(\"helllo world00999293\")", null)
        webView.evaluateJavascript("window.postMessage(\"hello world test000\");") { result ->
            println("Result of evaluateJavascript: $result")
        }
        webView.evaluateJavascript("window.addEventListener(\"message\", receiveMessage(event)\n" +
                "{" +
                "  console.log(\"eventtesting\"=event);\n" +
                "    return;}, false);",null)
        webView.settings.domStorageEnabled = true

        webView.loadUrl(userurl)
        //  webView.loadUrl("file:///android_asset/webview.html");


        webView.clearCache(true) // Clears the cache, including disk and memory caches.
        webView.clearFormData()  // Clears any stored form data in the WebView.
        webView.clearHistory()
        webView.webViewClient = WebViewClient()

        val iv_mic: ImageView = findViewById<ImageView>(R.id.webview_mic_ic)
        tv_Speech_to_text = findViewById<TextView>(R.id.webview_text);

        iv_mic?.let { micButton ->
            micButton.setOnClickListener(View.OnClickListener {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault()
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
                } catch (e: Exception) {
                    Toast
                        .makeText(
                            this, " " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            })
        }








        fun openApp(packageName: String) {
            val packageManager = this.packageManager
            // Check if app is installed
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                // App not found or not installed
                Toast.makeText(this, "App not found: $packageName", Toast.LENGTH_SHORT).show()
            }
        }





        appbarbackButton.setOnClickListener{
            searchview.setQuery("",false);
            if(searchSelection.visibility==View.VISIBLE)
            {
                searchSelection.visibility=View.GONE
                appbar.visibility=View.VISIBLE

            }
        }

        searchButton.setOnClickListener{
            if(searchSelection.visibility==View.GONE) {
                searchSelection.visibility = View.VISIBLE;
                appbar.visibility=View.GONE;
            }
        }

        helppagebutton.setOnClickListener{
            if(helpage.visibility==View.GONE)
            {
                homeButton.layoutParams.width=(25 * scale + 0.5f).toInt();
                micButton.layoutParams.width=(25 * scale + 0.5f).toInt();
                helppagebutton.layoutParams.width=(35 * scale + 0.5f).toInt();
                homepage.visibility=View.GONE;
                helpage.visibility=View.VISIBLE;
                helpselction.visibility=View.GONE;
                homeselection.visibility=View.GONE;
                micselection.visibility=View.GONE;
                micbar.visibility=View.GONE;
                checkpermission();
            }
        }

        micButton.setOnClickListener{
            if(micselection.visibility==View.GONE)
            {
                homeButton.layoutParams.width=(25 * scale + 0.5f).toInt();
                micButton.layoutParams.width=(35 * scale + 0.5f).toInt();
                helppagebutton.layoutParams.width=(25 * scale + 0.5f).toInt();
                homepage.visibility=View.GONE;
                helpage.visibility=View.GONE;
                helpselction.visibility=View.GONE;
                homeselection.visibility=View.GONE;
                micselection.visibility=View.VISIBLE;
                micbar.visibility=View.GONE;
            }
        }

        homeButton.setOnClickListener{
            if(homepage.visibility==View.GONE)
            {
                homeButton.layoutParams.width=(35 * scale + 0.5f).toInt();
                micButton.layoutParams.width=(25 * scale + 0.5f).toInt();
                helppagebutton.layoutParams.width=(25 * scale + 0.5f).toInt();
                helpage.visibility=View.GONE;
                homepage.visibility=View.VISIBLE;
                helpselction.visibility=View.GONE;
                homeselection.visibility=View.GONE;
                micselection.visibility=View.GONE;
                micbar.visibility=View.GONE;
                recentappsPermission();
            }
        }

        chrome_usage_button.setOnClickListener{
            if(chrome_description.visibility==View.VISIBLE) {
                chrome_description.visibility = View.GONE;
                chrome_video.pause()
            }
            else {
                chrome_description.visibility = View.VISIBLE;
                permisionText0.visibility = View.GONE;
                gestureDescription.visibility=View.GONE;
                yt_description.visibility=View.GONE
                wts_description.visibility=View.GONE
            }
        }

        wts_usage_button.setOnClickListener{
            if(wts_description.visibility==View.VISIBLE) {
                wts_description.visibility = View.GONE;
                whatsapp_video.pause()
            }
            else {
                wts_description.visibility = View.VISIBLE
                permisionText0.visibility = View.GONE;
                gestureDescription.visibility=View.GONE;
                yt_description.visibility=View.GONE
                chrome_description.visibility=View.GONE
            }
        }
        yt_usage_button.setOnClickListener{
            if(yt_description.visibility==View.VISIBLE) {
                yt_description.visibility = View.GONE;
                videoView.pause()
            }
            else {
                yt_description.visibility = View.VISIBLE;
                permisionText0.visibility = View.GONE;
                gestureDescription.visibility=View.GONE;
                wts_description.visibility=View.GONE
                chrome_description.visibility=View.GONE
            }
        }
       
        dropdownbutton0.setOnClickListener{
            if(permisionText0.visibility==View.VISIBLE)
                permisionText0.visibility=View.GONE;
            else {
                permisionText0.visibility = View.VISIBLE;
                gestureDescription.visibility=View.GONE;
                yt_description.visibility=View.GONE
                wts_description.visibility=View.GONE
                chrome_description.visibility=View.GONE
            }
        }

        gestureButton.setOnClickListener{
            if(gestureDescription.visibility==View.VISIBLE)
                gestureDescription.visibility=View.GONE;
            else {
                gestureDescription.visibility = View.VISIBLE;
                permisionText0.visibility = View.GONE;
                yt_description.visibility=View.GONE
                wts_description.visibility=View.GONE
                chrome_description.visibility=View.GONE
            }
        }
        // Set OnClickListener for the ImageView



        //home page
        fun isTouchInsideView(event: MotionEvent, view: View): Boolean {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val x = event.rawX
            val y = event.rawY
            return x > location[0] && x < location[0] + view.width && y > location[1] && y < location[1] + view.height
        }

        val rootLayout = findViewById<View>(R.id.main_page); // Replace with your actual root layout ID
        rootLayout.setOnTouchListener { _, event ->
            // Check if the touch event is outside the SearchView
            if (searchview.visibility==View.VISIBLE && !isTouchInsideView(event, searchview)) {
                // Close the SearchView
                searchview.setQuery("",false);
                if(searchSelection.visibility==View.VISIBLE)
                {
                    searchSelection.visibility=View.GONE
                    appbar.visibility=View.VISIBLE
                }
            }
            false // Return false to allow other touch events to be handled
        }


        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (appnames.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(this@MainActivity, "No Apps found..", Toast.LENGTH_LONG)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

    }

    private fun hasUsageAccessPermission(): Boolean {
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName
            )
        }

        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            // For versions prior to Android M, overlay permission is not required
            true
        }
    }

    private fun showAllPermissionsGrantedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissions Granted")
            .setMessage("All required permissions have been granted.")
            .setPositiveButton("OK") { _, _ ->
                // Handle the "OK" button click if needed
            }
            .show()
    }

    private fun requestUsageAccessPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissions Required")
            .setMessage("Please grant the following permissions to use this app:\n\n" +
                    "1. Usage access permission\n" +
//                    "2. Accessibility access permission\n" +
                    "2. Overlay permission\n" +
                    "You can also grant these and other permissions later using the help page")
            .setPositiveButton("Grant Usage Access") { _, _ ->
                openUsageAccessSettings()
                alertDialog = null
                // Comment out the line below if you want the dialog to remain open
                //alertDialog?.dismiss()
            }
            .setNegativeButton("Grant Overlay Permission") { _, _ ->
                openOverlaySettings()
                alertDialog = null
                // Comment out the line below if you want the dialog to remain open
//                alertDialog?.dismiss()
            }
//            .setNeutralButton("Grant Accessibility Access") { _, _ ->
//                openAccessibilitySettings()
//                alertDialog = null
//                // Comment out the line below if you want the dialog to remain open
//                //alertDialog?.dismiss()
//            }

            // Remove the line below to make the dialog non-dismissable
             .setCancelable(true)
            .setOnDismissListener {
                alertDialog = null
            }

        alertDialog = builder.show()
    }




    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivityForResult(intent,1);
    }

    private fun openOverlaySettings() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        startActivityForResult(intent, 1);
    }
    private fun openUsageAccessSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivityForResult(intent, USAGE_ACCESS_REQUEST_CODE)
    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        @Nullable data: Intent?
    )
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )
                tv_Speech_to_text!!.text =
                    tv_Speech_to_text!!.text.toString()+" "+Objects.requireNonNull(result)?.get(0) ?: null
            }
        }
    }

    private fun openApp(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }

}