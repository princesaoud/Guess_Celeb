package com.example.guessceleb;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    ArrayList<String> celeb_name;
    ArrayList<String> celeb_image;
    final String PATTERN_IMAGE = "img src=\"(.*?)\"";
    final String PATTERN_NAME = "alt=\"(.*?)\"";
    String the_solution = "";
    ImageView profile_image;

    @Override
    public void onClick(View view) {
        Button button;
        String text;
        switch (view.getId()) {
            case R.id.button:
                button = findViewById(R.id.button);
                text = (String) button.getText();
                if (text.contentEquals(the_solution))
                    Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                PlayGame();
                break;

            case R.id.button2:
                button = findViewById(R.id.button2);
                text = (String) button.getText();
                if (text.contentEquals(the_solution))
                    Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                PlayGame();
                break;

            case R.id.button3:
                button = findViewById(R.id.button3);
                text = (String) button.getText();
                if (text.contentEquals(the_solution))
                    Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                PlayGame();
                break;

            case R.id.button4:
                button = findViewById(R.id.button4);
                text = (String) button.getText();
                if (text.contentEquals(the_solution))
                    Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                PlayGame();
                break;

        }
    }

    class GettingTheImage extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                InputStream is = huc.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    class DownloadPosh extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {
                String text = "";
                URL url = new URL(urls[0]);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                InputStream is = huc.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int reading = reader.read();

                while (reading != -1) {
                    text += (char) reading;
                    reading = reader.read();
                }
                return text;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile_image = findViewById(R.id.imageView);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        DownloadPosh posh = new DownloadPosh();
        celeb_image = new ArrayList<>();

        try {
            String website = posh.execute("http://www.posh24.se/kandisar").get();
            String[] split_website = website.split("class=\"channelList\"");
            celeb_image = listOfMatches(PATTERN_IMAGE, Arrays.toString(split_website));
            celeb_name = listOfMatches(PATTERN_NAME, Arrays.toString(split_website));


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        PlayGame();
    }

    private void PlayGame() {

        try {
            GettingTheImage image = new GettingTheImage();
            Random rand = new Random();

            int solution = rand.nextInt(celeb_name.size()+1);
            the_solution = celeb_name.get(solution);

            for (int i = 1; i <= 4; i++) {
                int pos_name = new Random().nextInt(celeb_name.size() + 1);
                setButtonName(i, celeb_name.get(pos_name));
            }

            Bitmap image_to_set = image.execute(celeb_image.get(solution)).get();
            profile_image.setImageBitmap(image_to_set);
            int position = rand.nextInt(3) + 1;
            setButtonName(position, the_solution);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setButtonName(int position, String text) {
        Button button;
        switch (position) {
            case 1:
                button = findViewById(R.id.button);
                button.setText(text);
                break;

            case 2:
                button = findViewById(R.id.button2);
                button.setText(text);
                break;

            case 3:
                button = findViewById(R.id.button3);
                button.setText(text);
                break;

            case 4:
                button = findViewById(R.id.button4);
                button.setText(text);
                break;

        }
    }

    public ArrayList<String> listOfMatches(String imp_pattern, String imp_website) {
        ArrayList<String> mList = new ArrayList<>();
        Pattern pattern = Pattern.compile(imp_pattern);
        Matcher matche = pattern.matcher(imp_website);
        Log.d(TAG, "onCreate: matche = " + matche.find());
        while (matche.find()) {
            mList.add(matche.group(1));
        }
        return mList;
    }
}
