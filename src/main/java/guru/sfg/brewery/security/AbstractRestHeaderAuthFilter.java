package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//COnfigura a authenticacao , depois te que incluir na configuracao
@Slf4j
public abstract class AbstractRestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {

    public AbstractRestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected abstract String getPassword(HttpServletRequest httpServletRequest);

    protected abstract String getUserName(HttpServletRequest httpServletRequest);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String userName = getUserName(httpServletRequest);
        String password = getPassword(httpServletRequest);

        if(userName==null){
            userName="";
        }
        if(password==null){
            password="";
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName,password);

        if(!StringUtils.isEmpty(userName)){
            return this.getAuthenticationManager().authenticate(token);
        } else {
            return null;
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Request is to process authentication");
            }

            try {
                Authentication authResult = this.attemptAuthentication(request, response);
                if(authResult!=null){
                    this.successfulAuthentication(request, response, chain, authResult);
                } else {
                    chain.doFilter(request,response);
                }
            }catch (AuthenticationException e){
                unsuccessfulAuthentication(request,response,e);
            }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

    }

}
