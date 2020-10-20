package pe.gob.mimp.sisdna.reporte.modelo;

import java.util.ArrayList;
import java.util.List;


public class Formulario03DSLDReport {
    
    private String nombreEntidadResponsable;
    private String nombreDNA;
    private String nroInscripcionDNA;
    private String documentoCreacionDNA;
    private String direccionDNA;
    private String departamento;
    private String provincia;
    private String distrito;
    private String correoElectronicoContacto;
    private String telefonos;
    private String direccionPerteneceDNA;
    private String diasHorasAtencion;
    private String presupuestoDesignado;
    private String numeroDeAmbientesDNA;
    private String numeroDeAmbientesAudiencia;
    private String nombresResp;
    private String apellidosResp;
    private String telefonoResp;
    private String correoResp;
    private String dniResp;
    private String documentoDesignacionResp;
    private String gradoInstruccionResp;
    private String edadResp;
    private String sexoResp;
    private String profesionResp;
    private String colegioProfesionalResp;
    private String colegiaturaResp;
    private String fechaLugarLlevoCurso;
    private String fechaLugarFormacion;
    private List<Detalle03DSLDREport> listaDefensores;
    private List<Detalle03DSLDREport> listaPromoteres;
    private List<Detalle03DSLDREport> listaPersonal;
    private String authNotificacion;
    private List<DeclaracionJuradaReport> declaraciones;
    
    public Formulario03DSLDReport() {
        this.nombreEntidadResponsable = "";
        this.nombreDNA = "";
        this.nroInscripcionDNA = "";
        this.documentoCreacionDNA = "";
        this.direccionDNA = "";
        this.departamento = "";
        this.provincia = "";
        this.distrito = "";
        this.correoElectronicoContacto = "";
        this.telefonos = "";
        this.direccionPerteneceDNA = "";
        this.diasHorasAtencion = "";
        this.presupuestoDesignado = "";
        this.numeroDeAmbientesDNA = "";
        this.numeroDeAmbientesAudiencia = "";
        this.nombresResp = "";
        this.apellidosResp = "";
        this.telefonoResp = "";
        this.correoResp = "";
        this.dniResp = "";
        this.documentoDesignacionResp = "";
        this.gradoInstruccionResp = "";
        this.edadResp = "";
        this.sexoResp = "";
        this.profesionResp = "";
        this.colegioProfesionalResp = "";
        this.colegiaturaResp = "";
        this.fechaLugarLlevoCurso = "";
        this.fechaLugarFormacion = "";
        this.listaDefensores = new ArrayList<>();
        this.listaPromoteres = new ArrayList<>();
        this.listaPersonal = new ArrayList<>();
        this.authNotificacion = "";
        this.declaraciones = new ArrayList<>();
    }
    
    
    public String getNombreEntidadResponsable() {
        return nombreEntidadResponsable;
    }

    public void setNombreEntidadResponsable(String nombreEntidadResponsable) {
        this.nombreEntidadResponsable = nombreEntidadResponsable;
    }

    public String getNombreDNA() {
        return nombreDNA;
    }

    public void setNombreDNA(String nombreDNA) {
        this.nombreDNA = nombreDNA;
    }

    public String getNroInscripcionDNA() {
        return nroInscripcionDNA;
    }

    public void setNroInscripcionDNA(String nroInscripcionDNA) {
        this.nroInscripcionDNA = nroInscripcionDNA;
    }

    public String getDocumentoCreacionDNA() {
        return documentoCreacionDNA;
    }

    public void setDocumentoCreacionDNA(String documentoCreacionDNA) {
        this.documentoCreacionDNA = documentoCreacionDNA;
    }

    public String getDireccionDNA() {
        return direccionDNA;
    }

