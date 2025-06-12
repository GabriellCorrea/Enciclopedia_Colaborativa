package dao;

import modelos.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PaginaArtigoDAO implements BaseDAO {

    private Connection connection;

    public PaginaArtigoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {

        if (!(objeto instanceof PaginaArtigo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo PaginaArtigo.");
        }

        PaginaArtigo paginaArtigo = (PaginaArtigo) objeto;

        try {
            String sql = "INSERT INTO PaginaArtigoDAO VALUES (?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setInt(1,paginaArtigo.getArtigo().getIdArtigo());
                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        paginaArtigo.setIdPagina(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int idPaginaArtigo) { return null; }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() { return null; }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {

        ArrayList<Object> artigos = new ArrayList<>();
        Artigo ultima = null;

        try {

            String sql = "SELECT pga.idPaginaArtigo, pga.idArtigo. art.tituloArtigo, usu.nomeUsuario AS nomeAutor, com.idComentario, com.avaliacao, com.texto, com.idUsuario "
                    + "FROM PaginaArtigo AS pga "
                    + "LEFT JOIN Artigo AS art ON pga.idArtigo = art.idArtigo "
                    + "LEFT JOIN Escreve AS esc ON art.idArtigo = esc.idArtigo "
                    + "LEFT JOIN Usuario AS usu ON esc.idUsuario = usu.idUsuario "
                    + "LEFT JOIN Comentario AS com ON pga.idPaginaArtigo = com.idPaginaArtigo;";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();

                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        if (ultima == null || ultima.getIdArtigo() != rst.getInt(2)) {
                            int identificador = rst.getInt("idArtigo");
                            String tituloArtigo = rst.getString("tituloArtigo");
                            String categoria = rst.getString("categoria");
                            Date dtUltimaMod = rst.getDate("dtUltimaMod");
                            Artigo a = new Artigo(identificador, tituloArtigo, Categorias.getCategoriaPorString(categoria), dtUltimaMod);
                            artigos.add(a);
                            ultima = a;
                        }

                        if (rst.getInt(5) != 0) {
                            int idComentario = rst.getInt("idComentario");
                            int nota = rst.getInt("avaliacao");
                            String texto = rst.getString("texto");

                            Comentario c = new Comentario(idComentario, nota, texto);
                            ultima.adicionarAvaliacao(c);
                        }
                    }
                }
                return artigos;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Object objeto) { ; }

    @Override
    public void excluir(int idPaginaArtigo) {
        try {
            String sql = "DELETE FROM PaginaArtigo WHERE idPaginaArtigo = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idPaginaArtigo);

                int linhasAfetadas = pstm.executeUpdate();

                if (linhasAfetadas == 0) {
                    throw new SQLException("Falha ao deletar: nenhuma linha foi afetada.");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}