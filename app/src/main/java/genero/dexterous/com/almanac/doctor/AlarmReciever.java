package genero.dexterous.com.almanac.doctor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import genero.dexterous.com.almanac.Home.HomeActivity;

/**
 * Created by piyush on 2/29/16.
 */
public class AlarmReciever extends BroadcastReceiver
{
//      @Override
public void onReceive(final Context context, Intent intent) {
    //this will update the UI with message
    HomeActivity inst = HomeActivity.instance();

    //this will sound the alarm tone
    //this will sound the alarm once, if you wish to
    //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    if (alarmUri == null) {
        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
    Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
    ringtone.play();

    //this will send a notification message
//    ComponentName comp = new ComponentName(context.getPackageName(),
//            AlarmService.class.getName());
//    startWakefulService(context, (intent.setComponent(comp)));
    setResultCode(Activity.RESULT_OK);
}
}

