package com.directuc.android.com.directuc.android.views;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.directuc.android.DirectUC;
import com.directuc.android.com.directuc.android.utils.Prefs;
import com.directuc.android.R;

public class LoginActivity extends ActionBarActivity {
	
	private ViewFlipper mViewFlipper;

    private static final int ANIMATION_DURATION = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// escondemos la actionbar
		getSupportActionBar().hide();
		
		// cambiamos la font del titulo
		Typeface robotoTypeFace = Typeface.createFromAsset(getAssets(), DirectUC.ROBOTO_FONT_PATH);
		TextView textViewTitle = (TextView)findViewById(R.id.LoginActivity_textView_title);
		textViewTitle.setTypeface(robotoTypeFace);
				
		// configuramos los botones de ingreso y los de next y prev
        configurarBotonesServicio();
        configurarBotonesNextPrev();
		
		mViewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper_login);
		//mViewFlipper.setOnTouchListener(new TouchListenerViewFlipper());
		
		usuarioGuardado();
	}
	
	private void usuarioGuardado() {
		if (Prefs.existeUsuario(this)) {
			EditText editTextUsuario = (EditText)findViewById(R.id.editText_loginActivity_usuario_uc);
			EditText editTextPassword = (EditText)findViewById(R.id.editText_loginActivity_passwrod);
			((CheckBox)findViewById(R.id.checkBox_Recordar)).setChecked(true);
			
			editTextUsuario.setText(Prefs.getUsuarioUCPref(this));
			editTextPassword.setText(Prefs.getPasswordPref(this));
		}
	}

    private void configurarBotonesNextPrev() {
        ImageButton nextBtn = (ImageButton)findViewById(R.id.imageButton_next);
        ImageButton prevBtn = (ImageButton)findViewById(R.id.imageButton_prev);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewFlipper.setInAnimation(inFromLeftAnimation());
                mViewFlipper.setOutAnimation(outToRightAnimation());
                mViewFlipper.showNext();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewFlipper.setInAnimation(inFromRightAnimation());
                mViewFlipper.setOutAnimation(outToLeftAnimation());
                mViewFlipper.showPrevious();
            }
        });
    }

	private void configurarBotonesServicio() {
		Button portalucbtn = (Button)findViewById(R.id.button_portaluc);
		Button webcursosbtn = (Button)findViewById(R.id.button_webcursos);
		
		portalucbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBoxRecordar(v);

                abrirServicio(DirectUC.SERVICIOS.PORTALUC);
            }
        });

        webcursosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBoxRecordar(v);

                abrirServicio(DirectUC.SERVICIOS.WEBCURSOS);
            }
        });
	}

    private void abrirServicio(DirectUC.SERVICIOS servicio) {
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra(DirectUC.EXTRA_SERVICIO, servicio);
        startActivity(intent);
    }

    private void checkCheckBoxRecordar(View v) {
        CheckBox checkBoxRecordar = (CheckBox)findViewById(R.id.checkBox_Recordar);
        if (checkBoxRecordar.isChecked()) {
            guardarDatosUsuario();
        } else {
            Prefs.borrarUsuario(v.getContext());
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void guardarDatosUsuario() {
		String usuarioUC = ((EditText)findViewById(R.id.editText_loginActivity_usuario_uc)).getText().toString();
		String password = ((EditText)findViewById(R.id.editText_loginActivity_passwrod)).getText().toString();
		
		Prefs.setUsuarioUCPref(getApplicationContext(), usuarioUC);
		Prefs.setPasswordPref(getApplicationContext(), password);
	}

	
	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 
														+1.0f,
														Animation.RELATIVE_TO_PARENT,
														0.0f,
														Animation.RELATIVE_TO_PARENT,
														0.0f,
														Animation.RELATIVE_TO_PARENT,
														0.0f);
		
		inFromRight.setDuration(ANIMATION_DURATION);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		
		return inFromRight;
	}
	
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(ANIMATION_DURATION);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
	}
	
	private Animation outToRightAnimation() {
	        Animation outtoRight = new TranslateAnimation(
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, +1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoRight.setDuration(ANIMATION_DURATION);
	        outtoRight.setInterpolator(new AccelerateInterpolator());
	        return outtoRight;
	    }
	
    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(ANIMATION_DURATION);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

}
