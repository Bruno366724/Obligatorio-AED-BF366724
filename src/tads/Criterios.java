package tads;

import dominio.Despliegue;

public class Criterios {

    private Criterios() { }

    public static final Criterio POR_ID =
        (Despliegue a, Despliegue b) -> a.getId().compareTo(b.getId());

    public static final Criterio POR_CODIGO =
        (Despliegue a, Despliegue b) -> a.getCodigo().compareTo(b.getCodigo());
}