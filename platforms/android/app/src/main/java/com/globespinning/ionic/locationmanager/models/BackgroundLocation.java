package com.globespinning.ionic.locationmanager.models;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BackgroundLocation implements Parcelable {
    public static final int POST_PENDING = 1;

    private Long locationId = null;
    private Integer locationProvider = null;
    private Long batchStartMillis = null;
    private String provider;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private long time = 0;
    private long elapsedRealtimeNanos = 0;
    private float accuracy = 0.0f;
    private float speed = 0.0f;
    private float bearing = 0.0f;
    private double altitude = 0.0f;
    private float radius = 0.0f;
    private boolean hasAccuracy = false;
    private boolean hasAltitude = false;
    private boolean hasSpeed = false;
    private boolean hasBearing = false;
    private boolean hasRadius = false;
    private int status = POST_PENDING;
    private Bundle extras = null;

    private static final long TWO_MINUTES_IN_NANOS = 1000000000L * 60 * 2;

    public BackgroundLocation() {}

    /**
     * Construct BackgroundLocation by copying properties from android Location.
     * @param location
     */
    @Deprecated
    public BackgroundLocation(Location location) {
        this(BackgroundLocation.fromLocation(location));
    }

    @Deprecated
    public BackgroundLocation(Integer locationProvider, Location location) {
        this(location);
        this.locationProvider = locationProvider;
    }

    /**
     * Construct stationary BackgroundLocation.
     * @param locationProvider
     * @param location
     * @param radius radius of stationary region
     */
    @Deprecated
    public BackgroundLocation(Integer locationProvider, Location location, float radius) {
        this(locationProvider, location);
        setRadius(radius);
    }

    /**
     * Construct a new Location object that is copied from an existing one.
     * @param location
     */
    public BackgroundLocation(BackgroundLocation l) {
        locationId = l.locationId;
        locationProvider = l.locationProvider;
        batchStartMillis = l.batchStartMillis;
        provider = l.provider;
        latitude = l.latitude;
        longitude = l.longitude;
        time = l.time;
        elapsedRealtimeNanos = l.elapsedRealtimeNanos;
        accuracy = l.accuracy;
        speed = l.speed;
        bearing = l.bearing;
        altitude = l.altitude;
        radius = l.radius;
        hasAccuracy = l.hasAccuracy;
        hasAltitude = l.hasAltitude;
        hasSpeed = l.hasSpeed;
        hasBearing = l.hasBearing;
        hasRadius = l.hasRadius;
        status = l.status;
        extras = (l.extras == null) ? null : new Bundle(l.extras);
    }

    private static BackgroundLocation fromParcel(Parcel in) {
        BackgroundLocation l = new BackgroundLocation();

        l.locationId = in.readLong();
        l.locationProvider = in.readInt();
        l.batchStartMillis = in.readLong();
        l.provider = in.readString();
        l.latitude = in.readDouble();
        l.longitude = in.readDouble();
        l.time = in.readLong();
        l.elapsedRealtimeNanos = in.readLong();
        l.accuracy = in.readFloat();
        l.speed = in.readFloat();
        l.bearing = in.readFloat();
        l.altitude = in.readDouble();
        l.radius = in.readFloat();
        l.hasAccuracy = in.readInt() != 0;
        l.hasAltitude = in.readInt() != 0;
        l.hasSpeed = in.readInt() != 0;
        l.hasBearing = in.readInt() != 0;
        l.hasRadius = in.readInt() != 0;
        l.status = in.readInt();
        l.extras = in.readBundle();

        return l;
    }

    public static BackgroundLocation fromLocation(Location location) {
        BackgroundLocation l = new BackgroundLocation();

        l.provider = location.getProvider();
        l.latitude = location.getLatitude();
        l.longitude = location.getLongitude();
        l.time = location.getTime();
        l.accuracy = location.getAccuracy();
        l.speed = location.getSpeed();
        l.bearing = location.getBearing();
        l.altitude = location.getAltitude();
        l.hasAccuracy = location.hasAccuracy();
        l.hasAltitude = location.hasAltitude();
        l.hasSpeed = location.hasSpeed();
        l.hasBearing = location.hasBearing();
        l.extras = location.getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            l.elapsedRealtimeNanos = location.getElapsedRealtimeNanos();
        }

        return l;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(locationId);
        dest.writeInt(locationProvider);
        dest.writeLong(batchStartMillis);
        dest.writeString(provider);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(time);
        dest.writeLong(elapsedRealtimeNanos);
        dest.writeFloat(accuracy);
        dest.writeFloat(speed);
        dest.writeFloat(bearing);
        dest.writeDouble(altitude);
        dest.writeFloat(radius);
        dest.writeInt(hasAccuracy ? 1 : 0);
        dest.writeInt(hasAltitude ? 1 : 0);
        dest.writeInt(hasSpeed ? 1 : 0);
        dest.writeInt(hasBearing ? 1 : 0);
        dest.writeInt(hasRadius ? 1 : 0);
        dest.writeInt(status);
        dest.writeBundle(extras);
    }

    public static final Parcelable.Creator<BackgroundLocation> CREATOR
            = new Parcelable.Creator<BackgroundLocation>() {
        public BackgroundLocation createFromParcel(Parcel in) {
            return BackgroundLocation.fromParcel(in);
        }
        public BackgroundLocation[] newArray(int size) {
            return new BackgroundLocation[size];
        }
    };

    /**
     * Returns the name of the provider that generated this fix.
     * @return the provider, or null if it has not been set
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Return the UTC time of this fix, in milliseconds since January 1, 1970.
     *
     * <p>Note that the UTC time on a device is not monotonic: it
     * can jump forwards or backwards unpredictably. So always use
     * {@link #getElapsedRealtimeNanos} when calculating time deltas.
     *
     * <p>On the other hand, {@link #getTime} is useful for presenting
     * a human readable time to the user, or for carefully comparing
     * location fixes across reboot or across devices.
     *
     * <p>All locations generated by the {@link com.globespinning.ionic.locationmanager.LocationManager}
     * are guaranteed to have a valid UTC time, however remember that
     * the system time may have changed since the location was generated.
     *
     * @return time of fix, in milliseconds since January 1, 1970.
     */
    public long getTime() {
        return time;
    }

    /**
     * Set the UTC time of this fix, in milliseconds since January 1,
     * 1970.
     *
     * @param time UTC time of this fix, in milliseconds since January 1, 1970
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Return the time of this fix, in elapsed real-time since system boot.
     *
     * <p>This value can be reliably compared to
     * {@link android.os.SystemClock#elapsedRealtimeNanos},
     * to calculate the age of a fix and to compare Location fixes. This
     * is reliable because elapsed real-time is guaranteed monotonic for
     * each system boot and continues to increment even when the system
     * is in deep sleep (unlike {@link #getTime}.
     *
     * <p>All locations generated by the {@link com.globespinning.ionic.locationmanager.LocationManager}
     * are guaranteed to have a valid elapsed real-time.
     *
     * @return elapsed real-time of fix, in nanoseconds since system boot.
     */
    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    /**
     * Get the estimated accuracy of this location, in meters.
     *
     * <p>We define accuracy as the radius of 68% confidence. In other
     * words, if you draw a circle centered at this location's
     * latitude and longitude, and with a radius equal to the accuracy,
     * then there is a 68% probability that the true location is inside
     * the circle.
     *
     * <p>In statistical terms, it is assumed that location errors
     * are random with a normal distribution, so the 68% confidence circle
     * represents one standard deviation. Note that in practice, location
     * errors do not always follow such a simple distribution.
     *
     * <p>This accuracy estimation is only concerned with horizontal
     * accuracy, and does not indicate the accuracy of bearing,
     * velocity or altitude if those are included in this Location.
     *
     * <p>If this location does not have an accuracy, then 0.0 is returned.
     * All locations generated by the {@link com.globespinning.ionic.locationmanager.LocationManager} include
     * an accuracy.
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * Get the speed if it is available, in meters/second over ground.
     *
     * <p>If this location does not have a speed then 0.0 is returned.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Set the speed, in meters/second over ground.
     *
     * <p>Following this call {@link #hasSpeed} will return true.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        this.hasSpeed = true;
    }

    /**
     * Return radius of stationary region.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Sets radius of stationary region.
     */
    public void setRadius(float radius) {
        this.radius = radius;
        this.hasRadius = true;
    }

    /**
     * Return android Location instance
     *
     * @return android.location.Location instance
     */
    public Location getLocation() {
        Location l = new Location(provider);
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        l.setTime(time);
        if (hasAccuracy) l.setAccuracy(accuracy);
        if (hasAltitude) l.setAltitude(altitude);
        if (hasSpeed) l.setSpeed(speed);
        if (hasBearing) l.setBearing(bearing);
        l.setExtras(extras);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            l.setElapsedRealtimeNanos(elapsedRealtimeNanos);
        }

        return l;
    }

    /** Determines whether one Location reading is better than the current Location fix
     *
     * Origin: https://developer.android.com/guide/topics/location/strategies.html
     *
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    public static boolean isBetterLocation(BackgroundLocation location, BackgroundLocation currentBestLocation) {
        if (location == null) {
            return false;
        }
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        long timeDeltaInNanos = 0;
        // Check whether the new location fix is newer or older
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // because getTime is not monotonic
            timeDeltaInNanos = location.getElapsedRealtimeNanos() - currentBestLocation.getElapsedRealtimeNanos();
        } else {
            // unfortunately there is no other way for pre JELLY_BEAN_MR1 (API Level 17)
            timeDeltaInNanos = (location.getTime() - currentBestLocation.getTime()) * 1000000;
        }

        boolean isSignificantlyNewer = timeDeltaInNanos > TWO_MINUTES_IN_NANOS;
        boolean isSignificantlyOlder = timeDeltaInNanos < -TWO_MINUTES_IN_NANOS;
        boolean isNewer = timeDeltaInNanos > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public String toString () {
        StringBuilder s = new StringBuilder();
        s.append("BGLocation[").append(provider);
        s.append(String.format(" %.6f,%.6f", latitude, longitude));
        s.append(" id=").append(locationId);
        if (hasAccuracy) {
            s.append(String.format(" acc=%.0f", accuracy));
        } else {
            s.append(" acc=???");
        }
        if (time == 0) {
            s.append(" t=?!?");
        } else {
            s.append(" t=").append(time);
        }
        if (elapsedRealtimeNanos == 0) {
            s.append(" et=?!?");
        } else {
            s.append(" et=");
            TimeUtils.formatDuration(elapsedRealtimeNanos / 1000000L, s);
        }
        if (hasAltitude) s.append(" alt=").append(altitude);
        if (hasSpeed) s.append(" vel=").append(speed);
        if (hasBearing) s.append(" bear=").append(bearing);
        if (hasRadius) s.append(" radius=").append(radius);
        if (extras != null) {
            s.append(" {").append(extras).append('}');
        }
        s.append(" locprov=").append(locationProvider);
        s.append("]");

        return s.toString();
    }

    /**
     * Returns location as JSON object.
     * @throws JSONException
     */
    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("provider", provider);
        json.put("locationProvider", locationProvider);
        json.put("time", time);
        json.put("latitude", latitude);
        json.put("longitude", longitude);
        if (hasAccuracy) json.put("accuracy", accuracy);
        if (hasSpeed) json.put("speed", speed);
        if (hasAltitude) json.put("altitude", altitude);
        if (hasBearing) json.put("bearing", bearing);
        if (hasRadius) json.put("radius", radius);

        return json;
    }

}

