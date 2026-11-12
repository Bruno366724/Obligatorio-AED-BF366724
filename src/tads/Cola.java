package tads;

import dominio.Despliegue;

public class Cola {

    private NodoLista frente;
    private NodoLista fin;
    private int cantidad;

    public boolean estaVacia() { return frente == null; }
    public int getCantidad() { return cantidad; }

    // O(1): gracias al puntero 'fin'.
    public void encolar(Despliegue d) {
        NodoLista nuevo = new NodoLista(d);
        if (fin == null) frente = nuevo;
        else fin.siguiente = nuevo;
        fin = nuevo;
        cantidad++;
    }

    // O(1). Devuelve null si está vacía.
    public Despliegue desencolar() {
        if (frente == null) return null;
        Despliegue d = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        cantidad--;
        return d;
    }

    // O(p): solo para validar el ERROR_4 de la op 11.
    public boolean contiene(Despliegue d) {
        for (NodoLista n = frente; n != null; n = n.siguiente) {
            if (n.dato == d) return true;
        }
        return false;
    }
}