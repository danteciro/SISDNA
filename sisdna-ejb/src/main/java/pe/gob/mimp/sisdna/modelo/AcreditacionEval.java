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
@Table(name = "ACREDITACION_EVAL")
public class AcreditacionEval implements Serializable{
    @Id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NID_ACREDITACION")
    private Acreditacion acreditacion;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NID_PADRE")
    private Inscripcion padre;
    
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
    
    @JoinColumn(name = "NID_ESTADO", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo estado;
    
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acreditacionEval", fetch = FetchType.LAZY)
    private List<PersonaDnaAcreEval> equipo;
    
    @Size(max = 850)
    @Column(name = "OBS_FEC_ORDENANZA")
    private String obsFecOrdenanza;
  
    @Size(max = 850)
    @Column(name = "OBS_NRO_ORDENANZA")
    private String obsNroOrdenanza;
    
    @Size(max = 850)
    @Column(name = "OBS_ESTADO_CONS")
    private String obsEstadoCons;
    
    @Size(max = 850)
    @Column(name = "OBS_DOCS")
    private String obsDocs;

    public AcreditacionEval() {
        this.flgEquipoComputo = false;
        this.flgImpresora = false;
        this.flgInternet = false;
        this.flgArchivadores = false;
        this.flgAccesibilidad = false;
        this.flgAreaLudica = false;
        this.txtEstadoCons = "";
    }

    public Acreditacion getAcreditacion() {
        return acreditacion;
    }

    public void setAcreditacion(Acreditacion acreditacion) {
        this.acreditacion = acreditacion;
    }
    
    public Inscripcion getPadre() {
        return padre;
    }

    public void setPadre(Inscripcion padre) {
        this.padre = padre;
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

    public List<PersonaDnaAcreEval> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<PersonaDnaAcreEval> equipo) {
        this.equipo = equipo;
    }
    
    public Catalogo getEstado() {
        return estado;
    }

    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    public String getObsFecOrdenanza() {
        return obsFecOrdenanza;
    }

    public void setObsFecOrdenanza(String obsFecOrdenanza) {
        this.obsFecOrdenanza = obsFecOrdenanza;
    }

    public String getObsNroOrdenanza() {
        return obsNroOrdenanza;
    }

    public void setObsNroOrdenanza(String obsNroOrdenanza) {
        this.obsNroOrdenanza = obsNroOrdenanza;
    }
    
    
    
    public String getObsEstadoCons() {
        return obsEstadoCons;
    }

    public void setObsEstadoCons(String obsEstadoCons) {
        this.obsEstadoCons = obsEstadoCons;
    }

    public String getObsDocs() {
        return obsDocs;
    }

    public void setObsDocs(String obsDocs) {
        this.obsDocs = obsDocs;
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
    
}
