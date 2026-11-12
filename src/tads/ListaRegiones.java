package tads;

import dominio.Region;

public class ListaRegiones {

    private NodoRegion primero;

    // O(1): inserta al principio (la letra no pide orden para regiones).
    public void agregar(Region r) {
        NodoRegion nuevo = new NodoRegion(r);
        nuevo.siguiente = primero;
        primero = nuevo;
    }

    // O(R), R = cantidad de regiones.
    public Region buscar(String codigo) {
        for (NodoRegion n = primero; n != null; n = n.siguiente) {
            if (n.dato.getCodigo().equals(codigo)) return n.dato;
        }
        return null;
    }
}