package learn.advance.com.advancelearn.tabpagerview;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import learn.advance.com.advancelearn.R;

/**
 * Created by gongzibiao on 2018/3/2.
 */

public class MyFragment extends Fragment implements View.OnClickListener{
    private Context mContext;
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private Button btn_four;
    private TextView fragmentText;

    private String mFragmentVisible;
    private static MyFragment newFragment;

    public static MyFragment newInstance(String FragmentVisible) {
        newFragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", FragmentVisible);
        //bundle.putString("passwd", passwd);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentVisible=newFragment.getArguments().getString("name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        fragmentText = (TextView)view.findViewById(R.id.fragmentText);
        fragmentText.setText("");
        fragmentText.setText(mFragmentVisible);

        btn_one = (Button)view.findViewById(R.id.btn_one);
        btn_two = (Button)view.findViewById(R.id.btn_two);
        btn_three = (Button)view.findViewById(R.id.btn_three);
        btn_four = (Button)view.findViewById(R.id.btn_four);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_one:
                TextView mTextViewDeal = (TextView)getActivity().findViewById(R.id.tab_menu_deal_num);
                mTextViewDeal.setText("11");
                mTextViewDeal.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_two:
                TextView mTextViewPoi = (TextView)getActivity().findViewById(R.id.tab_menu_poi_num);
                mTextViewPoi.setText("99");
                mTextViewPoi.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_three:
                TextView mTextViewMore = (TextView)getActivity().findViewById(R.id.tab_menu_setting_partner);
                mTextViewMore.setText("999+");
                mTextViewMore.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_four:
                ImageView mImageView = (ImageView) getActivity ().findViewById(R.id.tab_menu_more_num);

                mImageView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
