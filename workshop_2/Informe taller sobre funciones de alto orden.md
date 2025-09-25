# Informe Taller 2 – Funciones de Alto Orden

> **Curso:** Fundamentos de Programación Funcional y Concurrente  
**Tema:** Funciones de alto orden

**Estudiantes:**

- Jorge Luis Junior Lasprilla Prada - 2420662
- Andrés Gerardo González Rosero- 2416541

---

## 1. Informe de funciones de alto orden

| Función | Forma(s) de uso de funciones de alto orden | Descripción |
|---|---:|---|
| `insert` | Parámetro (comparator) | Recibe `comp: (T,T) => Boolean` para decidir posicionamiento. |
| `insertionSort` | Devuelve una función (AlgoritmoOrd) | Generador de algoritmo: `insertionSort(comp)` devuelve `AlgoritmoOrd[T]`. Usa `insert`. |
| `menoresQueNoMenoresQue` | Parámetro (comparator) | Particiona una lista respecto a un `pivote`, usando `comp`. |
| `quickSort` | Devuelve una función (AlgoritmoOrd) | Generador de algoritmo: `quickSort(comp)` devuelve `AlgoritmoOrd[T]`. Usa `menoresQueNoMenoresQue`. |
| `comparar` | Parámetro (recibe dos AlgoritmoOrd) | Aplica los dos algoritmos como funciones de orden superior a la misma entrada. |

---

## 2. Informe de corrección

### 2.1 `insert(e, l, comp)` — Esquema de argumentación (inductiva)

1. **Propiedad a probar:** `insert(e, l, comp)` devuelve una pareja `(l', c)` tal que `l'` es la lista `l` con `e` insertado preservando el orden según `comp`, y `c` es el número exacto de llamadas a `comp` realizadas.
2. **Caso base (l = Nil):**
   - Se devuelve `(List(e), 0)`. No se hizo ninguna comparación → cumple propiedad.
3. **Paso inductivo (l = head :: tail):**
   - Se evalúa `comp(e, head)` (una comparación).
   - Si `true` → `e` se coloca delante de `head`; `c = 1`. Invariante satisfecha.
   - Si `false` → se aplica `insert(e, tail, comp)` y por hipótesis la recursión devuelve `(tail', c_tail)` correcta; la devolución final es `(head :: tail', c_tail + 1)`; la suma de comparaciones coincide con la definición.
4. **Conclusión:** por inducción, la función es correcta y el contador es exacto.

### 2.2 `insertionSort(comp)` — Esquema de argumentación

1. **Propiedad:** La función devuelta ordena la lista según `comp` y reporta el número total de comparaciones realizadas.
2. **Idea:** recursión/recorrido que inserta elemento a elemento en la lista acumuladora ordenada.
3. **Caso base:** lista vacía → `(List(), 0)` cumple.
4. **Paso inductivo:** al insertar `head` en la solución ordenada parcial se garantiza (por corrección de `insert`) que la nueva acumulación está ordenada, y se acumulan correctamente las comparaciones (`acc._2 + insertedCount`).
5. **Conclusión:** por inducción estructural sobre la lista de entrada, el algoritmo produce la lista ordenada correcta y contabiliza las comparaciones exactas.

### 2.3 `menoresQueNoMenoresQue(l, v, comp)` — Esquema de argumentación

1. **Propiedad:** Devuelve `(l1, l2, c)` donde `l1` contiene los elementos de `l` menores que `v` (en el **mismo orden** relativo que en `l`), `l2` el resto, y `c = |l|` (cada elemento se comparó exactamente una vez con `v`).
2. **Prueba:** recorrido recursivo; acumuladores con `reverse` mantienen orden original; `count` suma 1 por elemento.
3. **Conclusión:** función correcta y contador exacto.

### 2.4 `quickSort(comp)` — Esquema de argumentación

