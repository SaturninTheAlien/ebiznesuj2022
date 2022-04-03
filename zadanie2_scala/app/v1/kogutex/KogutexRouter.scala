package v1.kogutex

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter

import javax.inject.Inject
import play.api.routing.sird._


class KogutexRouter @Inject()(controller: KogutexController) extends SimpleRouter{
  val prefix = "/api/v1/kogutex/"

  def link(id: Int): String = {
    import io.lemonlabs.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {

    case GET(p"/hello") =>
      controller.hello



    case GET(p"/product") =>
      controller.indexProduct

    case GET(p"/product/$id<[0-9]+>") =>
      controller.getProduct(id.toInt)

    case POST(p"/product") =>
      controller.postProduct

    case PUT(p"/product/$id<[0-9]+>") =>
      controller.putProduct(id.toInt)

    case DELETE(p"/product/$id<[0-9]+>") =>
      controller.deleteProduct(id.toInt)




    case GET(p"/chicken_breed") =>
      controller.indexChickenBreed

    case GET(p"/chicken_breed/$id<[0-9]+>") =>
      controller.getChickenBreed(id.toInt)

    case POST(p"/chicken_breed") =>
      controller.postChickenBreed

    case PUT(p"/chicken_breed/$id<[0-9]+>") =>
      controller.putChickenBreed(id.toInt)

    case DELETE(p"/chicken_breed/$id<[0-9]+>") =>
      controller.deleteChickenBreed(id.toInt)



    case GET(p"/category") =>
      controller.indexCategory

    case GET(p"/category/$id<[0-9]+>") =>
      controller.getCategory(id.toInt)

    case POST(p"/category") =>
      controller.postCategory

    case PUT(p"/category/$id<[0-9]+>") =>
      controller.putCategory(id.toInt)

    case DELETE(p"/category/$id<[0-9]+>") =>
      controller.deleteCategory(id.toInt)

  }
}
