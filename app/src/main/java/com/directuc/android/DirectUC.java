package com.directuc.android;

public class DirectUC {
	
	public static String ROBOTO_FONT_PATH = "fonts/Roboto-Regular.ttf";
	public enum SERVICIOS { PORTALUC, WEBCURSOS };
	
	/** --- SERVICIOS --- **/
	
	// -- Portal UC --
	public static final String URL_PORTALUC = "https://portal.uc.cl/web/home-community/inicio";
	public static final String URL_PORTALUC_LOGIN = "https://sso.uc.cl/cas/login?service=https%3A%2F%2Fportal.uc.cl%2Fc%2Fportal%2Flogin";
	public static final String PORTALUC_USER_ID = "username";
	public static final String PORTALUC_PASSWORD_ID = "password";
	public static final String PORTALUC_LT_ID = "lt";
	public static final String PORTALUC_EXECUTION_ID = "execution";
	public static final String PORTALUC_EVENT_ID = "_eventId";
	public static final String PORTALUC_SUBMIT_ID = "submit";
	public static final String PORTALUC_EVENT_VALUE = "submit";
	public static final String PORTALUC_SUBMIT_VALUE = "Iniciar Sesión";
	
	// -- WebCursos --
	public static final String URL_WEBCURSOS_POST = "http://webcurso.uc.cl/direct/session";
	public static final String URL_WEBCURSOS = "http://webcurso.uc.cl/portal";
	public static final String USERID_WEBCURSOS = "_username";
	public static final String PASSWORDID_WEBCURSOS = "_password";
	
	public static final String EXTRA_SERVICIO = "SERVICIO";
	
	private DirectUC() {
																
	}

}
