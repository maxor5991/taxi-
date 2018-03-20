package com.example.franchesco.taxichofer.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.franchesco.taxichofer.Clases.Pedidos;
import com.example.franchesco.taxichofer.R;

import java.util.ArrayList;

/**
 * Created by HP on 11/12/2017.
 */

public class ListaPedidoAdaptador extends ArrayAdapter<Pedidos> {

    ArrayList<Pedidos> pedidos;
    Context context;
    int resource;

    public ListaPedidoAdaptador(Context context, int resource,ArrayList<Pedidos> pedidos) {
        super(context, resource, pedidos);
        this.pedidos = pedidos;
        this.context = context;
        this.resource = resource;
    }
    //Capturando el Id del Pedido
    public String getIdPedido(int position){
        return this.pedidos.get(position).getIdPedido();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_pedido,null,true);
        }
        Pedidos pedidos = getItem(position);

        TextView txtIdPedido = (TextView)convertView.findViewById(R.id.txtIdPedido);
        txtIdPedido.setText(pedidos.getIdPedido());

        TextView txtReferenciaOrigen = (TextView)convertView.findViewById(R.id.txtReferencia);
        txtReferenciaOrigen.setText(pedidos.getReferenciaOrigen());

        TextView txtHora = (TextView)convertView.findViewById(R.id.txtHora);
        txtHora.setText(pedidos.getHora());

        TextView txtNumPersonas = (TextView)convertView.findViewById(R.id.txtNumPerso);
        txtNumPersonas.setText(pedidos.getNumPersonas());

        return convertView;
    }
}
