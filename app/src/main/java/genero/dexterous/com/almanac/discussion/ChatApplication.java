package genero.dexterous.com.almanac.discussion;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by piyush on 1/6/16.
 */
public class ChatApplication extends MultiDexApplication {


    @Override

    public void onCreate() {

        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);


        Parse.initialize(this, "0h5gkyE7xp5o8wliA05MrtWKNuwiALohCM1aqWeq", "uNJ8aXJzrAVU1ZunDA7Wwt3mpzopey6DQ2j5tWwc");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());




    }



    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

}