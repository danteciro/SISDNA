package pe.gob.mimp.sisdna.administrado;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import pe.gob.mimp.core.Internet;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaInfoFacadeLocal;
import pe.gob.mimp.sisdna.fachada.DefensoriaPersonaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.InscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.MunicipalidadFacadeLocal;
import pe.gob.mimp.sisdna.fachada.ParamFiltro;
import pe.gob.mimp.sisdna.fachada.ParametroDnaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.DefensoriaInfo;
import pe.gob.mimp.sisdna.modelo.DefensoriaPersona;
import pe.gob.mimp.sisdna.modelo.Inscripcion;
import pe.gob.mimp.sisdna.modelo.Municipalidad;
import pe.gob.mimp.sisdna.modelo.ParametroDna;
import pe.gob.mimp.sisdna.modelo.PersonaDna;
import pe.gob.mimp.sisdna.util.Constantes;


@Named(value = "migracionAdministrado")
@ViewScoped
public class MigracionAdministrado extends AdministradorAbstracto implements Serializable{
    
    @EJB
    private InscripcionFacadeLocal inscripcionFacade;
    
    @EJB
    private PersonaDnaFacadeLocal personaDnaFacade;
    
    @EJB
    private DefensoriaFacadeLocal defensoriaFacade;
  
    @EJB
    private DefensoriaInfoFacadeLocal defensoriaInfoFacade;
  
    @EJB
    private DefensoriaPersonaFacadeLocal defensoriaPersonaFacade;

    @EJB
    private ProvinciaFacadeLocal fachadaProvincia;
    
    @EJB
    private DistritoFacadeLocal fachadaDistrito;

    @EJB
    private ParametroDnaFacadeLocal parametroFacade;

    @EJB
    private MunicipalidadFacadeLocal municipalidadFacade;

