package tads;

import dominio.Despliegue;

public class Pila {

    private NodoLista tope;
    private int cantidad;

    public boolean estaVacia() { return tope == null; }
    public int getCantidad() { return cantidad; }

    // O(1)
    public void apilar(Despliegue d) {
        NodoLista nuevo = new NodoLista(d);
        nuevo.siguiente = tope;
        tope = nuevo;
        cantidad++;
    }

    // O(1). Devuelve null si está vacía.
    public Despliegue desapilar() {
        if (tope == null) return null;
        Despliegue d = tope.dato;
        tope = tope.siguiente;
        cantidad--;
        return d;
    }
}