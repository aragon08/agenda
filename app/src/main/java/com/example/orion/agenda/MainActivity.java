package com.example.orion.agenda;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    ArrayList<Event> eventList=new ArrayList<Event>();

    DBEvent objDB;
    SQLiteDatabase DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //esto crea el recycler view
        recycler=(RecyclerView) findViewById(R.id.reciclador);
        lManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(lManager);

        //esto crea el adapter con la lista de eventos
        adapter=new EventAdapter(this,eventList);

        //y esto setea el adapter al recyclerview
        recycler.setAdapter(adapter);
    }

    public void consultarBD() {

        objDB=new DBEvent(this,"CURSO",null,1);
        DB=objDB.getWritableDatabase();

        eventList.clear();

        Cursor c=DB.rawQuery("SELECT * FROM event ORDER BY event", null);
        if (c.moveToFirst()){
            do{
                Event temporal= new Event();

                temporal.setIdEvento(c.getString(0));
                temporal.setEvento(c.getString(1));
                temporal.setFecha(c.getString(2));
                temporal.setHora(c.getString(3));
                temporal.setDescripcion(c.getString(4));
                temporal.setContacto(c.getString(5));
                temporal.setTel(c.getString(6));

                //aqui se agrega el temporal a la lista de eventos
                eventList.add(temporal);

            }while (c.moveToNext());

            DB.close();
        }else{
            Toast.makeText(getApplicationContext(), "No hay eventos",Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        consultarBD();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itmAddEvent:
                Intent intent1= new Intent(this,NewEvent.class);
                startActivity(intent1);

                return true;
            case R.id.itmAbout:
                Intent intent= new Intent(this,About.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
