package net.godcode.unfinagled

import java.net.{SocketAddress, InetSocketAddress}
import com.twitter.finagle.builder.{Server, ServerBuilder}
import unfiltered.request._
import unfiltered.response._

object ExampleServer extends App {

  def ufService =
    UnfilteredService {
      case GET(Path("/")) => ResponseString("Hi")
      case GET(Path("/foobar")) => ResponseString("GOT foobar!")
    }

  // Serve our service on a port
  val address: SocketAddress = new InetSocketAddress(10000)

  val server: Server =
    ServerBuilder()
      .bindTo(address)
      .name("HttpServer")
      .codec(UnfilteredCodec(_compressionLevel = 1))
      .build(ufService)

}
