package huuduc.nhd.bai3;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Comparable<Note>{
    public static final String ITEM_SEP = System.getProperty("line.separator");

    private String title;
    private String content;
    private Date date = new Date();

    public final static String TITLE = "title";
    public final static String CONTENT = "content";
    public final static String POSITION = "position";
    public final static String DATE = "date";
    public final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    public Note(){}

    public Note(String title, String content, Date date){
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Note(Intent intent){
        this.title = intent.getStringExtra(Note.TITLE);
        this.content = intent.getStringExtra(Note.CONTENT);
        try {
            this.date = Note.FORMAT.parse(intent.getStringExtra(Note.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getTitle(){ return this.title; }

    public void setTitle(String title){ this.title = title; }

    public String getContent(){ return this.content; }

    public void setContent(String content){ this.content = content; }

    public Date getDate(){return this.date;}

    public void setDate(Date date){this.date = date;}

    public static void packageIntent(Intent intent, String title, String content, String date){
        intent.putExtra(Note.TITLE, title);
        intent.putExtra(Note.CONTENT,content);
        intent.putExtra(Note.DATE, date);
    }

    public static void packageIntent(Intent intent,String title, String content, int position){
        intent.putExtra(Note.TITLE, title);
        intent.putExtra(Note.CONTENT, content);
        intent.putExtra(Note.POSITION, String.valueOf(position));
    }
    public String toLog(){
        return "Title: " + this.title  + " " +  "Content: " + this.content + "Last Modified: " + Note.FORMAT.format(this.date);
    }

    @Override
    public int compareTo(Note note) {
        return this.title.compareTo(note.getTitle());
    }
}
