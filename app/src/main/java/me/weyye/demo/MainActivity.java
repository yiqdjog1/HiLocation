package me.weyye.demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.weyye.hilocation.HiLocation;
import me.weyye.hilocation.LocationListener;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tvStart).setOnClickListener(this);
        findViewById(R.id.tvStop).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiLocation.with(MainActivity.this).onDestory();
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
        Toast.makeText(MainActivity.this, amapLocation.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(AMapLocationClient locationClient, AMapLocation amapLocation) {
//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        Log.e("AmapError", "location Error, ErrCode:"
                + amapLocation.getErrorCode() + ", errInfo:"
                + amapLocation.getErrorInfo());
        HiLocation.getInstance().stop();//停止定位
        //或者
        //locationClient.stopLocation();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvStart:
                List<PermissionItem> permissionItems = new ArrayList<>();
                permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "地理位置", R.drawable.permission_ic_location));
                permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "存储权限", R.drawable.permission_ic_storage));
                permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone));
                HiPermission.create(this)
                        .permissions(permissionItems)
                        .checkMutiPermission(new PermissionCallback() {
                            @Override
                            public void onClose() {
                            }

                            @Override
                            public void onFinish() {
                                //每2秒定位一次
                                HiLocation.with(MainActivity.this).callBack(MainActivity.this).interval(2000).start();
                            }

                            @Override
                            public void onDeny(String permission, int position) {

                            }

                            @Override
                            public void onGuarantee(String permission, int position) {

                            }
                        });
                break;
            case R.id.tvStop:
                //停止定位
                HiLocation.with(MainActivity.this).stop();
                break;
        }


    }
}
