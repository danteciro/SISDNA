package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * Clase: Catalogo.java <br>
 * Entidad que registra la información de los cursos que dicta la DSLD <br>
 * Fecha de Creación: 20/11/2018<br>
 * 
 * Los catálogos principales son:<br>
 * ORIGEN DE DNA<br>
 * FUNCIÓN DE PERSONAL DE DNA<br>
 * ESTADO<br>
 * DOCUMENTOS DE INSCRIPCIÓN<br>
 * DOCUMENTOS DE ACREDITACIÓN<br>
 * GRADO DE INSTRUCCIÓN<br>
 * FUNCION EQUIPO ACREDITADO<br>
 * OCUPACIONES<br>
 * CURSO: PONDERACIONES<br>
 * CURSO: DOCUMENTACIÓN<br>
 * CURSO: REQUISITOS<br>
 * SUPERVISIÓN: ESTADOS<br>
 * SUPERVISIÓN: ESTADOS DE CONSERVACIÓN<br>
 * SUPERVISIÓN: OBS. RESPECTO A LA ENTIDAD RESPONSABLE<br>
 * SUPERVISIÓN: OBS. RESPECTO A LA DEFENSORÍA DE LA NIÑA, NIÑO Y ADOLESCENTE<br>
 * SUPERVISIÓN: OBS. RESPECTO A LOS INTEGRANTES DEL SERVICIO DNA<br>
 * 
 * @author BooleanCore
 * @see CursoReq
 */
@Entity
@Table(name = "CATALOGO")
public class Catalogo implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id
    @SequenceGenerator(name = "SQ_CATALOGO", sequenceName = "SQ_CATALOGO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CATALOGO")
    @Column(name = "NID_CATALOGO")
    private BigInteger nidCatalogo;

    @Column(name = "NID_PADRE")
    private BigInteger nidPadre;
   
    @Size(max = 250)
    @Column(name = "TXT_NOMBRE")
    private String txtNombre;

    @Column(name = "VALOR_NUM")
    private BigDecimal valorNum;

    @Lob
    @Column(name = "ARCHIVO", columnDefinition="BLOB")
    private byte[] archivo;
   
    @Size(max = 150)
    @Column(name = "ARCHIVO_NOMBRE")
    private String archivoNombre;

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
    
    public Catalogo(){
        this.txtNombre = "";
    }
    
    /**
     * Devuelve el id del catálogo
     * @return el id del catálogo
     */
    public BigInteger getNidCatalogo() {
        return nidCatalogo;
    }

    /**
     * Coloca el id del catálogo
     * @param nidCatalogo el id del catálogo
     */
    public void setNidCatalogo(BigInteger nidCatalogo) {
        this.nidCatalogo = nidCatalogo;
    }

    /**
     * Devuelve el nombre del catálogo
     * @return el nombre del catálogo
     */
    public String getTxtNombre() {
        return txtNombre;
    }

    /**
     * Coloca el nombre del catálogo
     * @param txtNombre el nombre del catálogo
     */
    public void setTxtNombre(String txtNombre) {
        this.txtNombre = txtNombre;
    }

   /**
    * Devuelve el id del catálogo al que pertenece
    * @return el id del catálogo padre
    */
    public BigInteger getNidPadre() {
        return nidPadre;
    }

    /**
     * Coloca el id del catálogo al que pertenece
     * @param nidPadre el id del catálogo padre
     */
    public void setNidPadre(BigInteger nidPadre) {
        this.nidPadre = nidPadre;
    }

    /**
     * Devuelve el valor numérico del catálogo
     * @return el valor numérico
     */
    public BigDecimal getValorNum() {
        return valorNum;
    }

    /**
     * Coloca el valor numérico del catálogo
     * @param valorNum valor numérico
     */
    public void setValorNum(BigDecimal valorNum) {
        this.valorNum = valorNum;
    }

    /**
     * Devuelve el archivo en bytes del catálogo
     * @return el archivo en bytes
     */
    public byte[] getArchivo() {
        return archivo;
    }

    /**
     * Coloca el archivo en bytes del catálogo
     * @param archivo archivo en bytes
     */
    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
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
    
    
}
