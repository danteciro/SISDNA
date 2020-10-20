package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.AcreditacionEval;

@Local 
public interface AcreditacionEvalFacadeLocal {
    
    void create(AcreditacionEval acreditacionEval);

    void edit(AcreditacionEval acreditacionEval);

    void remove(AcreditacionEval acreditacionEval);

    AcreditacionEval find(Object id);

    List<AcreditacionEval> findAll();

    List<AcreditacionEval> findRange(int[] range);

    int count();
    
    List<AcreditacionEval> findAllByField(Object field, Object value);
     
    List<AcreditacionEval> obtenerPorParametros(Object parametrosRecibidos);
    
    AcreditacionEval findPorParametros(Object parametrosRecibidos);
    
}
