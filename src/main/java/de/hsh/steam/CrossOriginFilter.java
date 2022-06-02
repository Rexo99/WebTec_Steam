package de.hsh.steam;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CrossOriginFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext creq, ContainerResponseContext cresp) {

        cresp.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        cresp.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        cresp.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        cresp.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept");
    }
}