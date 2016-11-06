package com.antika.berk.engcardbegin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
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

public class FillBlankFragment extends Fragment
{
    InterstitialAd gecisReklam;
    AdRequest adRequest;

    JSONArray jsonarray;

    Button berk_a, berk_b, berk_c, berk_d;

    RelativeLayout cardLayout1, cardLayout2;
    TextView tv1, tv2;
    ImageView iv1, iv2;
    ImageButton ib1, ib2;
    Animation animSlide, animTurn, animTurn2;

    String question, image;
    boolean isTurn = false;
    int sayac = 0;

    int dogru;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        //SET ADDMOB********************************************************************************
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("9E20D979AA1C3F17F6D2A1FF23D1F9FF")//DEVICE ID FOR DONT BAN ADMOB
                .build();
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

        berk_a      = ( Button )       view.findViewById( R.id.button3    );
        berk_b      = ( Button )       view.findViewById( R.id.button4    );
        berk_c      = ( Button )       view.findViewById( R.id.button5    );
        berk_d      = ( Button )       view.findViewById( R.id.button6    );
        cardLayout1 = (RelativeLayout) view.findViewById(R.id.card_layout1);
        cardLayout2 = (RelativeLayout) view.findViewById(R.id.card_layout2);
        tv1         = (TextView)       view.findViewById(R.id.textView6   );
        tv2         = (TextView)       view.findViewById(R.id.textView7   );
        iv1         = (ImageView)      view.findViewById(R.id.imageView5  );
        iv2         = (ImageView)      view.findViewById(R.id.imageView4  );
        ib1         = (ImageButton)    view.findViewById(R.id.imageButton3);
        ib2         = (ImageButton)    view.findViewById(R.id.imageButton2);

        animSlide = AnimationUtils.loadAnimation(getContext(), R.anim.slide    );
        animTurn  = AnimationUtils.loadAnimation(getContext(), R.anim.turnback );
        animTurn2 = AnimationUtils.loadAnimation(getContext(), R.anim.turnback2);

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!animSlide.hasStarted() || animSlide.hasEnded()) && (!animTurn.hasStarted() || animTurn.hasEnded()) && (!animTurn2.hasStarted() || animTurn2.hasEnded()))
                    cardLayout1.startAnimation(animTurn);
            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!animSlide.hasStarted() || animSlide.hasEnded()) && (!animTurn.hasStarted() || animTurn.hasEnded()) && (!animTurn2.hasStarted() || animTurn2.hasEnded()))
                    cardLayout1.startAnimation(animTurn);
            }
        });

        animTurn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(isTurn == true)
                {
                    tv1.setText(question);
                    iv1.setImageBitmap(null);
                    tv2.setText(question);
                    iv2.setImageBitmap(null);
                    isTurn = false;
                }
                else if(isTurn == false)
                {
                    tv1.setText("");
                    Picasso.with(getContext()).load(getString(R.string.server_url) + "/img/" + image).into(iv1);
                    tv2.setText("");
                    Picasso.with(getContext()).load(getString(R.string.server_url) + "/img/" + image).into(iv2);
                    isTurn = true;
                }
                cardLayout1.startAnimation(animTurn2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animSlide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardLayout2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardLayout2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        new GetTests().execute();
        cardLayout2.setVisibility(View.GONE);

        berk_a.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                nextStep(0, berk_a);
            }
        });
        berk_b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                nextStep(1, berk_b);
            }
        });
        berk_c.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                nextStep(2, berk_c);
            }
        });
        berk_d.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                nextStep(3, berk_d);
            }
        });

        return view;
    }

    private void setTest()
    {
        try
        {
            Random rand = new Random();
            int rnd = rand.nextInt(jsonarray.length()) + 0;
            JSONObject obje = jsonarray.getJSONObject(rnd);
            String mytext = obje.getString("Sentence").toLowerCase().replace(obje.getString("Text").toLowerCase(), "______");
            tv1.setText(mytext);
            tv2.setText(mytext);
            question = mytext;
            image = obje.getString("Image");
            iv1.setImageBitmap(null);
            iv2.setImageBitmap(null);

            dogru = rnd%4;
            if(rnd%4 == 0) berk_a.setText(obje.getString("Text"));
            else berk_a.setText(jsonarray.getJSONObject((rnd + 1) % jsonarray.length()).getString("Text"));
            if(rnd%4 == 1) berk_b.setText(obje.getString("Text"));
            else berk_b.setText(jsonarray.getJSONObject((rnd + 2) % jsonarray.length()).getString("Text"));
            if(rnd%4 == 2) berk_c.setText(obje.getString("Text"));
            else berk_c.setText(jsonarray.getJSONObject((rnd + 3) % jsonarray.length()).getString("Text"));
            if(rnd%4 == 3) berk_d.setText(obje.getString("Text"));
            else berk_d.setText(jsonarray.getJSONObject((rnd + 4) % jsonarray.length()).getString("Text"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void nextStep(int sira, Button btn)
    {
        TransitionDrawable trans;
        if(dogru == sira)
        {
            Drawable backgrounds[] = new Drawable[2];
            Resources res = getResources();
            backgrounds[0] = res.getDrawable(R.drawable.btn_true);
            backgrounds[1] = res.getDrawable(R.drawable.button_background);
            trans = new TransitionDrawable(backgrounds);
            btn.setBackground(trans);
            trans.startTransition(1000); // duration 3 seconds
        }
        else
        {
            Drawable backgrounds1[] = new Drawable[2];
            Resources res = getResources();
            backgrounds1[0] = res.getDrawable(R.drawable.btn_false);
            backgrounds1[1] = res.getDrawable(R.drawable.button_background);

            Button[] btns = {berk_a, berk_b, berk_c, berk_d};
            trans = new TransitionDrawable(backgrounds1);
            btn.setBackground(trans);
            trans.startTransition(1000); // duration 3 seconds

            Drawable backgrounds2[] = new Drawable[2];
            backgrounds2[0] = res.getDrawable(R.drawable.btn_true);
            backgrounds2[1] = res.getDrawable(R.drawable.button_background);
            trans = new TransitionDrawable(backgrounds2);
            btns[dogru].setBackground(trans);
            trans.startTransition(1000); // duration 3 seconds
        }
        sayac++;
        if(sayac == Integer.parseInt(getString(R.string.splash_add_duration)))
        {
            sayac = 0;
            if (gecisReklam.isLoaded())
                gecisReklam.show();
        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if((!animSlide.hasStarted() || animSlide.hasEnded()) && (!animTurn.hasStarted() || animTurn.hasEnded()) && (!animTurn2.hasStarted() || animTurn2.hasEnded()))
                {
                    cardLayout2.startAnimation(animSlide);
                    setTest();
                    isTurn = false;
                }
            }
        }, 1000);
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

    public class GetTests extends AsyncTask<String, String, String>
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
            if(!s.equals("1"))
            {
                //NOT INTERNET CONNECTION OR SERVER CONNECTION
                Toast.makeText(getContext(), getString(R.string.havent_internet), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            setTest();
            progressDialog.dismiss();
        }
    }
}
