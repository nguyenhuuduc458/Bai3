package huuduc.nhd.bai3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNote extends Activity {

    private EditText mTitleText, mContentText;
    private Button mResetButton, mSubmitButton, mCancelButton;
    private int edit_position = 0;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        maping();
        
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleText.setText("");
                mContentText.setText("");
            }
        });
        
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title   = mTitleText.getText().toString().trim();
                String content = mContentText.getText().toString().trim();
                if(title.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                    mTitleText.requestFocus();
                }else if(content.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter content", Toast.LENGTH_SHORT).show();
                    mContentText.requestFocus();
                }else{
                    Intent intent = new Intent();
                    Note.packageIntent(intent,title,content,getCurrentDate());
                    if(editData()){
                        intent.putExtra(Note.ID,String.valueOf(id));
                        intent.putExtra(Note.POSITION,String.valueOf(edit_position));
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        editData();

    }
    private void maping(){
        mTitleText    = (EditText) findViewById(R.id.title);
        mContentText  = (EditText) findViewById(R.id.content);
        mResetButton  = (Button)   findViewById(R.id.resetButton);
        mCancelButton = (Button)   findViewById(R.id.cancelButton);
        mSubmitButton = (Button)   findViewById(R.id.submitButton);
    }

    private boolean editData(){
       Intent data = getIntent();
       if(data.getStringExtra(Note.ID) != null){
           Note note = new Note(data);
           mTitleText.setText(note.getTitle());
           mContentText.setText(note.getContent());
           edit_position = Integer.parseInt(data.getStringExtra(Note.POSITION));
           id = note.getId();
           return true;
       }else{
           return false;
       }
    }

    private String getCurrentDate(){
       try{
           Calendar cal = Calendar.getInstance();
           int day      = cal.get(Calendar.DAY_OF_MONTH);
           int month    = cal.get(Calendar.MONTH) + 1;
           int year     = cal.get(Calendar.YEAR);
           return day + "-" + month + "-" + year;
       }catch(Exception e){
           e.printStackTrace();
           return null;
       }
    }

}
