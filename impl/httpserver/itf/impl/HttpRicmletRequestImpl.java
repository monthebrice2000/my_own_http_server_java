package httpserver.itf.impl;
import examples.CountRicmlet;
import examples.HelloRicmlet;
import httpserver.itf.*;

import java.io.*;

/*
 * This class allows to build an object representing an HTTP static request
 */
public class HttpRicmletRequestImpl extends HttpRicmletRequest {
	static final String DEFAULT_FILE = "index.html";

	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
		super(hs, method, ressname, br);
	}

	public void process(HttpResponse resp) throws Exception {
	// TO COMPLETE

		System.out.println( getRessname() );
		String pathName = this.getRessname();
		/**
		 * Handle dynamic pages
		 */
		if (pathName.startsWith("/ricmlets/examples/HelloRicmlet")) {
			String clsName = "examples.HelloRicmlet";
			Class<?> c = Class.forName(clsName);
			HttpRicmlet helloRicmlet = (HelloRicmlet) c.getDeclaredConstructor().newInstance();
			helloRicmlet.doGet(this, (HttpRicmletResponse) resp);
			System.out.println("I was there sorry");
		} else if (pathName.startsWith("/ricmlets/examples/CountRicmlet")) {
			String clsName2 = "examples.CountRicmlet";
			Class<?> c2 = Class.forName(clsName2);
			HttpRicmlet countRicmlet = (CountRicmlet) c2.getDeclaredConstructor().newInstance();
			countRicmlet.doGet(this, (HttpRicmletResponse) resp);
			System.out.println("I was there sorry");
			/**
			 * Handle static pages
			 */
		} else {
			System.out.println("It's a static request");
			File folder = this.m_hs.getFolder();
			String fileToSend = null;
			File index = null;

			if (this.getRessname().contains("/")) {
				fileToSend = this.segmentPath(this.getRessname().split("/"));
			}

			if (fileToSend.equals("FILES") || fileToSend.equals("")) {
				index = new File("FILES", "index.html");
			} else if (folder.getName().equals("FILES")) {
				index = new File(folder, fileToSend);
			} else {
				index = new File("FILES", fileToSend);
			}

			if (index.exists() && index.isFile()) {
				resp.setReplyOk();
				resp.setContentLength((int) (index.length()));
				resp.setContentType(HttpRequest.getContentType(fileToSend.equals("FILES") ? "index.html" : this.getRessname()));
				PrintStream body = resp.beginBody();
				FileInputStream fis = new FileInputStream(index);
				byte[] data = new byte[(int) index.length()];
				fis.read(data);
				fis.close();
				body.write(data);
			} else {
				if (this.getRessname().contains("ricmlets")) {
					resp.setReplyError(404, "Ricmlet Not Found");
				} else {
					resp.setReplyError(404, "File Not Found");
				}
			}
		}
	}


	private String segmentPath( String[] path ){
		return path[ path.length -1 ];
	}

	@Override
	public HttpSession getSession() {
		return null;
	}

	@Override
	public String getArg(String name) {
		if( !this.getRessname().contains( name )){
			return "undefined";
		}
		int pos = this.getRessname().indexOf('?');
		String query = this.getRessname().substring( pos+1 );
		String paramName = query.split("&")[0];
		String paramSurName = query.split("&")[1];
		return name.equals("name") ? paramName.split("=")[1] : paramSurName.split("=")[1];
	}

	@Override
	public String getCookie(String name) {
		return null;
	}
}
