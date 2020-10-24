package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Acreditacion;

@Local
public interface AcreditacionFacadeLocal {
    void create(Acreditacion inscripcion);

    void edit(Acreditacion inscripcion);

    void remove(Acreditacion inscripcion);

    Acreditacion find(Object id);

    List<Acreditacion> findAll();

    List<Acreditacion> findRange(int[] range);

    int count();
    
    List<Acreditacion> findAllByField(Object field, Object value);
     
    List<Acreditacion> obtenerPorParametros(Object parametrosRecibidos);
    
    List<Acreditacion> obtenerPorParametrosObject(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
    
    Acreditacion findPorParametros(Object parametrosRecibidos);
    
    List<Acreditacion> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos); 
    
    List<Acreditacion> filtrarPorConstancia(String nidConstancia);
    
    List<Acreditacion> filtrarDepartamentos(BigDecimal nidDepartamento);
    
    List<Acreditacion> filtrarProvincias(BigDecimal nidProvincia);
    
    List<Acreditacion> filtrarDistritos(BigDecimal nidDistrito);
    
    List<Acreditacion> filtrarObservadasVencidos(BigInteger estadoPorEvaluar, Integer numDias);
  
    List<Object[]> filtrarPorEvaluarPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence);
  
    List<Object[]>  filtrarSubsanadasPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence);
  
}
