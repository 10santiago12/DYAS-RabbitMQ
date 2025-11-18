package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Random;

/**
 * OrderProducer - Productor de pedidos para el sistema de procesamiento asíncrono
 * Este productor simula la creación de pedidos de una tienda online
 * y los envía a una cola de RabbitMQ para ser procesados
 */
public class OrderProducer {
    // Nombre de la cola donde se enviarán los pedidos
    private final static String QUEUE_NAME = "order_queue";

    public static void main(String[] args) {
        // Configurar la conexión a RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            
            // Declarar la cola - se creará si no existe
            // Parámetros: nombre, durable, exclusive, autoDelete, argumentos
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Simular la creación de 10 pedidos
            Random random = new Random();
            String[] productos = {"Laptop", "Mouse", "Teclado", "Monitor", "Audífonos"};
            
            for (int i = 1; i <= 10; i++) {
                // Crear un pedido con formato JSON simple
                String producto = productos[random.nextInt(productos.length)];
                int cantidad = random.nextInt(5) + 1;
                double precio = (random.nextDouble() * 1000) + 50;
                
                String pedido = String.format(
                    "{\"orderId\": %d, \"producto\": \"%s\", \"cantidad\": %d, \"precio\": %.2f}",
                    i, producto, cantidad, precio
                );

                // Publicar el mensaje en la cola
                // Parámetros: exchange, routingKey, props, mensaje en bytes
                channel.basicPublish("", QUEUE_NAME, null, pedido.getBytes("UTF-8"));
                
                System.out.println(" [✓] Pedido enviado: " + pedido);
                
                // Pequeña pausa para simular pedidos en tiempo real
                Thread.sleep(500);
            }

            System.out.println("\n [✓] Todos los pedidos han sido enviados exitosamente!");

        } catch (Exception e) {
            System.err.println("Error al enviar pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
