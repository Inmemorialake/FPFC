package object Huffman {

  abstract class ArbolH
  case class Nodo(izq: ArbolH, der: ArbolH, cars: List[Char], peso: Int) extends ArbolH
  case class Hoja(car: Char, peso: Int) extends ArbolH

  def peso(arbol: ArbolH): Int = arbol match {
    case Hoja(_, p)       => p
    case Nodo(_, _, _, p) => p
  }

  def cars(arbol: ArbolH): List[Char] = arbol match {
    case Hoja(c, _)       => List(c)
    case Nodo(_, _, cs,_) => cs
  }

  def hacerNodoArbolH(izq: ArbolH, der: ArbolH): Nodo =
    Nodo(izq, der, cars(izq) ::: cars(der), peso(izq) + peso(der))

  def cadenaALista(cad: String): List[Char] = cad.toList

  def ocurrencias(cars: List[Char]): List[(Char, Int)] = {
    def contar(xs: List[Char], c: Char): Int =
      xs match {
        case Nil => 0
        case h :: t => if (h == c) 1 + contar(t, c) else contar(t, c)
      }
    cars.distinct.map(c => (c, contar(cars, c)))
  }

  def listaDeHojasOrdenadas(frecs: List[(Char, Int)]): List[Hoja] =
    frecs.sortBy(_._2).map { case (c, f) => Hoja(c, f) }

  def listaUnitaria(arboles: List[ArbolH]): Boolean = arboles match {
    case _ :: Nil => true
    case _        => false
  }

  def combinar(arboles: List[ArbolH]): List[ArbolH] = arboles match {
    case a1 :: a2 :: resto =>
      val nuevo = hacerNodoArbolH(a1, a2)
      (nuevo :: resto).sortBy(peso)
    case _ => arboles
  }

  def hastaQue(
    cond: List[ArbolH] => Boolean,
    mezclar: List[ArbolH] => List[ArbolH]
  )(listaOrdenadaArboles: List[ArbolH]): List[ArbolH] = {
    if (cond(listaOrdenadaArboles)) listaOrdenadaArboles
    else hastaQue(cond, mezclar)(mezclar(listaOrdenadaArboles))
  }

  def crearArbolDeHuffman(cars: List[Char]): ArbolH = {
    val hojas = listaDeHojasOrdenadas(ocurrencias(cars))
    hastaQue(listaUnitaria, combinar)(hojas).head
  }

  type Bit = Int

  def decodificar(arbol: ArbolH, bits: List[Bit]): List[Char] = {
    def recorrer(actual: ArbolH, bitsRestantes: List[Bit]): List[Char] = actual match {
      case Hoja(c, _) =>
        if (bitsRestantes.isEmpty) List(c)
        else c :: recorrer(arbol, bitsRestantes)
      case Nodo(izq, der, _, _) => bitsRestantes match {
        case Nil       => Nil
        case 0 :: tail => recorrer(izq, tail)
        case 1 :: tail => recorrer(der, tail)
        case List(_, _*) => ???
      }
    }
    recorrer(arbol, bits)
  }

  def codificar(arbol: ArbolH)(texto: List[Char]): List[Bit] = {
    def codChar(arbol: ArbolH, c: Char): List[Bit] = arbol match {
      case Hoja(ch, _) if ch == c => Nil
      case Nodo(izq, der, _, _) =>
        if (cars(izq).contains(c)) 0 :: codChar(izq, c)
        else 1 :: codChar(der, c)
    }
    texto.flatMap(c => codChar(arbol, c))
  }

  type TablaCodigos = List[(Char, List[Bit])]

  def codigoEnBits(tabla: TablaCodigos)(car: Char): List[Bit] = tabla match {
    case Nil => Nil
    case (c, bits) :: tail =>
      if (c == car) bits else codigoEnBits(tail)(car)
  }

  def mezclarTablasDeCodigos(a: TablaCodigos, b: TablaCodigos): TablaCodigos = {
    val prefA = a.map { case (c, bits) => (c, 0 :: bits) }
    val prefB = b.map { case (c, bits) => (c, 1 :: bits) }
    prefA ::: prefB
  }

  def convertir(arbol: ArbolH): TablaCodigos = arbol match {
    case Hoja(c, _)       => List((c, Nil))
    case Nodo(izq, der, _, _) =>
      mezclarTablasDeCodigos(convertir(izq), convertir(der))
  }

  def codificarRapido(arbol: ArbolH)(texto: List[Char]): List[Bit] = {
    val tabla = convertir(arbol)
    texto.flatMap(codigoEnBits(tabla))
  }

}