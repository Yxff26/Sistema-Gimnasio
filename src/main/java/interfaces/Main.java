/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme;

public class Main {
    public static void main(String[] args) {
        
        // 1. Inicializar el tema moderno ANTES de abrir cualquier ventana
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatMaterialDesignDarkIJTheme());
        } catch (Exception ex) {
            System.err.println("No se pudo iniciar el tema FlatLaf: " + ex.getMessage());
        }

        // 2. Ejecutar la ventana de Login de forma segura
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLogin ventanaLogin = new FrmLogin();
                ventanaLogin.setLocationRelativeTo(null); // Esto centra la ventana en la pantalla
                ventanaLogin.setVisible(true); // Esto la hace visible
            }
        });
    }
}