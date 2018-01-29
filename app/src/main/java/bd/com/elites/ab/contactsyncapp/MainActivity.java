package bd.com.elites.ab.contactsyncapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import bd.com.elites.ab.contactsyncapp.Model.Data;
import bd.com.elites.ab.contactsyncapp.asyncTask.BackgroundTask;
import bd.com.elites.ab.contactsyncapp.database.DBHelper;
import bd.com.elites.ab.contactsyncapp.utils.Message;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    BackgroundTask backgroundTask = null;
    SwipeRefreshLayout swipeView;
    DBHelper helper,helper2;
    ListView lv1;
    String namecsv = "";
    String phonecsv = "";
    String url = "http://192.168.201.1:80/ScriptAppSync/ContactSyncApp/insert_contact.php";
    String url_delete = "http://192.168.201.1:80/ScriptAppSync/ContactSyncApp/delete_contact.php";
    //String namearray[];
    //String phonearray[];
    ArrayList<String> namearray;
    ArrayList<String> phonearray;



    ProgressDialog progressBar;
    SQLiteDatabase db,db2;

//
    List<Data> list,filteredDataList;
    /*
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter adapter;
     */
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter adapter;

    //

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        // mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        // backgroundTask = new BackgroundTask(this, this);
        setContentView(R.layout.activity_main);
       // lv1 = (ListView) findViewById(R.id.listView1);
        namearray = new ArrayList<>();
        phonearray = new ArrayList<>();
        if(!doesDatabaseExist(MainActivity.this,"contactdatabase")) new AsyncContact().execute();
        else{
            Message.message(MainActivity.this,"Database exist");
            helper = new DBHelper(MainActivity.this);
            db = helper.getReadableDatabase();
            namearray = helper.getAppContactDetail();
            phonearray = helper.getAppPhoneDetail();
            list = new ArrayList<>();
            for(int i=0;i<namearray.size();i++){
                //list.add(new Data("Pic 1","yes!! 1",R.drawable.alessi,0));
                list.add(new Data(namearray.get(i),phonearray.get(i),R.mipmap.ic_launcher));
            }

            recyclerView =  (RecyclerView) findViewById(R.id.recyclerViewMain);


            layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new RecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);



            /*
            //Create Array Adapter and Pass ArrayOfValues to it.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, namearray);
            //BindAdpater with our Actual ListView
            lv1.setAdapter(adapter);
            //Do something on click on ListView Click on Items
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //============================================
                    // Display number of contact on click.
                    //===========================================
                    //String msg = phonearray[arg2];
                    String msg = phonearray.get(arg2);
                    Toast.makeText(getApplicationContext(), msg + " p:" + phonearray.size() + " :n: " +namearray.size(), Toast.LENGTH_LONG).show();
                }
            });
            */
        }
        swipeView =  ((SwipeRefreshLayout)findViewById(R.id.swipe));
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // (new AsyncContact().execute()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                new AsyncContactSyncHttpReqeust().execute();//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredDataList = filter(list, newText);
        adapter.setFilter(filteredDataList);
        return true;
        // return false






        //return false;
    }
    private List<Data> filter(List<Data> dataList, String newText) {
        newText=newText.toLowerCase();
        String text;
        filteredDataList=new ArrayList<>();
        for(Data dataFromDataList:dataList){
            text=dataFromDataList.title.toLowerCase();

            if(text.contains(newText)){
                filteredDataList.add(dataFromDataList);
            }
        }

        return filteredDataList;
    }

    private class AsyncContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setMessage("Loading...");
            progressBar.show();
            helper = new DBHelper(MainActivity.this);
            db = helper.getWritableDatabase();
            //====================================================================
            //ListView to Display Contact Names and on click Phone Number in Toast.
            //====================================================================


           // lv1 = (ListView) findViewById(R.id.listView1);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (phones.moveToNext()) {
                //Read Contact Name
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if(name == null){
                    name = "-";
                }
                //Read Phone Number
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(phoneNumber == null){
                    phoneNumber = "-";
                }
                db = helper.getWritableDatabase();
                long id = helper.insert(name, phoneNumber, db);
                if (id < 0) {
                } else {
                    // Message.message(MainActivity.this,"inserted --> " + name);
                }
                if (name != null) {
                    namecsv += name + ",";
                    phonecsv += phoneNumber + ",";

                    namearray.add(name);
                    phonearray.add(phoneNumber);
                }

            }
            phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            list = new ArrayList<>();
            for(int i=0;i<namearray.size();i++){
                //list.add(new Data("Pic 1","yes!! 1",R.drawable.alessi,0));
                list.add(new Data(namearray.get(i),phonearray.get(i),R.mipmap.ic_launcher));
            }

            recyclerView =  (RecyclerView) findViewById(R.id.recyclerViewMain);


            layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new RecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);


            /*

            //Create Array Adapter and Pass ArrayOfValues to it.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, namearray);
            //BindAdpater with our Actual ListView
            lv1.setAdapter(adapter);

            //Do something on click on ListView Click on Items
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //============================================
                    // Display number of contact on click.
                    //===========================================
                    //String msg = phonearray[arg2];
                    String msg = phonearray.get(arg2);
                    Toast.makeText(getApplicationContext(), msg +"n:" + namearray.size() + " p:"+phonearray.size(), Toast.LENGTH_LONG).show();
                }
            });
            */


            progressBar.dismiss();
            // new AsyncContactSyncHttpReqeust().execute();
        }
    }


    class AsyncContactSyncHttpReqeust extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Message.message(MainActivity.this,"Sync Started");
            helper = new DBHelper(MainActivity.this);
            db = helper.getWritableDatabase();
            helper2 = new DBHelper(MainActivity.this);
            db2 = helper2.getWritableDatabase();
            namearray.clear();
            phonearray.clear();




        }

        @Override
        protected Void doInBackground(Void... params) {

            //1. again collect all the contacts from phone storage & insert them in the array
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (phones.moveToNext()) {
                //Read Contact Name
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //Read Phone Number
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                helper.getWritableDatabase();
                if(helper.selectOneItem(name,phoneNumber).getCount() !=0) {
                    // helper.update(name, phoneNumber, db);
                }
                else{
                    helper2.getWritableDatabase();

                    helper2.insert(name,phoneNumber,db2);
                    helper2.close();
                    Log.e("inserted","inserted from phone book to local app");
                }
                /*
                if (id < 0) {
                } else {
                    // Message.message(MainActivity.this,"inserted --> " + name);
                }
                */
                if (name != null) {
                    //  namecsv += name + ",";
                    // phonecsv += phoneNumber + ",";
                    namearray.add(name);
                    phonearray.add(phoneNumber);
                }

            }
            phones.close();


            ArrayList<String> db_name = helper.getAppContactDetail();
            ArrayList<String> db_phone =  helper.getAppPhoneDetail();
            for(int i=0;i<db_name.size();i++){
                if(!namearray.contains(db_name.get(i))){

                    helper.deleteTitle(db_name.get(i),db);
                    //also delete from the server.....................

                    try {
                        URL u = new URL(url_delete);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        //httpURLConnection.setDoInput(true);
                        OutputStream OS = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                        String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(db_name.get(i), "UTF-8") + "&" +
                                URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(db_phone.get(i), "UTF-8");// + "&" +
                        //URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");
                        bufferedWriter.write(data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        OS.close();
                        InputStream IS = httpURLConnection.getInputStream();
                        IS.close();
                        //httpURLConnection.connect();
                        httpURLConnection.disconnect();
                    } catch (IOException e) {
                        //TODO Handle problems..
                    }

                    ///








                }
            }


            for (int i = 0; i < phonearray.size(); i++) {
                //final String name = namearray[i];
                final String name = namearray.get(i);
                final String phone = phonearray.get(i);
                try {
                    URL u = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    //httpURLConnection.setDoInput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");// + "&" +
                    //URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    IS.close();
                    //httpURLConnection.connect();
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    //TODO Handle problems..
                }

            }
            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeView.setRefreshing(false);
            Message.message(MainActivity.this, "Sync Completed");
            //show updated list of list view... Create Array Adapter and Pass ArrayOfValues to it.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, namearray);
            //BindAdpater with our Actual ListView
            lv1.setAdapter(adapter);

            //Do something on click on ListView Click on Items
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //============================================
                    // Display number of contact on click.
                    //===========================================
                    //String msg = phonearray[arg2];
                    String msg = phonearray.get(arg2);
                    Toast.makeText(getApplicationContext(), msg +"n:" + namearray.size() + " p:"+phonearray.size(), Toast.LENGTH_LONG).show();
                }
            });


        }
    }
}



//http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
//http://easyway2in.blogspot.ae/2015/07/android-mysql-database-connect.html
//swipe layout
//https://sites.google.com/site/forhadmethun/android/swiperefreshlayout
//http://stackoverflow.com/questions/13311727/android-sqlite-insert-or-update