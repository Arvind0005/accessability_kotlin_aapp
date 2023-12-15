package com.example.myapplication

//import android.content.BroadcastReceiver

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Service
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Choreographer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.filament.utils.KtxLoader
import com.google.android.filament.utils.ModelViewer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.ByteBuffer


class FloatingWindowGFG : Service() {
    private var floatView: ViewGroup? = null
    private var LAYOUT_TYPE = 0
    private var floatWindowLayoutParam: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null
    private var maximizeBtn: Button? = null
    private var closeApplicationBtn: ImageButton? = null
    private var readingTextView: TextView? = null
    var captions_message="";
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0.toFloat()
    private var initialTouchY: Float = 0.toFloat()
    private var isMoveAction = false
//    private var broadcastReceiver: BroadcastReceiver? = null
    private lateinit var myTextView: TextView
    private val handler = Handler(Looper.getMainLooper())


    //glb
    private lateinit var surfaceView: SurfaceView
    private lateinit var choreographer: Choreographer
    private lateinit var modelViewer: ModelViewer

    private var userurl:String="https://letstalksign.org/extension/page1.html";

    fun getAccount(accountManager: AccountManager): Account? {
        val accounts = accountManager.getAccountsByType("com.google")
        val account: Account?
        account = if (accounts.size > 0) {
            accounts[0]
        } else {
            null
        }
        return account
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        var maxi=true;
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        floatView = inflater.inflate(R.layout.overlay_layout, null) as ViewGroup
        val webView = floatView!!.findViewById<WebView>(R.id.overlay_webview)
        var floatViewheight=floatView!!.height;
        var floatViewWidth=floatView!!.width;
        println("User URL: $userurl")


        System.out.println("im iNnnnnnnnnnnnnnnnnnnnnnnnnnnn");

        println("wiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        println(width);
        println(height);

        println("original floatview ${floatView!!.width},${floatView!!.height}");
        fun  maximizeOverlay(){
            println("maxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxii");
            maxi=true;
            println("${floatView!!.width},${floatView!!.height}");
            floatView!!.layoutParams.height=(height * 0.53f).toInt();
            floatView!!.layoutParams.width=(width * 0.67f).toInt();
            this.floatWindowLayoutParam=floatWindowLayoutParam;
//            floatView!!.layoutParams=LinearLayout.LayoutParams(webView.width, webView.height)
            println("${floatView!!.width},${floatView!!.height}");
        }

        val manager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        val list: Array<Account> = manager.getAccountsByType("com.google")

        var gmail: String? = null

        for (account in list) {
            gmail = account.name
            println(gmail);
            break
        }

        println("AAAAAAAAAAAAAAAAAAAAAAAAAAACCCCCCCCCCCCC");
        println(gmail);
//        val accountName = account!!.name
//        val fullName = accountName.substring(0, accountName.lastIndexOf("@"))
//        println()

//        Toast.makeText(this, "Full Name: $fullName", Toast.LENGTH_SHORT).show()
//        emailEditText.setText(accountName)
//        fullNameEditText.setText(fullName)
        fun minimizeOverlay()
        {
            println("minixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxii");
            maxi=false;
            println("${floatView!!.width},${floatView!!.height}");
            floatView!!.layoutParams.height=150;
            floatView!!.layoutParams.width=150;
            this.floatWindowLayoutParam=floatWindowLayoutParam;
            println("${floatView!!.width},${floatView!!.height}");
        }
//        floatView!!.setOnClickListener {
//            if (maxi)
//                minimizeOverlay();
//            else
//                maximizeOverlay();
//        }
//        readingTextView = floatView!!.findViewById(R.id.overlay_text)

//        maximizeBtn = floatView!!.findViewById(R.id.buttonMaximize)
        var mConnection: ServiceConnection? = null
        closeApplicationBtn = floatView!!.findViewById(R.id.window_close)
        closeApplicationBtn?.setOnClickListener(View.OnClickListener {
            stopSelf();
            finish();
            System.exit(0)
        })

        webView.webViewClient = MyWebViewClient()

        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = "useragentstring"
        webView.getSettings().setSupportMultipleWindows(true);

        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        println("helllooo world");
        webView.evaluateJavascript("console.log(\"helllo world00999293\")", null)
        webView.evaluateJavascript("window.postMessage(\"hello world test000\");") { result ->
            println("Result of evaluateJavascript: $result")
        }
        println("helllooo world");
//        webView.evaluateJavascript("window.addEventListener(\"message\", receiveMessage(event)\n" +
//                "{" +
//                "  console.log(\"eventtesting\"=event);\n" +
//                "    return;}, false);",null)
        webView.settings.domStorageEnabled = true

        webView.loadUrl(userurl!!)
     //   webView.loadUrl("file:///android_asset/webview.html");


        webView.clearCache(true) // Clears the cache, including disk and memory caches.
        webView.clearFormData()  // Clears any stored form data in the WebView.
        webView.clearHistory()
        webView.webViewClient = WebViewClient()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("WebView Console.log",consoleMessage.sourceId()+" "+consoleMessage.lineNumber()+" "+consoleMessage.message());
                return true
            }
        }

