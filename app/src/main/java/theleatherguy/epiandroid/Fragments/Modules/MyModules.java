package theleatherguy.epiandroid.Fragments.Modules;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.AdapterModuleExpendableListView;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

/**
 * Created by olivier.medec on 27/01/2016.
 */
public class MyModules extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String[]>> listDataChild;
    private String _token;
    View inflatedView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_module, container, false);
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null)
            _token = intent.getStringExtra("token");

        expListView = (ExpandableListView)inflatedView.findViewById(R.id.expandableModuleListView);
        getModule();
        return inflatedView;
    }

    private void getModule() {
        RequestParams params = new RequestParams();
        params.put("token", _token);

        EpitechRest.get("modules", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String[]>>();
                if (getActivity() != null) {
                    theleatherguy.epiandroid.Beans.Modules modules = new Gson().fromJson(response.toString(),
                            theleatherguy.epiandroid.Beans.Modules.class);
                    List<theleatherguy.epiandroid.Beans.Modules.Module> moduleList = modules.modules;

                    List<String[]> ToAdd = new ArrayList<String[]>();
                    String SemesterSave = "-";
                    for (theleatherguy.epiandroid.Beans.Modules.Module mod : moduleList) {
                        if (SemesterSave != mod.semester) {
                            addList(SemesterSave, ToAdd);
                            SemesterSave = mod.semester;
                            if (mod.semester != "1")
                                ToAdd = new ArrayList<String[]>();
                        }
                        ToAdd.add(new String[]{mod.title, mod.grade});
                    }
                    addList(SemesterSave, ToAdd);
                    listDataHeader.remove(0);
                    listAdapter = new AdapterModuleExpendableListView(getActivity(), listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);

                    inflatedView.findViewById(R.id.expandableModuleListView).setVisibility(View.VISIBLE);
                    inflatedView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                    inflatedView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            }

            private void addList(String header, List<String[]> ToAdd) {
                listDataChild.put("Semester " + header, ToAdd);
                listDataHeader.add("Semester " + header);
            }
        });
    }
}
