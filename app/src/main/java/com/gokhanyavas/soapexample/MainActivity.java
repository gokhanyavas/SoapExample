package com.gokhanyavas.soapexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {


    private final String NAMESPACE = "http://www.w3schools.com/webservices/";
    private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
    private final String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
    private final String METHOD_NAME = "CelsiusToFahrenheit";
    private String TAG = "GOKHANYAVAS";
    private static String celcius;
    private static String fahren;
    Button b;
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Celcius Edittext Kontrol
        et = (EditText) findViewById(R.id.editText1);
        //Fahrenheit Text Kontrol
        tv = (TextView) findViewById(R.id.tv_result);

        //Buttona basıldığında webservis çağırılıyor.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Değer olup olmadığı kontrol ediliyor, değer yoksa uyarı veriliyor.
                if (et.getText().length() != 0 && et.getText().toString() != "") {

                    celcius = et.getText().toString();

                    AsyncCallWS task = new AsyncCallWS();

                    task.execute();
                    //Eğer textview boşsa uyarı ver.
                } else {
                    tv.setText("Celcius Değeri Girin!");
                }

            }
        });
    }


    public void getFahrenheit(String celsius) {
        //İstek yaratıyorum
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Parametreler
        PropertyInfo celsiusPI = new PropertyInfo();
        //Parametre ismi
        celsiusPI.setName("Celsius");
        //Parametre değeri
        celsiusPI.setValue(celsius);
        //Parametre data tipi
        celsiusPI.setType(double.class);
        //isteği parametreye ekliyorum.
        request.addProperty(celsiusPI);
        //Envelope oluşturuyorum
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // SOAP objesini set ediyorum
        envelope.setOutputSoapObject(request);
        // HTTP
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            fahren = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getFahrenheit(celcius);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            tv.setText(fahren + "° F");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            tv.setText("Hesaplanıyor...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
