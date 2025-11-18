# Guía Rápida - RabbitMQ

## 📋 Requisitos Previos

1. **Instalar RabbitMQ**
   - Descargar Erlang: https://www.erlang.org/downloads
   - Descargar RabbitMQ: https://www.rabbitmq.com/install-windows.html
   - Instalar ambos como administrador

2. **Verificar que RabbitMQ esté corriendo**
   ```powershell
   Get-Service RabbitMQ
   ```
   - Si no está corriendo: `net start RabbitMQ`
   - Panel web: http://localhost:15672 (user: `guest`, pass: `guest`)

## 🐍 Python

### Instalación
```powershell
pip install pika
```

### Ejecutar
```powershell
# Terminal 1 - Consumidor (ejecutar primero)
python demoPy/OrderConsumer.py

# Terminal 2 - Productor
python demoPy/OrderProducer.py
```

## ☕ Java

### Compilar
```powershell
cd demo
mvn clean compile
```

### Ejecutar
```powershell
# Terminal 1 - Consumidor (ejecutar primero)
mvn exec:java -Dexec.mainClass="com.example.OrderConsumer"

# Terminal 2 - Productor
mvn exec:java -Dexec.mainClass="com.example.OrderProducer"
```

## 📁 Ejemplos Disponibles

### Básicos (Hello World)
- Python: `Producer.py` / `Consumer.py`
- Java: `Producer.java` / `Consumer.java`

### Sistema de Pedidos (Taller)
- Python: `OrderProducer.py` / `OrderConsumer.py`
- Java: `OrderProducer.java` / `OrderConsumer.java`

## ⚠️ Solución de Problemas

**RabbitMQ no conecta:**
- Verificar que el servicio esté corriendo: `Get-Service RabbitMQ`
- Reiniciar servicio: `Restart-Service RabbitMQ`

**Error "pika not found" en Python:**
```powershell
pip install pika
```

**Error de compilación en Java:**
```powershell
cd demo
mvn clean install
```
