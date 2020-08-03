package com.example.orion.agenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditEvent extends AppCompatActivity {

    private Event evento;

    private int dia, mes, ano, hora, minutos;
    Calendar c;
    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    DBEvent objDB;
    SQLiteDatabase DB;

    //BDEvento objDB;
    //SQLiteDatabase DB;

    @BindView(R.id.etEditEvent)
    EditText etEditEvent;

    @BindView(R.id.etEditDate)
    EditText etEditDate;

    @BindView(R.id.etEditTime)
    EditText etEditTime;

    @BindView(R.id.etEditDescrip)
    EditText etEditDescrip;

    @BindView(R.id.etEditContact)
    EditText etEditContact;

    @BindView(R.id.etEditTelContact)
    EditText etEditTelContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);

        objDB=new DBEvent(this,"CURSO", null,1);
        DB=objDB.getWritableDatabase();

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String idEvento = extras.getString("id");
            if(idEvento!=null){

                String[] args = new String[] { idEvento};
                Cursor c = DB.rawQuery("select * from EVENT where idevent = ? LIMIT 1",args);

                if (c.moveToFirst()){
                    do{
                        evento = new Event();

                        evento.setIdEvento(c.getString(0));
                        evento.setEvento(c.getString(1));
                        evento.setFecha(c.getString(2));
                        evento.setHora(c.getString(3));
                        evento.setDescripcion(c.getString(4));
                        evento.setContacto(c.getString(5));
                        evento.setTel(c.getString(6));

                    }while (c.moveToNext());

                    DB.close();
                }
            }

            if(evento != null){
                etEditEvent.setText(evento.getEvento());
                etEditDate.setText(evento.getFecha());
                etEditTime.setText(evento.getHora());
                etEditDescrip.setText(evento.getDescripcion());
                etEditContact.setText(evento.getContacto());
                etEditTelContact.setText(evento.getTel());

            }
        }

    }

    @OnClick({R.id.btnEditDate, R.id.btnEditTime, R.id.btnEditAddContact, R.id.btnEditAddEvent})
    public void botones(View v) {
        switch (v.getId()) {
            case R.id.btnEditDate:
                c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etEditDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
                break;
            case R.id.btnEditTime:
                c = Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etEditTime.setText(hourOfDay + ":" + minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.show();
                break;
            case R.id.btnEditAddContact:
                Intent intent = new Intent(Intent.ACTION_PICK);
                //intent.setType(ContactsContract.Data.);
                //intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                    break;
                }
            case R.id.btnEditAddEvent:
                //tengo que revisar esto
                String event=etEditEvent.getText().toString();
                String date=etEditDate.getText().toString();
                String time=etEditTime.getText().toString();
                String descrip=etEditDescrip.getText().toString();
                String contact=etEditContact.getText().toString();
                String phone=etEditTelContact.getText().toString();

                objDB=new DBEvent(this,"CURSO", null,1);
                DB=objDB.getWritableDatabase();

                String Qry = "UPDATE event set event = '"+event+"', dateEvent = '"+date+"', timeEvent = '"+time+"',descrip = '"+descrip+"',contact='"+contact+"',phone = '"+phone+"' where idevent = '"+evento.getIdEvento()+"'";
                DB.execSQL(Qry);
                Toast.makeText(this, "Evento Agregado", Toast.LENGTH_SHORT).show();

                DB.close();

                finish();

                break;
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.Data.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Email.ADDRESS};
            Cursor cursor = getContentResolver().query(contactUri, projection,null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int contactIndex =cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                //int emailIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                String contact= cursor.getString(contactIndex);
                String number = cursor.getString(numberIndex);
                //String mail=cursor.getString(emailIndex);
                etEditContact.setText(contact);
                etEditTelContact.setText(number);
                //etEmail.setText(mail);

            }


        }
    }

}
