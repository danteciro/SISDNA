package pe.gob.mimp.sisdna.administrado;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.export.ExcelOptions;
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
import pe.gob.mimp.seguridad.modelo.Usuario;
import pe.gob.mimp.sisdna.modelo.PersonaPre;
import pe.gob.mimp.sisdna.fachada.CursoEvaluacionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.CursoInscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.CursoProgramacion;
import pe.gob.mimp.sisdna.modelo.CursoEvaluacion;
import pe.gob.mimp.sisdna.modelo.CursoInscripcion;
import pe.gob.mimp.sisdna.modelo.CursoInscripcionPrereq;
import pe.gob.mimp.sisdna.modelo.CursoInscripcionReq;
import pe.gob.mimp.sisdna.modelo.CursoPonderacion;
import pe.gob.mimp.sisdna.modelo.CursoPrereq;
import pe.gob.mimp.sisdna.modelo.CursoReq;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.sisdna.reporte.modelo.CursoCertificadoReport;
import pe.gob.mimp.sisdna.util.ColumnModel;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.sisdna.util.ConstantesReporte;
import pe.gob.mimp.sisdna.util.DnaUtil;
import pe.gob.mimp.sisdna.util.NumeroLetras;
import pe.gob.mimp.sisdna.util.PdfUtil;
import pe.gob.mimp.sisdna.fachada.CursoProgramacionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaPreFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Curso;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.reporte.modelo.CertificadoItemReport;
import pe.gob.mimp.sisdna.util.CorreoDnaAdministrado;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedenteJudicial;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedentePenal;
import pe.gob.mimp.webservicemimp.auxiliar.AntecedentePolicial;
import pe.gob.mimp.webservicemimp.auxiliar.IdentificacionReniec;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * Clase: CursoInscripcionAdministrado.java <br>
 * Clase encargada de gestionar las solicitudes de inscripción.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "cursoInscripcionAdministrado")
@ViewScoped
public class CursoInscripcionAdministrado extends AdministradorAbstracto implements Serializable {

 //   private static final Logger // LOGGER = LogManager.getLogger(CursoInscripcionAdministrado.class.getName());

    private CursoProgramacion cursoProgramacion;
    private int modo;
 
    private List<Provincia> listaProvincias;
    private List<Distrito> listaDistritos;
 
    private CursoInscripcion cursoInscripcion;
    private List<CursoInscripcion> listaInscripcion;
    private boolean declaro;
    private boolean declaroCorreo;
    private boolean permiteIngreso;
    private boolean registroExitoso;
    
    private List<Map<String, Object>> listaReq;
    private String msgError;
    
    private BigDecimal nidDepartamento;
    private BigDecimal nidProvincia;
    private BigDecimal nidDistrito;
     
    private BigDecimal nidCurso;
    private BigDecimal nidCursoProgramacion;
  
    private BigDecimal origTipoDoc;
    private String origNroDoc;
    private String nuevoNroDoc;
    
    private BigDecimal busTutor;
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private List<Provincia> listaProvinciasBus;
   
    List<ColumnModel> columnasEval;
    List<Map<String, Object>> listaEvalMap;
    private Boolean verCalificar;
   
    private ExcelOptions excelOpt;
   
    @EJB
    private CursoProgramacionFacadeLocal cursoProgramacionFacade;
    
    @EJB
    private CursoInscripcionFacadeLocal cursoInscripcionFacade;

    @EJB
    private CursoEvaluacionFacadeLocal cursoEvaluacionFacade;

    @EJB
    private PersonaPreFacadeLocal personapreFacade;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    
    @EJB
    private ParametroDnaFacadeLocal parametroFacade;
    
    
   
    @PostConstruct
    public void initLoad() {
        this.modo = Constantes.MODO_LISTADO;     
        this.columnasEval = new ArrayList<>();   
        this.cursoInscripcion = new CursoInscripcion();
        this.cursoInscripcion.setCursoProgramacion(new CursoProgramacion());
        this.verCalificar=false;
        
        AuthAdministrado authAdministrado = (AuthAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{authAdministrado}", AuthAdministrado.class);
      
        if(authAdministrado.getAuthTutor()!=null)
            this.busTutor = authAdministrado.getAuthTutor();
        
   }
    
     /**
     * muestra el título del formulario según el modo
     * @return el título
     */
    public String titulo() {
        switch(this.modo) {
            case Constantes.MODO_NUEVO: return "Nueva Solicitud de Inscripción";
            case Constantes.MODO_UPDATE: return "Actualización de Solicitud de Inscripción";
            case Constantes.CURINS_MODO_EVALUACION: return "Evaluación de Solicitud de Inscripción";
            case Constantes.CURINS_MODO_CALIFICACION: return "Calificación";
        }
        return "";
    } 
   
    /**
     * cambia para regresar al modo listado
     */
    public void regresar(){
       this.modo = Constantes.MODO_LISTADO;
    }

     
      public Integer escribirFiltro(XSSFWorkbook workbook, XSSFSheet sheet, Integer num, String titulo) {
        
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
       
        if(titulo != null) {
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Contenido:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(titulo);  
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
        }
           
        if(this.busDepartamento!=null) {
            Row row = sheet.createRow(num++);
            Departamento dep = this.departamentoFacade.find(this.busDepartamento);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Departamento:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(dep.getTxtDescripcion());  
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
            
        }
        
        if(this.busProvincia!=null) {
            Row row = sheet.createRow(num++);
            Provincia prov = this.provinciaFacade.find(this.busProvincia);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Provincia:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(prov.getTxtDescripcion());         
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
        }
        
        if(this.busDistrito!=null) {
            Row row = sheet.createRow(num++);
            Distrito dis = this.distritoFacade.find(this.busDistrito);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Distrito:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(dis.getTxtDescripcion());     
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
        }
     
        
        if(this.nidCurso!=null) {
            
            CursoAdministrado cursoAdministrado = (CursoAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{cursoAdministrado}", CursoAdministrado.class);
    
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Curso:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(cursoAdministrado.obtenerNombreCurso(this.nidCurso));
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4)); 
        }
    
        if(this.busTutor!=null) {
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Tutor:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            
            Usuario u = usuarioAdministrado.getEntidad(this.busTutor.toPlainString());
           // cell.setCellValue(u.getPersona().nombreApellidos());
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
        }
    
        if(this.nidCursoProgramacion!=null) {
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Programación:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            
            CursoProgramacion tmp = this.cursoProgramacionFacade.find(this.nidCursoProgramacion);
            cell.setCellValue(tmp.getCiudad() + " - " + formatter.format(tmp.getFechaIni()) + " - " + formatter.format(tmp.getFechaFin()) );
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
        }
        return num;
    }

    public void postProcessXLSSolicitudes(Object document) {
        this.postProcessXLS(document, "SOLICITUDES DE INSCRIPCIÓN");
    }
    
    public void postProcessXLSInscripciones(Object document) {
        this.postProcessXLS(document, "INSCRIPCIONES");
    }
      
    public void postProcessXLSCalificaciones(Object document) {
        this.postProcessXLS(document, "CALIFICACIONES");
    }  
    
    public void postProcessXLS(Object document, String titulo) {
        XSSFWorkbook workbook = (XSSFWorkbook) document;
        XSSFSheet sheet = workbook.getSheetAt(0);
        workbook.setSheetName(0, "LISTADO");
        sheet.setColumnWidth(0, 5000); 
       
        sheet.shiftRows(0, sheet.getLastRowNum()+1, 6, true,true);
        int numero = this.escribirFiltro(workbook, sheet, 0,titulo);
       
        sheet.shiftRows(6, sheet.getLastRowNum()+1, numero-6, true,true);
       
               
        XSSFRow header = sheet.getRow(numero);
        
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontHeightInPoints((short) 8);
        fontHeader.setBold(true);
        
    
        XSSFCellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFont(fontHeader);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setWrapText(true);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);

        XSSFFont fontCell = workbook.createFont();
        fontCell.setFontHeightInPoints((short) 10);
       
