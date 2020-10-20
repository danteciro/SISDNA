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
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
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

    private AntecedentePolicial antecedentePolicial;

    private AntecedentePenal antecedentePenal;

    private AntecedenteJudicial antecedenteJudicial;

    private IdentificacionReniec identificacionReniec;
    
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
    private Inscripcion inscripcionVer;
    private List<PersonaDna> personalVer;
    private PersonaDna personaVer;
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
    private ProvinciaFacadeLocal fachadaProvincia;
    
    @EJB
    private DistritoFacadeLocal fachadaDistrito;

    @EJB
    private DepartamentoFacadeLocal fachadaDepartamento;

    @EJB
    private DocInscripcionFacadeLocal docFacade;
            
    @EJB
    private CorreoService correoService;

    private List<Provincia> provincias;
    private List<Distrito> distritos;
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
    private Inscripcion inscripcionAEvaluar;
    private List<PersonaDna> personasAEvaluar;
    private PersonaDna personaAEvaluar;
    private InscripcionEval inscripcionEval;
    private PersonaDnaEval personaDnaEval;
    private PersonaDnaEval clonePersonaDnaEval;
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
    private List<PersonaDna> personalSubsanar;
    private List<PersonaDna> personalRemover;
    private PersonaDna personaSubsanar;
    private PersonaDna responsableSubsanar;
    private PersonaDna personaRemover;
    private String correoResponsable;
    
    private TabView tabView;

    public InscripcionAdministrado() {
        this.listaPersonal = new ArrayList<>();
        this.listaPersonalRemovidos = new ArrayList<>();
        this.provincias = new ArrayList<>();
        this.distritos = new ArrayList<>();
        this.provinciasDna = new ArrayList<>();
        this.distritosDna = new ArrayList<>();
        this.verOrigen = true;
        this.nombreSede = "";
        this.verMensajeOK = false;
        this.dniValido = false;
        this.personasAEvaluar = new ArrayList<>();
        this.personaAEvaluar = new PersonaDna();
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
        this.inscripcionVer = new Inscripcion();
        this.personalVer = new ArrayList<>();
        this.inscripcionSubsanar = new Inscripcion();
        this.personalSubsanar = new ArrayList<>();
        this.personalRemover = new ArrayList<>();
        this.inscripcion = new Inscripcion();
        this.inscripcion.setDna(new Defensoria());
        this.correoResponsable = "";
    }
    
    @PostConstruct
    public void initLoad() {
        this.busTipo = "1";
        this.initFiltro();
        this.nuevaDna();
        this.initAdjuntos();
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.RUTA_ARCHIVO);
        this.ruta = parametros.get(0).getTxtValor();
        
        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.RUTA_CONSTANCIAS);
        this.rutaConstancias = parametros.get(0).getTxtValor();
        
        this.initPersona();
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
        this.clonePersonaDnaEval = new PersonaDnaEval();
        this.clonePersonaDnaEval.setFuncion(new Catalogo());
        
        //Subsanar
        this.personaSubsanar = new PersonaDna();
        this.personaSubsanar.setFuncion(new Catalogo());
        this.personaSubsanar.setNidDepartamento(BigDecimal.ZERO);
        this.personaSubsanar.setNidProvincia(BigDecimal.ZERO);
        this.personaSubsanar.setNidDistrito(BigDecimal.ZERO);
        this.personaSubsanar.setProfesion(new Catalogo());
        
        //Ver
        this.personaVer = new PersonaDna();
        this.personaVer.setFuncion(new Catalogo());
        
        //Remover
        this.personaRemover =new PersonaDna();
        this.personaRemover.setFuncion(new Catalogo());
    }
    
    /**
     * Nueva dna
     */
    public void nuevaDna(){
       this.defensoria = new Defensoria();
       this.defensoria.setTxtTipo(Constantes.TIPO_SEDE_CENTRAL);
       this.defensoria.setOrigen(new Catalogo());
       this.verOrigen = true;
       this.codigo = null;
       this.nombreSede = "";
        
    }
    
    /**
     * Obtener defensoria
     * @param item - Defensoria
     */
    public void obtenerDefensoria(Defensoria item) {
        this.defensoria = item;
        this.verOrigen = this.defensoria.getTxtTipo().equals("CEN");
        if(item.getTxtTipo().equals("ANX")) {
            this.codigo = item.getTxtConstancia();
            this.nombreSede = item.getCentral().getTxtNombre();       
        }
        this.obtenerProvincias();
        this.obtenerDistritos();
    }
    
    /**
     * Obtener documentos inscripcion
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerDocumentosInscripcion(){
        List<Catalogo> listaDocumentosInscripcion = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_DOCUMENTO_INSCRIPCION, true, "txtNombre", false);
        return listaDocumentosInscripcion;
    }
    
    /**
     * Inicializar nueva defensoria
     * @param item - Defensoria
     */
    public void nuevaSolicitud(Defensoria item) {
         this.inscripcion = new Inscripcion();
         this.inscripcion.setDna(item);
         this.listaPersonal = new ArrayList<>();
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
     * Obtener el origen
     * @return lista de catálogos
     */
    public List<Catalogo> obtenerOrigen() {
        return  this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_ORIGEN, true, "txtNombre", false);
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
     * muestra los datos referentes al anexo e inicializa si es que es un registro nuevo
     */
    public void renderOrigen(){
        String tipoSede = this.defensoria.getTxtTipo();
        if(this.defensoria.getNidDna()==null) {
            this.defensoria = new Defensoria();
            this.defensoria.setTxtTipo(tipoSede);       
            this.defensoria.setOrigen(new Catalogo());
        } else{
            this.defensoria.setTxtTipo(tipoSede);       
        }
        this.verOrigen = tipoSede.equals(Constantes.TIPO_SEDE_CENTRAL);
        
    }   
    
    
    
    /**
     * Devuelve las funciones del personal, si ya ingresó un responsable entonces lo retira de la lista de funciones a asignar
     * @return  lista de catálogos
     */
    public List<Catalogo> obtenerFunciones() {
        List<Catalogo> listaCatalogos = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_FUNCION, true, "txtNombre", false);
        
        if(this.verificaResponsable())
           for(Catalogo funcion: listaCatalogos)
                if(funcion.getNidCatalogo() == Constantes.CATALOGO_FUNCION_RESPONSABLE) {
                    listaCatalogos.remove(funcion);
                    break;
                }
        
        return  listaCatalogos;
    }
   
    /**
     * Obtener el grado de instruccion
     * @return  lista de catálogos
     */
    public List<Catalogo> obtenerGradoInstruccion() {
        List<Catalogo> listaCatalogos = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_GRADO_INSTRUCCION, true, "txtNombre", false);
        return  listaCatalogos;
    }

     /**
     * Obtener las ocupaciones
     * @return  lista de catálogos
     */
    public List<Catalogo> obtenerOcupaciones() {
        List<Catalogo> listaCatalogos = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_OCUPACIONES, true, "txtNombre", false);
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
        estadoNuevo.setNidCatalogo(Constantes.CATALOGO_ESTADO_NUEVO);
        Catalogo estadoDenegado = new Catalogo();
        estadoDenegado.setNidCatalogo(Constantes.CATALOGO_ESTADO_DENEGADA);
     
        ParametroNodo param = new ParametroNodo();
        param.adicionar("flgActivo", BigInteger.ONE);
        
        if(this.indexTab == 0 && this.busCodigo!=null ) {
             param.adicionar("txtConstancia",  this.busCodigo);
             this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosAndEstado(param, estadoNuevo, estadoDenegado);
             return;
        }

        if(this.indexTab == 1) {
            if(this.busDepartamento!=null) 
                param.adicionar("nidDepartamento", this.busDepartamento);
            if(this.busProvincia!=null)
                param.adicionar("nidProvincia", this.busProvincia);
            if(this.busDistrito!=null)
                param.adicionar("nidDistrito", this.busDistrito);
            
            this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosAndEstado(param, estadoNuevo, estadoDenegado);
       
        }

      
          
    }
    
   /**
    * En el panelDna realiza la búsqueda de la DNA del tipo sede central para poder crear el anexo
    * @param fc - FacesContext
    * @param component - UIComponent
    * @param value - valor de componente
    * @throws ValidatorException  - excepción generada cuando existe un error en la validación
    */
      public void buscarDna(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
       
      
        if(value!=null && !String.valueOf(value).equals("") && !String.valueOf(value).equals("00000") 
                && (this.defensoria.getTxtConstancia()==null || !((String)value).equals(this.defensoria.getTxtConstancia())) ) {
          
               ParametroNodo param = new ParametroNodo();
               param.adicionar("txtConstancia", String.valueOf(value));
               param.adicionar("txtTipo", Constantes.TIPO_SEDE_CENTRAL);

               List<Defensoria> listaDna;            
               try {
                   listaDna = this.defensoriaFacade.obtenerPorParametros(param);
               }catch(Exception ex){
                  throw new ValidatorException(new FacesMessage("Error al buscar la DNA"));
               }    

               if(listaDna!=null && listaDna.size()>0) {
                   Defensoria resDefensoria = listaDna.get(0);
                   this.defensoria.setCentral(resDefensoria);
                   this.defensoria.setTxtConstancia(resDefensoria.getTxtConstancia());
                   this.defensoria.setOrigen(resDefensoria.getOrigen());
                   this.defensoria.setNidDepartamento(resDefensoria.getNidDepartamento());
                   this.defensoria.setTxtEntidad(resDefensoria.getTxtEntidad());
                   this.obtenerProvincias();
                   this.defensoria.setNidProvincia(resDefensoria.getNidProvincia());
                    this.obtenerDistritos();
                   this.defensoria.setNidDistrito(resDefensoria.getNidDistrito());
                   this.nombreSede = resDefensoria.getTxtNombre();
               } else {
                   this.renderOrigen();
                   throw new ValidatorException(new FacesMessage("DNA no existe"));
               }
         }
    }
    
    /**
     * Obtener nombre departamento
     * @return String
     */
    public String getNombreDepartamento() {
        if(this.inscripcion.getDna() == null || this.inscripcion.getDna().getNidDepartamento() == null){ return "";}
        return (this.inscripcion.getDna().getNidDepartamento()!=null)?fachadaDepartamento.find(this.inscripcion.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre provincia
     * @return String
     */
    public String getNombreProvincia() {
        if(this.inscripcion.getDna() == null || this.inscripcion.getDna().getNidProvincia() == null){ return "";}
        return (this.inscripcion.getDna().getNidProvincia()!=null)?fachadaProvincia.find(this.inscripcion.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre disstrito
     * @return String
     */
    public String getNombreDistrito() {
        if(this.inscripcion.getDna() == null || this.inscripcion.getDna().getNidDistrito() == null){ return "";}
        return (this.inscripcion.getDna().getNidDistrito()!=null)?fachadaDistrito.find(this.inscripcion.getDna().getNidDistrito()).getTxtDescripcion():"";
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
        return  fachadaDepartamento.obtenerDepartamentos();
    }

    /**
     * Obtener provincias
     */
    public void obtenerProvincias() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.defensoria.getNidDepartamento());
        this.provincias = fachadaProvincia.obtenerProvincias(departamento);
    }
    
    /**
     * Obtener distritos
     */
    public void obtenerDistritos() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.defensoria.getNidProvincia());
        this.distritos = fachadaDistrito.obtenerDistritos(provincia);
    }
    
    /**
     * Obtener provincias
     */
    public void obtenerProvinciasDna() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.personaDna.getNidDepartamento());
        this.provinciasDna = fachadaProvincia.obtenerProvincias(departamento);
    }
    
    /**
     * Obteer distritos
     */
    public void obtenerDistritosDna() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.personaDna.getNidProvincia());
        this.distritosDna = fachadaDistrito.obtenerDistritos(provincia);
    }
    
    /**
     * Obtener provincias
     */
     public void obtenerProvinciasBus() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.busDepartamento);
        this.provinciasBus = fachadaProvincia.obtenerProvincias(departamento);
    }
    
     /**
      * Obtener distritos
      */
    public void obtenerDistritosBus() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.busProvincia);
        this.distritosBus = fachadaDistrito.obtenerDistritos(provincia);
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
        if(this.personaDna.getFuncion() != null && this.personaDna.getFuncion().getNidCatalogo() != null){
           for(PersonaDna personal : this.listaPersonal){
                if(personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)) {
                     return true;
                }
            }
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
     * Cargar nuevas Dna
     */
    public void cargarNuevasDna(){
         ParametroNodoObject param = new ParametroNodoObject();
         Catalogo estado = new Catalogo();
         estado.setNidCatalogo(Constantes.CATALOGO_ESTADO_NUEVO);
         param.adicionar("estado", estado);
         this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosObject(param, true, "txtNombre", true);

    }
   
    /**
     * Anular Dna
     */
    public void anularDna() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.defensoria.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.defensoria.setTxtPc(Internet.obtenerNombrePC());
            this.defensoria.setTxtIp(Internet.obtenerIPPC());
            this.defensoria.setFecModificacion(new Date());
            this.defensoriaFacade.edit(this.defensoria);
        } catch (Exception e) {
           
        }
    }
   
      /**
    * Validar el formulario de defensoria. 
    * Si seleccionó ANEXO entonces debe buscar una sede central antes de guardar
    * @param event - ComponentSystemEvent - evento posterior a la validación de componentes
    */
   public void validarDna(final ComponentSystemEvent event) {
       
        FacesContext fc = FacesContext.getCurrentInstance();
        final UIComponent formulario = event.getComponent();
        final UIInput txtCodigo = (UIInput) formulario.findComponent("txtCodigo");
       
        if(this.defensoria.getTxtTipo().equals(Constantes.TIPO_SEDE_ANEXO) && this.nombreSede.equals("")) {
            adicionarMensajeError("Error","Debe ingresar el código de DNA de la sede central");
            txtCodigo.setValid(false);
            fc.validationFailed();
            fc.renderResponse(); 
        }

    }
   
    
    /**
     * Crear Dna
     */
    public void createDna() {
        
        try {
            
            Catalogo estado = new Catalogo();
            estado.setNidCatalogo(Constantes.CATALOGO_ESTADO_NUEVO);// id catalogo para estado NUEVO
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            
            if(this.defensoria.getTxtTipo().equals(Constantes.TIPO_SEDE_CENTRAL)) {
                String cidDepartamento = StringUtils.leftPad(String.valueOf(this.defensoria.getNidDepartamento()), 2, '0') ;
                String numero = StringUtils.leftPad(String.valueOf(800 + this.defensoriaFacade.getNroConstancia(this.defensoria.getNidDepartamento())),3,'0');
                this.defensoria.setTxtConstancia(cidDepartamento.concat(numero));
            } else 
                this.defensoria.setTxtConstancia(this.codigo);
            
            this.defensoria.setMigrado(0);
            this.defensoria.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.defensoria.setEstado(estado);
            this.defensoria.setTxtPc(Internet.obtenerNombrePC());
            this.defensoria.setTxtIp(Internet.obtenerIPPC());
            this.defensoria.setFecRegistro(new Date());
            this.defensoria.setFlgActivo(BigInteger.ONE);
            this.defensoriaFacade.create(this.defensoria);
            
            // muestra el listado de defensorias nuevas
            this.tabView.setActiveIndex(2);
            this.busEstado = Constantes.CATALOGO_ESTADO_NUEVO;
            this.busTipo = "1"; // buscar defensorias
            this.cargarNuevasDna();
           
        } catch (Exception e) {
            adicionarMensajeError("Error al crear DNA", e.getMessage());
        }
  
    }
    
    /**
     * Acutalizar Dna
     */
    public void updateDna() {
         try {
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.defensoria.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.defensoria.setTxtPc(Internet.obtenerNombrePC());
            this.defensoria.setTxtIp(Internet.obtenerIPPC());
            this.defensoria.setFecModificacion(new Date());
            this.defensoria.setFlgActivo(BigInteger.ONE);
            this.defensoriaFacade.edit(this.defensoria);
           
        } catch (Exception e) {
            adicionarMensajeError("Error al Actualizar DNA", e.getMessage());
        }
    }
    
    /**
     * Enviar Correo
     */
     public void enviarCorreo() {
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarCorreo(this.responsable, correoDnaAdministrado);
    } 
    
     /**
      * Crear usuario ligado a la PersonaDna Responsable
      * @return Usuario
      */
    public Usuario crearUsuario() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
          
            TipoDocumento tipoDoc = new TipoDocumento();
            tipoDoc.setNidTipoDocumento(BigDecimal.valueOf(1));

            Persona persona = new Persona();
            persona.setTipoDocumento(tipoDoc);
            persona.setTxtDocumento(this.responsable.getTxtDocumento());
            persona.setTxtApellidoPaterno(this.responsable.getTxtApellidoPaterno());
            persona.setTxtApellidoMaterno(this.responsable.getTxtApellidoMaterno());
            persona.setTxtNombres(this.responsable.getTxtNombres());
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
          
            String encriptado = usuarioAdministrado.encriptar(Constantes.PASSWORD);
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
            adicionarMensajeError("Error:","No pudo generar el usuario");
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
                adicionarMensajeError("Error:", "Debe agregar un responsable");
                fc.validationFailed();
                fc.renderResponse(); 
            }           

            for(DocInscripcion doc : this.adjuntos) {
            
                if(doc.getTxtNombre()==null){
                    adicionarMensajeError("Debe adjuntar los archivos solicitados","");
                    fc.validationFailed();
                    fc.renderResponse(); 
                    break;
                }
            }            
    }
     
    /**
     * Preparar para el guardado
     */
    public void prepararSave() {
       
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
                    
                    Usuario usuario = this.crearUsuario();
                    this.inscripcion.setNidUsuario(usuario.getNidUsuario());
                }
            } else {
                Usuario usuario = this.crearUsuario();
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
        this.inscripcion.setFlagAcredita(BigInteger.ZERO);
        this.prepararSave();
        try {
            this.inscripcionFacade.create(this.inscripcion);
            this.inscripcion.getDna().setEstado(this.inscripcion.getEstado());
            this.defensoriaFacade.edit(this.inscripcion.getDna());
            this.verMensajeOK = true;
   
        } catch (Exception e) {
            adicionarMensajeError("Error","Error al guardar la Solicitud de Inscripción");
        }
       
        this.enviarCorreo();
          
    }
  
    /**
     * Actualizar la acreditacion
     */
    public void update() {
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
           
        this.inscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
        this.inscripcion.setFecModificacion(new Date());
        this.inscripcion.setDocumentos(this.adjuntos);
        this.prepararSave();
        try {
            this.inscripcionFacade.edit(this.inscripcion);
            this.enviarCorreo();
            this.verMensajeOK = true;
   
        } catch (Exception e) {
            adicionarMensajeError("Error","Error al guardar la Solicitud de Inscripción");
        }
       
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
      
        ParametroNodo nodo = new ParametroNodo();
        nodo.adicionar("txtCorreo", correo);
        Catalogo funcion = new Catalogo();
        funcion.setNidCatalogo(Constantes.CATALOGO_FUNCION_RESPONSABLE);
        nodo.adicionar("funcion", funcion);
        nodo.adicionar("flgActivo", BigInteger.ONE);
        
        List<PersonaDna> listaTmp = this.personaDnaFacade.obtenerPorParametros(nodo);
        if(listaTmp.size()>0) {
             if(this.personaDna.getNidPersona()==null)
                throw new ValidatorException(new FacesMessage("El correo electrónico ya ha sido registrado por otro usuario"));
             else 
                if(!this.personaDna.getNidPersona().equals(listaTmp.get(0).getNidPersona())) 
                    throw new ValidatorException(new FacesMessage("El correo electrónico ya ha sido registrado por otro usuario"));

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
         IdentificacionReniec consultaReniec = (IdentificacionReniec)getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{identificacionReniec}", IdentificacionReniec.class);
       
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
                /*
                ReniecConsultaDniPortType port = service.getReniecConsultaDniHttpsSoap11Endpoint();
                PeticionConsulta datosPersona = new PeticionConsulta();
                datosPersona.setNuDniConsulta(dni);
                datosPersona.setNuDniUsuario(Constantes.RENIEC_DNI);
                datosPersona.setNuRucUsuario(Constantes.RENIEC_RUC);
                datosPersona.setPassword(Constantes.RENIEC_PASSWORD); 
                ResultadoConsulta result = port.consultar(datosPersona);
                */
                
                this.identificacionReniec.obtenerConsultaReniec(dni);
                
                if (this.identificacionReniec.getNOMBRES() != null) {
                  this.dniValido = true;

                  this.personaDna.setTxtDocumento(dni);
                  this.personaDna.setTxtApellidoPaterno(this.identificacionReniec.getAPPAT());
                  this.personaDna.setTxtApellidoMaterno(this.identificacionReniec.getAPMAT());

                  this.separarNombres(this.identificacionReniec.getNOMBRES()); 
                  this.personaDna.setTxtDireccion(this.identificacionReniec.getDIRECCION());

                }  else {
                     throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
                }
                
                /*
                if(result.getCoResultado().equals(Constantes.RENIEC_OK)) {

                  this.dniValido = true;

                  this.personaDna.setTxtDocumento(datosPersona.getNuDniConsulta());
                  this.personaDna.setTxtApellidoPaterno(result.getDatosPersona().getApPrimer());
                  this.personaDna.setTxtApellidoMaterno(result.getDatosPersona().getApSegundo());

                  this.separarNombres(result.getDatosPersona().getPrenombres()); 
                  this.personaDna.setTxtDireccion(result.getDatosPersona().getDireccion());

                }  else {
                     throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
                }
                
                */
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
        String result = "";
        try { 
            
            //INPEAJudicialesPortType port = service_1.getINPEAJudicialesHttpsSoap11Endpoint();
            String apepat = personalDna.getTxtApellidoPaterno();
            String apemat = personalDna.getTxtApellidoMaterno();
            String nombres = personalDna.getTxtNombres();
            //result = port.getAntecedenteJudicial(apepat, apemat, nombres);
            
            this.antecedenteJudicial.obtenerAntJudicial(apepat, apemat, nombres);
            return this.antecedenteJudicial.getRESPUESTA();
            
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
            //PJAntecedentesPenalesPortType port = service_2.getPJAntecedentesPenalesHttpsSoap11Endpoint();
            String xApellidoPaterno = personalDna.getTxtApellidoPaterno();
            String xApellidoMaterno = personalDna.getTxtApellidoMaterno();
            String xNombre1 = personalDna.getTxtNombre1();
            String xNombre2 = (personalDna.getTxtNombre2()!=null)?personalDna.getTxtNombre2():"";
            String xNombre3 = (personalDna.getTxtNombre3()!=null)?personalDna.getTxtNombre3():"";
            String xDni = personalDna.getTxtDocumento();
            
            this.antecedentePenal.obtenerAntPenal(xApellidoPaterno, xApellidoMaterno, xNombre1, xNombre2, xNombre3, xDni);
            return this.antecedentePenal.getRESPUESTA();
           /*
            Holder<String> xCodigoRespuesta = new Holder<>();
            Holder<String> xMensajeRespuesta = new Holder<>();
            port.verificarAntecedentesPenales(xApellidoPaterno, xApellidoMaterno, xNombre1, xNombre2, xNombre3, xDni, CredencialPoderJudicial.obtenerMotivoConsulta(), CredencialPoderJudicial.obtenerProcesoEntidad(), CredencialPoderJudicial.obtenerRucEntidad(), CredencialPoderJudicial.obtenerIPPublico(), xDni, CredencialPoderJudicial.obtenerPC(), CredencialPoderJudicial.obtenerIP(), CredencialPoderJudicial.obtenerUsuario(), CredencialPoderJudicial.obtenerMAC(), xCodigoRespuesta, xMensajeRespuesta);
            if (null != xMensajeRespuesta.value) {
               return xMensajeRespuesta.value;
            } else {
                return "NO SE ENCONTRARON RESULTADOS";
            }
            */
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
        
        //MininterAntPolicialesPortType port = service_3.getMininterAntPolicialesHttpsSoap11Endpoint();
        // TODO initialize WS operation arguments here
        String vDNI = personalDna.getTxtDocumento();
        String tipoConsulta = "D";
   
        try {
        
            this.antecedentePolicial.obtenerAntPolicial(vDNI);
            return this.antecedentePolicial.getRESPUESTA();
            
        } catch (Exception e) {
            return "ERROR";
        }
        
        /*return "-";
        
        try {

            if (tipoConsulta.equals("D")) {
                String respuestaDni = port.consultaDniGeneral(CredencialMiniter.obtenerUsuario(), CredencialMiniter.obtenerClave(), 
                        vDNI, CredencialMiniter.obtenerEntidad(), vDNI);
                
                if (respuestaDni.equals("0")) {
                    return "NO PRESENTA ANTECEDENTES POLICIALES VIGENTES";
                } else if (respuestaDni.equals("1")) {
                    return "PRESENTA ANTECEDENTES POLICIALES VIGENTES";
                } else if (respuestaDni.equals("101")) {
                    return "USUARIO NO REGISTRADO";
                } else if (respuestaDni.equals("102")) {
                    return "CLAVE ERRADA";
                } else if (respuestaDni.equals("103")) {
                    return "DNI INCORRECTO";
                } else if (respuestaDni.equals("104")) {
                    return "FALTA PARAMETRO DE INGRESO";
                } else if (respuestaDni.equals("105")) {
                    return "ERROR DE SERVICIO";
                } else if (respuestaDni.equals("106")) {
                    return "FALTA ENTIDAD DE CONSULTA";
                } else if (respuestaDni.equals("107")) {
                    return "FALTA DNI DE CONSULTA";
                } else {
                   return "ERROR";
                }
            } else {
                String nombres = personalDna.getTxtNombres();
                String apepat = personalDna.getTxtApellidoPaterno();
                String apemat = personalDna.getTxtApellidoMaterno();
                String respuestaNombres = port.consultaNombreGeneral(CredencialMiniter.obtenerUsuario(), CredencialMiniter.obtenerClave(), nombres, apemat, apemat, CredencialMiniter.obtenerEntidad(), CredencialMiniter.obtenerDniConsulta());

                if (respuestaNombres.equals("0")) {
                    return "NO PRESENTA ANTECEDENTES POLICIALES VIGENTES";
                } else if (respuestaNombres.equals("1")) {
                    return "PRESENTA ANTECEDENTES POLICIALES VIGENTES";
                } else if (respuestaNombres.equals("101")) {
                    return "USUARIO NO REGISTRADO";
                } else if (respuestaNombres.equals("102")) {
                    return "CLAVE ERRADA";
                } else if (respuestaNombres.equals("103")) {
                    return "DNI INCORRECTO";
                } else if (respuestaNombres.equals("104")) {
                    return "FALTA PARAMETRO DE INGRESO";
                } else if (respuestaNombres.equals("105")) {
                    return "ERROR DE SERVICIO";
                } else if (respuestaNombres.equals("106")) {
                    return "FALTA ENTIDAD DE CONSULTA";
                } else if (respuestaNombres.equals("107")) {
                    return "FALTA DNI DE CONSULTA";
                } else {
                    return "ERROR";
                }
          }
        } catch (Exception e) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, e);
            return "ERROR";
        }
          */
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
            /*
            List<DocInscripcion> listaNueva = new ArrayList<>();
            for(DocInscripcion doc : this.adjuntos){
                if(!doc.getTipo().getNidCatalogo().equals(docInscripcion.getTipo().getNidCatalogo()))listaNueva.add(doc);
            }
            listaNueva.add(docInscripcion);
            this.adjuntos = listaNueva;
            */
            adicionarMensaje("Carga de Archivo:",event.getFile().getFileName() + " terminado");
            
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
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarConstacia(this.responsable, this.inscripcion, correoDnaAdministrado);    
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
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarOficio(this.responsable, this.inscripcion, correoDnaAdministrado);
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
       this.personaSubsanar = item;
       this.personaDna = this.personaSubsanar;
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
        
        if(this.personalSubsanar != null && !this.personalSubsanar.isEmpty()){
            PersonaDna responsableTmp = this.obtenerResponsable(this.personalSubsanar);
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
        
        PersonaDna responsableTmp = this.obtenerResponsable(this.personalSubsanar);
        if(responsableTmp != null && 
            (this.personaSubsanar.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && this.personaSubsanar.getNidPersona() == null)){
                adicionarMensajeError("Error","Ya existe un responsable.");
                fc.validationFailed();
                fc.renderResponse(); 
        }
    }

    /**
     * Obtener provincias
     */
    public void obtenerProvinciasSubsanar() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.inscripcionSubsanar.getDna().getNidDepartamento());
        this.provincias = fachadaProvincia.obtenerProvincias(departamento);
    }
    
    /**
     * Obtener distritos
     */
    public void obtenerDistritosSubsanar() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.inscripcionSubsanar.getDna().getNidProvincia());
        this.distritos = fachadaDistrito.obtenerDistritos(provincia);
    }
    
    /**
     * Obtener la lista de inscripciones por Responsable
     * @return lista de inscripciones
     */
    public List<Inscripcion> obtenerListaPorResponsable() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class); 
            List<Inscripcion> inscripciones = this.inscripcionFacade.findAllByFieldOrder("nidUsuario", usuarioAdministrado.getEntidadSeleccionada().getNidUsuario(), true, "fecRegistro", false);
            return inscripciones;
        }catch(Exception ex){
        
        }
        return null;
    }
    
    /**
     * Obtener la inscripcion a subsanar
     * @param item - Inscripcion
     */
    public void obtenerInscripcionSubsnar(Inscripcion item) {
        this.inscripcionSubsanar = item;
        this.personalRemover = new ArrayList<>();
        this.personalSubsanar = this.obtenerPersonalActivo(this.inscripcionSubsanar.getPersonal());
        this.adjuntos = this.obtenerAdjuntos(this.inscripcionSubsanar);
        this.responsableSubsanar = this.obtenerResponsable(this.personalSubsanar);
        this.correoResponsable = this.responsableSubsanar.getTxtCorreo();
        this.obtenerProvinciasSubsanar();
        this.obtenerDistritosSubsanar();
    }
    
    /**
     * Subsanar Inscripcion
     */
    public void subsanar() {
         try {
            PersonaDna responsableTmp = this.obtenerResponsable(this.personalSubsanar);
            if(responsableTmp == null){
                adicionarMensajeError("Error","El personal debe contener un responsable por lo menos.");
                return;
            }
            Catalogo estado = new Catalogo();
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            estado.setNidCatalogo(Constantes.CATALOGO_ESTADO_SUBSANADA);
            for(PersonaDna persona : this.personalSubsanar){
                if(persona.getNidPersona()== null){
                    persona.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    persona.setTxtPj(this.verificarPJPenales(persona));
                    persona.setTxtInpe(this.verificarInpeJudiciales(persona));
                    persona.setTxtPnp(this.verificarPnp(persona));
                    persona.setInscripcion(this.inscripcionSubsanar);
                    persona.setTxtPc(Internet.obtenerNombrePC());
                    persona.setTxtIp(Internet.obtenerIPPC());
                    persona.setFecRegistro(new Date());
                    this.inscripcionSubsanar.getPersonal().add(persona);
                }else{
                    persona.setFecModificacion(new Date());
                    if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)){
                        if(!persona.getTxtCorreo().equals(this.correoResponsable)){
                            this.cambiarDeResponsable(this.personalSubsanar, this.inscripcionSubsanar);
                        }
                    }
                }
            }
            for(PersonaDna persona : this.personalRemover){
                if(persona.getNidPersona() != null && persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)){
                    this.cambiarDeResponsable(this.personalSubsanar,this.inscripcionSubsanar);
                }
                persona.setFlgActivo(BigInteger.ZERO);
                persona.setFecModificacion(new Date());
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
            this.inscripcionSubsanar.getDna().setEstado(estado);
            this.defensoriaFacade.edit(this.inscripcionSubsanar.getDna());
            
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
        this.responsable = this.obtenerResponsable(personal);
        Usuario usuarioActual = this.usuarioFacade.find(inscripcion.getNidUsuario());
        usuarioActual.setFlgActivo(BigInteger.ZERO);
        this.usuarioFacade.edit(usuarioActual);
        Usuario usuarioNuevo = this.crearUsuario();
        inscripcion.setNidUsuario(usuarioNuevo.getNidUsuario());
        
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
        this.personalSubsanar = this.obtenerPersonalActivo(this.personalSubsanar);
    }
    /**
     * Remover a la PersonaDna de el personal
     */
    public void removerPersonaSubsanar(){
        this.personalSubsanar.remove(this.personaRemover);
        this.personalRemover.add(this.personaRemover);
        if(this.esResponsable(this.personaRemover)){
            this.adicionarMensajeError("Se removio Responsable", "Debe agregar un nuevo responsable.");
        }
    }
    
    /**
     * Agregar persona a personal de una acreditacion a subsanar
     */
    public void agregarPersonaSubsanar(){
        if(this.personaDna.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) &&
                this.obtenerResponsable(this.personalSubsanar) != null){
                this.adicionarMensajeError("Ya existe Responsable", "Debe agregar una nueva persona con otra función.");
            return;
        }
        PersonaDna personaAgregar = this.personaDna;
        Catalogo funcion = this.catalogoFacade.find(personaAgregar.getFuncion().getNidCatalogo());
        personaAgregar.setFuncion(funcion);
        personaAgregar.setFlgActivo(BigInteger.ONE);
        this.personalSubsanar.add(personaAgregar);
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoInsVer() { 
        if(this.inscripcionVer == null || this.inscripcionVer.getDna() == null) return "";
        return (this.inscripcionVer.getDna().getNidDepartamento()!=null)?fachadaDepartamento.find(this.inscripcionVer.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre la provincia
     * @return String
     */
    public String getNombreProvinciaInsVer() {
        if(this.inscripcionVer == null || this.inscripcionVer.getDna() == null) return "";
        return (this.inscripcionVer.getDna().getNidProvincia()!=null)?fachadaProvincia.find(this.inscripcionVer.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el distrito
     * @return String
     */
    public String getNombreDistritoInsVer() {
        if(this.inscripcionVer == null || this.inscripcionVer.getDna() == null) return "";
        return (this.inscripcionVer.getDna().getNidDistrito()!=null)?fachadaDistrito.find(this.inscripcionVer.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoVer() { 
        if(this.personaVer == null) return "";
        return (this.personaVer.getNidDepartamento()!=null)?fachadaDepartamento.find(this.personaVer.getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaVer() {
        if(this.personaVer == null) return "";
        return (this.personaVer.getNidProvincia()!=null)?fachadaProvincia.find(this.personaVer.getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener nombre de el distrito 
     * @return String
     */
    public String getNombreDistritoVer() {
        if(this.personaVer == null) return "";
        return (this.personaVer.getNidDistrito()!=null)?fachadaDistrito.find(this.personaVer.getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el grado de instruccion
     * @return String
     */
    public String getNombreGradoDeInstruccionVer() {
        if(this.personaVer == null) return "";
        return (this.personaVer.getNidInstruccion()!=null)?catalogoFacade.find(BigInteger.valueOf(this.personaVer.getNidInstruccion())).getTxtNombre():"";
    }
    
    /**
     * Obtener la la fecha del curson con el formato MM/dd/yyyy
     * @return String
     */
    public String getFechaCursoTexto() {
        if(this.personaVer == null || this.personaVer.getTxtFechaCurso() == null) return "";
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(this.personaVer.getTxtFechaCurso());
        return date;
    }
    
    /**
     * Obtener la inscripcion a ver
     * @param inscripcion - Inscripcion
     */
    public void obtenerInscripcionVer(Inscripcion inscripcion){
        this.inscripcionVer = inscripcion;
        this.personalVer = this.obtenerPersonalActivo(inscripcion.getPersonal());
        if(inscripcion.getEstado().getNidCatalogo()!= Constantes.CATALOGO_ESTADO_NUEVO)  
           this.adjuntos = this.inscripcionVer.getDocumentos() != null ? this.inscripcionVer.getDocumentos(): new ArrayList<DocInscripcion>();
    } 
    
    /**
     * Obtener la PersonaDna para el panel Ver
     * @param persona - PersonaDna
     */
    public void obtenerPersonaVer(PersonaDna persona){
            this.personaVer = persona;
    }
    
    /**
     * Obtener el nombre de la instruccion de la PersonaDna
     * @return String
     */
    public String getNombreInstruccion(){
        if(this.personaDna == null ||this.personaDna.getNidInstruccion() == null){return "";}
        else{ return this.catalogoFacade.find(BigInteger.valueOf(this.personaDna.getNidInstruccion())).getTxtNombre();}
    }
    
    /**
     * Obtener el nombre de la instruccion de la PersonaDnaEval
     * @return String
     */
    public String getNombreInstruccionEval(){
        if(this.personaDna == null || this.personaDna.getPersonaEval().getNidInstruccion() == null){return "";}
        else {return this.catalogoFacade.find(BigInteger.valueOf(this.personaDna.getPersonaEval().getNidInstruccion())).getTxtNombre();}
    }
    
    /**
     * Obtener el nombre de la instruccioon de la PersonaDnaEval
     * @return String
     */
    public String getNombreInstruccionCloneEval(){
        if(this.clonePersonaDnaEval == null || this.clonePersonaDnaEval.getNidInstruccion() == null){return "";}
        else{return this.catalogoFacade.find(BigInteger.valueOf(this.clonePersonaDnaEval.getNidInstruccion())).getTxtNombre();}
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoSub() {
        return (this.inscripcionSubsanar!=null &&
                this.inscripcionSubsanar.getDna()!=null && 
                    this.inscripcionSubsanar.getDna().getNidDepartamento()!=null)?fachadaDepartamento.find(this.inscripcionSubsanar.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaSub() {
        return (this.inscripcionSubsanar!=null &&
                this.inscripcionSubsanar.getDna()!=null && 
                this.inscripcionSubsanar.getDna().getNidProvincia()!=null)?fachadaProvincia.find(this.inscripcionSubsanar.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del distrito
     * @return String
     */
    public String getNombreDistritoSub() {
        return (this.inscripcionSubsanar!=null &&
                this.inscripcionSubsanar.getDna()!=null && 
                this.inscripcionSubsanar.getDna().getNidDistrito()!=null)?fachadaDistrito.find(this.inscripcionSubsanar.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del Departamento
     * @return String
     */
    public String getNombreDepartamentoEvaluar() {
        return (this.inscripcionAEvaluar.getDna().getNidDepartamento()!=null)?fachadaDepartamento.find(this.inscripcionAEvaluar.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaEvaluar() {
        return (this.inscripcionAEvaluar.getDna().getNidProvincia()!=null)?fachadaProvincia.find(this.inscripcionAEvaluar.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el distrito
     * @return String
     */
    public String getNombreDistritoEvaluar() {
        return (this.inscripcionAEvaluar.getDna().getNidDistrito()!=null)?fachadaDistrito.find(this.inscripcionAEvaluar.getDna().getNidDistrito()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del departamento
     * @return String
     */
    public String getNombreDepartamentoPersonaEvaluar() {
        return (this.clonePersonaDnaEval != null && this.clonePersonaDnaEval.getNidDepartamento()!=null)?fachadaDepartamento.find(this.clonePersonaDnaEval.getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @return String
     */
    public String getNombreProvinciaPersonaEvaluar() {
        return (this.clonePersonaDnaEval != null && this.clonePersonaDnaEval.getNidProvincia()!=null)?fachadaProvincia.find(this.clonePersonaDnaEval.getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de el distrito
     * @return String
     */
    public String getNombreDistritoPersonaEvaluar() {
        return (this.clonePersonaDnaEval != null &&  this.clonePersonaDnaEval.getNidDistrito()!=null)?fachadaDistrito.find(this.clonePersonaDnaEval.getNidDistrito()).getTxtDescripcion():"";
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
         inscripcionEval.setEstado(inscripcion.getEstado());
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
             personaDnaEval.setNidInstruccion(persona.getNidInstruccion());
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
        try {
            PersonaDnaEval clone =(PersonaDnaEval)personaEval.clonar();
            this.clonePersonaDnaEval = clone;
            this.personaDnaEval = personaEval;
            this.initPanelPersonaEvaluar(clonePersonaDnaEval);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Obtener la inscripcion a evaluar
     * @param item - Inscripcion
     */
    public void obtenerInscripcionAEvaluar(Inscripcion item) {
        this.flgCorreoObs = false;
        this.flgDiasHoraObs = false;
        this.flgDireccionObs = false;
        this.flgGerenciaObs = false;
        this.flgNroAmbObs = false;
        this.flgNroAmbPrivObs = false;
        this.flgPresupuestoObs = false;
        this.inscripcionAEvaluar = this.inscripcionFacade.find(item.getNidInscripcion());
        item.setPersonal(this.inscripcionAEvaluar.getPersonal());
        item.setDocumentos(this.inscripcionAEvaluar.getDocumentos());
        this.inscripcionAEvaluar = item;
        this.personalESubsanar = this.obtenerPersonalActivo(this.inscripcionAEvaluar.getPersonal());
        this.flgDocObs = false;
        this.inscripcionEval = this.generarInscripcionEval(this.inscripcionAEvaluar);
        this.personasDnaEval = this.obtenerPersonalActivoEval(inscripcionEval.getPersonal());
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
    
    /**
     * Evaluar la inscripcion
     */
    public void evaluarInscripcion(){
        try{
            if(this.flgDenegarInscripcion){
                Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_DENEGADA);
                this.inscripcionAEvaluar.setFecDenegado(new Date());
                this.inscripcionAEvaluar.setFecModificacion(new Date());
                this.inscripcionAEvaluar.setEstado(estado);
                this.inscripcionFacade.edit(this.inscripcionAEvaluar);
                // cambia estado de defensoría
                this.inscripcionAEvaluar.getDna().setEstado(estado);
                this.defensoriaFacade.edit(this.inscripcionAEvaluar.getDna());
                
            }else if(this.inscripcionAEvaluar.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_POR_EVALUAR)){
                this.inscripcionAEvaluar.setObsDenegado(null);
                if(this.existeObservacionInscripcion(this.inscripcionEval)){
                    this.inscripcionEval.setTxtPc(Internet.obtenerNombrePC());
                    this.inscripcionEval.setTxtIp(Internet.obtenerIPPC());
                    this.inscripcionEval.setFecRegistro(new Date());
                    this.inscripcionEval.setFlgActivo(BigInteger.ONE);
                    this.inscripcionEvalFacade.create(inscripcionEval);  

                    Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_OBSERVADA);
                    this.inscripcionAEvaluar.setFecObservado(new Date());
                    this.inscripcionAEvaluar.setFecModificacion(new Date());
                    this.inscripcionAEvaluar.setEstado(estado);
                    this.inscripcionFacade.edit(this.inscripcionAEvaluar);
                
                    // cambia estado de defensoría
                    this.inscripcionAEvaluar.getDna().setEstado(estado);
                    this.defensoriaFacade.edit(this.inscripcionAEvaluar.getDna());

                }else{
                    Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_INSCRITA);
                    this.inscripcionAEvaluar.setFecInscrito(new Date());
                    this.inscripcionAEvaluar.setFecModificacion(new Date());
                    this.inscripcionAEvaluar.setEstado(estado);
                    this.inscripcionFacade.edit(this.inscripcionAEvaluar);
                    // cambia estado de defensoría
                    this.inscripcionAEvaluar.getDna().setEstado(estado);
                    this.defensoriaFacade.edit(this.inscripcionAEvaluar.getDna());

                }
            }else if(this.inscripcionAEvaluar.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_SUBSANADA)){
                    Catalogo estado = this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_INSCRITA);
                    this.inscripcionAEvaluar.setFecInscrito(new Date());
                    this.inscripcionAEvaluar.setFecModificacion(new Date());
                    this.inscripcionAEvaluar.setEstado(estado);
                    this.inscripcionFacade.edit(this.inscripcionAEvaluar);
                      // cambia estado de defensoría
                    this.inscripcionAEvaluar.getDna().setEstado(estado);
                    this.defensoriaFacade.edit(this.inscripcionAEvaluar.getDna());

            }
        }catch (Exception e) {
            adicionarMensajeError("Problemas al evaluar la inscripción.", e.getMessage());
        }
        this.responsable = this.obtenerResponsable(this.inscripcionAEvaluar.getPersonal());
        this.enviarCorreo();
    }
    
    /**
     * Obtener lo documentos que se deben adjuntar
     * @return lista de documentos de inscripción
     */
    public List<DocInscripcion> obtenerAdjuntos() {
        try{
          Inscripcion i = new Inscripcion();
          i.setNidInscripcion(this.inscripcionEval.getInscripcion().getNidInscripcion());
          return this.docFacade.findAllByField("inscripcion", i);
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
        try{
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
        return (inscripcion.getDna().getNidDepartamento()!=null)?fachadaDepartamento.find(inscripcion.getDna().getNidDepartamento()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @param inscripcion - Inscripcion
     * @return String
     */
    public String getNombreProvinciaIns(Inscripcion inscripcion) {
        return (inscripcion.getDna().getNidProvincia()!=null)?fachadaProvincia.find(inscripcion.getDna().getNidProvincia()).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del distrito
     * @param inscripcion - Inscripcion
     * @return String
     */
    public String getNombreDistritoIns(Inscripcion inscripcion) {
        return (inscripcion.getDna().getNidDistrito()!=null)?fachadaDistrito.find(inscripcion.getDna().getNidDistrito()).getTxtDescripcion():"";
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
            Departamento departamento = this.fachadaDepartamento.find(inscripcion.getDna().getNidDepartamento());
            salida.setDepartamento(departamento.getTxtDescripcion());
        }
        if(inscripcion.getDna().getNidProvincia()!= null){
            Provincia provincia = this.fachadaProvincia.find(inscripcion.getDna().getNidProvincia());
            salida.setProvincia(provincia.getTxtDescripcion());
        }
        if(inscripcion.getDna().getNidDistrito()!= null){
            Distrito distrito = this.fachadaDistrito.find(inscripcion.getDna().getNidDistrito());
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
            salida.setNombresResp(responsable.getTxtNombres() != null ? responsable.getTxtNombres():"");
            salida.setApellidosResp(responsable.getTxtApellidoPaterno() != null &&responsable.getTxtApellidoMaterno() != null ?responsable.getTxtApellidoPaterno() + " " + responsable.getTxtApellidoMaterno():"");
            salida.setTelefonoResp(responsable.getTxtTelefono() != null ? responsable.getTxtTelefono() : "");
            salida.setCorreoResp(responsable.getTxtCorreo()!= null?responsable.getTxtCorreo() : "");
            salida.setDniResp(responsable.getTxtDocumento() != null?responsable.getTxtDocumento() : "");
            salida.setDocumentoDesignacionResp(responsable.getTxtDocDesignacion() != null ? responsable.getTxtDocDesignacion() : "");
            String gradoInstruccion = "";
            if(responsable.getNidInstruccion() != null){
                gradoInstruccion = this.catalogoFacade.find(BigInteger.valueOf(responsable.getNidInstruccion())).getTxtNombre();
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
                            persona.getTxtNombres() != null ? persona.getTxtNombres():"");
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
                        responsable.getTxtNombres() != null ? responsable.getTxtNombres():"");
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
        salida.setDepartamento(persona.getNidDepartamento()!= null ? this.fachadaDepartamento.find(persona.getNidDepartamento()).getTxtDescripcion() : "");
        salida.setProvincia(persona.getNidProvincia()!= null ? this.fachadaProvincia.find(persona.getNidProvincia()).getTxtDescripcion() : "");
        salida.setDistrito(persona.getNidDistrito()!= null ? this.fachadaDistrito.find(persona.getNidDistrito()).getTxtDescripcion() : "");
        salida.setDireccion(persona.getTxtDireccion()!= null ? persona.getTxtDireccion() : "");
        salida.setDni(persona.getTxtDocumento()!= null ? persona.getTxtDocumento() : "");
        String date = "____";
        if(persona.getTxtFechaCurso() != null){
            date = DnaUtil.obtenerFecha(persona.getTxtFechaCurso());
        }
        salida.setFechaCurso(date);
        salida.setFuncion(persona.getFuncion() != null ? persona.getFuncion().getTxtNombre() : "");
        String gradoInstruccion = "";
        if(persona.getNidInstruccion() != null){
            gradoInstruccion = this.catalogoFacade.find(BigInteger.valueOf(persona.getNidInstruccion())).getTxtNombre();
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
            adicionarMensajeError("Faltan datos", "Faltan los datos ligados al DNI.");
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

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Distrito> getDistritos() {
        return distritos;
    }

    public void setDistritos(List<Distrito> distritos) {
        this.distritos = distritos;
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

    public List<PersonaDna> getPersonasAEvaluar() {
        return personasAEvaluar;
    }

    public void setPersonasAEvaluar(List<PersonaDna> personasAEvaluar) {
        this.personasAEvaluar = personasAEvaluar;
    }

    public PersonaDna getPersonaAEvaluar() {
        return personaAEvaluar;
    }

    public void setPersonaAEvaluar(PersonaDna personaAEvaluar) {
        this.personaAEvaluar = personaAEvaluar;
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

    public Inscripcion getInscripcionAEvaluar() {
        return inscripcionAEvaluar;
    }

    public void setInscripcionAEvaluar(Inscripcion inscripcionAEvaluar) {
        this.inscripcionAEvaluar = inscripcionAEvaluar;
    }

    public String getTextoBtnEvaluar() {
        return textoBtnEvaluar;
    }

    public void setTextoBtnEvaluar(String textoBtnEvaluar) {
        this.textoBtnEvaluar = textoBtnEvaluar;
    }

    public PersonaDnaEval getClonePersonaDnaEval() {
        return clonePersonaDnaEval;
    }

    public void setClonePersonaDnaEval(PersonaDnaEval clonePersonaDnaEval) {
        this.clonePersonaDnaEval = clonePersonaDnaEval;
    }

    public Boolean getFlgDenegarInscripcion() {
        return flgDenegarInscripcion;
    }

    public void setFlgDenegarInscripcion(Boolean flgDenegarInscripcion) {
        this.flgDenegarInscripcion = flgDenegarInscripcion;
    }
    
    public Inscripcion getInscripcionVer() {
        return inscripcionVer;
    }

    public void setInscripcionVer(Inscripcion inscripcionVer) {
        this.inscripcionVer = inscripcionVer;
    }

    public List<PersonaDna> getPersonalVer() {
        return personalVer;
    }

    public void setPersonalVer(List<PersonaDna> personalVer) {
        this.personalVer = personalVer;
    }

    public PersonaDna getPersonaVer() {
        return personaVer;
    }

    public void setPersonaVer(PersonaDna personaVer) {
        this.personaVer = personaVer;
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

    public List<PersonaDna> getPersonalSubsanar() {
        return personalSubsanar;
    }

    public void setPersonalSubsanar(List<PersonaDna> personalSubsanar) {
        this.personalSubsanar = personalSubsanar;
    }

    public List<PersonaDna> getPersonalRemover() {
        return personalRemover;
    }

    public void setPersonalRemover(List<PersonaDna> personalRemover) {
        this.personalRemover = personalRemover;
    }

    public PersonaDna getPersonaSubsanar() {
        return personaSubsanar;
    }

    public void setPersonaSubsanar(PersonaDna personaSubsanar) {
        this.personaSubsanar = personaSubsanar;
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
    
}