package io.github.rosariopfernandes.meuassistentepessoal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener{
    private AIConfiguration config;
    private AIService service;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = new AIConfiguration("07fb051312bc4548944e0ebfb4290c71",
                AIConfiguration.SupportedLanguages.Portuguese,//Altere esta linha de acordo com o idioma que voce escolheu na consola
                AIConfiguration.RecognitionEngine.System);
        service = AIService.getService(this, config);
        service.setListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.startListening();
            }
        });
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        // Obter os parametros caso existam
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        textView.setText("Query:" + result.getResolvedQuery() + //A frase que o utilizador usou
                "\nSpeech: " + result.getFulfillment().getSpeech() + //A resposta
                "\nParameters: " + parameterString); //Os parametros
    }

    @Override
    public void onError(AIError error) {
        textView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {}

    @Override
    public void onListeningStarted() {
        textView.setText("Escutando...");
    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {
        textView.setText("Carregando...");
    }
}
