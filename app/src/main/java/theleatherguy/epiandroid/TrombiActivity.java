package theleatherguy.epiandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import theleatherguy.epiandroid.Fragments.Profile;

public class TrombiActivity extends AppCompatActivity
{
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trombi);

		String name = "User";

		Intent intent = getIntent();
		if (intent != null)
			name = intent.getStringExtra("user");


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
		toolbar.setTitle(name);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Profile()).commit();
	}
}
