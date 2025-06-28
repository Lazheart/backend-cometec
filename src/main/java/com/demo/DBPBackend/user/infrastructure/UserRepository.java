package com.demo.DBPBackend.user.infrastructure;

import com.demo.DBPBackend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos originales sin paginación (mantener para compatibilidad)
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findAll();

    // Métodos de paginación básicos
    Page<User> findAll(Pageable pageable);
    
    // Métodos de búsqueda con paginación
    Page<User> findByNameContaining(String name, Pageable pageable);
    Page<User> findByLastnameContaining(String lastname, Pageable pageable);
    Page<User> findByEmailContaining(String email, Pageable pageable);
    Page<User> findByRole(com.demo.DBPBackend.user.domain.Role role, Pageable pageable);
    
    // Métodos con ordenamiento
    Page<User> findAllByOrderByNameAsc(Pageable pageable);
    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<User> findAllByOrderByEmailAsc(Pageable pageable);
    
    // Métodos de búsqueda por relaciones con ordenamiento
    Page<User> findByRoleOrderByCreatedAtDesc(com.demo.DBPBackend.user.domain.Role role, Pageable pageable);
    Page<User> findByRoleOrderByNameAsc(com.demo.DBPBackend.user.domain.Role role, Pageable pageable);
}
