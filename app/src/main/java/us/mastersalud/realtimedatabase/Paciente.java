package us.mastersalud.realtimedatabase;


public class Paciente {
    private String nombre;
    private String apellidos;
    private String grupoSanguineo;
    private String nuhsa;

    //Creamos constructor vacío (importante), constructor, getters y toString

    public Paciente() {
    }

    public Paciente(String nombre, String apellidos, String grupoSanguineo, String nuhsa) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.grupoSanguineo = grupoSanguineo;
        this.nuhsa = nuhsa;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public String getNuhsa() {
        return nuhsa;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public void setNuhsa(String nuhsa) {
        this.nuhsa = nuhsa;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", grupoSanguineo='" + grupoSanguineo + '\'' +
                ", nuhsa='" + nuhsa + '\'' +
                '}';
    }
}
