package dao;

import modelos.Usuario;

import java.sql.Connection;
import java.util.ArrayList;

public class UsuarioDAO implements BaseDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) { this.connection = connection; }

    @Override
    public void salvar(Object objeto) {

    }

    @Override
    public Object buscarPorId(int id) {
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return null;
    }

    @Override
    public void atualizar(Object objeto) {

    }

    @Override
    public void excluir(int id) {

    }
}
