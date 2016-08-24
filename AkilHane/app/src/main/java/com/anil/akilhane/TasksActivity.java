package com.anil.akilhane;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONObject;

/**
 * Created by Anil on 2.05.2016.
 */
public class TasksActivity extends AppCompatActivity {
    Spinner dropdown;
    RadioGroup rgPeriod,rgStatus;
    String period,status,time,process,note;
    EditText etTime,etProcess,etNote;

    User mUser = User.getInstance();
    HttpOper mHttpOper = HttpOper.getInstance();
    Device mDevice = Device.getInstance();

    Device lamp1,lamp2,lamp3,lamp4,nebuliser,door1,door2,motion,curtain,irrigation,valve,
            aircondit,selectedDevice;

    JSONObject jsonLapmp1,jsonLapmp2,jsonLapmp3,jsonLapmp4,jsonNebuliser,jsonDoor1,jsonDoor2,jsonMotion
            ,jsonCurtain,jsonIrrigation,jsonValve,jsonAircondit;

    Integer Lapmp1Id,Lapmp2Id,Lapmp3Id,Lapmp4Id,NebuliserId,Door1Id,Door2Id,MotionId,CurtainId,IrrigationId,
            ValveId,AirconditId;

    String Lapmp1Loc,Lapmp1Nam,Lapmp2Loc,Lapmp2Nam,Lapmp3Loc,Lapmp3Nam,Lapmp4Loc,Lapmp4Nam,NebuliserLoc,
            NebuliserNam,Door1Loc,Door1Nam,Door2Loc,Door2Nam,MotionLoc,MotionNam,CurtainLoc,CurtainNam
            ,IrrigationLoc,IrrigationNam,ValveLoc,ValveNam,AirconditLoc,AirconditNam;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudevice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        mHttpOper.mContext = this;

        etTime = (EditText) findViewById(R.id.etTime);
        etProcess = (EditText) findViewById(R.id.etProcess);
        etNote = (EditText) findViewById(R.id.etNote);

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
            Lapmp1Loc = jsonLapmp1.getString("location");
            Lapmp1Nam = jsonLapmp1.getString("name");

            jsonLapmp2 = (JSONObject) AllDevicesStatus.get("2");
            Lapmp2Id = Integer.parseInt(jsonLapmp2.getString("ID"));
            Lapmp2Loc = jsonLapmp2.getString("location");
            Lapmp2Nam = jsonLapmp2.getString("name");

            jsonLapmp3 = (JSONObject) AllDevicesStatus.get("3");
            Lapmp3Id = Integer.parseInt(jsonLapmp3.getString("ID"));
            Lapmp3Loc = jsonLapmp3.getString("location");
            Lapmp3Nam = jsonLapmp3.getString("name");

            jsonLapmp4 = (JSONObject) AllDevicesStatus.get("11");
            Lapmp4Id = Integer.parseInt(jsonLapmp4.getString("ID"));
            Lapmp4Loc = jsonLapmp4.getString("location");
            Lapmp4Nam = jsonLapmp4.getString("name");

            jsonNebuliser = (JSONObject) AllDevicesStatus.get("4");
            NebuliserId = Integer.parseInt(jsonNebuliser.getString("ID"));
            NebuliserLoc = jsonNebuliser.getString("location");
            NebuliserNam = jsonNebuliser.getString("name");

            jsonDoor1 = (JSONObject) AllDevicesStatus.get("5");
            Door1Id = Integer.parseInt(jsonDoor1.getString("ID"));
            Door1Loc = jsonDoor1.getString("location");
            Door1Nam = jsonDoor1.getString("name");

            jsonDoor2 = (JSONObject) AllDevicesStatus.get("12");
            Door2Id = Integer.parseInt(jsonDoor2.getString("ID"));
            Door2Loc = jsonDoor2.getString("location");
            Door2Nam = jsonDoor2.getString("name");

            jsonMotion = (JSONObject) AllDevicesStatus.get("6");
            MotionId = Integer.parseInt(jsonMotion.getString("ID"));
            MotionLoc = jsonMotion.getString("location");
            MotionNam = jsonMotion.getString("name");

            jsonCurtain = (JSONObject) AllDevicesStatus.get("7");
            CurtainId = Integer.parseInt(jsonCurtain.getString("ID"));
            CurtainLoc = jsonCurtain.getString("location");
            CurtainNam = jsonCurtain.getString("name");

