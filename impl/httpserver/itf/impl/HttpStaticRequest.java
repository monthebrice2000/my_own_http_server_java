package httpserver.itf.impl;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;


import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

/*
 * This class allows to build an object representing an HTTP static request
 */
public class HttpStaticRequest extends HttpRequest {
	static final String DEFAULT_FILE = "index.html";
	
	public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}
	
	public void process(HttpResponse resp) throws Exception {
	// TO COMPLETE
		File folder = this.m_hs.getFolder();
		String fileToSend = null;
		File index = null;

		if( this.getRessname().contains("/") ){
			fileToSend = this.segmentPath( this.getRessname().split("/") );
		}

		if( fileToSend.equals("FILES") ){
			index = new File( "FILES", "index.html" );
		}else if( folder.getName().equals("FILES")  ){
			index = new File( folder, fileToSend );
		}else{
			index = new File( "FILES", fileToSend );
		}

		if( index.exists() && index.isFile() ){
			resp.setReplyOk();
			resp.setContentLength( (int) ( index.length() ) );
			resp.setContentType( HttpRequest.getContentType( fileToSend.equals("FILES") ? "index.html" : this.getRessname() ) );
			PrintStream body = resp.beginBody();
			FileInputStream fis = new FileInputStream( index );
			byte[] data = new byte[ (int) index.length() ];
			fis.read( data );
			fis.close();
			body.write( data );
		}else{
			resp.setReplyError( 404, "File Not Found");
		}
	}


	private String segmentPath( String[] path ){
		return path[ path.length -1 ];
	}

}
