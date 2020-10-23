package pe.gob.mimp.sisdna.reporte.modelo;


/**
 * Clase: CertificadoItemReport.java <br>
 * Clase que representa los ítems del certificado del curso
 * Fecha de Creación: 21/10/2019<br>
 * 
 * @author BooleanCore
 */
public class CertificadoItemReport {
    
    private String item;

    private String porcentaje;

    private String puntaje;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    
}
