package us.mastersalud.realtimedatabase;

import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_APELLIDOS;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_GRUPO_SANGUINEO;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_NOMBRE;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_NUHSA;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuscaActivity extends AppCompatActivity {

    //Atributo nuhsa
    private String nuhsa;
    private TextView nombre,apellidos,grupoSanguineo,tvNuhsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.element_patient);

        //Recupero el String de nuhsa que me pasó la anterior activity
        nuhsa=getIntent().getStringExtra(Constantes.nuhsa);

        //Busco las vistas que tengo que modificar en mi interfaz
        nombre=findViewById(R.id.tvNombre);
        apellidos=findViewById(R.id.tvApellidos);
        grupoSanguineo=findViewById(R.id.tvGrupoSanguineo);
        tvNuhsa = findViewById(R.id.tvNuhsa);

        //Referencia al contenedor de botones del element_patient.xml
        LinearLayout btnContainerEditDelete = findViewById(R.id.btnContainerEditDelete);
        // Ocultamos el contenedor de los botones en la vista de detalle, ya que aquí he pensado no modificar ni borrar elementos.
        btnContainerEditDelete.setVisibility(View.GONE);

        //Instancia de mi base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Punto de acceso al nuhsa que se nos ha pasado
        DatabaseReference puntoAcceso = database.getReference(Constantes.pacientes + "/" + nuhsa);

        //Listener que escucha si hay algún elemento colgando de ese punto de acceso
        puntoAcceso.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si no es un nuhsa vacío (se podría controlar en la activity anterior) Y hay algo colgando..
                if(!nuhsa.isEmpty() && dataSnapshot.getValue()!=null) {//Nuhsa encontrado
                    //Recuperamos el paciente y lo mostramos aplicandole los mismos estilos que en el componente del listado, para que sea homogeneo
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);
                    // Formato de texto para Nombre
                    assert paciente != null;
                    String nombreTexto = VISUALIZACION_ETIQUETA_NOMBRE + paciente.getNombre();
                    SpannableString spannableNombre = new SpannableString(nombreTexto);
                    spannableNombre.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    nombre.setText(spannableNombre);
                    nombre.setTextSize(28);

                    // Formato de texto para Apellidos
                    String apellidosTexto = VISUALIZACION_ETIQUETA_APELLIDOS + paciente.getApellidos();
                    SpannableString spannableApellidos = new SpannableString(apellidosTexto);
                    spannableApellidos.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    apellidos.setText(spannableApellidos);
                    apellidos.setTextSize(24);

                    // Formato de texto para Grupo Sanguíneo
                    String grupoSanguineoTexto = VISUALIZACION_ETIQUETA_GRUPO_SANGUINEO + paciente.getGrupoSanguineo();
                    SpannableString spannableGrupoSanguineo = new SpannableString(grupoSanguineoTexto);
                    spannableGrupoSanguineo.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    grupoSanguineo.setText(spannableGrupoSanguineo);
                    grupoSanguineo.setTextSize(24);

                    // Formato de texto para NUHSA
                    String nuhsaTexto = VISUALIZACION_ETIQUETA_NUHSA + paciente.getNuhsa();
                    SpannableString spannableNuhsa = new SpannableString(nuhsaTexto);
                    spannableNuhsa.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvNuhsa.setText(spannableNuhsa);
                    tvNuhsa.setTextSize(24);
                }
                else{
                    //Mostramos un mensaje de no encontrado y volvemos eliminando la activity
                    Toast.makeText(BuscaActivity.this,"Paciente no encontrado",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
}
