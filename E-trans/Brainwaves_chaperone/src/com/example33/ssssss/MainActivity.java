package com.example33.ssssss;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.support.v4.widget.DrawerLayout;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	public static List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
	static SimpleAdapter simpleAdpt;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private static boolean logged=false;
	private static boolean toPay=true;
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}
	public HashMap<String, String> createPlanet(String key, String name) {
HashMap<String, String> planet = new HashMap<String, String>();
planet.put(key, name);

return planet;
	}

	public void logOut(View v)
	{
		logged=false;
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(6)).commit();
	

	}
	/////////////
	class SendAmount extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            //this is to show dialogue
            
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            */
            
        }
        
        protected String doInBackground(String... args) {
               String myacc=args[0];
               String cust_accno=args[1];
               String amount=args[2];
            String payment_url ="http://bittest.net16.net/transfer.php"; 
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("myacc",myacc ));
            params.add(new BasicNameValuePair("cust_accno", cust_accno));
            params.add(new BasicNameValuePair("amount", amount));
            
            
            
             /*
             JSONObject json = jsonParser.makeHttpRequest(payment_url,
                    "POST", params);
               */
               
               
            HttpPost postMethod = new HttpPost(payment_url);
            try{
            postMethod.setEntity(new UrlEncodedFormEntity(params));
            
            DefaultHttpClient hc = new DefaultHttpClient();

            
            ResponseHandler<String> handler=new BasicResponseHandler();
            HttpResponse response = hc.execute(postMethod);
            String body=handler.handleResponse(response);
            String []body1=body.split("<");
            
            return body1[0];
            }
            catch(Exception e){
            	
            }
            
            /*
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpClient.execute(httppost, responseHandler);
            */
            
            return "Transaction failed";
        }
        
        protected void onPostExecute(String result) {
            // dismiss the dialog once done
        	
        	Toast.makeText(getApplicationContext(),"Payement Successfull",Toast.LENGTH_SHORT).show();
        	
        	updateNotification(result);
        }
  }
	public void scanItems(View v){
		 FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(8)).commit();
		
		
	}
	public void Refresh(View v){
		updateNotification("");
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void updateNotification(String result)
	{
		String  my_url="http://bittest.net16.net/notifyshopkeeper.php?myacc=12345678123456";
 	WebView webView=(WebView)findViewById(R.id.content_frame);
 	WebSettings webSettings = webView.getSettings();
	  webSettings.setJavaScriptEnabled(true);
	  webView.setWebViewClient(new WebViewClient());
	  webSettings.setBuiltInZoomControls(true);
	  webView.setVerticalScrollBarEnabled(false);
	  webView.setHorizontalScrollBarEnabled(false);
	  webView.loadUrl(my_url);
  

	}
////////////////// qr scanning  /////////////////
	public void scanqr(View view) {
		toPay=true;
	 	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		startActivityForResult(intent, 0);
 }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
		if (resultCode == RESULT_OK) {
		String contents = intent.getStringExtra("SCAN_RESULT");
		//String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		Toast.makeText(this, contents,Toast.LENGTH_LONG).show();
		////////////////////////////////
		if(toPay==true){
			
		
		   final Dialog myDialog = new Dialog(this);
			
			myDialog.setContentView(R.layout.saveinnew);
			myDialog.setTitle("E-Transfer" );
			Button b_ok = (Button) myDialog.findViewById( R.id.button_save);
		    Button b_cancel = (Button) myDialog.findViewById( R.id.button_cancel);
			final EditText et = (EditText) myDialog.findViewById( R.id.input_folder_name );
			
			b_cancel.setOnClickListener( 
				new View.OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						myDialog.dismiss();
						et.setText("");
					}
				} 
			);
			
			b_ok.setOnClickListener( 
					new View.OnClickListener()
					{
						@Override
						public void onClick(View arg0) 
						{
							myDialog.dismiss();
							String amount = et.getText().toString();
							 try{
							//doNetworkCompuation();
								 String myacc="12345678912345";
									String customer_acc="12345678123456";
									String payable_amt =amount;
									SendAmount abc=new SendAmount();
									abc.execute(new String[]{myacc,customer_acc,payable_amt});
								 
								 
							 }
							 catch(Exception e)
							 {
								 Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
							 }
							//Toast.makeText(getBaseContext(), "Page has been saved to " + et.getText().toString(), Toast.LENGTH_LONG).show();
						}
					} 
				);
			
			
			
			myDialog.show();
		
		
		
		
		////////////////////////////
		
		
		
		
		// Handle successful scan
		}
       else{
    	  // contents;
    	   planetsList.add(createPlanet("planet", contents));
   		simpleAdpt.notifyDataSetChanged();
         toPay=false;

		}
		}
		else if (resultCode == RESULT_CANCELED) {
		//Handle cancel
			Toast.makeText(this, "could not scan qr",Toast.LENGTH_LONG).show();
			
		}

		}
		}
		
		
	//////////////////////////////////////////////
	static Bitmap bitmap;
	
