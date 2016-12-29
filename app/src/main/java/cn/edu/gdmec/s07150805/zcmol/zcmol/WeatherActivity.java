package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.message.Weather;
import com.message.WeatherResult;
import com.message.WeatherResult_future;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    //初始化控件
    private TextView one,two,three,four,five,six;
    private TextView one_one,two_two,three_three,four_four,five_five,six_six;
    private TextView weather_time,weather_city,weather_temperature,
            weather_find,weather_fell,weather_status;
    private ImageView weather_back,
            weather_refresh,weather_search;
    ///////
    final static int WEATHER_MESSAGE = 0;
    static String cityStr = "广州";
    private String key = "&key=a675315273732bf6501967763cc1d3e0";
    private String HttpUrl = "http://v.juhe.cn/weather/index?format=2&cityname=";
    //假装在广州
    private String city = "";
    private String urlStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //实例化控件
        one = (TextView) findViewById(R.id.weather_one);
        two = (TextView) findViewById(R.id.weather_two);
        three = (TextView) findViewById(R.id.weather_three);
        four = (TextView) findViewById(R.id.weather_four);
        five = (TextView) findViewById(R.id.weather_five);
        six = (TextView) findViewById(R.id.weather_six);
        one_one = (TextView) findViewById(R.id.weather_one_one);
        two_two = (TextView) findViewById(R.id.weather_two_two);
        three_three = (TextView) findViewById(R.id.weather_three_three);
        four_four = (TextView) findViewById(R.id.weather_four_four);
        five_five = (TextView) findViewById(R.id.weather_five_five);
        six_six = (TextView) findViewById(R.id.weather_six_six);
        weather_time = (TextView) findViewById(R.id.weather_time);
        weather_city = (TextView) findViewById(R.id.weather_city);
        weather_temperature = (TextView) findViewById(R.id.weather_temperature);
        weather_find = (TextView) findViewById(R.id.weather_find);
        weather_fell = (TextView) findViewById(R.id.weather_fell);
        weather_status = (TextView) findViewById(R.id.weather_status);
        weather_back = (ImageView) findViewById(R.id.weather_back);
        weather_refresh = (ImageView) findViewById(R.id.weather_refresh);
        weather_search = (ImageView) findViewById(R.id.weather_search);
        /////
        try {
            city = new String(java.net.URLEncoder.encode(cityStr, "utf-8").getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        urlStr = HttpUrl+city+key;
        sendMessage();
        weather_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        weather_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    city = new String(java.net.URLEncoder.encode(cityStr, "utf-8").getBytes());
                    urlStr = HttpUrl+city+key;
                    sendMessage();
                    Toast.makeText(WeatherActivity.this,"更新成功",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        weather_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(WeatherActivity.this).create();
                alertDialog.setTitle("输入你想要的城市");
                LayoutInflater inflater = getLayoutInflater();
                View linearLayout = inflater.inflate(R.layout.weather_search,null);
                alertDialog.setView(linearLayout);
                final EditText editText = (EditText) linearLayout.findViewById(R.id.search_weather);
                DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       switch (which){
                           case AlertDialog.BUTTON_POSITIVE:
                               if(!editText.getText().toString().equals("")) {
                                   try {
                                       cityStr = editText.getText().toString();
                                       city = new String(java.net.URLEncoder.encode(editText.getText().toString(), "utf-8").getBytes());
                                       urlStr = HttpUrl + city + key;
                                       sendMessage();
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                               }else{
                                   Toast.makeText(WeatherActivity.this,"输入值不能为空",Toast.LENGTH_SHORT).show();
                               }
                               break;
                           case AlertDialog.BUTTON_NEGATIVE:
                               alertDialog.dismiss();
                               break;
                       }
                    }
                };
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"确定",dialogInterface);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"取消",dialogInterface);
                alertDialog.show();

            }
        });
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
                    message.what = WEATHER_MESSAGE;
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

  /*  handler处理信息*/

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEATHER_MESSAGE:
                    String Jsonmessage = (String) msg.obj;
                    String text = "";
                    try {
                        JSONObject jsonObject = new JSONObject(Jsonmessage);
                        text = (String) jsonObject.get("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //解释gson
                    weatherJson(Jsonmessage);
            }
        }
    };
    //解释gson数据
    public void weatherJson(String json){
         Gson gson = new Gson();
        Weather weather = gson.fromJson(json,Weather.class);
        if(weather.resultcode.equals("200")){
            weather_city.setText(weather.result.today.city);
            weather_fell.setText(weather.result.today.dressing_index);
            weather_find.setText(weather.result.sk.wind_direction);
            weather_time.setText(weather.result.sk.time);
            weather_temperature.setText(weather.result.today.temperature);
            weather_status.setText(weather.result.today.weather);
            one.setText("周"+weather.result.future[1].week.replace("星期",""));
            one_one.setText(weather.result.future[1].weather);
            two.setText("周"+weather.result.future[2].week.replace("星期",""));
            two_two.setText(weather.result.future[2].weather);
            three.setText("周"+weather.result.future[3].week.replace("星期",""));
            three_three.setText(weather.result.future[3].weather);
            four.setText("周"+weather.result.future[4].week.replace("星期",""));
            four_four.setText(weather.result.future[4].weather);
            five.setText("周"+weather.result.future[5].week.replace("星期",""));
            five_five.setText(weather.result.future[5].weather);
            six.setText("周"+weather.result.future[6].week.replace("星期",""));
            six_six.setText(weather.result.future[6].weather);
        }
    }


}
