package com.directuc.android.com.directuc.android.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.directuc.android.DirectUC;
import com.directuc.android.PortalUCServiceI;
import com.directuc.android.PortalUCWebViewClient;
import com.directuc.android.com.directuc.android.utils.PortalUCDownloader;
import com.directuc.android.com.directuc.android.utils.Prefs;
import com.directuc.android.R;
import com.directuc.android.WebCursosWebViewClient;
import com.directuc.android.com.directuc.android.utils.WebCursosDownloader;

import org.apache.http.cookie.Cookie;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressLint("setJavaScriptEnabled")
public class WebActivity extends ActionBarActivity implements PortalUCServiceI {
	
	protected WebView mWebView;
	protected ProgressBar mProgressBar;
	private DirectUC.SERVICIOS mServicio;
	
	// Exclusivo portal uc
	private PortalUCDataFormWrapper mPortalUCDataForm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_web);

        mServicio = (DirectUC.SERVICIOS)getIntent().getExtras().getSerializable(DirectUC.EXTRA_SERVICIO);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getServiceName(mServicio));
        actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
        //setSupportProgressBarIndeterminate(true);
        //setSupportProgressBarIndeterminateVisibility(false);

		mProgressBar = (ProgressBar)findViewById(R.id.progressBar_webView);
		mWebView = (WebView)findViewById(R.id.webView_ing);
		webViewConfig();
		
		if (mServicio == DirectUC.SERVICIOS.PORTALUC) {
			mWebView.loadUrl(getServicePostUrl(mServicio));
		} else {
			mWebView.postUrl(getServicePostUrl(mServicio), EncodingUtils.getBytes(getServicePostString(mServicio), "BASE64"));
		}
	}

    private String getServiceName(DirectUC.SERVICIOS servicio) {
        switch(servicio) {
            case PORTALUC:
                return getResources().getString(R.string.boton_portaluc);
            case WEBCURSOS:
                return getResources().getString(R.string.boton_webcursos);
            default:
                return getResources().getString(R.string.app_name);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == android.R.id.home) {
            if (getParent() != null) {
                NavUtils.navigateUpFromSameTask(this);
            } else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                finish();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
	}

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
	
	@SuppressLint("NewApi")
	public void webViewConfig() {
		mWebView.getSettings().setBuiltInZoomControls(true);
		
		// Para HoneyComb o mayor, sacamos los controles del zoom
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.getSettings().setDisplayZoomControls(false);
		}
		
		mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				mProgressBar.setProgress(progress);
			}
		});
		
		// para poder ver el html para capturar los datos del portal uc
		if (mServicio == DirectUC.SERVICIOS.PORTALUC) {
			mWebView.addJavascriptInterface(new JavascriptI(this), "HTMLOUT");
            mWebView.setWebViewClient(new PortalUCWebViewClient(this));
            mWebView.setDownloadListener(new PortalUCDownloader(this));
		} else if (mServicio == DirectUC.SERVICIOS.WEBCURSOS) {
            mWebView.setWebViewClient(new WebCursosWebViewClient(this));
            mWebView.setDownloadListener(new WebCursosDownloader(this));
        }

		/*mWebView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {



                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setType(mimetype);
                startActivity(intent);
				
			}
		});*/
		
	}
	
	private String getServiceUrl(DirectUC.SERVICIOS servicio) {
		switch(servicio) {
		case PORTALUC:
			return DirectUC.URL_PORTALUC;
		case WEBCURSOS:
			return DirectUC.URL_WEBCURSOS;
		}
		
		return "";
	}
	
	private String getServicePostUrl(DirectUC.SERVICIOS servicio) {
		switch(servicio) {
		case PORTALUC:
			return DirectUC.URL_PORTALUC_LOGIN;
		case WEBCURSOS:
			return DirectUC.URL_WEBCURSOS_POST;
		}
		
		return "";
	}
	
	private String getServicePostString(DirectUC.SERVICIOS servicio) {
		String result = "";
		
		String usuariouc = Prefs.getUsuarioUCPref(this);
		String password = Prefs.getPasswordPref(this);
		
		switch (servicio) {
		case PORTALUC:
			result = getPortalUCPostString(usuariouc, password);
			break;
		case WEBCURSOS:
			result =  getWebCursosPostString(usuariouc, password);
			break;			
		}
		
		return result;
	}
	
	private String getPortalUCPostString(String usuariouc, String password) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(DirectUC.PORTALUC_USER_ID)
				.append("=")
				.append(usuariouc)
				.append("&")
				.append(DirectUC.PORTALUC_PASSWORD_ID)
				.append("=")
				.append(password);
		
		String lt = mPortalUCDataForm.getLt();
		String execution = mPortalUCDataForm.getExecution();
		
		// agregamos las variables al string que estamos construyendo
		sBuilder.append("&")
				.append(DirectUC.PORTALUC_LT_ID)
				.append("=")
				.append(lt)
				.append("&")
				.append(DirectUC.PORTALUC_EXECUTION_ID)
				.append("=")
				.append(execution)
				.append("&")
				.append(DirectUC.PORTALUC_EVENT_ID)
				.append("=")
				.append(DirectUC.PORTALUC_EVENT_VALUE)
				.append("&")
				.append(DirectUC.PORTALUC_SUBMIT_ID)
				.append("=")
				.append(DirectUC.PORTALUC_SUBMIT_VALUE);
		
		return sBuilder.toString();
	}
	
	private String getWebCursosPostString(String usuariouc, String password) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(DirectUC.USERID_WEBCURSOS)
				.append("=")
				.append(usuariouc)
				.append("&")
				.append(DirectUC.PASSWORDID_WEBCURSOS)
				.append("=")
				.append(password);
		
		return sBuilder.toString();
	}
	
	private String getCookieString(String urlServicio, Cookie cookie) {
		if (urlServicio.equals(DirectUC.URL_PORTALUC)) {
			return cookie.getName() + "=" + cookie.getValue() + "; domain=portal.uc.cl; COOKIE_SUPPORT=true";
		} else if (urlServicio.equals(DirectUC.URL_WEBCURSOS)) {
			return cookie.getName() + "=" + cookie.getValue() + ";";
		} else {
			return "";
		}
	}
	
	private class JavascriptI {
		
		private PortalUCServiceI mPortalUCServiceI;
		
		public JavascriptI(PortalUCServiceI portalUCServiceI) {
			mPortalUCServiceI = portalUCServiceI;
		}
		
		@JavascriptInterface
		public void processHTML(String html) {
			Document doc = Jsoup.parse(html);
			Elements inputElementsLt = doc.select("input[name=lt]");
			Elements inputElementsExecution = doc.select("input[name=execution]");
			
			String lt = "", execution = "";
			
			for (Element el : inputElementsLt) { lt = el.val(); }
			for (Element el : inputElementsExecution) { execution = el.val(); }
			
			final PortalUCDataFormWrapper wrapper = new PortalUCDataFormWrapper(lt, execution);
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mPortalUCServiceI.setPortalUCFormData(wrapper.getLt(), wrapper.getExecution());
					
				}
			});
		}
	}
	
	private class PortalUCDataFormWrapper {
		
		private String mLt, mExecution;
		
		public PortalUCDataFormWrapper(String lt, String execution) {
			mLt = lt;
			mExecution = execution;
		}
		
		public String getLt() {
			return mLt;
		}
		
		public String getExecution() {
			return mExecution;
		}
	}

	@Override
	public void setPortalUCFormData(String lt, String execution) {
		mPortalUCDataForm = new PortalUCDataFormWrapper(lt, execution);
		
		mWebView.postUrl(getServicePostUrl(mServicio), EncodingUtils.getBytes(getServicePostString(mServicio), "BASE64"));
        mWebView.clearHistory();
	}

}
