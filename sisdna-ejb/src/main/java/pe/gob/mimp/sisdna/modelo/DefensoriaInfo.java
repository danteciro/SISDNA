package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Clase: DefensoriaInfo.java<br>
 * Entidad que registra la información de la defensoría que proviene de la inscripción y acreditación. <br>
 * Fecha de Creación: 20/10/2019<br>
 * 
 * @author BooleanCore
 */
@Entity
@Table(name = "DEFENSORIA_INFO")
public class DefensoriaInfo implements Serializable {
    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_DNA_INFO", sequenceName = "SQ_DNA_INFO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DNA_INFO")
    @Column(name = "NID_INFO")
    private BigInteger nidInfo;

    @Size(max = 150)
    @Column(name = "TXT_DOCUMENTO")
    private String txtDocumento;

    @Size(max = 150)
    @Column(name = "TXT_DIRECCION")
    private String txtDireccion;
    
    @Size(max = 150)
    @Column(name = "TXT_CORREO")
    private String txtCorreo;

    @Size(max = 20)
    @Column(name = "TXT_TELEFONO")
    private String txtTelefono;

    @Size(max = 50)
    @Column(name = "TXT_GERENCIA")
    private String txtGerencia;

    @Column(name = "AMBIENTES")
    private Integer ambientes;

    @Column(name = "AMBIENTES_PRIV")
    private Integer ambientesPriv;

    @Column(name = "PRESUPUESTO")
    private BigDecimal presupuesto;

    @Size(max = 50)
    @Column(name = "DIAS")
    private String dias;

    @Column(name = "FEC_INSCRIPCION")
    @Temporal(TemporalType.DATE)
    private Date fecInscrito;
    

    // Acreditación
    @Column(name = "FLG_EQUIPO_COMPUTO")
    private Boolean flgEquipoComputo;
    
    @Column(name = "FLG_IMPRESORA")
    private Boolean flgImpresora;
    
    @Column(name = "FLG_INTERNET")
    private Boolean flgInternet;
    
    @Column(name = "FLG_ARCHIVADORES")
    private Boolean flgArchivadores;
    
    @Column(name = "FLG_ACCESIBILIDAD")
    private Boolean flgAccesibilidad;
    
    @Column(name = "FLG_AREA_LUDICA")
    private Boolean flgAreaLudica;
    
    @Size(max = 50)
    @Column(name = "TXT_NORMA")
    private String txtNorma;
    
    @Size(max = 3)
    @Column(name = "TXT_ESTADO_CONS")
    private String txtEstadoCons;
    