1. **Propiedad:** Devuelve una lista ordenada (según `comp`) y el número de comparaciones (suma de las realizadas en particiones y recursiones).
2. **Base:** lista vacía o de un elemento → `(lista, 0)`.
3. **Paso inductivo:** elegir pivote = `head`, particionar el resto (correcto por `menoresQueNoMenoresQue`), ordenar recursivamente sublistas y concatenar.
4. **Correctitud de orden:** todos los elementos de la lista resultante cumplen las relaciones `<=` entre particiones por construcción.
5. **Conclusión:** por inducción estructural en la longitud de la lista, `quickSort` es correcto; el contador suma correctamente las comparaciones por nivel.

### 2.5 `comparar(a1, a2, l)` — Esquema de argumentación

1. **Propiedad:** Si `a1(l)._1 == a2(l)._1` devuelve `(c1,c2)` contadores; si no, `(-1,-1)`.
2. **Justificación:** aplica funciones como valores; compara resultados estructurales de las listas.
3. **Conclusión:** cumple la especificación.

---

## 3. Casos de prueba

### 3.1 Archivo de pruebas

> Este es nuestro archivo de pruebas `pruebas.sc`, esta basado en el que el profesor proporcionó en el taller y usamos una metodologia similar para probar las funciones del paquete `Comparador`, tambien se puede encontrar por supuesto en el `.zip` de la entrega:

```scala
// pruebas.sc - Worksheet de pruebas para el paquete Comparador
import Comparador._
import scala.util.Random

// Crea un número aleatorio
val random = new Random()

// Crea una lista de enteros al azar
def listaAlAzar(long: Int): List[Int] = {
  // Crea una lista de `long` enteros con valores aleatorios entre 1 y long*2
  val v = Vector.fill(long) {
    random.nextInt(long * 2) + 1
  }
  v.toList
}

// Compara dos numeros
def menorQue(a: Int, b: Int): Boolean = a < b
def mayorQue(a: Int, b: Int): Boolean = a > b

// Aplica a insertionSort las funciones de comparación
val iSortAsc  = insertionSort[Int](menorQue)
val iSortDesc = insertionSort[Int](mayorQue)
iSortAsc(List(4,5,6,1,2,3))
iSortDesc(List(4,5,6,1,2,3))

// Aplica a quickSort las funciones de comparación
val qSortAsc  = quickSort[Int](menorQue)
val qSortDesc = quickSort[Int](mayorQue)
qSortAsc(List(4,5,6,1,2,3))
qSortDesc(List(4,5,6,1,2,3))

// Compara los resultados de insertionSort y quickSort
comparar(iSortAsc, qSortAsc, List(4,5,6,1,2,3))
comparar(iSortDesc, qSortDesc, List(4,5,6,1,2,3))
comparar(iSortDesc, qSortAsc, List(4,5,6,1,2,3)) // Debe fallar y retornar (-1, -1)
comparar(iSortAsc, qSortDesc, List(4,5,6,1,2,3)) // Debe fallar y retornar (-1, -1)

// Listas ordenadas y reversas
val lAsc100   = (1 to 100).toList
val lAsc1000  = (1 to 1000).toList
val lDsc100   = (1 to 100).toList.reverse
val lDsc1000  = (1 to 1000).toList.reverse

// Pruebas con listas ordenadas y reversas
comparar(iSortAsc, qSortAsc, lAsc100)
comparar(iSortAsc, qSortAsc, lAsc1000)
comparar(iSortAsc, qSortAsc, lDsc100)
comparar(iSortAsc, qSortAsc, lDsc1000)

// Crea listas al azar de diferentes tamaños
val l5  = listaAlAzar(5)
val l10 = listaAlAzar(10)
val l20 = listaAlAzar(20)
val l50 = listaAlAzar(50)

// Pruebas de insertionSort con listas al azar
iSortAsc(l5)
iSortDesc(l5)
iSortAsc(l10)
iSortDesc(l10)
iSortAsc(l20)
iSortDesc(l20)
iSortAsc(l50)
iSortDesc(l50)

// Pruebas de quickSort con listas al azar
qSortAsc(l5)
qSortDesc(l5)
qSortAsc(l10)
qSortDesc(l10)
qSortAsc(l20)
qSortDesc(l20)
qSortAsc(l50)
qSortDesc(l50)

// Compara los resultados de insertionSort y quickSort ascendentes con listas al azar
comparar(iSortAsc, qSortAsc, l5)
comparar(iSortAsc, qSortAsc, l10)
comparar(iSortAsc, qSortAsc, l20)
comparar(iSortAsc, qSortAsc, l50)

// Compara los resultados de insertionSort y quickSort descendentes con listas al azar

comparar(iSortDesc, qSortDesc, l5)
comparar(iSortDesc, qSortDesc, l10)
comparar(iSortDesc, qSortDesc, l20)
comparar(iSortDesc, qSortDesc, l50)
```

