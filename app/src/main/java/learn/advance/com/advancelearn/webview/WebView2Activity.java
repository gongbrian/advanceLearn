package learn.advance.com.advancelearn.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jacksen.permissionutil.PermissionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import learn.advance.com.advancelearn.R;

public class WebView2Activity extends AppCompatActivity {

    private WebView wView;
    private static final int CODE_REQUEST_CALL_PHONE = 001;
    private static final int TAG_PERMISSION = 1023;
    private String json = null;
    private Handler handler=new Handler();

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2);

        //PermissionUtil.requestPermissions(WebView2Activity.this, CODE_REQUEST_CALL_PHONE, true, Manifest.permission.READ_CONTACTS);
        //设置WebView的相关设置,依次是:
        //支持js,不保存表单,不保存密码,不支持缩放
        //同时绑定Java对象
        wView = (WebView) findViewById(R.id.wView);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setSaveFormData(false);
        wView.getSettings().setSavePassword(false);
        wView.getSettings().setSupportZoom(false);
        wView.getSettings().setDefaultTextEncodingName("UTF-8");
        wView.addJavascriptInterface(new SharpJS(WebView2Activity.this), "sharp");
        //wView.addJavascriptInterface(new MyObject(WebView2Activity.this), "myObj");
        wView.setWebViewClient(new myWebViewClient());
        wView.loadUrl("file:///android_asset/demo3.html");
        Log.e("gongzibiao","onCreate（）");
    }

    //自定义一个Js的业务类,传递给JS的对象就是这个,调用时直接javascript:sharp.contactlist()
    public class SharpJS {
        private Context context;

        public SharpJS(Context context) {
            this.context = context;
        }
        @JavascriptInterface
        public void contactlist() {
            try {
                //String json = null;
                Log.e("gongzibiao","contactlist() is running!");
                if (ContextCompat.checkSelfPermission(WebView2Activity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(WebView2Activity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(WebView2Activity.this, "deny for what???", Toast.LENGTH_SHORT).show();
                        json = buildJson(getContacts());
                    } else {
                        Toast.makeText(WebView2Activity.this, "show the request popupwindow", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(WebView2Activity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                TAG_PERMISSION);
                    }
                } else {
                    Toast.makeText(WebView2Activity.this, "agreed", Toast.LENGTH_SHORT).show();
                    json = buildJson(getContacts());
                }
                //String json = buildJson(getContacts());
                handler.post(new Runnable() {
                @Override
                public void run() {
                    wView.loadUrl("javascript:show('" + json + "')");
                }
                });
            } catch (Exception e) {
                Log.e("gongzibiao","settings data error" + e);
            }

        }
        @JavascriptInterface
        @SuppressLint("MissingPermission")
        public void call(final String phone) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("gongzibiao", "call() is running !");
                    Intent it = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(it);
                }
            });
        }

        //将显示Toast和对话框的方法暴露给JS脚本调用
        @JavascriptInterface
        public void showToast(String name) {
            Log.e("gongzibiao","showToast() is running !");
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void showDialog() {
            new AlertDialog.Builder(context)
                    .setTitle("联系人列表").setIcon(R.mipmap.ic_launcher)
                    .setItems(new String[]{"基神", "B神", "曹神", "街神", "翔神"}, null)
                    .setPositiveButton("确定", null).create().show();
        }
    }

    //将获取到的联系人集合写入到JsonObject对象中,再添加到JsonArray数组中
    private String buildJson(List<Contact> contacts)throws Exception
    {
        JSONArray array = new JSONArray();
        for(Contact contact:contacts)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", contact.getId());
            jsonObject.put("name", contact.getName());
            jsonObject.put("phone", contact.getPhone());
            array.put(jsonObject);
        }
        Log.e("gongzibiao",array.toString());
        return array.toString();
    }

    //定义一个获取联系人的方法,返回的是List<Contact>的数据
    public List<Contact> getContacts()
    {
        List<Contact> Contacts = new ArrayList<Contact>();
        //①查询raw_contacts表获得联系人的id
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //查询联系人数据
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while(cursor.moveToNext())
        {
            Contact contact = new Contact();
            //获取联系人姓名,手机号码
            contact.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            Contacts.add(contact);
        }
        cursor.close();
        return Contacts;
    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /*if (CODE_REQUEST_CALL_PHONE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);*/
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        switch (requestCode) {
            case TAG_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(WebView2Activity.this, "allow", Toast.LENGTH_SHORT).show();
                    try {
                        json = buildJson(getContacts());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(WebView2Activity.this, "deny", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
