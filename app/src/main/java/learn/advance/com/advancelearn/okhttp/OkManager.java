package learn.advance.com.advancelearn.okhttp;

/**
 * Created by gongzibiao on 2018/3/16.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/15 0015.
 * 封装工具类
 * 这一个类主要将OkHttp3工具类进行封装，用于对数据的传输，包括Spring,Json,img，表单等数据的提交与获取
 */
public class OkManager {
    private OkHttpClient client;
    private volatile static OkManager manager;   //防止多个线程访问时
    private final String TAG = OkManager.class.getSimpleName();  //获得类名
    private Handler handler;

    private static final String BASE_URL = "http://116.62.169.111:8888/web-learn/";//请求接口根地址

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private OkManager() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    //采用单例模式获取对象
    public static OkManager getInstance() {
        OkManager instance = null;
        if (manager == null) {
            synchronized (OkManager.class) {                //同步代码块
                if (instance == null) {
                    instance = new OkManager();
                    manager = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 请求返回的是JSON字符串
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final Fun1 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    /**
     * 请求返回相应结果的是Json对象
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue, final Fun4 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (JSONException e) {

                    }
                }
            }
        });
    }

    /**
     * 返回响应的对象是一个字节数组
     *
     * @param data
     * @param callBack
     */
    private void onSuccessByteMethod(final byte[] data, final Fun2 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(data);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void onSuccessImgMethod(final Bitmap bitmap, final Fun3 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(bitmap);
                    } catch (Exception e) {

                    }

                }
            }
        });
    }

    /**
     * 同步请求,在Android开发中不常用，因为会阻塞UI线程
     *
     * @param url
     * @return
     */

    public String synaGetByUrl(String url) {
        //构建一个Request请求
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();  //execute用于同步请求数据
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 异步请求,请求返回Json字符串
     *
     * @param url
     * @param callback
     */
    public void asyncJsonStringByURL(String url, Map<String, String> headersParams, Map<String, String> bodysparams,final Fun1 callback) {
        //final Request request = new Request.Builder().url(url).build();
        Request.Builder RequestBuilder=new Request.Builder();
        RequestBuilder.url(url);//添加URL地址
        RequestBuilder.headers(SetHeaders(headersParams));//添加请求头
        RequestBuilder.post(SetRequrestBody(bodysparams));
        final Request request = RequestBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            //enqueue是调用了一个入队的方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callback);
                }
            }
        });

    }

    public void asyncJsonStringByUserSet(String url, final Fun1 callback) {
        //final Request request = new Request.Builder().url(url).build();
        FormBody body = addBodys();
        final Request request = addHeaders().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            //enqueue是调用了一个入队的方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callback);
                }
            }
        });

    }

    /**
     * 异步请求，请求返回Json对象
     *
     * @param url
     * @param callback
     */

    public void asyncJsonObjectByUrl(String url, final Fun4 callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });

    }

    /**
     * 异步请求，请求返回的byte字节数组
     *
     * @param url
     * @param callback
     */
    public void asyncGetByteByUrl(String url, final Fun2 callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessByteMethod(response.body().bytes(), callback);
                }
            }
        });
    }

    /**
     * 异步请求，请求返回图片
     *
     * @param url
     * @param callback
     */
    public void asyncDownLoadImgtByUrl(String url, final Fun3 callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    byte[] data = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    onSuccessImgMethod(bitmap, callback);

                    System.out.println(data.length);
                }
            }
        });

    }

    /**
     * 模拟表单的提交
     *
     * @param url
     * @param param
     * @param callback
     */
    public void sendComplexForm(String url, Map<String, String> param, final Fun4 callback) {
        FormBody.Builder form_builder = new FormBody.Builder();  //表单对象，包含以input开始的对象，模拟一个表单操作，以HTML表单为主
        //如果键值对不为空，且值不为空
        if (param != null && !param.isEmpty()) {
            //循环这个表单，zengqiang for循环
            for (Map.Entry<String, String> entry : param.entrySet()) {
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }
        //声明一个请求对象体
        RequestBody request_body = form_builder.build();
        //采用post的方式进行提交
        Request request = new Request.Builder().url(url).post(request_body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });
    }

    /**
     * 向服务器提交String请求
     *
     * @param url
     * @param content
     * @param callback
     */
    public void sendStringByPost(String url, String content, final Fun4 callback) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });

    }

    //回调
    public interface Fun1 {
        void onResponse(String result);
    }

    interface Fun2 {
        void onResponse(byte[] result);
    }

    public interface Fun3 {
        void onResponse(Bitmap bitmap);
    }

    public interface Fun4 {
        void onResponse(JSONObject jsonObject);
    }

    /**
     * okHttp get同步请求
     * @param actionUrl  接口地址
     * @param paramsMap   请求参数
     */
    private void requestGetBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s/%s?%s", BASE_URL, actionUrl, tempParams.toString());
            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            //创建一个Call
            final Call call = client.newCall(request);
            //执行请求
            final Response response = call.execute();
            response.body().string();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("device-udid", "a46d663d675d4858ea7d0a21c2de06e9");
        builder.addHeader("device-client", "weapp");
        builder.addHeader("device-code", "6015");
        builder.addHeader("api-version", "1.9");
        return builder;
    }


    /**
     * 设置请求头
     * @param headersParams
     * @return
     */
    private Headers SetHeaders(Map<String, String> headersParams){
        Headers headers=null;
        okhttp3.Headers.Builder headersbuilder=new okhttp3.Headers.Builder();

        if(headersParams != null)
        {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                Log.d("get http", "get_headers==="+key+"===="+headersParams.get(key));
            }
        }
        headers=headersbuilder.build();

        return headers;
    }


    private FormBody addBodys(){
        FormBody.Builder body=new FormBody.Builder();
        body.add("grant_type","abcdef");
        body.add("appid","23");
        body.add("appsecret","101");
        return body.build();
    }

    /**
     * post请求参数
     * @param BodyParams
     * @return
     */
    private RequestBody SetRequrestBody(Map<String, String> BodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
                Log.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
            }
        }
        body=formEncodingBuilder.build();
        return body;

    }

    /**
     * Post上传图片的参数
     * @param BodyParams
     * @param fileParams
     * @return
     */
    private RequestBody SetFileRequestBody(Map<String, String> BodyParams,Map<String, String> fileParams){
        //带文件的Post参数
        RequestBody body=null;
	    okhttp3.MultipartBody.Builder MultipartBodyBuilder=new okhttp3.MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = null;

        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
                Log.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
            }
        }

        if(fileParams != null){
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            int i=0;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                i++;
                MultipartBodyBuilder.addFormDataPart(key, fileParams.get(key));
                Log.d("post http", "post_Params==="+key+"===="+fileParams.get(key));

                fileBody = RequestBody.create(MEDIA_TYPE_PNG, new File(fileParams.get(key)));
                MultipartBodyBuilder.addFormDataPart(key, i+".png", fileBody);
            }
        }



        body=MultipartBodyBuilder.build();
        return body;

    }


}
