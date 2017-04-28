/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.edumax.student;

import android.os.Bundle;
import org.apache.cordova.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.app.ActivityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.view.ViewGroup;
import android.view.*;
import android.content.Context;
import android.graphics.*;
import java.util.concurrent.TimeUnit;
import android.util.Log;
import android.provider.Settings;
import android.os.Build;
import android.net.Uri;

public class MainActivity extends CordovaActivity
{
   /* @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);
    }*/

    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    private Button hiddenExitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
       // setContentView(R.layout.activity_main);

        // every time someone enters the kiosk mode, set the flag true
        PrefUtils.setKioskModeActive(true, getApplicationContext());

       /* hiddenExitButton = (Button) findViewById(R.id.hiddenExitButton);
        hiddenExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Break out!
                PrefUtils.setKioskModeActive(false, getApplicationContext());
                Toast.makeText(getApplicationContext(),"You can leave the app now!", Toast.LENGTH_SHORT).show();
            }
        });*/

        loadUrl(launchUrl);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Please give my app this permission!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                disableStatusBar();
            }
        }
        else {
            disableStatusBar();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
    }

     @Override
public void onStop() {
  super.onStop();
  Log.i(TAG, "Invoking onStop'");
   /* try {
          Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        }catch (InterruptedException e) {
          Log.i(TAG, "Thread interrupted: 'MainActivity'");
        }*/

        // nothing to do here
        // … really
    }

@Override
public void onPause(){
  super.onPause();
  //((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);

}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public static void preventStatusBarExpansion(Context context) {
    WindowManager manager = ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
    localLayoutParams.gravity = Gravity.TOP;
    localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

    localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

    int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    int result = 0;
    if (resId > 0) {
      result = context.getResources().getDimensionPixelSize(resId);
    } else {
      // Use Fallback size:
      result = 60; // 60px Fallback
    }

    localLayoutParams.height = result;
    localLayoutParams.format = PixelFormat.TRANSPARENT;

    CustomViewGroup view = new CustomViewGroup(context);
    manager.addView(view, localLayoutParams);
}

public static final int OVERLAY_PERMISSION_REQ_CODE = 4545;
protected CustomViewGroup blockingView = null;

/*
@Override
protected void onDestroy() {

    super.onDestroy();

    if (blockingView!=null) {
        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        manager.removeView(blockingView);
    }
}


@Override
protected void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Please give my app this permission!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                disableStatusBar();
            }
        }
        else {
            disableStatusBar();
        }
}
*/

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "User can access system settings without this permission!", Toast.LENGTH_SHORT).show();
        }
        else
        { disableStatusBar();
        }
    }
}

protected void disableStatusBar() {

    WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
    localLayoutParams.gravity = Gravity.TOP;
    localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

            // this is to enable the notification to receive touch events
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

            // Draws over status bar
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

    localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
    localLayoutParams.height = (int) (40 * getResources().getDisplayMetrics().scaledDensity);
    localLayoutParams.format = PixelFormat.TRANSPARENT;

    blockingView = new CustomViewGroup(this);
    manager.addView(blockingView, localLayoutParams);
}

public static class CustomViewGroup extends ViewGroup {
    public CustomViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Intercepted touch!
        return true;
    }
}
}
