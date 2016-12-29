package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.PopupWindow;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
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
public class Index_ClassifyActivity extends Activity {
    private TextView index;
    private TextView find;
    private GridView new_class;
    private ImageView menu;
    private TextView exit;
    private TextView about;

    //传感器
    private SensorManager sensorManager;
    //振动器
    private Vibrator vibrator;
    private static final int SENSOR_SHAKE = 10;

    private PopupWindow popupWindow;
    private int from = 0;
    private String[] newClass_name = {"社会","国内","国际","娱乐","体育",
    "军事","科技","财经","时尚"};
    private String[] newClass = {"shehui","guonei","guoji","yule","tiyu",
            "junshi","keji","caijing","shishang"};
    private int[] newClass_img = {R.drawable.shehui,R.drawable.guonei,R.drawable.guoji,
    R.drawable.yule,R.drawable.tiyu,R.drawable.junshi,R.drawable.keji,R.drawable.caijing,
    R.drawable.shishang};
    //设置数据适配器
    private List<Map<String,Object>> list;
    private Map<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classify);

        index = (TextView) findViewById(R.id.classify_index);
        find = (TextView) findViewById(R.id.classify_find);
        new_class = (GridView) findViewById(R.id.new_class);
        menu = (ImageView) findViewById(R.id.menu);

        //摇一摇
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用此方法，menu不会顶置
                //popupWindow.showAsDropDown(v);
                initPopupWindow();
            }
        });
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Index_ClassifyActivity.this,IndexActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.out_to_right,R.anim.in_from_left);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Index_ClassifyActivity.this,Index_FindActivity.class);
                startActivity(intent);
            }
        });

        list = new ArrayList<>();
        for(int i=0;i<newClass_img.length;i++){
            map = new HashMap<>();
            map.put("new_class_img",newClass_img[i]);
            map.put("new_class_txt",newClass_name[i]);
            map.put("newClass",newClass[i]);
            list.add(map);
        }
        news();
    }
    static class ViewHolder{
        private ImageView new_class_img;
        private TextView new_class_txt;
        private String newClass = "";
    }
    //设置分类
    public void news() {
            class MyBaseAdapter extends BaseAdapter{
                private Context context;
                private List<Map<String,Object>> data;
                private LayoutInflater layoutInflater;
                public MyBaseAdapter(Context context,List<Map<String,Object>> data){
                    super();
                    this.context = context;
                    this.data = data;
                    this.layoutInflater = LayoutInflater.from(context);
                }
                @Override
                public int getCount() {
                    return data.size();
                }

                @Override
                public Object getItem(int position) {
                    return data.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHolder viewHolder = null;
                    if(convertView==null){
                        viewHolder = new ViewHolder();
                        convertView = layoutInflater.inflate(R.layout.classify_class,parent,false);
                        viewHolder.new_class_img = (ImageView) convertView.findViewById(R.id.new_class_img);
                        viewHolder.new_class_txt = (TextView) convertView.findViewById(R.id.new_class_txt);
                        convertView.setTag(viewHolder);
                    }else{
                        viewHolder = (ViewHolder) convertView.getTag();
                    }
                    viewHolder.new_class_img.setImageResource(Integer.parseInt(data.get(position).get("new_class_img").toString()));
                    viewHolder.new_class_txt.setText(data.get(position).get("new_class_txt").toString());
                    viewHolder.new_class_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("new_type",data.get(position).get("newClass").toString());
                            bundle.putInt("new_class_img",Integer.parseInt(data.get(position).get("new_class_img").toString()));
                            bundle.putString("new_class_txt",data.get(position).get("new_class_txt").toString());
                            Intent intent = new Intent(Index_ClassifyActivity.this,New_classActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    });
                    return convertView;
                }
            }
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(this,list);
        new_class.setAdapter(myBaseAdapter);
    }
    public void initPopupWindow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.menu, null);
        //内容，高度，宽度
        if(Location.BOTTOM.ordinal() == from){
            popupWindow = new PopupWindow(popupWindowView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
        }else{
            popupWindow = new PopupWindow(popupWindowView, LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, true);
        }
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationLeftFade);

        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);

        //显示位置
        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.LEFT, 0, 500);

        //设置背景半透明
        backgroundAlpha(0.5f);
        exit = (TextView) popupWindowView.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Index_ClassifyActivity.this);
                builder.setTitle("zcmol");
                builder.setMessage("是否确定退出？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Index_ClassifyActivity.this, MainActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Index_ClassifyActivity.this);
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

        popupWindowView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(float bgAlpha)
    {
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
                handler3.sendMessage(msg);
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
    Handler handler3 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                        /*Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", 2000).show();
                        Log.i("sensor", "检测到摇晃，执行操作！");*/
                    Intent intent=new Intent(Index_ClassifyActivity.this,Index_FindActivity.class);
                    startActivity(intent);
                    break;
            }
        }

    };
}