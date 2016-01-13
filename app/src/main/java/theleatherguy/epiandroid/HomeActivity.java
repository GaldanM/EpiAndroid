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
import android.widget.ListView;
import android.widget.TextView;
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
import theleatherguy.epiandroid.Adapters.ListDeliveriesAdapter;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.Beans.Login;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private ListView        listToday;
	private ListView        listTomorrow;
	private ListView        listDeliveries;
	private ListView        listMarks;

	private String              _token;
	private List<Infos.Board.Event>   _today;
	private List<Infos.Board.Event>   _tomorrow;
	private List<Infos.Board.Project> _deliveries;
	//private List<>   _marks;

	private Infos 		_infos;
	private TextView 	_headerLogin;
	private TextView 	_headerGpa;
	private TextView 	_headerCity;
	private TextView 	_headerCredit;
	private TextView 	_headerPromo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Intent intent = getIntent();
		if (intent != null)
			_token = intent.getStringExtra("token");

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.getMenu().getItem(0).setChecked(true);
		navigationView.setNavigationItemSelectedListener(this);

		listToday = (ListView) findViewById(R.id.listToday);
		listToday.setEmptyView(findViewById(R.id.emptyToday));
		listTomorrow = (ListView) findViewById(R.id.listTomorrow);
		listTomorrow.setEmptyView(findViewById(R.id.emptyTomorrow));
		listDeliveries = (ListView) findViewById(R.id.listDeliveries);
		listDeliveries.setEmptyView(findViewById(R.id.emptyDeliveries));
		listMarks = (ListView) findViewById(R.id.listMarks);
		listMarks.setEmptyView(findViewById(R.id.emptyMarks));

		_headerCredit = (TextView)findViewById(R.id.txtCredit);
		_headerPromo = (TextView)findViewById(R.id.txtPromo);
		_headerLogin = (TextView)findViewById(R.id.txtLogin);
		_headerGpa = (TextView)findViewById(R.id.txtGpa);
		_headerCity = (TextView)findViewById(R.id.txtCity);

		getWeekDelivery();

		getUserInfo();

		Toast.makeText(getApplicationContext(), _token, Toast.LENGTH_LONG).show();
	}

	private void getUserInfo()
	{
		RequestParams params = new RequestParams();
		params.put("user", "moulin_c");
		params.put("token", _token);

		EpitechRest.get("user", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				_infos = new Gson().fromJson(response.toString(), Infos.class);
				_headerCredit.setText(_infos.infos.credits);
				_headerPromo.setText(_infos.infos.promo);
				_headerLogin.setText(_infos.infos.login);
				_headerGpa.setText(_infos.infos.gpa.gpa);
				//_headerCity.setText(_infos.infos.);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{

			}
		});
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
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
				listDeliveries.setAdapter(new ListDeliveriesAdapter(HomeActivity.this, _deliveries));
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
}