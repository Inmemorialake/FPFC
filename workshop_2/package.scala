package object Comparador {

  type AlgoritmoOrd[T] = List[T] => (List[T], Int)
  type Comparador[T] = (T, T) => Boolean

  def insert[T](e: T, l: List[T], comp: Comparador[T]): (List[T], Int) = {
    if (l.isEmpty) (List(e), 0)
    else {
      val head = l.head
      val tail = l.tail
      if (comp(e, head)) {
        (e :: l, 1)
      } else {
        val (insertedTail, countTail) = insert(e, tail, comp)
        (head :: insertedTail, 1 + countTail)
      }
    }
  }

  def insertionSort[T](comp: Comparador[T]): AlgoritmoOrd[T] = {
    (l: List[T]) => {
      def sort(unsorted: List[T], acc: (List[T], Int)): (List[T], Int) = {
        if (unsorted.isEmpty) acc
        else {
          val (insertedList, insertedCount) = insert(unsorted.head, acc._1, comp)
          sort(unsorted.tail, (insertedList, acc._2 + insertedCount))
        }
      }
      sort(l, (List(), 0))
    }
  }

  def menoresQue_noMenoresQue[T](l: List[T], v: T, comp: Comparador[T]): (List[T], List[T], Int) = {
    def particionar(lista: List[T], accMenores: List[T], accNoMenores: List[T], count: Int): (List[T], List[T], Int) = {
      if (lista.isEmpty) (accMenores.reverse, accNoMenores.reverse, count)
      else {
        val head = lista.head
        val nuevaCuenta = count + 1
        if (comp(head, v)) particionar(lista.tail, head :: accMenores, accNoMenores, nuevaCuenta)
        else particionar(lista.tail, accMenores, head :: accNoMenores, nuevaCuenta)
      }
    }
    particionar(l, List(), List(), 0)
  }

  def quickSort[T](comp: Comparador[T]): AlgoritmoOrd[T] = {
    (l: List[T]) => {
      def sort(lista: List[T]): (List[T], Int) = {
        if (lista.isEmpty || lista.tail.isEmpty) (lista, 0)
        else {
          val pivote = lista.head
          val porParticionar = lista.tail
          val (menores, noMenores, compsParticion) = menoresQue_noMenoresQue(porParticionar, pivote, comp)
          val (listaMenoresOrdenada, compsMenores) = sort(menores)
          val (listaNoMenoresOrdenada, compsNoMenores) = sort(noMenores)
          val listaOrdenada = listaMenoresOrdenada ++ (pivote :: listaNoMenoresOrdenada)
          val totalComparaciones = compsParticion + compsMenores + compsNoMenores
          (listaOrdenada, totalComparaciones)
        }
      }
      sort(l)
    }
  }

  def comparar[T](a1: AlgoritmoOrd[T], a2: AlgoritmoOrd[T], l: List[T]): (Int, Int) = {
    val (listaOrdenada1, comparaciones1) = a1(l)
    val (listaOrdenada2, comparaciones2) = a2(l)
    if (listaOrdenada1 == listaOrdenada2) (comparaciones1, comparaciones2)
    else (-1, -1)
  }
}
