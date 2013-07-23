package net.godcode

import java.net.{SocketAddress, InetSocketAddress}
import org.jboss.netty.handler.codec.http.{HttpVersion, DefaultHttpResponse, HttpRequest, HttpResponse, HttpResponseStatus}
import org.jboss.netty.buffer.ChannelBuffers
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.Service
import com.twitter.util.Future
import net.godcode.codec.UnfilteredCodec
import unfiltered.request._
import unfiltered.response._

object Server extends App {

  def ufService =
    UnfilteredService {
      case GET(Path("/")) => ResponseString("Hi")
      case GET(Path("/foobar")) => ResponseString("GOT foobar!")
    }

  def stdService = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest): Future[HttpResponse] =
      Future {
        val r = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        r.setContent(ChannelBuffers.copiedBuffer("Hi", java.nio.charset.Charset.defaultCharset()))
        r
      }
  }

  // Serve our service on a port
  val address: SocketAddress = new InetSocketAddress(10000)

  val server: Server = ServerBuilder()
    .bindTo(address)
    .name("HttpServer")
    .codec(UnfilteredCodec(_compressionLevel = 1))
    .build(ufService)

}