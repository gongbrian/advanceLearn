package learn.advance.com.advancelearn;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import learn.advance.com.advancelearn.okhttp.OKHttpMainActivity;
import learn.advance.com.advancelearn.okhttp.AsyncHttpScrollingActivity;
import learn.advance.com.advancelearn.spinner.SpinnerMainActivity;
import learn.advance.com.advancelearn.tabpagerview.tabMainActivity;
import learn.advance.com.advancelearn.viewpagerFragment.VFMainActivity;
import learn.advance.com.advancelearn.webview.JSCameraActivity;
import learn.advance.com.advancelearn.webview.WebView2Activity;
import learn.advance.com.advancelearn.webview.WebViewActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextMessage;
    private Button advance_spinner;
    private Button tab_button;
    private Button bt_c,bt_d,bt_e,bt_f,bt_g,bt_h,bt_I,bt_J;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        advance_spinner=(Button)findViewById(R.id.bt_a);
        advance_spinner.setOnClickListener(this);

        tab_button=(Button)findViewById(R.id.bt_b);
        tab_button.setOnClickListener(this);

        bt_c = (Button) findViewById(R.id.bt_c);
        bt_c.setOnClickListener(this);

        bt_d = (Button) findViewById(R.id.bt_d);
        bt_d.setOnClickListener(this);

        bt_e = (Button) findViewById(R.id.bt_e);
        bt_e.setOnClickListener(this);

        bt_f = (Button) findViewById(R.id.bt_f);
        bt_f.setOnClickListener(this);
        bt_g = (Button) findViewById(R.id.bt_g);
        bt_g.setOnClickListener(this);

        bt_h = (Button) findViewById(R.id.bt_h);
        bt_h.setOnClickListener(this);

        bt_I = (Button) findViewById(R.id.bt_I);
        bt_I.setOnClickListener(this);

        bt_J = (Button) findViewById(R.id.bt_J);
        bt_J.setOnClickListener(this);

        MainActivityPermissionsDispatcher.MyNeedsPermissionWithPermissionCheck(MainActivity.this);

    }

    public void onClick(View v){
        Toast.makeText(this,"点击了。。。", Toast.LENGTH_SHORT);
        switch(v.getId()) {
            case R.id.bt_a:
                Intent intenta = new Intent(MainActivity.this,SpinnerMainActivity.class);
                startActivity(intenta);
                break;

            case R.id.bt_b:
                Intent intentb = new Intent(MainActivity.this,tabMainActivity.class);
                startActivity(intentb);
                break;
            case R.id.bt_c:
                Intent intentc = new Intent(MainActivity.this,VFMainActivity.class);
                startActivity(intentc);
                break;
            case R.id.bt_d:
                Intent intentd = new Intent(MainActivity.this, OKHttpMainActivity.class);
                startActivity(intentd);
                break;
            case R.id.bt_e:
                Intent intente = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intente);
                break;
            case R.id.bt_f:
                Intent intentf = new Intent(MainActivity.this, GetUUIDActivity.class);
                startActivity(intentf);
                break;
            case R.id.bt_g:
                Intent intentg = new Intent(MainActivity.this, WebView2Activity.class);
                startActivity(intentg);
                break;
            case R.id.bt_h:
                Intent intenth = new Intent(MainActivity.this, AsyncHttpScrollingActivity.class);
                startActivity(intenth);
                break;

            case R.id.bt_I:
                Intent intentI = new Intent(MainActivity.this, JSCameraActivity.class);
                startActivity(intentI);
                break;

            case R.id.bt_J:
                Intent intentJ = new Intent("android.intent.action.tabpagerview.MainActivity");
                startActivity(intentJ);
                break;
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void MyNeedsPermission() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
