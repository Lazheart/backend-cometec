package com.demo.DBPBackend.usuario.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String nombre;

    @Column(length = 30, nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    @Email
    private String correo;

    @Column(nullable = false, length = 128)
    private String contraseña;

    @Column(nullable = false, length = 15, unique = true)
    private String telefono;
}
