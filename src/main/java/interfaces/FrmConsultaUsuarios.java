/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author yefry
 */
public class FrmConsultaUsuarios extends javax.swing.JFrame {

    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
 
    // Índices de columnas para referenciarlas en los filtros
    private static final int COL_LOGIN = 0;
    private static final int COL_NIVEL = 1;
    private static final int COL_NOMBRE = 2;
    private static final int COL_APELLIDOS = 3;
    private static final int COL_CORREO = 4;
 
    public FrmConsultaUsuarios() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        // --- PLACEHOLDERS (FlatLaf) en los campos de filtro ---
        txtFiltroLogin.putClientProperty("JTextField.placeholderText", "Filtrar por login");
        txtFiltroNombre.putClientProperty("JTextField.placeholderText", "Filtrar por nombre");
        txtFiltroApellidos.putClientProperty("JTextField.placeholderText", "Filtrar por apellidos");
        txtFiltroCorreo.putClientProperty("JTextField.placeholderText", "Filtrar por correo");
 
        // Vaciar los campos para que se vea el placeholder (NetBeans los deja
        // con texto de ejemplo "jTextField1" que tapa el placeholder)
        txtFiltroLogin.setText("");
        txtFiltroNombre.setText("");
        txtFiltroApellidos.setText("");
        txtFiltroCorreo.setText("");
 
