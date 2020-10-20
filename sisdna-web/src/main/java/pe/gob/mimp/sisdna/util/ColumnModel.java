package pe.gob.mimp.sisdna.util;

import java.io.Serializable;


/**
 * Clase: ColumnModel.java <br>
 * Clase utilizada para crear columnas dinámicas. <br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
public class ColumnModel  implements Serializable {
    
    private String header;
    private String property;

    public ColumnModel() {
    }
 
    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setProperty(String property) {
        this.property = property;
    }
    
}
