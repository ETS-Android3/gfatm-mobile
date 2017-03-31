package com.ihsinformatics.gfatmmobile.util;

/**
 * Created by Haris on 3/22/2017.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;


import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.ihsinformatics.gfatmmobile.App;


/**
 * @author owais.hussain@irdresearch.org
 *
 */
public class HttpsClient extends DefaultHttpClient {
    private static final String TAG = "HttpsClient";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private final Context context;

    public HttpsClient(Context context) {
        this.context = context;
    }

    /**
     * Makes HTTPS GET call to server and returns the response. The method
     * automatically appends authentication header using App.getUsername() and
     * App.getPassword() methods
     *
     * @param requestUri
     * @return
     */
    public String request(String requestUri) {
        return request(new HttpGet(requestUri));
    }

    /**
     * Overloaded method
     *
     * @param request
     * @return
     */
    public String request(HttpUriRequest request) {
        String responseString = null;
        try {
            HttpsClient client = new HttpsClient(context);
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            Log.d(TAG, "Http response code: " + statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                // Log.d (TAG, responseString);
            } else {
                response.getEntity().getContent().close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    /**
     * Makes HTTPS GET call to client via REST call and returns the response.
     * The method automatically appends authentication header using
     * App.getUsername() and App.getPassword() methods.
     *
     * @param requestUri
     *            fully qualified URI, e.g.
     *            https://myserver:port/ws/rest/v1/concept
     * @return
     */
    public String clientGet(String requestUri) {
        HttpsClient client = new HttpsClient(context);
        HttpUriRequest request = null;
        String response = "";
        String auth = "";
        try {
            request = new HttpGet(requestUri);
            auth = Base64.encodeToString(("admin" + ":" + "Admin1234").getBytes("UTF-8"), Base64.NO_WRAP);
            request.addHeader("Authorization", auth);
            response = client.request(request);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }

    /**
     * Makes a POST call to the server and returns the attached Entity in a
     * String
     *
     * @param postUri
     * @param content
     * @return
     */
    public String clientPost(String postUri, String content) {
        HttpsClient client = new HttpsClient(context);
        HttpUriRequest request = null;
        HttpResponse response = null;
        HttpEntity entity;
        StringBuilder builder = new StringBuilder();
        String auth = "";
        try {
			/*
			 * Uncomment if you do not want to send data in Parameters HttpPost
			 * httpPost = new HttpPost (postUri); httpPost.setHeader ("Accept",
			 * "application/json"); httpPost.setHeader ("Content-Type",
			 * "application/json"); StringEntity stringEntity = new StringEntity
			 * (content); httpPost.setEntity (stringEntity); request = httpPost;
			 */
            auth = Base64.encodeToString(("admin" + ":" + "Admin1234").getBytes("UTF-8"), Base64.NO_WRAP);
            request = new HttpGet(postUri);
            request.addHeader("Authorization", auth);
            response = client.execute(request);
            entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(is));
            builder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
                builder.append(line);
            entity.consumeContent();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            builder.append("UNSUPPORTED_ENCODING");
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            builder.append("SERVER_NOT_RESPONDING");
        }
        return builder.toString();
    }
}