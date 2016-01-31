package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.R;

/**
 * Created by olivier.medec on 31/01/2016.
 */
public class ListPlanning extends BaseAdapter {
    private List<Event> _event;
    private LayoutInflater mInflater;
    private Context mContext;

    public ListPlanning(List<Event> event, Context context) {
        _event = event;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return _event.size();
    }

    @Override
    public Object getItem(int position) {
        return _event.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;
        TextView moduleName;

        layoutItem = (LinearLayout)mInflater.inflate(R.layout.item_list_planning, parent, false);
        moduleName = (TextView)layoutItem.findViewById(R.id.eventTitle);
        moduleName.setText(_event.get(position).titlemodule + " " + _event.get(position).acti_title);

        TextView horaire = (TextView)layoutItem.findViewById(R.id.horaire);
        horaire.setText(_event.get(position).start +  "-" +  _event.get(position).end);

        TextView room = (TextView)layoutItem.findViewById(R.id.room);
        if (_event.get(position).room != null)
            room.setText(_event.get(position).room.code);

        return layoutItem;
    }
}
