package pe.gob.mimp.sisdna.administrado;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.CursoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Curso;
import pe.gob.mimp.sisdna.modelo.CursoDocum;
import pe.gob.mimp.sisdna.modelo.CursoPonderacion;
import pe.gob.mimp.sisdna.modelo.CursoPrereq;
import pe.gob.mimp.sisdna.modelo.CursoReq;
import pe.gob.mimp.sisdna.util.Constantes;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * Clase: CursoAdministrado.java <br>
 * Clase encargada de gestionar los cursos que dicta la DSLD.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "cursoAdministrado")
@ViewScoped
public class CursoAdministrado extends AdministradorAbstracto implements Serializable {

   //  private static final Logger LOGGER = LogManager.getLogger(CursoInscripcionAdministrado.class.getName());

    private Curso curso;
    private List<Curso> lista;
    private int modo;
    private BigInteger nidRequisito;
    private BigDecimal nidPrereq;
    
    private List<Provincia> listaProvincias;
    private List<Distrito> listaDistritos;
    

    private List<String> listaCursos;
    private String cursoPorAgregar;
    
    @EJB
    private CursoFacadeLocal cursoFacade;


    public CursoAdministrado() {
        this.lista = new ArrayList<>();
   }
    
    @PostConstruct
    public void initLoad() {
       this.curso = new Curso();
       this.modo = Constantes.MODO_LISTADO;
     
    }
    
    /**
     * Muestra el título del formulario según el modo
     * @return el título del modo
     */
    public String titulo() {
        switch(this.modo) {
            case 1: return "Nuevo Curso";
            case 2: return "Actualización de Curso";
        }
        return "";
    } 
   
    /**
     * Regresa al modo listado
     */
    public void regresar(){this.titulo();
       this.modo = Constantes.MODO_LISTADO;
    }
   
   
    /**
     * Devuelve la lista de cursos activos ordenados por el nombre
     * @return lista de cursos
     */
    public List<Curso> obtenerCursosActivos(){
        return this.cursoFacade.obtenerActivos(true, "nombre");        
    }

    /**
     * Devuelve el nombre del curso más sus siglas
     * @param nidCurso id del Curso
     * @return nombre del curso ( siglas )
     */
    public String obtenerNombreCurso(BigDecimal nidCurso) {
        Curso cs = this.cursoFacade.find(nidCurso);
        return cs.getNombre() + " (" + cs.getSiglas() + ")";
    }
    
