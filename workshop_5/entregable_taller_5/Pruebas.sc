import Benchmark . _
import kmedianas2D . _

// ---------- PRUEBAS FUNCIONES IMPLEMENTADAS ----------

// Prueba de clasificarSeq
val puntos1 = Vector(new Punto(0.1,0.1), new Punto(1.0,1.0), new Punto(0.9,0.8))
val medianas1 = Vector(new Punto(0.0,0.0), new Punto(1.0,1.0))
val clasif1 = clasificarSeq(puntos1, medianas1)

// Prueba de actualizarSeq
val nuevasMedianas1 = actualizarSeq(clasif1, medianas1)

// Prueba de hayConvergenciaSeq
val convergencia1 = hayConvergenciaSeq(0.01, medianas1, nuevasMedianas1)

// Prueba de kMedianasSeq con pocos puntos
val puntos2 = generarPuntos(3, 16)
val medianasIni2 = inicializarMedianas(3, puntos2)
val resultadoSeq2 = kMedianasSeq(puntos2, medianasIni2, 0.01)

// Prueba de kMedianasPar (comparación)
val resultadoPar2 = kMedianasPar(puntos2, medianasIni2, 0.01)




// -------PRUEBAS DE DESEMPEÑO-------------


val r1  = tiemposKmedianas(generarPuntos(2,512).toSeq, 2, 0.01)
val r3  = tiemposKmedianas(generarPuntos(2,2048).toSeq, 2, 0.01)
val r5  = tiemposKmedianas(generarPuntos(2,8192).toSeq, 2, 0.01)
val r7  = tiemposKmedianas(generarPuntos(2,32768).toSeq, 2, 0.01)
val r9  = tiemposKmedianas(generarPuntos(2,131072).toSeq, 2, 0.01)

val r11 = tiemposKmedianas(generarPuntos(4,512).toSeq, 4, 0.01)
val r13 = tiemposKmedianas(generarPuntos(4,2048).toSeq, 4, 0.01)
val r15 = tiemposKmedianas(generarPuntos(4,8192).toSeq, 4, 0.01)
val r17 = tiemposKmedianas(generarPuntos(4,32768).toSeq, 4, 0.01)
val r19 = tiemposKmedianas(generarPuntos(4,131072).toSeq, 4, 0.01)

val r21 = tiemposKmedianas(generarPuntos(8,512).toSeq, 8, 0.01)
val r23 = tiemposKmedianas(generarPuntos(8,2048).toSeq, 8, 0.01)
val r25 = tiemposKmedianas(generarPuntos(8,8192).toSeq, 8, 0.01)
val r27 = tiemposKmedianas(generarPuntos(8,32768).toSeq, 8, 0.01)
val r29 = tiemposKmedianas(generarPuntos(8,131072).toSeq, 8, 0.01)

val r31 = tiemposKmedianas(generarPuntos(16,512).toSeq, 16, 0.01)
val r33 = tiemposKmedianas(generarPuntos(16,2048).toSeq, 16, 0.01)
val r35 = tiemposKmedianas(generarPuntos(16,8192).toSeq, 16, 0.01)
val r37 = tiemposKmedianas(generarPuntos(16,32768).toSeq, 16, 0.01)
val r39 = tiemposKmedianas(generarPuntos(16,131072).toSeq, 16, 0.01)

val r41 = tiemposKmedianas(generarPuntos(32,512).toSeq, 32, 0.01)
val r43 = tiemposKmedianas(generarPuntos(32,2048).toSeq, 32, 0.01)
val r45 = tiemposKmedianas(generarPuntos(32,8192).toSeq, 32, 0.01)
val r47 = tiemposKmedianas(generarPuntos(32,32768).toSeq, 32, 0.01)
val r49 = tiemposKmedianas(generarPuntos(32,131072).toSeq, 32, 0.01)

val r51 = tiemposKmedianas(generarPuntos(64,512).toSeq, 64, 0.01)
val r53 = tiemposKmedianas(generarPuntos(64,2048).toSeq, 64, 0.01)
val r55 = tiemposKmedianas(generarPuntos(64,8192).toSeq, 64, 0.01)
val r57 = tiemposKmedianas(generarPuntos(64,32768).toSeq, 64, 0.01)
val r59 = tiemposKmedianas(generarPuntos(64,131072).toSeq, 64, 0.01)

val r61 = tiemposKmedianas(generarPuntos(128,512).toSeq, 128, 0.01)
val r63 = tiemposKmedianas(generarPuntos(128,2048).toSeq, 128, 0.01)
val r65 = tiemposKmedianas(generarPuntos(128,8192).toSeq, 128, 0.01)
val r67 = tiemposKmedianas(generarPuntos(128,32768).toSeq, 128, 0.01)
val r69 = tiemposKmedianas(generarPuntos(128,131072).toSeq, 128, 0.01)


val r71 = tiemposKmedianas(generarPuntos(256,512).toSeq, 256, 0.01)
val r73 = tiemposKmedianas(generarPuntos(256,2048).toSeq, 256, 0.01)
val r75 = tiemposKmedianas(generarPuntos(256,8192).toSeq, 256, 0.01)
val r77 = tiemposKmedianas(generarPuntos(256,32768).toSeq, 256, 0.01)
val r79 = tiemposKmedianas(generarPuntos(256,131072).toSeq, 256, 0.01)



val puntos_1 = generarPuntos(4, 40).toSeq
val puntos_2 = generarPuntos(3, 15).toSeq
val puntos_3 = generarPuntos(5,100).toSeq
probarKmedianas(puntos_1, 4, 0.01)
probarKmedianas(puntos_2, 3, 0.01)
probarKmedianas(puntos_3, 5, 0.01)
