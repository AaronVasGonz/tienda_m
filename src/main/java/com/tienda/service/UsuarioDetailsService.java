/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tienda.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
/**
 *
 * @author arjoz
 */
public interface UsuarioDetailsService {
    public UserDetails loadUserByUsername(String username) throws 
            UsernameNotFoundException;
    
}
