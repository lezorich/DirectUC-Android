package com.directuc.android.com.directuc.android.utils;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que envuelve las preferencias compartidas de DirectUC. Esta clase es final para prevenir que 
 * se creen subclases de la misma. Además, la idea es que esta clase tampoco sea instanciada, por eso
 * el constructo es privado.
 * @author lukas zorich
 *
 */
public final class Prefs {
	private static String PREF_NAME = "datos_usuario_uc";
	
	private static final String USUARIOUC_PREF = "usuariouc_pref";
	private static final String PASSWORD_PREF = "password_pref";
	
	private static final String USUARIOUC_DEFAULT = "usuariouc";
	private static final String PASSWORD_DEFAULT = "no_password";
	
	/**
	 * Constructor privado para impedir que esta clase sea instanciada.
	 */
	private Prefs() {
		throw new AssertionError();
	}
	
	/**
	 * Metodo privado que devuelve las preferencias de la aplicacion.
	 * @param context
	 * @return
	 */
	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(Prefs.PREF_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * Obtiene el usuario que está guardado actualmente en la aplicacion.
	 * @param context 
	 * @return usuario uc
	 */
	public static String getUsuarioUCPref(Context context) {
		return getPrefs(context).getString(USUARIOUC_PREF, Prefs.USUARIOUC_DEFAULT);
	}
	
	/**
	 * Obtiene la contraseña que está guardada actualmente en la aplicacion.
	 * @param context
	 * @return contraseña 
	 */
	public static String getPasswordPref(Context context) {
		return getPrefs(context).getString(Prefs.PASSWORD_PREF, Prefs.PASSWORD_DEFAULT);
	}
	
	/**
	 * Settea el usuario uc para acceder a los distintos servicios de la universidad.
	 * @param context
	 * @param nuevoUsuario nuevo usuario uc a guardar
	 */
	public static void setUsuarioUCPref(Context context, String nuevoUsuario) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(Prefs.USUARIOUC_PREF, nuevoUsuario);
		editor.commit();
	}
	
	/**
	 * Settea la contraseña para acceder a los distintos servicios de la universidad.
	 * @param context
	 * @param nuevaPassword nueva contraseña a guardar.
	 */
	public static void setPasswordPref(Context context, String nuevaPassword) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(Prefs.PASSWORD_PREF, nuevaPassword);
		editor.commit();
	}
	
	/**
	 * Verifica si existe o no un usuario actualmente en DirectUC.
	 * @param context
	 * @return <b>true</b> si existe un usuario y <b>false</b> en caso contrario.
	 */
	public static boolean existeUsuario(Context context) {
		return (getUsuarioUCPref(context) == Prefs.USUARIOUC_DEFAULT ? false : true);
	}
	
	public static void borrarUsuario(Context context) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(Prefs.USUARIOUC_PREF, Prefs.USUARIOUC_DEFAULT);
		editor.putString(PASSWORD_PREF, PASSWORD_DEFAULT);
		editor.commit();
	}

}
