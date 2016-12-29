package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.Random;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by JMC on 2016/12/21.
 */
public class Index_FindActivity extends Activity {
    private TextView classify;
    private TextView index;
    private RelativeLayout robot, weather, position;
    private ImageView menu;
    private PopupWindow popupWindow;
    private int from = 0;
    private TextView exit;
    private TextView about;

    //传感器
    private SensorManager sensorManager;
    //振动器
    private Vibrator vibrator;
    private static final int SENSOR_SHAKE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
        classify = (TextView) findViewById(R.id.find_classify);
        index = (TextView) findViewById(R.id.find_index);
        position = (RelativeLayout) findViewById(R.id.position);


        //摇一摇
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index_FindActivity.this, Index_ClassifyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index_FindActivity.this, IndexActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
        //点击功能服务
        robot = (RelativeLayout) findViewById(R.id.robot);
        weather = (RelativeLayout) findViewById(R.id.weather);
        robot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index_FindActivity.this, RobotActivity.class);
                startActivity(intent);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index_FindActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index_FindActivity.this, BaiduMapActivity.class);
                startActivity(intent);
            }
        });
        menu = (ImageView) findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用此方法，menu不会顶置
                //popupWindow.showAsDropDown(v);
                initPopupWindow();
            }
        });
    }

    public void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.menu, null);
        //内容，高度，宽度
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        } else {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        }
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationLeftFade);

        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);

        //显示位置
        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.find, null), Gravity.LEFT, 0, 500);

        //设置背景半透明
        backgroundAlpha(0.5f);
        exit = (TextView) popupWindowView.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Index_FindActivity.this);
                builder.setTitle("zcmol");
                builder.setMessage("是否确定退出？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Index_FindActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        about = (TextView) popupWindowView.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Index_FindActivity.this);
                builder.setTitle("zcmol");
                builder.setMessage("版本：Adroid 1.0\n\n制作者：冯业佳、陈敬民");
                builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        //关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    // 菜单弹出方向
    public enum Location {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;

    }


    //摇一摇切屏-------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //记得在不用的时候关掉传感器，因为手机黑屏是不会自动关掉传感器的，当然如果你觉得电量一直都很足，那算我多嘴咯。
    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            //设备坐标系是固定于设备的，与设备的方向（在世界坐标系中的朝向）无关
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                //设置震动时长
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler4.sendMessage(msg);
            }
        }

        //当传感器精度的改变时  进行的操作
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 动作执行
     */
    Handler handler4 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                        /*Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", 2000).show();
                        Log.i("sensor", "检测到摇晃，执行操作！");*/
                    Intent intent = new Intent(Index_FindActivity.this, IndexActivity.class);
                    startActivity(intent);
                    break;
            }
        }

    };
}