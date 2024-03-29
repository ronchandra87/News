package com.server.connector;

	import java.security.KeyStore;
	import java.security.cert.CertificateException;
	import java.security.cert.X509Certificate;

	import javax.net.ssl.SSLContext;
	import javax.net.ssl.TrustManager;
	import javax.net.ssl.X509TrustManager;
	 
	import org.apache.http.client.HttpClient;
	import org.apache.http.conn.ClientConnectionManager;
	import org.apache.http.conn.scheme.PlainSocketFactory;
	import org.apache.http.conn.scheme.Scheme;
	import org.apache.http.conn.scheme.SchemeRegistry;
	import org.apache.http.conn.ssl.SSLSocketFactory;
	import org.apache.http.impl.client.DefaultHttpClient;
	import org.apache.http.impl.conn.SingleClientConnManager;

	import android.content.Context;
	 
	public class NewsHTTPSClient extends DefaultHttpClient
	{
	 
		//final Context context;
	 
		public NewsHTTPSClient() 
		{
			//this.context = context;
		}
	 
		@Override
		protected ClientConnectionManager createClientConnectionManager()
		{
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			// Register for port 443 our SSLSocketFactory with our keystore
			// to the ConnectionManager
			registry.register(new Scheme("https", newSslSocketFactory(), 443));
			return new SingleClientConnManager(getParams(), registry);
		}
	 
		private SSLSocketFactory newSslSocketFactory()
		{
			try
			{
				// Get an instance of the Bouncy Castle KeyStore format
				KeyStore trusted = KeyStore.getInstance("BKS");
				// Get the raw resource, which contains the keystore with
				// your trusted certificates (root and any intermediate certs)
				// Pass the keystore to the SSLSocketFactory. The factory is responsible
				// for the verification of the server certificate.
				SSLSocketFactory sf = new SSLSocketFactory(trusted);
				// Hostname verification from certificate
				// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
				sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
				return sf;
			} catch (Exception e)
			{
				throw new AssertionError(e);
			}
		}
		
		
		
		public HttpClient sslClient(HttpClient client) {
		    try {
		        X509TrustManager tm = new X509TrustManager() {
		            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		            }
	 
		            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		            }
	 
		            public X509Certificate[] getAcceptedIssuers() {
		                return null;
		            }
		        };
		        SSLContext ctx = SSLContext.getInstance("TLS");
		        ctx.init(null, new TrustManager[]{tm}, null);
		        SSLSocketFactory ssf = new NewsSSLSocketFactory(ctx);
		        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		        ClientConnectionManager ccm = client.getConnectionManager();
		        SchemeRegistry sr = ccm.getSchemeRegistry();
		        sr.register(new Scheme("https", ssf, 443));
		        return new DefaultHttpClient(ccm, client.getParams());
		    } catch (Exception ex) {
		        return null;
		    }
		}
		
		
}
