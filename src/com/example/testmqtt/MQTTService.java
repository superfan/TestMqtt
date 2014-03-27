package com.example.testmqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * @author Dominik Obermaier
 */
public class MQTTService extends Service {

    public static final String BROKER_URL = "tcp://220.180.134.38:8011";
    //public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";

    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
    public static final String clientId = "A000004927B96C";

    public static final String TOPIC = "de/eclipsemagazin/blackice/warnings";
    private MqttClient mqttClient;


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        try {
            mqttClient = new MqttClient(BROKER_URL, clientId, null);

            mqttClient.setCallback(new PushCallback(this));
            
            /*MqttConnectOptions opts = new MqttConnectOptions();
            opts.setCleanSession(true);
            opts.setConnectionTimeout(1);
            opts.setKeepAliveInterval(60);
            mqttClient.connect(opts);*/
            mqttClient.connect();

            //Subscribe to all subtopics of homeautomation
            mqttClient.subscribe(TOPIC);


        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
