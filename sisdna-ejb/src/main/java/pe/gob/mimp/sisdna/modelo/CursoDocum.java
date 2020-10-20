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
 * Clase: CursoDocum.java<br>
 * Entidad que guarda los documentos de los cursos que dicta la DSLD<br>
 * 
 * Fecha de Creación: 20/09/2019<br>
 * @author BooleanCore
 * @see Curso
 * @see Catalogo
 */
@Entity
@Table(name = "CURSO_DOCUM")
@XmlRootElement
public class CursoDocum implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO_DOCUM", sequenceName = "SQ_CURSO_DOCUM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO_DOCUM")
    @Column(name = "NID_DOCUM")
    private BigDecimal nidDocum;
    
    @JoinColumn(name="NID_CURSO", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Curso curso;

    @JoinColumn(name = "NID_CATALOGO")
    @ManyToOne(fetch=FetchType.LAZY)
    private Catalogo catalogo;
    
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
     * Devuelve el id del documento del curso
     * @return Id del documento
     */
    public BigDecimal getNidDocum() {
        return nidDocum;
    }

    /**
     * Coloca el id del documento del curso
     * @param nidDocum  id del documento 
     */
    public void setNidDocum(BigDecimal nidDocum) {
        this.nidDocum = nidDocum;
    }

    /**
     * Devuelve el curso al que pertenece el documento
     * @return Curso
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * Coloca el curso al que pertenece el documento
     * @param curso Curso al que pertenece el documento
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /**
     * Devuelve el catálogo del documento. El sistema obtiene la información de catálogo CURSO: DOCUMENTOS.
     * @return Catalogo del documento
     */
    public Catalogo getCatalogo() {
        return catalogo;
    }

    
      /**
     * Coloca el catálogo del documento. El sistema obtiene la información de catálogo CURSO: DOCUMENTOS.
     * @param catalogo del documento
     */
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

  
    /**
     * Devuelve el archivo en bytes de los documentos del curso: brochure, syllabus, etc
     * @return archivo en bytes
     */
    public byte[] getArchivo() {
        return archivo;
    }

    /**
     * Coloca el archivo en bytes de los documentos del curso: brochure, syllabus, etc
     * @param archivo bytes del archivo
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

    
    
}
