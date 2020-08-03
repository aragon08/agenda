package com.example.orion.agenda;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Orion on 14/04/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventViewHolder> {
    private ArrayList<Event> eventList;
    private Context context;

    public EventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public eventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        //eventViewHolder event=new eventViewHolder(v);
        return new eventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(eventViewHolder holder, int position) {
        holder.tvEvento.setText(eventList.get(position).getEvento());
        holder.tvEventoFecha.setText(eventList.get(position).getFecha());
        holder.tvEventoHora.setText(eventList.get(position).getHora());
        holder.tvEventoDescrip.setText(eventList.get(position).getDescripcion());
        holder.tvTel.setText(eventList.get(position).getTel());
        holder.tvContacto.setText(eventList.get(position).getContacto());
        holder.cardView.setTag(position);
        //holder
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class eventViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private CardView cardView;
        private TextView tvEvento;
        private TextView tvEventoFecha;
        private TextView tvEventoHora;
        private TextView tvEventoDescrip;
        private TextView tvTel;
        private TextView tvContacto;

        int position = -1;

        public eventViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardEvent);
            tvEvento = (TextView) itemView.findViewById(R.id.tvEvento);
            tvEventoFecha = (TextView) itemView.findViewById(R.id.tvEventoFecha);
            tvEventoHora = (TextView) itemView.findViewById(R.id.tvEventoHora);
            tvEventoDescrip = (TextView) itemView.findViewById(R.id.tvEventoDescrip);
            tvTel = (TextView) itemView.findViewById(R.id.tvTel);
            tvContacto = (TextView) itemView.findViewById(R.id.tvContacto);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            position = (int) v.getTag();

            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem call = menu.add(Menu.NONE, 3, 3, "Call");
            MenuItem sms = menu.add(Menu.NONE, 4, 4, "SMS");
            edit.setOnMenuItemClickListener(listenerOnEditMenu);
            delete.setOnMenuItemClickListener(listenerOnDeleteMenu);
            call.setOnMenuItemClickListener(listenerOnCallMenu);
            sms.setOnMenuItemClickListener(listenerOnSMSMenu);
        }

        MenuItem.OnMenuItemClickListener listenerOnEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                Event eventPressed = eventList.get(position);

                Intent edit= new Intent(context, EditEvent.class);

                edit.putExtra("id",eventPressed.getIdEvento());
                edit.putExtra("editar",eventPressed.getEvento());
                context.startActivity(edit);

                return false;
            }
        };

        MenuItem.OnMenuItemClickListener listenerOnDeleteMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Event eventPressed = eventList.get(position);


                DBEvent objDB=new DBEvent(context,"CURSO", null,1);
                SQLiteDatabase DB=objDB.getWritableDatabase();

                DB.execSQL("DELETE FROM event WHERE idevent='"+eventPressed.getIdEvento()+"'");
                DB.close();

                ((MainActivity) context).consultarBD();

                return false;
            }
        };

        MenuItem.OnMenuItemClickListener listenerOnCallMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Event eventPressed = eventList.get(position);

                Log.v("call!", eventList.get(position).getDescripcion());

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + eventPressed.getTel()));

                context.startActivity(intent);


                return false;
            }
        };

        MenuItem.OnMenuItemClickListener listenerOnSMSMenu=new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Event eventPressed = eventList.get(position);

              /*  SmsManager sms= SmsManager.getDefault();
                sms.sendTextMessage(eventPressed.getTel(),null,"Tienes un evento! " + eventPressed.getEvento() + " " + eventPressed.getFecha() + " "+eventPressed.getHora(),null,null);
*/

              Intent i = new Intent(Intent.ACTION_VIEW);
                i.putExtra("address",eventPressed.getTel());
                i.putExtra("sms_body","Tienes un evento! " + eventPressed.getEvento() + " " + eventPressed.getFecha() + " "+eventPressed.getHora());
                i.setType("vnd.android-dir/mms-sms");
                context.startActivity(i);

                return false;
            }
        };
    }

}
