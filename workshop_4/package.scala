package object SubsecuenciaMasLarga {
  type Secuencia = Seq[Int]
  type Subsecuencia = Seq[Int]

  def subindices(i: Int, n: Int): Set[Seq[Int]] = { //**
    val m = n - i
    if (m <= 0) Set(List.empty)
    else {
      (for {
        bitmask <- 0 until (1 << m)
      } yield {
        (for (j <- 0 until m if (bitmask & (1 << j)) != 0) yield i + j).toList
      }).toSet + List.empty
    }
  }

  def subSecuenciaAsoc(s: Secuencia, inds: Seq[Int]): Subsecuencia = {
    (for (index <- inds) yield s(index)).toList
  }

  def subSecuenciasDe(s: Secuencia): Set[Subsecuencia] = {
    val indicesSet = subindices(0, s.length)
    for (inds <- indicesSet) yield subSecuenciaAsoc(s, inds).toList
  }

  def incremental(s: Subsecuencia): Boolean = {
    val n = s.length
    if (n <= 1) true
    else {
      val todosIncrementales = for {
        i <- 1 until n
        if s(i-1) < s(i)
      } yield true
      todosIncrementales.length == (n - 1)
    }
  }

  def subSecuenciasInc(s: Secuencia): Set[Subsecuencia] = { //**
    for {
      sub <- subSecuenciasDe(s)
      if incremental(sub)
    } yield sub.toList
  }

  def subsecuenciaIncrementalMasLarga(s: Secuencia): Subsecuencia = { //**
    val subs = subSecuenciasInc(s)
    if (subs.isEmpty) List.empty
    else {
      subs.foldLeft(subs.head) { (maxSub, currentSub) =>
        if (currentSub.length > maxSub.length) currentSub else maxSub
      }.toList
    }
  }

  def ssimlComenzandoEn(i: Int, s: Secuencia): Subsecuencia = {
    val n = s.length
    if (i >= n) List.empty
    else {
      val candidatos = for {
        j <- i + 1 until n
        if s(j) > s(i)
      } yield ssimlComenzandoEn(j, s).toList

      if (candidatos.isEmpty) List(s(i))
      else {
        val masLarga = candidatos.reduceLeft { (currentMax, candidato) =>
          if (candidato.length > currentMax.length) candidato else currentMax
        }
        s(i) +: masLarga
      }
    }
  }

  def subSecIncMasLargaV2(s: Secuencia): Subsecuencia = { //**
    val n = s.length
    if (n == 0) List.empty
    else {
      def construirMemo(indices: Seq[Int], memo: Map[Int, Subsecuencia]): Map[Int, Subsecuencia] = {
        if (indices.isEmpty) memo
        else {
          val i = indices.head
          val resultado = if (i >= n) List.empty
          else {
            val candidatos = for {
              j <- i + 1 until n
              if s(j) > s(i)
            } yield memo.getOrElse(j, List.empty)

            val candidatosFiltrados = candidatos.filter(_.nonEmpty)

            if (candidatosFiltrados.isEmpty) List(s(i))
            else {
              val masLarga = candidatosFiltrados.reduceLeft { (currentMax, candidato) =>
                if (candidato.length > currentMax.length) candidato else currentMax
              }
              s(i) +: masLarga
            }
          }
          construirMemo(indices.tail, memo + (i -> resultado))
        }
      }

      val indicesInvertidos = (0 until n).toList.reverse
      val memoCompleto = construirMemo(indicesInvertidos, Map.empty)

      memoCompleto.values.reduceLeft { (currentMax, candidato) =>
        if (candidato.length > currentMax.length) candidato else currentMax
      }.toList
    }
  }
}