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
import com.co.iatech.crm.sugarmovil.adapters.RecyclerOpportunitiesAdapter;
import com.co.iatech.crm.sugarmovil.core.Info;
import com.co.iatech.crm.sugarmovil.model.OportunidadDetalle;
import com.co.iatech.crm.sugarmovil.util.GlobalClass;


public class OpportunityActivity extends AppCompatActivity {


    /**
     * Debug.
     */
    private static final String TAG = "OpportunityActivity";

    /**
     * Tasks.
     */
    private GetOpportunityTask mTareaObtenerOportunidad = null;

    /**
     * Member Variables.
     */
    private String mUrl;
    private String mIdOportunidad;
    private OportunidadDetalle mOportunidadDetalle;

    /**
     * UI References.
     */
    private Toolbar mCuentaToolbar;
    private ImageButton mImageButtonEdit;
    private LinearLayout mLayoutContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity);

        // Variable Global
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        mUrl = globalVariable.getUrl();
        Log.d(TAG, mUrl);

        Intent intent = getIntent();
        mIdOportunidad = intent.getStringExtra(Info.OPORTUNIDAD_SELECCIONADA.name());
        Log.d(TAG, "Id oportunidad " + mIdOportunidad);

        // Main Toolbar
        mCuentaToolbar = (Toolbar) findViewById(R.id.toolbar_opportunity);
        setSupportActionBar(mCuentaToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mImageButtonEdit = (ImageButton) findViewById(R.id.ic_edit);

        // Contenido
        mLayoutContenido = (LinearLayout) findViewById(R.id.layout_contenido);

        //Eventos
        mImageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Editar oportunidad ");
                // Edit Oportunidad Activity
                Intent intentEditarOportunidad = new Intent(OpportunityActivity.this,
                        EditOpportunityActivity.class);
                intentEditarOportunidad.putExtra(Info.OPORTUNIDAD_SELECCIONADA.name(), mOportunidadDetalle);
                startActivity(intentEditarOportunidad);
            }
        });

        // Tarea obtener cuenta
        mTareaObtenerOportunidad = new GetOpportunityTask();
        mTareaObtenerOportunidad.execute(String.valueOf(mIdOportunidad));
    }

    public void ponerValores(OportunidadDetalle oportunidadDetalle) {
        TextView valorNombre = (TextView) findViewById(R.id.valor_nombre);
        valorNombre.setText(oportunidadDetalle.getName());
        TextView valorTipo = (TextView) findViewById(R.id.valor_tipo);
        valorTipo.setText(oportunidadDetalle.getTipo_c());
        TextView valorEtapa = (TextView) findViewById(R.id.valor_etapa);
        valorEtapa.setText(oportunidadDetalle.getSales_stage());
        TextView valorCuenta = (TextView) findViewById(R.id.valor_cuenta);
        valorCuenta.setText(oportunidadDetalle.getNameAccount());
        TextView valorUsuario = (TextView) findViewById(R.id.valor_usuario);
        valorUsuario.setText(oportunidadDetalle.getUsuario_final_c());
        TextView valorFecha = (TextView) findViewById(R.id.valor_fecha);
        valorFecha.setText(oportunidadDetalle.getDate_closed());
        TextView valorEstimado = (TextView) findViewById(R.id.valor_estimado);
        valorEstimado.setText(oportunidadDetalle.getValoroportunidad_c());
        TextView valorProbabilidad = (TextView) findViewById(R.id.valor_probabilidad);
        valorProbabilidad.setText(oportunidadDetalle.getProbability());
        TextView valorCampana = (TextView) findViewById(R.id.valor_campana);
        valorCampana.setText(oportunidadDetalle.getNameCampaign());
        TextView valorMedio = (TextView) findViewById(R.id.valor_medio);
        valorMedio.setText(oportunidadDetalle.getMedio_c());
        TextView valorFuente = (TextView) findViewById(R.id.valor_fuente);
        valorFuente.setText(oportunidadDetalle.getFuente_c());
        TextView valorPaso = (TextView) findViewById(R.id.valor_paso);
        valorPaso.setText(oportunidadDetalle.getNext_step());
        TextView valorDescripcion = (TextView) findViewById(R.id.valor_descripcion);
        valorDescripcion.setText(oportunidadDetalle.getDescription());
        TextView valorAsignado = (TextView) findViewById(R.id.valor_asignado_a);
        valorAsignado.setText(oportunidadDetalle.getAssigned_user_name());
        TextView valorEnergia = (TextView) findViewById(R.id.valor_energia);
        valorEnergia.setText(oportunidadDetalle.getEnergia_c());
        TextView valorComunicaciones = (TextView) findViewById(R.id.valor_comunicaciones);
        valorComunicaciones.setText(oportunidadDetalle.getComunicaciones_c());
        TextView valorIluminacion = (TextView) findViewById(R.id.valor_iluminacion);
        valorIluminacion.setText(oportunidadDetalle.getIluminacion_c());
    }

    /**
     * Representa una tarea asincrona de obtencion de oportunidad.
     */
    public class GetOpportunityTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OpportunityActivity.this, ProgressDialog.THEME_HOLO_DARK);
            progressDialog.setMessage("Cargando información oportunidad...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // Parametros
                String idOportunidad = params[0];

                // Respuesta
                String account = null;

                // Intento de obtener cuenta
                HttpClient httpClientOpportunity = new DefaultHttpClient();
                HttpGet httpGetAccount = new HttpGet(mUrl
                        + "getOpportunity");
                httpGetAccount.setHeader("idOpportunity", idOportunidad);

                try {
                    HttpResponse response = httpClientOpportunity
                            .execute(httpGetAccount);
                    account = EntityUtils.toString(response
                            .getEntity());
                    account = account.replace("\n", "")
                            .replace("\r", "");
                    Log.d(TAG, "Oportunidad Response: "
                            + account);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                JSONObject jObj = new JSONObject(account);

                JSONArray jArr = jObj.getJSONArray("results");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = jArr.getJSONObject(i);
                    mOportunidadDetalle = new OportunidadDetalle(obj);
                }

                return true;
            } catch (Exception e) {
                Log.d(TAG, "Buscar Oportunidad Error: "
                        + e.getClass().getName() + ":" + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTareaObtenerOportunidad = null;
            progressDialog.dismiss();

            if (success) {
                ponerValores(mOportunidadDetalle);
            }
        }

        @Override
        protected void onCancelled() {
            mTareaObtenerOportunidad = null;
            Log.d(TAG, "Cancelado ");
        }
    }
}
