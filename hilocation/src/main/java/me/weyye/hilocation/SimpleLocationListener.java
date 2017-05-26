package me.weyye.hilocation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public abstract class SimpleLocationListener implements LocationListener{
    @Override
    public void onError(AMapLocationClient locationClient, AMapLocation aMapLocation) {
        locationClient.stopLocation();
    }
}
