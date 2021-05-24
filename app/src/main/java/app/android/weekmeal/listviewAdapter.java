package app.android.weekmeal;

import android.content.Context;
import android.text.Layout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class listviewAdapter extends ArrayAdapter<String> {

    List<String> arrayList;
    Context myContext;
    LayoutInflater inflater;
    private SparseBooleanArray selectedItemIds;

    public listviewAdapter(Context context, int id, List<String> list){
        super(context, id, list);
        myContext=context;
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareToIgnoreCase(t1);
            }
        });
        arrayList=list;
        selectedItemIds=new SparseBooleanArray();
        inflater = LayoutInflater.from(context);
    }
    private class viewHolder{
        ImageView imageView;
        TextView textView;
    }
    @Override
    public View getView(int position,View view,ViewGroup parent) {
        final listviewAdapter.viewHolder viewholder;

        if (view == null) {
            viewholder=new listviewAdapter.viewHolder();
            view = inflater.inflate(R.layout.listviewlayout, null);

            viewholder.textView = view.findViewById(R.id.textview_recipe);
            viewholder.imageView = view.findViewById(R.id.imageview_recipe);

            view.setTag(viewholder);

        } else
            viewholder = (listviewAdapter.viewHolder)  view.getTag();

        viewholder.textView.setText(arrayList.get(position));
        viewholder.imageView.setImageResource(android.R.drawable.btn_star);//need to be changed

        return view;
    }

    @Override
    public void remove(String object) {
        arrayList.remove(object);
        notifyDataSetChanged();
    }

    public  SparseBooleanArray getSelectedIds() {
        return selectedItemIds;
    }

    public void  toggleSelection(int position) {
        selectView(position, !selectedItemIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedItemIds.put(position,  value);
        else
            selectedItemIds.delete(position);

        notifyDataSetChanged();
    }
    public void  removeSelection() {
        selectedItemIds = new  SparseBooleanArray();
        notifyDataSetChanged();
    }
}
