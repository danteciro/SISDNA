package pe.gob.mimp.sisdna.administrado;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.CatalogoFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.util.Constantes;

/**
 * Clase: CatalogoAdministrado.java <br>
 * Clase encargada de gestionar los catálogos. <br>
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@Named(value = "catalogoAdministrado")
@ViewScoped
public class CatalogoAdministrado extends AdministradorAbstracto implements Serializable {

    private Catalogo catalogo;
    private List<Catalogo> lista;
    private List<Catalogo> padres;
    private BigInteger padre;
    
    @EJB
    private CatalogoFacadeLocal fachada;

    public CatalogoAdministrado() {
        this.lista = new ArrayList<>();
        this.padres = new ArrayList<>();
   }
    
    @PostConstruct
    public void initLoad() {
        this.catalogo = new Catalogo();
        this.catalogo.setNidPadre(null);
     
    }
    
    /**
     * inicializa el nuevo catálogo
     */
    public void nuevoCatalogo() {
        this.catalogo = new Catalogo();
        this.catalogo.setNidPadre(padre);
     
    }
    
    /**
     * Genera el listado de catálogos por padre
     */
    public void obtenerTodos() {
        
        try {
        
            if(this.padre!=null) 
                this.lista = this.fachada.findAllByFieldOrder("nidPadre", this.padre, true, "nidCatalogo", true);
            
        }catch(Exception ex) {

        }
    }
    
    
     /**
     * Devuelve el listado de catálogos padre
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerPadres() {
        this.padres = this.fachada.findAllByFieldOrder("nidPadre", 0, true, "txtNombre", true);
        return this.padres;
    }
    
   /**
    * Setea el catálogo proveniente del datatable
    * @param item item que proviene del datatable
    */ 
   public void obtenerCatalogo(Catalogo item) {
       this.catalogo = item;
   }
   
   /**
    * Cambia el estado ( activo o inactivo ) del catálogo
    * @param entidad catálogo a anular
    */ 
   public void anular(Catalogo entidad) {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            entidad.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            entidad.setFlgActivo(this.catalogo.getFlgActivo());
            entidad.setTxtPc(Internet.obtenerNombrePC());
            entidad.setTxtIp(Internet.obtenerIPPC());
            entidad.setFecModificacion(new Date());
            fachada.edit(entidad);
            adicionarMensaje("","El catálogo ha sido anulado con éxito.");
        } catch (Exception e) {
            adicionarMensajeWarning("Error al anular el catálogo", e.getMessage());
        }
    }
    
   /**
    * Actualiza la información del catálogo
    */
    public void update() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.catalogo.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.catalogo.setTxtPc(Internet.obtenerNombrePC());
            this.catalogo.setTxtIp(Internet.obtenerIPPC());
            this.catalogo.setFecModificacion(new Date());
            this.catalogo.setFlgActivo(BigInteger.ONE);
            this.fachada.edit(catalogo);
            adicionarMensaje("","El catálogo ha sido actualizado con éxito.");
        }catch(Exception ex) {
            adicionarMensajeWarning("","Error al actualizar el catálogo");
        }
    }
    
    /**
     * Registra el nuevo catálogo
     */
    public void create() {
        
        try {
    
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.catalogo.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            //this.catalogo.setNidCatalogo(null);
            if(this.catalogo.getNidPadre() == null)
                this.catalogo.setNidPadre(BigInteger.ZERO); 
            this.catalogo.setTxtPc(Internet.obtenerNombrePC());
            this.catalogo.setTxtIp(Internet.obtenerIPPC());
            this.catalogo.setFecRegistro(new Date());
            this.catalogo.setFlgActivo(BigInteger.ONE);
            this.fachada.create(catalogo);
            adicionarMensaje("","El catálogo ha sido registrado con éxito.");
        }catch(Exception ex) {
            adicionarMensajeWarning("","Error al registrar el catálogo");
        }
    }
    
    /**
     * Devuelve la lista de documentos de inscripción
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerDocumentosInscripcion(){
        List<Catalogo> listaDocumentosInscripcion;
        listaDocumentosInscripcion = fachada.findAllByField("nidPadre", Constantes.CATALOGO_DOCUMENTO_INSCRIPCION);
        return listaDocumentosInscripcion;
    }

    /**
     * Devuelve la lista de funciones del personal de la DNA
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerFunciones(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_FUNCION, true,"txtNombre", false);
    }
    
     /**
     * Devuelve la lista de funciones del personal de la DNA
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerProfesiones(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_OCUPACIONES, true,"txtNombre", false);
    }
    
    
       
     /**
     * Devuelve la lista de estados de la DNA
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerEstadosDna(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_ESTADO_DNA, true,"txtNombre", false);
    }
    
    /**
     * Devuelve la lista de ponderaciones del curso
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerCursoPonderaciones(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_CURSO_PONDERACIONES, true,"txtNombre", false);
    }
    
    /**
     * Devuelve la lista de la documentación de los cursos
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerCursoDocumentacion(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_CURSO_DOCUM, true,"txtNombre", false);
    }
    
    /**
     * Devuelve la lista de requisitos del curso
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerCursoRequisitos(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_CURSO_REQ, true,"txtNombre", false);
    }
    
  
    /**
     * Devuelve la lista de los estados de supervisión
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerSupervisionEstados(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_SUPERVISION_ESTADOS, true,"txtNombre", false);
    }
    
     /**
     * Devuelve la lista de los estados de conservación
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerSupervisionEstadosConservacion(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_SUPERVISION_ESTADOS_CONSERVACION, true,"txtNombre", false);
    }
    
    /**
     * Devuelve la lista de las observaciones respecto a la entidad responsable
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerSupervisionObsEntidad(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_SUPERVISION_OBS_ENTIDAD, true,"nidCatalogo", false);
    }
   
    /**
     * Devuelve la lista de las observaciones respecto a la defensoría de la niña, niño y adolescente
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerSupervisionObsDna(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_SUPERVISION_OBS_DNA, true,"nidCatalogo", false);
    }
   
    /**
     * Devuelve la lista de las observaciones respecto a la entidad responsable
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerSupervisionObsIntegrantes(){
        return this.fachada.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_SUPERVISION_OBS_INTEGRANTES, true,"nidCatalogo", false);
    }
   
    
    /**
     * Devuelve el catálogo buscado por su id
     * @param nidCatalogo id del catálogo
     * @return el catálogo
     */
    public Catalogo obtenerEntidad(BigInteger nidCatalogo){
        return this.fachada.find(nidCatalogo);
    }
    
    
      /**
     * gestiona la subida de archivos del catálogo y setea sus bytes para ser guardados como BLOB.
     * @param event - FileUploadEvent - evento generado al subir archivo
     */
    public void upload(FileUploadEvent event) {
        
        try {
         
            this.catalogo.setArchivo(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.catalogo.setArchivoNombre(event.getFile().getFileName());
            adicionarMensaje("","Carga de archivo " + event.getFile().getFileName() + " terminada");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
        }
    }
  
    /**
     * convierte el contenido en bytes de los archivos a stream para ser descargados.
     * @return StreamedContent
     */
     public StreamedContent descargar() {
        
        InputStream ips;
        
        try {
         
            ips = new ByteArrayInputStream(this.catalogo.getArchivo());
            return new DefaultStreamedContent(ips,"",this.catalogo.getArchivoNombre());

        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
        }
        return null;
    }
    
     /**
      * permite descargar el archivo
      * @param item catálogo
      * @return stream del archivo
      */
    public StreamedContent descargar(Catalogo item) {
        
        InputStream ips;
        
        try {
         
            ips = new ByteArrayInputStream(item.getArchivo());
            return new DefaultStreamedContent(ips,"",item.getArchivoNombre());

        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
        }
        return null;
    }
    
      /**
     * elimina el contenido del archivo.
     */
     public void eliminarArchivo() {
        this.catalogo.setArchivo(null);
        this.catalogo.setArchivoNombre(null);
    }
    
     public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public List<Catalogo> getLista() {
        return lista;
    }

    public void setLista(List<Catalogo> lista) {
        this.lista = lista;
    }

    public List<Catalogo> getPadres() {
        return padres;
    }

    public void setPadres(List<Catalogo> padres) {
        this.padres = padres;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

}
