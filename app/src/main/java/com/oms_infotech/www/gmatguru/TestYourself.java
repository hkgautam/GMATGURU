package com.oms_infotech.www.gmatguru;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class TestYourself extends AppCompatActivity {


    int time,i,correct=0,wrong=0,attempted=0;
    String node,level,total;
    RadioGroup radioGroup;
    TextView tvque;
    Firebase firebase;
    RadioButton opt1, opt2, opt3, opt4, radioButton;
    Button btnsubmit;
    TextView tvtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_yourself);

        tvtime = (TextView) findViewById(R.id.textViewtime);
        tvque = (TextView) findViewById(R.id.tvque100);

        opt1 = (RadioButton) findViewById(R.id.opt1100);
        opt2 = (RadioButton) findViewById(R.id.opt2100);
        opt3 = (RadioButton) findViewById(R.id.opt3100);
        opt4 = (RadioButton) findViewById(R.id.opt4100);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup100);
        btnsubmit = (Button) findViewById(R.id.buttonsubmit100);

        final AlertDialog.Builder alert=new AlertDialog.Builder(this);

        total = getIntent().getExtras().getString("number");
        level = getIntent().getExtras().getString("level");
        String url = getIntent().getExtras().getString("url");
        firebase = new Firebase(url);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Random r = new Random();
                node = (String) dataSnapshot.child("TotalQues").getValue();
                i = r.nextInt(Integer.valueOf(node)) + 1;

                tvque.setText((String) dataSnapshot.child("Que" + i).getValue());
                opt1.setText((String) dataSnapshot.child("Opt" + i + "1").getValue());
                opt2.setText((String) dataSnapshot.child("Opt" + i + "2").getValue());
                opt3.setText((String) dataSnapshot.child("Opt" + i + "3").getValue());
                opt4.setText((String) dataSnapshot.child("Opt" + i + "4").getValue());


                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(radioGroup.getCheckedRadioButtonId()!=-1) {
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton = (RadioButton) findViewById(selectedId);
                            String str = radioButton.getText().toString();

                            if (str.equals(dataSnapshot.child("Ans" + i).getValue())) {
                                correct = correct + 1;
                                attempted = attempted + 1;
                            } else {
                                wrong = wrong + 1;
                                attempted = attempted + 1;
                            }

                            if (attempted == Integer.valueOf(total)) {
                                btnsubmit.setText("Finish");
                                Intent intent = new Intent(TestYourself.this, FinishQuiz.class);
                                intent.putExtra("correct", correct);
                                intent.putExtra("wrong", wrong);
                                intent.putExtra("attempted", attempted);
                                intent.putExtra("total", total);
                                startActivity(intent);
                            }

                            next();
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Choose an option !", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        time=Integer.valueOf(total)*90000;
//        sel=String.valueOf(time);
//        tvtime.setText(sel);
        new CountDownTimer(time,1000){

            @Override
            public void onTick(long l) {
                int k= (int) (l/1000);
                tvtime.setText(""+k);
            }

            @Override
            public void onFinish() {
                alert.setMessage("TIME UP !!");
                alert.setCancelable(false);
                alert.setPositiveButton("View Results", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(TestYourself.this,FinishQuiz.class);
                        intent.putExtra("correct",correct);
                        intent.putExtra("wrong",wrong);
                        intent.putExtra("attempted",attempted);
                        intent.putExtra("total",total);
                        startActivity(intent);
                        finish();
                    }
                });


                AlertDialog alertDialog=alert.create();
                alertDialog.show();

            }
        }.start();
    }
    void next() {
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Random r = new Random();
                node= (String) dataSnapshot.child("TotalQues").getValue();
                i = r.nextInt(Integer.valueOf(node)) + 1;
                tvque.setText((String) dataSnapshot.child("Que" + i).getValue());
                opt1.setText((String) dataSnapshot.child("Opt" + i + "1").getValue());
                opt2.setText((String) dataSnapshot.child("Opt" + i + "2").getValue());
                opt3.setText((String) dataSnapshot.child("Opt" + i + "3").getValue());
                opt4.setText((String) dataSnapshot.child("Opt" + i + "4").getValue());

                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(radioGroup.getCheckedRadioButtonId()!=-1) {
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton = (RadioButton) findViewById(selectedId);
                            String str = radioButton.getText().toString();

                            if (str.equals(dataSnapshot.child("Ans" + i).getValue())) {
                                correct = correct + 1;
                                attempted = attempted + 1;
                            } else {
                                wrong = wrong + 1;
                                attempted = attempted + 1;
                            }
                            if (attempted == Integer.valueOf(total) - 1) {
                                btnsubmit.setText("Finish");
                            }
                            if (attempted == Integer.valueOf(total)) {
                                Intent intent = new Intent(TestYourself.this, FinishQuiz.class);
                                intent.putExtra("correct", correct);
                                intent.putExtra("wrong", wrong);
                                intent.putExtra("total", total);
                                startActivity(intent);
                            }
                            next();
                        }else {
                            Toast.makeText(getApplicationContext(), "Choose an option !", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
