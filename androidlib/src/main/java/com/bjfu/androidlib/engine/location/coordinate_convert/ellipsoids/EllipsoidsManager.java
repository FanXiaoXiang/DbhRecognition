package com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids;

import com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.paraments.Beijing_54;
import com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.paraments.GJ_2000;
import com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.paraments.WGS_84;
import com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.paraments.Xian_80;
import com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.utils.Ellipsoid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11827 on 2017/2/16.
 */

public class EllipsoidsManager {
    private static EllipsoidsManager ellipsoidsManager = null;
    private List<Ellipsoid> ellipsoidList;
    private final Ellipsoid[] ellipsoids;

    private EllipsoidsManager() {
        if (ellipsoidList == null) ellipsoidList = new ArrayList<Ellipsoid>();
        register(new Ellipsoid(GJ_2000.NAME, GJ_2000.LONG_AXLE, 1 / GJ_2000.F2));
        register(new Ellipsoid(Beijing_54.NAME, Beijing_54.LONG_AXLE, 1 / Beijing_54.F2));
        register(new Ellipsoid(Xian_80.NAME, Xian_80.LONG_AXLE, 1 / Xian_80.F2));
        register(new Ellipsoid(WGS_84.NAME, WGS_84.LONG_AXLE, 1 / WGS_84.F2));
        ellipsoids = getEllipsoidArray();
    }

    public static EllipsoidsManager getInstance() {
        synchronized (EllipsoidsManager.class) {
            if (ellipsoidsManager == null) {
                synchronized (EllipsoidsManager.class) {
                    ellipsoidsManager = new EllipsoidsManager();
                }
            }
        }
        return ellipsoidsManager;
    }

    private void register(Ellipsoid ellipsoid) {
        ellipsoidList.add(ellipsoid);
    }

    private boolean unregister(Ellipsoid ellipsoid) {
        if (ellipsoidList == null) return false;
        return ellipsoidList.remove(ellipsoid);
    }

    private Ellipsoid[] getEllipsoidArray() {
        if (ellipsoidList == null) return null;
        Ellipsoid[] es = new Ellipsoid[ellipsoidList.size()];
        for (int i = 0; i < ellipsoidList.size(); i++) {
            es[i] = ellipsoidList.get(i);
        }
        return es;
    }

    public final Ellipsoid[] getEllipsoids() {
        return ellipsoids;
    }
}
