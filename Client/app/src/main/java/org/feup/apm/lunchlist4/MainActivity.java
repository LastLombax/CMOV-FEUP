package org.feup.apm.lunchlist4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.apm.lunchlist4.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
  public final static String ID_EXTRA="org.feup.apm.lunchlist.POS";
  TransactionsHelper helper;
  static long currentId = -1;
  Cursor model;
  TransactionAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startActivityForResult(new Intent(this, LoginActivity.class),123);
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    ActionBar bar = getSupportActionBar();
    if (bar != null) {
      bar.setIcon(R.drawable.medium_logo2);
      bar.setDisplayShowHomeEnabled(true);
    }
    setContentView(R.layout.activity_main);
    helper = new TransactionsHelper(this);

    model = helper.getAll();
    startManagingCursor(model);
    adapter=new TransactionAdapter(model);

    ListView list = findViewById(R.id.listview);
    list.setAdapter(adapter);
    list.setEmptyView(findViewById(R.id.empty_list));
    list.setOnItemClickListener(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    helper.close();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    new MenuInflater(this).inflate(R.menu.main, menu);
    return (super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == R.id.add) {
      startActivity(new Intent(this, NewTransaction.class));
      overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
      return(true);
    }
    return(super.onOptionsItemSelected(item));
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
    Intent i=new Intent(this, DetailsTransaction.class);
    currentId = id;
    i.putExtra(ID_EXTRA, String.valueOf(id));
    startActivity(i);
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }


  /*                                              *
   * Sub class with the custom ListView adapter   *
   *                                              */

  class RestaurantAdapter extends CursorAdapter {
    RestaurantAdapter(Cursor c) {
      super(MainActivity.this, c);
    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
      View row=getLayoutInflater().inflate(R.layout.row, parent, false);
      ((TextView)row.findViewById(R.id.title)).setText(helper.getName(c));
     // ((TextView)row.findViewById(R.id.address)).setText(helper.getAddress(c));
      ImageView symbol = row.findViewById(R.id.symbol);
      if (helper.getType(c).equals("sit"))
        symbol.setImageResource(R.drawable.ball_red);
      else if (helper.getType(c).equals("take"))
        symbol.setImageResource(R.drawable.ball_yellow);
      else
        symbol.setImageResource(R.drawable.ball_green);
      return(row);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }
  }

  class TransactionAdapter extends CursorAdapter {
    TransactionAdapter(Cursor c) {
      super(MainActivity.this, c);
    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
      View row=getLayoutInflater().inflate(R.layout.row, parent, false);
      ((TextView)row.findViewById(R.id.title)).setText("insert title here");
      ((TextView)row.findViewById(R.id.description)).setText("X" +" items");
      ((TextView)row.findViewById(R.id.price)).setText("5"+"€");

      ImageView symbol = row.findViewById(R.id.symbol);
      symbol.setImageResource(R.drawable.batmanoutfit);

      return(row);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }


  }


}
