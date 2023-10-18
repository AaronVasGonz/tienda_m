package com.tienda.service;


import com.tienda.domain.Categoria;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author arjoz
 */
public interface CategoriaService {
    public List<Categoria> getCategorias(boolean Activo);
}
