package pe.gob.mimp.sisdna.administrado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.mimp.core.Archivo;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.core.PasswordUtil;
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
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
import pe.gob.mimp.sisdna.fachada.AcreditacionEvalFacadeLocal;
import pe.gob.mimp.sisdna.fachada.AcreditacionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.CatalogoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DocAcreditacionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.InscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaDnaAcreFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Acreditacion;
import pe.gob.mimp.sisdna.modelo.AcreditacionEval;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.DefensoriaInfo;
import pe.gob.mimp.sisdna.modelo.DefensoriaPersona;
import pe.gob.mimp.sisdna.modelo.DocAcreditacion;
import pe.gob.mimp.sisdna.modelo.Inscripcion;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.sisdna.modelo.PersonaDna;
import pe.gob.mimp.sisdna.modelo.PersonaDnaAcre;
import pe.gob.mimp.sisdna.modelo.PersonaDnaAcreEval;
import pe.gob.mimp.sisdna.reporte.modelo.ConstanciaReport;
import pe.gob.mimp.sisdna.reporte.modelo.DeclaracionJuradaReport;
import pe.gob.mimp.sisdna.reporte.modelo.Detalle04DSLDReport;
import pe.gob.mimp.sisdna.reporte.modelo.Formulario04DSLDReport;
import pe.gob.mimp.sisdna.service.CorreoService;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.sisdna.util.ConstantesReporte;
import pe.gob.mimp.sisdna.util.CorreoDnaAdministrado;
import pe.gob.mimp.sisdna.util.PdfUtil;
import pe.gob.mimp.sisdna.util.DnaUtil;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedenteJudicial;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedentePenal;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedentePolicial;
import pe.gob.mimp.webservicemimp.auxiliar.IdentificacionReniec;


@Named(value = "acreditacionAdministrado")
@ViewScoped
public class AcreditacionAdministrado extends AdministradorAbstracto implements Serializable {
    
    private Logger LOG = Logger.getLogger(InscripcionAdministrado.class.getName());
 
    private List<Defensoria> listaDefensoria;
    private List<Acreditacion> lista;
    private String busTipo;
    private String busCodigo;
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private BigInteger busEstado;
    private int indexTab = 0;
    private Acreditacion acreditacion;
    private List<DocAcreditacion> adjuntos;
    private String ruta;
    private List<PersonaDnaAcre> equipoExterno;
    private PersonaDnaAcre personaEquipoExterno;
    private PersonaDnaAcre responsableActualizar;
    private Boolean showBtnResponsable;
    private Boolean esPersonaExternaActualizar;
    private List<PersonaDnaAcre> equipoActualizar;
    private PersonaDnaAcre personaActualizar; 
    private PersonaDnaAcre bPersonaActualizar;
    private boolean dniValido;
    private List<UploadedFile> archivos;
    private AcreditacionEval acreditacionEval;
    private PersonaDnaAcreEval clonePersonaEval;
    private PersonaDnaAcreEval personaEval;
    private List<PersonaDnaAcreEval> equipoEval;
    private List<PersonaDnaAcre> equipoAEvaluar; 
    private String txtObsPersonaEval;
    private Boolean flgDocObs;
    private Boolean flgCorreoObs;
    private Boolean flgDireccionObs;
    private Boolean flgGerenciaObs;
    private Boolean flgDiasHoraObs;
    private Boolean flgPresupuestoObs;
    private Boolean flgNroAmbObs;
    private Boolean flgNroAmbPrivObs;
    private Boolean flgNorma;
    private Boolean flgAdjuntosObs;
    private Boolean flgFecOrdenanza;
    private Boolean flgNroOrdenanza;
    private Boolean flgDocs;
    private Boolean flgEstadoCons;
    private String textoBtnEvaluar;
    private Boolean flgDenegarAcreditacion;
    private Boolean flgEdadPersonaObs;
    private Boolean flgProfesionPersonaObs;
    private Boolean flgColegioPersonaObs;
    private Boolean flgLugarPersonaObs;
    private Boolean flgCorreoPersonaObs;
    private Boolean flgDireccionPersonaObs;
    private Boolean flgAntecedentePersonaObs;
    private Boolean flgFechaCursoObs;
    private Boolean flgInstruccionObs;
    private Boolean flgNroColegiaturaObs;
    private List<Provincia> provincias;
    private List<Distrito> distritos;
    private List<Provincia> provinciasDna;
    private List<Distrito> distritosDna;
    private List<Provincia> provinciasBus;
    private List<Distrito> distritosBus;
    private String rutaConstancias;
    private PersonaDnaAcre responsable;
    private PersonaDnaAcre personaRemover;
    private List<PersonaDnaAcre> equipoRemover;
    private PersonaDnaAcre personaCorreoValidar;
    private Boolean flgResponsable;
    private int modo;
    private Boolean verMensajeOK;
    private List<Defensoria> resDefensorias;
    
    @EJB
    private AcreditacionFacadeLocal acreditacionFacade;
   
    @EJB
    private PersonaDnaAcreFacadeLocal personaDnaAcreFacade;
    
    @EJB
    private PersonaDnaFacadeLocal personaDnaFacade;
    
    @EJB
    private AcreditacionEvalFacadeLocal acreditacionEvalFacade;
    
    @EJB
    private DefensoriaFacadeLocal defensoriaFacade;

    @EJB
    private ParametroDnaFacadeLocal parametroFacade;
 
    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    @EJB
    private PersonaFacadeLocal fachadaPersona;

    @EJB
    private PerfilFacadeLocal fachadaPerfil;

    @EJB
    private CatalogoFacadeLocal catalogoFacade;

    @EJB
    private EstadoUsuarioFacadeLocal fachadaEstadoUsuario;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    
    @EJB
    private DocAcreditacionFacadeLocal docFacade;
    
    @EJB
    private PerfilUsuarioFacadeLocal perfilUsuarioFacade;
    
    @EJB
    private CorreoService correoService;
    
    public AcreditacionAdministrado(){
        this.acreditacion = new Acreditacion();
        this.provincias = new ArrayList<>();
        this.distritos = new ArrayList<>();
        this.provinciasDna = new ArrayList<>();
        this.distritosDna = new ArrayList<>();
        this.equipoExterno = new ArrayList<>();
        this.dniValido = false;
        this.showBtnResponsable = false;
        this.esPersonaExternaActualizar = false;
        this.lista = new ArrayList<>();
        this.acreditacionEval = new AcreditacionEval();
        this.equipoEval = new ArrayList<>();
        this.equipoAEvaluar = new ArrayList<>();
        this.txtObsPersonaEval = "";
        this.flgFecOrdenanza = false;
        this.flgNroOrdenanza = false;
        this.flgDocs = false;
        this.flgEstadoCons = false;
        this.flgDocObs = false;
        this.flgCorreoObs = false;
        this.flgDireccionObs = false;
        this.flgGerenciaObs = false;
        this.flgDiasHoraObs = false;
        this.flgPresupuestoObs = false;
        this.flgNroAmbObs = false;
        this.flgNroAmbPrivObs = false;
        this.flgAdjuntosObs = false;
        this.textoBtnEvaluar = "";
        this.flgEdadPersonaObs = false;
        this.flgProfesionPersonaObs = false;
        this.flgColegioPersonaObs = false;
        this.flgLugarPersonaObs = false;
        this.flgCorreoPersonaObs = false;
        this.flgDireccionPersonaObs = false;
        this.flgAntecedentePersonaObs = false;
        this.flgFechaCursoObs = false;
        this.flgInstruccionObs = false;
        this.flgNroColegiaturaObs = false;
        this.flgNorma = false;
        this.flgDenegarAcreditacion = false;
        this.equipoRemover = new ArrayList<>();
        
        this.flgResponsable = false;
        this.verMensajeOK = false;
    }
    
