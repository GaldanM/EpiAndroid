package theleatherguy.epiandroid.Fragments.Planning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListPlanning;
import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

/**
 * Created by olivier.medec on 30/01/2016.
 */
public class Planning extends Fragment {
    private String _token = null;
    private ArrayAdapter<CharSequence> adapter;
    private Button btnDatePicker = null;
    private View _inflatedView = null;
    private String _date = getCurrentDate();
    private int sort = 0;
    private ListView _listView = null;
    private Bundle args = null;
    private Infos _infos = null;
    private int _year;
    private int _month;
    private int _day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _inflatedView = inflater.inflate(R.layout.fragment_planning, container, false);
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null)
            _token = intent.getStringExtra("token");

        Spinner spinner = (Spinner)_inflatedView.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.country_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = position;
                _listView.invalidateViews();
                getInfos();
                Toast.makeText(getActivity(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDatePicker = (Button)_inflatedView.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.setArguments(args);
                picker.show(getFragmentManager(), "datePicker");
                picker.setTargetFragment(Planning.this, 1);
            }
        });


        _listView = (ListView)_inflatedView.findViewById(R.id.listPlanning);
        getInfos();
        return _inflatedView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            _date = intent.getStringExtra("DATE");
            _year = intent.getIntExtra("year", 2015);
            _month = intent.getIntExtra("month", 1);
            _day = intent.getIntExtra("day", 1);
            args = new Bundle();
            args.putInt("year", _year);
            args.putInt("month", _month);
            args.putInt("day", _day);
        }
        _listView.invalidateViews();
        getInfos();
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        _year = c.get(Calendar.YEAR);
        _month = c.get(Calendar.MONTH);
        _day = c.get(Calendar.DAY_OF_MONTH);

        args = new Bundle();
        args.putInt("year", _year);
        args.putInt("month", _month);
        args.putInt("day", _day);
        return String.valueOf(_year) + '-' + String.valueOf(_month + 1) + "-" + String.valueOf(_day);
    }

    public void getPlanning() {
        RequestParams params = new RequestParams();
        params.put("token", _token);
        params.put("start", _date);
        params.put("end", _date);

        EpitechRest.get("planning", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (getActivity() != null) {
                    List<Event> event = new Gson().fromJson(response.toString(),
                            new TypeToken<List<Event>>() {
                            }.getType());

                    List<Event> eventsList = new ArrayList<>();
                    for (Event i : event) {
                        if (sort == 0) {
                            if (i.semester == _infos.infos.semester
                                    || i.semester == _infos.infos.semester + 1
                                    || i.semester == 0)
                                eventsList.add(i);
                        } else if (sort == 1) {
                            if (i.module_registered) {
                                eventsList.add(i);
                            }
                        } else if (sort == 2) {
                            if (!i.event_registered.equals("false")) {
                                eventsList.add(i);
                            }
                        }
                    }
                    ListPlanning planningAdapter = new ListPlanning(eventsList, getActivity());
                    _listView.setAdapter(planningAdapter);
                    planningAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                if (getActivity() != null) {
                    if (statusCode >= 400 && statusCode < 500)
                        Toast.makeText(getActivity().getApplicationContext(), "Error from dev, soz !1", Toast.LENGTH_LONG).show();
                    else if (statusCode >= 500)
                        Toast.makeText(getActivity().getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity().getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getInfos() {
        RequestParams params = new RequestParams();
        params.put("token", _token);

        EpitechRest.get("infos", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (getActivity() != null) {
                    _infos = new Gson().fromJson(response.toString(), Infos.class);
                    Log.w("Infos", _infos.infos.location);
                    getPlanning();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (getActivity() != null) {
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
