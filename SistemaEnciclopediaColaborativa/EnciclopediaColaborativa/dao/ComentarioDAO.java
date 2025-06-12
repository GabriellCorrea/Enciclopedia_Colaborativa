package dao;

import java.sql.*;

import java.util.ArrayList;

import modelos.*;

public class ComentarioDAO implements BaseDAO{

    private Connection connection;

    public ComentarioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Comentario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Comentario.");
        }

        Comentario comentario = (Comentario) objeto;

        try {
            String sql = "INSERT INTO Comentario (avaliacao, texto, idArtigo, idUsuario, idPaginaArtigo) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setInt(1, comentario.getNota());
                pstm.setString(2, comentario.getTexto());
                pstm.setInt(3, comentario.getIdArtigo());
                pstm.setInt(4, comentario.getAutor().getIdUsuario());
                pstm.setInt(5, comentario.getIdPaginaArtigo());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        comentario.setIdComentario(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int id) { return null; }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> comentarios = new ArrayList<Object>();
        Usuario autor = null; UsuarioDAO usuDAO = new UsuarioDAO(connection);

        try {
            String sql = "SELECT com.idComentario, com.avaliacao, com.texto, com.idUsuario, art.idArtigo, art.tituloArtigo, art.categoria, art.dtUltimaMod "
                    + "FROM Comentario AS com "
                    + "LEFT JOIN Artigo AS art ON com.idArtigo = art.idArtigo;";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idComentario = rst.getInt("idComentario");
                    int avaliacao = rst.getInt("avaliacao");
                    String texto = rst.getString("texto");
                    int idUsuario = rst.getInt("idUsuario");
                    int idArtigo = rst.getInt("idArtigo");

                    autor = (Usuario) usuDAO.buscarPorId(idUsuario);
                    Comentario c = new Comentario(idComentario, avaliacao, autor, texto, idArtigo);
                    comentarios.add(c);
                }
            }
            return comentarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() { return null; }

    public ArrayList<Comentario> listarTodos(Artigo artigo) {

        ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

        try {
            String sql = "SELECT com.idComentario, com.avaliacao, com.texto, com.idUsuario, usu.nomeUsuario FROM Comentario AS com LEFT JOIN Usuario AS usu ON com.idUsuario = usu.idUsuario WHERE idArtigo = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, artigo.getIdArtigo());
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idComentario = rst.getInt("idComentario");
                    int avaliacao = rst.getInt("avaliacao");
                    String texto = rst.getString("texto");

                    Comentario comentario = new Comentario(idComentario, avaliacao, texto);
                    comentarios.add(comentario);
                }
            }
            return comentarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Comentario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Comentario.");
        }

        Comentario comentario = (Comentario) objeto;

        String sql = "UPDATE Comentario SET avaliacao = ?, texto = ? WHERE idComentario = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, comentario.getNota());
            pstm.setString(2, comentario.getTexto());
            pstm.setInt(3, comentario.getIdComentario());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar comentario: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int idComentario) {
        try {
            String sql = "DELETE FROM Comentario WHERE idComentario = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idComentario);

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