import Benchmark._
import kmedianas2D._

// Pruebas con diferentes tamaños de datos
println("=== PRUEBAS K-MEANS ===")

// Prueba pequeña (16 puntos, 3 clusters)
println("\n--- Prueba pequeña (16 puntos, 3 clusters) ---")
val puntos16_3 = generarPuntos(3, 16).toSeq
val resultados16_3 = tiemposKmedianas(puntos16_3, 3, 0.01)
println(s"Tiempo secuencial: ${resultados16_3._1}")
println(s"Tiempo paralelo: ${resultados16_3._2}")
println(s"Aceleración: ${resultados16_3._3}")

// Generar gráficos para la prueba pequeña
probarKmedianas(puntos16_3, 3, 0.01)

// Prueba mediana (1024 puntos, 8 clusters)
println("\n--- Prueba mediana (1024 puntos, 8 clusters) ---")
val puntos1024_8 = generarPuntos(8, 1024).toSeq
val resultados1024_8 = tiemposKmedianas(puntos1024_8, 8, 0.01)
println(s"Tiempo secuencial: ${resultados1024_8._1}")
println(s"Tiempo paralelo: ${resultados1024_8._2}")
println(s"Aceleración: ${resultados1024_8._3}")

// Prueba grande (32768 puntos, 32 clusters)
println("\n--- Prueba grande (32768 puntos, 32 clusters) ---")
val puntos32768_32 = generarPuntos(32, 32768).toSeq
val resultados32768_32 = tiemposKmedianas(puntos32768_32, 32, 0.001)
println(s"Tiempo secuencial: ${resultados32768_32._1}")
println(s"Tiempo paralelo: ${resultados32768_32._2}")
println(s"Aceleración: ${resultados32768_32._3}")

// Prueba muy grande (100000 puntos, 64 clusters)
println("\n--- Prueba muy grande (100000 puntos, 64 clusters) ---")
val puntos100000_64 = generarPuntos(64, 100000).toSeq
val resultados100000_64 = tiemposKmedianas(puntos100000_64, 64, 0.001)
println(s"Tiempo secuencial: ${resultados100000_64._1}")
println(s"Tiempo paralelo: ${resultados100000_64._2}")
println(s"Aceleración: ${resultados100000_64._3}")

// Verificar corrección comparando resultados
println("\n--- Verificación de corrección ---")
val medianasSeq = kMedianasSeq(puntos16_3, inicializarMedianas(3, puntos16_3), 0.01)
val medianasPar = kMedianasPar(puntos16_3, inicializarMedianas(3, puntos16_3), 0.01)

println("Medianas secuenciales: " + medianasSeq.mkString(", "))
println("Medianas paralelas: " + medianasPar.mkString(", "))
println("¿Resultados equivalentes?: " +
  medianasSeq.zip(medianasPar).forall { case (a, b) =>
    a.distanciaAlCuadrado(b) < 0.01
  }
)