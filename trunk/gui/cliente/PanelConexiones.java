/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente;


import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;

import gui.cliente.configuracionRed.*;
import gui.ProgressTask;
import gui.GuiManager;

import nucleo.cliente.ControlCliente;
import nucleo.cliente.NodoClienteException;
import nucleo.cliente.NodoServidorException;
import nucleo.cliente.ControlClienteException;



/**
 *
 * @author Antonio
 */
public class PanelConexiones 
        extends JPanel implements ActionListener, PropertyChangeListener{

    //componentes GUI
    public final PanelConexionRegistro conexionRegistro = new PanelConexionRegistro();
    public final PanelConexionDS conexionDS = new PanelConexionDS();
    public ProgressTask progressTask;

    //Elementos no gráficos
    private ControlCliente controlCliente;
    private final EstadoGlobal estadoGlobal;


    public PanelConexiones(ControlCliente cc, EstadoGlobal estadoGlobal) {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Network Connections"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.controlCliente = cc;
        this.estadoGlobal = estadoGlobal;
        this.estadoGlobal.estadoGlobal.addPropertyChangeListener(this);
        this.estadoGlobal.estadoRegistrado.addPropertyChangeListener(this);

        this.conexionRegistro.botonConectar.addActionListener(this);
        this.conexionDS.botonConectar.addActionListener(this);

        this.conexionDS.setEnabled(false);

         //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets.top = 50;
        gbc.insets.left = 20;
        gbc.insets.right = 20;
        super.add(this.conexionRegistro, gbc);

        gbc.gridx++;
        gbc.insets.bottom = 50;
        super.add(this.conexionDS, gbc);
        
    }


    /**
     * Control de eventos de conexión/desconexión
     */
    public void actionPerformed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        // EVENTO DE INICIO/CIERRE DE SESIÓN EN REGISTRO
        if (source == this.conexionRegistro.botonConectar) {

            //SI NO EXISTE UNA SESIÓN INICIADA
            if ( ! this.conexionRegistro.botonConectar.getEstadoConexion()){

                    this.progressTask = new ProgressTask<Boolean, Void>(true) {

                        public Boolean doInBackground()
                                throws ControlClienteException{

                            PanelConexiones.this.conexionRegistro.setEnabled(false);
                            InetSocketAddress isa = PanelConexiones.this.conexionRegistro.campoIP.getInetSocketAddress();
                            PanelConexiones.this.controlCliente.conectarConServidor(
                                    isa.getAddress().getHostAddress(), isa.getPort());
                            
                            return true;
                        }

                        public void doneAfter(Boolean conectado) {

                            PanelConexiones.this.conexionRegistro.setEnabled(true);
                            if ( conectado!=null && conectado){
                                 //Si el proceso de registro finaliza con éxito se habilita el panel de configuración PT
                                PanelConexiones.this.estadoGlobal.setEstadoGlobal(Estado.Estados.REGISTRADO);
                                PanelConexiones.this.estadoGlobal.setEstadoRegistrado(Estado.Estados.CONFIGURAR_SOCKETS_PT);

                            }else if(super.isCancelled()){
                                //si el usuario cancela el proceso de registro hay que comprobar si se llega a completar éste
                                try{                                    
                                    PanelConexiones.this.controlCliente.desconectarConServidor();
                                }catch(ControlClienteException cce) {}
                                    PanelConexiones.this.estadoGlobal.setEstadoGlobal(Estado.Estados.DESCONECTADO);                                    
                            }
                        }

                    };
                    
                    this.progressTask.ejecutarTarea(this, "Connecting...", true);

            //SI EXISTE UNA SESIÓN INICIADA
            }else{                

                this.progressTask = new ProgressTask<Void, Void>(true) {

                    public Void doInBackground() throws ControlClienteException{

                        if (PanelConexiones.this.estadoGlobal.getEstadoRegistrado() == Estado.Estados.SOCKETS_PT_CONFIGURADOS) {
                             //si los sockets del PT están conectados hay que cerrarlos
                             PanelConexiones.this.conexionDS.botonConectar.doClick();
                             //while (PanelConexiones.this.estadoGlobal.getEstadoRegistrado() != Estado.Estados.CONFIGURAR_SOCKETS_PT);
                             while(! PanelConexiones.this.progressTask.isDone());
                        }
                        PanelConexiones.this.conexionRegistro.setEnabled(false);
                        PanelConexiones.this.controlCliente.desconectarConServidor();
                        PanelConexiones.this.estadoGlobal.setEstadoGlobal(Estado.Estados.DESCONECTADO);
                        return null;
                    }

                    public void doneAfter(Void v) {

                        //comprobar si se realiza el cierre de sesión correctamente
                        try{

                            PanelConexiones.this.controlCliente.notificarActividadServidor();
                            PanelConexiones.this.estadoGlobal.setEstadoGlobal(Estado.Estados.REGISTRADO);

                        }catch(ControlClienteException cce) {
                            
                            PanelConexiones.this.conexionRegistro.setEnabled(true);
                            PanelConexiones.this.estadoGlobal.setEstadoGlobal(Estado.Estados.DESCONECTADO);                            
                        }
                    }

                };

                this.progressTask.ejecutarTarea(this, "Disconnecting from server...", false);
               
            }

         //EVENTO DE CONFIGURAR Y CONECTAR SOCKETS DEL PT
        }else if(source == this.conexionDS.botonConectar) {

            //SI NO ESTÁN CONECTADOS
            if ( ! this.conexionDS.botonConectar.getEstadoConexion()) {

                final InetSocketAddress isaLocal =  PanelConexiones.this.conexionDS.conexionLocal.getInetSocketAddress();
                final InetSocketAddress isaRemota = PanelConexiones.this.conexionDS.conexionRemota.getInetSocketAddress();

            this.progressTask = new ProgressTask<Boolean, Void>(true) {

                public Boolean doInBackground()
                        throws NodoServidorException, NodoClienteException {

                    PanelConexiones.this.controlCliente.getControlNodos().configurarSocketNodoLocal(
                            isaLocal.getAddress().getHostAddress(), isaLocal.getPort());
                    
                    PanelConexiones.this.controlCliente.getControlNodos().configurarSocketNodoRemoto(
                            isaRemota.getAddress().getHostAddress(), isaRemota.getPort());
                   
                    PanelConexiones.this.controlCliente.getControlNodos().conectarSockets();

                    return true;
                }


                public void doneAfter(Boolean conectado) {

                    PanelConexiones.this.conexionDS.setEnabled(true);
                    if (conectado!=null && conectado) {
                        PanelConexiones.this.estadoGlobal.setEstadoRegistrado(Estado.Estados.SOCKETS_PT_CONFIGURADOS);
                    }
                    
                }
            };

            this.progressTask.ejecutarTarea(this, "Connecting sockets...", false);

            //SI ESTÁN CONECTADOS
            }else{

                try{

                    this.controlCliente.getControlNodos().cerrarSocketNodoLocal();

                }catch(NodoClienteException nce) {

                    String mensaje = "Client operation failed:" + '\n';
                       JOptionPane.showMessageDialog(
                                PanelConexiones.this, mensaje + nce.getMessage() ,
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE
                                );
                }


                this.progressTask = new ProgressTask<Void,Void>(true) {

                    public Void doInBackground() throws NodoServidorException{

                        PanelConexiones.this.conexionDS.setEnabled(false);
                        PanelConexiones.this.controlCliente.getControlNodos().cerrarSocketNodoRemoto();

                        return null;
                    }

                    public void doneAfter(Void v) {

                        PanelConexiones.this.conexionDS.setEnabled(true);
                        PanelConexiones.this.estadoGlobal.setEstadoRegistrado(Estado.Estados.CONFIGURAR_SOCKETS_PT);
                        
                    }
                };

                this.progressTask.ejecutarTarea(this, "Closing sockets...", false);

            }
            
        }

    }//fin action performed


    /**
     * Control de eventos de cambios de estado
     */
    public void propertyChange(PropertyChangeEvent pce) {

        if (pce.getPropertyName().equals("estado")){

            Estado.Estados nuevoEstado = (Estado.Estados) pce.getNewValue();
            this.ajustarComponentes(nuevoEstado);
                                                 
        }        
    }

    public void ajustarComponentes(Estado.Estados nuevoEstado) {

         switch(nuevoEstado) {

                case REGISTRADO:

                    this.conexionRegistro.botonConectar.setEstadoConexion(true);
                    this.conexionRegistro.campoIP.setEnabled(false);
                    
                    break;

                case DESCONECTADO:

                    this.conexionRegistro.botonConectar.setEstadoConexion(false);
                    this.conexionRegistro.setEnabled(true);
                    this.conexionDS.botonConectar.setEstadoConexion(false);
                    this.conexionDS.setEnabled(false);
                    this.estadoGlobal.setEstadoRegistrado(Estado.Estados.INDEFINIDO);
                    break;

                case CONFIGURAR_SOCKETS_PT:

                    this.conexionDS.setEnabled(true);
                    this.conexionDS.botonConectar.setEstadoConexion(false);
                    break;

                case SOCKETS_PT_CONFIGURADOS:

                    this.conexionDS.botonConectar.setEstadoConexion(true);
                    this.conexionDS.conexionLocal.setEnabled(false);
                    this.conexionDS.conexionRemota.setEnabled(false);
                    break;
            }
    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);
        
        if(enabled) {

            this.ajustarComponentes(this.estadoGlobal.getEstadoGlobal());
            this.ajustarComponentes(this.estadoGlobal.getEstadoRegistrado());
        }
        
    }    

}
