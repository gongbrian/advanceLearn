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

    private Button testButton, getJsonButton, button3, rfidgetjsonButton;
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

//    private String rfid_jsonpath = "http://116.62.169.111:8080/apms/rest/user/ajax_login";
    private String rfid_jsonpath = "https://jiuhuiquancheng.top/sites/api/?seller/list";

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

        final Map<String, String> headersdatas = new HashMap<String, String>();//这里是添加你的请求头参数
        headersdatas.put("device-udid", "a46d663d675d4858ea7d0a21c2de06e9");
        headersdatas.put("device-client", "weapp");
        headersdatas.put("device-code", "6015");
        headersdatas.put("api-version", "1.6");


        final Map<String, String> datas = new HashMap<String, String>();//这里是拼接的请求参数
        datas.put("token", "d122e3c72332c5af12a71d77c9f1462f5901d276");
        datas.put("pagination", "23");
        datas.put("category_id", "25");

//        OkHttpUtils.getInstance().getBeanExecute("你的接口url", headersdatas, datas,this, mHandler,WeatherBeanclass);
        manager = OkManager.getInstance();
        getJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.asyncJsonStringByURL(rfid_jsonpath, headersdatas, datas, new OkManager.Fun1() {
                    @Override
                    public void onResponse(String result) {
                        Log.i(Tag, result);   //获取JSON字符串
                    }
                });
            }
        });

        rfidgetjsonButton = (Button) findViewById(R.id.rfidgetjson);
        rfidgetjsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.asyncJsonStringByUserSet(jsonpath, new OkManager.Fun1() {
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
