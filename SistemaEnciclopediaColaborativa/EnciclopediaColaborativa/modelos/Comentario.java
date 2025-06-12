package modelos;

public class Comentario {
    private int idcomentario;
    private int nota;
    private Usuario autor;
    private String texto;
    private int idartigo;
    private int idpaginaartigo;

    public Comentario(int avaliacao, Usuario autor, String texto) {
        this.autor = autor;

        if (avaliacao > 0 && avaliacao <= 5)
            this.nota = avaliacao;
        if (texto.length() > 10)
            this.texto = texto;
    }

    // Para CRUD
    public Comentario(int idcomentario, int avaliacao, String texto) {
        this.idcomentario = idcomentario;

        if (avaliacao > 0 && avaliacao <= 5)
            this.nota = avaliacao;
        if (texto.length() > 10)
            this.texto = texto;
    }

    // Para CRUD
    public Comentario(int idcomentario, int avaliacao, String texto, int idartigo, int idpaginaartigo) {
        this.idcomentario = idcomentario;
        this.idartigo = idartigo;
        this.idpaginaartigo = idpaginaartigo;

        if (avaliacao > 0 && avaliacao <= 5)
            this.nota = avaliacao;
        if (texto.length() > 10)
            this.texto = texto;
    }

    // Para CRUD
    public Comentario(int idcomentario, int nota, Usuario autor, String texto, int idartigo) {
        this.idcomentario = idcomentario;
        this.nota = nota;
        this.autor = autor;
        this.texto = texto;
        this.idartigo = idartigo;
    }

    public int getIdComentario() {
        return idcomentario;
    }

    public void setIdComentario(int idcomentario) {
        this.idcomentario = idcomentario;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getTexto() {
        return texto;
    }

    public int getNota() {
        return nota;
    }

    public int getIdArtigo() {
        return idartigo;
    }

    public int getIdPaginaArtigo() {
        return idpaginaartigo;
    }

    public void exibirComentario () {
        System.out.println("Avaliação: " + nota);
        System.out.println("Autor: " + autor.getNomeUsuario());
        System.out.println("\""+texto+"\"");
    }

    public void editarComentario(Integer novaavaliacao){
        if (nota > 0 && nota <=5)
            nota = novaavaliacao;
    }
    public void editarComentario(String novotexto){
        if (!(novotexto.isEmpty()))
            texto = novotexto;
    }
    public void editarComentario(Integer novaavaliacao, String novotexto){
        if ((nota > 0 && nota <= 5) && !(novotexto.isEmpty())) {
            nota = novaavaliacao;
            texto = novotexto;
        }
    }
}
