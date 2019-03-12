package com.globespinning.ionic.locationmanager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.globespinning.ionic.locationmanager.models.BackgroundLocation;
import com.globespinning.ionic.locationmanager.models.PluginException;

import android.app.Activity;
import android.content.Context;

public class GSLocationManager extends CordovaPlugin {

    public static final String ACTION_GET_CURRENT_LOCATION = "getCurrentLocation";

    private BackgroundGeolocationFacade facade;

    public static class ErrorPluginResult {
        public static PluginResult from(String message, int code) {
            JSONObject json = new JSONObject();
            try {
                json.put("code", code);
                json.put("message", message);
            } catch (JSONException e) {
                // not interested
            }
            return new PluginResult(PluginResult.Status.ERROR, json);
        }

        public static PluginResult from(String message, Throwable cause, int code) {
            JSONObject json = new JSONObject();
            try {
                json.put("code", code);
                json.put("message", message);
                json.put("cause", from(cause));
            } catch (JSONException e) {
                // not interested
            }
            return new PluginResult(PluginResult.Status.ERROR, json);
        }

        public static PluginResult from(PluginException e) {
            JSONObject json = new JSONObject();
            try {
                json.put("code", e.getCode());
                json.put("message", e.getMessage());
                if (e.getCause() != null) {
                    json.put("cause", from(e.getCause()));
                }
            } catch (JSONException ex) {
                // not interested
            }

            return new PluginResult(PluginResult.Status.ERROR, json);
        }

        private static JSONObject from(Throwable e) {
            JSONObject error = new JSONObject();
            try {
                error.put("message", e.getMessage());
            } catch (JSONException e1) {
                // not interested
            }
            return error;
        }
    }

    // at the initialize function, we can configure the tools we want to use later, like the sensors
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        facade = new BackgroundGeolocationFacade(getContext());
    }

    // safety unregistering from the events if the application stops somehow
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // this is the main part of the plugin, we have to handle all of the actions sent from the js
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (ACTION_GET_CURRENT_LOCATION.equals(action)) {
            runOnWebViewThread(new Runnable() {
                @Override
                public void run() {
                    int timeout = args.optInt(0, Integer.MAX_VALUE);
                    long maximumAge = args.optLong(1, Long.MAX_VALUE);
                    boolean enableHighAccuracy = args.optBoolean(2, false);
                    try {
                        BackgroundLocation location = facade.getCurrentLocation(timeout, maximumAge, enableHighAccuracy);
                        callbackContext.success(location.toJSONObject());
                    } catch (JSONException e) {
                        callbackContext.sendPluginResult(ErrorPluginResult.from(e.getMessage(), 2));
                    } catch (PluginException e) {
                        callbackContext.sendPluginResult(ErrorPluginResult.from(e));
                    }
                }
            });

            return true;
        }

        return false;  // Returning false results in a "MethodNotFound" error.
    }

    public Activity getActivity() {
        return cordova.getActivity();
    }

    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    private void runOnWebViewThread(Runnable runnable) {
        cordova.getThreadPool().execute(runnable);
    }
}
