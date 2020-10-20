package pe.gob.mimp.sisdna.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "PARAMETRO_DEMUNA")
@XmlRootElement
public class ParametroDna implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SQ_PARAMETRO_DEMUNA", sequenceName = "SQ_PARAMETRO_DEMUNA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PARAMETRO_DEMUNA")
    @Column(name = "NID_PARAMETRO")
    private BigDecimal nidParametro;

    @Column(name = "CID_PARAMETRO")
    private String cidParametro;

    @Column(name = "NID_PARAMETRO_PADRE")
    private BigInteger nidParametroPadre;

    @Column(name = "TXT_VALOR")
    private String txtValor;

    @Column(name = "NUM_VALOR1")
    private BigInteger numValor1;

    @Column(name = "NUM_VALOR2")
    private BigInteger numValor2;

    @Column(name = "FLG_ACTIVO")
    private BigInteger flgActivo;

    @Column(name = "NID_USUARIO")
    private BigInteger nidUsuario;

    @Column(name = "TXT_PC")
    private String txtPc;

    @Column(name = "TXT_IP")
    private String txtIp;

    @Column(name = "FEC_EDICION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEdicion;

    @Column(name = "COMENTARIO")
    private String comentario;

    public ParametroDna() {
    }

    public ParametroDna(BigDecimal nidParametro) {
        this.nidParametro = nidParametro;
    }

    public BigDecimal getNidParametro() {
        return nidParametro;
    }

    public void setNidParametro(BigDecimal nidParametro) {
        this.nidParametro = nidParametro;
    }

    public String getCidParametro() {
        return cidParametro;
    }

    public void setCidParametro(String cidParametro) {
        this.cidParametro = cidParametro;
    }

    public BigInteger getNidParametroPadre() {
        return nidParametroPadre;
    }

    public void setNidParametroPadre(BigInteger nidParametroPadre) {
        this.nidParametroPadre = nidParametroPadre;
    }

    public String getTxtValor() {
        return txtValor;
    }

    public void setTxtValor(String txtValor) {
        this.txtValor = txtValor;
    }

    public BigInteger getNumValor1() {
        return numValor1;
    }

    public void setNumValor1(BigInteger numValor1) {
        this.numValor1 = numValor1;
    }

    public BigInteger getNumValor2() {
        return numValor2;
    }

    public void setNumValor2(BigInteger numValor2) {
        this.numValor2 = numValor2;
    }

    public BigInteger getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(BigInteger flgActivo) {
        this.flgActivo = flgActivo;
    }

    public BigInteger getNidUsuario() {
        return nidUsuario;
    }

    public void setNidUsuario(BigInteger nidUsuario) {
        this.nidUsuario = nidUsuario;
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

    public Date getFecEdicion() {
        return fecEdicion;
    }

    public void setFecEdicion(Date fecEdicion) {
        this.fecEdicion = fecEdicion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nidParametro != null ? nidParametro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroDna)) {
            return false;
        }
        ParametroDna other = (ParametroDna) object;
        if ((this.nidParametro == null && other.nidParametro != null) || (this.nidParametro != null && !this.nidParametro.equals(other.nidParametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.mimp.sisdna.modelo.ParametroDna[ nidParametro=" + nidParametro + " ]";
    }

}
