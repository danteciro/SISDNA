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
 * Clase: SupervisionObs.java<br>
 * Entidad que registra las observaciones de las supervisiones a las Defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 * @see Catalogo
 * @see Supervision
 * @see Catalogo
 */
@Entity
@Table(name = "SUPERVISION_OBS")
@XmlRootElement
public class SupervisionObs implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_SUP_OBS", sequenceName = "SQ_SUP_OBS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SUP_OBS")
    @Column(name = "NID_OBS")
    private BigInteger nidObs;

    @JoinColumn(name = "NID_SUPERVISION", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Supervision supervision;
  
    @ManyToOne
    @JoinColumn(name = "NID_CATALOGO", nullable = false)
    @JoinFetch(value=INNER)
    private Catalogo catalogo;
  
    @Column(name = "ESTADO")
    private Byte estado;
    
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

    public SupervisionObs() {
        this.catalogo = new Catalogo();
    }
    
       
    /**
     * Devuelve el id de la observación
     * @return el id de la observación
     */
    public BigInteger getNidObs() {
        return nidObs;
    }

    /**
     * Coloca el id de la observación
     * @param nidObs id de la observación
     */
    public void setNidObs(BigInteger nidObs) {
        this.nidObs = nidObs;
    }
    
    /**
     * Devuelve la supervisión a la que pertenece
     * @return supervisión
     */
    public Supervision getSupervision() {
        return supervision;
    }

    /**
     * Coloca la supervisión a la que pertenece
     * @param supervision supervisión a la que pertenece
     */
    public void setSupervision(Supervision supervision) {
        this.supervision = supervision;
    }

    /**
     * Devuelve el catálogo de la observación. Catálogo SUPERVISIONES: OBS DNA
     * @return catálogo de la observación
     */
    public Catalogo getCatalogo() {
        return catalogo;
    }

    /**
     * Coloca el catálogo de la observación. Catálogo SUPERVISIONES: OBS DNA
     * @param catalogo catálogo de la observación
     */
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Devuelve el estado de la observación: Conforme (1) Observado (2) o No aplica (3)
     * @return el estado de la observcación
     */
    public Byte getEstado() {
        return estado;
    }

    /**
     * Coloca el estado de la observación: Conforme (1) Observado (2) o No aplica (3)
     * @param estado estado de la observación
     */
    public void setEstado(Byte estado) {
        this.estado = estado;
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
