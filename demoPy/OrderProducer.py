import pika
import json
import random
import time

"""
OrderProducer - Productor de pedidos para el sistema de procesamiento asíncrono
Este productor simula la creación de pedidos de una tienda online
y los envía a una cola de RabbitMQ para ser procesados
"""

def main():
    # Nombre de la cola donde se enviarán los pedidos
    QUEUE_NAME = 'order_queue'
    
    try:
        # Conectar con RabbitMQ en localhost
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        channel = connection.channel()

        # Declarar la cola - se creará si no existe
        channel.queue_declare(queue=QUEUE_NAME)

        # Lista de productos para simular pedidos
        productos = ['Laptop', 'Mouse', 'Teclado', 'Monitor', 'Audífonos']

        # Simular la creación de 10 pedidos
        for i in range(1, 11):
            # Crear un pedido con datos aleatorios
            pedido = {
                'orderId': i,
                'producto': random.choice(productos),
                'cantidad': random.randint(1, 5),
                'precio': round(random.uniform(50, 1050), 2)
            }

            # Convertir el pedido a JSON
            mensaje = json.dumps(pedido)

            # Publicar el mensaje en la cola
            # exchange='': usa el exchange por defecto
            # routing_key: nombre de la cola
            channel.basic_publish(
                exchange='',
                routing_key=QUEUE_NAME,
                body=mensaje
            )

            print(f" [✓] Pedido enviado: {mensaje}")

            # Pequeña pausa para simular pedidos en tiempo real
            time.sleep(0.5)

        print("\n [✓] Todos los pedidos han sido enviados exitosamente!")

        # Cerrar la conexión
        connection.close()

    except pika.exceptions.AMQPConnectionError as e:
        print(f"Error: No se pudo conectar a RabbitMQ. ¿Está corriendo en localhost?")
        print(f"Detalles: {e}")
    except Exception as e:
        print(f"Error al enviar pedidos: {e}")

if __name__ == '__main__':
    main()
