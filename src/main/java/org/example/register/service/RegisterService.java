package org.example.register.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.TypedQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.example.register.entity.UserEntity;
import org.example.register.request.RegisterRequest;
import org.example.register.response.RegisterResponse;

import jakarta.ws.rs.NotFoundException;

public class RegisterService {

    public String returnStatus(String message) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setMessage(new StringBuilder()
                .append("Status: ")
                .append(message)
                .toString());
        return registerResponse.toString();
    }

    public void postUser(RegisterRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setRealName(userRequest.getRealName());
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword(userRequest.getPassword());

        EntityManager entityManager = Persistence
                .createEntityManagerFactory("jax-rs-sample")
                .createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(userEntity);
            transaction.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            System.out.println("Transação concluída com sucesso");
            entityManager.clear();
            entityManager.close();
        }
    }

    public RegisterRequest getUserById(Long id) {
        EntityManager entityManager = Persistence
                .createEntityManagerFactory("jax-rs-sample")
                .createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            UserEntity userEntity = entityManager.find(UserEntity.class, id);

            transaction.commit();
            if (userEntity == null) {
                return null;
            }

            RegisterRequest userRequest = new RegisterRequest();
            userRequest.setRealName(userEntity.getRealName());
            userRequest.setUserName(userEntity.getUsername());
            userRequest.setEmail(userEntity.getEmail());
            userRequest.setPassword(userEntity.getPassword());

            return userRequest;
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    public void updateUser(Long id, RegisterRequest userRequest) {
        EntityManager entityManager = Persistence
                .createEntityManagerFactory("jax-rs-sample")
                .createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            UserEntity userEntity = entityManager.find(UserEntity.class, id);
            if (userEntity == null) {
                throw new NotFoundException("User with id " + id + " not found");
            }

            userEntity.setRealName(userRequest.getRealName());
            userEntity.setUsername(userRequest.getUsername());
            userEntity.setEmail(userRequest.getEmail());
            userEntity.setPassword(userRequest.getPassword());

            entityManager.merge(userEntity);
            transaction.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    public void deleteUser(Long id) {
        EntityManager entityManager = Persistence
                .createEntityManagerFactory("jax-rs-sample")
                .createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            UserEntity userEntity = entityManager.find(UserEntity.class, id);
            if (userEntity == null) {
                throw new NotFoundException("User with id " + id + " not found");
            }

            entityManager.remove(userEntity);
            transaction.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> userList = new ArrayList<>();
        EntityManager entityManager = Persistence
                .createEntityManagerFactory("jax-rs-sample")
                .createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            TypedQuery<UserEntity> query = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
            userList = query.getResultList();

            transaction.commit();
            return userList;
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

}
