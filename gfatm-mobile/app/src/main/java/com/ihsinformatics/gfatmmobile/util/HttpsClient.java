/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
/**
 * This class handles all HTTPS (secure) calls
 */

package com.ihsinformatics.gfatmmobile.util;


import android.content.Context;

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

/**
 * @author owais.hussain@irdresearch.org
 */

@Deprecated
public class HttpsClient extends DefaultHttpClient {
    private static final String TAG = "HttpsClient";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private final Context context;

    public HttpsClient(Context context) {
        this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        // Get an instance of the Bouncy Castle KeyStore format
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, containing keystore with your trusted
            // certificates (root & intermediate certs)
            /*
			 * This keystore was created using a utility called Keystore
			 * Explorer
			 */
            InputStream in = context.getResources().openRawResource(R.raw.ihskeystore);
            // Initialize the keystore with the provided trusted certificates
            // Also provide the password of the keystore
            trusted.load(in, context.getResources().getString(R.string.trust_store_password).toCharArray());
            // Pass the keystore to the SSLSocketFactory, which is responsible
            // for the server certificate verification
            SSLSocketFactory socketFactory = new SSLSocketFactory(trusted);
            // Hostname verification from certificate. Use
            // SSLSocketFactory.STRICT_HOSTNAME_VERIFIER for production
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
            // Register for port 443 our SSLSocketFactory with our keystore to
            // the ConnectionManager
            registry.register(new Scheme("https", socketFactory, HTTPS_PORT));
            return new SingleClientConnManager(getParams(), registry);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (CertificateException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (UnrecoverableKeyException e) {
            throw new AssertionError(e);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
