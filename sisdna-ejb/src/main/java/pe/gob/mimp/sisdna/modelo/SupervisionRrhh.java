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
import org.eclipse.persistence.annotations.JoinFetch;
import static org.eclipse.persistence.annotations.JoinFetchType.INNER;

/**
 * Clase: SupervisionRrhh.java<br>
 * Entidad que registra las personas que laboran en las Defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Supervision
 */
@Entity
@Table(name = "SUPERVISION_RRHH")
@XmlRootElement
public class SupervisionRrhh implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_SUP_RRHH", sequenceName = "SQ_SUP_RRHH", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SUP_RRHH")
    @Column(name = "NID_RRHH")
    private BigDecimal nidRrhh;
    
    @JoinColumn(name = "NID_SUPERVISION", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Supervision supervision;
     
    @ManyToOne
    @JoinColumn(name = "NID_FUNCION", nullable = false)
    @JoinFetch(value=INNER)
    private Catalogo funcion;
     
    @Column(name = "DERECHO_H")
    private Byte derechoH;
   
    @Column(name = "DERECHO_M")
    private Byte derechoM;
    
    @Column(name = "TRABSOC_H")
    private Byte trabSocH;
   
    @Column(name = "TRABSOC_M")
    private Byte trabSocM;

    @Column(name = "PSICOLOGIA_H")
    private Byte psicologiaH;
   
    @Column(name = "PSICOLOGIA_M")
    private Byte psicologiaM;

    @Column(name = "EDUCACION_H")
    private Byte educacionH;
   
    @Column(name = "EDUCACION_M")
    private Byte educacionM;

    @Column(name = "OTRO_H")
    private Byte otroH;
   
    @Column(name = "OTRO_M")
    private Byte otroM;

    @Column(name = "TOTAL_H")
    private Integer totalH;
   
    @Column(name = "TOTAL_M")
    private Integer totalM;

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
     * Devuelve el id de la persona
     * @return id de la persona
     */
    public BigDecimal getNidRrhh() {
        return nidRrhh;
    }

    /**
     * Coloca el id de la persona
     * @param nidRrhh id de la persona
     */
    public void setNidRrhh(BigDecimal nidRrhh) {
        this.nidRrhh = nidRrhh;
    }

    /**
     * Devuelve la supervisión a la que pertenece
     * @return la supervisión a la que pertenece
     */
    public Supervision getSupervision() {
        return supervision;
    }

    /**
     * Coloca la supervisión a la que pertenece
     * @param supervision la supervisión a la que pertenece
     */
    public void setSupervision(Supervision supervision) {
        this.supervision = supervision;
    }

    /**
     * Devuelve el catálogo de la función que desempeña. Catálogo FUNCIÓN DE PERSONAL DE DNA
     * @return función que desempeña
     */
    public Catalogo getFuncion() {
        return funcion;
    }

    /**
     * Coloca el catálogo de la función que desempeña. Catálogo FUNCIÓN DE PERSONAL DE DNA
     * @param funcion función que desempeña
     */
    public void setFuncion(Catalogo funcion) {
        this.funcion = funcion;
    }

    public Byte getDerechoH() {
        return derechoH;
    }

    public void setDerechoH(Byte derechoH) {
        this.derechoH = derechoH;
    }

    public Byte getDerechoM() {
        return derechoM;
    }

    public void setDerechoM(Byte derechoM) {
        this.derechoM = derechoM;
    }

    public Byte getTrabSocH() {
        return trabSocH;
    }

    public void setTrabSocH(Byte trabSocH) {
        this.trabSocH = trabSocH;
    }

    public Byte getTrabSocM() {
        return trabSocM;
    }

    public void setTrabSocM(Byte trabSocM) {
        this.trabSocM = trabSocM;
    }

    public Byte getPsicologiaH() {
        return psicologiaH;
    }

    public void setPsicologiaH(Byte psicologiaH) {
        this.psicologiaH = psicologiaH;
    }

    public Byte getPsicologiaM() {
        return psicologiaM;
    }

    public void setPsicologiaM(Byte psicologiaM) {
        this.psicologiaM = psicologiaM;
    }

    public Byte getEducacionH() {
        return educacionH;
    }

    public void setEducacionH(Byte educacionH) {
        this.educacionH = educacionH;
    }

    public Byte getEducacionM() {
        return educacionM;
    }

    public void setEducacionM(Byte educacionM) {
        this.educacionM = educacionM;
    }

    public Byte getOtroH() {
        return otroH;
    }

    public void setOtroH(Byte otroH) {
        this.otroH = otroH;
    }

    public Byte getOtroM() {
        return otroM;
    }

    public void setOtroM(Byte otroM) {
        this.otroM = otroM;
    }

    public Integer getTotalH() {
        return totalH;
    }

    public void setTotalH(Integer totalH) {
        this.totalH = totalH;
    }

    public Integer getTotalM() {
        return totalM;
    }

    public void setTotalM(Integer totalM) {
        this.totalM = totalM;
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
