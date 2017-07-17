package com.example.roshan.ncab;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by roshan on 6/28/17.
 */

public class Background extends AsyncTask<String, Void, String> {
    Context context;
    Activity activity;

    public Background(Context ctxt) {
        this.context = ctxt;
        activity = (Activity) ctxt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        // String insert = "http://appybite.com/index.php?route=api/food/register&firstname="+firstname+"&lastname="+lastname+"&email="+email+"&telephone="+phone+"&address="+address+"&city="+city+"&password="+password+"&confirm="+confirm_pass+"&token=F46AxXU0XAhBZWbN7gIbEB81ZZHfJW2Q";
        String insert="http://10.42.0.1/Push_Notification/fcm_notification.php";
        if (type.equals("insert")) {
            try {
                String fcm_token = params[1];
                String email = params[2];
                URL url = new URL(insert);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("fcm_token", "UTF-8") + "=" + URLEncoder.encode(fcm_token, "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                Log.d("Background", "Post Data = " + post_data);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream stream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                String result = "Success ";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                stream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        Toast.makeText(context,result, Toast.LENGTH_LONG).show();
    }

}