            jsonIrrigation = (JSONObject) AllDevicesStatus.get("8");
            IrrigationId = Integer.parseInt(jsonIrrigation.getString("ID"));
            IrrigationLoc = jsonIrrigation.getString("location");
            IrrigationNam = jsonIrrigation.getString("name");

            jsonValve = (JSONObject) AllDevicesStatus.get("9");
            ValveId = Integer.parseInt(jsonValve.getString("ID"));
            ValveLoc = jsonValve.getString("location");
            ValveNam = jsonValve.getString("name");

            jsonAircondit = (JSONObject) AllDevicesStatus.get("10");
            AirconditId = Integer.parseInt(jsonAircondit.getString("ID"));
            AirconditLoc = jsonAircondit.getString("location");
            AirconditNam = jsonAircondit.getString("name");


        }catch (Exception e){
            Log.d("TasksAct", "Jsonparse Hatası");
        }

        //id active loc name status

        lamp1 = new Device(Lapmp1Id,true,Lapmp1Loc,Lapmp1Nam,"1");
        lamp2 = new Device(Lapmp2Id,true,Lapmp2Loc,Lapmp2Nam,"1");
        lamp3 = new Device(Lapmp3Id,true,Lapmp3Loc,Lapmp3Nam,"1");
        lamp4 = new Device(Lapmp4Id,true,Lapmp4Loc,Lapmp4Nam,"1");

        nebuliser = new Device(NebuliserId,true,NebuliserLoc,NebuliserNam,"1");

        door1 = new Device(Door1Id,true,Door1Loc,Door1Nam,"1");
        door2 = new Device(Door2Id,true,Door2Loc,Door2Nam,"1");

        motion = new Device(MotionId,true,MotionLoc,MotionNam,"1");

        curtain = new Device(CurtainId,true,CurtainLoc,CurtainNam,"1");

        irrigation = new Device(IrrigationId,true,IrrigationLoc,IrrigationNam,"1");

        valve = new Device(ValveId,true,ValveLoc,ValveNam,"1");

        aircondit = new Device(AirconditId,true,AirconditLoc,AirconditNam,"1");

        String[] devicesList = new String[]{lamp1.getLocation()+" "+lamp1.getName(),lamp2.getLocation()+" "+lamp2.getName(),
                lamp3.getLocation()+" "+lamp3.getName(),lamp4.getLocation()+" "+lamp4.getName(),
                nebuliser.getLocation()+" "+nebuliser.getName(),door1.getLocation()+" "+door1.getName(),
                door2.getLocation()+" "+door2.getName(),motion.getLocation()+" "+motion.getName(),
                curtain.getLocation()+" "+curtain.getName(),irrigation.getLocation()+" "+irrigation.getName(),
                valve.getLocation()+" "+valve.getName(),aircondit.getLocation()+" "+aircondit.getName()};

        dropdown = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, devicesList);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch ((int) id) {
                    case 0:
                        selectedDevice = lamp1;
                        break;
                    case 1:
                        selectedDevice = lamp2;
                        break;
                    case 2:
                        selectedDevice = lamp3;
                        break;
                    case 3:
                        selectedDevice = lamp4;
                        break;
                    case 4:
                        selectedDevice = nebuliser;
                        break;
                    case 5:
                        selectedDevice = door1;
                        break;
                    case 6:
                        selectedDevice = door2;
                        break;
                    case 7:
                        selectedDevice = motion;
                        break;
                    case 8:
                        selectedDevice = curtain;
                        break;
                    case 9:
                        selectedDevice = irrigation;
                        break;
                    case 10:
                        selectedDevice = valve;
                        break;
                    case 11:
                        selectedDevice = aircondit;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        rgPeriod = (RadioGroup) findViewById(R.id.rgPeriod);

        rgPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_always) {
                    period = "always";
                }
                else if(checkedId == R.id.radio_weekdays) {
                    period = "weekdays";
                }
                else if(checkedId == R.id.radio_weekends) {
                    period = "weekends";
                }

            }
        });

        rgStatus = (RadioGroup) findViewById(R.id.rgStatus);
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_on) {
                    status = "on";
                } else if (checkedId == R.id.radio_weekdays) {
                    status = "off";
                }
            }
        });

        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = etTime.getText().toString();
                process = etProcess.getText().toString();
                note = etNote.getText().toString();

                mHttpOper.post_deviceTask("/rest/tasks", selectedDevice.getId(), status, time, period, process, note);

                etNote.setText(" ");
                etProcess.setText(" ");
                etTime.setText(" ");
                rgPeriod.clearCheck();
                rgStatus.clearCheck();

            }
        });



    }


}
