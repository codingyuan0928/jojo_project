package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.AdministratorDAO;
import com.group1.gosports_jojo.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@Repository
public class AdministratorJPADAO implements AdministratorDAO {
    private static final String GET_BY_EMAIL_PSTMT="SELECT * FROM ADMINISTRATORS WHERE email = ?";
    private static final String GET_ALL_STMT = "SELECT * FROM ADMINISTRATORS ORDER BY ADMINISTRATOR_ID ASC";

    private EntityManager entityManager;

    @Autowired
    public AdministratorJPADAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void insert(Administrator administrator) {
        entityManager.persist(administrator);
    }

    @Override
    public void update(Administrator administrator) {
        entityManager.merge(administrator);
    }

    @Override
    public void delete(Administrator administrator) {
        entityManager.remove(administrator);
    }

    @Transactional(readOnly = true)
    @Override
    public Administrator getById(int id) {
        return entityManager.find(Administrator.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Administrator> getAll() {
        return entityManager.createNativeQuery(GET_ALL_STMT, Administrator.class).getResultList();
    }
    @Override
    public Administrator getByEmail(String email){
        List<Administrator> result =entityManager.createNativeQuery(GET_BY_EMAIL_PSTMT, Administrator.class)
                .setParameter(1, email)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
