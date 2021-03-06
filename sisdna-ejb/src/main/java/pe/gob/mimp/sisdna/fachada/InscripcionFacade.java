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
import pe.gob.mimp.sisdna.modelo.Inscripcion;


@Stateless
public class InscripcionFacade extends AbstractFacade<Inscripcion> implements InscripcionFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InscripcionFacade() {
        super(Inscripcion.class);
    }

    @Override
    public Integer obtenerNroConstancia(){
        Query q = em.createNativeQuery("SELECT SQ_CONSTANCIA_INSCRIPCION.nextval from DUAL");
        BigDecimal result=(BigDecimal)q.getSingleResult();   
        return result.intValue();
    }
    
    @Override
    public List<Inscripcion> filtrarPorConstancia(String nidConstancia) {
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarPorConstancia", Inscripcion.class);
      query.setParameter("txtConstancia", nidConstancia);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarDepartamentos(BigDecimal nidDepartamento){
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarDepartamentos", Inscripcion.class);
      query.setParameter("nidDepartamento", nidDepartamento);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarProvincias(BigDecimal nidProvincia){
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarProvincias", Inscripcion.class);
      query.setParameter("nidProvincia", nidProvincia);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarDistritos(BigDecimal nidDistrito) {
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarDistritos", Inscripcion.class);
      query.setParameter("nidDistrito", nidDistrito);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarObservadasVencidos(BigInteger estadoPorEvaluar, Integer numDias) {
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarObservadasVencidos", Inscripcion.class);
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -numDias);
      query.setParameter("estadoPorEvaluar", estadoPorEvaluar);
     query.setParameter("hoy", cal.getTime());
      return  (List<Inscripcion>)query.getResultList();
    }
 
    @Override
    public List<Object[]> filtrarPorEvaluarPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence) {
         String cad = "SELECT d.txt_constancia, d.txt_nombre, to_char(a.fec_registro + " + vence + ",'dd/MM/YYYY' ) FROM inscripcion a inner join defensoria d on a.nid_dna = d.nid_dna WHERE a.nid_estado = "
                        + estadoPorEvaluar.intValue() 
                        + " and trunc(sysdate) >= TRUNC(a.fec_registro + " + numDias + " )";

         Query q = em.createNativeQuery(cad);
         return q.getResultList();
    }
   
  
   
    @Override
    public List<Object[]> filtrarSubsanadasPorVencer(BigInteger estadoPorEvaluar, Integer numDias, Integer vence) {
        String cad = "SELECT d.txt_constancia, d.txt_nombre, to_char(a.fec_subsanado + " + vence + ",'dd/MM/YYYY' ) FROM inscripcion a inner join defensoria d on a.nid_dna = d.nid_dna WHERE a.nid_estado = "
                        + estadoPorEvaluar.intValue() 
                        + " and trunc(sysdate) >= TRUNC(a.fec_subsanado + " + numDias + " )";

         Query q = em.createNativeQuery(cad);
         return q.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarPorConstancia(String txtConstancia , BigInteger nidCatalgo) {
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarPorConstanciaEstado", Inscripcion.class);
      query.setParameter("txtConstancia", txtConstancia);
      query.setParameter("nidCatalogo", nidCatalgo);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarDepartamentos(BigDecimal nidDepartamento, BigInteger nidCatalgo){
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarDepartamentosEstado", Inscripcion.class);
      query.setParameter("nidDepartamento", nidDepartamento);
      query.setParameter("nidCatalogo", nidCatalgo);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarProvincias(BigDecimal nidProvincia , BigInteger nidCatalgo){
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarProvinciasEstado", Inscripcion.class);
      query.setParameter("nidProvincia", nidProvincia);
      query.setParameter("nidCatalogo", nidCatalgo);
      return  (List<Inscripcion>)query.getResultList();
    }
    
    @Override
    public List<Inscripcion> filtrarDistritos(BigDecimal nidDistrito , BigInteger nidCatalgo) {
      TypedQuery<Inscripcion> query = em.createNamedQuery("Inscripcion.filtrarDistritosEstado", Inscripcion.class);
      query.setParameter("nidDistrito", nidDistrito);
      query.setParameter("nidCatalogo", nidCatalgo);
      return  (List<Inscripcion>)query.getResultList();
    }
    
}