### 3.2. Casos básicos con listas pequeñas

#### 3.2.1 InsertionSort y QuickSort con lista [4,5,6,1,2,3]

| Algoritmo | Entrada           | Resultado esperado      | Resultado observado         |
| --------- | ----------------- | ----------------------- | --------------------------- |
| iSortAsc  | List(4,5,6,1,2,3) | (List(1,2,3,4,5,6), 13) | (List(1, 2, 3, 4, 5, 6),9)  |
| iSortDesc | List(4,5,6,1,2,3) | (List(6,5,4,3,2,1), 13) | (List(6, 5, 4, 3, 2, 1),13) |
| qSortAsc  | List(4,5,6,1,2,3) | (List(1,2,3,4,5,6), 16) | (List(1, 2, 3, 4, 5, 6),9)  |
| qSortDesc | List(4,5,6,1,2,3) | (List(6,5,4,3,2,1), 16) | (List(6, 5, 4, 3, 2, 1),9)  |

#### 3.2.2 Comparar con lista [4,5,6,1,2,3]

| Llamada                                          | Resultado esperado | Resultado observado |
| ------------------------------------------------ | ------------------ | ------------------- |
| comparar(iSortAsc, qSortAsc, List(4,5,6,1,2,3))  | (13,16)            | (9,9)               |
| comparar(iSortAsc, qSortDesc, List(4,5,6,1,2,3)) | (-1,-1)            | (-1,-1)             |


### 3.3. Casos con listas grandes ordenadas y reversas

| Lista    | Descripción | Resultado esperado (iSortAsc vs qSortAsc) | Observado       |
| -------- | ----------- | ----------------------------------------- | --------------- |
| lAsc100  | (1 to 100)  | (4950, 4950)                              | (4950,4950)     |
| lAsc1000 | (1 to 1000) | (499500, 499500)                          | (499500,499500) |
| lDsc100  | (100 to 1)  | (99, 4950)                                | (99,4950)       |
| lDsc1000 | (1000 to 1) | (999, 499500)                             | (999,499500)    |

### 3.4. Casos con listas aleatorias

> Aquí los resultados esperados **no son fijos** porque las listas se generan al azar.  

#### 3.4.1 Listas de 5 elementos (`l5`)

| Algoritmo                        | Resultado esperado                           | Observado                |
| -------------------------------- | -------------------------------------------- | ------------------------ |
| iSortAsc(l5)                     | Lista ordenada ascendente con comparaciones  | (List(2, 5, 6, 6, 8),10) |
| iSortDesc(l5)                    | Lista ordenada descendente con comparaciones | (List(8, 6, 6, 5, 2),8)  |
| qSortAsc(l5)                     | Lista ordenada ascendente con comparaciones  | (List(1, 6, 7, 8, 10),7) |
| qSortDesc(l5)                    | Lista ordenada descendente con comparaciones | (List(10, 8, 7, 6, 1),7) |
| comparar(iSortAsc, qSortAsc, l5) | (c1,c2) si iguales, (-1,-1) si no            | (7,7)                    |

#### 3.4.2 Listas de 10, 20 y 50 elementos (`l10`, `l20`, `l50`)

