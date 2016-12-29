package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.Chat;
import com.chat.ChatResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatActivity extends AppCompatActivity {
    private EditText sedMes;
    private Button id_chat_send;
    private ListView chat_other;
    final static int NEW_MESSAGE = 0;
    final static int SEND_MESSAGE = 1;
    private String strUTF8="";
    private List<Map<String,Object>> list;
    private Map<String,Object> map;
    //聊天判断是否返回信息
    private int isReturnMsg = 0;
    private int iii=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatwithljm);
        sedMes = (EditText) findViewById(R.id.setMes);
        id_chat_send = (Button) findViewById(R.id.id_chat_send);
        chat_other = (ListView) findViewById(R.id.chat_other);
        list = new ArrayList<>();
        id_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sedMes.getText().toString().equals("")) {
                    sendMessage(7150805,sedMes.getText().toString());
                    for(int i=0;i<500;i++){
                         iii+=1;
                    }
                    chatSend();
                } else {
                    Toast.makeText(chatActivity.this, "输入信息不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        //计时器
        final CountDownTimer cdt = new CountDownTimer(100000000,2000) {
            //执行100000000/2000次
            @Override
            public void onTick(long millisUntilFinished) {
               chatSend();
            }
            @Override
            public void onFinish() {

            }
        };
        cdt.start();
    }
    public void chatSend() {
//        final String urlStr = "http://www.zcmol.cn/androidSend.php?id="+id+"&message="+strUTF8;
        final String urlStr = "http://www.zcmol.cn/readAndroid.php";
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
//发送聊天信息
public void sendMessage(int id,String sendMsg) {
      String Utf8 = "";
    try{
        Utf8 = URLEncoder.encode(sendMsg,"UTF-8");
    }catch (Exception e){
        Utf8 = sendMsg;
    }
    final String urlStr = "http://www.zcmol.cn/androidSend.php?id="+id+"&message="+Utf8;
    sedMes.setText("");
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
                message.what = SEND_MESSAGE;
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
//                    Toast.makeText(chatActivity.this,Jsonmessage,Toast.LENGTH_LONG).show();
                    if(ResultJson(Jsonmessage)){
                        baseAdapter();
                    }
                    break;
                case SEND_MESSAGE:
                    break;
            }
        }
    };
    //初始化控件
    static class ViewHolder{
        private ImageView imageView;
        private TextView msg;
        private TextView name;
        private TextView date;
    }
    public void baseAdapter(){
        class MyAdapter extends BaseAdapter{
            private Context context;
            private List<Map<String,Object>> data;
            private LayoutInflater inflater;
            public MyAdapter(Context context,List<Map<String,Object>> data){
                this.context = context;
                this.data = data;
                this.inflater = LayoutInflater.from(context);
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
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                View viewLeft = null;
                View viewRight = null;
                if (viewLeft == null) {
                    viewHolder = new ViewHolder();
                    viewLeft = inflater.inflate(R.layout.activity_robot_left,parent,false);
                    viewHolder.imageView = (ImageView) viewLeft.findViewById(R.id.chat_from_icon);
                    viewHolder.msg = (TextView) viewLeft.findViewById(R.id.chat_from_content);
                    viewHolder.name = (TextView) viewLeft.findViewById(R.id.chat_from_name);
                    viewHolder.date = (TextView) viewLeft.findViewById(R.id.chat_from_createDate);
                    viewLeft.setTag(viewHolder);
                }
                if(viewRight ==null){
                    viewHolder = new ViewHolder();
                    viewRight = inflater.inflate(R.layout.activity_robot_right,parent,false);
                    viewHolder.imageView = (ImageView) viewRight.findViewById(R.id.chat_my);
                    viewHolder.msg = (TextView) viewRight.findViewById(R.id.chat_myContent);
                    viewHolder.name = (TextView) viewRight.findViewById(R.id.chat_myName);
                    viewHolder.date = (TextView) viewRight.findViewById(R.id.chat_from_createDate);
                    viewRight.setTag(viewHolder);
                }
                if(data.get(position).get("id").toString().equals("7150813")) {
                    viewHolder = (ViewHolder) viewLeft.getTag();
                    viewHolder.imageView.setImageResource(Integer.parseInt(data.get(position).get("chat_from_icon").toString()));
                    viewHolder.name.setText(data.get(position).get("chat_from_name").toString());
                    viewHolder.msg.setText(data.get(position).get("chat_from_content").toString());
                    viewHolder.date.setText(data.get(position).get("chat_from_createDate").toString());
                    return viewLeft;
                }
                if(data.get(position).get("id").toString().equals("7150805")){
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
        MyAdapter myAdapter = new MyAdapter(chatActivity.this,list);
        chat_other.setAdapter(myAdapter);
    }
    public boolean ResultJson(String json){
        Gson gson = new Gson();
        Chat chat = gson.fromJson(json,Chat.class);
        if(chat.reason.equals("00000")){
            if(chat.result.length!=isReturnMsg) {
                list.clear();
                isReturnMsg = chat.result.length;
                for (ChatResult chatResult : chat.result) {
                    if (chatResult.id.equals("7150813")) {
                        map = new HashMap<>();
                        map.put("chat_from_icon", R.drawable.icon);
                        map.put("chat_from_content", chatResult.message);
                        map.put("chat_from_name", "冯业佳");
                        map.put("chat_from_createDate", chatResult.time);
                        map.put("id", chatResult.id);
                        list.add(map);
                    } else {
                        map = new HashMap<>();
                        map.put("chat_my", R.drawable.my);
                        map.put("chat_myContent", chatResult.message);
                        map.put("chat_myName", "陈敬民");
                        map.put("chat_from_createDate", chatResult.time);
                        map.put("id", chatResult.id);
                        list.add(map);
                    }
                }
                return true;
            }
            return false;
        }else{
            Toast.makeText(chatActivity.this,"接受数据失败",Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
