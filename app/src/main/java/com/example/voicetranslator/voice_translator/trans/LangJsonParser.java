package com.example.voicetranslator.voice_translator.trans;

import android.text.Html;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

public class LangJsonParser {
    private static String m3910a(String str) {
        JSONArray jSONArray;
        try {
            jSONArray = (JSONArray) new JSONArray(str).get(0);
        } catch (JSONException e) {
            e.printStackTrace();
            jSONArray = null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                sb.append(((JSONArray) jSONArray.get(i)).get(0).toString());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        if (sb.length() > 100) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String m3911a(String str, String str2, String str3) {
        String str4;
        String str5 = str2;
        String str6 = str3;
        String str7 = "trans";
        String str8 = "UTF-8";
        String str9 = "terms";
        String str10 = "sentences";
        String str11 = "dict";
        if (str.length() > 100) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(".");
            str4 = sb.toString();
        } else {
            str4 = str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Character.toLowerCase(str4.charAt(0)));
        sb2.append(str4.substring(1));
        String sb3 = sb2.toString();
        try {
            sb3 = URLEncoder.encode(sb3, str8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String format = String.format(m3912a(m3914b("aHR0cHM6Ly90cmFuc2xhdGUuZ29vZ2xlLmNvbS90cmFuc2xhdGVfYS90P2NsaWVudD1hdCZzYz0xJnY9Mi4wJnNsPSVzJnRsPSVzJmhsPW5sJmllPVVURi04Jm9lPVVURi04JnRleHQ9JXM=")), new Object[]{str5, str6, sb3});
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpParams params = defaultHttpClient.getParams();
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.valueOf(10000));
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.valueOf(10000));
            params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, str8);
            HttpProtocolParams.setUserAgent(params, "AndroidTranslate/2.5.3 2.5.3 (gzip)");
            String readLine = new BufferedReader(new InputStreamReader(defaultHttpClient.execute(new HttpPost(format)).getEntity().getContent(), "utf-8"), 8).readLine();
            if (readLine == null || readLine.length() <= 0) {
                return m3913b(str4, str5, str6);
            }
            JSONObject jSONObject = new JSONObject(readLine);
            StringBuilder sb4 = new StringBuilder();
            if (jSONObject.getJSONArray(str10) != null) {
                for (int i = 0; i < jSONObject.getJSONArray(str10).length(); i++) {
                    if (jSONObject.getJSONArray(str10).getJSONObject(i).has(str7)) {
                        sb4.append(jSONObject.getJSONArray(str10).getJSONObject(i).getString(str7));
                    }
                }
                if (jSONObject.has(str11)) {
                    sb4.append("\n\n###dict");
                    for (int i2 = 0; i2 < jSONObject.getJSONArray(str11).length(); i2++) {
                        if (jSONObject.getJSONArray(str11).getJSONObject(i2).getJSONArray(str9) != null) {
                            for (int i3 = 0; i3 < jSONObject.getJSONArray(str11).getJSONObject(i2).getJSONArray(str9).length(); i3++) {
                                String string = jSONObject.getJSONArray(str11).getJSONObject(i2).getJSONArray(str9).getString(i3);
                                if (!sb4.toString().toLowerCase(Locale.getDefault()).contains(string.toLowerCase(Locale.getDefault()))) {
                                    sb4.append(string);
                                    sb4.append("\n");
                                }
                            }
                        }
                    }
                }
            }
            if (sb4.length() <= 0) {
                return m3913b(str4, str5, str6);
            }
            if (sb4.length() > 100) {
                sb4.setLength(sb4.length() - 1);
            }
            return sb4.toString();
        } catch (Exception unused) {
            return m3913b(str4, str5, str6);
        }
    }

    private static String m3912a(byte[] bArr) {
        return new String(Base64.decodeBase64(bArr));
    }

    private static String m3913b(String str, String str2, String str3) {
        String str4 = "UTF-8";
        try {
            URLConnection openConnection = new URL(String.format(m3912a(m3914b("aHR0cHM6Ly90cmFuc2xhdGUuZ29vZ2xlYXBpcy5jb20vdHJhbnNsYXRlX2Evc2luZ2xlP2NsaWVudD1ndHgmc2w9JXMmdGw9JXMmZHQ9dCZxPSVz")), new Object[]{str, str2, URLEncoder.encode(str3, str4)})).openConnection();
            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), str4));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    bufferedReader.close();
                    return m3910a(sb.toString());
                }
            }
        } catch (Exception unused) {
            return m3915c(str3, str, str2);
        }
    }

    private static byte[] m3914b(String str) {
        return str.getBytes();
    }

    private static String m3915c(String str, String str2, String str3) {
        String str4 = "\"translatedText\":\"";
        String str5 = "UTF-8";
        String str6 = "";
        try {
            if (str.split(" ").length == 1) {
                str = str.toLowerCase(Locale.getDefault());
            }
            URLConnection openConnection = new URL(String.format(m3912a(m3914b("aHR0cDovL215bWVtb3J5LnRyYW5zbGF0ZWQubmV0L2FwaS9nZXQ/cT0lcyZsYW5ncGFpcj0lc3wlcw==")), new Object[]{URLEncoder.encode(str, str5), str2, str3})).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), str5));
            StringBuilder sb = new StringBuilder(100000);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
            bufferedReader.close();
            if (sb.length() != 0 && sb.toString().contains(str4)) {
                str6 = StringEscapeUtils.unescapeXml(Html.fromHtml(sb.substring(sb.indexOf(str4) + 18, sb.indexOf("\",\""))).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str6.length() > 100 ? str6.substring(0, str6.length() - 1) : str6;
    }

    public static byte[] m3442a(String str, String str2) {
        byte[] bArr = new byte[0];
        try {
            String format = String.format(m3440a(m3441a("aHR0cHM6Ly90cmFuc2xhdGUuZ29vZ2xlLmNvbS90cmFuc2xhdGVfdHRzP3E9JXMmdGw9JXMmdG90YWw9MSZpZHg9MCZ0ZXh0bGVuPTEzJnRrPTI4MzgwMCZjbGllbnQ9dHctb2I=")), new Object[]{URLEncoder.encode(str2, "utf-8"), str});
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(format);
            httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.0; .NET CLR 1.1.4322; .NET CLR 2.0.50215;)");
            HttpResponse execute = defaultHttpClient.execute(httpGet);
            return execute.getStatusLine().getStatusCode() == 200 ? EntityUtils.toByteArray(execute.getEntity()) : bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return bArr;
        }
    }

    private static byte[] m3441a(String str) {
        return str.getBytes();
    }

    private static String m3440a(byte[] bArr) {
        return new String(Base64.decodeBase64(bArr));
    }
}