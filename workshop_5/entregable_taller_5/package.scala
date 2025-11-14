package object kmedianas2D {

  import scala.annotation.tailrec
  import scala.collection.{Map, Seq}
  import scala.collection.parallel.CollectionConverters._
  import scala.util.Random
  import common._

  // ----------------------------------------------
  // Definiciones comunes para las dos versiones
  // ----------------------------------------------

  class Punto(val x: Double, val y: Double) {
    private def cuadrado(v: Double): Double = v * v

    def distanciaAlCuadrado(that: Punto): Double =
      cuadrado(that.x - x) + cuadrado(that.y - y)

    private def round(v: Double): Double = (v * 100).toInt / 100.0

    override def toString = s"(${round(x)}, ${round(y)})"
  }

  def generarPuntos(k: Int, num: Int): Seq[Punto] = {
    val randx = new Random(1)
    val randy = new Random(3)
    (0 until num).map { i =>
      val x = ((i + 1) % k) * 1.0 / k + randx.nextDouble() * 0.5
      val y = ((i + 5) % k) * 1.0 / k + randy.nextDouble() * 0.5
      new Punto(x, y)
    }
  }

  def inicializarMedianas(k: Int, puntos: Seq[Punto]): Seq[Punto] = {
    val rand = new Random(7)
    (0 until k).map(_ => puntos(rand.nextInt(puntos.length)))
  }


  def hallarPuntoMasCercano(p: Punto, medianas: Seq[Punto]): Punto = {
    assert(medianas.nonEmpty)
    medianas
      .map(pto => (pto, p.distanciaAlCuadrado(pto)))
      .sortWith((a, b) => a._2 < b._2)
      .head
      ._1
  }


  // -------------VERSIONES SECUENCIALES----------------


  def calculePromedioSeq(medianaVieja: Punto, puntos: Seq[Punto]): Punto = {
    if (puntos.isEmpty) medianaVieja
    else {
      new Punto(
        puntos.map(p => p.x).sum / puntos.length,
        puntos.map(p => p.y).sum / puntos.length
      )
    }
  }

  def clasificarSeq(puntos: Seq[Punto], medianas: Seq[Punto]): Map[Punto, Seq[Punto]] = {
    puntos.groupBy(p => hallarPuntoMasCercano(p, medianas))
  }


  def actualizarSeq(clasif: Map[Punto, Seq[Punto]], medianasViejas: Seq[Punto]): Seq[Punto] = {
    medianasViejas.map { mediana =>  calculePromedioSeq(mediana, clasif.getOrElse(mediana, Seq())) }
  }

  @tailrec
  def hayConvergenciaSeq(eta: Double, medianasViejas: Seq[Punto], medianasNuevas: Seq[Punto]): Boolean = {
    if (medianasViejas.isEmpty) true
    else {
      val distancia = medianasViejas.head.distanciaAlCuadrado(medianasNuevas.head)
      if (distancia > eta * eta) false
      else hayConvergenciaSeq(eta, medianasViejas.tail, medianasNuevas.tail)
    }
  }

  @tailrec
  final def kMedianasSeq(puntos: Seq[Punto], medianas: Seq[Punto], eta: Double): Seq[Punto] = {

    val clasif: Map[Punto, Seq[Punto]] = clasificarSeq(puntos, medianas)

    val nuevasMedianas: Seq[Punto] = actualizarSeq(clasif, medianas)

    val convergieron = hayConvergenciaSeq(eta, medianas, nuevasMedianas)

    if (convergieron)
      nuevasMedianas
    else
      kMedianasSeq(puntos, nuevasMedianas, eta)
  }







  // --------------------VERSIONES PARALELAS---------------------------


  def calculePromedioPar(medianaVieja: Punto, puntos: Seq[Punto]): Punto = {
    if (puntos.isEmpty) medianaVieja
    else {
      val puntosPar = puntos.par
      new Punto(
        puntosPar.map(p => p.x).sum / puntos.length,
        puntosPar.map(p => p.y).sum / puntos.length
      )
    }
  }



  def clasificarPar(umb: Int)(puntos: Seq[Punto], medianas: Seq[Punto]): Map[Punto, Seq[Punto]] = {

    def unirClasificaciones(m1: Map[Punto, Seq[Punto]], m2: Map[Punto, Seq[Punto]]): Map[Punto, Seq[Punto]] = {
      (m1.toSeq ++ m2.toSeq).groupBy(_._1).map { case (mediana, listaPares) => mediana -> listaPares.flatMap(_._2) }
    }


    if (puntos.length <= umb)
       clasificarSeq(puntos,medianas)
    else {

      val (izq, der) = puntos.splitAt(puntos.length / 2)

      val (clasifIzq, clasifDer) = parallel(
        clasificarPar(umb)(izq, medianas),
        clasificarPar(umb)(der, medianas)
      )

      unirClasificaciones(clasifIzq, clasifDer)

    }
  }

  def actualizarPar(clasif: Map[Punto, Seq[Punto]], medianasViejas: Seq[Punto]): Seq[Punto] = {

    //DEBO implementar paralelismo de tareas?? en este caso no se dice explicitamente en el texto
    medianasViejas.par.map { mediana => calculePromedioPar(mediana, clasif.getOrElse(mediana, Seq())) }.seq
  }

  def hayConvergenciaPar(eta: Double, medianasViejas: Seq[Punto], medianasNuevas: Seq[Punto]): Boolean = {

    val umb = 2 // umbral interno, se debe ajustar, probar cual es mejor

    if (medianasViejas.length <= umb) {
      (medianasViejas zip medianasNuevas)
        .forall { case (vieja, nueva) =>
          vieja.distanciaAlCuadrado(nueva) <= eta * eta
        }
    } else {
      val mid = medianasViejas.length / 2
      val (viejasIzq, viejasDer) = medianasViejas.splitAt(mid)
      val (nuevasIzq, nuevasDer) = medianasNuevas.splitAt(mid)


      val (resIzq, resDer) = parallel(
        hayConvergenciaPar(eta, viejasIzq, nuevasIzq),
        hayConvergenciaPar(eta, viejasDer, nuevasDer)
      )

      resIzq && resDer
    }
  }

  @tailrec
  def kMedianasPar(puntos: Seq[Punto], medianas: Seq[Punto], eta: Double): Seq[Punto] = {

    val umbral = 5

    val clasif: Map[Punto, Seq[Punto]] = clasificarPar(umbral)(puntos, medianas)

    val nuevasMedianas: Seq[Punto] = actualizarPar(clasif, medianas)

    val convergieron: Boolean = hayConvergenciaPar(eta, medianas, nuevasMedianas)

    if (convergieron) nuevasMedianas
    else
      kMedianasPar(puntos, nuevasMedianas, eta)
  }



}
