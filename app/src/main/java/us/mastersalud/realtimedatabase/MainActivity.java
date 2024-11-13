package us.mastersalud.realtimedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Atributos de la clase:
    //EditText para el texto de búsqueda de paciente por Nuhsa
    EditText ETnuhsa;
    //Atributos para la base de datos de Firebase
    FirebaseDatabase database;
    //y el punto de acceso del fichero JSON
    DatabaseReference puntoAcceso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lo único que voy a hacer es recuperar de la interfaz el EditText
        ETnuhsa=findViewById(R.id.editText);
    }

    //OnClick asignado desde el Layout
    //En vez de butonBusca.setOnClickListener...
    public void buscaPaciente(View view) {
        //Recupero la cadena de texto del EditText
        String nuhsa= ETnuhsa.getText().toString();
        //Voy a lanzar la activity BuscaActivity con  el parámetro del nuhsa recuperado mediante putExtra

        Intent intent=new Intent(MainActivity.this,BuscaActivity.class);
        intent.putExtra(Constantes.nuhsa,nuhsa);
        startActivity(intent);
    }
    //OnClick asignado desde el Layout
    public void insertaPaciente(View view) {
        //Genero un paciente aleatorio
        Paciente paciente=generaPacienteAleatorio();

        //Recupero una instancia de mi base de datos
        database=FirebaseDatabase.getInstance();
        //Referencio a la raíz
        puntoAcceso =database.getReference();
        //Navego con child y establezco como valor el objeto paciente ¡directamente!
        puntoAcceso.child(Constantes.pacientes).child(paciente.getNuhsa()).setValue(paciente);
        //Ahora navego para incluir ese mismo paciente colgando de su grupo sanguíneo
        puntoAcceso.child(paciente.getGrupoSanguineo()).child(paciente.getNuhsa()).setValue(paciente);
        //Muestro un mensaje para indicar que ha ido bien
        Toast.makeText(this,paciente.toString(),Toast.LENGTH_LONG).show();

    }
    private Paciente generaPacienteAleatorio(){
        final Random random = new Random();
        String nuhsa=Constantes.nuhsa+random.nextInt(9999);
        String grupo=Constantes.grupo[random.nextInt(8)];
        return(new Paciente(Constantes.nombre[random.nextInt(8)],
                Constantes.apellido[random.nextInt(8)]+" "+Constantes.apellido[random.nextInt(8)],
                grupo,
                nuhsa));
    }

    public void mostrarPacientes(View view) {
        // Crear el Intent para navegar a la actividad de pacientes
        Intent intent = new Intent(MainActivity.this, ListaPacientes.class);
        startActivity(intent);
    }
}
