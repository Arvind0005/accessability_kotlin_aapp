//package com.example.myapplication
//
//import android.annotation.SuppressLint
////import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.provider.Settings
//import android.view.ViewGroup
//import android.view.accessibility.AccessibilityManager
//import android.widget.Button
//import android.widget.FrameLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatDelegate
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//import java.io.IOException
//
//
//class SubMainActivity : AppCompatActivity() {
//    private var floatViewx: ViewGroup? = null
//
////    private var broadcastReceiver: BroadcastReceiver? = null
//    private lateinit var accessibilityService: MyAccessibilityService<Any?>
//    private var readingTextView: TextView? = null
//    private var dialog: AlertDialog? = null
//    private val floatView: ViewGroup? = null
//    private var overlay: Button? = null
//    private lateinit var myTextView: TextView
//    private lateinit var buttonspeak :Button
//    private lateinit var buttonsettings :Button
//    private var tv_Speech_to_text: TextView? = null
//    private val REQUEST_CODE_SPEECH_INPUT = 1
//    private var iv_mic: Button? = null
//    var resbody="";
//
//    companion object {
//
//        lateinit var glContainerx: FrameLayout
//    }
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//        val helppageIntent=Intent(this,MainActivity::class.java)
//        startActivity(helppageIntent);
//
////        val intent5 = Intent(this@MainActivity, SpeechToTextActivity::class.java);
////        startActivity(intent5);
//
//
////        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
////        floatViewx = inflater.inflate(R.layout.overlay_layout, null) as ViewGroup
//
////        glContainerx=floatViewx!!.findViewById<FrameLayout>(R.id.gl_container)
////        GlobalData.glContainer = floatView!!.findViewById<FrameLayout>(R.id.gl_container)
//        val receivedIntent = intent
//        var sharedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT)
//
////        if(!checkOverlayDisplayPermission())
////            requestOverlayDisplayPermission();
//
//
//        if (sharedText != null) {
//            val intent4 = Intent(this@SubMainActivity, FloatingWindowGFG::class.java);
//            intent4.putExtra("url", sharedText.toString());
//            startService(intent4);
//            finish();
//        };
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main)
////
////
////
////        myTextView = findViewById(R.id.text20)
//
////        broadcastReceiver = object : BroadcastReceiver() {
////            override fun onReceive(context: Context, intent: Intent) {
////                val readingText = intent?.getStringExtra("READING_TEXT")
////                myTextView.setText(readingText);
//////                val intent4 = Intent(this@MainActivity, FloatingWindowGFG::class.java)
//////                // FloatingWindowGFG service is started
//////                startService(intent4)
////                // Process the reading text here
////            }
////        }
////        buttonsettings=findViewById<Button>(R.id.button_settings);
////        buttonsettings.setOnClickListener(View.OnClickListener { // Create an intent to open the Accessibility settings
////            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
////            startActivity(intent)
////        })
//
////        val intentFilter = IntentFilter("READING_TEXT")
////        registerReceiver(broadcastReceiver, intentFilter)
//
//
//
//
//
//        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
//        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
//        val accessibilityServiceString = "$packageName/${MyAccessibilityService::class.java.canonicalName}"
//        val isServiceEnabled = enabledServices?.contains(accessibilityServiceString) == true
//
//        val MainserviceIntent = Intent(this, MyAccessibilityService::class.java)
//        startService(MainserviceIntent);
//
//
//
//
////        if (!isServiceEnabled) {
////            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
////            startActivity(intent)
////        }
//
//
//
//        accessibilityService = MyAccessibilityService();
//
////        iv_mic = findViewById<Button>(R.id.button_speak)
////
////        buttonsettings=findViewById<Button>(R.id.button_settings);
////        buttonsettings.setOnClickListener(View.OnClickListener { // Create an intent to open the Accessibility settings
//////            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//////            startActivity(intent)
////            val helppageIntent=Intent(this,HelpPage::class.java)
////            startActivity(helppageIntent);
////        })
////        var editText =findViewById<EditText>(R.id.edit_text_below_settings);
////        var userEnteredurl: String;
////
////        tv_Speech_to_text = findViewById<TextView>(R.id.tv_speech_to_text)
////        iv_mic?.setOnClickListener{
////            userEnteredurl=editText.text.toString();
////        }
//
////        iv_mic?.let { micButton ->
////            micButton.setOnClickListener(View.OnClickListener {
////                userEnteredurl=editText.text.toString();
////                println("heeeeeeeeeeeeeeeelo");
//////                wIntent= Intent(this,Webview_activity::class.java)
//////                startActivity(wIntent);
////                val intent4 = Intent(this@MainActivity, FloatingWindowGFG::class.java);
////
////                println(userEnteredurl);
////                if(userEnteredurl =="") {
////                    println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeennnnnnnnnnnnnnnnoo");
////                    intent4.putExtra("url", "https://deepvisiontech.ai/tnstartup/home.html");
////                }
////                else
////                    intent4.putExtra("url", userEnteredurl);
////                // FloatingWindowGFG service is started
////                startService(intent4)
//////                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//////                intent.putExtra(
//////                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//////                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//////                )
//////                intent.putExtra(
//////                    RecognizerIntent.EXTRA_LANGUAGE,
//////                    Locale.getDefault()
//////                )
//////                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
//////                try {
//////                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
//////                } catch (e: Exception) {
//////                    Toast
//////                        .makeText(
//////                            this, " " + e.message,
//////                            Toast.LENGTH_SHORT
//////                        )
//////                        .show()
//////                }
////            })
////        }
//
//    }
//
////    override fun onActivityResult(
////        requestCode: Int, resultCode: Int,
////        @Nullable data: Intent?
////    ) {
////        super.onActivityResult(requestCode, resultCode, data)
////        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
////            if (resultCode == RESULT_OK && data != null) {
////                val result = data.getStringArrayListExtra(
////                    RecognizerIntent.EXTRA_RESULTS
////                )
////                tv_Speech_to_text!!.text =
////                    Objects.requireNonNull(result)?.get(0) ?: null
////            }
////        }
////    }
//     private fun fetchCaptionsFromAPI(youtubeUrl: String) {
//        // Define your Flask API endpoint
//        val apiEndpoint = "https://f556-103-185-239-79.ngrok-free.app/get_captions?url=$youtubeUrl"
//
//
//        // Create an instance of OkHttpClient
//        val client = OkHttpClient()
//
//        // Create a request for the API endpoint
//        val request = Request.Builder()
//            .url(apiEndpoint)
//            .build()
//
//        // Make the GET request asynchronously
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                // Handle the failure, e.g., show an error message
//                runOnUiThread {
//                    // Update your UI here with the error message if needed
//                    // For example, you can show a Toast message
//                    println(e.message)
//                    Toast.makeText(this@SubMainActivity, "Failed to retrieve captions: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                // Handle the successful response
//                val responseBody = response.body?.string()
//
//                runOnUiThread {
//                    // Update your UI with the response (captions)
//                    // For example, you can set the text of a TextView
//                    readingTextView?.text = responseBody
//                    println("uithereeeeeeeeeeeeeeeeed");
//                    myTextView.text = responseBody;
//                    println(responseBody);
//                }
//                println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                println(responseBody);
//            }
//        })
//    }
//
//    private fun requestOverlayDisplayPermission() {
//        // An AlertDialog is created
//        val builder = AlertDialog.Builder(this)
//
//        // This dialog can be closed, just by taping
//        // anywhere outside the dialog-box
//        builder.setCancelable(true)
//
//        // The title of the Dialog-box is set
//        builder.setTitle("Screen Overlay Permission Needed")
//
//        // The message of the Dialog-box is set
//        builder.setMessage("Enable 'Display over other apps' from System Settings.")
//
//        // The event of the Positive-Button is set
//        builder.setPositiveButton(
//            "Open Settings"
//        ) { dialog, which -> // The app will redirect to the 'Display over other apps' in Settings.
//            // This is an Implicit Intent. This is needed when any Action is needed
//            // to perform, here it is
//            // redirecting to an other app(Settings).
//            val intent = Intent(
//                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                Uri.parse("package:$packageName")
//            )
//
//            // This method will start the intent. It takes two parameter, one is the Intent and the other is
//            // an requestCode Integer. Here it is -1.
//            startActivityForResult(intent, RESULT_OK)
//        }
//        dialog = builder.create()
//        // The Dialog will
//        // show in the screen
//        dialog?.show()
//    }
//
//    private fun checkOverlayDisplayPermission(): Boolean {
//        // Android Version is lesser than Marshmallow or
//        // the API is lesser than 23
//        // doesn't need 'Display over other apps' permission enabling.
//        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            // If 'Display over other apps' is not enabled
//            // it will return false or else true
//            if (!Settings.canDrawOverlays(this)) {
//                false
//            } else {
//                true
//            }
//        } else {
//            true
//        }
//    }
//}