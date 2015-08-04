package com.yun.tabswitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TabActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] items = new String[]{
        "RadioButtonFm","RadioButtonFm2","TabHostFM"};

    Class[] clazz = new Class[]{RadioButtonFm.class,RadioButtonFM2.class,TabHostFM.class};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tab);
        ListView lv = new ListView(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
        lv.setAdapter(adapter);
        setContentView(lv);
        lv.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TabActivity.this, clazz[position]);
        startActivity(intent);
    }
}