     @PostConstruct
    public void initLoad() {
        this.busTipo = "1";
        this.initFiltro();
      
        this.adjuntos = new ArrayList<>();
        List<Catalogo> listaDoc = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_DOCUMENTO_ACREDITACION,true, "txtNombre", false);
        for(Catalogo docAd: listaDoc) {
            DocAcreditacion docACre = new DocAcreditacion();
            docACre.setTipo(docAd);
            this.adjuntos.add(docACre);
        }
        
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.RUTA_ARCHIVO);
        this.ruta = parametros.get(0).getTxtValor();
        
        parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.RUTA_CONSTANCIAS);
        this.rutaConstancias = parametros.get(0).getTxtValor();
        
        this.lista = this.acreditacionFacade.findAll();
        this.initPersona();
    }
    
       /**
     * Muestra el título del formulario según el modo
     * @return el título del modo
     */
    public String titulo() {
        switch(this.modo) {
            case Constantes.MODO_NUEVO: return "Nueva Solicitud de Acreditación"; 
            case Constantes.MODO_UPDATE: return "Actualización de Solicitud de Acreditación";
            case Constantes.MODO_EVALUACION: return "Evaluación de Solicitud de Acreditación";
            case Constantes.MODO_EVALUACION_SUBSANACION: return "Evaluación de Solicitud de Acreditación Subsanada";
            case Constantes.MODO_SUBSANAR: return "Subsanar Observaciones";
            case Constantes.MODO_VER: return "Ver Solicitud de Acreditación";
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
     * Inicializar filtros para busqueda
     */
    public void initFiltro() {
        this.busCodigo = null;
        this.busDepartamento = null;
        this.busProvincia = null;
        this.busDistrito = null;
        this.busEstado = null;
        this.lista = null;
   
    }
    
    /**
     * Metodo que se encarga de la subida de archivos
     * @param event - FileUploadEvent - evento generado al subir archivo
     */   
    public void handleFileUpload(FileUploadEvent event) {
        try {
            String tiempo = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            String name = FilenameUtils.getBaseName(event.getFile().getFileName()); 
            String ext = FilenameUtils.getExtension(event.getFile().getFileName()); 
            
            String fileName = name.concat(tiempo).concat(".").concat(ext);
            
            Archivo archivo = new Archivo(event.getFile(), BigDecimal.ZERO, "archivos");
	    Archivo.subirArchivo(archivo.getArchivoArreglo(), this.ruta.concat(fileName), this.ruta);
                        
            DocAcreditacion docAcreditacion = (DocAcreditacion)event.getComponent().getAttributes().get("rowAdjunto");
            // si está reemplazando el archivo entonces se borra el antiguo
            if(docAcreditacion.getTxtNombre() != null) {
              Files.deleteIfExists(Paths.get(this.ruta.concat(docAcreditacion.getTxtNombre())));
            }
            
            docAcreditacion.setTxtNombre(fileName);
            /*
            List<DocInscripcion> listaNueva = new ArrayList<>();
            for(DocInscripcion doc : this.adjuntos){
                if(!doc.getTipo().getNidCatalogo().equals(docInscripcion.getTipo().getNidCatalogo()))listaNueva.add(doc);
            }
            listaNueva.add(docInscripcion);
            this.adjuntos = listaNueva;
            */
            adicionarMensaje("","Carga de Archivo: " + event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al subir archivo", ex.getMessage());
        }
    }
    
    /**
     * Verificar el proceso de busqueda de dni
     * @param ctx - FacesContext 
     * @param component - UIComponent
     * @param value - valor del componente
     */
    public void verificarReniec(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        
        String dni = (String)value;
          
        if(dni==null || dni.length()<8)
              throw new ValidatorException(new FacesMessage("El DNI debe tener 8 dígitos"));

        if(!dni.equals(this.personaEquipoExterno.getTxtDocumento())) {
                IdentificacionReniec identificacionReniec = new IdentificacionReniec();
                Catalogo profesionExterno = this.personaEquipoExterno.getProfesion();
                
                /*
                if(this.personaEquipoExterno.getProfesion() == null || this.personaEquipoExterno.getProfesion().getNidCatalogo() == null){
                    adicionarMensajeError("Debes escoger la profesión de la persona.","Error");
                    this.initPersona();
                    return;
                }
           
                for(PersonaDnaAcre personal : this.equipoExterno){
                    if(personal.getTxtDocumento().equals(dni) 
                            && personal.getProfesion().getNidCatalogo().compareTo(this.personaEquipoExterno.getProfesion().getNidCatalogo()) == 0) {
                        adicionarMensajeError("Ya existe DNI que cumple con la profesión escogida.","Error");
                        this.initPersona();
                        this.personaEquipoExterno.setProfesion(profesionExterno);
                        return;
                    }
                }
             */
                this.dniValido = false;
                this.initPersona();
                  
                try {
                    
                 identificacionReniec.obtenerConsultaReniec(dni);
               
                } catch (Exception ex) {
                    throw new ValidatorException(new FacesMessage("Error al consultar el DNI"));
                }
                
                if (identificacionReniec.getNOMBRES() != null) {
                  this.dniValido = true;
                    
                  this.personaEquipoExterno.setTxtDocumento(dni);
                  this.personaEquipoExterno.setTxtApellidoPaterno(identificacionReniec.getAPPAT());
                  this.personaEquipoExterno.setTxtApellidoMaterno(identificacionReniec.getAPMAT());
                  this.separarNombres(identificacionReniec.getNOMBRES()); 
                  this.personaEquipoExterno.setTxtDireccion(identificacionReniec.getDIRECCION());
                  
                  
                     
                String[] ubigeo = identificacionReniec.getUBIGEO().split("/");
                List<Departamento> ldep = departamentoFacade.findAllByField("txtDescripcion", ubigeo[0]);
                if(!ldep.isEmpty()) {

                    this.personaEquipoExterno.setNidDepartamento(ldep.get(0).getNidDepartamento());
                    this.obtenerProvinciasDna();

                    if(this.personaEquipoExterno.getNidDepartamento().compareTo(BigDecimal.valueOf((long)7))==0) {
                        this.personaEquipoExterno.setNidProvincia(BigDecimal.valueOf((long)67));
                        this.obtenerDistritosDna();

                        List<Distrito> ldist = distritoFacade.findAllByField("txtDescripcion", ubigeo[1]);
                        if(!ldist.isEmpty())
                            this.personaEquipoExterno.setNidDistrito(ldist.get(0).getNidDistrito());

                    } else {

                        List<Provincia> lprov = provinciaFacade.findAllByField("txtDescripcion", ubigeo[1]);
                        if(!lprov.isEmpty()) {
                            this.personaEquipoExterno.setNidProvincia(lprov.get(0).getNidProvincia());
                            this.obtenerDistritosDna();
                        }
                        List<Distrito> ldist = distritoFacade.findAllByField("txtDescripcion", ubigeo[2]);
                        if(!ldist.isEmpty())
                            this.personaEquipoExterno.setNidDistrito(ldist.get(0).getNidDistrito());

                   }
                }
                    
                  

                }  else {
                     throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
                }
              
        }

    }
    
    /**
     * Seperar nombres obtenidos de reniec
     * @param nombresFull - String - nombres y apellidos
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
        
        this.personaEquipoExterno.setTxtNombre1(listaNombres.get(0));

        if(listaNombres.size()>1)
          this.personaEquipoExterno.setTxtNombre2(listaNombres.get(1));

        if(listaNombres.size()>2)
          this.personaEquipoExterno.setTxtNombre3(listaNombres.get(2));

    }
    
    /**
     * Inicializar personas
     */
    public void initPersona() {
        this.personaEquipoExterno = new PersonaDnaAcre();
        this.showBtnResponsable = false;
        this.personaActualizar = new PersonaDnaAcre();
        this.clonePersonaEval =  new PersonaDnaAcreEval();
        this.personaEval =  new PersonaDnaAcreEval();
        this.personaRemover =new PersonaDnaAcre();
     
    } 
    
    
    /**
     * Obtener lista de funciones
     * @return lista de catálogos de funciones
     */
    public List<Catalogo> obtenerFunciones() {
        List<Catalogo> listaCatalogos = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_FUNCION,true, "txtNombre", false); 
        List<Catalogo> listaFiltrada = new ArrayList<>();
        for(Catalogo funcion : listaCatalogos){
            if(funcion.getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) || funcion.getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_DEFENSOR)){
                listaFiltrada.add(funcion);
            }
        }
        return  listaFiltrada;
    }

    
    /**
     * Obtener nombre de departamento externo
     * @param nid
     * @return 
     */
    public String getNombreDepartamento(BigDecimal nid) { 
        try{
            return (nid!=null)?departamentoFacade.find(nid).getTxtDescripcion():"";
        }catch(Exception ex){
            return "";
        }
    }
    
    /**
     * Obtener nombre de provincia Externo
     * @param nid
     * @return String
     */
    public String getNombreProvincia(BigDecimal nid) {
        try{
            return (nid!=null)?provinciaFacade.find(nid).getTxtDescripcion():"";
        }catch(Exception ex){
            return "";
        }
    }
    
    /**
     * Obtener nombre de distrito externo
     * @param nid
     * @return String
     */
    public String getNombreDistrito(BigDecimal nid) {
        try{
            return (nid!=null)?distritoFacade.find(nid).getTxtDescripcion():"";
        }catch(Exception ex){
            return "";
        }
    }

    /**
     * Obtener el valor de esPersonaExternaActualizar
     * @return Boolean
     */
    public Boolean getEsPersonaExternaActualizar() {
        return esPersonaExternaActualizar;
    }

    /**
     * Dar valor a esPersonaExternaActualizar 
     * @param esPersonaExternaActualizar - Boolean
     */
    public void setEsPersonaExternaActualizar(Boolean esPersonaExternaActualizar) {
        this.esPersonaExternaActualizar = esPersonaExternaActualizar;
    }
    
    /**
     * Obtener departamentos
     * @return lista de departamentos
     */
    public List<Departamento> obtenerDepartamentos() {
        return  departamentoFacade.obtenerDepartamentos();
    }
    
    /**
     * Obtener provincias
     */
    public void obtenerProvincias() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.acreditacion.getDna().getNidDepartamento());
        this.provincias = provinciaFacade.obtenerProvincias(departamento);
    }
    
    /**
     * Obtener distritos
     */
    public void obtenerDistritos() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.acreditacion.getDna().getNidProvincia());
        this.distritos = distritoFacade.obtenerDistritos(provincia);
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
     * Verificar Inpe
     * @param personalDna - PersonaDnaAcre - personal de la fase de acreditación
     * @return String
     */
    public String verificarInpeJudiciales(PersonaDnaAcre personalDna) {
        String result = "";
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
        return result;
    }
    
    /**
     * Verificar Pj
     * @param personalDna - PersonaDnaAcre - personal de la fase de acreditación
     * @return String
     */
    public String verificarPJPenales(PersonaDnaAcre personalDna){
        
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
     * Verificar PNP
     * @param personalDna - PersonaDnaAcre - personal de la fase de acreditación
     * @return String
     */
    public String verificarPnp(PersonaDnaAcre personalDna){
        
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
     * Cambiar de tipo de busqueda
     * @param evt - evento del TabView de primefaces
     */
    public void cambiarBusqueda(TabChangeEvent evt) {
        if(this.indexTab == 1)
           this.busCodigo = null ;
        if(this.indexTab == 0) {
           this.busDepartamento = null;
           this.busProvincia = null;
           this.busDistrito = null;
        }
    }
    
    /**
     * busca las DNA con estado NUEVO para la inscripción externa
     */
    public void buscarExterno() {   
        
        this.resDefensorias = null;
        this.acreditacion = new Acreditacion();
        
        if(this.indexTab == 0 && this.busCodigo!=null ) {
             this.resDefensorias = this.defensoriaFacade.filtrarPorConstancia( this.busCodigo, Constantes.CATALOGO_DNA_INSCRITA);
        }

        if(this.indexTab == 1) {
            if(this.busDepartamento!=null){
                this.resDefensorias = this.defensoriaFacade.filtrarDepartamentos(this.busDepartamento , Constantes.CATALOGO_DNA_INSCRITA);
                return;
            }
            if(this.busProvincia!=null){
                this.resDefensorias = this.defensoriaFacade.filtrarProvincias(this.busProvincia , Constantes.CATALOGO_DNA_INSCRITA);
                return;
            }
            if(this.busDistrito!=null){
                this.resDefensorias = this.defensoriaFacade.filtrarDistritos(this.busDistrito , Constantes.CATALOGO_DNA_INSCRITA);
            }
        }
         
    }
    
    /**
     * Buscar inscripciones
     */
    public void buscar() {
        
        this.acreditacion = new Acreditacion();
       
        ParametroNodoObject param = new ParametroNodoObject();

        if(this.busTipo.equals("1")) {
            Catalogo estadoBus = new Catalogo();
            estadoBus.setNidCatalogo(Constantes.CATALOGO_DNA_INSCRITA);
            param.adicionar("estado", estadoBus);
            
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
           
            if(param.getParametros().size()>0) 
               this.listaDefensoria = this.defensoriaFacade.obtenerPorParametrosObject(param, true, "txtNombre", true);
            
        } else {
        
            if(this.indexTab == 0 && this.busCodigo!=null ) {
                 this.lista = this.acreditacionFacade.filtrarPorConstancia(this.busCodigo);
                 return;
            }

            if(this.indexTab == 1) {
                if(this.busDepartamento!=null){
                    this.lista = this.acreditacionFacade.filtrarDepartamentos(this.busDepartamento);
                    return;
                }
                if(this.busProvincia!=null){
                    this.lista = this.acreditacionFacade.filtrarProvincias(this.busProvincia);
                    return;
                }
                if(this.busDistrito!=null){
                    this.lista = this.acreditacionFacade.filtrarDistritos(this.busDistrito);
                    return;
                }
            }

            if(this.indexTab == 2 && this.busEstado!=null) {
                Catalogo estadoBus = new Catalogo();
                estadoBus.setNidCatalogo(this.busEstado);
                param.adicionar("estado", estadoBus);
            }
            if(param.getParametros().size()>0)
                this.lista = this.acreditacionFacade.obtenerPorParametrosObject(param, true, "fecRegistro", true);
        }
        
    }
    
    /**
     * Generar acreditacion
     * @param dna
     */
    public void generarAcreditacionExterna(Defensoria dna) {
        this.modo = Constantes.MODO_NUEVO;
        this.acreditacion = new Acreditacion();
        this.acreditacion.setDna(dna);
        this.acreditacion.setTxtConstancia(dna.getTxtConstancia());
        this.acreditacion.setTxtDireccion(dna.getDefensoriaInfo().getTxtDireccion());
        this.acreditacion.setDias(dna.getDefensoriaInfo().getDias());
        this.acreditacion.setTxtCorreo(dna.getDefensoriaInfo().getTxtCorreo());
        this.acreditacion.setTxtTelefono(dna.getDefensoriaInfo().getTxtTelefono());
        this.acreditacion.setTxtGerencia(dna.getDefensoriaInfo().getTxtGerencia());
        this.acreditacion.setAmbientes(dna.getDefensoriaInfo().getAmbientes());
        this.acreditacion.setAmbientesPriv(dna.getDefensoriaInfo().getAmbientesPriv());
        this.equipoExterno =  new ArrayList<>();
        this.adjuntos = new ArrayList<>();
        List<Catalogo> listaDoc = this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_DOCUMENTO_ACREDITACION,true, "txtNombre", false);
        for(Catalogo docAd: listaDoc) {
            DocAcreditacion docACre = new DocAcreditacion();
            docACre.setTipo(docAd);
            this.adjuntos.add(docACre);
        }
        DefensoriaPersona responsableAux = this.obtenerResponsableActivoPersonal(dna.getListaPersonaDna());
        if(responsableAux !=  null){
            PersonaDnaAcre responsableAcre = this.crearPersonaDnaAcre(responsableAux);
            this.equipoExterno.add(responsableAcre);
        }
    }
    
    /**
     * Crear persona 
     * @param persona - PersonaDna
     * @return PersonaDnaAcre
     */
    private PersonaDnaAcre crearPersonaDnaAcre(DefensoriaPersona persona){
        PersonaDnaAcre personaDnaAcre = new PersonaDnaAcre();
        personaDnaAcre.setEdad(persona.getEdad());
        personaDnaAcre.setTxtDocumento(persona.getTxtDocumento());
        personaDnaAcre.setFuncion(persona.getFuncion());
        personaDnaAcre.setNidDepartamento(persona.getNidDepartamento());
        personaDnaAcre.setNidProvincia(persona.getNidProvincia());
        personaDnaAcre.setNidDistrito(persona.getNidDistrito());
        personaDnaAcre.setInstruccion(persona.getInstruccion());
        personaDnaAcre.setTxtApellidoMaterno(persona.getTxtApellidoMaterno());
        personaDnaAcre.setTxtApellidoPaterno(persona.getTxtApellidoPaterno());
        personaDnaAcre.setTxtNombre1(persona.getTxtNombre1());
        personaDnaAcre.setTxtNombre2(persona.getTxtNombre2());
        personaDnaAcre.setTxtNombre3(persona.getTxtNombre3());
        personaDnaAcre.setTxtColegiatura(persona.getTxtColegiatura());
        personaDnaAcre.setTxtCorreo(persona.getTxtCorreo());
        personaDnaAcre.setTxtDireccion(persona.getTxtDireccion());
        personaDnaAcre.setTxtFechaCurso(persona.getTxtFechaCurso());
        personaDnaAcre.setTxtInpe(persona.getTxtInpe());
        personaDnaAcre.setTxtLugarCurso(persona.getTxtLugarCurso());
        personaDnaAcre.setTxtPj(persona.getTxtPj());
        personaDnaAcre.setTxtPnp(persona.getTxtPnp());
        personaDnaAcre.setProfesion(persona.getProfesion());
        personaDnaAcre.setTxtSexo(persona.getTxtSexo());
        personaDnaAcre.setTxtTelefono(persona.getTxtTelefono());
        personaDnaAcre.setTxtColegio(persona.getTxtColegio());
        personaDnaAcre.setFlgActivo(persona.getFlgActivo());
        return personaDnaAcre;
    }
    
    /**
     * Descargar acreditacion
     * @param item - instancia de Acreditacion
     * @return StreamedContent
     */
    public StreamedContent downloadAcreditacion(Acreditacion item) {
        try {
            this.imprimirFormulario04DSLDPDF(item);
            return null;
        }catch(Exception ex) { 
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }
    
    /**
     * Imprimir formulario
     * @param acreditacion - instancia de Acreditacion
     */
    private void imprimirFormulario04DSLDPDF(Acreditacion acreditacion){
        List<Formulario04DSLDReport> datasource;
        Map<String,Object> params;
        datasource = new ArrayList<>();
        params = new HashMap<>();
        datasource.add(this.crearFormulario04DSLD(acreditacion));
        params.put(ConstantesReporte.FORM04DSDL_SUBREPORT_PARAM, FacesContext.getCurrentInstance().getExternalContext().getRealPath(ConstantesReporte.FORM04DSDL_SUBREPORT_PATH));
        params.put(ConstantesReporte.DECLARACION_JURADA_PARAM, FacesContext.getCurrentInstance().getExternalContext().getRealPath(ConstantesReporte.DECLARACION_JURADA_PATH));
        try {
            PdfUtil.crearPDF(params, ConstantesReporte.FORM04DSDL_PATH, datasource, ConstantesReporte.FORM04DSDL_NAME);
        } catch (Exception ex) {
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Crear formulario
     * @param acreditacion - instancia de Acreditacion
     * @return Formulario04DSLDReport
     */
    private Formulario04DSLDReport crearFormulario04DSLD(Acreditacion acreditacion){
        Formulario04DSLDReport salida;        
        salida = new Formulario04DSLDReport();
        
        if(acreditacion != null){
            
        
                Defensoria dna = acreditacion.getDna();
                if(dna.getTxtNombre() != null)salida.setNombreDemuna(dna.getTxtNombre());
                if(dna.getTxtEntidad() != null)salida.setMunicipalidad(dna.getTxtEntidad());
                
                String nroConstancia = "00000";
                if(dna.getTxtConstancia() != null){
                    nroConstancia = dna.getTxtConstancia();
                }
                
                salida.setNroInscripcion(nroConstancia);
                if(acreditacion.getAmbientes() != null)salida.setNroAmbientes(acreditacion.getAmbientes().toString());
                if(acreditacion.getAmbientesPriv()!= null)salida.setNroAmbientesPrivados(acreditacion.getAmbientesPriv().toString());
                if(acreditacion.getTxtTelefono() != null)salida.setTelefono(acreditacion.getTxtTelefono());
                if(acreditacion.getTxtDireccion()!= null)salida.setDireccion(acreditacion.getTxtDireccion());
                if(acreditacion.getTxtCorreo()!= null)salida.setEmailContacto(acreditacion.getTxtCorreo());
                if(acreditacion.getTxtGerencia() != null)salida.setOrganoPerteneceDemuna(acreditacion.getTxtGerencia());
                if(acreditacion.getDias() != null)salida.setDiasHorasAtencion(acreditacion.getDias());
               
                if(dna.getNidDepartamento() != null){
                    salida.setDepartamento(this.departamentoFacade.find(dna.getNidDepartamento()).getTxtDescripcion());
                }
                if(dna.getNidProvincia()!= null){
                    salida.setProvincia(this.provinciaFacade.find(dna.getNidProvincia()).getTxtDescripcion());
                }
                if(dna.getNidDistrito()!= null){
                    salida.setDistrito(this.distritoFacade.find(dna.getNidDistrito()).getTxtDescripcion());
                }
            
                String date = DnaUtil.obtenerFecha(acreditacion.getFecRegistro());
                salida.setFecha("Fecha : " + date);
                String txtFecNroOrdenanza = DnaUtil.obtenerFecha(acreditacion.getFecOrdenanza());
                if(acreditacion.getTxtDocumento()!= null){
                    txtFecNroOrdenanza = txtFecNroOrdenanza + " " + acreditacion.getTxtDocumento();
                }
                salida.setFechaNroOrdenanza(txtFecNroOrdenanza);
                if(acreditacion.getTxtNorma()!= null)salida.setNormaArtOrdenanza(acreditacion.getTxtNorma());

                if(acreditacion.getFlgEquipoComputo())salida.setConEquipoDeComputo("X");
                else salida.setSinEquipoDeComputo("X");

                if(acreditacion.getFlgImpresora())salida.setConImpresora("X");
                else salida.setSinImpresora("X");

                if(acreditacion.getFlgInternet())salida.setConInternet("X");
                else salida.setSinInternet("X");

                if(acreditacion.getFlgArchivadores())salida.setConArchivadoresSeguros("X");
                else salida.setSinArchivadoresSeguros("X");

                if(acreditacion.getFlgAccesibilidad())salida.setConAccesibilidad("X");
                else salida.setSinAccesibilidad("X");

                if(acreditacion.getFlgAreaLudica())salida.setConAreaLudica("X");
                else salida.setSinAreaLudica("X");

                switch (acreditacion.getTxtEstadoCons()) {
                    case Constantes.ESTADO_CONSERVACION_BUENO:
                        salida.setConEstadoBueno("X");
                        break;
                    case Constantes.ESTADO_CONSERVACION_REGULAR:
                        salida.setConEstadoRegular("X");
                        break;
                    default:
                        salida.setConEstadoMalo("X");
                        break;
                }

            if(acreditacion.getEquipo() != null){
                for(PersonaDnaAcre persona : acreditacion.getEquipo()){
                    if(persona.getFlgActivo().equals(BigInteger.ONE)){
                        salida.getDeclaraciones().add(this.crearDeclaracionJurada(persona));
                        Detalle04DSLDReport detalle = new Detalle04DSLDReport();
                        detalle.setNombres(persona.getTxtNombre1() + " " + persona.getTxtApellidoPaterno() + " " + persona.getTxtApellidoMaterno());
                        detalle.setDni(persona.getTxtDocumento());
                        detalle.setColegio(persona.getTxtColegio()!= null ? persona.getTxtColegio(): "");
                        detalle.setColegiatura(persona.getTxtColegiatura() != null ? persona.getTxtColegiatura() : "");
                        detalle.setCelular(persona.getTxtTelefono() != null ? persona.getTxtTelefono() : "");
                        detalle.setEmail(persona.getTxtCorreo() != null ? persona.getTxtCorreo() : "");
                        detalle.setFuncion(persona.getFuncion().getTxtNombre());
                        detalle.setFechaCurso(persona.getTxtFechaCurso() != null ? DnaUtil.obtenerFecha(persona.getTxtFechaCurso()) : "____") ;
                        salida.getEquipo().add(detalle);
                    }
                }
            }
        }
        return salida;
    }
    
    /**
     * Descargar Formulario
     */
    public void descargarForm() {
        if(this.acreditacion.getNidAcreditacion() != null){
            this.acreditacion = this.acreditacionFacade.find(this.acreditacion.getNidAcreditacion());
        }
        this.imprimirFormulario04DSLDPDF(this.acreditacion);
    }
    
    /**
     * Validar equipo
     * @param personas - lista de PersonaDnaAcre
     * @return String
     */
    private String validarEquipo(List<PersonaDnaAcre> personas){
        String mensaje = "";
        Boolean tieneAbogado = false;
        Boolean tienePsicologo = false;
        Boolean tieneResponsable = this.existeResponsableActivo(personas);
        
        if(personas != null){
            for (PersonaDnaAcre persona : personas) {
                if(persona.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_ABOGADO)){tieneAbogado = true;}
                if(persona.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_PSICOLOGO)){tienePsicologo = true;}
            }
        }
        if(!tieneAbogado || !tienePsicologo || !tieneResponsable){
            mensaje += "El equipo debe contener ";
            if(!tieneResponsable){mensaje += " un Responsable ";}
            if(!tieneAbogado){mensaje += " un/a Abogado/a ";}
            if(!tienePsicologo){mensaje += " un/a Psicólogo/a ";}
        }
        
        return mensaje;
    }
    
    private String validarReponsable(List<PersonaDnaAcre> personas){
        String mensaje = "";
        Boolean tieneResponsable = this.existeResponsableActivo(personas);
        Boolean faltanDatosImportantes;
        faltanDatosImportantes = false;
        if(tieneResponsable){
            PersonaDnaAcre responsableAux = this.obtenerResponsableActivoEquipo(personas);
            if((responsableAux.getTxtCorreo() == null || responsableAux.getTxtCorreo().equals(""))
                    || responsableAux.getTxtFechaCurso() == null
                    || responsableAux.getTxtFechaCurso() == null
                    || responsableAux.getEdad() == null
                    || responsableAux.getTxtSexo() == null
                    || responsableAux.getProfesion() == null
                    || responsableAux.getTxtColegiatura() == null
                    || responsableAux.getTxtColegio() == null
                    || responsableAux.getNidDepartamento() == null
                    || responsableAux.getNidProvincia() == null
                    || responsableAux.getNidDistrito() == null
                    || responsableAux.getTxtDocumento() == null
                    || responsableAux.getTxtDireccion() == null)faltanDatosImportantes = true;
        }
        if(faltanDatosImportantes){
            mensaje = "Faltan datos para el responsable";
        }
        return mensaje;
    }
    
    /**
     * Crear declaracion jurada
     * @param persona
     * @return 
     */
    private DeclaracionJuradaReport crearDeclaracionJurada(PersonaDnaAcre persona){
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
            //gradoInstruccion = this.catalogoFacade.find(BigInteger.valueOf(persona.getNidInstruccion())).getTxtNombre();
            gradoInstruccion = persona.getInstruccion().getTxtNombre();
        }
        salida.setGradoInstruccion(gradoInstruccion);
        salida.setLugarCurso(persona.getTxtLugarCurso()!= null ? persona.getTxtLugarCurso() : "____");
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
     * Validar documentos
     * @return 
     */
    private Boolean validarDocumentosAdjuntos(){
        try {
            Boolean salida = true;
            for(DocAcreditacion doc : this.adjuntos) {
                if(doc.getTxtNombre()==null){
                     salida = false;
                     break;
                }
             
                doc.setAcreditacion(this.acreditacion);
                doc.setTxtPc(Internet.obtenerNombrePC());
                doc.setTxtIp(Internet.obtenerIPPC());
                doc.setFecRegistro(new Date());
                doc.setFlgActivo(BigInteger.ONE);
                
                UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
                if(usuarioAdministrado!=null) 
                    doc.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
             
            }
            return salida;
        }catch(Exception ex){
            return false;
        }
    }
    
       /**
      * Genera la contraseña del usuario
      * @return la contraseña
      */
     public String generaPassword() {
         return PasswordUtil.generatePassword(2, PasswordUtil.ALPHA_CAPS) + PasswordUtil.generatePassword(2, PasswordUtil.ALPHA) + PasswordUtil.generatePassword(2, PasswordUtil.SPECIAL_CHARS) + PasswordUtil.generatePassword(2, PasswordUtil.NUMERIC);
            
     }
   
    /**
     * Crear acreditacion
     */
    public void crearAcreditacionExterna() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            for(DocAcreditacion doc : this.adjuntos) {
                doc.setAcreditacion(this.acreditacion);
                doc.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                doc.setTxtPc(Internet.obtenerNombrePC());
                doc.setTxtIp(Internet.obtenerIPPC());
                doc.setFecRegistro(new Date());
                doc.setFlgActivo(BigInteger.ONE);
            }
            this.obtenerEquipoExterno();
            for (PersonaDnaAcre persona : this.equipoExterno) {
                persona.setTxtPj(this.verificarPJPenales(persona));
                persona.setTxtInpe(this.verificarInpeJudiciales(persona));
                persona.setTxtPnp(this.verificarPnp(persona));
                persona.setAcreditacion(this.acreditacion);
                
                persona.setFecRegistro(new Date());
                persona.setFlgActivo(BigInteger.ONE);
                persona.setAcreditacion(this.acreditacion);
            }
            
            this.acreditacion.setTxtPc(Internet.obtenerNombrePC());
            this.acreditacion.setTxtIp(Internet.obtenerIPPC());
            this.acreditacion.setFecRegistro(new Date());
            this.acreditacion.setFlgActivo(BigInteger.ONE);
            this.acreditacion.setEquipo(this.equipoExterno);
            this.acreditacion.setNidUsuario(this.acreditacion.getDna().getDefensoriaInfo().getNidUsuario());
            this.acreditacion.setDocumentos(this.adjuntos);
            Catalogo estado = new Catalogo();
            estado.setNidCatalogo(Constantes.CATALOGO_ESTADO_POR_EVALUAR);
            this.acreditacion.setEstado(estado);
          
            String pass = this.generaPassword();
            this.responsable = this.obtenerResponsableActivoEquipo(acreditacion.getEquipo());
           
            boolean creaUsuarioResponsable = false;
            
            if(this.seDebeCambiarDeUsuario(this.acreditacion)) {
                this.cambiarResponsable(this.acreditacion, pass);
                creaUsuarioResponsable = true;
            }
            if(acreditacion.getNidUsuario() == null) {
                 Usuario usuarioNuevo = this.crearUsuario(pass);
                 acreditacion.setNidUsuario(usuarioNuevo.getNidUsuario());
                 creaUsuarioResponsable = true;
            }

            this.acreditacionFacade.create(this.acreditacion);
            this.modo = Constantes.MODO_LISTADO;
            this.verMensajeOK = true;
            
            adicionarMensaje("","La solicitud de acreditación ha sido registrada exitósamente");
            if( creaUsuarioResponsable )
                 this.enviarCorreo(this.responsable, pass);
            
        } catch (Exception e) {
            adicionarMensajeError("Error al guardar la Acreditación", e.getMessage());
        }
        
  
    }
    
    /**
     * En cambio de funcion
     */
    public void onPersonaFuncionChange() {
        this.showBtnResponsable = false;
        if(!this.esPersonaExternaActualizar){
            PersonaDnaAcre aux = new PersonaDnaAcre();
            aux.setProfesion(new Catalogo());
            aux.setFuncion(this.personaEquipoExterno.getFuncion());
            this.personaEquipoExterno = aux;
        }
        if(!this.personaEquipoExterno.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_ACREDITADO_FUNCION_TRABAJADOR)){
            this.showBtnResponsable = true;
            PersonaDnaAcre responsableAux = null;
            for(PersonaDnaAcre persona :  this.equipoExterno){
                if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)){
                    responsableAux = persona;
                    break;
                }
            }
            if(responsableAux != null){
                for(PersonaDnaAcre persona :  this.equipoExterno){
                    if(responsableAux.getTxtDocumento().equals(persona.getTxtDocumento()) && 
                            !persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) &&
                                    !persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_ACREDITADO_FUNCION_TRABAJADOR)){
                        this.showBtnResponsable = false;
                        break;
                    }
                }
            }
        }else{
            this.showBtnResponsable = false;
        }
    }
    
    /**
     * En cambio de funcion
     */
    public void onPersonaProfesionChange() {
        if(!this.esPersonaExternaActualizar){
            PersonaDnaAcre aux = new PersonaDnaAcre();
            aux.setProfesion(this.personaEquipoExterno.getProfesion());
            this.personaEquipoExterno = aux;
        }else{
            /*Boolean existePersona = this.existeYaPersonaConDniYProfesion(this.personaEquipoExterno, this.equipoExterno);
            if(existePersona)this.adicionarMensajeError("", "Ya existe DNI que cumple con esta profesión.");*/
        }
    }
    
    /**
     * Verifica si existe responsable en el equipo ingresado
     * @param equipo - lista de PersonaDnaAcre
     * @return Boolean
     */
    public Boolean existeResponsableActivo(List<PersonaDnaAcre> equipo){
        Boolean salida = false;
        for(PersonaDnaAcre persona : equipo){
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                salida = true;
                break;
            }
        }
        return salida;
    }
    
    /**
     * Validar si es que el responsalbe cumple otra profesion
     * @param equipo - lista de PersonaDnaAcre
     * @return Boolean
     */
    public Boolean responsableYaCumpleOtraProfesion(List<PersonaDnaAcre> equipo){
        Boolean salida = true;
        PersonaDnaAcre responsableAux = this.obtenerResponsableActivoEquipo(this.equipoExterno);
        if(responsableAux.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_ABOGADO) || 
                responsableAux.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_PSICOLOGO)){
            salida = false;
        }else{
            for(PersonaDnaAcre persona: equipo){
                if(persona.getTxtDocumento().equals(responsableAux.getTxtDocumento())
                        && !persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) 
                        && (persona.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_ABOGADO)
                        || persona.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_PSICOLOGO))){
                    salida = false;
                }
            }
        }
        return salida;
    }
    
    /**
     * Verificar si la persona es abogado o psicologo
     * @param persona - PersonaDnaAcre
     * @return Boolean
     */
    public Boolean esAbogadoOPsicologo(PersonaDnaAcre persona){
        return persona != null && persona.getProfesion() != null && persona.getProfesion().getNidCatalogo() != null && (persona.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_ABOGADO) 
                || persona.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_PSICOLOGO));
    }
    
    /**
     * Verificar si la persona es responsable
     * @param persona - PersonaDnaAcre
     * @return Boolean
     */
    public Boolean esReponsable(PersonaDnaAcre persona){
        return this.flgResponsable || (persona.getFuncion() != null && 
                persona.getFuncion().getNidCatalogo()!= null && 
                persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE));
          
    }
    
    /**
     * Agregar a equipo
     */
    public void agregarAEquipo() { 
        Catalogo profesion = this.catalogoFacade.find(this.personaEquipoExterno.getProfesion().getNidCatalogo());
        Catalogo funcion = this.catalogoFacade.find(Constantes.CATALOGO_FUNCION_DEFENSOR);
        if(this.flgResponsable)funcion = this.catalogoFacade.find(Constantes.CATALOGO_FUNCION_RESPONSABLE);
        this.personaEquipoExterno.setFuncion(funcion);
        this.personaEquipoExterno.setProfesion(profesion);
        this.personaEquipoExterno.setFlgActivo(BigInteger.ONE);
        this.equipoExterno.add(this.personaEquipoExterno);
        this.showBtnResponsable = false;
        this.initPersona();
        this.obtenerEquipoExterno();
    }
    
    /**
     * Inicializar a la persona para subsanar.
     */
    public void initPersonaAdd(){
        this.personaEquipoExterno = new PersonaDnaAcre();
        this.personaCorreoValidar = this.personaEquipoExterno;
        this.showBtnResponsable = false;
        this.esPersonaExternaActualizar = false;
        this.flgResponsable = false;
        this.initPersona();
   }
    
    
    /**
     * Obtener perosna externo
     * @param persona - PersonaDnaAcre
     */
    public void obtenerPersonaExternoActualizar(PersonaDnaAcre persona){
        try {
            this.personaEquipoExterno = persona.clonar();
            this.personaActualizar = persona;
            this.personaCorreoValidar = personaEquipoExterno;
            this.obtenerProvinciasDna();
            this.obtenerDistritosDna();
            this.esPersonaExternaActualizar = true;
            this.showBtnResponsable = false;
            if(!this.personaEquipoExterno.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && 
                    (this.personaEquipoExterno.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_ABOGADO) 
                    || this.personaEquipoExterno.getProfesion().getNidCatalogo().equals(Constantes.CATALOGO_OCUPACION_PSICOLOGO)) ){
                this.showBtnResponsable = this.responsableYaCumpleOtraProfesion(this.equipoExterno);
            }
            if(persona.getInstruccion()==null)
                this.personaEquipoExterno.setInstruccion(new Catalogo());
            
        } catch (CloneNotSupportedException ex) {
            
        }
    }
    
    /**
     * Actualizar equipo
     */
    public void actualizarPersonaEquipoExterno(){
        Catalogo funcion = this.catalogoFacade.find(this.personaEquipoExterno.getFuncion().getNidCatalogo());
        this.personaEquipoExterno.setFuncion(funcion);
        this.personaActualizar.actualizar(this.personaEquipoExterno);
        this.initPersona();
        this.obtenerEquipoExterno();
    }
    
    /**
     * Obtener persona externo.
     * @param persona - PersonaDnaAcre
     */
    public void obtenerPersonaExternoEliminar(PersonaDnaAcre persona){
        this.personaEquipoExterno = persona;
    }
    
    /**
     * Elimnar persona equipo externo
     */
    public void eliminarPersonaEquipoExterno(){
        this.personaEquipoExterno.setFlgActivo(BigInteger.ZERO);
        this.initPersona();
        this.obtenerEquipoExterno();
    }
    
    /**
     * Obtener equipo externo
     */
    private void obtenerEquipoExterno(){
        List<PersonaDnaAcre> auxEquipo = new ArrayList<>();
        for(PersonaDnaAcre persona :  this.equipoExterno){
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                auxEquipo.add(persona);
            }else{
                if(!persona.getFlgActivo().equals(BigInteger.ZERO)){
                    auxEquipo.add(persona);
                }
            }
        }
        this.equipoExterno = auxEquipo;
    }
    
    /**
     * Obtener equipo externo
     */
    private List<PersonaDnaAcre> obtenerEquipoActivo(Acreditacion acreditacion){
        List<PersonaDnaAcre> auxEquipo = new ArrayList<>();
        for(PersonaDnaAcre persona :  acreditacion.getEquipo()){
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                auxEquipo.add(persona);
            }else{
                if(!persona.getFlgActivo().equals(BigInteger.ZERO)){
                    auxEquipo.add(persona);
                }
            }
        }
        return auxEquipo;
    }
    
  
 
    /**
     * Obtener fecha curso texto
     * @return String
     */
    public String getFechaCursoTexto() {
        if(this.personaEquipoExterno== null || this.personaEquipoExterno.getTxtFechaCurso() == null) return "";
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(this.personaEquipoExterno.getTxtFechaCurso());
        return date;
    }
    
    /**
     * Obtener acreditacion ver
     * @param acreditacion - instancia de Acreditación
     */
    public void obtenerAcreditacionVer(Acreditacion acreditacion){
        this.modo = Constantes.MODO_VER;
        this.acreditacion = acreditacion;
        this.equipoExterno = acreditacion.getEquipo();
       // if(acreditacion.getEstado().getNidCatalogo()!= Constantes.CATALOGO_ESTADO_NUEVO)  
           this.adjuntos = this.acreditacion.getDocumentos() != null ? this.acreditacion.getDocumentos(): new ArrayList<DocAcreditacion>();
    } 
    
    /**
     * Obtener persona ver
     * @param persona - PersonaDnaAcre
     */
    public void obtenerPersonaVer(PersonaDnaAcre persona){
            this.personaEquipoExterno = persona;
    }
    
 
    
    /**
     * Obtener lista por responsable
     * @return lista de Acreditación
     */
    public List<Acreditacion> obtenerListaPorResponsable() {
        try {
            AuthAdministrado authAdministrado = (AuthAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{authAdministrado}", AuthAdministrado.class); 
            if(authAdministrado.getDna()!=null)
                return this.acreditacionFacade.findAllByFieldOrder("dna", authAdministrado.getDna(), true, "fecRegistro", false);
            else
                return this.acreditacionFacade.findAllByFieldOrder("nidUsuario", authAdministrado.getAuthResponsable(), true, "fecRegistro", false);
    
          
        }catch(ELException ex){
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Subsanar acreditacion
     */
    public void subsanar() {
        Catalogo estado = new Catalogo();
        estado.setNidCatalogo(Constantes.CATALOGO_ESTADO_SUBSANADA);
        this.acreditacion.setEstado(estado);
        this.actualizar();
    }
    
   
    
    /**
     * Obtener adjuntos
     * @return lista de documentos de la acreditación
     */
    public List<DocAcreditacion> obtenerAdjuntosEvaluar() {
        try{
          Acreditacion i = new Acreditacion();
          i.setNidAcreditacion(this.acreditacionEval.getAcreditacion().getNidAcreditacion());
          return this.docFacade.findAllByField("acreditacion", i);
        }catch (Exception e) {
            adicionarMensajeError("No se puede cargar los adjuntos","");
        }
        return null;
    }
    
    
    /**
     * Obtener acreditacion subasanado
     * @param item - Acreditacion
     */
    public void obtenerSubsanado(Acreditacion item) {
        this.acreditacion = item;
        this.flgDenegarAcreditacion = false;
        this.equipoAEvaluar = this.obtenerEquipoActivo(this.acreditacion);
        for(DocAcreditacion doc : this.acreditacion.getDocumentos())this.adjuntos.add(doc);
        this.obtenerProvinciasDna();
        this.obtenerDistritosDna();
    }
    
    /**
     * Obtener acreditacion para constancia
     * @param item - Acreditacion
     */
    public void obtenerAcreditacionConstancia(Acreditacion item) {
        this.acreditacion = item;
    }
    
    
    
    /**
     * Obtener nombre conservacion
     * @return String
     */
    public String obtenerNombreEsConservacion(){
        String nombre;
        nombre = "";
        switch (this.acreditacion.getAcreditacionEval().getTxtEstadoCons()) {
            case "1":
                nombre="Bueno";
                break;
            case "2":
                nombre="Regular";
                break;
            default:
                nombre="Malo";
                break;
        }
        return nombre;
    }
    
    /**
     * Obtener estado.
     * @return lista de estados
     */
    public List<Catalogo> obtenerEstado() {
        List<Catalogo> estados =  this.catalogoFacade.findAllByFieldOrder("nidPadre", Constantes.CATALOGO_ESTADO,true, "txtNombre", false); 
        List<Catalogo> estadosFiltrados = new ArrayList<>();
        for(Catalogo estado : estados){
            if(!estado.getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_INSCRITA) && !estado.getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_NUEVO)){
                estadosFiltrados.add(estado);
            }
        }
        return  estadosFiltrados; 
    }
    
    /**
     * Cambiar tipo de busqueda
     * @param evt - TabChangeEvent - evento del TabView
     */
    public void cambiarBusquedaAcreditacion(TabChangeEvent evt) {
        TabView tv = (TabView) evt.getComponent();
        this.indexTab = tv.getActiveIndex();
    }
    
   
    /**
     * Descargar constancia
     * @param item - Acreditacion
     * @return StreamedContent
     */
    public StreamedContent downloadConstancia(Acreditacion item) {
        try {
            String nroConstancia = item.getTxtConstancia() != null ? item.getTxtConstancia(): "";
            String fileName = Constantes.PREFIX_ACREDITACION.
                               concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                               concat(".").concat(Constantes.EXTENSION_PDF);
            this.acreditacionFacade.edit(item);
            this.imprimirConstanciaPDF(item, fileName);
            return null;
        }catch(Exception ex) { 
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
            return null; 
        }
    }
    
    /**
     * Descargar constancia
     */
    public void downloadConstanciaPdf() {
            String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
            String fileName = Constantes.PREFIX_ACREDITACION.
                               concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                               concat(".").concat(Constantes.EXTENSION_PDF);
            this.imprimirConstanciaPDF(this.acreditacion, fileName);
    }
    
    /**
     * Crear constancia PDF
     * @param item - Acreditacion
     * @return ConstanciaReport
     */
    private ConstanciaReport crearConstanciaPDF(Acreditacion item){
        ConstanciaReport salida = new ConstanciaReport();
        
        String nombreDepartamento = this.getNombreDepartamento(item.getDna().getNidDepartamento());
        String nombreProvincia = this.getNombreProvincia(item.getDna().getNidProvincia());
        String nombreDistrito = this.getNombreDistrito(item.getDna().getNidDistrito());
        salida.setDepartamento(nombreDepartamento);
        salida.setProvincia(nombreProvincia);
        salida.setDistrito(nombreDistrito);
        salida.setTipoConstancia("Acreditación");
        String nroConstancia = "00000";        
        if(item.getTxtConstancia() != null)nroConstancia = item.getTxtConstancia();
        salida.setNrDeConstancia(nroConstancia + ConstantesReporte.PREFIJO_ACREDITACION);
        String fechaOrdenanza = "";
        if(item.getFecResolucion()!= null)fechaOrdenanza = DnaUtil.obtenerFecha(item.getFecResolucion());
        salida.setFeOrdenanza(fechaOrdenanza);
        String nroOrdenanza = "________";
        if(item.getTxtNroResolucion()!= null && !item.getTxtNroResolucion().equals(""))nroOrdenanza = item.getTxtNroResolucion();
        salida.setNrOrdenanza(nroOrdenanza);
        
        return salida;
    }
    
    /**
     * Imprimir constancia
     * @param item - Acreditacion
     * @param nombreConstancia - String
     */
    private void imprimirConstanciaPDF(Acreditacion item,String nombreConstancia){
        List<ConstanciaReport> datasource;
        Map<String,Object> params;
        datasource = new ArrayList<>();
        params = new HashMap<>();
        datasource.add(this.crearConstanciaPDF(item));
         try {
            PdfUtil.crearPDF(params, ConstantesReporte.CONSTANCIA_ACRE_PATH, datasource, nombreConstancia);
        } catch (Exception ex) {
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Subir constancia
     * @param event - FileUploadEvent - evento generado al subir archivo
     */
    public void uploadConstancia(FileUploadEvent event) {
        try {
            
            String ext = FilenameUtils.getExtension(event.getFile().getFileName()); 
            String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
            String fileName = Constantes.PREFIX_ACREDITACION.
                    concat(Constantes.PREFIX_FIRMADO).
                    concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                    concat(".").concat(ext);
            
            File fileUpload = new File(this.rutaConstancias, fileName);
         
            OutputStream out;
            out = new FileOutputStream(fileUpload);
            out.write(event.getFile().getContents());
            out.close();
            
            this.acreditacion.setFlagConstancia(1);
            
        } catch(IOException ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
            adicionarMensajeError("Error al subir archivo", ex.getMessage());
        }
    }
    
    /**
     * Descargar constancia.
     * @return StreamedContent
     */
    public StreamedContent downloadConstanciaFirmada() {
        try {
            
            String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
            String fileName = Constantes.PREFIX_ACREDITACION.
                           concat(Constantes.PREFIX_FIRMADO).
                           concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                           concat(".").concat(Constantes.EXTENSION_PDF);
            File fileUpload = new File(this.rutaConstancias, fileName);
            InputStream ips = new FileInputStream(fileUpload);
            return new DefaultStreamedContent(ips,"",fileName);
        }catch(FileNotFoundException ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Enviar constancia
     */
    public void enviarConstancia() {
        
        try {
            this.acreditacion.setFlagConstancia(1);
            this.acreditacionFacade.edit(this.acreditacion);
        }catch(Exception ex) {
            adicionarMensajeError("Error al tratar de guardar la constancia","");
        }
         
         String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
         String fileName = Constantes.PREFIX_ACREDITACION.
                           concat(Constantes.PREFIX_FIRMADO).
                           concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                           concat(".").concat(Constantes.EXTENSION_PDF);
            
        CorreoDnaAdministrado correoAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarConstancia( Constantes.TPL_CONSTANCIA_ACREDITACION,
                                             this.responsable.getNombresApellidos(), 
                                             this.responsable.getTxtCorreo(),
                                             this.acreditacion.getDna().getTxtNombre(), 
                                             this.rutaConstancias + fileName, correoAdministrado);
    }
    
    /**
     * Subir oficio.
     * @param event - FileUploadEvent - evento generado al subir archivo
     */
    public void uploadOficio(FileUploadEvent event) {
        try {
            
            String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
            String fileName = Constantes.PREFIX_OFICIO.
                    concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                    concat(".").concat(Constantes.EXTENSION_PDF);
            File fileUpload = new File(this.rutaConstancias, fileName);
         
            OutputStream out;
            out = new FileOutputStream(fileUpload);
            out.write(event.getFile().getContents());
            out.close();
            
            this.acreditacion.setFlagOficio(1);
            
        } catch(IOException ex) {
            adicionarMensajeError("Error al subir archivo", ex.getMessage());
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Enviar oficio
     */
    public void enviarOficio() {
        try {
            this.acreditacion.setFlagOficio(1);
            this.acreditacionFacade.edit(this.acreditacion);
        }catch(Exception ex) {
            adicionarMensajeError("Error al guardar el oficio","Error" );
        }
        
        String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
        String fileName = Constantes.PREFIX_OFICIO.
                    concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                    concat(".").concat(Constantes.EXTENSION_PDF);
        CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        /*
        this.correoService.enviarOficio(Constantes.TPL_OFICIO_DENEGACION_ACRE, 
                                            this.responsable.getNombresApellidos(), 
                                            this.responsable.getTxtCorreo(),
                                            this.acreditacion.getDna().getTxtNombre(), this.rutaConstancias + fileName, correoDnaAdministrado);    
        
        */
    } 
    
    /**
     * Descargar oficio
     * @return StreamedContent
     */
    public StreamedContent downloadOficio() {
        try {
         String nroConstancia = this.acreditacion.getTxtConstancia() != null ? this.acreditacion.getTxtConstancia(): "";
         String fileName = Constantes.PREFIX_OFICIO.
                            concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                            concat(".").concat(Constantes.EXTENSION_PDF);
         File fileUpload = new File(this.rutaConstancias, fileName);
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",fileName);
        }catch(FileNotFoundException ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Descargar oficio.
     * @param item - Acreditacion
     * @return StreamedContent
     */
    public StreamedContent downloadOficio(Acreditacion item) {
        try {
         String nroConstancia = item.getTxtConstancia() != null ? item.getTxtConstancia(): "";
         String fileName = Constantes.PREFIX_OFICIO.
                            concat(StringUtils.leftPad(nroConstancia, 5, '0')).
                            concat(".").concat(Constantes.EXTENSION_PDF);
         File fileUpload = new File(this.rutaConstancias, fileName);
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",fileName);
        }catch(FileNotFoundException ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Remover persona del equipo
     * @param item
     */
    public void removerPersonaActualizar(PersonaDnaAcre item ){
        this.equipoExterno.remove(item);
        this.equipoRemover.add(item);
        if(this.obtenerResponsableActivoEquipo(this.equipoExterno) == null) adicionarMensajeWarning("No existe responsable", "El equipo debe tener un responsable");
    }
    
    /**
     * Remover persona del equipo
     * @param persona
     */
    public void removerPersonaExterno(PersonaDnaAcre persona){
        this.equipoExterno.remove(persona);
        if(this.obtenerResponsableActivoEquipo(this.equipoExterno) == null) adicionarMensajeWarning("No existe responsble", "El equipo debe tener un responsable");
    }
    
    /**
     * Obtener provincia
     */
    public void obtenerProvinciasDna() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.personaEquipoExterno.getNidDepartamento());
        this.provinciasDna = provinciaFacade.obtenerProvincias(departamento);
    }
    
    /**
     * Obtener distrito
     */
    public void obtenerDistritosDna() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.personaEquipoExterno.getNidProvincia());
        this.distritosDna = distritoFacade.obtenerDistritos(provincia);
    }
    
    /**
     * Obtener lista del personal activo.
     * @param personal
     * @return lista de personal de acreditación
     */
    private List<PersonaDnaAcre> obtenerPersonalActivo(List<PersonaDnaAcre> personal){
        List<PersonaDnaAcre> personas = new ArrayList<>();
        for(PersonaDnaAcre persona : personal) {
             if(persona.getFlgActivo().equals(BigInteger.ONE))personas.add(persona);
        }
        return personas;
    }
    
    /**
     * Obtener lista de personl del equipo activos.
     * @param personal
     * @return lista de personal de acreditación
     */
    private List<PersonaDnaAcreEval> obtenerPersonalActivoEval(List<PersonaDnaAcreEval> personal){
        List<PersonaDnaAcreEval> personas = new ArrayList<>();
        for(PersonaDnaAcreEval persona : personal) {
             if(persona.getFlgActivo().equals(BigInteger.ONE))personas.add(persona);
        }
        return personas;
    }
     
    
    /**
     * Validar la creacion de una acreditacion.
     * @param event - ComponentSystemEvent - evento post validación de componentes
     */
   public void validarCreate(final ComponentSystemEvent event) {
       
        FacesContext fc = FacesContext.getCurrentInstance();
        
        if(!this.validarDocumentosAdjuntos()){     
            adicionarMensajeError("Debe adjuntar los archivos solicitados","");
            fc.validationFailed();
            fc.renderResponse(); 
        }
        String mensajeError = this.validarEquipo(this.equipoExterno);
        if(!mensajeError.equals("") ){
            adicionarMensajeError(mensajeError, "");
            fc.validationFailed();
            fc.renderResponse(); 
        }
        
        mensajeError = this.validarReponsable(this.equipoExterno);
        if(!mensajeError.equals("")){
            adicionarMensajeError(mensajeError, "");
            fc.validationFailed();
            fc.renderResponse(); 
        }
        
    }
    
   /**
    * Validar acreditacion subsanada.
    * @param event  - ComponentSystemEvent - evento post validación de componentes
    */
   public void validarSubsanar(final ComponentSystemEvent event) {
       FacesContext fc = FacesContext.getCurrentInstance();
        if(!this.validarDocumentosAdjuntos()){     
            adicionarMensajeError("Debe adjuntar los archivos solicitados","");
            fc.validationFailed();
            fc.renderResponse(); 
        }
        String mensajeError = this.validarEquipo(this.equipoExterno);
        if(!mensajeError.equals("") ){
            adicionarMensajeError(mensajeError,"");
            fc.validationFailed();
            fc.renderResponse(); 
        }
    }
   
 
    
    
    /**
     * Obtener los documentos a adjuntar.
     * @return lista de documentos de acreditación
     */
    public List<DocAcreditacion> obtenerAdjuntos() {
        try{
          Acreditacion i = new Acreditacion();
          i.setNidAcreditacion(this.acreditacionEval.getAcreditacion().getNidAcreditacion());
          return this.docFacade.findAllByField("inscripcion", i);
        }catch (Exception e) {
            adicionarMensajeError("No se puede cargar los adjuntos","Error" );
        }
        return null;
    }
    
    /**
     * Obtener nombre del documento adjunto.
     * @param doc - DocAcreditacion
     * @return StreamedContent
     */
    public StreamedContent obtenerNombreAdjunto(DocAcreditacion doc) {
        try {
         File fileUpload = new File(this.ruta, doc.getTxtNombre());
         InputStream ips = new FileInputStream(fileUpload);
         return new DefaultStreamedContent(ips,"",doc.getTxtNombre());
        }catch(FileNotFoundException ex) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Validar si se puede evaluar la acreditacion.
     * @param acreditacion - Acreditacion
     * @return Boolean
     */
    public Boolean sePuedeEvaluar(Acreditacion acreditacion){
        return !(acreditacion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_SUBSANADA)
                || acreditacion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_POR_EVALUAR));
    }
    
    /**
     * Obtener el nombre del boton para evaluar.
     * @return String
     */
    public String obtenerNombreBtnEvaluar(){
        String value = "Acreditar";
        if(this.acreditacion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_POR_EVALUAR)){
            if(this.existeObservacionAcreditacion(this.acreditacionEval))value = "Observar";
        }else if(this.acreditacion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_SUBSANADA)){
            if(this.flgDenegarAcreditacion)value = "Denegar";
        }
        return value;
    }
    
    /**
     * Verificar si existe observaciones.
     * @param acreditacionEval - AcreditacionEval
     * @return Boolean
     */
    private Boolean existeObservacionAcreditacion(AcreditacionEval acreditacionEval){
        Boolean salida = false; 
        if(this.flgDocs || this.flgEstadoCons || this.flgNroOrdenanza || this.flgFecOrdenanza ||  !acreditacionEval.getFlgAccesibilidad()
                || !acreditacionEval.getFlgArchivadores() || !acreditacionEval.getFlgAreaLudica() || !acreditacionEval.getFlgEquipoComputo()
                || !acreditacionEval.getFlgEquipoComputo() || !acreditacionEval.getFlgImpresora() || !acreditacionEval.getFlgInternet()
                || flgAdjuntosObs){
                   salida = true;
        }
        if(acreditacionEval.getEquipo()!= null){
            for(PersonaDnaAcreEval persona : acreditacionEval.getEquipo()){
                if(persona.getObsAntecedentes() != null || persona.getObsColegio()!= null
                        || persona.getObsCorreo()!= null || persona.getObsDireccion()!= null || persona.getObsEdad()!= null
                        || persona.getObsLugarDeCurso()!= null || persona.getObsProfesion()!= null){
                    salida = true;
                }
            }
        }
        return salida;
    }
    
    /**
     * Obtener la acrediacion a evaluar de la lista.
     * @param item - Acreditacion
     */
    public void obtenerAcreditacionAEvaluar(Acreditacion item) {
        this.modo = Constantes.MODO_EVALUACION;
        this.flgAdjuntosObs = false;
        this.flgEstadoCons = false;
        this.flgDocObs = false;
        this.flgCorreoObs = false;
        this.flgDireccionObs = false;
        this.flgGerenciaObs = false;
        this.flgDiasHoraObs = false;
        this.flgPresupuestoObs = false;
        this.flgNroAmbObs = false;
        this.flgNroAmbPrivObs = false;
        this.flgNroOrdenanza = false;
        this.flgFecOrdenanza = false;
        this.flgNorma = false;
        this.acreditacion = this.acreditacionFacade.find(item.getNidAcreditacion());
        item.setEquipo(this.acreditacion.getEquipo());
        item.setDocumentos(this.acreditacion.getDocumentos());
      //  item.setPadre(this.acreditacion.getPadre());
        this.acreditacion = item;
        this.flgDenegarAcreditacion = false;
        this.acreditacionEval = this.generarAcreditacionEval(this.acreditacion);
        this.equipoEval = this.obtenerPersonalActivoEval(acreditacionEval.getEquipo());
    }
    
    /**
     * Se genera una instancia de AcreditacionEval ligada a la Acreditacion actual.
     * @param acreditacion - Acreditacion
     * @return AcreditacionEval
     */
    private  AcreditacionEval generarAcreditacionEval(Acreditacion acreditacion){
        AcreditacionEval acreditacionEvalAux;
        acreditacionEvalAux = new AcreditacionEval();
        acreditacionEvalAux.setAcreditacion(acreditacion);
        acreditacionEvalAux.setTxtDocumento(acreditacion.getTxtDocumento());
        acreditacionEvalAux.setTxtDireccion(acreditacion.getTxtDireccion());
        acreditacionEvalAux.setTxtCorreo(acreditacion.getTxtCorreo());
        acreditacionEvalAux.setTxtTelefono(acreditacion.getTxtTelefono());
        acreditacionEvalAux.setTxtGerencia(acreditacion.getTxtGerencia());
        acreditacionEvalAux.setAmbientes(acreditacion.getAmbientes());
        acreditacionEvalAux.setAmbientesPriv(acreditacion.getAmbientesPriv());
        acreditacionEvalAux.setDias(acreditacion.getDias());        
        acreditacionEvalAux.setEstado(acreditacion.getEstado());
        acreditacionEvalAux.setFlgActivo(acreditacion.getFlgActivo());
        acreditacionEvalAux.setFlgAccesibilidad(acreditacion.getFlgAccesibilidad());
        acreditacionEvalAux.setFlgArchivadores(acreditacion.getFlgArchivadores());
        acreditacionEvalAux.setFlgAreaLudica(acreditacion.getFlgAreaLudica());
        acreditacionEvalAux.setFlgEquipoComputo(acreditacion.getFlgEquipoComputo());
        acreditacionEvalAux.setFlgImpresora(acreditacion.getFlgImpresora());
        acreditacionEvalAux.setFlgInternet(acreditacion.getFlgInternet());
        acreditacionEvalAux.setTxtEstadoCons(acreditacion.getTxtEstadoCons());
        acreditacionEvalAux.setFecOrdenanza(acreditacion.getFecOrdenanza());
        acreditacionEvalAux.setTxtNorma(acreditacion.getTxtNorma()); 
        
        List<PersonaDnaAcreEval> equipo = new ArrayList<>();
         for(PersonaDnaAcre persona : acreditacion.getEquipo()){
             PersonaDnaAcreEval personaDnaEval = new PersonaDnaAcreEval();
             personaDnaEval.setPersona(persona);
             personaDnaEval.setFlgActivo(persona.getFlgActivo());
             personaDnaEval.setAcreditacionEval(acreditacionEvalAux);
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
             personaDnaEval.setTxtColegio(persona.getTxtColegio());
             equipo.add(personaDnaEval);
         }
         acreditacionEvalAux.setEquipo(equipo);
         return acreditacionEvalAux;
    }
    
    /**
     * Inicializar el panel para evaluar persona.
     * @param personaDnaAcreEval - PersonaDnaAcreEval
     */
    private void initPanelPersonaEvaluar(PersonaDnaAcreEval personaDnaAcreEval){
        this.flgEdadPersonaObs = false;
        this.flgProfesionPersonaObs = false;
        this.flgColegioPersonaObs = false;
        this.flgLugarPersonaObs = false;
        this.flgCorreoPersonaObs = false;
        this.flgDireccionPersonaObs = false;
        this.flgAntecedentePersonaObs = false;
        this.flgInstruccionObs = false;
        this.flgNroColegiaturaObs = false;
        this.flgFechaCursoObs = false;
        
        if(personaDnaAcreEval.getObsEdad() != null && !personaDnaAcreEval.getObsEdad().equals("")) this.flgEdadPersonaObs = true;
        if(personaDnaAcreEval.getObsProfesion()!= null && !personaDnaAcreEval.getObsProfesion().equals("")) this.flgProfesionPersonaObs = true;
        if(personaDnaAcreEval.getObsColegio()!= null && !personaDnaAcreEval.getObsColegio().equals("")) this.flgColegioPersonaObs = true;
        if(personaDnaAcreEval.getObsCorreo()!= null && !personaDnaAcreEval.getObsCorreo().equals("")) this.flgCorreoPersonaObs = true;
        if(personaDnaAcreEval.getObsDireccion()!= null && !personaDnaAcreEval.getObsDireccion().equals("")) this.flgDireccionPersonaObs = true;
        if(personaDnaAcreEval.getObsLugarDeCurso()!= null && !personaDnaAcreEval.getObsLugarDeCurso().equals("")) this.flgLugarPersonaObs = true;
        if(personaDnaAcreEval.getObsAntecedentes()!= null && !personaDnaAcreEval.getObsAntecedentes().equals("")) this.flgAntecedentePersonaObs = true;
        if(personaDnaAcreEval.getObsInstruccion()!= null && !personaDnaAcreEval.getObsInstruccion().equals("")) this.flgInstruccionObs = true;
        if(personaDnaAcreEval.getObsColegiatura()!= null && !personaDnaAcreEval.getObsColegiatura().equals("")) this.flgNroColegiaturaObs = true;
        if(personaDnaAcreEval.getObsFechaDeCurso()!= null && !personaDnaAcreEval.getObsFechaDeCurso().equals("")) this.flgFechaCursoObs = true;
        
    }
    
    /**
     * Obtener persona a evaluar de la acreditacion.
     * @param personaDnaAcreEval - PersonaDnaAcreEval
     */
    public void obtenerPersonaAEvaluar(PersonaDnaAcreEval personaDnaAcreEval){
        try {
          
            this.clonePersonaEval = (PersonaDnaAcreEval)personaDnaAcreEval.clonar();
            this.personaEval = personaDnaAcreEval;
            this.initPanelPersonaEvaluar(clonePersonaEval);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(InscripcionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void actualizarDefensoria() {
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class); 
        
        Defensoria dna = this.acreditacion.getDna();
        DefensoriaInfo info =  dna.getDefensoriaInfo();
        if(info ==null) {
            info = new DefensoriaInfo();
            this.acreditacion.getDna().setDefensoriaInfo(info);
        }
        info.setFecAcreditacion(this.acreditacion.getFecAcreditacion());
        info.setTxtDocumento(acreditacion.getTxtDocumento());
        info.setTxtDireccion(acreditacion.getTxtDireccion());
        info.setTxtCorreo(acreditacion.getTxtCorreo());
        info.setTxtTelefono(acreditacion.getTxtTelefono());
        info.setTxtGerencia(acreditacion.getTxtGerencia());
        info.setAmbientes(acreditacion.getAmbientes());
        info.setAmbientesPriv(acreditacion.getAmbientesPriv());
        info.setDias(acreditacion.getDias());        
        info.setTxtEstadoCons(this.acreditacion.getTxtEstadoCons());
        info.setFecOrdenanza(this.acreditacion.getFecOrdenanza());
        info.setNidUsuario(this.acreditacion.getNidUsuario());
        info.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
        Catalogo estado = this.acreditacion.getDna().getEstado();
        estado.setNidCatalogo(Constantes.CATALOGO_DNA_ACREDITADA);
        dna.setEstado(estado);
        
        for(DefensoriaPersona p: dna.getListaPersonaDna()) {
            p.setFlgActivo(BigInteger.ZERO);
            p.setFecModificacion(new Date());
            p.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
        }
            
        for(PersonaDnaAcre pd: this.acreditacion.getEquipo()) {
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
            p.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            
            dna.getListaPersonaDna().add(p);
        }
        this.defensoriaFacade.edit(this.acreditacion.getDna());
        
    }
     
    /**
     * Evaluar acreditacion.
     */
    public void evaluarAcreditacion(){
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class); 
        CorreoDnaAdministrado correoAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
       
        try{
            if(this.flgDenegarAcreditacion){
                this.acreditacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                this.acreditacion.setFecDenegado(new Date());
                this.acreditacion.setFecModificacion(new Date());
                this.acreditacion.setEstado(this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_DENEGADA));
             //   this.acreditacion.getPadre().setFlagAcredita(BigInteger.ZERO);
               // this.inscripcionFacade.edit(this.acreditacion.getPadre());
                this.acreditacionFacade.edit(this.acreditacion);
                adicionarMensaje("","La solicitud de acreditación ha sido denegada exitósamente");
            }else if(this.acreditacion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_POR_EVALUAR)){
                this.acreditacion.setObsDenegado(null);
                if(this.existeObservacionAcreditacion(this.acreditacionEval)){
                    this.acreditacionEval.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.acreditacionEval.setTxtPc(Internet.obtenerNombrePC());
                    this.acreditacionEval.setTxtIp(Internet.obtenerIPPC());
                    this.acreditacionEval.setFecRegistro(new Date());
                    this.acreditacionEval.setFlgActivo(BigInteger.ONE);
                    this.acreditacionEvalFacade.create(this.acreditacionEval);  

                    this.acreditacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.acreditacion.setFecObservado(new Date());
                    this.acreditacion.setFecModificacion(new Date());
                    this.acreditacion.setEstado(this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_OBSERVADA));
                    this.acreditacionFacade.edit(this.acreditacion);
                    
                    adicionarMensaje("","La solicitud de acreditación ha sido observada exitósamente");
                    PersonaDnaAcre pr = this.obtenerResponsableActivoEquipo(this.acreditacion.getEquipo());
                    this.correoService.enviarSolicitudObservada(Constantes.TPL_ACREDITACION_OBSERVADA, 
                            pr.getNombresApellidos(), pr.getTxtCorreo(),
                            this.acreditacion.getDna().getTxtNombre(),correoAdministrado);
                }else{
                    this.acreditacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.acreditacion.setFecAcreditacion(new Date());
                    this.acreditacion.setFecModificacion(new Date());
                    this.acreditacion.setEstado(this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_ACREDITADA));
                    this.acreditacionFacade.edit(this.acreditacion);
                    this.actualizarDefensoria();
                    
                    adicionarMensaje("","La solicitud de acreditación ha sido aprobada ( acreditada ) exitósamente");
                }
            }else if(this.acreditacion.getEstado().getNidCatalogo().equals(Constantes.CATALOGO_ESTADO_SUBSANADA)){
                    this.acreditacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    this.acreditacion.setFecAcreditacion(new Date());
                    this.acreditacion.setFecModificacion(new Date());
                    this.acreditacion.setEstado(this.catalogoFacade.find(Constantes.CATALOGO_ESTADO_ACREDITADA));
                    this.acreditacionFacade.edit(this.acreditacion);
                    this.actualizarDefensoria();
                    adicionarMensaje("","La solicitud de acreditación ha sido aprobada ( acreditada ) exitósamente");
                    
            }
            this.modo = Constantes.MODO_LISTADO;
            this.buscar();
            
        }catch (Exception e) {
            adicionarMensajeError("Problemas al evaluar la acreditación.", e.getMessage());
        }
      //  this.enviarCorreo(this.obtenerResponsableActivoEquipo(this.acreditacion.getEquipo()));
    }
    
    /**
     * Evaluar persona de la acreditacion.
     */
    public void evaluarPersona(){
       try{
           this.personaEval.setObsAntecedentes(this.clonePersonaEval.getObsAntecedentes());
           this.personaEval.setObsColegio(this.clonePersonaEval.getObsColegio());
           this.personaEval.setObsCorreo(this.clonePersonaEval.getObsCorreo());
           this.personaEval.setObsDireccion(this.clonePersonaEval.getObsDireccion());
           this.personaEval.setObsEdad(this.clonePersonaEval.getObsEdad());
           this.personaEval.setObsLugarDeCurso(this.clonePersonaEval.getObsLugarDeCurso());
           this.personaEval.setObsProfesion(this.clonePersonaEval.getObsProfesion());
           this.personaEval.setObsAntecedentes(this.clonePersonaEval.getObsAntecedentes());
           this.personaEval.setObsInstruccion(this.clonePersonaEval.getObsInstruccion());
           this.personaEval.setObsColegiatura(this.clonePersonaEval.getObsColegiatura());
           this.personaEval.setObsFechaDeCurso(this.clonePersonaEval.getObsFechaDeCurso());
       }catch (Exception e) {
           adicionarMensajeError("Problemas al evaluar la acreditacion.", e.getMessage());
       }
    }
    
    /**
     * Se obtiene la Acreditacion a actualizar.
     * @param item - Acreditacion
     */
    public void obtenerAcreditacionActualizar(Acreditacion item){
        try {
            this.modo = Constantes.MODO_UPDATE;
            this.equipoRemover = new ArrayList<>();
            this.acreditacion = this.acreditacionFacade.find(item.getNidAcreditacion());
            item.setEquipo(this.acreditacion.getEquipo());
            item.setDocumentos(this.acreditacion.getDocumentos());
            item.setAcreditacionEval(this.acreditacion.getAcreditacionEval());

            this.acreditacionEval = this.acreditacion.getAcreditacionEval();
            this.adjuntos = new ArrayList<>();
            for(DocAcreditacion doc : this.acreditacion.getDocumentos())this.adjuntos.add(doc);
            this.equipoExterno = this.obtenerEquipoActivo(this.acreditacion);
           // this.inscripcionSeleccionada = this.acreditacion.getPadre();
            this.responsableActualizar = this.obtenerResponsableActivoEquipo(this.equipoExterno).clonar();
            this.obtenerResponsableActivo(this.acreditacion);
            this.flgDenegarAcreditacion = false;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(AcreditacionAdministrado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void obtenerAcreditacionEvSub(Acreditacion item){
        this.obtenerAcreditacionActualizar(item);
        this.modo = Constantes.MODO_EVALUACION_SUBSANACION;
    }
    
     /**
     * Se obtiene la Acreditacion a actualizar.
     * @param item - Acreditacion
     */
    public void obtenerAcreditacionSubsanar(Acreditacion item){
        this.obtenerAcreditacionActualizar(item);
        this.modo = Constantes.MODO_SUBSANAR;
    }
    
    
    /**
     * Actualizar acreditacion.
     */
    public void actualizar(){
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            for(PersonaDnaAcre persona : this.equipoExterno){
                if(persona.getNidPersonaAcre()== null && !this.existeYaPersonaConDniYProfesion(persona, this.acreditacion.getEquipo())){
                    persona.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    persona.setTxtPj(this.verificarPJPenales(persona));
                    persona.setTxtInpe(this.verificarInpeJudiciales(persona));
                    persona.setTxtPnp(this.verificarPnp(persona));
                    persona.setAcreditacion(this.acreditacion);
                    persona.setTxtPc(Internet.obtenerNombrePC());
                    persona.setTxtIp(Internet.obtenerIPPC());
                    persona.setFecRegistro(new Date());
                    persona.setFlgActivo(BigInteger.ONE);
                    this.acreditacion.getEquipo().add(persona);
                }else{
                    persona.setFecModificacion(new Date());
                }
            }
            PersonaDnaAcre responsableAux = this.obtenerResponsableActivoEquipo(this.equipoExterno);
            Boolean cambiarReponsable = this.seDebeCambiarResponsable( this.equipoRemover) || this.seDebeCambiarUsuario2(this.responsableActualizar,responsableAux);
            if(cambiarReponsable) {
                String pass = this.generaPassword();
                this.cambiarResponsable(this.acreditacion, pass);
            }
            for(PersonaDnaAcre persona : this.equipoRemover){
                persona.setFlgActivo(BigInteger.ZERO);
                persona.setFecModificacion(new Date());
            }
            this.acreditacion.setDocumentos(this.adjuntos);
            this.acreditacion.setTxtPc(Internet.obtenerNombrePC());
            this.acreditacion.setTxtIp(Internet.obtenerIPPC());
            this.acreditacion.setFecSubsanado(new Date());
            this.acreditacion.setFecModificacion(new Date());
            this.acreditacion.setFlgActivo(BigInteger.ONE);
            this.acreditacion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.acreditacionFacade.edit(this.acreditacion);
         //   this.inscripcionFacade.edit(this.inscripcionSeleccionada);
            this.obtenerResponsableActivo(this.acreditacion);
            this.modo = Constantes.MODO_LISTADO;
             adicionarMensaje("","La solicitud de acreditación ha sido actualizada exitósamente");
         } catch (Exception e) {
            adicionarMensajeError("Error al guardar la Acreditación","Error");
        }
    //    this.enviarCorreo(this.obtenerResponsableActivoEquipo(this.acreditacion.getEquipo()));
    }
    
    /**
     * Se obtiene el responsable de el equipo de la Acreditacion.
     * @param acreditacion , Acreditacion actual.
     */
    private void obtenerResponsableActivo(Acreditacion acreditacion){
        for(PersonaDnaAcre persona : acreditacion.getEquipo()){
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                this.responsable = persona;
                break;
            }
        }
    }
    
    /**
     * Obtener responsable Activo
     * @param equipo - lista de personal de acreditación
     * @return PersonaDnaAcre
     */
    private PersonaDnaAcre obtenerResponsableActivoEquipo(List<PersonaDnaAcre> equipo){
        PersonaDnaAcre responsableAux = null;
        for(PersonaDnaAcre persona : equipo){   
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                responsableAux = persona;
                break;
            }
        }
        return responsableAux;
    }
    
    /**
     * Obtener responsable Activo
     * @param equipo - lista de personal de la DNA
     * @return PersonaDna
     */
    private DefensoriaPersona obtenerResponsableActivoPersonal(List<DefensoriaPersona> personal){
        DefensoriaPersona responsableAux = null;
        if(personal != null){
            for(DefensoriaPersona persona : personal){   
                if(persona.getFuncion() != null && persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) && persona.getFlgActivo().equals(BigInteger.ONE)){
                    responsableAux = persona;
                    break;
                }
            }
        }
        return responsableAux;
    }
    
    /**
     * Envio de correo al responsable
     * @param responsable - persona de la DNA
     * @param pass
     */
     public void enviarCorreo(PersonaDnaAcre responsable, String pass) {
        CorreoDnaAdministrado correoAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}", CorreoDnaAdministrado.class);
        this.correoService.enviarCorreoCredenciales(responsable.getNombresApellidos(), responsable.getTxtCorreo(), pass, correoAdministrado);
    }
    
     /**
     * Obtener la fecha con el formato MM/dd/yyyy
     * @param fecha - Date
     * @return String
     */
    public String obtenerFechaConFormato(Date fecha){
        if(fecha == null) return "";
        String pattern = "MM/dd/yyyy";
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
        if( this.personaEquipoExterno.getTxtApellidoMaterno() == null || this.personaEquipoExterno.getTxtApellidoMaterno().equals("") 
                || this.personaEquipoExterno.getTxtApellidoPaterno() == null || this.personaEquipoExterno.getTxtApellidoPaterno().equals("") 
                || this.personaEquipoExterno.getTxtNombre1() == null || this.personaEquipoExterno.getTxtNombre1().equals("") ){ 
            adicionarMensajeError("Faltan datos", "Faltan los datos ligados al DNI.");
            fc.validationFailed();
            fc.renderResponse(); 
        }
    }
    
    /**
     * validador de correo, verifica que no existan correos ( usuarios ) de responsable duplicados
     * @param fc - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - genera exception si hay un error en la validación
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
        
        List<PersonaDnaAcre> listaTmpA = this.personaDnaAcreFacade.obtenerPorParametros(nodo);
        List<PersonaDna> listaTmpI = this.personaDnaFacade.obtenerPorParametros(nodo);
        if((listaTmpA.size()>0  || listaTmpI.size()>0)) {
            if(listaTmpA.size()>0){
                if(this.personaCorreoValidar.getNidPersonaAcre()==null)
                throw new ValidatorException(new FacesMessage("El correo electrónico ya ha sido registrado por otro usuario"));
             else 
                if(!this.personaCorreoValidar.getNidPersonaAcre().equals(listaTmpA.get(0).getNidPersonaAcre())) 
                    throw new ValidatorException(new FacesMessage("El correo electrónico ya ha sido registrado por otro usuario"));
            }else if(listaTmpI.size()>0){
                
            }
        }
    }
    
    /**
     * Cambiar al responsable de la Inscripcion
     * @param acreditacion - Acreditacion
     * @param inscripcion - Inscripcion
     */
    private void cambiarResponsable(Acreditacion acreditacion, String pass){
        if(acreditacion.getDna().getDefensoriaInfo().getNidUsuario() != null){
            Usuario usuarioActual = this.usuarioFacade.find(acreditacion.getDna().getDefensoriaInfo().getNidUsuario());
            usuarioActual.setFlgActivo(BigInteger.ZERO);
            this.usuarioFacade.edit(usuarioActual);
        }
        Usuario usuarioNuevo = this.crearUsuario(pass);
        acreditacion.setNidUsuario(usuarioNuevo.getNidUsuario());
    }
    
 
    
  
    public void colocarFuncionResponsable(){
        PersonaDna responsable = null;
        /*
        for(PersonaDna persona : this.inscripcionSeleccionada.getPersonal()){
            if(persona.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)){
                responsable = persona;
                break;
            }
        }
        if(responsable !=  null){
            PersonaDnaAcre responsableAcre = this.crearPersonaDnaAcre(responsable);
            responsableAcre.setFuncion(this.personaEquipoExterno.getFuncion());
            this.personaEquipoExterno = responsableAcre;
            this.obtenerProvinciasDnaExterno();
            this.obtenerDistritosDnaExterno();
        }
        */
    }
    
    /**
     * Verificar si el responsable esta siendo removido
     * @param equipoRemover - lista de personal de acreditación
     * @return Boolean
     */
    private Boolean seDebeCambiarResponsable(List<PersonaDnaAcre> equipoRemover){
        Boolean salida = false;
        PersonaDnaAcre responsableAux = this.obtenerResponsableActivoEquipo(equipoRemover);
        if(responsableAux != null && responsableAux.getNidPersonaAcre() != null) salida = true;
        return salida;
    }
    
    /**
     * Verificar si se cambio el correo de el Responsable actual
     * @param responsableActual - PersonaDnaAcre
     * @param responsableActualizar - PersonaDnaAcre
     * @return Boolean
     */
    private Boolean seDebeCambiarUsuario2(PersonaDnaAcre responsableActual , PersonaDnaAcre responsableActualizar){
        Boolean salida = false;
        if(!responsableActual.getTxtCorreo().equals(responsableActualizar.getTxtCorreo()) && responsableActualizar.getNidPersonaAcre() != null){
            salida = true;
        }
        return salida;
    }
    /**
     * Verificar si se debe cambiar el responsable
     * @param acreditacion - Acreditacion
     * @param inscripcion - Inscripcion
     * @return Boolean
     */
    private Boolean seDebeCambiarDeUsuario(Acreditacion acreditacion){
        Boolean salida = false;
        DefensoriaPersona responsableDna = this.obtenerResponsableActivoPersonal(acreditacion.getDna().getListaPersonaDna());
        PersonaDnaAcre responsableAcreditacion = this.obtenerResponsableActivoEquipo(acreditacion.getEquipo());
        if(responsableDna == null || !responsableDna.getTxtCorreo().toUpperCase().equals(responsableAcreditacion.getTxtCorreo().toUpperCase())){
            salida = true;
        } 
        return salida;
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
            this.fachadaPersona.create(persona);


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


            List<EstadoUsuario> estadousuario = this.fachadaEstadoUsuario.findAllByField("txtEstadoUsuario", "APROBADO");
            if (null != estadousuario) {
                usuario.setNidEstadoUsuario(estadousuario.get(0).getNidEstadoUsuario().toBigInteger());
            }
            
            this.usuarioFacade.create(usuario);
            
            PerfilUsuarioPK pk = new PerfilUsuarioPK();
            List<Perfil> perfiles = this.fachadaPerfil.obtenerPerfilesDelModulo(new Modulo(Constantes.NID_MODULO));
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
    
    private Boolean existeYaPersonaConDniYProfesion(PersonaDnaAcre persona , List<PersonaDnaAcre> equipo){
        Boolean salida = false;
        for(PersonaDnaAcre personaAcre : equipo){
            if(persona.getTxtDocumento().equals(personaAcre.getTxtDocumento()) && persona.getProfesion().getNidCatalogo().equals(personaAcre.getProfesion().getNidCatalogo())){
                salida = true;
                break;
            }
        }
        return salida;
    }
    
    public int getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(int indexTab) {
        this.indexTab = indexTab;
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

    public BigInteger getBusEstado() {
        return busEstado;
    }

    public void setBusEstado(BigInteger busEstado) {
        this.busEstado = busEstado;
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

    public Acreditacion getAcreditacion() {
        return acreditacion;
    }

    public void setAcreditacion(Acreditacion acreditacion) {
        this.acreditacion = acreditacion;
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
    
  
    public List<PersonaDnaAcre> getEquipoExterno() {
        return equipoExterno;
    }

    public void setEquipoExterno(List<PersonaDnaAcre> equipoExterno) {
        this.equipoExterno = equipoExterno;
    }

    public PersonaDnaAcre getPersonaEquipoExterno() {
        return personaEquipoExterno;
    }

    public void setPersonaEquipoExterno(PersonaDnaAcre personaEquipoExterno) {
        this.personaEquipoExterno = personaEquipoExterno;
    }

    public boolean isDniValido() {
        return dniValido;
    }

    public void setDniValido(boolean dniValido) {
        this.dniValido = dniValido;
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

    public List<DocAcreditacion> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<DocAcreditacion> adjuntos) {
        this.adjuntos = adjuntos;
    }
    public String getFileName(DocAcreditacion doc) {
        return doc.getTxtNombre();
    }

    public List<UploadedFile> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<UploadedFile> archivos) {
        this.archivos = archivos;
    }

    public List<Acreditacion> getLista() {
        return lista;
    }

    public void setLista(List<Acreditacion> lista) {
        this.lista = lista;
    }

    public Boolean getShowBtnResponsable() {
        return showBtnResponsable;
    }

    public void setShowBtnResponsable(Boolean showBtnResponsable) {
        this.showBtnResponsable = showBtnResponsable;
    }
    
    public PersonaDnaAcre getbPersonaActualizar() {
        return bPersonaActualizar;
    }
    
    public void setbPersonaActualizar(PersonaDnaAcre bPersonaActualizar) {
        this.bPersonaActualizar = bPersonaActualizar;
    }

    public List<PersonaDnaAcre> getEquipoActualizar() {
        return equipoActualizar;
    }

    public void setEquipoActualizar(List<PersonaDnaAcre> equipoActualizar) {
        this.equipoActualizar = equipoActualizar;
    }

    public PersonaDnaAcre getPersonaActualizar() {
        return personaActualizar;
    }

    public void setPersonaActualizar(PersonaDnaAcre personaActualizar) {
        this.personaActualizar = personaActualizar;
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
    
    public Boolean getFlgAdjuntosObs() {
        return flgAdjuntosObs;
    }

    public void setFlgAdjuntosObs(Boolean flgAdjuntosObs) {
        this.flgAdjuntosObs = flgAdjuntosObs;
    }

    public Boolean getFlgDenegarAcreditacion() {
        return flgDenegarAcreditacion;
    }

    public void setFlgDenegarAcreditacion(Boolean flgDenegarAcreditacion) {
        this.flgDenegarAcreditacion = flgDenegarAcreditacion;
    }

    public AcreditacionEval getAcreditacionEval() {
        return acreditacionEval;
    }

    public void setAcreditacionEval(AcreditacionEval acreditacionEval) {
        this.acreditacionEval = acreditacionEval;
    }

    public PersonaDnaAcreEval getClonePersonaEval() {
        return clonePersonaEval;
    }

    public void setClonePersonaEval(PersonaDnaAcreEval clonePersonaEval) {
        this.clonePersonaEval = clonePersonaEval;
    }

    public PersonaDnaAcreEval getPersonaEval() {
        return personaEval;
    }

    public void setPersonaEval(PersonaDnaAcreEval personaEval) {
        this.personaEval = personaEval;
    }

    public List<PersonaDnaAcreEval> getEquipoEval() {
        return equipoEval;
    }

    public void setEquipoEval(List<PersonaDnaAcreEval> equipoEval) {
        this.equipoEval = equipoEval;
    }

    public Acreditacion getAcreditacionAEvaluar() {
        return acreditacion;
    }

    public void setAcreditacionAEvaluar(Acreditacion acreditacion) {
        this.acreditacion = acreditacion;
    }

    public List<PersonaDnaAcre> getEquipoAEvaluar() {
        return equipoAEvaluar;
    }

    public void setEquipoAEvaluar(List<PersonaDnaAcre> equipoAEvaluar) {
        this.equipoAEvaluar = equipoAEvaluar;
    }

    public String getTxtObsPersonaEval() {
        return txtObsPersonaEval;
    }

    public void setTxtObsPersonaEval(String txtObsPersonaEval) {
        this.txtObsPersonaEval = txtObsPersonaEval;
    }
    
    public Boolean getFlgDocs() {
        return flgDocs;
    }

    public void setFlgDocs(Boolean flgDocs) {
        this.flgDocs = flgDocs;
    }

    public Boolean getFlgEstadoCons() {
        return flgEstadoCons;
    }

    public void setFlgEstadoCons(Boolean flgEstadoCons) {
        this.flgEstadoCons = flgEstadoCons;
    }

    public String getTextoBtnEvaluar() {
        return textoBtnEvaluar;
    }

    public void setTextoBtnEvaluar(String textoBtnEvaluar) {
        this.textoBtnEvaluar = textoBtnEvaluar;
    }
      
    public PersonaDnaAcre getPersonaRemover() {
        return personaRemover;
    }

    public void setPersonaRemover(PersonaDnaAcre personaRemover) {
        this.personaRemover = personaRemover;
    }

    public Boolean getFlgInstruccionObs() {
        return flgInstruccionObs;
    }

    public void setFlgInstruccionObs(Boolean flgInstruccionObs) {
        this.flgInstruccionObs = flgInstruccionObs;
    }
    
    public Boolean getFlgNroColegiaturaObs() {
        return flgNroColegiaturaObs;
    }

    public void setFlgNroColegiaturaObs(Boolean flgNroColegiaturaObs) {
        this.flgNroColegiaturaObs = flgNroColegiaturaObs;
    }

    public String getBusTipo() {
        return busTipo;
    }

    public void setBusTipo(String busTipo) {
        this.busTipo = busTipo;
    }

    public PersonaDnaAcre getPersonaCorreoValidar() {
        return personaCorreoValidar;
    }

    public void setPersonaCorreoValidar(PersonaDnaAcre personaCorreoValidar) {
        this.personaCorreoValidar = personaCorreoValidar;
    }

    public Boolean getFlgFecOrdenanza() {
        return flgFecOrdenanza;
    }

    public void setFlgFecOrdenanza(Boolean flgFecOrdenanza) {
        this.flgFecOrdenanza = flgFecOrdenanza;
    }

    public Boolean getFlgNroOrdenanza() {
        return flgNroOrdenanza;
    }

    public void setFlgNroOrdenanza(Boolean flgNroOrdenanza) {
        this.flgNroOrdenanza = flgNroOrdenanza;
    }

    public Boolean getFlgResponsable() {
        return flgResponsable;
    }

    public void setFlgResponsable(Boolean flgResponsable) {
        this.flgResponsable = flgResponsable;
    }

    public List<Defensoria> getListaDefensoria() {
        return listaDefensoria;
    }

    public void setListaDefensoria(List<Defensoria> listaDefensoria) {
        this.listaDefensoria = listaDefensoria;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public Boolean getFlgDocObs() {
        return flgDocObs;
    }

    public void setFlgDocObs(Boolean flgDocObs) {
        this.flgDocObs = flgDocObs;
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

    public Boolean getFlgNorma() {
        return flgNorma;
    }

    public void setFlgNorma(Boolean flgNorma) {
        this.flgNorma = flgNorma;
    }

    public Boolean getVerMensajeOK() {
        return verMensajeOK;
    }

    public void setVerMensajeOK(Boolean verMensajeOK) {
        this.verMensajeOK = verMensajeOK;
    }

    public List<Defensoria> getResDefensorias() {
        return resDefensorias;
    }

    public void setResDefensorias(List<Defensoria> resDefensorias) {
        this.resDefensorias = resDefensorias;
    }
    
}
