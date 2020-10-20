package pe.gob.mimp.sisdna.dto;


import java.math.BigInteger;

/**
 * Clase utilizada para el datatable de las observaciones de la ficha
 * @author BooleanCore
 */
public class SupervisionObsDto {
    
    
    
    private BigInteger nidSupervisionObs;
    
    private BigInteger nidCatalogo;
    
    private String descripcion;
    
    private Byte estado;
    
    private Byte estadoOriginal;

    public BigInteger getNidSupervisionObs() {
        return nidSupervisionObs;
    }

    public void setNidSupervisionObs(BigInteger nidSupervisionObs) {
        this.nidSupervisionObs = nidSupervisionObs;
    }

    public BigInteger getNidCatalogo() {
        return nidCatalogo;
    }

    public void setNidCatalogo(BigInteger nidCatalogo) {
        this.nidCatalogo = nidCatalogo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Byte getEstado() {
        return estado;
    }

    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    public Byte getEstadoOriginal() {
        return estadoOriginal;
    }

    public void setEstadoOriginal(Byte estadoOriginal) {
        this.estadoOriginal = estadoOriginal;
    }
    
    
    
}
