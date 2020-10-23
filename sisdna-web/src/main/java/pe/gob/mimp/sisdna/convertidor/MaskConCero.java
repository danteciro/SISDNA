package pe.gob.mimp.sisdna.convertidor;

import javax.faces.convert.Converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang.StringUtils;

/**
 * Clase: MaskConCero.java <br>
 * Clase Converter que completa con ceros. Usada para el número de la constancia
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@FacesConverter("maskConCero")
public class MaskConCero implements Converter {

    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        return (submittedValue != null && !submittedValue.equals("")) ? StringUtils.leftPad(submittedValue, 5, '0') : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        return (modelValue != null && !String.valueOf(modelValue).equals("")) ? StringUtils.leftPad(modelValue.toString(), 5, '0'): "";
    }

}
