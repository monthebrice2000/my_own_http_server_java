package httpserver.itf.impl;

import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;

public class UnknownRicmletRequest extends HttpRicmletRequest {
	public UnknownRicmletRequest(HttpServer httpserver, String method, String ressname, BufferedReader br) throws IOException {
		super(httpserver, method, ressname, br);
	}

	@Override
	public void process(HttpResponse resp) throws IOException {
		resp.setReplyError(501, "Unknown Method");
	}

	@Override
	public HttpSession getSession() {
		return null;
	}

	@Override
	public String getArg(String name) {
		return null;
	}

	@Override
	public String getCookie(String name) {
		return null;
	}
}
