package theleatherguy.epiandroid.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListEventsHomeAdapter;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class HomeToday extends Fragment
{
	private ListView    listToday;
	private String                      _token;
	private List<Infos.Board.Event>     _today;
	private View                        rootView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_today_home, container, false);
		super.onCreate(savedInstanceState);

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
		RequestParams params = new RequestParams();
		params.put("token", _token);

		EpitechRest.get("infos", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.FRANCE);
				Calendar t = Calendar.getInstance();
				_today = new ArrayList<>();
				Infos infos = new Gson().fromJson(response.toString(), Infos.class);
				for (Infos.Board.Event event:infos.board.activites)
				{
					if (event.date_inscription.equals("false"))
					{
						try
						{
							Calendar c = Calendar.getInstance();
							c.setTime(format.parse(event.timeline_start));
							if (c.get(Calendar.DAY_OF_MONTH) == t.get(Calendar.DAY_OF_MONTH) || event.token.equals("1"))
								_today.add(event);
						} catch (ParseException e)
						{
							Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				}
				listToday.setAdapter(new ListEventsHomeAdapter(getActivity(), _today, _token));
				rootView.findViewById(R.id.card_today).setVisibility(View.VISIBLE);
				rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (statusCode >= 400 && statusCode < 500)
					Toast.makeText(getActivity().getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
				else if (statusCode >= 500)
					Toast.makeText(getActivity().getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getActivity().getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
			}

		});
	}
}
