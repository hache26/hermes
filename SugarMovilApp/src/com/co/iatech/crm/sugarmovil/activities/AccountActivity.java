package com.co.iatech.crm.sugarmovil.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import com.co.iatech.crm.sugarmovil.R;
import com.co.iatech.crm.sugarmovil.activities.ui.SlidingTabLayout;
import com.co.iatech.crm.sugarmovil.activtities.modules.AccountsModuleActions;
import com.co.iatech.crm.sugarmovil.activtities.modules.ActionsStrategy;
import com.co.iatech.crm.sugarmovil.activtities.modules.Modules;
import com.co.iatech.crm.sugarmovil.adapters.ViewPagerAdapter;
import com.co.iatech.crm.sugarmovil.conex.ControlConnection;
import com.co.iatech.crm.sugarmovil.conex.TypeInfoServer;
import com.co.iatech.crm.sugarmovil.core.Info;
import com.co.iatech.crm.sugarmovil.model.CuentaDetalle;
import com.co.iatech.crm.sugarmovil.model.converters.lists.ListConverter.DataToGet;
import com.co.iatech.crm.sugarmovil.util.GlobalClass;
import com.co.iatech.crm.sugarmovil.util.ListsConversor;
import com.co.iatech.crm.sugarmovil.util.ListsConversor.ConversorsType;
import com.software.shell.fab.ActionButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class AccountActivity extends AppCompatActivity implements View.OnClickListener, AccountsModuleActions{

    /**
     * Debug.
     */
    private static final String TAG = "AccountActivity";

    /**
     * Member Variables.
     */
   
    private ViewPagerAdapter viewAdapter;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
  

    /**
     * UI References.
     */
    private Toolbar mCuentaToolbar;
    private ImageButton imageButtonEdit;
 
    private ImageButton imageButtonContacts;
    private ImageButton imageButtonOpps;
    private ImageButton imageButtonTasks;
    private ImageButton imageButtonCalls;
    private String idAccount;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_activity_account);

        Intent intent = getIntent();
        idAccount= intent.getStringExtra(MODULE.name());       
                
        mCuentaToolbar = (Toolbar) findViewById(R.id.toolbar_account);
     	setSupportActionBar(mCuentaToolbar);
     	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
    	slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.abc_search_url_text));   
    	//mSlidingTabLayout.setDistributeEvenly(true);
    	
              
       this.applyActions();

        GetAccountTask mTareaObtenerCuenta = new GetAccountTask();
        mTareaObtenerCuenta.execute(idAccount);

    }
    
    
    public void ponerValores(CuentaDetalle cuentaDetalle) {
        TextView valorRazon = (TextView) findViewById(R.id.valor_razon);
        valorRazon.setText(cuentaDetalle.getName());
        TextView valorNit = (TextView) findViewById(R.id.valor_nit);
        valorNit.setText(cuentaDetalle.getNit_c());
        TextView valorCodigo = (TextView) findViewById(R.id.valor_codigo);
        valorCodigo.setText(cuentaDetalle.getCod_alterno_c());
        TextView valorCanal = (TextView) findViewById(R.id.valor_canal);
        valorCanal.setText(ListsConversor.convert(ConversorsType.CHANNEL, cuentaDetalle.getCanal_c(), DataToGet.VALUE));
        TextView valorSector = (TextView) findViewById(R.id.valor_sector);
        valorSector.setText(cuentaDetalle.getSector_c());
        TextView valorTel1 = (TextView) findViewById(R.id.valor_tel1);
        valorTel1.setText(cuentaDetalle.getPhone_office());
        TextView valorExt1 = (TextView) findViewById(R.id.valor_ext1);
        valorExt1.setText(cuentaDetalle.getExtension1_c());
        TextView valorTel2 = (TextView) findViewById(R.id.valor_tel2);
        valorTel2.setText(cuentaDetalle.getPhone_alternate());
        TextView valorExt2 = (TextView) findViewById(R.id.valor_ext2);
        valorExt2.setText(cuentaDetalle.getExtension2_c());
        TextView valorCelular = (TextView) findViewById(R.id.valor_celular);
        valorCelular.setText(cuentaDetalle.getCelular_c());
        TextView valorFax = (TextView) findViewById(R.id.valor_fax);
        valorFax.setText(cuentaDetalle.getPhone_fax());
        TextView valorDireccion = (TextView) findViewById(R.id.valor_direccion);
        valorDireccion.setText(cuentaDetalle.getDireccion_c());
        TextView valorMunicipio = (TextView) findViewById(R.id.valor_municipio);
        valorMunicipio.setText(cuentaDetalle.getMunicipio_c());
        TextView valorDepartamento = (TextView) findViewById(R.id.valor_departamento);
        valorDepartamento.setText(
    			ListsConversor.convert(ConversorsType.DPTO, cuentaDetalle.getDepartamento_c(), DataToGet.VALUE)
    			);
    	

        TextView valorZona = (TextView) findViewById(R.id.valor_zona);
        valorZona.setText(ListsConversor.convert(ConversorsType.ZONE, cuentaDetalle.getZona_c(), DataToGet.VALUE));
        TextView valorUen = (TextView) findViewById(R.id.valor_uen);
        valorUen.setText(cuentaDetalle.getUen_c());
        TextView valorEmail = (TextView) findViewById(R.id.valor_email);
        valorEmail.setText(cuentaDetalle.getEmail_address());
        TextView valorWeb = (TextView) findViewById(R.id.valor_web);
        valorWeb.setText(cuentaDetalle.getWebsite());
        TextView valorGrupo = (TextView) findViewById(R.id.valor_grupo);
        valorGrupo.setText(cuentaDetalle.getGrupo_objetivo_c());
        TextView valorSegmento = (TextView) findViewById(R.id.valor_segmento);
        valorSegmento.setText(cuentaDetalle.getSegmento_c());
        TextView valorEstado = (TextView) findViewById(R.id.valor_estado);
        valorEstado.setText(cuentaDetalle.getEstado_c());
        
        TextView valorDescuento = (TextView) findViewById(R.id.valor_descuento);
        valorDescuento.setText(cuentaDetalle.getDescuentocomercial2_c());
        
        TextView valorDescuento2 = (TextView) findViewById(R.id.valor_descuento2);
        valorDescuento2.setText(cuentaDetalle.getDescuentocomercial_c());
                
        TextView valorPresupuesto = (TextView) findViewById(R.id.valor_presupuesto);
        valorPresupuesto.setText(cuentaDetalle.getPresupuestoanual_c());
        TextView valorDescripcion = (TextView) findViewById(R.id.valor_descripcion);
        valorDescripcion.setText(cuentaDetalle.getDescription());
        TextView valorTransporte = (TextView) findViewById(R.id.valor_c_transporte);
        valorTransporte.setText(cuentaDetalle.getCorreotransporte_c());
        TextView valorCreado = (TextView) findViewById(R.id.valor_creado);
        valorCreado.setText(cuentaDetalle.getDate_entered());
        TextView valorUsuario = (TextView) findViewById(R.id.valor_usuario);
        valorUsuario.setText(cuentaDetalle.getAssigned_user_name());
        TextView valorConstitucion = (TextView) findViewById(R.id.valor_constitucion);
        valorConstitucion.setText(cuentaDetalle.getFechaempresa_c());
        TextView valorActual = (TextView) findViewById(R.id.valor_actual);
        valorActual.setText(cuentaDetalle.getVentasactual_c());
        TextView valorAnterior = (TextView) findViewById(R.id.valor_anterior);
        valorAnterior.setText(cuentaDetalle.getVentasanterior_c());
        TextView valorNumeroAlianzas = (TextView) findViewById(R.id.valor_numero_alianzas);
        valorNumeroAlianzas.setText(cuentaDetalle.getNumeroalianzas_c());
        TextView valorAlianzas = (TextView) findViewById(R.id.valor_alianzas);
        valorAlianzas.setText(cuentaDetalle.getAlianzasestrategicas_c());
        TextView valorOrigen = (TextView) findViewById(R.id.valor_origen);
        valorOrigen.setText(cuentaDetalle.getOrigencuenta_c());
        TextView valorFecha = (TextView) findViewById(R.id.valor_fecha);
        valorFecha.setText(cuentaDetalle.getFechafacturacion_c());
        TextView valorDiaria = (TextView) findViewById(R.id.valor_diaria);
        valorDiaria.setText(cuentaDetalle.getFacturaciondiara_c());
        TextView valorAcumulada = (TextView) findViewById(R.id.valor_acumulada);
        valorAcumulada.setText(cuentaDetalle.getFacturacionmes_c());
        TextView valorCumplimiento = (TextView) findViewById(R.id.valor_cumplimiento);
        valorCumplimiento.setText(cuentaDetalle.getPorcentaje_cumplimiento_c());
        TextView valorAutorizada = (TextView) findViewById(R.id.valor_autorizada);
        valorAutorizada.setText(cuentaDetalle.getFacturacionautorizada_c());
        TextView valorNoAutorizada = (TextView) findViewById(R.id.valor_no_autorizada);
        valorNoAutorizada.setText(cuentaDetalle.getFacturacionnoautorizada_c());
        TextView valorFechaDespacho = (TextView) findViewById(R.id.valor_fecha_despacho);
        valorFechaDespacho.setText(cuentaDetalle.getFecha_despacho_c());
        TextView valorRemesa = (TextView) findViewById(R.id.valor_remesa);
        valorRemesa.setText(cuentaDetalle.getRemesa_c());
        TextView valorDestino = (TextView) findViewById(R.id.valor_destino);
        valorDestino.setText(cuentaDetalle.getDestino_c());
        TextView valorDestinatario = (TextView) findViewById(R.id.valor_destinatario);
        valorDestinatario.setText(cuentaDetalle.getNombredestinatario_c());
        TextView valorUnidades = (TextView) findViewById(R.id.valor_unidades);
        valorUnidades.setText(cuentaDetalle.getUnidades_c());
        TextView valorDocumentos = (TextView) findViewById(R.id.valor_documento);
        valorDocumentos.setText(cuentaDetalle.getDocumento_c());
        TextView valorDestinatarioPendientes = (TextView) findViewById(R.id.valor_destinatario_pendientes);
        valorDestinatarioPendientes.setText(cuentaDetalle.getNombredestinatario2_c());
        TextView valorDestinoPendientes = (TextView) findViewById(R.id.valor_destino_pendientes);
        valorDestinoPendientes.setText(cuentaDetalle.getNombredestinatario2_c());
        TextView valorMotivo = (TextView) findViewById(R.id.valor_motivo);
        valorMotivo.setText(cuentaDetalle.getMotivo_c());
        TextView valorCupo = (TextView) findViewById(R.id.valor_cupo);
        valorCupo.setText(cuentaDetalle.getCupodisponible_c());
        TextView valorCupoCr = (TextView) findViewById(R.id.valor_cupo_cr);
        valorCupoCr.setText(cuentaDetalle.getCupocr_c());
        TextView valorTotal = (TextView) findViewById(R.id.valor_total);
        valorTotal.setText(cuentaDetalle.getTotalcartera_c());
        TextView valorCondicion = (TextView) findViewById(R.id.valor_condicion);
        valorCondicion.setText(cuentaDetalle.getCondpago_c());
        TextView valorPlazo = (TextView) findViewById(R.id.valor_plazo);
        valorPlazo.setText(cuentaDetalle.getPlpago_c());
        TextView valorPromedio = (TextView) findViewById(R.id.valor_promedio);
        valorPromedio.setText(cuentaDetalle.getPrompago_c());
        TextView valorVencida = (TextView) findViewById(R.id.valor_vencida);
        valorVencida.setText(cuentaDetalle.getCarteravencida_c());
        TextView valorAVencer = (TextView) findViewById(R.id.valor_a_vencer);
        valorAVencer.setText(cuentaDetalle.getCarteravencer_c());
    }

	@Override
	public void onClick(View v) {
		
		Modules module = null;
		if(v.getId() == imageButtonContacts.getId()){
			Log.d(TAG, "Contactos x cuenta ");
			module = Modules.CONTACTS;
		}else if(v.getId() == imageButtonOpps.getId()){
			Log.d(TAG, "Oportunidades X Cuenta ");
			module = Modules.OPPORTUNITIES;
		}else if(v.getId() == imageButtonTasks.getId()){
			Log.d(TAG, "Tareas X Cuenta ");
			module = Modules.TASKS;
		}else if(v.getId() == imageButtonCalls.getId()){
			Log.d(TAG, "Llamadas X Cuenta ");
			module = Modules.CALLS;
		}
		ActivitiesMediator.getInstance().setActualID(idAccount, MODULE);
		ActivitiesMediator.getInstance().showList(AccountActivity.this, module, true);
	}


	@Override
	protected void onResume() {
		ActivitiesMediator.getInstance().setActualID(idAccount, MODULE);
		super.onResume();
	}



	/**
    * Representa una tarea asincrona de obtencion de cuenta.
    */
   public class GetAccountTask extends AsyncTask<String, Void, Boolean> {
       private ProgressDialog progressDialog;
       private CuentaDetalle cuentaActual;
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           progressDialog = new ProgressDialog(AccountActivity.this, ProgressDialog.THEME_HOLO_DARK);
           progressDialog.setMessage("Cargando informacion de cuenta...");
           progressDialog.setIndeterminate(true);
           progressDialog.show();
       }

       @Override
       protected Boolean doInBackground(String... params) {
           try {
               // Parametros
               String idAccount = params[0];

               // Respuesta
               String account = null;

               // Intento de obtener cuenta
               ControlConnection.addHeader("idAccount", idAccount);
               account  = ControlConnection.getInfo(TypeInfoServer.getAccount, AccountActivity.this);
               JSONObject jObj = new JSONObject(account);

               JSONArray jArr = jObj.getJSONArray("results");
               for (int i = 0; i < jArr.length(); i++) {
                   JSONObject obj = jArr.getJSONObject(i);
                  
                   cuentaActual = new CuentaDetalle(obj);
                   
               }

               return true;
           } catch (Exception e) {
               Log.d(TAG, "Buscar Cuenta Error: "
                       + e.getClass().getName() + ":" + e.getMessage());
               return false;
           }
       }

       @Override
       protected void onPostExecute(final Boolean success) {
    	   
           progressDialog.dismiss();

           if (success) {
        	   viewAdapter.setActualAccount(cuentaActual);
        	   viewPager.setAdapter(viewAdapter);
        	   slidingTabLayout.setViewPager(viewPager);
           }
       }

       @Override
       protected void onCancelled() {
          // mTareaObtenerCuenta = null;
           Log.d(TAG, "Cancelado ");
       }
   }

   @Override
	public ActionButton getActionButton() {
		return null;
	}

	@Override
	public ImageButton getEditButton() {
		return null;
	}

	@Override
	public Modules getModule() {
		return MODULE;
	}

	@Override
	public String getAssignedUser() {
		return null;
	}


	@Override
	public Parcelable getBean() {
		return null;
	}

	@Override
	public void applyActions() {
		imageButtonEdit = (ImageButton) findViewById(R.id.ic_edit); 
		imageButtonEdit.setVisibility(View.INVISIBLE);
    	
      //ToolBar Opciones
		imageButtonContacts = (ImageButton) findViewById(R.id.image_contacts);
        imageButtonContacts.setOnClickListener(this);
        
        imageButtonOpps = (ImageButton) findViewById(R.id.image_opportunities);
        imageButtonOpps.setOnClickListener(this);
        
        imageButtonTasks =  (ImageButton) findViewById(R.id.image_tasks);
        imageButtonTasks.setOnClickListener(this);
        
        imageButtonCalls =  (ImageButton) findViewById(R.id.image_calls);
        imageButtonCalls.setOnClickListener(this);
	    
        GlobalClass gc = (GlobalClass)getApplicationContext();
        ActionsStrategy.definePermittedActions(this, gc);

	}


	@Override
	public boolean chargeIdPreviousModule() {
		return idAccount != null;
	}
}
