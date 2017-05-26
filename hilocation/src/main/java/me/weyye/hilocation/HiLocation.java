package me.weyye.hilocation;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class HiLocation {

    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mLocationClient;
    private Context mContext;
    private boolean mIsOnceLocation;
    private LocationListener mLocationListener;

    public HiLocation(Context context) {
        mContext = context;
        mLocationClient = new AMapLocationClient(mContext);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setInterval(1000);
        //设置定位监听
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

    public static HiLocation create(Context context) {
        return new HiLocation(context);
    }

    public HiLocation mode(AMapLocationClientOption.AMapLocationMode mode) {
        mLocationOption.setLocationMode(mode);
        return this;
    }

    public HiLocation timeOut(long time) {
        mLocationOption.setHttpTimeOut(time);
        return this;
    }

    public HiLocation interval(long time) {
        if (time == -1) {
            mIsOnceLocation = true;
        } else {

            mIsOnceLocation = false;
            mLocationOption.setInterval(time);
        }

        return this;
    }

    public HiLocation start() {
        mLocationClient.startLocation();
        return this;
    }

    public HiLocation stop() {
        mLocationClient.stopLocation();
        return this;
    }

    public HiLocation onDestory() {
        mLocationClient.onDestroy();
        return this;
    }

    public HiLocation callBack(LocationListener listener) {
        mLocationListener = listener;
        mLocationClient.setLocationListener(new HiLocationListener(this));
        return this;
    }

    public HiLocation onceLocation() {
        mIsOnceLocation = true;
        return this;
    }


    static class HiLocationListener implements AMapLocationListener {

        private WeakReference<HiLocation> mHiLocationWeakReference;
        private HiLocation lo;

        public HiLocationListener(HiLocation hiLocation) {
            mHiLocationWeakReference = new WeakReference<HiLocation>(hiLocation);
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            HiLocation hiLocation = mHiLocationWeakReference.get();
            int errorCode = aMapLocation.getErrorCode();
            if (errorCode == 0) {
                //只定位一次，停止定位
                if (hiLocation.mIsOnceLocation)
                    hiLocation.stop();
                //定位成功
                if (hiLocation.mLocationListener != null)
                    hiLocation.mLocationListener.onSuccess(aMapLocation);
            } else if (errorCode != 8) {
                //因为修改了jar改了包名，所以第一次定位会抛出异常,出现code为8的错误，所以直接过滤
                if (hiLocation.mLocationListener != null)
                    hiLocation.mLocationListener.onError(hiLocation.mLocationClient, aMapLocation);
            }
        }
    }

}
