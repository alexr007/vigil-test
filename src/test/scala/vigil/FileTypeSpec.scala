package vigil

import cats.implicits.catsSyntaxOptionId
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class FileTypeSpec extends AnyFunSpec with Matchers {

  import vigil.FileType._

  it("parses cvs extension") {
    detect("1.CSV") shouldEqual CSV.some
  }

  it("parses tvs extension") {
    detect("2.tsv") shouldEqual TSV.some
  }

  it("handles wrong stuff properly") {
    detect("3.txt") shouldEqual None
  }

}
