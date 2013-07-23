package net.godcode.unfinagled

import org.scalatest.FeatureSpec
import unfiltered.scalatest.Hosted
import java.net.{InetSocketAddress, SocketAddress}
import com.twitter.finagle.builder.{ServerConfig, ServerBuilder, Server => FinagleServer}
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest}
import dispatch.classic.{Handler, Http}
import unfiltered.netty.cycle.Plan.Intent

trait Served extends FeatureSpec with Hosted {

  val address: SocketAddress = new InetSocketAddress(port)

  def intent: Intent

  def getServer: FinagleServer =
      ServerBuilder()
        .bindTo(address)
        .name("TestHttpServer@" + port)
        .codec(UnfilteredCodec())
        .build(UnfilteredService(intent))

  //def setup: (ServerBuilder[HttpRequest, HttpResponse, ServerConfig.Yes, ServerConfig.Yes, ServerConfig.Yes] => FinagleServer)

  val status: Handler.F[Int] = { case (c, _, _) => c }

  def withHttp[T](req: Http => T): T = {
    val h = new Http
    try { req(h) }
    finally { h.shutdown() }
  }

  override protected def withFixture(test: NoArgTest) {
    val server = getServer
    try {
      test() // Invoke the test function
    } finally {
      server.close()
    }
  }
}