        fun sendMessageToWebView(message:String) {
            println("messsageeeeeeeeeeeeesss");
            println(message);
            //         webView.clearCache(true) // Clears the cache, including disk and memory caches.
//            webView.clearFormData()  // Clears any stored form data in the WebView.
//            webView.clearHistory()
//            webView.webViewClient = WebViewClient()
            println("before execution");
           // webView.evaluateJavascript("https://trrain4-web.letstalksign.org/script/mainScript-server-UG-V3.5-ext.js");
            webView.evaluateJavascript("sendMessage("+message.toString()+");", null);
            webView.evaluateJavascript("dt("+message+")") {result->
                println("the message is evaluated"+result.toString());
            }
            println("executed");
        }


        fun fetchCaptionsFromAPI(youtubeUrl: String): String {

            println("heeeeeeeeeeeeeeelo"+youtubeUrl);

            // Define your Flask API endpoint

            val apiEndpoint = "https://65bd-103-185-239-71.ngrok-free.app/get_captions?url=$youtubeUrl"


            // Create an instance of OkHttpClient
            val client = OkHttpClient()

            // Create a request for the API endpoint
            val request = Request.Builder()
                .url(apiEndpoint)
                .build()
            var responseB="";

            // Make the GET request asynchronously
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle the failure, e.g., show an error message

                    // Update your UI here with the error message if needed
                    // For example, you can show a Toast message
                    println()
                    println(e.message)
                    responseB= e.toString();

                }

