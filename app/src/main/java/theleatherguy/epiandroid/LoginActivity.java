package theleatherguy.epiandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.Login;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;

public class LoginActivity extends AppCompatActivity
{
	private View        _focusView;
	private EditText    _editLogin;
	private EditText    _editPassword;
	private Button      _bConnect;

	private Login       _login;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		setTitle(R.string.title_activity_login);

		findViewById(R.id.loadingPanel).setVisibility(View.GONE);

		TextView logo = (TextView) findViewById(R.id.textLogo);
		logo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway.ttf"));
		_bConnect = (Button) findViewById(R.id.bConnect);
		_editLogin = (EditText) findViewById(R.id.editLogin);
		_editPassword = (EditText) findViewById(R.id.editPassword);

		_bConnect.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				View focus = getCurrentFocus();
				if (focus != null)
					inputManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				attemptLogin();
			}
		});

		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		_editLogin.setText(pref.getString("login", null));
		_editPassword.setText(pref.getString("pass", null));

		if (!_editLogin.getText().toString().isEmpty()
				&& !_editPassword.getText().toString().isEmpty())
			attemptLogin();
	}

	private void attemptLogin()
	{
		findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

		_editLogin.setError(null);
		_editPassword.setError(null);

		String login = _editLogin.getText().toString().trim();
		String password = _editPassword.getText().toString();
		boolean cancel = false;
		_focusView = _editLogin;

		if (TextUtils.isEmpty(login))
		{
			_editLogin.setError(getString(R.string.error_field_required));
			cancel = true;
		}
		else if (!validateLogin(login))
		{
			_editLogin.setError(getString(R.string.error_invalid_login));
			cancel = true;
		}

		if (TextUtils.isEmpty(password))
		{
			_editPassword.setError(getString(R.string.error_field_required));
			if (!cancel)
				_focusView = _editPassword;
			cancel = true;
		}

		if (cancel)
		{
			findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			_focusView.requestFocus();
		}
		else
			getToken(login, password);
	}

	private boolean validateLogin(String login)
	{
		return login.matches("([a-z]{2,6})_([a-z1-9])");
	}

	public void getToken(final String login, final String password)
	{
		_bConnect.setEnabled(false);
		RequestParams params = new RequestParams();
		params.put("login", login);
		params.put("password", password);

		EpitechRest.post("login", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				_login = new Gson().fromJson(response.toString(), Login.class);
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra("token", _login.token);
				intent.putExtra("login", login);
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
				Editor edit = pref.edit();
				edit.putString("login", login);
				edit.putString("pass", password);
				edit.apply();
				startActivity(intent);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (statusCode >= 400 && statusCode < 500)
				{
					_editPassword.setError(getString(R.string.error_incorrect_cred));
					_focusView = _editPassword;
					findViewById(R.id.loadingPanel).setVisibility(View.GONE);
					_focusView.requestFocus();
				}
				else if (statusCode >= 500)
					Toast.makeText(getApplicationContext(), "Server down, try again later", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), "Check your connection", Toast.LENGTH_LONG).show();
				_bConnect.setEnabled(true);
			}
		});
	}
}

