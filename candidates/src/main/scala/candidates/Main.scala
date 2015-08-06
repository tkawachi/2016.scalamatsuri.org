package candidates

import candidates.Lang.{ English, Japanese }
import candidates.pages.{ ListPage, DetailPage }
import candidates.Page._
import japgolly.scalajs.react.extra.router2.StaticDsl.RouteB
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ React, ReactComponentB }
import japgolly.scalajs.react.extra.router2.{ Router, RouterConfigDsl, BaseUrl, Redirect }
import org.scalajs.dom
import org.scalajs.dom.Element

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object Main extends JSApp {
  // HTML element ID to embed this application
  val appElementId = "candidates"

  val defaultLang = Lang.English

  def parseLang(element: Element): Lang =
    Option(element.getAttribute("data-lang")).flatMap(Lang.valueOf)
      .getOrElse(defaultLang)

  def parseLang(): Lang =
    parseLang(dom.document.getElementById(appElementId))

  @JSExport
  override def main(): Unit = {

    val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
      import dsl._

      (
        staticRoute("#list", ListP) ~>
        renderR(ctl => ListPage(parseLang(), ctl)) |

        dynamicRouteCT(("#" ~ int).caseclass1(DetailP.apply)(DetailP.unapply)) ~>
        dynRenderR { case (DetailP(id), ctl) => DetailPage(parseLang(), id, ctl) }

      )
        .notFound(redirectToPage(ListP)(Redirect.Replace))
    }

    val baseUrl = BaseUrl(dom.window.location.href.replaceFirst("#.*$", ""))
    val router = Router(baseUrl, routerConfig)

    React.render(router(), dom.document.getElementById(appElementId))
  }
}
