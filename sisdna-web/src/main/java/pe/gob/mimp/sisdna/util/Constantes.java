
package pe.gob.mimp.sisdna.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Clase: Constantes.java <br>
 * Clase que contiene las constantes utilizadas en el sistema. <br>
 * 
 * @author BooleanCore
 */
public class Constantes {
    
    public static final String TIPO_SEDE_CENTRAL = "CEN";
    public static final String TIPO_SEDE_ANEXO = "ANX";
  
    public static final String PREFIX_INSCRIPCION = "i";
    public static final String PREFIX_ACREDITACION = "a";
    public static final String PREFIX_FIRMADO = "f";
    public static final String PREFIX_OFICIO = "o";
    public static final String EXTENSION_PDF = "pdf";
   
    public static final BigDecimal NID_MODULO = BigDecimal.valueOf(16);
 
    public static final BigInteger CATALOGO_ORIGEN = BigInteger.valueOf(1);
    public static final BigInteger CATALOGO_FUNCION = BigInteger.valueOf(2);
    public static final BigInteger CATALOGO_ESTADO = BigInteger.valueOf(3);
    public static final BigInteger CATALOGO_DOCUMENTO_INSCRIPCION = BigInteger.valueOf(4);
    public static final BigInteger CATALOGO_DOCUMENTO_ACREDITACION = BigInteger.valueOf(5);
    public static final BigInteger CATALOGO_GRADO_INSTRUCCION = BigInteger.valueOf(6);
    public static final BigInteger CATALOGO_ACREDITADO_FUNCION = BigInteger.valueOf(7);
    public static final BigInteger CATALOGO_OCUPACIONES = BigInteger.valueOf(8);
    public static final BigInteger CATALOGO_CURSO_PONDERACIONES = BigInteger.valueOf(9);
    public static final BigInteger CATALOGO_CURSO_DOCUM = BigInteger.valueOf(10);
    public static final BigInteger CATALOGO_CURSO_REQ = BigInteger.valueOf(11);
    public static final BigInteger CATALOGO_SUPERVISION_ESTADOS = BigInteger.valueOf(12);
    public static final BigInteger CATALOGO_SUPERVISION_ESTADOS_CONSERVACION = BigInteger.valueOf(13);
    public static final BigInteger CATALOGO_SUPERVISION_OBS_ENTIDAD = BigInteger.valueOf(14);
    public static final BigInteger CATALOGO_SUPERVISION_OBS_DNA = BigInteger.valueOf(15);
    public static final BigInteger CATALOGO_SUPERVISION_OBS_INTEGRANTES = BigInteger.valueOf(16);
    public static final BigInteger CATALOGO_ESTADO_DNA = BigInteger.valueOf(17);
   
    public static final BigInteger CATALOGO_ORIGEN_PROVINCIAL = BigInteger.valueOf(160);
    public static final BigInteger CATALOGO_ORIGEN_DISTRITAL = BigInteger.valueOf(161);
   
    public static final BigInteger CATALOGO_ESTADO_NUEVO = BigInteger.valueOf(114);
    public static final BigInteger CATALOGO_ESTADO_POR_EVALUAR = BigInteger.valueOf(115);
    public static final BigInteger CATALOGO_ESTADO_OBSERVADA = BigInteger.valueOf(117);
    public static final BigInteger CATALOGO_ESTADO_DENEGADA = BigInteger.valueOf(119);
    public static final BigInteger CATALOGO_ESTADO_INSCRITA = BigInteger.valueOf(116);
    public static final BigInteger CATALOGO_ESTADO_SUBSANADA = BigInteger.valueOf(118);
    public static final BigInteger CATALOGO_ESTADO_ACREDITADA = BigInteger.valueOf(140);
    
    public static final BigInteger CATALOGO_FUNCION_RESPONSABLE = BigInteger.valueOf(120);
    public static final BigInteger CATALOGO_FUNCION_DEFENSOR = BigInteger.valueOf(121);
    public static final BigInteger CATALOGO_FUNCION_PROMOTOR = BigInteger.valueOf(122);
    public static final BigInteger CATALOGO_FUNCION_PERSONAL_APOYO = BigInteger.valueOf(123);
    
    public static final BigInteger CATALOGO_ACREDITADO_FUNCION_ABOGADO = BigInteger.valueOf(136);
    public static final BigInteger CATALOGO_ACREDITADO_FUNCION_PSICOLOGO = BigInteger.valueOf(137);
    public static final BigInteger CATALOGO_ACREDITADO_FUNCION_TRABAJADOR = BigInteger.valueOf(138);
    
    public static final BigInteger CATALOGO_OCUPACION_ABOGADO = BigInteger.valueOf(182);
    public static final BigInteger CATALOGO_OCUPACION_PSICOLOGO = BigInteger.valueOf(181);
    public static final BigInteger CATALOGO_OCUPACION_TRABAJADOR = BigInteger.valueOf(184);
   
