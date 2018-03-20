package com.example.franchesco.taxichofer.Adaptadores;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franchesco.taxichofer.Activitys.EstadosCarrera;
import com.example.franchesco.taxichofer.Clases.Carreras;
import com.example.franchesco.taxichofer.R;

import java.util.List;


/**
 * Created by HP on 11/12/2017.
 */

public class ListaCarreraAdaptador extends RecyclerView.Adapter<ListaCarreraAdaptador.CarreraViewHolder>{


    List<Carreras> items;

    public static class CarreraViewHolder extends RecyclerView.ViewHolder {

        TextView txtIdPediCar;
        TextView txtRefereCar;
        TextView txtNumPersoCar;
        TextView txtHoraCar;
        TextView txtDirSol;
        TextView txtDirOrSol;

        public CarreraViewHolder(final View v)
        {
            super(v);
            txtIdPediCar = v.findViewById(R.id.txtIdPediCar);
            txtRefereCar = v.findViewById(R.id.txtRefereCar);
            txtNumPersoCar = v.findViewById(R.id.txtNumPersoCar);
            txtHoraCar = v.findViewById(R.id.txtHoraCar);
            txtDirSol = v.findViewById(R.id.txtDirSol);
            txtDirOrSol = v.findViewById(R.id.txtDirOrSol);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), EstadosCarrera.class);
                    i.putExtra("IdPedido",Integer.parseInt(txtIdPediCar.getText().toString()));
                    view.getContext().startActivity(i);
                }
            });
        }
    }

    public ListaCarreraAdaptador(List<Carreras> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CarreraViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_carrera, viewGroup, false);
        return new CarreraViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CarreraViewHolder viewHolder, int i)
    {
        viewHolder.txtIdPediCar.setText(items.get(i).getIdPedido());
        viewHolder.txtRefereCar.setText(items.get(i).getReferenciaOrigen());
        viewHolder.txtHoraCar.setText(items.get(i).getHora());
        viewHolder.txtNumPersoCar.setText(items.get(i).getNumPersonas());
        viewHolder.txtDirSol.setText(items.get(i).getDirDestino());
        viewHolder.txtDirOrSol.setText(items.get(i).getDirOrigen());
    }
}
