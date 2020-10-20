package pe.gob.mimp.sisdna.reporte.modelo;


public class Detalle03DSLDREport {
   
    private String nombresApellidos;
    private String fehcaLugar;
    private String dni;
    private String sexoF;
    private String sexoM;

    public Detalle03DSLDREport() {
        this.nombresApellidos = "";
        this.fehcaLugar = "";
        this.dni = "";
        this.sexoF = "";
        this.sexoM = "";
    }
    
    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getFehcaLugar() {
        return fehcaLugar;
    }

    public void setFehcaLugar(String fehcaLugar) {
        this.fehcaLugar = fehcaLugar;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getSexoF() {
        return sexoF;
    }

    public void setSexoF(String sexoF) {
        this.sexoF = sexoF;
    }

    public String getSexoM() {
        return sexoM;
    }

    public void setSexoM(String sexoM) {
        this.sexoM = sexoM;
    }
    
    
    
    
}   
