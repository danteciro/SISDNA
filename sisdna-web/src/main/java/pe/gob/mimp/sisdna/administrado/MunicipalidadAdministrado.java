package pe.gob.mimp.sisdna.administrado;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import jxl.CellType;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Departamento;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.MunicipalidadFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.modelo.Municipalidad;

/**
 * Clase: MunicipalidadAdministrado.java <br>
 * Clase encargada de gestionar las municipalidades.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "municipalidadAdministrado")
@ViewScoped
public class MunicipalidadAdministrado extends AdministradorAbstracto implements Serializable{

   // private static final Logger // LOGGER = LogManager.getLogger(MunicipalidadAdministrado.class.getName());

    private Municipalidad municipalidad;
 
    private List<Municipalidad> listaMunicipalidad;
     
    private byte[] archivo;
  
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
 
   @EJB
    private MunicipalidadFacadeLocal municipalidadFacade;
   
    @EJB
    private DepartamentoFacadeLocal departamentoFacade;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @PostConstruct
    public void initLoad() {
  
    }
    
    
    
    public void obtener(BigInteger nidDna) {
        this.municipalidad = this.municipalidadFacade.find(nidDna);
    }
    
     /**
     * Devuelve la lista de departamentos
     * @return lista de departamentos
     */
    public List<Departamento> obtenerDepartamentos() {
        this.busProvincia = null;
        this.busDistrito = null;
        return  this.departamentoFacade.obtenerDepartamentos();
    }
    
   /**
     * Devuelve la lista de provincias según el departamento
     * @return lista de provincias
     */
     public List<Provincia> obtenerProvinciasBus() {
        this.busDistrito = null;
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.busDepartamento);
        return this.provinciaFacade.obtenerProvincias(departamento);
    }
    
     /**
      * Devuelve la lista de distritos según la provincia
     * @return lista de distritos
      */
    public List<Distrito> obtenerDistritosBus() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.busProvincia);
        return this.distritoFacade.obtenerDistritos(provincia);
    }
    
    
   /**
     * busqueda de la municipalidad por departamento, provincia y distrito
     */
    public void buscar() {
        
        ParamFiltro param = new ParamFiltro();

        if(this.busDepartamento!=null) 
            param.adicionar("nidDepartamento", this.busDepartamento);

        if(this.busProvincia!=null)
            param.adicionar("nidProvincia", this.busProvincia);

        if(this.busDistrito!=null)
            param.adicionar("nidDistrito", this.busDistrito);

        if(param.getParametros().size()>0) 
           this.listaMunicipalidad = this.municipalidadFacade.obtenerPorFiltro(param, true, "ubigeo", false);

       
    }
    /**
     * inicializa la carga
     */
    public void nuevaCarga() {
        this.archivo = null;
    }
       /**
     * permite subir el contenido en bytes del excel
     * @param event - FileUploadEvent - evento generado al subir el archivo
     */
    public void upload(FileUploadEvent event) {
        
        try {
         
            this.archivo = IOUtils.toByteArray(event.getFile().getInputstream());
            adicionarMensaje("", "Carga de Archivo: " + event.getFile().getFileName() + " terminado");
           
        } catch(Exception ex) {
            adicionarMensajeWarning("", "Error al subir archivo");
            // LOGGER.error("Error al subir el archivo",ex);
        }
    }
  
 
     /**
     * Realiza la importación de las Municipalidades desde archivo Excel
     */
    public void importar() {
    
        try {
            // elimina primero todos los registros de las municipalidades
            this.municipalidadFacade.deleteAll();
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);

            XSSFWorkbook workbook =  new XSSFWorkbook(new ByteArrayInputStream(this.archivo));
             
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            int r = 0;
            for (Row row: sheet) {
             
               if(r>4) {
                    Municipalidad m = new Municipalidad();

                    int i = 0;
                    for(Cell cell: row) {
                        switch(i) {
                            case 0: 
                                m.setUbigeo(cell.getStringCellValue());
                                m.setNidDepartamento(BigDecimal.valueOf(Long.valueOf(m.getUbigeo().substring(0,2))));

                                List<Provincia> provincias = this.provinciaFacade.findAllByField("cidProvincia", m.getUbigeo().substring(0,4));
                                m.setNidProvincia(provincias.get(0).getNidProvincia());

                                List<Distrito> distritos = this.distritoFacade.findAllByField("cidDistrito", m.getUbigeo());
                                m.setNidDistrito(distritos.get(0).getNidDistrito());

                                break;
                            case 4:    
                                m.setCargo(cell.getStringCellValue());
                                break;
                            case 5:    
                                m.setNombre(cell.getStringCellValue());
                                break;
                            case 6:       
                                m.setSexo(cell.getStringCellValue());
                                break;
                            case 7:       
                                m.setDireccion(cell.getStringCellValue());
                                break;
                            case 8:       
                                m.setCodigo(cell.getStringCellValue());
                                break;    
                            case 9: 
                               switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_NUMERIC:
                                                  m.setTelefono(String.valueOf(cell.getNumericCellValue()));
                                                  break;
                                    case Cell.CELL_TYPE_STRING:
                                                  m.setTelefono(cell.getStringCellValue());
                                                  break;
}
                                
                                break;    
                            case 10:       
                                m.setCorreo(cell.getStringCellValue());
                                break;    

                        }
                        i++;
                    }
                   if(m.getNidDepartamento()!=null && m.getNidProvincia()!=null && m.getNidDistrito()!=null) {
                        m.setFlgActivo(BigInteger.ONE);
                        m.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                        m.setTxtPc(Internet.obtenerNombrePC());
                        m.setTxtIp(Internet.obtenerIPPC());
                        m.setFecRegistro(new Date());

                        this.municipalidadFacade.create(m);
                   }
               }
               r++;
            }
            this.buscar();
            
            adicionarMensaje("","La importación se realizó con éxito");
            
        } catch (Exception ex) {
            
             adicionarMensajeWarning("", "Ocurrió un error al importar las municipalidades. Por favor, intente nuevamente.");
           // LOGGER.error(ex);
        } 

    }

    public Municipalidad getMunicipalidad() {
        return municipalidad;
    }

    public void setMunicipalidad(Municipalidad municipalidad) {
        this.municipalidad = municipalidad;
    }

    public List<Municipalidad> getListaMunicipalidad() {
        return listaMunicipalidad;
    }

    public void setListaMunicipalidad(List<Municipalidad> listaMunicipalidad) {
        this.listaMunicipalidad = listaMunicipalidad;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
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
    
    
    
}