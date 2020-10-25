package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;


@Entity
@Table(name = "INSCRIPCION_EVAL")
public class InscripcionEval implements Serializable{
    
    @Id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NID_INSCRIPCION")
    private Inscripcion inscripcion;
    
    @JoinColumn(name = "NID_DNA", referencedColumnName = "NID_DNA")
    @ManyToOne(fetch = FetchType.LAZY)
    private Defensoria dna;

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

    @JoinColumn(name = "NID_ESTADO", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo estado;

    @Column(name = "FLG_CONSTANCIA")
    private Integer flagConstancia;

    @Column(name = "TXT_PC")
    private String txtPc;

    @Column(name = "TXT_IP")
    private String txtIp;
    
    @Column(name = "FLG_ACTIVO")
    private BigInteger flgActivo;
    
    @Column(name = "FEC_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRegistro;
    
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    
    @Column(name = "NID_USUARIO_REG")
    private BigInteger nidUsuarioReg;
    
    @Column(name = "NID_USUARIO_MOD")
    private BigInteger nidUsuarioMod;
    
    @Size(max = 300)
    @Column(name = "OBS_DOCCREACION")
    private String obsDocCreacion;
 
    @Size(max = 300)
    @Column(name = "OBS_DIRECCION")
    private String obsDireccion;
    
    @Size(max = 300)
    @Column(name = "OBS_GERENCIA")
    private String obsGerencia;
    
    @Size(max = 300)
    @Column(name = "OBS_CORREO")
    private String obsCorreo;
    
    @Size(max = 300)
    @Column(name = "OBS_AMBIENTES_PRIV")
    private String obsAmbientesPriv;
    
    @Size(max = 300)
    @Column(name = "OBS_AMBIENTES")
    private String obsAmbientes;
    
    @Size(max = 300)
    @Column(name = "OBS_DIAS")
    private String obsDias;
    
    @Size(max = 300)
    @Column(name = "OBS_PRESUPUESTO")
    private String obsPresupuesto;
    
    @Size(max = 300)
    @Column(name = "OBS_DOCS")
    private String obsDocs;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscripcionEval", fetch = FetchType.LAZY)
    private List<PersonaDnaEval> personal;

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Defensoria getDna() {
        return dna;
    }

    public void setDna(Defensoria dna) {
        this.dna = dna;
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

    public Catalogo getEstado() {
        return estado;
    }

    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    public Integer getFlagConstancia() {
        return flagConstancia;
    }

    public void setFlagConstancia(Integer flagConstancia) {
        this.flagConstancia = flagConstancia;
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

    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(BigInteger flgActivo) {
        this.flgActivo = flgActivo;
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

    public BigInteger getNidUsuarioReg() {
        return nidUsuarioReg;
    }

    public void setNidUsuarioReg(BigInteger nidUsuarioReg) {
        this.nidUsuarioReg = nidUsuarioReg;
    }

    public BigInteger getNidUsuarioMod() {
        return nidUsuarioMod;
    }

    public void setNidUsuarioMod(BigInteger nidUsuarioMod) {
        this.nidUsuarioMod = nidUsuarioMod;
    }

    public String getObsDireccion() {
        return obsDireccion;
    }

    public void setObsDireccion(String obsDireccion) {
        this.obsDireccion = obsDireccion;
    }

    public String getObsGerencia() {
        return obsGerencia;
    }

    public void setObsGerencia(String obsGerencia) {
        this.obsGerencia = obsGerencia;
    }

    public String getObsCorreo() {
        return obsCorreo;
    }

    public void setObsCorreo(String obsCorreo) {
        this.obsCorreo = obsCorreo;
    }

    public String getObsAmbientesPriv() {
        return obsAmbientesPriv;
    }

    public void setObsAmbientesPriv(String obsAmbientesPriv) {
        this.obsAmbientesPriv = obsAmbientesPriv;
    }

    public String getObsAmbientes() {
        return obsAmbientes;
    }

    public void setObsAmbientes(String obsAmbientes) {
        this.obsAmbientes = obsAmbientes;
    }

    public String getObsDias() {
        return obsDias;
    }

    public void setObsDias(String obsDias) {
        this.obsDias = obsDias;
    }

    public String getObsPresupuesto() {
        return obsPresupuesto;
    }

    public void setObsPresupuesto(String obsPresupuesto) {
        this.obsPresupuesto = obsPresupuesto;
    }

    public String getObsDocs() {
        return obsDocs;
    }

    public void setObsDocs(String obsDocs) {
        this.obsDocs = obsDocs;
    }

    public List<PersonaDnaEval> getPersonal() {
        return personal;
    }

    public void setPersonal(List<PersonaDnaEval> personal) {
        this.personal = personal;
    }

    public String getObsDocCreacion() {
        return obsDocCreacion;
    }

    public void setObsDocCreacion(String obsDocCreacion) {
        this.obsDocCreacion = obsDocCreacion;
    }

    
}
