package theleatherguy.epiandroid.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListEventsHomeAdapter;
import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class HomeToday extends Fragment
{
	private ListView    listToday;
	private String                      _token;
	private List<Event>     _today;
	private View                        rootView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_today_home, container, false);

		Intent intent = getActivity().getIntent();
		if (intent != null)
			_token = intent.getStringExtra("token");

		listToday = (ListView) rootView.findViewById(R.id.listToday);
		listToday.setEmptyView(rootView.findViewById(R.id.emptyToday));

		getToday();

		return (rootView);
	}

	private void getToday()
	{
		final RequestParams params = new RequestParams();
		Calendar start = Calendar.getInstance();
		String today = Integer.toString(start.get(Calendar.YEAR)) + "-"
				+ String.format("%02d", start.get(Calendar.MONTH) + 1) + "-"
				+ Integer.toString(start.get(Calendar.DAY_OF_MONTH));
		params.put("token", _token);
		params.put("start", today);
		params.put("end", today);

		EpitechRest.get("planning", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response)
			{
				if (getActivity() != null)
				{
					List<Event> events = new Gson().fromJson(response.toString(), new TypeToken<List<Event>>(){}.getType());
					_today = new ArrayList<>();
					for (Event event : events)
					{
						if (event.event_registered.equals("registered"))
							_today.add(event);
					}
					listToday.setAdapter(new ListEventsHomeAdapter(getActivity(), _today, _token));
					rootView.findViewById(R.id.card_today).setVisibility(View.VISIBLE);
					rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (getActivity() != null)
				{
					if (statusCode >= 400 && statusCode < 500)
						Toast.makeText(getActivity().getApplicationContext(), "Error from dev TODAY, soz !", Toast.LENGTH_LONG).show();
					else if (statusCode >= 500)
						Toast.makeText(getActivity().getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
					rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
				}
			}

		});
	}
}
