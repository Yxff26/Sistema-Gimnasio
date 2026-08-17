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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


/**
 *
 * @author yefry
 */
public class FrmMantReservaActividades extends javax.swing.JFrame {

    private boolean esNuevaReserva = true;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Método que bloquea los botones cuando no hay datos activos
    private void aplicarEstadoInicial() {
        btnGuardar.setEnabled(false);
        btnLimpiarCampos.setEnabled(true); 
        btnEliminar.setEnabled(false);
        
        // Bloquear campos hasta que se busque un ID
        jdcFechaReserva.setEnabled(false);
        jdcFechaBaja.setEnabled(false);
        txtIdEstReservaAct.setEnabled(false);
        txtIdCliente.setEnabled(false);
        txtIdActividad.setEnabled(false);
        txtIdResHorAct.setEnabled(false);
        
        txtDescEstado.setEnabled(false);
        txtDescCliente.setEnabled(false);
        txtDescActividad.setEnabled(false);
        txtDescHorario.setEnabled(false);
        
        esNuevaReserva = true;
    }

    // Método para limpiar la pantalla y devolverla al estado base
    private void limpiarCampos() {
        txtIdReservaAct.setText("");
        jdcFechaReserva.setDate(null);
        jdcFechaBaja.setDate(null);
        txtIdEstReservaAct.setText("");
        txtIdCliente.setText("");
        txtIdActividad.setText("");
        txtIdResHorAct.setText("");
        
        txtDescEstado.setText("");
        txtDescCliente.setText("");
        txtDescActividad.setText("");
        txtDescHorario.setText("");
        
        txtIdReservaAct.requestFocus();
        aplicarEstadoInicial();
    }

    // Método auxiliar para validar si un ID existe en otro archivo (Llave foránea)
    private boolean existeEnArchivo(String id, String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return false; // Si el archivo no existe, no hay registros

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                // Asumimos que el ID es la primera columna
                if (datos.length > 0 && datos[0].trim().equals(id)) {
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
                    // Caso particular si clientes.txt maneja Nombre y Apellido (columnas 1 y 2)
                    if (nombreArchivo.equalsIgnoreCase("clientes.txt") && datos.length >= 3 && !datos[2].trim().matches("\\d+")) {
                        return datos[1].trim() + " " + datos[2].trim();
                    }
                    return datos[1].trim(); // Retorna la segunda columna (Nombre / Descripción)
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar descripción en " + nombreArchivo + ": " + e.getMessage());
        }
        return "No encontrado";
    }

    // Método auxiliar para actualizar todas las descripciones
    private void cargarDescripciones() {
        txtDescEstado.setText(buscarDescripcion(txtIdEstReservaAct.getText().trim(), "estados_reserva.txt"));
        txtDescCliente.setText(buscarDescripcion(txtIdCliente.getText().trim(), "clientes.txt"));
        txtDescActividad.setText(buscarDescripcion(txtIdActividad.getText().trim(), "actividades.txt"));
        txtDescHorario.setText(buscarDescripcion(txtIdResHorAct.getText().trim(), "horarios_actividades.txt"));
    }

