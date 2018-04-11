package learn.advance.com.advancelearn.okhttp;
/**
 * Created by gongzibiao on 2018/3/16.
 */

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import learn.advance.com.advancelearn.R;
import okhttp3.OkHttpClient;


public class OKHttpMainActivity extends AppCompatActivity {

    private Button testButton, getJsonButton, button3;
    private ImageView testImageView;
    private final static int SUCCESS_SATUS = 1;
    private final static int FAILURE = 0;
    private final static String Tag = OKHttpMainActivity.class.getSimpleName();


    private OkManager manager;

    private OkHttpClient clients;

    //图片下载的请求地址
    //private String img_path = "http://localhost:8080/web-learn/UploadDownloadServlet?method=download";
    private String img_path = "http://116.62.169.111:8888/web-learn/UploadDownloadServlet?method=download";
    //请求返回值为Json数组
    //private String jsonpath = "http://localhost:8080/web-learn/ServletJson";
    //private String jsonpath = "http://116.62.169.111:8888/web-learn/ServletJson";
    //private String jsonpath = "https://jiuhuiquancheng.top/sites/api/?url=device/setDeviceToken";
    private String jsonpath = "https://jiuhuiquancheng.top/sites/api/?url=shop/token";

    //登录验证请求
    //private String login_path="http://localhost:8080/web-learn/OkHttpLoginServlet";
    private String login_path="http://116.62.169.111:8888/web-learn/OkHttpLoginServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_activity_main);
        testButton = (Button) findViewById(R.id.test);
        getJsonButton = (Button) findViewById(R.id.getjson);


        testImageView = (ImageView) findViewById(R.id.testImageView);
        button3 = (Button) findViewById(R.id.button3);

        //-----------------------------------------------------------------------------------------
        manager = OkManager.getInstance();
        getJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.asyncJsonStringByURL(jsonpath, new OkManager.Fun1() {
                    @Override
                    public void onResponse(String result) {
                        Log.i(Tag, result);   //获取JSON字符串
                    }
                });
            }
        });
        //-------------------------------------------------------------------------
        //用于登录请求测试，登录用户名和登录密码应该Sewrver上的对应
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", "123");
                map.put("password", "123");
                manager.sendComplexForm(login_path, map, new OkManager.Fun4() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(Tag, jsonObject.toString());
                    }
                });
            }
        });
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manager.asyncDownLoadImgtByUrl(img_path, new OkManager.Fun3() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // testImageView.setBackgroundResource(0);
                        testImageView.setImageBitmap(bitmap);
                        Log.i(Tag, "231541645");
                    }
                });
            }
        });

    }


}
