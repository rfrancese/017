package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FotoVideoActivity extends ActionBarActivity {

	//private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private ImageView imageView;
	private ArrayList<String> foto=new ArrayList<String>();
	private static String photoUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foto_video);

		upload();      
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				LinearLayout ll=(LinearLayout) findViewById(R.id.linear);
				imageView=new ImageView(this);
				imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), 450, 450));
				ll.addView(imageView);
				
				foto.add(photoUrl);
				
				Toast.makeText(this, "Immagine salvata in "+photoUrl+"!", Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
				Toast.makeText(this, "Operazione annullata!", Toast.LENGTH_LONG).show();
			} else {
				// Image capture failed, advise user
				Toast.makeText(this, "Errore nella cattura dell'immagine!", Toast.LENGTH_LONG).show();
			}
		}
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, 
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoUrl, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(photoUrl, options);
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, 
			int reqWidth, int reqHeight, String path) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.foto_video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.action_camera:
			upload();
			return true;

		case R.id.action_salva:
			salvaFoto();
			return true;

		case R.id.action_info:
			goInfo();
			return true;

		case R.id.action_logout:
			SharedPreferences settings = getSharedPreferences("login", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", null);
			// Commit the edits!
			editor.commit();
			goLogin();
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void salvaFoto() {
		
		int t_id=-1;
		Bundle extra=getIntent().getExtras();

		if (extra!=null) {
			t_id=extra.getInt("id");
		}

		DatabaseHandler dbHandler = new DatabaseHandler(this);
		
		for (String s: foto)
			dbHandler.addFoto(s, t_id);
		
		showToast("Foto salvate correttamente!");
		goMain();
	}


	private void upload() {

		// create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		String state = Environment.getExternalStorageState();

		Log.d("Directory", state);

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "MyTravelDiary");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyTravelDiary", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg");
		}  else {
			return null;
		}

		photoUrl=mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg";
		Log.d("foto", mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");

		return mediaFile;
	}

	public void goMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}

	private void goLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	private void showToast(String msg) {
		Context context=getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}
}
