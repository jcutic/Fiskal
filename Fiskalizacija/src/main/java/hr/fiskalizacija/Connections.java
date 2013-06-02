package hr.fiskalizacija;

import hr.model.bussinessArea.CertParameters;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Koristi se za komunikaciju sa web servisom porezne uprave 
 */
public class Connections{

	 private static final Logger LOG = LoggerFactory.getLogger(Connections.class);
	
	 private static final String TRUSTED_MANAGER_TYPE_PKIX = "PKIX";
	 private String urlFiskal = "https://cistest.apis-it.hr:8449/FiskalizacijaServiceTest";
	 private int connTimeout = 1000;
	 private int readTimeout = 15000;	
	 
	 /**
	  * @param urlFiskal URL web servisa na koji se vrši spajanje, defaultno je URL demo servisa porezne uprave. Za potvrdu defaultnih vrijednosti potrebno je unesti null ili ""
	  * @param connTimeout vrijeme koje je potrebno da se uspostavi konekcija u milisekundama, defaultno je 1000. Za prihvaæanje defaultnih vrijednosti potrebno je unesti 0
	  * @param readTimeout vrijeme koje se èeka response web servisa porezne uprave u milisekundama, defaultno je 15000. Za prihvaæanje defaultnih vrijednosti potrebno je unesti 0
	  */
	 public Connections(String url, int connTimeout, int readTimeout){
		 if(!(url == null || url == "")){
			 this.urlFiskal = url; 
		 }
		 if(connTimeout > 0){
			 this.connTimeout = connTimeout;
		 }
		 if(readTimeout > 0){
			 this.readTimeout = readTimeout;
		 }
	 }
	 
    /* override metode openConnection, kako bi se postavio timeout */
    URLStreamHandler urlStrHandler = new URLStreamHandler(){
		@Override
		protected URLConnection openConnection(URL url) throws IOException {
			URLConnection urlConnection = new URL(url.toString()).openConnection();
              
              /* Timeout konekcija */
              urlConnection.setConnectTimeout(connTimeout); 
              urlConnection.setReadTimeout(readTimeout); 
              return urlConnection;
		}   	
    };
 
    
    /**
     *  Slanje SOAP poruku prema serveru Porezne uprave
     * @param message potpisana SOAP poruka koja se šalje servisu
     * @param certParameters objekt u kojem su definirane postavke keystorea za dohvat SSL certifikata 
     * @return SOAP poruku odgovora web servisa
     */
    public SOAPMessage sendSoapMessage(SOAPMessage message, CertParameters certParameters){
       
        SOAPMessage soapResponse = null;
		try {
			
			/* Dohvat SSL certifikata iz keystorea */
			KeyStore keyStore = KeyStore.getInstance(CertParameters.KEYSTORE_TYPE_JKS);
			keyStore.load(new FileInputStream(certParameters.getPathOfJKSCert() + certParameters.getNameOfJKSCert() + 
					CertParameters.EXTENSION_OF_JKS), certParameters.getPasswdOfJKSCert().toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TRUSTED_MANAGER_TYPE_PKIX);
			tmf.init(keyStore);
			SSLContext sslctx = SSLContext.getInstance(CertParameters.CERTIFICATE_TYPE_SSL);
			sslctx.init(null, tmf.getTrustManagers(), null);

			/* Postavljanje SSL certifikata u factory te slanje SOAP poruke */
			HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
			soapResponse = SOAPConnectionFactory.newInstance().createConnection()
					.call(message, new URL(new URL(urlFiskal), "", urlStrHandler));
		}catch(Exception e){
			LOG.error("Exception", e);	
		}		
        return soapResponse;
    }
}
