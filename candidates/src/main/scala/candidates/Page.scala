package candidates

sealed trait Page

object Page {
  case object ListP extends Page
  case class DetailP(id: Int) extends Page
}
