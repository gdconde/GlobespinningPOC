package com.globespinning.ionic.locationmanager;

import android.content.Context;
import android.location.Location;

import com.github.jparkie.promise.Promise;
import com.globespinning.ionic.locationmanager.models.BackgroundLocation;
import com.globespinning.ionic.locationmanager.models.PluginException;
import java.util.concurrent.TimeoutException;

public class BackgroundGeolocationFacade {

    private final Context mContext;

    public BackgroundGeolocationFacade(Context context) {
        mContext = context;
    }


    public BackgroundLocation getCurrentLocation(int timeout, long maximumAge, boolean enableHighAccuracy) throws PluginException {
        LocationManager locationManager = LocationManager.getInstance(getContext());
        Promise<Location> promise = locationManager.getCurrentLocation(timeout, maximumAge, enableHighAccuracy);
        try {
            promise.await();
            Location location = promise.get();
            if (location != null) {
                return BackgroundLocation.fromLocation(location);
            }

            Throwable error = promise.getError();
            if (error == null) {
                throw new PluginException("Location not available", 2); // LOCATION_UNAVAILABLE
            }
            if (error instanceof LocationManager.PermissionDeniedException) {
                throw new PluginException("Permission denied", 1); // PERMISSION_DENIED
            }
            if (error instanceof TimeoutException) {
                throw new PluginException("Location request timed out", 3); // TIME_OUT
            }

            throw new PluginException(error.getMessage(), 2); // LOCATION_UNAVAILABLE
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting location", e);
        }
    }

    private Context getContext() {
        return mContext;
    }

}
