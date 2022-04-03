package v1.kogutex

import net.logstash.logback.marker.LogstashMarker
import play.api.{Logger, MarkerContext}
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{ActionBuilder, AnyContent, BaseController, BodyParser, ControllerComponents, DefaultActionBuilder, MessagesRequestHeader, PlayBodyParsers, PreferredMessagesProvider, Request, RequestHeader, Result, WrappedRequest}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait KogutexRequestHeader
  extends MessagesRequestHeader
    with PreferredMessagesProvider

class KogutexRequest[A](request: Request[A], val messagesApi: MessagesApi)
  extends WrappedRequest(request)
    with KogutexRequestHeader

trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)) = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(
                                             implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker(
        "remoteAddress" -> request.remoteAddress)
    }
  }
}

class KogutexActionBuilder @Inject()(messagesApi: MessagesApi,
                                  playBodyParsers: PlayBodyParsers)(
                                   implicit val executionContext: ExecutionContext)
  extends ActionBuilder[KogutexRequest, AnyContent]
    with RequestMarkerContext
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type KogutexRequestBlock[A] = KogutexRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: KogutexRequestBlock[A]): Future[Result] = {
    // Convert to marker context and use request in block
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(
      request)
    logger.trace(s"invokeBlock: ")

    val future = block(new KogutexRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case other =>
          result
      }
    }
  }
}

case class KogutexControllerComponents @Inject()(
                                               KogutexActionBuilder: KogutexActionBuilder,
                                               actionBuilder: DefaultActionBuilder,
                                               parsers: PlayBodyParsers,
                                               messagesApi: MessagesApi,
                                               langs: Langs,
                                               fileMimeTypes: FileMimeTypes,
                                               executionContext: scala.concurrent.ExecutionContext)
  extends ControllerComponents


class KogutexBaseController @Inject()(pcc: KogutexControllerComponents)
  extends BaseController
    with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = pcc

  def KogutexAction: KogutexActionBuilder = pcc.KogutexActionBuilder

  //def KogutexResourceHandler: KogutexResourceHandler = Nil
}
