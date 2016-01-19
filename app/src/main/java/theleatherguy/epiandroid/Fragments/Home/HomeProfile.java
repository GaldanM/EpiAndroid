package theleatherguy.epiandroid.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class HomeProfile extends Fragment
{
	private View        rootView;
	private String      token;
	private String      login;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.rootView = inflater.inflate(R.layout.fragment_today_home, container, false);
		super.onCreate(savedInstanceState);

		Intent intent = getActivity().getIntent();
		if (intent != null)
		{
			this.token = intent.getStringExtra("token");
			this.login = intent.getStringExtra("login");
		}

		getProfile();

		return (rootView);
	}

	private void getProfile()
	{
		RequestParams params = new RequestParams();

		params.put("token", this.token);
		params.put("login", this.login);

		EpitechRest.get("infos", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				if (getActivity() != null)
				{
					Infos infos = new Gson().fromJson(response.toString(), Infos.class);
					//rootView.findViewById(R.id.profilePanel).setVisibility(View.VISIBLE);
					rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (getActivity() != null)
				{
					if (statusCode >= 400 && statusCode < 500)
						Toast.makeText(getActivity().getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
					else if (statusCode >= 500)
						Toast.makeText(getActivity().getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getActivity().getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
				}
			}

		});
	}
}
