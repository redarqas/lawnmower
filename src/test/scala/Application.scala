import org.algo.examples.Application._
import org.scalatest._

class AppSpec extends FlatSpec with Matchers {
  import Solution._
  //Tester le cas passant 
  "A mower" should "end in the right place" in {
     val input = """5 5
     1 2 N
     GAGAGAGAA
     3 3 E
     AADAADADDA """
    solveString(input) should be (List[Mower](Mower((1,3), 'N'), Mower((5,1), 'E')))
  }
  
  //Tester le cas non passant : F n'est pas une commande valid ici
  it should "throw InvalidInputException if the input is not parseable" in {
    val input = """5 5
      1 2 N
      F"""
    a [InvalidInputException] should be thrownBy {
      solveString(input)
    } 
  }
}