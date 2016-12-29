package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class New_classActivity extends AppCompatActivity {
    //发送接收新闻类信息
    private String key = "&key=337f655e0d7c46443a126477a2e762e6";
    private String httpUrl = "http://v.juhe.cn/toutiao/index?type=";
    private String urlStr = "";
    final static int NEW_MESSAGE = 0;
    private List<Map<String,Object>> list;
    private Map<String,Object> map;
    //控件初始化
    private ImageView new_class_img;
    private TextView new_class_txt;
    private ListView new_class_list;
    private SetImageViewUtil[] setImageViewUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);
        new_class_img = (ImageView) findViewById(R.id.new_class_img);
        new_class_txt = (TextView) findViewById(R.id.new_class_txt);
        new_class_list = (ListView) findViewById(R.id.new_class_list);
        list = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        new_class_img.setImageResource(bundle.getInt("new_class_img"));
        new_class_txt.setText(bundle.getString("new_class_txt"));
        String newClass = bundle.getString("new_type");
        urlStr = httpUrl+newClass+key;
        sendMessage();
    }
   static class ViewHolder{
       private TextView title;
       private TextView date;
       private TextView from;
       private ImageView imageView;
       private String url;
       private int i=0;
   }
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
                    news(Jsonmessage);
                    baseAdapter();
            }
        }
    };
    public void news(String json){
        Gson gson = new Gson();
        News news = gson.fromJson(json,News.class);
        if(news.reason.equals("成功的返回")){
            setImageViewUtil  = new SetImageViewUtil[news.result.data.length];
            int i =0;
               for(Data resultData : news.result.data){
                   map = new HashMap<>();
                   map.put("title",resultData.title);
                   map.put("from",resultData.author_name);
                   map.put("date",resultData.date);
                   map.put("imageView",resultData.thumbnail_pic_s);
                   map.put("url",resultData.url);
                   map.put("i",i);
                   list.add(map);
                   i++;
               }
        }else{
            Toast.makeText(New_classActivity.this,"数据请求失败,错误码："+news.error_code,Toast.LENGTH_LONG).show();
        }
    }
    public void baseAdapter(){
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
                        Intent intent = new Intent(New_classActivity.this,New_toSeeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                return convertView;
            }
        }
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(this,list);
        new_class_list.setAdapter(myBaseAdapter);
    }
}
