package com.directuc.android;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

/**
 * Created by lukas on 30-03-14.
 */
public class WebCursosWebViewClient extends DirectUCWebViewClient {

    private static final String URL_LOGOUT = "sso.uc.cl/cas/prelogout";

    public WebCursosWebViewClient(ActionBarActivity context) {
        super(context);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.equals(DirectUC.URL_WEBCURSOS_POST)) {
            view.loadUrl(DirectUC.URL_WEBCURSOS);
            view.clearHistory();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // si pone salir en el PORTAL, volvemos a la LoginActivity
        if (salirServicio(url, URL_LOGOUT)) {
            return true;
        } else {
            return false;
        }
    }
}
