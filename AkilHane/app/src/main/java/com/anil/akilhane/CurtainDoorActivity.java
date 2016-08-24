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
public class CurtainDoorActivity extends AppCompatActivity {

    List<Device> curtaindoorArraylist = new ArrayList<>();
    HttpOper mHttpOper = HttpOper.getInstance();
    User mUser = User.getInstance();
    Device mDevice = Device.getInstance();

    Device door1,door2,curtain1;
    JSONObject jsonDoor1,jsonDoor2,jsonCurtain1;
    Integer door1Id,door2Id,curtain1Id;
    Boolean door1Ac,door2Ac,curtain1Ac;
    String door1Loc,door1Nam,door1Sta,door2Loc,door2Nam,door2Sta,curtain1Loc,curtain1Nam,curtain1Sta;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudevice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_door);

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

        //2 door 1 curtain

        try {
            jsonDoor1 = (JSONObject) AllDevicesStatus.get("5");
            door1Id = Integer.parseInt(jsonDoor1.getString("ID"));
            door1Ac = Boolean.parseBoolean(jsonDoor1.getString("active"));
            door1Loc = jsonDoor1.getString("location");
            door1Nam = jsonDoor1.getString("name");
            door1Sta = jsonDoor1.getString("status");

            jsonDoor2 = (JSONObject) AllDevicesStatus.get("12");
            door2Id = Integer.parseInt(jsonDoor2.getString("ID"));
            door2Ac = Boolean.parseBoolean(jsonDoor2.getString("active"));
            door2Loc = jsonDoor2.getString("location");
            door2Nam = jsonDoor2.getString("name");
            door2Sta = jsonDoor2.getString("status");

            jsonCurtain1 = (JSONObject) AllDevicesStatus.get("7");
            curtain1Id = Integer.parseInt(jsonCurtain1.getString("ID"));
            curtain1Ac = Boolean.parseBoolean(jsonCurtain1.getString("active"));
            curtain1Loc = jsonCurtain1.getString("location");
            curtain1Nam = jsonCurtain1.getString("name");
            curtain1Sta = jsonCurtain1.getString("status");

        }catch (Exception e){
            Log.d("CurtainDoorAct", "Jsonparse Hatası");
        }

        door1 = new Device(door1Id,door1Ac,door1Loc,door1Nam,door1Sta);
        curtaindoorArraylist.add(new Device(door1.getId(), door1.getActive(), door1.getLocation(), door1.getName(), door1.getStatus()));

        door2 = new Device(door2Id,door2Ac,door2Loc,door2Nam,door2Sta);
        curtaindoorArraylist.add(new Device(door2.getId(), door2.getActive(),door2.getLocation(), door2.getName(), door2.getStatus()));

        curtain1 = new Device(curtain1Id,curtain1Ac,curtain1Loc,curtain1Nam,curtain1Sta);
        curtaindoorArraylist.add(new Device(curtain1.getId(),curtain1.getActive(),curtain1.getLocation(),curtain1.getName(),curtain1.getStatus()));

        ListView listemiz = (ListView) findViewById(R.id.liste);
        final OzelAdapter adaptorumuz = new OzelAdapter(this, curtaindoorArraylist);
        listemiz.setAdapter(adaptorumuz);

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    if (door1.getStatus().equals("Off")) {
                        door1.setStatus("On");
                    } else {
                        door1.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", door1.getId(), door1.getStatus());
                    curtaindoorArraylist.set(0, door1);
                    adaptorumuz.notifyDataSetChanged();
                }

                if (id == 1) {
                    if (door2.getStatus().equals("Off")) {
                        door2.setStatus("On");
                    } else {
                        door2.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", door2.getId(), door2.getStatus());
                    curtaindoorArraylist.set(1, door2);
                    adaptorumuz.notifyDataSetChanged();
                }

                if (id == 2) {
                    if (curtain1.getStatus().equals("Off")) {
                        curtain1.setStatus("On");
                    } else {
                        curtain1.setStatus("Off");
                    }
                    mHttpOper.post_changeDeviceStatus("/rest/switch", curtain1.getId(), curtain1.getStatus());
                    curtaindoorArraylist.set(2, curtain1);
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

            imageView.setImageResource(R.drawable.curtain_door_icon);

            /*if (device.getName().equals("Door")) {
                imageView.setImageResource(R.drawable.keyboard_icon);
            }
            else {
                imageView.setImageResource(R.drawable.locked_icon);
            }*/

            return satirView;
        }
    }
}
