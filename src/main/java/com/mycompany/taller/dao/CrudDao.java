package com.mycompany.taller.dao;

import java.util.List;

public interface CrudDao<T> {
    boolean agregar(T t);
    boolean actualizar(T t);
    boolean eliminar(int id);
    T buscarPorId(int id);
    List<T> listar();
}


