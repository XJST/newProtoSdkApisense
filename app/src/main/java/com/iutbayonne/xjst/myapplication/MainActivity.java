package com.iutbayonne.xjst.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Set;

import io.apisense.sdk.APISENSE;
import io.apisense.sdk.adapter.SimpleAPSCallback;
import io.apisense.sdk.core.store.Crop;

public class MainActivity extends AppCompatActivity {

    protected APISENSE apisense;
    protected APISENSE.Sdk sdk;
    protected String cropIdentifier = "abglrkyDVZiQwj6JmRLK";
    protected String sdkKey = "0aa513d7-7b8c-4673-b928-2ee6aa67bfdf";

    // Install and start the collect, using your accessKey if the access is private
    private void installExperiment() {
        Log.e("CropInstalled", "Install and start the collect, using your accessKey if the access is private");
        sdk.getCropManager().installOrUpdate(cropIdentifier,  new SimpleAPSCallback<Crop>() {
            @Override
            public void onDone(Crop crop) {
                // Crop Installed, ready to be started.
                Log.e("CropInstalled", "Crop Installed, ready to be started.");
                start(crop);
            }
        });
    }
    private void start(Crop crop) {
        Set<String> deniedPermissions = sdk.getCropManager().deniedPermissions(crop);
        if (deniedPermissions.isEmpty()) {
            sdk.getCropManager().start(crop, new SimpleAPSCallback<Crop>() {
                @Override
                public void onDone(Crop crop) {
                    // Crop finally started.
                }
            });
        } else {
            // Request missing permissions before starting the crop
            // We consider that the user will accept
            ActivityCompat.requestPermissions(this, deniedPermissions.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize
        apisense = new APISENSE(getApplication());
        Log.e("ApisenseInitialised", "Initialize");

        //Authorize your SDK
        apisense.useSdkKey(sdkKey);
        Log.e("SDK_Authorized", "Authorize your SDK");

        //Instanciate the SDK
        sdk = apisense.getSdk();
        Log.e("ApisenseInstanceiate", "Apisense instanciate");

        // Log your Bee user in if no session available.
        if (sdk.getSessionManager().isConnected()) {
            // Connecté à une session bee
            Log.e("BeeConnected", "Connected with Bee user");
            installExperiment();
        }
        else {
            // Non connecté à une session bee
            Log.e("BeeNotConnected", "Not Connected with Bee user");
            sdk.getSessionManager().applicationLogin(new SimpleAPSCallback<Void>() {
                @Override
                public void onDone(Void aVoid) {
                    Log.e("VoidBeeUserConnection", "Connected with a Void Bee user");
                    installExperiment(); // You can now install the experiment.
                }
            });
        }
    }
}
