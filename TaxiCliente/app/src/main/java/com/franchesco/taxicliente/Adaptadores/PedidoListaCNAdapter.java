package com.franchesco.taxicliente.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.franchesco.taxicliente.Clases.PedidoListaCN;
import com.franchesco.taxicliente.R;

import java.util.ArrayList;

/**
 * Created by HP on 28/11/2017.
 */

public class PedidoListaCNAdapter extends ArrayAdapter<PedidoListaCN> {

    ArrayList<PedidoListaCN> pedidoListaCN;
    Context context;
    int resource;

    public PedidoListaCNAdapter(Context context, int resource,ArrayList<PedidoListaCN> pedidoListaCN) {
        super(context, resource, pedidoListaCN);
        this.pedidoListaCN = pedidoListaCN;
        this.context = context;
        this.resource = resource;
    }

    //Capturando el Id del Pedido
    public String getDni(int position){
        return this.pedidoListaCN.get(position).getDniChofer();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_lista_pedido_cn,null,true);
        }
        PedidoListaCN pedidoListaCN = getItem(position);

        TextView txtReferenciaOrigenCN = (TextView)convertView.findViewById(R.id.txtReferenciaCN);
        txtReferenciaOrigenCN.setText(pedidoListaCN.getReferenciaOrigen());

        TextView txtHoraCN = (TextView)convertView.findViewById(R.id.txtHoraCN);
        txtHoraCN.setText(pedidoListaCN.getHora());


        TextView txtNumPersonasCN = (TextView)convertView.findViewById(R.id.txtNumPersoCN);
        txtNumPersonasCN.setText(pedidoListaCN.getNumPersonas());

        TextView txtDniChofer = (TextView)convertView.findViewById(R.id.txtDniChofer);
        txtDniChofer.setText(pedidoListaCN.getDniChofer());

        return convertView;
    }
}
