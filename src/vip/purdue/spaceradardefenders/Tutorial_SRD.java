package vip.purdue.spaceradardefenders;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Tutorial_SRD extends Activity{

	Button gain1, gain2, gain3, gain4;//, gain5;
	Button bs1, bs2, bs3, bs4;//,  bs5;
	Button ret2game;
	TextView title;
	int screenWidth, screenHeight;
	int quizAnswer;
	int backgroundColor = 0;
	Typeface font;
	
	byte[] tutorialStatus;  //1 for complete, 0 for incomplete
	String tutFilename;
	FileOutputStream tutOutputStream;
	FileInputStream tutInputStream;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorials_srd);
        tutFilename = "tutorialFile";
        tutorialStatus = new byte[10];
        readTutorialFile();
		
        Display display = getWindowManager().getDefaultDisplay();
    	Point screenSize = new Point();
    	display.getSize(screenSize);
    	screenWidth = screenSize.x;
    	screenHeight = screenSize.y;
    	
    	final CharSequence[] gain1choice = {"Onmi-Directional or Isotropic Radiator.", "One Direction", "Unidirectional", "Radiator"};
    	final CharSequence[] gain2choice = {"It looks cool", "Reflects a signal in a given direction.", "Protects the antenna from rain", "Decrease the gain"};
    	final CharSequence[] gain3choice = {"A solar panel", "An antenna cohort", "An antenna array.", "Large-scale radiator"};
    	final CharSequence[] gain4choice = {"Increase the dimension of the antenna or reflector", "Add more elements to an array", "Use a reflector or other structure to direct the radiation", "Use FR4."};
    	//final CharSequence[] gain5choice = {"Choice 1", "Choice 2", "Choice 3", "Choice 4"};
    	final CharSequence[] bs1choice = {"Physically move the radiator.", "You can't", "Reduce the gain of the antenna", "Put tin foil on it"};
    	final CharSequence[] bs2choice = {"It is considered a bad antenna", "It can only look at 1/1000 of its surroundings at any point.", "It can scan everywhere at the same time", "Frii's formula does not apply"};
    	final CharSequence[] bs3choice = {"They cannot", "By increasing the output power", "By changing the phases of individual signals.", "Filtering the signal with a flux capacitor"};
    	final CharSequence[] bs4choice = {"True.", "False", "Only on weekends", "This is not a valid choice"};
    	//final CharSequence[] bs5choice = {"Choice 1", "Choice 2", "Choice 3", "Choice 4"};
    
    	final int[] quizAnswers = {0,1,2,3,0,0,1,2,0,0}; //numbers from 0-3
    	
        gain1 = (Button) findViewById(R.id.bgain1);
        gain2 = (Button) findViewById(R.id.bgain2);
        gain3 = (Button) findViewById(R.id.bgain3);
        gain4 = (Button) findViewById(R.id.bgain4);
        //gain5 = (Button) findViewById(R.id.bgain5);
        bs1 = (Button) findViewById(R.id.bbs1);
        bs2 = (Button) findViewById(R.id.bbs2);
        bs3 = (Button) findViewById(R.id.bbs3);
        bs4 = (Button) findViewById(R.id.bbs4);
        //bs5 = (Button) findViewById(R.id.bbs5);
        title = (TextView) findViewById(R.id.tvRadarTut);
        ret2game = (Button) findViewById(R.id.bret2game);
    
    	font = Typeface.createFromAsset(getAssets(), "Venera-500.otf");
    	title.setTypeface(font);
        gain1.setTypeface(font);
        gain2.setTypeface(font);
        gain3.setTypeface(font);
        gain4.setTypeface(font);
        //gain5.setTypeface(font);
        bs1.setTypeface(font);
        bs2.setTypeface(font);
        bs3.setTypeface(font);
        bs4.setTypeface(font);
        //bs5.setTypeface(font);
        ret2game.setTypeface(font);
       
        if(tutorialStatus[0] == 0)
        	gain1.setEnabled(false);
        if(tutorialStatus[1] == 0)
        	gain2.setEnabled(false);
        if(tutorialStatus[2] == 0)
        	gain3.setEnabled(false);
        if(tutorialStatus[3] == 0)
        	gain4.setEnabled(false);
        //if(tutorialStatus[4] == 0)
        	//gain5.setEnabled(false);
        if(tutorialStatus[5] == 0)
        	bs1.setEnabled(false);
        if(tutorialStatus[6] == 0)
        	bs2.setEnabled(false);
        if(tutorialStatus[7] == 0)
        	bs3.setEnabled(false);
        if(tutorialStatus[8] == 0)
        	bs4.setEnabled(false);
        //if(tutorialStatus[9] == 0)
        	//bs5.setEnabled(false);
        
        ret2game.setEnabled(true);
        
        gain1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Gain Level 1");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutgain1_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(getString(R.string.qgain1))
							.setSingleChoiceItems(gain1choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[0]){
										text = "Correct!";
										gain2.setEnabled(true);
										tutorialStatus[1] = 1;
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
        
        gain2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Gain Level 2");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutgain2_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(R.string.qgain2)
							.setSingleChoiceItems(gain2choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[1]){
										text = "Correct!";
										gain3.setEnabled(true);
										tutorialStatus[2] = 1;
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
        
        gain3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Gain Level 3");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutgain3_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(R.string.qgain3)
							.setSingleChoiceItems(gain3choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[2]){
										text = "Correct!";
										gain4.setEnabled(true);
										tutorialStatus[3] = 1;
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
        
        gain4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Gain Level 4");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutgain4_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(R.string.qgain4)
							.setSingleChoiceItems(gain4choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[3]){
										text = "Correct!";
										//gain5.setEnabled(true);
										tutorialStatus[4] = 1;
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
        
        /*
        gain5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Gain Level 5");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutgain5_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(R.string.qgain5)
							.setSingleChoiceItems(gain5choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[4]){
										text = "Correct!";
										//gain6.setEnabled(true);
										//tutorialStatus[5] = 1;
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
		*/
        
        bs1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Beam Steering Level 1");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutbs1_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(getString(R.string.qbs1))
							.setSingleChoiceItems(bs1choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[5]){
										text = "Correct!";
										bs2.setEnabled(true);
										tutorialStatus[6] = 1;
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
        
        bs2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Beam Steering Level 2");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutbs2_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(getString(R.string.qbs2))
							.setSingleChoiceItems(bs2choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[6]){
										text = "Correct!";
										bs3.setEnabled(true);
										tutorialStatus[7] = 1;
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
        
        bs3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Beam Steering Level 3");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutbs3_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(getString(R.string.qbs3))
							.setSingleChoiceItems(bs3choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[7]){
										text = "Correct!";
										bs4.setEnabled(true);
										tutorialStatus[8] = 1;
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
        
        bs4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Beam Steering Level 4");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutbs4_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(getString(R.string.qbs4))
							.setSingleChoiceItems(bs4choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[8]){
										text = "Correct!";
										//bs5.setEnabled(true);
										tutorialStatus[9] = 1;
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
        
        /*
        bs5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder startTutorialBuilder = new Builder(Tutorial_SRD.this);
				LayoutInflater inflater = Tutorial_SRD.this.getLayoutInflater();
				
				startTutorialBuilder.setTitle("Beam Steering Level 5");
				startTutorialBuilder.setView(inflater.inflate(R.layout.tutbs5_window, null));
				
				startTutorialBuilder.setPositiveButton(R.string.quiz, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Builder startQuizBuilder = new Builder(Tutorial_SRD.this);
						quizAnswer = 0;
						startQuizBuilder.setTitle(getString(R.string.qbs5))
							.setSingleChoiceItems(bs5choice, 0, new DialogInterface.OnClickListener() {
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
									if(quizAnswer == quizAnswers[9]){
										text = "Correct!";
										//bs6.setEnabled(true);
										//tutorialStatus[10] = 1;
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
		*/
        
        ret2game.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				writeTutorialFile();
				try{
					Class<?> ourClass = Class.forName("vip.purdue.spaceradardefenders.GFXSurface");
					Intent ourIntent = new Intent(Tutorial_SRD.this, ourClass);
					startActivity(ourIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
	}
}
