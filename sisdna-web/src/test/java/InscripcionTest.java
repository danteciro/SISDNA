/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigInteger;
import javax.ejb.EJB;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InscripcionTest {
        
   
    public InscripcionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void pruebaReniec() {
           /*
        try { // Call Web Service Operation
            
               ReniecConsultaDni service = new ReniecConsultaDni();
                ReniecConsultaDniPortType port = service.getReniecConsultaDniHttpsSoap11Endpoint();
                // TODO initialize WS operation arguments here
                PeticionConsulta datosPersona = new PeticionConsulta();
                datosPersona.setNuDniConsulta("17622713");
                datosPersona.setNuDniUsuario("47352969");
                datosPersona.setNuRucUsuario("20336951527");
                datosPersona.setPassword("47352969"); 
                ResultadoConsulta result = port.consultar(datosPersona);
                System.out.println("Result = "+result.getCoResultado());
                System.out.println(result.getDatosPersona().getApPrimer());
                System.out.println(result.getDatosPersona().getApSegundo());
                System.out.println(result.getDatosPersona().getPrenombres());
                System.out.println(result.getDatosPersona().getEstadoCivil());
                System.out.println(result.getDatosPersona().getDireccion());
                System.out.println(result.getDatosPersona().getUbigeo());
          
            
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
*/
    }
    @Test
    public void verificarInpeJudiciales() {
        /*
        try { // Call Web Service Operation
            INPEAJudiciales service = new INPEAJudiciales();
            INPEAJudicialesPortType port = service.getINPEAJudicialesHttpsSoap11Endpoint();
            // TODO initialize WS operation arguments here
            String apepat = "ALCANTARA";
            String apemat = "FRANCIA";
            String nombres = "MARIANO ALONSO";
            // TODO process result here
            String result = port.getAntecedenteJudicial(apepat, apemat, nombres);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
*/
    }
    
     @Test
     public void ingresarTabla() {
         System.out.print("INICIADO INSERT");
     /*   try {  
       // DocumentoInscripcionFacadeLocal docInscripcionFacade = new DocumentoInscripcionFacade();
        DocumentoInscripcion doc = new DocumentoInscripcion();
        doc.setNidDoc(BigInteger.valueOf(1));
        doc.setTxtNombre("hola");
        this.docInscripcionFacade.create(doc);
        }catch(Exception ex) {
            System.out.print(ex.getMessage());
        }*/
     }
}
