package pe.gob.mimp.sisdna.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

/**
 * Clase PdfUtil.java
 * Clase que contiene las funciones para generar y descargar PDF
 * @author BooleanCore
 */
public class PdfUtil {
    
    public static void crearPDF(Map<String,Object> params , String jasperPath , List<?> dataSource , String fileName ) throws JRException , IOException{
        String relativeWebPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperPath);
        String imagePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(ConstantesReporte.IMAGEN_PATH);
        InputStream is = new FileInputStream(imagePath);
        params.put("logo", is);
        File file = new File(relativeWebPath);
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dataSource,false);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params , source);
        
        // enviar a web
        HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename="+fileName);
        ServletOutputStream stream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(print, stream);
        FacesContext.getCurrentInstance().responseComplete();
    }
    
   
     public static byte[] downloadPDF(Map<String,Object> params , String jasperPath , List<?> dataSource ) throws JRException , IOException{
        String relativeWebPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperPath);
        File file = new File(relativeWebPath);
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dataSource,false);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params , source);
        return JasperExportManager.exportReportToPdf(print);
    }
     
       public static byte[] downloadPDF(Map<String,Object> params , String jasperPath , Map<String, Object> dataSource ) throws JRException , IOException{
        String relativeWebPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperPath);
        File file = new File(relativeWebPath);
        JRMapArrayDataSource source = new JRMapArrayDataSource(new Object[]{dataSource});
 
        //JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dataSource,false);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params , source);
        return JasperExportManager.exportReportToPdf(print);
    }
    public static void generarFilePDF(Map<String,Object> params , String jasperPath , List<?> dataSource , String fileName , String rutaGuardar) throws JRException , IOException{
        String relativeWebPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperPath);
        File file = new File(relativeWebPath);
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dataSource,false);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params , source);
        
        // generar archivo PDF
        OutputStream output = new FileOutputStream(new File(rutaGuardar,fileName)); 
        JasperExportManager.exportReportToPdfStream(print, output); 
       
    }
}