    // Asigna eventos a los campos ID para que al presionar Enter o cambiar de campo se actualice la descripción
    private void agregarListenersBusqueda(javax.swing.JTextField txtId, javax.swing.JTextField txtDesc, String archivo) {
        txtId.addActionListener(e -> txtDesc.setText(buscarDescripcion(txtId.getText().trim(), archivo)));

        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                txtDesc.setText(buscarDescripcion(txtId.getText().trim(), archivo));
            }
        });
    }
    
        private void aplicarFiltrosNumericos() {
        // Filtro 1: Solo números enteros (para Código de Cuota y Código de Cliente)
        DocumentFilter filtroEnteros = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        };

        // Aplicar Filtro 1 a los campos de códigos
        ((AbstractDocument) txtIdReservaAct.getDocument()).setDocumentFilter(filtroEnteros);
        ((AbstractDocument) txtIdCliente.getDocument()).setDocumentFilter(filtroEnteros);
        ((AbstractDocument) txtIdEstReservaAct.getDocument()).setDocumentFilter(filtroEnteros);
        ((AbstractDocument) txtIdActividad.getDocument()).setDocumentFilter(filtroEnteros);
        ((AbstractDocument) txtIdResHorAct.getDocument()).setDocumentFilter(filtroEnteros);
    }


    /**
     * Creates new form FrmMantReservaActividades
     */
    public FrmMantReservaActividades() {
        initComponents();
        setLocationRelativeTo(null);
        aplicarFiltrosNumericos(); 
        setTitle("Mantenimiento de Reservas Actividades");

        // --- MAGIA DE FLATLAF: PLACEHOLDERS ---
        txtIdReservaAct.putClientProperty("JTextField.placeholderText", "Ej. 1001");
        txtIdEstReservaAct.putClientProperty("JTextField.placeholderText", "Ej. 1 (Activa)");
        txtIdCliente.putClientProperty("JTextField.placeholderText", "Ej. 50");
        txtIdActividad.putClientProperty("JTextField.placeholderText", "Ej. 5");
        txtIdResHorAct.putClientProperty("JTextField.placeholderText", "Ej. 12");

        // Configurar campos de descripción como solo lectura
        txtDescEstado.setEditable(false);
        txtDescCliente.setEditable(false);
        txtDescActividad.setEditable(false);
        txtDescHorario.setEditable(false);

        // Agregar listeners para actualizar descripción al escribir/salir del campo
        agregarListenersBusqueda(txtIdEstReservaAct, txtDescEstado, "estados_reserva.txt");
        agregarListenersBusqueda(txtIdCliente, txtDescCliente, "clientes.txt");
        agregarListenersBusqueda(txtIdActividad, txtDescActividad, "actividades.txt");
        agregarListenersBusqueda(txtIdResHorAct, txtDescHorario, "horarios_actividades.txt");

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

        btnGuardar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtIdReservaAct = new javax.swing.JTextField();
        btnRegresar = new javax.swing.JButton();
        jdcFechaReserva = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        txtIdEstReservaAct = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiarCampos = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jdcFechaBaja = new com.toedter.calendar.JDateChooser();
        jLabel33 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtIdActividad = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtIdResHorAct = new javax.swing.JTextField();
        txtDescActividad = new javax.swing.JTextField();
        txtDescCliente = new javax.swing.JTextField();
        txtDescEstado = new javax.swing.JTextField();
        txtDescHorario = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnGuardar.setBackground(new java.awt.Color(0, 153, 0));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel28.setText("Cod. Estado Reserva *");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Mantenimiento de Reserva de Actividades");

        txtIdReservaAct.setText("jTextField1");
        txtIdReservaAct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdReservaActActionPerformed(evt);
            }
        });

        btnRegresar.setBackground(new java.awt.Color(204, 153, 0));
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        jLabel8.setText("Cod. Reserva *");

        txtIdEstReservaAct.setText("jTextField1");

        jLabel31.setText("Fecha Reserva *");

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

        jLabel32.setText("Fecha Baja *");

        jLabel33.setText("Cod. Cliente *");

        txtIdCliente.setText("jTextField1");

        jLabel34.setText("Cod. Actividad *");

        txtIdActividad.setText("jTextField1");

        jLabel35.setText("Cod. Horario Act. *");

        txtIdResHorAct.setText("jTextField1");

        txtDescActividad.setText("jTextField1");

        txtDescCliente.setText("jTextField1");

        txtDescEstado.setText("jTextField1");

        txtDescHorario.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(144, 144, 144))
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(txtIdReservaAct, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33))
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jdcFechaReserva, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                    .addComponent(txtIdCliente)
                                    .addComponent(txtDescCliente)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnLimpiarCampos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(jLabel32)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcFechaBaja, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(txtIdEstReservaAct)
                            .addComponent(txtDescEstado, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(113, 113, 113))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtIdActividad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                            .addComponent(txtDescActividad, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDescHorario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                            .addComponent(txtIdResHorAct, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(55, 55, 55)
                    .addComponent(btnRegresar)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdReservaAct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIdEstReservaAct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(jdcFechaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(btnLimpiarCampos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32)
                            .addComponent(jdcFechaBaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(txtIdActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDescActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdResHorAct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(btnRegresar)
                    .addContainerGap(294, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String idStr = txtIdReservaAct.getText().trim();
        Date utilFechaReserva = jdcFechaReserva.getDate();
        Date utilFechaBaja = jdcFechaBaja.getDate();
        String idEst = txtIdEstReservaAct.getText().trim();
        String idCli = txtIdCliente.getText().trim();
        String idAct = txtIdActividad.getText().trim();
        String idHor = txtIdResHorAct.getText().trim();

        // 1. Validar campos obligatorios
        if (idStr.isEmpty() || utilFechaReserva == null || utilFechaBaja == null || 
            idEst.isEmpty() || idCli.isEmpty() || idAct.isEmpty() || idHor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validaciones de archivos (Llaves Foráneas)
        if (!existeEnArchivo(idEst, "estados_reserva.txt")) {
            JOptionPane.showMessageDialog(this, "El Estado de Reserva no existe en el archivo.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!existeEnArchivo(idCli, "clientes.txt")) {
            JOptionPane.showMessageDialog(this, "El Cliente no existe en el archivo de Clientes.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!existeEnArchivo(idAct, "actividades.txt")) {
            JOptionPane.showMessageDialog(this, "La Actividad no existe en el archivo de Actividades.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!existeEnArchivo(idHor, "horarios_actividades.txt")) {
            JOptionPane.showMessageDialog(this, "El Horario no existe en el archivo de Horarios.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fechaResStr = dateFormat.format(utilFechaReserva);
        String fechaBajaStr = dateFormat.format(utilFechaBaja);

        File archivo = new File("reservas_actividades.txt");
        List<String> lineasArchivo = new ArrayList<>();

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length > 0 && datos[0].trim().equals(idStr)) {
                        if (!esNuevaReserva) {
                            // Actualiza y mantiene el estado 1 (Activo) al final
                            lineasArchivo.add(idStr + "," + fechaResStr + "," + fechaBajaStr + "," + idEst + "," + idCli + "," + idAct + "," + idHor + ",1");
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
            // Agrega nuevo con estado 1
            lineasArchivo.add(idStr + "," + fechaResStr + "," + fechaBajaStr + "," + idEst + "," + idCli + "," + idAct + "," + idHor + ",1");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (String l : lineasArchivo) {
                pw.println(l);
            }
            JOptionPane.showMessageDialog(this, "Datos de reserva guardados correctamente.");
            limpiarCampos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtIdReservaActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdReservaActActionPerformed
        String idStr = txtIdReservaAct.getText().trim();
        if (idStr.isEmpty()) return;

        try {
            Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de la Reserva debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            txtIdReservaAct.setText("");
            txtIdReservaAct.requestFocus();
            return;
        }

        File archivo = new File("reservas_actividades.txt");
        boolean encontrado = false;
        String fechaRes = "", fechaBaja = "", idEst = "", idCli = "", idAct = "", idHor = "", estado = "1";

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length >= 7 && datos[0].trim().equals(idStr)) {
                        encontrado = true;
                        fechaRes = datos[1].trim();
                        fechaBaja = datos[2].trim();
                        idEst = datos[3].trim();
                        idCli = datos[4].trim();
                        idAct = datos[5].trim();
                        idHor = datos[6].trim();
                        estado = datos.length > 7 ? datos[7].trim() : "1"; // Eliminación lógica
                        break;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
            }
        }

        // Habilitar los campos para escritura independientemente de si existe o no
        jdcFechaReserva.setEnabled(true);
        jdcFechaBaja.setEnabled(true);
        txtIdEstReservaAct.setEnabled(true);
        txtIdCliente.setEnabled(true);
        txtIdActividad.setEnabled(true);
        txtIdResHorAct.setEnabled(true);

        txtDescEstado.setEnabled(true);
        txtDescCliente.setEnabled(true);
        txtDescActividad.setEnabled(true);
        txtDescHorario.setEnabled(true);

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "Creando", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            esNuevaReserva = true;

            btnGuardar.setEnabled(true);
            btnEliminar.setEnabled(false);
            jdcFechaReserva.requestFocus();
        } else {
            if (estado.equals("0")) {
                JOptionPane.showMessageDialog(this, "Esta reserva se encuentra ELIMINADA.\nNo es posible modificarla.", "Inactiva", JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
                return;
            }

            JOptionPane.showMessageDialog(this, "Modificando", "Aviso", JOptionPane.WARNING_MESSAGE);
            esNuevaReserva = false;

            // Cargar fechas al JDateChooser
            try {
                if (!fechaRes.isEmpty()) jdcFechaReserva.setDate(dateFormat.parse(fechaRes));
                if (!fechaBaja.isEmpty()) jdcFechaBaja.setDate(dateFormat.parse(fechaBaja));
            } catch (Exception e) {
                System.out.println("Error parseando fechas: " + e.getMessage());
            }

            txtIdEstReservaAct.setText(idEst);
            txtIdCliente.setText(idCli);
            txtIdActividad.setText(idAct);
            txtIdResHorAct.setText(idHor);

            // Cargar automáticamente las descripciones correspondientes a los IDs
            cargarDescripciones();

            btnGuardar.setEnabled(true);
            btnEliminar.setEnabled(true);
            jdcFechaReserva.requestFocus();
        }
    }//GEN-LAST:event_txtIdReservaActActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        FrmMenuPrincipal menu = new FrmMenuPrincipal();
        menu.setVisible(true);
        menu.setLocationRelativeTo(null);

        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String idStr = txtIdReservaAct.getText().trim();
        if (idStr.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar la reserva ID: " + idStr + "?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        File archivo = new File("reservas_actividades.txt");
        List<String> lineasArchivo = new ArrayList<>();
        boolean encontrado = false;

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length > 0 && datos[0].trim().equals(idStr)) {
                        // Rescatamos los datos y forzamos el estado a 0 (Eliminación lógica)
                        String fRes = datos.length > 1 ? datos[1].trim() : "";
                        String fBaj = datos.length > 2 ? datos[2].trim() : "";
                        String est = datos.length > 3 ? datos[3].trim() : "";
                        String cli = datos.length > 4 ? datos[4].trim() : "";
                        String act = datos.length > 5 ? datos[5].trim() : "";
                        String hor = datos.length > 6 ? datos[6].trim() : "";
                        lineasArchivo.add(idStr + "," + fRes + "," + fBaj + "," + est + "," + cli + "," + act + "," + hor + ",0");
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
                JOptionPane.showMessageDialog(this, "Reserva eliminada lógicamente.");
                limpiarCampos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarCamposActionPerformed

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
            java.util.logging.Logger.getLogger(FrmMantReservaActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMantReservaActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMantReservaActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMantReservaActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMantReservaActividades().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    public javax.swing.JButton btnLimpiarCampos;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel8;
    private com.toedter.calendar.JDateChooser jdcFechaBaja;
    private com.toedter.calendar.JDateChooser jdcFechaReserva;
    private javax.swing.JTextField txtDescActividad;
    private javax.swing.JTextField txtDescCliente;
    private javax.swing.JTextField txtDescEstado;
    private javax.swing.JTextField txtDescHorario;
    private javax.swing.JTextField txtIdActividad;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdEstReservaAct;
    private javax.swing.JTextField txtIdResHorAct;
    private javax.swing.JTextField txtIdReservaAct;
    // End of variables declaration//GEN-END:variables
}
