package cn.edu.gdmec.s07150805.zcmol.zcmol;


import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.message.RobotMsg;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotActivity extends AppCompatActivity {
    private ListView listView;
    private EditText editText ;
    private TextView chat_from_content;
    private final String URLStr = "http://www.tuling123.com/openapi/api";
    private final String key = "8981903f47af43be82c5dd7d3aaf560f";
    String urlStr = URLStr+"?key="+key;
    final static int ROBOT_MESSAGE = 0;
    //设置聊天数据适配器
    private SimpleAdapter  msgForRobot;
    private List<Map<String,Object>> list;
    private Map<String,Object> map;
    public String content="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        listView = (ListView) findViewById(R.id.tuLing);
        editText = (EditText) findViewById(R.id.setMes);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        //初始化时默认的对话
        list = new ArrayList<>();
        map = new HashMap<>();
        map.put("chat_from_icon",R.drawable.icon);
        map.put("chat_from_content","我是早茶月光，你要找我玩吗？");
        map.put("chat_from_name","早茶月光");
        map.put("chat_from_createDate",time);
        map.put("type","1");
        list.add(map);
        baseAdapter();
        //长按复制

    }
    // 向机器人发送信息
    public void sendMessage(View view) {
        //发送的信息
        //时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        content = editText.getText().toString();
        map = new HashMap<>();
        map.put("chat_my",R.drawable.my);
        map.put("chat_myContent",content);
        map.put("chat_myName","小熊猫");
        map.put("chat_from_createDate",time);
        map.put("type","0");
        list.add(map);
        baseAdapter();
        String sendmessage = editText.getText().toString();
        final String params = "info=" + sendmessage;
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
                    outputStream.write(params.getBytes());

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    Message message = new Message();
                    message.obj = result.toString();
                    message.what = ROBOT_MESSAGE;
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
    //初始化控件
    static class ViewHolder{
        private ImageView imageView;
        private TextView msg;
        private TextView name;
        private TextView date;
        private String type="";
    }

    /*
     handler处理信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ROBOT_MESSAGE:
                    String Jsonmessage = (String) msg.obj;
                    String text = "";
                    try {
                        JSONObject jsonObject = new JSONObject(Jsonmessage);
                        text = (String) jsonObject.get("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    textView.setText(Jsonmessage);
                    //解释gson
                    JsonStr(Jsonmessage);
                    baseAdapter();
                    /*
                    //数据适配器
                    msgForRobot = new SimpleAdapter(RobotActivity.this,list,R.layout.activity_robot_left,
                            new String[]{"chat_from_icon","chat_from_content","chat_from_name","chat_from_createDate"},
                            new int[]{R.id.chat_from_icon,R.id.chat_from_content,R.id.chat_from_name,R.id.chat_from_createDate});
                    listView.setAdapter(msgForRobot);
                    */

            }
        }
    };
    //解释gson
    public  void JsonStr(String json){
        Gson gson = new Gson();
        RobotMsg robotMsg = gson.fromJson(json, RobotMsg.class);
        //时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        if(robotMsg.code == 100000){
            map = new HashMap<>();
            map.put("chat_from_icon",R.drawable.icon);
            map.put("chat_from_content",robotMsg.text);
            map.put("chat_from_name","早茶月光");
            map.put("chat_from_createDate",time);
            map.put("type","1");
            list.add(map);
        }else{
            map = new HashMap<>();
            map.put("chat_from_icon",R.drawable.icon);
            map.put("chat_from_content","sorry,出错了~~~");
            map.put("chat_from_name","早茶月光");
            map.put("chat_from_createDate",time);
            map.put("type","1");
            list.add(map);
        }
    }
    //baseAdapter数据适配器
    public void baseAdapter(){
        class MyBaseAdapter extends BaseAdapter{
            private List<Map<String,Object>> data;
            private Context context;
            private LayoutInflater layoutInflater;
            //构造方法
            public MyBaseAdapter(Context context,List<Map<String,Object>> data){
                super();
                this.context = context;
                this.data = data;
                layoutInflater = LayoutInflater.from(context);
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
                 View viewRight = null;
                 View viewLeft = null;
                //设置左右信息布局 convertView为左，viewRight为右
                    if (viewLeft == null) {
                            viewHolder = new ViewHolder();
                            viewLeft = layoutInflater.inflate(R.layout.activity_robot_left,parent,false);
                            viewHolder.imageView = (ImageView) viewLeft.findViewById(R.id.chat_from_icon);
                            viewHolder.msg = (TextView) viewLeft.findViewById(R.id.chat_from_content);
                            viewHolder.name = (TextView) viewLeft.findViewById(R.id.chat_from_name);
                            viewHolder.date = (TextView) viewLeft.findViewById(R.id.chat_from_createDate);
                            viewLeft.setTag(viewHolder);
                        }
                    if(viewRight ==null){
                        viewHolder = new ViewHolder();
                        viewRight = layoutInflater.inflate(R.layout.activity_robot_right,parent,false);
                        viewHolder.imageView = (ImageView) viewRight.findViewById(R.id.chat_my);
                        viewHolder.msg = (TextView) viewRight.findViewById(R.id.chat_myContent);
                        viewHolder.name = (TextView) viewRight.findViewById(R.id.chat_myName);
                        viewHolder.date = (TextView) viewRight.findViewById(R.id.chat_from_createDate);
                        viewRight.setTag(viewHolder);
                    }
                    if(data.get(position).get("type").equals("1")){
                        viewHolder = (ViewHolder) viewLeft.getTag();
                        viewHolder.imageView.setImageResource(Integer.parseInt(data.get(position).get("chat_from_icon").toString()));
                        viewHolder.name.setText(data.get(position).get("chat_from_name").toString());
                        viewHolder.msg.setText(data.get(position).get("chat_from_content").toString());
                        viewHolder.date.setText(data.get(position).get("chat_from_createDate").toString());
                       //长按复制
                        viewHolder.msg.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                cmb.setText(data.get(position).get("chat_from_content").toString());
                                Toast.makeText(RobotActivity.this,"内容已复制",Toast.LENGTH_LONG).show();
                                return true;
                            }
                        });
                        return viewLeft;
                    }
                     if(data.get(position).get("type").equals("0")){
                        viewHolder = (ViewHolder) viewRight.getTag();
                        viewHolder.imageView.setImageResource(Integer.parseInt(data.get(position).get("chat_my").toString()));
                        viewHolder.name.setText(data.get(position).get("chat_myName").toString());
                        viewHolder.msg.setText(data.get(position).get("chat_myContent").toString());
                        viewHolder.date.setText(data.get(position).get("chat_from_createDate").toString());
                        return viewRight;
                    }
                     return convertView;
                }
        }
             MyBaseAdapter myBaseAdapter = new MyBaseAdapter(this,list);
            listView.setAdapter(myBaseAdapter);
    }
   //

}
