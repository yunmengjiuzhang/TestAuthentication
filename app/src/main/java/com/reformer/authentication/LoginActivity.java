//package com.reformer.authentication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.reformer.authentication.utils.UIUtils;
//
//public class LoginActivity extends BaseActivity implements View.OnClickListener {
//
//    private Button btn_login;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        btn_login = (Button) findViewById(R.id.btn_login);
//        btn_login.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_login:
//                UIUtils.startActivity(new Intent(mCtx, MainActivity.class));
//                break;
//        }
//    }
//}
