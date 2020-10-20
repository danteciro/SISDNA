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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Clase: CursoPonderacion.java<br>
 * Entidad que registra las ponderaciones usadas para la evaluación de los cursos. <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Curso
 * @see Catalogo
 */
@Entity
@Table(name = "CURSO_PONDERACION")
@XmlRootElement
public class CursoPonderacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO_PON", sequenceName = "SQ_CURSO_PON", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO_PON")
    @Column(name = "NID_PONDERACION")
    private BigDecimal nidPonderacion;
   
    @JoinColumn(name="NID_CURSO", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Curso curso;
    
    @JoinColumn(name = "NID_CATALOGO", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Catalogo catalogo;
    
    @Column(name = "PONDERACION")
    private BigDecimal ponderacion;
    
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoPonderacion", fetch = FetchType.LAZY)
    private List<CursoEvaluacion> listaEvaluacion;
  
    
    /**
     * Devuelve el id de la ponderación
     * @return el id de la ponderación
     */
    public BigDecimal getNidPonderacion() {
        return nidPonderacion;
    }

    /**
     * Coloca el id de la ponderación
     * @param nidPonderacion  el id de la ponderación
     */
    public void setNidPonderacion(BigDecimal nidPonderacion) {
        this.nidPonderacion = nidPonderacion;
    }

    /**
     * Devuelve el curso al que pertenece
     * @return curso
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * Coloca el curso al que pertenece
     * @param curso curso al que pertenece
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /**
     * Devuelve el catálogo de las ponderaciones. Catálogo CURSO: PONDERACIONES
     * @return el catálogo de la ponderación
     */
    public Catalogo getCatalogo() {
        return catalogo;
    }

    /**
     * Coloca el catálogo de las ponderaciones. Catálogo CURSO: PONDERACIONES
     * @param catalogo catálogo de la ponderación
     */
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Devuelve la ponderación utilizada para el cálculo de los puntajes
     * @return la ponderación
     */
    public BigDecimal getPonderacion() {
        return ponderacion;
    }

    /**
     * Coloca la ponderación utilizada para el cálculo de los puntajes
     * @param ponderacion ponderación de los puntajes
     */
    public void setPonderacion(BigDecimal ponderacion) {
        this.ponderacion = ponderacion;
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
     * Devuelve la lista de evaluaciones de cursos que utilizan la ponderación
     * @return lista de evaluaciones
     */
    public List<CursoEvaluacion> getListaEvaluacion() {
        return listaEvaluacion;
    }

    /**
     * Coloca la lista de evaluaciones de cursos que utilizan la ponderación 
     * @param listaEvaluacion lista de evaluaciones
     */
    public void setListaEvaluacion(List<CursoEvaluacion> listaEvaluacion) {
        this.listaEvaluacion = listaEvaluacion;
    }

  
}
