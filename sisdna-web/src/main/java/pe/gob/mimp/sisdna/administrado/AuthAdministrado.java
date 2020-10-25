package pe.gob.mimp.sisdna.administrado;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.seguridad.fachada.ModuloFacadeLocal;
import pe.gob.mimp.seguridad.fachada.PerfilFacadeLocal;
import pe.gob.mimp.seguridad.modelo.Modulo;
import pe.gob.mimp.seguridad.modelo.Perfil;

/**
 * Clase: AuthAdministrado.java <br>
 * Clase que se encarga de gestionar el tipo de usuario que ingresa al sistema.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "authAdministrado")
@ManagedBean
@SessionScoped
public class AuthAdministrado implements Serializable {

    private BigDecimal authTutor;
    
    @EJB
    private PerfilFacadeLocal perfilFacade;
   
    @EJB
    private ModuloFacadeLocal moduloFacade;
 
   
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
  
    /**
     * Realiza el login del sistema y determina si el usuario es tutor
     * @param event evento del botón
     * @throws Exception excepción
     */
    public void login(ActionEvent event) throws Exception {
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
        usuarioAdministrado.login(event);
        
        this.authTutor = null;
        
        List<Modulo> modulos = this.moduloFacade.findAllByField("txtNombreTecnico", "DEMUNA");
        List<Perfil> listaPerfiles = this.perfilFacade.obtenerPerfiles(usuarioAdministrado.getEntidadSeleccionada(),modulos.get(0));
    
        if(!listaPerfiles.isEmpty() && listaPerfiles.get(0).getTxtPerfil().equals("TUTOR")) {
            this.authTutor = usuarioAdministrado.getEntidad().getNidUsuario();

        }
    }

    public BigDecimal getAuthTutor() {
        return authTutor;
    }

    public void setAuthTutor(BigDecimal authTutor) {
        this.authTutor = authTutor;
    }
    
}