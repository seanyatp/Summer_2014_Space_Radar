package vip.purdue.spaceradardefenders;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Space extends Activity{	
	
	byte[][] startingBoard;
	byte[] startingTutorials;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		startingTutorials = new byte[]{1,0,0,0,0,/**/ 1,0,0,0,0};
		//startingTutorials = new byte[]{1,1,1,1,1,/**/ 1,1,1,1,1};  //please don't remove
			//first 5 are gain, then beam steering
			//1 is enabled, 0 is disabled
		startingBoard = new byte[][]{{0,0,0,0,0,0,0,0,0},{0,1,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,-1,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,1,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
			/* 0 for blank space, 
			 * -1 for base
			 * Else, %6 for gain level (range from 1 to 5)
			 * 		/6 +1 for beam steering level (range from 1 to 5)
			 * i.e., 14 is Gain level 2, Beam Steering level 3		
			*/
		String tutFilename = "tutorialFile";
		String boardFilename = "boardFile";
		FileOutputStream tutOutputStream;
		FileOutputStream boardOutputStream;
		try {
			  tutOutputStream = openFileOutput(tutFilename, Context.MODE_PRIVATE);
			  for(int i=0; i<startingTutorials.length; i++){
				  tutOutputStream.write(startingTutorials[i]);
			  }
			  tutOutputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		try {
			  boardOutputStream = openFileOutput(boardFilename, Context.MODE_PRIVATE);
			  for(int i=0; i<9; i++){
				  for(int j=0; j<7; j++){
					  boardOutputStream.write(startingBoard[j][i]);
				  }
			  }
			  boardOutputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		
		setContentView(R.layout.space);
		TextView txt = (TextView) findViewById(R.id.font_space);
		Button bStartGame = (Button) findViewById(R.id.bStartGame);
		Button bTutorials = (Button) findViewById(R.id.bTutorials);
		Typeface font = Typeface.createFromAsset(getAssets(), "Venera-500.otf");
		txt.setTypeface(font);
		bStartGame.setTypeface(font);
		bTutorials.setTypeface(font);
		
		bStartGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Class<?> ourClass = Class.forName("vip.purdue.spaceradardefenders.GFXSurface");
					Intent ourIntent = new Intent(Space.this, ourClass);
					startActivity(ourIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			}
		});
		
		bTutorials.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Class<?> ourClass = Class.forName("vip.purdue.spaceradardefenders.Tutorial_SRD");
					Intent ourIntent = new Intent(Space.this, ourClass);
					startActivity(ourIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			}
		});
		
		
	}

}
