package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import pe.gob.mimp.sisdna.modelo.Supervision;

/**
 * Clase: SupervisionFacade.java<br>
 * Clase que implementa la interface de la supervisión.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class SupervisionFacade extends AbstractFacade<Supervision> implements SupervisionFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SupervisionFacade() {
        super(Supervision.class);
    }

    @Override
    public List<Supervision> obtenerSupervisionesPorCodigo(String codigo) {
        
        String cad = "select s from Supervision s where s.defensoria.txtConstancia=:codigo";
        Query q = em.createQuery(cad);
        q.setParameter("codigo", codigo);
     
        return (List<Supervision>)q.getResultList();
    }
  
    @Override
    public List<Supervision> obtenerSupervisiones(BigDecimal nidDepartamento, 
                                                BigDecimal nidProvincia, 
                                                BigDecimal nidDistrito, 
                                                BigInteger nidEstado,
                                                Date fechaDesde, Date fechaHasta, String codigo, BigDecimal nidSupervisor) {
        
        String cadprin = "select s from Supervision s";
        
        String cad = "";
        
        if(codigo!=null && !codigo.equals(""))
            cad = cad + " s.defensoria.txtConstancia=:codigo";
        else {  
            if(nidDepartamento!=null)
                cad = cad + " s.defensoria.nidDepartamento=:nidDepartamento";
             if(nidProvincia!=null)
                cad = cad + " and s.defensoria.nidProvincia=:nidProvincia";
             if(nidDistrito!=null)
                cad = cad + " and s.defensoria.nidDistrito=:nidDistrito";
            
        }

         if(nidEstado!=null)
            cad = cad + (!cad.equals("")?" and ":"") + " s.estado.nidCatalogo=:nidEstado";
         if(fechaDesde!=null)
            cad = cad + (!cad.equals("")?" and ":"") + " s.fecha>=:fechaDesde";
         if(fechaHasta!=null)
            cad =  cad + (!cad.equals("")?" and ":"") + " s.fecha<=:fechaHasta";

        if(nidSupervisor!=null )
            cad = cad + " s.nidSupervisor=:nidSupervisor";
        
        cad = cadprin + (!cad.equals("")?" where " + cad:"");
      
         Query q = em.createQuery(cad);
         if(codigo!=null && !codigo.equals(""))
              q.setParameter("codigo", codigo);
         else {  
             if(nidDepartamento!=null)
                q.setParameter("nidDepartamento", nidDepartamento);
             if(nidProvincia!=null)
                q.setParameter("nidProvincia", nidProvincia);
             if(nidDistrito!=null)
                q.setParameter("nidDistrito", nidDistrito);
            
         }
         
         if(nidEstado!=null)
            q.setParameter("nidEstado", nidEstado);
         if(fechaDesde!=null)
            q.setParameter("fechaDesde", fechaDesde);
         if(fechaHasta!=null)
            q.setParameter("fechaHasta", fechaHasta);
         
         if(nidSupervisor!=null )
            q.setParameter("nidSupervisor", nidSupervisor);
       
         return (List<Supervision>)q.getResultList();
    }
  
   
    @Override
    public List<Object[]> reporte(  Integer numReporte, 
                                    BigDecimal nidDepartamento, 
                                    BigDecimal nidProvincia,
                                    BigDecimal nidDistrito, 
                                    String desde,
                                    String hasta,
                                    Integer anho,
                                    Integer estadoObs, BigInteger estadoSup, BigInteger estadoDna, 
                                    BigInteger origen, BigDecimal nidSupervisor) {
        
        StoredProcedureQuery query = em.createStoredProcedureQuery("PROC_REP_SUPERVISION");
        query.registerStoredProcedureParameter("REPORTE_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DEP_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_PROV_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DIST_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ANHO_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("DESDE_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("HASTA_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_ESTADO_OBS_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_ESTADO_SUP_IN", BigInteger.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_ESTADO_DNA_IN", BigInteger.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_ORIGEN_IN", BigInteger.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_SUPERVISOR_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("RES", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("REPORTE_IN", numReporte);
        query.setParameter("NID_DEP_IN", nidDepartamento);
        query.setParameter("NID_PROV_IN", nidProvincia);
        query.setParameter("NID_DIST_IN", nidDistrito);
        query.setParameter("ANHO_IN", anho);
        query.setParameter("DESDE_IN", desde);
        query.setParameter("HASTA_IN", hasta);
        query.setParameter("NID_ESTADO_OBS_IN", estadoObs);
        query.setParameter("NID_ESTADO_SUP_IN", estadoSup);
        query.setParameter("NID_ESTADO_DNA_IN", estadoDna);
        query.setParameter("NID_ORIGEN_IN", origen);
        query.setParameter("NID_SUPERVISOR_IN", nidSupervisor);
        
        query.execute();
        
              
        return (List<Object[]>)query.getOutputParameterValue("RES");
        
    }
  
}
