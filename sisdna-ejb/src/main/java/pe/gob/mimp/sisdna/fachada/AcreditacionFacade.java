package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import pe.gob.mimp.sisdna.modelo.Acreditacion;
import pe.gob.mimp.sisdna.modelo.Inscripcion;

@Stateless
public class AcreditacionFacade extends AbstractFacade<Acreditacion> implements AcreditacionFacadeLocal {
    
    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AcreditacionFacade() {
        super(Acreditacion.class);
    }
    
    @Override
    public List<Acreditacion> filtrarPorConstancia(String nidConstancia) {
      TypedQuery<Acreditacion> query = em.createNamedQuery("Acreditacion.filtrarPorConstancia", Acreditacion.class);
      query.setParameter("txtConstancia", nidConstancia);
      return  (List<Acreditacion>)query.getResultList();
    }
    
    @Override
    public List<Acreditacion> filtrarDepartamentos(BigDecimal nidDepartamento){
      TypedQuery<Acreditacion> query = em.createNamedQuery("Acreditacion.filtrarDepartamentos", Acreditacion.class);
      query.setParameter("nidDepartamento", nidDepartamento);
      return  (List<Acreditacion>)query.getResultList();
    }
    
    @Override
    public List<Acreditacion> filtrarProvincias(BigDecimal nidProvincia){
      TypedQuery<Acreditacion> query = em.createNamedQuery("Acreditacion.filtrarProvincias", Acreditacion.class);
      query.setParameter("nidProvincia", nidProvincia);
      return  (List<Acreditacion>)query.getResultList();
    }
    
    @Override
    public List<Acreditacion> filtrarDistritos(BigDecimal nidDistrito) {
      TypedQuery<Acreditacion> query = em.createNamedQuery("Acreditacion.filtrarDistritos", Acreditacion.class);
      query.setParameter("nidDistrito", nidDistrito);
      return  (List<Acreditacion>)query.getResultList();
    }
    
    
    @Override
    public List<Acreditacion>  filtrarObservadasVencidos(BigInteger estadoPorEvaluar, Integer numDias) {
              
      TypedQuery<Acreditacion> query = em.createNamedQuery("Acreditacion.filtrarObservadasVencidos", Acreditacion.class);
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -numDias);
      query.setParameter("estadoPorEvaluar", estadoPorEvaluar);
      query.setParameter("hoy", cal.getTime());
      return  (List<Acreditacion>)query.getResultList();
    }
 
    @Override
    public List<Object[]> filtrarPorEvaluarPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence) {

            
         String cad = "SELECT a.txt_constancia, d.txt_nombre, to_char(a.fec_registro + " + vence + ",'dd/MM/YYYY' ) FROM acreditacion a inner join defensoria d on a.nid_dna = d.nid_dna WHERE a.nid_estado = "
                        + estadoPorEvaluar.intValue() 
                        + " and trunc(sysdate) >= TRUNC(a.fec_registro + " + numDias + " )";

         Query q = em.createNativeQuery(cad);
         return q.getResultList();
    }
   
    @Override
    public List<Object[]> filtrarSubsanadasPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence) {
       String cad = "SELECT a.txt_constancia, d.txt_nombre, to_char(a.fec_subsanado + " + vence + ",'dd/MM/YYYY' ) FROM acreditacion a inner join defensoria d on a.nid_dna = d.nid_dna WHERE a.nid_estado = "
                        + estadoPorEvaluar.intValue() 
                        + " and trunc(sysdate) >= TRUNC(a.fec_subsanado + " + numDias + " )";

         Query q = em.createNativeQuery(cad);
         return q.getResultList();
    }
}
