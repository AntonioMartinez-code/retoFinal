package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
public class portada extends AppCompatActivity {
    ImageView sol;

    Button btnIniciar;

    private ObjectAnimator animatorRotation;
    boolean pausa = false;
    private long animationDuration = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);


        btnIniciar = findViewById(R.id.btnIniciar);
        sol = findViewById(R.id.imgSol);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animacion();



            }
        });
    }

    public void animacion() {


                animatorRotation = ObjectAnimator.ofFloat(sol, "rotation", 0f, 360f);
                animatorRotation.setDuration(animationDuration);
                AnimatorSet animatorSetRotator = new AnimatorSet();
                animatorSetRotator.playTogether(animatorRotation);
                animatorSetRotator.start();


       //sleep();

        Intent i = new Intent(this, login.class);
        startActivity(i);
       // overridePendingTransition(R.transition.fade_in,R.transition.fade_out);
        }
        public void sleep(){
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }







