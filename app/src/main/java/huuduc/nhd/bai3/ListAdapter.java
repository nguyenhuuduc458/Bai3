package huuduc.nhd.bai3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends BaseAdapter {
    private final List<Note> items = new ArrayList<>();
    private final Context mContext;
    private final List<Note> filterArray = new ArrayList<>();
    CreateXMLFile xml = new CreateXMLFile();

    public ListAdapter(Context context){
        mContext = context;
    }

    public void add(Note note){
        items.add(note);
        filterArray.add(note);
        notifyDataSetChanged();
        Toast.makeText(mContext, "Add successfully ", Toast.LENGTH_SHORT).show();
    }
    public void addRange(List<Note> notes){
       items.addAll(notes);
       filterArray.addAll(notes);
       notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    public void clearItem(int pos){
        items.remove(pos);
        filterArray.remove(pos);
        notifyDataSetChanged();
        Toast.makeText(mContext, "Item in " + pos + " position has been removed", Toast.LENGTH_SHORT).show();
    }


    public void editItem(int pos, Note note){
       items.get(pos).setContent(note.getContent());
       items.get(pos).setTitle(note.getTitle());
       notifyDataSetChanged();
       Toast.makeText(mContext, "Edit successfully", Toast.LENGTH_SHORT).show();
    }

    public List<Note> getAllListNote(){
        return this.items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    public class ViewHolder{
        TextView mTitle;
        TextView mContent;
        TextView mDate;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View row = view;

        if(row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.note,null);

            viewHolder = new ViewHolder();
            Note note = (Note) getItem(i);

            viewHolder.mTitle   = row.findViewById(R.id.showTitle);
            viewHolder.mContent = row.findViewById(R.id.showContent);
            viewHolder.mDate    = row.findViewById(R.id.showDate);

            viewHolder.mTitle.setText(note.getTitle());
            viewHolder.mContent.setText(note.getContent());
            viewHolder.mDate.setText("Last modified: " + Note.FORMAT.format(note.getDate()));

            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }

        return row;
    }

    public void filter(String text){
        items.clear();
        if(text.length() == 0){
            addRange(filterArray);
            notifyDataSetChanged();
        }else{
            for(Note item : this.filterArray){
                if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
