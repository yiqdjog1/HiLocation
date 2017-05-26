package me.weyye.hilocation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public interface LocationListener {
    void onSuccess(AMapLocation aMapLocation);

    void onError(AMapLocationClient locationClient, AMapLocation aMapLocation);
}
