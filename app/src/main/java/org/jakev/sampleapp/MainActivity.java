package org.jakev.sampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jakev.serviceutils.ConnectorException;
import org.jakev.serviceutils.SystemServiceConnector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonDeviceId = (Button)findViewById(R.id.dev_id_button);
        Button buttonDial = (Button)findViewById(R.id.dial_button);

        buttonDeviceId.setOnClickListener(this);
        buttonDial.setOnClickListener(this);
    }

    /* Call getDeviceId() example */
    private void doGetDeviceId() {

        Toast toast;
        String response;

        try {
            SystemServiceConnector ssc = new SystemServiceConnector("iphonesubinfo",
                    "com.android.internal.telephony.IPhoneSubInfo");
            response = (String)ssc.callMethod("getDeviceId");

        } catch (ConnectorException e) {
            e.printStackTrace();

            response = "Error getting device ID! Check logs for more details.";
        }

        toast = Toast.makeText(this, response, Toast.LENGTH_LONG);
        toast.show();
    }

    /* Call dial("123456789") example */
    private void doDial() {

        String response;
        Toast toast;

        try {
            SystemServiceConnector ssc = new SystemServiceConnector("phone",
                    "com.android.internal.telephony.ITelephony");
            ssc.callMethod("dial", "123456789");

        } catch (ConnectorException e) {
            e.printStackTrace();

            response = "Error calling dial()! Check logs for more details.";
            toast = Toast.makeText(this, response, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dev_id_button:
                doGetDeviceId();
                break;
            case R.id.dial_button:
                doDial();
                break;
        }
    }
}
