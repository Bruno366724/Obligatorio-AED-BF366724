package tads;

import dominio.Despliegue;

public class NodoLista {

    public Despliegue dato;
    public NodoLista siguiente;
    public NodoLista anterior; // solo lo usa ListaOrdenada

    public NodoLista(Despliegue dato) {
        this.dato = dato;
    }
}