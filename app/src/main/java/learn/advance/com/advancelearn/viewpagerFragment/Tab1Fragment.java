package learn.advance.com.advancelearn.viewpagerFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import learn.advance.com.advancelearn.R;

/**
 * Created by gongzibiao on 2018/3/2.
 */

public class Tab1Fragment extends Fragment {
    private String mTextViewString;
    private TextView mTextView;
    private static Tab1Fragment newFragment;

    private GridView maingv;
    private GridView maingv_quick;

    public static Tab1Fragment newInstance(String s) {
        newFragment = new Tab1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", s);
        //bundle.putString("passwd", passwd);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextViewString=newFragment.getArguments().getString("name");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        mTextView = (TextView)view.findViewById(R.id.textView);
        mTextView.setText(mTextViewString);

        //获取到GridView
        maingv = (MyGridView) view.findViewById(R.id.gridview);
        maingv_quick = (MyGridView) view.findViewById(R.id.gridview_quick);
        //给gridview设置数据适配器
        maingv.setAdapter(new MainGridViewAdapter(getActivity()));
        maingv_quick.setAdapter(new MainGridViewAdapter(getActivity()));
        //点击事件
        maingv.setOnItemClickListener(new MainItemClickListener());
        maingv_quick.setOnItemClickListener(new MainItemClickListener());
        return view;
    }

    private class MainItemClickListener implements AdapterView.OnItemClickListener {
        /**
         * @param parent 代表当前的gridview
         * @param view 代表点击的item
         * @param position 当前点击的item在适配中的位置
         * @param id 当前点击的item在哪一行
         */
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            switch (position) {
                case 0:
                    Intent intent = new Intent("android.intent.action.tabMainActivity");
                    startActivity(intent);
                    break;
            }
        }
    }

}
