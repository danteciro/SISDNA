package pe.gob.mimp.sisdna.administrado;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.core.Util;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;

/**
 * Clase: ParametroDnaAdministrado.java <br>
 * Clase encargada de gestionar los parámetros. <br>
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@Named(value = "parametroDnaAdministrado")
@ViewScoped
public class ParametroDnaAdministrado extends AdministradorAbstracto implements Serializable {

    private ParametroDna entidadSeleccionada;
    private List<ParametroDna> entidades;

    @EJB
    private ParametroDnaFacadeLocal fachada;

    public ParametroDnaAdministrado() {
        this.entidadSeleccionada = new ParametroDna();
        this.entidades = new ArrayList<>();
    }

    public ParametroDna getEntidadSeleccionada() {
        return entidadSeleccionada;
    }

    public void setEntidadSeleccionada(ParametroDna entidadSeleccionada) {
        this.entidadSeleccionada = entidadSeleccionada;
    }

    public List<ParametroDna> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<ParametroDna> entidades) {
        this.entidades = entidades;
    }

    public List<ParametroDna> obtenerTodos() {
        this.entidades = this.fachada.obtenerTodos(true, "comentario", true);
        return this.entidades;
    }
    
    public void obtenerParametro(ParametroDna item) {
        this.entidadSeleccionada = item;
    }
   
    /**
     * Devuelve el tiempo de permanencia en pantalla de los mensajes y alertas
     * @return el tiempo
     */
    public int tiempoAlertas() {
        int valor = 0;
        try {
            List<ParametroDna> parametro = fachada.findAllByField("cidParametro", "TIEMPOALERTAS");
            if (null != parametro && 0 < parametro.size()) {
                valor = parametro.get(0).getNumValor1().intValue();
            } else {
                Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.INFO, "{0}tiempoAlertas: No se ha definido un parámetro para este valor", Util.tiempo());
            }
        } catch (Exception e) {
            Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName()).log(Level.SEVERE, null, e);
        }
        return valor;
    }

    /**
     * Devuelve el tiempo de idle
     * @return el tiempo
     */
    public int tiempoIdle() {
        int valor = 0;
        try {
            List<ParametroDna> parametro = fachada.findAllByField("cidParametro", "TIEMPOSESION");
            if (null != parametro && 0 < parametro.size()) {
                valor = parametro.get(0).getNumValor1().intValue();
            } else {
                System.out.println("tiempoIdle: tiempoAlertas: No se ha definido un parámetro para este valor.");
            }
        } catch (Exception e) {
            System.out.println("tiempoIdle: " + e);
        }
        return valor;
    }

    /**
     * Actualiza el parámetro
     */
    public void editar() {
 
        try {
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
 
            this.entidadSeleccionada.setNidUsuario(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario().toBigInteger());
            this.entidadSeleccionada.setTxtPc(Internet.obtenerNombrePC());
            this.entidadSeleccionada.setTxtIp(Internet.obtenerIPPC());
            this.entidadSeleccionada.setFecEdicion(new Date());
            this.entidadSeleccionada.setFlgActivo(BigInteger.ONE);
            this.fachada.edit(this.entidadSeleccionada);
            
        } catch (Exception ex) {
            adicionarMensajeWarning("", "Error al actualizar el parámetro. Por favor, intente nuevamente.");            
        }
    }

    /**
     * invalida las sesiones
     */
    public void invalidarSesion() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
    }

    /**
     * redirige al home
     * @throws IOException excepción
     */
    public void redirigir() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/");
        ec.invalidateSession();
    }

}
