package com.example.demo.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CuponDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Cupon;
import com.example.demo.repository.CuponRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CuponService {

    public static final int PORCENTAJE_DESCUENTO = 10;
    private static final String CARACTERES_CODIGO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CuponRepository cuponRepository;
    private final ClienteService clienteService;

    public CuponService(CuponRepository cuponRepository, ClienteService clienteService) {
        this.cuponRepository = cuponRepository;
        this.clienteService = clienteService;
    }

    public CuponDTO obtenerDisponible(Long idCliente) {
        return cuponRepository.findFirstByCliente_IdClienteAndFechaUsoIsNullOrderByIdCuponAsc(idCliente)
                .map(this::mapear)
                .orElse(null);
    }

    public Cupon validar(Long idCliente, String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Ingresa un código de descuento.");
        }
        String codigoNormalizado = codigo.trim().toUpperCase(Locale.ROOT);
        Cupon cupon = cuponRepository.findByCodigoIgnoreCase(codigoNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("El código de descuento no es válido."));
        if (!idCliente.equals(cupon.getCliente().getIdCliente()) || !cupon.estaDisponible()) {
            throw new IllegalArgumentException("El código no está disponible para esta cuenta.");
        }
        return cupon;
    }

    public void marcarUsado(Cupon cupon) {
        cupon.setFechaUso(LocalDate.now());
        cuponRepository.save(cupon);
    }

    public CuponDTO obtenerOCrearDisponible(Cliente cliente) {
        return cuponRepository.findFirstByCliente_IdClienteAndFechaUsoIsNullOrderByIdCuponAsc(
                        cliente.getIdCliente())
                .map(this::mapear)
                .orElseGet(() -> mapear(crear(cliente)));
    }

    private Cupon crear(Cliente cliente) {
        Cupon cupon = new Cupon();
        cupon.setCodigo(generarCodigo());
        cupon.setCliente(cliente);
        cupon.setFechaEmision(LocalDate.now());
        return cuponRepository.save(cupon);
    }

    private String generarCodigo() {
        String codigo;
        do {
            StringBuilder sufijo = new StringBuilder(10);
            for (int i = 0; i < 10; i++) {
                sufijo.append(CARACTERES_CODIGO.charAt(RANDOM.nextInt(CARACTERES_CODIGO.length())));
            }
            codigo = "CARIBE-" + sufijo;
        } while (cuponRepository.existsByCodigo(codigo));
        return codigo;
    }

    private CuponDTO mapear(Cupon cupon) {
        return new CuponDTO(cupon.getCodigo(), PORCENTAJE_DESCUENTO, cupon.getFechaEmision());
    }
}
