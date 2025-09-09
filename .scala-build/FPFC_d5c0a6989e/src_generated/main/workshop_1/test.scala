package workshop_1


final class test$_ {
def args = test_sc.args$
def scriptPath = """workshop_1/test.sc"""
/*<script>*/
import FuncionesRecursivas._

// ----- Pruebas de maxLin -----
maxLin(List(5))              // 5
maxLin(List(3,2,9,1))        // 9
maxLin(List(10,10,10))       // 10
maxLin(List(1,2,3,4,5))      // 5
maxLin(List(100,2,99,3))     // 100

// ----- Pruebas de maxIt -----
maxIt(List(5))               // 5
maxIt(List(3,2,9,1))         // 9
maxIt(List(10,10,10))        // 10
maxIt(List(1,2,3,4,5))       // 5
maxIt(List(100,2,99,3))      // 100

// ----- Pruebas de movsTorresHanoi -----
movsTorresHanoi(1)           // 1
movsTorresHanoi(2)           // 3
movsTorresHanoi(3)           // 7
movsTorresHanoi(4)           // 15
movsTorresHanoi(5)           // 31
movsTorresHanoi(64)          // número enorme
val siglo: BigInt = BigInt(60) * BigInt(60) * BigInt(24) * BigInt(365) * BigInt(100)
movsTorresHanoi(64) / siglo  // comparación con siglos

// ----- Pruebas de torresHanoi -----
torresHanoi(1,1,2,3)         // List((1,3))
torresHanoi(2,1,2,3)         // List((1,2), (1,3), (2,3))
torresHanoi(3,1,2,3)         // movimientos esperados para 3 discos
torresHanoi(4,1,2,3)         // movimientos esperados para 4 discos

/*</script>*/ /*<generated>*//*</generated>*/
}

object test_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new test$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export test_sc.script as `test`

