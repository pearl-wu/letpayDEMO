package com.bais.let;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.widget.Toast;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import com.letv.tvos.paysdk.LetvOnPayListener;
import com.letv.tvos.paysdk.LetvPay;
import com.letv.tvos.paysdk.appmodule.pay.model.LetvOrder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends CordovaPlugin {
    	
    	  protected static final String LOG_TAG = "letOSOrder__pay";
	      private String username = null;
	      private HashMap<String, Object> mOrder;	      
	      private CallbackContext batteryCallbackContext = null;

	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{
    		      			
    		if(action.equals("Pay")){
    			
    			this.batteryCallbackContext = callbackContext;

				JSONObject options = args.getJSONObject(0);
				username = LetvPay.getUniqueId(cordova.getActivity()); // 获取用户环境唯一标识,无线MAC地址
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
				String partner_order_no = options.getString("partner_order_no");
				if(partner_order_no.equals("")){
					partner_order_no = sdf.format(dt);
				}
				
				Map<String, String> customMap = new HashMap<String, String>();
				customMap.put("customMessage", "I am customMessage on client");				
				
				doPay(
					username, 
					partner_order_no, 
					options.getString("subject"), 
					options.getString("price"), 
					1,
					"http://kyytv.ebais.com.cn/let_productimg.png", 
					options.getString("partner_notify_url"),	
					customMap
				);
    	       		return true;
    	       
    		}else if(action.equals("Iandroid")){
    			
    			this.batteryCallbackContext = callbackContext;
    			boolean tr = args.getBoolean(0);
    			if(tr){
    			  String androidId = Secure.getString(cordova.getActivity().getContentResolver(), Secure.ANDROID_ID);
    			  Resultecho(true, androidId);
    			}
    		  	return true;
    		  
    		}else if(action.equals("Packageinfo")){
    			
    			this.batteryCallbackContext = callbackContext;
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
    			Resultecho(true, packageinfo);
    			return true;
    			
    		}else if(action.equals("Echo")){
    			
    			String context = args.getString(0);
    			int duration = args.getInt(1);
    			Toast.makeText(cordova.getActivity(), context, Toast.LENGTH_SHORT).show();
    			return true;
    			
    		}else if(action.equals("Sign")){
    			
    			this.batteryCallbackContext = callbackContext;
    			String mag = RSASign.sign(args.getString(0), Config.getPrikey(), "utf-8" );
    			Resultecho(true, mag);
    			return true;
    			
    		}
    		
    	     return false;
    	  
    	  }


			public void Resultecho(Boolean boo, String info){
    		   if(boo){
    			   this.batteryCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, info));
    		   } else {
    			   this.batteryCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, info));
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
						mOrder.put("notify_url", order.getCallback());
						// 客户端的自定义信息
						mOrder.put("custom_params", order.getCustomParams());
						
						Resultecho(true, "PAY success");	//mOrder.toString()

					}

					@Override
					public void onPayFailed(LetvOrder order, int stateCode) {
						//LetvOrder具体请参考onPaySuccess
						Resultecho(true, "PAY cancel");		//"支付失败，状态码：" + stateCode + "\n 失败订单信息如下：\n" + mOrder.toString()						
					}
					
				});
			}
			

    }