                override fun onResponse(call: Call, response: Response) {
                    // Handle the successful response
                    val responseBody = response.body?.string()

                    handler.post {
                        if (responseBody != null) {
                            sendMessageToWebView(responseBody)
                        };
                        else
                        {
                            sendMessageToWebView("response is null");
                        }
//                    readingTextView?.text = responseBody
//                    myTextView.text = responseBody
                    }
//                    readingTextView?.text = responseBody
                    println("uithereeeeeeeeeeeeeeeeed");
//                    myTextView.text = responseBody;
                    println(responseBody);
                    println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    if (responseBody != null) {
//                        sendMessageToWebView(responseBody);
                    }
                    if (responseBody != null) {
                        responseB=responseBody
                    };
                }
            })
          //  sendMessageToWebView("android message");
            return responseB;
        }


        fun FloatingWindowGFG.fetchAndSendMessage(url:String): String {
             var res="";
            // Start a coroutine
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Perform the API call asynchronously
                    val captionsMessage = withContext(Dispatchers.IO) {
                        fetchCaptionsFromAPI(url.toString())
                    }
                    res=captionsMessage;
                    println("heeeeeeeeeelomachaan");
                    println(captionsMessage);
                    // Once the API call is complete, send the message to WebView
                    handler.postDelayed({ sendMessageToWebView(captions_message) }, 2000)

                } catch (e: Exception) {
                    // Handle exceptions if needed
                    res=e.toString();
                    e.printStackTrace()
                }
            }
            return res.toString();
        }




        if (intent != null) {
            val extras = intent.getStringExtra("url");
            println("extrasssssssssssssssssssssssssssssss");
            println(extras);
            captions_message =fetchCaptionsFromAPI(extras.toString());
//            val handler = Handler()
//            handler.postDelayed({ sendMessageToWebView(captions_message) }, 2000)
            userurl=extras.toString();
            userurl="https://letstalksign.org/extension/page1.html";
        } else {
            // Handle the case where the intent is null
//            userurl = "https://deepvisiontech.ai/tnstartup/home.html"
            userurl="https://letstalksign.org/extension/page1.html";
        }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                webView.evaluateJavascript("var FunctionOne = function () { window.postMessage(\"hellllo world how are you\",*);};"
//                       , null);
            } else {
//                webView.loadUrl("javascript:"
//                        + "var FunctionOne = function () {"
//                        + "  window.postMessage(\"hellllo world how are you\",*);}catch(e){}"
//                        + "};");
            }
            println("Uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuurrrrrrrrrrrrrrrrrrrrrrrrrrlllllll"+userurl+" hello");
            println(userurl);



        println("meeeeeeeeeeeeehvcgdfgcfcfdcfcfd");
        println("fcfdcdcddcdcdcdcdcdcdc"+captions_message);





            LAYOUT_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_TOAST
            }
            floatWindowLayoutParam = WindowManager.LayoutParams(
                (width * 0.67f).toInt(), (height * 0.53f).toInt(),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

            floatWindowLayoutParam!!.gravity =  Gravity.CENTER
            floatWindowLayoutParam!!.x = 0
            floatWindowLayoutParam!!.y = 0
            windowManager!!.addView(floatView, floatWindowLayoutParam)

            maximizeBtn?.setOnClickListener(View.OnClickListener {
                stopSelf()
                windowManager!!.removeView(floatView)
                val backToHome = Intent(this@FloatingWindowGFG, MainActivity::class.java)
                backToHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(backToHome)
            })



            //moves the floating window
            floatView!!.setOnTouchListener(object : OnTouchListener {
                val floatWindowLayoutUpdateParam: WindowManager.LayoutParams =
                    floatWindowLayoutParam as WindowManager.LayoutParams
                var x = 0.0
                var y = 0.0
                var px = 0.0
                var py = 0.0



                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    System.out.println("xcvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            x = floatWindowLayoutUpdateParam.x.toDouble()
                            y = floatWindowLayoutUpdateParam.y.toDouble()
                            px = event.rawX.toDouble()
                            py = event.rawY.toDouble()
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            isMoveAction = false
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val deltaX = (event.rawX - initialTouchX).toInt()
                            val deltaY = (event.rawY - initialTouchY).toInt()
                            floatWindowLayoutUpdateParam.x = (x + event.rawX - px).toInt()
                            floatWindowLayoutUpdateParam.y = (y + event.rawY - py).toInt()
                            windowManager!!.updateViewLayout(floatView, floatWindowLayoutUpdateParam)
                            if (Math.abs(deltaX) > 5 || Math.abs(deltaY) > 5) {
                                isMoveAction = true
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!isMoveAction) {
                                // If it's a tap (not a move), call the appropriate function
//                                if (maxi) {
//                                    minimizeOverlay()
//                                } else {
//                                    maximizeOverlay()
//                                }
                            }
                        }
                    }
                    return false
                }
            })

            // Create and register a BroadcastReceiver
//        broadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                val readingText = intent.getStringExtra("READING_TEXT")
//                readingTextView?.setText(readingText)
//            }
//        }
            val intentFilter = IntentFilter("READING_TEXT")
//        registerReceiver(broadcastReceiver, intentFilter)

//        glb files renderning

//        val glContainer = floatView!!.findViewById<FrameLayout>(R.id.gl_container)
//        println(glContainerx)
            println("dshchgsdvchvscghvsdghvsdvcdggcsvdfdf");
//        println(glContainer);

//        glContainer.layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT
//        );


//        val loadedglbContainer = floatView!!.findViewById<FrameLayout>(R.id.gl_container)
//        println(floatView!!.findViewById<FrameLayout>(R.id.gl_container))
//        println("globalContainer")
//        println(loadedglbContainer);
//        println("jhsdcbhdvchgdvcvhsvcvsgdcgcgvdg $loadedglbContainer")
//        val glSurfaceView = SurfaceView(this).apply {
//            loadedglbContainer
//        }
//        loadedglbContainer?.addView(glSurfaceView)

// Request a layout invalidation
            //    loadedglbContainer?.requestLayout()

            //ewj
