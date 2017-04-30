package trainedge.rom;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Task_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static final String TAG = "TaskActivity";

    Spinner spin_list;
    Button btn_send;
    private FirebaseDatabase fb;
    private DatabaseReference myRef;
    private EditText etTask;
    private boolean selectedMember = false;
    private ArrayList<String> list;
    private ArrayList<String> listEmail;
    private Spinner spinType;
    private String uid;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_activity);
        etTask = (EditText) findViewById(R.id.etTask);
        spin_list = (Spinner) findViewById(R.id.spin_list);
        spinType = (Spinner) findViewById(R.id.spinType);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        fb = FirebaseDatabase.getInstance();
        myRef = fb.getReference("users");
        list = new ArrayList<>();
        listEmail = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listEmail);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("wait");
        dialog.show();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                listEmail.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String emailN = snapshot.child("email").getValue(String.class);
                        String uid = snapshot.getKey();
                        if (emailN.equals(email)) {
                            continue;
                        }
                        listEmail.add(emailN);
                        list.add(uid);
                        adapter.notifyDataSetChanged();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                    Toast.makeText(Task_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        spin_list.setOnItemSelectedListener(this);


        spin_list.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedMember = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        final String taskName = etTask.getText().toString().trim();
        if (taskName.isEmpty()) {
            etTask.setError("Fill task details");
        }
        int pos = spin_list.getSelectedItemPosition();
        final String userUid = list.get(pos);
        final String emailselected = listEmail.get(pos);
        if (!emailselected.isEmpty()) {

            HashMap<String, Object> task = new HashMap<>();
            task.put("for", emailselected);
            task.put("task", taskName);
            task.put("status", false);
            task.put("type", spinType.getSelectedItem().toString());
            task.put("seen", false);
            task.put("notified", false);
            FirebaseDatabase.getInstance().getReference("tasks_created").child(uid).push().setValue(task, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(Task_activity.this, "task successfully created", Toast.LENGTH_SHORT).show();
                        etTask.setText("");
                        spin_list.setSelection(0);
                        spinType.setSelection(0);

                    } else {
                        Toast.makeText(Task_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            task = new HashMap<>();
            task.put("from", uid);
            task.put("email", email);
            task.put("task", taskName);
            task.put("seen", false);
            task.put("status", false);
            task.put("type", spinType.getSelectedItem().toString());
            FirebaseDatabase.getInstance()
                    .getReference("tasks_notification")
                    .child(userUid)
                    .push()
                    .setValue(task);
        }
    }
}


