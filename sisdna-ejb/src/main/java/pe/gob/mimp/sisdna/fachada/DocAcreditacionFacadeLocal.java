package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.DocAcreditacion;

@Local
public interface DocAcreditacionFacadeLocal {

    void create(DocAcreditacion docAcreditacion);

    void edit(DocAcreditacion docAcreditacion);

    void remove(DocAcreditacion docAcreditacion);

    DocAcreditacion find(Object id);

    List<DocAcreditacion> findAll();

    List<DocAcreditacion> findRange(int[] range);

    int count();
    
    List<DocAcreditacion> findAllByField(Object field, Object value);
    
}
