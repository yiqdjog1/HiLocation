# HiLocation
一个基于高德定位sdk封装的定位库，无需配置免key

# 起因
现在几乎每个App都有定位，而一般为了简单我们会选择一些第三方定位sdk，像腾讯定位，百度定位以及高德定位
虽说是简单了不少，但是每次申请key，配置很繁琐。于是出现了`HiLocation`,无需繁琐的配置，一行代码搞定,就是这么任性!

# 初衷
只为大家更简单更方便的集成定位sdk，**我会不定时更新定位sdk**。[点击查看原理](/amap.md)

# Demo
[点击下载](/app-debug.apk)

# 使用

## Step1
``` java
dependencies {
    ...
    compile 'me.weyye:hilocation:1.0.2'
}
```
## Step2

### 开始定位

``` java
HiLocation.with(MainActivity.this).callBack(MainActivity.this).interval(1000).start();//每个1秒定位一次
```
如果只定位一次可以调用`onceLocation()`

``` java
HiLocation.with(MainActivity.this).callBack(MainActivity.this).onceLocation().start();//只定位一次
```

回调

``` java
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
    }
```

### 结束定位

``` java
HiLocation.with(MainActivity.this).stop();
//或者
//HiLocation.getInstance().stop();
```

## Step3
在合适的时间调用onDestory(),比如`Activity`
``` java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiLocation.with(MainActivity.this).onDestory();
        //或者
        //HiLocation.getInstance().onDestory();
    }
```
## 注意

* 6.0以上手机需要自己主动申请**定位和存储权限**

* 使用过程中6.0以上手机Logcat可能会看到报错`SecurityException` 缺少READ_PHONE_STATE权限。如果你不想看到这个错可以手动申请该权限



当然，为了保证我们在使用权限的时候没有后顾之忧，建议大家在每次启动app的时候申请一些常用权限，详情可以看[HiPermission](https://github.com/yewei02538/HiPermission)

# 最后

如果这个对你有帮助，就给个star呗~

# 联系我

* QQ:505141450
* Email:[hiweyye@gmail.com](mailto:hiweyye@gmail.com)
* My Blog:[http://weyye.me](http://weyye.me)
* 简书:[http://www.jianshu.com/u/c5da2f9c87fb](http://www.jianshu.com/u/c5da2f9c87fb)
* CSDN:[http://blog.csdn.net/yewei02538](http://blog.csdn.net/yewei02538)

# License
    Copyright (C) 2017 WeyYe

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
