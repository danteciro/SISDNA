package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.JoinFetch;
import static org.eclipse.persistence.annotations.JoinFetchType.INNER;

/**
 * Clase: Supervision.java<br>
 * Entidad que registra las supervisiones a las Defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see SupervisionRrhh 
 * @see SupervisionObs
 * @see Defensoria
 * @see Catalogo
 */
@Entity
@Table(name = "SUPERVISION")
@XmlRootElement
public class Supervision implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_SUPERVISION", sequenceName = "SQ_SUPERVISION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SUPERVISION")
    @Column(name = "NID_SUPERVISION")
    private BigInteger nidSupervision;
   
    @JoinColumn(name = "NID_DNA", nullable = false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Defensoria defensoria;
     
    @ManyToOne
    @JoinColumn(name = "NID_ESTADO", nullable = false)
    @JoinFetch(value=INNER)
    private Catalogo estado;
   
    @ManyToOne
    @JoinColumn(name = "NID_ESTADO_CONS", nullable = false)
    @JoinFetch(value=INNER)
    private Catalogo estadoCons;
    
    @JoinColumn(name = "RESP_PROFESION", nullable = false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Catalogo respProfesion;
    
    @Column(name = "NID_SUPERVISOR")
    private BigDecimal nidSupervisor;
   
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
    
    @Size(max = 150)
    @Column(name = "DNA_TELEFONO")
    private String dnaTelefono;
   
    @Size(max = 150)
    @Column(name = "DNA_CORREO")
    private String dnaCorreo;
    
    @Size(max = 150)
    @Column(name = "DNA_HORARIO")
    private String dnaHorario;
    
    @Size(max = 8)
    @Column(name = "RESP_DNI")
    private String respDni;
    
    @Size(max = 50)
    @Column(name = "RESP_APE_PAT")
    private String respApePat;
    
    @Size(max = 50)
    @Column(name = "RESP_APE_MAT")
    private String respApeMat;
    
    @Size(max = 50)
    @Column(name = "RESP_NOMBRE1")
    private String respNombre1;
    
    @Size(max = 50)
    @Column(name = "RESP_NOMBRE2")
    private String respNombre2;
    
    @Size(max = 50)
    @Column(name = "RESP_NOMBRE3")
    private String respNombre3;
    
    @Size(max = 150)
    @Column(name = "RESP_COLEGIO")
    private String respColegio;
  
    @Size(max = 150)
    @Column(name = "RESP_COLEGIATURA")
    private String respColegiatura;
  
    @Size(max = 150)
    @Column(name = "RESP_CORREO")
    private String respCorreo;
  
    @Size(max = 150)
    @Column(name = "RESP_TELEFONO")
    private String respTelefono;
  
    @Column(name = "RESP_FECHA_CARGO")
    @Temporal(TemporalType.DATE)
    private Date respFechaCargo;
    
    
    @Column(name = "TIPO_LOCAL")
    private Byte tipoLocal;
    
    @Column(name = "NRO_AMBIENTES")
    private Byte nroAmbientes;
    
    @Column(name = "PLAN_VIGENTE")
    private Byte planVigente;
    
    @Size(max = 500)
    @Column(name = "ACTIV_PROM")
    private String activProm;
    
    @Column(name = "CUENTA_COMUDENA")
    private Byte cuentaComudena;
    
    @Column(name = "CUENTA_COORD")
    private Byte cuentaCoord;
    
    @Size(max = 500)
    @Column(name = "OTROS_ESPACIOS")
    private String otrosEspacios;
    
    @Size(max = 500)
    @Column(name = "COMENTARIOS")
    private String comentarios;
    
    @Size(max = 50)
    @Column(name = "OTRA_PROFESION")
    private String otraProfesion;

  
    @Size(max = 200)
    @Column(name = "ARCHIVO_NOMBRE")
    private String archivoNombre;
    
    @Lob
    @Column(name = "ARCHIVO_FICHA")
    private byte[] archivoFicha;
   
    
    @Size(max = 150)
    @Column(name = "ENTIDAD_AUT")
    private String entidadAutoridad;

    @Size(max = 150)
    @Column(name = "ENTIDAD_DIR")
    private String entidadDireccion;

    @Size(max = 8)
    @Column(name = "ENT_NRO_DNI")
    private String entNroDni;

    @Size(max = 50)
    @Column(name = "ENT_APE_PAT")
    private String entApePat;

    @Size(max = 50)
    @Column(name = "ENT_APE_MAT")
    private String entApeMat;

    @Size(max = 50)
    @Column(name = "ENT_NOMBRES")
    private String entNombres;

    @Size(max = 50)
    @Column(name = "ENT_CARGO")
    private String entCargo;

    @Column(name = "FLG_ACTIVO")
    private BigInteger flgActivo;
    
    @Column(name = "FLG_NOFUNCIONA")
    private Byte flgNoFunciona;
    
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
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervision", fetch = FetchType.LAZY)
    @OrderBy("nidRrhh")
    private List<SupervisionRrhh> listaRrhh;

 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervision", fetch = FetchType.LAZY)
    @OrderBy("nidObs")
    private List<SupervisionObs> listaObs;

    public Supervision() {
        this.defensoria = new Defensoria();
        this.estado = new Catalogo();
        this.estadoCons = new Catalogo();
        this.respProfesion = new Catalogo();
        this.listaRrhh = new ArrayList<>();
        this.listaObs = new ArrayList<>();
    }


    
    /**
     * Devuelve el id de la supervisión
     * @return el id de la supervisión
     */
    public BigInteger getNidSupervision() {
        return nidSupervision;
    }

    /**
     * Coloca el id de la supervisión
     * @param nidSupervision  el id de la supervision
     */
    public void setNidSupervision(BigInteger nidSupervision) {
        this.nidSupervision = nidSupervision;
    }

    /**
     * Devuelve la defensoría supervisada
     * @return defensoría
     */
    public Defensoria getDefensoria() {
        return defensoria;
    }

    /**
     * Coloca la defensoría supervisada
     * @param defensoria defensoría supervisada
     */
    public void setDefensoria(Defensoria defensoria) {
        this.defensoria = defensoria;
    }

    /**
     * Devuelve el catálogo del estado. Catálogo SUPERVISIONES: ESTADOS
     * @return catálogo del estado
     */
    public Catalogo getEstado() {
        return estado;
    }

    /**
     * Coloca el catálogo del estado. Catálogo SUPERVISIONES: ESTADOS
     * @param estado catálogo del estado
     */
    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }


    /**
     * Devuelve el tipo de local
     * @return tipo de local
     */
    public Byte getTipoLocal() {
        return tipoLocal;
    }

    /**
     * Coloca el tipo de local
     * @param tipoLocal tipo de local
     */
    public void setTipoLocal(Byte tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    /**
     * Devuelve el número de ambientes
     * @return número de ambientes
     */
    public Byte getNroAmbientes() {
        return nroAmbientes;
    }

    /**
     * Coloca el número de ambientes
     * @param nroAmbientes número de ambientes
     */
    public void setNroAmbientes(Byte nroAmbientes) {
        this.nroAmbientes = nroAmbientes;
    }

    /**
     * Devuelve el estado de conservación. Bueno (1), Regular (2), Malo (3)
     * @return estado de la conservación
     */
    public Catalogo getEstadoCons() {
        return estadoCons;
    }

    /**
     * Coloca el estado de conservación. Bueno (1), Regular (2), Malo (3)
     * @param estadoCons estado de la conservación
     */
    public void setEstadoCons(Catalogo estadoCons) {
        this.estadoCons = estadoCons;
    }

    /**
     * Devuelve si cuenta con plan vigente. Sí (1), No (0)
     * @return Sí (1), No (0)
     */
    public Byte getPlanVigente() {
        return planVigente;
    }

    /**
     * Coloca si cuenta con plan vigente. Sí (1), No (0)
     * @param planVigente Sí (1), No (0)
     */
    public void setPlanVigente(Byte planVigente) {
        this.planVigente = planVigente;
    }

    /**
     * Devuelve las actividades de promoción / difusión
     * @return actividades de promoción / difusión
     */
    public String getActivProm() {
        return activProm;
    }

    /**
     * Coloca las actividades de promoción / difusión
     * @param activProm actividades de promoción / difusión
     */
    public void setActivProm(String activProm) {
        this.activProm = activProm;
    }

    /**
   * Devuelve si cuenta con comudena. Sí (1), No (0)
   * @return Sí (1), No (0)
   */
    public Byte getCuentaComudena() {
        return cuentaComudena;
    }

    /**
   * Coloca si cuenta con comudena. Sí (1), No (0)
   * @param cuentaComudena Sí (1), No (0)
   */
    public void setCuentaComudena(Byte cuentaComudena) {
        this.cuentaComudena = cuentaComudena;
    }

    /**
   * Devuelve si cuenta con coordinadora de DNA. Sí (1), No (0)
   * @return Sí (1), No (0)
   */
    public Byte getCuentaCoord() {
        return cuentaCoord;
    }

   /**
   * Coloca si cuenta con coordinadora de DNA. Sí (1), No (0)
   * @param cuentaCoord Sí (1), No (0)
   */
    public void setCuentaCoord(Byte cuentaCoord) {
        this.cuentaCoord = cuentaCoord;
    }

    /**
     * Devuelve otros espacios de coordinación multisectorial participa la DNA
     * @return otros espacios
     */
    public String getOtrosEspacios() {
        return otrosEspacios;
    }

    /**
     * Coloca otros espacios de coordinación multisectorial participa la DNA
     * @param otrosEspacios otros espacios de coordinación
     */
    public void setOtrosEspacios(String otrosEspacios) {
        this.otrosEspacios = otrosEspacios;
    }

    /**
     * Devuelve los comentarios
     * @return los comentarios
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * Coloca los comentarios
     * @param comentarios los comentarios
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
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
     * Devuelve la lista de personas que laboran en la DNA
     * @return lista de personas
     */
    public List<SupervisionRrhh> getListaRrhh() {
        return listaRrhh;
    }

    /**
     * Coloca la lista de personas que laboran en la DNA
     * @param listaRrhh lista de personas
     */
    public void setListaRrhh(List<SupervisionRrhh> listaRrhh) {
        this.listaRrhh = listaRrhh;
    }

    /**
     * Devuelve la lista de observaciones
     * @return lista de observaciones
     */
    public List<SupervisionObs> getListaObs() {
        return listaObs;
    }

    /**
     * Devuelve la lista de observaciones
     * @param listaObs lista de observaciones
     */
    public void setListaObs(List<SupervisionObs> listaObs) {
        this.listaObs = listaObs;
    }

      /**
     * Devuelve el archivo en bytes de la ficha
     * @return el archivo en bytes
     */
    public byte[] getArchivoFicha() {
        return archivoFicha;
    }
      /**
     * Coloca el archivo en bytes de la ficha
     * @param archivoFicha el archivo en bytes
     */
    public void setArchivoFicha(byte[] archivoFicha) {
        this.archivoFicha = archivoFicha;
    }

    /**
     * Devuelve el id del supervisor ( id del usuario con perfil DSLD )
     * @return el id del supervisor
     */
    public BigDecimal getNidSupervisor() {
        return nidSupervisor;
    }

    /**
     * Coloca el id del supervisor ( id del usuario con perfil DSLD )
     * @param nidSupervisor el id del supervisor
     */
    public void setNidSupervisor(BigDecimal nidSupervisor) {
        this.nidSupervisor = nidSupervisor;
    }

    /**
     * Devuelve la fecha de la supervisión
     * @return la fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Coloca la fecha de la supervisión
     * @param fecha la fecha de la supervisión
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve la hora de la supervisión
     * @return la hora
     */
    public Date getHora() {
        return hora;
    }

    /**
     * Coloca la hora de la supervisión
     * @param hora la hora de la supervisión
     */
    public void setHora(Date hora) {
        this.hora = hora;
    }

    /**
     * Devuelve el DNI del entrevistado
     * @return el DNI
     */
    public String getEntNroDni() {
        return entNroDni;
    }

    /**
     * Coloca el DNI del entrevistado
     * @param entNroDni el DNI
     */
    public void setEntNroDni(String entNroDni) {
        this.entNroDni = entNroDni;
    }

    /**
     * Devuelve el apellido paterno del entrevistado
     * @return el apellido paterno
     */
    public String getEntApePat() {
        return entApePat;
    }

    /**
     * Coloca el apellido paterno del entrevistado
     * @param entApePat el apellido paterno
     */
    public void setEntApePat(String entApePat) {
        this.entApePat = entApePat;
    }

    /**
     * Devuelve el apellido materno del entrevistado
     * @return el apellido materno
     */
    public String getEntApeMat() {
        return entApeMat;
    }

    /**
     * Coloca el apellido materno del entrevistado
     * @param entApeMat el apellido materno
     */
    public void setEntApeMat(String entApeMat) {
        this.entApeMat = entApeMat;
    }

    /**
     * Devuelve los nombres del entrevistado
     * @return los nombres
     */
    public String getEntNombres() {
        return entNombres;
    }

    /**
     * Coloca los nombres del entrevistado
     * @param entNombres los nombres
     */
    public void setEntNombres(String entNombres) {
        this.entNombres = entNombres;
    }

    /**
     * Devuelve el cargo del entrevistado
     * @return el cargo
     */
    public String getEntCargo() {
        return entCargo;
    }

    /**
     * Coloca el cargo del entrevistado
     * @param entCargo el cargo 
     */
    public void setEntCargo(String entCargo) {
        this.entCargo = entCargo;
    }


    /**
     * Devuelve el nombre de la máxima autoridad de la entidad responsable. Ej: el alcalde
     * @return el nombre
     */
    public String getEntidadAutoridad() {
        return entidadAutoridad;
    }

    /**
     * Coloca el nombre de la máxima autoridad de la entidad responsable. Ej: el alcalde
     * @param entidadAutoridad el nombre
     */
    public void setEntidadAutoridad(String entidadAutoridad) {
        this.entidadAutoridad = entidadAutoridad;
    }

    /**
     * Devuelve la dirección de la entidad responsable
     * @return la dirección
     */
    public String getEntidadDireccion() {
        return entidadDireccion;
    }

    /**
     * Coloca la dirección de la entidad responsable
     * @param entidadDireccion la entidad
     */
    public void setEntidadDireccion(String entidadDireccion) {
        this.entidadDireccion = entidadDireccion;
    }

    /**
     * Devuelve el teléfono de la DNA
     * @return el teléfono
     */
    public String getDnaTelefono() {
        return dnaTelefono;
    }

    /**
     * Coloca el teléfono de la DNA
     * @param dnaTelefono el teléfono
     */
    public void setDnaTelefono(String dnaTelefono) {
        this.dnaTelefono = dnaTelefono;
    }

    /**
     * Devuelve el correo de la DNA
     * @return el correo
     */
    public String getDnaCorreo() {
        return dnaCorreo;
    }

    /**
     * Coloca el correo de la DNA
     * @param dnaCorreo el correo
     */
    public void setDnaCorreo(String dnaCorreo) {
        this.dnaCorreo = dnaCorreo;
    }

    /**
     * Devuelve el horario de la DNA
     * @return el horario
     */
    public String getDnaHorario() {
        return dnaHorario;
    }

    /**
     * Coloca el horario de la DNA
     * @param dnaHorario el horario
     */
    public void setDnaHorario(String dnaHorario) {
        this.dnaHorario = dnaHorario;
    }

    /**
     * Devuelve el DNI del responsable
     * @return el DNI
     */
    public String getRespDni() {
        return respDni;
    }

    /**
     * Coloca el DNI del responsable
     * @param respDni el DNI
     */
    public void setRespDni(String respDni) {
        this.respDni = respDni;
    }

    /**
     * Devuelve el apellido paterno del responsable
     * @return el apellido paterno
     */
    public String getRespApePat() {
        return respApePat;
    }

    /**
     * Coloca el apellido paterno del responsable
     * @param respApePat el apellido paterno
     */
    public void setRespApePat(String respApePat) {
        this.respApePat = respApePat;
    }

    /**
     * Devuelve el apellido materno del responsable
     * @return  el apellido materno
     */
    public String getRespApeMat() {
        return respApeMat;
    }

    /**
     * Coloca el apellido materno del responsable
     * @param respApeMat el apellido materno
     */
    public void setRespApeMat(String respApeMat) {
        this.respApeMat = respApeMat;
    }

    /**
     * Devuelve el primer nombre del responsable
     * @return el primer nombre
     */
    public String getRespNombre1() {
        return respNombre1;
    }

    /**
     * Coloca el primer nombre del responsable
     * @param respNombre1 el primer nombre
     */
    public void setRespNombre1(String respNombre1) {
        this.respNombre1 = respNombre1;
    }

    /**
     * Devuelve el segundo nombre del responsable
     * @return el segundo nombre
     */
    public String getRespNombre2() {
        return respNombre2;
    }

    /**
     * Coloca  el segundo nombre del responsable
     * @param respNombre2 el segundo nombre
     */
    public void setRespNombre2(String respNombre2) {
        this.respNombre2 = respNombre2;
    }

    /**
     * Devuelve el tercer nombre del responsable
     * @return el tercer nombre
     */
    public String getRespNombre3() {
        return respNombre3;
    }
    
    /**
     * Coloca el tercer nombre del responsable
     * @param respNombre3 el tercer nombre
     */
    public void setRespNombre3(String respNombre3) {
        this.respNombre3 = respNombre3;
    }

    /**
     * Devuelve el Colegio del responsable
     * @return el Colegio
     */
    public String getRespColegio() {
        return respColegio;
    }

    /**
     * Coloca el Colegio del responsable
     * @param respColegio el Colegio
     */
    public void setRespColegio(String respColegio) {
        this.respColegio = respColegio;
    }

    /**
     * Devuelve la colegiatura del responsable
     * @return la colegiatura
     */
    public String getRespColegiatura() {
        return respColegiatura;
    }

    /**
     * Coloca la colegiatura del responsable
     * @param respColegiatura la colegiatura
     */
    public void setRespColegiatura(String respColegiatura) {
        this.respColegiatura = respColegiatura;
    }

    /**
     * Devuelve el correo del responsable
     * @return el correo
     */
    public String getRespCorreo() {
        return respCorreo;
    }

    /**
     * Coloca el correo del responsable
     * @param respCorreo el correo
     */
    public void setRespCorreo(String respCorreo) {
        this.respCorreo = respCorreo;
    }

    /**
     * Devuelve el teléfono del responsable
     * @return el teléfono
     */
    public String getRespTelefono() {
        return respTelefono;
    }

    /**
     * Coloca el teléfono del responsable
     * @param respTelefono el teléfono
     */
    public void setRespTelefono(String respTelefono) {
        this.respTelefono = respTelefono;
    }

    /**
     * Devuelve la fecha desde que ejerce el cargo el responsable
     * @return la fecha
     */
    public Date getRespFechaCargo() {
        return respFechaCargo;
    }

    /**
     * Coloca la fecha desde que ejerce el cargo el responsable
     * @param respFechaCargo la fecha
     */
    public void setRespFechaCargo(Date respFechaCargo) {
        this.respFechaCargo = respFechaCargo;
    }

    /**
     * Devuelve la profesión del responble. Catálogo OCUPACIONES.
     * @return el catálogo
     */
    public Catalogo getRespProfesion() {
        return respProfesion;
    }

    /**
     * Coloca la profesión del responble. Catálogo OCUPACIONES.
     * @param respProfesion el catálogo
     */
    public void setRespProfesion(Catalogo respProfesion) {
        this.respProfesion = respProfesion;
    }

    /**
     * Devuelve el nombre del archivo de la ficha
     * @return el nombre del archivo
     */
    public String getArchivoNombre() {
        return archivoNombre;
    }

    /**
     * Coloca el nombre del archivo de la ficha
     * @param archivoNombre el nombre del archivo
     */
    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
    }

    /**
     * Devuelve el nombre de la otra profesión de los integrantes de la DNA
     * @return el nombre
     */
    public String getOtraProfesion() {
        return otraProfesion;
    }

    /**
     * Coloca el nombre de la otra profesión de los integrantes de la DNA
     * @param otraProfesion el nombre
     */
    public void setOtraProfesion(String otraProfesion) {
        this.otraProfesion = otraProfesion;
    }

    /**
     * Devuelve si la DNA no funciona
     * @return true si no funciona
     */
    public boolean isFlgNoFunciona() {
        return this.flgNoFunciona!=null && this.flgNoFunciona==1;
    }

    /**
     * Coloca 1 si la DNA no funciona
     * @param flgNoFunciona true si no funciona
     */
    public void setFlgNoFunciona(boolean flgNoFunciona) {
        this.flgNoFunciona = (flgNoFunciona)?(byte)1:(byte)0;;
    }
  
}