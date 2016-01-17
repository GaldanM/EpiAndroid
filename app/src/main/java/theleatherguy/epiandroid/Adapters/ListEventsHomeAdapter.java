package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.Dialogs.TokenDialog;
import theleatherguy.epiandroid.R;

public class ListEventsHomeAdapter extends ArrayAdapter<Infos.Board.Event>
{
	EventViewHolder viewHolder;
	String          token;
	Context context;

	public ListEventsHomeAdapter(Context context, List<Infos.Board.Event> listEvents, String token)
	{
		super(context, 0, listEvents);
		this.context = context;
		this.token = token;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final Infos.Board.Event event = getItem(position);

		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_event_home, parent, false);

		viewHolder = (EventViewHolder) convertView.getTag();
		if (viewHolder == null)
		{
			viewHolder = new EventViewHolder();
			viewHolder.event = (TextView) convertView.findViewById(R.id.textEvent);
			viewHolder.module = (TextView) convertView.findViewById(R.id.textModule);
			viewHolder.date = (TextView) convertView.findViewById(R.id.textDate);
			viewHolder.salle = (TextView) convertView.findViewById(R.id.textSalle);
			viewHolder.token = (Button) convertView.findViewById(R.id.bToken);
			convertView.setTag(viewHolder);
		}

		try
		{
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.FRANCE);
			Calendar e = Calendar.getInstance();
			String date = "";

			e.setTime(format.parse(event.timeline_start));
			date += e.get(Calendar.DAY_OF_MONTH) + "/" + e.get(Calendar.MONTH) + 1 + " -> "
					+ String.format(Locale.FRANCE, "%02d:%02d", e.get(Calendar.HOUR_OF_DAY), e.get(Calendar.MINUTE)) + " - ";

			e.setTime(format.parse(event.timeline_end));
			date += String.format(Locale.FRANCE, "%02d:%02d", e.get(Calendar.HOUR_OF_DAY), e.get(Calendar.MINUTE));

			viewHolder.date.setText(date);
		}
		catch (ParseException er)
		{
			Log.d(this.getClass().getName(), er.getMessage());
		}

		viewHolder.event.setText(event.title);
		viewHolder.module.setText(event.module);
		viewHolder.salle.setText(event.salle);
		if (event.token.equals("1"))
		{
			viewHolder.token.setVisibility(View.VISIBLE);
			viewHolder.token.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					TokenDialog builder = new TokenDialog(context, token, event);
					builder.setTitle("Enter Token");
					AlertDialog mDialog = builder.create();
					mDialog.show();
				}
			});
		}
		return convertView;
	}

	private class EventViewHolder
	{
		public TextView event;
		public TextView module;
		public TextView date;
		public TextView salle;
		public Button   token;
	}
}
