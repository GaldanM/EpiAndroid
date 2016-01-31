package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import theleatherguy.epiandroid.Beans.Message;
import theleatherguy.epiandroid.R;

public class ListMessagesNotifsAdapter extends ArrayAdapter<Message>
{
	public ListMessagesNotifsAdapter(Context context, List<Message> listMessages)
	{
		super(context, 0, listMessages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_notif, parent, false);

		ProjectViewHolder viewHolder = (ProjectViewHolder) convertView.getTag();
		if (viewHolder == null)
		{
			viewHolder = new ProjectViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.textTitle);
			viewHolder.content = (TextView) convertView.findViewById(R.id.textContent);
			viewHolder.date = (TextView) convertView.findViewById(R.id.textDate);
			convertView.setTag(viewHolder);
		}

		Message msg = getItem(position);

		viewHolder.title.setText(Html.fromHtml(msg.title));
		viewHolder.content.setText(Html.fromHtml(msg.content));
		viewHolder.date.setText(msg.date);

		return convertView;
	}

	private class ProjectViewHolder
	{
		public TextView     title;
		public TextView     content;
		public TextView     date;
	}
}
