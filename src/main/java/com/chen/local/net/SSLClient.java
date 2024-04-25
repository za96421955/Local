package com.chen.local.net;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 用于进行Https请求的HttpClient
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2024/4/12
 */
public class SSLClient extends DefaultHttpClient {

    public SSLClient() throws Exception{
        super();
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, getTrustingManager(), null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }

    private static TrustManager[] getTrustingManager() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        return trustAllCerts;
    }
}
