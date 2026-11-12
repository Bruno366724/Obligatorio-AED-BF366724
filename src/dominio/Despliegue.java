package dominio;

import sistema.Modalidad;

public class Despliegue {

    private final String id;
    private final String codigo;
    private final String descripcion;
    private final boolean cuantizado;
    private final Modalidad modalidad;

    public Despliegue(String id, String codigo, String descripcion,
                      boolean cuantizado, Modalidad modalidad) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cuantizado = cuantizado;
        this.modalidad = modalidad;
    }

    public String getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public boolean isCuantizado() { return cuantizado; }
    public Modalidad getModalidad() { return modalidad; }

    // Formato de salida: id;codigo;descripcion;cuantizado;modalidad
    public String datos() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(';')
          .append(codigo).append(';')
          .append(descripcion).append(';')
          .append(cuantizado).append(';')
          .append(modalidad.getTexto());
        return sb.toString();
    }
}
