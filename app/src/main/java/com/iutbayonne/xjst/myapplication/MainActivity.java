package com.iutbayonne.xjst.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.apisense.sdk.APISENSE;
import io.apisense.sdk.adapter.SimpleAPSCallback;
import io.apisense.sdk.core.store.Crop;

public class MainActivity extends AppCompatActivity {

    protected APISENSE apisense;
    protected APISENSE.Sdk sdk;
    protected String cropIdentifier = "a7cfa9f3-295a-486d-8d3f-45595b71ab74";
    // Install and start the collect, using your accessKey if the access is private
    private void installExperiment() {
        sdk.getCropManager().installOrUpdate(cropIdentifier,  new SimpleAPSCallback<Crop>() {
            @Override
            public void onDone(Crop crop) {
                super.onCreate(savedInstanceState);
                // Crop Installed, ready to be started.
                sdk.getCropManager().start(crop, new SimpleAPSCallback<Crop>() {
                    @Override
                    public void onDone(Crop crop) {
                        super.onCreate(savedInstanceState);
                        // Crop finally started.
                    }
                });
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize
        apisense = new APISENSE(getApplication());
        setContentView(R.layout.apisense_initialise_activity);

        //Authorize your SDK
        apisense.useSdkKey(cropIdentifier);
        setContentView(R.layout.sdk_autorise_activity);

        //Instanciate the SDK
        sdk = apisense.getSdk();
        setContentView(R.layout.apisense_instancie_activity);

        // Log your Bee user in if no session available.
        if (sdk.getSessionManager().isConnected()) {
            // Connecté à une session bee
            setContentView(R.layout.session_bee_disponible_activity);
            installExperiment();
        }
        else {
            // Non connecté à une session bee
            setContentView(R.layout.session_bee_non_disponible_activity);
            sdk.getSessionManager().applicationLogin(new SimpleAPSCallback<Void>() {
                @Override
                public void onDone(Void aVoid) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.dans_le_ondone_activity);
                    installExperiment(); // You can now install the experiment.
                }
            });
        }
    }
}
