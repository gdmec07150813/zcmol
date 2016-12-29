package cn.edu.gdmec.s07150805.zcmol.zcmol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class New_toSeeActivity extends AppCompatActivity {
    private WebView webView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_see);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.new_webView);
        title.setText(bundle.getString("title"));
        webView.loadUrl(bundle.getString("webHttp"));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return false;
            }
        });

    }
}
