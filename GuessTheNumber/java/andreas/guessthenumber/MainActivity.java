package andreas.guessthenumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText guessing;
    private int times,number;
    private TextView timesText,HintText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        times=5;
        timesText=(TextView) findViewById(R.id.times);
        timesText.setText(times+" "+getResources().getString(R.string.Times));
        HintText=(TextView) findViewById(R.id.hint);
        guessing=(EditText) findViewById(R.id.guessing);
        Random r = new Random();
        number = r.nextInt(100)+1;

    }
    public void check(View v) // Enter button
    {
        if(times>0) {

            if (guessing.getText().toString().equals(""))
            {
                Toast.makeText(this, getResources().getString(R.string.no_number), Toast.LENGTH_SHORT).show();
            }
            else if (Integer.parseInt(guessing.getText().toString()) < 1)
            {
                Toast.makeText(this, getResources().getString(R.string.less), Toast.LENGTH_SHORT).show();

            }
            else if (Integer.parseInt(guessing.getText().toString()) > 100)
            {
                Toast.makeText(this, getResources().getString(R.string.greater), Toast.LENGTH_SHORT).show();
            }
            else
                {
                times--;
                if (Integer.parseInt(guessing.getText().toString()) < number)
                {
                    HintText.setText(getResources().getString(R.string.bigger)+ " "+ Integer.parseInt(guessing.getText().toString()));
                    timesText.setText(times + " " + getResources().getString(R.string.Times));
                }
                else if (Integer.parseInt(guessing.getText().toString()) > number)
                {
                    HintText.setText(getResources().getString(R.string.smaller)+" "+ Integer.parseInt(guessing.getText().toString()));
                    timesText.setText(times + " " + getResources().getString(R.string.Times));
                }
                else if (Integer.parseInt(guessing.getText().toString()) == number)
                {
                    HintText.setText(getResources().getString(R.string.Congrats));
                    times=-1;
                }
                if(times==0)
                {
                    HintText.setText(getResources().getString(R.string.number)+" "+number);
                }


            }
            guessing.setText("");
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.restart_msg),Toast.LENGTH_SHORT).show();
        }

        }
    public void restart(View v) // Restart Button
    {
        times=5;
        timesText.setText(times+" "+getResources().getString(R.string.Times));
        Random r = new Random();
        HintText.setText("");
        guessing.setText("");
        number = r.nextInt(100)+1;
    }
}
