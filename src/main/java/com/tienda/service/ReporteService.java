/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tienda.service;


import java.util.Map;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import org.springframework.core.io.Resource;

/**
 *
 * @author arjoz
 */
public interface ReporteService {
    public ResponseEntity<Resource> generaReporte(
    String reporte,
    Map<String, Object> parametros,
    String tipo) throws IOException;
    
}
