package com.co.iatech.crm.sugarmovil.activities;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.co.iatech.crm.sugarmovil.R;
import com.co.iatech.crm.sugarmovil.adapters.RecyclerTasksAdapter;
import com.co.iatech.crm.sugarmovil.core.Info;
import com.co.iatech.crm.sugarmovil.model.TareaDetalle;
import com.co.iatech.crm.sugarmovil.util.GlobalClass;


public class TaskActivity extends AppCompatActivity {


    /**
     * Debug.
     */
    private static final String TAG = "TaskActivity";

    /**
     * Tasks.
     */
    private GetTaskTask mTareaObtenerTarea = null;

    /**
     * Member Variables.
     */
    private String mUrl;
    private String mIdTarea;
    private TareaDetalle mTareaDetalle;

    /**
     * UI References.
     */
    private Toolbar mTareaToolbar;
    private ImageButton mImageButtonEdit;
    private LinearLayout mLayoutContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Variable Global
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        mUrl = globalVariable.getUrl();
        Log.d(TAG, mUrl);

        Intent intent = getIntent();
        mIdTarea = intent.getStringExtra(Info.ID_TAREA.name());
        Log.d(TAG, "Id producto " + mIdTarea);

        // Main Toolbar
        mTareaToolbar = (Toolbar) findViewById(R.id.toolbar_task);
        setSupportActionBar(mTareaToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mImageButtonEdit = (ImageButton) findViewById(R.id.ic_edit);

        // Contenido
        mLayoutContenido = (LinearLayout) findViewById(R.id.layout_contenido);

        //Eventos
        mImageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Editar tarea ");
                // Edit Task Activity
//                Intent intentEditarTarrea = new Intent(TaskActivity.this,
//                        EditTaskActivity.class);
//                intentEditarTarrea.putExtra(Info.TAREA_ACTUAL.name(), mTareaDetalle);
//                startActivity(intentEditarTarrea);
            }
        });

        // Tarea obtener tarea
        mTareaObtenerTarea = new GetTaskTask();
        mTareaObtenerTarea.execute(String.valueOf(mIdTarea));
    }

    public void ponerValores(TareaDetalle tareaDetalle) {
        TextView valorAsunto = (TextView) findViewById(R.id.valor_asunto);
        valorAsunto.setText(tareaDetalle.getName());
        TextView valorEstado = (TextView) findViewById(R.id.valor_estado);
        valorEstado.setText(tareaDetalle.getStatus());
        TextView valorFechaInicio = (TextView) findViewById(R.id.boton_fecha_inicio);
        valorFechaInicio.setText(tareaDetalle.getDate_start());
        TextView valorFechaVence = (TextView) findViewById(R.id.boton_fecha_vence);
        valorFechaVence.setText(tareaDetalle.getDate_due());
        TextView valorContacto = (TextView) findViewById(R.id.valor_contacto);
        valorContacto.setText(tareaDetalle.getContact_name());
        TextView valorEstimado = (TextView) findViewById(R.id.valor_estimado);
        valorEstimado.setText(tareaDetalle.getTrabajo_estimado_c());
        TextView valorPrioridad = (TextView) findViewById(R.id.valor_prioridad);
        valorPrioridad.setText(tareaDetalle.getPriority());
        TextView valorDescripcion = (TextView) findViewById(R.id.valor_descripcion);
        valorDescripcion.setText(tareaDetalle.getDescription());
        TextView valorAsignado = (TextView) findViewById(R.id.valor_asignado_a);
        valorAsignado.setText(tareaDetalle.getAssigned_user_name());
        TextView valorTipo = (TextView) findViewById(R.id.valor_tipo);
        valorTipo.setText(tareaDetalle.getParent_type());
        TextView valorNombre = (TextView) findViewById(R.id.valor_nombre);
        valorNombre.setText(tareaDetalle.getParent_name());
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume Task Activity");
        super.onResume();

        // Tarea obtener tarea
//        mTareaObtenerTarea = new GetTaskTask();
//        mTareaObtenerTarea.execute(String.valueOf(mTareaDetalle.getId()));
    }

    /**
     * Representa una tarea asincrona de obtencion de tarea.
     */
    public class GetTaskTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TaskActivity.this, ProgressDialog.THEME_HOLO_DARK);
            progressDialog.setMessage("Cargando información tarea...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // Parametros
                String idTarea = params[0];

                // Respuesta
                String product = null;

                // Intento de obtener tarea
                HttpClient httpClientAccount = new DefaultHttpClient();
                HttpGet httpGetAccount = new HttpGet(mUrl
                        + "getTask");
                httpGetAccount.setHeader("idTask", idTarea);

                try {
                    HttpResponse response = httpClientAccount
                            .execute(httpGetAccount);
                    product = EntityUtils.toString(response
                            .getEntity());
                    product = product.replace("\n", "")
                            .replace("\r", "");
                    Log.d(TAG, "Tarea Response: "
                            + product);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                JSONObject jObj = new JSONObject(product);

                JSONArray jArr = jObj.getJSONArray("results");
                if( jArr.length() > 0) {
                    JSONObject obj = jArr.getJSONObject(0);
                    mTareaDetalle = new TareaDetalle(obj);
                 }

                return true;
            } catch (Exception e) {
                Log.d(TAG, "Buscar Tarea Error: "
                        + e.getClass().getName() + ":" + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTareaObtenerTarea = null;
            progressDialog.dismiss();

            if (success) {
                ponerValores(mTareaDetalle);

            }
        }

        @Override
        protected void onCancelled() {
            mTareaObtenerTarea = null;
            Log.d(TAG, "Cancelado ");
        }
    }
}