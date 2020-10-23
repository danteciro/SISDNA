package pe.gob.mimp.sisdna.reporte.modelo;

import java.util.ArrayList;
import java.util.List;

public class Formulario04DSLDReport {
       
    
    private String fecha;
    private String municipalidad;
    private String nroInscripcion;
    private String nombreDemuna;
    private String fechaNroOrdenanza;
    private String normaArtOrdenanza;
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String emailContacto;
    private String telefono;
    private String diasHorasAtencion;
    private String organoPerteneceDemuna;
    private String nroAmbientes;
    private String nroAmbientesPrivados;
    
    private String conEquipoDeComputo;
    private String sinEquipoDeComputo;
    private String conImpresora;
    private String sinImpresora;
    private String conInternet;
    private String sinInternet;
    private String conArchivadoresSeguros;
    private String sinArchivadoresSeguros;
    private String conAccesibilidad;
    private String sinAccesibilidad;
    private String conAreaLudica;
    private String sinAreaLudica;
    
    private String conEstadoBueno;
    private String conEstadoRegular;
    private String conEstadoMalo;
    private String authNotifiacion;
    
    private List<Detalle04DSLDReport> equipo;
    private List<DeclaracionJuradaReport> declaraciones;
    
    
    public Formulario04DSLDReport(){
    
        this.fecha = "";
        this.municipalidad = "";
        this.nroInscripcion = "";
        this.nombreDemuna = "";
        this.fechaNroOrdenanza = "";
        this.normaArtOrdenanza = "";
        this.direccion = "";
        this.departamento = "";
        this.provincia = "";
        this.distrito = "";
        this.emailContacto = "";
        this.telefono = "";
        this.diasHorasAtencion = "";
        this.organoPerteneceDemuna = "";
        this.nroAmbientes = "";
        this.nroAmbientesPrivados = "";
        this.conEquipoDeComputo = "";
        this.sinEquipoDeComputo = "";
        this.conImpresora = "";
        this.sinImpresora = "";
        this.conInternet = "";
        this.sinInternet = "";
        this.conArchivadoresSeguros = "";
        this.sinArchivadoresSeguros = "";
        this.conAccesibilidad = "";
        this.sinAccesibilidad = "";
        this.conAreaLudica = "";
        this.sinAreaLudica = "";
        this.conEstadoBueno = "";
        this.conEstadoRegular = "";
        this.conEstadoMalo = "";
        this.authNotifiacion = "";
        this.equipo = new ArrayList<>();
        this.declaraciones = new ArrayList<>();
    }
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMunicipalidad() {
        return municipalidad;
    }

    public void setMunicipalidad(String municipalidad) {
        this.municipalidad = municipalidad;
    }

    public String getNroInscripcion() {
        return nroInscripcion;
    }

    public void setNroInscripcion(String nroInscripcion) {
        this.nroInscripcion = nroInscripcion;
    }

    public String getNombreDemuna() {
        return nombreDemuna;
    }

    public void setNombreDemuna(String nombreDemuna) {
        this.nombreDemuna = nombreDemuna;
    }

    public String getFechaNroOrdenanza() {
        return fechaNroOrdenanza;
    }

    public void setFechaNroOrdenanza(String fechaNroOrdenanza) {
        this.fechaNroOrdenanza = fechaNroOrdenanza;
    }

    public String getNormaArtOrdenanza() {
        return normaArtOrdenanza;
    }

