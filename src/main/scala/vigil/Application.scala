package vigil

import cats.effect.kernel._
import cats.implicits._
import fs2._
import fs2.io.file.Files
import fs2.io.file.Path
import java.nio.file.{Files => JFiles}

class Application[F[_]: Sync](implicit ff: Files[F]) {

  private def streamFolderFileNames(name: String) =
    ff.list(Path(name))
      .evalFilter(p => F.delay(!JFiles.isDirectory(p.toNioPath)))
      .filter(p => FileType.detect(p.toString).isDefined)

  private def readFileContents(path: Path) =
    ff.readAll(path)
      .through(text.utf8.decode)
      .through(text.lines)
      .drop(1)

  private def writeFileContents(dstPath: Path, name: String, data: (Int, Int)) = {
    val (key, value) = data
    val contents = s"$key\t$value"
    val target = dstPath / s"$name.tsv"

    Stream
      .emit(contents)
      .through(text.utf8.encode)
      .through(ff.writeAll(target))
      .compile
      .drain
  }

  private def doTheLogic(srcPath: Path, fileType: FileType, dstPath: Path) =
    readFileContents(srcPath)
      .map(fileType.lineParser.parse)
      .unNone
      .compile
      // TODO: 1. we can make it using O(N values)
      //  by writing custom collector, which will count during collection
      // TODO: 2. we can stream here and use O(1) memory
      //  but only in case we have guarantee that all the values in order
      .toList
      .map(Logic.process)
      .flatMap { data =>
        val nameOnly = fileType.nameWoExt(srcPath.fileName.toString)
        writeFileContents(dstPath, nameOnly, data)
      }

  private def processOne(srcPath: Path, dstPath: String) =
    FileType
      .detect(srcPath.toString)
      .map { fileType => doTheLogic(srcPath, fileType, Path(dstPath)) }
      .getOrElse(F.unit)

  def run(srcPath: String, dstPath: String)(implicit ev: Concurrent[F]) =
    streamFolderFileNames(srcPath)
      .parEvalMap(10)(srcPath => processOne(srcPath, dstPath))
      .compile
      .drain

}

object Application {

  def go[F[_]: Sync: Concurrent: Files](srcPath: String, dstPath: String) =
    new Application[F].run(srcPath, dstPath)

}
