package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.R;

public class ListDeliveriesHomeAdapter extends ArrayAdapter<Infos.Board.Project>
{
	public ListDeliveriesHomeAdapter(Context context, List<Infos.Board.Project> listProjects)
	{
		super(context, 0, listProjects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_project_home, parent, false);

		ProjectViewHolder viewHolder = (ProjectViewHolder) convertView.getTag();
		if (viewHolder == null)
		{
			viewHolder = new ProjectViewHolder();
			viewHolder.project = (TextView) convertView.findViewById(R.id.textProject);
			viewHolder.module = (TextView) convertView.findViewById(R.id.textModule);
			convertView.setTag(viewHolder);
		}

		Infos.Board.Project proj = getItem(position);

		viewHolder.project.setText(proj.acti_title);
		viewHolder.module.setText(proj.title_module);

		return convertView;
	}

	private class ProjectViewHolder
	{
		public TextView project;
		public TextView module;
	}
}
