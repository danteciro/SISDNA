package pe.gob.mimp.sisdna.administrado;


import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.sisdna.fachada.SupervisionFacadeLocal;
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.modelo.Departamento;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.seguridad.modelo.Usuario;
import pe.gob.mimp.sisdna.fachada.CatalogoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.CursoProgramacionFacadeLocal;

/**
 * Clase: ReporteAdministrado.java <br>
 * Clase encargada de generar los reportes de capacitación y supervisión.<br>
 * Fecha de Creación: 12/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "reporteAdministrado")
@ViewScoped
public class ReporteAdministrado extends AdministradorAbstracto implements Serializable {

    private List<Object[]> lista;
    
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private BigInteger busEstado;
    private String busCodigo;
    private Date busDesde;
    private Date busHasta;
    private Integer busAnho;
    private BigDecimal busTutor;
    private BigDecimal busCurso;
    private Integer busEstadoEval;
    private BigInteger busEstadoDna;
    
    private BigInteger busOrigen;
    
    private Map<Integer,String> titulosReporte;
   
    private boolean verDepartamento;
    private boolean verProvincia;
    private boolean verDistrito;
    private boolean verAnho;
    private boolean verFechas;
    private boolean verEstadoSup;
    private boolean verEstadoEval;
    private boolean verEstadoDna;
    private boolean verOrigen;

    private boolean verCurso;
    private boolean verUsuario;
  
    private Integer reporte;
    private String tituloReporte;
    
    private String[] nombreCursos;
    
    @EJB
    private CursoProgramacionFacadeLocal cursoProgramacionFacade;

    @EJB
    private SupervisionFacadeLocal supervisionFacade;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    
    @EJB
    private CatalogoFacadeLocal catalogoFacade;
    
    
    @PostConstruct
    public void initLoad() {

       this.verDepartamento = false;
       this.verProvincia = false;
       this.verDistrito = false;
       this.verAnho = false;
       this.verFechas = false;
       this.verEstadoSup = false;
       this.verCurso = false;
       this.verUsuario = false;
       this.verEstadoDna = false;
       this.verOrigen = false;
      
       this.busDepartamento = null;
       this.busProvincia = null;
       this.busDistrito = null; 
       this.busCurso = null;
       this.busTutor = null;
       this.busEstadoDna = null;
       this.busOrigen = null;
       
       this.titulosReporte = new HashMap<>();
       this.titulosReporte.put(10,"Número de Defensores/as capacitados x Mes");
       this.titulosReporte.put(11,"Número de Defensores/as capacitados x Trimestre");
       this.titulosReporte.put(20,"Número de Defensores/as capacitados x Mes x Sexo x Curso");
       this.titulosReporte.put(21,"Número de Defensores/as capacitados x Trimestre x Sexo x Curso");
       this.titulosReporte.put(12,"Relación de Defensores/as capacitados ");
       this.titulosReporte.put(30,"Listado de DEMUNA fortalecidas");
      
       //( dep de origen del participante ) tutor y tipo de curso
         
       this.titulosReporte.put(50,"Observaciones de DNA supervisadas x Mes");
       this.titulosReporte.put(51,"Departamentos de DNA supervisadas x Mes");
       this.titulosReporte.put(52,"Relación de Supervisiones");
      
    }
    
    /**
     * devuelve el título del reporte de capacitación según su index
     * @param rep index del reporte
     * @return título del reporte
     */
    public String titulo(Integer rep) {
        return this.titulosReporte.get(rep);
    }
    
     public void seleccionarReporte() {
        
       this.tituloReporte = this.titulosReporte.get(this.reporte);
        
       this.verDepartamento = true;
       this.verProvincia = true;
       this.verDistrito = true;
       this.verFechas = true;
       this.verAnho = true;
       this.verUsuario = true;
       this.verEstadoEval = true;
                 
       this.resetLista();
       
       if(this.reporte<30) {
            this.verCurso = true;
            this.verEstadoSup = false;
       } else {
           this.verCurso = false;
           this.busCurso = null;
           this.verOrigen = true;
           this.verEstadoSup = true;
           this.verEstadoDna = true;
          
                
           if(this.reporte == 30) {
               this.verFechas = false;
               this.verEstadoSup = false;
               this.verEstadoDna = false;
               this.verAnho = false;
               this.verUsuario = false;
               this.verEstadoEval = false;
           } 
           if(this.reporte >= 51) {
               this.verEstadoEval = false;
               this.busEstadoEval = null;
           }
         
       }
       
     }
     
     /**
      * limpia el reporte
      */
    public void resetLista() {
        this.lista = null;
        
        if(this.busAnho != null) {
            this.verFechas = false;
            this.busDesde = null;
            this.busHasta = null;
        } else
            this.verFechas = true;
        
        if(this.busDesde!=null || this.busHasta!=null) {
            this.verAnho = false;
            this.busAnho = null;
        } else
            this.verAnho = true;
        
        
    }
     
     /**
     * genera el reporte según el filtro seleccionado
     */
    public void generar() {
        
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
      String fechaIni = null;
      String fechaFin = null;
      
      if(this.busDesde != null) 
           fechaIni = formatter.format(this.busDesde);  
      if(this.busHasta != null) 
           fechaFin = formatter.format(this.busHasta);  
 
       switch( this.reporte) {
            case 10: 
            case 11:
            case 12:
                this.lista = this.cursoProgramacionFacade.reporteCapacitacion(  this.reporte,
                                                                                this.busDepartamento, 
                                                                                this.busProvincia, 
                                                                                this.busDistrito,
                                                                                this.busAnho,
                                                                                fechaIni,
                                                                                fechaFin,
                                                                                this.busCurso,
                                                                                this.busTutor,
                                                                                this.busEstadoEval); 
                break;
            case 20:
            case 21:
                
                Map<String, Object> resp = this.cursoProgramacionFacade.reporteCapacitacionPorCurso(this.reporte,
                                                                                this.busDepartamento, 
                                                                                this.busProvincia, 
                                                                                this.busDistrito,
                                                                                this.busAnho,
                                                                                fechaIni,
                                                                                fechaFin,
                                                                                this.busCurso,
                                                                                this.busTutor,
                                                                                this.busEstadoEval);
                this.nombreCursos = ((String)resp.get("cursos")).split(",");
                this.lista = (List<Object[]>)(resp.get("lista")); 
                break;
           case 30: 
                this.lista = this.cursoProgramacionFacade.reporteFortalecidas(this.busDepartamento,  this.busProvincia,  this.busDistrito, 
                                                    fechaIni, fechaFin,this.busAnho);  
                break;
             
            default: 
                this.lista = this.supervisionFacade.reporte(    this.reporte,
                                                                this.busDepartamento, 
                                                                this.busProvincia, 
                                                                this.busDistrito,
                                                                fechaIni,  fechaFin,  this.busAnho,
                                                                this.busEstadoEval, this.busEstado, this.busEstadoDna,
                                                                this.busOrigen, this.busTutor );
                                                                
                                                                
        }
        
                  
    }
    
    /**
     * deriva hacia el reporte solicitado
     */
     public void exportar() {
        
        switch(this.reporte) {
            case 10: 
            case 11: this.exportarRepCapacitacion10(); break;
            case 12: this.exportarRepCapacitacion12(); break;
            case 20: // this.exportarRepCapacitacion20(); break;
            case 21: this.exportarRepCapacitacion20(); break;
            case 30: this.exportarRepCapacitacion30(); break;
            case 50: this.exportarRepSupervision50(); break;
            case 51: this.exportarRepSupervision51(); break;
            case 52: this.exportarRepSupervision52(); break;
            
        }
    }
     
     
    private Integer escribirFiltro(XSSFWorkbook workbook, XSSFSheet sheet, Integer num) {
        
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
       
        
        if(this.busDepartamento!=null) {
            Row row = sheet.createRow(num++);
            Departamento dep = this.departamentoFacade.find(this.busDepartamento);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Departamento:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(dep.getTxtDescripcion());           
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
        
        if(this.busAnho!=null) {
            Row row = sheet.createRow(num++);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Año:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(this.busAnho);            
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
        
        if(this.busCurso!=null) {
            
            CursoAdministrado cursoAdministrado = (CursoAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{cursoAdministrado}", CursoAdministrado.class);
    
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Curso:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(cursoAdministrado.obtenerNombreCurso(this.busCurso));
        }
    
        if(this.busTutor!=null) {
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            
            if(this.reporte<50)
                cell.setCellValue("Tutor:");    
            else
                cell.setCellValue("Supervisor:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            
            Usuario u = usuarioAdministrado.getEntidad(this.busTutor.toPlainString());
            cell.setCellValue(u.getPersona().getTxtNombres());
        }
    
        if(this.busEstadoEval!=null) {
            
            
            Row row = sheet.createRow(num++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("Calificación:");    
        
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue((this.busEstadoEval==1)?"APROBADO":(this.busEstadoEval==2)?"DESAPROBADO":"PENDIENTE");
        }
          
    
        return num++;
    }
    
    private void escribirLista(XSSFWorkbook workbook, XSSFSheet sheet, Integer rowNum, boolean tieneTotal) {
       this. escribirLista(workbook, sheet,  rowNum, tieneTotal, null);
    }
    
    private void escribirLista(XSSFWorkbook workbook, XSSFSheet sheet, Integer rowNum, boolean tieneTotal, Integer remover) {
        
        XSSFFont fontCell = workbook.createFont();
        fontCell.setFontHeightInPoints((short) 10);
        
        XSSFCellStyle styleCell = workbook.createCellStyle();
        styleCell.setFont(fontCell);
        styleCell.setWrapText(true);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setVerticalAlignment(VerticalAlignment.CENTER);
       

        XSSFFont fontCellBold = workbook.createFont();
        fontCellBold.setFontHeightInPoints((short) 10);
        fontCellBold.setBold(true);
        
        XSSFCellStyle styleCellBold = workbook.createCellStyle();
        styleCellBold.setFont(fontCellBold);
        styleCellBold.setWrapText(true); 
        styleCellBold.setBorderTop(BorderStyle.THIN);
        
        XSSFCellStyle styleCellFinal = workbook.createCellStyle();
        styleCellFinal.setFont(fontCell);
        styleCellFinal.setWrapText(true); 
        styleCellFinal.setBorderBottom(BorderStyle.THIN);
        styleCellFinal.setBorderLeft(BorderStyle.THIN);
        styleCellFinal.setBorderRight(BorderStyle.THIN);
       
        int x = 0;
        rowNum++;
        for (Object[] datatype : this.lista) {
            
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
           
            for (Object field : datatype) {
                 if(remover==null || colNum<= (datatype.length-remover-1) ) {
                    Cell cell = row.createCell(colNum);
                    if(tieneTotal && x == this.lista.size()-1)
                        cell.setCellStyle(styleCellBold);
                    else if(!tieneTotal && x == this.lista.size()-1) {
                        cell.setCellStyle(styleCellFinal);
                    } else
                        cell.setCellStyle(styleCell);
                    
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    } else if (field instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) field).doubleValue());
                    }
                 }
                 colNum++;   
            }
            x++;
                  
        }
        
        this.escribirFooter(sheet, rowNum );

    }
    
    /**
     * escribe el titulo
     * @param workbook
     * @param sheet
     * @param tituloRep
     * @param totalColCombinadas 
     */
    private void escribirTitulo(XSSFWorkbook workbook, XSSFSheet sheet, String tituloRep, Integer totalColCombinadas) {
      
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 16);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        
        Row row = sheet.createRow(0);
        row.setHeight((short)1000);
        Cell cellTitulo = row.createCell(0);
        cellTitulo.setCellStyle(style);
        cellTitulo.setCellValue(tituloRep);    
        
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,totalColCombinadas));       
      
    }
    
    
    /**
     * escribe y estiliza la cabecera
     * @param workbook
     * @param sheet
     * @param header
     * @param rowNum
     * @param colNum
     * @param ancho
     * @param numFilasCombinadas
     * @param numColCombinadas 
     */
    private void escribirHeader(XSSFWorkbook workbook, XSSFSheet sheet, String header, Integer rowNum, Integer colNum, 
                                                            Integer ancho, Integer numFilasCombinadas, Integer numColCombinadas)  {
        
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

        Row row = sheet.getRow(rowNum);
        
        if(row == null)
           row = sheet.createRow(rowNum);
        
        sheet.setColumnWidth(colNum, ancho); 
        Cell cellHeader = row.createCell(colNum);
        cellHeader.setCellStyle(styleHeader);
        cellHeader.setCellValue(header);
      
        if(numFilasCombinadas!=null && numFilasCombinadas>0) {
            for(int x=1; x<=numFilasCombinadas-1; x++ ) {
                
                row = sheet.getRow(rowNum+x);
                if(row == null)
                   row = sheet.createRow(rowNum+x); 
                
                cellHeader = row.createCell(colNum);
                cellHeader.setCellStyle(styleHeader);
                cellHeader.setCellValue("");
             }
            
            if(numColCombinadas==null) 
                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum + numFilasCombinadas-1, colNum, colNum));
            
        }
        
        if(numColCombinadas!=null && numColCombinadas>0) {
            for(int x=1; x<=numColCombinadas-1; x++ ) {
                row = sheet.getRow(rowNum);
                cellHeader = row.createCell(colNum+x);
                cellHeader.setCellStyle(styleHeader);
                cellHeader.setCellValue("");
                if(numFilasCombinadas!=null && numFilasCombinadas>0) {
                    for(int y=1; y<=numFilasCombinadas-1; y++ ) {

                        Row rowy = sheet.getRow(rowNum+y);
                        if(rowy == null)
                           rowy = sheet.createRow(rowNum+y); 

                        cellHeader = rowy.createCell(colNum+x);
                        cellHeader.setCellStyle(styleHeader);
                     }
                }
            }
            
            if(numFilasCombinadas==null) 
                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, colNum, colNum + numColCombinadas-1));
            
        }
        
        if(numFilasCombinadas!=null && numColCombinadas!=null )
            sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+ numFilasCombinadas-1, colNum , colNum + numColCombinadas-1));

    }
    
    /**
     * genera el response
     * @param nomArchivo
     * @return 
     */
    private HttpServletResponse getResponse(String nomArchivo) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response =  (HttpServletResponse)context.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+nomArchivo+".xlsx");
        return response;
    }
    
    /**
     * escribe las fuentes en el footer
     * @param sheet
     * @param rowNum 
     */
    private void escribirFooter(XSSFSheet sheet, Integer rowNum) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");  
        Row row = sheet.createRow(rowNum + 2);
        Cell cell = row.createCell(0);
        cell.setCellValue("Fuente: Base de datos de la DSLD - DGNNA - MIMP");
        row = sheet.createRow(rowNum + 3);
        cell = row.createCell(0);
        cell.setCellValue("Fecha de reporte: " + dateFormat.format(new Date()));

    }

    /**
     * exporta reporte de capacitación
     */
    private void exportarRepCapacitacion10() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte),8);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        this.escribirHeader(workbook, sheet, "SEDE", rowNum, colNum++, 4000, null, null);
        this.escribirHeader(workbook, sheet, "FECHA DE INICIO", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "FECHA DE FIN", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "DEPARTAMENTOS PARTICIPANTES", rowNum, colNum++, 8000, null, null);
        this.escribirHeader(workbook, sheet, "MASCULINO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "FEMENINO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "TOTAL", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "TUTOR", rowNum, colNum++, 7000, null, null);
        this.escribirHeader(workbook, sheet, "TIPO CURSO", rowNum, colNum++, 4000, null, null);
        
               
        XSSFFont fontCell = workbook.createFont();
        fontCell.setFontHeightInPoints((short) 10);
        
        XSSFCellStyle styleCell = workbook.createCellStyle();
        styleCell.setFont(fontCell);
        styleCell.setWrapText(true);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setVerticalAlignment(VerticalAlignment.CENTER);
      

        XSSFFont fontCellBold = workbook.createFont();
        fontCellBold.setFontHeightInPoints((short) 12);
        fontCellBold.setBold(true);
        
        XSSFCellStyle styleCellBold = workbook.createCellStyle();
        styleCellBold.setFont(fontCellBold);
        styleCellBold.setWrapText(true); 
        styleCellBold.setBorderLeft(BorderStyle.THIN);
        styleCellBold.setBorderRight(BorderStyle.THIN);
        styleCellBold.setBorderTop(BorderStyle.THIN);
        styleCellBold.setBorderBottom(BorderStyle.THIN);
        
        rowNum++;
        for (Object[] datatype : this.lista) {
            
            Row row = sheet.createRow(rowNum++);
            colNum = 0;
            
            for (int index=1; index<datatype.length-3;index++) {
                
                    Cell cell = row.createCell(colNum);
                    
                    if(index==1 && datatype[index]!=null &&  ((String)datatype[index]).equals("**subtotal**")) {
                        cell.setCellStyle(styleCellBold);
                        cell.setCellValue((String) datatype[0]);
                        cell = row.createCell(1);
                        cell.setCellStyle(styleCellBold);
                        cell = row.createCell(2);
                        cell.setCellStyle(styleCellBold);
                        cell = row.createCell(3);
                        cell.setCellStyle(styleCellBold);
                        sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1, 0, 3));
                        colNum = colNum +4;
                        index = index + 3;
                    } else {
                        if(((String)datatype[1]).equals("**subtotal**")) 
                            cell.setCellStyle(styleCellBold);
                        else 
                            cell.setCellStyle(styleCell);
                        
                        if (datatype[index] instanceof String) {
                            cell.setCellValue((String) datatype[index]);
                        } else if (datatype[index] instanceof Integer) {
                            cell.setCellValue((Integer) datatype[index]);
                        } else if (datatype[index] instanceof BigDecimal) {
                            cell.setCellValue(((BigDecimal) datatype[index]).doubleValue());
                        }
                        colNum++;   
                    }
                
            }
                  
        }
        
        this.escribirFooter(sheet, rowNum );
      
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
 
    /**
     * exporta reporte de capacitación
     */
     private void exportarRepCapacitacion12() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte),15);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        this.escribirHeader(workbook, sheet, "TIPO DE DOCUMENTO", rowNum, colNum++, 3000, null, null);
        this.escribirHeader(workbook, sheet, "NÚMERO DE DOCUMENTO", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "APELLIDO PATERNO", rowNum, colNum++, 3000, null, null);
        this.escribirHeader(workbook, sheet, "APELLIDO MATERNO", rowNum, colNum++, 3000, null, null);
        this.escribirHeader(workbook, sheet, "NOMBRES", rowNum, colNum++, 3000, null, null);
        this.escribirHeader(workbook, sheet, "DNA", rowNum, colNum++,14000, null, null);
        this.escribirHeader(workbook, sheet, "ORIGEN", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "DEPARTAMENTO", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "PROVINCIA", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "DISTRITO", rowNum, colNum++, 6000, null, null);
        this.escribirHeader(workbook, sheet, "CURSO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "CIUDAD", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "FECHA DE INICIO", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "FECHA DE FIN", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "CALIFICACIÓN", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "TUTOR", rowNum, colNum++, 7000, null, null);
        
        this.escribirLista(workbook, sheet, rowNum, false);
        
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
 
        
    public void exportarRepCapacitacion20() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte), 1 + this.nombreCursos.length);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        if(this.reporte == 20 )
            this.escribirHeader(workbook, sheet, "TOTAL X MES X SEXO", rowNum, colNum++, 5000, null, null);
        else 
            this.escribirHeader(workbook, sheet, "TOTAL X TRIMESTRE X SEXO", rowNum, colNum++, 5000, null, null);
        
        this.escribirHeader(workbook, sheet, "TOTAL", rowNum, colNum++, 2500, null, null);
        
        for(String h: this.nombreCursos) 
            this.escribirHeader(workbook, sheet, h, rowNum, colNum++, 2500, null, null);
        
        XSSFFont fontCell = workbook.createFont();
        fontCell.setFontHeightInPoints((short) 10);
        
        XSSFCellStyle styleCell = workbook.createCellStyle();
        styleCell.setFont(fontCell);
        styleCell.setWrapText(true);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setVerticalAlignment(VerticalAlignment.CENTER);
      

        XSSFFont fontCellBold = workbook.createFont();
        fontCellBold.setFontHeightInPoints((short) 12);
        fontCellBold.setBold(true);
        
        XSSFCellStyle styleCellBold = workbook.createCellStyle();
        styleCellBold.setFont(fontCellBold);
        styleCellBold.setWrapText(true); 
        styleCellBold.setBorderLeft(BorderStyle.THIN);
        styleCellBold.setBorderRight(BorderStyle.THIN);
        styleCellBold.setBorderTop(BorderStyle.THIN);
        styleCellBold.setBorderBottom(BorderStyle.THIN);
        
        rowNum++;
        
        int indexInicio = this.reporte-19;
        
        for (Object[] datatype : this.lista) {
            
            Row row = sheet.createRow(rowNum++);
            colNum = 0;
            
            for (int index=indexInicio; index<datatype.length;index++) {
                
                    Cell cell = row.createCell(colNum);
                    
                    if(index==indexInicio && datatype[index]!=null &&  ((String)datatype[index]).equals("**")) {
                        cell.setCellStyle(styleCellBold);
                        cell.setCellValue((String) datatype[indexInicio-1]);
                    } else {
                        if(((String)datatype[indexInicio]).equals("**")) 
                            cell.setCellStyle(styleCellBold);
                        else
                            cell.setCellStyle(styleCell);
                        if (datatype[index] instanceof String) {
                            cell.setCellValue((String) datatype[index]);
                        } else if (datatype[index] instanceof Integer) {
                            cell.setCellValue((Integer) datatype[index]);
                        } else if (datatype[index] instanceof BigDecimal) {
                            cell.setCellValue(((BigDecimal) datatype[index]).doubleValue());
                        }
                       
                    }
                  colNum++;  
            }
                  
        }
        
        this.escribirFooter(sheet, rowNum );
      
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
   
    
    public void exportarRepCapacitacion30() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte),9);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        this.escribirHeader(workbook, sheet, "N°", rowNum, colNum++, 1000, null, null);
        this.escribirHeader(workbook, sheet, "SEDE", rowNum, colNum++, 3000, null, null);
        this.escribirHeader(workbook, sheet, "DEMUNA", rowNum, colNum++, 17000, null, null);
        this.escribirHeader(workbook, sheet, "REGISRO / INSCRIPCIÓN", rowNum, colNum++, 3000, null, null);
        this.escribirHeader(workbook, sheet, "DEPARTAMENTO", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "PROVINCIA", rowNum, colNum++, 4500, null, null);
        this.escribirHeader(workbook, sheet, "DISTRITO", rowNum, colNum++, 5000, null, null);
        this.escribirHeader(workbook, sheet, "TIPO", rowNum, colNum++, 4000, null, null);
        this.escribirHeader(workbook, sheet, "MES", rowNum, colNum++, 3500, null, null);
        this.escribirHeader(workbook, sheet, "MESx", rowNum, colNum++, 2500, null, null);
       
        this.escribirLista(workbook, sheet, rowNum, false);
        
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
      
    public void exportarRepSupervision50() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte),12);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        this.escribirHeader(workbook, sheet, "OBSERVACIONES", rowNum, colNum++, 18000, null, null);
        this.escribirHeader(workbook, sheet, "ENERO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "FEBRERO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "MARZO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "ABRIL", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "MAYO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "JUNIO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "JULIO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "AGOSTO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "SETIEMBRE", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "OCTUBRE", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "NOVIEMBRE", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "DICIEMBRE", rowNum, colNum++, 2500, null, null);
        
               
        this.escribirLista(workbook, sheet, rowNum, false);
        
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    public void exportarRepSupervision51() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte),13);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        this.escribirHeader(workbook, sheet, "DEPARTAMENTO", rowNum, colNum++, 5000, null, null);
        this.escribirHeader(workbook, sheet, "TOTAL", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "ENERO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "FEBRERO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "MARZO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "ABRIL", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "MAYO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "JUNIO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "JULIO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "AGOSTO", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "SETIEMBRE", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "OCTUBRE", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "NOVIEMBRE", rowNum, colNum++, 2500, null, null);
        this.escribirHeader(workbook, sheet, "DICIEMBRE", rowNum, colNum++, 2500, null, null);
        
               
        this.escribirLista(workbook, sheet, rowNum, true);
        
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
      
   public void exportarRepSupervision52() {
        
        
        HttpServletResponse response =  this.getResponse("reporte");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("REPORTE");
      
        this.escribirTitulo(workbook, sheet, this.titulosReporte.get(this.reporte),8);
        
        int rowNum = 2; int colNum = 0;
  
        rowNum = this.escribirFiltro(workbook, sheet, rowNum);
        
        this.escribirHeader(workbook, sheet, "FECHA", rowNum, colNum++, 4000, null, null);
        this.escribirHeader(workbook, sheet, "ESTADO DE SUPERVISIÓN", rowNum, colNum++, 5000, null, null);
        this.escribirHeader(workbook, sheet, "DEPARTAMENTO", rowNum, colNum++, 5000, null, null);
        this.escribirHeader(workbook, sheet, "PROVINCIA", rowNum, colNum++, 5000, null, null);
        this.escribirHeader(workbook, sheet, "DISTRITO", rowNum, colNum++, 5000, null, null);
        this.escribirHeader(workbook, sheet, "NOMBRE DE DNA", rowNum, colNum++, 10000, null, null);
        this.escribirHeader(workbook, sheet, "TIPO DE DNA", rowNum, colNum++, 4000, null, null);
        this.escribirHeader(workbook, sheet, "ESTADO DE DNA", rowNum, colNum++, 4000, null, null);
        this.escribirHeader(workbook, sheet, "SUPERVISOR", rowNum, colNum++, 7000, null, null);
               
        this.escribirLista(workbook, sheet, rowNum, false);
        
        try {
             workbook.write(response.getOutputStream());
        } catch (IOException e) {
        
        }
        FacesContext.getCurrentInstance().responseComplete();
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
    
   

    public List<Object[]> getLista() {
        return lista;
    }

    public void setLista(List<Object[]> lista) {
        this.lista = lista;
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

    public String getBusCodigo() {
        return busCodigo;
    }

    public void setBusCodigo(String busCodigo) {
        this.busCodigo = busCodigo;
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

    public Integer getBusAnho() {
        return busAnho;
    }

    public void setBusAnho(Integer busAnho) {
        this.busAnho = busAnho;
    }

   
    public boolean isVerDepartamento() {
        return verDepartamento;
    }

    public void setVerDepartamento(boolean verDepartamento) {
        this.verDepartamento = verDepartamento;
    }

    public boolean isVerProvincia() {
        return verProvincia;
    }

    public void setVerProvincia(boolean verProvincia) {
        this.verProvincia = verProvincia;
    }

    public boolean isVerDistrito() {
        return verDistrito;
    }

    public void setVerDistrito(boolean verDistrito) {
        this.verDistrito = verDistrito;
    }

    public boolean isVerAnho() {
        return verAnho;
    }

    public void setVerAnho(boolean verAnho) {
        this.verAnho = verAnho;
    }

    public boolean isVerFechas() {
        return verFechas;
    }

    public void setVerFechas(boolean verFechas) {
        this.verFechas = verFechas;
    }

    public boolean isVerEstado() {
        return verEstadoSup;
    }

    public void setVerEstado(boolean verEstado) {
        this.verEstadoSup = verEstado;
    }

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }

    public Integer getReporte() {
        return reporte;
    }

    public void setReporte(Integer reporte) {
        this.reporte = reporte;
    }

    public Map<Integer, String> getTitulosReporte() {
        return titulosReporte;
    }

    public void setTitulosReporte(Map<Integer, String> titulosReporte) {
        this.titulosReporte = titulosReporte;
    }

    public String[] getNombreCursos() {
        return nombreCursos;
    }

    public void setNombreCursos(String[] nombreCursos) {
        this.nombreCursos = nombreCursos;
    }

    public BigDecimal getBusTutor() {
        return busTutor;
    }

    public void setBusTutor(BigDecimal busTutor) {
        this.busTutor = busTutor;
    }

    public BigDecimal getBusCurso() {
        return busCurso;
    }

    public void setBusCurso(BigDecimal busCurso) {
        this.busCurso = busCurso;
    }

    public BigInteger getBusOrigen() {
        return busOrigen;
    }

    public void setBusOrigen(BigInteger busOrigen) {
        this.busOrigen = busOrigen;
    }

    public boolean isVerCurso() {
        return verCurso;
    }

    public void setVerCurso(boolean verCurso) {
        this.verCurso = verCurso;
    }

    public boolean isVerUsuario() {
        return verUsuario;
    }

    public void setVerUsuario(boolean verUsuario) {
        this.verUsuario = verUsuario;
    }

    public Integer getBusEstadoEval() {
        return busEstadoEval;
    }

    public void setBusEstadoEval(Integer busEstadoEval) {
        this.busEstadoEval = busEstadoEval;
    }

    public boolean isVerEstadoSup() {
        return verEstadoSup;
    }

    public void setVerEstadoSup(boolean verEstadoSup) {
        this.verEstadoSup = verEstadoSup;
    }

    public boolean isVerEstadoEval() {
        return verEstadoEval;
    }

    public void setVerEstadoEval(boolean verEstadoEval) {
        this.verEstadoEval = verEstadoEval;
    }

    public BigInteger getBusEstadoDna() {
        return busEstadoDna;
    }

    public void setBusEstadoDna(BigInteger busEstadoDna) {
        this.busEstadoDna = busEstadoDna;
    }

    public boolean isVerEstadoDna() {
        return verEstadoDna;
    }

    public void setVerEstadoDna(boolean verEstadoDna) {
        this.verEstadoDna = verEstadoDna;
    }

    public boolean isVerOrigen() {
        return verOrigen;
    }

    public void setVerOrigen(boolean verOrigen) {
        this.verOrigen = verOrigen;
    }

  
    
    
}
