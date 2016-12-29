package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
private Button register;
    private ImageView imageView;
    private Button back_login;
    private EditText username;
    private EditText re_pwd;
    private EditText re_pwd2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        imageView= (ImageView) findViewById(R.id.re_circle);
        Animation mAnimation= AnimationUtils.loadAnimation(this,R.anim.enlarge);
        //动画执行完后停留在此动画的最后一帧
        mAnimation.setFillAfter(true);
        //执行动画
        imageView.startAnimation(mAnimation);
        /*点击切换页面*/
        register= (Button) findViewById(R.id.register);
        back_login= (Button) findViewById(R.id.back_login);
        username= (EditText) findViewById(R.id.username);
        re_pwd= (EditText) findViewById(R.id.re_pwd);
        re_pwd2= (EditText) findViewById(R.id.re_pwd2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString().trim();
                String pass=re_pwd.getText().toString().trim();
                String pass2=re_pwd2.getText().toString().trim();

                User user=new User();
                user.setUsername(name);
                user.setUserpwd(pass);

                int result=SqliteDB.getInstance(getApplicationContext()).saveUser(user);
                if (pass.equals(pass2)){
                    if (result==1){
                        Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else  if (result==-1)
                    {
                        Toast.makeText(RegisterActivity.this,"用户名已经存在！",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,"两次密码输入有误！",Toast.LENGTH_LONG).show();
                }

            }
        });
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
            }
        });
    }
}
