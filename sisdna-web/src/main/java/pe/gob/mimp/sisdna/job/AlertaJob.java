package pe.gob.mimp.sisdna.job;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.mail.internet.MimeUtility;
import pe.gob.mimp.core.Correo;
import pe.gob.mimp.seguridad.fachada.UsuarioFacadeLocal;
import pe.gob.mimp.seguridad.modelo.Usuario;
import pe.gob.mimp.sisdna.fachada.AcreditacionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.InscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Acreditacion;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Inscripcion;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.sisdna.util.Constantes;

/**
 *
 * @author Boolean Core
 */
@Startup
@Singleton
public class AlertaJob {
   /*
    @EJB 
    InscripcionFacadeLocal inscripcionFacade;
    
    @EJB 
    AcreditacionFacadeLocal acreditacionFacade;
   
    @EJB
    private ParametroDnaFacadeLocal parametroFacade;
 
    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    private Correo correo;
    
    @PostConstruct
    public void onStartup() {
  
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_SMTP);
        String smtp = parametros.get(0).getTxtValor();
        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_FROM);
        String from = parametros.get(0).getTxtValor();
        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_PORT);
        String port = parametros.get(0).getTxtValor();
        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_PASSWORD);
        String password = parametros.get(0).getTxtValor();

        this.correo = new Correo();
        this.correo.setServidor(smtp);
        this.correo.setRemitente(from);
        this.correo.setPuerto(port);
        this.correo.setUsuario(from);
        this.correo.setClave(password);
        this.correo.setContenido("");
        this.correo.setHtml(true);
    
    }
    
 //   @Schedule( second="*", minute="*", hour="1",persistent=false)
    public void enviarAlertaVencimientoPorEvaluar() {
      
        try {
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_CORREO_ALERTAS);
            String correoAlertas = parametros.get(0).getTxtValor();
            this.correo.getDestinatarios().add(correoAlertas);
            this.correo.setTitulo(MimeUtility.encodeText("Alerta de vencimiento de evaluación de inscripción", "utf-8", "B"));

            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SIPLAZO_VENCE);
            Integer vence = ((BigInteger)parametros.get(0).getNumValor1()).intValue();

            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SIPLAZO_ALERTA);
            Integer numDias = vence - parametros.get(0).getNumValor1().intValue();


            String contenido = this.cargarTpl(Constantes.TPL_ALERTA_INSCRIPCION);
            List<Object[]> vencidos = this.inscripcionFacade.filtrarPorEvaluarPorVencer(Constantes.CATALOGO_ESTADO_POR_EVALUAR, numDias, vence);
            for(Object[] inscripcion: vencidos) {
              contenido = contenido.replace("{1}", (String)inscripcion[0])
                                   .replace("{0}", (String)inscripcion[1])
                                   .replace("{2}", (String)inscripcion[2]);
              this.correo.setContenido(contenido);
              this.correo.enviarCorreo();
            }
        }catch(Exception ex) {
            
        }
    }
    
  //  @Schedule( second="*", minute="*", hour="1",persistent=false)
    public void enviarAlertaVencimientoSubsanada() {
      
    try {
       
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_CORREO_ALERTAS);
        String correoAlertas = parametros.get(0).getTxtValor();
        this.correo.getDestinatarios().add(correoAlertas);
        this.correo.setTitulo(MimeUtility.encodeText("Alerta de vencimiento de evaluación de inscripción subsanada", "utf-8", "B"));

        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SIPLAZO_VENCE);
        Integer vence = ((BigInteger)parametros.get(0).getNumValor1()).intValue();

        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SIPLAZO_ALERTA);
        Integer numDias = vence - parametros.get(0).getNumValor1().intValue();

        String contenido = this.cargarTpl(Constantes.TPL_ALERTA_INSCRIPCION_SUBSANADA);
        List<Object[]> vencidos = this.inscripcionFacade.filtrarPorEvaluarPorVencer(Constantes.CATALOGO_ESTADO_SUBSANADA, numDias, vence);
        for(Object[] inscripcion: vencidos) {
          contenido = contenido.replace("{1}", (String)inscripcion[0])
                               .replace("{0}", (String)inscripcion[1])
                               .replace("{2}", (String)inscripcion[2]);
          this.correo.setContenido(contenido);
          this.correo.enviarCorreo();
        }
     }catch(Exception ex) {

     }
      
    }

  //  @Schedule( second="*", minute="*", hour="1",persistent=false)
    public void denegarObservadasVencidas() {
      
    try {
           
            this.correo.setTitulo(MimeUtility.encodeText("Alerta de inscripciones vencidas denegadas", "utf-8", "B"));

            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SIPLAZO_VENCE);
            BigInteger numDias = parametros.get(0).getNumValor1();

            String contenido = this.cargarTpl(Constantes.TPL_INS_DENEGADO);
            List<Inscripcion> vencidos = this.inscripcionFacade.filtrarObservadasVencidos(Constantes.CATALOGO_ESTADO_OBSERVADA, numDias.intValue());

            Catalogo estadoDenegado = new Catalogo();
            estadoDenegado.setNidCatalogo(Constantes.CATALOGO_ESTADO_DENEGADA);

            for(Inscripcion inscripcion: vencidos) {

                  inscripcion.setEstado(estadoDenegado);
                  this.inscripcionFacade.edit(inscripcion);
                  List<String> destinatarios = new ArrayList<>();
                  Usuario usuario = this.usuarioFacade.find(inscripcion.getNidUsuario());
                  destinatarios.add(usuario.getTxtUsuario());
               
                  contenido = contenido
                                   .replace("{0}", usuario.getPersona().getTxtNombres() + " " + usuario.getPersona().getTxtApellidoPaterno()  + " " + usuario.getPersona().getTxtApellidoMaterno())
                                   .replace("{1}", inscripcion.getDna().getTxtConstancia() )
                                   .replace("{0}", inscripcion.getDna().getTxtNombre());
                  this.correo.setDestinatarios(destinatarios);
                  this.correo.setContenido(contenido);
                  this.correo.enviarCorreo();

            }  
      }catch(Exception ex) {

     }
      
    }
    
    
    @Schedule( second="*", minute="*", hour="1",persistent=false)
    public void enviarAlertaVencimientoPorEvaluarAcreditacion() {
      
     try {
           
            this.correo.setTitulo(MimeUtility.encodeText("Alerta de vencimiento de evaluación de acreditación", "utf-8", "B"));

            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_CORREO_ALERTAS);
            String correoAlertas = parametros.get(0).getTxtValor();
            this.correo.getDestinatarios().add(correoAlertas);
            
            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SAPLAZO_VENCE);
            Integer vence = ((BigInteger)parametros.get(0).getNumValor1()).intValue();

            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SAPLAZO_ALERTA);
            Integer numDias = vence - parametros.get(0).getNumValor1().intValue();

            String contenido = this.cargarTpl(Constantes.TPL_ALERTA_ACREDITACION);
            List<Object[]> vencidos = this.acreditacionFacade.filtrarPorEvaluarPorVencer(Constantes.CATALOGO_ESTADO_POR_EVALUAR, numDias, vence);
            for(Object[] acreditacion: vencidos) {
              contenido = contenido.replace("{1}", (String)acreditacion[0] )
                                   .replace("{0}", (String)acreditacion[1])
                                   .replace("{2}", (String)acreditacion[2]);
              this.correo.setContenido(contenido);
              this.correo.enviarCorreo();
            }
            
        }catch(Exception ex) {

       }
    }
    
   // @Schedule( second="*", minute="*", hour="1",persistent=false)
    public void enviarAlertaVencimientoSubsanadaAcreditacion() {
      
     try {
           
            this.correo.setTitulo(MimeUtility.encodeText("Alerta de vencimiento de evaluación de acreditación subsanada", "utf-8", "B"));
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_CORREO_ALERTAS);
            String correoAlertas = parametros.get(0).getTxtValor();
            this.correo.getDestinatarios().add(correoAlertas);

            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SAPLAZO_VENCE);
            Integer vence = ((BigInteger)parametros.get(0).getNumValor1()).intValue();

            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SAPLAZO_ALERTA);
            Integer numDias = vence - parametros.get(0).getNumValor1().intValue();
            
            String contenido = this.cargarTpl(Constantes.TPL_ALERTA_ACREDITACION_SUBSANADA);
            List<Object[]> vencidos = this.acreditacionFacade.filtrarSubsanadasPorVencer(Constantes.CATALOGO_ESTADO_SUBSANADA, numDias, vence);
            
            for(Object[]  acreditacion: vencidos) {
              contenido = contenido.replace("{1}", (String)acreditacion[0] )
                                   .replace("{0}", (String)acreditacion[1])
                                   .replace("{2}", (String)acreditacion[2]);
              this.correo.setContenido(contenido);
              this.correo.enviarCorreo();
            }
            
       }catch(Exception ex) {

       }
    }
 
   // @Schedule( second="*", minute="*", hour="1",persistent=false)
    public void denegarObservadasVencidasAcreditacion() {
        
       try {
           
            this.correo.setTitulo(MimeUtility.encodeText("Alerta de acrediación vencida denegada", "utf-8", "B"));
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.PARAMETRO_SIPLAZO_VENCE);
            BigInteger numDias = parametros.get(0).getNumValor1();

            String contenido = this.cargarTpl(Constantes.TPL_ACRE_DENEGADO);
            List<Acreditacion> vencidos = this.acreditacionFacade.filtrarObservadasVencidos(Constantes.CATALOGO_ESTADO_OBSERVADA, numDias.intValue());

            Catalogo estadoDenegado = new Catalogo();
            estadoDenegado.setNidCatalogo(Constantes.CATALOGO_ESTADO_DENEGADA);

            for(Acreditacion acreditacion: vencidos) {

                  acreditacion.setEstado(estadoDenegado);
                  this.acreditacionFacade.edit(acreditacion);
                  List<String> destinatarios = new ArrayList<>();
                  Usuario usuario = this.usuarioFacade.find(acreditacion.getNidUsuario());
                  destinatarios.add(usuario.getTxtUsuario());
                
                  contenido = contenido
                                   .replace("{0}", usuario.getPersona().getTxtNombres() + " " + usuario.getPersona().getTxtApellidoPaterno()  + " " + usuario.getPersona().getTxtApellidoMaterno())
                                   .replace("{1}", acreditacion.getDna().getTxtNombre() )
                                   .replace("{2}", acreditacion.getDna().getTxtConstancia());
                  this.correo.setDestinatarios(destinatarios);
                  this.correo.setContenido(contenido);
                  this.correo.enviarCorreo();

            }  
        }catch(Exception ex) {

       }
    }
    
    public String cargarTpl(String tpl) {
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", tpl);
        return parametros.get(0).getTxtValor();
    }
    */
   
    
}

  
