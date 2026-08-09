/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaces;

import com.toedter.calendar.JDateChooser;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author yefry
 */
public class FrmMantReserva extends javax.swing.JFrame {

    private boolean esNuevaReserva = true;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Método que bloquea los botones y controles cuando no hay búsqueda/datos activos
    private void aplicarEstadoInicial() {
        btnGuardar.setEnabled(false);
        btnLimpiarCampos.setEnabled(true);
        btnEliminar.setEnabled(false);

        // Bloquear campos de edición hasta que se busque un ID
        jdcFechaReserva.setEnabled(false);
        txtIdSala.setEnabled(false);
        txtIdCliente.setEnabled(false);
        txtIdHorarioReserva.setEnabled(false);
        txtIdEstReserva.setEnabled(false);

        txtDescSala.setEnabled(false);
        txtDescCliente.setEnabled(false);
        txtDescHorarioReserva.setEnabled(false);
        txtDescEstReserva.setEnabled(false);

        esNuevaReserva = true;
    }

    // Método para limpiar la pantalla y devolverla al estado base
    private void limpiarCampos() {
        txtIdReserva.setText("");
        jdcFechaReserva.setDate(null);
        txtIdSala.setText("");
        txtIdCliente.setText("");
        txtIdHorarioReserva.setText("");
        txtIdEstReserva.setText("");

        txtDescSala.setText("");
        txtDescCliente.setText("");
        txtDescHorarioReserva.setText("");
        txtDescEstReserva.setText("");

        txtIdReserva.requestFocus();
        aplicarEstadoInicial();
    }

    // Método auxiliar para validar si un ID existe en otro archivo (Llave foránea)
    private boolean existeEnArchivo(String id, String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length > 0 && datos[0].trim().equals(id.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer " + nombreArchivo + ": " + e.getMessage());
        }
        return false;
    }

