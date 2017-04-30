package trainedge.rom;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import trainedge.rom.models.Task;

/**
 * Created by CISE on 30/04/2017.
 */

class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {

    private final Context context;
    private final List<Task> taskList;
    private final LayoutInflater inflater;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.simple_task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Task taskModel = taskList.get(position);
        holder.tvTask.setText(taskModel.getTask());
        holder.tvEmail.setText(taskModel.getEmail());
        holder.tvType.setText(taskModel.getType());
        if (taskModel.getStatus()) {
            holder.tvStatus.setText("Incomplete");
        } else {
            holder.tvStatus.setText("Complete");
        }
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle("Actions");
                dialog.setMessage("have u complete the task");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Complete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        taskModel.setStatus(true);
                        taskModel.setSeen(true);
                        DatabaseReference notification = FirebaseDatabase.getInstance().getReference("tasks_notification").child(uid).child(taskModel.getKey());
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("from", taskModel.getFrom());
                        data.put("email", taskModel.getEmail());
                        data.put("task", taskModel.getTask());
                        data.put("seen", true);
                        data.put("status", true);
                        data.put("type", taskModel.getType());
                        notification.setValue(data);
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvTask;
        TextView tvEmail;
        TextView tvType;
        TextView tvStatus;
        RelativeLayout rlContainer;

        public Holder(View itemView) {
            super(itemView);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            tvTask = (TextView) itemView.findViewById(R.id.tvTask);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            rlContainer = (RelativeLayout) itemView.findViewById(R.id.rvContainer);

        }
    }
}
