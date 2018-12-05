package pe.josimar.tantahuilca.calificadofinal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText nombreInput;
    private EditText correoInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nombreInput = findViewById(R.id.fullname_input);
        correoInput = findViewById(R.id.correo_input);
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_send:
                sendPost();
                return true;

            default:

        }
        return super.onOptionsItemSelected(item);
    }

    private void sendPost() {
        Log.d(TAG, "sendPost()");

        String fullname = nombreInput.getText().toString();
        String correo = correoInput.getText().toString();

        if (fullname.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "currentUser: " + currentUser);

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
        DatabaseReference postRef = postsRef.push();

        Post post = new Post();
        post.setId(postRef.getKey());
        post.setFullname(fullname);
        post.setCorreo(correo);
        post.setUserid(currentUser.getUid());

        postRef.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onSuccess");
                    Toast.makeText(RegisterActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Log.e(TAG, "onFailure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void takeLocate(View view) {

    }
}
