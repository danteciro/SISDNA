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
import pe.gob.mimp.general.fachada.DepartamentoFacadeLocal;
import pe.gob.mimp.general.fachada.DistritoFacadeLocal;
import pe.gob.mimp.general.fachada.ProvinciaFacadeLocal;
import pe.gob.mimp.general.fachada.administrado.AdministradorAbstracto;
import pe.gob.mimp.general.modelo.Distrito;
import pe.gob.mimp.general.modelo.Provincia;
import pe.gob.mimp.sisdna.fachada.DefensoriaFacadeLocal;
import pe.gob.mimp.sisdna.fachada.InscripcionFacadeLocal;
import pe.gob.mimp.sisdna.fachada.PersonaDnaFacadeLocal;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import pe.gob.mimp.sisdna.modelo.Inscripcion;
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
    private ProvinciaFacadeLocal fachadaProvincia;
    
    @EJB
    private DistritoFacadeLocal fachadaDistrito;

    @EJB
    private DepartamentoFacadeLocal fachadaDepartamento;

    public void migrar() {
     
        try {
            
            Catalogo estadoNueva = new Catalogo();
            estadoNueva.setNidCatalogo(Constantes.CATALOGO_ESTADO_NUEVO);
          
            Catalogo estadoInscrita = new Catalogo();
            estadoInscrita.setNidCatalogo(Constantes.CATALOGO_ESTADO_INSCRITA);
            
            Catalogo estadoDenegada = new Catalogo();
            estadoDenegada.setNidCatalogo(Constantes.CATALOGO_ESTADO_DENEGADA);
           
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
            
            Database db = DatabaseBuilder.open(new File("c:\\upload\\DNA.mdb"));
            Table table = db.getTable("dna");
           
            int i=0;
         
            for(Row row : table) {
              i++;
               System.out.println("NUMEROOO");
               System.out.println(i);
              Defensoria d = new Defensoria();
              d.setTxtConstancia((String)row.get("codigo"));
              d.setTxtNombre((String)row.get("dna"));
              d.setTxtTipo(Constantes.TIPO_SEDE_CENTRAL);
              
              if(((String)row.get("modelo")).equals("01"))
                origen.setNidCatalogo(BigInteger.valueOf(60));//PROVINCIAL 
              else if(((String)row.get("modelo")).equals("02"))
                origen.setNidCatalogo(BigInteger.valueOf(61));//DISTRITAL
              else if(((String)row.get("modelo")).equals("03"))
                origen.setNidCatalogo(BigInteger.valueOf(62));//ESCOLAR
              else if(((String)row.get("modelo")).equals("04"))
                origen.setNidCatalogo(BigInteger.valueOf(63));//COMUNAL
              else if(((String)row.get("modelo")).equals("05"))
                origen.setNidCatalogo(BigInteger.valueOf(64));//PARROQUIAL
              else if(((String)row.get("modelo")).equals("09"))
                origen.setNidCatalogo(BigInteger.valueOf(65));//CENTRO POBLADO
              else
                origen.setNidCatalogo(BigInteger.valueOf(66));//OTROS
                  
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
                if(listaDistritos!=null && listaDistritos.size()>0)
                    d.setNidDistrito(listaDistritos.get(0).getNidDistrito());
              }
              
              d.setEstadoRegistro((String)row.get("estado_registro"));
              
              if(((String)row.get("estado_registro")).equals("7")) 
                 d.setEstado(estadoInscrita);
              else
                d.setEstado(estadoNueva);
           
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
            
              this.defensoriaFacade.create(d);
              
              if(((String)row.get("estado_registro")).equals("7")) {
                 Inscripcion inscripcion = new Inscripcion();
                 inscripcion.setDna(d);
                 inscripcion.setFecRegistro((Date)row.get("f_registro"));
                 inscripcion.setTxtDocumento((String)row.get("doc_creacion"));
                 inscripcion.setTxtDireccion((String)row.get("direccion"));
                 inscripcion.setTxtCorreo((String)row.get("email"));
                 inscripcion.setDias((String)row.get("horario"));
                 inscripcion.setTxtTelefono((String)row.get("fono1"));
                 inscripcion.setEstado(estadoInscrita);
                 inscripcion.setFlgActivo(BigInteger.ONE);
                 this.inscripcionFacade.create(inscripcion);
               }
             
            }

            table = db.getTable("defensores");

            for(Row row : table) {
                
               List<Defensoria> listaDefensoria = this.defensoriaFacade.findAllByField("txtConstancia", (String)row.get("codigo_dna"));
                
                if(listaDefensoria.size()>0) {
                
                    List<Inscripcion> listaInscripcion = this.inscripcionFacade.findAllByField("dna", listaDefensoria.get(0) );
                
                    if(listaInscripcion.size()>0) {
                        
                        PersonaDna persona = new PersonaDna();
                        persona.setInscripcion(listaInscripcion.get(0));
                        String[] apellidos = ((String)row.get("apellido")).split(" ");
                        
                        if(apellidos.length>0)
                            persona.setTxtApellidoPaterno(apellidos[0]);
                        if(apellidos.length>1)
                            persona.setTxtApellidoMaterno(apellidos[1]);

                        this.separarNombres((String)row.get("nombres"), persona);
                        persona.setTxtSexo(((String)row.get("sexo")).toUpperCase());
                        
                        if(row.get("edad")!=null)
                            persona.setEdad(BigInteger.valueOf((Short)row.get("edad")));
                        
                        persona.setTxtDocumento((String)row.get("dni"));
                        persona.setTxtTelefono((String)row.get("telefono"));
                        persona.setTxtColegiatura((String)row.get("colegiatura"));
                        persona.setTxtCorreo((String)row.get("email"));
                        persona.setFlgActivo(BigInteger.ONE);
                
                        if(((String)row.get("cargo")).equals("1")) {
                             persona.setFuncion(responsable);
                        }
                        if(((String)row.get("cargo")).equals("2")) {
                             persona.setFuncion(defensor);
                        }
                        if(((String)row.get("cargo")).equals("3")) {
                             persona.setFuncion(promotor);
                        }
                        if(((String)row.get("cargo")).equals("4")) {
                             persona.setFuncion(apoyo);
                        }
                          
                        if(((String)row.get("ocupacion")).equals("03")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(80));
                             persona.setProfesion(profesion);
                        } else if(((String)row.get("ocupacion")).equals("04")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(81));
                             persona.setProfesion(profesion);
                        } else if(((String)row.get("ocupacion")).equals("05")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(82));
                             persona.setProfesion(profesion);
                        } else if(((String)row.get("ocupacion")).equals("06")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(83));
                             persona.setProfesion(profesion);
                        } else if(((String)row.get("ocupacion")).equals("07")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(84));
                             persona.setProfesion(profesion);
                        } else if(((String)row.get("ocupacion")).equals("10")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(85));
                             persona.setProfesion(profesion);
                        } else if(((String)row.get("ocupacion")).equals("12")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(86));
                             persona.setProfesion(profesion);
                        }  else if(((String)row.get("ocupacion")).equals("13")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(87));
                             persona.setProfesion(profesion);
                        }  else if(((String)row.get("ocupacion")).equals("17")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(88));
                             persona.setProfesion(profesion);
                        }  else if(((String)row.get("ocupacion")).equals("18")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(89));
                             persona.setProfesion(profesion);
                        }  else if(((String)row.get("ocupacion")).equals("23")) {
                             profesion.setNidCatalogo(BigInteger.valueOf(90));
                             persona.setProfesion(profesion);
                        }  else {
                             profesion.setNidCatalogo(BigInteger.valueOf(91));
                             persona.setProfesion(profesion);
                        } 
                        this.personaDnaFacade.create(persona);
                    }

                }
            }
           
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }  
    
     public void separarNombres(String nombresFull, PersonaDna persona) {
    
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