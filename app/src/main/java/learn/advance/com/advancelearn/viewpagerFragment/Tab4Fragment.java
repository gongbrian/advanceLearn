package learn.advance.com.advancelearn.viewpagerFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learn.advance.com.advancelearn.R;

/**
 * Created by gongzibiao on 2018/3/2.
 */

public class Tab4Fragment extends Fragment {
    private String mTextViewString;
    private TextView mTextView;
    private static Tab4Fragment newFragment;

    public static Tab4Fragment newInstance(String s) {
        newFragment = new Tab4Fragment();
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
        View view = inflater.inflate(R.layout.my_fragment,container,false);
        mTextView = (TextView)view.findViewById(R.id.textView);
        mTextView.setText(mTextViewString);
        return view;
    }
}
