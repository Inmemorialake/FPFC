package object FuncionesRecursivas {

  def maxLin(l: List[Int]): Int = l match {
    case Nil => throw new IllegalArgumentException("La lista no puede estar vacía")
    case x :: Nil => x
    case x :: xs =>
      val maxTail = maxLin(xs)      
      if (x > maxTail) x else maxTail
  }

  def maxIt(l: List[Int]): Int = {
    if (l.isEmpty) throw new IllegalArgumentException("La lista no puede estar vacía")
    var maxActual = l.head
    for (elem <- l.tail) {
      if (elem > maxActual) {
        maxActual = elem
      }
    }
    maxActual
  }

  def movsTorresHanoi(n: Int): BigInt = {
    if (n < 1) throw new IllegalArgumentException("El número de discos debe ser positivo")
    BigInt(2).pow(n) - 1
  }

  def torresHanoi(n: Int, t1: Int, t2: Int, t3: Int): List[(Int, Int)] = {
    if (n < 1) throw new IllegalArgumentException("El número de discos debe ser positivo")
    if (n == 1) {
      List((t1, t3))
    } else {
      val parte1 = torresHanoi(n - 1, t1, t3, t2)
      val parte2 = List((t1, t3))
      val parte3 = torresHanoi(n - 1, t2, t1, t3) 
      parte1 ++ parte2 ++ parte3
    }
  }
}
