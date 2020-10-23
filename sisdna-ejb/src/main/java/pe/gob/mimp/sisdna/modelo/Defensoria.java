package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * Clase: Defensoria.java<br>
 * Entidad que registra las defensorías. <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Catalogo
 */
@Entity
@Table(name = "DEFENSORIA")
public class Defensoria implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id
    @SequenceGenerator(name = "SQ_DEFENSORIA", sequenceName = "SQ_DEFENSORIA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DEFENSORIA")
    @Column(name = "NID_DNA")
    private BigInteger nidDna;

    @JoinColumn(name = "NID_INFO", referencedColumnName = "NID_INFO")
    @ManyToOne(fetch = FetchType.LAZY)
    private DefensoriaInfo defensoriaInfo;
  
    @Column(name = "TXT_CONSTANCIA")
    private String txtConstancia;

    @JoinColumn(name = "NID_CENTRAL", referencedColumnName = "NID_DNA")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Defensoria central;

    @JoinColumn(name = "NID_ORIGEN", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo origen;

    @JoinColumn(name = "NID_ESTADO", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo estado;
    
    @Size(max = 3)
    @Column(name = "TXT_TIPO")
    private String txtTipo;
        
    @Size(max = 150)
    @Column(name = "TXT_ENTIDAD")
    private String txtEntidad;

    @Size(max = 150)
    @Column(name = "TXT_NOMBRE")
    private String txtNombre;

    @Column(name = "NID_DEPARTAMENTO")
    private BigDecimal nidDepartamento;

    @Column(name = "NID_PROVINCIA")
    private BigDecimal nidProvincia;

    @Column(name = "NID_DISTRITO")
    private BigDecimal nidDistrito;

    @Column(name = "MIGRADO")
    private Integer migrado;

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

    @Size(max = 2)
    @Column(name = "PIM")
    private String pim;
   
    @Size(max = 2)
    @Column(name = "PIM2019")
    private String pim2019;
   
    @Column(name = "DEF_F")
    private Short defF;
   
    @Column(name = "DEF_M")
    private Short defM;
   
    @Column(name = "PROMDEF_F")
    private Short promdefF;
   
    @Column(name = "PROMDEF_M")
    private Short promdefM;
   
    @Column(name = "OTROS_F")
    private Short otrosF;
   
    @Column(name = "OTROS_M")
    private Short otrosM;
   
    @Column(name = "LATITUD")
    private String latitud;
   
    @Column(name = "LONGITUD")
    private String longitud;
    
    @Size(max = 1)
    @Column(name = "ESTADO_REGISTRO")
    private String estadoRegistro;
    
    @Column(name = "FEC_INSCRIPCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInscripcion;
    
    @Column(name = "FEC_ACREDITACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAcreditacion;    
    
    @Column(name = "FEC_ORDENANZA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecOrdenanza;    
   
    @Column(name = "TXT_NRO_ORDENANZA")
    private String txtNroOrdenanza;    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "defensoria", fetch = FetchType.LAZY)
    private List<DefensoriaPersona> listaPersonaDna;
    
    public Defensoria() {
        this.origen = new Catalogo();
        this.estado = new Catalogo();
        this.defensoriaInfo = new DefensoriaInfo();
        this.listaPersonaDna = new ArrayList<>();
    }
   
    
    
    /**
     * Devuelve el id de la DNA
     * @return el id de la DNA
     */
    public BigInteger getNidDna() {
        return nidDna;
    }

    /**
     * Coloca el id de la DNA
     * @param nidDna el id de la DNA
     */
    public void setNidDna(BigInteger nidDna) {
        this.nidDna = nidDna;
    }

    /**
     * Devuelve la constancia de la DNA
     * @return la constancia de la DNA
     */
    public String getTxtConstancia() {
        return txtConstancia;
    }

    /**
     * Coloca la constancia de la DNA
     * @param txtConstancia la constancia de la DNA
     */
    public void setTxtConstancia(String txtConstancia) {
        this.txtConstancia = txtConstancia;
    }

    /**
     * Devuelve el origen de la DNA. Catálogo: ORIGEN DE DNA
     * @return el catálogo del origen
     */
    public Catalogo getOrigen() {
        return origen;
    }

    /**
     * Coloca el origen de la DNA. Catálogo: ORIGEN DE DNA
     * @param origen el catálogo del origen
     */
    public void setOrigen(Catalogo origen) {
        this.origen = origen;
    }

    /**
     * Devuelve el tipo de la DNA.
     * @return el tipo de la DNA
     */
    public String getTxtTipo() {
        return txtTipo;
    }

    /**
     * Coloca el tipo de la DNA
     * @param txtTipo el tipo de la DNA
     */
    public void setTxtTipo(String txtTipo) {
        this.txtTipo = txtTipo;
    }

    /**
     * Devuelve la entidad de la DNA
     * @return la entidad de la DNA
     */
    public String getTxtEntidad() {
        return txtEntidad;
    }

    /**
     * Coloca la entidad de la DNA
     * @param txtEntidad la entidad de la DNA
     */
    public void setTxtEntidad(String txtEntidad) {
        this.txtEntidad = txtEntidad;
    }

    /**
     * Devuelve el nombre de la DNA
     * @return el nombre de la DNA
     */
    public String getTxtNombre() {
        return txtNombre;
    }

    /**
     * Coloca el nombre de la DNA
     * @param txtNombre el nombre de la DNA
     */
    public void setTxtNombre(String txtNombre) {
        this.txtNombre = txtNombre;
    }

    /**
     * Devuelve el id del departamento de la DNA
     * @return el id del departamento
     */
    public BigDecimal getNidDepartamento() {
        return nidDepartamento;
    }

    /**
     * Coloca el id del departamento de la DNA
     * @param nidDepartamento el id del departamento
     */
    public void setNidDepartamento(BigDecimal nidDepartamento) {
        this.nidDepartamento = nidDepartamento;
    }

    /**
     * Devuelve el id de la provincia de la DNA
     * @return el id de la provincia
     */
    public BigDecimal getNidProvincia() {
        return nidProvincia;
    }

    /**
     * Coloca el id de la provincia de la DNA
     * @param nidProvincia el id de la provincia
     */
    public void setNidProvincia(BigDecimal nidProvincia) {
        this.nidProvincia = nidProvincia;
    }

    /**
     * Devuelve el id del distrito de la DNA
     * @return el id del distrito
     */
    public BigDecimal getNidDistrito() {
        return nidDistrito;
    }

    /**
     * Coloca el id del distrito de la DNA
     * @param nidDistrito el id del distrito
     */
    public void setNidDistrito(BigDecimal nidDistrito) {
        this.nidDistrito = nidDistrito;
    }

   /**
    * Devuelve el estado de la DNA. Catálogo: ESTADO
    * @return el catálogo del estado
    */
    public Catalogo getEstado() {
        return estado;
    }

    /**
     * Coloca el estado de la DNA. Catálogo: ESTADO
     * @param estado el catálogo del estado
     */
    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    /***
     * Devuelve la defensoría central a la que pertenece
     * @return defensoría
     */
    public Defensoria getCentral() {
        return central;
    }

    /**
     * Coloca la defensoría central a la que pertenece
     * @param central la defensoría central
     */
    public void setCentral(Defensoria central) {
        this.central = central;
    }

    /**
     * Devuelve si los datos provienen de migración
     * @return flag de migración
     */
    public Integer getMigrado() {
        return migrado;
    }

    /**
     * Coloca si los datos provienen de migración
     * @param migrado flag de migración
     */
    public void setMigrado(Integer migrado) {
        this.migrado = migrado;
    }

    /**
     * Devuelve el pim del 2019
     * @return el pim del 2019
     */
    public String getPim2019() {
        return pim2019;
    }

    /**
     * Coloca el pim del 2019
     * @param pim2019 el pim del 2019
     */
    public void setPim2019(String pim2019) {
        this.pim2019 = pim2019;
    }

    /** 
     * Devuelve el número de mujeres con función de Defensora
     * @return número de mujeres
     */
    public Short getDefF() {
        return defF;
    }

    /**
     * Coloca el número de mujeres con función de Defensora 
     * @param defF número de mujeres
     */
    public void setDefF(Short defF) {
        this.defF = defF;
    }

    /** 
     * Devuelve el número de hombres con función de Defensor
     * @return número de hombres
     */
    public Short getDefM() {
        return defM;
    }

    /**
     * Coloca el número de hombres con función de Defensor
     * @param defM número de hombres
     */
    
    public void setDefM(Short defM) {
        this.defM = defM;
    }

    /**
     * Devuelve el número de mujeres con función de Promotor
     * @return número de mujeres
     */
    public Short getPromdefF() {
        return promdefF;
    }

    /**
     * Coloca el número de mujeres con función de Promotor
     * @param promdefF número de mujeres
     */
    public void setPromdefF(Short promdefF) {
        this.promdefF = promdefF;
    }

    /**
     * Devuelve el número de hombres con función de Promotor
     * @return número de hombres
     */
    public Short getPromdefM() {
        return promdefM;
    }

    /**
     * Coloca el número de hombres con función de Promotor
     * @param promdefM número de hombres
     */
    public void setPromdefM(Short promdefM) {
        this.promdefM = promdefM;
    }

    /**
     * Devuelve el número de mujeres con otras funciones
     * @return número de mujeres
     */
    public Short getOtrosF() {
        return otrosF;
    }

    /**
     * Coloca el número de mujeres con otras funciones
     * @param otrosF número de mujeres
     */
    public void setOtrosF(Short otrosF) {
        this.otrosF = otrosF;
    }

    /**
     * Devuelve el número de hombres con otras funciones
     * @return número de hombres
     */
    public Short getOtrosM() {
        return otrosM;
    }

    /**
     * Coloca el número de hombres con otras funciones
     * @param otrosM número de hombres
     */
    public void setOtrosM(Short otrosM) {
        this.otrosM = otrosM;
    }

    /**
     * Devuelve el estado del registro
     * @return estado del registro
     */
    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    /**
     * Coloca el estado del registro
     * @param estadoRegistro estado del registro
     */
    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    /**
     * Devuelve la fecha de inscripción
     * @return la fecha de inscripción
     */
    public Date getFecInscripcion() {
        return fecInscripcion;
    }

    /**
     * Coloca la fecha de inscripción
     * @param fecInscripcion fecha de inscripción
     */
    public void setFecInscripcion(Date fecInscripcion) {
        this.fecInscripcion = fecInscripcion;
    }

    /**
     * Devuelve la fecha de acreditación
     * @return la fecha de acreditación
     */
    public Date getFecAcreditacion() {
        return fecAcreditacion;
    }

    /**
     * Coloca la fecha de acreditación
     * @param fecAcreditacion la fecha de acreditación
     */
    public void setFecAcreditacion(Date fecAcreditacion) {
        this.fecAcreditacion = fecAcreditacion;
    }

    /**
     * Devuelve la fecha de ordenanza
     * @return fecha de ordenanza
     */
    public Date getFecOrdenanza() {
        return fecOrdenanza;
    }

    /**
     * Coloca la fecha de la ordenanza
     * @param fecOrdenanza la fecha de la ordenanza
     */
    public void setFecOrdenanza(Date fecOrdenanza) {
        this.fecOrdenanza = fecOrdenanza;
    }

    /**
     * Devuelve el número de la ordenanza
     * @return el número de la ordenanza
     */
    public String getTxtNroOrdenanza() {
        return txtNroOrdenanza;
    }

    /**
     * Coloca el número de la ordenanza
     * @param txtNroOrdenanza número de la ordenanza
     */
    public void setTxtNroOrdenanza(String txtNroOrdenanza) {
        this.txtNroOrdenanza = txtNroOrdenanza;
    }

    /**
     * Devuelve el pim
     * @return el pim
     */
    public String getPim() {
        return pim;
    }

    /**
     * Coloca el pim
     * @param pim el pim
     */
    public void setPim(String pim) {
        this.pim = pim;
    }

    /**
     * Devuelve la latitud de la DNA
     * @return la latitud de la DNA
     */
    public String getLatitud() {
        return latitud;
    }

    /**
     * Coloca la latitud de la DNA
     * @param latitud la latitud de la DNA
     */
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    /**
     * Devuelve la longitud de la DNA
     * @return la longitud de la DNA
     */
    public String getLongitud() {
        return longitud;
    }

    /**
     * Coloca la longitud de la DNA
     * @param longitud la longitud de la DNA
     */
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    
    /**
     * Devuelve el estado ( activo o inactivo ) del registro del curso
     * @return activo (1), inactivo (0)
     */
    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    /**
     * Coloca el valor del estado del registro ( activo o inactivo )
     * @param flgActivo estado activo (1) o inactivo (1)
     */
    public void setFlgActivo(BigInteger flgActivo) {
        this.flgActivo = flgActivo;
    }

    /**
     * Devuelve el nombre del dispositivo que realizó la operación de insert/update
     * @return nombre del dispositivo
     */
    public String getTxtPc() {
        return txtPc;
    }

    /**
     * Coloca  el nombre del dispositivo que realizó la operación de insert/update
     * @param txtPc nombre del dispositivo
     */
    public void setTxtPc(String txtPc) {
        this.txtPc = txtPc;
    }

    /**
     * Devuelve la IP del dispositivo que realizó la operación de insert/update
     * @return IP del dispositivo
     */
    public String getTxtIp() {
        return txtIp;
    }

    /**
     * Coloca la IP del dispositivo que realizó la operación de insert/update
     * @param txtIp la IP del dispositivo
     */
    public void setTxtIp(String txtIp) {
        this.txtIp = txtIp;
    }

    /**
     * Devuelve la fecha y hora del insert del curso
     * @return Fecha y hora del insert
     */
    public Date getFecRegistro() {
        return fecRegistro;
    }

    /**
     * Coloca la fecha y hora del insert del curso
     * @param fecRegistro   Fecha y hora del insert 
     */
    public void setFecRegistro(Date fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    /**
     * Devuelve la fecha y hora del update del curso
     * @return Fecha y hora del update
     */
    public Date getFecModificacion() {
        return fecModificacion;
    }

    /**
     * Coloca la fecha y hora del update del curso
     * @param fecModificacion   Fecha y hora del update 
     */
    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    /**
     * Devuelve el id del usuario que realizó el update
     * @return Id del usuario
     */
    public BigDecimal getNidUsuarioMod() {
        return nidUsuarioMod;
    }

    /**
     * Coloca el id del usuario que realizó el update
     * @param nidUsuarioMod Id del usuario
     */
    public void setNidUsuarioMod(BigDecimal nidUsuarioMod) {
        this.nidUsuarioMod = nidUsuarioMod;
    }

    /**
     * Devuelve el Id del usuario que realizó el insert
     * @return Id del usuario
     */
    public BigDecimal getNidUsuarioReg() {
        return nidUsuarioReg;
    }

    /**
     * Coloca el Id del usuario que realizó el insert
     * @param nidUsuarioReg Id del usuario
     */
    public void setNidUsuarioReg(BigDecimal nidUsuarioReg) {
        this.nidUsuarioReg = nidUsuarioReg;
    }

    public DefensoriaInfo getDefensoriaInfo() {
        return defensoriaInfo;
    }

    public void setDefensoriaInfo(DefensoriaInfo defensoriaInfo) {
        this.defensoriaInfo = defensoriaInfo;
    }
    
    public List<DefensoriaPersona> getListaPersonaDna() {
        return listaPersonaDna;
    }

    public void setListaPersonaDna(List<DefensoriaPersona> listaPersonaDna) {
        this.listaPersonaDna = listaPersonaDna;
    }
    
    /**
     * Devuelve la persona responsable de la DNA. Catálogo Responsable (nid_catalogo = 120) y el flgActivo = 1
     * @return el responsable
     */
    public DefensoriaPersona getResponsable() {
        BigInteger CATALOGO_RESPONSABLE = BigInteger.valueOf(120);
        for(DefensoriaPersona d : this.listaPersonaDna) {
            if(d.getFuncion().getNidCatalogo().compareTo(CATALOGO_RESPONSABLE)==0 && d.getFlgActivo().compareTo(BigInteger.ONE)==0)
                return d;
        }
        return new DefensoriaPersona();
    }
}
