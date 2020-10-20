package pe.gob.mimp.sisdna.administrado;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Departamento;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.modelo.Curso;
import pe.gob.mimp.sisdna.modelo.CursoProgramacion;
import pe.gob.mimp.sisdna.modelo.CursoPrereq;
import pe.gob.mimp.sisdna.modelo.CursoReq;
import pe.gob.mimp.sisdna.fachada.CursoProgramacionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.CursoInscripcion;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.sisdna.reporte.modelo.AprobadoReport;
import pe.gob.mimp.sisdna.reporte.modelo.ResolucionDirectoralReport;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.sisdna.util.ConstantesReporte;
import pe.gob.mimp.sisdna.util.DnaUtil;
import pe.gob.mimp.sisdna.util.PdfUtil;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * Clase: CursoProgramacionAdministrado.java <br>
 * Clase encargada de gestionar las programaciones de los cursos.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "cursoProgramacionAdministrado")
@ViewScoped
public class CursoProgramacionAdministrado extends AdministradorAbstracto implements Serializable {

   // private static final Logger // LOGGER = LogManager.getLogger(CursoProgramacionAdministrado.class.getName());

    private CursoProgramacion cursoProgramacion;
    private List<CursoProgramacion> lista;
    private int modo;
    private List<Object[]> listaReporte;
    private String[] nombreCursos;
    private Integer reporte;
    private List<Provincia> listaProvincias;
    private List<Distrito> listaDistritos;
 
    private List<Map<String, Object>> listaReq;
    private BigDecimal nidCursoProgramacion;
    private String msgError;
    
    private BigDecimal nidDepartamento;
    private BigDecimal nidProvincia;
    private BigDecimal nidDistrito;
    private BigDecimal nidCurso;     
     
    private BigDecimal busCurso;     
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private BigDecimal busTutor;
    private Date busDesde;
    private Date busHasta;
   
    @EJB
    private CursoProgramacionFacadeLocal cursoProgramacionFacade;
    
    @EJB
    private ParametroDnaFacadeLocal parametroFacade;
   
    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    
    
   
    @PostConstruct
    public void initLoad() {
       this.cursoProgramacion = new CursoProgramacion();
       this.modo = Constantes.MODO_LISTADO;
     
    }
    
    /**
     * muestra el título del formulario según el modo
     * @return String
     */
    public String titulo() {
        switch(this.modo) {
            case 1: return "Nueva Programación de curso";
            case 2: return "Actualización de Programación de curso";
        }
        return "";
    } 
   
     /**
     * cambia para regresar al modo listado
     */
    public void regresar(){
       this.modo = Constantes.MODO_LISTADO;
    }
   
    /**
     * Permite cargar el curso según el parámetro de la URL, usado para la inscripción desde el portal
     * Setea el mensaje de error de no existir programación del curso, estar inactivo o haber pasado la fecha de inscripción
     */
    public void cargarCurso() {
       this.cursoProgramacion = this.cursoProgramacionFacade.find(this.nidCursoProgramacion);
       
       if(this.cursoProgramacion==null)
           this.msgError = "El curso no existe. Por favor regrese al listado de cursos y seleccione uno.";
       else 
           if(this.cursoProgramacion.getFlgActivo().equals(BigInteger.ZERO) ||
                !this.cursoProgramacion.isFlgPublicar())
                    this.msgError = "El curso no existe. Por favor regrese al listado de cursos y seleccione uno.";
           else
               if(this.cursoProgramacion.getFechaFinIns().before(new Date())) 
                   this.msgError = "El curso ha finalizado. Por favor regrese al listado de cursos y seleccione uno.";
               else
                   this.msgError = null;
               
    }
    
     /**
     * devuelve la lista de departamentos
     * @return lista de departamentos
     */
    public List<Departamento> obtenerDepartamentos() {
        return  this.departamentoFacade.obtenerDepartamentos();
    }
    