/////////////// create qr //////////////////////
	 @SuppressLint("NewApi")
	public void createqr(View view) {
		 	
		// EditText qrInput = (EditText) findViewById(R.id.qrInput);
		   String qrInputText ="12345678123456";
		  // Log.v(LOG_TAG, qrInputText);
		 
		   //Find screen size
		   WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		   Display display = manager.getDefaultDisplay();
		   Point point = new Point();
		   display.getSize(point);
		   int width = point.x;
		   int height = point.y;
		   int smallerDimension = width < height ? width : height;
		   smallerDimension = smallerDimension * 3/4;
		 
		   //Encode with a QR Code image
		   QRCodeEncode qrCodeEncoder = new QRCodeEncode(qrInputText, 
		             null, 
		             Contents.Type.TEXT,  
		             BarcodeFormat.QR_CODE.toString(), 
		             smallerDimension);
		   try {
		    bitmap = qrCodeEncoder.encodeAsBitmap();
		  //  ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		  //  myImage.setImageBitmap(bitmap);
		 
		   
		    FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(6)).commit();
		
		    ///////////////////
		   } catch (WriterException e) {
		    e.printStackTrace();
		   }
	 }
	 ////////////// sign up //////////////////////////
	 public void signup(View v)
	 {
		 logged=true;
		 FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(7)).commit();
			//logged=false;
		
	 }
	////////////////////////////////////////////////
	public void authinticate(View v)
	{
		
		EditText editText1 = (EditText) findViewById(R.id.user_name);
		String uname = editText1.getText().toString();
		EditText editText2 = (EditText) findViewById(R.id.user_password);
		String pass = editText2.getText().toString();
		/////////// toast//////////
		/*Context context = getApplicationContext();
		CharSequence text = ""+uname+"-"+pass;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		//toast.show();
		*/
		///////////////
		String username="rajeevh.93@gmail.com";
		String password="1234";
		if(uname.equals(username)&& pass.equals(password) ){
			logged=true;
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(1)).commit();
		
	
		}else{
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast1 = Toast.makeText(context, "check username or password", duration);
			toast1.show();
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(6)).commit();
			logged=false;
	
		}
			
			
			
			
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#980000")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
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
 public void addItem(View v){
	 toPay=false;
	 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		startActivityForResult(intent, 0);

 }
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			//Drawable image  =getResources().getDrawable(WallPaperoneFragment.imagefield.get(position));imageView.setImageDrawable(image);
			
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@SuppressLint("SetJavaScriptEnabled")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView;
			WebView webview;
			if(logged)
			{
			int pos= getArguments().getInt(
					ARG_SECTION_NUMBER);
			switch(pos){
			case 1: rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
				webview = (WebView) rootView
					.findViewById(R.id.content_frame);
			webview.getSettings().setJavaScriptEnabled(true);
		    webview.loadUrl("http://bittest.net16.net/last5details.php");
			
				return rootView;
				
			case 2:
				rootView = inflater.inflate(R.layout.my_qr, container,
					false);
				return rootView;
			case 3: rootView = inflater.inflate(R.layout.fragment_transactions, container,
					false);
			/*TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));*/
			 webview = (WebView) rootView
					.findViewById(R.id.content_frame);
			webview.getSettings().setJavaScriptEnabled(true);
		    webview.loadUrl("http://bittest.net16.net/notifyshopkeeper.php?myacc=12345678123456");
				return rootView;
			case 4: rootView = inflater.inflate(R.layout.fragment_change_pin, container,
					false);
				return rootView;
			case 5: rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			    return rootView;
			case 6: rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
				return rootView;
			case 7: rootView = inflater.inflate(R.layout.fragement_signup, container,
					false);
			logged=false;
			return rootView;
			case 8: rootView = inflater.inflate(R.layout.fragment_scan_items, container,
					false);
			logged=false;
			
				/////////////////////////////////////////////////////////////
				  ListView lv = (ListView) rootView.findViewById(R.id.listView);
				     
				     
				    // This is a simple adapter that accepts as parameter
				    // Context
				    // Data list
				    // The row layout that is used during the row creation
				    // The keys used to retrieve the data
				    // The View id used to show the data. The key number and the view id must match
				     simpleAdpt = new SimpleAdapter(getActivity(), planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
				    
				 
				    lv.setAdapter(simpleAdpt);
				    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
				    	 
				       

						@Override
						public boolean onItemLongClick(AdapterView<?>parentAdapter, View view,
								int position,long id) {
							// TODO Auto-generated method stub
							  TextView clickedView = (TextView) view;
							    planetsList.remove(position);
							    simpleAdpt.notifyDataSetChanged();
					            //Toast.makeText(getActivity(), "Item with id ["+id+"] - Position ["+position+"] - Planet ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();
					    
							return false;
						}
				   });
				 	
				//////////////////////////////////////////////////////////////////
					return rootView;
		
			}
			}
			 rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
