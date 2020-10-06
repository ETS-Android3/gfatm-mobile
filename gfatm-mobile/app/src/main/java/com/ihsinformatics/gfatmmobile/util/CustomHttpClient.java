package com.ihsinformatics.gfatmmobile.util;

import android.content.Context;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class CustomHttpClient extends DefaultHttpClient {

    private static final String TAG = "HttpsClient";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    public static final String DEV_URL = "ihsinformatics";
    public static final String HTTPS = "https";
    private Context context;


    public CustomHttpClient(Context context) {
        super();
        this.context = context;

    }


    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        ClientConnectionManager clientConnectionManager = super.createClientConnectionManager();
        SchemeRegistry registry = new SchemeRegistry();

        if (App.getIp().contains(DEV_URL)) {
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier(new CustomHostnameVerifier());
            Scheme scheme = (new Scheme(HTTPS, socketFactory, HTTPS_PORT));
            //getConnectionManager().getSchemeRegistry().register(scheme);
            registry.register(scheme);
            clientConnectionManager = new SingleClientConnManager(getParams(), registry);

        } else {
            try {
                KeyStore trusted = KeyStore.getInstance("BKS");


                InputStream in = context.getResources().openRawResource(R.raw.ihskeystore);

                trusted.load(in, context.getResources().getString(R.string.trust_store_password).toCharArray());

                SSLSocketFactory socketFactory = new SSLSocketFactory(trusted);

                socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
                registry.register(new Scheme(HTTPS, socketFactory, HTTPS_PORT));
                clientConnectionManager = new SingleClientConnManager(getParams(), registry);
            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException | UnrecoverableKeyException | IOException e) {
                throw new AssertionError(e);
            }
        }
        return clientConnectionManager;
    }
}
