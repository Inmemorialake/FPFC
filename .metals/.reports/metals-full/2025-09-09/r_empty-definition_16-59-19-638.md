error id: file://<WORKSPACE>/workshop_1/test.sc:
file://<WORKSPACE>/workshop_1/test.sc
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -FuncionesRecursivas.FuncionesRecursivas.
	 -FuncionesRecursivas.
	 -scala/Predef.FuncionesRecursivas.
offset: 24
uri: file://<WORKSPACE>/workshop_1/test.sc
text:
```scala
import FuncionesRecursiv@@as._

// ----- Pruebas de maxLin -----
maxLin(List(5))
maxLin(List(3,2,9,1))
maxLin(List(10,10,10))
maxLin(List(1,2,3,4,5))
maxLin(List(100,2,99,3))

// ----- Pruebas de maxIt -----
maxIt(List(5))
maxIt(List(3,2,9,1))
maxIt(List(10,10,10))
maxIt(List(1,2,3,4,5))
maxIt(List(100,2,99,3))

// ----- Pruebas de movsTorresHanoi -----
movsTorresHanoi(1)
movsTorresHanoi(2)
movsTorresHanoi(3)
movsTorresHanoi(4)
movsTorresHanoi(5)
movsTorresHanoi(64)
val siglo: BigInt = BigInt(60) * BigInt(60) * BigInt(24) * BigInt(365) * BigInt(100)
movsTorresHanoi(64) / siglo

// ----- Pruebas de torresHanoi -----
torresHanoi(1,1,2,3)
torresHanoi(2,1,2,3)
torresHanoi(3,1,2,3)
torresHanoi(4,1,2,3)

```


#### Short summary: 

empty definition using pc, found symbol in pc: 