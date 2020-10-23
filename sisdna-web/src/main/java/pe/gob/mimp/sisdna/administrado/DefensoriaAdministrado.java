package pe.gob.mimp.sisdna.administrado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Departamento;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.general.util.ParametroNodoObject;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.CatalogoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.DefensoriaPersona;
import pe.gob.mimp.sisdna.util.Constantes;
import pe.gob.mimp.webservicemimp.auxiliar.IdentificacionReniec;

/**
 * Clase: DefensoriaAdministrado.java <br>
 * Clase encargada de gestionar las defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Named(value = "defensoriaAdministrado")
@ViewScoped
public class DefensoriaAdministrado extends AdministradorAbstracto implements Serializable{

    private Defensoria defensoria;
    private DefensoriaPersona defensoriaPersona;

    private List<Defensoria> listaDefensoria;
    private List<DefensoriaPersona> listaDefensoriaPersona;
    private List<DefensoriaPersona> personalRemover;
    
    private int modo;
  
    private BigDecimal busDepartamento;
    private BigDecimal busProvincia;
    private BigDecimal busDistrito;
    private String busCodigo;
 
    private IdentificacionReniec identificacionReniec;
 
    @EJB
    private DefensoriaFacadeLocal defensoriaFacade;
   
    @EJB
    private DepartamentoFacadeLocal departamentoFacade;

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    
    @EJB
    private DistritoFacadeLocal distritoFacade;

    @EJB
    private CatalogoFacadeLocal catalogoFacade;

    @PostConstruct
    public void initLoad() {
      this.modo = Constantes.MODO_LISTADO;
      this.identificacionReniec = new IdentificacionReniec();
    }
    
     /**
     * cambia para regresar al modo listado
     */
    public void regresar(){
       this.modo = Constantes.MODO_LISTADO;
    }
   
    /**
     * Devuelve la lista de defensorías según el id del distrito
     * @param nidDistrito el id del distrito
     * @return la lista de defensorías
     */
    public List<Defensoria> obtenerDefensorias(BigDecimal nidDistrito) {
        
        if(nidDistrito != null) {
            ParametroNodoObject param = new ParametroNodoObject();
            param.adicionar("nidDistrito", nidDistrito);
            return this.defensoriaFacade.obtenerPorParametrosObject(param,true,"txtNombre",false);
        } else
            return null;
    }
    
    public void obtener(BigInteger nidDna) {
        this.defensoria = this.defensoriaFacade.find(nidDna);
        this.modo = Constantes.MODO_UPDATE;
    }
    
     /**
     * Devuelve la lista de departamentos
     * @return lista de departamentos
     */
    public List<Departamento> obtenerDepartamentos() {
        this.busProvincia = null;
        this.busDistrito = null;
        return  this.departamentoFacade.obtenerDepartamentos();
    }
    
   /**
     * Devuelve la lista de provincias según el departamento
     * @return lista de provincias
     */
     public List<Provincia> obtenerProvinciasBus() {
        this.busDistrito = null;
        Departamento departamento = new Departamento();
        departamento.setNidDepartamento(this.busDepartamento);
        return this.provinciaFacade.obtenerProvincias(departamento);
    }
    
     /**
      * Devuelve la lista de distritos según la provincia
      * @return lista de distritos
      */
    public List<Distrito> obtenerDistritosBus() {
        Provincia provincia = new Provincia();
        provincia.setNidProvincia(this.busProvincia);
        return this.distritoFacade.obtenerDistritos(provincia);
    }
    
    
   /**
     * Búsqueda de defensorías por departamento, provincia y distrito
     */
    public void buscar() {
        
        ParamFiltro param = new ParamFiltro();

        if( this.busCodigo!=null ) {
            this.listaDefensoria = this.defensoriaFacade.findAllByField("txtConstancia", this.busCodigo);
            return;
        }

        if(this.busDepartamento!=null) 
            param.adicionar("nidDepartamento", this.busDepartamento);

        if(this.busProvincia!=null)
            param.adicionar("nidProvincia", this.busProvincia);

        if(this.busDistrito!=null)
            param.adicionar("nidDistrito", this.busDistrito);

        if(param.getParametros().size()>0) 
           this.listaDefensoria = this.defensoriaFacade.obtenerPorFiltro(param, true, "txtNombre", true);

       
    }
    
      /**
     * Acutalizar los datos de la defensoría
     */
    public void update() {
         try {
            
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
            this.defensoria.setNidUsuarioMod(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
            this.defensoria.setTxtPc(Internet.obtenerNombrePC());
            this.defensoria.setTxtIp(Internet.obtenerIPPC());
            this.defensoria.setFecModificacion(new Date());
            this.defensoria.setFlgActivo(BigInteger.ONE);
            this.defensoriaFacade.edit(this.defensoria);
            this.modo = Constantes.MODO_LISTADO;
            adicionarMensaje("", "La Defensoría ha sido actualizada con éxito");
            
        } catch (Exception e) {
            adicionarMensajeWarning("", "Error al actualizar la Defensoría");
        }
    }
    
    /**
     * Obtener el nombre del departamento
     * @param nidDepartamento el id del departamento
     * @return String el nombre
     */
    public String getNombreDepartamento(BigDecimal nidDepartamento) {
        return (nidDepartamento!=null)?this.departamentoFacade.find(nidDepartamento).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre de la provincia
     * @param nidProvincia el id de la provincia
     * @return String el nombre
     */
    public String getNombreProvincia(BigDecimal nidProvincia) {
        return (nidProvincia!=null)?this.provinciaFacade.find(nidProvincia).getTxtDescripcion():"";
    }
    
    /**
     * Obtener el nombre del distrito
     * @param nidDistrito el id del distrito
     * @return String el nombre
     */
    public String getNombreDistrito(BigDecimal nidDistrito) {
        return (nidDistrito!=null)?this.distritoFacade.find(nidDistrito).getTxtDescripcion():"";
    }
    
    
     /**
     * Inicializar PersonaDna 
     */
   public void initPersona(){
       this.defensoriaPersona = new DefensoriaPersona();
       
   }
     /**
     * Obtener personaldna de la inscripcion
     * @param item - PersonaDna
     */
    public void obtenerPersona(DefensoriaPersona item) {
        try {
            
           this.defensoriaPersona = item;
           
           if(item.getProfesion()==null) 
               this.defensoriaPersona.setProfesion(new Catalogo());
           if(item.getFuncion()==null) 
               this.defensoriaPersona.setFuncion(new Catalogo());
           
            /*
            this.obtenerProvinciasDna();
            this.obtenerDistritosDna();       
            */
        }catch(Exception ex) {
            
        }    
    }

     /**
     * Agrega Persona a la lista de personas de la defensoría
     */
    public void agregarPersonal() {
      try {
        this.defensoriaPersona.setFuncion(this.catalogoFacade.find(this.defensoriaPersona.getFuncion().getNidCatalogo()));
        this.defensoriaPersona.setFlgActivo(BigInteger.ONE);
        this.listaDefensoriaPersona.add(this.defensoriaPersona);
      }catch(Exception ex) {
      }
    }
    
     /**
     * Actualiza la función del personal
     */
    public void actualizarPersonal() {
        this.defensoriaPersona.setFuncion(this.catalogoFacade.find(this.defensoriaPersona.getFuncion().getNidCatalogo()));
    }
    
     /**
     * Elimina el personal de la Defensoría
     */
    public void eliminarPersona() { 
      this.listaDefensoriaPersona.remove(this.defensoriaPersona);
      this.personalRemover.add(this.defensoriaPersona);
      if(this.defensoriaPersona.isResponsable()){
            this.adicionarMensajeError("Se removió Responsable", "Debe agregar un nuevo responsable.");
        }
    }
    
       /**
     * Valida si se ingresó un responsable y los archivos solicitados
     * @param event - ComponentSystemEvent - evento posterior a la validación de componentes
     */
    public void validar(final ComponentSystemEvent event) {
            FacesContext fc = FacesContext.getCurrentInstance();
     
            DefensoriaPersona responsable = null;
           
            if(this.listaDefensoriaPersona != null && !this.listaDefensoriaPersona.isEmpty()){
                for (DefensoriaPersona personal : this.listaDefensoriaPersona) 
                    if(personal.isResponsable()) responsable = personal;
            }
            if(responsable == null){
                adicionarMensajeError("Error:", "Debe agregar un responsable");
                fc.validationFailed();
                fc.renderResponse(); 
            }           
/*
            for(DocInscripcion doc : this.adjuntos) {
            
                if(doc.getTxtNombre()==null){
                    adicionarMensajeError("Debe adjuntar los archivos solicitados","");
                    fc.validationFailed();
                    fc.renderResponse(); 
                    break;
                }
            }            */
    }
     /**
    * Validar persona
    * @param event - ComponentSystemEvent - evento generada posterior a la validación de componentes
    */
   public void validarPersona(final ComponentSystemEvent event) {
       
        FacesContext fc = FacesContext.getCurrentInstance();
        final UIComponent formulario = event.getComponent();
        final UIInput nidFuncion = (UIInput) formulario.findComponent("nidFuncion");

       if( this.defensoriaPersona.getTxtApellidoMaterno() == null || this.defensoriaPersona.getTxtApellidoMaterno().equals("") 
                || this.defensoriaPersona.getTxtApellidoPaterno() == null || this.defensoriaPersona.getTxtApellidoPaterno().equals("") 
                || this.defensoriaPersona.getTxtNombre1() == null || this.defensoriaPersona.getTxtNombre1().equals("") ){ 
            adicionarMensajeError("Faltan datos", "Faltan los datos ligados al DNI.");
            fc.validationFailed();
            fc.renderResponse(); 
        }
        
        if(nidFuncion.getValue()!=null && nidFuncion.getValue().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE)  ) {
            for(DefensoriaPersona personal : this.listaDefensoriaPersona){
                /*
                if( ( this.personaDna.getNidPersona()==null || 
                        (this.personaDna.getNidPersona()!=null && !personal.getNidPersona().equals(this.personaDna.getNidPersona())))
                         && personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) ) {
                            adicionarMensajeError("Error","Ya existe un responsable. Si desea asignar la función de Responsable primero debe remover el anterior");
                            fc.validationFailed();
                            fc.renderResponse(); 
               }
                */
                if( !personal.getTxtDocumento().equals(this.defensoriaPersona.getTxtDocumento() ) 
                         && personal.getFuncion().getNidCatalogo().equals(Constantes.CATALOGO_FUNCION_RESPONSABLE) ) {
                    adicionarMensajeError("Error","Ya existe un responsable. Si desea asignar la función de Responsable primero debe remover el anterior");
                    fc.validationFailed();
                    fc.renderResponse(); 
                }
            }
        }

    }
   
    
     /**
     * Verificar el proceso de busqueda de dni
     * @param ctx - FacesContext
     * @param component - UIComponent
     * @param value - valor del componente
     * @throws ValidatorException - excepción generada si existe error en la validación
   */
    public void verificarReniec(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
         IdentificacionReniec consultaReniec = (IdentificacionReniec)getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{identificacionReniec}", IdentificacionReniec.class);
       
        String dni = (String)value;
          
        if(dni==null || dni.length()<8)
              throw new ValidatorException(new FacesMessage("El DNI debe tener 8 dígitos"));

        if(!dni.equals(this.defensoriaPersona.getTxtDocumento())) {
            try { 

                for(DefensoriaPersona personal : this.listaDefensoriaPersona){
                    if(personal.getTxtDocumento().equals(dni)) {
                        adicionarMensajeError("Error","El DNI ya ha sido ingresado.");
                        this.initPersona();
                        return;
                   }
                }

                this.initPersona();
                                
                this.identificacionReniec.obtenerConsultaReniec(dni);
                
                if (this.identificacionReniec.getNOMBRES() != null) {
                
                  this.defensoriaPersona.setTxtDocumento(dni);
                  this.defensoriaPersona.setTxtApellidoPaterno(this.identificacionReniec.getAPPAT());
                  this.defensoriaPersona.setTxtApellidoMaterno(this.identificacionReniec.getAPMAT());

                  this.separarNombres(this.identificacionReniec.getNOMBRES()); 
                  this.defensoriaPersona.setTxtDireccion(this.identificacionReniec.getDIRECCION());

                }  else {
                     throw new ValidatorException(new FacesMessage("El DNI ingresado no existe"));
                }
                
                
            } catch (Exception ex) {
                // TODO handle custom exceptions here
            }
            
        }

    }
    
     /**
     * Separa los nombres obtenidos por reniec en los tres nombres
     * @param nombresFull - String - Nombres y Apellidos
     */
    public void separarNombres(String nombresFull) {
    
        String nombres[] = nombresFull.split(" ");

        int i = 0;    
        List<String> listaNombres = new ArrayList<>();
        boolean compuesto = false;    
        
        for(String n: nombres) {
            if(n.equals("Y") || n.equals("DE") || n.equals("DEL") || n.equals("LA") || n.equals("EL")) {
                compuesto = true;
                listaNombres.set(i-1, listaNombres.get(i-1) + " " + n);
            }  else {
                if(compuesto) {
                    compuesto = false;
                    listaNombres.set(i-1, listaNombres.get(i-1) + " " + n);
                } else {
                    listaNombres.add(n);
                    i++;
                }
            }    
           
        }
        
        this.defensoriaPersona.setTxtNombre1(listaNombres.get(0));

        if(listaNombres.size()>1)
          this.defensoriaPersona.setTxtNombre2(listaNombres.get(1));

        if(listaNombres.size()>2)
          this.defensoriaPersona.setTxtNombre3(listaNombres.get(2));

    }
    
    public List<Defensoria> getListaDefensoria() {
        return listaDefensoria;
    }

    public void setListaDefensoria(List<Defensoria> listaDefensoria) {
        this.listaDefensoria = listaDefensoria;
    }

    public BigDecimal getBusDepartamento() {
        return busDepartamento;
    }

    public void setBusDepartamento(BigDecimal busDepartamento) {
        this.busDepartamento = busDepartamento;
    }

    public BigDecimal getBusProvincia() {
        return busProvincia;
    }

    public void setBusProvincia(BigDecimal busProvincia) {
        this.busProvincia = busProvincia;
    }

    public BigDecimal getBusDistrito() {
        return busDistrito;
    }

    public void setBusDistrito(BigDecimal busDistrito) {
        this.busDistrito = busDistrito;
    }

    public String getBusCodigo() {
        return busCodigo;
    }

    public void setBusCodigo(String busCodigo) {
        this.busCodigo = busCodigo;
    }

    public Defensoria getDefensoria() {
        return defensoria;
    }

    public void setDefensoria(Defensoria defensoria) {
        this.defensoria = defensoria;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public DefensoriaPersona getDefensoriaPersona() {
        return defensoriaPersona;
    }

    public void setDefensoriaPersona(DefensoriaPersona defensoriaPersona) {
        this.defensoriaPersona = defensoriaPersona;
    }

    public List<DefensoriaPersona> getListaDefensoriaPersona() {
        return listaDefensoriaPersona;
    }

    public void setListaDefensoriaPersona(List<DefensoriaPersona> listaDefensoriaPersona) {
        this.listaDefensoriaPersona = listaDefensoriaPersona;
    }

    public List<DefensoriaPersona> getPersonalRemover() {
        return personalRemover;
    }

    public void setPersonalRemover(List<DefensoriaPersona> personalRemover) {
        this.personalRemover = personalRemover;
    }
    
    
}