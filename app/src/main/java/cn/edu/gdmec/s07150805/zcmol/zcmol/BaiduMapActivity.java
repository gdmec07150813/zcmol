package cn.edu.gdmec.s07150805.zcmol.zcmol;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class BaiduMapActivity extends Activity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private Button requestLocButton;
    private RadioGroup group;
    private RadioButton r1;
    private RadioButton r2;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private BitmapDescriptor mCurrentMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        requestLocButton = (Button) findViewById(R.id.btn);
        group = (RadioGroup) findViewById(R.id.group);
        r1 = (RadioButton) findViewById(R.id.r1);r1.setChecked(true);
        r2 = (RadioButton) findViewById(R.id.r2);
        mCurrentMode = LocationMode.NORMAL;
        requestLocButton.setText("普通");

        //按钮的一个监听，改变改变定位图标的模式
        OnClickListener btnClickListener=new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL: //正常模式
                        requestLocButton.setText("跟随");
                        mCurrentMode=LocationMode.FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));

                        break;
                    case COMPASS:  //罗盘模式
                        requestLocButton.setText("普通");
                        mCurrentMode = LocationMode.NORMAL;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING: //跟随模式
                        requestLocButton.setText("罗盘");
                        mCurrentMode = LocationMode.COMPASS;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;

                    default:
                        break;
                }

            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        OnCheckedChangeListener radioButtonListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.r1) {
                    // 传入null则，恢复默认图标
                    mCurrentMarker = null;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, null));

                }else if (checkedId==R.id.r2) {
                    // 修改为自定义marker，即自定义图标
                    mCurrentMarker = BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_launcher);
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker,
                                    Color.RED,Color.YELLOW ));
                }

            }


        };
        group.setOnCheckedChangeListener(radioButtonListener);
        mMapView = (MapView) findViewById(R.id.bmapView); //找到我们的地图控件
        mBaiduMap = mMapView.getMap(); //获得地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); //设置为普通模式的地图
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);  //定位用到的一个类
        mLocClient.registerLocationListener(myListener); //注册监听

        ///LocationClientOption类用来设置定位SDK的定位方式，
        LocationClientOption option = new LocationClientOption(); //以下是给定位设置参数
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

    }
    boolean isFirstLoc = true; // 是否首次定位
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}