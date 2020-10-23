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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase: CursoInscripcionReq.java<br>
 * 
 * Entidad que registra los requisitos cargados en la solicitud de inscripción. <br>
 * Fecha de Creación: 20/09/2019<br>
 * @author BooleanCore
 * @see Curso
 * @see CursoInscripcion
 * @see CursoPrereq
 */
@Entity
@Table(name = "CURSO_INSCRIPCION_REQ")
@XmlRootElement
public class CursoInscripcionReq implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO_INSREQ", sequenceName = "SQ_CURSO_INSREQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO_INSREQ")
    @Column(name = "NID_INSREQ")
    private BigDecimal nidInsreq;

    @JoinColumn(name="NID_INSCRIPCION", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private CursoInscripcion cursoInscripcion;

    @JoinColumn(name="NID_REQ", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private CursoReq cursoReq;
    
    @Lob
    @Column(name = "ARCHIVO", columnDefinition="BLOB")
    private byte[] archivo;

    @Size(max = 150)
    @Column(name = "ARCHIVO_NOMBRE")
    private String archivoNombre;
   
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


    /**
     * Devuelve el id del requisito
     * @return id del requisito
     */
    public BigDecimal getNidInsreq() {
        return nidInsreq;
    }

    /**
     * Coloca el id del requisito
     * @param nidInsreq el id del requisito
     */
    public void setNidInsreq(BigDecimal nidInsreq) {
        this.nidInsreq = nidInsreq;
    }

    /**
     * Devuelve la solicitud de inscripción del participante
     * @return CursoInscripcion inscripción al que pertenece
     */
    public CursoInscripcion getCursoInscripcion() {
        return cursoInscripcion;
    }

     /**
     * Coloca la solicitud de inscripción del participante
     * @param cursoInscripcion la inscripción al que pertenece
     */
    public void setCursoInscripcion(CursoInscripcion cursoInscripcion) {
        this.cursoInscripcion = cursoInscripcion;
    }

     /**
     * Devuelve el requisito de la solicitud de inscripción
     * @return requisito del curso
     */
    public CursoReq getCursoReq() {
        return cursoReq;
    }

     /**
     * Coloca el requisito de la solicitud de inscripción
     * @param cursoReq  requisito del curso
     */
    public void setCursoReq(CursoReq cursoReq) {
        this.cursoReq = cursoReq;
    }

    /**
     * Devuelve el archivo en bytes del requisito
     * @return archivo en bytes
     */
    public byte[] getArchivo() {
        return archivo;
    }

    /**
     * Coloca el archivo en bytes del requisito
     * @param archivo en bytes
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
     * @param fecRegistro Fecha y hora del insert 
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
     * @param fecModificacion Fecha y hora del update 
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
