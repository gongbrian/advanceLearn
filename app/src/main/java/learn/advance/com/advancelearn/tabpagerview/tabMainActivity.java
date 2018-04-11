package learn.advance.com.advancelearn.tabpagerview;

/**
 * Created by gongzibiao on 2018/3/2.
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import learn.advance.com.advancelearn.R;
import learn.advance.com.advancelearn.viewpagerFragment.Tab1Fragment;
import learn.advance.com.advancelearn.viewpagerFragment.Tab2Fragment;
import learn.advance.com.advancelearn.viewpagerFragment.Tab3Fragment;

public class tabMainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ly_one,ly_two,ly_three,ly_four;
    private TextView mTextView1,mTextView2,mTextView3,mTextView4;
    private TextView mTextNum1,mTextNum2,mTextNum3;
    private ImageView mImageView;

    private MyFragment f1,f2,f3,f4;
    private FragmentManager fragmentManager;

    ViewPager viewpager;
    ArrayList  listfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);

        bindView();
        //ly_one.performClick();
       FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //MyFragment fg1 = new MyFragment();
        //transaction.add(R.id.fragment_container ,fg1);
        //transaction.commit();
    }

    private void bindView() {

        ly_one = (LinearLayout)findViewById(R.id.ly_tab_menu_deal);
        ly_two = (LinearLayout)findViewById(R.id.ly_tab_menu_poi);
        ly_three = (LinearLayout)findViewById(R.id.ly_tab_menu_more);
        ly_four = (LinearLayout)findViewById(R.id.ly_tab_menu_user);

        mTextView1 = (TextView)findViewById(R.id.tab_menu_deal);
        mTextView2 = (TextView)findViewById(R.id.tab_menu_poi);
        mTextView3 = (TextView)findViewById(R.id.tab_menu_more);
        mTextView4 = (TextView)findViewById(R.id.tab_menu_user);

        mTextNum1 = (TextView)findViewById(R.id.tab_menu_deal_num);
        mTextNum2 = (TextView)findViewById(R.id.tab_menu_poi_num);
        mTextNum3 = (TextView)findViewById(R.id.tab_menu_more_num);

        mImageView = (ImageView)findViewById(R.id.tab_menu_setting_partner);

        ly_one.setOnClickListener(this);
        ly_two.setOnClickListener(this);
        ly_three.setOnClickListener(this);
        ly_four.setOnClickListener(this);

        viewpager = (ViewPager) findViewById(R.id.vp); //获取ViewPager
        listfragment=new ArrayList<Fragment>(); //new一个List<Fragment>
        Fragment tabf1 = Tab1Fragment.newInstance("第一个viewpage+fragment");
        Fragment tabf2 = Tab2Fragment.newInstance("第二个viewpage+fragment");;
        Fragment tabf3 = Tab3Fragment.newInstance("第三个viewpage+fragment");;//添加三个fragment到集合
        listfragment.add(tabf1);
        listfragment.add(tabf2);
        listfragment.add(tabf3);

        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        myFragmentPagerAdapter mfpa=new myFragmentPagerAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数

        viewpager.setAdapter(mfpa);
        viewpager.setCurrentItem(0); //设置当前页是第一页
    }

    //重置所有文本的选中状态
    private void setSelected() {
        mTextView1.setSelected(false);
        mTextView2.setSelected(false);
        mTextView3.setSelected(false);
        mTextView4.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            Log.e("gongzibiao",f1.toString());
            transaction.hide(f1);
        }
        if(f2!=null){
            Log.e("gongzibiao",f2.toString());
            transaction.hide(f2);
        }
        if(f3!=null){
            Log.e("gongzibiao",f3.toString());
            transaction.hide(f3);
        }
        if(f4!=null){
            Log.e("gongzibiao",f4.toString());
            transaction.hide(f4);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.ly_tab_menu_deal:
                setSelected();
                mTextView1.setSelected(true);
                mTextNum1.setVisibility(View.INVISIBLE);
                //
                viewpager.setCurrentItem(0);
                if(f1==null){
                    f1 = MyFragment.newInstance("第一个fragment");
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;
            case R.id.ly_tab_menu_poi:
                setSelected();
                mTextView2.setSelected(true);
                mTextNum2.setVisibility(View.INVISIBLE);
                viewpager.setCurrentItem(1);
                if(f2==null){
                    f2 = MyFragment.newInstance("第二个fragment");
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.ly_tab_menu_user:
                setSelected();
                mTextView4.setSelected(true);
                mImageView.setVisibility(View.INVISIBLE);
                viewpager.setCurrentItem(2);
                if(f4==null){
                    f4 = MyFragment.newInstance("第三个fragment");
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                break;

            case R.id.ly_tab_menu_more:
                setSelected();
                mTextView3.setSelected(true);
                mTextNum3.setVisibility(View.INVISIBLE);
                if(f3==null){
                    f3 = MyFragment.newInstance("第四个fragment");
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;
        }
        transaction.commit();
    }


}
