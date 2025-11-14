package object kmedianas2D {
  import scala.annotation.tailrec
  import scala.collection.{Map, Seq}
  import scala.collection.parallel.CollectionConverters._
  import scala.util.Random
  import common._
  import Benchmark._

  // Clase que representa un punto en el plano 2D
  // Entrada: x, y (coordenadas del punto)
  // Salida: Instancia de Punto con operaciones de distancia
  class Punto(val x: Double, val y: Double) {
    private def cuadrado(v: Double): Double = v * v

    // Calcula la distancia euclidiana al cuadrado entre este punto y otro
    // Entrada: that (punto de referencia)
    // Salida: Double (distancia al cuadrado)
    def distanciaAlCuadrado(that: Punto): Double =
      cuadrado(that.x - x) + cuadrado(that.y - y)

    private def round(v: Double): Double = (v * 100).toInt / 100.0

    // Representación en string del punto con coordenadas redondeadas
    override def toString = s"(${round(x)},${round(y)})"
  }

  // Genera una secuencia de puntos distribuidos alrededor de k centros
  // Entrada: k (número de clusters), num (número de puntos a generar)
  // Salida: Seq[Punto] (puntos generados aleatoriamente)
  def generarPuntos(k: Int, num: Int): Seq[Punto] = {
    val randx = new Random(1)
    val randy = new Random(3)
    (0 until num).map { i =>
      val x = ((i + 1) % k) * 1.0 / k + randx.nextDouble() * 0.5
      val y = ((i + 5) % k) * 1.0 / k + randy.nextDouble() * 0.5
      new Punto(x, y)
    }
  }

  // Inicializa k medianas seleccionando puntos aleatorios del conjunto
  // Entrada: k (número de medianas), puntos (conjunto de puntos disponible)
  // Salida: Seq[Punto] (medianas iniciales seleccionadas aleatoriamente)
  def inicializarMedianas(k: Int, puntos: Seq[Punto]): Seq[Punto] = {
    val rand = new Random(7)
    (0 until k).map(_ => puntos(rand.nextInt(puntos.length)))
  }

  // Encuentra la mediana más cercana a un punto dado
  // Entrada: p (punto de referencia), medianas (secuencia de medianas)
  // Salida: Punto (mediana más cercana al punto p)
  def hallarPuntoMasCercano(p: Punto, medianas: Seq[Punto]): Punto = {
    assert(medianas.nonEmpty)
    medianas.minBy(m => p.distanciaAlCuadrado(m))
  }

  // Calcula el promedio (centroide) de un conjunto de puntos (versión secuencial)
  // Entrada: medianaVieja (mediana actual), puntos (puntos del cluster)
  // Salida: Punto (nuevo punto promedio del cluster)
  def calculePromedioSeq(medianaVieja: Punto, puntos: Seq[Punto]): Punto = {
    if (puntos.isEmpty) medianaVieja
    else {
      new Punto(
        puntos.map(p => p.x).sum / puntos.length,
        puntos.map(p => p.y).sum / puntos.length
      )
    }
  }

  // Calcula el promedio (centroide) de un conjunto de puntos (versión paralela)
  // Entrada: medianaVieja (mediana actual), puntos (puntos del cluster)
  // Salida: Punto (nuevo punto promedio del cluster calculado en paralelo)
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

  // Determina el umbral para decidir cuándo usar paralelismo basado en el tamaño
  // Entrada: tam (tamaño del conjunto de puntos)
  // Salida: Int (valor del umbral calculado)
  def umbral(tam: Int): Int = {
    // Umbral basado en el tamaño de los puntos
    math.pow(2, ((math.log(tam)/math.log(2))/2).toInt).toInt
  }

  // VERSIONES SECUENCIALES

  // Clasifica puntos en clusters según la mediana más cercana (versión secuencial)
  // Entrada: puntos (puntos a clasificar), medianas (medianas actuales)
  // Salida: Map[Punto, Seq[Punto]] (asignación de cada mediana a sus puntos)
  def clasificarSeq(puntos: Seq[Punto], medianas: Seq[Punto]): Map[Punto, Seq[Punto]] = {
    puntos.groupBy(p => hallarPuntoMasCercano(p, medianas))
  }

  // Actualiza las medianas calculando el promedio de cada cluster (versión secuencial)
  // Entrada: clasif (asignación puntos-medianas), medianasViejas (medianas actuales)
  // Salida: Seq[Punto] (nuevas medianas calculadas)
  def actualizarSeq(clasif: Map[Punto, Seq[Punto]], medianasViejas: Seq[Punto]): Seq[Punto] = {
    medianasViejas.map { medianaVieja =>
      val puntosDelCluster = clasif.getOrElse(medianaVieja, Seq.empty)
      if(puntosDelCluster.nonEmpty) calculePromedioSeq(medianaVieja, puntosDelCluster)
      else medianaVieja
    }
  }

  // Verifica si el algoritmo ha convergido (versión secuencial)
  // Entrada: eta (tolerancia), medianasViejas (medianas anteriores), medianasNuevas (medianas actuales)
  // Salida: Boolean (true si convergió, false si no)
  def hayConvergenciaSeq(eta: Double, medianasViejas: Seq[Punto], medianasNuevas: Seq[Punto]): Boolean = {
    medianasViejas.zip(medianasNuevas).forall { case (vieja, nueva) =>
      vieja.distanciaAlCuadrado(nueva) < eta
    }
  }

  // Algoritmo completo de K-Means (versión secuencial recursiva de cola)
  // Entrada: puntos (puntos a clusterizar), medianas (medianas iniciales), eta (tolerancia)
  // Salida: Seq[Punto] (medianas finales después de la convergencia)
  @tailrec
  final def kMedianasSeq(puntos: Seq[Punto], medianas: Seq[Punto], eta: Double): Seq[Punto] = {
    val clasificacion = clasificarSeq(puntos, medianas)
    val nuevasMedianas = actualizarSeq(clasificacion, medianas)

    if (hayConvergenciaSeq(eta, medianas, nuevasMedianas)) nuevasMedianas
    else kMedianasSeq(puntos, nuevasMedianas, eta)
  }

  // VERSIONES PARALELAS

  // Clasifica puntos en clusters usando paralelismo de tareas
  // Entrada: umb (umbral para paralelismo), puntos (puntos a clasificar), medianas (medianas actuales)
  // Salida: Map[Punto, Seq[Punto]] (asignación de cada mediana a sus puntos)
  def clasificarPar(umb: Int)(puntos: Seq[Punto], medianas: Seq[Punto]): Map[Punto, Seq[Punto]] = {
    if (puntos.size <= umb) clasificarSeq(puntos, medianas)
    else {
      val (left, right) = puntos.splitAt(puntos.size / 2)
      val result = parallel(
        clasificarPar(umb)(left, medianas),
        clasificarPar(umb)(right, medianas)
      )
      combinarMaps(result._1,result._2)
    }
  }

  // Combina dos mapas de clasificación sumando las secuencias de puntos por mediana
  // Entrada: m1, m2 (mapas a combinar)
  // Salida: Map[Punto, Seq[Punto]] (mapa combinado)
  def combinarMaps(m1: Map[Punto, Seq[Punto]], m2: Map[Punto, Seq[Punto]]): Map[Punto, Seq[Punto]] = {
    (m1.keySet ++ m2.keySet).map { key =>
      val a = m1.getOrElse(key, Seq.empty)
      val b = m2.getOrElse(key, Seq.empty)
      key -> (a ++ b)
    }.toMap
  }

  // Actualiza las medianas usando paralelismo de datos
  // Entrada: clasif (asignación puntos-medianas), medianasViejas (medianas actuales)
  // Salida: Seq[Punto] (nuevas medianas calculadas en paralelo)
  def actualizarPar(clasif: Map[Punto, Seq[Punto]], medianasViejas: Seq[Punto]): Seq[Punto] = {
    val indexed: Seq[(Int, Punto)] = medianasViejas.zipWithIndex.map { case (m, i) => (i, m) }
    val computed: Seq[(Int, Punto)] = indexed.par.map { case (i, medianaVieja) =>
      val pts = clasif.getOrElse(medianaVieja, Seq.empty)
      val nueva = if (pts.nonEmpty) calculePromedioPar(medianaVieja, pts) else medianaVieja
      (i, nueva)
    }.toList
    computed.sortBy(_._1).map(_._2)
  }

  // Verifica convergencia usando paralelismo de datos
  // Entrada: eta (tolerancia), medianasViejas (medianas anteriores), medianasNuevas (medianas actuales)
  // Salida: Boolean (true si convergió, false si no)
  def hayConvergenciaPar(eta: Double, medianasViejas: Seq[Punto], medianasNuevas: Seq[Punto]): Boolean = {
    // Usar paralelismo de datos para verificar convergencia
    medianasViejas.zip(medianasNuevas).par.forall { case (vieja, nueva) =>
      vieja.distanciaAlCuadrado(nueva) < eta
    }
  }

  // Algoritmo completo de K-Means (versión paralela recursiva de cola)
  // Entrada: puntos (puntos a clusterizar), medianas (medianas iniciales), eta (tolerancia)
  // Salida: Seq[Punto] (medianas finales después de la convergencia)
  @tailrec
  final def kMedianasPar(puntos: Seq[Punto], medianas: Seq[Punto], eta: Double): Seq[Punto] = {
    val clasificacion = clasificarPar(umbral(puntos.length))(puntos, medianas)
    val nuevasMedianas = actualizarPar(clasificacion, medianas)

    if (hayConvergenciaPar(eta, medianas, nuevasMedianas)) nuevasMedianas
    else kMedianasPar(puntos, nuevasMedianas, eta)
  }
}