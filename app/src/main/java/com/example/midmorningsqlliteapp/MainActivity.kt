package com.example.midmorningsqlliteapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.method.TextKeyListener.clear
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtNumber = findViewById(R.id.mEdtNumber)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        // Create a database called eMobilis08
        db = openOrCreateDatabase("eMobilis08", Context.MODE_PRIVATE, null)
        // Create a table users in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(lina VARCHAR, arafa VARCH, kitambulisho VARCHAR)")
        //Set onClick listeners to buttons
        btnSave.setOnClickListener {
            //Receive the data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtNumber.text.toString().trim()
            // check if the user is submitting empty fields
            if (name.isEmpty()||email.isEmpty()||idNumber.isEmpty()){
                //display an error message
                message("Empty fields","please fill all inputs")
            }else{
                //proceed to save the data
                db.execSQL("INSERT INTO users VALUES('"+name+"', '"+email+"','"+idNumber+"')")
                clear()
                message("SUCCESS","USER saced successfully")
            }
            btnView.setOnClickListener {
                var cursor =db.rawQuery("SELECT * FROM users",null)

                //check if there is any record inthe db
                if (cursor.count == 0){
                    message("No results","sorry no users found")
                }else{
                    //use string buffer to append all the available records using a loop
                    var buffer = StringBuffer()
                    while (cursor.moveToNext()){
                        var retrievedName = cursor.getString(0)
                        var retrievedEmail = cursor.getString(1)
                        var retrievedIdNumber = cursor.getString(2)
                        buffer.append(retrievedName+"\n")
                        buffer.append(retrievedEmail+"\n")
                        buffer.append(retrievedIdNumber+"\n")
                    }
                    message("USERS",buffer.toString())
                }
            }
            btnDelete.setOnClickListener {
                val idNumber =edtNumber.text.toString().trim()
                if (idNumber.isEmpty()){
                    message("EMPTY FIELDS","PLEASE ENTER AN ID NUMBER")
                }else{
                    //use cursor to select the user with given ID
                    var cursor =db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNumber+"'",null)
                    // check if the record exists
                    if (cursor.count ==0){
                        message("no records","sorry no user with ID"+idNumber)
                    }else{
                        //proceed to delete the user
                        db.execSQL("DELETE FROM users WHERE kitambulisho= '"+idNumber+"'")
                        clear()
                        message("success","user deleted successfully")

                    }

                }
            }
        }

        fun message(title:String, message:String){
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(title)
            alertDialog.setMessage(message)
            alertDialog.setPositiveButton("Cancel", null)
            alertDialog.create().show()
        }
        fun clear(){
            edtName.setText("")
            edtEmail.setText("")
            edtNumber.setText("")

        }
    }