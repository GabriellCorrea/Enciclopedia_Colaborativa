import dao.PaginaPrincipalDAO;
import db.ConnectionFactory;
import modelos.*;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Usuario usuario = new Usuario("Estevão");
        Usuario ed1 = new Usuario("Enzo");
        Usuario ed2 = new Usuario("Gabriel");
        Comentario comentario = new Comentario(5, usuario, "Achei supimpa");
        Comentario comentario1 = new Comentario(5, ed2, "Do balacobaco");
        Comentario comentario2 = new Comentario(3, ed1, "Não gostei mt não");


        Date hj = new Date(2025, 6, 11);
        Topico topico = new Topico("Realmente, ele é lindo", "Não há dúvidas, ele é o mais lindo...", 1);
        Artigo artigo = new Artigo(usuario, "Estevão é lindo", Categorias.ARTE, topico, hj);
        Artigo artigo1 = new Artigo(ed2, "Gabriel é lindo", Categorias.ARTE, topico, hj);

        PaginaArtigo paginaArtigo = new PaginaArtigo(artigo);
        paginaArtigo.enviarComentario(comentario);
        paginaArtigo.enviarComentario(comentario1);
        paginaArtigo.enviarComentario(comentario2);
        paginaArtigo.exibir();
        System.out.println("Link do artigo: " + paginaArtigo.compartilharLink());

        artigo.editarTopico(ed1, topico, "Ele não é lindo", "Ei, isso é mentira!");
        artigo.editarTopico(ed2, topico, "Ele é lindo sim", "Ei, isso é verdade!");
        paginaArtigo.exibir();

        System.out.println("Media: " + artigo.getMediaAvaliacoes());

        List<Artigo> lista = new ArrayList<>();
        lista.add(artigo);
        lista.add(artigo1);

        PaginaPrincipal paginaP = new PaginaPrincipal(lista);
        paginaP.exibir();

        ConnectionFactory fabricaDeConexao = new ConnectionFactory();
        Connection connection = fabricaDeConexao.recuperaConexao();


        PaginaPrincipalDAO pgpDAO = new PaginaPrincipalDAO(connection);
        pgpDAO.salvar(paginaP);
        System.out.println("\nIDPaginaPrincipal: " + paginaP.getId());
        // pgpDAO.excluir(5);
        // pgpDAO.excluir(6);
    }
}