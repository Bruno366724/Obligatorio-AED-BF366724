package tads;

import dominio.Region;

public class NodoRegion {

    public Region dato;
    public NodoRegion siguiente;

    public NodoRegion(Region dato) {
        this.dato = dato;
    }
}