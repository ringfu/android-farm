package com.mycompany.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mycompany.farm.R;
import com.mycompany.farm.util.Base64Utils;
import com.mycompany.farm.util.SharedPreferencesUtils;
import com.mycompany.farm.widget.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
        //布局内的控件
        private EditText et_name;
        private EditText et_password;
        private Button mLoginBtn;
        private CheckBox checkBox_password;
        private CheckBox checkBox_login;
        private ImageView iv_see_password;

        private LoadingDialog mLoadingDialog; //显示正在加载的对话框


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);  // 跳转到 LoginActivity 后，通过 setContentView 绘制layout
            initViews(); // 一次性 findViewbyId 建立控件和view的联系，这是个好习惯
            setupEvents();
            initData();

        }

        private void initData() {

            //判断用户第一次登陆
            if (firstLogin()) {
                checkBox_password.setChecked(false);//取消记住密码的复选框
                checkBox_login.setChecked(false);//取消自动登录的复选框
            }
            //判断是否记住密码
            if (remenberPassword()) {
                checkBox_password.setChecked(true);//勾选记住密码
                setTextNameAndPassword();//把密码和账号输入到输入框中
            } else {
                setTextName();//把用户账号放到输入账号的输入框中
            }

            //判断是否自动登录
            if (autoLogin()) {
                checkBox_login.setChecked(true);
                login();//去登录就可以

            }
        }

        /**
         * 把本地保存的数据设置数据到输入框中
         */
        public void setTextNameAndPassword() {
            et_name.setText("" + getLocalName());
            et_password.setText("" + getLocalPassword());
        }

        /**
         * 设置数据到输入框中
         */
        public void setTextName() {
            et_name.setText("" + getLocalName());
        }


        /**
         * 获得保存在本地的用户名
         */
        public String getLocalName() {
            //获取SharedPreferences对象，使用自定义类的方法来获取对象
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            String name = helper.getString("name");
            return name;
        }


        /**
         * 获得保存在本地的密码
         */
        public String getLocalPassword() {
            //获取SharedPreferences对象，使用自定义类的方法来获取对象
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            String password = helper.getString("password");
            return Base64Utils.decryptBASE64(password);   //解码一下
//       return password;   //解码一下

        }

        /**
         * 判断是否自动登录
         */
        private boolean autoLogin() {
            //获取SharedPreferences对象，使用自定义类的方法来获取对象
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            boolean autoLogin = helper.getBoolean("autoLogin", false);
            return autoLogin;
        }

        /**
         * 判断是否记住密码
         */
        private boolean remenberPassword() {
            //获取SharedPreferences对象，使用自定义类的方法来获取对象
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            boolean remenberPassword = helper.getBoolean("remenberPassword", false);
            return remenberPassword;
        }

        // 连接控件和布局view对象
        private void initViews() {
            mLoginBtn = (Button) findViewById(R.id.btn_login);
            et_name = (EditText) findViewById(R.id.et_account);
            et_password = (EditText) findViewById(R.id.et_password);
            checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);
            checkBox_login = (CheckBox) findViewById(R.id.checkBox_login);
            iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
        }
        // 设置全部控件监听器
        private void setupEvents() {
            mLoginBtn.setOnClickListener(this);
            checkBox_password.setOnCheckedChangeListener(this);
            checkBox_login.setOnCheckedChangeListener(this);
            iv_see_password.setOnClickListener(this);

        }

        /**
         * 判断是否是第一次登陆
         */
        private boolean firstLogin() {
            //获取SharedPreferences对象，使用自定义类的方法来获取对象
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            boolean first = helper.getBoolean("first", true);
            if (first) {
                //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
                helper.putValues(new SharedPreferencesUtils.ContentValue("first", false),
                        new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                        new SharedPreferencesUtils.ContentValue("autoLogin", false),
                        new SharedPreferencesUtils.ContentValue("name", ""),
                        new SharedPreferencesUtils.ContentValue("password", ""));
                return true;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    loadUserName();    //无论如何保存一下用户名
                    login(); //登陆
                    break;
                case R.id.iv_see_password:
                    setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                    break;

            }
        }

        /**
         * 模拟登录情况
         */
        private void login() {

            //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
            if (getAccount().isEmpty()){
                showToast("你输入的账号为空！");
                return;
            }

            if (getPassword().isEmpty()){
                showToast("你输入的密码为空！");
                return;
            }
            //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
            showLoading();//显示加载框
            Thread loginRunnable = new Thread() {

                @Override
                public void run() {
                    super.run();
                    setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态


                    //睡眠3秒
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //判断账号和密码
                    if (getAccount().equals("admin") && getPassword().equals("2018122")) {
                        showToast("登录成功");

                        loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;
                        // 登录成功跳转到listview对应界面
                        startActivity(new Intent(LoginActivity.this, ListviewActivity.class));
                        finish();//关闭页面
                    } else {
                        showToast("输入的登录账号或密码不正确");
                    }

                    setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                    hideLoading();//隐藏加载框
                }
            };
            loginRunnable.start();


        }


        /**
         * 保存用户账号
         */
        public void loadUserName() {
            if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {
                SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
                helper.putValues(new SharedPreferencesUtils.ContentValue("name", getAccount()));
            }

        }

        /**
         * 设置密码可见和不可见的相互转换
         */
        private void setPasswordVisibility() {
            if (iv_see_password.isSelected()) {
                iv_see_password.setSelected(false);
                //密码不可见
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            } else {
                iv_see_password.setSelected(true);
                //密码可见
                et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

        }

        /**
         * 获取账号
         */
        public String getAccount() {
            return et_name.getText().toString().trim();//去掉空格
        }

        /**
         * 获取密码
         */
        public String getPassword() {
            return et_password.getText().toString().trim();//去掉空格
        }


        /**
         * 保存用户选择“记住密码”和“自动登陆”的状态
         */
        private void loadCheckBoxState() {
            loadCheckBoxState(checkBox_password, checkBox_login);
        }

        /**
         * 保存按钮的状态值
         */
        public void loadCheckBoxState(CheckBox checkBox_password, CheckBox checkBox_login) {

            //获取SharedPreferences对象，使用自定义类的方法来获取对象
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

            //如果设置自动登录
            if (checkBox_login.isChecked()) {
                //创建记住密码和自动登录是都选择,保存密码数据
                helper.putValues(
                        new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                        new SharedPreferencesUtils.ContentValue("autoLogin", true),
                        new SharedPreferencesUtils.ContentValue("password", Base64Utils.encryptBASE64(getPassword())));

            } else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
                //创建记住密码和自动登录是默认不选,密码为空
                helper.putValues(
                        new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                        new SharedPreferencesUtils.ContentValue("autoLogin", false),
                        new SharedPreferencesUtils.ContentValue("password", ""));
            } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
                //创建记住密码为选中和自动登录是默认不选,保存密码数据
                helper.putValues(
                        new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                        new SharedPreferencesUtils.ContentValue("autoLogin", false),
                        new SharedPreferencesUtils.ContentValue("password", Base64Utils.encryptBASE64(getPassword())));
            }
        }

        /**
         * 是否可以点击登录按钮
         *
         * @param clickable
         */
        public void setLoginBtnClickable(boolean clickable) {
            mLoginBtn.setClickable(clickable);
        }


        /**
         * 显示加载的进度款
         */
        public void showLoading() {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
            }
            mLoadingDialog.show();
        }


        /**
         * 隐藏加载的进度框
         */
        public void hideLoading() {
            if (mLoadingDialog != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.hide();
                    }
                });

            }
        }


        /**
         * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
         *
         * @param buttonView 按钮对象
         * @param isChecked  按钮的状态
         */
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == checkBox_password) {  //记住密码选框发生改变时
                if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                    checkBox_login.setChecked(false);
                }
            } else if (buttonView == checkBox_login) {   //自动登陆选框发生改变时
                if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                    checkBox_password.setChecked(true);
                }
            }
        }


        /**
         * 监听回退键
         */
        @Override
        public void onBackPressed() {
            if (mLoadingDialog != null) {
                if (mLoadingDialog.isShowing()) {
                    mLoadingDialog.cancel();
                } else {
                    finish();
                }
            } else {
                finish();
            }

        }

        /**
         * 页面销毁前回调的方法
         */
        protected void onDestroy() {
            if (mLoadingDialog != null) {
                mLoadingDialog.cancel();
                mLoadingDialog = null;
            }
            super.onDestroy();
        }


        public void showToast(final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });

        }



    }
