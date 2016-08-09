package com.bais.let.pay.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.letv.tvos.paysdk.LetvOnPayListener;
import com.letv.tvos.paysdk.LetvPay;
import com.letv.tvos.paysdk.appmodule.pay.model.LetvOrder;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends CordovaPlugin {
    	
    	  protected static final String LOG_TAG = "letOSOrder__pay";
		  private static final int RESULT_OK = 0;
		  private String username = null;
	      private HashMap<String, Object> mOrder;
	      CallbackContext callbackContext;

	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{
    		      			
    		if(action.equals("Pay")){
				final JSONObject options = args.getJSONObject(0);
				//username = LetvPay.getUniqueId(cordova.getActivity()); // 获取用户环境唯一标识,无线MAC地址
				//Toast.makeText(cordova.getActivity(), username, Toast.LENGTH_LONG).show();

				String partner_order_no = null;
				//String subject_id = null;
				String subject = null;
				String price = null;
				String partner_notify_url = null;

				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
				try {
					//subject_id = options.getString("subject_id");
					subject = options.getString("subject");
					price = options.getString("price");
					//partner_notify_url = options.getString("partner_notify_url");
					partner_order_no = options.getString("partner_order_no");
				} catch (Exception e) {
			           /* PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getLocalizedMessage());
						result.setKeepCallback(false); // release status callback in JS side
						callbackContext.sendPluginResult(result);*/
				}


				if(partner_order_no.equals("")){
					partner_order_no = sdf.format(dt);
				}
				Toast.makeText(cordova.getActivity(), partner_order_no+subject+price, Toast.LENGTH_LONG).show();
				Map<String, String> customMap = new HashMap<String, String>();
				customMap.put("customMessage", "");
				//doPay(username, partner_order_no, subject, price, 1,"http://kyytv.ebais.com.cn/let_productimg.png", "",	customMap);

    	       return true;
    		}else if(action.equals("Iandroid")){
    			boolean tr = args.getBoolean(0);
    			if(tr){
    			  String androidId = Secure.getString(cordova.getActivity().getContentResolver(), Secure.ANDROID_ID);
    			  Resultecho(true, androidId, callbackContext);
    			}
    		  return true;
    		}else if(action.equals("Packageinfo")){
    			int no = args.getInt(0);
    			String packageinfo="";
    			if(no==1){
    				//packageName
    				packageinfo = cordova.getActivity().getPackageName();				    				
    			}else if(no==2){
    				//strVersionCode
    		        try {
    		            PackageInfo packageInfo = cordova.getActivity().getPackageManager().getPackageInfo(cordova.getActivity().getPackageName(),0);
    		            packageinfo = String.valueOf(packageInfo.versionCode);
    		        } catch (NameNotFoundException e) {
    		            // TODO Auto-generated catch block
    		            e.printStackTrace();
    		        }  				
    			}else if(no==3){
    		        try {
    		        	PackageInfo packageInfo = cordova.getActivity().getPackageManager().getPackageInfo(cordova.getActivity().getPackageName(),0);
    		        	packageinfo = packageInfo.versionName;
    		        } catch (NameNotFoundException e) {
    		            // TODO Auto-generated catch block
    		            e.printStackTrace();
    		            packageinfo = "Cannot load Version!";
    		        }  	  				
    			}	
    			Resultecho(true, packageinfo, callbackContext);
    			return true;
    		}else if(action.equals("Echo")){
    			String context = args.getString(0);
    			int duration = args.getInt(1);

    			Toast.makeText(cordova.getActivity(), context, Toast.LENGTH_SHORT).show();

    			return true;
    		}else if(action.equals("Sign")){

    			/*String mag = RSASign.sign(args.getString(0), Config.getPrikey(), "utf-8" );
    			Resultecho(true, mag, callbackContext);*/

    			String source = args.getString(0);

    			final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyiWG0LoDRe+kDF9jiHI832/WF5StlzWPyELerpH7AF5W3WecatWT3b3tkOTWtbdNxd4L8KMxNks/Wh9DoNM5jyAUUdfRWuwKN7VuSoAJ0oJjxdBqJSCr1eLaMaUkJntCyTJ+U0EWXi30KzVj6IScuk2H2r+KV1E9N619jE5EUHwIDAQAB";
    			final String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALKJYbQugNF76QMX2OIcjzfb9YXlK2XNY/IQt6ukfsAXlbdZ5xq1ZPdve2Q5Na1t03F3gvwozE2Sz9aH0Og0zmPIBRR19Fa7Ao3tW5KgAnSgmPF0GolIKvV4toxpSQme0LJMn5TQRZeLfQrNWPohJy6TYfav4pXUT03rX2MTkRQfAgMBAAECgYEAkFWMTVRDBBf+emevCK06A1iplwN8ICL5p9poAjVL2xO2D7J4qRj4dSkFQjMV+A0PoW2S0TRSZmxH5hGKjA8UwjBof1KygiyEzgD+avCGjDjKvQRn+O9lrK+3IIXT5pzK17aFPT4JBnzrSxzdPZSXcg2uK24Jskn0KbArCzy0cJECQQDV4I90mNJNv5Sgc1usL5N3fpr4pcIsgv11ldtnzR8pXraKhaXgHSY16LBxUHJXL1vGB6rEIsAmQFK0rKtYNBSLAkEA1bL9/4qiEegEVliwuDr+AAcWt0T5ji1dlImltFFTc3n7hQqs8tBu6LM6a5wD2hARAuhbTe+09eBsa/1aX1ttPQJASw88SVc4t0B9ELJrgcpQnqc4C/tgYe62tQWaspsyTHqI5aRxym1wc4ruIVZySla8hos6SwtHFCWO4QmYOKROcQJATtaUIlyQ4i5Iu1pJCA6renMjDEXkE3HlHlGR0m5WbTnJcxG3MHV5FVxZ5y1NtntK61mxpZUTm8pJ9aUoXBmsuQJBAKniWRntexbddWU04Rv0RY0IBRcdU42YCechwusEOHbOSoBTQGB9acf78Kpv8S9d1UAsiSaFbuqiVarZlVaRTfg=";

    			try {

					PublicKey publicKey = RSAUtils.loadPublicKey(publicKeyStr);
	                byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
	                String afterencrypt = Base64Utils.encode(encryptByte);

	                /* PrivateKey privateKey = RSAUtils.loadPrivateKey(privateKeyStr);
	               			 // byte[] dstr =  Base64Utils.decode(den);
	               			 byte[] context = RSAUtils.decryptData(den.getBytes(), privateKey);*/

	                Resultecho(true, afterencrypt, callbackContext);
	               // Toast.makeText(cordova.getActivity(), afterencrypt, Toast.LENGTH_LONG).show();
	               // Log.i("RSAUtils.decryptByPrivatekey", new String(afterencrypt));

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

    			byte[] data = source.getBytes();

    			try {

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    			return true;
    		}
    		
    	     return false;
    	  
    	  }    	  
			private ContextWrapper getResources() {
				// TODO Auto-generated method stub
				return null;
			}


			public void Resultecho(Boolean boo, String meg, CallbackContext callbackContext){
    		   if(boo){
    	           callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, meg));
    		   } else {
    		       callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, meg));
    		   }			
    		}


		    /**
		     * 判断是否有网络连接
		     * 
		     * @return
		     */
		    public boolean isNetworkAvailable() {
		        ConnectivityManager cm = (ConnectivityManager) cordova.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo info = cm == null ? null : cm.getActiveNetworkInfo();
		        return (info != null && info.isConnected());
		    }
			
			

			private void doPay(String username, String productId, String productName, String price, int quantity,
							   String productImgUrl, String serverMessage, Map<String, String> customParams) {

				LetvOrder letvOrder = new LetvOrder(username, productId, productName, price, quantity, productImgUrl);
				// 自定义参数,可选
				letvOrder.setServerMessage(serverMessage);//服务端自定义信息，会在服务端同步回调中返回
				letvOrder.setCustomParams(customParams); //客户端自定义信息，会在客户端同步回调中返回

				LetvPay.pay(cordova.getActivity(), letvOrder, new LetvOnPayListener() {

					@Override
					public void onPaySuccess(LetvOrder order) {

						mOrder = new HashMap<String, Object>();
						// 商品id
						mOrder.put("product_id", order.getExternalProductId());
						// 商品名称
						mOrder.put("product_name", order.getMarketName());
						// 商品单价，非订单金额，单位为元
						mOrder.put("price", order.getPrice());
						// 商品图片
						mOrder.put("product_img", order.getProductImg());
						// 用户唯一标识
						mOrder.put("user_name", order.getUsername());
						// 商品数量
						mOrder.put("product_count", order.getQuality() + "");
						// 应用AppId
						mOrder.put("appid", order.getMaster());
						// 支付回调地址
						mOrder.put("notify_url", order.getCallback()+"?p=baisget001");
						// 客户端的自定义信息
						mOrder.put("custom_params", order.getCustomParams());

						outputMessage(cordova.getActivity(), mOrder.toString(), 3, callbackContext);

					}

					@Override
					public void onPayFailed(LetvOrder order, int stateCode) {
						//LetvOrder具体请参考onPaySuccess
						outputMessage(cordova.getActivity(), "支付失败，状态码：" + stateCode + "\n 失败订单信息如下：\n" + mOrder.toString(), 5, callbackContext);

					}
				});
			}

			private void outputMessage(Context context, String msg, int level, CallbackContext callbackContext) {

				if (msg != null && msg.length() > 0) {
					//Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
					if (level == 3) {
						Resultecho(true, "PAY success",callbackContext);
						Log.i("lepay_app_tv", msg);
					} else if (level == 5) {
						Resultecho(true, msg,callbackContext);
						Log.e("lepay_app_tv", msg);
					} else {
						Resultecho(true, "PAY error",callbackContext);
						Log.e("lepay_app_tv", "PAY error");
					}
				}
			}

    }