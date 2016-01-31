package theleatherguy.epiandroid.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class TokenDialog extends AlertDialog.Builder
{
	private EditText editText;
	private Event event;

	private String token;
	private String code;

	public TokenDialog(Context activity, String token, Event e)
	{
		super(activity);
		event = e;
		this.token = token;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.enter_token, null);
		editText = (EditText) view.findViewById(R.id.editToken);
		setView(view);
		this.setPositiveButton("Ok", this.positiveButton());
		this.setNegativeButton("Cancel", this.cancelButton());
	}

	private DialogInterface.OnClickListener positiveButton()
	{
		return new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				code = editText.getText().toString();
				if (code.length() == 8)
					sendToken(token, event.scolaryear, event.codemodule, event.codeinstance, event.codeacti, event.codeevent, code);
				else
					Toast.makeText(getContext(), "Incorrect token", Toast.LENGTH_LONG).show();

			}
		};
	}

	private DialogInterface.OnClickListener cancelButton()
	{
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		};
	}

	public String getCode()
	{
		return (code);
	}

	private void sendToken(String token, String scolar, String module, String instance, String acti, String event, String code)
	{
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("scolaryear", scolar);
		params.put("codemodule", module);
		params.put("codeinstance", instance);
		params.put("codeacti", acti);
		params.put("codeevent", event);
		params.put("tokenvalidationcode", code);

		EpitechRest.post("token", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				Toast.makeText(getContext(), "Success with status " + Integer.toString(statusCode), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (statusCode >= 400 && statusCode < 500)
					Toast.makeText(getContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
				else if (statusCode >= 500)
					Toast.makeText(getContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
			}

		});
	}
}