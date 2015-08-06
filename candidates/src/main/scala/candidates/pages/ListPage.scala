package candidates.pages

import candidates.Page.DetailP
import candidates.TalkLength.{ Fifteen, Forty }
import candidates._
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.extra.router2.RouterCtl

object ListPage {
  case class Props(lang: Lang, ctl: RouterCtl[Page])

  val component = ReactComponentB[Props]("Candidates list")
    .render { p =>
      def createRow(candidate: Candidate, description: Description) = {
        <.div(
          ^.className := "row candidate-row",
          <.div(
            ^.className := "col-sm-8",
            p.ctl.link(DetailP(candidate.id))(
              description.title
            )
          ),
          <.div(
            ^.className := "col-sm-4",
            candidate.iconUrl.map { url =>
              <.img(^.src := url, ^.className := "icon-mini")
            },
            s" ${description.name}"
          )
        )
      }

      def createList(list: Seq[Candidate], talkLength: TalkLength) = {
        val filtered: Seq[(Candidate, Description)] =
          list
            .filter(_.talkLength == talkLength)
            .flatMap { candidate =>
              candidate.descriptions.get(p.lang).map(candidate -> _)
            }

        if (filtered.nonEmpty) {
          <.div(
            filtered.map((createRow _).tupled)
          )
        } else {
          <.div(^.className := "text-muted", "No sessions found.")
        }
      }

      <.div(
        <.h3("40 minutes sessions"),
        createList(Candidate.shuffledList, Forty),
        <.h3("15 minutes sessions"),
        createList(Candidate.shuffledList, Fifteen)
      )
    }.build

  def apply(lang: Lang, ctl: RouterCtl[Page]) = component(Props(lang, ctl))
}
