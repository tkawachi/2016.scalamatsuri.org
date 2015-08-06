package candidates.pages

import candidates.Page.ListP
import candidates.{ Page, Lang, Candidate }
import japgolly.scalajs.react.{ ReactElement, ReactComponentB }
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.extra.router2.RouterCtl

object DetailPage {
  case class Props(lang: Lang, id: Int, ctl: RouterCtl[Page])

  val component = ReactComponentB[Props]("Candidate detail")
    .render { p =>
      (for {
        candidate <- Candidate.list.find(_.id == p.id)
        desc <- candidate.descriptions.get(p.lang)
      } yield {
        <.div(
          ^.id := s"candidate-${candidate.id}",
          <.h2(desc.title),
          <.div(^.className := "abst", desc.abst),
          <.img(^.src := candidate.iconUrl, ^.className := "icon"),
          desc.name,
          <.hr,
          p.ctl.link(ListP)("Back to list")
        ): ReactElement
      }).getOrElse(<.div("Not found"))
    }.build

  def apply(lang: Lang, id: Int, ctl: RouterCtl[Page]) =
    component(Props(lang, id, ctl))
}
