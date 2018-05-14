package learn.advance.com.advancelearn.webview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import learn.advance.com.advancelearn.R;
import multiimageselector.advance.com.multiimageselector.MultiImageSelector;
import multiimageselector.advance.com.multiimageselector.utils.FileUtils;

public class JSCameraActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    /**
     * 当前Activity的实例
     */
    protected Activity mActivity;
    /**
     * 当前上下文实例
     */
    protected Context mContext;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int REQUEST_IMAGE = 0x012;

    private static final String DEFAULT_URL = "file:///android_asset/camera.html";
    // 拍照，SD卡所需的全部权限
    String[] mPermissionList = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private PopupWindow pop;
    private LinearLayout ll_popup;
    /**
     * 选择的照片
     */
    private List<String> selectImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jscamera);

        mContext = this;
        mActivity = this;
        webView = (WebView) findViewById(R.id.webView);
        initWebView();
        Init();

        webView.loadUrl(DEFAULT_URL);
    }

    private void Init() {
        pop = new PopupWindow(this);

        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        //ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);


        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (osVersion >= 23) {
                    //UIUtils.showToast("请设置摄像头权限");
                    //return;
                }

                photo();
                pop.dismiss();
                //ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (osVersion >= 23) {
                    //UIUtils.showToast("请设置SD卡读取权限");
                    //return;
                }

                MultiImageSelector selector = MultiImageSelector.create();
                selector.showCamera(false);
                // selector.count(5);
                selector.single();
                //selector.multi();
                selector.origin((ArrayList<String>) selectImages);
                selector.start(mActivity, REQUEST_IMAGE);

                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_exit);
                pop.dismiss();
                //ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                //ll_popup.clearAnimation();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE://拍照
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    String path = FileUtils.saveBitmap(bm, fileName);
                    //if (!StringUtil.isBland(path))
                        selectImages.add(path);
                    Toast.makeText(this,"selectImages.size() = " + selectImages.size(),Toast.LENGTH_SHORT);
                    break;
                case REQUEST_IMAGE://相册
                    selectImages = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    Toast.makeText(this,"selectImages.size() = " + selectImages.size(),Toast.LENGTH_SHORT);
                    //UIUtils.showToast("selectImages.size() = " + selectImages.size());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    private void initWebView() {
        // 获得 webView的设置
        webSettings = webView.getSettings();
        /**
         * 设置当前页面可以执行js 代码，默认情况下是false
         */
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR); // 设置默认缩放级别
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 以单列的形式排列布局

        // webView 自带 的缩放按钮与webView的触摸事件有冲突
        //  webSettings.setBuiltInZoomControls(true); // 显示默认的缩放按钮

        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("onTouch============");

                return false;// 如果返回true 网页就不能响应触摸操作了
            }
        });

        /*
         * 为webView 添加一个js 接口
         * 参数一： 是一个java对象
         * 参数二：是一个随意的字符串
         * 该方法的功能是在网页中创建一个js 对象，对象名就是参数二字符串。js对象中的功能，由参数一提供。
         */
        webView.addJavascriptInterface(new Object() {

            /**
             * 声明一个在js 中可以调用的方法，
             * 注意：4.4以上这里要加注解。
             * 类名：shangyukeji和方法名paizhao跟html保持一致
             */
            @JavascriptInterface
            public void paizhao() {
                // 缺少权限时, 进入权限配置页面
                ActivityCompat.requestPermissions(mActivity, mPermissionList, 100);
                //ll_popup.startAnimation(AnimationUtils.loadAnimation(JSCameraActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(webView, Gravity.BOTTOM, 0, 0);
                //UIUtils.showToast("paizhao");
                Toast.makeText(JSCameraActivity.this,"paizhao",Toast.LENGTH_SHORT);
            }

        }, "shangyukeji");
    }


}
