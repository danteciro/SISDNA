package pe.gob.mimp.sisdna.service;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.internet.MimeUtility;
import pe.gob.mimp.sisdna.modelo.Inscripcion;
import pe.gob.mimp.sisdna.modelo.PersonaDna;
import pe.gob.mimp.sisdna.modelo.PersonaDnaAcre;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.sisdna.util.CorreoDnaAdministrado;

@Stateless
public class CorreoService {
    
    /**
     * Envia correo
     * @param responsable - personal en la fase de acreditación
     * @param correoDnaAdministrado el bean del correo
     */
    @Asynchronous
    public void enviarCorreo(PersonaDnaAcre responsable ,  CorreoDnaAdministrado correoDnaAdministrado) {
        try {
            this.agregarContenidoCorreoYEnviar(Constantes.TPL_CREDENCIAL,responsable.getTxtNombre1(), responsable.getTxtApellidoPaterno(), responsable.getTxtCorreo(), correoDnaAdministrado);
        }catch(Exception ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Envia correo
     * @param responsable - personal en la fase de inscripción
     * @param correoDnaAdministrado - el bean del correo
     */
    @Asynchronous
    public void enviarCorreo(PersonaDna responsable ,  CorreoDnaAdministrado correoDnaAdministrado) {
        try {
            this.agregarContenidoCorreoYEnviar(Constantes.TPL_CREDENCIAL,responsable.getTxtNombre1(), responsable.getTxtApellidoPaterno(), responsable.getTxtCorreo(), correoDnaAdministrado);
        }catch(Exception ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Ingresa los valores para poder enviar el correo
     * @param tpl
     * @param nombre1
     * @param apellidoPaterno
     * @param correo
     * @param correoDnaAdministrado
     * @throws UnsupportedEncodingException 
     */
    private void agregarContenidoCorreoYEnviar(String tpl , String nombre1 , String apellidoPaterno , String correo , CorreoDnaAdministrado correoDnaAdministrado) throws UnsupportedEncodingException{
        String contenido = correoDnaAdministrado.cargarTpl(tpl)
                                .replace("{0}", nombre1.concat(apellidoPaterno))
                                .replace("{1}", correo)
                                .replace("{2}", Constantes.PASSWORD);
        correoDnaAdministrado.getCorreo().setContenido(contenido);
        correoDnaAdministrado.getCorreo().getDestinatarios().add(correo);
        correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Correo de Envío de Credenciales", "utf-8", "B"));
        correoDnaAdministrado.getCorreo().setHtml(true);
        correoDnaAdministrado.getCorreo().enviarCorreo();
    }
    
    /**
     * Enviar constancia
     * @param responsable - persona en la fase de inscripción
     * @param inscripcion - instancia de la inscripción
     * @param correoDnaAdministrado  - bean del correo
     */
    @Asynchronous
    public void enviarConstacia(PersonaDna responsable , Inscripcion inscripcion ,CorreoDnaAdministrado correoDnaAdministrado){
        try{
            this.agregarContenidoCorreoYEnviar(Constantes.TPL_CONSTANCIA_INSCRIPCION , inscripcion.getDna().getTxtNombre(),responsable.getTxtCorreo(),correoDnaAdministrado);
        }catch(Exception ex){
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Enviar constancia
     * @param responsable - persona en la fase de acreditación
     * @param inscripcion - instancia de la inscripción
     * @param correoDnaAdministrado  - bean del correo
     */
    @Asynchronous
    public void enviarConstacia(PersonaDnaAcre responsable , Inscripcion inscripcion ,CorreoDnaAdministrado correoDnaAdministrado){
        try{
            this.agregarContenidoCorreoYEnviar(Constantes.TPL_CONSTANCIA_ACREDITACION , inscripcion.getDna().getTxtNombre(),responsable.getTxtCorreo(),correoDnaAdministrado);
        }catch(Exception ex){
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Ingresa los valores para poder enviar el correo
     * @param tpl - plantilla del correo
     * @param dnaNombre - nombre de la demuna
     * @param correo - correo electrónico destinatario
     * @param correoDnaAdministrado - bean del correo
     * @throws UnsupportedEncodingException 
     */
    private void agregarContenidoCorreoYEnviar(String tpl , String dnaNombre , String correo ,CorreoDnaAdministrado correoDnaAdministrado) throws UnsupportedEncodingException{
        String contenido = correoDnaAdministrado.cargarTpl(tpl)
                                    .replace("{0}", dnaNombre);
        correoDnaAdministrado.getCorreo().setContenido(contenido);
        correoDnaAdministrado.getCorreo().getDestinatarios().add(correo);
        correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Notificación de Constancia de Inscripción", "utf-8", "B"));
        correoDnaAdministrado.getCorreo().setHtml(true);
        correoDnaAdministrado.getCorreo().enviarCorreo();  
    }
    
    /**
     * Enviar oficio
     * @param responsable - personal en la fase de inscripción
     * @param inscripcion - instancia de la inscripción
     * @param correoDnaAdministrado  - bean del correo
     */
    @Asynchronous
    public void enviarOficio(PersonaDna responsable , Inscripcion inscripcion ,CorreoDnaAdministrado correoDnaAdministrado){
        try{
            this.agregarContenidoCorreoYEnviarOficio(Constantes.TPL_OFICIO_DENEGACION , inscripcion.getDna().getTxtNombre(),responsable.getTxtCorreo(),correoDnaAdministrado);
        }catch(Exception ex){
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Enviar oficio
     * @param responsable - personal en la fase de acreditación
     * @param inscripcion - instancia de la inscripción
     * @param correoDnaAdministrado  - bean del correo
     */
    @Asynchronous
    public void enviarOficio(PersonaDnaAcre responsable , Inscripcion inscripcion ,CorreoDnaAdministrado correoDnaAdministrado){
        try{
            this.agregarContenidoCorreoYEnviarOficio(Constantes.TPL_OFICIO_DENEGACION_ACRE , inscripcion.getDna().getTxtNombre(),responsable.getTxtCorreo(),correoDnaAdministrado);
        }catch(Exception ex){
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Ingresa los valores para poder enviar el correo
     * @param tpl - plantilla del correo
     * @param dnaNombre - nombre de la demuna
     * @param correo - correo electrónico destinatario
     * @param correoDnaAdministrado - bean del correo
     * @throws UnsupportedEncodingException 
     */
    private void agregarContenidoCorreoYEnviarOficio(String tpl , String dnaNombre , String correo ,CorreoDnaAdministrado correoDnaAdministrado) throws UnsupportedEncodingException{
        String contenido = correoDnaAdministrado.cargarTpl(tpl)
                                    .replace("{0}", dnaNombre);
        correoDnaAdministrado.getCorreo().setContenido(contenido);
        correoDnaAdministrado.getCorreo().getDestinatarios().add(correo);
        correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Notificación de Oficio de Denegación", "utf-8", "B"));
        correoDnaAdministrado.getCorreo().setHtml(true);
        correoDnaAdministrado.getCorreo().enviarCorreo();  
    }
    
    
}
