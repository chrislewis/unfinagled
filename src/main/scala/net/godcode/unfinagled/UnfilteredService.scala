package net.godcode.unfinagled

import com.twitter.finagle.Service
import com.twitter.util.{FuturePool, Future}
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import unfiltered.response.{Connection, ContentLength, NotFound, Responder, HttpResponse => UHttpResponse}
import unfiltered.netty._

/**
 * Functions for constructing finagle Services from Unfiltered types.
 */
object UnfilteredService {

  val nettyResponse: HttpResponseStatus => HttpResponse =
    new DefaultHttpResponse(HTTP_1_1, _)

  /**
   * Creates a Finagle wrapper service around a synchronous Unfiltered intent.
   *
   * @param intent the intent to wrap
   * @param futurePool the pool on which the intent will be applied. The default assumes the intent is non-blocking.
   * @return the Finagle service wrapper
   */
  def apply(intent: cycle.Plan.Intent, futurePool: FuturePool = FuturePool.immediatePool) : Service[HttpRequest, HttpResponse] =
    new Service[HttpRequest, HttpResponse] {
      def apply(request: HttpRequest): Future[HttpResponse] =
        request match {
          case uf: RequestAdapter =>
            futurePool {
              val rf = intent.lift(uf.binding).getOrElse(NotFound)
              val nres = new ResponseBinding(nettyResponse(OK))
              val keepAlive = HttpHeaders.isKeepAlive(request)
              val closer = new Responder[HttpResponse] {
                def respond(res: UHttpResponse[HttpResponse]) {
                  res.outputStream.close()
                  (if (keepAlive)
                     Connection("Keep-Alive") ~>
                       ContentLength(
                         res.underlying.getContent().readableBytes().toString)
                   else unfiltered.response.Connection("close")
                  )(res)
                }
              }
              (rf ~> closer)(nres).underlying
            }
          case _ =>
            // TODO should probably log this as it indicates a problem with handler ordering in the pipeline.
            Future(nettyResponse(INTERNAL_SERVER_ERROR))
        }
    }

}