package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import theleatherguy.epiandroid.Beans.Alert;
import theleatherguy.epiandroid.R;

public class ListAlertsNotifsAdapter extends ArrayAdapter<Alert>
{
	public ListAlertsNotifsAdapter(Context context, List<Alert> listAlerts)
	{
		super(context, 0, listAlerts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_alert_notif, parent, false);

		ProjectViewHolder viewHolder = (ProjectViewHolder) convertView.getTag();
		if (viewHolder == null)
		{
			viewHolder = new ProjectViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.textAlert);
			convertView.setTag(viewHolder);
		}

		Alert alrt = getItem(position);

		viewHolder.title.setText(Html.fromHtml(alrt.title));

		return convertView;
	}

	private class ProjectViewHolder
	{
		public TextView     title;
	}
}
