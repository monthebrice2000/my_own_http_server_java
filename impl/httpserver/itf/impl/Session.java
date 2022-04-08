package httpserver.itf.impl;

import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpSession;

import java.io.PrintStream;
import java.util.Set;

public class Session implements HttpSession {
    protected HttpServer m_hs;
    protected HttpRicmletRequest m_req;

    public Session(HttpServer m_hs, HttpRicmletRequest m_req) {
        this.m_hs = m_hs;
        this.m_req = m_req;
    }

    @Override
    public String getId() {
        Set keys = this.m_req.cookiess.keySet();
        for( Object item : keys ){
            
        }

        return null;
    }

    @Override
    public Object getValue(String key) {
        return this.m_req.cookiess.get( key );
    }

    @Override
    public void setValue(String key, Object value) {
        this.m_req.cookiess.put( key, value );
    }
}
