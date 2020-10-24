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
@Table(name = "ACREDITACION")
@NamedQueries({
    @NamedQuery(name = "Acreditacion.filtrarPorConstancia", query = "SELECT a FROM Acreditacion as a WHERE a.dna.txtConstancia=:txtConstancia and a.dna.flgActivo = 1"),
    @NamedQuery(name = "Acreditacion.filtrarDepartamentos", query = "SELECT a FROM Acreditacion as a WHERE a.dna.nidDepartamento=:nidDepartamento and a.dna.flgActivo = 1"),
    @NamedQuery(name = "Acreditacion.filtrarProvincias", query = "SELECT a FROM Acreditacion as a WHERE a.dna.nidProvincia=:nidProvincia and a.dna.flgActivo = 1"),
    @NamedQuery(name = "Acreditacion.filtrarDistritos", query = "SELECT a FROM Acreditacion as a WHERE a.dna.nidDistrito=:nidDistrito and a.dna.flgActivo = 1"),
    
    @NamedQuery(name = "Acreditacion.filtrarObservadasVencidos", query = "SELECT i from Acreditacion as i WHERE i.estado.nidCatalogo = :estadoPorEvaluar and func('TRUNC',i.fecObservado) < func('TRUNC',:hoy)"),
 
})
public class Acreditacion implements Serializable{
    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_ACREDITACION", sequenceName = "SQ_ACREDITACION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ACREDITACION")
    @Column(name = "NID_ACREDITACION")
    private BigDecimal nidAcreditacion;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NID_DNA")
    private Defensoria dna;
    
    @JoinColumn(name = "NID_ESTADO", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo estado;
   
    @Column(name = "FEC_ORDENANZA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecOrdenanza; 
     
    @Size(max = 150)
    @Column(name = "TXT_NORMA")
    private String txtNorma;
  
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

    @Size(max = 150)
    @Column(name = "TXT_GERENCIA")
    private String txtGerencia;

    @Column(name = "AMBIENTES")
    private Integer ambientes;

    @Column(name = "AMBIENTES_PRIV")
    private Integer ambientesPriv;

    @Size(max = 150)
    @Column(name = "DIAS")
    private String dias;
    
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
    
     
    @Size(max = 3)
    @Column(name = "TXT_ESTADO_CONS")
    private String txtEstadoCons;
    
    @Column(name = "FEC_RESOLUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecResolucion; 
    
    @Size(max = 100)
    @Column(name = "TXT_NRO_RESOLUCION")
    private String txtNroResolucion;
    
    
    @Column(name = "FLG_CONSTANCIA")
    private Integer flagConstancia;

    @Column(name = "FLG_OFICIO")
    private Integer flagOficio;
    
    @Column(name = "FEC_ACREDITACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAcreditacion;
    
    @Column(name = "FEC_OBSERVADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecObservado;
    
    @Size(max = 850)
    @Column(name = "OBS_DENEGADO")
    private String obsDenegado;
    
    @Column(name = "FEC_SUBSANADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSubsanado;
    
    @Column(name = "FEC_DENEGADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDenegado;
    
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
    private BigDecimal nidUsuarioReg;
    
    @Column(name = "NID_USUARIO_MOD")
    private BigDecimal nidUsuarioMod;
    
    @Column(name = "NID_USUARIO")
    private BigDecimal nidUsuario;
    
    @Size(max = 10)
    @Column(name = "TXT_CONSTANCIA")
    private String txtConstancia;

    
    @OneToOne(fetch=FetchType.LAZY, mappedBy="acreditacion")
    private AcreditacionEval acreditacionEval;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acreditacion", fetch = FetchType.LAZY)
    private List<PersonaDnaAcre> equipo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acreditacion", fetch = FetchType.LAZY)
    private List<DocAcreditacion> documentos;
    
    public Acreditacion() {
        this.flgEquipoComputo = false;
        this.flgImpresora = false;
        this.flgInternet = false;
        this.flgArchivadores = false;
        this.flgAccesibilidad = false;
        this.flgAreaLudica = false;
        this.txtEstadoCons = "";
    }
    
    public BigDecimal getNidAcreditacion() {
        return nidAcreditacion;
    }

    public void setNidAcreditacion(BigDecimal nidAcreditacion) {
        this.nidAcreditacion = nidAcreditacion;
    }

    public Defensoria getDna() {
        return dna;
    }

    public void setDna(Defensoria dna) {
        this.dna = dna;
    }

  
    public String getTxtEstadoCons() {
        return txtEstadoCons;
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
    
    public void setTxtEstadoCons(String txtEstadoCons) {
        this.txtEstadoCons = txtEstadoCons;
    }

    public String getTxtNorma() {
        return txtNorma;
    }

    public void setTxtNorma(String txtNorma) {
        this.txtNorma = txtNorma;
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

   

    public List<PersonaDnaAcre> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<PersonaDnaAcre> equipo) {
        this.equipo = equipo;
    }

    public Catalogo getEstado() {
        return estado;
    }

    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    public Date getFecAcreditacion() {
        return fecAcreditacion;
    }

    public void setFecAcreditacion(Date fecAcreditacion) {
        this.fecAcreditacion = fecAcreditacion;
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

    public String getObsDenegado() {
        return obsDenegado;
    }

    public void setObsDenegado(String obsDenegado) {
        this.obsDenegado = obsDenegado;
    }
    
    public AcreditacionEval getAcreditacionEval() {
        return acreditacionEval;
    }

    public void setAcreditacionEval(AcreditacionEval acreditacionEval) {
        this.acreditacionEval = acreditacionEval;
    }

    public List<DocAcreditacion> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocAcreditacion> documentos) {
        this.documentos = documentos;
    }

    public BigDecimal getNidUsuario() {
        return nidUsuario;
    }

    public void setNidUsuario(BigDecimal nidUsuario) {
        this.nidUsuario = nidUsuario;
    }

    public String getTxtConstancia() {
        return txtConstancia;
    }

    public void setTxtConstancia(String txtConstancia) {
        this.txtConstancia = txtConstancia;
    }

   
    public Integer getFlagConstancia() {
        return flagConstancia;
    }

    public void setFlagConstancia(Integer flagConstancia) {
        this.flagConstancia = flagConstancia;
    }

    public Integer getFlagOficio() {
        return flagOficio;
    }

    public void setFlagOficio(Integer flagOficio) {
        this.flagOficio = flagOficio;
    }

    public Date getFecOrdenanza() {
        return fecOrdenanza;
    }

    public void setFecOrdenanza(Date fecOrdenanza) {
        this.fecOrdenanza = fecOrdenanza;
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
    
}
