package httpserver.itf.impl;
import examples.CountRicmlet;
import examples.HelloRicmlet;
import examples.MyFirstCookieRicmlet;
import httpserver.itf.*;

import java.io.*;
import java.util.HashMap;

/*
 * This class allows to build an object representing an HTTP static request
 */
public class HttpRicmletRequestImpl extends HttpRicmletRequest {
	static final String DEFAULT_FILE = "index.html";
	static HashMap<String, HttpRicmlet> servlets = new HashMap<>(2);

	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
		super(hs, method, ressname, br);
		this.cookiess = new HashMap<>(10);
		this.initCookies( this.br );
	}

	public void process(HttpResponse resp) throws Exception {
	// TO COMPLETE

		System.out.println( getRessname() );
		String pathName = this.getRessname();
		/**
		 * Handle dynamic pages
		 */
		if (pathName.startsWith("/ricmlets/examples/HelloRicmlet")) {
            HttpRicmlet helloRicmlet = null;
            if( this.servlets.get("HelloRicmlet") == null ){
                String clsName = "examples.HelloRicmlet";
                Class<?> c = Class.forName(clsName);
                helloRicmlet = (HelloRicmlet) c.getDeclaredConstructor().newInstance();
                this.servlets.put("HelloRicmlet", helloRicmlet );
            }else{
                helloRicmlet = this.servlets.get("helloRicmlet");
            }
            helloRicmlet.doGet(this, (HttpRicmletResponse) resp);
		} else if (pathName.startsWith("/ricmlets/examples/CountRicmlet")) {
            HttpRicmlet countRicmlet = null;
		    if( this.servlets.get("CountRicmlet") == null ){
                String clsName2 = "examples.CountRicmlet";
                Class<?> c2 = Class.forName(clsName2);
                countRicmlet = (CountRicmlet) c2.getDeclaredConstructor().newInstance();
                this.servlets.put("CountRicmlet", countRicmlet );
            }else{
		        countRicmlet = this.servlets.get("CountRicmlet");
            }
			countRicmlet.doGet(this, (HttpRicmletResponse) resp);
			/**
			 * Handle static pages
			 */
		} else if (pathName.startsWith("/ricmlets/examples/MyFirstCookieRicmlet")) {
            HttpRicmlet myFirstCookieRicmlet = null;
            if( this.servlets.get("MyFirstCookieRicmlet") == null ){
                String clsName3 = "examples.MyFirstCookieRicmlet";
                Class<?> c3 = Class.forName(clsName3);
                myFirstCookieRicmlet = (MyFirstCookieRicmlet) c3.getDeclaredConstructor().newInstance();
                this.servlets.put("MyFirstCookieRicmlet", myFirstCookieRicmlet );
            }else{
                myFirstCookieRicmlet = this.servlets.get("MyFirstCookieRicmlet");
            }
            myFirstCookieRicmlet.doGet(this, (HttpRicmletResponse) resp);
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
		/*if( this.cookiess.get("name") == null ){
			this.cookiess.put(name, name);
		}*/
		return (String) this.cookiess.get(name);
	}

	private void initCookies(BufferedReader br ) throws IOException {
		String line = null;
		while( (line = br.readLine() ) != null ){
			if( line.toLowerCase().contains("cookie") )
				break;
		}

		if( line != null ){
			String[] keys = line.split("Cookie: ")[1].split(";");

			for( String item : keys ){
				String key = item.split("=")[0];
				String value = item.split("=")[1];
				this.cookiess.put(key, value);
			}
		}
	}
}
