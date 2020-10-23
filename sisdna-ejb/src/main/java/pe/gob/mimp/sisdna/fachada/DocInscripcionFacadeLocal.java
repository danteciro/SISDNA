package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.DocInscripcion;


@Local
public interface DocInscripcionFacadeLocal {

    void create(DocInscripcion docInscripcion);

    void edit(DocInscripcion docInscripcion);

    void remove(DocInscripcion docInscripcion);

    DocInscripcion find(Object id);

    List<DocInscripcion> findAll();

    List<DocInscripcion> findRange(int[] range);

    int count();
    
    List<DocInscripcion> findAllByField(Object field, Object value);
    
}
