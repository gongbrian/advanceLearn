package learn.advance.com.advancelearn.viewpagerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import learn.advance.com.advancelearn.R;
import learn.advance.com.advancelearn.spinner.SpinnerMainActivity;

public class VFMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_a;
    private Button bt_a;


    ViewPager viewpager;
    ArrayList listfragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    tv_a.setText(R.string.title_home);
                    viewpager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    tv_a.setText(R.string.title_dashboard);
                    viewpager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    tv_a.setText(R.string.title_notifications);
                    viewpager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vf_activity_main);

        bt_a=(Button)findViewById(R.id.bt_a);
        bt_a.setOnClickListener(this);

        viewpager = (ViewPager) findViewById(R.id.vp); //获取ViewPager
        listfragment=new ArrayList<Fragment>(); //new一个List<Fragment>
        Fragment tabf1 = Tab1Fragment.newInstance("第一个viewpage+fragment");
        Fragment tabf2 = Tab2Fragment.newInstance("第二个viewpage+fragment");;
        Fragment tabf3 = Tab3Fragment.newInstance("第三个viewpage+fragment");;//添加三个fragment到集合
        listfragment.add(tabf1);
        listfragment.add(tabf2);
        listfragment.add(tabf3);

        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        learn.advance.com.advancelearn.tabpagerview.myFragmentPagerAdapter mfpa=new learn.advance.com.advancelearn.tabpagerview.myFragmentPagerAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数

        viewpager.setAdapter(mfpa);
        viewpager.setCurrentItem(0); //设置当前页是第一页

        tv_a = (TextView) findViewById(R.id.tv_a);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void onClick(View v){
        Toast.makeText(this,"点击了。。。", Toast.LENGTH_SHORT);
        switch(v.getId()) {
            case R.id.bt_a:
                Intent intenta = new Intent(VFMainActivity.this,SpinnerMainActivity.class);
                startActivity(intenta);
                break;
        }
    }

}
