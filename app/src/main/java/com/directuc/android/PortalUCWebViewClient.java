package com.directuc.android;

import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

/**
 * Created by lukas on 30-03-14.
 */
public class PortalUCWebViewClient extends DirectUCWebViewClient {

    private static final String URL_LOGOUT = "logout?url=https://portal.uc.cl";
    private static final String URL_DOMINIO = "https://portal.uc.cl/web/home-community";

    public PortalUCWebViewClient(ActionBarActivity context) {
        super(context);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.equals(DirectUC.URL_PORTALUC_LOGIN)) {
            view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (salirServicio(url, URL_LOGOUT)) {
            return true;
        } /*else if  (urlFueraDominio(url, URL_DOMINIO)) {
            return true;
        } */else {
            return false;
        }
    }




}
