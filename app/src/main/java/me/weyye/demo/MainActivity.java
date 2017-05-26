package me.weyye.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.weyye.hilocation.HiLocation;
import me.weyye.hilocation.LocationListener;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //声明mLocationOption对象
//        List<PermissionItem> permissionItems = new ArrayList<>();
//
//        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "地理位置", R.drawable.permission_ic_location));
        HiPermission.create(this)
//                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }

                    @Override
                    public void onFinish() {
//                        HiLocation.create(MainActivity.this).callBack(MainActivity.this).interval(-1).start();
                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HiLocation.create(MainActivity.this).callBack(MainActivity.this).interval(-1).start();
            }
        });

    }


    @Override
    public void onSuccess(AMapLocation amapLocation) {
        //定位成功回调信息，设置相关消息
        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        amapLocation.getLatitude();//获取纬度
        amapLocation.getLongitude();//获取经度
        amapLocation.getAccuracy();//获取精度信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(amapLocation.getTime());
        df.format(date);//定位时间
        Log.i("tag", amapLocation.toString());
    }

    @Override
    public void onError(AMapLocationClient locationClient, AMapLocation amapLocation) {
//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        Log.e("AmapError", "location Error, ErrCode:"
                + amapLocation.getErrorCode() + ", errInfo:"
                + amapLocation.getErrorInfo());
    }
}
