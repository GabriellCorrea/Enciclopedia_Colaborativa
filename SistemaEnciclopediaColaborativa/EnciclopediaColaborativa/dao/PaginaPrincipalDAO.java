package dao;

import modelos.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PaginaPrincipalDAO implements BaseDAO {

    private Connection connection;

    public PaginaPrincipalDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {

        if (!(objeto instanceof PaginaPrincipal)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo PaginaPrincipal.");
        }

        PaginaPrincipal paginaPrincipal = (PaginaPrincipal) objeto;

        try {
            String sql = "INSERT INTO PaginaPrincipal VALUES ()";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        paginaPrincipal.setId(rst.getInt(1));
                        for (Artigo artigo : paginaPrincipal.getArtigos()) {
                            alocarArtigo(paginaPrincipal, artigo);
                    }
                }
            }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void alocarArtigo(PaginaPrincipal pg, Artigo art) {
        try {
            String sql = "UPDATE Artigo SET idPaginaPrincipal = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {

                pstm.setInt(1, pg.getIdPagina());

                pstm.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int id) {
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() { return null; }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {

        ArrayList<Object> artigos = new ArrayList<>();
        Artigo ultima = null; Usuario autor = null;
        UsuarioDAO usuDAO = new UsuarioDAO(connection);

        try {

            String sql = "SELECT pgp.idPaginaPrincipal, art.idArtigo, art.tituloArtigo, art.categoria, art.dtUltimaMod, usu.idUsuario, usu.nomeUsuario AS nomeAutor "
                    + "FROM PaginaPrincipal AS pgp "
                    + "LEFT JOIN Artigo AS art ON pgp.idPaginaPrincipal = art.idPaginaPrincipal "
                    + "LEFT JOIN Escreve AS esc ON art.idArtigo = esc.idArtigo "
                    + "LEFT JOIN Usuario AS usu ON esc.idUsuario = usu.idUsuario;";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();

                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        if (ultima == null || ultima.getIdArtigo() != rst.getInt(2)) {
                            int pagp_id = rst.getInt(1);

                            int idArtigo = rst.getInt("idArtigo");
                            String tituloArtigo = rst.getString("tituloArtigo");
                            String categoria = rst.getString("categoria");
                            Date dtultimamod = rst.getDate("dtUltimaMod");
                            int idAutor = rst.getInt("idUsuario");

                            autor = (Usuario) usuDAO.buscarPorId(idAutor);
                            Artigo a = new Artigo(autor, idArtigo, tituloArtigo, Categorias.getCategoriaPorString(categoria), dtultimamod);
                            artigos.add(a);
                            ultima = a;
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
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM PaginaPrincipal WHERE idPaginaPrincipal = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

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