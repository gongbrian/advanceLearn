package learn.advance.com.advancelearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import learn.advance.com.advancelearn.okhttp.OKHttpMainActivity;
import learn.advance.com.advancelearn.spinner.SpinnerMainActivity;
import learn.advance.com.advancelearn.tabpagerview.tabMainActivity;
import learn.advance.com.advancelearn.viewpagerFragment.VFMainActivity;

/**
 * Created by gongzibiao on 2018/3/23.
 */

public class GetUUIDActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextMessage;
    private Button advance_spinner;
    private Button tab_button;
    private Button bt_c,bt_d;


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
        mTextMessage.setText(getUDID(this));
        Log.e("gongzibao",getUDID(this));
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

    }

    public void onClick(View v){
        Toast.makeText(this,"点击了。。。", Toast.LENGTH_SHORT);
        switch(v.getId()) {
            case R.id.bt_a:
                Intent intenta = new Intent(this,SpinnerMainActivity.class);
                startActivity(intenta);
                break;

            case R.id.bt_b:
                Intent intentb = new Intent(this,tabMainActivity.class);
                startActivity(intentb);
                break;
            case R.id.bt_c:
                Intent intentc = new Intent(this,VFMainActivity.class);
                startActivity(intentc);
                break;
            case R.id.bt_d:
                Intent intentd = new Intent(this, OKHttpMainActivity.class);
                startActivity(intentd);
                break;
        }
    }

    protected static final String PREFS_FILE = "gank_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "gank_device_id";
    protected static String uuid;
    /**
     * 获取唯一标识码
     * @param mContext
     * @return
     */
    public synchronized static String getUDID(Context mContext)
    {
        if( uuid ==null ) {
            if( uuid == null) {
                final SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences( PREFS_FILE, Context.MODE_PRIVATE);
                final String id = prefs.getString(PREFS_DEVICE_ID, null );

                if (id != null) {
                    // Use the ids previously computed and stored in the prefs file
                    uuid = id;
                } else {

                    final String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    // Use the Android ID unless it's broken, in which case fallback on deviceId,
                    // unless it's not available, then fallback on a random number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            try {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            @SuppressLint("MissingPermission")
                            final String deviceId = ((TelephonyManager) mContext.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                            uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    // Write the value out to the prefs file
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                }
            }
        }
        return uuid;
    }

}