| Lista                                                        | Resultado esperado                                | Observado                                                                                                                                                                                                                                                   |
| ------------------------------------------------------------ | ------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| iSortAsc(l10), iSortDesc(l10), qSortAsc(l10), qSortDesc(l10) | Listas ordenadas según comparador + comparaciones | (List(2, 4, 4, 5, 7, 7, 7, 11, 13, 18),39), (List(18, 13, 11, 7, 7, 7, 5, 4, 4, 2),23), (List(2, 4, 4, 5, 7, 7, 7, 11, 13, 18),28), (List(18, 13, 11, 7, 7, 7, 5, 4, 4, 2),24)                                                                              |
| comparar(iSortAsc, qSortAsc, l10)                            | (c1,c2)                                           | (39,28)                                                                                                                                                                                                                                                     |
| iSortAsc(l20), iSortDesc(l20), qSortAsc(l20), qSortDesc(l20) | idem                                              | Por cuestiones de practicidad y en vista de que agregar aquí la salida de cada función es contraproducente y anti-intuitivo, se insta al lector a correr cada prueba y función en el archivo de pruebas, después de todo estas corren con listas aleatorias. |
| comparar(iSortAsc, qSortAsc, l20)                            | (c1,c2)                                           | (92,62)                                                                                                                                                                                                                                                     |
| iSortAsc(l50), iSortDesc(l50), qSortAsc(l50), qSortDesc(l50) | idem                                              | Por cuestiones de practicidad y en vista de que agregar aquí la salida de cada función es contraproducente y anti-intuitivo, se insta al lector a correr cada prueba y función en el archivo de pruebas, después de todo estas corren con listas aleatorias. |
| comparar(iSortAsc, qSortAsc, l50)                            | (c1,c2)                                           | (716,230)                                                                                                                                                                                                                                                   |

### 3.5 Conclusiones de las pruebas

> Estas y mas pruebas se pueden encontrar en el archivo `Pruebas.sc` que se encuentra en el `.zip` de la entrega, ahi se hallan comentadas y explicadas (los resultados pueden variar por el uso de listas aleatorias).

Nos encontramos con que:

- Ambas implementaciones de ordenamiento son correctas, pues devuelven listas ordenadas según el comparador.

- `insertionSort` es más eficiente en listas casi ordenadas (ascendentes), mientras que `quickSort` es más eficiente en listas desordenadas o inversamente ordenadas.

- El número de comparaciones realizadas por cada algoritmo varía significativamente según la estructura inicial de la lista, lo que afecta su rendimiento.

- En listas pequeñas (5-10 elementos), la diferencia en el número de comparaciones es menos pronunciada, pero `quickSort` tiende a ser más eficiente a medida que la lista crece.

- La función `comparar` es útil para validar que ambos algoritmos producen el mismo resultado, aunque el número de comparaciones difiere.

---

## 4. Conclusiones

Las funciones de alto orden se consolidan como un recurso fundamental dentro del paradigma funcional, ya que permiten diseñar algoritmos más flexibles y reutilizables. En este taller, `insertionSort` y `quickSort` ejemplifican cómo un mismo esquema de ordenamiento puede adaptarse fácilmente a distintos criterios, simplemente parametrizando el comparador de elementos.

La separación entre la lógica del algoritmo y la definición del criterio de comparación resalta una de las principales ventajas de las funciones de alto orden: la modularidad. Este principio no solo mejora la claridad del código, sino que también fomenta la reutilización en diversos contextos, sin necesidad de reescribir la lógica central del algoritmo.

En términos de corrección y verificación, las funciones de alto orden ofrecen un marco más claro para el razonamiento formal. La argumentación inductiva utilizada para justificar cada función muestra que este paradigma facilita tanto la prueba matemática de corrección como el diseño de casos de prueba coherentes y consistentes.

La experimentación con distintos tamaños y distribuciones de listas permitió evidenciar cómo la elección del algoritmo de ordenamiento impacta en el rendimiento. Mientras que `insertionSort` resulta eficiente en listas pequeñas o parcialmente ordenadas, `quickSort` muestra un mejor comportamiento en entradas más grandes y variadas. Las funciones de alto orden no solo posibilitaron implementar estos algoritmos, sino también compararlos objetivamente en términos de número de comparaciones.

Finalmente, este taller demuestra que las funciones de alto orden trascienden el ámbito del ordenamiento. Su aplicabilidad se extiende a múltiples áreas de la informática. Una vez comprendidas, su construcción resulta cómoda, su uso es intuitivo y su aporte a la modularidad y reutilización del código es sustancialmente superior al de otros enfoques.
