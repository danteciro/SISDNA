package pe.gob.mimp.sisdna.util;

/**
 * Clase: ConstantesReporte.java <br>
 * Clase que contiene las constantes utilizadas para los reportes del sistema. <br>
 * 
 * @author BooleanCore
 */
public class ConstantesReporte {
       
    //FORMULARIO 03 DSDL
    public static final String FORM03DSDL_PATH = "/resources/reportes/Formulario03DSLD1.jasper";
    public static final String FORM03DSDL_SUBREPORT_PATH = "/resources/reportes/Formulario03DSLDSubreport.jasper";
    public static final String FORM03DSDL_NAME = "Formulario03DSLD1.pdf";
    public static final String FORM03DSDL_SUBREPORT_PARAM = "subReporte";
    
    //FORMULARIO 04 DSDL
    public static final String FORM04DSDL_PATH = "/resources/reportes/Formulario04DSLD.jasper";
    public static final String FORM04DSDL_SUBREPORT_PATH = "/resources/reportes/Formulario04DSLDSubreport.jasper";
    public static final String FORM04DSDL_NAME = "Formulario04DSLD.pdf";
    public static final String FORM04DSDL_SUBREPORT_PARAM = "subReporte";
    
    //CONSTANCIA 
    public static final String CONSTANCIA_PATH = "/resources/reportes/Constancia.jasper";
    public static final String CONSTANCIA_ACRE_PATH = "/resources/reportes/ConstanciaAcre.jasper";
    public static final String PREFIJO_ACREDITACION = "AR";
    //Declaracion Jurada
    public static final String DECLARACION_JURADA_PATH = "/resources/reportes/Form03Declaracion.jasper";
    public static final String DECLARACION_JURADA_PARAM = "subReporte2";
    
    //Logo
    public static final String IMAGEN_PATH = "/resources/imagen/logomimp.png";
    
    //cursos
    public static final String CURSO_CERTIFICADO_PATH = "/resources/reportes/CursoCertificado.jasper";
    public static final String CURSO_RD_PATH = "/resources/reportes/ResolucionDirectoral.jasper";
}
