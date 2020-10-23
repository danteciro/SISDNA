package pe.gob.mimp.webservicemimp.auxiliar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 * Clase: AntecedenteJudicial.java <br>
 * Clase que consume el WS de Antecedentes Judiciales.<br>
 * 
 */
public class AntecedenteJudicial implements Serializable {

    private static final Logger logger = Logger.getLogger(AntecedenteJudicial.class.getName());
    private static final long serialVersionUID = 1L;

    private String CODRESPUESTA;
    private String RESPUESTA;

    public AntecedenteJudicial() {

    }

    public String getCODRESPUESTA() {
        return CODRESPUESTA;
    }

    public void setCODRESPUESTA(String CODRESPUESTA) {
        this.CODRESPUESTA = CODRESPUESTA;
    }

    public String getRESPUESTA() {
        return RESPUESTA;
    }

    public void setRESPUESTA(String RESPUESTA) {
        this.RESPUESTA = RESPUESTA;
    }

    public void obtenerAntJudicial(String apepat, String apemat, String nombres) throws MalformedURLException {

        StringBuilder urlProxy = new StringBuilder();
       urlProxy.append("http://192.168.4.50:8080/wsService/faces/ConsultaAntJudicial?apePaterno=").append(apepat.replace(" ", "%20"))
       // urlProxy.append("https://appweb.mimp.gob.pe:8181/wsService/faces/ConsultaAntJudicial?apePaterno=").append(apepat.replace(" ", "%20"))
                .append("&apeMaterno=").append(apemat.replace(" ", "%20"))
                .append("&nombre=").append(nombres.replace(" ", "%20"));

        System.out.println("urlProxy:" + urlProxy);
        try {
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                    new javax.net.ssl.HostnameVerifier() {
                        public boolean verify(String hostname,
                                javax.net.ssl.SSLSession sslSession) {
                            if (hostname.equals("192.168.4.42")) {
                                return true;
                            }
                            return false;
                        }
                    });

            URL url = new URL(new String(urlProxy));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/html");
            InputStream instream = conn.getInputStream();                     
            BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            System.out.println("calling ws...");
            StringBuilder sb = new StringBuilder();
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
            } finally {
                instream.close();
            }
            JSONObject jsonObjReturnFB = new JSONObject(sb.toString());
            System.out.println("jsonObjReturnFB-xCodigoRespuesta:" + jsonObjReturnFB.get("xCodigoRespuesta"));
            System.out.println("jsonObjReturnFB-xMensajeRespuesta:" + jsonObjReturnFB.get("xMensajeRespuesta"));

            if (jsonObjReturnFB.get("xCodigoRespuesta").equals("0000") || jsonObjReturnFB.get("xCodigoRespuesta").equals("0001")) {
                this.CODRESPUESTA = jsonObjReturnFB.get("xCodigoRespuesta").toString();
                this.RESPUESTA = jsonObjReturnFB.get("xMensajeRespuesta").toString();
            }
        } catch (IOException | JSONException e) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, e);
        }

    }
}