//        val loadedglbContainer = GlobalContainerHolder.glContainer
//        println("jhsdcbhdvchgdvcvhsvcvsgdcgcgvdg $loadedglbContainer")
//        val glSurfaceView = SurfaceView(this)
//        loadedglbContainer.apply { loadedglbContainer}
//


//        val glSurfaceView = SurfaceView(this)
//        glContainer.addView(glSurfaceView)
//        choreographer = Choreographer.getInstance()
//        modelViewer = ModelViewer(glSurfaceView)
//        glSurfaceView.setOnTouchListener(modelViewer);
//        choreographer.postFrameCallback(frameCallback)
//
//        var namei =  "facial_expressions"
//        loadGltf(namei.toString())
//        modelViewer.scene.skybox = Skybox.Builder().build(modelViewer.engine)
//        loadEnvironment("venetian_crossroads_2k")
//        choreographer.postFrameCallback(frameCallback)


        return START_STICKY
    }




    private val frameCallback = object : Choreographer.FrameCallback {
        private var currentAnimationIndex = 0
        private val startTime = System.nanoTime()
        private var seconds = 0.0
        var count=0.0
        private var nextAnimationIndex = 1 // Index of the next animation to play

        override fun doFrame(currentTime: Long) {
            val elapsedSeconds = (currentTime - startTime).toDouble() / 1_000_000_000
            choreographer.postFrameCallback(this)
            modelViewer.animator?.apply {
                if (animationCount > 0) {
//                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                    System.out.println(modelViewer.animator?.getAnimationName(currentAnimationIndex));
                    val currentAnimationDuration = getAnimationDuration(currentAnimationIndex).toFloat()
                    val nextAnimationDuration = getAnimationDuration(nextAnimationIndex).toFloat()
                    val currentAnimationTime = (seconds - count) // Time spent in the current animation

                    // Calculate blend weight for current and next animations
                    val currentBlendWeight = 1.0f - (currentAnimationTime / currentAnimationDuration).toFloat()

                    val nextBlendWeight = 1.0f - ((nextAnimationDuration - currentAnimationTime) / nextAnimationDuration).toFloat()

                    applyAnimation(currentAnimationIndex, currentBlendWeight)
                    applyAnimation(nextAnimationIndex, nextBlendWeight)

                    // Check if current animation is about to end, and switch to the next animation
                    if (currentAnimationTime >= currentAnimationDuration - 1.0f) {
                        count += currentAnimationDuration
                        currentAnimationIndex = nextAnimationIndex
                        nextAnimationIndex = (currentAnimationIndex + 1) % animationCount
                    }
                    seconds = elapsedSeconds
                }
                updateBoneMatrices()
            }
            modelViewer.render(0)
        }
    }


    private fun loadGltf(name: String) {
        val buffer = readAsset("models/${name}.gltf")
        System.out.println("dddddddddddddddddddddddddddddddddddddddddddddd");
//        System.out.println(buffer);
        modelViewer.loadModelGltf(buffer) { uri -> readAsset("models/$uri") }
        modelViewer.transformToUnitCube()
        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
    }
    private fun readAsset(assetName: String): ByteBuffer {
        val input = assets.open(assetName)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }
    private fun loadEnvironment(ibl: String) {
        // Create the indirect light source and add it to the scene.
        var buffer = readAsset("envs/$ibl/${ibl}_ibl.ktx")
        KtxLoader.createIndirectLight(modelViewer.engine, buffer).apply {
            intensity = 50_000f
            modelViewer.scene.indirectLight = this
        }

        // Create the sky box and add it to the scene.
        buffer = readAsset("envs/$ibl/${ibl}_skybox.ktx")
        KtxLoader.createSkybox(modelViewer.engine, buffer).apply{
            modelViewer.scene.skybox = this
        }
    }


    override fun onBind(intent: Intent): IBinder? {
//        choreographer.postFrameCallback(frameCallback)
        return null
    }

    private fun finish() {
        System.exit(0)
    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
                //      bitmap.recycle()
                System.gc()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

//        windowManager!!.removeView(floatView)
//
//        // Unregister the BroadcastReceiver
//        unregisterReceiver(broadcastReceiver)
//        choreographer.removeFrameCallback(frameCallback)
        stopSelf()
        //modelViewer.release()
    }


}


