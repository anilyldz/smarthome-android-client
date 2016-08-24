package com.anil.akilhane;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Anil on 17.03.2016.
 */
public class HttpOper {

    private static HttpOper instance = new HttpOper();

    private HttpOper() {
    }

    public static HttpOper getInstance() {
        return instance;
    }

    User mUser = User.getInstance();
    Device mDevice = Device.getInstance();

    String userinfoBase64, humidity, heat,temperature,responseServer,responseServer2,responseServer3;
    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response;
    Boolean loginResult = false;
    Context mContext;
    LoginActivity lAct;
    MainActivity mAct;

    public void get_login(final String url) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpGet httpget = new HttpGet(mUser.getServerip() + url);
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.addHeader("Authorization", "Basic " + userinfoBase64);
                    response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    responseServer = EntityUtils.toString(entity);
                    Log.d("get_login Resp", response.getStatusLine().toString());

                } catch (Exception e) {
                    Log.d("get_login", "doInBackground Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    final ProgressDialog loadingDialog = ProgressDialog.show(lAct, "",
                            "Oturum Açılıyor. Lütfen Bekleyiniz...", true);
                    loadingDialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            loadingDialog.dismiss();

                            if (responseServer != null) {
                                loginResult = response.getStatusLine().toString().equals("HTTP/1.1 200 OK");
                            }

                            if (loginResult) {
                                mContext.startActivity(new Intent(mContext, MainActivity.class));
                            } else {
                                ((TextView) lAct.findViewById(R.id.twInformation)).setText("Login Sırasında Hata Oluştu");
                            }

                        }
                    }, 2000);

                } catch (Exception e) {
                    Log.d("get_login", "onPostExecute Hatası");

                }
            }
        }.execute(null, null, null);
    }


    public void get_humidity_heat(final String url) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ((TextView) mAct.findViewById(R.id.twHumidity)).setText("...");
                ((TextView) mAct.findViewById(R.id.twHeat)).setText("...");
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpGet httpget = new HttpGet(mUser.getServerip() + url);
                    //Post the data:
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.addHeader("Authorization", "Basic " + userinfoBase64);
                    response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    responseServer = EntityUtils.toString(entity);
                    Log.d("get_humidity_heat Resp", responseServer);

                } catch (Exception e) {
                    Log.d("get_humidity_heat", "doInBackground Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (responseServer != null) {
                    try {
                        JSONObject tempatureJson = new JSONObject(responseServer);
                        temperature = tempatureJson.getString("thermometer");
                        String[] separatedtemp = temperature.split("/");
                        humidity = separatedtemp[1];
                        heat = separatedtemp[0];
                        ((TextView) mAct.findViewById(R.id.twHumidity)).setText(humidity);
                        ((TextView) mAct.findViewById(R.id.twHeat)).setText(heat + "ºC");
                    } catch (Exception e) {
                        Log.d("get_humidity_heat", "onPostExecute Hatası");
                    }
                } else {
                    ((TextView) mAct.findViewById(R.id.twHumidity)).setText("Hata");
                    ((TextView) mAct.findViewById(R.id.twHeat)).setText("Hata");
                }

            }
        }.execute(null, null, null);
    }

    public void post_changeDeviceStatus(final String url, final int deviceid, final String status) {
        class RequestTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... uri) {

                try {
                    HttpPost httppost = new HttpPost(mUser.getServerip() + url);

                    JSONObject json = new JSONObject();
                    json.put("deviceid", deviceid);
                    json.put("status", status);
                    HttpEntity e2 = new StringEntity(json.toString());
                    httppost.setEntity(e2);

                    httppost.setHeader("Content-Type", "application/json");
                    httppost.addHeader("Authorization", "Basic " + userinfoBase64);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    responseServer3  = EntityUtils.toString(entity);

                    Log.d("post_changeDeviceStatus", responseServer3);

                    JSONObject Jsonofffer = new JSONObject(responseServer3);
                    String name = Jsonofffer.getString("username"); //username değil offer olucak
                    mDevice.setOffer(name);

                    Log.d("post_changeDeviceStatus", name);

                    //offer burda dönücek jsondan offerlı kısmı alırsın on post ex. ta ilgili acte toast olarak çıkartırsın

                } catch (Exception e) {
                    Log.d("post_changeDeviceStatus", "onPostExecute Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    if (response.getStatusLine().toString().equals("HTTP/1.1 201 CREATED")) {
                        //loginResult = response.getStatusLine().toString().equals("HTTP/1.1 201 CREATED");
                        Toast.makeText(mContext, "İşlem Başarıyla Gerçekleştirildi", Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, mDevice.getOffer(), Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(mContext, "Hata Oluştu", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Log.d("get_login", "onPostExecute Hatası");

                }

            }
        }
        new RequestTask().execute();
    }


    public void post_homeMessage(final String url, final String firstline, final String secondline) {
        class RequestTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... uri) {

                try {
                    HttpPost httppost = new HttpPost(mUser.getServerip() + url);

                    JSONObject json = new JSONObject();
                    json.put("first line", firstline);
                    json.put("second line", secondline);
                    HttpEntity e2 = new StringEntity(json.toString());
                    httppost.setEntity(e2);

                    httppost.setHeader("Content-Type", "application/json");
                    httppost.addHeader("Authorization", "Basic " + userinfoBase64);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);


                    Log.d("post_homeMessage", result); //response.getStatusLine().toString()

                } catch (Exception e) {
                    Log.d("post_homeMessage", "onPostExecute Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    if (responseServer != null) {
                        loginResult = response.getStatusLine().toString().equals("HTTP/1.1 201 CREATED");
                    }

                    if (loginResult) {
                        Toast.makeText(mContext, "İşlem Başarıyla Gerçekleştirildi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Hata Oluştu", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("get_login", "onPostExecute Hatası");

                }

            }
        }
        new RequestTask().execute();
    }


    public void post_deviceTask(final String url, final int deviceid, final String status, final String time,
                                final String period,final String process, final String note) {
        class RequestTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... uri) {

                try {
                    HttpPost httppost = new HttpPost(mUser.getServerip() + url);

                    JSONObject json = new JSONObject();
                    json.put("deviceid", deviceid);
                    json.put("switch", status);
                    json.put("time", time);
                    json.put("period", period);
                    json.put("process", process);
                    json.put("note", note);

                    HttpEntity e2 = new StringEntity(json.toString());
                    httppost.setEntity(e2);

                    httppost.setHeader("Content-Type", "application/json");
                    httppost.addHeader("Authorization", "Basic " + userinfoBase64);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);

                    Log.d("post_deviceTask", result);

                } catch (Exception e) {
                    Log.d("post_deviceTask", "doInBackground Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    if (responseServer != null) {
                        loginResult = response.getStatusLine().toString().equals("HTTP/1.1 201 CREATED");
                    }

                    if (loginResult) {
                        Toast.makeText(mContext, "İşlem Başarıyla Gerçekleştirildi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Hata Oluştu", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("get_login", "onPostExecute Hatası");

                }

            }
        }
        new RequestTask().execute();
    }

    public class ScheduledService extends Service {
        private Timer timer = new Timer();

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    get_humidity_heat("/rest/anil");
                }
            }, 0, 5 * 60 * 1000);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }


    public void get_alldeviceStatus(final String url) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpGet httpget = new HttpGet(mUser.getServerip() + url);
                    //Post the data:
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.addHeader("Authorization", "Basic " + userinfoBase64);
                    response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    responseServer = EntityUtils.toString(entity);
                    Log.d("get_deviceStatus Resp", responseServer);

                } catch (Exception e) {
                    Log.d("get_deviceStatus", "doInBackground Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (responseServer != null) {
                    try {
                        JSONObject statusJson = new JSONObject(responseServer);
                        JSONObject sys  = statusJson.getJSONObject("device");
                        mDevice.setDeviceStatus(sys);

                    } catch (Exception e) {
                        Log.d("get_deviceStatus", "onPostExecute Hatası");
                    }
                }
            }
        }.execute(null, null, null);
    }

    public void get_report(final String url) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ((TextView) mAct.findViewById(R.id.tvReport)).setText("...");
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpGet httpget = new HttpGet(mUser.getServerip() + url);
                    //Post the data:
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.addHeader("Authorization", "Basic " + userinfoBase64);
                    response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    responseServer2 = EntityUtils.toString(entity);
                    Log.d("get_report", responseServer2);

                } catch (Exception e) {
                    Log.d("get_report hatası", "doInBackground Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (responseServer2 != null) {
                    try {
                        JSONObject statusJson2 = new JSONObject(responseServer2);

                        JSONObject jsonReport = (JSONObject) statusJson2.get("activity");
                        String report_username = jsonReport.getString("username");
                        String report_date = jsonReport.getString("date");
                        String report_name = jsonReport.getString("device_name");
                        String report_currentstatus = jsonReport.getString("currentstatus");
                        ((TextView) mAct.findViewById(R.id.tvReport)).setText("Kullanıcı " + report_username + " " + report_date +
                                " Saatinde " + report_name + " adlı aygıtı " + report_currentstatus + " Durumuna Getirdi");

                    } catch (Exception e) {
                        Log.d("get_deviceStatus", "onPostExecute Hatası");
                    }
                }
            }
        }.execute(null, null, null);
    }


    /*public void get_allTasks(final String url) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpGet httpget = new HttpGet(mUser.getServerip() + url);
                    //Post the data:
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.addHeader("Authorization", "Basic " + userinfoBase64);
                    response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    responseServer3 = EntityUtils.toString(entity);
                    Log.d("get_allTasks Resp", responseServer3);

                } catch (Exception e) {
                    Log.d("get_allTasks", "doInBackground Hatası");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (responseServer3 != null) {
                    try {
                        JSONObject sys  = new JSONObject(responseServer3);
                        JSONObject tasksList = sys.getJSONObject("tasks");

                        mDevice.seTtasks(tasksList);

                    } catch (Exception e) {
                        Log.d("get_deviceStatus", "onPostExecute Hatası");
                    }
                }
            }
        }.execute(null, null, null);
    }

    public void get_deleteTasks(final String url) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpGet httpget = new HttpGet(mUser.getServerip() + url);
                    //Post the data:
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.addHeader("Authorization", "Basic " + userinfoBase64);
                    response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    responseServer = EntityUtils.toString(entity);
                    Log.d("get_deleteTasks Resp", responseServer);

                } catch (Exception e) {
                    Log.d("get_deleteTasks", "doInBackground Hatası");
                }
                return null;
            }
        }.execute(null, null, null);
    }*/
}
