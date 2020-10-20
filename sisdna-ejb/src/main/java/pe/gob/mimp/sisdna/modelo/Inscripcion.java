package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "INSCRIPCION")
@NamedQueries({
    @NamedQuery(name = "Inscripcion.filtrarPorConstancia", query = "SELECT i FROM Inscripcion as i WHERE i.dna.txtConstancia=:txtConstancia and i.dna.flgActivo = 1"),
    @NamedQuery(name = "Inscripcion.filtrarDepartamentos", query = "SELECT i FROM Inscripcion as i WHERE i.dna.nidDepartamento=:nidDepartamento and i.dna.flgActivo = 1"),
    @NamedQuery(name = "Inscripcion.filtrarProvincias", query = "SELECT i FROM Inscripcion as i WHERE i.dna.nidProvincia=:nidProvincia and i.dna.flgActivo = 1"),
    @NamedQuery(name = "Inscripcion.filtrarDistritos", query = "SELECT i FROM Inscripcion as i WHERE i.dna.nidDistrito=:nidDistrito and i.dna.flgActivo = 1"),

    @NamedQuery(name = "Inscripcion.filtrarObservadasVencidos", query = "SELECT i from Inscripcion as i WHERE i.estado.nidCatalogo = :estadoPorEvaluar and func('TRUNC',i.fecObservado) < func('TRUNC',:hoy)"),
    @NamedQuery(name = "Inscripcion.filtrarPorEvaluarPorVencer", query = "SELECT i from Inscripcion as i WHERE i.estado.nidCatalogo = :estadoPorEvaluar and func('TRUNC',i.fecRegistro) <= func('TRUNC',:hoy)"),
    @NamedQuery(name = "Inscripcion.filtrarSubsanadasPorVencer", query = "SELECT i from Inscripcion as i WHERE i.estado.nidCatalogo = :estadoPorEvaluar and func('TRUNC',i.fecSubsanado) <= func('TRUNC',:hoy)"),

    @NamedQuery(name = "Inscripcion.filtrarPorConstanciaEstado", query = "SELECT i FROM Inscripcion as i WHERE i.dna.txtConstancia=:txtConstancia and i.dna.flgActivo = 1 and (i.flagAcredita = 0 or i.flagAcredita IS NULL )and i.estado.nidCatalogo=:nidCatalogo"),
    @NamedQuery(name = "Inscripcion.filtrarDepartamentosEstado", query = "SELECT i FROM Inscripcion as i WHERE i.dna.nidDepartamento=:nidDepartamento and i.dna.flgActivo = 1 and (i.flagAcredita = 0 or i.flagAcredita IS NULL ) and i.estado.nidCatalogo=:nidCatalogo"),
    @NamedQuery(name = "Inscripcion.filtrarProvinciasEstado", query = "SELECT i FROM Inscripcion as i WHERE i.dna.nidProvincia=:nidProvincia and i.dna.flgActivo = 1 and (i.flagAcredita = 0 or i.flagAcredita IS NULL ) and  i.estado.nidCatalogo=:nidCatalogo"),
    @NamedQuery(name = "Inscripcion.filtrarDistritosEstado", query = "SELECT i FROM Inscripcion as i WHERE i.dna.nidDistrito=:nidDistrito and i.dna.flgActivo = 1 and (i.flagAcredita = 0 or i.flagAcredita IS NULL ) and i.estado.nidCatalogo=:nidCatalogo"),

})
public class Inscripcion implements Serializable {
    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_INSCRIPCION", sequenceName = "SQ_INSCRIPCION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_INSCRIPCION")
    @Column(name = "NID_INSCRIPCION")
    private BigDecimal nidInscripcion;

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

    @Column(name = "FLG_OFICIO")
    private Integer flagOficio;
    
    @Column(name = "FLG_ACREDITA")
    private BigInteger flagAcredita;

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
    
    @Column(name = "NID_USUARIO")
    private BigDecimal nidUsuario;
   
    @Column(name = "NID_USUARIO_REG")
    private BigDecimal nidUsuarioReg;
    
    @Column(name = "NID_USUARIO_MOD")
    private BigDecimal nidUsuarioMod;
    
    @Column(name = "FEC_INSCRIPCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInscrito;
    
    @Column(name = "FEC_OBSERVADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecObservado;
    
    @Column(name = "FEC_SUBSANADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSubsanado;
    
    @Column(name = "FEC_DENEGADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDenegado;
    
    @Size(max = 850)
    @Column(name = "OBS_DENEGADO")
    private String obsDenegado;
    
    @OneToOne(fetch=FetchType.LAZY, mappedBy="inscripcion")
    private InscripcionEval inscripcionEval;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscripcion", fetch = FetchType.LAZY)
    private List<PersonaDna> personal;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscripcion", fetch = FetchType.LAZY)
    private List<DocInscripcion> documentos;
   
    public BigDecimal getNidInscripcion() {
        return nidInscripcion;
    }
    
    public void setNidInscripcion(BigDecimal nidInscripcion) {
        this.nidInscripcion = nidInscripcion;
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

    public Date getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public BigDecimal getNidUsuario() {
        return nidUsuario;
    }

    public void setNidUsuario(BigDecimal nidUsuario) {
        this.nidUsuario = nidUsuario;
    }

    public BigDecimal getNidUsuarioReg() {
        return nidUsuarioReg;
    }

    public void setNidUsuarioReg(BigDecimal nidUsuarioReg) {
        this.nidUsuarioReg = nidUsuarioReg;
    }

    public BigDecimal getNidUsuarioMod() {
        return nidUsuarioMod;
    }

    public void setNidUsuarioMod(BigDecimal nidUsuarioMod) {
        this.nidUsuarioMod = nidUsuarioMod;
    }

    
    public List<PersonaDna> getPersonal() {
        return personal;
    }

    public void setPersonal(List<PersonaDna> personal) {
        this.personal = personal;
    }

    public Date getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(Date fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

   
    public List<DocInscripcion> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocInscripcion> documentos) {
        this.documentos = documentos;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Date getFecInscrito() {
        return fecInscrito;
    }

    public void setFecInscrito(Date fecInscrito) {
        this.fecInscrito = fecInscrito;
    }

    public Date getFecObservado() {
        return fecObservado;
    }

    public void setFecObservado(Date fecObservado) {
        this.fecObservado = fecObservado;
    }

    public Date getFecSubsanado() {
        return fecSubsanado;
    }

    public void setFecSubsanado(Date fecSubsanado) {
        this.fecSubsanado = fecSubsanado;
    }

    public Date getFecDenegado() {
        return fecDenegado;
    }

    public void setFecDenegado(Date fecDenegado) {
        this.fecDenegado = fecDenegado;
    }

    public InscripcionEval getInscripcionEval() {
        return inscripcionEval;
    }

    public void setInscripcionEval(InscripcionEval inscripcionEval) {
        this.inscripcionEval = inscripcionEval;
    }

    public String getObsDenegado() {
        return obsDenegado;
    }

    public void setObsDenegado(String obsDenegado) {
        this.obsDenegado = obsDenegado;
    }

    public Integer getFlagOficio() {
        return flagOficio;
    }

    public void setFlagOficio(Integer flagOficio) {
        this.flagOficio = flagOficio;
    }

    public Defensoria getDna() {
        return dna;
    }

    public void setDna(Defensoria dna) {
        this.dna = dna;
    }

    public BigInteger getFlagAcredita() {
        return flagAcredita;
    }

    public void setFlagAcredita(BigInteger flagAcredita) {
        this.flagAcredita = flagAcredita;
    }
    
}  
