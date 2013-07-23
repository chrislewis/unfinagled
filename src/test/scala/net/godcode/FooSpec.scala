package net.godcode

import org.scalatest.GivenWhenThen
import org.scalatest.matchers.ShouldMatchers
import unfiltered.response._
import unfiltered.request._
import dispatch.classic.Handler


class FooSpec extends Served with GivenWhenThen with ShouldMatchers {

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