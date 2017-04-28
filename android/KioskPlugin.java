package com.edumax.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.apache.cordova.*;
import android.widget.*;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import com.edumax.student.KioskActivity;
import org.json.JSONObject;
import android.content.Context;

public class KioskPlugin extends CordovaPlugin {
    
    public static final String EXIT_KIOSK = "exitKiosk";
    
    public static final String IS_IN_KIOSK = "isInKiosk";
    
        private Context ctx = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {

            if (IS_IN_KIOSK.equals(action)) {
                
                callbackContext.success(Boolean.toString(PrefUtils.isKioskModeActive(ctx)));
                return true;
                
            } else if (EXIT_KIOSK.equals(action)) {
                
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                
                Intent chooser = Intent.createChooser(intent, "Select destination...");                
                if (intent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
                    cordova.getActivity().startActivity(chooser);
                }
                PrefUtils.setKioskModeActive(false, ctx);

                callbackContext.success();
                return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }
}

