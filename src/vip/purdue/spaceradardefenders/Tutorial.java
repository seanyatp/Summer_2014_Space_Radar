package vip.purdue.spaceradardefenders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tutorial extends Activity{

	Button startTut, rwd1, rwd2;
	TextView title;
	int screenWidth, screenHeight;
	int quizAnswer;
	int backgroundColor = 0;
	Typeface font;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
		
        Display display = getWindowManager().getDefaultDisplay();
    	Point screenSize = new Point();
    	display.getSize(screenSize);
    	screenWidth = screenSize.x;
    	screenHeight = screenSize.y;
    	
    	final CharSequence[] quiz1choice = {"1", "2", "3", "4"};
    	final CharSequence[] quiz2choice = {"Radar", "String Theory", "Chex Mix", "Badmitton"};
    
        startTut = (Button) findViewById(R.id.begTut);
        rwd1 = (Button) findViewById(R.id.rew1);
        rwd2 = (Button) findViewById(R.id.rew2);
        title = (TextView) findViewById(R.id.tvWelcome);
    
    	font = Typeface.createFromAsset(getAssets(), "Venera-500.otf");
        startTut.setTypeface(font);
        rwd1.setTypeface(font);
        rwd2.setTypeface(font);
       
        rwd1.setEnabled(false);
        rwd2.setEnabled(false);
        
        startTut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial.this);
				LayoutInflater inflater = Tutorial.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Tutorial");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutorial_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle("The answer to this question is 3.")
							.setSingleChoiceItems(quiz1choice, 0, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									quizAnswer = which;
								}
							})
							.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Context context = getApplicationContext();
									int duration = Toast.LENGTH_SHORT;
									CharSequence text = "Error";
									if(quizAnswer == 2){
										text = "Correct!";
										rwd1.setEnabled(true);
									} else{
										text = "Incorrect!";
									}
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
						AlertDialog startQuiz = startQuizBuilder.create();
						startQuiz.show();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog startTutorial = startTutorialBuilder.create();
				startTutorial.show();
				
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(startTutorial.getWindow().getAttributes());
				lp.width = screenWidth*4/5;
				lp.height = screenHeight*4/5;
				lp.x = 0;
				lp.y = 0;
				startTutorial.getWindow().setAttributes(lp);
			}
		});
        
        rwd1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial.this);
				LayoutInflater inflater = Tutorial.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Tutorial 2");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutorial_window2, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(R.string.quest2)
							.setSingleChoiceItems(quiz2choice, 0, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									quizAnswer = which;
								}
							})
							.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Context context = getApplicationContext();
									int duration = Toast.LENGTH_SHORT;
									CharSequence text = "Error";
									if(quizAnswer == 0){
										text = "Correct!";
										rwd2.setEnabled(true);
									} else{
										text = "Incorrect!";
									}
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
						AlertDialog startQuiz = startQuizBuilder.create();
						startQuiz.show();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog startTutorial = startTutorialBuilder.create();
				startTutorial.show();
				
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(startTutorial.getWindow().getAttributes());
				lp.width = screenWidth*4/5;
				lp.height = screenHeight*4/5;
				lp.x = 0;
				lp.y = 0;
				startTutorial.getWindow().setAttributes(lp);
			}
		});
        
        rwd2.setOnClickListener(new View.OnClickListener() {
        	//View temp = findViewById(R.id.textView1);
        	View root = title.getRootView();
			@Override
			public void onClick(View v) {
				backgroundColor = (backgroundColor+1) % 5;
				if(backgroundColor == 0)
					root.setBackgroundColor(Color.WHITE);
				else if(backgroundColor == 1)
					root.setBackgroundColor(Color.RED);
				else if(backgroundColor == 2)
					root.setBackgroundColor(Color.BLUE);
				else if(backgroundColor == 3)
					root.setBackgroundColor(Color.BLACK);
				else if(backgroundColor == 4)
					root.setBackgroundColor(Color.GREEN);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
