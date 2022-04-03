package v1.kogutex

import play.api.libs.json.{Format, Json}
import play.api.mvc
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Results}

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import v1.kogutex.model.{CategoryRepository, ChickenBreedRepository, ProductRepository}

import java.lang.Readable

case class RestMessage(detail:String)

object RestMessage {
  implicit val format: Format[RestMessage] = Json.format
}

class KogutexController @Inject()(cc: KogutexControllerComponents)(
  implicit ec: ExecutionContext)
  extends KogutexBaseController(cc) {


  def hello: Action[AnyContent] = helloMessage("Witaj w Kogutex, sklepie dla hodowców kurczaków i innego drobiu.")

  def helloMessage(message:String): Action[AnyContent] = Action { implicit request =>
    Results.Ok( Json.toJson(RestMessage(message)))
  }


  def indexProduct : Action[AnyContent] = Action {implicit request =>
    Results.Ok(Json.toJson(ProductRepository.all))
  }

  def getProduct(id:Int) : Action[AnyContent] = Action {implicit request =>
    val product = ProductRepository.get(id)
    if(product.isDefined)Results.Ok(Json.toJson(product.get))
    else Results.NotFound(Json.toJson(RestMessage(s"Product with id: $id not found")))
  }

  def postProduct: Action[AnyContent] = Action {implicit request =>
    try {

      val product = ProductRepository.post(request.body)
      if (product.isDefined) Results.Created(Json.toJson(product.get))
      else Results.BadRequest(Json.toJson(RestMessage("Incorrect JSON")))
    }
    catch {
      case e: RuntimeException => Results.BadRequest(Json.toJson(RestMessage(e.getMessage)))
    }
  }

  def putProduct(id:Int) : Action[AnyContent] = Action {implicit request =>
    try {
      val product = ProductRepository.put(id, request.body)
      if(product.isDefined)Results.Ok(Json.toJson(product.get))
      else Results.BadRequest(Json.toJson(RestMessage("Incorrect JSON")))
    }
    catch {
      case e: RuntimeException => Results.BadRequest(Json.toJson(RestMessage(e.getMessage)))
    }
  }

  def deleteProduct(id:Int) : Action[AnyContent] = Action {implicit request =>
    val tmp = ProductRepository.delete(id)
    if(tmp)Results.NoContent
    else Results.NotFound(Json.toJson(RestMessage(s"Product with id: $id not found")))
  }



  def indexChickenBreed: Action[AnyContent] = Action { implicit request =>
    Results.Ok( Json.toJson(ChickenBreedRepository.all()))
  }

  def getChickenBreed(id: Int): Action[AnyContent] = Action { implicit  request =>
    val tmp = ChickenBreedRepository.get(id)
    if(tmp.isDefined) Results.Ok( Json.toJson(tmp.get))
    else Results.NotFound(Json.toJson(RestMessage(s"Chicken breed with id: $id not found")))
  }

  def postChickenBreed: Action[AnyContent] = Action { implicit  request =>
    val tmp = ChickenBreedRepository.post(request.body)
    if(tmp.isDefined) Results.Created( Json.toJson(tmp.get))
    else Results.BadRequest(Json.toJson(RestMessage("Incorrect JSON")))
  }

  def putChickenBreed(id: Int): Action[AnyContent] = Action {implicit request =>
    val tmp = ChickenBreedRepository.put(id, request.body)
    if(tmp.isDefined) Results.Ok( Json.toJson(tmp.get))
    else Results.BadRequest(Json.toJson(RestMessage("Incorrect JSON")))
  }

  def deleteChickenBreed(id: Int):Action[AnyContent] = Action {implicit request =>
    val tmp = ChickenBreedRepository.delete(id)
    if(tmp)Results.NoContent
    else Results.NotFound(Json.toJson(RestMessage(s"Chicken breed with id: $id not found")))
  }


  def indexCategory: Action[AnyContent] = Action { implicit request =>
    Results.Ok(Json.toJson(CategoryRepository.all()))
  }

  def getCategory(id: Int): Action[AnyContent] = Action { implicit request =>
    val tmp = CategoryRepository.get(id)
    if(tmp.isDefined)Results.Ok(Json.toJson(tmp.get))
    else Results.NotFound(Json.toJson(RestMessage(s"Product category with id: $id not found")))
  }

  def putCategory(id: Int) : Action[AnyContent] = Action {implicit  request =>
    val tmp = CategoryRepository.put(id, request.body)
    if(tmp.isDefined)Results.Ok(Json.toJson(tmp.get))
    else Results.BadRequest(Json.toJson(RestMessage("Incorrect JSON")))
  }

  def postCategory: Action[AnyContent] = Action { implicit request =>
    val c = CategoryRepository.post(request.body)
    if(c.isDefined)Results.Created(Json.toJson(c))
    else Results.BadRequest(Json.toJson(RestMessage("Incorrect JSON")))
  }

  def deleteCategory(id: Int) : Action[AnyContent] = Action {implicit  request =>
    val tmp = CategoryRepository.delete(id)
    if(tmp)Results.NoContent
    else Results.NotFound(Json.toJson(RestMessage(s"Product category with id: $id not found")))
  }
}
