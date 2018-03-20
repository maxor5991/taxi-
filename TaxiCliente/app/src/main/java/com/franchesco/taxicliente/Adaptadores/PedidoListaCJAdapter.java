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

import com.franchesco.taxicliente.Clases.PedidoListaCJ;
import com.franchesco.taxicliente.R;

import java.util.ArrayList;

/**
 * Created by HP on 06/12/2017.
 */

public class PedidoListaCJAdapter extends ArrayAdapter<PedidoListaCJ> {

    ArrayList<PedidoListaCJ> pedidoListaCJ;
    Context context;
    int resource;

    public PedidoListaCJAdapter(Context context, int resource,ArrayList<PedidoListaCJ> pedidoListaCJ) {
        super(context, resource, pedidoListaCJ);
        this.pedidoListaCJ = pedidoListaCJ;
        this.context = context;
        this.resource = resource;
    }

    //Capturando el Id del Pedido
    public String getDniChofer(int position){
        return this.pedidoListaCJ.get(position).getDniChofer();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_lista_pedido_cj,null,true);
        }
        PedidoListaCJ pedidoListaCJ = getItem(position);

        TextView txtReferenciaOrigenCJ = (TextView)convertView.findViewById(R.id.txtReferenciaCJ);
        txtReferenciaOrigenCJ.setText(pedidoListaCJ.getReferenciaOrigen());

        TextView txtHoraCJ = (TextView)convertView.findViewById(R.id.txtHoraCJ);
        txtHoraCJ.setText(pedidoListaCJ.getHora());


        TextView txtNumPersonasCJ = (TextView)convertView.findViewById(R.id.txtNumPersoCJ);
        txtNumPersonasCJ.setText(pedidoListaCJ.getNumPersonas());

        TextView txtDniChofer = (TextView)convertView.findViewById(R.id.txtDniChofer);
        txtDniChofer.setText(pedidoListaCJ.getDniChofer());

        return convertView;
    }
}
