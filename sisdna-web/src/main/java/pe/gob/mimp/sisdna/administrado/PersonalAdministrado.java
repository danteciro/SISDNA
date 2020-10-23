package pe.gob.mimp.sisdna.administrado;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.PersonalFacadeLocal;
import pe.gob.mimp.sisdna.modelo.PersonaPre;

/**
 * Clase: PersonalAdministrado.java <br>
 * Clase encargada de gestionar las personas que solicitan inscripción a los cursos.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "personalAdministrado")
@ViewScoped
public class PersonalAdministrado extends AdministradorAbstracto implements Serializable {

   //  private static final Logger // LOGGER = LogManager.getLogger(PersonalAdministrado.class.getName());

    private PersonaPre personal;
    private List<PersonaPre> lista;
    private List<PersonaPre> padres;
    private BigInteger padre;
    
    @EJB
    private PersonalFacadeLocal personalFacade;

   
    
    @PostConstruct
    public void initLoad() {
        this.personal = new PersonaPre();
     
    }
    /**
     * inicializa la nueva persona
     */
    public void nuevoPersonal() {
        this.personal = new PersonaPre();
     
    }
    
    /**
     * devuelve todas las personas
     * @return lista de personas
     */
    public List<PersonaPre> obtenerTodos() {
        
        try {
               this.lista = this.personalFacade.findAll();
            
        }catch(Exception ex) {

        }
        return this.lista;
    }
    
    /**
     * devuelve las personas activas
     * @return  lista de personas
     */
    public List<PersonaPre> obtenerActivos(){
        return this.personalFacade.obtenerActivos(true, "apePaterno");
    }
    
    /**
     * carga la información de la persona
     * @param item la persona
     */
    public void obtenerPersonal(PersonaPre item) {
       this.personal = item;
    }
   
   
    /**
     * cambia el estado de la persona: activo o inactivo
     * @param entidad la persona
     */
    public void anular(PersonaPre entidad) {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            entidad.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            entidad.setFlgActivo(this.personal.getFlgActivo());
            entidad.setTxtPc(Internet.obtenerNombrePC());
            entidad.setTxtIp(Internet.obtenerIPPC());
            entidad.setFecModificacion(new Date());
            personalFacade.edit(entidad);
            adicionarMensaje("","El personal ha sido anulado con éxito.");
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al anular el personal");
            // LOGGER.error("Error al anular la persona", ex);
        }
    }
    
    /**
     * actualiza la información del personal
     */
    public void update() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.personal.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.personal.setTxtPc(Internet.obtenerNombrePC());
            this.personal.setTxtIp(Internet.obtenerIPPC());
            this.personal.setFecModificacion(new Date());
            this.personal.setFlgActivo(BigInteger.ONE);
            this.personalFacade.edit(personal);
            adicionarMensaje("","El personal ha sido actualizado con éxito.");
        }catch(Exception ex) {
            adicionarMensajeWarning("","Error al actualizar el personal");
            // LOGGER.error("Error al actualizar la persona", ex);
        }
    }
    
    /**
     * guarda el registro de la persona
     */
    public void create() {
        
        try {
    
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.personal.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.personal.setTxtPc(Internet.obtenerNombrePC());
            this.personal.setTxtIp(Internet.obtenerIPPC());
            this.personal.setFecRegistro(new Date());
            this.personal.setFlgActivo(BigInteger.ONE);
            this.personalFacade.create(personal);
            adicionarMensaje("","El personal ha sido registrado con éxito.");
        }catch(Exception ex) {
            adicionarMensajeWarning("","Error al registrar el personal");
            // LOGGER.error("Error al registrar la persona", ex);
        }
    }
    
  
    public PersonaPre obtenerEntidad(BigInteger nidPersonal){
        return this.personalFacade.find(nidPersonal);
    }
    
    public PersonaPre getPersonal() {
        return personal;
    }

    public void setPersonal(PersonaPre personal) {
        this.personal = personal;
    }

    public List<PersonaPre> getLista() {
        return lista;
    }

    public void setLista(List<PersonaPre> lista) {
        this.lista = lista;
    }

    public List<PersonaPre> getPadres() {
        return padres;
    }

    public void setPadres(List<PersonaPre> padres) {
        this.padres = padres;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

}
