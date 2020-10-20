package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;


@Entity
@Table(name = "PERSONA_DNA_EVAL")
public class PersonaDnaEval implements Serializable , Cloneable{
    private static final long serialVersionUID = 1L;
 
    @Id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NID_PERSONA")
    private PersonaDna persona;
 
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NID_INSCRIPCION", nullable = false)
    private InscripcionEval inscripcionEval;
    
    @Size(max = 20)
    @Column(name = "TXT_DOCUMENTO")
    private String txtDocumento;
   
    @Size(max = 50)
    @Column(name = "TXT_APELLIDO_PATERNO")
    private String txtApellidoPaterno;
   
    @Size(max = 50)
    @Column(name = "TXT_APELLIDO_MATERNO")
    private String txtApellidoMaterno;
    
    @Size(max = 50)
    @Column(name = "TXT_NOMBRE1")
    private String txtNombre1;
    
    @Size(max = 50)
    @Column(name = "TXT_NOMBRE2")
    private String txtNombre2;
     
    @Size(max = 50)
    @Column(name = "TXT_NOMBRE3")
    private String txtNombre3;
      
    @Size(max = 1)
    @Column(name = "TXT_SEXO")
    private String txtSexo;
    
    @Size(max = 150)
    @Column(name = "TXT_DIRECCION")
    private String txtDireccion;
    
    @Size(max = 150)
    @Column(name = "TXT_CORREO")
    private String txtCorreo;
    
    @Column(name = "NID_INSTRUCCION")
    private Integer nidInstruccion;
    
    @Size(max = 150)
    @Column(name = "TXT_TELEFONO")
    private String txtTelefono;
    
