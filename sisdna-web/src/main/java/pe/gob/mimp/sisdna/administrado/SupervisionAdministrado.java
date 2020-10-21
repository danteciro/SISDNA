package pe.gob.mimp.sisdna.administrado;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.SupervisionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Supervision;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.modelo.Departamento;
import pe.gob.mimp.seguridad.modelo.Usuario;
import pe.gob.mimp.sisdna.dto.SupervisionObsDto;
import pe.gob.mimp.sisdna.fachada.CatalogoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.MunicipalidadFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.DefensoriaPersona;
import pe.gob.mimp.sisdna.modelo.Municipalidad;
import pe.gob.mimp.sisdna.modelo.SupervisionObs;
import pe.gob.mimp.sisdna.modelo.SupervisionRrhh;
import pe.gob.mimp.webservicemimp.auxiliar.IdentificacionReniec;

/**
 * Clase: SupervisionAdministrado.java <br>
 * Clase encargada de gestionar los supervisiones que realiza la DSLD.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "supervisionAdministrado")
@ViewScoped
public class SupervisionAdministrado extends AdministradorAbstracto implements Serializable {

  //   private static final Logger // LOGGER = LogManager.getLogger(SupervisionAdministrado.class.getName());

    private Supervision supervision;
    private List<Supervision> lista;
    
    private List<SupervisionObsDto> listaObsEntidad;
    private List<SupervisionObsDto> listaObsDna;
    private List<SupervisionObsDto> listaObsIntegrantes;
    
    private int modo;
    private BigInteger nidRequisito;
    private BigDecimal nidPrereq;
    
    private List<Provincia> listaProvincias;
    private List<Distrito> listaDistritos;
    
    private List<String> listaSupervisions;
    private String supervisionPorAgregar;

    private BigDecimal busSupervisor;
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private BigInteger busEstado;
    private String busCodigo;
    private Date busDesde;
    private Date busHasta;
    private Integer busAnho;
    private boolean verNoSupervisadas;
    
    private String nomDepartamento;
    private String nomProvincia;
    private String nomDistrito;

    private String dnaCodigo;
    private String dnaCodigoOriginal;
    
    private List<Defensoria> listaSedes;
    
    private IdentificacionReniec identificacionReniec;
 
    private String[] repMeses;
    
    @EJB
    private SupervisionFacadeLocal supervisionFacade;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    
    @EJB
    private DefensoriaFacadeLocal defensoriaFacade;
 
    @EJB
    private MunicipalidadFacadeLocal municipalidadFacade;
 
    @EJB
    private CatalogoFacadeLocal catalogoFacade;
    
    public SupervisionAdministrado() {
        this.lista = new ArrayList<>();
   }
    
    @PostConstruct
    public void initLoad() {
       this.supervision = new Supervision();
       this.modo = Constantes.MODO_LISTADO;
       this.verNoSupervisadas = false;
       this.identificacionReniec = new IdentificacionReniec();
    }
    
    /**
     * muestra el título del formulario según el modo
     * @return el título del modo
     */
    public String titulo() {
        switch(this.modo) {
            case 1: return "Nueva Ficha de Supervisión";
            case 2: return "Actualización de Ficha de Supervisión";
        }
        return "";
    } 
   
    /**
     * cambia para regresar al modo listado
     */
    public void regresar(){
       this.titulo();
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
         if(this.busEstado!=null) {
            Row row = sheet.createRow(num++);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Estado:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            
            cell.setCellValue(this.catalogoFacade.find(this.busEstado).getTxtNombre());
            
        }
        if(this.busDesde!=null) {
            Row row = sheet.createRow(num++);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Fecha de inicio:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(formatter.format(this.busDesde));            
        }
        
        if(this.busHasta!=null) {
            Row row = sheet.createRow(num++);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Fecha de fin:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(formatter.format(this.busHasta));            
        }
        
        if(this.busSupervisor!=null) {
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Supervisor:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            Usuario u = usuarioAdministrado.getEntidad(this.busSupervisor.toPlainString());
//            cell.setCellValue(u.getPersona().nombreApellidos());
            sheet.addMergedRegion(new CellRangeAddress(num-1,num-1, 1, 4));
        }
    
      
        return num;
    }
    
    public void postProcessXLSSupervisiones(Object document) {
        this.postProcessXLS(document, "SUPERVISIONES");
    }
      
    public void postProcessXLSNoSupervisadas(Object document) {
        this.postProcessXLS(document, "DNA NO SUPERVISADAS");
    }  
    
      public void postProcessXLS(Object document, String titulo) {
        XSSFWorkbook workbook = (XSSFWorkbook) document;
        XSSFSheet sheet = workbook.getSheetAt(0);
        workbook.setSheetName(0, "LISTADO");
        
        if(titulo.equals("DNA NO SUPERVISADAS"))
            sheet.setColumnWidth(0, 13000); 
        else
            sheet.setColumnWidth(0, 5000); 
       
        sheet.shiftRows(0, sheet.getLastRowNum()+1, 8, true,true);
        int numero = this.escribirFiltro(workbook, sheet, 0,titulo);
       
        sheet.shiftRows(6, sheet.getLastRowNum()+1, numero-8, true,true);
       
               
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
     * devuelve la lista de supervisiones activos ordenados por el nombre
     * @return lista de supervisiones
     */
    public List<Supervision> obtenerSupervisionesActivas(){
        return this.supervisionFacade.obtenerActivos(true, "nombre");        
    }

    /**
     * Verificar el proceso de busqueda de dni
     * @param ctx - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
   */
    public void verificarReniec(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        
        String dni = (String)value;
          
        if(dni==null || dni.length()<8)
              throw new ValidatorException(new FacesMessage("El DNI debe tener 8 dígitos"));

        try { 

          if(this.supervision.getEntNroDni()==null || !dni.equals(this.supervision.getEntNroDni())) {   
         
            this.identificacionReniec.obtenerConsultaReniec(dni);

            if (this.identificacionReniec.getNOMBRES() != null) {
              
              this.supervision.setEntApePat(this.identificacionReniec.getAPPAT());
              this.supervision.setEntApeMat(this.identificacionReniec.getAPMAT());
              this.supervision.setEntNombres(this.identificacionReniec.getNOMBRES());
              
            }  else {
                this.supervision.setEntApePat(null);
                this.supervision.setEntApeMat(null);
                this.supervision.setEntNombres(null);
                throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
            }
          }   
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
    }
    
      /**
     * Verifica el DNI del Responsable de la DNA
     * @param ctx - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
   */
    public void verificarReniecResponsable(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        
        String dni = (String)value;
          
        if(dni==null || dni.length()<8)
              throw new ValidatorException(new FacesMessage("El DNI debe tener 8 dígitos"));

        try { 
            if(this.supervision.getRespDni()==null || !dni.equals(this.supervision.getRespDni())) {   
                
                this.identificacionReniec.obtenerConsultaReniec(dni);

                if (this.identificacionReniec.getNOMBRES() != null) {

                  this.supervision.setRespApePat(this.identificacionReniec.getAPPAT());
                  this.supervision.setRespApeMat(this.identificacionReniec.getAPMAT());
                  this.separarNombres(this.identificacionReniec.getNOMBRES());

                }  else {
                    this.supervision.setRespApePat(null);
                    this.supervision.setRespApeMat(null);
                    this.supervision.setRespNombre1(null);
                    this.supervision.setRespNombre2(null);
                    this.supervision.setRespNombre3(null);
                    throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
                }
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
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
        
        this.supervision.setRespNombre1(listaNombres.get(0));

        if(listaNombres.size()>1)
          this.supervision.setRespNombre2(listaNombres.get(1));

        if(listaNombres.size()>2)
          this.supervision.setRespNombre3(listaNombres.get(2));

    }
    
    /**
     * inicializa la defensoría de la supervisión
     */
    private void resetDefensoria() {
         this.supervision.setDefensoria(new Defensoria());
    }
    
    /**
     * inicializa los valores de la DNA y de la autoridad de la entidad
     */
    private void resetDefensoriaInfo() {
         this.supervision.setDnaCorreo(null);
         this.supervision.setDnaTelefono(null);
         this.supervision.setDnaHorario(null);
         this.supervision.setEntidadAutoridad(null);
         this.supervision.setEntidadDireccion(null);
    }
    
    /**
     * inicializa los datos del responsable de la DNA
     */
    private void resetResponsable() {
        this.supervision.setRespDni(null);
        this.supervision.setRespApePat(null);
        this.supervision.setRespApeMat(null);
        this.supervision.setRespNombre1(null);
        this.supervision.setRespNombre2(null);
        this.supervision.setRespNombre3(null);
        this.supervision.setRespProfesion(null);
        this.supervision.setRespColegio(null);
        this.supervision.setRespColegiatura(null);
        this.supervision.setRespCorreo(null);
        this.supervision.setRespTelefono(null);
        this.supervision.setRespFechaCargo(null);
       
    }
     
    /**
     * Valida si existe la DNA y carga los nombres del departamento, provincia y distrito. <br>
     * Carga el nombre del alcalde y la dirección de la municipalidad si es que el origien de la demuna es municipal
     * @param ctx - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
     */
    public void validarDna(FacesContext ctx, UIComponent component, Object value) throws ValidatorException	{
        
         
      
        if( value!= null && !value.equals("")) {
          String codigoDna = value.toString();
                   
          if(!codigoDna.equals(this.dnaCodigoOriginal) ) {
            this.dnaCodigoOriginal = codigoDna;
            ParamFiltro param = new ParamFiltro();
            param.adicionar("txtConstancia", codigoDna);
            List<Defensoria> listaDna = this.defensoriaFacade.obtenerPorFiltro( param, true, "txtConstancia", false);
            
            if(!listaDna.isEmpty()) {
                
                if(listaDna.size()>1) {
                    this.listaSedes = listaDna;   
                    this.resetDefensoria();
                    this.resetDefensoriaInfo();
                } else {
                    this.supervision.setDefensoria(listaDna.get(0));
                    this.nomDepartamento = this.departamentoFacade.find(listaDna.get(0).getNidDepartamento()).getTxtDescripcion();
                    this.nomProvincia = this.provinciaFacade.find(listaDna.get(0).getNidProvincia()).getTxtDescripcion();
                    this.nomDistrito = this.distritoFacade.find(listaDna.get(0).getNidDistrito()).getTxtDescripcion();

                    if(listaDna.get(0).getDefensoriaInfo()!=null) {
                        this.supervision.setDnaCorreo(listaDna.get(0).getDefensoriaInfo().getTxtCorreo());
                        this.supervision.setDnaTelefono(listaDna.get(0).getDefensoriaInfo().getTxtTelefono());
                        this.supervision.setDnaHorario(listaDna.get(0).getDefensoriaInfo().getDias());
                    } else {
                        this.resetDefensoriaInfo();
                    }
                    
                    this.resetResponsable();
                   
                    DefensoriaPersona responsable = listaDna.get(0).getResponsable();
                    
                    if(responsable!=null) {
                        this.supervision.setRespDni(responsable.getTxtDocumento());
                        this.supervision.setRespApePat(responsable.getTxtApellidoPaterno());
                        this.supervision.setRespApeMat(responsable.getTxtApellidoMaterno());
                        this.supervision.setRespNombre1(responsable.getTxtNombre1());
                        this.supervision.setRespNombre2(responsable.getTxtNombre2());
                        this.supervision.setRespNombre3(responsable.getTxtNombre3());
                        this.supervision.setRespColegio(responsable.getTxtColegio());
                        this.supervision.setRespColegiatura(responsable.getTxtColegiatura());
                        this.supervision.setRespCorreo(responsable.getTxtCorreo());
                        this.supervision.setRespTelefono(responsable.getTxtTelefono());
                        
                        if(responsable.getProfesion()==null)
                            this.supervision.setRespProfesion(new Catalogo());
                        else
                            this.supervision.setRespProfesion(responsable.getProfesion());
                    } 

                    if(listaDna.get(0).getOrigen()!=null && 
                         ( listaDna.get(0).getOrigen().getNidCatalogo().compareTo(Constantes.CATALOGO_ORIGEN_PROVINCIAL)==0 || 
                              listaDna.get(0).getOrigen().getNidCatalogo().compareTo(Constantes.CATALOGO_ORIGEN_DISTRITAL)==0 )  ) {

                            ParamFiltro pmuni = new ParamFiltro();
                            pmuni.adicionar("nidDepartamento", listaDna.get(0).getNidDepartamento());
                            pmuni.adicionar("nidProvincia", listaDna.get(0).getNidProvincia());
                            pmuni.adicionar("nidDistrito", listaDna.get(0).getNidDistrito());
                            List<Municipalidad> listaMunis = this.municipalidadFacade.obtenerPorFiltro(pmuni, true, "nombre", false);

                            if(!listaMunis.isEmpty()) {
                                this.supervision.setEntidadAutoridad(listaMunis.get(0).getNombre());
                                this.supervision.setEntidadDireccion(listaMunis.get(0).getDireccion());
                            }
                    }
                }
            }
            else {
                this.resetDefensoria();
                this.resetDefensoriaInfo();
                this.nomDepartamento = "";
                this.nomProvincia = "";
                this.nomDistrito = "";
                this.dnaCodigoOriginal = "" ;
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "El código ingresado no existe",""));
            }
          }
        } else
             throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar el código de la DNA",""));
        
           
           
    }
    
    /**
     * inicializa los valores para ingresar una nueva supervisión. 
     * Setea la lista de observaciones desde la tabla Catálogo.
    */
    public void nuevo() {
        this.supervision = new Supervision();
        this.modo = Constantes.MODO_NUEVO;
        this.listaObsEntidad = new ArrayList<>();
        this.listaObsDna = new ArrayList<>();
        this.listaObsIntegrantes = new ArrayList<>();
        this.nomDepartamento = null;
        this.nomProvincia = null;
        this.nomDistrito = null;
        this.dnaCodigo = "";
        this.dnaCodigoOriginal = "";
        
        CatalogoAdministrado catalogoAdministrado = (CatalogoAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{catalogoAdministrado}", CatalogoAdministrado.class);
        List<Catalogo> listaCatalogos = catalogoAdministrado.obtenerSupervisionObsEntidad();
        
        for(Catalogo c: listaCatalogos) {
            SupervisionObsDto cp = new SupervisionObsDto();
            cp.setNidCatalogo(c.getNidCatalogo());
            cp.setDescripcion(c.getTxtNombre());
            cp.setEstado(Constantes.SUPERVISION_OBS_NOAPLICA);
            this.listaObsEntidad.add(cp);
         }
        
        listaCatalogos = catalogoAdministrado.obtenerSupervisionObsDna();
        
        for(Catalogo c: listaCatalogos) {
            SupervisionObsDto cp = new SupervisionObsDto();
            cp.setNidCatalogo(c.getNidCatalogo());
            cp.setDescripcion(c.getTxtNombre());
            cp.setEstado(Constantes.SUPERVISION_OBS_NOAPLICA);
            this.listaObsDna.add(cp);
         }
        
        listaCatalogos = catalogoAdministrado.obtenerSupervisionObsIntegrantes();
        
        for(Catalogo c: listaCatalogos) {
            SupervisionObsDto cp = new SupervisionObsDto();
            cp.setNidCatalogo(c.getNidCatalogo());
            cp.setDescripcion(c.getTxtNombre());
            cp.setEstado(Constantes.SUPERVISION_OBS_NOAPLICA);
            this.listaObsIntegrantes.add(cp);
         }
        
        List<Catalogo> listaFunciones = catalogoAdministrado.obtenerFunciones();
        
        for(Catalogo c: listaFunciones) {
            SupervisionRrhh cp = new SupervisionRrhh();
            cp.setFuncion(c);
            cp.setDerechoH((byte)0);
            cp.setDerechoM((byte)0);
            cp.setEducacionH((byte)0);
            cp.setEducacionM((byte)0);
            cp.setOtroH((byte)0);
            cp.setOtroM((byte)0);
            cp.setPsicologiaH((byte)0);
            cp.setPsicologiaM((byte)0);
            cp.setTrabSocH((byte)0);
            cp.setTrabSocM((byte)0);
            cp.setTotalH(0);
            cp.setTotalM(0);
            this.supervision.getListaRrhh().add(cp);
         }
        
    }

       
    /**
     * obtiene todos los registros de los supervisiones
     * @return lista de supervisiones
     */
    public List<Supervision> obtenerTodos() {
        
        try {
        
            if(this.busCodigo!=null && !this.busCodigo.equals("")) {
                this.lista = this.supervisionFacade.obtenerSupervisionesPorCodigo(this.busCodigo);
            } else    {
              
               if(this.busDepartamento!=null || 
                        this.busProvincia!=null || 
                            this.busDistrito!=null || 
                                 this.busEstado!=null || 
                                        this.busSupervisor!=null ||
                                            this.busDesde!=null || 
                                                this.busHasta!=null || this.busCodigo !=null)
                this.lista = this.supervisionFacade.obtenerSupervisiones(this.busDepartamento, 
                                                                         this.busProvincia, 
                                                                         this.busDistrito, 
                                                                         this.busEstado, 
                                                                         this.busDesde, this.busHasta, this.busCodigo, this.busSupervisor);
               
               else
                   this.lista = null;
             }
        }catch(Exception ex) {
             // LOGGER.error("Error al consultar los supervisiones", ex);
        }
        return this.lista;
    }
    
    /**
     * obtiene todos los registros de las dna no supervisadas
     * @return lista de dnas
     */
    public List<Object[]> obtenerDefensorias() {
        
        try {
        
           return this.defensoriaFacade.obtenerDefensoriasNoSupervisadas(this.busDepartamento, this.busProvincia, this.busDistrito, 
                                                                            this.busDesde, this.busHasta, this.busCodigo);

        }catch(Exception ex) {
             // LOGGER.error("Error al consultar las defensorías", ex);
        }
        return null;
    }
    
   /**
    * carga la información de la supervisión seleccionada y cambia al modo update
    * @param item - Supervision - objeto de la supervisión enviada del datatable
    */  
   public void obtener(Supervision item) {
       this.supervision = item;
       this.modo = Constantes.MODO_UPDATE;
       this.supervisionPorAgregar = null;
       this.nidRequisito = null;
       
       // información de demuna
       this.dnaCodigo = item.getDefensoria().getTxtConstancia();
       this.dnaCodigoOriginal = this.dnaCodigo;
      // TODO sedes
       
       this.nomDepartamento = this.departamentoFacade.find(item.getDefensoria().getNidDepartamento()).getTxtDescripcion();
       this.nomProvincia = this.provinciaFacade.find(item.getDefensoria().getNidProvincia()).getTxtDescripcion();
       this.nomDistrito = this.distritoFacade.find(item.getDefensoria().getNidDistrito()).getTxtDescripcion();

       // fin de info de demuna
       
       this.listaObsEntidad = new ArrayList<>();
       this.listaObsDna = new ArrayList<>();
       this.listaObsIntegrantes = new ArrayList<>();
     
       for(SupervisionObs obs: item.getListaObs()) {
            Catalogo c = obs.getCatalogo();
            SupervisionObsDto dto = new SupervisionObsDto();
            dto.setNidSupervisionObs(obs.getNidObs());
            dto.setNidCatalogo(c.getNidCatalogo());
            dto.setDescripcion(c.getTxtNombre());
            dto.setEstado(obs.getEstado());
            dto.setEstadoOriginal(obs.getEstado());
            
            if(c.getNidPadre().compareTo(Constantes.CATALOGO_SUPERVISION_OBS_ENTIDAD)==0)
                this.listaObsEntidad.add(dto);
            else {
                 if(c.getNidPadre().compareTo(Constantes.CATALOGO_SUPERVISION_OBS_DNA)==0)
                    this.listaObsDna.add(dto);
                 else   
                    if(c.getNidPadre().compareTo(Constantes.CATALOGO_SUPERVISION_OBS_INTEGRANTES)==0)
                        this.listaObsIntegrantes.add(dto);
            }
       }
            
   }
   
   /**
    * permite cambiar el estado del registro del supervision: activo e inactivo
    */
     public void anular() {
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.supervision.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.supervision.setFlgActivo(this.supervision.getFlgActivo());
            this.supervision.setTxtPc(Internet.obtenerNombrePC());
            this.supervision.setTxtIp(Internet.obtenerIPPC());
            this.supervision.setFecModificacion(new Date());
            this.supervisionFacade.edit(this.supervision);
            adicionarMensaje("","La ficha de supervisión ha cambiado su estado a " + ((this.supervision.getFlgActivo().compareTo(BigInteger.ONE)==0)?"Activo":"Inactivo") + " con éxito.");
         
            this.modo = Constantes.MODO_LISTADO;
            
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al actualizar la Supervisión.");
            // LOGGER.error("Error al anular el supervision",ex);
        }
    }
    
    /**
     * actualiza la información del registro del supervision.
     */ 
    public void update() {
        
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.supervision.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.supervision.setTxtPc(Internet.obtenerNombrePC());
            this.supervision.setTxtIp(Internet.obtenerIPPC());
            this.supervision.setFecModificacion(new Date());
            this.supervision.setFlgActivo(BigInteger.ONE);
          
            for(SupervisionObsDto sod: this.listaObsEntidad) {
                if(!sod.getEstado().equals(sod.getEstadoOriginal())) {
                    for(SupervisionObs supObs: this.supervision.getListaObs()) {
                        if(sod.getNidSupervisionObs().compareTo(supObs.getNidObs())==0) {
                            supObs.setEstado(sod.getEstado());
                            supObs.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                            supObs.setTxtPc(Internet.obtenerNombrePC());
                            supObs.setTxtIp(Internet.obtenerIPPC());
                            supObs.setFecModificacion(new Date());
                            supObs.setFlgActivo(BigInteger.ONE);
                            break;
                        }
                    }
               }
            }
            
            for(SupervisionObsDto sod: this.listaObsDna) {
                if(!sod.getEstado().equals(sod.getEstadoOriginal())) {
                    for(SupervisionObs supObs: this.supervision.getListaObs()) {
                        if(sod.getNidSupervisionObs().compareTo(supObs.getNidObs())==0) {
                            supObs.setEstado(sod.getEstado());
                            supObs.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                            supObs.setTxtPc(Internet.obtenerNombrePC());
                            supObs.setTxtIp(Internet.obtenerIPPC());
                            supObs.setFecModificacion(new Date());
                            supObs.setFlgActivo(BigInteger.ONE);
                            break;
                        }
                    }
               }
            }
            
            for(SupervisionObsDto sod: this.listaObsIntegrantes) {
                if(!sod.getEstado().equals(sod.getEstadoOriginal())) {
                    for(SupervisionObs supObs: this.supervision.getListaObs()) {
                        if(sod.getNidSupervisionObs().compareTo(supObs.getNidObs())==0) {
                            supObs.setEstado(sod.getEstado());
                            supObs.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                            supObs.setTxtPc(Internet.obtenerNombrePC());
                            supObs.setTxtIp(Internet.obtenerIPPC());
                            supObs.setFecModificacion(new Date());
                            supObs.setFlgActivo(BigInteger.ONE);
                            break;
                        }
                    }
               }
            }
            
            this.supervisionFacade.edit(this.supervision);
            adicionarMensaje("","La ficha de supervisión ha sido actualizada con éxito.");
            this.modo = Constantes.MODO_LISTADO;
            
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al actualizar la ficha de supervisión. Por favor, intente nuevamente.");
             // LOGGER.error("Error al actualizar el supervision",ex);
        }
    }
    
    /**
     * permite crear o actualizar según el modo del formulario. 
     * Guarda la información de los prerequisitos, requisitos, ponderaciones y documentos
     */
    public void create() {
    
        if(this.modo == Constantes.MODO_UPDATE)
            this.update();
        else
        try {
    
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.supervision.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.supervision.setTxtPc(Internet.obtenerNombrePC());
            this.supervision.setTxtIp(Internet.obtenerIPPC());
            this.supervision.setFecRegistro(new Date());
            this.supervision.setFlgActivo(BigInteger.ONE);
       
            for(SupervisionRrhh srh: this.supervision.getListaRrhh()) {
                srh.setSupervision(this.supervision);
                srh.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                srh.setTxtPc(Internet.obtenerNombrePC());
                srh.setTxtIp(Internet.obtenerIPPC());
                srh.setFecRegistro(new Date());
                srh.setFlgActivo(BigInteger.ONE);
            }
     
            for(SupervisionObsDto sod: this.listaObsEntidad) {
                SupervisionObs supObs = new SupervisionObs();
                supObs.getCatalogo().setNidCatalogo(sod.getNidCatalogo());
                supObs.setEstado(sod.getEstado());
                supObs.setSupervision(this.supervision);
                supObs.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                supObs.setTxtPc(Internet.obtenerNombrePC());
                supObs.setTxtIp(Internet.obtenerIPPC());
                supObs.setFecRegistro(new Date());
                supObs.setFlgActivo(BigInteger.ONE);
                this.supervision.getListaObs().add(supObs);
            }
            
            for(SupervisionObsDto sod: this.listaObsDna) {
                SupervisionObs supObs = new SupervisionObs();
                supObs.getCatalogo().setNidCatalogo(sod.getNidCatalogo());
                supObs.setEstado(sod.getEstado());
                supObs.setSupervision(this.supervision);
                supObs.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                supObs.setTxtPc(Internet.obtenerNombrePC());
                supObs.setTxtIp(Internet.obtenerIPPC());
                supObs.setFecRegistro(new Date());
                supObs.setFlgActivo(BigInteger.ONE);
                this.supervision.getListaObs().add(supObs);
            }
             
            for(SupervisionObsDto sod: this.listaObsIntegrantes) {
                SupervisionObs supObs = new SupervisionObs();
                supObs.getCatalogo().setNidCatalogo(sod.getNidCatalogo());
                supObs.setEstado(sod.getEstado());
                supObs.setSupervision(this.supervision);
                supObs.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                supObs.setTxtPc(Internet.obtenerNombrePC());
                supObs.setTxtIp(Internet.obtenerIPPC());
                supObs.setFecRegistro(new Date());
                supObs.setFlgActivo(BigInteger.ONE);
                this.supervision.getListaObs().add(supObs);
            }
              
            this.supervisionFacade.create(this.supervision);
            this.modo = Constantes.MODO_LISTADO;
            
            adicionarMensaje("","La ficha de supervisión ha sido registrada con éxito.");
                 
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al crear la ficha de supervisión. Por favor, intente nuevamente.");
            // LOGGER.error("Error al regisgtrar el supervision",ex);
        }
    }

   /**
    * calcula el total de integrantes por cada función dentro de la ficha
    * @param itemRh el registro de la función
    */
   public void calcularTotal(SupervisionRrhh itemRh) {
       
       itemRh.setTotalH( (Integer)(itemRh.getDerechoH()+ itemRh.getEducacionH() + itemRh.getTrabSocH() + itemRh.getPsicologiaH() + itemRh.getOtroH()) );
       itemRh.setTotalM( (Integer)(itemRh.getDerechoM()+ itemRh.getEducacionM() + itemRh.getTrabSocM() + itemRh.getPsicologiaM() + itemRh.getOtroM()) );
   }
    
    /**
     * permite subir el contenido en bytes de la ficha
     * @param event - FileUploadEvent - evento generado al subir el archivo
     */
    public void uploadFicha(FileUploadEvent event) {
        
        try {
         
            this.supervision.setArchivoFicha(IOUtils.toByteArray(event.getFile().getInputstream()));
            this.supervision.setArchivoNombre(event.getFile().getFileName());
            adicionarMensaje("", "Carga de Archivo: "+event.getFile().getFileName() + " terminado");
           
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir el archivo",ex);
        }
    }
  
    /**
     * devuelve el stream del contenido en bytes de la ficha
     * @return StreamedContent
     */
    public StreamedContent descargarFicha() {
        
        try {
         
             InputStream ips = new ByteArrayInputStream(this.supervision.getArchivoFicha());
             return new DefaultStreamedContent(ips, "application/pdf", this.supervision.getArchivoNombre());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo",ex);
        }
        return null;
    }
    
    /**
     * devuelve el stream del contenido en bytes de la ficha
     * @param item item de supervisión
     * @return StreamedContent stream de archivo
     */
    public StreamedContent descargarFicha(Supervision item) {
        
        try {
         
             InputStream ips = new ByteArrayInputStream(item.getArchivoFicha());
             return new DefaultStreamedContent(ips, "application/pdf", item.getArchivoNombre());
          
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al descargar archivo");
            // LOGGER.error("Error al descargar el archivo",ex);
        }
        return null;
    }
    
     /**
     * elimina el contenido del archivo de la ficha
     */
    public void eliminarFicha() {
        this.supervision.setArchivoFicha(null);
        this.supervision.setArchivoNombre(null);
    }
    
    
     /**
     * devuelve la lista de departamentos
     * @return lista de departamentos
     */
    public List<Departamento> obtenerDepartamentos() {
        return  this.departamentoFacade.obtenerDepartamentos();
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
    
  

    public Supervision getSupervision() {
        return supervision;
    }

    public void setSupervision(Supervision supervision) {
        this.supervision = supervision;
    }

    public List<Supervision> getLista() {
        return lista;
    }

    public void setLista(List<Supervision> lista) {
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

    public List<String> getListaSupervisions() {
        return listaSupervisions;
    }

    public void setListaSupervisions(List<String> listaSupervisions) {
        this.listaSupervisions = listaSupervisions;
    }

    public String getSupervisionPorAgregar() {
        return supervisionPorAgregar;
    }

    public void setSupervisionPorAgregar(String supervisionPorAgregar) {
        this.supervisionPorAgregar = supervisionPorAgregar;
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

    public List<SupervisionObsDto> getListaObsEntidad() {
        return listaObsEntidad;
    }

    public void setListaObsEntidad(List<SupervisionObsDto> listaObsEntidad) {
        this.listaObsEntidad = listaObsEntidad;
    }

    public List<SupervisionObsDto> getListaObsDna() {
        return listaObsDna;
    }

    public void setListaObsDna(List<SupervisionObsDto> listaObsDna) {
        this.listaObsDna = listaObsDna;
    }

    public List<SupervisionObsDto> getListaObsIntegrantes() {
        return listaObsIntegrantes;
    }

    public void setListaObsIntegrantes(List<SupervisionObsDto> listaObsIntegrantes) {
        this.listaObsIntegrantes = listaObsIntegrantes;
    }

    public String getNomDepartamento() {
        return nomDepartamento;
    }

    public void setNomDepartamento(String nomDepartamento) {
        this.nomDepartamento = nomDepartamento;
    }

    public String getNomProvincia() {
        return nomProvincia;
    }

    public void setNomProvincia(String nomProvincia) {
        this.nomProvincia = nomProvincia;
    }

    public String getNomDistrito() {
        return nomDistrito;
    }

    public void setNomDistrito(String nomDistrito) {
        this.nomDistrito = nomDistrito;
    }

    public String getDnaCodigo() {
        return dnaCodigo;
    }

    public void setDnaCodigo(String dnaCodigo) {
        this.dnaCodigo = dnaCodigo;
    }

    public List<Defensoria> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<Defensoria> listaSedes) {
        this.listaSedes = listaSedes;
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

    public boolean isVerNoSupervisadas() {
        return verNoSupervisadas;
    }

    public void setVerNoSupervisadas(boolean verNoSupervisadas) {
        this.verNoSupervisadas = verNoSupervisadas;
    }

    public String[] getRepMeses() {
        return repMeses;
    }

    public void setRepMeses(String[] repMeses) {
        this.repMeses = repMeses;
    }

    public BigInteger getBusEstado() {
        return busEstado;
    }

    public void setBusEstado(BigInteger busEstado) {
        this.busEstado = busEstado;
    }

    public String getBusCodigo() {
        return busCodigo;
    }

    public void setBusCodigo(String busCodigo) {
        this.busCodigo = busCodigo;
    }

    public String getDnaCodigoOriginal() {
        return dnaCodigoOriginal;
    }

    public void setDnaCodigoOriginal(String dnaCodigoOriginal) {
        this.dnaCodigoOriginal = dnaCodigoOriginal;
    }

    public Integer getBusAnho() {
        return busAnho;
    }

    public void setBusAnho(Integer busAnho) {
        this.busAnho = busAnho;
    }

    public BigDecimal getBusSupervisor() {
        return busSupervisor;
    }

    public void setBusSupervisor(BigDecimal busSupervisor) {
        this.busSupervisor = busSupervisor;
    }
    
    
    

}
