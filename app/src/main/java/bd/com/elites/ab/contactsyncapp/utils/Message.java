package bd.com.elites.ab.contactsyncapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by forhad on 12-08-2016.
 */
public class Message {
    public static void message(Context context, String message){
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
    }
}
