package com.anil.akilhane;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    HttpOper mHttpOper = HttpOper.getInstance();
    User mUser = User.getInstance();
    Device mDevice = Device.getInstance();

    ImageButton btnLight,btnCurtainDoor,btnClimatization,btnWaterSystems;
    Button btnRefresh;

    //Context mContext = this;

    TextView tvDate, tvTime,tvReport;
    String date, time;

    Calendar c;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_keyboard:

                LinearLayout lihomeMessage = new LinearLayout(this);
                lihomeMessage.setOrientation(LinearLayout.VERTICAL);
                final EditText Etfirstline = new EditText(this);
                Etfirstline.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                Etfirstline.setHint("İlk Satır");
                final EditText Etsecondline = new EditText(this);
                Etsecondline.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                Etsecondline.setHint("İkinci Satır");
                lihomeMessage.addView(Etfirstline);
                lihomeMessage.addView(Etsecondline);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Kapı Mesajı");

                Etfirstline.setInputType(InputType.TYPE_CLASS_TEXT);
                Etsecondline.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(lihomeMessage);

                builder.setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String firstline = Etfirstline.getText().toString();
                        firstline = turkishchar(firstline);

                        String secondline = Etsecondline.getText().toString();
                        secondline = turkishchar(secondline);

                        String basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
                        basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                        mHttpOper.userinfoBase64 = basicAuth;
                        mHttpOper.post_homeMessage("/rest/lcd", firstline, secondline);

                    }
                });

                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;

            case R.id.action_logout:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Oturum Kapatılıyor")
                        .setMessage("Oturumu kapatmak istediğinizden emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("İptal", null)
                        .show();
                return true;

            case R.id.action_tasks:
                startActivity(new Intent(MainActivity.this, TasksActivity.class));
                return true;

            case R.id.action_parfume:
                mHttpOper.post_changeDeviceStatus("/rest/switch", 4, "On");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHttpOper.mContext = this;
        mHttpOper.mAct = this;

        c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        date = df.format(c.getTime());
        time = tf.format(c.getTime());

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(date);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText(time);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayUseLogoEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setLogo(R.drawable.actionbar_main_icon);
        mActionBar.setTitle("   " + "AkılHane");
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAF5F2")));

        try {
            String basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
            basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
            mHttpOper.userinfoBase64 = basicAuth;
            mHttpOper.get_humidity_heat("/rest/thermometer"); //demo
        } catch (Exception e) {
            Log.d("Hata", e.toString());
        }

        try {
            String basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
            basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
            mHttpOper.userinfoBase64 = basicAuth;
            mHttpOper.get_alldeviceStatus("/rest/status");
        } catch (Exception e){
            Log.d("Hata", e.toString());
        }

        try {
            String basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
            basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
            mHttpOper.userinfoBase64 = basicAuth;
            mHttpOper.get_report("/rest/activity");
        } catch (Exception e){
            Log.d("Hata", e.toString());
        }

        btnLight = (ImageButton) findViewById(R.id.btnLight);

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LightsActivity.class));
            }
        });

        btnCurtainDoor = (ImageButton) findViewById(R.id.btnCurtainDoor);

        btnCurtainDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CurtainDoorActivity.class));
            }
        });

        btnClimatization = (ImageButton) findViewById(R.id.btnClimatization);

        btnClimatization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClimatizationActivity.class));
            }
        });

        btnWaterSystems = (ImageButton) findViewById(R.id.btnWaterSystems);
        btnWaterSystems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WaterSystemsActivity.class));
            }
        });

        btnRefresh = (Button) findViewById(R.id.btn_Refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String basicAuth = mUser.getUsername() + ":" + mUser.getPassword();
                    basicAuth = Base64.encodeToString(basicAuth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                    mHttpOper.userinfoBase64 = basicAuth;
                    mHttpOper.get_report("/rest/activity");
                } catch (Exception e){
                    Log.d("Hata", e.toString());
                }
            }
        });



    }

    @Override
    public void onBackPressed() {

    }

    public String turkishchar(String line) {

        line = line.replace("ç", "c");
        line = line.replace("ı", "i");
        line = line.replace("ü", "u");
        line = line.replace("ğ", "g");
        line = line.replace("ö", "o");
        line = line.replace("ş", "s");
        line = line.replace("İ", "I");
        line = line.replace("Ğ", "G");
        line = line.replace("Ü", "U");
        line = line.replace("Ö", "O");
        line = line.replace("Ş", "S");
        line = line.replace("Ç", "C");

        return line;
    }


}
