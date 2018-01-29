package bd.com.elites.ab.contactsyncapp.http;

import android.content.ContentValues;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class HttpRequests {

    public HttpRequests() {

    }

    public static HttpResponse sendHttpGetRequest(String urlString, ContentValues params) {
        return sendHttpGetRequest(urlString, params, null);
    }

    public static HttpResponse sendHttpGetRequest(String urlString, ContentValues params, ContentValues headers) {

        HttpResponse response = null;
        if (params != null && params.size() > 0) {
            ArrayList<String> nameValueList = new ArrayList<>();
            for (String key : params.keySet()) {
                nameValueList.add(key + "=" + params.get(key));
            }

            if (urlString.endsWith("?")) {
                urlString += TextUtils.join("&", nameValueList);
            } else {
                urlString += "?" + TextUtils.join("&", nameValueList);
            }
        }

        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(METHODS.GET);

            // add request headers
            // connection.setRequestProperty("PropertyName","Value");
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.getAsString(key));
                }
            }
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            // start the connection
            connection.connect();

            // fetch HttpResponseCode
            int responseCode = connection.getResponseCode();

            // fetch HttResponseBody
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer resposeBody = new StringBuffer();
            String inputLine = null;
            while ((inputLine = inputReader.readLine()) != null) {
                resposeBody.append(inputLine);
            }
            inputReader.close();

            // set the response
            response = new HttpResponse();
            response.setResponseCode(responseCode);
            response.setResponseBody(resposeBody.toString());

            // close the connection
            if (connection != null) {
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static HttpResponse sendHttpPostRequest(String urlString, ContentValues params) {
        return sendHttpPostRequest(urlString, params, null);
    }

    public static HttpResponse sendHttpPostRequest(String urlString, ContentValues params, ContentValues headers) {

        HttpResponse response = null;
        String urlParams = "";

        if (params != null && params.size() > 0) {
            ArrayList<String> nameValueList = new ArrayList<>();
            for (String key : params.keySet()) {
                nameValueList.add(key + "=" + params.get(key));
            }

            urlParams += TextUtils.join("&", nameValueList);
        }

        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(METHODS.POST);

            // add request headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.getAsString(key));
                }
            }
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            // start the connection and write post-data
            connection.setDoOutput(true);
            connection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(urlParams);
            dataOutputStream.flush();
            dataOutputStream.close();

            // fetch HttpResponseCode
            int responseCode = connection.getResponseCode();

            // fetch HttResponseBody
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer resposeBody = new StringBuffer();
            String inputLine = null;
            while ((inputLine = inputReader.readLine()) != null) {
                resposeBody.append(inputLine);
            }
            inputReader.close();

            // set the response
            response = new HttpResponse();
            response.setResponseCode(responseCode);
            response.setResponseBody(resposeBody.toString());

//            If we need to access the HTTP HEADERS
//            Map<String, List<String>> hdrs = connection.getHeaderFields();
//            Set<String> hdrKeys = hdrs.keySet();
//            for (String k : hdrKeys)
//            System.out.println("Key: " + k + "  Value: " + hdrs.get(k));

            // close the connection
            if (connection != null) {
                connection.disconnect();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private class METHODS {
        static final String GET = "GET";
        static final String POST = "POST";

    }

    public class STANDARD_HEADERS {
        public static final String Authorization = "Authorization";
        public static final String From = "From";

    }

}
