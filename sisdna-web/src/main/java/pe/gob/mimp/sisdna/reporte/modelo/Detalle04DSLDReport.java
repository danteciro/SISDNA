package pe.gob.mimp.sisdna.reporte.modelo;


public class Detalle04DSLDReport {
    
    private String nombres;
    private String dni;
    private String fechaCurso;
    private String colegio;
    private String colegiatura;
    private String celular;
    private String email;
    private String funcion;

    public Detalle04DSLDReport() {
        
        this.dni = "";
        this.fechaCurso = "";
        this.colegio = "";
        this.colegiatura = "";
        this.celular = "";
        this.email = "";
        this.nombres = "";
        this.funcion = "";
        
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechaCurso() {
        return fechaCurso;
    }

    public void setFechaCurso(String fechaCurso) {
        this.fechaCurso = fechaCurso;
    }

    public String getColegio() {
        return colegio;
    }

    public void setColegio(String colegio) {
        this.colegio = colegio;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }
    
}
