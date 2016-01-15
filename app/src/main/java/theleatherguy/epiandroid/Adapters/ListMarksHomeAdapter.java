package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.R;

public class ListMarksHomeAdapter extends ArrayAdapter<Infos.Board.Mark>
{
	public ListMarksHomeAdapter(Context context, List<Infos.Board.Mark> listMarks)
	{
		super(context, 0, listMarks);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_mark_home, parent, false);

		MarkViewHolder viewHolder = (MarkViewHolder) convertView.getTag();
		if (viewHolder == null)
		{
			viewHolder = new MarkViewHolder();
			viewHolder.proj = (TextView) convertView.findViewById(R.id.textMark);
			viewHolder.mark = (TextView) convertView.findViewById(R.id.textMark);
			convertView.setTag(viewHolder);
		}

		Infos.Board.Mark mark = getItem(position);

		viewHolder.proj.setText(mark.title);
		viewHolder.mark.setText(mark.note);

		return convertView;
	}

	private class MarkViewHolder
	{
		public TextView proj;
		public TextView mark;
	}
}
