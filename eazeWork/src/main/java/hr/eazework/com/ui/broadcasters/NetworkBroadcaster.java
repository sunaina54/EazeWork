package hr.eazework.com.ui.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hr.eazework.com.ui.util.Utility;


/**
 * Created by allsmartlt218 on 22-12-2016.
 */

public class NetworkBroadcaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(Utility.isNetworkAvailable(context)) {
//            Intent uploadTraIntent=new Intent(context,UploadInOutLocationService.class);
//            context.startService(uploadTraIntent);
        }
    }
}
