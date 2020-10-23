package pe.gob.mimp.sisdna.reporte.modelo;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


/**
 * Clase: CursoCertificadoReport.java <br>
 * Clase encargarda de alimentar el reporte del certificado de estudios
 * Fecha de Creación: 21/10/2019<br>
 * 
 * @author BooleanCore
 */
public class CursoCertificadoReport {
    
    private String nombre;
    
    private String curso;
    
    private String ciudad;
    
    private String fecha;
    
    private String horas;
    
    private String fechaCert;
    
    private String nombreDir;
    
    private String sumario;

    private String nombreTutor;
    
    private String nroRd;
   
    private String notaFinal;
   
    private List<CertificadoItemReport> listaItems = new ArrayList<>();
   
    private JRBeanCollectionDataSource itemsDataSource;
   
 
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getFechaCert() {
        return fechaCert;
    }

    public void setFechaCert(String fechaCert) {
        this.fechaCert = fechaCert;
    }

    public String getNombreDir() {
        return nombreDir;
    }

    public void setNombreDir(String nombreDir) {
        this.nombreDir = nombreDir;
    }

    public String getSumario() {
        return sumario;
    }

    public void setSumario(String sumario) {
        this.sumario = sumario;
    }
    
    
    public List<CertificadoItemReport> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<CertificadoItemReport> listaItems) {
        this.listaItems = listaItems;
    }
    
    public JRBeanCollectionDataSource getItemsDataSource() {
        return new JRBeanCollectionDataSource(this.listaItems, false);
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getNroRd() {
        return nroRd;
    }

    public void setNroRd(String nroRd) {
        this.nroRd = nroRd;
    }

    public String getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(String notaFinal) {
        this.notaFinal = notaFinal;
    }

    
}
