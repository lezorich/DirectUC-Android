package com.directuc.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.directuc.android.com.directuc.android.views.LoginActivity;

public class DirectUCWebViewClient extends WebViewClient {
	
	protected ActionBarActivity mContext;
	
	public DirectUCWebViewClient(ActionBarActivity context) {
		super();
		
		mContext = context;
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		handler.proceed();
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		
		mContext.setSupportProgressBarIndeterminateVisibility(true);
		View progressBarView = mContext.findViewById(R.id.progressBar_webView);
		progressBarView.clearAnimation();
		progressBarView.setVisibility(View.VISIBLE);
		
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		
		mContext.setSupportProgressBarIndeterminateVisibility(false);
		Animation animation = AnimationUtils.loadAnimation((Activity)view.getContext(), R.anim.progressbar_alpha_anim);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				View progressBarView = mContext.findViewById(R.id.progressBar_webView);
				progressBarView.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) { }

			@Override
			public void onAnimationStart(Animation animation) { }
		});
		animation.reset();
		View progressBarView = mContext.findViewById(R.id.progressBar_webView);

		if (progressBarView != null ) {
			progressBarView.clearAnimation();
			progressBarView.startAnimation(animation);
		}
	}

    /**
     * Revisa si es que la url está fuera del dominio del servicio
     * @param url url actual del webview
     * @param urlLogout url del dominio del servicio
     * @return <b>true</b> si se sale del servicio, <b>false</b> en caso contrario.
     */
    protected boolean salirServicio(String url, String urlLogout) {
        if (url.endsWith(urlLogout)) {
            if (mContext.getParent() != null) {
                NavUtils.navigateUpFromSameTask(mContext);
            } else {
                Intent i = new Intent(mContext.getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(i);

                mContext.finish();
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Revisa si la url está fuera del dominio del servicio. Si está fuera, entonces abrimos la url en el browser
     * del dispositivo.
     *
     * @param url url actual del webview
     * @param urlDominio dominio del servicio
     * @return <b>true</b> si la url está fuera del servicio, <b>false</b> en caso contrario.
     */
    protected boolean urlFueraDominio(String url, String urlDominio) {
        if (!url.startsWith(urlDominio)) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);

            return true;
        } else {
            return false;
        }
    }

}
