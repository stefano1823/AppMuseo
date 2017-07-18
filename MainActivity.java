package com.example.gerar.smuseum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Classe Main che gestisce il riconoscimento di un QR Code
 */
public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    /**
     *  Procedura che permette di avviare l'activity
     * @param savedInstanceState Oggetto di tipo Bundle che contiene lo stato precedentemente salvato dell'attività
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Procedura che consente, una volta premuto il bottone relativo, di avviare la fotocamera per il riconoscimento del QR Code.
     * @param w
     */
    public void onClick(View w){
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
}

    /**
     * Procedura che mette in pausa l'utilizzo della fotocamera
     */
    @Override
    protected void onPause() {
        mScannerView.stopCamera();
        super.onPause();
    }

    /**
     * Gestisce l'oggetto result restituito dal listener, inviando un messaggio di risposta.
     * @param result Oggetto da gestire
     */
    @Override
    public void handleResult(Result result) {
        Log.v("handleResult", result.getText());
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        */
        onPause();
        Intent cambio = new Intent(getApplicationContext(), Main2Activity.class);
        /*String c = result.getText().toString().substring(5,result.getText().toString().length());*/
        cambio.putExtra("handleResult", result.getText() );
        startActivity(cambio);

    }
}