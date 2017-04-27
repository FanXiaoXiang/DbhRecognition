package com.bjfu.androidlib.engine.location.get.google;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.bjfu.androidlib.R;
import com.bjfu.androidlib.engine.location.get.utils.StatusChangeListener;
import com.bjfu.androidlib.engine.location.get.utils.Trigger;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 11827 on 2017/2/17.
 */

public class GoogleLocationService implements Trigger {
    private LocationManager lm = null;
    private GoogleLocation googleLocation;
    private StatusChangeListener listener;
    private Context context;
    private String TAG = "mLocation";

    public GoogleLocationService(Context context) {
        this.context = context;
        googleLocation = new GoogleLocation();
    }

    public void checkGpsProvider() {
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public boolean start(StatusChangeListener listener) {

        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        checkGpsProvider();

        setListener(listener);
        //为获取地理位置信息时设置查询条件
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        //获取位置信息
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        Location location = lm.getLastKnownLocation(bestProvider);
        //监听状态
        lm.addGpsStatusListener(gpsListener);
        //绑定监听，有4个参数
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        //参数2，位置信息更新周期，单位毫秒
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        //参数4，监听
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        return true;
    }

    public void stop() {
        if (lm == null) return;
        listener = null;
        lm.removeGpsStatusListener(gpsListener);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, R.string.gps_permisson_exception, Toast.LENGTH_SHORT).show();
            return;
        }
        lm.removeUpdates(locationListener);
        lm = null;
//        System.out.println("GPS stop");
    }

    private void setListener(StatusChangeListener listener) {
        this.listener = listener;
    }


    private void notifyStatusChange() {
        if (listener == null) return;
        listener.onStatusChange(googleLocation);
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    //位置监听
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            System.out.println("onLocationChanged");
            googleLocation.setLocation(location);
            notifyStatusChange();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//        System.out.println("onStatusChanged");
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    System.out.println("LocationProvider.AVAILABLE");
                    googleLocation.setStatus(GpsInitStatus.AVAILABLE);
                    notifyStatusChange();
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    System.out.println("LocationProvider.OUT_OF_SERVICE");
                    googleLocation.setStatus(GpsInitStatus.OUT_OF_SERVICE);
                    notifyStatusChange();
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    System.out.println("LocationProvider.TEMPORARILY_UNAVAILABLE");
                    googleLocation.setStatus(GpsInitStatus.TEMPORARILY_UNAVAILABLE);
                    notifyStatusChange();
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, R.string.gps_permisson_exception, Toast.LENGTH_SHORT).show();
                return;
            }
            googleLocation.setLocation(lm.getLastKnownLocation(provider));
            notifyStatusChange();
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
            googleLocation.setStatus(GpsInitStatus.TEMPORARILY_UNAVAILABLE);
            notifyStatusChange();
        }
    };

    //状态监听
    GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            System.out.println("onGpsStatusChanged");
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    googleLocation.setCurrentStatus(GpsCurrentStatus.GPS_EVENT_FIRST_FIX);
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    googleLocation.setCurrentStatus(GpsCurrentStatus.GPS_EVENT_SATELLITE_STATUS);
                    //获取当前状态
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, R.string.gps_permisson_exception, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    GpsStatus gpsStatus = lm.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    List<GpsSatellite> satellites = googleLocation.getGpsSatellites();
                    satellites.clear();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        satellites.add(iters.next());
                        count++;
                    }
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    googleLocation.setCurrentStatus(GpsCurrentStatus.GPS_EVENT_STARTED);
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    googleLocation.setCurrentStatus(GpsCurrentStatus.GPS_EVENT_STOPPED);
                    break;
            }
            notifyStatusChange();
        }
    };
}
