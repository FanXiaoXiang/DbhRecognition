package com.bjfu.androidlib.engine.location.get.google;

import android.location.GpsSatellite;
import android.location.Location;

import com.bjfu.androidlib.engine.location.get.utils.ILocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11827 on 2017/2/19.
 */

public class GoogleLocation implements ILocation {
    private Location location;
    private GpsInitStatus status;
    private GpsCurrentStatus currentStatus;
    private List<GpsSatellite> gpsSatellites;

    public GoogleLocation() {
        gpsSatellites = new ArrayList<GpsSatellite>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GpsInitStatus getStatus() {
        return status;
    }

    public void setStatus(GpsInitStatus status) {
        this.status = status;
    }

    public GpsCurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GpsCurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<GpsSatellite> getGpsSatellites() {
        return gpsSatellites;
    }
}
