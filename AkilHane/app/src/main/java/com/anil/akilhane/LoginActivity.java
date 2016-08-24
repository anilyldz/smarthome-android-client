package com.anil.akilhane;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Anil on 15.03.2016.
 */
public class LoginActivity extends Activity {

    HttpOper mHttpOper = HttpOper.getInstance();
    User mUser = User.getInstance();

    String basicAuth;
    EditText EtUserName, EtPassword, EtServerip;
    CheckBox cbTest;

    Context mContext;
    LoginActivity lAct = this;

    TextView twInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mHttpOper.mContext = this;
        mHttpOper.lAct = this;

        EtUserName = (EditText) findViewById(R.id.EtUsername);
        EtPassword = (EditText) findViewById(R.id.EtPassword);
        EtServerip = (EditText) findViewById(R.id.EtServerip);
        twInformation = (TextView) findViewById(R.id.twInformation);

        mUser.setUsername(EtUserName.getText().toString());
        mUser.setPassword(EtPassword.getText().toString());
        mUser.setServerip(EtServerip.getText().toString());

        cbTest = (CheckBox) findViewById(R.id.cbTest);

        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            EtUserName = (EditText) findViewById(R.id.EtUsername);
                                            EtPassword = (EditText) findViewById(R.id.EtPassword);
                                            EtServerip = (EditText) findViewById(R.id.EtServerip);
                                            twInformation = (TextView) findViewById(R.id.twInformation);

                                            mUser.setUsername(EtUserName.getText().toString());
                                            mUser.setPassword(EtPassword.getText().toString());
                                            mUser.setServerip(EtServerip.getText().toString());

                                            if (cbTest.isChecked()) {

                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                            } else {
                                                try {
                                                    basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
                                                    basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                                                    mHttpOper.userinfoBase64 = basicAuth;
                                                    mHttpOper.get_login("/rest/login");

                                                } catch (Exception e) {
                                                    Log.d("Hata", e.toString());
                                                }
                                            }
                                        }
                                    }
        );
    }
}
