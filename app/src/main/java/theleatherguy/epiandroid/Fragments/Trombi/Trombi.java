package theleatherguy.epiandroid.Fragments.Trombi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;
import theleatherguy.epiandroid.TrombiActivity;

public class Trombi extends Fragment
{
	private View        rootView;
	private View        loadingPanel;
	private TextView    logo;
	private EditText    userLogin;
	private Button      bSearch;

	private String      token;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_trombi, container, false);

		Intent intent = getActivity().getIntent();
		if (intent != null)
			this.token = intent.getStringExtra("token");

		logo = (TextView) rootView.findViewById(R.id.textTrombi);
		logo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway.ttf"));
		userLogin = (EditText) rootView.findViewById(R.id.editTrombi);
		bSearch = (Button) rootView.findViewById(R.id.bSearch);
		loadingPanel = rootView.findViewById(R.id.loadingPanel);
		loadingPanel.setVisibility(View.GONE);

		bSearch.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				bSearch.setEnabled(false);
				if (validateLogin(userLogin.getText().toString().trim()))
				{
					InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					View focus = getActivity().getCurrentFocus();
					if (focus != null)
						inputManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					loadingPanel.setVisibility(View.VISIBLE);
					getUser(userLogin.getText().toString().trim());
				}
				else
					userLogin.setError(getString(R.string.error_invalid_login));
			}
		});

		return (rootView);
	}

	private boolean validateLogin(String login)
	{
		return login.matches("[a-z]+?\\.[a-z]+@epitech\\.eu");
	}

	private void    getUser(final String login)
	{
		RequestParams params = new RequestParams();

		params.put("token", this.token);
		params.put("user", login);

		EpitechRest.get("user", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				String rep = response.toString().substring(2, 7);
				if (getActivity() != null
						&& !rep.equals("error"))
				{
						Intent intent = new Intent(getActivity().getApplicationContext(), TrombiActivity.class);
						intent.putExtra("user", login);
						intent.putExtra("token", token);
						startActivity(intent);
				}
				else
					userLogin.setError("No such user");
				loadingPanel.setVisibility(View.GONE);
				bSearch.setEnabled(true);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (getActivity() != null)
				{
					if (statusCode >= 400 && statusCode < 500)
						Toast.makeText(getContext(), "Error from dev from Profile, soz !\n" + errorResponse.toString(), Toast.LENGTH_LONG).show();
					else if (statusCode >= 500)
						Toast.makeText(getContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
					loadingPanel.setVisibility(View.GONE);
					bSearch.setEnabled(true);
				}
			}

		});
	}
}
