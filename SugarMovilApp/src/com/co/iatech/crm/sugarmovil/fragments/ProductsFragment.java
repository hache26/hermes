package com.co.iatech.crm.sugarmovil.fragments;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.co.iatech.crm.sugarmovil.R;
import com.co.iatech.crm.sugarmovil.activities.MainActivity;
import com.co.iatech.crm.sugarmovil.adapters.RecyclerProductsAdapter;
import com.co.iatech.crm.sugarmovil.model.Producto;
import com.co.iatech.crm.sugarmovil.util.GlobalClass;

public class ProductsFragment extends Fragment {
    /**
     * Debug.
     */
    private static final String TAG = "ProductsFragment";

    /**
     * Tasks.
     */
    private GetCallsTask mTareaObtenerLlamadas = null;

    /**
     * Member Variables.
     */
    private GlobalClass mGlobalVariable;
    private String mUrl;
    private ArrayList<Producto> mProductsArray = new ArrayList<>();

    /**
     * UI References.
     */
    private View mRootView;
    private TextView mMainTextView;
    private SearchView mMainSearchView;
    private RecyclerView mRecyclerViewProducts;
    private RecyclerView.Adapter mRecyclerViewProductsAdapter;
    private RecyclerView.LayoutManager mRecyclerViewProductsLayoutManager;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /*
    * Nueva Instancia de ProductsFragment.
    */
    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Parametros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_products, container, false);

        // Variable Global
        mGlobalVariable = (GlobalClass) getActivity()
                .getApplicationContext();
        mUrl = mGlobalVariable.getUrl();
        mGlobalVariable.setmSelectedButton(4);

        // Main Toolbar
        mMainTextView = ((MainActivity) getActivity()).getMainTextView();
        mMainTextView.setText("PRODUCTOS");
        mMainTextView.setVisibility(View.VISIBLE);
        mMainSearchView = ((MainActivity) getActivity()).getMainSearchView();
        mMainSearchView.setVisibility(View.VISIBLE);

        // Recycler
        mRecyclerViewProducts = (RecyclerView) mRootView.findViewById(R.id.recycler_view_products);
        mRecyclerViewProducts.setHasFixedSize(true);

        mRecyclerViewProductsLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewProducts.setLayoutManager(mRecyclerViewProductsLayoutManager);

        // Eventos
        mMainSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainTextView.setVisibility(View.GONE);
            }
        });

        mMainSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mMainTextView.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mMainSearchView.getWindowToken(), 0);

                try {
                    ((RecyclerProductsAdapter) mRecyclerViewProducts.getAdapter()).flushFilter();
                } catch (Exception e) {
                    Log.d(TAG, "Error añadiendo el filtro de busqueda");
                }

                return false;
            }
        });

        mMainSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {// Filtro para llamadas
                    ((RecyclerProductsAdapter) mRecyclerViewProducts.getAdapter()).setFilter(query);
                } catch (Exception e) {
                    Log.d(TAG, "Error añadiendo el filtro de busqueda");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {// Filtro para llamadas
                    ((RecyclerProductsAdapter) mRecyclerViewProducts.getAdapter()).setFilter(newText);
                } catch (Exception e) {
                    Log.d(TAG, "Error añadiendo el filtro de busqueda");
                }

                return false;
            }
        });

        // Tarea para consultar productos
        mTareaObtenerLlamadas = new GetCallsTask();
        mTareaObtenerLlamadas.execute();

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainSearchView.clearFocus();
        try {
            mMainSearchView.setIconified(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResume Fragment Products");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Representa una tarea asincrona de obtencion de productos.
     */
    public class GetCallsTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_DARK);
            progressDialog.setMessage("Cargando productos...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Parametros
                String productos = null;

                // Intento de obtener productos
                HttpClient httpClientProducts = new DefaultHttpClient();
                HttpGet httpGetProducts = new HttpGet(mUrl
                        + "getProductos");

                try {
                    HttpResponse response = httpClientProducts
                            .execute(httpGetProducts);
                    productos = EntityUtils.toString(response
                            .getEntity());
                    productos = productos.replace("\n", "")
                            .replace("\r", "");
                    Log.d(TAG, "Productos Response: "
                            + productos);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                mProductsArray.clear();

                JSONObject jObj = new JSONObject(productos);

                JSONArray jArr = jObj.getJSONArray("results");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = jArr.getJSONObject(i);
                    mProductsArray.add(new Producto(obj));
                }

                return true;
            } catch (Exception e) {
                Log.d(TAG, "Buscar Productos Error: "
                        + e.getClass().getName() + ":" + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTareaObtenerLlamadas = null;
            progressDialog.dismiss();

            if (success) {
                if (mProductsArray.size() > 0) {
                    mRecyclerViewProductsAdapter = new RecyclerProductsAdapter(getActivity(), mProductsArray);
                    mRecyclerViewProducts.setAdapter(mRecyclerViewProductsAdapter);
                } else {
                    Log.d(TAG,
                            "No hay Productos: "
                                    + mProductsArray.size());
                }
            }
        }

        @Override
        protected void onCancelled() {
            mTareaObtenerLlamadas = null;
            Log.d(TAG, "Cancelado ");
        }
    }
}