    public void setNormaArtOrdenanza(String normaArtOrdenanza) {
        this.normaArtOrdenanza = normaArtOrdenanza;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDiasHorasAtencion() {
        return diasHorasAtencion;
    }

    public void setDiasHorasAtencion(String diasHorasAtencion) {
        this.diasHorasAtencion = diasHorasAtencion;
    }

    public String getOrganoPerteneceDemuna() {
        return organoPerteneceDemuna;
    }

    public void setOrganoPerteneceDemuna(String organoPerteneceDemuna) {
        this.organoPerteneceDemuna = organoPerteneceDemuna;
    }

    public String getNroAmbientes() {
        return nroAmbientes;
    }

    public void setNroAmbientes(String nroAmbientes) {
        this.nroAmbientes = nroAmbientes;
    }

    public String getNroAmbientesPrivados() {
        return nroAmbientesPrivados;
    }

    public void setNroAmbientesPrivados(String nroAmbientesPrivados) {
        this.nroAmbientesPrivados = nroAmbientesPrivados;
    }

    public String getConEquipoDeComputo() {
        return conEquipoDeComputo;
    }

    public void setConEquipoDeComputo(String conEquipoDeComputo) {
        this.conEquipoDeComputo = conEquipoDeComputo;
    }

    public String getSinEquipoDeComputo() {
        return sinEquipoDeComputo;
    }

    public void setSinEquipoDeComputo(String sinEquipoDeComputo) {
        this.sinEquipoDeComputo = sinEquipoDeComputo;
    }

    public String getConImpresora() {
        return conImpresora;
    }

    public void setConImpresora(String conImpresora) {
        this.conImpresora = conImpresora;
    }

    public String getSinImpresora() {
        return sinImpresora;
    }

    public void setSinImpresora(String sinImpresora) {
        this.sinImpresora = sinImpresora;
    }

    public String getConInternet() {
        return conInternet;
    }

    public void setConInternet(String conInternet) {
        this.conInternet = conInternet;
    }

    public String getSinInternet() {
        return sinInternet;
    }

    public void setSinInternet(String sinInternet) {
        this.sinInternet = sinInternet;
    }

    public String getConArchivadoresSeguros() {
        return conArchivadoresSeguros;
    }

    public void setConArchivadoresSeguros(String conArchivadoresSeguros) {
        this.conArchivadoresSeguros = conArchivadoresSeguros;
    }

    public String getSinArchivadoresSeguros() {
        return sinArchivadoresSeguros;
    }

    public void setSinArchivadoresSeguros(String sinArchivadoresSeguros) {
        this.sinArchivadoresSeguros = sinArchivadoresSeguros;
    }

    public String getConAccesibilidad() {
        return conAccesibilidad;
    }

    public void setConAccesibilidad(String conAccesibilidad) {
        this.conAccesibilidad = conAccesibilidad;
    }

    public String getSinAccesibilidad() {
        return sinAccesibilidad;
    }

    public void setSinAccesibilidad(String sinAccesibilidad) {
        this.sinAccesibilidad = sinAccesibilidad;
    }

    public String getConAreaLudica() {
        return conAreaLudica;
    }

    public void setConAreaLudica(String conAreaLudica) {
        this.conAreaLudica = conAreaLudica;
    }

    public String getSinAreaLudica() {
        return sinAreaLudica;
    }

    public void setSinAreaLudica(String sinAreaLudica) {
        this.sinAreaLudica = sinAreaLudica;
    }

    public String getConEstadoBueno() {
        return conEstadoBueno;
    }

    public void setConEstadoBueno(String conEstadoBueno) {
        this.conEstadoBueno = conEstadoBueno;
    }

    public String getConEstadoRegular() {
        return conEstadoRegular;
    }

    public void setConEstadoRegular(String conEstadoRegular) {
        this.conEstadoRegular = conEstadoRegular;
    }

    public String getConEstadoMalo() {
        return conEstadoMalo;
    }

    public void setConEstadoMalo(String conEstadoMalo) {
        this.conEstadoMalo = conEstadoMalo;
    }

    public String getAuthNotifiacion() {
        return authNotifiacion;
    }

    public void setAuthNotifiacion(String authNotifiacion) {
        this.authNotifiacion = authNotifiacion;
    }

    public List<Detalle04DSLDReport> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<Detalle04DSLDReport> equipo) {
        this.equipo = equipo;
    }

    public List<DeclaracionJuradaReport> getDeclaraciones() {
        return declaraciones;
    }

    public void setDeclaraciones(List<DeclaracionJuradaReport> declaraciones) {
        this.declaraciones = declaraciones;
    }
    
}