    /**
     * Inicializa los valores para ingresar un nuevo curso. 
     * Setea la lista de ponderaciones desde la tabla Catálogo.
     * Setea la lista de documentos que posee el curso desde la tabla Catálogo
     */
    public void nuevo() {
        this.curso = new Curso();
        this.curso.setListaPrereq(new ArrayList<>());
        this.curso.setListaPonderacion(new ArrayList<>());
        this.curso.setListaDocum(new ArrayList<>());
        this.curso.setListaReq(new ArrayList<>());
        this.cursoPorAgregar = null;
        this.nidRequisito = null;
        this.modo = Constantes.MODO_NUEVO;
        CatalogoAdministrado catalogoAdministrado = (CatalogoAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{catalogoAdministrado}", CatalogoAdministrado.class);
        List<Catalogo> listaCat = catalogoAdministrado.obtenerCursoPonderaciones();
        
        for(Catalogo c: listaCat) {
            CursoPonderacion cp = new CursoPonderacion();
            cp.setCatalogo(c);
            cp.setPonderacion(c.getValorNum());
            this.curso.getListaPonderacion().add(cp);
        }
        
        List<Catalogo> listaDocum = catalogoAdministrado.obtenerCursoDocumentacion();
        
        for(Catalogo c: listaDocum) {
            CursoDocum cd = new CursoDocum();
            cd.setCatalogo(c);
            this.curso.getListaDocum().add(cd);
        }
       
    }
    
    /**
     * Obtiene todos los registros de los cursos
     * @return lista de cursos
     */
    public List<Curso> obtenerTodos() {
        
        try {
        
             this.lista = this.cursoFacade.obtenerTodos(true, "nombre", true);
            
        }catch(Exception ex) {
          //   LOGGER.error("Error a consultar los cursos", ex);
        }
        return this.lista;
    }
    
   /**
    * Carga la información del curso seleccionado y cambia al modo update
    * @param item - Curso - objeto del curso enviado del datatable
    */  
   public void obtener(Curso item) {
       this.curso = item;
       this.modo = Constantes.MODO_UPDATE;
       this.cursoPorAgregar = null;
       this.nidRequisito = null;
   }
   
   /**
    * Permite cambiar el estado del registro del curso: activo e inactivo
    */
     public void anular() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.curso.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.curso.setFlgActivo(this.curso.getFlgActivo());
            this.curso.setTxtPc(Internet.obtenerNombrePC());
            this.curso.setTxtIp(Internet.obtenerIPPC());
            this.curso.setFecModificacion(new Date());
            this.cursoFacade.edit(this.curso);
            
            if(this.curso.getFlgActivo().compareTo(BigInteger.ONE)==0)
                adicionarMensaje("","El curso ha cambiado su estado a activo con éxito");
            else
                adicionarMensaje("","El curso ha cambiado su estado a inactivo con éxito");
            
            this.modo = Constantes.MODO_LISTADO;
            
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al cambiar el estado del curso. Por favor, intente nuevamente.");
//            LOGGER.error("Error al anular el curso",ex);
        }
    }
    
    /**
     * Actualiza la información del registro del curso.
     */ 
    public void update() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.curso.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.curso.setTxtPc(Internet.obtenerNombrePC());
            this.curso.setTxtIp(Internet.obtenerIPPC());
            this.curso.setFecModificacion(new Date());
            this.curso.setFlgActivo(BigInteger.ONE);
            this.cursoFacade.edit(this.curso);
            adicionarMensaje("","El curso ha sido actualizado con éxito.");
            this.modo = Constantes.MODO_LISTADO;
         
                 
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al actualizar el curso. Por favor, intente nuevamente.");
//             LOGGER.error("Error al actualizar el curso",ex);
        }
    }
    
    /**
     * Permite crear o actualizar según el modo del formulario. 
     * Guarda la información de los prerequisitos, requisitos, ponderaciones y documentos
     */
    public void create() {
    
        if(this.modo == Constantes.MODO_UPDATE)
            this.update();
        else
        try {
    
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.curso.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.curso.setTxtPc(Internet.obtenerNombrePC());
            this.curso.setTxtIp(Internet.obtenerIPPC());
            this.curso.setFecRegistro(new Date());
            this.curso.setFlgActivo(BigInteger.ONE);
        
            for(CursoPrereq cpr: this.curso.getListaPrereq()) {
                cpr.setCurso(this.curso);
                cpr.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                cpr.setTxtPc(Internet.obtenerNombrePC());
                cpr.setTxtIp(Internet.obtenerIPPC());
                cpr.setFecRegistro(new Date());
                cpr.setFlgActivo(BigInteger.ONE);
            }
        
            for(CursoPonderacion cp: this.curso.getListaPonderacion()) {
                cp.setCurso(this.curso);
                cp.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                cp.setTxtPc(Internet.obtenerNombrePC());
                cp.setTxtIp(Internet.obtenerIPPC());
                cp.setFecRegistro(new Date());
                cp.setFlgActivo(BigInteger.ONE);
            }        
        
            for(CursoDocum cd: this.curso.getListaDocum()) {
                cd.setCurso(this.curso);
                cd.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                cd.setTxtPc(Internet.obtenerNombrePC());
                cd.setTxtIp(Internet.obtenerIPPC());
                cd.setFecRegistro(new Date());
                cd.setFlgActivo(BigInteger.ONE);
            }
            
            for(CursoReq cr: this.curso.getListaReq()) {
                cr.setCurso(this.curso);
                cr.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                cr.setTxtPc(Internet.obtenerNombrePC());
                cr.setTxtIp(Internet.obtenerIPPC());
                cr.setFecRegistro(new Date());
                cr.setFlgActivo(BigInteger.ONE);
            }
        
            this.cursoFacade.create(this.curso);
            this.modo = Constantes.MODO_LISTADO;
            
            adicionarMensaje("","El curso ha sido registrado con éxito.");
                 
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al crear el Curso. Por favor, intente nuevamente.");
//            LOGGER.error("Error al regisgtrar el curso",ex);
        }
    }

    /**
     * Carga el contenido en bytes en el campo foto
     * @param event - FileUploadEvent - evento generado al subir la foto
     */
    public void uploadFoto(FileUploadEvent event) {
        
        try {
            this.curso.setFoto(IOUtils.toByteArray(event.getFile().getInputstream()));
            adicionarMensaje("","Carga de Archivo: " + event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeError("", "Error al subir archivo");
//            LOGGER.error("Error al subir la foto",ex);
        }
    }
    
    /**
     * Devuelve el stream de la foto guardada
     * @return StreamedContent
     */
    public StreamedContent verFoto() {
        
        InputStream ips;
        
        FacesContext context = FacesContext.getCurrentInstance();
          try {
                
                ips = new ByteArrayInputStream(this.curso.getFoto());
                return new DefaultStreamedContent(ips, "image/jpg");

            } catch(Exception ex) {
                adicionarMensajeError("","Error al descargar archivo");
//                 LOGGER.error("Error al descargar la foto",ex);
            }
        return null;
    }
    
    /**
     * Devuelve el stream de los bytes solicitado
     * @param param , es el contenido en bytes
     * @return StreamedContent
     */
    public StreamedContent verFotoParam(byte[] param) {
        
        InputStream ips;
        
         try {

             ips = new ByteArrayInputStream(param);
             return new DefaultStreamedContent(ips, "image/jpg");//, "image/jpg"

         } catch(Exception ex) {
             adicionarMensajeWarning("","Error al descargar archivo");
 //            LOGGER.error("Error al descargar la foto",ex);
         }
        return null;
    }
    
    /**
     * Permite subir el contenido en bytes de los documentos, según el index presentado en el datatable
     * @param event - FileUploadEvent - evento generado al subir el archivo
     */
    public void upload(FileUploadEvent event) {
        
        try {
         
            Integer index = (Integer)event.getComponent().getAttributes().get("index");
            this.curso.getListaDocum().get(index).setArchivo(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.curso.getListaDocum().get(index).setArchivoNombre(event.getFile().getFileName());
            adicionarMensaje("","Carga de Archivo:" + event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
//            LOGGER.error("Error al subir el archivo",ex);
        }
    }
  
    /**
     * Devuelve el stream del contenido en bytes del documento seleccionado según el index presentado en el datatable
     * @param index - el número del archivo del documento
     * @return StreamedContent
     */
    public StreamedContent descargar(int index) {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.curso.getListaDocum().get(index).getArchivo());
             return new DefaultStreamedContent(ips, "application/pdf",this.curso.getListaDocum().get(index).getArchivoNombre());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
 //           LOGGER.error("Error al descargar el archivo",ex);
        }
        return null;
    }
    
   
    
    
    /**
     * Permite agregar requisitos al curso si no ha sido agregado con anterioridad
     */   
    public void agregarRequisito(){
       
        boolean existe = false;
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
         
        for(CursoReq c: this.curso.getListaReq()) {
            if(c.getCatalogo().getNidCatalogo().equals(this.nidRequisito)) {
                existe = true;
                break;
            }
        }
        if(!existe) {
            
            try {  
                
              CatalogoAdministrado catalogoAdministrado = (CatalogoAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{catalogoAdministrado}", CatalogoAdministrado.class);
              CursoReq cr = new CursoReq();
              cr.setCatalogo(catalogoAdministrado.obtenerEntidad(this.nidRequisito));
              cr.setCurso(this.curso);
              cr.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
              cr.setTxtPc(Internet.obtenerNombrePC());
              cr.setTxtIp(Internet.obtenerIPPC());
              cr.setFecRegistro(new Date());
              cr.setFlgActivo(BigInteger.ONE);
              this.curso.getListaReq().add(cr);        

            } catch(Exception ex) {
//                LOGGER.error("Error al agregar el requisito",ex);
            }
        }
        
    }
    
    /**
     * Elimina el requisito del curso. 
     * Si el requisito del curso es nuevo entonces sólo lo remueve del datatable
     * Si el requisito del curso ya ha sido guardado con anterioridad entonces inactiva el registro.
     * @param itemReq - CursoReq - el rerequisito del curso
     */    
    public void anularRequisito(CursoReq itemReq) {
      
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
        
        if(itemReq.getNidReq()==null)
            this.curso.getListaReq().remove(itemReq);
        else {
          
            try {  
            
                itemReq.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                itemReq.setTxtPc(Internet.obtenerNombrePC());
                itemReq.setTxtIp(Internet.obtenerIPPC());
                itemReq.setFecModificacion(new Date());
                itemReq.setFlgActivo(BigInteger.ZERO);
            
            } catch(Exception ex) {
  //          LOGGER.error("Error al anular el requisito",ex);
            }
        }
        
    }
    
   /**
    * Permite agregar el curso prerequisito si es que aún no ha sido agregado
    */
    public void agregarPrerequisito(){
       
        boolean existe = false;
      
        for(CursoPrereq c: this.curso.getListaPrereq()) {
            if(c.getCursoPrereq().getNidCurso().equals(this.nidPrereq)) {
                existe = true;
                break;
            }
        }
        if(!existe) {
            CursoPrereq cpr = new CursoPrereq();
            cpr.setCurso(this.curso);
            cpr.setCursoPrereq(this.cursoFacade.find(this.nidPrereq));
            this.curso.getListaPrereq().add(cpr);
        }

    }
    
    /**
     * Anula el curso prerequisito.
     * Si el prerequisito del curso es nuevo entonces sólo lo remueve del datatable
     * Si el prerequisito del curso ya ha sido guardado con anterioridad entonces inactiva el registro.
     * @param itemPrereq - CursoPrereq - el prerequisito del curso
     */
    public void anularPrerequisito(CursoPrereq itemPrereq) {
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
     
        if(itemPrereq.getNidPrereq()==null)
            this.curso.getListaPrereq().remove(itemPrereq);
        else {
            
            try {  
            
                itemPrereq.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                itemPrereq.setTxtPc(Internet.obtenerNombrePC());
                itemPrereq.setTxtIp(Internet.obtenerIPPC());
                itemPrereq.setFecModificacion(new Date());
                itemPrereq.setFlgActivo(BigInteger.ZERO);
            
            } catch(Exception ex) {
   //             LOGGER.error("Error al anular el prerrequisito del curso",ex);
            }
           
        }
        
    }
  
    /**
     * devuelve la lista de cursos para uso del control autocomplete
     * @param query - String
     * @return lista de cursos
     */
    public List<String> obtenerAutocompleteCursos(String query) {
        List<String> lcursos = new ArrayList<>();
        List<Curso> cursos;
        
        try {
            
            if (0 < query.trim().length()) {
                ParamFiltro param = new ParamFiltro();
                param.adicionar("nombre", query, ParamFiltro.LIKE);
                cursos = this.cursoFacade.obtenerPorFiltro(param, true, "nombre", false);
            } else 
                cursos = this.cursoFacade.obtenerActivos(true, "nombre");
            
            
            if(0 < cursos.size()) {
                for(Curso c: cursos)  {
                    if(this.cursoPorAgregar==null || !this.cursoPorAgregar.equals(c.getNombre()))
                      lcursos.add(c.getNombre());
                } 
                
            } else
                adicionarMensajeError("Información", "No existen cursos con ese nombre");
           
        } catch (Exception e) {
            adicionarMensajeError("Error", e.getMessage());
        }

        return lcursos;
        
    }
   
    /**
     * valida el formulario del curso luego de validar los controles
     * Calcula si la suma de ponderaciones es 100% , de lo contrario muestra mensajes de error
     * @param event - ComponentSystemEvent - evento generado posterior a la validación de los componentes
     */
    public void validarCurso(final ComponentSystemEvent event) {
        
            FacesContext fc = FacesContext.getCurrentInstance();
         
            double total = 0;
            
            for(CursoPonderacion cp : this.curso.getListaPonderacion()) {
            
                if(cp.getPonderacion() == null){
                    adicionarMensajeError("Debe ingresar todas las ponderaciones de evaluación del curso","" );
                    fc.validationFailed();
                    fc.renderResponse(); 
                    break;
                } else {
                    total = total + cp.getPonderacion().doubleValue();
                }
            }            
            
            if(total < 100) {
                adicionarMensajeError("El total de las ponderaciones es menor al 100%","" );
                fc.validationFailed();
                fc.renderResponse(); 
            }
            if(total > 100) {
                adicionarMensajeError("El total de las ponderaciones superan el 100%","" );
                fc.validationFailed();
                fc.renderResponse(); 
            }
        
            for(CursoDocum cd: this.curso.getListaDocum()) {
                if(cd.getArchivo()==null) {
                    adicionarMensajeError("Debe adjuntar el archivo PDF del " + cd.getCatalogo().getTxtNombre(),"" );
                    fc.validationFailed();
                    fc.renderResponse(); 
                }
           } 
    }
    
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Curso> getLista() {
        return lista;
    }

    public void setLista(List<Curso> lista) {
        this.lista = lista;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public BigInteger getNidRequisito() {
        return nidRequisito;
    }

    public void setNidRequisito(BigInteger nidRequisito) {
        this.nidRequisito = nidRequisito;
    }

    public CursoFacadeLocal getCursoFacade() {
        return cursoFacade;
    }

    public void setCursoFacade(CursoFacadeLocal cursoFacade) {
        this.cursoFacade = cursoFacade;
    }

    public BigDecimal getNidPrereq() {
        return nidPrereq;
    }

    public void setNidPrereq(BigDecimal nidPrereq) {
        this.nidPrereq = nidPrereq;
    }

    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List<Distrito> getListaDistritos() {
        return listaDistritos;
    }

    public void setListaDistritos(List<Distrito> listaDistritos) {
        this.listaDistritos = listaDistritos;
    }

    public List<String> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<String> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public String getCursoPorAgregar() {
        return cursoPorAgregar;
    }

    public void setCursoPorAgregar(String cursoPorAgregar) {
        this.cursoPorAgregar = cursoPorAgregar;
    }

   

}
