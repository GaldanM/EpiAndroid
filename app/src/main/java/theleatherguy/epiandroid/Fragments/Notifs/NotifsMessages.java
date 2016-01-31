package theleatherguy.epiandroid.Fragments.Notifs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListMessagesNotifsAdapter;
import theleatherguy.epiandroid.Beans.Message;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

public class NotifsMessages extends Fragment
{
	private String          token;
	private ListView        listMessages;
	private List<Message>   messages;
	private View            rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_messages_notifs, container, false);

		Intent intent = getActivity().getIntent();
		if (intent != null)
			this.token = intent.getStringExtra("token");

		listMessages = (ListView) rootView.findViewById(R.id.listMessages);
		listMessages.setEmptyView(rootView.findViewById(R.id.emptyMessages));

		getMessages();

		return (rootView);
	}

	private void getMessages()
	{
		RequestParams params = new RequestParams();
		params.put("token", this.token);

		EpitechRest.get("messages", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response)
			{
				if (getActivity() != null)
				{
					List<Message> msgs = new Gson().fromJson(response.toString(), new TypeToken<List<Message>>(){}.getType());
					messages = new ArrayList<>();
					for (Message msg : msgs)
						messages.add(msg);
					listMessages.setAdapter(new ListMessagesNotifsAdapter(getActivity(), messages));
					rootView.findViewById(R.id.card_messages).setVisibility(View.VISIBLE);
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
				}
			}

		});
	}
}
