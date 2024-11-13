package us.mastersalud.realtimedatabase;

import static us.mastersalud.realtimedatabase.Constantes.EDITAR_PACIENTE;
import static us.mastersalud.realtimedatabase.Constantes.REFERENCE_FIREBASE_PACIENTES;
import static us.mastersalud.realtimedatabase.Constantes.TXT_BOTON_CANCELAR;
import static us.mastersalud.realtimedatabase.Constantes.TXT_BOTON_GUARDAR;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_APELLIDOS;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_GRUPO_SANGUINEO;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_NOMBRE;
import static us.mastersalud.realtimedatabase.Constantes.VISUALIZACION_ETIQUETA_NUHSA;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {


    //Lista de pacientes a mostrar
    private final List<Paciente> pacienteList;

    // Constructor que recibe la lista de pacientes
    public PacienteAdapter(List<Paciente> pacienteList) {
        this.pacienteList = pacienteList;
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista de un elemento a partir del layout element_patient para mostrarlo en el RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_patient, parent, false);
        return new PacienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteAdapter.PacienteViewHolder holder, int position) {
        // // Obtiene el paciente de la lista en la posición actual
        Paciente paciente = pacienteList.get(position);

        // Creamos un SpannableString para mostrar "Nombre: " en negrita seguido del nombre del paciente
        String nombreTexto = VISUALIZACION_ETIQUETA_NOMBRE+ paciente.getNombre();
        SpannableString spannableNombre = new SpannableString(nombreTexto);
        spannableNombre.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // "Nombre: " en negrita

        // Asignamos el texto con formato al TextView de nombre
        holder.nombreTextView.setText(spannableNombre);

        // Creamos un SpannableString para mostrar "Apellidos: " en negrita seguido de los apellidos del paciente
        String apellidosTexto = VISUALIZACION_ETIQUETA_APELLIDOS + paciente.getApellidos();
        SpannableString spannableApellidos = new SpannableString(apellidosTexto);
        spannableApellidos.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // "Apellidos: " en negrita
        holder.apellidoTextView.setText(spannableApellidos);

        // Creamos un SpannableString para mostrar "Grupo Sanguineo: " en negrita seguido del grupo sanguineo del paciente
        String grupoSanguineoTexto = VISUALIZACION_ETIQUETA_GRUPO_SANGUINEO + paciente.getGrupoSanguineo();
        SpannableString spannableGrupoSanguineo = new SpannableString(grupoSanguineoTexto);
        spannableGrupoSanguineo.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // "Grupo Sanguíneo: " en negrita
        holder.grupoSanguineoTextView.setText(spannableGrupoSanguineo);

        // Creamos un SpannableString para mostrar "Nuhsa: " en negrita seguido del Nuhsa del paciente
        String nuhsaTexto = VISUALIZACION_ETIQUETA_NUHSA + paciente.getNuhsa();
        SpannableString spannableNuhsa = new SpannableString(nuhsaTexto);
        spannableNuhsa.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // "NUHSA: " en negrita
        holder.nuhsaTextView.setText(spannableNuhsa);

        // Configuración del botón de edición: al hacer clic, se muestra un diálogo para editar el paciente
        holder.btnEditPaciente.setOnClickListener(v ->showEditDialog(holder.itemView.getContext(),paciente));

        // Configuración del botón de borrado: al hacer clic, se borra el paciente
        holder.btnDeletePaciente.setOnClickListener(v -> deletePaciente(holder.itemView.getContext(),paciente));

    }

    private void deletePaciente(Context context, Paciente paciente) {
        // Referencias a ambas estructuras de la base de datos. Estructura Pacientes y Estructura Grupo Sanguíneo del paciente
        DatabaseReference pacientesRef = FirebaseDatabase.getInstance().getReference(REFERENCE_FIREBASE_PACIENTES);
        DatabaseReference grupoSanguineoRef = FirebaseDatabase.getInstance().getReference(paciente.getGrupoSanguineo());

        // Eliminamos de la estructura "Pacientes" el paciente
        pacientesRef.child(paciente.getNuhsa()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Eliminamos de la estructura grupo sanguineo
                        grupoSanguineoRef.child(paciente.getNuhsa()).removeValue()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        Toast.makeText(context, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error al eliminar de grupo sanguíneo", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(context, "Error al eliminar paciente", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEditDialog(Context context, Paciente paciente) {
        // Cargar el layout del diálogo
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_element, null);

        // Obtenemos las referencias a los campos de entrada
        EditText etEditNombre = view.findViewById(R.id.etEditNombre);
        EditText etEditApellidos = view.findViewById(R.id.etEditApellidos);
        EditText etEditGrupoSanguineo = view.findViewById(R.id.etEditGrupoSanguineo);
        EditText etEditNuhsa = view.findViewById(R.id.etEditNuhsa);

        // Rellenamos los campos con los datos actuales del paciente
        etEditNombre.setText(paciente.getNombre());
        etEditApellidos.setText(paciente.getApellidos());
        etEditGrupoSanguineo.setText(paciente.getGrupoSanguineo());
        // Deshabilitamos campo NUHSA para que no se pueda editar
        etEditNuhsa.setEnabled(false);

        // Construir el diálogo para poder editar el paciente
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(EDITAR_PACIENTE);

        builder.setPositiveButton(TXT_BOTON_GUARDAR, (dialog, which) -> {
            // Obtenemos los nuevos valores ingresados por el usuario
            String nuevoNombre = etEditNombre.getText().toString().trim();
            String nuevosApellidos = etEditApellidos.getText().toString().trim();
            String nuevoGrupoSanguineo = etEditGrupoSanguineo.getText().toString().trim();
            //Verificamos si el grupo sanguíneo ha cambiado.
            boolean grupoSanguineoCambio = !nuevoGrupoSanguineo.equals(paciente.getGrupoSanguineo());

            // Creamos un mapa con los nuevos datos del paciente
            Map<String, Object> pacientes = new HashMap<>();
            pacientes.put("nombre", nuevoNombre);
            pacientes.put("apellidos", nuevosApellidos);


            // Si cambia el grupo sanguíneo, entonces:
            //1. Eliminamos el paciente del grupo sanguíneo anterior
            //2. Creamos un nuevo grupo sanguíneo con los nuevos datos
            //3. Actualizamos los datos en Pacientes
            if (grupoSanguineoCambio) {
                String viejoGrupoSanguineo = paciente.getGrupoSanguineo();
                // Eliminamos el paciente del grupo sanguineo anterior
                DatabaseReference grupoRefAntiguo = FirebaseDatabase.getInstance()
                        .getReference(viejoGrupoSanguineo)
                        .child(paciente.getNuhsa());
                grupoRefAntiguo.removeValue();

                // Actualizamos el grupo sanguíneo de pacientes
                pacientes.put("grupoSanguineo", nuevoGrupoSanguineo);

                // Añadimos el paciente al nuevo grupo sanguíneo
                DatabaseReference grupoRefNuevo = FirebaseDatabase.getInstance()
                        .getReference(nuevoGrupoSanguineo)
                        .child(paciente.getNuhsa());
                grupoRefNuevo.setValue(pacientes);

                // Actualizamos en el nodo de Pacientes usando updateChildren
                DatabaseReference pacienteRef = FirebaseDatabase.getInstance()
                        .getReference("Pacientes")
                        .child(paciente.getNuhsa()); // Usar NUHSA como clave
                pacienteRef.updateChildren(pacientes);
            }else{
                //Si el grupo sanguineo no cambió, en este caso solo actualizamos la referencia de los campos Nombre y Apellidos:
                //1. Primero en el nuhsa de la entidad grupo sanguineo
                DatabaseReference grupoSanguineoRef = FirebaseDatabase.getInstance()
                        .getReference(paciente.getGrupoSanguineo()).child(paciente.getNuhsa());
                grupoSanguineoRef.updateChildren(pacientes);
                //2. Segundo en el nuhsa de la entidad pacientes
                DatabaseReference pacientesRef = FirebaseDatabase.getInstance()
                        .getReference("Pacientes").child(paciente.getNuhsa());
                pacientesRef.updateChildren(pacientes);
            }
        });
        // Cuando se hace clic en el botón "Cancelar" se cierra el diálogo sin hacer cambios
        builder.setNegativeButton(TXT_BOTON_CANCELAR, (dialog, which) -> dialog.dismiss());

        // Creamos y mostramos el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return pacienteList.size();
    }

    // Clase ViewHolder para almacenar las referencias a los elementos de la vista
    public static class PacienteViewHolder extends RecyclerView.ViewHolder {
        //Declaramos las vistas que usaremos
        public TextView nombreTextView, apellidoTextView, grupoSanguineoTextView, nuhsaTextView;
        public ImageButton btnEditPaciente, btnDeletePaciente;

        //Constructor que recibe la vista del elemento y encuentra las referencias a los componentes
        public PacienteViewHolder(View view) {
        // Llamada al constructor de la clase base (RecyclerView.ViewHolder)
            super(view);
            // Obtenemos referencias a los TextViews y botones en el layout
            nombreTextView = view.findViewById(R.id.tvNombre);
            apellidoTextView = view.findViewById(R.id.tvApellidos);
            grupoSanguineoTextView = view.findViewById(R.id.tvGrupoSanguineo);
            nuhsaTextView = view.findViewById(R.id.tvNuhsa);
            btnEditPaciente = view.findViewById(R.id.btnEditPaciente);
            btnDeletePaciente = view.findViewById(R.id.btnDeletePaciente);
        }
    }
}
