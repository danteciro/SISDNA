package pe.gob.mimp.sisdna.reporte.modelo;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


/**
 * Clase: ResolucionDirectoralReport.java <br>
 * Clase encargada de alimentar el reporte de la resolución directoral
 * Fecha de Creación: 21/10/2019<br>
 * 
 * @author BooleanCore
 */
public class ResolucionDirectoralReport {
    
    private String nombre;
    
    private String curso;
    
    private String ciudad;
    
    private String fechas;
    
    private String nombreDir;
    
    private String departamento;

    private String provincia;

    private String distrito;

    private String direccion;
    
    private String nroInforme;
    
    private String nroIntegrantes;

    private String nroAprobados;

    private List<AprobadoReport> listaAprobados = new ArrayList<>();
    
    private JRBeanCollectionDataSource aprobDataSource;
   
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

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    public String getNombreDir() {
        return nombreDir;
    }

    public void setNombreDir(String nombreDir) {
        this.nombreDir = nombreDir;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNroInforme() {
        return nroInforme;
    }

    public void setNroInforme(String nroInforme) {
        this.nroInforme = nroInforme;
    }

    public String getNroIntegrantes() {
        return nroIntegrantes;
    }

    public void setNroIntegrantes(String nroIntegrantes) {
        this.nroIntegrantes = nroIntegrantes;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNroAprobados() {
        return nroAprobados;
    }

    public void setNroAprobados(String nroAprobados) {
        this.nroAprobados = nroAprobados;
    }
    
    public List<AprobadoReport> getListaAprobados() {
        return listaAprobados;
    }

    public void setListaAprobados(List<AprobadoReport> listaAprobados) {
        this.listaAprobados = listaAprobados;
    }

    public JRBeanCollectionDataSource getAprobDataSource() {
        return new JRBeanCollectionDataSource(this.listaAprobados, false);
    }

     
}
