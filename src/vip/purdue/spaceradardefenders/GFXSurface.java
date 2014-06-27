package vip.purdue.spaceradardefenders;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class GFXSurface extends Activity implements OnTouchListener{

	MyBringBackSurface ourSurfaceView;
	float x, y;
	Rect box, box2, sideBar;
	Bitmap test, plus, background, gameover;
	Bitmap radar, base, gain1, gain2, gain3, gain4, gain5, antenna1, antenna2, antenna3, antenna4, antenna5;
	Bitmap alienPreDetect, alienPostDetect, alienRed;
	int screenWidth, screenHeight, playableWidth, playableHeight;

	int numColumns, numRows;
	byte[][] spacesType;
	Rect[][] spacesRect;
	RectF[][] spacesRectF;
	Matrix[][] spacesMatrix;
	float[][] spacesDegrees;
	byte[] tutorialStatus;  //1 for complete, 0 for incomplete
	int tutGainLvl, tutBSLvl, boxGainLvl, boxBSLvl;
	boolean isPlaying, playerTurn, cpuTurn;
	Random rand;
	Point touchedBox;
	
	float[] alienXPos;
	float[] alienYPos;
	int baseXPos;
	int baseYPos;
	int[] alienDetected;  //0 for undetected(black), 1 for detected(white), 2 for at base(red)
	float[] alienDeltaX;
	float[] alienDeltaY;
	
	String tutFilename;
	String boardFilename;
	FileOutputStream tutOutputStream;
	FileOutputStream boardOutputStream;
	FileInputStream tutInputStream;
	FileInputStream boardInputStream;
	
	//below is Katherine
	PopupWindow popUp;
	LinearLayout mainLayout;
	RelativeLayout rLayout;
    TextView tv, tv2, tvVerticalSpacing, tvVerticalSpacing2, levelTop, levelBottom;
	LinearLayout lLayout;
    Button bRad, bExit, bRad2;
    int counter = 0;
    //above is Katherine
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ourSurfaceView = new MyBringBackSurface(this);
		ourSurfaceView.setOnTouchListener(this);
		x = y = 0;
		box = new Rect();
		box2 = new Rect();
		sideBar = new Rect();
		rand = new Random();
		isPlaying = true;
		playerTurn = true;
		cpuTurn = false;
		touchedBox = new Point(0,0);
		numColumns = 9; //size of board
		numRows = 7; //size of board
		tutFilename = "tutorialFile";
		boardFilename = "boardFile";
		
		Display display = getWindowManager().getDefaultDisplay();
    	Point screenSize = new Point();
    	display.getSize(screenSize);
    	screenWidth = screenSize.x;
    	screenHeight = screenSize.y;
    	playableWidth = screenHeight * numColumns / numRows;
    	playableHeight = screenHeight;
    	
    	sideBar.set(playableWidth, 0, screenWidth, screenHeight);
		
		spacesType = new byte[numColumns][numRows];
		tutorialStatus = new byte[10];
		readBoardFile();
		readTutorialFile();
		
		/*for(int i=0; i<numColumns; i++){
			for(int j=0; j<numRows; j++){
				spacesType[i][j] = 0;
			}
		}*/
		
		int xStep = playableWidth / numColumns;
		int yStep = screenHeight / numRows;
		spacesRect = new Rect[numColumns][numRows];
		spacesRectF = new RectF[numColumns][numRows];
		spacesMatrix = new Matrix[numColumns][numRows];
		spacesDegrees = new float[numColumns][numRows];
		for(int i=0; i<numColumns; i++){
			for(int j=0; j<numRows; j++){
				spacesRect[i][j] = new Rect(i*xStep, j*yStep, (i+1)*xStep, (j+1)*yStep);
				spacesRectF[i][j] = new RectF(spacesRect[i][j]);
				spacesMatrix[i][j] = new Matrix();
				spacesMatrix[i][j].mapRect(spacesRectF[i][j]);
				spacesMatrix[i][j].setTranslate(spacesRect[i][j].left, spacesRect[i][j].top);
				spacesDegrees[i][j] = (float) 0.0;
			}
		}
		
		test = BitmapFactory.decodeResource(getResources(), R.drawable.greenball);
		plus = BitmapFactory.decodeResource(getResources(), R.drawable.plus);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.spacebackground);
		radar = BitmapFactory.decodeResource(getResources(), R.drawable.radar);
		base = BitmapFactory.decodeResource(getResources(), R.drawable.base);
		antenna1 = BitmapFactory.decodeResource(getResources(), R.drawable.antenna1);
		antenna2 = BitmapFactory.decodeResource(getResources(), R.drawable.antenna2);
		antenna3 = BitmapFactory.decodeResource(getResources(), R.drawable.antenna3);
		antenna4 = BitmapFactory.decodeResource(getResources(), R.drawable.antenna4);
		antenna5 = BitmapFactory.decodeResource(getResources(), R.drawable.antenna5);
		
		gain1 = BitmapFactory.decodeResource(getResources(), R.drawable.gain1);
		gain2 = BitmapFactory.decodeResource(getResources(), R.drawable.gain2);
		gain3 = BitmapFactory.decodeResource(getResources(), R.drawable.gain3);
		gain4 = BitmapFactory.decodeResource(getResources(), R.drawable.gain4);
		gain5 = BitmapFactory.decodeResource(getResources(), R.drawable.gain5);
		alienPreDetect = BitmapFactory.decodeResource(getResources(), R.drawable.monster_blk);
		alienPostDetect = BitmapFactory.decodeResource(getResources(), R.drawable.monster_white);
		alienRed = BitmapFactory.decodeResource(getResources(), R.drawable.monster_red);
		gameover = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
		
		//below is Katherine
		mainLayout = new LinearLayout(this);
		mainLayout.addView(ourSurfaceView);
		
		setContentView(mainLayout);
		//above is Katherine
		
		//below is Katherine's code for the popup window
	        
	        popUp = new PopupWindow(this);
	        rLayout = new RelativeLayout(this);
	        lLayout = new LinearLayout(this);     
			
			Typeface font = Typeface.createFromAsset(getAssets(), "Venera-500.otf");		
	       
	        tv = new TextView(this);
	        tv.setText("Gain");
	        tv.setWidth(300);
	        tv.setTypeface(font);   
	        tv.setTextSize(22);
	        
	        tv2 = new TextView(this);
	        tv2.setText("Beam Steering");
	        tv2.setWidth(700);
	        tv2.setTypeface(font);
	        tv2.setTextSize(22);
	        
	        levelTop = new TextView(this);
	        levelTop.setText("Level: 3/5");
	        levelTop.setWidth(600);
	        levelTop.setTypeface(font);   
	        levelTop.setTextSize(15);
	        
	        levelBottom = new TextView(this);
	        levelBottom.setText("Level: 1/5");
	        levelBottom.setWidth(600);
	        levelBottom.setTypeface(font);   
	        levelBottom.setTextSize(15);
	        
	        tvVerticalSpacing = new TextView(this);
	        tvVerticalSpacing.setWidth(350);
	        tvVerticalSpacing.setHeight(30);
	        
	        tvVerticalSpacing2 = new TextView(this);
	        tvVerticalSpacing2.setWidth(350);
	        tvVerticalSpacing2.setHeight(50);
	        
	        bExit = new Button(this);
	        bExit.setText(" X ");
	        bExit.setTextColor(Color.WHITE);
	        bExit.setTextSize(20);
	        bExit.setBackgroundResource(R.drawable.button_spacex3);
	        bExit.setTypeface(font);
	        
	        bRad = new Button(this);
	        bRad.setText(" Upgrade ");
	        bRad.setTextColor(Color.WHITE); 
	        bRad.setTextSize(17);
	        bRad.setBackgroundResource(R.drawable.butoon_upgrade3);
	        bRad.setTypeface(font); 
	        
	        bRad2 = new Button(this);
	        bRad2.setText("Research");
	        bRad2.setTextColor(Color.WHITE);
	        bRad2.setTextSize(17);
	        bRad2.setBackgroundResource(R.drawable.button_space3);
	        bRad2.setTypeface(font);

	        bExit.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        	popUp.dismiss();
	        	}
	        });
	        
	        LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsTopTv = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	               LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsLevelTop = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsLevelBottom = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsTopB = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsLowerTv = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsExit = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsSpacing = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        RelativeLayout.LayoutParams paramsLowB = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);


	        tv.setId(1);
	        bRad.setId(2);
	        tvVerticalSpacing2.setId(3);
	        tv2.setId(4);
	        bExit.setId(5);
	        bRad2.setId(6);
	        levelTop.setId(7);
	        levelBottom.setId(8);

	        paramsExit.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        paramsTopTv.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        paramsTopTv.setMargins(2, 110, 0, 0);
	        paramsLevelTop.addRule(RelativeLayout.BELOW, tv.getId());
	        paramsLevelTop.setMargins(12, 5, 0, 5);
	        paramsTopB.addRule(RelativeLayout.BELOW, levelTop.getId());
	        paramsTopB.setMargins(12, 5, 0, 5);
	        
	        paramsLowerTv.addRule(RelativeLayout.BELOW, bRad.getId());
	        paramsLowerTv.setMargins(2, 50, 0, 0);
	        paramsLevelBottom.addRule(RelativeLayout.BELOW, tv2.getId());
	        paramsLevelBottom.setMargins(12, 5, 0, 5);
	        paramsLowB.addRule(RelativeLayout.BELOW, levelBottom.getId());
	        paramsLowB.setMargins(12, 5, 0, 5);
	        
	        lLayoutParams.setMargins(10, 5, 5, 5); 
	        
	        rLayout.addView(bExit, paramsExit);
	        rLayout.addView(tv, paramsTopTv);
	        rLayout.addView(levelTop, paramsLevelTop);
	        rLayout.addView(bRad, paramsTopB);
	        
	        rLayout.addView(tv2, paramsLowerTv);
	        rLayout.addView(levelBottom, paramsLevelBottom);
	        rLayout.addView(bRad2, paramsLowB);
	        lLayout.addView(rLayout, lLayoutParams);
	        
	        popUp.setContentView(lLayout);
		//above is Katherine's code for the popup window
	}

	@Override
	protected void onPause() {
		super.onPause();
		ourSurfaceView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ourSurfaceView.resume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		x = event.getX();
		y = event.getY();
		int varia = 0;
		int boardSide, sidePixel = 0;
		boolean alienFound = false;
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			if(x <= playableWidth){
				alienFound = false;
				for(int i=0; i<alienXPos.length; i++){
					if(alienDetected[i]==1 && Math.abs(alienXPos[i]-x)<=50 && Math.abs(alienYPos[i]-y)<=50){
						alienDetected[i] = 0;
						boardSide = rand.nextInt(4);  //does not pick this value
						switch(boardSide){
							case 0:  //left
								sidePixel = rand.nextInt(playableHeight);
								alienXPos[i] = 0;
								alienYPos[i] = sidePixel;
								break;
							case 1:  //top
								sidePixel = rand.nextInt(playableWidth);
								alienXPos[i] = sidePixel;
								alienYPos[i] = 0;
								break;
							case 2:  //right
								sidePixel = rand.nextInt(playableHeight);
								alienXPos[i] = playableWidth;
								alienYPos[i] = sidePixel;
								break;
							case 3:  //bottom
								sidePixel = rand.nextInt(playableWidth);
								alienXPos[i] = sidePixel;
								alienYPos[i] = playableHeight;
								break;
							default:  //????  shouldn't happen...
								alienXPos[i] = 0;
								alienYPos[i] = 0;
								break;
						}
						alienDeltaX[i] = (baseXPos - alienXPos[i]) / 100;
						alienDeltaY[i] = (baseYPos - alienYPos[i]) / 100;
						alienFound = true;
					}
				}
				if(!alienFound){
					varia = getBox(x, y);  //returns i,j for spacesType[i][j] in touchedBox
					getBoxLvl();  //sets global variables boxGainLvl, boxBSLvl
				}
			} else{
				box = new Rect(screenWidth-100, screenHeight-100, screenWidth, screenHeight);
			}
			//below is Katherine
			//The below 2 lines make the popup appear. Just cut and paste them wherever you want it to be :).
			popUp.showAtLocation(mainLayout, Gravity.RIGHT, 10, 10);
            popUp.update(0, 0, screenWidth-playableWidth, screenHeight);
            if(boxGainLvl == -1){
            	levelTop.setText("Base");
            	levelBottom.setText("Base");
            	bRad.setEnabled(false);
            	bRad2.setEnabled(false);
            }
            else{
            	levelTop.setText("Level: " + boxGainLvl + "/5");
            	levelBottom.setText("Level: " + boxBSLvl + "/5");
            	bRad.setEnabled(true);
            	bRad2.setEnabled(true);
            	if(boxGainLvl < tutGainLvl){
            		changeToUpgradeButtonStyle(bRad);
            		bRad.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							spacesType[touchedBox.x][touchedBox.y]++;
							boxGainLvl++;
							levelTop.setText("Level: " + boxGainLvl + "/5");
							if(boxGainLvl >= tutGainLvl){
								popUp.dismiss();
							}
						}
					});
            	}
            	else{
            		changeToResearchButtonStyle(bRad);
            		if(boxGainLvl >= 5){
                		bRad.setText("Max Level");
            			bRad.setEnabled(false);
            		}
            		bRad.setOnClickListener(new View.OnClickListener() {  //Set onClick to go to tutorial screen
						@Override
						public void onClick(View v) {
							writeBoardFile();
							try{
								Class<?> ourClass = Class.forName("vip.purdue.spaceradardefenders.Tutorial_SRD");
								Intent ourIntent = new Intent(GFXSurface.this, ourClass);
								startActivity(ourIntent);
							}catch(ClassNotFoundException e){
								e.printStackTrace();
							}
						}
					});
            	}
            	if(boxBSLvl < tutBSLvl){
            		changeToUpgradeButtonStyle(bRad2);
            		bRad2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							spacesType[touchedBox.x][touchedBox.y]+=6;
							boxBSLvl++;
							levelBottom.setText("Level: " + boxBSLvl + "/5");
							if(boxBSLvl >= tutBSLvl){
								popUp.dismiss();
							}
						}
					});
            	}
            	else{
            		changeToResearchButtonStyle(bRad2);
            		if(boxBSLvl >= 5){
                		bRad2.setText("Max Level");
                		bRad2.setEnabled(false);
            		}
            		bRad2.setOnClickListener(new View.OnClickListener() {  //Set onClick to go to tutorial screen
						@Override
						public void onClick(View v) {
							writeBoardFile();
							try{
								Class<?> ourClass = Class.forName("vip.purdue.spaceradardefenders.Tutorial_SRD");
								Intent ourIntent = new Intent(GFXSurface.this, ourClass);
								startActivity(ourIntent);
							}catch(ClassNotFoundException e){
								e.printStackTrace();
							}
						}
					});
            	}
            }
            //above is Katherine
			if(varia == 0){
				playerTurn = false;
				cpuTurn = true;
				writeBoardFile();
			}
			break;
		case MotionEvent.ACTION_UP:
			/*while(cpuTurn){
				x = rand.nextFloat()*playableWidth;
				y = rand.nextFloat()*screenHeight;
				varia = getBox(x, y, 2);
				if(varia == 0){
					cpuTurn = false;
					playerTurn = true;
					writeBoardFile();
				}
			}*/
			break;
		}
		
		return true;
	}

	public class MyBringBackSurface extends SurfaceView implements Runnable{

		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false;
		
		public MyBringBackSurface(Context context) {
			super(context);
			ourHolder = getHolder();
		}
		
		public void pause(){
			isRunning = false;
			while(true){
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}
		
		public void resume(){
			isRunning = true;
			ourThread = new Thread(this);
			ourThread.start();
		}

		@Override
		public void run() {
			int gainLvl, bsLvl;
			Paint playerPaint = new Paint();
			Paint CPUPaint = new Paint();
			Paint sideBarPaint = new Paint();
			playerPaint.setARGB(255, 0, 255, 0);
			CPUPaint.setARGB(255, 255, 0, 0);
			sideBarPaint.setARGB(255,0,0,0);
			
			int radarWidth = radar.getWidth();
			int radarHeight = radar.getHeight();
			int baseWidth = base.getWidth();
			int baseHeight = base.getHeight();
			
			int gain1Width = gain1.getWidth();
			int gain2Width = gain2.getWidth();
			int gain3Width = gain3.getWidth();
			int gain4Width = gain4.getWidth();
			int gain5Width = gain5.getWidth();
			int gain1Height = gain1.getHeight();
			int gain2Height = gain2.getHeight();
			int gain3Height = gain3.getHeight();
			int gain4Height = gain4.getHeight();
			int gain5Height = gain5.getHeight();
			
			int antennaWidth = 60;
			int antennaHeight = 120;
			
			

			int alienWidth = alienPreDetect.getWidth();
			int alienHeight = alienPostDetect.getHeight();

			int degDelta = 0;
			
			//for determining if point is in rectangular-estimated radar area
			float[] values = new float[9];
			int matHeight = 0;
			int matWidth = 0;
			Point matPA = new Point(0,0);
			Point matPB = new Point(0,0);
			Point matPC = new Point(0,0);
			Point matPD = new Point(0,0);
			Point alienP = new Point(0,0);
			int matArea, triAreaTot = 0;
			int triArea1, triArea2, triArea3, triArea4 = 0;
			
			alienXPos = new float[]{0, 100, 500, playableWidth};
			alienYPos = new float[]{100, 0, playableHeight, 500};
			baseXPos = playableWidth / 2;
			baseYPos = playableHeight / 2;
			alienDetected = new int[]{0,0,0,0};  //0 for undetected(black), 1 for detected(white), 2 for at base(red)
			alienDeltaX = new float[]{0,0,0,0};
			alienDeltaY = new float[]{0,0,0,0};
			for(int i=0; i<alienXPos.length; i++){
				alienDeltaX[i] = (baseXPos - alienXPos[i]) / 100;
				alienDeltaY[i] = (baseYPos - alienYPos[i]) / 100;
			}
			
			
			/*
			int degrees = 0;
			RectF rectF = new RectF(spacesRect[4][3]);
			Matrix baseMatrix = new Matrix();
			baseMatrix.mapRect(rectF);
			baseMatrix.setTranslate((spacesRect[4][3].left + spacesRect[4][3].right)/2 - baseWidth/2, (spacesRect[4][3].top + spacesRect[4][3].bottom)/2 - baseHeight/2);
			*/

			while(isRunning){
				if(!ourHolder.getSurface().isValid())
					continue;
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Canvas canvas = ourHolder.lockCanvas();
				Rect source = new Rect(0, 0, background.getWidth(), background.getHeight());
				Rect dest = new Rect(0, 0, screenWidth, screenHeight);
				canvas.drawBitmap(background, source, dest, null);
				for(int i=0; i<numColumns; i++){
					for(int j=0; j<numRows; j++){
						switch(spacesType[i][j]){
							case -1:
								//playerPaint.setARGB(255,255,255,255);  //set to white for base
								//canvas.drawRect(spacesRect[i][j], playerPaint);

								canvas.drawBitmap(base, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - baseWidth/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - baseHeight/2, null);

								
								spacesMatrix[i][j].setTranslate((spacesRect[i][j].left + spacesRect[i][j].right)/2 - baseWidth/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - baseHeight/2);
								canvas.drawBitmap(base, spacesMatrix[i][j], null);
								break;
							case 0: break;  //leave blank for empty spaces
							default:
								gainLvl = spacesType[i][j] % 6;
								bsLvl = spacesType[i][j] / 6 + 1;
								//playerPaint.setARGB(255, gainLvl*51, bsLvl*51, 0);  //red is gain, green is bs
								//canvas.drawRect(spacesRect[i][j], playerPaint);

								
								switch(gainLvl){
								
								
								case 1:
									//canvas.drawBitmap(gain1, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain1Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain1Height/2, null);
									Rect antsrc1 = new Rect(0,0,antenna1.getWidth(),antenna1.getHeight());
									Rect antdest1 = new Rect((spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2,(spacesRect[i][j].top + spacesRect[i][j].bottom)/2,(spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2+antennaWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 +antennaHeight);
									canvas.drawBitmap(antenna1, antsrc1,antdest1,null);
									break;
								case 2:
									//canvas.drawBitmap(gain2, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain2Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 , null);
									Rect antsrc2 = new Rect(0,0,antenna2.getWidth(),antenna2.getHeight());
									Rect antdest2 = new Rect((spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2,(spacesRect[i][j].top + spacesRect[i][j].bottom)/2,(spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2+antennaWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 +antennaHeight);
									canvas.drawBitmap(antenna2, antsrc2,antdest2,null);
									break;
								case 3:
									//canvas.drawBitmap(gain3, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain3Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2, null);
									Rect antsrc3 = new Rect(0,0,antenna3.getWidth(),antenna3.getHeight());
									Rect antdest3 = new Rect((spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2,(spacesRect[i][j].top + spacesRect[i][j].bottom)/2-antennaHeight/3,(spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2+antennaWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 +antennaHeight*2/3);
									canvas.drawBitmap(antenna3, antsrc3,antdest3,null);
									break;
								case 4:
									//canvas.drawBitmap(gain4, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain4Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2, null);
									Rect antsrc4 = new Rect(0,0,antenna4.getWidth(),antenna4.getHeight());
									Rect antdest4 = new Rect((spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2,(spacesRect[i][j].top + spacesRect[i][j].bottom)/2-antennaHeight/3,(spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2+antennaWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 +antennaHeight*2/3);
									canvas.drawBitmap(antenna4, antsrc4,antdest4,null);
									break;
								case 5:
									//canvas.drawBitmap(gain5, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain5Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2, null);
									Rect antsrc5 = new Rect(0,0,antenna5.getWidth(),antenna5.getHeight());
									Rect antdest5 = new Rect((spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2,(spacesRect[i][j].top + spacesRect[i][j].bottom)/2-antennaHeight/2,(spacesRect[i][j].left + spacesRect[i][j].right)/2 - antennaWidth/2+antennaWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 +antennaHeight/2);
									canvas.drawBitmap(antenna5, antsrc5,antdest5,null);}
								
								switch(bsLvl){
									case 1:
										degDelta = 0;
										break;
									case 2:
										degDelta = 1;
										break;
									case 3:
										degDelta = 2;
										break;
									case 4:
										degDelta = 4;
										break;
									case 5:
										degDelta = 8;
										break;
									default:
										degDelta = 0;
										break;
								}
								
								switch(gainLvl){
									case 1:
										spacesMatrix[i][j].setTranslate((spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain1Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain1Height/2);
										spacesMatrix[i][j].postRotate(spacesDegrees[i][j]+=degDelta, (spacesRect[i][j].left + spacesRect[i][j].right)/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										canvas.drawBitmap(gain1, spacesMatrix[i][j], null);
										matHeight = gain1Height;
										matWidth = gain1Width;
										//canvas.drawBitmap(gain1, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain1Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain1Height/2, null);
										break;
									case 2:
										spacesMatrix[i][j].setTranslate((spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain2Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 );
										spacesMatrix[i][j].postRotate(spacesDegrees[i][j]+=degDelta, (spacesRect[i][j].left + spacesRect[i][j].right)/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										canvas.drawBitmap(gain2, spacesMatrix[i][j], null);
										matHeight = gain2Height;
										matWidth = gain2Width;
										//canvas.drawBitmap(gain2, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain2Width, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain2Height/2, null);
										break;
									case 3:
										spacesMatrix[i][j].setTranslate((spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain3Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										spacesMatrix[i][j].postRotate(spacesDegrees[i][j]+=degDelta, (spacesRect[i][j].left + spacesRect[i][j].right)/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										canvas.drawBitmap(gain3, spacesMatrix[i][j], null);
										matHeight = gain3Height;
										matWidth = gain3Width;
										//canvas.drawBitmap(gain3, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain3Width, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain3Height/2, null);
										break;
									case 4:
										spacesMatrix[i][j].setTranslate((spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain4Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										spacesMatrix[i][j].postRotate(spacesDegrees[i][j]+=degDelta, (spacesRect[i][j].left + spacesRect[i][j].right)/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										canvas.drawBitmap(gain4, spacesMatrix[i][j], null);
										matHeight = gain4Height;
										matWidth = gain4Width;
										//canvas.drawBitmap(gain4, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain4Width, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain4Height/2, null);
										break;
									case 5:
										spacesMatrix[i][j].setTranslate((spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain5Width/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										spacesMatrix[i][j].postRotate(spacesDegrees[i][j]+=degDelta, (spacesRect[i][j].left + spacesRect[i][j].right)/2, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2);
										canvas.drawBitmap(gain5, spacesMatrix[i][j], null);
										matHeight = gain5Height;
										matWidth = gain5Width;
										//canvas.drawBitmap(gain5, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - gain5Width, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - gain5Height/2, null);

										break;
									default:
										break;
								}

								//canvas.drawBitmap(radar, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - radarWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - radarHeight/2, null);

								
								spacesMatrix[i][j].getValues(values);
								matPA.set((int)values[2], (int)values[5]);
								matPB.set((int)(values[2] + values[0]*matWidth), (int)(values[5] + values[3]*matWidth));
								matPC.set((int)(values[2] + values[0]*matWidth - values[3]*matHeight), (int)(values[5] + values[3]*matWidth + values[0]*matHeight));
								matPD.set((int)(values[2] - values[3]*matHeight), (int)(values[5] + values[0]*matHeight));
								matArea = matWidth * matHeight;
								
								for(int k=0; k<alienXPos.length; k++){
									if(alienDetected[k] == 0){
										alienP.set((int)alienXPos[k]+alienWidth/2, (int)alienYPos[k]+alienHeight/2);
										triArea1 = (int)Math.abs((matPA.x*(matPB.y-alienP.y) + matPB.x*(alienP.y-matPA.y) + alienP.x*(matPA.y-matPB.y))/2);
										triArea2 = (int)Math.abs((matPB.x*(matPC.y-alienP.y) + matPC.x*(alienP.y-matPB.y) + alienP.x*(matPB.y-matPC.y))/2);
										triArea3 = (int)Math.abs((matPC.x*(matPD.y-alienP.y) + matPD.x*(alienP.y-matPC.y) + alienP.x*(matPC.y-matPD.y))/2);
										triArea4 = (int)Math.abs((matPD.x*(matPA.y-alienP.y) + matPA.x*(alienP.y-matPD.y) + alienP.x*(matPD.y-matPA.y))/2);
										triAreaTot = triArea1 + triArea2 + triArea3 + triArea4;
										if(triAreaTot <= (matArea*1.05)){  //give some buffer for rounding error
											alienDetected[k] = 1;
										}
									}
								}
								
								//canvas.drawBitmap(radar, (spacesRect[i][j].left + spacesRect[i][j].right)/2 - radarWidth, (spacesRect[i][j].top + spacesRect[i][j].bottom)/2 - radarHeight/2, null);

								break;
						}
					}
				
				}
				for(int i=0; i<alienXPos.length; i++){
					if(alienDetected[i] == 0){
						canvas.drawBitmap(alienPreDetect, alienXPos[i]+=alienDeltaX[i], alienYPos[i]+=alienDeltaY[i], null);
					}
					else if(alienDetected[i] == 1){
						canvas.drawBitmap(alienPostDetect, alienXPos[i]+=alienDeltaX[i], alienYPos[i]+=alienDeltaY[i], null);
					}
					else if(alienDetected[i] == 2){
						canvas.drawBitmap(alienRed, alienXPos[i]+=alienDeltaX[i], alienYPos[i]+=alienDeltaY[i], null);
					}
					
					if((Math.abs(alienXPos[i] - baseXPos) < 30) && (Math.abs(alienYPos[i] - baseYPos) < 30)){
						alienDetected[i] = 2;  //set to red for base encounter
						alienDeltaX[i] = 0;
						alienDeltaY[i] = 0;
					}
				}
				
				canvas.drawRect(sideBar, sideBarPaint);
				
				if(isOver()){
					//canvas.drawBitmap(gameover, 100, 100, null);
				}
				
				ourHolder.unlockCanvasAndPost(canvas);
			}
		}

	}
	
	private int getBox(float xin, float yin) {
		//set type to 0 for blank, 1 for player, 2 for CPU
		int xStep = playableWidth / numColumns;
		int yStep = screenHeight / numRows;
		int xr = xStep;
		int yb = yStep;
		int i = 0;
		int j = 0;
		while(xr < xin){
			i++;
			xr = xr + xStep;
		}
		while(yb < yin){
			j++;
			yb += yStep;
		}
		if(i >= numColumns)
			i = numColumns-1;
		if(j >= numRows)
			j = numRows-1;
		touchedBox.set(i,j);  //set touchedBox to which box in the grid was touched
		if(spacesType[i][j] == 0){
			spacesType[i][j] = 1;
			return 0;
		} else{
			return 1;  //return 1 for invalid press, ie space is already taken
		}
	}
	
	private void getBoxLvl(){
		int type = spacesType[touchedBox.x][touchedBox.y];
		boxGainLvl = type % 6;
		boxBSLvl = type / 6 + 1;
	}
	
	boolean isOver(){
		for(int i=0; i<numColumns; i++){
			for(int j=0; j<numRows; j++){
				if(spacesType[i][j] == 0){
					return false;
				}
			}
		}
		return true;
	}
	
	private void readTutorialFile(){
		try {
			  tutInputStream = openFileInput(tutFilename);
			  for(int i=0; i<tutorialStatus.length; i++){
				  tutorialStatus[i] = (byte) tutInputStream.read();
			  }
			  tutInputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		int j = 0;
		while(j<5){
			if(tutorialStatus[j] == 1)
				tutGainLvl = j+1;
			j++;
		}
		j = 5;
		while(j<10){
			if(tutorialStatus[j] == 1)
				tutBSLvl = j-4;
			j++;
		}
	}
	
	private void readBoardFile(){
		try {
			  boardInputStream = openFileInput(boardFilename);
			  for(int i=0; i<numColumns; i++){
				  for(int j=0; j<numRows; j++){
					  spacesType[i][j] = (byte) boardInputStream.read();
				  }
			  }
			  boardInputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}
	
	private void writeTutorialFile(){
		try {
			  tutOutputStream = openFileOutput(tutFilename, Context.MODE_PRIVATE);
			  for(int i=0; i<tutorialStatus.length; i++){
				  tutOutputStream.write(tutorialStatus[i]);
			  }
			  tutOutputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}
	
	private void writeBoardFile(){
		try {
			  boardOutputStream = openFileOutput(boardFilename, Context.MODE_PRIVATE);
			  for(int i=0; i<numColumns; i++){
				  for(int j=0; j<numRows; j++){
					  boardOutputStream.write(spacesType[i][j]);
				  }
			  }
			  boardOutputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}
	
	private void changeToUpgradeButtonStyle(Button changeToUpgrade){
		changeToUpgrade.setText(" Upgrade ");
		changeToUpgrade.setBackgroundResource(R.drawable.butoon_upgrade3);
		return;
	}
	
	private void changeToResearchButtonStyle(Button changeToResearch){
        changeToResearch.setText("Research");
        changeToResearch.setBackgroundResource(R.drawable.button_space3);
        return;
	}

	}
