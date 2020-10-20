
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase: Curso.java <br>
 * Entidad que registra la información de los cursos que dicta la DSLD <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see CursoReq
 */
@Entity
@Table(name = "CURSO")
@XmlRootElement
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO", sequenceName = "SQ_CURSO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO")
    @Column(name = "NID_CURSO")
    private BigDecimal nidCurso;
    
    @Size(max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Size(max = 400)
    @Column(name = "OBJETIVO")
    private String objetivo;
    
    @Lob
    @Column(name = "FOTO", columnDefinition="BLOB")
    private byte[] foto;
    
    @Column(name = "NRO_HORAS")
    private Integer nroHoras;
    
    @Size(max = 2000)
    @Column(name = "SUMARIO")
    private String sumario;
    
    @Size(max = 20)
    @Column(name = "SIGLAS")
    private String siglas;
  
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<CursoPrereq> listaPrereq;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<CursoPonderacion> listaPonderacion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<CursoReq> listaReq;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<CursoDocum> listaDocum;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<CursoProgramacion> listacursoProgramacion;
   
  

    /**
     * Devuelve el id del curso
     * @return Id del curso
     */
    public BigDecimal getNidCurso() {
        return nidCurso;
    }

     /**
     * Coloca el id del curso
     * @param nidCurso  Id del curso
     */
    public void setNidCurso(BigDecimal nidCurso) {
        this.nidCurso = nidCurso;
    }

    /**
     * Devuelve el nombre del curso
     * @return el texto del nombre del curso
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Coloca le nombre del curso
     * @param nombre    el texto del nombre del curso
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el objetivo del curso
     * @return Texto del objetivo
     */
    public String getObjetivo() {
        return objetivo;
    }

    /**
     * Coloca el objetivo del curso
     * @param objetivo  El texto del objetivo
     */
    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    /**
     * Devuelve la foto del curso
     * @return array de bytes de la foto
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * Coloca la foto del curso
     * @param foto array de bytes de la foto
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    /**
     * Devuelve el número de horas lectivas del curso
     * @return número de horas
     */
    public Integer getNroHoras() {
        return nroHoras;
    }

    /**
     * Coloca el número de horas lectivas del curso
     * @param nroHoras  Número de horas lectivas
     */
    public void setNroHoras(Integer nroHoras) {
        this.nroHoras = nroHoras;
    }

    /**
     * Devuelve el sumario del curso
     * @return Texto del sumario
     */
    public String getSumario() {
        return sumario;
    }

    /**
     * Coloca el sumario del curso
     * @param sumario Texto del sumario
     */
    public void setSumario(String sumario) {
        this.sumario = sumario;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
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
     * Devuelve la lista de los prerrequisitos del curso
     * @return lista de prerrequisitos
     */
    public List<CursoPrereq> getListaPrereq() {
        return listaPrereq;
    }

    /**
     * Coloca la lista de prerrequisitos
     * @param listaPrereq   Lista de prerrequisitos
     */
    public void setListaPrereq(List<CursoPrereq> listaPrereq) {
        this.listaPrereq = listaPrereq;
    }

    /**
     * Devuelve la lista de ponderaciones ( porcentaje del valor para evaluación ) del curso
     * @return Lista de ponderaciones
     */
    public List<CursoPonderacion> getListaPonderacion() {
        return listaPonderacion;
    }

    /**
     * Coloca la lista de ponderaciones ( porcentaje del valor para evaluación ) del curso
     * @param listaPonderacion Lista de ponderaciones
     */
    public void setListaPonderacion(List<CursoPonderacion> listaPonderacion) {
        this.listaPonderacion = listaPonderacion;
    }

    /**
     * Devuelve la lista de requisitos del curso
     * @return Lista de requisitos
     */
    public List<CursoReq> getListaReq() {
        return listaReq;
    }

    /**
     * Coloca la lista de requisitos del curso
     * @param listaReq Lista de requisitos
     */
    public void setListaReq(List<CursoReq> listaReq) {
        this.listaReq = listaReq;
    }

    /**
     * Devuelve la lista de documentos del curso
     * @return Lista de documentos
     */
    public List<CursoDocum> getListaDocum() {
        return listaDocum;
    }

    /**
     * Colocal la lista de documentos del curso
     * @param listaDocum    Lista de documentos 
     */
    public void setListaDocum(List<CursoDocum> listaDocum) {
        this.listaDocum = listaDocum;
    }

    /**
     * Devuelve la lista de programaciones de los cursos
     * @return  Lista de programaciones
     */
    public List<CursoProgramacion> getListacursoProgramacion() {
        return listacursoProgramacion;
    }

    /**
     * Coloca la lista de programaciones de los cursos
     * @param listacursoProgramacion    Lista de programaciones del curso
     */
    public void setListacursoProgramacion(List<CursoProgramacion> listacursoProgramacion) {
        this.listacursoProgramacion = listacursoProgramacion;
    }

 
}
