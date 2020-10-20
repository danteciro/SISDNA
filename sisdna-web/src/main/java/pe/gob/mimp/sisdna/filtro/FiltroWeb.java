package pe.gob.mimp.sisdna.filtro;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pe.gob.mimp.seguridad.administrado.UsuarioAdministrado;
import pe.gob.mimp.seguridad.filtro.FiltroAbstracto;

@WebFilter(filterName = "FiltroWeb", urlPatterns = {"/*"})
public class FiltroWeb extends FiltroAbstracto
{
    public FiltroWeb() 
    {
        getRutasDesprotegidas().add("sisdna-web/");
        getRutasDesprotegidas().add("inscripcion_externo.xhtml");
        getRutasDesprotegidas().add("acreditacion_externo.xhtml");
        getRutasDesprotegidas().add("cursos.xhtml");
        getRutasDesprotegidas().add("curso_inscripcion.xhtml");
    }    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Cache-Control", "no-cache, no-store,  must-revalidate"); 
        res.setHeader("Pragma", "no-cache"); 
        res.setDateHeader("Expires", 0); 
        
        UsuarioAdministrado loggedIn = (UsuarioAdministrado) req.getSession().getAttribute("usuarioAdministrado");

        String urlStr = req.getRequestURL().toString().toLowerCase();

       
          
        if (true == permitir(urlStr)) {
            chain.doFilter(request, response);
        } else {
            if (loggedIn == null || (false == loggedIn.estaLogeado())) {
                res.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        }
    }
  
}
