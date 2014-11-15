package com.example.recvideo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.recvideo.R;



import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder recorder;
    Timer updateTimer = null;
    private String TAG = "Test";
    private Calendar c = null;
    ProgressDialog progress;
	int id = 1;

    
    float tempLAx = 0;								//Temporary variables for sensor values
   	float tempLAy = 0;
   	float tempLAz = 0;
    
    float tempAx = 0;								//Temporary variables for sensor values
	float tempAy = 0;
	float tempAz = 0;
	
	float tempGx = 0;
	float tempGy = 0;
	float tempGz = 0;
	
	float tempMFx = 0;
	float tempMFy = 0;
	float tempMFz = 0;
	
	float tempOx = 0;
	float tempOy = 0;
	float tempOz = 0;
	
	float tempTc = 0;
	
	float tempPh = 0;
	
	float tempPr = 0;
	
	float tempLi = 0;
	
	double tempLong = 0;
	
	double tempLat = 0;
	
	double tempAcc = 0;
	
	double tempAlt = 0;
	
	long dataCaptureStart;
	long dataCaptureEnd;
	long imageCaptureStart; 
	long imageCaptureEnd;
	long timePictureTaken;
	
	int i = 0;
	boolean gpsOn = false;
	boolean recording = false;
	int sampleSize = 200000;
	int readInt=5;									//Sample rate for data capture in ms
	String[] time = new String[sampleSize];
	
	float[] laccelX = new float[sampleSize];			//Arrays for data capture
	float[] laccelY = new float[sampleSize];
	float[] laccelZ = new float[sampleSize];

	float[] accelX = new float[sampleSize];			//Arrays for data capture
	float[] accelY = new float[sampleSize];
	float[] accelZ = new float[sampleSize];

	
	float[] gyroX = new float[sampleSize];
	float[] gyroY = new float[sampleSize];
	float[] gyroZ = new float[sampleSize];

	
	float[] compassX = new float[sampleSize];
	float[] compassY = new float[sampleSize];
	float[] compassZ = new float[sampleSize];

	
	float[] orientationX = new float[sampleSize];
	float[] orientationY = new float[sampleSize];
	float[] orientationZ = new float[sampleSize];

	
	float[] temperatureC = new float[sampleSize];
	float[] pressureH = new float[sampleSize];
	float[] proximityP = new float[sampleSize];
	float[] lightI = new float[sampleSize];
	
	String timeStamp = null;
	
	SensorManager sm = null;
	LocationManager locationManager = null;
	LocationListener locationListener = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            	
        try{  
        	  initializeCamera(); 
	    }
	    catch(Exception ex){	    	
	    }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            if(locationManager != null)
            {
	    	  locationManager.removeUpdates(locationListener); 
	    	  locationManager = null;
            }
            mCamera = null;
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.release();
            if(locationManager != null)
            {
	    	  locationManager.removeUpdates(locationListener); 
	    	  locationManager = null;
            }
            mCamera = null;
        }

    }
    
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        if (mCamera == null) {
            initializeCamera(); 
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        if (mCamera == null) {
            initializeCamera(); 
        }
    }
    
    public void initializeCamera(){
    	 // Create an instance of Camera
        mCamera = getCameraInstance();
        initializeView();       
    }
    
    public void initializeView(){
    	
    	 try {
    	
	    	//recorder = new MediaRecorder();
	        mPreview = new CameraPreview(this,mCamera);
	        FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
	        preview.addView(mPreview);

    	 }catch (Exception e) {
             Log.d("Test", "Error setting camera preview: " + e.getMessage());
         }
    }
    
    public void initializeParameters(){    	     	    
    	
    	    tempLAx = 0;								//Temporary variables for sensor values
    	   	tempLAy = 0;
    	   	tempLAz = 0;
    	         
    	    tempAx = 0;								//Temporary variables for sensor values
    		tempAy = 0;
    		tempAz = 0;
    		
    		tempGx = 0;
    		tempGy = 0;
    		tempGz = 0;
    		
    		tempMFx = 0;
    		tempMFy = 0;
    		tempMFz = 0;
    		
    		tempOx = 0;  
    		tempOy = 0;
    		tempOz = 0;    
    		
    		tempTc = 0;    		
    		tempPh = 0;    		
    		tempPr = 0;    		
    		tempLi = 0;    		
    		tempLong = 0;    		
    		tempLat = 0;    		
    		tempAcc = 0;    		
    		tempAlt = 0;
    		
    		laccelX = new float[sampleSize];			//Arrays for data capture
    		laccelY = new float[sampleSize];
    		laccelZ = new float[sampleSize];
    		
    		accelX = new float[sampleSize];			//Arrays for data capture
    		accelY = new float[sampleSize];
    		accelZ = new float[sampleSize];
    		
    		gyroX = new float[sampleSize];
    		gyroY = new float[sampleSize];
    		gyroZ = new float[sampleSize];
    		
    		compassX = new float[sampleSize];
    		compassY = new float[sampleSize];
    		compassZ = new float[sampleSize];
    		
    		orientationX = new float[sampleSize];
    		orientationY = new float[sampleSize];
    		orientationZ = new float[sampleSize];
    		
    		temperatureC = new float[sampleSize];
    		pressureH = new float[sampleSize];
    		proximityP = new float[sampleSize];
    		lightI = new float[sampleSize];
    }
    
    public void initialiseSensors(){
    	// Do something in response to button click
    	sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);		//Define sensor manager and sensors
    	final Sensor laSensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
 	    final Sensor aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    final Sensor gSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	    final Sensor mfSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    final Sensor oSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    //final Sensor tSensor = sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
	    //final Sensor pSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
	    //final Sensor prSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	    final Sensor lSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);	    
	    sm.registerListener(myLinearAccelerometerListener, laSensor, SensorManager.SENSOR_DELAY_FASTEST);	
 	    sm.registerListener(myAccelerometerListener, aSensor, SensorManager.SENSOR_DELAY_FASTEST);	
		sm.registerListener(myGyroscopeListener, gSensor, SensorManager.SENSOR_DELAY_FASTEST);		//by registering sensor manager and
		sm.registerListener(myCompassListener, mfSensor, SensorManager.SENSOR_DELAY_FASTEST);		//Defining sensors
		sm.registerListener(myOrientationListener, oSensor, SensorManager.SENSOR_DELAY_FASTEST);
		//sm.registerListener(myTemperatureListener, tSensor, SensorManager.SENSOR_DELAY_FASTEST);
		//sm.registerListener(myPressureListener, pSensor, SensorManager.SENSOR_DELAY_FASTEST);
		//sm.registerListener(myProximityListener, prSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sm.registerListener(myLightListener, lSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    
   
    /** Nullify everything on program exit to clear memory. */
	@Override
	public void onDestroy() {
		super.onDestroy();
		laccelX = null;
		laccelY = null;
		laccelZ = null;		
		accelX = null;
		accelY = null;
		accelZ = null;
		gyroX = null;
		gyroY = null;
		gyroZ = null;
		compassX = null;
		compassY = null;
		compassZ = null;
		orientationX = null;
		orientationY = null;
		orientationZ = null;
		

		
		//mPreview= null;
		mCamera = null;
		sm = null;
  	  	locationManager= null; 
  	    locationListener = null;
	};

    
    public void captureData(View v) { 
    	try{
    		
    		c = Calendar.getInstance(); 
    		Button btnRecording = (Button) findViewById(R.id.button_capture);
	    	if(recording == false)
	    	{
	    		timeStamp = getTime();
	    		initializeParameters(); 
	    		prepareVideoRecorder();	    		
	    		initialiseSensors();
	    		 
	    		btnRecording.setText("Stop");
	    		recording = true;
	    		recorder.start(); 
	    			    
	    		dataCaptureStart = System.currentTimeMillis();	    		
	    		startCapturing();
	    	}
	    	else
	    	{
	    		stopCapturing();
	    		btnRecording.setText("Saving");
	    	
	    		recording = false;

	    		startBackgroundThread();
	    		
	    		releaseMediaRecorder();
	    	
		        btnRecording.setText("Start");
	    	}	    	
    	}
    	catch(Exception ex)
    	{
    		 Log.d(TAG, "capture failure");
    	}
    }
    
    private void startBackgroundThread()
    {
    	progress = ProgressDialog.show(this, "Please Wait",
    		    "Saving data to the disk..", true);
    	// Start a lengthy operation in a background thread
    	new Thread(
    	    new Runnable() {
    	        @Override
    	        public void run() {
    	            try {
                        // Sleep for 5 seconds
                    	writeSensorDataToFile(getFilename()  + "Sensor_" + timeStamp + ".csv");    	
                        progress.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, "write failure");
                    }    	            
    	        }
    	    }
    	// Starts the thread by calling the run() method in its Runnable
    	).start();
    }
    
    
    private boolean prepareVideoRecorder(){

        recorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        try{
        	mCamera.unlock();
        }catch (RuntimeException r){
		    Log.d(TAG, "mcamera unlock: " + r.getMessage());
        }
        recorder.setCamera(mCamera);

        // Step 2: Set sources
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

        // Step 4: Set output file
        String imageName =  getFilename()  + "Video_" + timeStamp + ".3gp";
        recorder.setOutputFile(imageName);

        // Step 5: Set the preview output
        recorder.setPreviewDisplay(mPreview.getHolder().getSurface());
 
        // Step 6: Prepare configured MediaRecorder
        try {
        	recorder.setVideoFrameRate(5);
        	recorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            //releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            //releaseMediaRecorder();
            return false;
        }
        return true;
    }


    private void releaseMediaRecorder(){
        if (recorder != null) {
        	recorder.reset();   // clear recorder configuration
        	recorder.release(); // release the recorder object     
        	recorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    
    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath() + "/RECVIDEO/" + getDate();
    	File file = new File(filepath);
        boolean status = false;  
        if(!file.exists()){
               status = file.mkdirs();   
        }        
        return (file.getAbsolutePath() + "/" );
	}
    
   
    public void startCapturing() {     	 	
    	updateTimer = new Timer();
    	updateTimer.scheduleAtFixedRate(new TimerTask(){
	    	public void run(){
	    					//Start capturing if all sensors are ready
		    		laccelX[i] = tempLAx;											//Store sensor data values into arrays
	    			laccelY[i] = tempLAy;
	    			laccelZ[i] = tempLAz;
	    			accelX[i] = tempAx;											//Store sensor data values into arrays
	    			accelY[i] = tempAy;
	    			accelZ[i] = tempAz;
	    			gyroX[i] = tempGx;
	    			gyroY[i] = tempGy;
	    			gyroZ[i] = tempGz;
	    			compassX[i] = tempMFx;
	    			compassY[i] = tempMFy;
	    			compassZ[i] = tempMFz;
	    			orientationX[i] = tempOx;
	    			orientationY[i] = tempOy;
	    			orientationZ[i] = tempOz;
	    			temperatureC[i] = tempTc;
	    			pressureH[i] = tempPh;
	    			proximityP[i] = tempPr;
	    			lightI[i] = tempLi;
	    			
	    			time[i] = Long.toString(System.currentTimeMillis());
    			
	    			i=i+1;													//Increment array position
	        }
	    }, 0, readInt);
    }
    
    
    public void stopCapturing()
    {
    	sm.unregisterListener(myLinearAccelerometerListener);	
    	sm.unregisterListener(myAccelerometerListener);	
		sm.unregisterListener(myGyroscopeListener);
		sm.unregisterListener(myCompassListener);
		sm.unregisterListener(myOrientationListener);
		sm.unregisterListener(myTemperatureListener);
		sm.unregisterListener(myPressureListener);
		sm.unregisterListener(myProximityListener);
		sm.unregisterListener(myLightListener);
		updateTimer.cancel();
    }
    
    public void saveImage(String dFile, byte[] data)
    {
    	File pictureFile = new File(dFile);      
    	
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    
    
    private void writeSensorDataToFile(String dFile) {        
    	try {																					//Saves data to a file
			
			File file = new File(dFile);
           	
			/** Write the header of sensor data file. */
			FileWriter writer = new FileWriter(file);
			writer.append("Time (ms)");
			writer.append(',');
			writer.append("LAx (m/s^2)");
			writer.append(',');
			writer.append("LAy (m/s^2)");
			writer.append(',');
			writer.append("LAz (m/s^2)");
			writer.append(',');
			writer.append("Ax (m/s^2)");
			writer.append(',');
			writer.append("Ay (m/s^2)");
			writer.append(',');
			writer.append("Az (m/s^2)");
			writer.append(',');
			writer.append("Gx (rad/s)");
			writer.append(',');
			writer.append("Gy (rad/s)");
			writer.append(',');
			writer.append("Gz (rad/s)");
			writer.append(',');
			writer.append("Ox (deg)");
			writer.append(',');
			writer.append("Oy (deg)");
			writer.append(',');
			writer.append("Oz (deg)");
			writer.append(',');
			writer.append("Mx (uT)");
			writer.append(',');
			writer.append("My (uT)");
			writer.append(',');
			writer.append("Mz (uT)");
			writer.append('\n');
   		 	
			/** Write values of sensor data file. */
			for (int j = 0; j < i; j++) {
				int timeMS = j*readInt;
				String lineI = (time[j] + "," + laccelX[j] + "," + laccelY[j] + "," + laccelZ[j] + "," + accelX[j] + "," + accelY[j] + "," + accelZ[j] + "," + gyroX[j] + "," + gyroY[j] + "," + gyroZ[j] + "," + orientationX[j] + "," + orientationY[j] + "," + orientationZ[j] + "," + compassX[j] + "," + compassY[j] + "," + compassZ[j]);
				writer.append(lineI);
				writer.append('\n');
			}
			
			/** Complete the sensor data file. */
			writer.flush();
			writer.close();
			
			//Enable the button
			Button btnRecording = (Button) findViewById(R.id.button_capture);
	
    		btnRecording.setEnabled(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}             
    }
    
    
    private void writeDetailsToFile(String dFile, String imageFile) {        
    	try {																					//Saves data to a file
			
			File file = new File(dFile);
			
			/** Write the header of sensor data file. */
			FileWriter  writer= new FileWriter(file,true);
			//BufferedWriter writer = new BufferedWriter(fwriter);

			writer.append("Time (ms)");
			writer.append(',');
			writer.append(getTime());
			writer.append('\n'); 
			writer.append("Longtitude");
			writer.append(',');
			writer.append(Double.toString(tempLong));
			writer.append('\n'); 
			writer.append("Latitude");
			writer.append(',');
			writer.append(Double.toString(tempLat));
			writer.append('\n'); 
			writer.append("Altitude");
			writer.append(',');
			writer.append(Double.toString(tempAlt));
			writer.append('\n'); 			
			writer.append("Accuracy");
			writer.append(',');
			writer.append(Double.toString(tempAcc));
			writer.append('\n'); 			
			writer.append("Start Data Capture (ms)");
			writer.append(',');
			writer.append(Long.toString(dataCaptureStart));
			writer.append('\n');
			writer.append("End Data Capture (ms)");
			writer.append(',');
			writer.append(Long.toString(dataCaptureEnd));
			writer.append('\n');
			writer.append("Start Image Capture (ms)");
			writer.append(',');
			writer.append(Long.toString(imageCaptureStart));
			writer.append('\n');
			writer.append("End Image Capture (ms)");
			writer.append(',');
			writer.append(Long.toString(imageCaptureEnd));
			writer.append('\n');
			writer.append("Time Picture Taken (ms)");
			writer.append(',');
			writer.append(Long.toString(timePictureTaken));
			writer.append('\n');

			
			//Read meta data
			File image = new File(imageFile);
			ExifInterface exif = new ExifInterface(file.getCanonicalPath());
			
					
			writer.append("Model");   
			writer.append(',');
			writer.append(exif.getAttribute(ExifInterface.TAG_MODEL));
			writer.append('\n');
			writer.append("Focal Length");
			writer.append(',');
			writer.append(exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
			writer.append('\n');
			writer.append("Flash");
			writer.append(',');
			writer.append(exif.getAttribute(ExifInterface.TAG_FLASH));
			writer.append('\n');
			writer.append("Date Time");
			writer.append(',');
			writer.append(exif.getAttribute(ExifInterface.TAG_DATETIME));
			writer.append('\n');
			writer.append("Width");
			writer.append(',');
			writer.append(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
			writer.append('\n');
			//writer.append("Exposure Time");
			//writer.append(',');
			//writer.append(exif.getAttribute(ExifInterface.TAG_ISO));
			//writer.append('\n');
								
			writer.append('\n'); 
			writer.flush();
			writer.close();
			/** Complete the sensor data file. */
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}             
    }
	

	private String getTime()
	{ 
		       
		int mYear = c.get(Calendar.YEAR);
	    int mMonth = c.get(Calendar.MONTH)+1;
	    int mDay = c.get(Calendar.DATE);
	    int mHour = c.get(Calendar.HOUR_OF_DAY);
	    int mMinute = c.get(Calendar.MINUTE);
	    int mSecond = c.get(Calendar.SECOND);
	    int mMiliSecond = c.get(Calendar.MILLISECOND);
	            
	    return (mYear+"_"+mMonth+"_"+mDay+"_"+mHour+"_"+mMinute+"_"+mSecond+"_"+mMiliSecond);	//Build the file name using date and time
		
	}
	
	private String getDate()
	{
		int mYear = c.get(Calendar.YEAR);
	    int mMonth = c.get(Calendar.MONTH)+1;
	    int mDay = c.get(Calendar.DATE);
            
	    return (mYear+"_"+mMonth+"_"+mDay);	//Build the file name using date and time
		
	}
    
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
        	int cameraId = findFrontFacingCamera();
            c = Camera.open(cameraId); // attempt to get a Camera instance
            Camera.Parameters cp = c.getParameters();

            List<Size> sl = cp.getSupportedPictureSizes();
            
            int w = 0,h = 0;
            for(Size s : sl){
               //if s.width meets whatever criteria you want set it to your w
               //and s.height meets whatever criteria you want for your h
            	int temp1 = s.width - 2592;
            	if (temp1 < 0.0F)temp1 = -temp1;
            	int temp2 = w - 2592;
            	if (temp2 < 0.0F)temp2 = -temp2;
            	if(temp1 < temp2)          
            	{
	               w = s.width;
	               h = s.height;	              
            	}
            }
            cp.setPictureSize(w,h);
            c.setParameters(cp);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        	int i = 0;
        }
        return c; // returns null if camera is unavailable
    }
    
    private static int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
          CameraInfo info = new CameraInfo();
          Camera.getCameraInfo(i, info);
          if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            
            cameraId = i;
            break;
          }
        }
        return cameraId;
      }
    
    final SensorEventListener myLinearAccelerometerListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
				
				tempLAx = sensorEvent.values[0];
				tempLAy = sensorEvent.values[1];
				tempLAz = sensorEvent.values[2];
						
 			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
    
    final SensorEventListener myAccelerometerListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				
				tempAx = sensorEvent.values[0];
				tempAy = sensorEvent.values[1];
				tempAz = sensorEvent.values[2];
 			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	/** Gyroscope Sensor Listener */
	final SensorEventListener myGyroscopeListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
				tempGx = sensorEvent.values[0];
    			tempGy = sensorEvent.values[1];
    			tempGz = sensorEvent.values[2];
 			}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	
	/** Magnetic Field Sensor Listener */
	final SensorEventListener myCompassListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
				tempMFx = sensorEvent.values[0];
    			tempMFy = sensorEvent.values[1];
    			tempMFz = sensorEvent.values[2];
			}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	
	/** Accelerometer Sensor Listener */
	final SensorEventListener myOrientationListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION){
				tempOx = sensorEvent.values[0];
				tempOy = sensorEvent.values[1];
				tempOz = sensorEvent.values[2];
	  		}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	final SensorEventListener myTemperatureListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_TEMPERATURE){
				tempTc = sensorEvent.values[0];	  		}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	final SensorEventListener myPressureListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
				tempPh = sensorEvent.values[0];	  		}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	final SensorEventListener myProximityListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
				tempPr = sensorEvent.values[0];	  		}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};
	
	final SensorEventListener myLightListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent sensorEvent){
			if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
				tempLi = sensorEvent.values[0];	  		}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
	};

}
