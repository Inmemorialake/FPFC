package object FuncionesRecursivas {

  def maxLin(l: List[Int]): Int = {
    def intMax(x: Int, y: Int) =
      if (x >= y) x else y
    if (l.tail.isEmpty) l.head else intMax(l.head, maxLin(l.tail))
  }

  def maxIt(l: List[Int]): Int = {
    def iter(remaining: List[Int], currentMax: Int): Int = {
      if (remaining.isEmpty) currentMax
      else {
        val newMax = if (remaining.head > currentMax) remaining.head else currentMax
        iter(remaining.tail, newMax)
      }
    }
    iter(l.tail, l.head)
  }

  def movsTorresHanoi(n: Int): BigInt = {
    if (n == 1) 1
    else 2 * movsTorresHanoi(n - 1) + 1
  }

  def torresHanoi(n: Int, t1: Int, t2: Int, t3: Int): List[(Int, Int)] = {
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