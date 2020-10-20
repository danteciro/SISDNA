package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import pe.gob.mimp.sisdna.modelo.CursoProgramacion;

/**
 * Clase: CursoProgramacionFacade.java<br>
 * Clase que implementa la interface de la programación del curso.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class CursoProgramacionFacade extends AbstractFacade<CursoProgramacion> implements CursoProgramacionFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CursoProgramacionFacade() {
        super(CursoProgramacion.class);
    }

    @Override
    public List<CursoProgramacion> cursosParaPublicar() {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicar", CursoProgramacion.class);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarDep(BigDecimal nidDepartamento) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarDep", CursoProgramacion.class);
      query.setParameter("nidDepartamento", nidDepartamento);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarProv(BigDecimal nidProvincia) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarProv", CursoProgramacion.class);
      query.setParameter("nidProvincia", nidProvincia);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarDis(BigDecimal nidDistrito) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarDis", CursoProgramacion.class);
      query.setParameter("nidDistrito", nidDistrito);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarCurso(BigDecimal nidCurso) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarCurso", CursoProgramacion.class);
      query.setParameter("nidCurso", nidCurso);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarDepCurso(BigDecimal nidDepartamento, BigDecimal nidCurso) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarDepCurso", CursoProgramacion.class);
      query.setParameter("nidCurso", nidCurso);
      query.setParameter("nidDepartamento", nidDepartamento);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarProvCurso(BigDecimal nidProvincia, BigDecimal nidCurso) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarProvCurso", CursoProgramacion.class);
      query.setParameter("nidCurso", nidCurso);
      query.setParameter("nidProvincia", nidProvincia);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<CursoProgramacion> cursosParaPublicarDisCurso(BigDecimal nidDistrito, BigDecimal nidCurso) {
      TypedQuery<CursoProgramacion> query = em.createNamedQuery("CursoProgramacion.cursosParaPublicarDisCurso", CursoProgramacion.class);
      query.setParameter("nidCurso", nidCurso);
      query.setParameter("nidDistrito", nidDistrito);
      return  (List<CursoProgramacion>)query.getResultList();
    }
    
    @Override
    public List<Object[]> reporteCapacitacion(  Integer reporte,
                                                BigDecimal nidDepartamento, 
                                                BigDecimal nidProvincia, 
                                                BigDecimal nidDistrito,
                                                Integer anho,
                                                String desde,
                                                String hasta,
                                                BigDecimal nidCurso,
                                                BigDecimal nidTutor,
                                                Integer aprueba) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("PROC_REP_CAP_SEDE");
        query.registerStoredProcedureParameter("REPORTE_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DEP_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_PROV_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DIST_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ANHO_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("DESDE_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("HASTA_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_CURSO_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_TUTOR_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ESTADO_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("RES", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("REPORTE_IN", reporte);
        query.setParameter("NID_DEP_IN", nidDepartamento);
        query.setParameter("NID_PROV_IN", nidProvincia);
        query.setParameter("NID_DIST_IN", nidDistrito);
        query.setParameter("ANHO_IN", anho);
        query.setParameter("DESDE_IN", desde);
        query.setParameter("HASTA_IN", hasta);
        query.setParameter("NID_CURSO_IN", nidCurso);
        query.setParameter("NID_TUTOR_IN", nidTutor);
        query.setParameter("ESTADO_IN", aprueba);
        query.execute();
        return (List<Object[]>)query.getOutputParameterValue("RES");
        
    }
    
    @Override
    public Map<String, Object> reporteCapacitacionPorCurso(Integer reporte,
                                                    BigDecimal nidDepartamento, 
                                                    BigDecimal nidProvincia, 
                                                    BigDecimal nidDistrito,
                                                    Integer anho,
                                                    String desde,
                                                    String hasta,
                                                    BigDecimal nidCurso,
                                                    BigDecimal nidTutor,
                                                    Integer aprueba) {
        
        StoredProcedureQuery query = em.createStoredProcedureQuery("PROC_REP_CAP_X_CURSO");
        query.registerStoredProcedureParameter("REPORTE_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DEP_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_PROV_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DIST_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ANHO_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("DESDE_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("HASTA_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_CURSO_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_TUTOR_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ESTADO_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("RESCOL", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("RES", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("REPORTE_IN", reporte);
        query.setParameter("NID_DEP_IN", nidDepartamento);
        query.setParameter("NID_PROV_IN", nidProvincia);
        query.setParameter("NID_DIST_IN", nidDistrito);
        query.setParameter("ANHO_IN", anho);
        query.setParameter("DESDE_IN", desde);
        query.setParameter("HASTA_IN", hasta);
        query.setParameter("NID_CURSO_IN", nidCurso);
        query.setParameter("NID_TUTOR_IN", nidTutor);
        query.setParameter("ESTADO_IN", aprueba);
        query.execute();
        Map<String, Object> resp = new HashMap<>();
        resp.put("cursos", (String)query.getOutputParameterValue("RESCOL"));
        resp.put("lista", (List<Object[]>)query.getOutputParameterValue("RES"));
        return resp;        
    }
    
    @Override
    public List<Object[]> reporteFortalecidas( 
                                    BigDecimal nidDepartamento, 
                                    BigDecimal nidProvincia,
                                    BigDecimal nidDistrito, 
                                    String desde,
                                    String hasta,
                                    Integer anho) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("PROC_REP_FORTALECIDAS");
        query.registerStoredProcedureParameter("NID_DEP_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_PROV_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NID_DIST_IN", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("DESDE_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("HASTA_IN", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ANHO_IN", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("RES", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("NID_DEP_IN", nidDepartamento);
        query.setParameter("NID_PROV_IN", nidProvincia);
        query.setParameter("NID_DIST_IN", nidDistrito);
        query.setParameter("ANHO_IN", anho);
        query.setParameter("DESDE_IN", desde);
        query.setParameter("HASTA_IN", hasta);
        query.execute();
        
        return (List<Object[]>)query.getOutputParameterValue("RES");
        
    }
}
