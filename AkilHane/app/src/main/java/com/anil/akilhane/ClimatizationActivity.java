package com.anil.akilhane;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil on 2.05.2016.
 */
public class ClimatizationActivity extends AppCompatActivity {

    HttpOper mHttpOper = HttpOper.getInstance();
    Device mDevice = Device.getInstance();
    User mUser = User.getInstance();

    Device aircondit,combi;
    JSONObject jsonAircondit,jsonCombi;
    Integer AirconditId,CombiId;
    Boolean AirconditAc,CombiAc;
    String AirconditLoc,AirconditNam,AirconditSta,CombiLoc,CombiNam,CombiSta;

    TextView tvAirconditMain,tvAirconditStatus,tvCombitMain,tvCombiStatus;
    EditText etAirconditdegree,etCombidegree;
    Button btn_aircondit,btn_combi;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudevice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climatizationt);

        mHttpOper.mContext = this;

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);

        try {
            String basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
            basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
            mHttpOper.userinfoBase64 = basicAuth;
            mHttpOper.get_alldeviceStatus("/rest/status");
        }catch (Exception e){
            Log.d("Hata", e.toString());
        }

        JSONObject AllDevicesStatus = mDevice.getDeviceStatus();

        try {
            jsonAircondit = (JSONObject) AllDevicesStatus.get("10");
            AirconditId = Integer.parseInt(jsonAircondit.getString("ID"));
            AirconditAc = Boolean.parseBoolean(jsonAircondit.getString("active"));
            AirconditLoc = jsonAircondit.getString("location");
            AirconditNam = jsonAircondit.getString("name");
            AirconditSta = jsonAircondit.getString("status");

            jsonCombi= (JSONObject) AllDevicesStatus.get("10"); //kombinin idsi gelicek buraya
            CombiId = Integer.parseInt(jsonAircondit.getString("ID"));
            CombiAc = Boolean.parseBoolean(jsonAircondit.getString("active"));
            CombiLoc = jsonAircondit.getString("location");
            CombiNam = jsonAircondit.getString("name");
            CombiSta = jsonAircondit.getString("status");

        }catch (Exception e){
            Log.d("ClimtiAct", "Jsonparse Hatası");
        }

        aircondit = new Device(AirconditId,AirconditAc,AirconditLoc,AirconditNam,AirconditSta);


        tvAirconditMain = (TextView) findViewById(R.id.tvAirconditMain);
        tvAirconditStatus = (TextView) findViewById(R.id.tvAirconditStatus);
        etAirconditdegree = (EditText) findViewById(R.id.etAirconditDegree);
        btn_aircondit = (Button) findViewById(R.id.btn_aircondit);

        tvAirconditMain.setText(aircondit.getLocation()+" "+aircondit.getName());
        tvAirconditStatus.setText(aircondit.getStatus());

        btn_aircondit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aircondit.getStatus().equals("Off")) {
                    aircondit.setStatus("On");
                    tvAirconditStatus.setText("On");
                } else {
                    aircondit.setStatus("Off");
                    tvAirconditStatus.setText("Off");
                }
                mHttpOper.post_changeDeviceStatus("/rest/switch", aircondit.getId(), aircondit.getStatus());
            }
        });

        combi = new Device(CombiId,CombiAc,CombiLoc,CombiNam,CombiSta);

        tvCombitMain = (TextView) findViewById(R.id.tvCombiMain);
        tvCombiStatus = (TextView) findViewById(R.id.tvCombiStatus);
        etCombidegree = (EditText) findViewById(R.id.etCombiDegree);
        btn_combi = (Button) findViewById(R.id.btn_combi);

        tvAirconditMain.setText(combi.getLocation()+" "+combi.getName());
        tvAirconditStatus.setText(combi.getStatus());

        /*btn_combi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (combi.getStatus().equals("Off")) {
                    combi.setStatus("On");
                    tvCombiStatus.setText("On");
                } else {
                    combi.setStatus("Off");
                    tvCombiStatus.setText("Off");
                }
                mHttpOper.post_changeDeviceStatus("/rest/switch", combi.getId(), combi.getStatus());
            }
        });*/
    }
}
