package net.godcode

import java.lang.Iterable
import java.util.{List, Set}
import java.util.Map.Entry
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, HttpRequest}
import org.jboss.netty.buffer.ChannelBuffer
import unfiltered.netty.RequestBinding

/**
 * A request wrapper that captures an `unfiltered.netty.RequestBinding` being an instance of
 * `org.jboss.netty.handler.codec.http.HttpRequest` and delegating calls to the binding's underlying request.
 * This unfortunate code is merited in that it allows an unfiltered codec to function with netty channel
 * handlers expecting an `org.jboss.netty.handler.codec.http.HttpRequest`.
 */
class RequestAdapter private (val binding: RequestBinding) extends HttpRequest {
  val underlying = binding.underlying.request
  override def getHeader(name: String): String = underlying.getHeader(name)
  override def getHeaders(name: String): List[String] = underlying.getHeaders(name)
  override def getHeaders: List[Entry[String, String]] = underlying.getHeaders
  override def containsHeader(name: String): Boolean = underlying.containsHeader(name)
  override def getHeaderNames: Set[String] = underlying.getHeaderNames
  override def getProtocolVersion: HttpVersion = underlying.getProtocolVersion
  override def setProtocolVersion(version: HttpVersion) { underlying.setProtocolVersion(version) }
  override def getContent: ChannelBuffer = underlying.getContent
  override def setContent(content: ChannelBuffer) { underlying.setContent(content) }
  override def addHeader(name: String, value: Any) { underlying.addHeader(name, value) }
  override def setHeader(name: String, value: Any) { underlying.setHeader(name, value) }
  override def setHeader(name: String, values: Iterable[_]) { underlying.setHeader(name, values) }
  override def removeHeader(name: String) { underlying.removeHeader(name) }
  override def clearHeaders() { underlying.clearHeaders() }
  override def getContentLength: Long = underlying.getContentLength()
  override def getContentLength(defaultValue: Long): Long = underlying.getContentLength(defaultValue)
  override def isChunked: Boolean = underlying.isChunked
  override def setChunked(chunked: Boolean) { underlying.setChunked(chunked) }
  override def isKeepAlive: Boolean = underlying.isKeepAlive
  override def getMethod: HttpMethod = underlying.getMethod
  override def setMethod(method: HttpMethod) { underlying.setMethod(method) }
  override def getUri: String = underlying.getUri
  override def setUri(uri: String) { underlying.setUri(uri) }
}

object RequestAdapter {
  def apply(binding: RequestBinding) = new RequestAdapter(binding)
}