# Informe taller sobre reconocimiento de patrones

## Uso del reconocimiento de patrones

## Informe de Corrección

## Casos de Prueba

>A continuación se documentan 5 casos de prueba por cada función definida en `package.scala` (package object `Huffman`). Las tablas muestran entradas (simplificadas), una breve descripción y el resultado esperado. Se ejecutan en `Pruebas.sc` mediante aserciones.

### peso

| Caso | Entrada | Descripción | Resultado Esperado |
|------|---------|-------------|--------------------|
| 1 | Hoja('a',5) | Peso de hoja simple | 5 |
| 2 | Hoja('b',2) | Peso de hoja simple | 2 |
| 3 | Nodo(Hoja('a',5),Hoja('b',2)) | Suma de pesos hijos | 7 |
| 4 | Nodo(Nodo(...,7),Hoja('a',5)) | Suma recursiva 7+5 | 12 |
| 5 | arbolBase (texto "abbcccdddd") | Peso total = longitud | 10 |

### cars

| Caso | Entrada | Descripción | Resultado Esperado |
|------|---------|-------------|--------------------|
| 1 | Hoja('z',1) | Lista de chars de hoja | List('z') |
| 2 | Nodo(Hoja('a',1),Hoja('b',1)) | Unión chars a y b | List('a','b') (orden no crítico) |
| 3 | Nodo(prev,Hoja('c',1)) | Inclusión de nuevo char | Contiene 'c' |
| 4 | arbolBase | Chars del árbol | Distinct de texto base |
| 5 | arbolBase | Sin duplicados | size == distinct.size |

### hacerNodoArbolH

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | (Hoja x3, Hoja y4) | Peso suma | 7 |
| 2 | (Hoja x3, Hoja y4) | Chars combinados | x,y |
| 3 | Nodo(xy, Hoja z1) | Peso acumulado | 8 |
| 4 | Nodo(xy, Hoja z1) | Contiene z | true |
| 5 | Nodo(xy, Hoja z1) | Total chars | 3 |

### cadenaALista

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | "" | Cadena vacía | Nil |
| 2 | "a" | Un carácter | List('a') |
| 3 | "ab" | Dos caracteres | List('a','b') |
| 4 | "aba" | Mantiene duplicados | List('a','b','a') |
| 5 | textoBase | Conversión correcta | coincide |

### ocurrencias

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | Nil | Lista vacía | Nil |
| 2 | List('a') | Un elemento | ('a',1) |
| 3 | a,a,b | Frecuencias parciales | a->2, b->1 |
| 4 | textoBase | Frecuencias completas | a1 b2 c3 d4 |
| 5 | textoBase | Orden aparición | a,b,c,d |

### listaDeHojasOrdenadas

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | (b3,a1,c2) | Orden ascendente | a,c,b |
| 2 | (b3,a1,c2) | Pesos orden asc | 1,2,3 |
| 3 | textoBase | Número de hojas | 4 |
| 4 | textoBase | Primera hoja | 'a' |
| 5 | textoBase | Última hoja | 'd' |

### listaUnitaria

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | Nil | No unitaria | false |
| 2 | [Hoja] | Unitaria | true |
| 3 | [Hoja,Hoja] | No unitaria | false |
| 4 | [3 hojas] | No unitaria | false |
| 5 | [arbolBase] | Unitaria | true |

### combinar

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | 3 hojas (1,2,3) | Reduce tamaño | 2 elementos |
| 2 | post-comb | Conserva suma pesos | 6 |
| 3 | 2 hojas resultantes | Reduce a 1 | 1 elemento |
| 4 | lista unitaria | Idempotente | Igual |
| 5 | Nil | Caso vacío | Nil |

### hastaQue

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | hojas textoBase | Termina en unitaria | size=1 |
| 2 | lista unitaria | Idempotente | misma lista |
| 3 | Nil + cond vacía | Retorna Nil | Nil |
| 4 | 2 hojas | Una combinación | size=1 |
| 5 | 2 hojas | Conserva suma pesos | igual suma |

### crearArbolDeHuffman

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | textoBase | Peso total | 10 |
| 2 | textoBase | Conjunto chars | {a,b,c,d} |
| 3 | "aaaa" | Peso acumulado | 4 |
| 4 | "aaaa" | Chars únicos | a |
| 5 | "ab" | Peso | 2 |

### decodificar

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | bits(textoBase) | Roundtrip | textoBase |
| 2 | bits(duplicado) | Tamaño doble | 2 * len |
| 3 | bits(textoBase) | No vacío | true |
| 4 | bits("aa" árbol único) | Caso hoja única | "aa" |
| 5 | bits rápidos | Equivalente | textoBase |

### codificar

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | textoBase | No vacío | bits != Nil |
| 2 | textoBase | Roundtrip | textoBase |
| 3 | duplicado | Solo 0/1 | true |
| 4 | "aa" árbol único | Código vacío | Nil |
| 5 | ddc | Roundtrip parcial | "ddc" |

### codigoEnBits

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | tablaBase | Cobertura | todos chars |
| 2 | 'a','b' | Códigos distintos | true |
| 3 | 'a' | Determinista | igual |
| 4 | '#' | Ausente => Nil | Nil |
| 5 | 'a' | Longitud válida | >=0 |

### mezclarTablasDeCodigos

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | ta,tb | Tamaño combinado | 4 |
| 2 | ta | Prefijo 0 | true |
| 3 | tb | Prefijo 1 | true |
| 4 | mezcla | Sin códigos vacíos | true |
| 5 | mezcla | Códigos únicos | true |

### convertir

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | arbolBase | Todos chars presentes | true |
| 2 | arbolBase | Algún código no vacío | true |
| 3 | arbolBase | Códigos únicos | true |
| 4 | árbol único | Código vacío | ('a',Nil) |
| 5 | arbolBase | Media longitud razonable | <= num chars |

### codificarRapido

| Caso | Entrada | Descripción | Resultado |
|------|---------|-------------|-----------|
| 1 | textoBase | Igual a codificar | true |
| 2 | ddc | Decodifica parcial | "ddc" |
| 3 | "aa" árbol único | Lista vacía | Nil |
| 4 | duplicado | Roundtrip duplicado | 2 * texto |
| 5 | textoBase | Misma longitud que codificar | true |

## Conclusiones
