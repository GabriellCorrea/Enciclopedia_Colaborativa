package modelos;

public class Topico {
    private int idtopico;
    private String nometopico, textotopico;
    private int idArtigo;

    public Topico(String nometopico, String textotopico, int idArtigo) {
        if ( !(nometopico.isEmpty()) && !(textotopico.isEmpty()) ) {
            this.nometopico = nometopico;
            this.textotopico = textotopico;
        }
    }

    public Topico(int idTopico, String nometopico, String textotopico, int idArtigo) {
        if ( !(nometopico.isEmpty()) && !(textotopico.isEmpty()) ) {
            this.nometopico = nometopico;
            this.textotopico = textotopico;
        }
    }

    public int getIdTopico() {
        return idtopico;
    }

    public void setIdTopico(int idtopico) {
        this.idtopico = idtopico;
    }

    public void editarNomeTopico(String nometopico) {
        if ( !(nometopico.isEmpty()) )
            this.nometopico = nometopico;
    }
    public String getNomeTopico() {
        return nometopico;
    }

    public void editarTextoTopico(String textotopico) {
        if ( !(textotopico.isEmpty()) )
            this.textotopico = textotopico;
    }

    public String getTextoTopico() {
        return textotopico;
    }
}