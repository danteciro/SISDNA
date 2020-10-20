package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.InscripcionEval;


@Local
public interface InscripcionEvalFacadeLocal {
    
    void create(InscripcionEval inscripcionEval);

    void edit(InscripcionEval inscripcionEval);

    void remove(InscripcionEval inscripcionEval);

    InscripcionEval find(Object id);

    List<InscripcionEval> findAll();

    List<InscripcionEval> findRange(int[] range);

    int count();
    
    List<InscripcionEval> findAllByField(Object field, Object value);
     
    List<InscripcionEval> obtenerPorParametros(Object parametrosRecibidos);
    
    InscripcionEval findPorParametros(Object parametrosRecibidos);
    
}
