package com.example.zombies8mymom.scarnesdice;

import android.app.Activity;
import android.os.Handler;
import java.lang.Thread;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    //Not sure how to do #define in java, so:
    public static final int NOCHANGE = -1;

    public final int NUM_DICE_FACES = 6;

    public int g_user_total_score = 0;
    public int g_user_turn_score = 0;
    public int g_computer_total_score = 0;
    public int g_computer_turn_score = 0;
    public boolean g_user_turn = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public int get_random_number(int maxsize)
    {
        Random rand = new Random();
        int random_dice_number = rand.nextInt(maxsize) + 1;
        return random_dice_number;
    }

    public void reset_score()
    {
        g_user_turn_score = 0;
        g_computer_turn_score = 0;
        TextView layout = (TextView) findViewById(R.id.edit_message);
        layout.setText("User score: " + Integer.toString(g_user_total_score) + " Computer score: " + Integer.toString(g_computer_total_score));
    }

    public void roll(View view)
    {
        if (!g_user_turn)
            return;
        int random_dice_number = get_random_number(NUM_DICE_FACES);
        change_dice_face(view, random_dice_number);
        if (random_dice_number <= 6 && random_dice_number > 1)
        {
            increment_score(random_dice_number, NOCHANGE);
        }
        else if(random_dice_number == 1)
        {
            reset_score();
            computer_turn(view);
        }
    }

    public void increment_score(int user_score, int computer_score)
    {
        int user_current_score = g_user_total_score;
        int computer_current_score = g_computer_total_score;
        if (user_score != NOCHANGE)
        {
            g_user_turn_score += user_score;
            user_current_score += g_user_turn_score;
        }
        if (computer_score != NOCHANGE)
        {
            g_computer_turn_score += computer_score;
            computer_current_score += g_computer_turn_score;
        }
        TextView layout = (TextView) findViewById(R.id.edit_message);
        layout.setText("User score: " + Integer.toString(user_current_score) + " Computer score: " + Integer.toString(computer_current_score));
        if(user_current_score >= 100)
        {
            layout.setText("You win!");
            reset_game();
        }
        if(computer_current_score >= 100)
        {
            layout.setText("You lose.");
            reset_game();
        }

    }

    public void reset_game(View view) {
        reset_game();
    }

    public void reset_game()
    {
        if(!g_user_turn)
            return;
        g_user_total_score = 0;
        g_computer_total_score = 0;
        reset_score();
    }

    public void hold_user(View view)
    {
        if(!g_user_turn)
            return;
        hold(view);
        computer_turn(view);
    }

    public void hold(View view)
    {
        g_user_total_score += g_user_turn_score;
        g_computer_total_score += g_computer_turn_score;
        g_user_turn_score = 0;
        g_computer_turn_score = 0;
    }


    public void computer_turn(View view)
    {
        g_user_turn = false;
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                View view = findViewById(R.id.edit_message);
                int random_dice_number = get_random_number(NUM_DICE_FACES);

                change_dice_face(view, random_dice_number);

                if(random_dice_number<=6&&random_dice_number>1) {
                    increment_score(NOCHANGE, random_dice_number);
                    if (g_computer_turn_score >= 20) {
                        hold(view);
                        g_user_turn=true;
                    } else {
                        handler.postDelayed(this, 1000);
                    }
                }
                else if(random_dice_number==1)
                {
                    reset_score();
                    g_user_turn=true;
                }

            }
        };
        handler.postDelayed(run, 1000);
    }

    public void change_dice_face(View view, int dice_number)
    {
        ImageView layout = (ImageView) findViewById(R.id.dice);
        switch(dice_number)
        {
            case 1:
                layout.setImageResource(R.drawable.dice1);
                break;
            case 2:
                layout.setImageResource(R.drawable.dice2);
                break;
            case 3:
                layout.setImageResource(R.drawable.dice3);
                break;
            case 4:
                layout.setImageResource(R.drawable.dice4);
                break;
            case 5:
                layout.setImageResource(R.drawable.dice5);
                break;
            case 6:
                layout.setImageResource(R.drawable.dice6);
                break;
            default:
                break;

        }

    }
}
