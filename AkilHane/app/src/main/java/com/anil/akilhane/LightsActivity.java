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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by Anil on 15.03.2016.
 */
public class LightsActivity extends AppCompatActivity {

    HttpOper mHttpOper = HttpOper.getInstance();
    Device mDevice = Device.getInstance();
    User mUser = User.getInstance();

    Device lamp1,lamp2,lamp3,lamp4;
    JSONObject jsonLapmp1,jsonLapmp2,jsonLapmp3,jsonLapmp4;
    Integer Lapmp1Id,Lapmp2Id,Lapmp3Id,Lapmp4Id;
    Boolean Lapmp1Ac,Lapmp2Ac,Lapmp3Ac,Lapmp4Ac;
    String Lapmp1Loc,Lapmp1Nam,Lapmp1Sta,Lapmp2Loc,Lapmp2Nam,Lapmp2Sta,Lapmp3Loc,Lapmp3Nam,Lapmp3Sta,Lapmp4Loc,Lapmp4Nam,Lapmp4Sta;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudevice, menu);
        return super.onCreateOptionsMenu(menu);

    }

    List<Device> lightArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

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
            jsonLapmp1 = (JSONObject) AllDevicesStatus.get("1");
            Lapmp1Id = Integer.parseInt(jsonLapmp1.getString("ID"));
            Lapmp1Ac = Boolean.parseBoolean(jsonLapmp1.getString("active"));
            Lapmp1Loc = jsonLapmp1.getString("location");
            Lapmp1Nam = jsonLapmp1.getString("name");
            Lapmp1Sta = jsonLapmp1.getString("status");


            jsonLapmp2 = (JSONObject) AllDevicesStatus.get("2");
            Lapmp2Id = Integer.parseInt(jsonLapmp2.getString("ID"));
            Lapmp2Ac = Boolean.parseBoolean(jsonLapmp2.getString("active"));
            Lapmp2Loc = jsonLapmp2.getString("location");
            Lapmp2Nam = jsonLapmp2.getString("name");
            Lapmp2Sta = jsonLapmp2.getString("status");

            jsonLapmp3 = (JSONObject) AllDevicesStatus.get("3");
            Lapmp3Id = Integer.parseInt(jsonLapmp3.getString("ID"));
            Lapmp3Ac = Boolean.parseBoolean(jsonLapmp3.getString("active"));
            Lapmp3Loc = jsonLapmp3.getString("location");
            Lapmp3Nam = jsonLapmp3.getString("name");
            Lapmp3Sta = jsonLapmp3.getString("status");

            jsonLapmp4 = (JSONObject) AllDevicesStatus.get("11");
            Lapmp4Id = Integer.parseInt(jsonLapmp4.getString("ID"));
            Lapmp4Ac = Boolean.parseBoolean(jsonLapmp4.getString("active"));
            Lapmp4Loc = jsonLapmp4.getString("location");
            Lapmp4Nam = jsonLapmp4.getString("name");
            Lapmp4Sta = jsonLapmp4.getString("status");

        }catch (Exception e){
            Log.d("LightsAct", "Jsonparse Hatası");
        }

        lamp1 = new Device(Lapmp1Id,Lapmp1Ac,Lapmp1Loc,Lapmp1Nam,Lapmp1Sta);
        lightArrayList.add(new Device(lamp1.getId(), lamp1.getActive(), lamp1.getLocation(), lamp1.getName(), lamp1.getStatus()));

        lamp2 = new Device(Lapmp2Id,Lapmp2Ac,Lapmp2Loc,Lapmp2Nam,Lapmp2Sta);
        lightArrayList.add(new Device(lamp2.getId(), lamp2.getActive(), lamp2.getLocation(), lamp2.getName(), lamp2.getStatus()));

        lamp3 = new Device(Lapmp3Id,Lapmp3Ac,Lapmp3Loc,Lapmp3Nam,Lapmp3Sta);
        lightArrayList.add(new Device(lamp3.getId(), lamp3.getActive(), lamp3.getLocation(), lamp3.getName(), lamp3.getStatus()));

        lamp4 = new Device(Lapmp4Id,Lapmp4Ac,Lapmp4Loc,Lapmp4Nam,Lapmp4Sta);
        lightArrayList.add(new Device(lamp4.getId(), lamp4.getActive(), lamp4.getLocation(), lamp4.getName(), lamp4.getStatus()));

        ListView listemiz = (ListView) findViewById(R.id.liste);
        final OzelAdapter adaptorumuz = new OzelAdapter(this, lightArrayList);
        listemiz.setAdapter(adaptorumuz);

        listemiz.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id==0){
                    if (lamp1.getStatus().equals("Off")){
                        lamp1.setStatus("On");
                    }
                    else{
                        lamp1.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", lamp1.getId(), lamp1.getStatus());
                    lightArrayList.set(0, lamp1);
                    adaptorumuz.notifyDataSetChanged();

                }

                if(id==1){
                    if (lamp2.getStatus().equals("Off")){
                        lamp2.setStatus("On");
                    }
                    else{
                        lamp2.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", lamp2.getId(), lamp2.getStatus());
                    lightArrayList.set(1, lamp2);
                    adaptorumuz.notifyDataSetChanged();
                }

                if(id==2){
                    if (lamp3.getStatus().equals("Off")){
                        lamp3.setStatus("On");
                    }
                    else{
                        lamp3.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", lamp3.getId(), lamp3.getStatus());
                    lightArrayList.set(2, lamp3);
                    adaptorumuz.notifyDataSetChanged();
                }

                if(id==3){
                    if (lamp4.getStatus().equals("Off")){
                        lamp4.setStatus("On");
                    }
                    else{
                        lamp4.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", lamp4.getId(), lamp4.getStatus());
                    lightArrayList.set(3, lamp4);
                    adaptorumuz.notifyDataSetChanged();
                }
            }
        });
        //device sırayla id active location name status
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
            imageView.setImageResource(R.drawable.light_icon_cuted);

            return satirView;
        }
    }

}
