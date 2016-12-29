package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.adapter.BannerAdapter;
import com.google.gson.Gson;
import com.news.Data;
import com.news.News;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;


import android.widget.Toast;
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
 *
 * Created by JMC on 2016/12/21.
 */
public class IndexActivity extends Activity {
    private ListView new_index;
    private TextView classify;
    private TextView find;
    private ImageView menu;
    private ImageView chat;


    //传感器
    private SensorManager sensorManager;
    //振动器
    private Vibrator vibrator;
    private static final int SENSOR_SHAKE = 10;
    //
    /*private PopupWindow popupWindow;
    private int from = 0;*/
    //轮播-----------------------------------------------------------------------
    // 声明控件
    private ViewPager mViewPager;
    private List<ImageView> mlist;
    private TextView mTextView;
    private LinearLayout mLinearLayout;
    private SetImageViewUtil[] setImageViewUtil;
    private SetImageViewUtil[] setImageViewUtil2;
    // 新闻图片
    private String[] bannerImages = new String[4];
    // 新闻标题
    private String[] bannerTexts = new String[4];
    //新闻链接
    private String[] bannerUrl = new String[4];
    // ViewPager适配器与监听器
    private BannerAdapter mAdapter;
    private BannerListener bannerListener;

    // 圆圈标志位
    private int pointIndex = 0;
    // 线程标志
    private boolean isStop = false;
    private TextView exit;
    private TextView about;
    //新闻链接
    private String urlStr = "http://v.juhe.cn/toutiao/index?type=top&key=337f655e0d7c46443a126477a2e762e6";
    final static int NEW_MESSAGE = 0;
    private List<Map<String,Object>> list;
    private Map<String,Object> map;
    //----------------------------------------------------------------------------
    private PopupWindow popupWindow;
    private int from = 0;
    /*private PopupWindow popupWindow;
    private int from = 0;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        classify = (TextView) findViewById(R.id.index_classify);
        find = (TextView) findViewById(R.id.index_find);
        new_index = (ListView) findViewById(R.id.new_index);
        chat = (ImageView) findViewById(R.id.chat);
        list = new ArrayList<>();
        classify.setClickable(true);
        find.setClickable(true);

        //摇一摇
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        menu = (ImageView) findViewById(R.id.menu);

        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用此方法，menu不会顶置
                //popupWindow.showAsDropDown(v);
                initPopupWindow();
            }
        });

        classify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, Index_ClassifyActivity.class);
                startActivity(intent);
            }
        });

        find.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, Index_FindActivity.class);
                startActivity(intent);
            }
        });
        chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, chatActivity.class);
                startActivity(intent);
            }
        });
        //摇一摇----------------------------------------------------

        //-----------------------------------------------

        /*menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用此方法，menu不会顶置
                //popupWindow.showAsDropDown(v);
                initPopupWindow();
            }
        });
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 2016/5/17 构建一个popupwindow的布局
                View popupView = IndexActivity.this.getLayoutInflater().inflate(R.layout.menu, null);
                // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
                PopupWindow window = new PopupWindow(popupView, 600, 800);
                // TODO: 2016/5/17 设置动画
                window.setAnimationStyle(R.style.popup_window_anim);
                // TODO: 2016/5/17 设置可以获取焦点
                window.setFocusable(true);
                //TODO: 2016/5/17 设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#454545")));
                // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);
                // TODO：更新popupwindow的状态
                window.update();
                // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(menu, 0, 20);
            }
        });*/
        sendMessage();
        //轮播-----------------------------------------------------------------------------------
        initView();
        initData();
        initAction();
        // 开启新线程，2秒一次更新Banner
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
    }
    static class ViewHolder{
        private TextView title;
        private TextView date;
        private TextView from;
        private ImageView imageView;
        private String url;
        private int i=0;
    }
        //-----------------------------------------------------------------------------------------
        //轮播--------------------------------------------------------------------------------------------
    /**
     * 初始化事件
     */
    private void initAction() {
        bannerListener = new BannerListener();
        mViewPager.setOnPageChangeListener(bannerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mlist.size());
        //用来出发监听器
        mViewPager.setCurrentItem(index);
        mLinearLayout.getChildAt(pointIndex).setEnabled(true);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mlist = new ArrayList<ImageView>();
        View view;
        LayoutParams params;

            setImageViewUtil2 = new SetImageViewUtil[4];
            for (int i = 0; i < 4; i++) {
                // 设置广告图
                ImageView imageView = new ImageView(IndexActivity.this);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                setImageViewUtil2[i].setImageToImageView(imageView, bannerImages[i]);
                mlist.add(imageView);
                // 设置圆圈点
                view = new View(IndexActivity.this);
                params = new LayoutParams(15, 15);
                view.setBackgroundResource(R.drawable.new_selectbackground);
                view.setLayoutParams(params);
                view.setEnabled(false);

                mLinearLayout.addView(view);
            }

        mAdapter = new BannerAdapter(mlist);
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 初始化View操作
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTextView = (TextView) findViewById(R.id.tv_bannertext);
        mLinearLayout = (LinearLayout) findViewById(R.id.points);
    }

    //实现VierPager监听器接口
    class BannerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % 4;
            mTextView.setText(bannerTexts[newPosition]);
            mLinearLayout.getChildAt(newPosition).setEnabled(true);
            mLinearLayout.getChildAt(pointIndex).setEnabled(false);
            // 更新标志位
            pointIndex = newPosition;

        }

    }

    @Override
    protected void onDestroy() {
        // 关闭定时器
        isStop = true;
        super.onDestroy();
    }
       //-----------------------------------------------------------------------------------------------------
       public void sendMessage() {

           new Thread(new Runnable() {
               @Override
               public void run() {
                   HttpURLConnection connection = null;
                   OutputStream outputStream = null;
                   BufferedReader reader = null;
                   StringBuilder result = new StringBuilder();
                   String line = "";
                   try {
                       URL url = new URL(urlStr);
                       connection = (HttpURLConnection) url.openConnection();
                       connection.setRequestMethod("POST");
                       connection.setReadTimeout(5000);
                       connection.setConnectTimeout(5000);

                       outputStream = connection.getOutputStream();
                       outputStream.write(urlStr.getBytes());

                       reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                       while ((line = reader.readLine()) != null) {
                           result.append(line);
                       }

                       Message message = new Message();
                       message.obj = result.toString();
                       message.what = NEW_MESSAGE;
                       handler.sendMessage(message);
                   } catch (Exception e) {
                       e.printStackTrace();
                   } finally {
                       if (reader != null) {
                           try {
                               reader.close();
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                       if (outputStream != null) {
                           try {
                               outputStream.close();
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                       connection.disconnect();
                   }
               }
           }).start();
       }

    /*
     handler处理信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEW_MESSAGE:
                    String Jsonmessage = (String) msg.obj;
                    String text = "";
                    try {
                        JSONObject jsonObject = new JSONObject(Jsonmessage);
                        text = (String) jsonObject.get("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                   textView.setText(Jsonmessage);
                    //解释gson
                    resultGson(Jsonmessage);
                    baseAdapter();
            }
        }
    };
    //存储数据
    public void resultGson(String json){
        Gson gson = new Gson();
        News news = gson.fromJson(json,News.class);
        if(news.reason.equals("成功的返回")){
            setImageViewUtil  = new SetImageViewUtil[news.result.data.length];
            int i =0;
            for(Data resultData : news.result.data) {
                if(i<4){
                    bannerTexts[i] = resultData.title;
                    bannerUrl[i] = resultData.url;
                    bannerImages[i] = resultData.thumbnail_pic_s;
                }else {
                    map = new HashMap<>();
                    map.put("title", resultData.title);
                    map.put("from", resultData.author_name);
                    map.put("date", resultData.date);
                    map.put("imageView", resultData.thumbnail_pic_s);
                    map.put("url", resultData.url);
                    map.put("i", i);
                    list.add(map);
                }
                i++;
            }
            initData();
        }else{
            Toast.makeText(IndexActivity.this,"获取数据失败",Toast.LENGTH_LONG).show();
        }
    }
    public void baseAdapter(){
        class MyBaseAdapter extends BaseAdapter {
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
                    convertView = layoutInflater.inflate(R.layout.classify_news,parent,false);
                    viewHolder.date = (TextView) convertView.findViewById(R.id.new_date);
                    viewHolder.from = (TextView) convertView.findViewById(R.id.new_from);
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.new_images);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.new_title);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.title.setText(data.get(position).get("title").toString());
                viewHolder.from.setText(data.get(position).get("from").toString());
                viewHolder.date.setText(data.get(position).get("date").toString());
                //空指针异常
                setImageViewUtil[Integer.parseInt(data.get(position).get("i").toString())].setImageToImageView(viewHolder.imageView,data.get(position).get("imageView").toString());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("webHttp",data.get(position).get("url")+"");
                        bundle.putString("title",data.get(position).get("title")+"");
                        Intent intent = new Intent(IndexActivity.this,New_toSeeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                return convertView;
            }
        }
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(this,list);
        new_index.setAdapter(myBaseAdapter);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                    builder.setTitle("zcmol");
                    builder.setMessage("是否确定退出？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
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
                handler2.sendMessage(msg);
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
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                        /*Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", 2000).show();
                        Log.i("sensor", "检测到摇晃，执行操作！");*/
                    Intent intent=new Intent(IndexActivity.this,Index_ClassifyActivity.class);
                    startActivity(intent);
                    break;
            }
        }

    };
}

