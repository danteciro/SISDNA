package pe.gob.mimp.sisdna.administrado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.mimp.core.Archivo;
import pe.gob.mimp.core.PasswordUtil;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Departamento;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.general.util.ParametroNodo;
import pe.gob.mimp.general.util.ParametroNodoObject;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.seguridad.fachada.EstadoUsuarioFacadeLocal;
import pe.gob.mimp.seguridad.fachada.PerfilFacadeLocal;
import pe.gob.mimp.seguridad.fachada.PerfilUsuarioFacadeLocal;
import pe.gob.mimp.seguridad.fachada.PersonaFacadeLocal;
import pe.gob.mimp.seguridad.fachada.UsuarioFacadeLocal;
import pe.gob.mimp.seguridad.modelo.EstadoUsuario;
import pe.gob.mimp.seguridad.modelo.Modulo;
import pe.gob.mimp.seguridad.modelo.Perfil;
import pe.gob.mimp.seguridad.modelo.PerfilUsuario;
import pe.gob.mimp.seguridad.modelo.PerfilUsuarioPK;
import pe.gob.mimp.seguridad.modelo.Persona;
import pe.gob.mimp.seguridad.modelo.TipoDocumento;
import pe.gob.mimp.seguridad.modelo.Usuario;
import pe.gob.mimp.sisdna.fachada.CatalogoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DocInscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.InscripcionEvalFacadeLocal;
import pe.gob.mimp.sisdna.fachada.InscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.DefensoriaInfo;
import pe.gob.mimp.sisdna.modelo.DefensoriaPersona;
import pe.gob.mimp.sisdna.modelo.DocInscripcion;
import pe.gob.mimp.sisdna.modelo.Inscripcion;
import pe.gob.mimp.sisdna.modelo.InscripcionEval;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.sisdna.modelo.PersonaDna;
import pe.gob.mimp.sisdna.modelo.PersonaDnaEval;
import pe.gob.mimp.sisdna.reporte.modelo.ConstanciaReport;
import pe.gob.mimp.sisdna.reporte.modelo.DeclaracionJuradaReport;
import pe.gob.mimp.sisdna.reporte.modelo.Detalle03DSLDREport;
import pe.gob.mimp.sisdna.reporte.modelo.Formulario03DSLDReport;
import pe.gob.mimp.sisdna.service.CorreoService;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.sisdna.util.ConstantesReporte;
import pe.gob.mimp.sisdna.util.PdfUtil;
import pe.gob.mimp.sisdna.util.CorreoDnaAdministrado;
import pe.gob.mimp.sisdna.util.DnaUtil;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedenteJudicial;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedentePenal;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedentePolicial;
import pe.gob.mimp.webservicemimp.auxiliar.IdentificacionReniec;

@Named(value = "inscripcionAdministrado")
@ViewScoped
public class InscripcionAdministrado extends AdministradorAbstracto implements Serializable{

    private Logger LOG = Logger.getLogger(InscripcionAdministrado.class.getName());
   
    private Defensoria defensoria;
    private Inscripcion inscripcion;
    private List<Defensoria> listaDefensoria;
    private List<Inscripcion> lista;
    private List<PersonaDna> listaPersonal;
    private List<PersonaDna> listaPersonalRemovidos;
    private PersonaDna personaDna;
    private PersonaDna personaDnaOriginal;
    private List<DocInscripcion> adjuntos;
    private String busTipo;
    private String busCodigo;
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private BigInteger busEstado;
    private int indexTab = 0;
  
    @EJB
    private InscripcionFacadeLocal inscripcionFacade;
   
    @EJB
    private PersonaDnaFacadeLocal personaDnaFacade;
   
    @EJB
    private DefensoriaFacadeLocal defensoriaFacade;
   
    @EJB
    private InscripcionEvalFacadeLocal inscripcionEvalFacade;
   
    @EJB
    private ParametroDnaFacadeLocal parametroFacade;
 
    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    @EJB
    private PersonaFacadeLocal personaFacade;

    @EJB
    private PerfilFacadeLocal perfilFacade;

    @EJB
    private CatalogoFacadeLocal catalogoFacade;

    @EJB
    private EstadoUsuarioFacadeLocal estadoUsuarioFacade;

    @EJB
    private PerfilUsuarioFacadeLocal perfilUsuarioFacade;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;

    @EJB
    private DocInscripcionFacadeLocal docFacade;
            
    @EJB
    private CorreoService correoService;


    private List<Provincia> provinciasDna;
    private List<Distrito> distritosDna;
    private List<Provincia> provinciasBus;
    private List<Distrito> distritosBus;
    private String codigo;
    private boolean verOrigen;
    private String nombreSede;
    private Inscripcion inscripcionSeleccionada;
    private PersonaDna responsable;
    private boolean verMensajeOK;
    private boolean dniValido;
    private String ruta;
    private List<UploadedFile> archivos;
    private String rutaConstancias;
    private InscripcionEval inscripcionEval;
    private PersonaDnaEval personaDnaEval;
    private String txtObsPersonaEval;
    private List<PersonaDnaEval> personasDnaEval;
    private List<PersonaDna> personalESubsanar;
    private Boolean flgDocObs;
    private Boolean flgCorreoObs;
    private Boolean flgDireccionObs;
    private Boolean flgGerenciaObs;
    private Boolean flgDiasHoraObs;
    private Boolean flgPresupuestoObs;
    private Boolean flgNroAmbObs;
    private Boolean flgNroAmbPrivObs;
    private Boolean flgPersonaObservada;
    private Boolean flgAdjuntosObs;
    private String textoBtnEvaluar;
    private Boolean flgEdadPersonaObs;
    private Boolean flgProfesionPersonaObs;
    private Boolean flgColegioPersonaObs;
    private Boolean flgLugarPersonaObs;
    private Boolean flgCorreoPersonaObs;
    private Boolean flgDireccionPersonaObs;
    private Boolean flgInstruccionObs;
    private Boolean flgNroColegiaturaObs;
    private Boolean flgFechaCursoObs;
    private Boolean flgAntecedentePersonaObs;
    private Boolean flgDocDesignacionObs;
    private Boolean flgDenegarInscripcion;
    private Inscripcion inscripcionSubsanar;
    private List<PersonaDna> personalRemover;
    private PersonaDna responsableSubsanar;
    private PersonaDna personaRemover;
    private String correoResponsable;
    
    private TabView tabView;
    private int modo;
   
    public InscripcionAdministrado() {
        this.listaPersonal = new ArrayList<>();
        this.listaPersonalRemovidos = new ArrayList<>();
        this.provinciasDna = new ArrayList<>();
        this.distritosDna = new ArrayList<>();
        this.verOrigen = true;
        this.nombreSede = "";
        this.verMensajeOK = false;
        this.dniValido = false;
        this.inscripcionEval =  new InscripcionEval();
        this.personaDnaEval = new PersonaDnaEval();
        this.personasDnaEval = new ArrayList<>();
        this.personalESubsanar = new ArrayList<>();
        this.flgDocObs = false;
        this.flgCorreoObs = false;
        this.flgDireccionObs = false;
        this.flgGerenciaObs = false;
        this.flgDiasHoraObs = false;
        this.flgPresupuestoObs = false;
        this.flgNroAmbObs = false;
        this.flgNroAmbPrivObs = false;
        this.flgAdjuntosObs = false;
        this.textoBtnEvaluar = "Confirmar";
        this.flgEdadPersonaObs = false;
        this.flgProfesionPersonaObs = false;
        this.flgColegioPersonaObs = false;
        this.flgLugarPersonaObs = false;
        this.flgCorreoPersonaObs = false;
        this.flgDireccionPersonaObs = false;
        this.flgInstruccionObs = false;
        this.flgNroColegiaturaObs = false;
        this.flgFechaCursoObs = false;
        this.flgAntecedentePersonaObs = false;
        this.flgDocDesignacionObs = false;
        this.flgDenegarInscripcion = false;
        this.inscripcionSubsanar = new Inscripcion();
        this.personalRemover = new ArrayList<>();
        this.inscripcion = new Inscripcion();
        this.inscripcion.setDna(new Defensoria());
        this.correoResponsable = "";
    }
    
