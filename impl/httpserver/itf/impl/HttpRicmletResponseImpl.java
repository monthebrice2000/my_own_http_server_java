package httpserver.itf.impl;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpRicmletResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/*
 * This class allows to build an object representing an HTTP response
 */
class HttpRicmletResponseImpl implements HttpRicmletResponse {
	protected HttpServer m_hs;
	protected PrintStream m_ps;
	protected HttpRicmletRequest m_req;

	protected HttpRicmletResponseImpl(HttpServer hs, HttpRicmletRequest req, PrintStream ps) {
		m_hs = hs;
		m_req = req;
		m_ps = ps;
	}

	public void setReplyOk() {
		m_ps.println("HTTP/1.0 200 OK");
		m_ps.println("Date: " + new Date());
		m_ps.println("Server: ricm-http 1.0");
	}

	public void sendCookie(){
		m_ps.println("Set-Cookie: "+ "MyFirstCookie" + "=" + this.m_req.getCookie("MyFirstCookie") );
	}

	public void setReplyError(int codeRet, String msg) throws IOException {
		m_ps.println("HTTP/1.0 "+codeRet+" "+msg);
		m_ps.println("Date: " + new Date());
		m_ps.println("Server: ricm-http 1.0");
		m_ps.println("Content-type: text/html");
		m_ps.println(); 
		m_ps.println("<HTML><HEAD><TITLE>"+msg+"</TITLE></HEAD>");
		m_ps.println("<BODY><H4>HTTP Error "+codeRet+": "+msg+"</H4></BODY></HTML>");
		m_ps.flush();
	}
	
	public void setContentLength(int length) {
		m_ps.println("Content-length: " + length);
	}

	public void setContentType(String type) {
		m_ps.println("Content-type: " + type);
	}

	/*
	 * Insert an empty line to the response
	 * and return the printstream to send the reply
	 * @see httpserver.itf.HttpResponse#beginBody()
	 */
	public PrintStream beginBody() {
		m_ps.println(); 
		return m_ps;
	}

	@Override
	public void setCookie(String name, String value) {
		this.m_req.cookiess.put(name, value);
	}
}
