package com.example.voicetranslator.helper;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class TranslateTask extends AsyncTask<String, Void, String> {

    private String BASE_URL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=";
    private String fromLang;
    public AsyncResponse json_response = null;
    Context mContext;
    private String text;
    private String toLang;

    public interface AsyncResponse {
        void processFinish(String str);
    }

    public TranslateTask(Context mContext, String text, String fromLang, String toLang) {
        this.mContext = mContext;
        this.text = text;
        this.fromLang = fromLang;
        this.toLang = toLang;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        try {
            String url = this.BASE_URL + this.fromLang + "&tl=" + this.toLang + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + URLEncoder.encode(this.text, "UTF-8");
            Helper.showLog(url);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(url));
                if (execute.getStatusLine().getStatusCode() == 200) {
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        stringBuilder.append(readLine);
                    }
                    content.close();
                    return stringBuilder.toString();
                }
                Helper.showLog("JSON", "Failed to download response");
                return stringBuilder.toString();
            } catch (Exception e) {
                stringBuilder.append("[\"ERROR\"]");
            }
        } catch (Exception e2) {
            Helper.showLog("EEE", e2.toString());
            return e2.toString();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        String response;
        if (result != null) {
            response = getResponse(result);
        } else {
            response = result;
        }
        this.json_response.processFinish(response);
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private String getResponse(String json) {
        String str2 = "";
        try {
            if (json.equals("[\"ERROR\"]")) {
                return null;
            }
            JSONArray jSONArray = new JSONArray(json);
            for (int i = 0; i < jSONArray.getJSONArray(0).length(); i++) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(jSONArray.getJSONArray(0).getJSONArray(i).getString(0));
                str2 = stringBuilder2.toString();
            }
            return str2;
        } catch (Exception e) {
        }

        return null;
    }
}