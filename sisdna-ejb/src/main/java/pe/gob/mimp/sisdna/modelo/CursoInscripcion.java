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
import javax.persistence.Lob;
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
 * Clase: CursoInscripcion.java<br>
 * Entidad que registra las solicitudes de inscripción en sus diferentes estados. <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Curso 
 * @see PersonaPre
 */
@Entity
@Table(name = "CURSO_INSCRIPCION")
@XmlRootElement
public class CursoInscripcion implements Serializable {

    private static final long serialVersionUID = 1L;
   
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO_INS", sequenceName = "SQ_CURSO_INS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO_INS")
    @Column(name = "NID_INSCRIPCION")
    private BigDecimal nidInscripcion;
   
    @JoinColumn(name="NID_CURSO_PROG", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private CursoProgramacion cursoProgramacion;
   
    @JoinColumn(name="NID_PERSONA", nullable = true)
    @ManyToOne(fetch=FetchType.LAZY)
    private PersonaPre persona;
    
    @Lob
    @Column(name = "ARCHIVO_OFICIO")
    private byte[] archivoOficio;
   
    @Size(max = 2000)
    @Column(name = "OBS_OBSERVA")
    private String obsObserva;
    
    @Size(max = 2000)
    @Column(name = "OBS_RECHAZO")
    private String obsRechazo;
    
    @Lob
    @Column(name = "ARCHIVO_OBS")
    private byte[] archivoObs;
    
    @Lob
    @Column(name = "ARCHIVO_REC")
    private byte[] archivoRechazo;
    
    @Size(max = 150)
    @Column(name = "ARCHIVO_NOM_OF")
    private String archivoNombreOficio;
   
    @Size(max = 150)
    @Column(name = "ARCHIVO_NOM_REC")
    private String archivoNombreRechazo;
   
    @Size(max = 150)
    @Column(name = "ARCHIVO_NOM_OBS")
    private String archivoNombreObservacion;
   
    
    @Column(name = "FEC_EVALUACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEvaluacion;
    
    @Column(name = "FEC_OBS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecObservacion;
    
           
    @Column(name = "ESTADO")
    private Byte estado;
    
    @Column(name = "ESTADO_EVAL")
    private Byte estadoEval;
    
    @Column(name = "NOTA_EVAL")
    private BigDecimal notaEval;
    
    @Lob
    @Column(name = "ARCHIVO_CERT", columnDefinition="BLOB")
    private byte[] archivoCert;
 
    @Size(max = 200)
    @Column(name = "ANT_PNP")
    private String antPnp;
   
    @Size(max = 200)
    @Column(name = "ANT_INPE")
    private String antInpe;
   
    @Size(max = 200)
    @Column(name = "ANT_PJ")
    private String antPj;
   
    @Column(name = "FLG_EXTERNO")
    private Byte flgExterno;
   
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
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoInscripcion", fetch = FetchType.LAZY)
    private List<CursoInscripcionPrereq> listaInscripcionPrereq;
   
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoInscripcion", fetch = FetchType.LAZY)
    private List<CursoInscripcionReq> listaInscripcionReq;
   
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoInscripcion", fetch = FetchType.LAZY)
    private List<CursoEvaluacion> listaEvaluacion;


  
    /**
     * Devuelve el id de la inscripción
     * @return el id de la inscripción
     */
    public BigDecimal getNidInscripcion() {
        return nidInscripcion;
    }

    /**
     * Coloca el id de la inscripción
     * @param nidInscripcion el id de la inscripción
     */
    public void setNidInscripcion(BigDecimal nidInscripcion) {
        this.nidInscripcion = nidInscripcion;
    }

    /**
     * Devuelve la programación del curso al que se inscribió el participante
     * @return la programación del curso
     */
    public CursoProgramacion getCursoProgramacion() {
        return cursoProgramacion;
    }

    /**
     * Coloca la programación del curso  al que se inscribió el participante
     * @param cursoProgramacion la programación del curso
     */
    public void setCursoProgramacion(CursoProgramacion cursoProgramacion) {
        this.cursoProgramacion = cursoProgramacion;
    }

    /**
     * Devuelve el texto cuando se observa la solicitud de inscripción
     * @return el texto de observaciones
     */
    public String getObsObserva() {
        return obsObserva;
    }

    /**
     * Coloca el texto cuando se observa la solicitud de inscripción
     * @param obsObserva  el texto de observaciones
     */
    public void setObsObserva(String obsObserva) {
        this.obsObserva = obsObserva;
    }

    /**
     * Devuelve el texto cuando se rechaza la solicitud de inscripción
     * @return el texto de observaciones
     */
    public String getObsRechazo() {
        return obsRechazo;
    }

    /**
     * Coloca el texto cuando se rechaza la solicitud de inscripción
     * @param obsRechazo el texto de observaciones
     */
    public void setObsRechazo(String obsRechazo) {
        this.obsRechazo = obsRechazo;
    }

    /**
     * Devuelve el archivo en bytes de la observación de la solicitud de inscripción
     * @return archivo en bytes
     */
    public byte[] getArchivoObs() {
        return archivoObs;
    }

    /**
     * Coloca el archivo en bytes de la observación de la solicitud de inscripción
     * @param archivoObs archivo en bytes
     */
    public void setArchivoObs(byte[] archivoObs) {
        this.archivoObs = archivoObs;
    }

    /**
     *  Devuelve el archivo en bytes del rechazo de la solicitud de inscripción
     * @return  archivo en bytes
     */
    public byte[] getArchivoRechazo() {
        return archivoRechazo;
    }

    /**
     * Coloca el archivo en bytes del rechazo de la solicitud de inscripción
     * @param archivoRechazo archivo en bytes
     */
    public void setArchivoRechazo(byte[] archivoRechazo) {
        this.archivoRechazo = archivoRechazo;
    }

    public String getArchivoNombreOficio() {
        return archivoNombreOficio;
    }

    public void setArchivoNombreOficio(String archivoNombreOficio) {
        this.archivoNombreOficio = archivoNombreOficio;
    }

    public String getArchivoNombreRechazo() {
        return archivoNombreRechazo;
    }

    public void setArchivoNombreRechazo(String archivoNombreRechazo) {
        this.archivoNombreRechazo = archivoNombreRechazo;
    }

    public String getArchivoNombreObservacion() {
        return archivoNombreObservacion;
    }

    public void setArchivoNombreObservacion(String archivoNombreObservacion) {
        this.archivoNombreObservacion = archivoNombreObservacion;
    }

    public Date getFecObservacion() {
        return fecObservacion;
    }

    public void setFecObservacion(Date fecObservacion) {
        this.fecObservacion = fecObservacion;
    }

  
    /**
     * Devuelve estado de la solicitud de inscripción: Preinscrito (1), Observado (2), Rechazado (3), Inscrito (4)
     * @return el estado de la solicitud
     */
     public Byte getEstado() {
        return estado;
    }

    /**
     * Coloca el estado de la solicitud de inscripción: Preinscrito (1), Observado (2), Rechazado (3), Inscrito (4)
     * @param estado de la solicitud
     */ 
    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el estado de la evaluación del inscrito: Pendiente (0), Aprobado (1), Desaprobado (2)
     * @return estado de la evaluación
     */
   public Byte getEstadoEval() {
        return estadoEval;
    }

   /**
    * Coloca el estado de la evaluación del inscrito: Pendiente (0), Aprobado (1), Desaprobado (2)
    * @param estadoEval  estado de la evaluación
    */
    public void setEstadoEval(Byte estadoEval) {
        this.estadoEval = estadoEval;
    }
    
    /**
     * Devuelve la nota final del inscrito
     * @return nota final
     */
    public BigDecimal getNotaEval() {
        return notaEval;
    }

    /**
     * Coloca la nota final del inscrito
     * @param notaEval nota final
     */
    public void setNotaEval(BigDecimal notaEval) {
        this.notaEval = notaEval;
    }

    /**
     * Devuelve la persona inscrita en el curso
     * @return persona inscrita
     */
    public PersonaPre getPersona() {
        return persona;
    }

    /**
     * Coloca la persona inscrita en el curso
     * @param persona persona inscrita en el curso
     */
    public void setPersona(PersonaPre persona) {
        this.persona = persona;
    }

    /**
     * Devuelve el archivo en bytes del certificado
     * @return el archivo en bytes
     */
    public byte[] getArchivoCert() {
        return archivoCert;
    }

    /**
     * Coloca el archivo en bytes del certificado
     * @param archivoCert archivo en bytes
     */
    public void setArchivoCert(byte[] archivoCert) {
        this.archivoCert = archivoCert;
    }

    /**
     * Devuelve los antecedentes policiales obtenidos a través de la PIDE
     * @return antecedentes policiales
     */
    public String getAntPnp() {
        return antPnp;
    }

    /**
     * Coloca los antecedentes policiales obtenidos a través de la PIDE
     * @param antPnp antecedentes policitales
     */
    public void setAntPnp(String antPnp) {
        this.antPnp = antPnp;
    }

    /**
     * Devuelve los antecedentes judiciales obtenidos a través de la PIDE
     * @return antecedentes judiciales
     */
    public String getAntInpe() {
        return antInpe;
    }

    /**
     * Coloca los antecedentes judiciales obtenidos a través de la PIDE
     * @param antInpe antecedentes judiciales
     */
    public void setAntInpe(String antInpe) {
        this.antInpe = antInpe;
    }

    /**
     * Devuelve los antecedentes penales obtenidos a través de la PIDE
     * @return antecedentes penales
     */
    public String getAntPj() {
        return antPj;
    }

    /**
     * Coloca los antecedentes penales obtenidos a través de la PIDE
     * @param antPj antecedentes penales
     */
    public void setAntPj(String antPj) {
        this.antPj = antPj;
    }

    /**
     * Devuelve la fecha de la evaluación
     * @return la fecha de la evaluación
     */
    public Date getFecEvaluacion() {
        return fecEvaluacion;
    }

    /**
     * Coloca la fecha de evaluación
     * @param fecEvaluacion la fecha de evaluación
     */
    public void setFecEvaluacion(Date fecEvaluacion) {
        this.fecEvaluacion = fecEvaluacion;
    }

    /**
     * Devuelve el archivo en bytes del oficio de aprobación de solicitud de inscripción
     * @return archivo en bytes
     */
    public byte[] getArchivoOficio() {
        return archivoOficio;
    }

    /**
     * Coloca el archivo en bytes del oficio de aprobación de solicitud de inscripción
     * @param archivoOficio archivo en bytes
     */
    public void setArchivoOficio(byte[] archivoOficio) {
        this.archivoOficio = archivoOficio;
    }

    public Byte getFlgExterno() {
        return flgExterno;
    }

    public void setFlgExterno(Byte flgExterno) {
        this.flgExterno = flgExterno;
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
     * Devuelve la lista de requisitos del curso al que se inscribió el participante
     * @return lista de requisitos
     */
    public List<CursoInscripcionReq> getListaInscripcionReq() {
        return listaInscripcionReq;
    }

    /**
     * Colocal la lista de requisitos del curso al que se inscribió el participante
     * @param listaInscripcionReq lista de requisitos
     */
    public void setListaInscripcionReq(List<CursoInscripcionReq> listaInscripcionReq) {
        this.listaInscripcionReq = listaInscripcionReq;
    }

    /**
     * Devuelve las evaluaciones o calificaciones del inscrito
     * @return lista de evaluaciones
     */
    public List<CursoEvaluacion> getListaEvaluacion() {
        return listaEvaluacion;
    }

    /**
     * Coloca la lista de evaluaciones o calificaciones del inscrito
     * @param listaEvaluacion lista de evaluaciones
     */
    public void setListaEvaluacion(List<CursoEvaluacion> listaEvaluacion) {
        this.listaEvaluacion = listaEvaluacion;
    }

    /**
     * Devuelve la lista de curso prerrequisitos del curso al que se inscribió el participante
     * @return lista de prerrequisitos
     */
    public List<CursoInscripcionPrereq> getListaInscripcionPrereq() {
        return listaInscripcionPrereq;
    }

    /**
     * Coloca la lista de curso prerrequisitos del curso al que se inscribió el participante
     * @param listaInscripcionPrereq lista de requisitos
     */
    public void setListaInscripcionPrereq(List<CursoInscripcionPrereq> listaInscripcionPrereq) {
        this.listaInscripcionPrereq = listaInscripcionPrereq;
    }

 
}
