package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pe.gob.mimp.general.util.ParametroNodo;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;
import java.text.SimpleDateFormat;  
/**
 * Clase: DefensoriaFacade.java<br>
 * Clase que implementa la interface de la defensoría.<br>
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class DefensoriaFacade extends AbstractFacade<Defensoria> implements DefensoriaFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DefensoriaFacade() {
        super(Defensoria.class);
    }

    @Override
    public List<Defensoria> obtenerPorParametrosAndEstado(Object parametrosRecibidos, Catalogo estadoNuevo, Catalogo estadoDenegado){
        javax.persistence.criteria.CriteriaQuery<Defensoria> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(Defensoria.class);
        ArrayList<ArrayList<Object>> parametros = ((ParametroNodo) parametrosRecibidos).getParametros();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicados = new ArrayList<Predicate>();
        Root<Defensoria> entitie = criteriaQuery.from(Defensoria.class);

        predicados.add(criteriaBuilder.or(criteriaBuilder.equal(entitie.get("estado"), estadoNuevo),criteriaBuilder.equal(entitie.get("estado"), estadoDenegado)));
                
        for (int i = 0; i < parametros.size(); i++) {
            predicados.add(criteriaBuilder.equal(entitie.get(parametros.get(i).get(0).toString()), parametros.get(i).get(1)));
        }

        criteriaQuery.select(entitie).where(predicados.toArray(new Predicate[]{}));

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }
   
    
     public int getNroConstancia(BigDecimal nidDepartamento) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Defensoria> rt = cq.from(Defensoria.class);
        List<Predicate> predicados = new ArrayList<>();
        predicados.add(criteriaBuilder.equal(rt.get("nidDepartamento"), nidDepartamento));
        predicados.add(criteriaBuilder.equal(rt.get("migrado"), 0));
      
        cq.select(criteriaBuilder.count(rt)).where(predicados.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
     
    @Override
    public List<Object[]> obtenerDefensoriasNoSupervisadas(BigDecimal nidDepartamento, BigDecimal nidProvincia, BigDecimal nidDistrito,
                                                            Date fechaDesde, Date fechaHasta, String codigo) {
        
         
         String cad = "";
         String cadFecha = "";
         
         if(codigo!=null)
            cad = " and d.txt_constancia=?";
         else {
              if(nidDepartamento!=null)
                cad = cad + " and d.nid_departamento=?";
             if(nidProvincia!=null)
                cad = cad + " and d.nid_provincia=?";
             if(nidDistrito!=null)
                cad = cad + " and d.nid_distrito=?";
         }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        
         if(fechaDesde!=null)
            cadFecha = " and s.fecha>=to_date('" + formatter.format(fechaDesde) + "','DD/MM/YYYY')";
         if(fechaHasta!=null)
            cadFecha = cadFecha + " and s.fecha<=to_date('" + formatter.format(fechaHasta) + "','DD/MM/YYYY')";
      
        
         String cadprin = "SELECT (g.txt_descripcion||' - '||p.txt_descripcion||' - '||dis.txt_descripcion) ubi, d.txt_constancia, d.txt_nombre " +
                            "FROM defensoria d LEFT JOIN supervision s ON d.nid_dna = s.nid_dna " + cadFecha + 
                            " inner join GENERAL.departamento g on d.nid_departamento = g.nid_departamento " +
                            " inner join GENERAL.provincia p on d.nid_departamento = p.nid_provincia " +
                            " inner join GENERAL.distrito dis on d.nid_distrito = dis.nid_distrito " +
                            " WHERE s.nid_dna IS NULL ";
      
         cad = cadprin + cad + " order by ubi";
      
         Query q = em.createNativeQuery(cad);
         int x=1;
         
         if(codigo!=null)
            q.setParameter(x++, codigo);
         else {
            if(nidDepartamento!=null)
              q.setParameter(x++, nidDepartamento);
            if(nidProvincia!=null)
              q.setParameter(x++, nidProvincia);
            if(nidDistrito!=null)
              q.setParameter(x++, nidDistrito);
            
          }
         
        
        return q.getResultList();
    }
   
}
