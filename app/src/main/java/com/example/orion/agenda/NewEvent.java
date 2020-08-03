package com.example.orion.agenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
//import android.icu.util.Calendar;
import java.util.Calendar;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEvent extends AppCompatActivity {

    private int dia, mes, ano, hora, minutos;
    Calendar c;
    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    DBEvent objDB;
    SQLiteDatabase DB;

    //BDEvento objDB;
    //SQLiteDatabase DB;

    @BindView(R.id.etNewEvent)
    EditText etNewEvent;

    @BindView(R.id.etDate)
    EditText etDate;

    @BindView(R.id.etTime)
    EditText etTime;

    @BindView(R.id.etDescrip)
    EditText etDescrip;

    @BindView(R.id.etContact)
    EditText etContact;

    @BindView(R.id.etTelContact)
    EditText etTelContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);
        objDB=new DBEvent(this,"CURSO", null,1);
        DB=objDB.getWritableDatabase();
    }

    @OnClick({R.id.btnDate, R.id.btnTime, R.id.btnAddContact, R.id.btnAddEvent})
    public void botones(View v) {
        switch (v.getId()) {
            case R.id.btnDate:
                c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, ano,mes,dia);
                datePickerDialog.show();
                break;
            case R.id.btnTime:
                c = Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTime.setText(hourOfDay + ":" + minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.show();
                break;
            case R.id.btnAddContact:
                Intent intent = new Intent(Intent.ACTION_PICK);
                //intent.setType(ContactsContract.Data.);
                //intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                    break;
                }
            case R.id.btnAddEvent:
                String event=etNewEvent.getText().toString();
                String date=etDate.getText().toString();
                String time=etTime.getText().toString();
                String descrip=etDescrip.getText().toString();
                String contact=etContact.getText().toString();
                String phone=etTelContact.getText().toString();

                String Qry = "INSERT INTO event(event,dateEvent,timeEvent,descrip,contact,phone) VALUES('"+event+"','"+date+"','"+time+"','"+descrip+"','"+contact+"','"+phone+"') ";
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
                etContact.setText(contact);
                etTelContact.setText(number);
                //etEmail.setText(mail);

            }


        }
    }
}
