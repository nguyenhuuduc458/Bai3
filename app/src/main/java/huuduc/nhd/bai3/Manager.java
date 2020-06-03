package huuduc.nhd.bai3;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;


public class Manager extends ListActivity implements SearchView.OnQueryTextListener {

    public enum sortType{
        ASC_NOTE,
        DESC_NOTE
    }

    private static final int ADD_ITEM_REQUEST = 0;
    private static final int EDIT_ITEM_REQUEST = 1;
    private static final String TAG = "Note of user";
    private static sortType flag = sortType.ASC_NOTE;

    ListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ListAdapter(this);

        setListAdapter(mAdapter);

        registerForContextMenu(getListView());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ADD_ITEM_REQUEST && data != null){
            Note note = new Note(data);
            mAdapter.add(note);
        }else if(resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST && data != null){
            Note note = new Note(data);
            mAdapter.editItem(Integer.parseInt(data.getStringExtra(Note.POSITION)),note);
            setListAdapter(mAdapter);
        }else {
            // do nothing in here
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_option,menu);

        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        SearchView searchView       = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        mAdapter.filter(text);
        setListAdapter(mAdapter);
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleteAllItem: {
                mAdapter.clear();
                return true;
            }
            case R.id.dumpLog:{
                dump();
                return true;
            }
            case R.id.addItem:{
                addItem();
                return true;
            }
            case R.id.sortTitleItem: {
                sortItem();
                return true;
            }
            default:
                return  super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == getListView().getId()){
            getMenuInflater().inflate(R.menu.menu_context,menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteItem:{
                deleteItem(item);
                return true;
            }
            case R.id.editItem:{
                editItem(item);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void dump()
    {
        for (int i = 0 ; i< mAdapter.getCount(); i++){
            String data =  ((Note) mAdapter.getItem(i)).toLog();
            Log.i(TAG,	"Item " + i + ": " + data.replace(Note.ITEM_SEP, ","));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getCount() == 0){
            loadItems();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFileXML();
    }

    protected void saveFileXML(){
        CreateXMLFile xml = new CreateXMLFile();
        xml.saveXMLFile(mAdapter.getAllListNote());
    }

    protected void loadItems(){
        CreateXMLFile xml = new CreateXMLFile();
        mAdapter.addRange(xml.loadXMLFile());
        mAdapter.addRangeForFilter(xml.loadXMLFile());
        setListAdapter(mAdapter);
    }

    protected void deleteItem(final MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setMessage("Are you sure to delete item?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mAdapter.clearItem(info.position);
                setListAdapter(mAdapter);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void editItem(final MenuItem item){
        Intent intent = new Intent(this, AddNote.class);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Note note = (Note) mAdapter.getItem(info.position);
        Note.packageIntent(intent,note.getTitle(), note.getContent(),info.position);
        startActivityForResult(intent,EDIT_ITEM_REQUEST);
    }

    protected void addItem(){
        Intent intent = new Intent(getApplicationContext(),AddNote.class);
        startActivityForResult(intent,ADD_ITEM_REQUEST);
    }

    protected void sortItem(){
        if(flag == sortType.ASC_NOTE){
            flag = sortType.DESC_NOTE;
            Collections.sort(mAdapter.getAllListNote());
        }else if(flag ==  sortType.DESC_NOTE){
            flag = sortType.ASC_NOTE;
            Collections.sort(mAdapter.getAllListNote(), Collections.reverseOrder());
        }
        setListAdapter(mAdapter);
    }

}
