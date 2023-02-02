// Android sample code to use an IR module 
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ConsumerIrManager mCIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCIR = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        Button button = findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCIR.hasIrEmitter()) {
                    Log.e(TAG, "No IR Emitter found\n");
                    Toast.makeText(MainActivity.this, "No IR Emitter found", Toast.LENGTH_SHORT).show();
                } else {
                    // Code to send IR signals goes here
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Code to handle IR data goes here
            }
        }
    }
}
