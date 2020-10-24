package pe.gob.mimp.sisdna.fachada;


import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.DefensoriaInfo;

/**
 * Interface: DefensoriaInfoFacadeLocal.java<br>
 * Interface de las defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface DefensoriaInfoFacadeLocal {

    void create(DefensoriaInfo defensoriaInfo);

    List<DefensoriaInfo> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
   
 
}
