package com.anil.akilhane;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil on 2.05.2016.
 */
public class WaterSystemsActivity extends AppCompatActivity {

    List<Device> WaterSystemsArraylist = new ArrayList<>();
    HttpOper mHttpOper = HttpOper.getInstance();
    User mUser = User.getInstance();
    Device mDevice = Device.getInstance();

    Device irrigation1,valve1;
    JSONObject jsonirrigation1,jsonvalve1;
    Integer irrigation1Id,valve1Id;
    Boolean irrigation1Ac,valve1Ac;
    String irrigation1Loc,irrigation1Nam,irrigation1Sta,valve1Loc,valve1Nam,valve1Sta;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudevice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_systems);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);

        mHttpOper.mContext = this;

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
            jsonirrigation1 = (JSONObject) AllDevicesStatus.get("8");
            irrigation1Id = Integer.parseInt(jsonirrigation1.getString("ID"));
            irrigation1Ac = Boolean.parseBoolean(jsonirrigation1.getString("active"));
            irrigation1Loc = jsonirrigation1.getString("location");
            irrigation1Nam = jsonirrigation1.getString("name");
            irrigation1Sta = jsonirrigation1.getString("status");

            jsonvalve1 = (JSONObject) AllDevicesStatus.get("9");
            valve1Id = Integer.parseInt(jsonvalve1.getString("ID"));
            valve1Ac = Boolean.parseBoolean(jsonvalve1.getString("active"));
            valve1Loc = jsonvalve1.getString("location");
            valve1Nam = jsonvalve1.getString("name");
            valve1Sta = jsonvalve1.getString("status");

        }catch (Exception e){
            Log.d("WaterSystemAct", "Jsonparse Hatası");
        }

        irrigation1 = new Device(irrigation1Id,irrigation1Ac,irrigation1Loc,irrigation1Nam,irrigation1Sta);
        WaterSystemsArraylist.add(new Device(irrigation1.getId(),irrigation1.getActive(),irrigation1.getLocation(),irrigation1.getName(),irrigation1.getStatus()));

        valve1 = new Device(valve1Id,valve1Ac,valve1Loc,valve1Nam,valve1Sta);
        WaterSystemsArraylist.add(new Device(valve1.getId(), valve1.getActive(),valve1.getLocation(), valve1.getName(), valve1.getStatus()));

        ListView listemiz = (ListView) findViewById(R.id.liste);
        final OzelAdapter adaptorumuz = new OzelAdapter(this, WaterSystemsArraylist);
        listemiz.setAdapter(adaptorumuz);

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    if (irrigation1.getStatus().equals("Off")) {
                        irrigation1.setStatus("On");
                    } else {
                        irrigation1.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", irrigation1.getId(), irrigation1.getStatus());
                    WaterSystemsArraylist.set(0, irrigation1);
                    adaptorumuz.notifyDataSetChanged();
                }

                if (id == 1) {
                    if (valve1.getStatus().equals("Off")) {
                        valve1.setStatus("On");
                    } else {
                        valve1.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", valve1.getId(), valve1.getStatus());
                    WaterSystemsArraylist.set(1, valve1);
                    adaptorumuz.notifyDataSetChanged();
                }



            }
        });

    }

    public class OzelAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Device> mLightsList;

        public OzelAdapter(Activity activity, List<Device> lights) {
            //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
            mInflater = (LayoutInflater) activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            //gösterilecek listeyi de alalım
            mLightsList = lights;
        }

        @Override
        public int getCount() {
            return mLightsList.size();
        }

        @Override
        public Device getItem(int position) {
            //şöyle de olabilir: public Object getItem(int position)
            return mLightsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View satirView;

            satirView = mInflater.inflate(R.layout.lineoflist_layout,null);
            TextView TvDeviceMain = (TextView) satirView.findViewById(R.id.TvDeviceMain);
            TextView TvDeviceStatus = (TextView) satirView.findViewById(R.id.TvDeviceStatus);
            ImageView imageView = (ImageView) satirView.findViewById(R.id.simge);

            Device device = mLightsList.get(position);

            TvDeviceMain.setText(device.getLocation()+" "+device.getName());
            TvDeviceStatus.setText(device.getStatus());

            imageView.setImageResource(R.drawable.water_systems_icon);

            /*if (device.getName().equals("Valve")) {
                imageView.setImageResource(R.drawable.keyboard_icon);
            }
            else {
                imageView.setImageResource(R.drawable.locked_icon);
            }*/

            return satirView;
        }
    }
}
