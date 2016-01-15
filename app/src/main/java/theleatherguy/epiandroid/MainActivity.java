package theleatherguy.epiandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListDeliveriesAdapter;
import theleatherguy.epiandroid.Adapters.ListEventsHomeAdapter;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private View        headerNav;
	private ListView    listToday;
	private ListView    listTomorrow;
	private ListView    listDeliveries;
	private ListView    listMarks;
	private TextView 	headerLogin;
	private ImageView   headerPicture;
	private TextView    headerGpa;
	private TextView    headerCity;
	private TextView    headerCredit;
	private TextView    headerPromo;

	private String                      _token;
	private String                      _login;
	private List<Infos.Board.Event>     _today;
	private List<Infos.Board.Event>     _tomorrow;
	private List<Infos.Board.Project>   _deliveries;
	//private List<>   _marks;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Intent intent = getIntent();
		if (intent != null)
		{
			_token = intent.getStringExtra("token");
			_login = intent.getStringExtra("login");
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
		nav.getMenu().getItem(0).setChecked(true);
		nav.setNavigationItemSelectedListener(this);

		headerNav = nav.getHeaderView(0);
		headerPicture = (ImageView) headerNav.findViewById(R.id.intraPic);
		headerCredit = (TextView) headerNav.findViewById(R.id.txtCredit);
		headerPromo = (TextView) headerNav.findViewById(R.id.txtPromo);
		headerLogin = (TextView) headerNav.findViewById(R.id.txtLogin);
		headerGpa = (TextView) headerNav.findViewById(R.id.txtGpa);
		headerCity = (TextView) headerNav.findViewById(R.id.txtCity);

		listToday = (ListView) findViewById(R.id.listToday);
		listTomorrow = (ListView) findViewById(R.id.listTomorrow);
		listDeliveries = (ListView) findViewById(R.id.listDeliveries);
		listMarks = (ListView) findViewById(R.id.listMarks);

		listToday.setEmptyView(findViewById(R.id.emptyToday));
		listTomorrow.setEmptyView(findViewById(R.id.emptyTomorrow));
		listDeliveries.setEmptyView(findViewById(R.id.emptyDeliveries));
		listMarks.setEmptyView(findViewById(R.id.emptyMarks));

		getActivities();
		getWeekDelivery();
		getUserInfo();

		Toast.makeText(getApplicationContext(), _token, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		} else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_home)
		{

		} else if (id == R.id.nav_alerts)
		{

		} else if (id == R.id.nav_trombi)
		{

		} else if (id == R.id.nav_modules)
		{

		} else if (id == R.id.nav_projects)
		{

		} else if (id == R.id.nav_marks)
		{

		} else if (id == R.id.nav_planning)
		{

		} else if (id == R.id.nav_events)
		{

		} else if (id == R.id.nav_login)
		{

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	private void getActivities()
	{
		RequestParams params = new RequestParams();
		params.put("token", _token);

		EpitechRest.get("infos", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
				Calendar t = Calendar.getInstance();
				_today = new ArrayList<>();
				_tomorrow = new ArrayList<>();
				Infos infos = new Gson().fromJson(response.toString(), Infos.class);
				for (Infos.Board.Event event:infos.board.activites)
				{
					if (event.date_inscription.equals("false"))
					{
						try
						{
							Calendar c = Calendar.getInstance();
							c.setTime(format.parse(event.timeline_start));
							if (c.get(Calendar.DAY_OF_MONTH) == t.get(Calendar.DAY_OF_MONTH))
								_today.add(event);
							else if (c.get(Calendar.DAY_OF_MONTH) == t.get(Calendar.DAY_OF_MONTH) + 1)
								_tomorrow.add(event);
						} catch (ParseException e)
						{
							Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				}
				listToday.setAdapter(new ListEventsHomeAdapter(MainActivity.this, _today));
				listTomorrow.setAdapter(new ListEventsHomeAdapter(MainActivity.this, _tomorrow));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (statusCode >= 400 && statusCode < 500)
					Toast.makeText(getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
				else if (statusCode >= 500)
					Toast.makeText(getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
			}

		});
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
				List<Infos.Board.Project> projs = new Gson().fromJson(response.toString(), new TypeToken<List<Infos.Board.Project>>(){}.getType());
				_deliveries = new ArrayList<>();
				for (Infos.Board.Project proj:projs)
				{
					if (proj.registered == 1
							&& (proj.type_acti.equals("Mini-Projets") || proj.type_acti.equals("Projet")))
						_deliveries.add(proj);
				}
				listDeliveries.setAdapter(new ListDeliveriesAdapter(MainActivity.this, _deliveries));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (statusCode >= 400 && statusCode < 500)
					Toast.makeText(getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
				else if (statusCode >= 500)
					Toast.makeText(getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
			}

		});
	}

	private void getUserInfo()
	{
		RequestParams params = new RequestParams();
		params.put("token", _token);
		params.put("user", _login);

		EpitechRest.get("user", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				Infos.Info info = new Gson().fromJson(response.toString(), Infos.Info.class);
				Picasso.with(getApplicationContext()).load(info.picture).into(headerPicture);
				headerCredit.setText(Integer.toString(info.credits));
				headerPromo.setText(info.promo);
				headerLogin.setText(info.login);
				headerGpa.setText(info.gpa.get(0).gpa);
				headerCity.setText(info.location);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{

			}
		});
	}
}