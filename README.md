# Solution pour la société MowItNow

Projet SBT 0.13.0, Scala 2.10.3

## Solution pour l'exemple fournit 

Exécuter la commande run dans la console sbt

## Tester la solution 

Un test BDD avec ScalaTest est fourni avec le livrable, il peut être enrichie pour jouer d'autres scenarios. 
On peut fournir des String ou des fichiers en entrée.

## Tester depuis la console Scala (en passant par SBT)

### Fichier : 

org.algo.examples.Application.Solution.solveFile("/tmp/test.txt").foreach(println)

### String : 

org.algo.examples.Application.Solution.solveFile("5 5\n1 2 N\nGAGAGAGAA").foreach(println)