    @Column(name = "FEC_ORDENANZA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecOrdenanza; 
    
    @Size(max = 100)
    @Column(name = "TXT_NRO_ORDENANZA")
    private String txtNroOrdenanza;
    
    @Column(name = "FEC_RESOLUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecResolucion; 
    
    @Size(max = 100)
    @Column(name = "TXT_NRO_RESOLUCION")
    private String txtNroResolucion;
    
    @Column(name = "FEC_ACREDITACION")
    @Temporal(TemporalType.DATE)
    private Date fecAcreditacion;
    
  
    // Auditoría 
    
    @Column(name = "FLG_ACTIVO")
    private BigInteger flgActivo;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TXT_PC")
    private String txtPc;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TXT_IP")
    private String txtIp;
    
    @Column(name = "FEC_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRegistro;
    
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    
    @Column(name = "NID_USUARIO_MOD")
    private BigDecimal nidUsuarioMod;
    
    @Column(name = "NID_USUARIO_REG")
    private BigDecimal nidUsuarioReg;

    public BigInteger getNidInfo() {
        return nidInfo;
    }

    public void setNidInfo(BigInteger nidInfo) {
        this.nidInfo = nidInfo;
    }

    public String getTxtDocumento() {
        return txtDocumento;
    }

    public void setTxtDocumento(String txtDocumento) {
        this.txtDocumento = txtDocumento;
    }

    public String getTxtDireccion() {
        return txtDireccion;
    }

    public void setTxtDireccion(String txtDireccion) {
        this.txtDireccion = txtDireccion;
    }

    public String getTxtCorreo() {
        return txtCorreo;
    }

    public void setTxtCorreo(String txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    public String getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(String txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public String getTxtGerencia() {
        return txtGerencia;
    }

    public void setTxtGerencia(String txtGerencia) {
        this.txtGerencia = txtGerencia;
    }

    public Integer getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(Integer ambientes) {
        this.ambientes = ambientes;
    }

    public Integer getAmbientesPriv() {
        return ambientesPriv;
    }

    public void setAmbientesPriv(Integer ambientesPriv) {
        this.ambientesPriv = ambientesPriv;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public Date getFecInscrito() {
        return fecInscrito;
    }

    public void setFecInscrito(Date fecInscrito) {
        this.fecInscrito = fecInscrito;
    }

    public Boolean getFlgEquipoComputo() {
        return flgEquipoComputo;
    }

    public void setFlgEquipoComputo(Boolean flgEquipoComputo) {
        this.flgEquipoComputo = flgEquipoComputo;
    }

    public Boolean getFlgImpresora() {
        return flgImpresora;
    }

    public void setFlgImpresora(Boolean flgImpresora) {
        this.flgImpresora = flgImpresora;
    }

    public Boolean getFlgInternet() {
        return flgInternet;
    }

    public void setFlgInternet(Boolean flgInternet) {
        this.flgInternet = flgInternet;
    }

    public Boolean getFlgArchivadores() {
        return flgArchivadores;
    }

    public void setFlgArchivadores(Boolean flgArchivadores) {
        this.flgArchivadores = flgArchivadores;
    }

    public Boolean getFlgAccesibilidad() {
        return flgAccesibilidad;
    }

    public void setFlgAccesibilidad(Boolean flgAccesibilidad) {
        this.flgAccesibilidad = flgAccesibilidad;
    }

    public Boolean getFlgAreaLudica() {
        return flgAreaLudica;
    }

    public void setFlgAreaLudica(Boolean flgAreaLudica) {
        this.flgAreaLudica = flgAreaLudica;
    }

    public String getTxtNorma() {
        return txtNorma;
    }

    public void setTxtNorma(String txtNorma) {
        this.txtNorma = txtNorma;
    }

    public String getTxtEstadoCons() {
        return txtEstadoCons;
    }

    public void setTxtEstadoCons(String txtEstadoCons) {
        this.txtEstadoCons = txtEstadoCons;
    }

    public Date getFecOrdenanza() {
        return fecOrdenanza;
    }

    public void setFecOrdenanza(Date fecOrdenanza) {
        this.fecOrdenanza = fecOrdenanza;
    }

    public String getTxtNroOrdenanza() {
        return txtNroOrdenanza;
    }

    public void setTxtNroOrdenanza(String txtNroOrdenanza) {
        this.txtNroOrdenanza = txtNroOrdenanza;
    }

    public Date getFecResolucion() {
        return fecResolucion;
    }

    public void setFecResolucion(Date fecResolucion) {
        this.fecResolucion = fecResolucion;
    }

    public String getTxtNroResolucion() {
        return txtNroResolucion;
    }

    public void setTxtNroResolucion(String txtNroResolucion) {
        this.txtNroResolucion = txtNroResolucion;
    }

    public Date getFecAcreditacion() {
        return fecAcreditacion;
    }

    public void setFecAcreditacion(Date fecAcreditacion) {
        this.fecAcreditacion = fecAcreditacion;
    }

    
    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(BigInteger flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getTxtPc() {
        return txtPc;
    }

    public void setTxtPc(String txtPc) {
        this.txtPc = txtPc;
    }

    public String getTxtIp() {
        return txtIp;
    }

    public void setTxtIp(String txtIp) {
        this.txtIp = txtIp;
    }

    public Date getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(Date fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public Date getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public BigDecimal getNidUsuarioMod() {
        return nidUsuarioMod;
    }

    public void setNidUsuarioMod(BigDecimal nidUsuarioMod) {
        this.nidUsuarioMod = nidUsuarioMod;
    }

    public BigDecimal getNidUsuarioReg() {
        return nidUsuarioReg;
    }

    public void setNidUsuarioReg(BigDecimal nidUsuarioReg) {
        this.nidUsuarioReg = nidUsuarioReg;
    }
    
    
    
    
}  
