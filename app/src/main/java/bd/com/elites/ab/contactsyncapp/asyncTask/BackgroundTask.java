package bd.com.elites.ab.contactsyncapp.asyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import bd.com.elites.ab.contactsyncapp.http.HttpRequests;
import bd.com.elites.ab.contactsyncapp.utils.Constants;
//import bd.com.elites.ab.contactsyncapp.utils.UtilityFunctions;



public class BackgroundTask extends AsyncTask<String, Integer, Object> {

    Context context;
    BackgroundTaskListener caller;


    public BackgroundTask(Context context, BackgroundTaskListener caller) {
        this.context = context;
        this.caller = caller;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // caller.onPreExecuteAsynctask(this.identifier);
    }

    @Override
    protected Object doInBackground(String... params) {

        Object result = null;

       // if (this.identifier == Constants.ASYNCTASK_IDENTIFIERS.LOGIN) {
            String url = Constants.WEB_SERVIES.BASE_URL + Constants.WEB_SERVIES.LOGIN;
            ContentValues values = new ContentValues();
            values.put("Name", params[0]);
            values.put("Phone", params[1]);

            result = HttpRequests.sendHttpPostRequest(url, values);
       // }

        return result;

    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        //caller.onPostExecuteAsynctask(this.identifier, result);
    }

}
