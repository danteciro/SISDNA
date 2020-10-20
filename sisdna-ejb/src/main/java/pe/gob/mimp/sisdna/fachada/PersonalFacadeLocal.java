package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.PersonaPre;

@Local
public interface PersonalFacadeLocal {

    void create(PersonaPre personal);

    void edit(PersonaPre personal);

    void remove(PersonaPre personal);

    PersonaPre find(Object id);

    List<PersonaPre> findAll();

    List<PersonaPre> findRange(int[] range);

    int count();
    
    List<PersonaPre> findAllByField(Object field, Object value);
    
    List<PersonaPre> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos); 
     
    List<PersonaPre> obtenerPorParametros(Object parametrosRecibidos);
    
    PersonaPre findPorParametros(Object parametrosRecibidos);
    
    List<PersonaPre> obtenerActivos(boolean order, String orderFiled);
    
    List<PersonaPre> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
     
    
 }