    @PostConstruct
    public void initLoad() {
        this.busTipo = "1";
        this.initFiltro();
        this.initAdjuntos();
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.RUTA_ARCHIVO);
        this.ruta = parametros.get(0).getTxtValor();
        
        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.RUTA_CONSTANCIAS);
        this.rutaConstancias = parametros.get(0).getTxtValor();
        
        this.initPersona();
        this.modo = Constantes.MODO_LISTADO;
    }
    
       /**
     * Muestra el título del formulario según el modo
     * @return el título del modo
     */
    public String titulo() {
        switch(this.modo) {
            case Constantes.MODO_NUEVO: return "Nueva Inscripción"; 
            case Constantes.MODO_UPDATE: return "Actualización de Inscripción";
            case Constantes.MODO_EVALUACION: return "Evaluación de Inscripción";
            case Constantes.MODO_EVALUACION_SUBSANACION: return "Evaluación de Inscripción Subsanada";
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
     * Inicializar los filtros
     */
    public void initFiltro() {
        this.busCodigo = null;
        this.busDepartamento = null;
        this.busProvincia = null;
        this.busDistrito = null;
        this.busEstado = null;
        this.listaDefensoria = null;
        this.lista = null;
   
    }
    
    /**
     * Inicializar los documentos adjuntos
     */
    public void initAdjuntos() {
        this.adjuntos = new ArrayList<>();
      
        List<Catalogo> listaDoc = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_DOCUMENTO_INSCRIPCION, true, "txtNombre", false);
        for(Catalogo docAd: listaDoc) {
            DocInscripcion docIns = new DocInscripcion();
            docIns.setTipo(docAd);
            this.adjuntos.add(docIns);
        }      
    }
    
    /**
     * Inicializar el estado de las personas
     */
    public void initPersona() {
        this.personaDna = new PersonaDna();
        this.personaDna.setFuncion(new Catalogo());
        this.personaDna.setProfesion(new Catalogo());
       
        this.personaDnaEval = new PersonaDnaEval();
        this.personaDnaEval.setFuncion(new Catalogo());
        
               
        //Remover
        this.personaRemover =new PersonaDna();
        this.personaRemover.setFuncion(new Catalogo());
    }

  
    
    /**
     * Inicializar nueva defensoria
     * @param item - Defensoria
     */
    public void nuevaSolicitud(Defensoria item) {
         this.inscripcion = new Inscripcion();
         this.inscripcion.setDna(item);
         this.listaPersonal = new ArrayList<>();
         this.modo = Constantes.MODO_NUEVO;
         this.initAdjuntos();
    }
    
    /**
     * Obtener la inscripcion
     * @param item - Inscripcion
     */
    public void obtenerSolicitud(Inscripcion item) {
        this.inscripcion = this.inscripcionFacade.find(item.getNidInscripcion());
        item.setPersonal(this.inscripcion.getPersonal());
        item.setDocumentos(this.inscripcion.getDocumentos());
        this.personalRemover = new ArrayList<>();
        // muestra solo personal activo 
        this.listaPersonal = this.obtenerPersonalActivo(this.inscripcion.getPersonal());
        this.responsable = this.obtenerResponsable(listaPersonal);
        if(this.inscripcion.getEstado().getNidCatalogo()!= Constantes.CATALOGO_ESTADO_NUEVO)  {
            this.adjuntos = new ArrayList<>();
            for(DocInscripcion doc : this.inscripcion.getDocumentos()){
                adjuntos.add(doc);
            }
        }
    }
    
    /**
     * Obtener todas las inscripciones
     * @return lista de inscripciones
     */
    public List<Inscripcion> obtenerTodos() {
        this.lista = this.inscripcionFacade.findAll();
        return this.lista;
    }
   
  
    /**
     * Obtener el estado
     * @return  lista de catálogos
     */
    public List<Catalogo> obtenerEstado() {
        List<Catalogo> estados =  this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_ESTADO, true, "txtNombre", false); 
        List<Catalogo> estadosFiltrados = new ArrayList<>();
        for(Catalogo estado : estados){
            if(!estado.getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_ACREDITADA)){
                estadosFiltrados.add(estado);
            }
        }
        return estadosFiltrados;
    }
    
   
    
    
    
    /**
     * Devuelve las funciones del personal, si ya ingresó un responsable entonces lo retira de la lista de funciones a asignar
     * @return  lista de catálogos
     */
    public List<Catalogo> obtenerFunciones() {
        List<Catalogo> listaCatalogos = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_FUNCION, true, "txtNombre", false);
        
        if( this.verificaResponsable() && ( this.personaDna.getFuncion().getNidCatalogo() == null || (this.personaDna.getFuncion().getNidCatalogo() != null &&  this.personaDna.getFuncion().getNidCatalogo().compareTo(Constantes.CATALOGO_FUNCION_RESPONSABLE)!=0) ) )
                    for(Catalogo funcion: listaCatalogos)
                         if(funcion.getNidCatalogo().compareTo(Constantes.CATALOGO_FUNCION_RESPONSABLE)==0) {
                             LOG.info("--- REMOVIENDO RESPONSABLE --- ");

                             listaCatalogos.remove(funcion);
                             break;
                         }
        
        return  listaCatalogos;
    }
   
    
    /**
     * Cambiar el tipo de busqueda
     * @param evt - TabChangeEvent - evento generado al cambiar el TabView
     */
    public void cambiarBusqueda(TabChangeEvent evt) {
        TabView tv = (TabView) evt.getComponent();
        this.indexTab = tv.getActiveIndex();
    }
    
    /**
     * busqueda privada de DNAs segun criterios
     */
    public void buscar() {
        
        this.inscripcionSeleccionada = null;
        this.inscripcion = new Inscripcion();
        ParametroNodoObject param = new ParametroNodoObject();
                
        if(this.busTipo.equals("1")) {
            
            if(this.indexTab == 0 && this.busCodigo!=null ) {
                this.listaDefensoria = this.defensoriaFacade.findAllByField("txtConstancia", this.busCodigo);
                return;
            }
            
            if(this.indexTab == 1) {
                if(this.busDepartamento!=null) 
                    param.adicionar("nidDepartamento", this.busDepartamento);
                if(this.busProvincia!=null)
                    param.adicionar("nidProvincia", this.busProvincia);
                if(this.busDistrito!=null)
                    param.adicionar("nidDistrito", this.busDistrito);
            }
            if(this.indexTab == 2 && this.busEstado!=null) {
                Catalogo estadoBus = new Catalogo();
                estadoBus.setNidCatalogo(this.busEstado);
                param.adicionar("estado", estadoBus);
            }
            
            if(param.getParametros().size()>0) 
               this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosObject(param, true, "txtNombre", true);

        } else {
           
           if(this.indexTab == 0 && this.busCodigo!=null ) {
                 this.lista = this.inscripcionFacade.filtrarPorConstancia( this.busCodigo );
                 return;
           } 

           if(this.indexTab == 1) {
                if(this.busDistrito!=null) {
                    this.lista = this.inscripcionFacade.filtrarDistritos(this.busDistrito);
                    return;
                }
                if(this.busProvincia!=null) {
                    this.lista = this.inscripcionFacade.filtrarProvincias(this.busProvincia);
                    return;
                }
                if(this.busDepartamento!=null) {
                    this.lista = this.inscripcionFacade.filtrarDepartamentos(this.busDepartamento);
                    return;
                }
           }

           if(this.indexTab == 2 && this.busEstado!=null) {
                Catalogo estadoBus = new Catalogo();
                estadoBus.setNidCatalogo(this.busEstado);
                param.adicionar("estado", estadoBus);
                this.lista = this.inscripcionFacade.obtenerPorParametrosObject(param, true, "fecRegistro", true);
           }
           
        }
 
    }
    
    /**
     * busca las DNA con estado NUEVO y DENEGADA para la inscripción externa
     */
    public void buscarExterno() {
        
        this.inscripcionSeleccionada = null;
        this.inscripcion = new Inscripcion();
       
        Catalogo estadoNuevo = new Catalogo();
        estadoNuevo.setNidCatalogo(Constantes.CATALOGO_DNA_NUEVO);
        Catalogo estadoRegistrada = new Catalogo();
        estadoRegistrada.setNidCatalogo(Constantes.CATALOGO_DNA_REGISTRADA);
     
        ParametroNodo param = new ParametroNodo();
        param.adicionar("flgActivo", BigInteger.ONE);
        
        if(this.indexTab == 0 && this.busCodigo!=null ) {
             param.adicionar("txtConstancia",  this.busCodigo);
             this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosAndEstado(param, estadoNuevo, estadoRegistrada);
             return;
        }

        if(this.indexTab == 1) {
            if(this.busDepartamento!=null) 
                param.adicionar("nidDepartamento", this.busDepartamento);
            if(this.busProvincia!=null)
                param.adicionar("nidProvincia", this.busProvincia);
            if(this.busDistrito!=null)
                param.adicionar("nidDistrito", this.busDistrito);
            
            this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosAndEstado(param, estadoNuevo, estadoRegistrada);
       
        }

      
          
    }
    

    
    /**
     * Obtener nombre departamento
     * @return String
     */
    public String getNombreDepartamento() {
        if(this.inscripcion.getDna() == null || this.inscripcion.getDna().getNidDepartamento() == null){ return "";}
        return (this.inscripcion.getDna().getNidDepartamento()!=null)?departamentoFacade.find(this.inscripcion.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre provincia
     * @return String
     */
    public String getNombreProvincia() {
        if(this.inscripcion.getDna() == null || this.inscripcion.getDna().getNidProvincia() == null){ return "";}
        return (this.inscripcion.getDna().getNidProvincia()!=null)?provinciaFacade.find(this.inscripcion.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre disstrito
     * @return String
     */
    public String getNombreDistrito() {
        if(this.inscripcion.getDna() == null || this.inscripcion.getDna().getNidDistrito() == null){ return "";}
        return (this.inscripcion.getDna().getNidDistrito()!=null)?distritoFacade.find(this.inscripcion.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener departamentos
     * @return lista de departamentos
     */
    public List<Departamento> obtenerDepartamentos() {
        if(this.personaDna == null){
            this.personaDna = new PersonaDna();
            this.personaDna.setFuncion(new Catalogo());
            this.personaDna.setNidDepartamento(BigDecimal.ZERO);
            this.personaDna.setNidProvincia(BigDecimal.ZERO);
            this.personaDna.setNidDistrito(BigDecimal.ZERO);
        }
        return  departamentoFacade.obtenerDepartamentos();
    }

   
    
    /**
     * Obtener provincias
     */
    public void obtenerProvinciasDna() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.personaDna.getNidDepartamento());
        this.provinciasDna = provinciaFacade.obtenerProvincias(departamento);
    }
    
    /**
     * Obteer distritos
     */
    public void obtenerDistritosDna() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.personaDna.getNidProvincia());
        this.distritosDna = distritoFacade.obtenerDistritos(provincia);
    }
    
    /**
     * Obtener provincias
     */
     public void obtenerProvinciasBus() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.busDepartamento);
        this.provinciasBus = provinciaFacade.obtenerProvincias(departamento);
    }
    
     /**
      * Obtener distritos
      */
    public void obtenerDistritosBus() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.busProvincia);
        this.distritosBus = distritoFacade.obtenerDistritos(provincia);
    }
    
    /**
     * Eliminar PersonaDna del personal
     */
    public void eliminarPersonal() { 
      this.listaPersonal.remove(this.personaDna);
      this.personalRemover.add(this.personaDna);
      if(this.esResponsable(this.personaDna)){
            this.adicionarMensajeError("Se removió Responsable", "Debe agregar un nuevo responsable.");
        }
    }
    
    /**
     * Verifica si es el responsable
     * @return true si existe responsable en la lista del personal
     */
    public boolean verificaResponsable() {
        LOG.info("--- Verificando Responsable --- ");
        
        for(PersonaDna personal : this.listaPersonal)
          if(personal.getFuncion() != null && 
                 personal.getFuncion().getNidCatalogo() != null &&
                     personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)) {
              LOG.info("--- Existe Responsable --- ");
               return true;
          }
           
        
        return false;
    }
    
    /**
     * Obtener personaldna de la inscripcion
     * @param item - PersonaDna
     */
    public void obtenerPersonalDna(PersonaDna item) {
        try {
            this.personaDnaOriginal = item;
            this.personaDna = item.clonar();
            this.obtenerProvinciasDna();
            this.obtenerDistritosDna();       
        }catch(Exception ex) {
            
        }    
    }
    
    /**
     * Renovar los estados 
     * @param event - ComponentSystemEvent - evento posterior a la validación de componentes
     */
    public void reset(final ComponentSystemEvent event) {
        /*
        FacesContext fc = FacesContext.getCurrentInstance();
        final UIComponent formulario = event.getComponent();
        final UIInput estado = (UIInput) formulario.findComponent("txtEstadoBus");
        estado.resetValue();*/
        this.busEstado = BigInteger.ZERO;
    }
    /**
     * Agrega Persona a la lista de personas que se muestra en la inscripción
     */
    public void agregarPersonal() {
      try {
        this.personaDna.setFuncion(this.catalogoFacade.find(this.personaDna.getFuncion().getNidCatalogo()));
        this.personaDna.setFlgActivo(BigInteger.ONE);
        this.listaPersonal.add(this.personaDna);
      }catch(Exception ex) {
      }
    }
    
    /**
     * Actualizar el personal
     */
    public void actualizarPersonal() {
        this.personaDna.setFuncion(this.catalogoFacade.find(this.personaDna.getFuncion().getNidCatalogo()));
        this.personaDnaOriginal.actualizar(this.personaDna);
    }
    
    
   
    
  
    /**
     * Enviar Correo
     * @param pass
     */
     public void enviarCorreo(String pass) {
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarCorreoCredenciales(this.responsable.getNombresApellidos(), this.responsable.getTxtCorreo(), pass, correoDnaAdministrado);
    } 
     
     
         
     /**
      * Genera la contraseña del usuario
      * @return la contraseña
      */
     public String generaPassword() {
         return PasswordUtil.generatePassword(2, PasswordUtil.ALPHA_CAPS) + PasswordUtil.generatePassword(2, PasswordUtil.ALPHA) + PasswordUtil.generatePassword(2, PasswordUtil.SPECIAL_CHARS) + PasswordUtil.generatePassword(2, PasswordUtil.NUMERIC);
            
     }
   
    
     /**
      * Crear usuario ligado a la PersonaDna Responsable
      * @param pass
      * @return Usuario
      */
    public Usuario crearUsuario(String pass) {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
          
            TipoDocumento tipoDoc = new TipoDocumento();
            tipoDoc.setNidTipoDocumento(BigDecimal.valueOf(1));

            Persona persona = new Persona();
            persona.setTipoDocumento(tipoDoc);
            persona.setTxtDocumento(this.responsable.getTxtDocumento());
            persona.setTxtApellidoPaterno(this.responsable.getTxtApellidoPaterno());
            persona.setTxtApellidoMaterno(this.responsable.getTxtApellidoMaterno());
            persona.setTxtNombres(this.responsable.getApellidosNombres());
            persona.setFlgActivo(BigInteger.ONE);
            persona.setTxtPc(Internet.obtenerNombrePC());
            persona.setTxtIp(Internet.obtenerIPPC());
            persona.setFecEdicion(new Date());
            this.personaFacade.create(persona);


            Usuario usuario = new Usuario();
            usuario.setPersona(persona);
            usuario.setTxtUsuario(this.responsable.getTxtCorreo());
            usuario.setPersona(persona);
            usuario.setFlgActivo(BigInteger.ONE);
            usuario.setTxtPc(Internet.obtenerNombrePC());
            usuario.setTxtIp(Internet.obtenerIPPC());
            usuario.setFecEdicion(new Date());
          
            String encriptado = usuarioAdministrado.encriptar(pass);
            usuario.setTxtPassword(encriptado);

            List<EstadoUsuario> estadousuario = this.estadoUsuarioFacade.findAllByField("txtEstadoUsuario", "APROBADO");
            if (null != estadousuario) {
                usuario.setNidEstadoUsuario(estadousuario.get(0).getNidEstadoUsuario().toBigInteger());
            }
            
            this.usuarioFacade.create(usuario);
            
            PerfilUsuarioPK pk = new PerfilUsuarioPK();
            List<Perfil> perfiles = this.perfilFacade.obtenerPerfilesDelModulo(new Modulo(Constantes.NID_MODULO));
            Perfil perfilTmp = null;
            for(Perfil perfil: perfiles)
                if(perfil.getTxtPerfil().equals(Constantes.PERFIL_RESPONSABLE))
                    perfilTmp = perfil;
            
            pk.setNidPerfil(BigInteger.valueOf(perfilTmp.getNidPerfil().longValue()));
            pk.setNidUsuario(usuario.getNidUsuario().toBigInteger());

            PerfilUsuario perfilUsuario = new PerfilUsuario();
            perfilUsuario.setPerfilUsuarioPK(pk);
            perfilUsuario.setPerfil(perfilTmp);

            perfilUsuario.setUsuario(usuario);
            perfilUsuario.setNidUsuario2(BigInteger.ONE);
            perfilUsuario.setFlgActivo(new Short("1"));
            perfilUsuario.setTxtPc(Internet.obtenerNombrePC());
            perfilUsuario.setTxtIp(Internet.obtenerIPPC());
            perfilUsuario.setFecModificacion(new Date());
            this.perfilUsuarioFacade.create(perfilUsuario);

            return usuario;
        
        }catch(Exception ex) {
            adicionarMensajeError("No pudo generar el usuario","");
        }  
        return null;
    }
     
   
     /**
     * Valida si se ingresó un responsable y los archivos solicitados
     * @param event - ComponentSystemEvent - evento posterior a la validación de componentes
     */
    public void validar(final ComponentSystemEvent event) {
            FacesContext fc = FacesContext.getCurrentInstance();
     
            this.responsable = null;
           
            if(this.listaPersonal != null && !this.listaPersonal.isEmpty()){
                for (PersonaDna personal : this.listaPersonal) 
                    if(personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)) this.responsable = personal;
            }
            if(this.responsable == null){
                adicionarMensajeError("Debe agregar un responsable","");
                fc.validationFailed();
                fc.renderResponse(); 
            }           

            for(DocInscripcion doc : this.adjuntos) {
            
                if(doc.getTxtNombre()==null){
                    adicionarMensajeError("Debe adjuntar los documentos solicitados","");
                    fc.validationFailed();
                    fc.renderResponse(); 
                    break;
                }
            }            
    }
     
    /**
     * Preparar para el guardado
     * @param pass
     */
    public void prepararSave(String pass) {
       
        try {
         
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
       
            for(DocInscripcion doc : this.adjuntos) {
                doc.setInscripcion(this.inscripcion);
                doc.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                doc.setTxtPc(Internet.obtenerNombrePC());
                doc.setTxtIp(Internet.obtenerIPPC());
                doc.setFecRegistro(new Date());
                doc.setFlgActivo(BigInteger.ONE);
            }
        
            this.inscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.inscripcion.setTxtIp(Internet.obtenerIPPC());
            this.inscripcion.setFlgActivo(BigInteger.ONE);
     
            List<PersonaDna> listaFinal = new ArrayList<>();
            
            for (PersonaDna personal : this.listaPersonal) {
                personal.setTxtPj(this.verificarPJPenales(personal));
                personal.setTxtInpe(this.verificarInpeJudiciales(personal));
                personal.setTxtPnp(this.verificarPnp(personal));
                personal.setInscripcion(this.inscripcion);
                
                if(personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE))
                    this.responsable = personal;
                else
                    personal.setTxtDocDesignacion(null);
                
                personal.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                personal.setTxtPc(Internet.obtenerNombrePC());
                personal.setTxtIp(Internet.obtenerIPPC());
                personal.setFecRegistro(new Date());
                personal.setFlgActivo(BigInteger.ONE);
                listaFinal.add(personal);
            }
            
            // cambiar a inactivo el personal removido
            for (PersonaDna personal : this.personalRemover) {
                personal.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                personal.setTxtPc(Internet.obtenerNombrePC());
                personal.setTxtIp(Internet.obtenerIPPC());
                personal.setFecModificacion(new Date());
                personal.setFlgActivo(BigInteger.ZERO);
                listaFinal.add(personal);
                /*if(personal.getNidPersona() != null && personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)){
                    this.cambiarDeResponsable(this.listaPersonal,this.inscripcion);
                }*/
            }
            
            this.inscripcion.setPersonal(listaFinal);
            this.inscripcion.setDocumentos(this.adjuntos);
           
            if(this.inscripcion.getNidInscripcion()!=null) {
                
                Usuario usuarioTmp = this.usuarioFacade.find(this.inscripcion.getNidUsuario());
                
                if(!usuarioTmp.getTxtUsuario().equals(this.responsable.getTxtCorreo())) {
                    usuarioTmp.setFlgActivo(BigInteger.ZERO);
                    this.usuarioFacade.edit(usuarioTmp);
                    
                    Usuario usuario = this.crearUsuario(pass);
                    this.inscripcion.setNidUsuario(usuario.getNidUsuario());
                }
            } else {
                Usuario usuario = this.crearUsuario(pass);
                this.inscripcion.setNidUsuario(usuario.getNidUsuario());
            }

        } catch (Exception e) {
            adicionarMensajeError("Error", "Error al tratar de guardar la Solicitud de Inscripción");
        }
       
    } 
    
    
  
    /**
     * modifica el estado de la inscripción "POR EVALUAR" y revisa la PIDE
     * 
     */     
    public void create() {
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
           
        this.inscripcion.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
        this.inscripcion.setFecRegistro(new Date());
        this.inscripcion.setEstado(this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_POR_EVALUAR)); 
         
        String pass = this.generaPassword();
             
        this.prepararSave(pass);
        try {
            this.inscripcionFacade.create(this.inscripcion);
            this.inscripcion.getDna().setEstado(this.inscripcion.getEstado());
            this.defensoriaFacade.edit(this.inscripcion.getDna());
            this.verMensajeOK = true;
            this.modo = Constantes.MODO_LISTADO;
            
            adicionarMensaje("","La solicitud de inscripción ha sido registrada exitósamente");

        } catch (Exception e) {
            adicionarMensajeError("Error","Error al guardar la Solicitud de Inscripción");
        }
       
        this.enviarCorreo(pass);
          
    }
  
    /**
     * Actualizar la inscripción
     */
    public void update() {
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
           
        this.inscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
        this.inscripcion.setFecModificacion(new Date());
        this.inscripcion.setDocumentos(this.adjuntos);
        
        String pass = this.generaPassword();
        
        this.prepararSave(pass);
        try {
            this.inscripcionFacade.edit(this.inscripcion);
             this.verMensajeOK = true;
              adicionarMensaje("","La solicitud de inscripción ha sido actualizada exitósamente");
        } catch (Exception e) {
            adicionarMensajeError("Error","Error al guardar la Solicitud de Inscripción");
        }
        
        this.enviarCorreo(pass);
          
       
    }
    
    /**
     * Separar el nombre obtenido por reniec
     * @param nombresFull - String - Nombres y Apellidos
     */
    public void separarNombres(String nombresFull) {
    
        String nombres[] = nombresFull.split(" ");

        int i = 0;    
        List<String> listaNombres = new ArrayList<>();
        boolean compuesto = false;    
        
        for(String n: nombres) {
            if(n.equals("Y") || n.equals("DE") || n.equals("DEL") || n.equals("LA") || n.equals("EL")) {
                compuesto = true;
                listaNombres.set(i-1, listaNombres.get(i-1) + " " + n);
            }  else {
                if(compuesto) {
                    compuesto = false;
                    listaNombres.set(i-1, listaNombres.get(i-1) + " " + n);
                } else {
                    listaNombres.add(n);
                    i++;
                }
            }    
           
        }
        
        this.personaDna.setTxtNombre1(listaNombres.get(0));

        if(listaNombres.size()>1)
          this.personaDna.setTxtNombre2(listaNombres.get(1));

        if(listaNombres.size()>2)
          this.personaDna.setTxtNombre3(listaNombres.get(2));

    }
    /**
     * validador de correo, verifica que no existan correos ( usuarios ) de responsable duplicados
     * @param fc - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
     */
    public void validarCorreo(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
       
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
   
        String correo = (String)value;
        Matcher matcher = pattern.matcher(correo);
        
        if(!matcher.matches())
            throw new ValidatorException(new FacesMessage("El correo electrónico no dispone de un formato válido"));
      
      
        ParamFiltro nodo = new ParamFiltro();
        nodo.adicionar("txtCorreo", correo);
        Catalogo funcion = new Catalogo();
        funcion.setNidCatalogo(Constantes.CATALOGO_FUNCION_RESPONSABLE);
        nodo.adicionar("funcion", funcion);
        
        if(this.personaDna.getInscripcion()!=null &&
                this.personaDna.getInscripcion().getNidInscripcion()!=null)
             nodo.adicionar("inscripcion", this.personaDna.getInscripcion(), ParamFiltro.NOTEQUAL);
        
        List<PersonaDna> listaTmp = this.personaDnaFacade.obtenerPorFiltro(nodo, true, "txtCorreo", false);
        
        if(listaTmp!=null && listaTmp.size()>0) {
            Defensoria dTmp = listaTmp.get(0).getInscripcion().getDna();
            throw new ValidatorException(new FacesMessage("El correo electrónico ya ha sido registrado por otro usuario en la DNA " + dTmp.getTxtConstancia() + " - " + dTmp.getTxtNombre()));
        }
      
    }
    
    /**
     * Verificar el proceso de busqueda de dni
     * @param ctx - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
   */
    public void verificarReniec(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
         IdentificacionReniec identificacionReniec = new IdentificacionReniec();
       
        String dni = (String)value;
          
        if(dni==null || dni.length()<8)
              throw new ValidatorException(new FacesMessage("El DNI debe tener 8 dígitos"));

        if(!dni.equals(this.personaDna.getTxtDocumento())) {
            try { 

                for(PersonaDna personal : this.listaPersonal){
                    if(personal.getTxtDocumento().equals(dni)) {
                        adicionarMensajeError("Error","El DNI ya ha sido ingresado.");
                        this.initPersona();
                        return;
                   }
                }

                this.dniValido = false;
                this.initPersona();
            
                identificacionReniec.obtenerConsultaReniec(dni);
                
                if (identificacionReniec.getNOMBRES() != null) {
                  this.dniValido = true;

                  this.personaDna.setTxtDocumento(dni);
                  this.personaDna.setTxtApellidoPaterno(identificacionReniec.getAPPAT());
                  this.personaDna.setTxtApellidoMaterno(identificacionReniec.getAPMAT());
                  this.personaDna.setTxtDireccion(identificacionReniec.getDIRECCION());
                
                  this.separarNombres(identificacionReniec.getNOMBRES()); 
                  
                  String[] ubigeo = identificacionReniec.getUBIGEO().split("/");
                  List<Departamento> ldep = departamentoFacade.findAllByField("txtDescripcion", ubigeo[0]);
                  if(!ldep.isEmpty()) {

                    this.personaDna.setNidDepartamento(ldep.get(0).getNidDepartamento());
                    this.obtenerProvinciasDna();

                    if(this.personaDna.getNidDepartamento().compareTo(BigDecimal.valueOf((long)7))==0) {
                        this.personaDna.setNidProvincia(BigDecimal.valueOf((long)67));
                        this.obtenerDistritosDna();

                        List<Distrito> ldist = distritoFacade.findAllByField("txtDescripcion", ubigeo[1]);
                        if(!ldist.isEmpty())
                            this.personaDna.setNidDistrito(ldist.get(0).getNidDistrito());

                    } else {

                        List<Provincia> lprov = provinciaFacade.findAllByField("txtDescripcion", ubigeo[1]);
                        if(!lprov.isEmpty()) {
                            this.personaDna.setNidProvincia(lprov.get(0).getNidProvincia());
                            this.obtenerDistritosDna();
                        }
                        List<Distrito> ldist = distritoFacade.findAllByField("txtDescripcion", ubigeo[2]);
                        if(!ldist.isEmpty())
                            this.personaDna.setNidDistrito(ldist.get(0).getNidDistrito());

                   }
                  }
                }  else {
                     throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
                }
                
            } catch (Exception ex) {
                // TODO handle custom exceptions here
            }
            
        }

    }
    
    /**
     * Verificar Inpe
     * @param personalDna - PersonaDna
     * @return String
     */
    public String verificarInpeJudiciales(PersonaDna personalDna) {
     
        try { 
            
            AntecedenteJudicial antecedenteJudicial = new AntecedenteJudicial();

  
            String apepat = personalDna.getTxtApellidoPaterno();
            String apemat = personalDna.getTxtApellidoMaterno();
            String nombres = personalDna.getApellidosNombres();
            
            antecedenteJudicial.obtenerAntJudicial(apepat, apemat, nombres);
            return antecedenteJudicial.getRESPUESTA();
            
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }
    
    /**
     * Verificar Pj Penales
     * @param personalDna - PersonaDna
     * @return String
     */
    public String verificarPJPenales(PersonaDna personalDna){
        
        try { 
           
             AntecedentePenal antecedentePenal = new AntecedentePenal();
            String xApellidoPaterno = personalDna.getTxtApellidoPaterno();
            String xApellidoMaterno = personalDna.getTxtApellidoMaterno();
            String xNombre1 = personalDna.getTxtNombre1();
            String xNombre2 = (personalDna.getTxtNombre2()!=null)?personalDna.getTxtNombre2():"";
            String xNombre3 = (personalDna.getTxtNombre3()!=null)?personalDna.getTxtNombre3():"";
            String xDni = personalDna.getTxtDocumento();
            
            antecedentePenal.obtenerAntPenal(xApellidoPaterno, xApellidoMaterno, xNombre1, xNombre2, xNombre3, xDni);
            return antecedentePenal.getRESPUESTA();
        
        } catch (Exception ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
            return "ERROR";
        }

    }
    
    /**
     * Verificar Pnp
     * @param personalDna - PersonaDna
     * @return String
     */
    public String verificarPnp(PersonaDna personalDna){
        
        String vDNI = personalDna.getTxtDocumento();
    
        try {
            AntecedentePolicial antecedentePolicial = new AntecedentePolicial();

            antecedentePolicial.obtenerAntPolicial(vDNI);
            return antecedentePolicial.getRESPUESTA();
            
        } catch (Exception e) {
            return "ERROR";
        }
        
    }

    /**
     * Metodo que se encarga de la subida de archivos
     * @param event - FileUploadEvent - evento generado al subir los archivos
     */   
    public void handleFileUpload(FileUploadEvent event) {
        try {
            String tiempo = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            String name = FilenameUtils.getBaseName(event.getFile().getFileName()); 
            String ext = FilenameUtils.getExtension(event.getFile().getFileName()); 
            
            String fileName = name.concat(tiempo).concat(".").concat(ext);
            
            Archivo archivo = new Archivo(event.getFile(), BigDecimal.ZERO, "archivos");
	    Archivo.subirArchivo(archivo.getArchivoArreglo(), this.ruta.concat(fileName), this.ruta);
                        
            DocInscripcion docInscripcion = (DocInscripcion)event.getComponent().getAttributes().get("rowAdjunto");
            // si está reemplazando el archivo entonces se borra el antiguo
            if(docInscripcion.getTxtNombre() != null) {
              Files.deleteIfExists(Paths.get(this.ruta.concat(docInscripcion.getTxtNombre())));
            }
            
            docInscripcion.setTxtNombre(fileName);
        
            adicionarMensaje("","Carga de Archivo: " + event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al subir archivo", ex.getMessage());
        }
    }
    
    /**
     * Subir la constancia
     * @param event - FileUploadEvent - evento generado al subir los archivos
     */
    public void uploadConstancia(FileUploadEvent event) {
        try {
            
            String ext = FilenameUtils.getExtension(event.getFile().getFileName()); 
            String nroConstancia = this.inscripcion.getDna().getTxtConstancia() != null ? this.inscripcion.getDna().getTxtConstancia() : "";
            String fileName = Constantes.PREFIX_INSCRIPCION.
                    concat(Constantes.PREFIX_FIRMADO).
                    concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                    concat(".").concat(ext);
            File fileUpload = new File(this.rutaConstancias, fileName);
         
            OutputStream out = new FileOutputStream(fileUpload);
            out.write(event.getFile().getContents());
            out.close();
            
            this.inscripcion.setFlagConstancia(1);
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al subir archivo", ex.getMessage());
        }
    }
    /**
     * Subir el archivo de oficio
     * @param event - FileUploadEvent - evento generado al subir los archivos
    */
     public void uploadOficio(FileUploadEvent event) {
        try {
            
            String fileName = Constantes.PREFIX_OFICIO.
                    concat(this.inscripcion.getDna().getTxtConstancia()).
                    concat(".").concat(Constantes.EXTENSION_PDF);
            File fileUpload = new File(this.rutaConstancias, fileName);
         
            OutputStream out = new FileOutputStream(fileUpload);
            out.write(event.getFile().getContents());
            out.close();
            
            this.inscripcion.setFlagOficio(1);
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al subir archivo", ex.getMessage());
        }
    }
     
     /**
      * Enviar la constancia al correo del Responsable
      */
    public void enviarConstancia() {
        
        try {
            this.inscripcion.setFlagConstancia(1);
            this.inscripcionFacade.edit(this.inscripcion);
        }catch(Exception ex) {
            adicionarMensajeError("Error", "Error al tratar de guardar.");
        }
        
        String nroConstancia = this.inscripcion.getDna().getTxtConstancia() != null ? this.inscripcion.getDna().getTxtConstancia() : "";
        String fileName = Constantes.PREFIX_INSCRIPCION.
                    concat(Constantes.PREFIX_FIRMADO).
                    concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                    concat(".").concat(Constantes.EXTENSION_PDF);
       
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarConstancia(Constantes.TPL_CONSTANCIA_INSCRIPCION, 
                                            this.responsable.getNombresApellidos(), 
                                            this.responsable.getTxtCorreo(),
                                            this.inscripcion.getDna().getTxtNombre(),  this.rutaConstancias + fileName, correoDnaAdministrado);    
    } 
    
     /**
      * Actualiza el flag de oficio de denegación y envía la notificación al correo del responsable
      */
    public void enviarOficio() {
        try {
            this.inscripcion.setFlagOficio(1);
            this.inscripcionFacade.edit(this.inscripcion);
        }catch(Exception ex) {
            adicionarMensajeError("Error", "Error al guardar.");
        }
        
        String fileName = Constantes.PREFIX_OFICIO.
                  concat(this.inscripcion.getDna().getTxtConstancia()).
                  concat(".").concat(Constantes.EXTENSION_PDF);
        
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        /*
        this.correoService.enviarOficio(Constantes.TPL_OFICIO_DENEGACION,
                                        this.responsable.getNombresApellidos(), 
                                        this.responsable.getTxtCorreo(),
                                        this.inscripcion.getDna().getTxtNombre(), this.rutaConstancias + fileName, correoDnaAdministrado);   
        */
    } 
    
    /**
     * Verificar si la PersonaDna es el responsable
     * @param persona - PersonaDna
     * @return Boolean
     */
    private Boolean esResponsable(PersonaDna persona){
        return persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE);
    }
    
    /**
     * Obtener el Responsable
     * @param personal - lista de personal de DNA
     * @return PersonaDna
     */
    private PersonaDna obtenerResponsable(List<PersonaDna> personal){
        PersonaDna responsable = null;
        for(PersonaDna persona : personal){
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                responsable = persona;
                break;
            }
        }
        return responsable;
    }
    
    /**
     * Obtener el personal activo de la Acreditacion
     * @param personal - lista de personal de DNA
     * @return lista de personal activo de DNA
     */
    private List<PersonaDna> obtenerPersonalActivo(List<PersonaDna> personal){
        List<PersonaDna> personas = new ArrayList<>();
        for(PersonaDna persona : personal) {
             if(persona.getFlgActivo().equals(BigInteger.ONE))personas.add(persona);
        }
        return personas;
    }
    
    /**
     * Obtener el PersonalActivo de la acreditacion a Evaluar
     * @param personal - lista de personal de DNA
     * @return lista de personal de DNA
     */
    private List<PersonaDnaEval> obtenerPersonalActivoEval(List<PersonaDnaEval> personal){
        List<PersonaDnaEval> personas = new ArrayList<>();
        for(PersonaDnaEval persona : personal) {
             if(persona.getFlgActivo().equals(BigInteger.ONE))personas.add(persona);
        }
        return personas;
    }
    
    /**
     * Obtenere los tipos de documentos a adjuntar
     * @param inscripcion - Inscripcion
     * @return lista de documentos de inscripción
     */
    private List<DocInscripcion> obtenerAdjuntos(Inscripcion inscripcion){
        List<DocInscripcion> salida  = new ArrayList<>();
        if(inscripcion.getEstado().getNidCatalogo() != Constantes.CATALOGO_ESTADO_NUEVO)  
           salida = inscripcion.getDocumentos() != null ? inscripcion.getDocumentos(): new ArrayList<DocInscripcion>();
        return salida;
    }
    
    /**
     * Inicializar PersonaDna 
     */
   public void initPersonaSubsanar(){
       this.personaDna = new PersonaDna();
       this.personaDna.setFuncion(new Catalogo());
       this.personaDna.setProfesion(new Catalogo());
   }
   
   /**
    * Ibtener persona subsanar
    * @param item  - PersonaDna
    */
   public void obtenerPersonaSubsanar(PersonaDna item){
       this.personaDna = item;
   }
   
   /**
    * Validar persona
    * @param event - ComponentSystemEvent - evento generada posterior a la validación de componentes
    */
   public void validarPersona(final ComponentSystemEvent event) {
       
        FacesContext fc = FacesContext.getCurrentInstance();
        final UIComponent formulario = event.getComponent();
        final UIInput nidFuncion = (UIInput) formulario.findComponent("nidFuncion");

        if(nidFuncion.getValue()!=null && nidFuncion.getValue().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)  ) {
            for(PersonaDna personal : this.listaPersonal){
                /*
                if( ( this.personaDna.getNidPersona()==null || 
                        (this.personaDna.getNidPersona()!=null && !personal.getNidPersona().equals(this.personaDna.getNidPersona())))
                         && personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) ) {
                            adicionarMensajeError("Error","Ya existe un responsable. Si desea asignar la función de Responsable primero debe remover el anterior");
                            fc.validationFailed();
                            fc.renderResponse(); 
               }
                */
                if( !personal.getTxtDocumento().equals(this.personaDna.getTxtDocumento() ) 
                         && personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) ) {
                    adicionarMensajeError("Error","Ya existe un responsable. Si desea asignar la función de Responsable primero debe remover el anterior");
                    fc.validationFailed();
                    fc.renderResponse(); 
                }
            }
        }

    }
   
   /**
    * Validar Inscripcion a subsanar
    * @param event - ComponentSystemEvent - evento generada posterior a la validación de componentes
   */
   public void validarSubsanar(final ComponentSystemEvent event) {

        FacesContext fc = FacesContext.getCurrentInstance();
        
        if(this.listaPersonal != null && !this.listaPersonal.isEmpty()){
            PersonaDna responsableTmp = this.obtenerResponsable(this.listaPersonal);
            if(responsableTmp == null){
                adicionarMensajeError("Error","El personal debe contener un responsable por lo menos.");
                fc.validationFailed();
                fc.renderResponse(); 
            }
        }else{
            adicionarMensajeError("Error","El personal debe contener un responsable por lo menos.");
            
        }

    }
   
   /**
    * Validar persona a Subsanar
    * @param event - ComponentSystemEvent - evento generada posterior a la validación de componentes
    */
   public void validarPersonaSubsanar(final ComponentSystemEvent event) {
       
        FacesContext fc = FacesContext.getCurrentInstance();
        final UIComponent formulario = event.getComponent();
        final UIInput nidFuncion = (UIInput) formulario.findComponent("nidFuncion");
        
        PersonaDna responsableTmp = this.obtenerResponsable(this.listaPersonal);
        if(responsableTmp != null && 
            (this.personaDna.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && this.personaDna.getNidPersona() == null)){
                adicionarMensajeError("Error","Ya existe un responsable.");
                fc.validationFailed();
                fc.renderResponse(); 
        }
    }

    /**
     * Obtener la lista de inscripciones por Responsable
     * @return lista de inscripciones
     */
    public List<Inscripcion> obtenerListaPorResponsable() {
        try {
            AuthAdministrado authAdministrado = (AuthAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{authAdministrado}", AuthAdministrado.class); 
            if(authAdministrado.getDna()!=null)
                return this.inscripcionFacade.findAllByFieldOrder("dna", authAdministrado.getDna(), true, "fecRegistro", false);
            else
                return this.inscripcionFacade.findAllByFieldOrder("nidUsuario", authAdministrado.getAuthResponsable(), true, "fecRegistro", false);
            
        }catch(Exception ex){
        
        }
        return null;
    }
    
    /**
     * Obtener la inscripcion a subsanar
     * @param item - Inscripcion
     */
    public void obtenerInscripcionSubsanar(Inscripcion item) {
        this.modo = Constantes.MODO_SUBSANAR;
        this.inscripcionSubsanar = item;
        this.personalRemover = new ArrayList<>();
        this.listaPersonal = this.obtenerPersonalActivo(this.inscripcionSubsanar.getPersonal());
        this.adjuntos = this.obtenerAdjuntos(this.inscripcionSubsanar);
        this.responsableSubsanar = this.obtenerResponsable(this.listaPersonal);
        this.correoResponsable = this.responsableSubsanar.getTxtCorreo();
        //this.obtenerProvinciasSubsanar();
      //  this.obtenerDistritosSubsanar();
    }
    
    /**
     * Subsanar Inscripcion
     */
    public void subsanar() {
        
         try {
             
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
          
            Catalogo estado = new Catalogo();
            estado.setNidCatalogo(Constantes.CATALOGO_ESTADO_SUBSANADA);
            
            for(PersonaDna persona : this.personalRemover){
                persona.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                persona.setFlgActivo(BigInteger.ZERO);
                persona.setFecModificacion(new Date());
            }
             
            for(PersonaDna persona : this.listaPersonal){
                
                persona.setTxtPc(Internet.obtenerNombrePC());
                persona.setTxtIp(Internet.obtenerIPPC());
               
                if(persona.getNidPersona()== null){
                    persona.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    persona.setTxtPj(this.verificarPJPenales(persona));
                    persona.setTxtInpe(this.verificarInpeJudiciales(persona));
                    persona.setTxtPnp(this.verificarPnp(persona));
                    persona.setInscripcion(this.inscripcionSubsanar);
                     persona.setFecRegistro(new Date());
                    this.inscripcionSubsanar.getPersonal().add(persona);
                    
                    if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)){
                        if(!persona.getTxtCorreo().equals(this.correoResponsable)){
                            this.cambiarDeResponsable(this.listaPersonal, this.inscripcionSubsanar);
                        }
                    }
                    
                }else{
                    persona.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    persona.setFecModificacion(new Date());
                    
                }
            }
           
            
            this.inscripcionSubsanar.setEstado(estado);
            this.inscripcionSubsanar.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.inscripcionSubsanar.setTxtPc(Internet.obtenerNombrePC());
            this.inscripcionSubsanar.setTxtIp(Internet.obtenerIPPC());
            this.inscripcionSubsanar.setFecSubsanado(new Date());
            this.inscripcionSubsanar.setFecModificacion(new Date());
            this.inscripcionSubsanar.setFlgActivo(BigInteger.ONE);
            this.inscripcionFacade.edit(this.inscripcionSubsanar);
            
            // actualiza dna
            this.inscripcionSubsanar.getDna().setEstado(estado); //TODO
            this.defensoriaFacade.edit(this.inscripcionSubsanar.getDna());//TODO
            
            this.modo = Constantes.MODO_LISTADO;
            
         } catch (Exception e) {
            adicionarMensajeError("Error al tratar de subsanar la Solicitud de Inscripción", this.inscripcion.getDna().getTxtNombre());
         }
    }
    
    
    /**
     * Cambiar al responsable de la Inscripcion
     * @param personal - lista del persona de DNA
     * @param inscripcion - Inscripcion
     */
    private void cambiarDeResponsable(List<PersonaDna> personal , Inscripcion inscripcion){
        
        String pass = this.generaPassword();
           
        this.responsable = this.obtenerResponsable(personal);
        Usuario usuarioActual = this.usuarioFacade.find(inscripcion.getNidUsuario());
        usuarioActual.setFlgActivo(BigInteger.ZERO);
        this.usuarioFacade.edit(usuarioActual);
                
        Usuario usuarioNuevo = this.crearUsuario(pass);
        this.inscripcion.setNidUsuario(usuarioNuevo.getNidUsuario());
        this.enviarCorreo(pass);
    }
    
    /**
     * Metodo que se realiza al subsanar a la persona
     */
    public void subsanarPersona(){}
    
    /**
     * Metodo que se realiza al remover a la persona del personal
     */
    public void removerPersona(){
        this.personaRemover.setFlgActivo(BigInteger.ZERO);
        if(this.esResponsable(this.personaRemover)){
            this.adicionarMensajeError("Se removio Responsable", "Debe agregar un nuevo responsable.");
        }
        this.listaPersonal = this.obtenerPersonalActivo(this.listaPersonal);
    }
    /**
     * Remover a la PersonaDna de el personal
     * @param personaRemover
     */
    public void removerPersonaSubsanar(PersonaDna personaRemover){
        this.listaPersonal.remove(personaRemover);
        this.personalRemover.add(personaRemover);
        if(this.esResponsable(personaRemover)){
            this.adicionarMensajeError("Se removió Responsable", "Debe agregar un nuevo responsable.");
        }
    }
    
    /**
     * Agregar persona a personal de una acreditacion a subsanar
     */
    public void agregarPersonaSubsanar(){
        if(this.personaDna.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) &&
                this.obtenerResponsable(this.listaPersonal) != null){
                this.adicionarMensajeError("Error", "Ya existe Responsable. Debe agregar una nueva persona con otra función.");
            return;
        }
        PersonaDna personaAgregar = this.personaDna;
        Catalogo funcion = this.catalogoFacade.find(personaAgregar.getFuncion().getNidCatalogo());
        personaAgregar.setFuncion(funcion);
        personaAgregar.setFlgActivo(BigInteger.ONE);
        this.listaPersonal.add(personaAgregar);
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoInsVer() { 
        if(this.inscripcion == null || this.inscripcion.getDna() == null) return "";
        return (this.inscripcion.getDna().getNidDepartamento()!=null)?departamentoFacade.find(this.inscripcion.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre la provincia
     * @return String
     */
    public String getNombreProvinciaInsVer() {
        if(this.inscripcion == null || this.inscripcion.getDna() == null) return "";
        return (this.inscripcion.getDna().getNidProvincia()!=null)?provinciaFacade.find(this.inscripcion.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el distrito
     * @return String
     */
    public String getNombreDistritoInsVer() {
        if(this.inscripcion == null || this.inscripcion.getDna() == null) return "";
        return (this.inscripcion.getDna().getNidDistrito()!=null)?distritoFacade.find(this.inscripcion.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoVer() { 
        if(this.personaDna == null) return "";
        return (this.personaDna.getNidDepartamento()!=null)?departamentoFacade.find(this.personaDna.getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaVer() {
        if(this.personaDna == null) return "";
        return (this.personaDna.getNidProvincia()!=null)?provinciaFacade.find(this.personaDna.getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre de el distrito 
     * @return String
     */
    public String getNombreDistritoVer() {
        if(this.personaDna == null) return "";
        return (this.personaDna.getNidDistrito()!=null)?distritoFacade.find(this.personaDna.getNidDistrito()).getTxtDescripcion():"";
    }
    
    
    /**
     * Obtener la la fecha del curson con el formato MM/dd/yyyy
     * @return String
     */
    public String getFechaCursoTexto() {
        if(this.personaDna == null || this.personaDna.getTxtFechaCurso() == null) return "";
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(this.personaDna.getTxtFechaCurso());
        return date;
    }
    
    /**
     * Obtener la inscripcion a ver
     * @param inscripcion - Inscripcion
     */
    public void obtenerInscripcionVer(Inscripcion inscripcion){
        this.inscripcion = inscripcion;
        this.listaPersonal = this.obtenerPersonalActivo(inscripcion.getPersonal());
        if(inscripcion.getEstado().getNidCatalogo()!= Constantes.CATALOGO_ESTADO_NUEVO)  
           this.adjuntos = this.inscripcion.getDocumentos() != null ? this.inscripcion.getDocumentos(): new ArrayList<DocInscripcion>();
    } 
    
    /**
     * Obtener la PersonaDna para el panel Ver
     * @param persona - PersonaDna
     */
    public void obtenerPersonaVer(PersonaDna persona){
            this.personaDna = persona;
    }

    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoSub() {
        return (this.inscripcionSubsanar!=null &&
                this.inscripcionSubsanar.getDna()!=null && 
                    this.inscripcionSubsanar.getDna().getNidDepartamento()!=null)?departamentoFacade.find(this.inscripcionSubsanar.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaSub() {
        return (this.inscripcionSubsanar!=null &&
                this.inscripcionSubsanar.getDna()!=null && 
                this.inscripcionSubsanar.getDna().getNidProvincia()!=null)?provinciaFacade.find(this.inscripcionSubsanar.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del distrito
     * @return String
     */
    public String getNombreDistritoSub() {
        return (this.inscripcionSubsanar!=null &&
                this.inscripcionSubsanar.getDna()!=null && 
                this.inscripcionSubsanar.getDna().getNidDistrito()!=null)?distritoFacade.find(this.inscripcionSubsanar.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del Departamento
     * @return String
     */
    public String getNombreDepartamentoEvaluar() {
        return (this.inscripcion.getDna().getNidDepartamento()!=null)?departamentoFacade.find(this.inscripcion.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaEvaluar() {
        return (this.inscripcion.getDna().getNidProvincia()!=null)?provinciaFacade.find(this.inscripcion.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el distrito
     * @return String
     */
    public String getNombreDistritoEvaluar() {
        return (this.inscripcion.getDna().getNidDistrito()!=null)?distritoFacade.find(this.inscripcion.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoPersonaEvaluar() {
        return (this.personaDnaEval != null && this.personaDnaEval.getNidDepartamento()!=null)?departamentoFacade.find(this.personaDnaEval.getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaPersonaEvaluar() {
        return (this.personaDnaEval != null && this.personaDnaEval.getNidProvincia()!=null)?provinciaFacade.find(this.personaDnaEval.getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el distrito
     * @return String
     */
    public String getNombreDistritoPersonaEvaluar() {
        return (this.personaDnaEval != null &&  this.personaDnaEval.getNidDistrito()!=null)?distritoFacade.find(this.personaDnaEval.getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Validar si se puede evaluar la inscripcion
     * @param inscripcion - Inscripcion
     * @return Boolean
     */
    public Boolean sePuedeEvaluar(Inscripcion inscripcion){
        if(inscripcion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_SUBSANADA)
        || inscripcion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_POR_EVALUAR)){
            return false;
        }
        return true;
    }
    
    /**
     * Obtener el nombre del boton que sirve para evaluar
     * @return String
     */
    public String obtenerNombreBtnEvaluar(){
        String value = "Inscribir";
        if(this.flgDenegarInscripcion){
            value = "Denegar";
        }else{
            if(this.existeObservacionInscripcion(this.inscripcionEval))value = "Observar";
        }
        return value;
    }
    
    /**
     * Generar la InscripcionEval a partir de una Inscripcion
     * @param inscripcion - Inscripcion
     * @return InscripcionEval
     */
    private  InscripcionEval generarInscripcionEval(Inscripcion inscripcion){
         InscripcionEval inscripcionEval = new InscripcionEval();
         inscripcionEval.setInscripcion(inscripcion);
         inscripcionEval.setDna(inscripcion.getDna());
         inscripcionEval.setTxtDocumento(inscripcion.getTxtDocumento());
         inscripcionEval.setTxtDireccion(inscripcion.getTxtDireccion());
         inscripcionEval.setTxtCorreo(inscripcion.getTxtCorreo());
         inscripcionEval.setTxtTelefono(inscripcion.getTxtTelefono());
         inscripcionEval.setTxtGerencia(inscripcion.getTxtGerencia());
         inscripcionEval.setAmbientes(inscripcion.getAmbientes());
         inscripcionEval.setAmbientesPriv(inscripcion.getAmbientesPriv());
         inscripcionEval.setPresupuesto(inscripcion.getPresupuesto());
         inscripcionEval.setDias(inscripcion.getDias());
       //  inscripcionEval.setEstado(inscripcion.getEstado());
         inscripcionEval.setFlagConstancia(inscripcion.getFlagConstancia());
        List<PersonaDnaEval> personal = new ArrayList<>();
         for(PersonaDna persona : inscripcion.getPersonal()){
             PersonaDnaEval personaDnaEval = new PersonaDnaEval();
             personaDnaEval.setPersona(persona);
             personaDnaEval.setFlgActivo(persona.getFlgActivo());
             personaDnaEval.setInscripcionEval(inscripcionEval);
             personaDnaEval.setTxtDocumento(persona.getTxtDocumento());
             personaDnaEval.setTxtApellidoMaterno(persona.getTxtApellidoMaterno());
             personaDnaEval.setTxtApellidoPaterno(persona.getTxtApellidoPaterno());
             personaDnaEval.setTxtNombre1(persona.getTxtNombre1());
             personaDnaEval.setTxtNombre2(persona.getTxtNombre2());
             personaDnaEval.setTxtNombre3(persona.getTxtNombre3());
             personaDnaEval.setTxtSexo(persona.getTxtSexo());
             personaDnaEval.setTxtDireccion(persona.getTxtDireccion());
             personaDnaEval.setTxtCorreo(persona.getTxtCorreo());
             personaDnaEval.setInstruccion(persona.getInstruccion());
             personaDnaEval.setTxtTelefono(persona.getTxtTelefono());
             personaDnaEval.setProfesion(persona.getProfesion());
             personaDnaEval.setTxtColegio(persona.getTxtColegio());
             personaDnaEval.setTxtColegiatura(persona.getTxtColegiatura());
             personaDnaEval.setTxtLugarCurso(persona.getTxtLugarCurso());
             personaDnaEval.setTxtFechaCurso(persona.getTxtFechaCurso());
             personaDnaEval.setTxtInpe(persona.getTxtInpe());
             personaDnaEval.setTxtPj(persona.getTxtPj());
             personaDnaEval.setTxtPnp(persona.getTxtPnp());
             personaDnaEval.setFuncion(persona.getFuncion());
             personaDnaEval.setNidDepartamento(persona.getNidDepartamento());
             personaDnaEval.setNidProvincia(persona.getNidProvincia());
             personaDnaEval.setNidDistrito(persona.getNidDistrito());
             personaDnaEval.setEdad(persona.getEdad());
             personaDnaEval.setTxtDocDesignacion(persona.getTxtDocDesignacion());
             personal.add(personaDnaEval);
         }
         inscripcionEval.setPersonal(personal);
         return inscripcionEval;
    }
    
    /**
     * Inicializar el panel para la persona a evaluar
     * @param personaDnaEval - PersonaDnaEval
     */
    private void initPanelPersonaEvaluar(PersonaDnaEval personaDnaEval){
        this.flgEdadPersonaObs = false;
        this.flgProfesionPersonaObs = false;
        this.flgColegioPersonaObs = false;
        this.flgLugarPersonaObs = false;
        this.flgCorreoPersonaObs = false;
        this.flgDireccionPersonaObs = false;
        this.flgAntecedentePersonaObs = false;
        this.flgDocDesignacionObs = false;
        this.flgInstruccionObs = false;
        this.flgNroColegiaturaObs = false;
        this.flgFechaCursoObs = false;
        
        if(personaDnaEval.getObsEdad() != null && !personaDnaEval.getObsEdad().equals("")) this.flgEdadPersonaObs = true;
        if(personaDnaEval.getObsProfesion()!= null && !personaDnaEval.getObsProfesion().equals("")) this.flgProfesionPersonaObs = true;
        if(personaDnaEval.getObsColegio()!= null && !personaDnaEval.getObsColegio().equals("")) this.flgColegioPersonaObs = true;
        if(personaDnaEval.getObsCorreo()!= null && !personaDnaEval.getObsCorreo().equals("")) this.flgCorreoPersonaObs = true;
        if(personaDnaEval.getObsDireccion()!= null && !personaDnaEval.getObsDireccion().equals("")) this.flgDireccionPersonaObs = true;
        if(personaDnaEval.getObsLugarDeCurso()!= null && !personaDnaEval.getObsLugarDeCurso().equals("")) this.flgLugarPersonaObs = true;
        if(personaDnaEval.getObsAntecedentes()!= null && !personaDnaEval.getObsAntecedentes().equals("")) this.flgAntecedentePersonaObs = true;
        if(personaDnaEval.getObsDocDesignacion()!= null && !personaDnaEval.getObsDocDesignacion().equals("")) this.flgDocDesignacionObs = true;
        if(personaDnaEval.getObsInstruccion()!= null && !personaDnaEval.getObsInstruccion().equals("")) this.flgInstruccionObs = true;
        if(personaDnaEval.getObsColegiatura()!= null && !personaDnaEval.getObsColegiatura().equals("")) this.flgNroColegiaturaObs = true;
        if(personaDnaEval.getObsFechaDeCurso()!= null && !personaDnaEval.getObsFechaDeCurso().equals("")) this.flgFechaCursoObs = true;
    }
    
    /**
     * Obtener la persona a evaluar
     * @param personaEval - PersonaDnaEval
     */
    public void obtenerPersonaAEvaluar(PersonaDnaEval personaEval){
            this.personaDnaEval = personaEval;
            this.initPanelPersonaEvaluar(personaEval);
       
    }
    
    /**
     * Obtener la inscripcion a evaluar
     * @param item - Inscripcion
     */
    public void obtenerInscripcionAEvaluar(Inscripcion item) {
        this.modo = Constantes.MODO_EVALUACION;
        this.flgCorreoObs = false;
        this.flgDiasHoraObs = false;
        this.flgDireccionObs = false;
        this.flgGerenciaObs = false;
        this.flgNroAmbObs = false;
        this.flgNroAmbPrivObs = false;
        this.flgPresupuestoObs = false;
        this.inscripcion = item;
        this.flgDocObs = false;
        this.inscripcionEval = this.generarInscripcionEval(item);
        this.personasDnaEval = this.obtenerPersonalActivoEval(inscripcionEval.getPersonal());
    }
    
     /**
     * Obtener la inscripcion a evaluar
     * @param item - Inscripcion
     */
    public void obtenerInscripcionAEvaluarSubsanacion(Inscripcion item) {
        this.modo = Constantes.MODO_EVALUACION_SUBSANACION;
        this.inscripcion = item;
        this.listaPersonal = item.getPersonal();
    }
     
    /**
     * Verificar si existe observaciones en la inscripcion
     * @param inscripcionEval - InscripcionEval
     * @return Boolean
     */
    private Boolean existeObservacionInscripcion(InscripcionEval inscripcionEval){        
        Boolean salida = false;
        if(this.flgCorreoObs || this.flgDireccionObs || this.flgGerenciaObs || this.flgDiasHoraObs
                   || this.flgPresupuestoObs || this.flgNroAmbObs || this.flgNroAmbPrivObs || this.flgAdjuntosObs
                   || this.flgInstruccionObs || this.flgNroColegiaturaObs || this.flgFechaCursoObs || this.flgDocObs){
                   salida = true;
        }
        if(inscripcionEval.getPersonal() != null){
            for(PersonaDnaEval persona : inscripcionEval.getPersonal()){
                if(persona.getObsAntecedentes() != null || persona.getObsColegiatura() != null || persona.getObsColegio()!= null
                        || persona.getObsCorreo()!= null || persona.getObsDireccion()!= null || persona.getObsEdad()!= null
                        || persona.getObsFechaDeCurso()!= null || persona.getObsInstruccion()!= null || persona.getObsLugarDeCurso()!= null
                        || persona.getObsProfesion()!= null || persona.getObsDocDesignacion() != null){
                    salida = true;
                }
            }
        }
        return salida;
    }
    
    public void actualizarDefensoria() {
        Defensoria dna = this.inscripcion.getDna();
        DefensoriaInfo info =  dna.getDefensoriaInfo();
        if(info ==null) {
            info = new DefensoriaInfo();
            this.inscripcion.getDna().setDefensoriaInfo(info);
        }
        info.setFecInscrito(this.inscripcion.getFecInscrito());
        info.setTxtDocumento(this.inscripcion.getTxtDocumento());
        info.setTxtDireccion(this.inscripcion.getTxtDireccion());
        info.setTxtCorreo(this.inscripcion.getTxtCorreo());
        info.setTxtTelefono(this.inscripcion.getTxtTelefono());
        info.setTxtGerencia(this.inscripcion.getTxtGerencia());
        info.setPresupuesto(this.inscripcion.getPresupuesto());
        info.setAmbientes(this.inscripcion.getAmbientes());
        info.setAmbientesPriv(this.inscripcion.getAmbientesPriv());    
        info.setDias(this.inscripcion.getDias());
        info.setNidUsuario(this.inscripcion.getNidUsuario());
        Catalogo estado = this.inscripcion.getDna().getEstado();
        estado.setNidCatalogo(Constantes.CATALOGO_DNA_INSCRITA);
        dna.setEstado(estado);
        
        for(DefensoriaPersona p: dna.getListaPersonaDna()) {
            p.setFlgActivo(BigInteger.ZERO);
            p.setFecModificacion(new Date());
            p.setNidUsuarioMod(this.inscripcion.getNidUsuarioMod());
        }
            
        for(PersonaDna pd: this.inscripcion.getPersonal()) {
            DefensoriaPersona p = new DefensoriaPersona();
            p.setDefensoria(dna);
            p.setNidDepartamento(pd.getNidDepartamento());
            p.setNidProvincia(pd.getNidProvincia());
            p.setNidDistrito(pd.getNidDistrito());
            p.setTxtDocumento(pd.getTxtDocumento());
            p.setTxtApellidoPaterno(pd.getTxtApellidoPaterno());
            p.setTxtApellidoMaterno(pd.getTxtApellidoMaterno());
            p.setTxtNombre1(pd.getTxtNombre1());
            p.setTxtNombre2(pd.getTxtNombre2());
            p.setTxtNombre3(pd.getTxtNombre3());
            p.setTxtSexo(pd.getTxtSexo());
            p.setTxtTelefono(pd.getTxtTelefono());
            p.setTxtDocDesignacion(pd.getTxtDocDesignacion());
            p.setTxtCorreo(pd.getTxtCorreo());
            p.setTxtColegiatura(pd.getTxtColegiatura());
            p.setTxtColegio(pd.getTxtColegio());
            p.setTxtInpe(pd.getTxtInpe());
            p.setTxtPnp(pd.getTxtPnp());
            p.setTxtPj(pd.getTxtPj());
            p.setFlgActivo(BigInteger.ONE);
            p.setProfesion(pd.getProfesion());
            p.setFuncion(pd.getFuncion());
            p.setInstruccion(pd.getInstruccion());
            p.setEdad(pd.getEdad());
            p.setTxtIp(pd.getTxtIp());
            p.setFecRegistro(new Date());
            p.setNidUsuarioReg(pd.getNidUsuarioMod());
            
            dna.getListaPersonaDna().add(p);
        }
        this.defensoriaFacade.edit(this.inscripcion.getDna());
        
    }
    /**
     * Evaluar la inscripcion
     */
    public void evaluarInscripcion(){
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class); 
        CorreoDnaAdministrado correoAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
      
        try{
            if(this.flgDenegarInscripcion){
                Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_DENEGADA);
                this.inscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                this.inscripcion.setFecDenegado(new Date());
                this.inscripcion.setFecModificacion(new Date());
                this.inscripcion.setEstado(estado);
                this.inscripcionFacade.edit(this.inscripcion);
                // cambia estado de defensoría
                //this.inscripcion.getDna().setEstado(estado);
                //this.defensoriaFacade.edit(this.inscripcion.getDna());
                adicionarMensaje("","La solicitud de inscripción ha sido denegada exitósamente");
            }else if(this.inscripcion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_POR_EVALUAR)){
                this.inscripcion.setObsDenegado(null);
                if(this.existeObservacionInscripcion(this.inscripcionEval)){
                    this.inscripcionEval.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.inscripcionEval.setTxtPc(Internet.obtenerNombrePC());
                    this.inscripcionEval.setTxtIp(Internet.obtenerIPPC());
                    this.inscripcionEval.setFecRegistro(new Date());
                    this.inscripcionEval.setFlgActivo(BigInteger.ONE);
                    this.inscripcionEvalFacade.create(inscripcionEval);  

                    Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_OBSERVADA);
                    this.inscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.inscripcion.setFecObservado(new Date());
                    this.inscripcion.setFecModificacion(new Date());
                    this.inscripcion.setEstado(estado);
                    this.inscripcionFacade.edit(this.inscripcion);
                    adicionarMensaje("","La solicitud de inscripción ha sido observada exitósamente");
                    // cambia estado de defensoría
                    //this.inscripcion.getDna().setEstado(estado);
                  //  this.defensoriaFacade.edit(this.inscripcion.getDna());
                     PersonaDna pr = this.obtenerResponsable(this.inscripcion.getPersonal());
                     this.correoService.enviarSolicitudObservada(Constantes.TPL_INSCRIPCION_OBSERVADA, pr.getNombresApellidos(), pr.getTxtCorreo(),
                            this.inscripcion.getDna().getTxtNombre(),correoAdministrado);

                }else{
                    Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_INSCRITA);
                    this.inscripcion.setFecInscrito(new Date());
                    this.inscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.inscripcion.setFecModificacion(new Date());
                    this.inscripcion.setEstado(estado);
                    this.inscripcionFacade.edit(this.inscripcion);
                    // cambia estado de defensoría
                   //  this.inscripcion.getDna().setEstado(estado);
                   // this.defensoriaFacade.edit(this.inscripcion.getDna());
                    this.actualizarDefensoria();
                    adicionarMensaje("","La solicitud de inscripción ha sido aprogada (inscrita) exitósamente");
                }
            }else if(this.inscripcion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_SUBSANADA)){
                    Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_INSCRITA);
                    this.inscripcion.setFecInscrito(new Date());
                    this.inscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.inscripcion.setFecModificacion(new Date());
                    this.inscripcion.setEstado(estado);
                    this.inscripcionFacade.edit(this.inscripcion);
                      // cambia estado de defensoría
                   // this.inscripcion.getDna().setEstado(estado);
                   // this.defensoriaFacade.edit(this.inscripcion.getDna());
                    this.actualizarDefensoria();
                    adicionarMensaje("","La solicitud de inscripción ha sido aprogada (inscrita) exitósamente");
            }
            this.modo = Constantes.MODO_LISTADO;
            
        }catch (Exception e) {
            adicionarMensajeError("Error", "Problemas al evaluar la inscripción.");
        }
        // this.enviarCorreo(); 
    }
    
    /**
     * Obtener lo documentos que se deben adjuntar
     * @return lista de documentos de inscripción
     */
    public List<DocInscripcion> obtenerAdjuntos() {
        try{
            
            if(this.inscripcionEval!=null && this.inscripcionEval.getInscripcion()!=null) {
                Inscripcion i = new Inscripcion();
                i.setNidInscripcion(this.inscripcionEval.getInscripcion().getNidInscripcion());
                return this.docFacade.findAllByField("inscripcion", i);
            }
        }catch (Exception e) {
            adicionarMensajeError("Error", "No se puede cargar los adjuntos");
        }
        return null;
    }
    
    /**
     * Obtener el nombre de el documento a adjuntar
     * @param doc - DocInscripcion
     * @return StreamedContent
     */
    public StreamedContent obtenerNombreAdjunto(DocInscripcion doc) {
        try {
         File fileUpload = new File(this.ruta, doc.getTxtNombre());
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",doc.getTxtNombre());
        }catch(Exception ex) { }
        return null;
    }
    
    /**
     * Descargar el oficio
     * @return StreamedContent
     */
    public StreamedContent downloadOficio() {
        try {
         
         String fileName = Constantes.PREFIX_OFICIO.
                            concat(this.inscripcion.getDna().getTxtConstancia()).
                            concat(".").concat(Constantes.EXTENSION_PDF);
         File fileUpload = new File(this.rutaConstancias, fileName);
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",fileName);
        }catch(Exception ex) { }
        return null;
    }
    
    /**
     * Descargar el oficio
     * @param item - Inscripcion
     * @return StreamedContent
     */
    public StreamedContent downloadOficio(Inscripcion item) {
        try {
         
         String fileName = Constantes.PREFIX_OFICIO.
                            concat(item.getDna().getTxtConstancia()).
                            concat(".").concat(Constantes.EXTENSION_PDF);
         File fileUpload = new File(this.rutaConstancias, fileName);
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",fileName);
        }catch(Exception ex) { }
        return null;
    }
    
    /**
     * Descargar la constancia
     * @return StreamedContent
     */
    public StreamedContent downloadConstanciaFirmada() {
        try {
         String fileName = Constantes.PREFIX_INSCRIPCION.
                           concat(Constantes.PREFIX_FIRMADO).
                           concat(this.inscripcion.getDna().getTxtConstancia()).
                           concat(".").concat(Constantes.EXTENSION_PDF);
         File fileUpload = new File(this.rutaConstancias, fileName);
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",fileName);
        }catch(Exception ex) { }
        return null;
    }
    
    
    /**
     * Evaluar a la persona
     */
    public void evaluarPersona(){
        try{/*
            this.personaDnaEval.setObsAntecedentes(this.txtObsPersonaEval);
            this.personaDnaEval.setObsColegio(this.clonePersonaDnaEval.getObsColegio());
            this.personaDnaEval.setObsCorreo(this.clonePersonaDnaEval.getObsCorreo());
            this.personaDnaEval.setObsDireccion(this.clonePersonaDnaEval.getObsDireccion());
            this.personaDnaEval.setObsEdad(this.clonePersonaDnaEval.getObsEdad());
            this.personaDnaEval.setObsLugarDeCurso(this.clonePersonaDnaEval.getObsLugarDeCurso());
            this.personaDnaEval.setObsProfesion(this.clonePersonaDnaEval.getObsProfesion());
            this.personaDnaEval.setObsAntecedentes(this.clonePersonaDnaEval.getObsAntecedentes());
            this.personaDnaEval.setObsInstruccion(this.clonePersonaDnaEval.getObsInstruccion());
            this.personaDnaEval.setObsFechaDeCurso(this.clonePersonaDnaEval.getObsFechaDeCurso());
            this.personaDnaEval.setObsColegiatura(this.clonePersonaDnaEval.getObsColegiatura());
            this.personaDnaEval.setObsDocDesignacion(this.clonePersonaDnaEval.getObsDocDesignacion());
            */
        }catch (Exception e) {
            adicionarMensajeError("Problemas al evaluar la inscripción.", e.getMessage());
        }
    }
    
    /**
     * Obtener el nombre de el departamento
     * @param inscripcion - Inscripcion
     * @return String
     */
    public String getNombreDepartamentoIns(Inscripcion inscripcion) {
        return (inscripcion.getDna().getNidDepartamento()!=null)?departamentoFacade.find(inscripcion.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @param inscripcion - Inscripcion
     * @return String
     */
    public String getNombreProvinciaIns(Inscripcion inscripcion) {
        return (inscripcion.getDna().getNidProvincia()!=null)?provinciaFacade.find(inscripcion.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del distrito
     * @param inscripcion - Inscripcion
     * @return String
     */
    public String getNombreDistritoIns(Inscripcion inscripcion) {
        return (inscripcion.getDna().getNidDistrito()!=null)?distritoFacade.find(inscripcion.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Descargar la constancia
     * @param item - Inscripcion
     * @return StreamedContent
     */
    public StreamedContent downloadConstancia(Inscripcion item) {
        try {
         String nroConstancia = item.getDna().getTxtConstancia() != null ? item.getDna().getTxtConstancia(): "";
         String fileName = Constantes.PREFIX_INSCRIPCION.
                            concat(nroConstancia).
                            concat(".").concat(Constantes.EXTENSION_PDF);
         this.imprimirConstanciaPDF(item, fileName);
         return null;
        }catch(Exception ex) { }
        return null;
    }
    
    /**
     * Crear PDF de la constancia
     * @param item - Inscripcion
     * @return ConstanciaReport
     */
    private ConstanciaReport crearConstanciaPDF(Inscripcion item){
        ConstanciaReport salida = new ConstanciaReport();
        
        String nombreDepartamento = this.getNombreDepartamentoIns(item);
        String nombreProvincia = this.getNombreProvinciaIns(item);
        String nombreDistrito = this.getNombreDistritoIns(item);
        salida.setDepartamento(nombreDepartamento);
        salida.setProvincia(nombreProvincia);
        salida.setDistrito(nombreDistrito);
        salida.setTipoConstancia("Inscripción");
        String nroConstancia = "00000";        
        if(item.getDna().getTxtConstancia() != null)
            nroConstancia = item.getDna().getTxtConstancia();
        salida.setNrDeConstancia(nroConstancia);
        
        return salida;
    }
    
    /**
     * Imprimir el PDF de la constancia
     * @param item - Inscripcion
     * @param nombreConstancia - String
     */
    private void imprimirConstanciaPDF(Inscripcion item,String nombreConstancia){
        List<ConstanciaReport> datasource;
        Map<String,Object> params;
        datasource = new ArrayList<>();
        params = new HashMap<>();
        datasource.add(this.crearConstanciaPDF(item));
         try {
            PdfUtil.crearPDF(params, ConstantesReporte.CONSTANCIA_PATH, datasource, nombreConstancia);
        } catch (Exception ex) {
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Descargar la inscripcion
     * @param item - Inscripcion
     * @return StreamedContent
     */
    public StreamedContent downloadInscripcion(Inscripcion item) {
        try {
            PersonaDna responsable = new PersonaDna();
            for(PersonaDna persona : item.getPersonal()){
                if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                    responsable = persona;
                    break;
                }
            }
            this.imprimirFormulario03DSLDPDF(item, responsable);
            return null;
        }catch(Exception ex) { }
        return null;
    }
   
    /**
     * Imprimir el Formulario03DSKLD
     * @param inscripcion - Inscripcion
     * @param responsable - PersonaDna
     */
    private void imprimirFormulario03DSLDPDF(Inscripcion inscripcion , PersonaDna responsable){

        List<Formulario03DSLDReport> datasource;
        Map<String,Object> params;
        datasource = new ArrayList<>();
        params = new HashMap<>();
        datasource.add(this.crearFormulario03DSLD(inscripcion , responsable));
        params.put(ConstantesReporte.FORM03DSDL_SUBREPORT_PARAM, FacesContext.getCurrentInstance().getExternalContext().getRealPath(ConstantesReporte.FORM03DSDL_SUBREPORT_PATH));
        params.put(ConstantesReporte.DECLARACION_JURADA_PARAM, FacesContext.getCurrentInstance().getExternalContext().getRealPath(ConstantesReporte.DECLARACION_JURADA_PATH));
        
        try {
            PdfUtil.crearPDF(params, ConstantesReporte.FORM03DSDL_PATH, datasource, ConstantesReporte.FORM03DSDL_NAME);
        } catch (Exception ex) {
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Crear el Formulario03DSLD
     * @param inscripcion - Inscripcion
     * @param responsable - PersonaDna
     * @return Formulario03DSLDReport
     */
    private Formulario03DSLDReport crearFormulario03DSLD(Inscripcion inscripcion , PersonaDna responsable){
        Formulario03DSLDReport salida;        
        salida = new Formulario03DSLDReport();

        salida.setNombreEntidadResponsable(inscripcion.getDna().getTxtEntidad() != null?inscripcion.getDna().getTxtEntidad():"");
        salida.setNombreDNA(inscripcion.getDna().getTxtNombre() != null?inscripcion.getDna().getTxtNombre():"");
        
        String numeroDeConstancia = "";
        if(inscripcion.getDna().getTxtConstancia() != null){
            String nidConstanciaStr = inscripcion.getDna().getTxtConstancia();
            for(int i = 0 ; i< 8 - nidConstanciaStr.length() ; i++)numeroDeConstancia += "0";
            numeroDeConstancia += nidConstanciaStr;
        }
        salida.setNroInscripcionDNA(numeroDeConstancia);
        salida.setDocumentoCreacionDNA(inscripcion.getTxtDocumento() != null?inscripcion.getTxtDocumento():"");
        salida.setDireccionDNA(inscripcion.getTxtDireccion() != null?inscripcion.getTxtDireccion():"");
        if(inscripcion.getDna().getNidDepartamento() != null){
            Departamento departamento = this.departamentoFacade.find(inscripcion.getDna().getNidDepartamento());
            salida.setDepartamento(departamento.getTxtDescripcion());
        }
        if(inscripcion.getDna().getNidProvincia()!= null){
            Provincia provincia = this.provinciaFacade.find(inscripcion.getDna().getNidProvincia());
            salida.setProvincia(provincia.getTxtDescripcion());
        }
        if(inscripcion.getDna().getNidDistrito()!= null){
            Distrito distrito = this.distritoFacade.find(inscripcion.getDna().getNidDistrito());
            salida.setDistrito(distrito.getTxtDescripcion());
        }
        salida.setCorreoElectronicoContacto(inscripcion.getTxtCorreo() != null?inscripcion.getTxtCorreo():"");
        salida.setTelefonos(inscripcion.getTxtTelefono() != null?inscripcion.getTxtTelefono():"");
        salida.setDireccionPerteneceDNA(inscripcion.getTxtGerencia() != null?inscripcion.getTxtGerencia():"");
        salida.setDiasHorasAtencion(inscripcion.getDias() != null?inscripcion.getDias():"");
        String presupuestoFormat = "";
        if(inscripcion.getPresupuesto() != null){
            DecimalFormat df = new DecimalFormat();
            BigDecimal presupuesto = inscripcion.getPresupuesto().setScale(2,BigDecimal.ROUND_UP);
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);            
            presupuestoFormat = df.format(presupuesto);
        }
        
        salida.setPresupuestoDesignado(presupuestoFormat);
        salida.setNumeroDeAmbientesDNA(inscripcion.getAmbientes() != null?inscripcion.getAmbientes().toString():"");
        salida.setNumeroDeAmbientesAudiencia(inscripcion.getAmbientesPriv() != null?inscripcion.getAmbientesPriv().toString():"");
        String nroConstancia = "00000";
        if(inscripcion.getDna().getTxtConstancia() != null){
            nroConstancia = inscripcion.getDna().getTxtConstancia();
        }
        salida.setNroInscripcionDNA(nroConstancia);
        if(responsable != null){
            salida.setNombresResp(responsable.getApellidosNombres() != null ? responsable.getApellidosNombres():"");
            salida.setApellidosResp(responsable.getTxtApellidoPaterno() != null &&responsable.getTxtApellidoMaterno() != null ?responsable.getTxtApellidoPaterno() + " " + responsable.getTxtApellidoMaterno():"");
            salida.setTelefonoResp(responsable.getTxtTelefono() != null ? responsable.getTxtTelefono() : "");
            salida.setCorreoResp(responsable.getTxtCorreo()!= null?responsable.getTxtCorreo() : "");
            salida.setDniResp(responsable.getTxtDocumento() != null?responsable.getTxtDocumento() : "");
            salida.setDocumentoDesignacionResp(responsable.getTxtDocDesignacion() != null ? responsable.getTxtDocDesignacion() : "");
            String gradoInstruccion = "";
            if(responsable.getInstruccion() != null){
                gradoInstruccion = responsable.getInstruccion().getTxtNombre();
            }
            salida.setGradoInstruccionResp(gradoInstruccion);
            salida.setEdadResp(responsable.getEdad() != null ? responsable.getEdad().toString() : "");
            salida.setSexoResp(responsable.getTxtSexo() != null ? responsable.getTxtSexo()  : "");
            String profesion = "";
            if(responsable.getProfesion() != null){
                if(responsable.getProfesion().getTxtNombre() != null && !responsable.getProfesion().getTxtNombre().equals("")){
                    profesion = responsable.getProfesion().getTxtNombre();
                }else{
                    Catalogo profesionCata = this.catalogoFacade.find(responsable.getProfesion().getNidCatalogo());
                    profesion = profesionCata.getTxtNombre();
                }
            }
            salida.setProfesionResp(profesion);
            salida.setColegioProfesionalResp(responsable.getTxtColegio() != null?responsable.getTxtColegio():"");
            salida.setColegiaturaResp(responsable.getTxtColegiatura() != null?responsable.getTxtColegiatura():"");
            String date = "_____";
            if(responsable.getTxtFechaCurso() != null){
                date = DnaUtil.obtenerFecha(responsable.getTxtFechaCurso());
            }
            salida.setFechaLugarLlevoCurso(responsable.getTxtLugarCurso()!= null?"Fecha " + date + ", lugar " + responsable.getTxtLugarCurso():"Fecha " + date + ",lugar _____");
            salida.setFechaLugarFormacion(responsable.getTxtLugarCurso()!= null?responsable.getTxtLugarCurso():"");
            salida.setAuthNotificacion("");
        }
        
        if(inscripcion.getPersonal() != null){
            Boolean existeDefenseor = false;
            for(PersonaDna persona : inscripcion.getPersonal()){
                if(!persona.getFlgActivo().equals(BigInteger.ZERO)){
                    salida.getDeclaraciones().add(this.crearDeclaracionJurada(persona));
                    Detalle03DSLDREport detalle = new Detalle03DSLDREport();
                    detalle.setNombresApellidos(
                            persona.getApellidosNombres() != null ? persona.getApellidosNombres():"");
                    String date = DnaUtil.obtenerFecha(persona.getTxtFechaCurso());
                    detalle.setFehcaLugar( persona.getTxtLugarCurso()  != null ?
                            date + " " + persona.getTxtLugarCurso():date);
                    detalle.setDni(persona.getTxtDocumento() != null ? persona.getTxtDocumento() : "");
                    if(persona.getTxtSexo() != null){
                        if(persona.getTxtSexo().equals("M")){
                            detalle.setSexoM(persona.getTxtSexo());
                        }else{
                            detalle.setSexoF(persona.getTxtSexo());
                        }
                    }

                    if(persona.getFuncion() != null){
                       if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_DEFENSOR)){
                           existeDefenseor=true;
                            salida.getListaDefensores().add(detalle);
                        }else if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_PROMOTOR)){
                            salida.getListaPromoteres().add(detalle);
                        }else if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_PERSONAL_APOYO)){
                            salida.getListaPersonal().add(detalle);
                        }
                    }
                }
            }
            if(!existeDefenseor){
                Detalle03DSLDREport detalle = new Detalle03DSLDREport();
                detalle.setNombresApellidos(
                        responsable.getApellidosNombres() != null ? responsable.getApellidosNombres():"");
                String date = DnaUtil.obtenerFecha(responsable.getTxtFechaCurso());
                detalle.setFehcaLugar( responsable.getTxtLugarCurso()  != null ?
                        date + " " + responsable.getTxtLugarCurso():date);
                detalle.setDni(responsable.getTxtDocumento() != null ? responsable.getTxtDocumento() : "");
                if(responsable.getTxtSexo() != null){
                    if(responsable.getTxtSexo().equals("M")){
                        detalle.setSexoM(responsable.getTxtSexo());
                    }else{
                        detalle.setSexoF(responsable.getTxtSexo());
                    }
                }
                salida.getListaDefensores().add(detalle);
            }
        }
        
        return salida;
    }
    
    /**
     * Crear la declaracion Jurada
     * @param persona - PersonaDna
     * @return DeclaracionJuradaReport
     */
    private DeclaracionJuradaReport crearDeclaracionJurada(PersonaDna persona){
        DeclaracionJuradaReport salida = new DeclaracionJuradaReport();
        salida.setApellidoPaterno(persona.getTxtApellidoPaterno() != null ? persona.getTxtApellidoPaterno() : "");
        salida.setApellidoMaterno(persona.getTxtApellidoMaterno() != null ? persona.getTxtApellidoMaterno() : "");
        salida.setColegiatura(persona.getTxtColegiatura()!= null ? persona.getTxtColegiatura() : "");
        salida.setColegio(persona.getTxtColegio()!= null ? persona.getTxtColegio() : "");
        salida.setCorreo(persona.getTxtCorreo()!= null ? persona.getTxtCorreo() : "");
        salida.setDepartamento(persona.getNidDepartamento()!= null ? this.departamentoFacade.find(persona.getNidDepartamento()).getTxtDescripcion() : "");
        salida.setProvincia(persona.getNidProvincia()!= null ? this.provinciaFacade.find(persona.getNidProvincia()).getTxtDescripcion() : "");
        salida.setDistrito(persona.getNidDistrito()!= null ? this.distritoFacade.find(persona.getNidDistrito()).getTxtDescripcion() : "");
        salida.setDireccion(persona.getTxtDireccion()!= null ? persona.getTxtDireccion() : "");
        salida.setDni(persona.getTxtDocumento()!= null ? persona.getTxtDocumento() : "");
        String date = "____";
        if(persona.getTxtFechaCurso() != null){
            date = DnaUtil.obtenerFecha(persona.getTxtFechaCurso());
        }
        salida.setFechaCurso(date);
        salida.setFuncion(persona.getFuncion() != null ? persona.getFuncion().getTxtNombre() : "");
        String gradoInstruccion = "";
        if(persona.getInstruccion() != null){
            gradoInstruccion = persona.getInstruccion().getTxtNombre();
        }
        salida.setGradoInstruccion(gradoInstruccion);
        salida.setLugarCurso(persona.getTxtLugarCurso()!= null ? persona.getTxtLugarCurso() : "");
        salida.setNombre1(persona.getTxtNombre1()!= null ? persona.getTxtNombre1() : "");
        salida.setNombre2(persona.getTxtNombre2()!= null ? persona.getTxtNombre2() : "");
        salida.setNombre3(persona.getTxtNombre3()!= null ? persona.getTxtNombre3() : "");
        String profesion = "";
        if(persona.getProfesion() != null){
            if(persona.getProfesion().getTxtNombre() != null && !persona.getProfesion().getTxtNombre().equals("")){
                profesion = persona.getProfesion().getTxtNombre();
            }else{
                Catalogo profesionCata = this.catalogoFacade.find(persona.getProfesion().getNidCatalogo());
                profesion = profesionCata.getTxtNombre();
            }
        }
        salida.setProfesion(profesion);
        salida.setSexo(persona.getTxtSexo()!= null ? persona.getTxtSexo() : "");
        salida.setTelefono(persona.getTxtTelefono() != null ? persona.getTxtTelefono() : "");
        
        return salida;
    }
    
    /**
     * Descargar el PDF del formulario
     */
    public void descargarForm() {
         this.imprimirFormulario03DSLDPDF(this.inscripcion ,this.responsable);
    }
    
    /**
     * Obtener la fecha con el formato MM/dd/yyyy
     * @param fecha - Date
     * @return String
     */
    public String obtenerFechaConFormato(Date fecha){
        if(fecha == null) return "";
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(fecha);
        return date;
    }
    
    /**
    * Validar datos DNI
    * @param event - ComponentSystemEvent 
    */
    public void validarPanelPersona(final ComponentSystemEvent event){
        FacesContext fc = FacesContext.getCurrentInstance();
        if( this.personaDna.getTxtApellidoMaterno() == null || this.personaDna.getTxtApellidoMaterno().equals("") 
                || this.personaDna.getTxtApellidoPaterno() == null || this.personaDna.getTxtApellidoPaterno().equals("") 
                || this.personaDna.getTxtNombre1() == null || this.personaDna.getTxtNombre1().equals("") ){ 
            adicionarMensajeError("Los datos de la persona están incompletos porque no ingresó un DNI válido","");
            fc.validationFailed();
            fc.renderResponse(); 
        }
    }
    
    public Boolean getFlgNroColegiaturaObs() {
        return flgNroColegiaturaObs;
    }

    public void setFlgNroColegiaturaObs(Boolean flgNroColegiaturaObs) {
        this.flgNroColegiaturaObs = flgNroColegiaturaObs;
    }
    
    public String getFileName(DocInscripcion doc) {
        return doc.getTxtNombre();
    }
       
    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

   

    public List<PersonaDna> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(List<PersonaDna> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }

    public PersonaDna getPersonaDna() {
        return personaDna;
    }

    public void setPersonaDna(PersonaDna personaDna) {
        this.personaDna = personaDna;
    }


    public List<Provincia> getProvinciasDna() {
        return provinciasDna;
    }

    public void setProvinciasDna(List<Provincia> provinciasDna) {
        this.provinciasDna = provinciasDna;
    }

    public List<Distrito> getDistritosDna() {
        return distritosDna;
    }

    public void setDistritosDna(List<Distrito> distritosDna) {
        this.distritosDna = distritosDna;
    }

    public List<Inscripcion> getLista() {
        return lista;
    }

    public void setLista(List<Inscripcion> lista) {
        this.lista = lista;
    }

    public String getBusCodigo() {
        return busCodigo;
    }

    public void setBusCodigo(String busCodigo) {
        this.busCodigo = busCodigo;
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

    public BigDecimal getBusDistrito() {
        return busDistrito;
    }

    public void setBusDistrito(BigDecimal busDistrito) {
        this.busDistrito = busDistrito;
    }

    public List<Provincia> getProvinciasBus() {
        return provinciasBus;
    }

    public void setProvinciasBus(List<Provincia> provinciasBus) {
        this.provinciasBus = provinciasBus;
    }

    public List<Distrito> getDistritosBus() {
        return distritosBus;
    }

    public void setDistritosBus(List<Distrito> distritosBus) {
        this.distritosBus = distritosBus;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public boolean isVerOrigen() {
        return verOrigen;
    }

    public void setVerOrigen(boolean verOrigen) {
        this.verOrigen = verOrigen;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public BigInteger getBusEstado() {
        return busEstado;
    }

    public void setBusEstado(BigInteger busEstado) {
        this.busEstado = busEstado;
    }

    public Inscripcion getInscripcionSeleccionada() {
        return inscripcionSeleccionada;
    }

    public void setInscripcionSeleccionada(Inscripcion inscripcionSeleccionada) {
        this.inscripcionSeleccionada = inscripcionSeleccionada;
    }

    public PersonaDna getResponsable() {
        return responsable;
    }

    public void setResponsable(PersonaDna responsable) {
        this.responsable = responsable;
    }

    public boolean isVerMensajeOK() {
        return verMensajeOK;
    }

    public void setVerMensajeOK(boolean verMensajeOK) {
        this.verMensajeOK = verMensajeOK;
    }

    public List<UploadedFile> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<UploadedFile> archivos) {
        this.archivos = archivos;
    }


    public List<DocInscripcion> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<DocInscripcion> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public int getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(int indexTab) {
        this.indexTab = indexTab;
    }

    public boolean isDniValido() {
        return dniValido;
    }

    public void setDniValido(boolean dniValido) {
        this.dniValido = dniValido;
    }

    public List<PersonaDna> getListaPersonalRemovidos() {
        return listaPersonalRemovidos;
    }

    public void setListaPersonalRemovidos(List<PersonaDna> listaPersonalRemovidos) {
        this.listaPersonalRemovidos = listaPersonalRemovidos;
    }

    public Boolean getFlgInstruccionObs() {
        return flgInstruccionObs;
    }

    public void setFlgInstruccionObs(Boolean flgInstruccionObs) {
        this.flgInstruccionObs = flgInstruccionObs;
    }

    public Boolean getFlgFechaCursoObs() {
        return flgFechaCursoObs;
    }

    public void setFlgFechaCursoObs(Boolean flgFechaCursoObs) {
        this.flgFechaCursoObs = flgFechaCursoObs;
    }
    
    public Boolean getFlgEdadPersonaObs() {
        return flgEdadPersonaObs;
    }

    public void setFlgEdadPersonaObs(Boolean flgEdadPersonaObs) {
        this.flgEdadPersonaObs = flgEdadPersonaObs;
    }

    public Boolean getFlgProfesionPersonaObs() {
        return flgProfesionPersonaObs;
    }

    public void setFlgProfesionPersonaObs(Boolean flgProfesionPersonaObs) {
        this.flgProfesionPersonaObs = flgProfesionPersonaObs;
    }

    public Boolean getFlgColegioPersonaObs() {
        return flgColegioPersonaObs;
    }

    public void setFlgColegioPersonaObs(Boolean flgColegioPersonaObs) {
        this.flgColegioPersonaObs = flgColegioPersonaObs;
    }

    public Boolean getFlgLugarPersonaObs() {
        return flgLugarPersonaObs;
    }

    public void setFlgLugarPersonaObs(Boolean flgLugarPersonaObs) {
        this.flgLugarPersonaObs = flgLugarPersonaObs;
    }

    public Boolean getFlgCorreoPersonaObs() {
        return flgCorreoPersonaObs;
    }

    public void setFlgCorreoPersonaObs(Boolean flgCorreoPersonaObs) {
        this.flgCorreoPersonaObs = flgCorreoPersonaObs;
    }

    public Boolean getFlgDireccionPersonaObs() {
        return flgDireccionPersonaObs;
    }

    public void setFlgDireccionPersonaObs(Boolean flgDireccionPersonaObs) {
        this.flgDireccionPersonaObs = flgDireccionPersonaObs;
    }

    public Boolean getFlgAntecedentePersonaObs() {
        return flgAntecedentePersonaObs;
    }

    public void setFlgAntecedentePersonaObs(Boolean flgAntecedentePersonaObs) {
        this.flgAntecedentePersonaObs = flgAntecedentePersonaObs;
    }

    public Boolean getFlgDocDesignacionObs() {
        return flgDocDesignacionObs;
    }

    public void setFlgDocDesignacionObs(Boolean flgDocDesignacionObs) {
        this.flgDocDesignacionObs = flgDocDesignacionObs;
    }
    
    public Boolean getFlgAdjuntosObs() {
        return flgAdjuntosObs;
    }

    public void setFlgAdjuntosObs(Boolean flgAdjuntosObs) {
        this.flgAdjuntosObs = flgAdjuntosObs;
    }   
    
    public String getTxtObsPersonaEval() {
        return txtObsPersonaEval;
    }

    public void setTxtObsPersonaEval(String txtObsPersonaEval) {
        this.txtObsPersonaEval = txtObsPersonaEval;
    }
    
    public Boolean getFlgCorreoObs() {
        return flgCorreoObs;
    }

    public void setFlgCorreoObs(Boolean flgCorreoObs) {
        this.flgCorreoObs = flgCorreoObs;
    }

    public Boolean getFlgDireccionObs() {
        return flgDireccionObs;
    }

    public void setFlgDireccionObs(Boolean flgDireccionObs) {
        this.flgDireccionObs = flgDireccionObs;
    }

    public Boolean getFlgGerenciaObs() {
        return flgGerenciaObs;
    }

    public void setFlgGerenciaObs(Boolean flgGerenciaObs) {
        this.flgGerenciaObs = flgGerenciaObs;
    }

    public Boolean getFlgDiasHoraObs() {
        return flgDiasHoraObs;
    }

    public void setFlgDiasHoraObs(Boolean flgDiasHoraObs) {
        this.flgDiasHoraObs = flgDiasHoraObs;
    }

    public Boolean getFlgPresupuestoObs() {
        return flgPresupuestoObs;
    }

    public void setFlgPresupuestoObs(Boolean flgPresupuestoObs) {
        this.flgPresupuestoObs = flgPresupuestoObs;
    }

    public Boolean getFlgNroAmbObs() {
        return flgNroAmbObs;
    }

    public void setFlgNroAmbObs(Boolean flgNroAmbObs) {
        this.flgNroAmbObs = flgNroAmbObs;
    }

    public Boolean getFlgNroAmbPrivObs() {
        return flgNroAmbPrivObs;
    }

    public void setFlgNroAmbPrivObs(Boolean flgNroAmbPrivObs) {
        this.flgNroAmbPrivObs = flgNroAmbPrivObs;
    }

    public Boolean getFlgPersonaObservada() {
        return flgPersonaObservada;
    }

    public void setFlgPersonaObservada(Boolean flgPersonaObservada) {
        this.flgPersonaObservada = flgPersonaObservada;
    }

    public InscripcionEval getInscripcionEval() {
        return inscripcionEval;
    }

    public void setInscripcionEval(InscripcionEval inscripcionEval) {
        this.inscripcionEval = inscripcionEval;
    }

    public PersonaDnaEval getPersonaDnaEval() {
        return personaDnaEval;
    }

    public void setPersonaDnaEval(PersonaDnaEval personaDnaEval) {
        this.personaDnaEval = personaDnaEval;
    }

    public List<PersonaDnaEval> getPersonasDnaEval() {
        return personasDnaEval;
    }

    public void setPersonasDnaEval(List<PersonaDnaEval> personasDnaEval) {
        this.personasDnaEval = personasDnaEval;
    }


    public String getTextoBtnEvaluar() {
        return textoBtnEvaluar;
    }

    public void setTextoBtnEvaluar(String textoBtnEvaluar) {
        this.textoBtnEvaluar = textoBtnEvaluar;
    }

    public Boolean getFlgDenegarInscripcion() {
        return flgDenegarInscripcion;
    }

    public void setFlgDenegarInscripcion(Boolean flgDenegarInscripcion) {
        this.flgDenegarInscripcion = flgDenegarInscripcion;
    }
    
   
    public PersonaDna getResponsableSubsanar() {
        return responsableSubsanar;
    }

    public void setResponsableSubsanar(PersonaDna responsableSubsanar) {
        this.responsableSubsanar = responsableSubsanar;
    }

    public PersonaDna getPersonaRemover() {
        return personaRemover;
    }

    public void setPersonaRemover(PersonaDna personaRemover) {
        this.personaRemover = personaRemover;
    }
    
    public Inscripcion getInscripcionSubsanar() {
        return inscripcionSubsanar;
    }

    public void setInscripcionSubsanar(Inscripcion inscripcionSubsanar) {
        this.inscripcionSubsanar = inscripcionSubsanar;
    }

    public List<PersonaDna> getPersonalRemover() {
        return personalRemover;
    }

    public void setPersonalRemover(List<PersonaDna> personalRemover) {
        this.personalRemover = personalRemover;
    }

    public Defensoria getDefensoria() {
        return defensoria;
    }

    public void setDefensoria(Defensoria defensoria) {
        this.defensoria = defensoria;
    }

    public String getBusTipo() {
        return busTipo;
    }

    public void setBusTipo(String busTipo) {
        this.busTipo = busTipo;
    }

    public List<Defensoria> getListaDefensoria() {
        return listaDefensoria;
    }

    public void setListaDefensoria(List<Defensoria> listaDefensoria) {
      this.listaDefensoria = listaDefensoria;
    }
   
   
    public Boolean getFlgDocObs() {
        return flgDocObs;
    }

    public void setFlgDocObs(Boolean flgDocObs) {
        this.flgDocObs = flgDocObs;
    }

    public PersonaDnaFacadeLocal getPersonaDnaFacade() {
        return personaDnaFacade;
    }

    public void setPersonaDnaFacade(PersonaDnaFacadeLocal personaDnaFacade) {
        this.personaDnaFacade = personaDnaFacade;
    }

    public TabView getTabView() {
        return tabView;
    }

    public void setTabView(TabView tabView) {
        this.tabView = tabView;
    }

    public List<PersonaDna> getPersonalESubsanar() {
        return personalESubsanar;
    }

    public void setPersonalESubsanar(List<PersonaDna> personalESubsanar) {
        this.personalESubsanar = personalESubsanar;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }
    
}