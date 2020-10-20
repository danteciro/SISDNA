package pe.gob.mimp.sisdna.util;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pe.gob.mimp.core.Correo;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.ParametroDna;


/**
 * Clase: CorreoAdministrado.java <br>
 * Clase que gestiona los envíos de correo. <br>
 * 
 * @author BooleanCore
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CorreoDnaAdministrado extends AdministradorAbstracto implements Serializable {

    private Correo correo;

    @EJB
    private ParametroDnaFacadeLocal parametroFacade;

    public Correo getCorreo() {
        return correo;
    }

    public void setCorreo(Correo correo) {
        this.correo = correo;
    }

    public CorreoDnaAdministrado() {
        this.correo = new Correo();
    }

    @PostConstruct
    public void init() {
        try {
            List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_SMTP);
            String smtp = parametros.get(0).getTxtValor();
            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_FROM);
            String from = parametros.get(0).getTxtValor();
            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_PORT);
            String port = parametros.get(0).getTxtValor();
            parametros = this.parametroFacade.findAllByField("cidParametro", Constantes.CORREO_PASSWORD);
            String password = parametros.get(0).getTxtValor();
            
            this.correo = new Correo();
            this.correo.setServidor(smtp);
            this.correo.setRemitente(from);
            this.correo.setPuerto(port);
            this.correo.setUsuario(from);
            this.correo.setClave(password);
            this.correo.setContenido("");
            
        } catch (Exception e) {
            adicionarMensaje("Configuracion de Correo", e.getMessage());
        }
    }

    public String cargarTpl(String tpl) {
        List<ParametroDna> parametros = this.parametroFacade.findAllByField("cidParametro", tpl);
        return parametros.get(0).getTxtValor();
    }
}
