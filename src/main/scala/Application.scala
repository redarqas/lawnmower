package org.algo.examples

object Application {

  //Alias pour les coordonnes 
  type Coord = (Int, Int)

  //Tondeuse : position + direction
  case class Mower(coord: Coord, dir: Char) {
    override def toString = coord._1.toString ++ " " ++ coord._2.toString ++ " " ++ dir.toString
  }

  //Console d'admin de la tondeuse
  case class Joystick(maxCoord: Coord) {

    def right(that: Mower): Mower = that match {
      case Mower(p, 'N') => Mower(p, 'E')
      case Mower(p, 'E') => Mower(p, 'S')
      case Mower(p, 'S') => Mower(p, 'W')
      case Mower(p, 'W') => Mower(p, 'N')
    }

    def left(that: Mower): Mower = that match {
      case Mower(p, 'N') => Mower(p, 'W')
      case Mower(p, 'W') => Mower(p, 'S')
      case Mower(p, 'S') => Mower(p, 'E')
      case Mower(p, 'E') => Mower(p, 'N')
    }

    def move(that: Mower): Mower = that match {
      case s @ Mower((x, y), 'N') =>
        if (y + 1 > maxCoord._2) s
        else Mower((x, y + 1), 'N')
      case s @ Mower((x, y), 'E') =>
        if (x + 1 > maxCoord._1) s
        else Mower((x + 1, y), 'E')
      case s @ Mower((x, y), 'S') =>
        if (y - 1 < 0) s
        else Mower((x, y - 1), 'S')
      case s @ Mower((x, y), 'W') =>
        if (x - 1 < 0) s
        else Mower((x - 1, y), 'W')
    }

    //Dispatcher de commande - action
    val dispatch = Map(('D' -> right _), ('G' -> left _), ('A' -> move _))
    //Ordonner l'execution liste d'instructions
    def instruct(initState: Mower, instructions: String): Mower = {
      instructions.foldLeft(initState)((state, command) => this.dispatch(command)(state))
    }

  }

  //Parser des commandes fourni sous forme de string
  import scala.util.parsing.combinator._
  //Type l'erreur de parsing 
  case class InvalidInputException(msg: String) extends Exception(msg)

  object InputParser extends JavaTokenParsers {
    val limitsP: Parser[Coord] = decimalNumber ~ decimalNumber ^^
      { case mx ~ my => (mx.toInt, my.toInt) }

    val stateP: Parser[Mower] = decimalNumber ~ decimalNumber ~ """[NESW]""".r ^^
      { case mx ~ my ~ s => Mower((mx.toInt, my.toInt), s.head) }

    val instructionsP: Parser[String] = "[DGA]+".r

    //Lit les limit du terrain
    def limits(line: String): Coord = {
      parseAll(limitsP, line) match {
        case Success(r, _) => r
        case e: NoSuccess => throw InvalidInputException(e.msg)
      }
    }
    //Lit l'état initail de la tondeuse
    def state(line: String): Mower = {
      parseAll(stateP, line) match {
        case Success(r, _) => r
        case e: NoSuccess => throw InvalidInputException(e.msg)
      }
    }
    //Lit les instruction à exécuter
    def instructions(line: String): String = {
      parseAll(instructionsP, line) match {
        case Success(r, _) => r
        case e: NoSuccess => throw InvalidInputException(e.msg)
      }
    }
  }

  object Solution {
    import InputParser._
    //Methode generique depuis un iterator
    def solveIterator(iter: Iterator[String]): List[Mower] = {
      val joystick = if (iter.hasNext) Joystick(limits(iter.next))
      else throw InvalidInputException("Invalid file structure : unable to read top limits")

      def actions(joystick: Joystick, group: iter.GroupedIterator[String]): List[Mower] = {
        def foldIter(e: Seq[String], acc: List[Mower]): List[Mower] = e match {
          case List(s, i) => joystick.instruct(state(s), instructions(i)) :: acc
          case _ => throw InvalidInputException("Invalid file structure : unable to read mower init state and instruction")
        }
        group.foldRight(Nil: List[Mower])(foldIter _)
      }

      actions(joystick, iter grouped 2)
    }

    //Resoudre dpeuis un echaine de caractéres
    def solveString(s: String): List[Mower] = {
      solveIterator(s.lines)
    }
    
    //Resoudre depuis un fichier
    def solveFile(filePath: String): List[Mower] = {
      val source = scala.io.Source.fromFile(filePath)
      val result = solveIterator(source.getLines)
      source.close()
      result
    }

  }

  //Jouer l'exemple fournit 
  def main(args: Array[String]) : Unit = {
    import Solution._
    val input = """5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA """
    solveString(input).foreach(println)
  }

}
