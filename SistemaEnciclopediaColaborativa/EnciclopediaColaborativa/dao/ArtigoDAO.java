package dao;

import java.sql.*;

import java.util.ArrayList;

import modelos.*;

public class ArtigoDAO implements BaseDAO {

    private Connection connection;

    public ArtigoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {

        if (!(objeto instanceof Artigo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Artigo.");
        }

        Artigo artigo = (Artigo) objeto;

        try {
            String sql = "INSERT INTO Artigo ( tituloArtigo, dtUltimaMod, categoria ) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setString(1, artigo.getTituloArtigo());
                pstm.setDate(2, artigo.getDtUltimaAlteracao());
                pstm.setString(3, artigo.getCategoria().getNomeCategoria());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        artigo.setIdArtigo(rst.getInt(1));

                        TopicoDAO topDAO = new TopicoDAO(connection);
                        for (Topico topico : artigo.getTopicos()) {
                            topDAO.salvar(topico, artigo);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int idArtigo) {
        try {
            String sql = "SELECT idArtigo, tituloArtigo, categoria, dtUltimaMod FROM Artigo WHERE idArtigo = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idArtigo);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idArtigo");
                    String tituloArtigo = rst.getString("tituloArtigo");
                    String categoria = rst.getString("categoria");
                    Date dtUltimaMod = rst.getDate("dtUltimaMod");
                    return new Artigo(identificador, tituloArtigo, Categorias.getCategoriaPorString(categoria), dtUltimaMod);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {

        ArrayList<Object> artigos = new ArrayList<>();

        try {
            String sql = "SELECT idArtigo, tituloArtigo, categoria, dtUltimaMod FROM Artigo";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idArtigo = rst.getInt("idArtigo");
                    String tituloArtigo = rst.getString("tituloArtigo");
                    String categoria = rst.getString("categoria");
                    Date dtUltimaMod = rst.getDate("dtUltimaMod");
                    Artigo a = new Artigo(idArtigo, tituloArtigo, Categorias.getCategoriaPorString(categoria), dtUltimaMod);
                    artigos.add(a);
                }
            }
            return artigos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {

        ArrayList<Object> artigos = new ArrayList<Object>();
        Artigo ultima = null; Usuario autor = null;
        UsuarioDAO usuDAO = new UsuarioDAO(connection);

        try {
            String sql = "SELECT art.idArtigo, usu.idUsuario, usu.nomeUsuario AS nomeAutor, art.tituloArtigo, art.categoria,  art.dtUltimaMod, top.idTopico, top.nomeTopico, top.textoTopico, com.idComentario, com.avaliacao, com.texto"
                            + "FROM Artigo AS art "
                            + "LEFT JOIN Topico AS top ON art.idArtigo = top.idArtigo "
                            + "LEFT JOIN Escreve AS esc ON art.idArtigo = esc.idArtigo "
                            + "LEFT JOIN Usuario AS usu ON esc.idUsuario = usu.idUsuario "
                            + "LEFT JOIN Comentario AS com ON art.idArtigo = com.idArtigo;";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    if (ultima == null || ultima.getIdArtigo() != rst.getInt(1)) {
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

                    if (rst.getInt(7) != 0) {
                        int idTopico = rst.getInt("idTopico");
                        String nomeTopico = rst.getString("nomeTopico");
                        String textoTopico = rst.getString("textoTopico");
                        int idArtigo = rst.getInt("idArtigo");
                        int idAutor = rst.getInt("idUsuario");
                        Topico topico = new Topico(idTopico, nomeTopico, textoTopico, idArtigo);

                        autor = (Usuario) usuDAO.buscarPorId(idAutor);
                        ultima.adicionarTopico(autor, topico);
                    }

                    if (rst.getInt(10) != 0) {
                        int idComentario = rst.getInt("idComentario");
                        int nota = rst.getInt("avaliacao");
                        String texto = rst.getString("texto");

                        Comentario c = new Comentario(idComentario, nota, texto);
                        ultima.adicionarAvaliacao(c);
                    }
                }
            }
            return artigos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Artigo)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Pessoa.");
        }

        Artigo artigo = (Artigo) objeto;
        TopicoDAO topDAO = new TopicoDAO(connection);

        String sql = "UPDATE Artigo SET tituloArtigo = ?, categoria = ?, dtUltimaMod = ? WHERE idArtigo = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, artigo.getTituloArtigo());
            pstm.setString(2, artigo.getCategoria().getNomeCategoria());
            pstm.setDate(3, artigo.getDtUltimaAlteracao());
            pstm.setInt(4, artigo.getIdArtigo());

            for (Topico topico : artigo.getTopicos()) { topDAO.atualizar(topico); }

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar topico: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int idArtigo) {
        try {
            String sql = "DELETE FROM Artigo WHERE idArtigo = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idArtigo);

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