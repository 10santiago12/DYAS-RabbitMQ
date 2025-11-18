package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * OrderConsumer - Consumidor de pedidos para el sistema de procesamiento asíncrono
 * Este consumidor recibe pedidos de la cola de RabbitMQ y los procesa
 * simulando validación, cálculo de totales y confirmación del pedido
 */
public class OrderConsumer {
    // Nombre de la cola de donde se leerán los pedidos
    private final static String QUEUE_NAME = "order_queue";

    public static void main(String[] args) {
        // Configurar la conexión a RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Declarar la cola - debe coincidir con la del productor
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            System.out.println(" [*] Esperando pedidos. Presiona CTRL+C para salir");
            System.out.println(" [*] Procesando pedidos...\n");

            // Definir el callback que se ejecutará cuando llegue un mensaje
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String mensaje = new String(delivery.getBody(), "UTF-8");
                
                try {
                    // Simular el procesamiento del pedido
                    System.out.println(" [→] Pedido recibido: " + mensaje);
                    
                    // Simular tiempo de procesamiento (validación, inventario, etc.)
                    procesarPedido(mensaje);
                    
                    System.out.println(" [✓] Pedido procesado exitosamente\n");
                    
                } catch (Exception e) {
                    System.err.println(" [✗] Error procesando pedido: " + e.getMessage());
                }
            };

            // Consumir mensajes de la cola
            // Parámetros: queue, autoAck, deliverCallback, cancelCallback
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                System.out.println("Consumidor cancelado: " + consumerTag);
            });

            // Mantener el consumidor corriendo
            // (No cerramos la conexión para que siga escuchando)

        } catch (Exception e) {
            System.err.println("Error en el consumidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método que simula el procesamiento de un pedido
     * En un sistema real, aquí se haría:
     * - Validación del pedido
     * - Verificación de inventario
     * - Cálculo de impuestos y envío
     * - Actualización de base de datos
     * - Envío de confirmación al cliente
     */
    private static void procesarPedido(String pedido) throws InterruptedException {
        // Simular procesamiento con una pausa
        Thread.sleep(1000);
        
        // Aquí iría la lógica de negocio real
        System.out.println("     → Validando inventario...");
        Thread.sleep(300);
        System.out.println("     → Calculando costos de envío...");
        Thread.sleep(300);
        System.out.println("     → Generando factura...");
        Thread.sleep(300);
        System.out.println("     → Enviando confirmación al cliente...");
    }
}
