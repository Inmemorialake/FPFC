package workshop_2


final class Pruebas$_ {
def args = Pruebas_sc.args$
def scriptPath = """workshop_2/Pruebas.sc"""
/*<script>*/

/*</script>*/ /*<generated>*//*</generated>*/
}

object Pruebas_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Pruebas$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Pruebas_sc.script as `Pruebas`

