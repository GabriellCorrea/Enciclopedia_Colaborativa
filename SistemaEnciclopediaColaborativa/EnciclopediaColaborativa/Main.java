import db.ConnectionFactory;
import modelos.*;
import dao.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*Usuario usuario = new Usuario("Estevão");
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
        // pgpDAO.excluir(6);*/
// A ConnectionFactory (da resposta anterior) é necessária.
        ConnectionFactory factory = new ConnectionFactory();

        // O try-with-resources fecha a conexão automaticamente, simplificando o código.
        try (Connection conn = factory.recuperaConexao()) {

            // Instancia os DAOs necessários
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);
            ArtigoDAO artigoDAO = new ArtigoDAO(conn);
            TopicoDAO topicoDAO = new TopicoDAO(conn);

            // --- 1. CRIAR (CREATE) ---
            Usuario autor = new Usuario("Leitor Beta", new Date(System.currentTimeMillis()), "beta@teste.com", "senha123", TipoUsuario.COLABORADOR);
            usuarioDAO.salvar(autor);

            Topico topico = new Topico("Tópico Essencial", "Este é o conteúdo principal.", 0);
            Artigo artigo = new Artigo(autor, "Artigo Rápido", Categorias.TECNOLOGIA, topico, new Date(System.currentTimeMillis()));
            artigoDAO.salvar(artigo);

            Topico topico2 = new Topico("O Impacto de Gutenberg", "A prensa de tipos móveis revolucionou a disseminação do conhecimento na Europa.", 0);
            Artigo artigo2 = new Artigo(autor, "A Invenção da Imprensa", Categorias.HISTORIA, topico2, new Date(System.currentTimeMillis()));
            artigoDAO.salvar(artigo2);

            Topico topico3 = new Topico("Análise de Dom Casmurro", "O ciúme e a dúvida são temas centrais na obra de Machado de Assis.", 0);
            Artigo artigo3 = new Artigo(autor, "O Realismo no Brasil", Categorias.LITERATURA, topico3, new Date(System.currentTimeMillis()));
            artigoDAO.salvar(artigo3);


            // Vincula o autor ao artigo na tabela de junção 'Escreve'
            usuarioDAO.cadastrarAutorias(autor, artigo);
            usuarioDAO.cadastrarAutorias(autor, artigo2);
            usuarioDAO.cadastrarAutorias(autor, artigo3); // Vincula ao mesmo 'autor'

            System.out.println("-> CRIADO: Artigo ID " + artigo.getIdArtigo() + " por Usuário ID " + autor.getIdUsuario());
            System.out.println("-> CRIADO: Artigo ID " + artigo2.getIdArtigo() + " Por Usuário ID " + autor.getIdUsuario());
            System.out.println("-> CRIADO: Artigo ID " + artigo3.getIdArtigo() + " Por Usuário ID " + autor.getIdUsuario());

            // --- 2. LER (READ) ---
            Artigo artigoDoBanco = (Artigo) artigoDAO.buscarPorId(artigo.getIdArtigo());
            System.out.println("-> LIDO: Título do artigo: '" + artigoDoBanco.getTituloArtigo() + "'");

            // --- 3. ATUALIZAR (UPDATE) ---
            artigoDoBanco.setTituloArtigo("Título do Artigo Foi Atualizado");
            artigoDAO.atualizar(artigoDoBanco);
            System.out.println("-> ATUALIZADO: Novo título é '" + artigoDoBanco.getTituloArtigo() + "'");

            System.out.println("-> DELETANDO apenas o primeiro artigo ('" + artigo.getTituloArtigo() + "') e suas dependências...");

            // 4.1. Deleta o vínculo do PRIMEIRO artigo na tabela 'Escreve'
            String sqlDeleteEscreve = "DELETE FROM Escreve WHERE idArtigo = ?";
            try (PreparedStatement pstm = conn.prepareStatement(sqlDeleteEscreve)) {
                pstm.setInt(1, artigo.getIdArtigo());
                pstm.executeUpdate();
                System.out.println("-> Vínculo do artigo " + artigo.getIdArtigo() + " removido.");
            }

            // 4.2. Deleta o tópico do PRIMEIRO artigo
            topicoDAO.excluir(topico.getIdTopico());
            System.out.println("-> Tópico do primeiro artigo removido.");

            // 4.3. Deleta o PRIMEIRO artigo
            artigoDAO.excluir(artigo.getIdArtigo());
            System.out.println("-> Artigo " + artigo.getIdArtigo() + " removido.");

            // 4.4. O USUÁRIO E OS OUTROS ARTIGOS NÃO SÃO DELETADOS
            System.out.println("\n-> OPERAÇÃO CONCLUÍDA: O primeiro artigo foi deletado.");
        
        } catch (Exception e) {
            System.err.println("!!! OCORREU UM ERRO DURANTE O TESTE !!!");
            e.printStackTrace();
        }
    }
}
