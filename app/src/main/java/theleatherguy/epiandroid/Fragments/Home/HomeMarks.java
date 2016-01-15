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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListMarksHomeAdapter;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class HomeMarks extends Fragment
{
	private ListView    listMarks;
	private String                      _token;
	private List<Infos.Board.Mark>      _marks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_marks_home, container, false);
		super.onCreate(savedInstanceState);

		Intent intent = getActivity().getIntent();
		if (intent != null)
			_token = intent.getStringExtra("token");

		listMarks = (ListView) rootView.findViewById(R.id.listMarks);
		listMarks.setEmptyView(rootView.findViewById(R.id.emptyMarks));

		getMarks();

		Toast.makeText(getActivity().getApplicationContext(), _token, Toast.LENGTH_LONG).show();

		return (rootView);
	}

	private void getMarks()
	{
		RequestParams params = new RequestParams();
		params.put("token", _token);

		EpitechRest.get("infos", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				_marks = new ArrayList<>();
				Infos infos = new Gson().fromJson(response.toString(), Infos.class);
				for (Infos.Board.Mark mark:infos.board.notes)
				{
					_marks.add(mark);
				}
				listMarks.setAdapter(new ListMarksHomeAdapter(getActivity(), _marks));
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
