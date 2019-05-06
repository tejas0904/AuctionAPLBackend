 package com.apl.auction.helper;
import java.io.IOException;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
 
@Named
@Provider
public class SessionInterceptor implements ContainerRequestFilter 
{
    @Context
    private HttpServletRequest servletRequest;
 
    public void filter(ContainerRequestContext requestContext) throws IOException 
    {
    	UriInfo info = requestContext.getUriInfo();
    	System.out.print("API NAME :: "+info.getPath().toString());
    	if(!requestContext.getMethod().equals("OPTIONS") && !info.getPath().toString().contains("logger") && !info.getPath().toString().contains("socket") 
    			&& !info.getPath().toString().contains("projector"))
    	{
    		
    		HttpSession session = servletRequest.getSession();
    		System.out.println(" for username :: "+session.getAttribute("email")); 
            if (session.getAttribute("email") == null || session.getAttribute("email").equals("")) 
            {    
                System.out.println("session null for :: "+session.getAttribute("email"));
                requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
            }
    	}
    }
}