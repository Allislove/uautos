package org.restapi.uautosapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class OriginFilter extends OncePerRequestFilter {

    // Variable final para manejar los origines permitidos, estos son los unicos que tendran acceso a los endpoints de Backend
    private static final List<String> ALLOWED_ORIGINS = List.of(
            //"http://localhost:4200",
            //"https://aws-front.com",
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        boolean isValidOrigin = origin != null && ALLOWED_ORIGINS.stream().anyMatch(origin::startsWith);
        boolean isValidReferer = referer != null && ALLOWED_ORIGINS.stream().anyMatch(referer::startsWith);

        // Comentar todo el IF para cuando se necesite ver la data de los endpoints en desarrollo/local o para pruebas
        /*if (!isValidOrigin && !isValidReferer) {
            //filterChain.doFilter(request, response); // Descomentar para permitir solicitudes desde postman
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("403 - Forbidden: Invalid Origin or Referer - Andrés Romaña");
            return;
        }*/

        filterChain.doFilter(request, response);
    }


}
