package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase: PersonaPre.java<br>
 * Entidad que registra las personas que se inscriben el curso.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Catalogo
 */
@Entity
@Table(name = "PERSONA")
@XmlRootElement
public class PersonaPre implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_PERSONA", sequenceName = "SQ_PERSONA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PERSONA")
    @Column(name = "NID_PERSONA")
    private BigDecimal nidPersona;
   
    @JoinColumn(name="NID_DNA", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Defensoria defensoria;
   
    @JoinColumn(name="NID_FUNCION", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Catalogo funcion;
   
    @NotNull
    @Column(name = "NID_TIPO_DOC")
    private BigDecimal nidTipoDoc;
    
    @NotNull
    @Size(max = 20)
    @Column(name = "NRO_DOC")
    private String nroDoc;
    
    @NotNull
    @Size(max = 150)
    @Column(name = "NOMBRES")
    private String nombres;
    
    @NotNull
    @Size(max = 150)
    @Column(name = "APE_PAT")
    private String apePaterno;
    
    @NotNull
    @Size(max = 150)
    @Column(name = "APE_MAT")
    private String apeMaterno;
    
    @Size(max = 1)
    @Column(name = "SEXO")
    private String sexo;
    
    @Size(max = 50)
    @Column(name = "TELEFONO")
    private String telefono;
    
    @Size(max = 50)
    @Column(name = "CORREO")
    private String correo;
    
    
    @Column(name = "NID_DEPARTAMENTO")
    private BigDecimal nidDepartamento;
    
    @Column(name = "NID_PROVINCIA")
    private BigDecimal nidProvincia;
    
    @Column(name = "NID_DISTRITO")
    private BigDecimal nidDistrito;
    
    
    @NotNull
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

    public PersonaPre() {
        this.funcion = new Catalogo();
    }

    
    
    /**
     * Devuelve el id de la persona
     * @return el id de la persona
     */
    public BigDecimal getNidPersona() {
        return nidPersona;
    }

    /**
     * Coloca el id de la persona
     * @param nidPersona el id de la persona
     */
    public void setNidPersona(BigDecimal nidPersona) {
        this.nidPersona = nidPersona;
    }

    /**
     * Devuelve el catálogo de la función que desempeña. Catálogo: FUNCIÓN DE PERSONAL DE DNA
     * @return catálogo de la función
     */
    public Catalogo getFuncion() {
        return funcion;
    }

    /**
     * Coloca el catálogo de la función que desempeña. Catálogo: FUNCIÓN DE PERSONAL DE DNA 
     * @param funcion catálogo de la función
     */
    public void setFuncion(Catalogo funcion) {
        this.funcion = funcion;
    }

    /**
     * Devuelve la defensoría a la que pertenece
     * @return la defensoría
     */
    public Defensoria getDefensoria() {
        return defensoria;
    }

    /**
     * Coloca la defensoría a la que pertenece
     * @param defensoria defensoría a la que pertenece
     */
    public void setDefensoria(Defensoria defensoria) {
        this.defensoria = defensoria;
    }

    /**
     * Devuelve el id del tipo de documento
     * @return el id del tipo de documento
     */
    public BigDecimal getNidTipoDoc() {
        return nidTipoDoc;
    }

    /**
     * Coloca el id del tipo de documento
     * @param nidTipoDoc el id del tipo de documento
     */
    public void setNidTipoDoc(BigDecimal nidTipoDoc) {
        this.nidTipoDoc = nidTipoDoc;
    }

    /**
     * Devuelve el número de documento
     * @return número de documento
     */
    public String getNroDoc() {
        return nroDoc;
    }

    /**
     * Coloca el número de documento
     * @param nroDoc número de documento
     */
    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    /**
     * Devuelve los nombre de la persona
     * @return nombres de la persona
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Coloca los nombres de la persona
     * @param nombres nombres de la persona
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Devuelve el apellido paterno de la persona
     * @return el apellido paterno
     */
    public String getApePaterno() {
        return apePaterno;
    }

    /**
     * Coloca el apellido paterno de la persona
     * @param apePaterno el apellido paterno
     */
    public void setApePaterno(String apePaterno) {
        this.apePaterno = apePaterno;
    }

    /**
     * Devuelve el apellido materno
     * @return el apellido materno
     */
    public String getApeMaterno() {
        return apeMaterno;
    }

    /**
     * Coloca el apellido materno
     * @param apeMaterno el apellido materno
     */
    public void setApeMaterno(String apeMaterno) {
        this.apeMaterno = apeMaterno;
    }

    /**
     * Devuelve el sexo de la persona
     * @return sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Coloca el sexo de la persona
     * @param sexo sexo de la persona
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * Devuelve el teléfono de la persona
     * @return teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Coloca el teléfono de la persona
     * @param telefono teléfono de la persona
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve el correo electrónico de contacto
     * @return correo electrónico
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Coloca el correo electrónico de contacto
     * @param correo correo electrónico
     */
    public void setCorreo(String correo) {
        this.correo = correo;
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
     * @param nidDepartamento el id de departamento
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
     * Devuelve el estado ( activo o inactivo ) del registro del curso
     * @return activo (1), inactivo (0)
     */
    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    /**
     * Coloca el valor del estado del registro ( activo o inactivo )
     * @param flgActivo - estado activo (1) o inactivo (1)
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

   /**
    * Devuelve los nombres y apellidos con formato: nombre apellido paterno apellido materno
    * @return nombres y apellidos
    */
    public String getNombreApellidos() {
         return this.nombres + " " + this.apePaterno + " " + this.apeMaterno;
    }
    
   /**
    * Devuelve los nombres y apellidos con formato: nombre, apellido paterno apellido materno
    * @return nombres y apellidos
    */
    public String getNombreApellidosSep() {
         return this.nombres + ", " + this.apePaterno + " " + this.apeMaterno;
    }
    
   /**
    * Devuelve los apellidos y nombres con formato: APELLIDOS, Nombres
    * @return apellidos y nombres
    */
    public String getApellidosNombres() {
         return this.apePaterno.toUpperCase() + " " + this.apeMaterno.toUpperCase() + ", " + this.nombres;
    }
}
