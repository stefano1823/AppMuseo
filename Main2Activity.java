package com.example.gerar.smuseum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

/**
 * Classe main che gestisce le informazioni relative alla scheda di un'opera una volta riconosciuto il rispettivo QR Code
 */
public class Main2Activity extends Activity implements OnInitListener{
    RequestQueue requestQueue;
    String urldb = "http://onlinemuseum.altervista.org/paginaQuery.php";
    TextView risultati;
    private TextToSpeech tts;
    private Button speak;

    /**
     * Procedura che permette di avviare l'activity
     * @param savedInstanceState Oggetto di tipo Bundle che contiene lo stato precedentemente salvato dell'attività
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent val = getIntent();
        String s = val.getStringExtra("handleResult");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        tts = new TextToSpeech(this,this);
        risultati = (TextView) findViewById(R.id.textView);
        speak = (Button) findViewById(R.id.button2);

        speak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                      speakOutNow();
                                    }
                                });
        info(s);




    }

    /**
     * Metodo che riconosce il codice comunicato dal QR code,interroga la base di dati memorizzando i dati relativi all'opera e mostrandoli a video.
     * @param res Stringa contenente il codice dell'opera.
     */
    void info(final String res){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urldb, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray opere = response.getJSONArray("opere");
                    boolean flag = false;
                    int lung1 = opere.length();
                    for (int i = 0; i < lung1; i++) {
                        JSONArray opera = opere.getJSONArray(i);
                        int lung2 = opera.length();
                        for (int j = 0; j < lung2 && !flag; j++) {
                            JSONObject unaopera = opera.getJSONObject(j);
                            String cod_opera = unaopera.getString("codice_opera");
                            String titolo = unaopera.getString("nome_opera");
                            String descr = unaopera.getString("descrizione");
                            String luogo = unaopera.getString("luogo");
                            String autore = unaopera.getString("autore");
                            String pstorico = unaopera.getString("periodo_storico");
                            String tecnica = unaopera.getString("tecnica");
                            String dim = unaopera.getString("dimensioni");
                            if (res.equals(cod_opera)) {
                                flag = true;
                                String so = "TITOLO" + ":" + "\t" + titolo + "\n" + "DESCRIZIONE" + ":" + "\n" + descr + "\n" + "LUOGO" + ":" + "\t" + luogo + "\n" + "AUTORE" + ":" + "\t" + autore + "\n" + "PERIODO STORICO " + ":" + "\t" + pstorico + "\n" + "TECNICA" + ":" + "\t" + tecnica + "\n" + "DIMENSIONI" + ":" + "\t" + dim + "\n";
                                risultati.setText(so);

                            }



                        }
                    } if (!flag) {
                        risultati.append("Opera non trovata!");
                    }
                } catch (JSONException e) {
                    risultati.setText(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Metodo che consente di segnalare il completamento dell'inizializzazione del Text-To-Speech
     * @param text  Indica lo stato di questa operazione
     */
    @Override
    public void onInit(int text) {
        if (text == TextToSpeech.SUCCESS){
            int language =  tts.setLanguage(Locale.ITALIAN);
            if(language == TextToSpeech.LANG_MISSING_DATA  || language == TextToSpeech.LANG_NOT_SUPPORTED){
                speak.setEnabled(true);
                speakOutNow();
            }
        }}

    /**
     * Metodo che avvia il lettore vocale del testo
     */
    private void speakOutNow(){
        String text = risultati.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }

    /**
     * Metodo che permette di terminare l'activity.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
    }
}