     /**
     * devuelve la lista de provincias según departamento
     */     
    public void obtenerProvincias() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.cursoProgramacion.getNidDepartamento());
        this.cursoProgramacion.setNidProvincia(null);
        this.cursoProgramacion.setNidDistrito(null);
        this.listaProvincias = this.provinciaFacade.obtenerProvincias(departamento);
        this.listaDistritos = null;
        
    }
    
       /**
     * devuelve la lista de provincias según departamento para el filtro del listado
     * @return lista de provincias
     */     
    public List<Provincia> obtenerProvinciasBus() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.busDepartamento);
        this.busProvincia = null;
        return this.provinciaFacade.obtenerProvincias(departamento);
        
    }
    
     /**
     * devuelve la lista de distritos según provincia.
     * Usado para el filtro del listado de distritos
     * @return lista de distritos
     */  
    public List<Distrito> obtenerDistritoBus() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.busProvincia);
        this.busDistrito = null;
        return this.distritoFacade.obtenerDistritos(provincia);
        
    }
    
    /**
     * devuelve la lista de distritos según provincia
     */
    public void obtenerDistritos() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.cursoProgramacion.getNidProvincia());
        this.cursoProgramacion.setNidDistrito(null);
        this.listaDistritos = this.distritoFacade.obtenerDistritos(provincia);
    }
    
     /**
     * devuelve la lista de provincias según departamento
     */ 
    public void obtenerProvinciasFiltro() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.nidDepartamento);
        this.nidProvincia = null;
        this.nidDistrito = null;
        this.listaProvincias = this.provinciaFacade.obtenerProvincias(departamento);
        
    }
    
      
    /**
     * devuelve la lista de distritos según provincia
     */
    public void obtenerDistritosFiltro() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.nidProvincia);
        this.nidDistrito = null;
        this.listaDistritos = this.distritoFacade.obtenerDistritos(provincia);
    }
    
    /**
     * devuelve el listado de programaciones activos según el id del curso
     * @param nidCurso - BigDecimal - id del curso
     * @return lista de programaciones
     */
    public List<CursoProgramacion> obtenerProgramacionesActivas(BigDecimal nidCurso){
        
        ParamFiltro param = new ParamFiltro();
        Curso curso = new Curso();
        curso.setNidCurso(nidCurso);
        param.adicionar("curso", curso);
          
        return this.cursoProgramacionFacade.obtenerPorFiltro(param, false, "fechaIni", false);

    }

    /**
     * inicializa el nuevo curso
     */
    public void nuevo() {
        this.cursoProgramacion = new CursoProgramacion();
        this.cursoProgramacion.setCurso(new Curso());
        this.modo = Constantes.MODO_NUEVO;
    }
    
    /**
     * Genera la lista de programaciones de cursos
     */
    public void obtenerTodos() {
        
        try {
             ParamFiltro param = new ParamFiltro();
             
             if(this.busCurso!=null) {
                 Curso curso = new Curso();
                 curso.setNidCurso(this.busCurso);
                 param.adicionar("curso", curso);
             }
             if(this.busTutor !=null) 
                 param.adicionar("nidTutor", this.busTutor);
             
             if(this.busDepartamento!=null)
                 param.adicionar("nidDepartamento", this.busDepartamento);
             
             if(this.busProvincia!=null)
                 param.adicionar("nidProvincia", this.busProvincia);
             
            if(this.busDistrito!=null)
                 param.adicionar("nidDistrito", this.busDistrito);
             
           
             this.lista = this.cursoProgramacionFacade.obtenerPorFiltro(param, false, "fechaIni", true);
            
        }catch(Exception ex) {
            // LOGGER.error("Error al consultar la lista de programación del curso", ex);
      
        }
    }
    
    /**
     * carga los datos de la programación y cambia a modo UPDATE
     * @param item - CursoProgramacion - objeto enviado por el datatable de programaciones
     */ 
    public void obtener(CursoProgramacion item) {
       this.cursoProgramacion = item;
       BigDecimal nidProvinciaTemp = item.getNidProvincia();
       BigDecimal nidDistritoTemp = item.getNidDistrito();
       this.obtenerProvincias();
       this.cursoProgramacion.setNidProvincia(nidProvinciaTemp);
       this.obtenerDistritos();
       this.cursoProgramacion.setNidDistrito(nidDistritoTemp);
       this.modo = Constantes.MODO_UPDATE;
    }
   
    /**
     * devuelve la lista de programaciones según el filtro de departamento, provincia, distrito y curso.
     * usado en el listado de cursos del portal
     * @return lista de programaciones
     */
    public List<CursoProgramacion> cursosParaPublicar(){
        
        if(this.nidDepartamento != null && this.nidProvincia == null && this.nidDistrito == null) {
            if(this.nidCurso == null)
                return this.cursoProgramacionFacade.cursosParaPublicarDep(this.nidDepartamento);
            else
                return this.cursoProgramacionFacade.cursosParaPublicarDepCurso(this.nidDepartamento, this.nidCurso);
        }
         
        if(this.nidDepartamento !=null && this.nidProvincia != null && this.nidDistrito == null){
            if(this.nidCurso == null)
                return this.cursoProgramacionFacade.cursosParaPublicarProv(this.nidProvincia);
            else
                return this.cursoProgramacionFacade.cursosParaPublicarProvCurso(this.nidProvincia, this.nidCurso);
        }
            
        if(this.nidDepartamento !=null && this.nidProvincia != null && this.nidDistrito != null){
            if(this.nidCurso == null)
                return this.cursoProgramacionFacade.cursosParaPublicarDis(this.nidDistrito);
            else
                return this.cursoProgramacionFacade.cursosParaPublicarDisCurso(this.nidDistrito, this.nidCurso);
        }
         if(this.nidCurso == null)
                return this.cursoProgramacionFacade.cursosParaPublicar();
         else
                return this.cursoProgramacionFacade.cursosParaPublicarCurso(this.nidCurso);
        

    }
    
     /**
     * actualiza el estado de la programación: activo e inactivo
     */
    public void anular() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoProgramacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoProgramacion.setFlgActivo(this.cursoProgramacion.getFlgActivo());
            this.cursoProgramacion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoProgramacion.setTxtIp(Internet.obtenerIPPC());
            this.cursoProgramacion.setFecModificacion(new Date());
            this.cursoProgramacionFacade.edit(this.cursoProgramacion);
            
            if(this.cursoProgramacion.getFlgActivo().compareTo(BigInteger.ONE)==0)
                adicionarMensaje("","La programación del curso ha cambiado su estado a activo con éxito.");
            else
                adicionarMensaje("","La programación del curso ha cambiado su estado a inactivo con éxito.");
         
            this.modo = Constantes.MODO_LISTADO;
            
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al cambiar de estado la programación del curso. Por favor, intente nuevamente.");
            // LOGGER.error("Error al anular la programación del curso", ex);
        }
    }
     
     /**
     * actualiza la información de la inscripción
     */
    public void update() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoProgramacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoProgramacion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoProgramacion.setTxtIp(Internet.obtenerIPPC());
            this.cursoProgramacion.setFecModificacion(new Date());
            this.cursoProgramacion.setFlgActivo(BigInteger.ONE);
            this.cursoProgramacionFacade.edit(this.cursoProgramacion);
            adicionarMensaje("","La programación del curso ha sido actualizada con éxito.");
            this.modo = Constantes.MODO_LISTADO;
         
                 
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al actualizar la programación del curso. Por favor, intente nuevamente.");
            // LOGGER.error("Error al actualizar la programación del curso", ex);
        }
    }
    
    /**
     * permite guardar el registro de la programación según el modo
     */
    public void create() {
    
        if(this.modo == Constantes.MODO_UPDATE)
            this.update();
        else
        try {
    
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoProgramacion.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoProgramacion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoProgramacion.setTxtIp(Internet.obtenerIPPC());
            this.cursoProgramacion.setFecRegistro(new Date());
            this.cursoProgramacion.setFlgActivo(BigInteger.ONE);
            this.cursoProgramacion.setEstado((byte)0);
            this.cursoProgramacionFacade.create(this.cursoProgramacion);
            this.modo = Constantes.MODO_LISTADO;
            
            adicionarMensaje("","La programación del curso ha sido registrada con éxito.");
                 
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al crear la programación del curso. Por favor, intente nuevamente.");
            // LOGGER.error("Error al crear la programación del curso", ex); 
        }
    }
    
     /**
     * Devuelve la lista de nombres de los de prerequisitos y requisitos 
     * @return lista de mapas ( catalogo, nombre ) 
     */
    public List<Map<String, Object>> obtenerRequisitos() {
        
        if(this.cursoProgramacion!=null) {
            
            List<Map<String, Object>> listaReque = new ArrayList<>();
        
            for(CursoReq cr: this.cursoProgramacion.getCurso().getListaReq()) {
                if(cr.getFlgActivo().equals(BigInteger.ONE)) {
                    Map<String, Object> mapa = new HashMap<>();
                    mapa.put("catalogo", cr.getCatalogo());
                    mapa.put("nombre",cr.getCatalogo().getTxtNombre());
                    listaReque.add(mapa);
                }

            }
            for(CursoPrereq cr: this.cursoProgramacion.getCurso().getListaPrereq()) {
                if(cr.getFlgActivo().equals(BigInteger.ONE)) {
                    Map<String, Object> mapa = new HashMap<>();
                    mapa.put("nombre", "Haber aprobado el curso \"" + cr.getCursoPrereq().getNombre() + "\"");
                    listaReque.add(mapa);
                }

            }
            return listaReque;
            
        } 
        return null;
    }
    
    /**
     * devuelve el stream del contenido en bytes del documento del curso
     * @param index - int - número del documento a descargar
     * @return StreamedContent
     */
    public StreamedContent descargar(int index) {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoProgramacion.getCurso().getListaDocum().get(index).getArchivo());
             return new DefaultStreamedContent(ips, "application/pdf","doc.pdf");
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo", ex);
        }
        return null;
    }
    
    
    /**
     * Permite subir la resolución directoral firmada
     * @param event - FileUploadEvent - evento generado al subir la resolución directoral
     */
     public void uploadRD(FileUploadEvent event) {
        
        try {
         
            this.cursoProgramacion.setArchivoRd(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.cursoProgramacion.setArchivoNombre(event.getFile().getFileName());
            adicionarMensaje("Carga de Archivo:",event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir archivo", ex);
        }
    }
  
    /**
     * devuelve el stream del contenido en bytes del archivo de la resolución directoral firmada
     * @return StreamedContent
     */
    public StreamedContent descargarRD() {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoProgramacion.getArchivoRd());
             return new DefaultStreamedContent(ips, "application/pdf",this.cursoProgramacion.getArchivoNombre());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
              // LOGGER.error("Error al descargar la resolución directoral", ex);
        }
        return null;
    }
    
     /**
     * elimina el contenido del archivo de la resolución directoral firmada
     */
    public void eliminarRD() {
        this.cursoProgramacion.setArchivoRd(null);
        this.cursoProgramacion.setArchivoNombre(null);
    }
    
    
    
      /**
     * genera la resolución directoral del curso
     * @param item - CursoProgramacion - programación del curso
     * @return CursoCertificadoReport
     */
    private ResolucionDirectoralReport resolucionDirectoralDs(CursoProgramacion item){
        
        
        ResolucionDirectoralReport salida = new ResolucionDirectoralReport();
        
        salida.setCurso(item.getCurso().getNombre());
        
        Departamento dep = this.departamentoFacade.find(item.getNidDepartamento());
        salida.setDepartamento(DnaUtil.capitalizar(dep.getTxtDescripcion().toLowerCase()));
       
        Provincia prov = this.provinciaFacade.find(item.getNidProvincia());
        salida.setProvincia(DnaUtil.capitalizar(prov.getTxtDescripcion().toLowerCase()));
        
        salida.setCiudad(item.getCiudad());
        
        salida.setCiudad(item.getCiudad());
        salida.setFechas(DnaUtil.rangoFechasConNombre(item.getFechaIni(),item.getFechaFin()));
        
        salida.setNroInforme(item.getRdInforme());
        salida.setDireccion(item.getDireccion());
        
        ParametroDna param = this.parametroFacade.find(Constantes.PARAM_NOMBRE_DIR);
        salida.setNombreDir(param.getTxtValor());
        
        List<AprobadoReport> listaNombres = new ArrayList<>();
        
        int nro = 0;
        
        for(CursoInscripcion cins: this.cursoProgramacion.getListaInscripcion()) {
            
            if(cins.getFlgActivo().compareTo(BigInteger.ONE)==0 && cins.getEstadoEval()!=null) {
                nro++;
                
                if(cins.getEstadoEval() == Constantes.CURINS_ESTADOEVAL_APROBADO) {
                    AprobadoReport arep = new AprobadoReport();
                    arep.setNombre(cins.getPersona().getApellidosNombres()); //getNombreApellidosSep
                    arep.setApellido(cins.getPersona().getApePaterno() + " " + cins.getPersona().getApeMaterno());
                    listaNombres.add(arep);
                }
            }
            
        }
        
        Collections.sort(listaNombres, (AprobadoReport x, AprobadoReport y) -> x.getApellido().compareToIgnoreCase(y.getApellido()));
        
        salida.setNroAprobados(String.valueOf(listaNombres.size()));
        salida.setListaAprobados(listaNombres);
        
        salida.setNroIntegrantes(String.valueOf(nro));
        
        return salida;
    }
    
    
    /**
     * Descarga el PDF de la Resolución Directoral
     * @return StreamedContent
     */
    public StreamedContent downloadRD(){
        
        List<ResolucionDirectoralReport> datasource = new ArrayList<>();
        Map<String,Object> params = new HashMap<>();
        
        ResolucionDirectoralReport reporte = this.resolucionDirectoralDs(this.cursoProgramacion);
        datasource.add(reporte);
       
        try {
            
            InputStream ips = new ByteArrayInputStream(PdfUtil.downloadPDF(params, ConstantesReporte.CURSO_RD_PATH, datasource));
             
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoProgramacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoProgramacion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoProgramacion.setTxtIp(Internet.obtenerIPPC());
            this.cursoProgramacion.setFecModificacion(new Date());
            this.cursoProgramacion.setFlgActivo(BigInteger.ONE);
            this.cursoProgramacionFacade.edit(this.cursoProgramacion);
            
            return new DefaultStreamedContent(ips, "application/pdf", "resdirectoral.pdf");
                
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
             // LOGGER.error("Error al descargar la resolución directoral", ex);
        }
         return null;
    }
    
    
     /**
     * valida el formulario del upload de la resolución directoral firmada
     * @param event - ComponentSystemEvent - evento generado posterior a la validación de los componentes
     */
    public void validarUploadRd(final ComponentSystemEvent event) {
        
            FacesContext fc = FacesContext.getCurrentInstance();
         
            if(this.cursoProgramacion.getArchivoRd()==null) {
                adicionarMensajeError("Debe subir la resolución directoral firmada","" );
                fc.validationFailed();
                fc.renderResponse(); 
            }
        
    }
    
 
    
    public CursoProgramacion getCursoProgramacion() {
        return cursoProgramacion;
    }

    public void setCursoProgramacion(CursoProgramacion CursoProgramacion) {
        this.cursoProgramacion = CursoProgramacion;
    }

    public List<CursoProgramacion> getLista() {
        return lista;
    }

    public void setLista(List<CursoProgramacion> lista) {
        this.lista = lista;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
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
    public List<Map<String, Object>> getListaReq() {
        return listaReq;
    }

    public void setListaReq(List<Map<String, Object>> listaReq) {
        this.listaReq = listaReq;
    }

    public BigDecimal getNidCursoProgramacion() {
        return nidCursoProgramacion;
    }

    public void setNidCursoProgramacion(BigDecimal nidCursoProgramacion) {
        this.nidCursoProgramacion = nidCursoProgramacion;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public BigDecimal getNidDepartamento() {
        return nidDepartamento;
    }

    public void setNidDepartamento(BigDecimal nidDepartamento) {
        this.nidDepartamento = nidDepartamento;
    }

    public BigDecimal getNidProvincia() {
        return nidProvincia;
    }

    public void setNidProvincia(BigDecimal nidProvincia) {
        this.nidProvincia = nidProvincia;
    }

    public BigDecimal getNidDistrito() {
        return nidDistrito;
    }

    public void setNidDistrito(BigDecimal nidDistrito) {
        this.nidDistrito = nidDistrito;
    }

    public BigDecimal getNidCurso() {
        return nidCurso;
    }

    public void setNidCurso(BigDecimal nidCurso) {
        this.nidCurso = nidCurso;
    }

    public BigDecimal getBusCurso() {
        return busCurso;
    }

    public void setBusCurso(BigDecimal busCurso) {
        this.busCurso = busCurso;
    }

    public BigDecimal getBusDepartamento() {
        return busDepartamento;
    }

    public void setBusDepartamento(BigDecimal busDepartamento) {
        this.busDepartamento = busDepartamento;
    }

    public BigDecimal getBusProvincia() {
        return busProvincia;
    }

    public void setBusProvincia(BigDecimal busProvincia) {
        this.busProvincia = busProvincia;
    }

    public BigDecimal getBusTutor() {
        return busTutor;
    }

    public void setBusTutor(BigDecimal busTutor) {
        this.busTutor = busTutor;
    }

    public BigDecimal getBusDistrito() {
        return busDistrito;
    }

    public void setBusDistrito(BigDecimal busDistrito) {
        this.busDistrito = busDistrito;
    }

    public Date getBusDesde() {
        return busDesde;
    }

    public void setBusDesde(Date busDesde) {
        this.busDesde = busDesde;
    }

    public Date getBusHasta() {
        return busHasta;
    }

    public void setBusHasta(Date busHasta) {
        this.busHasta = busHasta;
    }

    public List<Object[]> getListaReporte() {
        return listaReporte;
    }

    public void setListaReporte(List<Object[]> listaReporte) {
        this.listaReporte = listaReporte;
    }

    public Integer getReporte() {
        return reporte;
    }

    public void setReporte(Integer reporte) {
        this.reporte = reporte;
    }

    public String[] getNombreCursos() {
        return nombreCursos;
    }

    public void setNombreCursos(String[] nombreCursos) {
        this.nombreCursos = nombreCursos;
    }

    
    
}
