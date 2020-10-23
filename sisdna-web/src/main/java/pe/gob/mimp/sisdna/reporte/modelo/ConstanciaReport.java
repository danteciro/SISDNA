package pe.gob.mimp.sisdna.reporte.modelo;

public class ConstanciaReport {
    
    private String tipoConstancia;
    
    private String nrDeConstancia;
    
    private String distrito;
    
    private String provincia;
    
    private String departamento;
    
    private String nrOrdenanza;

    private String feOrdenanza;
    
    public ConstanciaReport() {
        this.tipoConstancia = "";
        this.nrDeConstancia = "";
        this.distrito = "";
        this.provincia = "";
        this.departamento = "";
        this.nrOrdenanza = "";
        this.feOrdenanza = "";
    }

    public String getTipoConstancia() {
        return tipoConstancia;
    }

    public void setTipoConstancia(String tipoConstancia) {
        this.tipoConstancia = tipoConstancia;
    }
    
    public String getNrDeConstancia() {
        return nrDeConstancia;
    }

    public void setNrDeConstancia(String nrDeConstancia) {
        this.nrDeConstancia = nrDeConstancia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNrOrdenanza() {
        return nrOrdenanza;
    }

    public void setNrOrdenanza(String nrOrdenanza) {
        this.nrOrdenanza = nrOrdenanza;
    }

    public String getFeOrdenanza() {
        return feOrdenanza;
    }

    public void setFeOrdenanza(String feOrdenanza) {
        this.feOrdenanza = feOrdenanza;
    }
    
}
