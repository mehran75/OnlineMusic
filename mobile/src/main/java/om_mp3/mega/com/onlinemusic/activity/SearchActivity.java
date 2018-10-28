package om_mp3.mega.com.onlinemusic.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.model.Song;
import om_mp3.mega.com.onlinemusic.server_interface.PreparePage;

public class SearchActivity extends Controller {


    private static final int SPEECH_TO_TEXT_REQUEST = 1;
    private EditText search_et;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        search_et = findViewById(R.id.search_et);

        progressBar = findViewById(R.id.progressBar);


        /* *******************************************************
         *          transparent navigation and status
         * *******************************************************/

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }


    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void speech_to_text(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "What are you searching for?");
        try {
            startActivityForResult(intent, SPEECH_TO_TEXT_REQUEST);
        } catch (ActivityNotFoundException a) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEECH_TO_TEXT_REQUEST: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search_et.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void search(View view) {

        if (search_et.getText().toString().isEmpty())
            search_et.setError(getString(R.string.empty_field_error));
        else {
            progressBar.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            new PreparePage(this).execute(search_et.getText().toString());

        }
    }

    @Override
    public void setSongList(ArrayList<Song> songs) {
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.search_btn).setVisibility(View.VISIBLE);

        if (isNetworkAvalable())
            if (songs != null && !songs.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putParcelableArrayListExtra(MainActivity.SONG_LIST, songs);
                startActivity(intent);
                finish();
            } else {
                showError("There is no result!");
            }
        else
            showError("Check your Internet Connection");


    }


    public boolean isNetworkAvalable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.OK, null)
                .create()
                .show();
    }

    public void use_offline(View view) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.OFFLINE_USE, true);

        startActivity(intent);
        finish();

    }
}