    public static final BigInteger CATALOGO_DNA_NUEVO = BigInteger.valueOf(349);
    public static final BigInteger CATALOGO_DNA_REGISTRADA = BigInteger.valueOf(350);
    public static final BigInteger CATALOGO_DNA_INSCRITA = BigInteger.valueOf(351);
    public static final BigInteger CATALOGO_DNA_NO_INSCRITA = BigInteger.valueOf(352);
    public static final BigInteger CATALOGO_DNA_NO_FUNCIONA = BigInteger.valueOf(353);
    public static final BigInteger CATALOGO_DNA_ACREDITADA = BigInteger.valueOf(354);
   
    public static final byte CURPROG_ESTADO_PENDIENTE = 0;
    public static final byte CURPROG_ESTADO_CONCLUIDO = 1;
   
    public static final byte CURINS_ESTADO_PREINSCRITO = 1;
    public static final byte CURINS_ESTADO_OBSERVADO = 2;
    public static final byte CURINS_ESTADO_RECHAZADO = 3;
    public static final byte CURINS_ESTADO_INSCRITO = 4;
   
    public static final byte CURINS_ESTADOEVAL_PENDIENTE = 0;
    public static final byte CURINS_ESTADOEVAL_APROBADO = 1;
    public static final byte CURINS_ESTADOEVAL_DESAPROBADO = 2;
    
    public static final byte SUPERVISION_OBS_NOAPLICA = 3;
   
    public static final String PARAMETRO_SIPLAZO_VENCE = "SIPLAZOVENCIMIENTO";
    public static final String PARAMETRO_SIPLAZO_ALERTA = "SIDIASALERTA";
    public static final String PARAMETRO_SAPLAZO_VENCE = "SAPLAZOVENCIMIENTO";
    public static final String PARAMETRO_SAPLAZO_ALERTA = "SADIASALERTA";
    public static final String PARAMETRO_CORREO_ALERTAS = "CORREO_ALERTAS";
   
    public static final String ESTADO_CONSERVACION_BUENO = "1";
    public static final String ESTADO_CONSERVACION_REGULAR = "2";
    public static final String ESTADO_CONSERVACION_MALO = "3";
    
    public static final String RUTA_ARCHIVO = "RUTAARCHIVO";
    public static final String RUTA_CONSTANCIAS = "RUTACONSTANCIAS";
  
    public static final String PASSWORD = "123456";

    public static final String PERFIL_RESPONSABLE = "RESPONSABLE";
    public static final String PERFIL_DSLD = "DSLD";
    public static final String PERFIL_ADMINISTRADOR = "ADMINISTRADOR";


    public static final String TPL_CREDENCIAL = "TPL_CREDENCIAL";
    public static final String TPL_ALERTA_INSCRIPCION = "TPL_ALERTA_INS";
    public static final String TPL_ALERTA_INSCRIPCION_SUBSANADA = "TPL_ALERTA_INS_SUB";
    public static final String TPL_ALERTA_ACREDITACION = "TPL_ALERTA_ACRE";
    public static final String TPL_ALERTA_ACREDITACION_SUBSANADA = "TPL_ALERTA_ACRE_SUB";
    public static final String TPL_CONSTANCIA_INSCRIPCION = "TPL_CONSTANCIA_INS";
    public static final String TPL_CONSTANCIA_ACREDITACION = "TPL_CONSTANCIA_ACRE";
    public static final String TPL_OFICIO_DENEGACION = "TPL_OFICIO_DEN";
    public static final String TPL_OFICIO_DENEGACION_ACRE = "TPL_OFICIO_DEN_ACRE";
    public static final String TPL_INSCRIPCION_OBSERVADA = "TPL_INS_OBS";
    public static final String TPL_ACREDITACION_OBSERVADA = "TPL_ACRE_OBS";
    public static final String TPL_INS_DENEGADO = "TPL_INS_DENEGADO";
    public static final String TPL_ACRE_DENEGADO = "TPL_ACRE_DENEGADO";
    public static final String TPL_CURSO_INS_OK = "TPL_CURSO_INS_OK";
    public static final String TPL_CURSO_INS_OBS = "TPL_CURSO_INS_OBS";
    public static final String TPL_CURSO_INS_REC = "TPL_CURSO_INS_REC";
    public static final String TPL_CURSO_CERT = "TPL_CURSO_CERT";
 
    public static final String CORREO_SMTP = "SMTP";
    public static final String CORREO_FROM = "FROM";
    public static final String CORREO_PORT = "PORT";
    public static final String CORREO_PASSWORD = "PASSWORD";
    
    public static final String RENIEC_DNI = "47352969";
    public static final String RENIEC_RUC = "20336951527";
    public static final String RENIEC_PASSWORD = "47352969";
    public static final String RENIEC_OK = "0000";
    
    public static final int MODO_LISTADO = 0;
    public static final int MODO_NUEVO = 1;
    public static final int MODO_UPDATE = 2;
    public static final int MODO_EVALUACION = 3;
    public static final int MODO_SUBSANAR = 4;
    public static final int MODO_EVALUACION_SUBSANACION = 5;
    public static final int MODO_VER = 6;
   
    public static final int CURINS_MODO_EVALUACION = 3;
    public static final int CURINS_MODO_CALIFICACION = 4;
    
    public static final BigDecimal PARAM_NOMBRE_DIR = BigDecimal.valueOf((long)25);
    
}
