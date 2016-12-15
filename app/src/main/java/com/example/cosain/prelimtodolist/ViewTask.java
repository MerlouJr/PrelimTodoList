package com.example.cosain.prelimtodolist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewTask extends Activity {
	protected TaskerDbHelper db;
	List<Task> list;
	MyAdapter adapt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);
		db = new TaskerDbHelper(this);
		list = db.getAllTasks();
		adapt = new MyAdapter(this, R.layout.list_inner_view, list);
		ListView listTask = (ListView) findViewById(R.id.listView1);
		listTask.setAdapter(adapt);

//		listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//				db.clear();
//			}
//		});


	}


	public void addTaskNow(View v) {
		EditText t = (EditText) findViewById(R.id.editText1);
		String s = t.getText().toString();
		if (s.equalsIgnoreCase("")) {
			Toast.makeText(this, "enter the task description first!!",
					Toast.LENGTH_LONG);
		} else {
			Task task = new Task(s, 0);
			db.addTask(task);
			Log.d("tasker", "data added");
			t.setText("");
			adapt.add(task);
			adapt.notifyDataSetChanged();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_task, menu);
		return true;
	}

	private class MyAdapter extends ArrayAdapter<Task> {

		Context context;
		List<Task> taskList = new ArrayList<Task>();
		int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId,
				List<Task> objects) {
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.taskList = objects;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckBox chk = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_inner_view,
						parent, false);
				chk = (CheckBox) convertView.findViewById(R.id.chkStatus);
				convertView.setTag(chk);

				chk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Task changeTask = (Task) cb.getTag();
						changeTask.setStatus(cb.isChecked() == true ? 1 : 0);
						db.updateTask(changeTask);
						Toast.makeText(
								getApplicationContext(),
								"Clicked on Checkbox: " + cb.getText() + " is "
										+ cb.isChecked(), Toast.LENGTH_LONG)
								.show();
					}

				});
			} else {
				chk = (CheckBox) convertView.getTag();
			}
			Task current = taskList.get(position);
			chk.setText(current.getTaskName());
			chk.setChecked(current.getStatus() == 1 ? true : false);
			chk.setTag(current);
			Log.d("listener", String.valueOf(current.getId()));
			return convertView;
		}

	}

}
