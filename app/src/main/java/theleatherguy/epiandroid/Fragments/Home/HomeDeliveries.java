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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListDeliveriesHomeAdapter;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class HomeDeliveries extends Fragment
{
	private String                      _token;
	private ListView                    listDeliveries;
	private List<Infos.Board.Project>   _deliveries;
	private View                        rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_deliveries_home, container, false);
		super.onCreate(savedInstanceState);

		Intent intent = getActivity().getIntent();
		if (intent != null)
			_token = intent.getStringExtra("token");

		listDeliveries = (ListView) rootView.findViewById(R.id.listDeliveries);
		listDeliveries.setEmptyView(rootView.findViewById(R.id.emptyDeliveries));

		getWeekDelivery();

		return (rootView);
	}

	private void getWeekDelivery()
	{
		RequestParams params = new RequestParams();
		params.put("token", _token);

		EpitechRest.get("projects", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response)
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				Calendar t = Calendar.getInstance();
				t.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				List<Infos.Board.Project> projs = new Gson().fromJson(response.toString(), new TypeToken<List<Infos.Board.Project>>(){}.getType());
				_deliveries = new ArrayList<>();
				for (Infos.Board.Project proj:projs)
				{
					if (proj.registered == 1
							&& (proj.type_acti.equals("Mini-Projets")
							|| proj.type_acti.equals("Projet")))
					{
						Log.d(this.getClass().getName(), "END_ACTI :: " + proj.end_acti);
						try
						{
							Calendar c = Calendar.getInstance();
							c.setTime(format.parse(proj.end_acti));
							if (c.get(Calendar.DAY_OF_YEAR) == t.get(Calendar.DAY_OF_YEAR))
								_deliveries.add(proj);
						}
						catch (ParseException e)
						{
							Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				}
				listDeliveries.setAdapter(new ListDeliveriesHomeAdapter(getActivity(), _deliveries));
				rootView.findViewById(R.id.card_deliveries).setVisibility(View.VISIBLE);
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
