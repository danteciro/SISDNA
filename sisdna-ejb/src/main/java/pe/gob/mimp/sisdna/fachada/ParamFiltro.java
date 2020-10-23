package pe.gob.mimp.sisdna.fachada;

import java.util.ArrayList;

/**
 * Clase: ParamFiltro.java<br>
 * Clase que gestiona los parámetros necesarios para las búsquedas.<br>
 * Fecha de Creación: 20/10/2019<br>
 * 
 * @author BooleanCore
 */
public class ParamFiltro {
    
    public static final String EQUAL = "eq";
    public static final String NOTEQUAL = "noteq";
    public static final String LIKE = "like";
    public static final String MAYORIGUAL = "MAIG";
    public static final String MENORIGUAL = "MEIG";


    private ArrayList<ArrayList<Object>> parametros;

    private Object joinTable1;
    private String joinField1;
    
    
	public ParamFiltro() {
		parametros = new ArrayList<ArrayList<Object>>();
	}

	public ArrayList<ArrayList<Object>> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<ArrayList<Object>> parametros) {
		this.parametros = parametros;
	}
	
        /**
         * Agrega el nuevo parámetro
         * @param campo nombre del atributo
         * @param valor valor del atributo
         */
	public void adicionar(Object campo, Object valor)
	{
		ArrayList<Object> parametro = new ArrayList<Object>();
		parametro.add(campo);
		parametro.add(valor);
                parametro.add(EQUAL);
		getParametros().add(parametro);
	}
        
        /**
         * Agrega el nuevo parámetro
         * @param campo nombre del atributo
         * @param valor valor del atributo         
         * @param condicional condición de filtro: EQUAL, NOTEQUAL, LIKE, MAYORIGUAL, MENORIGUAL
         */
        public void adicionar(Object campo, Object valor, String condicional)
	{
		ArrayList<Object> parametro = new ArrayList<Object>();
		parametro.add(campo);
		parametro.add(valor);
                parametro.add(condicional);
		getParametros().add(parametro);
	}

    public Object getJoinTable1() {
        return joinTable1;
    }

    public void setJoinTable1(Object joinTable1) {
        this.joinTable1 = joinTable1;
    }

    public String getJoinField1() {
        return joinField1;
    }

    public void setJoinField1(String joinField1) {
        this.joinField1 = joinField1;
    }

   
    public void addJoin(Object joinTable1, String joinField1) {
        this.joinTable1 = joinTable1;
        this.joinField1 = joinField1;
    }
        
        
}
