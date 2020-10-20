package pe.gob.mimp.webservicemimp.auxiliar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 * Clase: IdentificacionReniec.java <br>
 * Clase que consume el WS de Reniec.<br>
 * 
 */
public class IdentificacionReniec {

    private String CODIGO;
    private String NOMBRES;
    private String APPAT;
    private String APMAT;
    private String ESTADOCIVIL;
    private String DIRECCION;
    private String UBIGEO;

    public IdentificacionReniec() {

    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public String getNOMBRES() {
        return NOMBRES;
    }

    public void setNOMBRES(String NOMBRES) {
        this.NOMBRES = NOMBRES;
    }

    public String getAPPAT() {
        return APPAT;
    }

    public void setAPPAT(String APPAT) {
        this.APPAT = APPAT;
    }

    public String getAPMAT() {
        return APMAT;
    }

    public void setAPMAT(String APMAT) {
        this.APMAT = APMAT;
    }

    public String getESTADOCIVIL() {
        return ESTADOCIVIL;
    }

    public void setESTADOCIVIL(String ESTADOCIVIL) {
        this.ESTADOCIVIL = ESTADOCIVIL;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getUBIGEO() {
        return UBIGEO;
    }

    public void setUBIGEO(String UBIGEO) {
        this.UBIGEO = UBIGEO;
    }

    public void obtenerConsultaReniec(String dni) throws MalformedURLException {
        //ReniecWsClient client = new ReniecWsClient();
        try {

            if (dni != null) {
                if (dni.length() == 8) {
                 //   String urlProxy = "https://appweb.mimp.gob.pe:8181/wsService/faces/ConsultaReniec?nroDni=" + dni;
                  String urlProxy = "http://192.168.4.50:8080/wsService/faces/ConsultaReniec?nroDni=" + dni;

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

                        URL url = new URL(urlProxy);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setUseCaches(false);
                        conn.setDoOutput(true);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");
                        InputStream instream = conn.getInputStream();//add                        
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
                        System.out.println("jsonObjReturnFB-coResultado:" + jsonObjReturnFB.get("coResultado"));

                        if (jsonObjReturnFB.get("coResultado").equals("0000")) {
                            System.out.println("jsonObjReturnFB-datosPersona:" + jsonObjReturnFB.get("datosPersona"));
                            JSONObject jsonDataReturn = jsonObjReturnFB.getJSONObject("datosPersona");
                            this.setCODIGO(jsonObjReturnFB.get("coResultado").toString());
                            this.setNOMBRES(jsonDataReturn.get("preNombres").toString());
                            this.setAPPAT(jsonDataReturn.get("apPrimer").toString());
                            this.setAPMAT(jsonDataReturn.get("apSegundo").toString());
                            this.setESTADOCIVIL(jsonDataReturn.get("estadoCivil").toString());
                            this.setUBIGEO(jsonDataReturn.get("ubigeo").toString());
                            this.setDIRECCION(jsonDataReturn.get("direccion").toString());
                        } else {
                            this.setCODIGO("0001");
                        }
                    } catch (IOException | JSONException e) {
                        Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, e);
        }
    }
}
