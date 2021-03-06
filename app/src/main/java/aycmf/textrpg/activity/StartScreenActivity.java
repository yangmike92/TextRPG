package aycmf.textrpg.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import aycmf.textrpg.R;
import aycmf.textrpg.TextRPGApplication;
import aycmf.textrpg.view.StatusView;

public class StartScreenActivity extends AppCompatActivity {
    private Button startscreen_continue;
    private Button startscreen_newgame;
    private Button startscreen_achievements;
    private Button startscreen_settings;
    private Button startscreen_about;
    private Button startscreen_reset;
    private StatusView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextRPGApplication app = (TextRPGApplication) getApplicationContext();
        final Resources res = getResources();
        Typeface munro = Typeface.createFromAsset(getAssets(),"munro.ttf");

        setContentView(R.layout.activity_start_screen);
        if (app.hasExistingGame()) {
            app.loadGame();
        }

        statusView = (StatusView) findViewById(R.id.statusView);
        statusView.updateStatus();

        startscreen_continue = (Button) findViewById(R.id.startscreen_continue);
        startscreen_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueGame();
            }
        });
        startscreen_continue.setTypeface(munro);

        startscreen_newgame = (Button) findViewById(R.id.startscreen_newgame);
        startscreen_newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        startscreen_newgame.setTypeface(munro);

        startscreen_achievements = (Button) findViewById(R.id.startscreen_achievements);
        startscreen_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartScreenActivity.this, AchievementActivity.class);
                startActivity(i);
                                /*
                clearGame();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
            }
        });
        startscreen_achievements.setTypeface(munro);

        startscreen_about = (Button) findViewById(R.id.startscreen_about);
        startscreen_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartScreenActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        startscreen_about.setTypeface(munro);
        //startscreen_settings = (Button) findViewById(R.id.startscreen_settings);

        startscreen_reset = (Button) findViewById(R.id.startscreen_reset);
        startscreen_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGame();
                setButtonState();
                statusView.updateStatus();
            }
        });

        setButtonState();
    }

    public void continueGame(){
        Intent i = new Intent(StartScreenActivity.this, MainActivity.class);
        startActivity(i);
        //Toast.makeText(StartScreenActivity.this,app.getModelContainer().getCharacter().getName()+" with STR: " + app.getModelContainer().getCharacter().getStrength() + " DEX: " + app.getModelContainer().getCharacter().getDexterity() + " and such...",Toast.LENGTH_LONG).show();
    }

    public void newGame(){
        final TextRPGApplication app = (TextRPGApplication) getApplicationContext();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);//StartScreenActivity.this
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.character_creation_dialog, null);
        final AppCompatEditText characterName = (AppCompatEditText) dialogView.findViewById(R.id.characterName);
        final RadioGroup characterIcon = (RadioGroup) dialogView.findViewById(R.id.characterIcon);
        ((RadioButton) characterIcon.getChildAt(0)).setChecked(true);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Character Creation");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.setNegativeButton(android.R.string.cancel, null);
        final AlertDialog alert = dialogBuilder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (characterName.getText().toString().trim().length() == 0) {
                            characterName.setError("Character name is required.");
                        } else {
                            int characterIconID = 1;
                            int button = characterIcon.getCheckedRadioButtonId();
                            switch (button) {
                                case R.id.male:
                                    characterIconID = 1;
                                    break;
                                case R.id.female:
                                    characterIconID = 2;
                                    break;
                            }
                            app.newGame(characterName.getText().toString().trim(), characterIconID);
                            app.saveGame();
                            //below function call is only for testing purposes. Won't be needed once the GameActivity is implemented
                            setButtonState();
                            statusView.updateStatus();
                            //continueGame(true, characterName.getText().toString());
                            alert.dismiss();
                        }
                    }
                });
            }
        });
        alert.show();
        //alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    public void clearGame(){
        final TextRPGApplication app = (TextRPGApplication) getApplicationContext();
        app.clearGame();
    }

    private void setButtonState() {
        final TextRPGApplication app = (TextRPGApplication) getApplicationContext();
        startscreen_continue.setEnabled(app.hasExistingGame());
        startscreen_newgame.setEnabled(true);
    }
}
