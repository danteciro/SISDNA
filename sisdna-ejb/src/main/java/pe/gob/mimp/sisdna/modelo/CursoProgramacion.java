package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase: CursoProgramacion.java<br>
 * Entidad que registra las programaciones de los cursos. <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Curso
 * @see CursoInscripcion
 */
@Entity
@Table(name = "CURSO_PROGRAMACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicar", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1 and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarDep", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1  and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.nidDepartamento = :nidDepartamento"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarProv", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1  and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.nidProvincia = :nidProvincia"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarDis", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1  and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.nidDistrito = :nidDistrito"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarCurso", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1 and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.curso.nidCurso = :nidCurso"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarDepCurso", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1  and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.nidDepartamento = :nidDepartamento and c.curso.nidCurso = :nidCurso"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarProvCurso", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1  and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.nidProvincia = :nidProvincia and c.curso.nidCurso = :nidCurso"),
    @NamedQuery(name = "CursoProgramacion.cursosParaPublicarDisCurso", query = "SELECT c FROM CursoProgramacion c WHERE c.flgPublicar = 1 and c.flgActivo = 1  and c.fechaIniIns <= CURRENT_DATE  and c.fechaFinIns >= CURRENT_DATE and c.nidDistrito = :nidDistrito and c.curso.nidCurso = :nidCurso")
})
public class CursoProgramacion implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_CURSO_PROG", sequenceName = "SQ_CURSO_PROG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CURSO_PROG")
    @Column(name = "NID_CURSO_PROG")
    private BigDecimal nidCursoProg;
    
    @JoinColumn(name="NID_CURSO", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Curso curso;

    @Column(name = "NID_TUTOR")
    private BigDecimal nidTutor;
    
    @Column(name = "NID_DEPARTAMENTO")
    private BigDecimal nidDepartamento;
  
    @Column(name = "NID_PROVINCIA")
    private BigDecimal nidProvincia;
 
    @Column(name = "NID_DISTRITO")
    private BigDecimal nidDistrito;
    
    @Size(max = 50)
    @Column(name = "CIUDAD")
    private String ciudad;
    
    @Size(max = 150)
    @Column(name = "DIRECCION")
    private String direccion;    
    
    @Column(name = "FECHA_INI_INS")
    @Temporal(TemporalType.DATE)
    private Date fechaIniIns;
    
    @Column(name = "FECHA_FIN_INS")
    @Temporal(TemporalType.DATE)
    private Date fechaFinIns;
    
    @Column(name = "FLG_PUBLICAR")
    private Byte flgPublicar;
    
    @Column(name = "FLG_GENERARD")
    private Byte flgGenerard;
    
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Column(name = "CAPACIDAD")
    private BigInteger capacidad;
    
    @Column(name = "ESTADO")
    private Byte estado;
    
    @Lob
    @Column(name = "ARCHIVO_RD", columnDefinition="BLOB")
    private byte[] archivoRd;
 
    @Size(max = 150)
    @Column(name = "ARCHIVO_NOMBRE")
    private String archivoNombre;
   
    @Size(max = 50)
    @Column(name = "RD_NRO")
    private String rdNro;
    
    @Size(max = 50)
    @Column(name = "RD_INFORME")
    private String rdInforme;
    
    @Column(name = "RD_FECHA")
    @Temporal(TemporalType.DATE)
    private Date rdFecha;
         
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoProgramacion", fetch = FetchType.LAZY)
    private List<CursoInscripcion> listaInscripcion;

    
    /**
     * Devuelve el id de la programación del curso
     * @return el id de la programación
     */
    public BigDecimal getNidCursoProg() {
        return nidCursoProg;
    }

    /**
     * Coloca el id de la programación del curso
     * @param nidCursoProg el id de la programación
     */
    public void setNidCursoProg(BigDecimal nidCursoProg) {
        this.nidCursoProg = nidCursoProg;
    }

    /**
     * Devuelve el curso al que pertenece
     * @return el curso al que pertenece
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
     * Devuelve la fecha de inicio de la programación del curso
     * @return fecha de inicio
     */
    public Date getFechaIni() {
        return fechaIni;
    }

    /**
     * Coloca la fecha de inicio de la programación del curso
     * @param fechaIni fecha de inicio
     */
    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    /**
     * Devuelve la fecha de fin de la programación del curso
     * @return fecha de fin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * Coloca la fecha de fin de la programación del curso
     * @param fechaFin fecha de fin
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Devuelve si la programación será publicada en el portal
     * @return flag de publicación
     */
    public boolean isFlgPublicar() {
        return (this.flgPublicar!=null && this.flgPublicar==1);  
    }

    /**
     * Coloca si la programación será publicada en el portal
     * @param flgPublicar flag de publicación
     */
    public void setFlgPublicar(boolean flgPublicar) {
        this.flgPublicar = (flgPublicar)?(byte)1:(byte)0;
    }

    /**
     * Devuelve si la programación generará Resolución Directoral
     * @return flag de generación de resolución
     */
    public boolean isFlgGenerard() {
        return (this.flgGenerard!=null && this.flgGenerard==1);
    }

    /**
     * Coloca si la programación generará Resolución Directoral
     * @param flgGenerard flag de generación de resolución
     */
    public void setFlgGenerard(boolean flgGenerard) {
        this.flgGenerard = (flgGenerard)?(byte)1:(byte)0;
    }

    /**
     * Devuelve el id del usuario con perfil Tutor
     * @return id del usuario
     */
    public BigDecimal getNidTutor() {
        return nidTutor;
    }

    /**
     * Coloca el id del usuario con perfil Tutor
     * @param nidTutor id del tutor
     */
    public void setNidTutor(BigDecimal nidTutor) {
        this.nidTutor = nidTutor;
    }
   
    /**
     * Devuelve el id del departamento donde se llevará a cabo el curso
     * @return el id del departamento
     */
    public BigDecimal getNidDepartamento() {
        return nidDepartamento;
    }

    /**
     * Coloca el id del departamento donde se llevará a cabo el curso
     * @param nidDepartamento el id del departamento
     */
    public void setNidDepartamento(BigDecimal nidDepartamento) {
        this.nidDepartamento = nidDepartamento;
    }

    /**
     * Devuelve el id de la provincia donde se llevará a cabo el curso
     * @return el id de la provincia
     */
    public BigDecimal getNidProvincia() {
        return nidProvincia;
    }

    /**
     * Coloca el id de la provincia donde se llevará a cabo el curso
     * @param nidProvincia id de la provincia
     */
    public void setNidProvincia(BigDecimal nidProvincia) {
        this.nidProvincia = nidProvincia;
    }

    /**
     * Devuelve el id del distrito donde se llevará a cabo el curso
     * @return el id del distrito
     */
    public BigDecimal getNidDistrito() {
        return nidDistrito;
    }

    /**
     * Coloca el id del distrito donde se llevará a cabo el curso
     * @param nidDistrito el id del distrito
     */
    public void setNidDistrito(BigDecimal nidDistrito) {
        this.nidDistrito = nidDistrito;
    }

    /**
     * Devuelve la dirección donde se llevará a cabo el curso
     * @return dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Coloca la dirección donde se llevará a cabo el curso
     * @param direccion dirección del curso
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve la fecha de inicio de la inscripción del curso
     * @return fecha de inicio
     */
    public Date getFechaIniIns() {
        return fechaIniIns;
    }

    /**
     * Coloca la fecha de inicio de la inscripción del curso
     * @param fechaIniIns fecha de inicio
     */
    public void setFechaIniIns(Date fechaIniIns) {
        this.fechaIniIns = fechaIniIns;
    }

    /**
     * Devuelve la fecha de fin de la inscripción del curso
     * @return fecha de fin
     */
    public Date getFechaFinIns() {
        return fechaFinIns;
    }

    /**
     * Coloca la fecha de fin de la inscripción del curso
     * @param fechaFinIns fecha de fin
     */
    public void setFechaFinIns(Date fechaFinIns) {
        this.fechaFinIns = fechaFinIns;
    }

    /**
     * Devuelve la capacidad del curso
     * @return capacidad del curso
     */
    public BigInteger getCapacidad() {
        return capacidad;
    }

    /**
     * Coloca la capacidad del curso
     * @param capacidad capacidad del curso
     */
    public void setCapacidad(BigInteger capacidad) {
        this.capacidad = capacidad;
    }
    
    /**
     * Devuelve el estado de la programación: (0) Pendiente, (1) Concluido
     * @return estado de la programación
     */
     public Byte getEstado() {
        return estado;
    }

     /**
      * Coloca el estado de la programación: (0) Pendiente, (1) Concluido
      * @param estado estado de la programación
      */
    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el archivo en bytes del archivo de la Resolución Directoral
     * @return archivo en bytes
     */
    public byte[] getArchivoRd() {
        return archivoRd;
    }

    /**
     * Coloca el archivo en bytes del archivo de la Resolución Directoral
     * @param archivoRd archivo en bytes
     */
    public void setArchivoRd(byte[] archivoRd) {
        this.archivoRd = archivoRd;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
    }

    
    /**
     * Devuelve el nombre de la ciudad
     * @return nombre nombre de la ciudad
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Coloca el nombre de la ciudad
     * @param ciudad nombre de la ciudad
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Devuelve el número de la Resolución Directoral
     * @return número de la resolución
     */
    public String getRdNro() {
        return rdNro;
    }

    /**
     * Coloca el número de la Resolución Directoral
     * @param rdNro número de la resolución
     */
    public void setRdNro(String rdNro) {
        this.rdNro = rdNro;
    }

    /**
     * Devuelve el número de informe
     * @return número de informe
     */
    public String getRdInforme() {
        return rdInforme;
    }

    /**
     * Coloca el número de informe
     * @param rdInforme número de informe
     */
    public void setRdInforme(String rdInforme) {
        this.rdInforme = rdInforme;
    }

    /**
     * Devuelve la fecha de la Resolución Directoral
     * @return la fecha de la resolución
     */
    public Date getRdFecha() {
        return rdFecha;
    }

    /**
     * Coloca la fecha de la Resolución Directoral
     * @param rdFecha la fecha de la resolución
     */
    public void setRdFecha(Date rdFecha) {
        this.rdFecha = rdFecha;
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
     * Devuelve la lista de solicitudes de inscripción de la programación del curso
     * @return lista de solicitudes
     */
    public List<CursoInscripcion> getListaInscripcion() {
        return listaInscripcion;
    }

    /**
     * Colocal la lista de solicitudes de inscripción de la programación del curso
     * @param listaInscripcion lista de solicitudes
     */
    public void setListaInscripcion(List<CursoInscripcion> listaInscripcion) {
        this.listaInscripcion = listaInscripcion;
    }

   
    /**
     * Devuelve el detalle de la programación en formato: ciudad - fecha de inicio - fecha de fin
     * @return detalle de la programación
     */
    public String detalleProg() {
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
       String sfechaIni = format.format(this.fechaIni);
       String sfechaFin = format.format(this.fechaFin);
       return this.ciudad + " - " + sfechaIni + " - " + sfechaFin;
    }
    
    
}
