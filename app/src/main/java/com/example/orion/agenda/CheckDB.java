package com.example.orion.agenda;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckDB extends AppCompatActivity {

    @BindView(R.id.lvEvent)
    ListView lvEvent;

    DBEvent objEvent;
    SQLiteDatabase DB;
    ArrayAdapter<String> valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_db);
        lvEvent=(ListView) findViewById(R.id.lvEvent);
        ButterKnife.bind(this);

        valor=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        objEvent=new DBEvent(this, "CURSO", null,1);

        DB=objEvent.getReadableDatabase();

        Cursor c=DB.rawQuery("SELECT * FROM event ORDER BY event", null);
        if (c.moveToFirst()){
            do{
                valor.add(c.getString(1)+""+c.getString(2)+""+c.getString(3)+""+c.getString(4)+""+c.getString(5)+""+c.getString(6));
            }while (c.moveToNext());
            lvEvent.setAdapter(valor);
        }else{
            Toast.makeText(getApplicationContext(), "No existe evento",Toast.LENGTH_SHORT).show();
            this.finish();
        }

        registerForContextMenu(lvEvent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        if (v.getId()==R.id.lvEvent){
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(lvEvent.getAdapter().getItem(info.position).toString());

            inflater.inflate(R.menu.menu_contextual,menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.mnuEdit:
                Toast.makeText(this,"Funciona",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.mnuDelete:
                Toast.makeText(this,"Funciona",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.mnuCall:
                //Toast.makeText(this,"Funciona",Toast.LENGTH_SHORT).show();
                try{
                    Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("'tel:'"+lvEvent));
                    startActivity(intent);
                }catch (SecurityException e){}

                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }
}
