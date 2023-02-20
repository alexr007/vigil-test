package vigil

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Launcher extends IOApp {

  private def validateParams(args: List[String]): Either[String, (String, String)] = args match {
    case src :: dst :: _ => (src, dst).asRight
    case _              => "Source and Target paths expected as parameters".asLeft
  }

  override def run(args: List[String]): IO[ExitCode] =
    validateParams(args)
      .fold(
        errorMessage => IO.println(errorMessage).as(ExitCode.Error),
        { case (src, dst) => Application.go[IO](src, dst).as(ExitCode.Success) }
      )

}
