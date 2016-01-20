package theleatherguy.epiandroid.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.User;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;
import theleatherguy.epiandroid.RoundedAvatar;

public class HomeProfile extends Fragment
{
	private ImageView   profilePicture;
	private TextView    textFullName;
	private TextView    textYear;

	private View        rootView;
	private String      token;
	private String      login;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.rootView = inflater.inflate(R.layout.fragment_profile_home, container, false);
		super.onCreate(savedInstanceState);

		Intent intent = getActivity().getIntent();
		if (intent != null)
		{
			this.token = intent.getStringExtra("token");
			this.login = intent.getStringExtra("login");
		}

		profilePicture = (ImageView) rootView.findViewById(R.id.profilePicture);
		textFullName = (TextView) rootView.findViewById(R.id.fullName);
		textYear = (TextView) rootView.findViewById(R.id.year);

		getProfile();

		return (rootView);
	}

	private void getProfile()
	{
		RequestParams params = new RequestParams();

		params.put("token", this.token);
		params.put("user", this.login);

		EpitechRest.get("user", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				if (getActivity() != null)
				{
					User user = new Gson().fromJson(response.toString(), User.class);

					Picasso.with(getContext()).load(user.picture).resize(100, 100).transform(new RoundedAvatar()).into(profilePicture);
					String name = WordUtils.capitalize(user.title);
					textFullName.setText(name);
					String year = user.studentyear.toString();
					switch (user.studentyear)
					{
						case 1:
							year += "st year";
							break;
						case 2:
							year += "nd year";
							break;
						case 3:
							year += "rd year";
							break;
						default:
							year += "th year";
					}
					textYear.setText(year);
					rootView.findViewById(R.id.profilePanel).setVisibility(View.VISIBLE);
					rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (getActivity() != null)
				{
					if (statusCode >= 400 && statusCode < 500)
						Toast.makeText(getContext(), "Error from dev from Profile, soz !", Toast.LENGTH_LONG).show();
					else if (statusCode >= 500)
						Toast.makeText(getContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
				}
			}

		});
	}
}
