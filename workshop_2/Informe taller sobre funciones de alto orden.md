# Informe Taller 2 – Funciones de Alto Orden

**Curso:** Fundamentos de Programación Funcional y Concurrente  
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

## 3. Casos de prueba (para copiar en `pruebas.sc` y en el informe)

Este es nuestro archivo de pruebas `pruebas.sc`, esta basado en el que el profesor proporcionó en el taller y usamos una metodologia similar para probar las funciones del paquete `Comparador`, tambien se puede encontrar por supuesto en el `.zip` de la entrega:

```scala
// pruebas.sc - Worksheet de pruebas para el paquete Comparador
import Comparador._
import scala.util.Random

val random = new Random()

def listaAlAzar(long: Int): List[Int] = {
  // Crea una lista de `long` enteros con valores aleatorios entre 1 y long*2
  val v = Vector.fill(long) {
    random.nextInt(long * 2) + 1
  }
  v.toList
}

def menorQue(a: Int, b: Int): Boolean = a < b
def mayorQue(a: Int, b: Int): Boolean = a > b

val iSortAsc  = insertionSort[Int](menorQue)
val iSortDesc = insertionSort[Int](mayorQue)
iSortAsc(List(4,5,6,1,2,3))

val qSortAsc  = quickSort[Int](menorQue)
val qSortDesc = quickSort[Int](mayorQue)
qSortAsc(List(4,5,6,1,2,3))

comparar(iSortAsc, qSortAsc, List(4,5,6,1,2,3))
comparar(iSortAsc, qSortDesc, List(4,5,6,1,2,3))

val lAsc100   = (1 to 100).toList
val lAsc1000  = (1 to 1000).toList
val lDsc100   = (1 to 100).toList.reverse
val lDsc1000  = (1 to 1000).toList.reverse

comparar(iSortAsc, qSortAsc, lAsc100)
comparar(iSortAsc, qSortAsc, lAsc1000)
comparar(iSortAsc, qSortAsc, lDsc100)
comparar(iSortAsc, qSortAsc, lDsc1000)

val l5  = listaAlAzar(5)
val l10 = listaAlAzar(10)
val l20 = listaAlAzar(20)
val l50 = listaAlAzar(50)

iSortAsc(l5)
iSortDesc(l5)
iSortAsc(l10)
iSortDesc(l10)
iSortAsc(l20)
iSortDesc(l20)
iSortAsc(l50)
iSortDesc(l50)

qSortAsc(l5)
qSortDesc(l5)
qSortAsc(l10)
qSortDesc(l10)
qSortAsc(l20)
qSortDesc(l20)
qSortAsc(l50)
qSortDesc(l50)

comparar(iSortAsc, qSortAsc, l5)
comparar(iSortAsc, qSortAsc, l10)
comparar(iSortAsc, qSortAsc, l20)
comparar(iSortAsc, qSortAsc, l50)
```
