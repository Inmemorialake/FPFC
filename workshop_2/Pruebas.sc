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