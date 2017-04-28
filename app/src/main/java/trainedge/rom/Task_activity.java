package trainedge.rom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Task_activity extends AppCompatActivity {
    EditText tv_info;
    Spinner spin_list;
    Button  btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_activity);
        tv_info= (EditText) findViewById(R.id.tv_info);
         spin_list= (Spinner) findViewById(R.id.spin_list);
        btn_send= (Button) findViewById(R.id.btn_send);
    }
}
