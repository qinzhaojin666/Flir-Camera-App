package administrator.sahilpatel.com.flircameraapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Administrator on 9/19/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this.getApplicationContext());
    }
}
