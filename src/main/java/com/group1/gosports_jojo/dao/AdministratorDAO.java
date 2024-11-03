package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.entity.Administrator;

import java.util.List;

public interface AdministratorDAO {
    void insert(Administrator administrator);
    void update(Administrator administrator);
    void delete(Administrator administrator);
    Administrator getById(int id);
    List<Administrator> getAll();
    Administrator getByEmail(String email);
}