        configurarTabla();
        configurarComboNivel();
        cargarDatosUsuarios();
        actualizarConteo(); // muestra el total inicial
    }
 
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Login", "Nivel Acceso", "Nombre", "Apellidos", "Correo"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla estrictamente de consulta (no editable)
            }
        };
 
        tblUsuarios.setModel(modeloTabla);
 
        sorter = new TableRowSorter<>(modeloTabla);
        tblUsuarios.setRowSorter(sorter);
    }
 
    private void configurarComboNivel() {
        cmbNivelAcceso.removeAllItems();
        cmbNivelAcceso.addItem("Todos");
        cmbNivelAcceso.addItem("Administrador");
        cmbNivelAcceso.addItem("Socio");
        cmbNivelAcceso.setSelectedIndex(0);
    }
 
    private void cargarDatosUsuarios() {
        modeloTabla.setRowCount(0); // Limpiar filas anteriores
        File archivo = new File("usuarios.txt");
 
        if (!archivo.exists()) {
            return;
        }
 
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
 
                // Estructura: 0:Login, 1:Pass, 2:Nivel_Acceso (0,1), 3:Nombre, 4:Apellidos, 5:Correo
                if (datos.length >= 6) {
                    String login = datos[0].trim();
                    String nivelCod = datos[2].trim();
                    String nombre = datos[3].trim();
                    String apellidos = datos[4].trim();
                    String correo = datos[5].trim();
 
                    String nivelTexto = nivelCod.equals("0") ? "Administrador" : "Socio";
 
                    modeloTabla.addRow(new Object[]{login, nivelTexto, nombre, apellidos, correo});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al leer los usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
 
        actualizarConteo();
    }
 
    /**
     * Aplica todos los filtros activos (Login, Nivel de Acceso, Nombre,
     * Apellidos, Correo) combinados con AND. Un campo vacío significa
     * "no filtrar por esta columna".
     */
    private void filtrarTabla() {
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();
 
        agregarFiltroTexto(filtros, txtFiltroLogin.getText(), COL_LOGIN);
        agregarFiltroTexto(filtros, txtFiltroNombre.getText(), COL_NOMBRE);
        agregarFiltroTexto(filtros, txtFiltroApellidos.getText(), COL_APELLIDOS);
        agregarFiltroTexto(filtros, txtFiltroCorreo.getText(), COL_CORREO);
 
        String nivelSeleccionado = (String) cmbNivelAcceso.getSelectedItem();
        if (nivelSeleccionado != null && !nivelSeleccionado.equals("Todos")) {
            filtros.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(nivelSeleccionado) + "$", COL_NIVEL));
        }
 
        if (filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
 
        actualizarConteo();
    }
 
    private void agregarFiltroTexto(List<RowFilter<Object, Object>> filtros, String texto, int columna) {
        String valor = texto == null ? "" : texto.trim();
        if (!valor.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + Pattern.quote(valor), columna));
        }
    }
 
    /**
     * Actualiza la etiqueta debajo del JTable con el total de registros
     * que se están mostrando actualmente (respetando el filtro activo).
     */
    private void actualizarConteo() {
        int visibles = tblUsuarios.getRowCount();
        int total = modeloTabla.getRowCount();
        lblTotalRegistros.setText("Registros mostrados: " + visibles + " de " + total);
    }
 
    private void limpiarFiltros() {
        txtFiltroLogin.setText("");
        txtFiltroNombre.setText("");
        txtFiltroApellidos.setText("");
        txtFiltroCorreo.setText("");
        cmbNivelAcceso.setSelectedIndex(0);
        sorter.setRowFilter(null);
        actualizarConteo();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtFiltroLogin = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        btnLimpiarCampos = new javax.swing.JButton();
        txtFiltroNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFiltroApellidos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtFiltroCorreo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbNivelAcceso = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        lblTotalRegistros = new javax.swing.JLabel();

        jLabel2.setText("Login *");

        txtLogin.setText("jTextField1");
        txtLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoginActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Consulta de Usuarios");

        btnRegresar.setBackground(new java.awt.Color(204, 153, 0));
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        jLabel3.setText("Login:");

        txtFiltroLogin.setText("jTextField1");
        txtFiltroLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroLoginActionPerformed(evt);
            }
        });
        txtFiltroLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroLoginKeyReleased(evt);
            }
        });

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Login", "Nivel de Acceso", "Nombre", "Apellidos", "Correo"
            }
        ));
        jScrollPane1.setViewportView(tblUsuarios);

        btnLimpiarCampos.setBackground(new java.awt.Color(0, 153, 255));
        btnLimpiarCampos.setText("Limpiar");
        btnLimpiarCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposActionPerformed(evt);
            }
        });

        txtFiltroNombre.setText("jTextField1");
        txtFiltroNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroNombreActionPerformed(evt);
            }
        });
        txtFiltroNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroNombreKeyReleased(evt);
            }
        });

        jLabel4.setText("Nombre:");

        txtFiltroApellidos.setText("jTextField1");
        txtFiltroApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroApellidosActionPerformed(evt);
            }
        });
        txtFiltroApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroApellidosKeyReleased(evt);
            }
        });

        jLabel5.setText("Apellidos:");

        jLabel6.setText("Correo:");

        txtFiltroCorreo.setText("jTextField1");
        txtFiltroCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroCorreoActionPerformed(evt);
            }
        });
        txtFiltroCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroCorreoKeyReleased(evt);
            }
        });

        jLabel7.setText("Correo:");

        cmbNivelAcceso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBuscar.setBackground(new java.awt.Color(0, 153, 0));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        lblTotalRegistros.setText("Registros mostrados: 0 de 0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotalRegistros)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(btnRegresar)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1)
                        .addContainerGap(135, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtFiltroLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtFiltroApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLimpiarCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFiltroNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(21, 22, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbNivelAcceso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtFiltroCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1))
                        .addGap(54, 54, 54))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnRegresar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtFiltroLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtFiltroNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtFiltroCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cmbNivelAcceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtFiltroApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarCampos)
                    .addComponent(btnBuscar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalRegistros)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        FrmMenuPrincipal menu = new FrmMenuPrincipal();
        menu.setVisible(true);
        menu.setLocationRelativeTo(null);

        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void txtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoginActionPerformed
        
    }//GEN-LAST:event_txtLoginActionPerformed

    private void txtFiltroLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroLoginActionPerformed
       
    }//GEN-LAST:event_txtFiltroLoginActionPerformed

    private void btnLimpiarCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposActionPerformed
        limpiarFiltros();
        txtFiltroLogin.requestFocus();
    }//GEN-LAST:event_btnLimpiarCamposActionPerformed

    private void txtFiltroLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroLoginKeyReleased
        filtrarTabla();
    }//GEN-LAST:event_txtFiltroLoginKeyReleased

    private void txtFiltroNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroNombreActionPerformed

    private void txtFiltroNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroNombreKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroNombreKeyReleased

    private void txtFiltroApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroApellidosActionPerformed

    private void txtFiltroApellidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroApellidosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroApellidosKeyReleased

    private void txtFiltroCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroCorreoActionPerformed

    private void txtFiltroCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCorreoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroCorreoKeyReleased

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        filtrarTabla();
    }//GEN-LAST:event_btnBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmConsultaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmConsultaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmConsultaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmConsultaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmConsultaUsuarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnBuscar;
    public javax.swing.JButton btnLimpiarCampos;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbNivelAcceso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JTable tblUsuarios;
    public javax.swing.JTextField txtFiltroApellidos;
    public javax.swing.JTextField txtFiltroCorreo;
    public javax.swing.JTextField txtFiltroLogin;
    public javax.swing.JTextField txtFiltroNombre;
    public javax.swing.JTextField txtLogin;
    // End of variables declaration//GEN-END:variables
}
