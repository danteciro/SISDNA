package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Inscripcion;

@Local
public interface InscripcionFacadeLocal {

    void create(Inscripcion inscripcion);

    void edit(Inscripcion inscripcion);

    void remove(Inscripcion inscripcion);

    Inscripcion find(Object id);

    List<Inscripcion> findAll();

    List<Inscripcion> findRange(int[] range);

    int count();
    
    List<Inscripcion> findAllByField(Object field, Object value);
    
    List<Inscripcion> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos); 
     
    List<Inscripcion> obtenerPorParametros(Object parametrosRecibidos);
    
    List<Inscripcion> obtenerPorParametrosObject(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
    
    Inscripcion findPorParametros(Object parametrosRecibidos);
    
    List<Inscripcion> filtrarPorConstancia(String nidConstancia);
    
    List<Inscripcion> filtrarDepartamentos(BigDecimal nidDepartamento);
    
    List<Inscripcion> filtrarProvincias(BigDecimal nidProvincia);
    
    List<Inscripcion> filtrarDistritos(BigDecimal nidDistrito);
    

    List<Inscripcion> filtrarObservadasVencidos(BigInteger estadoPorEvaluar, Integer numDias);
  
    List<Object[]> filtrarPorEvaluarPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence);
  
    List<Object[]> filtrarSubsanadasPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence);
  

    List<Inscripcion> filtrarPorConstancia(String txtConstancia , BigInteger nidCatalogo);
    
    List<Inscripcion> filtrarDepartamentos(BigDecimal nidDepartamento , BigInteger nidCatalogo);
    
    List<Inscripcion> filtrarProvincias(BigDecimal nidProvincia , BigInteger nidCatalogo);
    
    List<Inscripcion> filtrarDistritos(BigDecimal nidDistrito , BigInteger nidCatalogo);
    
    Integer obtenerNroConstancia();
}
