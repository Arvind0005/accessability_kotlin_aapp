package com.example.myapplication

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance


class MyAccessibilityService : AccessibilityService() {
    private val capturedTextLiveData = MutableLiveData<String>()

    fun getCapturedTextLiveData(): LiveData<String> {
        return capturedTextLiveData
    }
    override fun onInterrupt() {}
    override fun onServiceConnected() {
        super.onServiceConnected()
        println("Accessibility was connected!")
    }

    /*EventType: TYPE_WINDOW_CONTENT_CHANGED; EventTime: 147911568;
    PackageName: com.android.systemui; MovementGranularity: 0; Action: 0;
    ContentChangeTypes: [CONTENT_CHANGE_TYPE_TEXT, CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION];
    WindowChangeTypes: [] [ ClassName: android.widget.FrameLayout; Text: [];
    ContentDescription: Network Speed Status Bar Item 18 KB/s; ItemCount: -1;
    CurrentItemIndex: -1; Enabled: true; Password: false; Checked: false;
    FullScreen: false; Scrollable: false; BeforeText: null; FromIndex: -1;
    ToIndex: -1; ScrollX: 0; ScrollY: 0; MaxScrollX: 0; MaxScrollY: 0; ScrollDeltaX: -1;
    ScrollDeltaY: -1; AddedCount: -1; RemovedCount: -1; ParcelableData: null ]; recordCount: 1

     EventType: TYPE_ANNOUNCEMENT; EventTime: 148797623; PackageName: com.oppo.launcher;
     MovementGranularity: 0; Action: 0; ContentChangeTypes: [];
     WindowChangeTypes: [] [ ClassName: android.widget.ScrollView; Text: [Page 2 of 5];
     ContentDescription: null; ItemCount: -1; CurrentItemIndex: -1;
     Enabled: true; Password: false; Checked: false; FullScreen: false; Scrollable: true;
     BeforeText: null; FromIndex: -1; ToIndex: -1; ScrollX: 1033; ScrollY: 0; MaxScrollX: 0;
     MaxScrollY: 0; ScrollDeltaX: -1; ScrollDeltaY: -1; AddedCount: -1; RemovedCount: -1;
     ParcelableData: null ]; recordCount: 0

     EventType: TYPE_VIEW_CLICKED; EventTime: 148853573; PackageName: com.android.systemui
     MovementGranularity: 0; Action: 0; ContentChangeTypes: [];
     WindowChangeTypes: [] [ ClassName: android.widget.Button; Text: [];
     ContentDescription: Screen Lock; ItemCount: -1;
     CurrentItemIndex: -1; Enabled: true; Password: false;
     Checked: false; FullScreen: false; Scrollable: false; BeforeText: null;
     FromIndex: -1; ToIndex: -1; ScrollX: 0; ScrollY: 0; MaxScrollX: 0; MaxScrollY: 0;
     ScrollDeltaX: -1; ScrollDeltaY: -1; AddedCount: -1; RemovedCount: -1; ParcelableData: null ];
     recordCount: 0


     EventType: TYPE_VIEW_FOCUSED; EventTime: 148776307; PackageName: com.android.settings; MovementGranularity: 0; Action: 0;
     ContentChangeTypes: []; WindowChangeTypes: [] [ ClassName: androidx.recyclerview.widget.RecyclerView;
     Text: []; ContentDescription: null; ItemCount: 32; CurrentItemIndex: -1;
     Enabled: true; Password: false; Checked: false; FullScreen: false; Scrollable: true;
     BeforeText: null; FromIndex: 19; ToIndex: 31; ScrollX: 0; ScrollY: 0; MaxScrollX: 0;
     MaxScrollY: 0; ScrollDeltaX: -1; ScrollDeltaY: -1; AddedCount: -1; RemovedCount: -1;
     ParcelableData: null ]; recordCount: 0


     EventType: TYPE_ANNOUNCEMENT; EventTime: 148797623; PackageName: com.oppo.launcher;
     MovementGranularity: 0; Action: 0; ContentChangeTypes: [];
     WindowChangeTypes: [] [ ClassName: android.widget.ScrollView; Text: [Page 2 of 5];
     ContentDescription: null; ItemCount: -1; CurrentItemIndex: -1; Enabled: true;
     Password: false; Checked: false; FullScreen: false; Scrollable: true;
     BeforeText: null; FromIndex: -1; ToIndex: -1; ScrollX: 1033; ScrollY: 0;
     MaxScrollX: 0; MaxScrollY: 0; ScrollDeltaX: -1; ScrollDeltaY: -1; AddedCount: -1;
     RemovedCount: -1; ParcelableData: null ]; recordCount: 0*/
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val eventType = event?.eventType
        val text = event?.text
        print(event);
        if (text != null) {
            if(text.isEmpty()) {
                print("cdhvgvdgdvgcdvgvcgdcddc");
                print(text);
            }
        }
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                System.out.println("testing");
                val text = event.text?.joinToString("\n") { it.toString() }
                System.out.println(text);
                capturedTextLiveData.postValue(text)
                // Update UI or perform actions based on the captured text
            }
            // Handle other event types...
        }
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