    public void migrar() {
     
        try {
            UsuarioAdministrado usuarioAdministrado = (UsuarioAdministrado) getFacesContext().getApplication().evaluateExpressionGet(getFacesContext(), "#{usuarioAdministrado}", UsuarioAdministrado.class);
       
            Catalogo estadoNueva = new Catalogo();
            estadoNueva.setNidCatalogo(Constantes.CATALOGO_ESTADO_NUEVO);
          
            Catalogo estadoInscrita = new Catalogo();
            estadoInscrita.setNidCatalogo(Constantes.CATALOGO_ESTADO_INSCRITA);
            
            Catalogo estadoDenegada = new Catalogo();
            estadoDenegada.setNidCatalogo(Constantes.CATALOGO_ESTADO_DENEGADA);
           
           
            Catalogo estadoDnaNoInscrita = new Catalogo();
            estadoDnaNoInscrita.setNidCatalogo(Constantes.CATALOGO_DNA_NO_INSCRITA);
            
            Catalogo estadoDnaInscrita = new Catalogo();
            estadoDnaInscrita.setNidCatalogo(Constantes.CATALOGO_DNA_INSCRITA);
            
            Catalogo estadoDnaRegistrada = new Catalogo();
            estadoDnaRegistrada.setNidCatalogo(Constantes.CATALOGO_DNA_REGISTRADA);
            
            Catalogo estadoDnaNoFunciona = new Catalogo();
            estadoDnaNoFunciona.setNidCatalogo(Constantes.CATALOGO_DNA_NO_FUNCIONA);
            
            
          
            Catalogo responsable = new Catalogo();
            responsable.setNidCatalogo(Constantes.CATALOGO_FUNCION_RESPONSABLE);
           
            Catalogo promotor = new Catalogo();
            promotor.setNidCatalogo(Constantes.CATALOGO_FUNCION_PROMOTOR);
           
            Catalogo defensor = new Catalogo();
            defensor.setNidCatalogo(Constantes.CATALOGO_FUNCION_DEFENSOR);
           
            Catalogo apoyo = new Catalogo();
            apoyo.setNidCatalogo(Constantes.CATALOGO_FUNCION_PERSONAL_APOYO);
           
            Catalogo origen = new Catalogo();
            Catalogo profesion = new Catalogo();
            
            List<ParametroDna> parametro = parametroFacade.findAllByField("cidParametro", "RUTA_MDB");
            if(parametro.size()>0) {
          
                    Database db = DatabaseBuilder.open(new File(parametro.get(0).getTxtValor()));
                    Table table = db.getTable("dna");

                    int i=0;

                    for(Row row : table) {
                      i++;
                       System.out.println("ÍNDICE: ");
                       System.out.println(i);
                      Defensoria d = new Defensoria();
                      d.setTxtConstancia((String)row.get("codigo"));
                      d.setTxtNombre((String)row.get("dna"));
                      d.setTxtTipo(Constantes.TIPO_SEDE_CENTRAL);

                      if(((String)row.get("modelo")).equals("01"))
                        origen.setNidCatalogo(BigInteger.valueOf(160));//PROVINCIAL 
                      else if(((String)row.get("modelo")).equals("02"))
                        origen.setNidCatalogo(BigInteger.valueOf(161));//DISTRITAL
                      else if(((String)row.get("modelo")).equals("03"))
                        origen.setNidCatalogo(BigInteger.valueOf(162));//ESCOLAR
                      else if(((String)row.get("modelo")).equals("04"))
                        origen.setNidCatalogo(BigInteger.valueOf(163));//COMUNAL
                      else if(((String)row.get("modelo")).equals("05"))
                        origen.setNidCatalogo(BigInteger.valueOf(164));//PARROQUIAL
                      else if(((String)row.get("modelo")).equals("09"))
                        origen.setNidCatalogo(BigInteger.valueOf(165));//CENTRO POBLADO
                      else
                        origen.setNidCatalogo(BigInteger.valueOf(166));//OTROS

                      d.setOrigen(origen);

                      d.setMigrado(1);

                      d.setFecRegistro((Date)row.get("f_registro"));
                      d.setFlgActivo(BigInteger.ONE);

                      String ubigeo = (String)row.get("ubigeo");
                      
                    
                    
                      if(ubigeo!=null && ubigeo.length()>=2)
                        d.setNidDepartamento(BigDecimal.valueOf(Long.valueOf(ubigeo.substring(0,2))));

                      if(ubigeo!=null && ubigeo.length()>=4) {
                        List<Provincia> listaProvincias = this.fachadaProvincia.findAllByField("cidProvincia", ubigeo.substring(0,4));
                        if(listaProvincias!=null && listaProvincias.size()>0)
                            d.setNidProvincia(listaProvincias.get(0).getNidProvincia());
                      }

                      if(ubigeo!=null && ubigeo.length()>=6) {
                        List<Distrito> listaDistritos = this.fachadaDistrito.findAllByField("cidDistrito", ubigeo.substring(0,6));
                        if(listaDistritos!=null && listaDistritos.size()>0) {
                            d.setNidDistrito(listaDistritos.get(0).getNidDistrito());
                            
                            if(((String)row.get("modelo")).equals("01") ) 
                              d.setTxtEntidad("Municipalidad Provincial de " + listaDistritos.get(0).getTxtDescripcion());
                            
                            if(((String)row.get("modelo")).equals("02"))
                              d.setTxtEntidad("Municipalidad Distrital de " + listaDistritos.get(0).getTxtDescripcion());
                            
                        } else
                           System.out.println("DISTRITO: " + ubigeo); 
                      }

                      d.setEstadoRegistro((String)row.get("estado_registro"));

                      if(((String)row.get("estado_registro")).equals("7")) 
                         d.setEstado(estadoDnaInscrita);
                      if(((String)row.get("estado_registro")).equals("9")) 
                        d.setEstado(estadoDnaNoInscrita);
                      if(((String)row.get("estado_registro")).equals("1")) 
                        d.setEstado(estadoDnaRegistrada);
                      if(((String)row.get("estado_registro")).equals("a")) 
                        d.setEstado(estadoDnaNoFunciona);

                       d.setPim((String)row.get("pim"));
                       d.setPim2019((String)row.get("pi2019"));
                       d.setDefF((Short)row.get("def_f"));
                       d.setDefM((Short)row.get("def_m"));
                       d.setPromdefF((Short)row.get("promdef_m"));
                       d.setPromdefM((Short)row.get("promdef_f"));
                       d.setOtrosF((Short)row.get("otros_f"));
                       d.setOtrosM((Short)row.get("otros_m"));
                       d.setFecInscripcion((Date)row.get("f_registro"));
                       d.setFecAcreditacion((Date)row.get("f_acreditacion"));
                       d.setFecOrdenanza((Date)row.get("f_rof"));
                       d.setTxtNroOrdenanza((String)row.get("rof"));
                       d.setLatitud((String)row.get("latitud"));
                       d.setLongitud((String)row.get("longitud"));
                       d.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                       d.setTxtPc(Internet.obtenerNombrePC());
                       d.setTxtIp(Internet.obtenerIPPC());
                       d.setFecRegistro((row.get("f_registro")==null)?(new Date()):(Date)row.get("f_registro"));
                      
                       if( ((String)row.get("codigo")).equals("04120"))
                         System.out.println("04120");
                       DefensoriaInfo defensoriaInfo = new DefensoriaInfo();
                       defensoriaInfo.setFecRegistro((row.get("f_registro")==null)?(new Date()):(Date)row.get("f_registro"));
                       defensoriaInfo.setTxtDocumento((String)row.get("doc_creacion"));
                       defensoriaInfo.setTxtDireccion((row.get("direccion")==null)?" ":(String)row.get("direccion"));
                       defensoriaInfo.setTxtCorreo((String)row.get("email"));
                       defensoriaInfo.setDias((String)row.get("horario"));
                       defensoriaInfo.setTxtTelefono((String)row.get("fono1"));
                       defensoriaInfo.setFlgActivo(BigInteger.ONE);

                       defensoriaInfo.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                       defensoriaInfo.setTxtPc(Internet.obtenerNombrePC());
                       defensoriaInfo.setTxtIp(Internet.obtenerIPPC());
                       
                       this.defensoriaInfoFacade.create(defensoriaInfo);
                       
                       d.setDefensoriaInfo(defensoriaInfo);
                       this.defensoriaFacade.create(d);

                      if(((String)row.get("estado_registro")).equals("7")) {
                         Inscripcion inscripcion = new Inscripcion();
                         inscripcion.setDna(d);
                         inscripcion.setFecRegistro((row.get("f_registro")==null)?(new Date()):(Date)row.get("f_registro"));
                         inscripcion.setTxtDocumento((String)row.get("doc_creacion"));
                         inscripcion.setTxtDireccion((String)row.get("direccion"));
                         inscripcion.setTxtCorreo((String)row.get("email"));
                         inscripcion.setDias((String)row.get("horario"));
                         inscripcion.setTxtTelefono((String)row.get("fono1"));
                         inscripcion.setEstado(estadoInscrita);
                         inscripcion.setFlgActivo(BigInteger.ONE);
                         inscripcion.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                         inscripcion.setTxtPc(Internet.obtenerNombrePC());
                         inscripcion.setTxtIp(Internet.obtenerIPPC());

                         this.inscripcionFacade.create(inscripcion);
                       }

                    }

                    table = db.getTable("defensores");

                    for(Row row : table) {

                       List<Defensoria> listaDefensoria = this.defensoriaFacade.findAllByField("txtConstancia", (String)row.get("codigo_dna"));

                        if(listaDefensoria.size()>0) {
                            if(row.get("dni")==null || row.get("apellido")==null)
                                continue;
                            
                            DefensoriaPersona dp = new DefensoriaPersona();
                          
                            String[] apellidos = ((String)row.get("apellido")).split(" ");

                            if(apellidos.length>0)
                                dp.setTxtApellidoPaterno(apellidos[0]);
                            if(apellidos.length>1)
                                dp.setTxtApellidoMaterno(apellidos[1]);

                            if(dp.getTxtApellidoMaterno()==null)
                                continue;
                        
                            this.separarNombres((String)row.get("nombres"), dp);
                            dp.setTxtSexo(((String)row.get("sexo")).toUpperCase());

                            if(row.get("edad")!=null)
                                dp.setEdad(new BigInteger(row.get("edad")+""));

                           switch ((String)row.get("cargo")) {
                               case "1":
                                   dp.setFuncion(responsable);
                                   break;
                               case "2":
                                   dp.setFuncion(defensor);
                                   break;
                               case "3":
                                   dp.setFuncion(promotor);
                                   break;
                               case "4":
                                   dp.setFuncion(apoyo);
                                   break;
                               default:
                                   continue;
                           }
                            
                            if(((String)row.get("ocupacion")).equals("03")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(180));
                                dp.setProfesion(profesion);
                            } else if(((String)row.get("ocupacion")).equals("04")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(181));
                                dp.setProfesion(profesion);
                            } else if(((String)row.get("ocupacion")).equals("05")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(182));
                                dp.setProfesion(profesion);
                            } else if(((String)row.get("ocupacion")).equals("06")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(183));
                                dp.setProfesion(profesion);
                            } else if(((String)row.get("ocupacion")).equals("07")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(184));
                                dp.setProfesion(profesion);
                            } else if(((String)row.get("ocupacion")).equals("10")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(185));
                                dp.setProfesion(profesion);
                            } else if(((String)row.get("ocupacion")).equals("12")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(186));
                                dp.setProfesion(profesion);
                            }  else if(((String)row.get("ocupacion")).equals("13")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(187));
                                dp.setProfesion(profesion);
                            }  else if(((String)row.get("ocupacion")).equals("17")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(188));
                                dp.setProfesion(profesion);
                            }  else if(((String)row.get("ocupacion")).equals("18")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(189));
                                dp.setProfesion(profesion);
                            }  else if(((String)row.get("ocupacion")).equals("23")) {
                                profesion.setNidCatalogo(BigInteger.valueOf(190));
                                dp.setProfesion(profesion);
                            }  else {
                                profesion.setNidCatalogo(BigInteger.valueOf(191));
                                dp.setProfesion(profesion);
                            } 

                            dp.setDefensoria(listaDefensoria.get(0));
                            
                            dp.setTxtDocumento((String)row.get("dni"));
                            dp.setTxtTelefono((String)row.get("telefono"));
                            dp.setTxtColegiatura((String)row.get("colegiatura"));
                            dp.setTxtCorreo((String)row.get("email"));
                            dp.setFlgActivo(BigInteger.ONE);
                            dp.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                            dp.setFecRegistro(new Date());
                            dp.setTxtPc(Internet.obtenerNombrePC());
                            dp.setTxtIp(Internet.obtenerIPPC());
                            dp.setInstruccion(null);
                            this.defensoriaPersonaFacade.create(dp);

                                
                            List<Inscripcion> listaInscripcion = this.inscripcionFacade.findAllByField("dna", listaDefensoria.get(0) );

                            if(listaInscripcion.size()>0) {

                                PersonaDna persona = new PersonaDna();
                                persona.setInscripcion(listaInscripcion.get(0));
                              
                                persona.setTxtApellidoPaterno(dp.getTxtApellidoPaterno());
                                persona.setTxtApellidoMaterno(dp.getTxtApellidoMaterno());
                                persona.setTxtNombre1(dp.getTxtNombre1());
                                persona.setTxtNombre2(dp.getTxtNombre2());
                                persona.setTxtNombre3(dp.getTxtNombre3());
                                persona.setEdad(dp.getEdad());

                                persona.setTxtDocumento(persona.getTxtDocumento());
                                persona.setTxtTelefono(persona.getTxtTelefono());
                                persona.setTxtColegiatura(persona.getTxtColegiatura());
                                persona.setTxtCorreo(persona.getTxtCorreo());
                                persona.setFlgActivo(BigInteger.ONE);
                                persona.setFuncion(dp.getFuncion());
                                persona.setProfesion(dp.getProfesion());
                                persona.setInstruccion(null);
                                persona.setNidUsuarioReg(usuarioAdministrado.getEntidadSeleccionada().getNidUsuario());
                                persona.setTxtPc(Internet.obtenerNombrePC());
                                persona.setTxtIp(Internet.obtenerIPPC());
                                persona.setFecRegistro(new Date());
                                this.personaDnaFacade.create(persona);

                            

                            }

                        }
                    }

            }
           
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }  
    
     public void separarNombres(String nombresFull, DefensoriaPersona persona) {
    
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
        
        persona.setTxtNombre1(listaNombres.get(0));

        if(listaNombres.size()>1)
          persona.setTxtNombre2(listaNombres.get(1));

        if(listaNombres.size()>2)
          persona.setTxtNombre3(listaNombres.get(2));

    }
                
}