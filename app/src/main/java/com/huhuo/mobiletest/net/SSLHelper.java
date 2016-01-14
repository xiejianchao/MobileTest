package com.huhuo.mobiletest.net;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.MySSLSocketFactory;

import org.xutils.http.RequestParams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.client.ClientProtocolException;

/**
 * Created by xiejc on 16/1/12.
 */
public class SSLHelper {

    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型 固定值
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型 固定值

    private static final String KEY_STORE_CLIENT_PATH = "client.p12";//客户端要给服务器端认证的证书
    private static final String KEY_STORE_TRUST_PATH = "cacert.bks";//客户端验证服务器端的证书库

    private static final String KEY_STORE_PASSWORD = "9fE<:l#Szb8_j971";// 客户端证书密码
    private static final String KEY_STORE_TRUST_PASSWORD = "pw12306";//客户端证书库密码

//    private static final String KEY_STORE_PASSWORD = "pw12306";// 客户端证书密码
//    private static final String KEY_STORE_TRUST_PASSWORD = "9fE<:l#Szb8_j971";//客户端证书库密码

    private static SSLSocketFactory sslSocketFactory;

    /**
     * 获取SSLContext
     *
     * @param context 上下文
     * @return SSLContext
     */
    public static SSLContext getSSLContext(Context context) {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

            InputStream ksIn = context.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
            InputStream tsIn = context.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
            try {
                keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            Log.e("tag", e.getMessage(), e);
        }
        return null;
    }
    /**
     * 获取SSLContext
     *
     * @param context 上下文
     * @return SSLContext
     */
    public static SSLContext getSSLContext2(Context context) {
        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            try {
                final MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            Log.e("tag", e.getMessage(), e);
        }
        return null;
    }

    private static class MySSLSocketFactory extends cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory {


        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
        }
    }

    private static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
            // TODO Auto-generated method stub
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public static synchronized final SSLSocketFactory getSSLSocketFactory(Context context){
        if (sslSocketFactory == null) {
            sslSocketFactory = getSSLContext(context).getSocketFactory();
        }
        return sslSocketFactory;
    }

    /**
     * 获取SslSocketFactory
     *
     * @param context 上下文
     * @param port
     * @return SSLSocketFactory
     */
    public static cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory getSslSocketFactory2(Context context, int port) {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

            InputStream ksIn = context.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
            InputStream tsIn = context.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
            try {
                keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {

                }
            }
//            return new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);
            return new cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);
        } catch (KeyManagementException | UnrecoverableKeyException | KeyStoreException | FileNotFoundException | NoSuchAlgorithmException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
