package us.mastersalud.realtimedatabase;

import static us.mastersalud.realtimedatabase.Constantes.REFERENCE_FIREBASE_PACIENTES;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaPacientes extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PacienteAdapter pacienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycler_view);

        // Configuracion del RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPatients);
        //Configuracion Layout recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Conexión con la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(REFERENCE_FIREBASE_PACIENTES);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Creamos una nueva lista de pacientes
                List<Paciente> pacienteList = new ArrayList<>();

                // Iteramos sobre los datos recibidos y añadimos cada paciente a la lista
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtenemos los datos del paciente
                    Paciente paciente = snapshot.getValue(Paciente.class);
                    pacienteList.add(paciente);
                }
                // Notificamos al Adapter que los datos han cambiado y actualizamos la vista
                pacienteAdapter = new PacienteAdapter(pacienteList);
                recyclerView.setAdapter(pacienteAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}
