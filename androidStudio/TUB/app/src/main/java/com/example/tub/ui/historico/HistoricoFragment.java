package com.example.tub.ui.historico;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tub.R;
import com.example.tub.databinding.FragmentHistoricoBinding;


public class HistoricoFragment extends Fragment {

    //private RequestQueue mRequestQueue;
    //private StringRequest mStringRequest;
    private FragmentHistoricoBinding binding;
    Context a = this.getContext();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final String linkApi = "https://a2f3-89-214-146-78.ngrok-free.app/api/v1/viagens/" + getString(R.string.id_cliente);//Mudar isto

        /*HistoricoViewModel HistoricoViewModel =
                new ViewModelProvider(this).get(HistoricoViewModel.class);

        binding = FragmentHistoricoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();*/

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        View view = inflater.inflate(R.layout.fragment_historico, container, false);
        TextView a =  (TextView) view.findViewById(R.id.textView);

        //a.setText("aaaaaaaaa");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, linkApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String apresentar = response.substring(response.indexOf('[') + 2, response.length() - 2);
                String[] parts = apresentar.split(",");
                String construct = "";
                for(int i = 0; i < parts.length; i++){
                    if(i % 2 == 0){
                        parts[i].replace("{", "");
                        construct = construct + parts[i].substring(1, parts[i].indexOf("=")) + ": " + parts[i].substring(parts[i].indexOf("=") + 1, parts[i].indexOf("T")) + "\nHoras: " + parts[i].substring(parts[i].indexOf("T") + 1)  + "\n";//+  parts[i].substring(parts[i].indexOf("=" + 1))
                    }else{
                        construct = construct + parts[i].substring(1, parts[i].indexOf("=")) + ": " + parts[i].substring(parts[i].indexOf("=") + 1, parts[i].length() - 2)+ "\n\n";//parts[];
                    }

                }
                a.setText(construct);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //a.setText(error.toString());
            }
        });
        //Toast.makeText(a,"OLLAAAAAA", Toast.LENGTH_LONG).show();//display the response on screen

        //sendAndRequestResponse();

        //return root;
        queue.add(stringRequest);



        return view;
    }

    /*private void sendAndRequestResponse() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getActivity());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, linkApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"ERROR", Toast.LENGTH_LONG).show();
                //Log.i("Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
