package com.example.demo.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    public static final String CLIENTE = "CLIENTE";
    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String OPERADOR = "OPERADOR";
    public static final String DOMICILIARIO = "DOMICILIARIO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
