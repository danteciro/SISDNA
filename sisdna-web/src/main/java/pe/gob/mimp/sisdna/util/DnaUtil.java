
package pe.gob.mimp.sisdna.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * Clase: DnaUtil.java <br>
 * Clase que contiene las funciones utilitarios del sistema. <br>
 * 
 * @author BooleanCore
 */
public class DnaUtil {
    
    /**
     * Devuelve la fecha en formato de cadena
     * @param fecha fecha a evaluar
     * @return fecha en cadena
     */
    public static String obtenerFecha(Date fecha){
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        if(fecha == null) return  "";
        String date = simpleDateFormat.format(fecha);
        return date;
    }
 
    /**
     * Cambia a mayúscula las oración menos las palabras de, la y el
     * @param palabras palabras a cambiar
     * @return las palabras en mayúsculas
     */
    public static String capitalizar(String palabras) {
        
        return WordUtils.capitalizeFully(palabras)
                            .replace(" De ", " de ")
                            .replace(" La ", " la ")
                            .replace(" El ", " el ");
    }
    
    /**
     * Devuelve la fecha con nombre de mes
     * @param fecha fecha a transformar
     * @return fecha en string
     */
    public static String fechaConNombreMes(Date fecha) {
        
        LocalDate ld = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month mes = ld.getMonth();
        String nombre = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        return StringUtils.leftPad(String.valueOf(ld.getDayOfMonth()), 2, "0") + " de " + nombre + " de " + String.valueOf(ld.getYear());
    }
    
    /**
     * Devuelve rango de fechas en formato "del día mes año al día mes año"
     * @param fechaIni fecha de inicio
     * @param fechaFin fecha de fin
     * @return rango de fechas
     */
    public static String rangoFechasConNombre(Date fechaIni, Date fechaFin) {
        
        LocalDate ld1 = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month mesIni = ld1.getMonth();
        
        LocalDate ld2 = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month mesFin = ld2.getMonth();
        
        String smesini = "";
        String sanhoini = "";
        String smesfin = " de " + mesFin.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String sfechafin = StringUtils.leftPad(String.valueOf(ld2.getDayOfMonth()), 2, "0") + smesfin + " de " + String.valueOf(ld2.getYear());
        
        if(mesIni.getValue()<mesFin.getValue()) {
            smesini = " de " + mesIni.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        }
        
        if(ld1.getYear() < ld2.getYear()) {
            sanhoini = " de " + String.valueOf(ld1.getYear());
        }    
       
        return "del " + StringUtils.leftPad(String.valueOf(ld1.getDayOfMonth()), 2, "0") + smesini + sanhoini + " al " + sfechafin;
       
    }
}
