package com.example.bookly.API;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bookly.Model.PredictResultModel;
import com.example.bookly.Model.SentimentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SentimentAnalysis {

    // link to the sentiment analysis api huggingface.co/distilbert-base-uncased-finetuned-sst-2-english
    static private String URL = "https://api-inference.huggingface.co/models/vietnhatthai/distilbert-base-uncased-finetuned-sst-2-english";

    public SentimentAnalysis () {
        // TODO: run analysis text on background and using Listener
    }

    public SentimentModel predict(String data){
        String response = "[[{\"label\":\"NEGATIVE\",\"score\":0.0},{\"label\":\"POSITIVE\",\"score\":1.0}]]";

        if (data.length() == 0) {
            return new SentimentModel();
        }

        try{
            response = new CallAPI().execute(URL, data).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<PredictResultModel> results = new ArrayList<>();
            try{
                JSONArray jsonArray = new JSONArray(response);
                jsonArray = (JSONArray) jsonArray.get(0);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String label = jsonObject.getString("label");
                    double score = jsonObject.getDouble("score");
                    results.add(new PredictResultModel(label, score));
            }
        } catch (Exception e) {
            e.printStackTrace();
            results.add(new PredictResultModel("NEGATIVE", 0.0));
            results.add(new PredictResultModel("POSITIVE", 1.0));
        }

        return new SentimentModel(results);
    }


    private static class CallAPI extends AsyncTask<String, Void, String> {

        public CallAPI() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];   // URL to call
            Log.d("response urlString", urlString);
            String data = params[1];        //data to post
            Log.d("response data", data);
            StringBuilder response = new StringBuilder();

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "Bearer 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");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONArray array = new JSONArray();
                array.put(data);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("inputs", array);
                DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());
                os.flush();
                os.close();
                Log.i("response code", String.valueOf(urlConnection.getResponseCode()));
                Log.i("response status" , urlConnection.getResponseMessage());
                if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response.append(line);
                    }
                }
                else {
                    response = new StringBuilder();
                }
                urlConnection.disconnect();
                return response.toString();
            } catch (Exception e) {
                Log.e("response", e.getMessage());
            }
            return "[[{\"label\":\"NEGATIVE\",\"score\":0.0},{\"label\":\"POSITIVE\",\"score\":1.0}]]";
        }
    }
}
