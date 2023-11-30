/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.service.impl;

import com.tienda.service.ReporteService;

import jakarta.mail.Quota;
import jakarta.mail.Quota.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReporteServicempl  implements ReporteService{
 @Autowired
     private DataSource dataSource;
    @Override
    public ResponseEntity<org.springframework.core.io.Resource> generaReporte(String reporte, Map<String, Object> parametros, String tipo) throws IOException {
           String estilo = tipo.equalsIgnoreCase("vPdf")?
                "inline; ":"attachment; ";
        //Se establece la ruta de los reportes
        String reportePath = "reportes";
        
        //Se define la salida temportal del reporte generado
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        
        //Se establece la fuenta para leeer el reporte .jasper
        ClassPathResource fuente = new ClassPathResource(
        reportePath+
                File.separator+
                reporte+
                ".jasper");
                //Se define el objeto que lee el archivo de reporte .jasper
                InputStream elReporte = fuente.getInputStream();
                
                try {
            //Se genera el reporte en memoria
                var reporteJasper = JasperFillManager
                        .fillReport(elReporte, 
                                parametros, 
                                dataSource.getConnection());
                
                //Inicia la respuesta al usuario a partir de aca
                MediaType mediaType = null;
                 String archivoSalida= "";
                
                //Se debe decidir cual tipo de reporte se genera
                switch(tipo){
                    case "Pdf", "vPdf"->{
                    //Se genera reporte pdf
                        JasperExportManager
                                .exportReportToPdfStream(reporteJasper, salida);
                        mediaType = MediaType.APPLICATION_PDF;
                        archivoSalida = reporte+".pdf";
                    }
                    case "Xls"->{
                       //Se desarga en excel
                       JRXlsxExporter paraExcel = new  JRXlsxExporter();
                       paraExcel.setExporterInput(
                       new SimpleExporterInput(reporteJasper)
                       );
                       
                       paraExcel.setExporterOutput(
                       new SimpleOutputStreamExporterOutput(salida)
                       );
                       
                        SimpleXlsxReportConfiguration configuracion = 
                                new SimpleXlsxReportConfiguration();
                        
                        configuracion.setDetectCellType(true);
                        configuracion.setCollapseRowSpan(true);
                        
                        paraExcel.setConfiguration(configuracion);
                        paraExcel.exportReport();
                        mediaType = MediaType.APPLICATION_OCTET_STREAM;
                        archivoSalida = reporte+".xlsx";
                    }
                    case "Csv" -> {
                    //Se descarga en CSv
                     JRCsvExporter paraCsv = new  JRCsvExporter();
                      paraCsv.setExporterInput(
                       new SimpleExporterInput(reporteJasper)
                       );
                       
                       paraCsv.setExporterOutput(
                       new SimpleWriterExporterOutput(salida)
                       );
                       
                     paraCsv.exportReport();
                     mediaType = MediaType.TEXT_PLAIN;
                     archivoSalida = reporte+".CSV";
                    }
                }
                //A partir de aca se realiza la respuesta al usuario
                byte[]data = salida.toByteArray();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Disposition", 
                        estilo+"filename=\""+archivoSalida+"\"");
                
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentLength(data.length)
                        .contentType(mediaType)
                        .body(new InputStreamResource(new ByteArrayInputStream(data)));
                        
                
        } catch (JRException|SQLException ex) {
            ex.printStackTrace();
   
        } 
                
                  return null;   
   
    }


    
    
    
}
