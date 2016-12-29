package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by JMC on 2016/12/26.
 */
public class action_Activity extends Activity{
        private ImageView action_bg;
        private ImageView action_hengtiao;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.action);

            //执行动画
            action_bg = (ImageView) findViewById(R.id.action_bg);
            action_hengtiao = (ImageView) findViewById(R.id.action_hengtiao);
            Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.action_bg);
            //动画执行完后停留在此动画的最后一帧
            mAnimation.setFillAfter(true);
            //执行动画
            action_bg.startAnimation(mAnimation);

            Animation mAnimation2 = AnimationUtils.loadAnimation(this, R.anim.action_hengtiao);
            //动画执行完后停留在此动画的最后一帧
            mAnimation2.setFillAfter(true);
            //执行动画
            action_hengtiao.startAnimation(mAnimation2);

            //动画定时器
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(action_Activity.this,
                            MainActivity.class); // 从启动动画ui跳转到登录ui
                    startActivity(intent);
                    action_Activity.this.finish(); // 结束启动动画界面

                }
            }, 4000); // 启动动画持续5秒钟
        }

}