package theleatherguy.epiandroid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.User;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;
import theleatherguy.epiandroid.Utils.RoundedAvatar;

public class Profile extends Fragment
{
	private View        rootView;
	private ImageView   profilePicture;
	private TextView    textFullName;
	private TextView    textYear;
	private TextView    textCity;

	private TextView    textGPA;
	private ProgressBar barGPA;

	private TextView    textCredits;
	private ProgressBar barCredits;
	private TextView    textCreditsMin;
	private TextView    textCreditsMax;

	private TextView    textLog;
	private ProgressBar barLog;
	private TextView    textLogMax;

	private String      token;
	private String      user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.rootView = inflater.inflate(R.layout.fragment_profile, container, false);

		this.rootView.findViewById(R.id.profilePanel).setVisibility(View.GONE);
		this.rootView.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

		Intent intent = getActivity().getIntent();
		if (intent != null)
		{
			this.token = intent.getStringExtra("token");
			this.user = intent.getStringExtra("user");
			if (this.user == null)
				this.user = "";
		}

		profilePicture = (ImageView) rootView.findViewById(R.id.profilePicture);
		textFullName = (TextView) rootView.findViewById(R.id.textFullName);
		textYear = (TextView) rootView.findViewById(R.id.textYearCourse);
		textCity = (TextView) rootView.findViewById(R.id.textCity);
		textGPA = (TextView) rootView.findViewById(R.id.textGPA);
		barGPA = (ProgressBar) rootView.findViewById(R.id.barGPA);
		textCredits = (TextView) rootView.findViewById(R.id.textCredits);
		barCredits = (ProgressBar) rootView.findViewById(R.id.barCredits);
		textCreditsMin = (TextView) rootView.findViewById(R.id.textCreditsMin);
		textCreditsMax = (TextView) rootView.findViewById(R.id.textCreditsMax);
		textLog = (TextView) rootView.findViewById(R.id.textLog);
		barLog = (ProgressBar) rootView.findViewById(R.id.barLog);
		textLogMax = (TextView) rootView.findViewById(R.id.textLogMax);

		getProfile();

		return (rootView);
	}

	private void getProfile()
	{
		RequestParams params = new RequestParams();

		params.put("token", this.token);
		params.put("user", this.user);

		EpitechRest.get("user", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				if (getActivity() != null)
				{
					User user = new Gson().fromJson(response.toString(), User.class);

					Picasso.with(getContext()).load(user.picture).resize(100, 100).transform(new RoundedAvatar()).into(profilePicture);

					if (user.firstname != null && user.lastname != null)
					{
						String name = user.firstname.substring(0, 1).toUpperCase() + user.firstname.substring(1) + " "
								+ user.lastname.substring(0, 1).toUpperCase() + user.lastname.substring(1);
						textFullName.setText(name);
					}

					if (user.studentyear != null)
					{
						String number;
						switch (user.studentyear)
						{
							case 1:
								number = "st";
								break;
							case 2:
								number = "nd";
								break;
							case 3:
								number = "rd";
								break;
							default:
								number = "th";
						}
						Spanned fHtml = Html.fromHtml(user.studentyear.toString() + "<sup><small>" + number + "</small></sup> Year");
						textYear.setText(fHtml);
					}

					if (user.location != null)
					{
						String city;
						switch (user.location)
						{
							case "FR/PAR":
								city = "Paris";
								break;
							case "FR/BDX":
								city = "Bordeaux";
								break;
							case "FR/LIL":
								city = "Lille";
								break;
							case "FR/LYN":
								city = "Lyon";
								break;
							case "FR/MAR":
								city = "Marseille";
								break;
							case "FR/MPL":
								city = "Montpellier";
								break;
							case "FR/NCY":
								city = "Nancy";
								break;
							case "FR/NAN":
								city = "Nantes";
								break;
							case "FR/NCE":
								city = "Nice";
								break;
							case "FR/REN":
								city = "Rennes";
								break;
							case "FR/STG":
								city = "Strasbourg";
								break;
							case "FR/TLS":
								city = "Toulouse";
								break;
							default:
								city = "Palet Town";
						}
						textCity.setText(city);
					}

					if (user.gpa != null)
					{
						String GPA = "GPA : " + user.gpa.get(0).gpa;
						textGPA.setText(GPA);
						Double gpa = Double.parseDouble(user.gpa.get(0).gpa) * 100;
						barGPA.setProgress(gpa.intValue());
					}

					if (user.credits != null)
					{
						String credits = "Credits : " + Integer.toString(user.credits);
						textCredits.setText(credits);
						Integer creds = user.credits - (60 * (user.studentyear - 1));
						barCredits.setProgress(creds);
						creds = 60 * (user.studentyear - 1);
						String credsMin = Integer.toString(creds);
						String credsMax = Integer.toString(creds + 60);
						textCreditsMin.setText(credsMin);
						textCreditsMax.setText(credsMax);
					}

					if (user.nsstat != null)
					{
						String log = "Log : " + Double.toString(user.nsstat.active) + "h";
						textLog.setText(log);
						barLog.setMax(user.nsstat.nslog_norm.intValue());
						barLog.setProgress(user.nsstat.active.intValue());
						String logMax = Integer.toString(user.nsstat.nslog_norm.intValue());
						textLogMax.setText(logMax);
					}

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
				}
			}

		});
	}
}
