package com.example.todo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ViewTask extends Activity {
	
	protected TaskDBHelper db;
	List<Task> list;
	MyAdapter adapt;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);
		
		db = new TaskDBHelper(this);
		list = db.getAllTasks();
		adapt = new MyAdapter(this,R.layout.list_inner_view,list);
		ListView listTask = (ListView)findViewById(R.id.listView1);
		listTask.setAdapter(adapt);
	}

	public void addTaskNow(View v){
		EditText t=(EditText)findViewById(R.id.editText1);
		String s = t.getText().toString();
		if(s.equalsIgnoreCase("")){
			Toast.makeText(this, "Enter the task ", Toast.LENGTH_LONG).show();
		}else{
			Task task = new Task(s, 0);
			db.addTask(task);
			Log.d("tasker", "data added");	
			t.setText("");
			adapt.add(task);
			adapt.notifyDataSetChanged();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.view_task, menu);
		return true;
	}
	
	private class MyAdapter extends ArrayAdapter<Task>{
		Context context;
		List<Task> taskList = new ArrayList<Task>();
		int layoutResourceId;
		
		public MyAdapter(Context context,int layoutResourcId,List<Task>objects){
			
			super(context,layoutResourcId,objects);
			this.layoutResourceId = layoutResourcId;
			this.taskList = objects;
			this.context = context;
		}
		
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		CheckBox check = null;
		if(convertView ==  null){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.list_inner_view,parent, false);
        check = (CheckBox) convertView.findViewById(R.id.chkStatus);

		convertView.setTag(check);
		check.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				Task changeTask = (Task) cb.getTag();
				changeTask.setStatus(cb.isChecked()==true ? 1 : 0);
				db.updateTask(changeTask);
				Toast.makeText(getApplicationContext(),"Clicked on checkbox:" +cb.getText() + "is" +cb.isChecked() , Toast.LENGTH_LONG).show();
				
			}
		});
	}else{
	check = (CheckBox)convertView.getTag();	
	}
		Task current = taskList.get(position);
		check.setText(current.getTaskName());
		check.setChecked(current.getStatus() == 1 ? true : false);
		check.setTag(current);
		Log.d("listener", String.valueOf(current.getId()));
		return convertView;
	}
	}
}
