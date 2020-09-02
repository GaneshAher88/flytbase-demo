package com.ganeshaher.flytbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.ganeshaher.flytbase.AppConstant.MYPREFERENCES;


public class MainActivity extends AppCompatActivity {

    EditText get_value;
    TextView answer_text;
    Button result_button;
    RecyclerView calculation_recylcer;
    ArrayList<Calculed>resultlist;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        resultlist=new ArrayList<>();
        get_value=findViewById(R.id.get_value_id);
        calculation_recylcer=findViewById(R.id.calculation_recylcer_id);
        answer_text=findViewById(R.id.answer_text_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        calculation_recylcer.setLayoutManager(linearLayoutManager);
        result_button=findViewById(R.id.result_button_id);
        result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  checkMultiplication(get_value.getText().toString().trim());
                String input= reformString(get_value.getText().toString().trim());

                if (input.isEmpty())
                {
                    get_value.setError("Enter expression");
                }
                else
                {
                    double result= evaluate(input);
                    resultlist.add(new Calculed(input,String.valueOf(result)));
                    answer_text.setText("Answer : "+String.valueOf(result));
                    CalculationItemAdapter calculationItemAdapter = new CalculationItemAdapter(MainActivity.this, resultlist);
                    calculation_recylcer.setAdapter(calculationItemAdapter);
                }



            }
        });
    }



    public  double evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();
        // For numbers
        Stack<Double> number_values = new Stack<Double>();
        // For Operators
        Stack<Character> ops_values = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {

            //add numbers here
            if (tokens[i] >= '0' && tokens[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                number_values.push(Double.parseDouble(sbuf.toString()));
            }

            // add operators here
            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/')
            {

                while (!ops_values.empty() && hasPrecedence(tokens[i], ops_values.peek()))
                    number_values.push(applyOpration(ops_values.pop(), number_values.pop(), number_values.pop()));

                ops_values.push(tokens[i]);
            }
        }

        //apply operation here
        while (!ops_values.empty())
            number_values.push(applyOpration(ops_values.pop(), number_values.pop(), number_values.pop()));

        // Here toppest values contains result, return it
        return number_values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public  boolean hasPrecedence(char op1, char op2)
    {
        // op1=letest , op2=previous
        if (op2=='*' && (op1 == '+' || op1 == '-' || op1 == '/')) { return true; }
        else if (op2=='+' && (op1 == '/' || op1 == '-')) { return true;}
        else if (op2=='/' && (op1 == '-')) { return true;}
        else if (op2=='-' && ( !String.valueOf(op1).equals("+") || !String.valueOf(op1).equals("*") || !String.valueOf(op1).equals("/"))) { if (op1=='*' || op2=='/' || op2=='+') { return false; } else { return true; } }
        else if (op2=='/' && op1=='/') { return true;}
        else if (op2=='*' && op1=='*') { return true;}
        else if (op2=='+' && op1=='+') { return true; }
        else if (op2=='-' && op1=='-') { return true;}
        else { return false;}

    }


    public  double applyOpration(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }


    private String reformString(String input_string)
    {

        String result_string = "";

        for(int i=0;i<input_string.length();i++) {
            char result=input_string.charAt(i);
            if(result=='+' || result=='/' || result=='-' || result=='*') { result_string=result_string+" "+String.valueOf(result)+" "; } else { result_string=result_string+String.valueOf(result); }
        }
        return result_string;
    }


    private class CalculationItemAdapter extends RecyclerView.Adapter<CalculationItemAdapter.MyHolder1>
    {

        private List<Calculed> calculation_list = new ArrayList<>();
        Context context;

        public CalculationItemAdapter(Context context,List<Calculed> calculation_list)
        {
            this.context=context;
            this.calculation_list=calculation_list;

        }

        @NonNull
        @Override
        public CalculationItemAdapter.MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calculated_item, parent, false);
            CalculationItemAdapter.MyHolder1 holder = new CalculationItemAdapter.MyHolder1(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final CalculationItemAdapter.MyHolder1 holder, int position)
        {

            final Calculed calculed=calculation_list.get(position);
            holder.question.setText("Que "+String.valueOf(position+1)+". "+calculed.getQue());
            holder.answer.setText("Ans : "+calculed.getAns());


        }



        @Override
        public int getItemCount() {
            return calculation_list.size();
        }

        public class MyHolder1 extends RecyclerView.ViewHolder
        {

            TextView question,answer;


            // EditText amount;
            public MyHolder1(View itemView) {
                super(itemView);

                question=itemView.findViewById(R.id.question_id);
                answer=itemView.findViewById(R.id.answer_id);


            }//constructor


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AppConstant.NAME, "");
            editor.putString(AppConstant.PASSWORD, "");
            editor.commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle(getString(R.string.exit_app))
                .setMessage(getString(R.string.msg31))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}