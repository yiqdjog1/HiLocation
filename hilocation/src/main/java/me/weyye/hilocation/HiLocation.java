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
    private HiLocationListener mHiLocationListener;

    private static HiLocation mInstance;

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

    public static HiLocation with(Context context) {
        if (mInstance == null) {
            synchronized (HiLocation.class) {
                if (mInstance == null) {
                    mInstance = new HiLocation(context);
                }
            }
        }
        return mInstance;
    }

    public static HiLocation getInstance() {
        if (mInstance == null) {
            throw new IllegalArgumentException("请先调用HiLocation.with(Context context)");
        }
        return mInstance;
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
        mHiLocationListener.onDestory();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        return this;
    }

    public HiLocation callBack(LocationListener listener) {

        if (mHiLocationListener != null)//之前注册过 ，移除掉
            mLocationClient.unRegisterLocationListener(mHiLocationListener);
        mHiLocationListener = new HiLocationListener(this, listener);
        mLocationClient.setLocationListener(mHiLocationListener);
        return this;
    }

    public HiLocation onceLocation() {
        mIsOnceLocation = true;
        return this;
    }


    static class HiLocationListener implements AMapLocationListener {

        private LocationListener mListener;
        private WeakReference<HiLocation> mHiLocationWeakReference;
        private HiLocation mHiLocation;

        public HiLocationListener(HiLocation hiLocation, LocationListener listener) {
            mHiLocation = hiLocation;
            mListener = listener;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            int errorCode = aMapLocation.getErrorCode();
            if (errorCode == 0) {
                //只定位一次，停止定位
                if (mHiLocation.mIsOnceLocation)
                    mHiLocation.stop();
                //定位成功
                if (mListener != null)
                    mListener.onSuccess(aMapLocation);
            } else if (errorCode != 8) {
                //因为修改了jar改了包名，所以第一次定位会抛出异常,出现code为8的错误，所以直接过滤
                if (mListener != null)
                    mListener.onError(mHiLocation.mLocationClient, aMapLocation);
            }
        }

        public void onDestory() {
            mHiLocation = null;
        }
    }

}