    // Método para obtener la descripción o nombre asociado a un ID en un archivo específico
    private String buscarDescripcion(String id, String nombreArchivo) {
        if (id == null || id.trim().isEmpty()) {
            return "";
        }
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return "Archivo no encontrado";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length > 1 && datos[0].trim().equals(id.trim())) {
                    // Manejo especial si clientes.txt maneja Nombre y Apellido en col 1 y 2
                    if (nombreArchivo.equalsIgnoreCase("clientes.txt") && datos.length >= 3 && !datos[2].trim().matches("\\d+")) {
                        return datos[1].trim() + " " + datos[2].trim();
                    }
                    return datos[1].trim(); // Retorna la descripción / nombre
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar descripción en " + nombreArchivo + ": " + e.getMessage());
        }
        return "No encontrado";
    }

    // Actualiza dinámicamente las descripciones
    private void cargarDescripciones() {
        txtDescSala.setText(buscarDescripcion(txtIdSala.getText().trim(), "salas.txt"));
        txtDescCliente.setText(buscarDescripcion(txtIdCliente.getText().trim(), "clientes.txt"));
        txtDescHorarioReserva.setText(buscarDescripcion(txtIdHorarioReserva.getText().trim(), "reservas_actividades.txt"));
        txtDescEstReserva.setText(buscarDescripcion(txtIdEstReserva.getText().trim(), "estados_reserva.txt"));
    }

    // Asigna eventos a los campos ID para autofiltro visual
    private void agregarListenersBusqueda(javax.swing.JTextField txtId, javax.swing.JTextField txtDesc, String archivo) {
        txtId.addActionListener(e -> txtDesc.setText(buscarDescripcion(txtId.getText().trim(), archivo)));

        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                txtDesc.setText(buscarDescripcion(txtId.getText().trim(), archivo));
            }
        });
    }

    /**
     * Constructor del Formulario
     */
    public FrmMantReserva() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Mantenimiento de Reservas");

        // --- PLACEHOLDERS DE FLATLAF ---
        txtIdReserva.putClientProperty("JTextField.placeholderText", "Ej. 2001");
        txtIdSala.putClientProperty("JTextField.placeholderText", "Ej. 10");
        txtIdCliente.putClientProperty("JTextField.placeholderText", "Ej. 50");
        txtIdHorarioReserva.putClientProperty("JTextField.placeholderText", "Ej. 1001");
        txtIdEstReserva.putClientProperty("JTextField.placeholderText", "Ej. 1 (Confirmada)");

        // Configurar campos de descripción como solo lectura
        txtDescSala.setEditable(false);
        txtDescCliente.setEditable(false);
        txtDescHorarioReserva.setEditable(false);
        txtDescEstReserva.setEditable(false);

        // Registrar listeners para actualización de etiquetas descriptivas
        agregarListenersBusqueda(txtIdSala, txtDescSala, "salas.txt");
        agregarListenersBusqueda(txtIdCliente, txtDescCliente, "clientes.txt");
        agregarListenersBusqueda(txtIdHorarioReserva, txtDescHorarioReserva, "reservas_actividades.txt");
        agregarListenersBusqueda(txtIdEstReserva, txtDescEstReserva, "estados_reserva.txt");

        limpiarCampos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        txtIdReserva = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtIdSala = new javax.swing.JTextField();
        txtIdHorarioReserva = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiarCampos = new javax.swing.JButton();
        jdcFechaReserva = new com.toedter.calendar.JDateChooser();
        btnGuardar = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        txtIdEstReserva = new javax.swing.JTextField();
        txtDescSala = new javax.swing.JTextField();
        txtDescCliente = new javax.swing.JTextField();
        txtDescHorarioReserva = new javax.swing.JTextField();
        txtDescEstReserva = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Mantenimiento de Reserva");

        btnRegresar.setBackground(new java.awt.Color(204, 153, 0));
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        txtIdReserva.setText("jTextField1");
        txtIdReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdReservaActionPerformed(evt);
            }
        });

        jLabel8.setText("Cod. Reserva *");

        jLabel22.setText("Cod. Cliente *");

        jLabel31.setText("Fecha de Reserva *");

        txtIdCliente.setText("jTextField1");

        jLabel23.setText("Cod. Horario Reserva *");

        txtIdSala.setText("jTextField1");

        txtIdHorarioReserva.setText("jTextField1");

        jLabel20.setText("Cod. Sala *");

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiarCampos.setBackground(new java.awt.Color(0, 153, 255));
        btnLimpiarCampos.setText("Limpiar Campos");
        btnLimpiarCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 153, 0));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel32.setText("Cod. Estado de Reserva *");

        txtIdEstReserva.setText("jTextField1");

        txtDescSala.setText("jTextField1");
        txtDescSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescSalaActionPerformed(evt);
            }
        });

        txtDescCliente.setText("jTextField1");

        txtDescHorarioReserva.setText("jTextField1");

        txtDescEstReserva.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDescSala, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addGap(39, 39, 39)
                                .addComponent(jdcFechaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDescEstReserva, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtIdEstReserva))))
                        .addGap(61, 61, 61)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdHorarioReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescHorarioReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnLimpiarCampos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnRegresar)
                                .addGap(176, 176, 176)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(74, 74, 74))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(62, 62, 62)
                            .addComponent(txtIdReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtIdSala, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(449, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(btnRegresar))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jdcFechaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdHorarioReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescHorarioReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdEstReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDescEstReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(btnLimpiarCampos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar)
                .addGap(16, 16, 16))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(38, 38, 38)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGap(40, 40, 40)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20))
                    .addContainerGap(240, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        FrmMenuPrincipal menu = new FrmMenuPrincipal();
        menu.setVisible(true);
        menu.setLocationRelativeTo(null);

        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void txtIdReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdReservaActionPerformed
        String idStr = txtIdReserva.getText().trim();
        if (idStr.isEmpty()) return;

        try {
            Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de la Reserva debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            txtIdReserva.setText("");
            txtIdReserva.requestFocus();
            return;
        }

        File archivo = new File("reservas.txt");
        boolean encontrado = false;
        String idSala = "", idCli = "", fechaRes = "", idHorario = "", idEst = "", estadoLogico = "1";

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length >= 6 && datos[0].trim().equals(idStr)) {
                        encontrado = true;
                        idSala = datos[1].trim();
                        idCli = datos[2].trim();
                        fechaRes = datos[3].trim();
                        idHorario = datos[4].trim();
                        idEst = datos[5].trim();
                        estadoLogico = datos.length > 6 ? datos[6].trim() : "1";
                        break;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar registro: " + e.getMessage());
            }
        }

        // Habilitar controles
        jdcFechaReserva.setEnabled(true);
        txtIdSala.setEnabled(true);
        txtIdCliente.setEnabled(true);
        txtIdHorarioReserva.setEnabled(true);
        txtIdEstReserva.setEnabled(true);

        txtDescSala.setEnabled(true);
        txtDescCliente.setEnabled(true);
        txtDescHorarioReserva.setEnabled(true);
        txtDescEstReserva.setEnabled(true);

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "Creando nuevo registro.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            esNuevaReserva = true;

            btnGuardar.setEnabled(true);
            btnEliminar.setEnabled(false);
            txtIdSala.requestFocus();
        } else {
            if (estadoLogico.equals("0")) {
                JOptionPane.showMessageDialog(this, "Esta reserva se encuentra ELIMINADA.\nNo es posible modificarla.", "Inactiva", JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
                return;
            }

            JOptionPane.showMessageDialog(this, "Modificando registro existente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            esNuevaReserva = false;

            try {
                if (!fechaRes.isEmpty()) jdcFechaReserva.setDate(dateFormat.parse(fechaRes));
            } catch (Exception e) {
                System.out.println("Error parseando fecha de reserva: " + e.getMessage());
            }

            txtIdSala.setText(idSala);
            txtIdCliente.setText(idCli);
            txtIdHorarioReserva.setText(idHorario);
            txtIdEstReserva.setText(idEst);

            cargarDescripciones();

            btnGuardar.setEnabled(true);
            btnEliminar.setEnabled(true);
            txtIdSala.requestFocus();
        }
    }//GEN-LAST:event_txtIdReservaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String idStr = txtIdReserva.getText().trim();
        if (idStr.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar la reserva ID: " + idStr + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        File archivo = new File("reservas.txt");
        List<String> lineasArchivo = new ArrayList<>();
        boolean encontrado = false;

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length > 0 && datos[0].trim().equals(idStr)) {
                        // Rescatamos datos y reescribimos con bandera de eliminación lógica "0"
                        String idSala = datos.length > 1 ? datos[1].trim() : "";
                        String idCli = datos.length > 2 ? datos[2].trim() : "";
                        String fRes = datos.length > 3 ? datos[3].trim() : "";
                        String idHor = datos.length > 4 ? datos[4].trim() : "";
                        String idEst = datos.length > 5 ? datos[5].trim() : "";
                        lineasArchivo.add(idStr + "," + idSala + "," + idCli + "," + fRes + "," + idHor + "," + idEst + ",0");
                        encontrado = true;
                    } else {
                        lineasArchivo.add(linea);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer archivo: " + e.getMessage());
                return;
            }
        }

        if (encontrado) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                for (String l : lineasArchivo) {
                    pw.println(l);
                }
                JOptionPane.showMessageDialog(this, "Reserva eliminada lógicamente con éxito.");
                limpiarCampos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarCamposActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String idStr = txtIdReserva.getText().trim();
        String idSala = txtIdSala.getText().trim();
        String idCli = txtIdCliente.getText().trim();
        Date utilFechaReserva = jdcFechaReserva.getDate();
        String idHorario = txtIdHorarioReserva.getText().trim();
        String idEst = txtIdEstReserva.getText().trim();

        // 1. Validar campos obligatorios (Todos según la imagen (*))
        if (idStr.isEmpty() || idSala.isEmpty() || idCli.isEmpty() || 
            utilFechaReserva == null || idHorario.isEmpty() || idEst.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos obligatorios (*) deben ser completados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validaciones de Existencia de Llaves Foráneas
        if (!existeEnArchivo(idSala, "salas.txt")) {
            JOptionPane.showMessageDialog(this, "El ID de Sala no existe en el archivo salas.txt.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!existeEnArchivo(idCli, "clientes.txt")) {
            JOptionPane.showMessageDialog(this, "El Cliente no existe en el archivo clientes.txt.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!existeEnArchivo(idHorario, "reservas_actividades.txt")) {
            JOptionPane.showMessageDialog(this, "El Horario de Reserva no existe en reservas_actividades.txt.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!existeEnArchivo(idEst, "estados_reserva.txt")) {
            JOptionPane.showMessageDialog(this, "El Estado de Reserva no existe en estados_reserva.txt.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fechaResStr = dateFormat.format(utilFechaReserva);

        File archivo = new File("reservas.txt");
        List<String> lineasArchivo = new ArrayList<>();

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length > 0 && datos[0].trim().equals(idStr)) {
                        if (!esNuevaReserva) {
                            // Actualizar manteniendo bandera de estado activo 1
                            lineasArchivo.add(idStr + "," + idSala + "," + idCli + "," + fechaResStr + "," + idHorario + "," + idEst + ",1");
                        }
                    } else {
                        lineasArchivo.add(linea);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer archivo: " + e.getMessage());
            }
        }

        if (esNuevaReserva) {
            for (String l : lineasArchivo) {
                String[] datos = l.split(",", -1);
                if (datos.length > 0 && datos[0].trim().equals(idStr)) {
                    JOptionPane.showMessageDialog(this, "El ID de Reserva ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            // Agregar nuevo con estado lógico 1
            lineasArchivo.add(idStr + "," + idSala + "," + idCli + "," + fechaResStr + "," + idHorario + "," + idEst + ",1");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (String l : lineasArchivo) {
                pw.println(l);
            }
            JOptionPane.showMessageDialog(this, "Datos de la reserva guardados correctamente.");
            limpiarCampos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtDescSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescSalaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescSalaActionPerformed

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
            java.util.logging.Logger.getLogger(FrmMantReserva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMantReserva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMantReserva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMantReserva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMantReserva().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    public javax.swing.JButton btnLimpiarCampos;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel8;
    private com.toedter.calendar.JDateChooser jdcFechaReserva;
    private javax.swing.JTextField txtDescCliente;
    private javax.swing.JTextField txtDescEstReserva;
    private javax.swing.JTextField txtDescHorarioReserva;
    private javax.swing.JTextField txtDescSala;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdEstReserva;
    private javax.swing.JTextField txtIdHorarioReserva;
    private javax.swing.JTextField txtIdReserva;
    private javax.swing.JTextField txtIdSala;
    // End of variables declaration//GEN-END:variables
}
