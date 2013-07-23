package net.godcode.unfinagled

import org.scalatest.GivenWhenThen
import org.scalatest.matchers.ShouldMatchers
import unfiltered.response._
import unfiltered.request._
import dispatch.classic.Handler
import net.godcode.unfinagled.{UnfilteredCodec, UnfilteredService}

class FooSpec extends Served with GivenWhenThen with ShouldMatchers {

  override def codec = UnfilteredCodec(
    _compressionLevel = 1,
    _enableTracing = true
  )

  def setup = _.build(UnfilteredService {
    case GET(Path("/foobar")) => ResponseString("baz")
  })

  feature("foobar requests") {
    scenario("GET baz") {
      http(host / "foobar" as_str) should be("baz")
    }

    scenario("not support POST") {
      withHttp { _.x(Handler((host / "foobar").POST, status)) should be(404) }
    }
  }

}