package com.mycompany.farm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycompany.farm.R;

public class MultiplexingFragment extends Fragment{
    private String name;
    private String name2;
    private TextView mText;

    public MultiplexingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//------------在这里得到从下面的bundle里传来的两个string，并赋值给name和name2，并全局化.---------
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("string");
            name2 = arguments.getString("string2");
        }
    }


    //复用的静态方法，两个参数都是String类型的，代表从Activity里传过来的一个集合的Name，一个数组的url地址
    public static Fragment getMultiplexing(String string, String string2) {
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        Bundle bundle = new Bundle();
//得到并赋值给string和string2，通过bundle传递
        bundle.putString("string", string);
        bundle.putString("string2", string2);
        multiplexingFragment.setArguments(bundle);
        return multiplexingFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_multiplexing, container, false);
        initView(inflate);
        return inflate;
    }


    private void initView(View inflate) {
        mText = (TextView) inflate.findViewById(R.id.mText);
        mText.setText(name);
    }



}

/*
*
* MultiplexingFragment
*
*
* public class MultiplexingFragment extends Fragment {

    private String name;
    private String name2;
    private TextView mText;

    public MultiplexingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//------------在这里得到从下面的bundle里传来的两个string，并赋值给name和name2，并全局化.---------
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("string");
            name2 = arguments.getString("string2");
        }
    }


    //复用的静态方法，两个参数都是String类型的，代表从Activity里传过来的一个集合的Name，一个数组的url地址
    public static Fragment getMultiplexing(String string, String string2) {
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        Bundle bundle = new Bundle();
//得到并赋值给string和string2，通过bundle传递
        bundle.putString("string", string);
        bundle.putString("string2", string2);
        multiplexingFragment.setArguments(bundle);
        return multiplexingFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_multiplexing, container, false);
        initView(inflate);
        return inflate;
    }


    private void initView(View inflate) {
        mText = (TextView) inflate.findViewById(R.id.mText);
        mText.setText(name);
    }
}
*
* */
