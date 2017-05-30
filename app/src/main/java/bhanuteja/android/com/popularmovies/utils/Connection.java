package bhanuteja.android.com.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 5/28/17.
 */

public class Connection {
    Context context;
    public Connection(Context context) {
        this.context = context;
    }
public boolean isInternet(){
    ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(manager!=null){
        NetworkInfo[] info = manager.getAllNetworkInfo();
        if (info != null)
            for (int i = 0; i < info.length; i++)
                if (info[i].getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
    }
    return false;
}


}
