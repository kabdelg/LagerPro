package controller;

public class Login {
		
	private static String UserStr = "Wevo";
	
	private static String passStr = "start";
		
	public static boolean LoginCheck(String user, String pass) {
		if (user.equals(UserStr) && pass.equals(passStr)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static String adminStr = "admin";
	
	private static String adminPassStr = "123";
	
	public static boolean AdminLoginCheck(String admin, String adminPass) {
		if (admin.equals(admin) && adminPass.equals(adminPassStr)) {
			System.out.println(adminPassStr);
			return true;
		} else {
			return false;
		}
	}
}