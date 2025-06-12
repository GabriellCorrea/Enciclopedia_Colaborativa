package modelos;

public class PaginaArtigo extends Pagina {
    private Artigo artigo;

    public PaginaArtigo(Artigo artigo) {
        this.artigo = artigo;
    }

    public Artigo getArtigo() {
        return artigo;
    }

    public void enviarComentario(Comentario comentario) {
        this.artigo.adicionarAvaliacao(comentario);
    }

    public void imprimirComentarios() {
        if (artigo.getAvaliacoes() == null)
            return;

        System.out.println("Comentários: ");
        for (Comentario comentario : artigo.getAvaliacoes()) {
            comentario.exibirComentario();
            System.out.print("\n");
        }
    }

    @Override
    public void exibir() {
        artigo.imprimirArtigo();
        imprimirComentarios();
    }
}