    public void setDireccionDNA(String direccionDNA) {
        this.direccionDNA = direccionDNA;
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

    public String getCorreoElectronicoContacto() {
        return correoElectronicoContacto;
    }

    public void setCorreoElectronicoContacto(String correoElectronicoContacto) {
        this.correoElectronicoContacto = correoElectronicoContacto;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getDireccionPerteneceDNA() {
        return direccionPerteneceDNA;
    }

    public void setDireccionPerteneceDNA(String direccionPerteneceDNA) {
        this.direccionPerteneceDNA = direccionPerteneceDNA;
    }

    public String getNumeroDeAmbientesDNA() {
        return numeroDeAmbientesDNA;
    }

    public void setNumeroDeAmbientesDNA(String numeroDeAmbientesDNA) {
        this.numeroDeAmbientesDNA = numeroDeAmbientesDNA;
    }

    public String getNumeroDeAmbientesAudiencia() {
        return numeroDeAmbientesAudiencia;
    }

    public void setNumeroDeAmbientesAudiencia(String numeroDeAmbientesAudiencia) {
        this.numeroDeAmbientesAudiencia = numeroDeAmbientesAudiencia;
    }

    public String getNombresResp() {
        return nombresResp;
    }

    public void setNombresResp(String nombresResp) {
        this.nombresResp = nombresResp;
    }

    public String getApellidosResp() {
        return apellidosResp;
    }

    public void setApellidosResp(String apellidosResp) {
        this.apellidosResp = apellidosResp;
    }

    public String getTelefonoResp() {
        return telefonoResp;
    }

    public void setTelefonoResp(String telefonoResp) {
        this.telefonoResp = telefonoResp;
    }

    public String getCorreoResp() {
        return correoResp;
    }

    public void setCorreoResp(String correoResp) {
        this.correoResp = correoResp;
    }

    public String getDniResp() {
        return dniResp;
    }

    public void setDniResp(String dniResp) {
        this.dniResp = dniResp;
    }

    public String getDocumentoDesignacionResp() {
        return documentoDesignacionResp;
    }

    public void setDocumentoDesignacionResp(String documentoDesignacionResp) {
        this.documentoDesignacionResp = documentoDesignacionResp;
    }

    public String getGradoInstruccionResp() {
        return gradoInstruccionResp;
    }

    public void setGradoInstruccionResp(String gradoInstruccionResp) {
        this.gradoInstruccionResp = gradoInstruccionResp;
    }

    public String getEdadResp() {
        return edadResp;
    }

    public void setEdadResp(String edadResp) {
        this.edadResp = edadResp;
    }

    public String getSexoResp() {
        return sexoResp;
    }

    public void setSexoResp(String sexoResp) {
        this.sexoResp = sexoResp;
    }

    public String getProfesionResp() {
        return profesionResp;
    }

    public void setProfesionResp(String profesionResp) {
        this.profesionResp = profesionResp;
    }

    public String getColegioProfesionalResp() {
        return colegioProfesionalResp;
    }

    public void setColegioProfesionalResp(String colegioProfesionalResp) {
        this.colegioProfesionalResp = colegioProfesionalResp;
    }

    public String getColegiaturaResp() {
        return colegiaturaResp;
    }

    public void setColegiaturaResp(String colegiaturaResp) {
        this.colegiaturaResp = colegiaturaResp;
    }

    public String getFechaLugarLlevoCurso() {
        return fechaLugarLlevoCurso;
    }

    public void setFechaLugarLlevoCurso(String fechaLugarLlevoCurso) {
        this.fechaLugarLlevoCurso = fechaLugarLlevoCurso;
    }

    public String getFechaLugarFormacion() {
        return fechaLugarFormacion;
    }

    public void setFechaLugarFormacion(String fechaLugarFormacion) {
        this.fechaLugarFormacion = fechaLugarFormacion;
    }

    public String getDiasHorasAtencion() {
        return diasHorasAtencion;
    }

    public void setDiasHorasAtencion(String diasHorasAtencion) {
        this.diasHorasAtencion = diasHorasAtencion;
    }

    public String getPresupuestoDesignado() {
        return presupuestoDesignado;
    }

    public void setPresupuestoDesignado(String presupuestoDesignado) {
        this.presupuestoDesignado = presupuestoDesignado;
    }
    public List<Detalle03DSLDREport> getListaDefensores() {
        return listaDefensores;
    }

    public void setListaDefensores(List<Detalle03DSLDREport> listaDefensores) {
        this.listaDefensores = listaDefensores;
    }

    public List<Detalle03DSLDREport> getListaPromoteres() {
        return listaPromoteres;
    }

    public void setListaPromoteres(List<Detalle03DSLDREport> listaPromoteres) {
        this.listaPromoteres = listaPromoteres;
    }

    public List<Detalle03DSLDREport> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(List<Detalle03DSLDREport> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }

    public String getAuthNotificacion() {
        return authNotificacion;
    }

    public void setAuthNotificacion(String authNotificacion) {
        this.authNotificacion = authNotificacion;
    }

    public List<DeclaracionJuradaReport> getDeclaraciones() {
        return declaraciones;
    }

    public void setDeclaraciones(List<DeclaracionJuradaReport> declaraciones) {
        this.declaraciones = declaraciones;
    }
    
}
