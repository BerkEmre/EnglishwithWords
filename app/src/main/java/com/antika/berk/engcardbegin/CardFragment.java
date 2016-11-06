package com.antika.berk.engcardbegin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class CardFragment extends Fragment
{
    AnimationSet animationSet;
    Animation animTurn, animTurn2, customAnim, customRoll;

    RelativeLayout cardLayout1, cardLayout2;
    Button turn;
    TextView tv1, tv2;
    ImageView iv1, iv2;
    ImageButton play_sound1, play_sound2;

    JSONArray jsonarray;

    boolean isTurn = false;
    int sayac = 0;
    String cardText1, cardImage1, cardBackText1, cardText2, cardImage2, cardBackText2;

    TTSManager ttsManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        //SET ADDMOB********************************************************************************
        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("9E20D979AA1C3F17F6D2A1FF23D1F9FF")//DEVICE ID FOR DONT BAN ADMOB
                .build();
        final InterstitialAd gecisReklam;
        gecisReklam = new InterstitialAd(getContext());
        gecisReklam.setAdUnitId(getString(R.string.admob_splash_add_key));
        gecisReklam.loadAd(adRequest);//Full Screen Add

        gecisReklam.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                gecisReklam.loadAd(adRequest);//Full Screen Add
            }
        });
        //******************************************************************************************
        //SET VIEWS*********************************************************************************
        cardLayout1  = (RelativeLayout) view.findViewById(R.id.card_layout_1);
        cardLayout2  = (RelativeLayout) view.findViewById(R.id.card_layout_2);
        turn         = (    Button    ) view.findViewById(R.id.button2      );
        tv1          = (   TextView   ) view.findViewById(R.id.textView4    );
        tv2          = (   TextView   ) view.findViewById(R.id.textView3    );
        iv1          = (   ImageView  ) view.findViewById(R.id.imageView3   );
        iv2          = (   ImageView  ) view.findViewById(R.id.imageView2   );
        play_sound1  = (ImageButton   ) view.findViewById(R.id.imageButton1 );
        play_sound2  = (ImageButton   ) view.findViewById(R.id.imageButton  );
        //******************************************************************************************

        ttsManager = new TTSManager();
        ttsManager.init(getContext());


        new Game().execute();//Execute AsynTasc for get Json Data

        cardLayout2.setVisibility(View.GONE);
        //SET ANIMATIONS****************************************************************************
        animTurn     = AnimationUtils.loadAnimation(getContext(), R.anim.turnback );
        animTurn2    = AnimationUtils.loadAnimation(getContext(), R.anim.turnback2);
        //******************************************************************************************

        turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!animTurn.hasStarted() || animTurn.hasEnded()) && (!animTurn2.hasStarted() || animTurn2.hasEnded()))
                    cardLayout1.startAnimation(animTurn);
            }
        });

        play_sound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String okunacak = tv1.getText().toString(), myString = "";
                for (char harf :
                        okunacak.toCharArray()) {
                    if (harf == '\n')
                     break;
                    myString += harf;
                }
                ttsManager.initQueue(myString);
            }
        });

        play_sound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String okunacak = tv1.getText().toString(), myString = "";
                for (char harf :
                        okunacak.toCharArray()) {
                    if (harf == '\n')
                        break;
                    myString += harf;
                }
                ttsManager.initQueue(myString);
            }
        });

        final boolean[] firstTouch = {true};
        final int[] x1 = {0};
        final int[] y1 = {0};
        final int[] x = new int[2];
        final int[] y = new int[2];
        cardLayout1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x2 = (int)event.getRawX(), y2 = (int)event.getRawY();
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                        Display display = wm.getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;
                        x[0] = (x2 - x1[0]);
                        y[0] = (y2 - y1[0]);
                        x[1] = (x2 - x1[0]);
                        y[1] = (y2 - y1[0]);
                        if(x[1] != 0 && y[0] != 0)
                        {
                            if(x[1] < 0) x[1] = x[1] * -1;
                            if (y[1] < 0) y[1] = y[1] * -1;
                            int katsayi1 = width / x[1];
                            int katsayi2 = height / y[1];
                            int gercekkatsayi = katsayi1 > katsayi2? katsayi1:katsayi2;

                            customAnim = new TranslateAnimation(0, x[0] * gercekkatsayi *2, 0, y[0] * gercekkatsayi*2);
                            customAnim.setDuration(800);
                            customAnim.setFillAfter(true);

                            if (x2 - x1[0] > 0) {
                                customRoll = new RotateAnimation(0, 90);
                            } else {
                                customRoll = new RotateAnimation(0, -90);
                            }
                            animationSet = new AnimationSet(true);
                            animationSet.addAnimation(customRoll);
                            animationSet.addAnimation(customAnim);
                            animationSet.setFillAfter(true);
                            animationSet.setDuration(1500);

                            if ((!animationSet.hasStarted() || animationSet.hasEnded()) && (!animTurn.hasStarted() || animTurn.hasEnded()) && (!animTurn2.hasStarted() || animTurn2.hasEnded()))
                            {
                                cardLayout2.startAnimation(animationSet);
                                try {nextStep();} catch (JSONException e) {e.printStackTrace();}
                            }
                        }

                        firstTouch[0] = true;
                        x1[0] = 0;
                        y1[0] = 0;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                    {
                        if(firstTouch[0])
                        {
                            x1[0] = (int)event.getRawX();
                            y1[0] = (int)event.getRawY();
                            firstTouch[0] = false;
                        }
                        break;
                    }
                }
                return true;
            }
        });

        animTurn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(isTurn == true)
                {
                    tv1.setText(cardText1);
                    isTurn = false;
                }
                else if(isTurn == false)
                {
                    tv1.setText(cardBackText1);
                    isTurn = true;
                }
                cardLayout1.startAnimation(animTurn2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return view;
    }

    protected JSONArray readCards() throws IOException, JSONException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(getString(R.string.server_url) + "/get_cards.php");
        HttpResponse response = client.execute(get);
        StatusLine status = response.getStatusLine();
        int s = status.getStatusCode();

        if(s == 200)
        {
            HttpEntity e = response.getEntity();
            String data = EntityUtils.toString(e);
            JSONArray posts = new JSONArray(data);
            return posts;
        }

        return null;
    }
    //ASYN TASK FOR GET JsonData and first input
    public class Game extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), getString(R.string.please_wait), getString(R.string.cards_loading));
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                jsonarray = readCards();
                return "1";
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                Random rand = new Random();
                int rnd = rand.nextInt(jsonarray.length()) + 0;

                try
                {
                    JSONObject obje = jsonarray.getJSONObject(rnd);
                    tv2.setText(obje.getString("Text") + "\n\n(" + obje.get("Type") + ")");
                    Picasso.with(getContext()).load(getString(R.string.server_url) + "/img/" + obje.getString("Image")).into(iv2);
                    cardBackText2 = obje.getString("BackText") + "\n\n" + obje.getString("Sentence");
                    cardText2 = obje.getString("Text") + "\n\n(" + obje.get("Type") + ")";
                    cardImage2 = obje.getString("Image");

                    rnd = rand.nextInt(jsonarray.length()) + 0;
                    obje = jsonarray.getJSONObject(rnd);
                    tv1.setText(obje.getString("Text") + "\n\n(" + obje.get("Type") + ")");
                    Picasso.with(getContext()).load(getString(R.string.server_url) + "/img/" + obje.getString("Image")).into(iv1);
                    cardBackText1 = obje.getString("BackText") + "\n\n" + obje.getString("Sentence");
                    cardText1 = obje.getString("Text") + "\n\n(" + obje.get("Type") + ")";
                    cardImage1 = obje.getString("Image");
                }
                catch (JSONException e)
                {
                    //Have Not Cards
                    Toast.makeText(getContext(), getString(R.string.havent_cards), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            else
            {
                //NOT INTERNET CONNECTION OR SERVER CONNECTION
                Toast.makeText(getContext(), getString(R.string.havent_internet), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            progressDialog.dismiss();
        }
    }

    private void nextStep() throws JSONException
    {
        cardLayout2.setVisibility(View.VISIBLE);
        isTurn = false;
        tv2.setText(tv1.getText());
        Picasso.with(getContext()).load(getString(R.string.server_url) + "/img/" + cardImage1).into(iv2);
        cardText2 = cardText1;
        cardImage2 = cardImage1;
        cardBackText2 = cardBackText1;

        Random rand = new Random();
        int rnd = rand.nextInt(jsonarray.length()) + 0;
        JSONObject obje = jsonarray.getJSONObject(rnd);

        tv1.setText(obje.getString("Text") + "\n\n(" + obje.get("Type") + ")");
        Picasso.with(getContext()).load(getString(R.string.server_url) + "/img/" + obje.getString("Image")).into(iv1);

        cardBackText1 = obje.getString("BackText") + "\n\n" + obje.getString("Sentence");
        cardText1 = obje.getString("Text") + "\n\n(" + obje.get("Type") + ")";
        cardImage1 = obje.getString("Image");
    }
}