        XSSFCellStyle styleCell = workbook.createCellStyle();
        styleCell.setFont(fontCell);
        styleCell.setWrapText(true);
        styleCell.setVerticalAlignment(VerticalAlignment.CENTER);
       
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellStyle(styleCell);
            }
        }
         
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            XSSFCell cell = header.getCell(i);
            cell.setCellStyle(styleHeader);
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");  
        Row row = sheet.createRow(sheet.getLastRowNum()+3);
        Cell cell = row.createCell(0);
        cell.setCellValue("Fuente: Base de datos de la DSLD - DGNNA - MIMP");
        row = sheet.createRow(sheet.getLastRowNum()+1);
        cell = row.createCell(0);
        cell.setCellValue("Fecha de reporte: " + dateFormat.format(new Date()));
      
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
     * permite cargar la programación y cambia al modo nuevo
     */
    public void cargarProgramacion() {
        this.cursoProgramacion = this.cursoProgramacionFacade.find(this.cursoInscripcion.getCursoProgramacion().getNidCursoProg());
        this.cursoInscripcion.setPersona(new PersonaPre());
        this.cursoInscripcion.getPersona().setFuncion(new Catalogo());
        this.cursoInscripcion.setListaInscripcionPrereq(new ArrayList<CursoInscripcionPrereq>());
        this.cursoInscripcion.setListaInscripcionReq(new ArrayList<CursoInscripcionReq>());
        this.declaro = false;
        this.declaroCorreo = false;
        this.modo = Constantes.MODO_NUEVO;
        this.nuevoDetalle();
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
     * devuelve la lista de provincias según departamento.
     * Usado en el formualario de inscripción
     */  
    public void obtenerProvinciasIns() {
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.cursoInscripcion.getPersona().getNidDepartamento());
        this.cursoInscripcion.getPersona().setNidProvincia(null);
        this.cursoInscripcion.getPersona().setNidDistrito(null);
        this.listaProvincias = this.provinciaFacade.obtenerProvincias(departamento);
        
    }
    
    /**
     * devuelve la lista de distritos según provincia
     * Usado en el formualario de inscripción
     */
    public void obtenerDistritosIns() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.cursoInscripcion.getPersona().getNidProvincia());
        this.cursoInscripcion.getPersona().setNidDistrito(null);
        this.listaDistritos = this.distritoFacade.obtenerDistritos(provincia);
    }
    
     /**
     * devuelve la lista de provincias según departamento.
     * Usado en el filtro de cursos publicados
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
     * Usado en el filtro de cursos publicados
     */
    public void obtenerDistritosFiltro() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.nidProvincia);
        this.nidDistrito = null;
        this.listaDistritos = this.distritoFacade.obtenerDistritos(provincia);
    }
    
    
      /**
     * devuelve la lista de provincias según departamento.
     * Usado para el filtro del listado de inscripciones
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
     * devuelve el nombre del distrito de la programación del curso
     * @return String
     */
    public String nombreCiudad() {
        return (this.distritoFacade.find(this.cursoProgramacion.getNidDistrito())).getTxtDescripcion();
    }

    /**
     * devuelve el nombre del distrito de la programación del curso según el id del distrito
     * @param nidDisritoParam - id del distrito
     * @return String
     */
    public String nombreCiudadParam(BigDecimal nidDisritoParam) {
        return (this.distritoFacade.find(nidDisritoParam)).getTxtDescripcion();
    }
    
    /**
     * setea a null la instancia de la programación del curso
     */
    public void resetProg() {
        this.cursoProgramacion = null;
        this.nidCursoProgramacion = null;
        
        this.obtenerTodos();
    }
    
    /**
     * devuelve la lista de solicitudes de inscripción al curso
    */
    public void obtenerTodos() {

        if(this.nidCursoProgramacion!=null) {
            
            CursoProgramacion ca = new CursoProgramacion();
            ca.setNidCursoProg(this.nidCursoProgramacion);
            
            ParamFiltro param = new ParamFiltro();
            param.adicionar("cursoProgramacion", ca);
         
            this.listaInscripcion = this.cursoInscripcionFacade.obtenerPorFiltro(param, true, "nidInscripcion", true);
            
        } else
            this.listaInscripcion = null;
        
        
    }
    
    public void obtenerListado(){
        this.obtenerInscritos();
        this.regresar();
    }
     /**
     * genera la lista de inscritos al curso
     */
    public void obtenerInscritos() {
        
        this.listaEvalMap = null;
        
        if(this.nidCursoProgramacion!=null) { 
            
            
            
            CursoProgramacion ca = this.cursoProgramacionFacade.find(this.nidCursoProgramacion);
            this.verCalificar = (new Date()).after(ca.getFechaIni());
            ParamFiltro param = new ParamFiltro();
            param.adicionar("cursoProgramacion", ca);
            param.adicionar("estado", Constantes.CURINS_ESTADO_INSCRITO);
         
            this.listaInscripcion = this.cursoInscripcionFacade.obtenerPorFiltro(param, true, "nidInscripcion", false);
            
        }  else
            this.listaInscripcion = null;
        
        
    }
    
     /**
      * carga la información de la inscripción del participante para edición
      * @param item - objeto CursoInscripcion enviado por el datatable
      */
    public void obtener(CursoInscripcion item) {
       this.cursoInscripcion = item;
       this.declaro = true;
       this.declaroCorreo = true;
       this.permiteIngreso = true;
       this.origTipoDoc = item.getPersona().getNidTipoDoc();
       this.origNroDoc = item.getPersona().getNroDoc();
       
       BigDecimal prov = item.getPersona().getNidProvincia();
       BigDecimal dist = item.getPersona().getNidDistrito();
       
       this.obtenerProvinciasIns();
       this.cursoInscripcion.getPersona().setNidProvincia(prov);
       this.obtenerDistritosIns();
       this.cursoInscripcion.getPersona().setNidDistrito(dist);
      
       if(this.cursoInscripcion.getPersona().getDefensoria()==null)
           this.cursoInscripcion.getPersona().setDefensoria(new Defensoria());
       
       this.subirRequisitos();
      
       this.modo = Constantes.MODO_UPDATE;
    }
   
       /**
      * carga la información de la inscripción del participante para evaluación
      * @param item - objeto CursoInscripcion enviado por el datatable
      */
    public void obtenerEval(CursoInscripcion item) {
       this.cursoInscripcion = item;
       this.modo = Constantes.CURINS_MODO_EVALUACION;
       
       BigDecimal prov = item.getPersona().getNidProvincia();
       BigDecimal dist = item.getPersona().getNidDistrito();
       
       this.obtenerProvinciasIns();
       this.cursoInscripcion.getPersona().setNidProvincia(prov);
       this.obtenerDistritosIns();
       this.cursoInscripcion.getPersona().setNidDistrito(dist);
      
       this.subirRequisitos();
       
       if(item.getEstado() == Constantes.CURINS_ESTADO_PREINSCRITO) {
           this.verificarInpeJudiciales(item.getPersona());
           this.verificarPJPenales(item.getPersona());
           this.verificarPnp(item.getPersona().getNroDoc());
       }
       
    }
   
     /**
     * Verificar antecedentes jucionales en el Inpe de la persona que solicita la inscripción
     * @param personal - PersonaPre
     */
    public void verificarInpeJudiciales(PersonaPre personal) {
     
        try { 
            
            AntecedenteJudicial antecedenteJudicial = new AntecedenteJudicial();
            String apepat = personal.getApePaterno();
            String apemat = personal.getApeMaterno();
            String nombres = personal.getNombres();
            
            antecedenteJudicial.obtenerAntJudicial(apepat, apemat, nombres);
            this.cursoInscripcion.setAntInpe(antecedenteJudicial.getRESPUESTA());
            
        } catch (Exception ex) {
//             // LOGGER.error("Error en antecedentes INPE", ex);
     
        }
    }
    
    /**
     * Verificar los antecedentes penales de la persona que solicita inscripción
     * @param personal - PersonaPre
      */
    public void verificarPJPenales(PersonaPre personal){
        
        try { 
        
            String xApellidoPaterno = personal.getApePaterno();
            String xApellidoMaterno = personal.getApeMaterno();
            List<String> listaNombres = this.separarNombres(personal.getNombres());
            
            String xNombre1 = listaNombres.get(0);
            String xNombre2 = (listaNombres.size()>1)?listaNombres.get(1):"";
            String xNombre3 = (listaNombres.size()>2)?listaNombres.get(2):"";
            
            String xDni = personal.getNroDoc();
            
            AntecedentePenal antecedentePenal = new AntecedentePenal();
            antecedentePenal.obtenerAntPenal(xApellidoPaterno, xApellidoMaterno, xNombre1, xNombre2, xNombre3, xDni);
            this.cursoInscripcion.setAntPj(antecedentePenal.getRESPUESTA());
           
        } catch (Exception ex) {
  //          // LOGGER.error("Error en antecedentes penales", ex);
        }

    }
    
    /**
     * Verificar los antecdentes policiales de la persona que solicita la inscripción
     * @param dni - número de dni de la persona
     */
    public void verificarPnp(String dni){
        
         try {
            AntecedentePolicial antecedentePolicial = new AntecedentePolicial();
            antecedentePolicial.obtenerAntPolicial(dni);
            this.cursoInscripcion.setAntPnp(antecedentePolicial.getRESPUESTA());
            
        } catch (Exception ex) {
 //           // LOGGER.error("Error en antecedentes policiales", ex);
        }
    }
    
     /**
     * Separar el nombre obtenido por reniec
     * @param nombresFull - String - Nombres y Apellidos
     * @return lista de nombres
     */
    public  List<String>  separarNombres(String nombresFull) {
    
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
        
      return listaNombres;

    }
    
    /**
     * inicializa la solicitud de inscripción externa
     * @param cursoProgramacionItem - programación del curso donde se realizará la inscripción
     */
    public void nuevo(CursoProgramacion cursoProgramacionItem) {
        this.cursoProgramacion = cursoProgramacionItem;
        this.cursoInscripcion = new CursoInscripcion();
        this.cursoInscripcion.setCursoProgramacion(cursoProgramacionItem);
        PersonaPre per = new PersonaPre();
        per.setNidTipoDoc(BigDecimal.ONE);
        this.cursoInscripcion.setPersona(per);
        this.cursoInscripcion.getPersona().setFuncion(new Catalogo());
        this.cursoInscripcion.getPersona().setDefensoria(new Defensoria());
        this.cursoInscripcion.setListaInscripcionPrereq(new ArrayList<CursoInscripcionPrereq>());
        this.cursoInscripcion.setListaInscripcionReq(new ArrayList<CursoInscripcionReq>());
        this.cursoInscripcion.setFlgExterno((byte)1);
        this.declaro = false;
        this.declaroCorreo = false;
        this.permiteIngreso = false;
        this.registroExitoso = false;
        this.nuevoNroDoc = "";
        this.modo = Constantes.MODO_NUEVO;
        this.nuevoDetalle();
    }
    
    /**
     * inicializa la solicitud de inscripción interna
     */
    public void nuevo() {
        this.cursoInscripcion = new CursoInscripcion();
        this.cursoProgramacion = this.cursoProgramacionFacade.find(this.nidCursoProgramacion);
        this.cursoInscripcion.setCursoProgramacion(this.cursoProgramacion);
        PersonaPre per = new PersonaPre();
        per.setNidTipoDoc(BigDecimal.ONE);
        this.cursoInscripcion.setPersona(per);
        this.cursoInscripcion.getPersona().setFuncion(new Catalogo());
        this.cursoInscripcion.getPersona().setDefensoria(new Defensoria());
        this.cursoInscripcion.setListaInscripcionPrereq(new ArrayList<CursoInscripcionPrereq>());
        this.cursoInscripcion.setListaInscripcionReq(new ArrayList<CursoInscripcionReq>());
        this.cursoInscripcion.setFlgExterno((byte)0);
        this.declaro = false;
        this.declaroCorreo = false;
        this.permiteIngreso = false;
        this.registroExitoso = false;
        this.nuevoNroDoc = "";
        this.modo = Constantes.MODO_NUEVO;
        this.nuevoDetalle();
    }
    
    /**
     * inicializa la lista de los requisitos y prerequisitos
     */
    public void nuevoDetalle() {
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
        
        try {
    
            for(CursoReq cr: this.cursoProgramacion.getCurso().getListaReq()) {
                if(cr.getFlgActivo().equals(BigInteger.ONE)) {
                    CursoInscripcionReq cir = new CursoInscripcionReq();
                    cir.setCursoInscripcion(this.cursoInscripcion);
                    cir.setCursoReq(cr);
                    cir.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    cir.setTxtPc(Internet.obtenerNombrePC());
                    cir.setTxtIp(Internet.obtenerIPPC());
                    cir.setFecRegistro(new Date());
                    cir.setFlgActivo(BigInteger.ONE);
                    this.cursoInscripcion.getListaInscripcionReq().add(cir);
                }
            }
            
            for(CursoPrereq cr: this.cursoProgramacion.getCurso().getListaPrereq()) {
                if(cr.getFlgActivo().equals(BigInteger.ONE)) {
                    CursoInscripcionPrereq cir = new CursoInscripcionPrereq();
                    cir.setCursoInscripcion(this.cursoInscripcion);
                    cir.setCursoPrereq(cr);
                    cir.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                    cir.setTxtPc(Internet.obtenerNombrePC());
                    cir.setTxtIp(Internet.obtenerIPPC());
                    cir.setFecRegistro(new Date());
                    cir.setFlgActivo(BigInteger.ONE);
                    this.cursoInscripcion.getListaInscripcionPrereq().add(cir);
                }

            }
            
            this.subirRequisitos();
            
        } catch (Exception e) {
  //      // LOGGER.error("Error al generar los requisitos y prerrequisitos", e);
        }

    }
    
    /**
     * setea la lista de requisitos ( consolidado de prerequisitos y requisitos ) para mostrar en el detalle del curso en el portal
     */
    public void subirRequisitos() {
        
        this.listaReq = new ArrayList<>();
        
        for(CursoInscripcionReq cr: this.cursoInscripcion.getListaInscripcionReq()) {
            if(cr.getFlgActivo().equals(BigInteger.ONE)) {
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("nombre", cr.getCursoReq().getCatalogo().getTxtNombre());
                mapa.put("tipo","R");
                mapa.put("objeto",cr);
                mapa.put("load",cr.getArchivo()!=null);
                this.listaReq.add(mapa);
            }
        }
        for(CursoInscripcionPrereq cr: this.cursoInscripcion.getListaInscripcionPrereq()) {
            if(cr.getFlgActivo().equals(BigInteger.ONE)) {
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("nombre", "Haber aprobado el curso \"" + cr.getCursoPrereq().getCursoPrereq().getNombre() + "\"");
                mapa.put("tipo","C");
                mapa.put("objeto",cr);
                mapa.put("load",cr.getArchivo()!=null);
                this.listaReq.add(mapa);
            }
                
        }
    }
    
    /**
     * Guarda la información de las personas que solicitan la inscripción
     * @throws Exception lanza excepción de la clase Internet
     */
    private void savePersona() throws Exception  {
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
           
        if(this.cursoInscripcion.getPersona().getNidPersona()==null) {
            PersonaPre pre = this.cursoInscripcion.getPersona();
            pre.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            pre.setTxtPc(Internet.obtenerNombrePC());
            pre.setTxtIp(Internet.obtenerIPPC());
            pre.setFecRegistro(new Date());
            pre.setFlgActivo(BigInteger.ONE);
            this.personapreFacade.create(pre);
        } else {
            PersonaPre pre = this.cursoInscripcion.getPersona();
            pre.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            pre.setTxtPc(Internet.obtenerNombrePC());
            pre.setTxtIp(Internet.obtenerIPPC());
            pre.setFecModificacion(new Date());
            this.personapreFacade.edit(pre);
        }
    }
    /**
     * guarda el registro de la solicitud de inscripción y la información de la persona inscrita
     */
    public void save() {
        
        if(this.modo == 2)
            this.update();
        else
        try {
    
                UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
                
                this.savePersona();
                this.cursoInscripcion.setCursoProgramacion(this.cursoProgramacion);
                this.cursoInscripcion.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
                this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
                this.cursoInscripcion.setFecRegistro(new Date());
                this.cursoInscripcion.setFlgActivo(BigInteger.ONE);
                this.cursoInscripcion.setEstado(Constantes.CURINS_ESTADO_PREINSCRITO);
                this.cursoInscripcionFacade.create(this.cursoInscripcion);
                this.modo = Constantes.MODO_LISTADO;
                this.registroExitoso = true;
                this.obtenerTodos();
                if(usuarioAdministrado.getEntidadSeleccionada()!=null && usuarioAdministrado.getEntidadSeleccionada().getNidUsuario()!=null)
                    adicionarMensaje("","La solicitud de inscripción al curso ha sido registrada con éxito.");
    
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al crear al registrar la solicitud de inscripción. Por favor, intente nuevamente.");
            // LOGGER.error("Error al guardar la solicitud de inscripción", e);
        }
    }
    
    /**
     * actualiza la información de la solicitud de inscripción
     */
    public void update() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            
            this.savePersona();            
            this.cursoInscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
            this.cursoInscripcion.setFecModificacion(new Date());
            
            this.cursoInscripcionFacade.edit(this.cursoInscripcion);
            this.modo = Constantes.MODO_LISTADO;
            this.origTipoDoc = null;
            this.origNroDoc = null;
            adicionarMensaje("","La solicitud de inscripción al curso ha sido actualizada con éxito.");
                 
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al actualizar la solicitud de inscripción al curso. Por favor, intente nuevamente.");
             // LOGGER.error("Error al actualizar la solicitud de inscripción", e);
        }
    }
    
    /**
     * actualiza el estado de la solicitud de inscripción: activo o inactivo
     */
    public void anular() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoInscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
            this.cursoInscripcion.setFecModificacion(new Date());
            this.cursoInscripcionFacade.edit(this.cursoInscripcion);
             this.modo = Constantes.MODO_LISTADO;
            adicionarMensaje("","La solicitud de inscripción ha cambiado su estado a " + ((this.cursoInscripcion.getFlgActivo().compareTo(BigInteger.ONE)==0)?"Activo":"Inactivo") + " con éxito.");
           
            
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al actualizar la solicitud de inscripción al curso. Por favor, intente nuevamente.");
             // LOGGER.error("Error al anular la solicitud de inscripción", e);
        }
    }
    
   
     /**
     * Aprueba la solicitud de inscripción. Cambia el registro a estado INSCRITO.
     */
    public void aprobar() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoInscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
            this.cursoInscripcion.setFecModificacion(new Date());
            this.cursoInscripcion.setFecEvaluacion(new Date());
            this.cursoInscripcion.setEstado(Constantes.CURINS_ESTADO_INSCRITO);
            this.cursoInscripcionFacade.edit(this.cursoInscripcion);
            this.modo = Constantes.MODO_LISTADO;
            adicionarMensaje("","La solicitud de inscripción al curso ha sido aprobada con éxito.");
                 
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al aprobar la solicitud de inscripción al curso. Por favor, intente nuevamente.");
            // LOGGER.error("Error al aprobar la solicitud de inscripción", e);
        }
        
        this.enviarCorreoAprueba(Constantes.TPL_CURSO_INS_OK);
    }
    
     /**
     * Observa la solicitud de inscripción. Cambia el registro a estado OBSERVADO.
     */
    public void observar() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoInscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
            this.cursoInscripcion.setFecModificacion(new Date());
            this.cursoInscripcion.setFecEvaluacion(new Date());
            this.cursoInscripcion.setFecObservacion(new Date());
            this.cursoInscripcion.setEstado(Constantes.CURINS_ESTADO_OBSERVADO);
            this.cursoInscripcionFacade.edit(this.cursoInscripcion);
            this.modo = Constantes.MODO_LISTADO;
            adicionarMensaje("","La solicitud de inscripción al curso ha sido observada con éxito.");
                 
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al observar la solicitud de inscripción al curso. Por favor, intente nuevamente.");
            // LOGGER.error("Error al observar la solicitud de inscripción", e);
        }
        
        this.enviarCorreoObs(Constantes.TPL_CURSO_INS_OBS);
    }
    
     /**
     * Rechaza la solicitud de inscripción. Cambia el registro a estado RECHAZADO.
     */
    public void rechazar() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.cursoInscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
            this.cursoInscripcion.setFecEvaluacion(new Date());
            this.cursoInscripcion.setFecModificacion(new Date());
            this.cursoInscripcion.setEstado(Constantes.CURINS_ESTADO_RECHAZADO);
            this.cursoInscripcionFacade.edit(this.cursoInscripcion);
            this.modo = Constantes.MODO_LISTADO;
            adicionarMensaje("","La solicitud de inscripción al curso ha sido rechazada con éxito.");
                 
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al rechazar la solicitud de inscripción al curso. Por favor, intente nuevamente.");
            // LOGGER.error("Error al rechazar la solicitud de inscripción", e);
        }
        
         this.enviarCorreoRec(Constantes.TPL_CURSO_INS_REC);
    }
    

    /**
     * permite setear el contenido del archivo en bytes en el campo archivo del prerequisito o requisito
     * @param event - FileUploadEvent - evento generado al subir los archivos de requisitos
     */
     public void uploadRequisito(FileUploadEvent event) {
        
        try {
         
            Integer index = (Integer)event.getComponent().getAttributes().get("indexReqAr");
            Map<String, Object> mapa = this.listaReq.get(index);
            this.listaReq.get(index).replace("load",true);
            
            if(String.valueOf(mapa.get("tipo")).equals("R")) {
              ((CursoInscripcionReq)mapa.get("objeto")).setArchivo(IOUtils.toByteArray(event.getFile().getInputstream()));
              ((CursoInscripcionReq)mapa.get("objeto")).setArchivoNombre(event.getFile().getFileName());
            } else {
              ((CursoInscripcionPrereq)mapa.get("objeto")).setArchivo(IOUtils.toByteArray(event.getFile().getInputstream()));
              ((CursoInscripcionPrereq)mapa.get("objeto")).setArchivoNombre(event.getFile().getFileName());
            }
            
            adicionarMensaje("","Carga de Archivo: "+ event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
             // LOGGER.error("Error al subir el archivo", ex);
        }
    }
  
    /**
     * devuelve el stream del contenido en bytes del campo archivo del prequisito o requisito
     * @param index - int - número del requisito
     * @return StreamedContent
     */
    public StreamedContent descargarRequisito(int index) {
        
        InputStream ips;
        
        try {
         
            Map<String, Object> mapa = this.listaReq.get(index);
           
            if(String.valueOf(mapa.get("tipo")).equals("R")) {
               ips = new ByteArrayInputStream(((CursoInscripcionReq)mapa.get("objeto")).getArchivo());
               return new DefaultStreamedContent(ips, "application/pdf",((CursoInscripcionReq)mapa.get("objeto")).getArchivoNombre());
            } else {
               ips = new ByteArrayInputStream(((CursoInscripcionPrereq)mapa.get("objeto")).getArchivo());
               return new DefaultStreamedContent(ips, "application/pdf",((CursoInscripcionPrereq)mapa.get("objeto")).getArchivoNombre());
            }
             
            
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo", ex);
        }
        return null;
    }
    
    /**
     * Devuelve la lista de nombres de los prerequisitos y requisitos 
     * @return lista de los nombres de los prerequisitos y requisitos 
     */
    public List<String> obtenerRequisitos() {
        
        List<String> listaReque = new ArrayList<>();
        
        for(CursoReq cr: this.cursoProgramacion.getCurso().getListaReq()) {
            if(cr.getFlgActivo().equals(BigInteger.ONE))
               listaReque.add(cr.getCatalogo().getTxtNombre());
        }
        for(CursoPrereq cr: this.cursoProgramacion.getCurso().getListaPrereq()) {
            if(cr.getFlgActivo().equals(BigInteger.ONE))
                listaReque.add("Haber aprobado el curso \"" + cr.getCursoPrereq().getNombre() + "\"");
        }
        return listaReque;
    }
    
    /**
     * devuelve el stream del documento del curso según el index
     * @param index - int - número del documento del curso
     * @return StreamedContent
     */
    public StreamedContent descargar(int index) {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoProgramacion.getCurso().getListaDocum().get(index).getArchivo());
             return new DefaultStreamedContent(ips, "application/pdf",this.cursoProgramacion.getCurso().getListaDocum().get(index).getArchivoNombre());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo", ex);
        }
        return null;
    }
    
     /**
     * permite setear el contenido del archivo en bytes del archivo de observaciones de la evaluación de la solicitud de inscripción
     * @param event - FileUploadEvent - evento generado al subir el archivo de la observación
     */
     public void uploadObs(FileUploadEvent event) {
        
        try {
         
            this.cursoInscripcion.setArchivoObs(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.cursoInscripcion.setArchivoNombreObservacion(event.getFile().getFileName());
            adicionarMensaje("","Carga de Archivo:"+event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir el archivo", ex);
        }
    }
     
    /**
     * permite setear el contenido del archivo en bytes del archivo de rechazo de la evaluación de la solicitud de inscripción
     * @param event - FileUploadEvent - evento generado al subir el archivo del rechazo
     */
     public void uploadRec(FileUploadEvent event) {
        
        try {
         
            this.cursoInscripcion.setArchivoRechazo(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.cursoInscripcion.setArchivoNombreRechazo(event.getFile().getFileName());
            adicionarMensaje("","Carga de Archivo:"+event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir el archivo", ex);
        }
    }
     
     
     /**
     * permite setear el contenido del archivo en bytes del archivo del oficio de la aprobación de la solicitud de inscripción
     * @param event - FileUploadEvent - evento generado al subir el archivo del oficio
     */
     public void uploadOficio(FileUploadEvent event) {
        
        try {
         
            this.cursoInscripcion.setArchivoOficio(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.cursoInscripcion.setArchivoNombreOficio(event.getFile().getFileName());
            adicionarMensaje("","Carga de Archivo: "+event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir el archivo", ex);
        }
    }
  
  
    /**
     * devuelve el stream del contenido en bytes del archivo de observaciones de la evaluación de la solicitud de inscripción
     * @return StreamedContent
     */
    public StreamedContent descargarObs() {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoInscripcion.getArchivoObs());
             return new DefaultStreamedContent(ips, "application/pdf",this.cursoInscripcion.getArchivoNombreObservacion());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo", ex);
        }
        return null;
    }
    
     /**
     * devuelve el stream del contenido en bytes del archivo de rechazo de la evaluación de la solicitud de inscripción
     * @return StreamedContent
     */
    public StreamedContent descargarRechazo() {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoInscripcion.getArchivoRechazo());
             return new DefaultStreamedContent(ips, "application/pdf",this.cursoInscripcion.getArchivoNombreRechazo());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo", ex);
        }
        return null;
    }
    
      /**
     * devuelve el stream del contenido en bytes del archivo del oficio de la aprobación de la solicitud de inscripción
     * @return StreamedContent
     */
    public StreamedContent descargarOficio() {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoInscripcion.getArchivoOficio());
             return new DefaultStreamedContent(ips, "application/pdf",this.cursoInscripcion.getArchivoNombreOficio());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo", ex);
        }
        return null;
    }
    
     /**
     * elimina el contenido del archivo de observaciones de la evaluación de la solicitud de inscripción
     */
    public void eliminarObs() {
        this.cursoInscripcion.setArchivoObs(null);
        this.cursoInscripcion.setArchivoNombreObservacion(null);
    }
    
    /**
     * elimina el contenido del archivo de rechazo de la evaluación de la solicitud de inscripción
     */
    public void eliminarRechazo() {
        this.cursoInscripcion.setArchivoRechazo(null);
        this.cursoInscripcion.setArchivoNombreRechazo(null);
    }
    
      /**
     * elimina el contenido del archivo del oficio de aprobación de la solicitud
     */
    public void eliminarOficio() {
        this.cursoInscripcion.setArchivoOficio(null);
        this.cursoInscripcion.setArchivoNombreOficio(null);
    }
    
    /**
     * valida el tipo y número de documento si existe ya registrado en el curso.
     * Si existe en otro curso setea la información de la persona.
     * @param ctx - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
     */
    public void validarNroDoc(FacesContext ctx, UIComponent component, Object value) throws ValidatorException	{
        
           String nroDoc = value.toString();
         
           if((this.modo == Constantes.MODO_NUEVO && !nroDoc.equals(this.nuevoNroDoc)) || ( this.modo == Constantes.MODO_UPDATE && !nroDoc.equals(this.origNroDoc))) {
               
               if(this.cursoInscripcion.getPersona().getNidTipoDoc()!=null &&
                       this.cursoInscripcion.getPersona().getNidTipoDoc().compareTo(BigDecimal.ONE)==0) {
                   
                   try {
                       IdentificacionReniec idenReniec = new IdentificacionReniec();
                       idenReniec.obtenerConsultaReniec(nroDoc);
                       if(!idenReniec.getCODIGO().equals("0000"))	
                           throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "El DNI ingresado no existe",""));
                       
                   } catch (MalformedURLException ex) {
                       // LOGGER.error("Error en validación de DNI", ex);
                   }
                    
               }
               this.nuevoNroDoc = nroDoc;
                ParamFiltro paramPer = new ParamFiltro();
                paramPer.adicionar("nidTipoDoc", this.cursoInscripcion.getPersona().getNidTipoDoc());
                paramPer.adicionar("nroDoc", nroDoc);

                List<PersonaPre> lper = this.personapreFacade.obtenerPorFiltro(paramPer, true, "nroDoc", false);

                if(!lper.isEmpty()) {

                     this.cursoInscripcion.setPersona(lper.get(0));

                     ParamFiltro param = new ParamFiltro();
                     param.adicionar("persona", this.cursoInscripcion.getPersona());
                     param.adicionar("cursoProgramacion", this.cursoInscripcion.getCursoProgramacion());

                     List<CursoInscripcion> ci = this.cursoInscripcionFacade.obtenerPorFiltro(param, true, "nidInscripcion", false);

                     if(!ci.isEmpty()) {
                          this.permiteIngreso = false;
                          throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ud. ya ha enviado su solicitud de inscripción al curso.",""));	
                     } else {
                          this.cursoInscripcion.setPersona(lper.get(0));
                         // this.cursoInscripcion.getPersona().setFuncion(new Catalogo());
                          this.permiteIngreso = true;
                     }

                } else {
                    BigDecimal tipoDoc = this.cursoInscripcion.getPersona().getNidTipoDoc();
                    this.cursoInscripcion.setPersona( new PersonaPre());
                    this.cursoInscripcion.getPersona().setNidTipoDoc(tipoDoc);
                    this.cursoInscripcion.getPersona().setFuncion(new Catalogo());
                    this.cursoInscripcion.getPersona().setDefensoria(new Defensoria());
                    this.permiteIngreso = true;
                }
           }
           
       
    }

    /**
     * valida el formulario de la inscripción posterior a la validación de los controles
     * @param event ComponentSystemEvent - evento generado posterior a la validación del formulario
     */
    public void validarInscripcion(final ComponentSystemEvent event) {
        
            FacesContext fc = FacesContext.getCurrentInstance();
     
         
            for(CursoInscripcionReq doc : this.cursoInscripcion.getListaInscripcionReq()) {
            
                if(doc.getArchivo()==null){
                    adicionarMensajeError("Debe adjuntar los archivos de los requisitos solicitados","");
                    fc.validationFailed();
                    fc.renderResponse(); 
                    break;
                }
            }            
            
            if(!this.declaro){
                adicionarMensajeError("Debe declarar bajo juramento no ser deudor alimentario y no tener antecedentes policiales, penales ni judiciales","");
                fc.validationFailed();
                fc.renderResponse(); 
            }
            if(!this.declaroCorreo){
                adicionarMensajeError("Debe autorizar la notificación electrónica a través del correo electrónico de contacto","");
                fc.validationFailed();
                fc.renderResponse(); 
            }
        
    }
    
    /**
      * valida que el oficio de aprobación se haya subido
     * @param event ComponentSystemEvent - evento generado posterior a la validación del formulario
     */
    public void validarAprobacion(final ComponentSystemEvent event) {
        
            FacesContext fc = FacesContext.getCurrentInstance();
     
         
            if(this.cursoInscripcion.getArchivoOficio()==null){
                 adicionarMensajeError("Debe adjuntar el oficio","");
                 fc.validationFailed();
                 fc.renderResponse(); 
            }            
           
     }            
           
    /**
     * obtiene los datos de la persona y setea la información del ubigeo para mostrar en los controles del formulario de inscripción
     */
    public void obtenerDatos() {
        
        if(this.cursoInscripcion.getPersona()!=null) {
            BigDecimal nidProv =  this.cursoInscripcion.getPersona().getNidProvincia();
            BigDecimal nidDist =  this.cursoInscripcion.getPersona().getNidDistrito();
        
            this.obtenerProvinciasIns();
            this.cursoInscripcion.getPersona().setNidProvincia(nidProv);
            this.obtenerDistritosIns();
            this.cursoInscripcion.getPersona().setNidDistrito(nidDist);
            
        }
        
    }
    
    
      
    /**
     * genera la lista de inscritos con las ponderaciones del curso en forma de tabla para evaluación
     */
    public void listaMatriculados() {
      
        this.modo = Constantes.CURINS_MODO_CALIFICACION;
        this.listaEvalMap = new ArrayList<>();
    
        if(this.nidCursoProgramacion!=null) {
     
            this.columnasEval = new ArrayList<>();   
            this.listaEvalMap = new ArrayList<>();   
           
            this.cursoProgramacion = this.cursoProgramacionFacade.find(this.nidCursoProgramacion);
            List<CursoPonderacion> listaPond = this.cursoProgramacion.getCurso().getListaPonderacion();
            
            for(CursoPonderacion cp: listaPond) {
                this.columnasEval.add(new ColumnModel(cp.getCatalogo().getTxtNombre().toUpperCase(), "p"+cp.getNidPonderacion()));
            }
            
            ParamFiltro param = new ParamFiltro();
            param.adicionar("cursoProgramacion", this.cursoProgramacion);
            param.adicionar("estado", Constantes.CURINS_ESTADO_INSCRITO);

            List<CursoInscripcion> listaIns = this.cursoInscripcionFacade.obtenerPorFiltro(param, true, "nidInscripcion", false);

            
            for(CursoInscripcion ci: listaIns) {
                Map<String, Object> mapa = new LinkedHashMap<>();
                mapa.put("inscripcion", ci);
                mapa.put("nombres", ci.getPersona().getNombreApellidos());
                mapa.put("estado", ci.getEstadoEval());
                mapa.put("notaEval", ci.getNotaEval());

                
                for(CursoPonderacion cp: listaPond) {
                    boolean existe = false;
                              
                    for(CursoEvaluacion ce: ci.getListaEvaluacion()) {
                        
                        if(ce.getCursoPonderacion().getNidPonderacion().equals(cp.getNidPonderacion())) {
                             mapa.put("p"+cp.getNidPonderacion(), ce.getNota() );
                             existe = true;
                             break;
                        }
                    }
                    if(!existe)
                        mapa.put("p"+cp.getNidPonderacion(), null );
                }
                listaEvalMap.add(mapa);                
            }
 
        }
        
     }
    
    /**
     * actualiza la nota del participante en el curso y setea el estado del participante según las notas: PENDIENTE, APROBADO y DESAPROBADO
     * @param evalMap - lista de mapas que contiene las nota
     * @param col - columna
     */
    public void updateNota(Map<String, Object> evalMap, String col) {
          
        try {
            
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            BigDecimal nidUsuario = usuarioAdministrado.getEntidadSeleccionada().getNidUsuario();
            BigDecimal nidPonderacion = BigDecimal.valueOf(Long.valueOf(col.substring(1)));
            
            CursoPonderacion cp = new CursoPonderacion();
            cp.setNidPonderacion(nidPonderacion);
            
            CursoInscripcion ci = (CursoInscripcion)evalMap.get("inscripcion");
            
            Integer nota = (evalMap.get(col)==null || evalMap.get(col).equals(""))?null:Integer.valueOf((String)evalMap.get(col));
            
            ParamFiltro param = new ParamFiltro();
            param.adicionar("cursoInscripcion", ci);
            param.adicionar("cursoPonderacion", cp);
            List<CursoEvaluacion> listae = this.cursoEvaluacionFacade.obtenerPorFiltro(param, true, "nidEvaluacion", false);
            
            CursoEvaluacion ce;
            
            if(listae.isEmpty()) {
            
                ce = new CursoEvaluacion();
                ce.setCursoInscripcion(ci);
                ce.setCursoPonderacion(cp);
                ce.setFlgActivo(BigInteger.ONE);
                ce.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                ce.setFecRegistro(new Date());
                ce.setNota(nota);
                ce.setTxtPc(Internet.obtenerNombrePC());
                ce.setTxtIp(Internet.obtenerIPPC());
                this.cursoEvaluacionFacade.create(ce);
                
            } else {
                
                ce = listae.get(0);
                ce.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                ce.setFecModificacion(new Date());
                ce.setNota(nota);
                ce.setTxtPc(Internet.obtenerNombrePC());
                ce.setTxtIp(Internet.obtenerIPPC());
                this.cursoEvaluacionFacade.edit(ce);
                
            }
           
            ci = this.cursoInscripcionFacade.find(ci.getNidInscripcion());
            evalMap.put("estado", ci.getEstadoEval());
            evalMap.put("notaEval", ci.getNotaEval());
            
        } catch(Exception ex) {     
              adicionarMensajeError("Error", "Error al tratar de actualizar la nota");
              // LOGGER.error("Error al actualizar la nota", ex);
        }    
    }
    
    private double redondear(double param) {
        return Double.valueOf(String.valueOf(Math.round(param)))/100;
    }
    /**
     * genera el certificado de capacitación
     * @param item - CursoInscripcion - inscripción del curso
     * @return CursoCertificadoReport
     */
    private CursoCertificadoReport certificadoDs(CursoInscripcion item){
        
        
        CursoCertificadoReport salida = new CursoCertificadoReport();
        salida.setNombre(item.getPersona().getNombreApellidos());
        
        CursoProgramacion cprog = item.getCursoProgramacion();
        salida.setCurso(cprog.getCurso().getNombre());
        Distrito distrito = distritoFacade.find(item.getCursoProgramacion().getNidDistrito());
        salida.setCiudad(DnaUtil.capitalizar(distrito.getTxtDescripcion().toLowerCase()));
        salida.setFecha(DnaUtil.rangoFechasConNombre(item.getCursoProgramacion().getFechaIni(),item.getCursoProgramacion().getFechaFin()));
        
        NumeroLetras num = new NumeroLetras();
        salida.setHoras(num.convertir(String.valueOf(item.getCursoProgramacion().getCurso().getNroHoras()), false) + "(" +String.valueOf(item.getCursoProgramacion().getCurso().getNroHoras()) + ")");
        
        
        if(cprog.getRdFecha()!=null)
            salida.setFechaCert(DnaUtil.fechaConNombreMes(cprog.getRdFecha()));
        else
            salida.setFechaCert("                           ");
        
        ParametroDna param = this.parametroFacade.find(Constantes.PARAM_NOMBRE_DIR);
        salida.setNombreDir(param.getTxtValor());
        salida.setSumario(item.getCursoProgramacion().getCurso().getSumario());
        
        salida.setNroRd(cprog.getRdNro());
        
        UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
//        salida.setNombreTutor(usuarioAdministrado.getUsuarioPorId(cprog.getNidTutor()).getPersona().nombreApellidos());
        salida.setNotaFinal(item.getNotaEval().toPlainString());
        
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.00", otherSymbols);
        
        for(CursoEvaluacion ev: item.getListaEvaluacion()) {
            CursoPonderacion cp = ev.getCursoPonderacion();
            CertificadoItemReport cir = new CertificadoItemReport();
            cir.setItem(cp.getCatalogo().getTxtNombre());
            cir.setPorcentaje(cp.getPonderacion().toPlainString() + "%");
            Double bd = this.redondear(cp.getPonderacion().doubleValue()*(Double.valueOf(String.valueOf(ev.getNota())))); 
            cir.setPuntaje(df.format(bd));
            
            salida.getListaItems().add(cir);
        }
        
        return salida;
    }
    
    /**
     * Imprimir el PDF de la constancia
     * @param item - CursoInscripcion - inscripción del curso
     * @return StreamedContent
     */
    public StreamedContent downloadCertificado(CursoInscripcion item){
        
        List<CursoCertificadoReport> datasource;
        Map<String,Object> params;
        datasource = new ArrayList<>();
        params = new HashMap<>();
        datasource.add(this.certificadoDs(item));
       
        try {
             InputStream ips = new ByteArrayInputStream(PdfUtil.downloadPDF(params, ConstantesReporte.CURSO_CERTIFICADO_PATH, datasource));
             return new DefaultStreamedContent(ips, "application/pdf", "certificado.pdf");//, "image/jpg"
                
        } catch (Exception ex) {
            // LOGGER.error("Error al descargar el certificado", ex);
        }
         return null;
    }
    
     /**
     * Permite subir el certificado del curso firmado
     * @param event - FileUploadEvent - evento generado al subir el certificado
     */
     public void uploadCertificado(FileUploadEvent event) {
        
        try {
         
            this.cursoInscripcion.setArchivoCert(IOUtils.toByteArray(event.getFile().getInputstream()));
            adicionarMensaje("","Carga de Archivo: " + event.getFile().getFileName() + " terminado");
            
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir el certificado", ex);
        }
    }
    
    /**
     * devuelve el stream del contenido en bytes del certificado del curso firmado
     * @return StreamedContent
     */
    public StreamedContent descargarCertificado() {
        
        InputStream ips;
        
        try {
         
             ips = new ByteArrayInputStream(this.cursoInscripcion.getArchivoCert());
             return new DefaultStreamedContent(ips, "application/pdf","cert_"+ this.cursoInscripcion.getPersona().getNombreApellidos().replace(" ", "") + ".pdf");
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
        }
        return null;
    }
    
     /**
     * elimina el contenido del archivo del certificado del curso
     */
    public void eliminarCertificado() {
        this.cursoInscripcion.setArchivoCert(null);
    }  
     
     
      
    /**
     * devuelve el listado de programaciones activos según el filtro de búsqueda: curso, tutor, departamento, provincia
     * @return lista de programaciones
     */
    public List<CursoProgramacion> obtenerProgramacionesActivas(){
        this.resetProg();
        ParamFiltro param = new ParamFiltro();
        Curso curso = new Curso();
        curso.setNidCurso(this.nidCurso);
        param.adicionar("curso", curso);
        this.modo = Constantes.MODO_LISTADO;
        
        if(this.busTutor !=null)
            param.adicionar("nidTutor", this.busTutor);
      
        if(this.busDepartamento !=null)
            param.adicionar("nidDepartamento", this.busDepartamento);
       
        if(this.busProvincia !=null)
            param.adicionar("nidProvincia", this.busProvincia);
     
        if(this.busDistrito !=null)
            param.adicionar("nidDistrito", this.busDistrito);
     
        return this.cursoProgramacionFacade.obtenerPorFiltro(param, false, "fechaIni", false);

    } 
    
    
    /**
     * envía la notificación de observación de las solicitudes de inscripción
     * @param tpl nombre del template
     */
    public void enviarCorreoObs(String tpl) {
        try {
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", tpl);

            String contenido = parametros.get(0).getTxtValor().replace("{0}", this.cursoInscripcion.getPersona().getNombres()+ " " + 
                                                                                    this.cursoInscripcion.getPersona().getApePaterno() + " " + 
                                                                                        this.cursoInscripcion.getPersona().getApeMaterno() );
            
            contenido = contenido.replace("{1}", this.cursoInscripcion.getCursoProgramacion().getCurso().getNombre())
                                 .replace("{2}", this.cursoInscripcion.getObsObserva());
            
            CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}",CorreoDnaAdministrado.class);
            correoDnaAdministrado.getCorreo().getDestinatarios().add(this.cursoInscripcion.getPersona().getCorreo());
            correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Notificación de Evaluación de Solicitud de Inscripción", "utf-8", "B"));
            correoDnaAdministrado.getCorreo().setContenido(contenido);
            correoDnaAdministrado.getCorreo().setHtml(true);
            
            if(this.cursoInscripcion.getArchivoObs()!=null)
            //    correoDnaAdministrado.getCorreo().addAttach(this.cursoInscripcion.getArchivoObs(), "documento.pdf");
            
            correoDnaAdministrado.getCorreo().enviarCorreo();
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al tratar de enviar la notificación al correo electrónico","");
        }    
    }
    
     /**
     * envía la notificación de rechazo de las solicitudes de inscripción
     * @param tpl nombre del template
     */
    public void enviarCorreoRec(String tpl) {
        try {
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", tpl);

            String contenido = parametros.get(0).getTxtValor().replace("{0}", this.cursoInscripcion.getPersona().getNombres()+ " " + 
                                                                                    this.cursoInscripcion.getPersona().getApePaterno() + " " + 
                                                                                        this.cursoInscripcion.getPersona().getApeMaterno() );
            
            contenido = contenido.replace("{1}", this.cursoInscripcion.getCursoProgramacion().getCurso().getNombre())
                                 .replace("{2}", this.cursoInscripcion.getObsRechazo());
            
            CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}",CorreoDnaAdministrado.class);
            correoDnaAdministrado.getCorreo().getDestinatarios().add(this.cursoInscripcion.getPersona().getCorreo());
            correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Notificación de Evaluación de Solicitud de Inscripción", "utf-8", "B"));
            correoDnaAdministrado.getCorreo().setContenido(contenido);
            correoDnaAdministrado.getCorreo().setHtml(true);
            
            if(this.cursoInscripcion.getArchivoRechazo()!=null)
         //       correoDnaAdministrado.getCorreo().addAttach(this.cursoInscripcion.getArchivoRechazo(), "documento.pdf");
            
            correoDnaAdministrado.getCorreo().enviarCorreo();
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al tratar de enviar la notificación al correo electrónico","");
        }    
    }
    
    
    /**
     * envía la notificación de aprobación de la solicitud de inscripción y adjunta el oficio
     * @param tpl nombre del template
     */
    public void enviarCorreoAprueba(String tpl) {
        try {
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", tpl);

            String contenido = parametros.get(0).getTxtValor().replace("{0}", this.cursoInscripcion.getPersona().getNombres()+ " " + 
                                                                                    this.cursoInscripcion.getPersona().getApePaterno() + " " + 
                                                                                        this.cursoInscripcion.getPersona().getApeMaterno() );
            
            contenido = contenido.replace("{1}", this.cursoInscripcion.getCursoProgramacion().getCurso().getNombre());
            
            CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}",CorreoDnaAdministrado.class);
            correoDnaAdministrado.getCorreo().getDestinatarios().add(this.cursoInscripcion.getPersona().getCorreo());
            correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Notificación de Aprobación de Solicitud de Inscripción", "utf-8", "B"));
            correoDnaAdministrado.getCorreo().setContenido(contenido);
            correoDnaAdministrado.getCorreo().setHtml(true);
            
            if(this.cursoInscripcion.getArchivoOficio()!=null)
//                correoDnaAdministrado.getCorreo().addAttach(this.cursoInscripcion.getArchivoOficio(), "oficio.pdf");
            
            correoDnaAdministrado.getCorreo().enviarCorreo();
            
        } catch(Exception ex) {
            adicionarMensajeError("Error al tratar de enviar la notificación al correo electrónico","");
        }    
    }
    
    /**
     * Guarda el certificado firmado y lo envía al correo del inscrito
     */
    public void updateAndSend() {
        
          try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            
            this.cursoInscripcion.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.cursoInscripcion.setTxtPc(Internet.obtenerNombrePC());
            this.cursoInscripcion.setTxtIp(Internet.obtenerIPPC());
            this.cursoInscripcion.setFecModificacion(new Date());
            
            this.cursoInscripcionFacade.edit(this.cursoInscripcion);
            this.modo = Constantes.MODO_LISTADO;
            this.origTipoDoc = null;
            this.origNroDoc = null;
                 
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al guardar y enviar el certificado.");
             // LOGGER.error("Error al actualizar la solicitud de inscripción", e);
        }
        this.enviarCorreoCertificado();
        adicionarMensaje("","El certificado ha sido guardado y enviado con éxito.");
            
    }
    
    /**
     * envía la notificación de correo electrónico adjuntando el certificado por aprobación del curso
     */
    public void enviarCorreoCertificado() {
        try {
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.TPL_CURSO_CERT);

            String contenido = parametros.get(0).getTxtValor().replace("{0}", this.cursoInscripcion.getPersona().getNombres()+ " " + 
                                                                                    this.cursoInscripcion.getPersona().getApePaterno() + " " + 
                                                                                        this.cursoInscripcion.getPersona().getApeMaterno() );
            
            contenido = contenido.replace("{1}", this.cursoInscripcion.getCursoProgramacion().getCurso().getNombre());
            
            CorreoDnaAdministrado correoDnaAdministrado = (CorreoDnaAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{correoDnaAdministrado}",CorreoDnaAdministrado.class);
            correoDnaAdministrado.getCorreo().getDestinatarios().add(this.cursoInscripcion.getPersona().getCorreo());
            correoDnaAdministrado.getCorreo().setTitulo(MimeUtility.encodeText("Notificación de Aprobación del Curso", "utf-8", "B"));
            correoDnaAdministrado.getCorreo().setContenido(contenido);
            correoDnaAdministrado.getCorreo().setHtml(true);
//            correoDnaAdministrado.getCorreo().addAttach(this.cursoInscripcion.getArchivoCert(),"certificado.pdf");
            
            if(this.cursoInscripcion.getCursoProgramacion().isFlgGenerard() && this.cursoInscripcion.getCursoProgramacion().getArchivoRd()!=null)
              //  correoDnaAdministrado.getCorreo().addAttach(this.cursoInscripcion.getCursoProgramacion().getArchivoRd(),"resdirectoral.pdf");
            
            correoDnaAdministrado.getCorreo().enviarCorreo();
            
        } catch(Exception ex) {
            adicionarMensajeError("Error","Error al tratar de enviar la notificación al correo electrónico");
        }    
    }
    
    /***
     * realiza la exportación del listado de solicitudes
     */
    public void exportarSolicitudes(){
        
       
        try {
            
             
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response =  (HttpServletResponse)context.getResponse();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=solicitudes.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("SOLICITUDES DE INSCRIPCIÓN");

            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 20);
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);

            XSSFFont fontHeader = workbook.createFont();
            fontHeader.setFontHeightInPoints((short) 8);
            fontHeader.setBold(true);

            XSSFCellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setFont(fontHeader);
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
            styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
            styleHeader.setWrapText(true);
            styleHeader.setBorderBottom(BorderStyle.THIN);
            styleHeader.setBorderTop(BorderStyle.THIN);
            styleHeader.setBorderLeft(BorderStyle.THIN);
            styleHeader.setBorderRight(BorderStyle.THIN);

            Row row = sheet.createRow(0);
            Cell cellTitulo = row.createCell(0);
            cellTitulo.setCellStyle(style);
            cellTitulo.setCellValue("SOLICITUDES DE INSCRIPCIÓN");    

            int rowNum = 2;
            int colNum = 0;

            Cell cellHeader;
            Cell cell;    

            row = sheet.createRow(rowNum++);
           
            sheet.setColumnWidth(colNum, 4000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("TIPO DOC");
            
            
            sheet.setColumnWidth(colNum, 4000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("NRO_DOC");

            sheet.setColumnWidth(colNum, 6000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("APELLIDO PATERNO");

            sheet.setColumnWidth(colNum, 6000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("APELLIDO PATERNO");

            sheet.setColumnWidth(colNum, 6000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("NOMBRES");

            sheet.setColumnWidth(colNum, 3000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("SEXO");

            sheet.setColumnWidth(colNum, 3000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("TELEFONO");
            
            sheet.setColumnWidth(colNum, 6000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("CORREO");

            sheet.setColumnWidth(colNum, 7000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("DEPARTAMENTO");

            sheet.setColumnWidth(colNum, 7000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("PROVINCIA");

            sheet.setColumnWidth(colNum, 7000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("DISTRITO");

            sheet.setColumnWidth(colNum, 9000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("DNA");

            sheet.setColumnWidth(colNum, 4000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("FUNCIÓN");

            sheet.setColumnWidth(colNum, 3000); 
            cellHeader = row.createCell(colNum++);
            cellHeader.setCellStyle(styleHeader);
            cellHeader.setCellValue("ESTADO");
            
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,colNum));
       
            XSSFFont fontCell = workbook.createFont();
            fontCell.setFontHeightInPoints((short) 10);

            XSSFCellStyle styleCell = workbook.createCellStyle();
            styleCell.setFont(fontCell);
            styleCell.setWrapText(true);

            for (CursoInscripcion ci : this.listaInscripcion) {

                row = sheet.createRow(rowNum++);
                colNum = 0;
                int col = 0;

                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getNroDoc());
               
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getNroDoc());
               
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getApePaterno());
                 
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getApeMaterno());
                 
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getNombres());
                 
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getSexo());
                 
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getTelefono());
                 
                 cell = row.createCell(colNum++);
                 cell.setCellStyle(styleCell);
                 cell.setCellValue(ci.getPersona().getCorreo());
                /*       
                for (Object field : datatype) {
                        cell = row.createCell(colNum++);
                        cell.setCellStyle(styleCell);
                        if (field instanceof String) {
                            cell.setCellValue((String) field);
                        } else if (field instanceof Integer) {
                            cell.setCellValue((Integer) field);
                        } else if (field instanceof BigDecimal) {
                            cell.setCellValue(((BigDecimal) field).doubleValue());
                        }

                    col++;
                }*/
            }

            workbook.write(response.getOutputStream());
            } catch (Exception ex) {
                // LOGGER.error("Error al generar el archivo Excel", ex);
        }
            
    }
    
    public CursoProgramacion getCursoProgramacion() {
        return cursoProgramacion;
    }

    public void setCursoProgramacion(CursoProgramacion CursoProgramacion) {
        this.cursoProgramacion = CursoProgramacion;
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

    public CursoInscripcion getCursoInscripcion() {
        return cursoInscripcion;
    }

    public void setCursoInscripcion(CursoInscripcion cursoInscripcion) {
        this.cursoInscripcion = cursoInscripcion;
    }

    public boolean isDeclaro() {
        return declaro;
    }

    public void setDeclaro(boolean declaro) {
        this.declaro = declaro;
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

    public boolean isDeclaroCorreo() {
        return declaroCorreo;
    }

    public void setDeclaroCorreo(boolean declaroCorreo) {
        this.declaroCorreo = declaroCorreo;
    }

    public List<ColumnModel> getColumnasEval() {
        return columnasEval;
    }

    public void setColumnasEval(List<ColumnModel> columnasEval) {
        this.columnasEval = columnasEval;
    }

    public List<Map<String, Object>> getListaEvalMap() {
        return listaEvalMap;
    }

    public void setListaEvalMap(List<Map<String, Object>> listaEvalMap) {
        this.listaEvalMap = listaEvalMap;
    }

    public boolean isPermiteIngreso() {
        return permiteIngreso;
    }

    public void setPermiteIngreso(boolean permiteIngreso) {
        this.permiteIngreso = permiteIngreso;
    }

    public boolean isRegistroExitoso() {
        return registroExitoso;
    }

    public void setRegistroExitoso(boolean registroExitoso) {
        this.registroExitoso = registroExitoso;
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



    public List<Provincia> getListaProvinciasBus() {
        return listaProvinciasBus;
    }

    public void setListaProvinciasBus(List<Provincia> listaProvinciasBus) {
        this.listaProvinciasBus = listaProvinciasBus;
    }

    public BigDecimal getBusTutor() {
        return busTutor;
    }

    public void setBusTutor(BigDecimal busTutor) {
        this.busTutor = busTutor;
    }

    public List<CursoInscripcion> getListaInscripcion() {
        return listaInscripcion;
    }

    public void setListaInscripcion(List<CursoInscripcion> listaInscripcion) {
        this.listaInscripcion = listaInscripcion;
    }

    public Boolean getVerCalificar() {
        return verCalificar;
    }

    public void setVerCalificar(Boolean verCalificar) {
        this.verCalificar = verCalificar;
    }

    
}
