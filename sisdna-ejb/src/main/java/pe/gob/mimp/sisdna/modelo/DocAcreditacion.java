package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
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
import javax.validation.constraints.Size;

@Entity
@Table(name = "DOC_ACREDITACION")
public class DocAcreditacion implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id
    @SequenceGenerator(name = "SQ_DOC_ACREDITACION", sequenceName = "SQ_DOC_ACREDITACION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DOC_ACREDITACION")
    @Column(name = "NID_DOCACRE")
    private BigInteger nidDocacre;

    @JoinColumn(name = "NID_DOC", referencedColumnName = "NID_CATALOGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo tipo;
   
    @JoinColumn(name = "NID_ACREDITACION")
    @ManyToOne(fetch = FetchType.LAZY)
    private Acreditacion acreditacion;
    
    @Size(max = 80)
    @Column(name = "TXT_NOMBRE")
    private String txtNombre;
   
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

    public BigInteger getNidDocacre() {
        return nidDocacre;
    }

    public void setNidDocacre(BigInteger nidDocacre) {
        this.nidDocacre = nidDocacre;
    }

    public Acreditacion getAcreditacion() {
        return acreditacion;
    }

    public void setAcreditacion(Acreditacion acreditacion) {
        this.acreditacion = acreditacion;
    }
    
    public Catalogo getTipo() {
        return tipo;
    }

    public void setTipo(Catalogo tipo) {
        this.tipo = tipo;
    }

   
    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(BigInteger flgActivo) {
        this.flgActivo = flgActivo;
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

   

    public String getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(String txtNombre) {
        this.txtNombre = txtNombre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.nidDocacre);
        hash = 89 * hash + Objects.hashCode(this.tipo);
        hash = 89 * hash + Objects.hashCode(this.acreditacion);
        hash = 89 * hash + Objects.hashCode(this.txtNombre);
        hash = 89 * hash + Objects.hashCode(this.txtPc);
        hash = 89 * hash + Objects.hashCode(this.txtIp);
        hash = 89 * hash + Objects.hashCode(this.flgActivo);
        hash = 89 * hash + Objects.hashCode(this.fecRegistro);
        hash = 89 * hash + Objects.hashCode(this.fecModificacion);
        hash = 89 * hash + Objects.hashCode(this.nidUsuarioReg);
        hash = 89 * hash + Objects.hashCode(this.nidUsuarioMod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DocAcreditacion other = (DocAcreditacion) obj;
        if (!Objects.equals(this.txtNombre, other.txtNombre)) {
            return false;
        }
        if (!Objects.equals(this.txtPc, other.txtPc)) {
            return false;
        }
        if (!Objects.equals(this.txtIp, other.txtIp)) {
            return false;
        }
        if (!Objects.equals(this.nidDocacre, other.nidDocacre)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.acreditacion, other.acreditacion)) {
            return false;
        }
        if (!Objects.equals(this.flgActivo, other.flgActivo)) {
            return false;
        }
        if (!Objects.equals(this.fecRegistro, other.fecRegistro)) {
            return false;
        }
        if (!Objects.equals(this.fecModificacion, other.fecModificacion)) {
            return false;
        }
        if (!Objects.equals(this.nidUsuarioReg, other.nidUsuarioReg)) {
            return false;
        }
        if (!Objects.equals(this.nidUsuarioMod, other.nidUsuarioMod)) {
            return false;
        }
        return true;
    }
    
    
    
}