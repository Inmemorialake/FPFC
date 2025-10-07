package workshop_3


final class Pruebas$_ {
def args = Pruebas_sc.args$
def scriptPath = """workshop_3/Pruebas.sc"""
/*<script>*/
import Huffman._

// Helpers de aserciones y reporte
case class TestResult(nombre:String, ok:Boolean, detalle:String = "")
val resultados = scala.collection.mutable.ArrayBuffer.empty[TestResult]
var total = 0
var fallos = 0

def assertCond(cond:Boolean, msg:String = "") = {
	total += 1
	if(!cond){
		fallos += 1
		resultados += TestResult(s"Test #$total", false, msg)
	} else resultados += TestResult(s"Test #$total", true, msg)
}

def assertEquals[A](obt:A, esp:A, msg:String = "") = assertCond(
	obt == esp,
	msg + s" | Esperado: $esp, Obtenido: $obt"
)

def section(titulo:String)(body: => Unit) = {
	println(s"\n=== $titulo ===")
	body
}

// Datos comunes
val textoBase = "abbcccdddd" // frecuencias: a1 b2 c3 d4
val listaCharsBase = cadenaALista(textoBase)
val arbolBase = crearArbolDeHuffman(listaCharsBase)
val tablaBase = convertir(arbolBase)

// 1. peso (5 pruebas)
section("peso") {
	val hA = Hoja('a', 5)
	val hB = Hoja('b', 2)
	val nodo = hacerNodoArbolH(hA, hB) // peso 7
	assertEquals(peso(hA), 5, "Hoja a") // 1
	assertEquals(peso(hB), 2, "Hoja b") // 2
	assertEquals(peso(nodo), 7, "Nodo suma") // 3
	val nodo2 = hacerNodoArbolH(nodo, hA) // 7 + 5 = 12
	assertEquals(peso(nodo2), 12, "Nodo compuesto") // 4
	assertEquals(peso(arbolBase), listaCharsBase.size, "Arbol base") // 5
}

// 2. cars (5 pruebas)
section("cars") {
	val h = Hoja('z', 1)
	assertEquals(cars(h), List('z'), "Hoja z") // 1
	val n = hacerNodoArbolH(Hoja('a',1), Hoja('b',1))
	assertEquals(cars(n).sorted, List('a','b'), "Nodo a-b") // 2
	val n2 = hacerNodoArbolH(n, Hoja('c',1))
	assertCond(cars(n2).contains('c'), "Nodo incluye c") // 3
	assertEquals(cars(arbolBase).sorted, listaCharsBase.distinct.sorted, "Base chars") // 4
	assertEquals(cars(arbolBase).distinct.size, cars(arbolBase).size, "Sin duplicados") // 5
}

// 3. hacerNodoArbolH (5 pruebas)
section("hacerNodoArbolH") {
	val h1 = Hoja('x',3)
	val h2 = Hoja('y',4)
	val n = hacerNodoArbolH(h1,h2)
	assertEquals(peso(n), 7, "Peso xy") // 1
	assertEquals(cars(n).sorted, List('x','y'), "Chars xy") // 2
	val h3 = Hoja('z',1)
	val n2 = hacerNodoArbolH(n,h3)
	assertEquals(peso(n2), 8, "Peso xyz") // 3
	assertCond(cars(n2).contains('z'), "Incluye z") // 4
	assertEquals(cars(n2).size, 3, "Tres chars") // 5
}

// 4. cadenaALista (5 pruebas)
section("cadenaALista") {
	assertEquals(cadenaALista(""), Nil, "Vacía") // 1
	assertEquals(cadenaALista("a"), List('a'), "Uno") // 2
	assertEquals(cadenaALista("ab"), List('a','b'), "Dos") // 3
	assertEquals(cadenaALista("aba"), List('a','b','a'), "Duplicados") // 4
	assertEquals(cadenaALista(textoBase).mkString, textoBase, "Base") // 5
}

// 5. ocurrencias (5 pruebas)
section("ocurrencias") {
	assertEquals(ocurrencias(Nil), Nil, "Vacía") // 1
	assertEquals(ocurrencias(List('a')), List(('a',1)), "Un elem") // 2
	val occ = ocurrencias(List('a','a','b'))
	assertCond(occ.contains(('a',2)) && occ.contains(('b',1)), "a=2 b=1") // 3
	val occ2 = ocurrencias(listaCharsBase)
	assertCond(occ2.toMap == Map('a'->1,'b'->2,'c'->3,'d'->4), "Frecs base") // 4
	assertEquals(occ2.map(_._1), List('a','b','c','d'), "Orden") // 5
}

// 6. listaDeHojasOrdenadas (5 pruebas)
section("listaDeHojasOrdenadas") {
	val hojas = listaDeHojasOrdenadas(List(('b',3),('a',1),('c',2)))
	assertEquals(hojas.map(_.car), List('a','c','b'), "Orden asc") // 1
	assertEquals(hojas.map(_.peso), List(1,2,3), "Pesos asc") // 2
	val hojas2 = listaDeHojasOrdenadas(ocurrencias(listaCharsBase))
	assertEquals(hojas2.size, 4, "Tamaño 4") // 3
	assertEquals(hojas2.head.car, 'a', "Primera a") // 4
	assertEquals(hojas2.last.car, 'd', "Ultima d") // 5
}

// 7. listaUnitaria (5 pruebas)
section("listaUnitaria") {
	assertCond(!listaUnitaria(Nil), "Nil") // 1
	assertCond(listaUnitaria(List(Hoja('a',1))), "Uno") // 2
	val hs = List(Hoja('a',1), Hoja('b',2))
	assertCond(!listaUnitaria(hs), "Dos") // 3
	assertCond(!listaUnitaria(hs :+ Hoja('c',3)), "Tres") // 4
	assertCond(listaUnitaria(List(arbolBase)), "Arbol base") // 5
}

// 8. combinar (5 pruebas)
section("combinar") {
	val hojas = listaDeHojasOrdenadas(List(('a',1),('b',2),('c',3)))
	val comb1 = combinar(hojas)
	assertEquals(comb1.size, 2, "Reduce 3->2") // 1
	assertEquals(peso(comb1.head)+ comb1.tail.map(peso).sum, 6, "Suma pesos") // 2
	val comb2 = combinar(comb1.sortBy(peso))
	assertEquals(comb2.size, 1, "Reduce 2->1") // 3
	assertEquals(combinar(comb2), comb2, "Idempotente unitaria") // 4
	assertEquals(combinar(Nil), Nil, "Nil") // 5
}

// 9. hastaQue (5 pruebas)
section("hastaQue") {
	val hojas = listaDeHojasOrdenadas(ocurrencias(listaCharsBase))
	val res = hastaQue(listaUnitaria, combinar)(hojas)
	assertEquals(res.size,1,"Final unitaria") // 1
	val res2 = hastaQue(listaUnitaria, combinar)(res)
	assertEquals(res2, res, "Idempotente") // 2
	val res3 = hastaQue(_.isEmpty, combinar)(Nil)
	assertEquals(res3, Nil, "Nil cond") // 3
	val hs2 = listaDeHojasOrdenadas(List(('a',1),('b',2)))
	val res4 = hastaQue(listaUnitaria, combinar)(hs2)
	assertEquals(res4.size,1,"Dos->una") // 4
	assertEquals(peso(res4.head), hs2.map(_.peso).sum, "Peso suma") // 5
}

// 10. crearArbolDeHuffman (5 pruebas)
section("crearArbolDeHuffman") {
	assertEquals(peso(arbolBase), listaCharsBase.size, "Peso base") // 1
	assertEquals(cars(arbolBase).sorted, listaCharsBase.distinct.sorted, "Chars base") // 2
	val arbolUno = crearArbolDeHuffman(cadenaALista("aaaa"))
	assertEquals(peso(arbolUno),4,"Peso uno") // 3
	assertEquals(cars(arbolUno), List('a'), "Solo a") // 4
	val arbolDos = crearArbolDeHuffman(cadenaALista("ab"))
	assertEquals(peso(arbolDos),2,"Peso ab") // 5
}

// 11. decodificar (5 pruebas)
section("decodificar") {
	val bitsBase = codificar(arbolBase)(listaCharsBase)
	assertEquals(decodificar(arbolBase, bitsBase).mkString, textoBase, "Roundtrip básico") //1
	val bits2 = codificar(arbolBase)(listaCharsBase ++ listaCharsBase)
	assertEquals(decodificar(arbolBase, bits2).size, listaCharsBase.size*2, "Duplicado") //2
	assertCond(decodificar(arbolBase, bitsBase).nonEmpty, "No vacío") //3
	val arbolUno = crearArbolDeHuffman(cadenaALista("aaaa"))
	val bitsUno = codificar(arbolUno)(List('a','a'))
	assertEquals(decodificar(arbolUno, bitsUno).mkString, "aa", "Hoja única") //4
	val bitsRap = codificarRapido(arbolBase)(listaCharsBase)
	assertEquals(decodificar(arbolBase, bitsRap).mkString, textoBase, "Roundtrip rápido") //5
}

// 12. codificar (5 pruebas)
section("codificar") {
	val bits = codificar(arbolBase)(listaCharsBase)
	assertCond(bits.nonEmpty, "No vacío") //1
	assertEquals(decodificar(arbolBase, bits).mkString, textoBase, "Roundtrip") //2
	val bits2 = codificar(arbolBase)(listaCharsBase ++ listaCharsBase)
	assertEquals(bits2.count(_==0) + bits2.count(_==1), bits2.size, "Solo 0/1") //3
	val arbolUno = crearArbolDeHuffman(cadenaALista("aaaa"))
	assertEquals(codificar(arbolUno)(List('a','a')), List(), "Código hoja única") //4
	val parcial = codificar(arbolBase)(List('d','d','c'))
	assertEquals(decodificar(arbolBase, parcial).mkString, "ddc", "Parcial") //5
}

// 13. codigoEnBits (5 pruebas)
section("codigoEnBits") {
	val t = tablaBase
	val todas = cars(arbolBase)
	assertEquals(t.map(_._1).toSet, todas.toSet, "Cobertura") //1
	val bA = codigoEnBits(t)('a')
	val bB = codigoEnBits(t)('b')
	assertCond(bA != bB, "Distintos a/b") //2
	assertEquals(codigoEnBits(t)('a'), bA, "Determinista a") //3
	assertEquals(codigoEnBits(t)('#'), Nil, "Ausente #") //4
	assertCond(bA.nonEmpty || todas.size==1, "Longitud válida") //5
}

// 14. mezclarTablasDeCodigos (5 pruebas)
section("mezclarTablasDeCodigos") {
	val ta: TablaCodigos = List(('a', List(0)), ('b', List(1)))
	val tb: TablaCodigos = List(('c', List(0)), ('d', List(1)))
	val mix = mezclarTablasDeCodigos(ta, tb)
	assertEquals(mix.size, 4, "Tamaño mix") //1
	assertCond(mix.find(_._1=='a').exists(_._2.head==0), "Prefijo a 0") //2
	assertCond(mix.find(_._1=='c').exists(_._2.head==1), "Prefijo c 1") //3
	assertCond(mix.map(_._2).forall(_.nonEmpty), "No vacíos") //4
	assertCond(mix.map(_._2).distinct.size==4, "Únicos") //5
}

// 15. convertir (5 pruebas)
section("convertir") {
	val t = convertir(arbolBase)
	val chars = cars(arbolBase)
	assertEquals(t.map(_._1).sorted, chars.sorted, "Todos chars") //1
	assertCond(t.exists(_._2.nonEmpty) || t.size==1, "Algún no vacío") //2
	assertEquals(t.map(_._2).distinct.size, t.size, "Sin duplicar") //3
	val tUno = convertir(crearArbolDeHuffman(cadenaALista("aaaa")))
	assertEquals(tUno, List(('a', Nil)), "Hoja única") //4
	assertCond(t.map(_._2.size).sum.toDouble / t.size <= chars.size, "Media ok") //5
}

// 16. codificarRapido (5 pruebas)
section("codificarRapido") {
	val bits = codificarRapido(arbolBase)(listaCharsBase)
	val bitsNorm = codificar(arbolBase)(listaCharsBase)
	assertEquals(bits, bitsNorm, "Igual codificar") //1
	val bits2 = codificarRapido(arbolBase)(List('d','d','c'))
	assertEquals(decodificar(arbolBase, bits2).mkString, "ddc", "Parcial") //2
	val arbolUno = crearArbolDeHuffman(cadenaALista("aaaa"))
	assertEquals(codificarRapido(arbolUno)(List('a','a')), List(), "Hoja única vacío") //3
	val bitsDup = codificarRapido(arbolBase)(listaCharsBase ++ listaCharsBase)
	assertEquals(decodificar(arbolBase, bitsDup).mkString, textoBase*2, "Duplicado") //4
	assertEquals(bits.size, bitsNorm.size, "Misma longitud") //5
}

// Resumen final
println("\n================= RESUMEN =================")
println(s"Total tests: $total  | Fallos: $fallos  | Exitos: ${total - fallos}")
if(fallos>0){
	println("\nFallos detallados:")
	resultados.filterNot(_.ok).zipWithIndex.foreach{ case (r,i) => println(s"${i+1}. ${r.nombre} -> ${r.detalle}") }
} else println("Todas las pruebas pasaron correctamente.")


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