    @JoinColumn(name = "NID_PROFESION", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo profesion;
    
    @Size(max = 150)
    @Column(name = "TXT_COLEGIO")
    private String txtColegio;
    
    @Size(max = 150)
    @Column(name = "TXT_COLEGIATURA")
    private String txtColegiatura;
    
    @Size(max = 150)
    @Column(name = "TXT_LUGAR_CURSO")
    private String txtLugarCurso;
    
    @Column(name = "TXT_FECHA_CURSO")
    @Temporal(TemporalType.DATE)
    private Date txtFechaCurso;

    @Size(max = 150)
    @Column(name = "TXT_DOC_DESIGNACION")
    private String txtDocDesignacion;
   
    @Size(max = 4000)
    @Column(name = "TXT_INPE")
    private String txtInpe;
    
    @JoinColumn(name = "NID_FUNCION", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo funcion;
    
    @Column(name = "NID_DEPARTAMENTO")
    private BigDecimal nidDepartamento;
  
    @Column(name = "NID_PROVINCIA")
    private BigDecimal nidProvincia;
 
    @Column(name = "NID_DISTRITO")
    private BigDecimal nidDistrito;
 
    @Size(max = 4000)
    @Column(name = "TXT_PJ")
    private String txtPj;
  
    @Size(max = 4000)
    @Column(name = "TXT_PNP")
    private String txtPnp;

    @Column(name = "EDAD")
    private BigInteger edad;
    
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
    
    @Size(max = 850)
    @Column(name = "OBS_EDAD")
    private String obsEdad;
    
    @Size(max = 850)
    @Column(name = "OBS_PROFESION")
    private String obsProfesion;
    
    @Size(max = 850)
    @Column(name = "OBS_COLEGIO")
    private String obsColegio;
    
    @Size(max = 850)
    @Column(name = "OBS_LUGAR_CURSO")
    private String obsLugarDeCurso;
    
     @Size(max = 850)
    @Column(name = "OBS_FECHA_CURSO")
    private String obsFechaDeCurso;
    
    @Size(max = 850)
    @Column(name = "OBS_CORREO")
    private String obsCorreo;
    
    @Size(max = 850)
    @Column(name = "OBS_DIRECCION")
    private String obsDireccion;
    
    @Size(max = 850)
    @Column(name = "OBS_INSTRUCCION")
    private String obsInstruccion;
    
    @Size(max = 850)
    @Column(name = "OBS_COLEGIATURA")
    private String obsColegiatura;
    
    @Size(max = 850)
    @Column(name = "OBS_DOC_DEGINACION")
    private String obsDocDesignacion;
   
    @Size(max = 850)
    @Column(name = "OBS_ANTECEDENTES")
    private String obsAntecedentes;
    
   
    public PersonaDna getPersona() {
        return persona;
    }

    public void setPersona(PersonaDna persona) {
        this.persona = persona;
    }
    
    public String getTxtDocumento() {
        return txtDocumento;
    }

    public void setTxtDocumento(String txtDocumento) {
        this.txtDocumento = txtDocumento;
    }

    public String getTxtApellidoPaterno() {
        return txtApellidoPaterno;
    }

    public void setTxtApellidoPaterno(String txtApellidoPaterno) {
        this.txtApellidoPaterno = txtApellidoPaterno;
    }

    public String getTxtApellidoMaterno() {
        return txtApellidoMaterno;
    }

    public void setTxtApellidoMaterno(String txtApellidoMaterno) {
        this.txtApellidoMaterno = txtApellidoMaterno;
    }

    public String getTxtNombre1() {
        return txtNombre1;
    }

    public void setTxtNombre1(String txtNombre1) {
        this.txtNombre1 = txtNombre1;
    }

    public String getTxtNombre2() {
        return txtNombre2;
    }

    public void setTxtNombre2(String txtNombre2) {
        this.txtNombre2 = txtNombre2;
    }

    public String getTxtNombre3() {
        return txtNombre3;
    }

    public void setTxtNombre3(String txtNombre3) {
        this.txtNombre3 = txtNombre3;
    }

    public String getTxtSexo() {
        return txtSexo;
    }

    public void setTxtSexo(String txtSexo) {
        this.txtSexo = txtSexo;
    }

    public String getTxtDireccion() {
        return txtDireccion;
    }

    public void setTxtDireccion(String txtDireccion) {
        this.txtDireccion = txtDireccion;
    }

    public String getTxtCorreo() {
        return txtCorreo;
    }

    public void setTxtCorreo(String txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    public Integer getNidInstruccion() {
        return nidInstruccion;
    }

    public void setNidInstruccion(Integer nidInstruccion) {
        this.nidInstruccion = nidInstruccion;
    }

    public String getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(String txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public Catalogo getProfesion() {
        return profesion;
    }

    public void setProfesion(Catalogo profesion) {
        this.profesion = profesion;
    }

    
    public String getTxtColegiatura() {
        return txtColegiatura;
    }

    public void setTxtColegiatura(String txtColegiatura) {
        this.txtColegiatura = txtColegiatura;
    }

    public String getTxtLugarCurso() {
        return txtLugarCurso;
    }

    public void setTxtLugarCurso(String txtLugarCurso) {
        this.txtLugarCurso = txtLugarCurso;
    }

    public Date getTxtFechaCurso() {
        return txtFechaCurso;
    }

    public void setTxtFechaCurso(Date txtFechaCurso) {
        this.txtFechaCurso = txtFechaCurso;
    }

    public String getTxtInpe() {
        return txtInpe;
    }

    public void setTxtInpe(String txtInpe) {
        this.txtInpe = txtInpe;
    }

    public Catalogo getFuncion() {
        return funcion;
    }

    public void setFuncion(Catalogo funcion) {
        this.funcion = funcion;
    }

    public BigDecimal getNidDepartamento() {
        return nidDepartamento;
    }

    public void setNidDepartamento(BigDecimal nidDepartamento) {
        this.nidDepartamento = nidDepartamento;
    }

    public BigDecimal getNidProvincia() {
        return nidProvincia;
    }

    public void setNidProvincia(BigDecimal nidProvincia) {
        this.nidProvincia = nidProvincia;
    }

    public BigDecimal getNidDistrito() {
        return nidDistrito;
    }

    public void setNidDistrito(BigDecimal nidDistrito) {
        this.nidDistrito = nidDistrito;
    }

    public String getTxtPj() {
        return txtPj;
    }

    public void setTxtPj(String txtPj) {
        this.txtPj = txtPj;
    }

    public String getTxtPnp() {
        return txtPnp;
    }

    public void setTxtPnp(String txtPnp) {
        this.txtPnp = txtPnp;
    }

    public String getTxtPc() {
        return txtPc;
    }

    public void setTxtPc(String txtPc) {
        this.txtPc = txtPc;
    }

    public String getTxtIp() {
        return txtIp;
    }

    public void setTxtIp(String txtIp) {
        this.txtIp = txtIp;
    }

    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(BigInteger flgActivo) {
        this.flgActivo = flgActivo;
    }

    public Date getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(Date fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public Date getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public BigDecimal getNidUsuarioReg() {
        return nidUsuarioReg;
    }

    public void setNidUsuarioReg(BigDecimal nidUsuarioReg) {
        this.nidUsuarioReg = nidUsuarioReg;
    }

    public BigDecimal getNidUsuarioMod() {
        return nidUsuarioMod;
    }

    public void setNidUsuarioMod(BigDecimal nidUsuarioMod) {
        this.nidUsuarioMod = nidUsuarioMod;
    }

    public String getObsAntecedentes() {
        return obsAntecedentes;
    }

    public void setObsAntecedentes(String obsAntecedentes) {
        this.obsAntecedentes = obsAntecedentes;
    }

    public InscripcionEval getInscripcionEval() {
        return inscripcionEval;
    }

    public void setInscripcionEval(InscripcionEval inscripcionEval) {
        this.inscripcionEval = inscripcionEval;
    }

    public BigInteger getEdad() {
        return edad;
    }

    public void setEdad(BigInteger edad) {
        this.edad = edad;
    }

    public String getObsEdad() {
        return obsEdad;
    }

    public void setObsEdad(String obsEdad) {
        this.obsEdad = obsEdad;
    }

    public String getObsProfesion() {
        return obsProfesion;
    }

    public void setObsProfesion(String obsProfesion) {
        this.obsProfesion = obsProfesion;
    }

    public String getObsColegio() {
        return obsColegio;
    }

    public void setObsColegio(String obsColegio) {
        this.obsColegio = obsColegio;
    }

    public String getObsLugarDeCurso() {
        return obsLugarDeCurso;
    }

    public void setObsLugarDeCurso(String obsLugarDeCurso) {
        this.obsLugarDeCurso = obsLugarDeCurso;
    }

    public String getObsCorreo() {
        return obsCorreo;
    }

    public void setObsCorreo(String obsCorreo) {
        this.obsCorreo = obsCorreo;
    }

    public String getObsDireccion() {
        return obsDireccion;
    }

    public void setObsDireccion(String obsDireccion) {
        this.obsDireccion = obsDireccion;
    }

    public String getTxtColegio() {
        return txtColegio;
    }

    public void setTxtColegio(String txtColegio) {
        this.txtColegio = txtColegio;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Object clonar() throws CloneNotSupportedException{
        return this.clone();
    }

    public String getObsFechaDeCurso() {
        return obsFechaDeCurso;
    }

    public void setObsFechaDeCurso(String obsFechaDeCurso) {
        this.obsFechaDeCurso = obsFechaDeCurso;
    }

    public String getObsInstruccion() {
        return obsInstruccion;
    }

    public void setObsInstruccion(String obsInstruccion) {
        this.obsInstruccion = obsInstruccion;
    }

    public String getObsColegiatura() {
        return obsColegiatura;
    }

    public void setObsColegiatura(String obsColegiatura) {
        this.obsColegiatura = obsColegiatura;
    }

    public String getTxtDocDesignacion() {
        return txtDocDesignacion;
    }

    public void setTxtDocDesignacion(String txtDocDesignacion) {
        this.txtDocDesignacion = txtDocDesignacion;
    }

    public String getObsDocDesignacion() {
        return obsDocDesignacion;
    }

    public void setObsDocDesignacion(String obsDocDesignacion) {
        this.obsDocDesignacion = obsDocDesignacion;
    }
    
    
}
