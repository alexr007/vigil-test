package vigil

import cats.implicits.catsSyntaxOptionId
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class LineParserSpec extends AnyFunSpec with Matchers {

  import vigil.LineParser._

  it("parses cvs properly") {
    val line = "123,456"
    CSVLineParser.parse(line) shouldEqual (123,456).some
  }

  it("parses tsv properly") {
    val line = "456\t789"
    TSVLineParser.parse(line) shouldEqual (456,789).some
  }

  it("handles crap properly #1") {
    val line = "123"
    TSVLineParser.parse(line) shouldEqual None
  }
  it("handles crap properly #2") {
    val line = "456 789"
    TSVLineParser.parse(line) shouldEqual None
  }

}
