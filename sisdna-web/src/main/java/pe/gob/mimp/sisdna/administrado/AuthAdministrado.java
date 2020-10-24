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
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaInfoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.DefensoriaInfo;
import pe.gob.mimp.sisdna.util.Constantes;

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
    private BigDecimal authResponsable;
    private Defensoria dna;
    
  
    @EJB
    private PerfilFacadeLocal perfilFacade;
   
    @EJB
    private ModuloFacadeLocal moduloFacade;
 
    @EJB
    private DefensoriaFacadeLocal defensoriaFacade;
  
    @EJB
    private DefensoriaInfoFacadeLocal defensoriaInfoFacade;
   
   
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
    
        if(!listaPerfiles.isEmpty() && listaPerfiles.get(0).getTxtPerfil().equals(Constantes.PERFIL_DSLD)) {
            this.authTutor = usuarioAdministrado.getEntidad().getNidUsuario();
        }
        
        if(!listaPerfiles.isEmpty() && listaPerfiles.get(0).getTxtPerfil().equals(Constantes.PERFIL_RESPONSABLE)) {
            this.authResponsable = usuarioAdministrado.getEntidad().getNidUsuario();
            ParamFiltro p = new ParamFiltro();
            p.adicionar("nidUsuario", this.authResponsable);
            List<DefensoriaInfo> listaInfo = this.defensoriaInfoFacade.obtenerPorFiltro(p, true, "nidInfo", false);
            
            if(listaInfo!=null && !listaInfo.isEmpty()) {
                ParamFiltro pdna = new ParamFiltro();
                pdna.adicionar("nidInfo", listaInfo.get(0).getNidInfo());
                List<Defensoria> dnas = this.defensoriaFacade.obtenerPorFiltro(listaPerfiles, true, "nidDna", true);
                
                if(dnas!=null && !dnas.isEmpty()) {
                    this.dna = dnas.get(0);
                }
            } 
        }
        
   
    }

    public BigDecimal getAuthTutor() {
        return authTutor;
    }

    public void setAuthTutor(BigDecimal authTutor) {
        this.authTutor = authTutor;
    }

    public BigDecimal getAuthResponsable() {
        return authResponsable;
    }

    public void setAuthResponsable(BigDecimal authResponsable) {
        this.authResponsable = authResponsable;
    }

    public Defensoria getDna() {
        return dna;
    }

    public void setDna(Defensoria dna) {
        this.dna = dna;
    }
    
    
}