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
 * Clase: CursoEvaluacion.java <br>
 * Descripción: Entidad que registra la información de las calificaciones de los inscritos al curso <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Curso
 * @see CursoInscripcion
 * @see CursoPonderacion
 */
@Entity
@Table(name = "CURSO_EVALUACION")
@XmlRootElement
public class CursoEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO_EV", sequenceName = "SQ_CURSO_EV", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO_EV")
    @Column(name = "NID_EVALUACION")
    private BigDecimal nidEvaluacion;
   
    
    @JoinColumn(name = "NID_INSCRIPCION", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private CursoInscripcion cursoInscripcion;
   
    @JoinColumn(name = "NID_PONDERACION", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private CursoPonderacion cursoPonderacion;
   
    @Column(name = "NOTA")
    private Integer nota;
   
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
    * Devuelve la id de la evaluación. 
    * @return id de la evaluación
    */
    public BigDecimal getNidEvaluacion() {
        return nidEvaluacion;
    }

    /**
     * Coloca el id de la evaluación
     * @param nidEvaluacion el id de la evaluación
     */
    public void setNidEvaluacion(BigDecimal nidEvaluacion) {
        this.nidEvaluacion = nidEvaluacion;
    }

    /**
     * Devuelve la inscripción del participante
     * @return CursoInscripcion inscripción al que pertenece
     */
    public CursoInscripcion getCursoInscripcion() {
        return cursoInscripcion;
    }

    /**
     * Coloca el inscripción del participante
     * @param cursoInscripcion la inscripción al que pertenece
     */
    public void setCursoInscripcion(CursoInscripcion cursoInscripcion) {
        this.cursoInscripcion = cursoInscripcion;
    }

    /**
     * Devuelve la ponderación que se utiliza para calcular el puntaje
     * @return la ponderación del curso
     */
    public CursoPonderacion getCursoPonderacion() {
        return cursoPonderacion;
    }

    /**
     * Coloca la ponderación que se utiliza para calcular el puntaje
     * @param cursoPonderacion ponderación del curso
     */
    public void setCursoPonderacion(CursoPonderacion cursoPonderacion) {
        this.cursoPonderacion = cursoPonderacion;
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


    /**
     * Devuelve la nota de la evaluación
     * @return nota obtenida
     */
    public Integer getNota() {
        return nota;
    }

    /**
     * Colcoa la nota de la evaluación
     * @param nota nota obtenida
     */
    public void setNota(Integer nota) {
        this.nota = nota;
    }
    
}
