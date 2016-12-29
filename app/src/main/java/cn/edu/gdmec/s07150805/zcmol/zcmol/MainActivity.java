package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button signin, back;
    private EditText count;
    private EditText pwd;
   /* private List<User> userList;
    private List<User> dataList = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //执行动画

        imageView = (ImageView) findViewById(R.id.circle);
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge);
        //动画执行完后停留在此动画的最后一帧
        mAnimation.setFillAfter(true);
        //执行动画
        imageView.startAnimation(mAnimation);
        /*点击切换页面*/
        signin = (Button) findViewById(R.id.SignIn);
        back = (Button) findViewById(R.id.register_username);
        count = (EditText) findViewById(R.id.username);
        pwd = (EditText) findViewById(R.id.pwd);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = count.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                //userList=SqliteDB.getInstance(getApplicationContext()).loadUser();
                int result = SqliteDB.getInstance(getApplicationContext()).Quer(pass, name);
                if (result == 1) {
                    Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,IndexActivity.class);
                    startActivity(intent);
                } else if (result == 0) {
                    Toast.makeText(MainActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();

                } else if (result == -1) {
                    Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

