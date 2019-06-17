package com.mycompany.farm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mycompany.farm.R;
import com.mycompany.farm.adapter.MyListViewApader;
import com.mycompany.farm.bean.User;
import com.mycompany.farm.fragment.MultiplexingFragment;

import java.util.ArrayList;
import java.util.List;

public class ListviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private FrameLayout mFrame;
    private List<User> mList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager supportFragmentManager = getSupportFragmentManager();
    private MyListViewApader apader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mFrame = (FrameLayout) findViewById(R.id.mFrame);
    }

    private void initData() {
        //左边listView集合添加数据，适配器适配
        listViewData();
        //添加fragment,复用fragment
        addFragment();
        //默认选中ListView第一条item
        replese(0);
        //ListView第一条item的Select为true
        mList.get(0).setSelect(true);
        //listView点击事件
        mListView.setOnItemClickListener(this);
    }

    /*
     * 方法名： listViewData()
     * 功    能：左边listView集合添加数据
     * 参    数：无
     * 返回值：无
     */
    // 需要添加的几种农场管理操作
    private void listViewData() {
        mList.add(new User("土地管理"));
        mList.add(new User("生产材料购买"));
        mList.add(new User("配肥和配药管理"));
        mList.add(new User("生产计划管理"));
        mList.add(new User("农作记录管理"));
        mList.add(new User("实际采收管理"));
        mList.add(new User("提醒管理"));
        mList.add(new User("预计可采收量"));
        mList.add(new User("蔬菜经营盈亏"));

        //适配器适配
        apader = new MyListViewApader(mList, this);
        mListView.setAdapter(apader);
    }
    /*
     * 方法名：addFragment()
     * 功    能：添加fragment,复用fragment
     * 参    数：无
     * 返回值：无
     */
    private void addFragment() {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        for (int i = 0; i < mList.size(); i++) {
            Fragment multiplexing = multiplexingFragment.getMultiplexing(mList.get(i).getName(), "");
            mFragmentList.add(multiplexing);
        }
        //添加fragment
        for (int i = 0; i < mFragmentList.size(); i++) {
            transaction.add(R.id.mFrame, mFragmentList.get(i));
        }
        transaction.commit();
    }
    /*
     * 方法名：replese(int position)
     * 功    能：根据点击事件的下标切换fragment页面
     * 参    数：int position
     * 返回值：无
     */
    private void replese(int position) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        //所有的fragment隐藏，position对应的fragment显示，提交。
        for (int i = 0; i < mFragmentList.size(); i++) {
            Fragment fragment = mFragmentList.get(i);
            transaction.hide(fragment);
        }
        transaction.show(mFragmentList.get(position)).commit();
    }
    /*
     * 方法名：onItemClick(AdapterView<?> parent, View view, int position, long id)
     * 功    能：ListView的item点击事件
     * 参    数：AdapterView<?> parent, View view, int position, long id
     * 返回值：无
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //切换fragment
        replese(position);
        //在bean类里写的一个标记 boolean类型的isSelect是关键，默认无状态， 并设置get set方法
        //集合里所有数据的Select设置为flase,position下标所对应的item的Select为true，刷新适配器。
        for (int i = 0; i <mList.size() ; i++) {
            mList.get(i).setSelect(false);
        }
        mList.get(position).setSelect(true);
        //在刷新一下适配器就ok
        apader.notifyDataSetChanged();
        Toast.makeText(ListviewActivity.this, "" + position, Toast.LENGTH_SHORT).show();
    }
}

