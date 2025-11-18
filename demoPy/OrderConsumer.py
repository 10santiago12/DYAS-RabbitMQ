import pika
import json
import time

"""
OrderConsumer - Consumidor de pedidos para el sistema de procesamiento asíncrono
Este consumidor recibe pedidos de la cola de RabbitMQ y los procesa
simulando validación, cálculo de totales y confirmación del pedido
"""

def procesar_pedido(pedido_json):
    """
    Función que simula el procesamiento de un pedido
    En un sistema real, aquí se haría:
    - Validación del pedido
    - Verificación de inventario
    - Cálculo de impuestos y envío
    - Actualización de base de datos
    - Envío de confirmación al cliente
    """
    # Parsear el JSON del pedido
    pedido = json.loads(pedido_json)
    
    print("     → Validando inventario...")
    time.sleep(0.3)
    print("     → Calculando costos de envío...")
    time.sleep(0.3)
    print("     → Generando factura...")
    time.sleep(0.3)
    print("     → Enviando confirmación al cliente...")
    time.sleep(0.3)
    
    # Calcular el total del pedido
    total = pedido['cantidad'] * pedido['precio']
    print(f"     → Total del pedido: ${total:.2f}")

def callback(ch, method, properties, body):
    """
    Función callback que se ejecuta cuando llega un mensaje
    """
    mensaje = body.decode('utf-8')
    
    try:
        print(f" [→] Pedido recibido: {mensaje}")
        
        # Simular el procesamiento del pedido
        procesar_pedido(mensaje)
        
        print(" [✓] Pedido procesado exitosamente\n")
        
    except Exception as e:
        print(f" [✗] Error procesando pedido: {e}\n")

def main():
    # Nombre de la cola de donde se leerán los pedidos
    QUEUE_NAME = 'order_queue'
    
    try:
        # Conectar con RabbitMQ en localhost
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        channel = connection.channel()

        # Declarar la cola - debe coincidir con la del productor
        channel.queue_declare(queue=QUEUE_NAME)

        print(" [*] Esperando pedidos. Presiona CTRL+C para salir")
        print(" [*] Procesando pedidos...\n")

        # Configurar el consumidor
        # queue: nombre de la cola
        # on_message_callback: función que se ejecuta al recibir un mensaje
        # auto_ack: confirmación automática de mensajes
        channel.basic_consume(
            queue=QUEUE_NAME,
            on_message_callback=callback,
            auto_ack=True
        )

        # Iniciar el consumo de mensajes (bloquea el hilo principal)
        channel.start_consuming()

    except KeyboardInterrupt:
        print("\n [!] Consumidor detenido por el usuario")
    except pika.exceptions.AMQPConnectionError as e:
        print(f"Error: No se pudo conectar a RabbitMQ. ¿Está corriendo en localhost?")
        print(f"Detalles: {e}")
    except Exception as e:
        print(f"Error en el consumidor: {e}")

if __name__ == '__main__':
    main()
