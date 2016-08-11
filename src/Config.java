package com.bais.let;

import android.widget.Toast;


public class Config {

	/**
	 * 商户ID   1322 线上
	 */
	private static String partner          = "";
	/**
	 * 支付完后云OS支付后端用来通过商户支付结果的url
	 */
	private static String partnerNotifyUrl = "";
	//private static String partnerNotifyUrl = "http://paydemo.yundev.cn/index.php";
	/**
	 * 商户密钥字符串
	 */
	
	//private static String prikey      = "MIICXQIBAAKBgQCyiWG0LoDRe+kDF9jiHI832/WF5StlzWPyELerpH7AF5W3WecatWT3b3tkOTWtbdNxd4L8KMxNks/Wh9DoNM5jyAUUdfRWuwKN7VuSoAJ0oJjxdBqJSCr1eLaMaUkJntCyTJ+U0EWXi30KzVj6IScuk2H2r+KV1E9N619jE5EUHwIDAQABAoGBAJBVjE1UQwQX/npnrwitOgNYqZcDfCAi+afaaAI1S9sTtg+yeKkY+HUpBUIzFfgND6FtktE0UmZsR+YRiowPFMIwaH9SsoIshM4A/mrwhow4yr0EZ/jvZayvtyCF0+acyte2hT0+CQZ860sc3T2Ul3INrituCbJJ9CmwKws8tHCRAkEA1eCPdJjSTb+UoHNbrC+Td36a+KXCLIL9dZXbZ80fKV62ioWl4B0mNeiwcVByVy9bxgeqxCLAJkBStKyrWDQUiwJBANWy/f+KohHoBFZYsLg6/gAHFrdE+Y4tXZSJpbRRU3N5+4UKrPLQbuizOmucA9oQEQLoW03vtPXgbGv9Wl9bbT0CQEsPPElXOLdAfRCya4HKUJ6nOAv7YGHutrUFmrKbMkx6iOWkccptcHOK7iFWckpWvIaLOksLRxQljuEJmDikTnECQE7WlCJckOIuSLtaSQgOq3pzIwxF5BNx5R5RkdJuVm05yXMRtzB1eRVcWectTbZ7SutZsaWVE5vKSfWlKFwZrLkCQQCp4lkZ7XsW3XVlNOEb9EWNCAUXHVONmAnnIcLrBDh2zkqAU0BgfWnH+/Cqb/EvXdVALIkmhW7qolWq2ZVWkU34";	
	
	public static String getPartner() {
		return partner;
	}

	public static String getPartnerNotifyUrl(String url) {
		if(url.isEmpty() == false){
			partnerNotifyUrl = url;
		}
		
		//partnerNotifyUrl = url.isEmpty() ? url : partnerNotifyUrl;
		return partnerNotifyUrl;
	}

	public static String getPrikey() {
		return prikey;
	}
}
