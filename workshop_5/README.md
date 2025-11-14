# Consideraciones adicionales para la entrega y evaluación

>Con el fin de garantizar la correcta reproducción de los experimentos realizados y evitar posibles inconsistencias durante la evaluación, se presentan a continuación algunas aclaraciones relevantes:

## **1. Especificaciones del equipo utilizado**

Las pruebas fueron ejecutadas en el siguiente entorno de hardware y software:

* **Procesador:** Intel® Core™ i5-12450H (8 núcleos, 12 hilos)
* **Memoria RAM:** 8 GB DDR5
* **GPU:** NVIDIA RTX 3050 Laptop GPU
* **Almacenamiento:** SSD NVMe
* **Sistema operativo:** Linux
* **Entorno de desarrollo:** IntelliJ IDEA + JDK 17 + Scala 2.13.10

Dado que la paralelización depende fuertemente del número de núcleos y de la disponibilidad de memoria, estos parámetros pueden afectar el rendimiento obtenido al ejecutar el algoritmo.

---

## **2. Generación automática de archivos HTML**

Durante la ejecución del proyecto, las funciones incluidas en el paquete `Benchmark` generan automáticamente archivos HTML que contienen visualizaciones con la asignación de clusters, medianas iniciales y medianas finales tanto para la versión secuencial como para la paralela. Estos archivos se generan al correr el proyecto y pueden abrirse en cualquier navegador moderno sin pasos adicionales.

---

## **3. Inclusión del proyecto completo**

Además de los paquetes solicitados en el enunciado, se incluye la carpeta completa del proyecto en IntelliJ IDEA. Esta medida se toma con el fin de:

* Evitar errores de compilación por dependencias o configuración del `build.sbt`.
* Garantizar que los paquetes se carguen correctamente.
* Permitir la ejecución directa del proyecto sin configuraciones adicionales.
* Reducir inconsistencias entre distintos entornos de evaluación.

Esto asegura que el taller pueda ser reproducido y ejecutado correctamente sin ajustes manuales por parte del evaluador.
