package com.example.demo.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ConfirmacionPedidoMailListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmacionPedidoMailListener.class);

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String fromAddress;

    public ConfirmacionPedidoMailListener(
            JavaMailSender mailSender,
            @Value("${app.mail.enabled:true}") boolean mailEnabled,
            @Value("${app.mail.from:pedidos@crustaceocaribeno.local}") String fromAddress) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.fromAddress = fromAddress;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void enviarConfirmacion(PedidoConfirmadoEvent evento) {
        if (!mailEnabled) {
            return;
        }
        if (evento.correoCliente() == null || evento.correoCliente().isBlank()) {
            LOGGER.warn("No se envió confirmación del pedido {}: el cliente no tiene correo.",
                    evento.pedido().getIdPedido());
            return;
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(fromAddress);
        mensaje.setTo(evento.correoCliente());
        mensaje.setSubject("Confirmación del pedido #" + evento.pedido().getIdPedido());
        mensaje.setText(construirMensaje(evento));
        try {
            mailSender.send(mensaje);
            LOGGER.info("Correo de confirmación del pedido {} enviado al servidor SMTP local.",
                    evento.pedido().getIdPedido());
        } catch (MailException error) {
            LOGGER.warn("No fue posible enviar por SMTP local la confirmación del pedido {}.",
                    evento.pedido().getIdPedido(), error);
        }
    }

    private String construirMensaje(PedidoConfirmadoEvent evento) {
        var pedido = evento.pedido();
        StringBuilder contenido = new StringBuilder()
                .append("Hola ").append(evento.nombreCliente()).append(",\n\n")
                .append("Recibimos tu pedido #").append(pedido.getIdPedido()).append(".\n")
                .append("Estado: ").append(pedido.getEstado()).append("\n\n")
                .append("Detalle:\n");
        evento.lineasPedido().forEach(linea -> contenido.append("- ").append(linea).append('\n'));
        contenido.append(String.format(Locale.forLanguageTag("es-CO"),
                "\nSubtotal: $%,.0f COP\nDescuento: -$%,.0f COP\nDomicilio: $%,.0f COP\nTotal: $%,.0f COP\n",
                pedido.getSubtotal(), pedido.getDescuento(), pedido.getDomicilio(), pedido.getTotal()));
        if (pedido.getCodigoCupon() != null) {
            contenido.append("Cupón utilizado en este pedido: ")
                    .append(pedido.getCodigoCupon())
                    .append(" (10 % de descuento).\n");
        }
        if (evento.codigoCuponDisponible() != null) {
            contenido.append("\nTu código personal para la próxima compra: ")
                    .append(evento.codigoCuponDisponible())
                    .append("\nObtén 10 % de descuento en platos y adicionales. No vence y es de un solo uso.");
        }
        return contenido.append("\n\nGracias por elegir Crustáceo Caribeño.").toString();
    }
}
