package com.example.formdemo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner column;
	private EditText edit;
	String[] dataTypes;
	int blocksize;
	int numberofrows;
	double pct_free;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*Spinner spinner = (Spinner) findViewById(R.id.blockEdit);
		blocksize = Integer.valueOf(String.valueOf(spinner.getSelectedItem()).split("K")[0]);
		
		spinner = (Spinner) findViewById(R.id.pct_spinner);
		pct_free = Integer.valueOf(String.valueOf(spinner.getSelectedItem()).split("\\(")[0]);
		
		edit = (EditText) findViewById(R.id.rowsEdit);
		numberofrows = Integer.valueOf(edit.getText().toString());*/
		
		column = (Spinner) findViewById(R.id.cols_spinner);
		Resources res = getResources();
		dataTypes = res.getStringArray(R.array.datatypes);
	}
	

	 
	public void createTable(View v) {
		//Toast.makeText(this, String.valueOf(column.getSelectedItem()), Toast.LENGTH_LONG).show();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dataTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		int column_count = Integer.valueOf(String.valueOf(column.getSelectedItem()));
		
		//LinearLayout testLayout = (LinearLayout)findViewById(R.id.linear1);
		TableLayout testLayout = (TableLayout)findViewById(R.id.table1);
		testLayout.removeAllViews();
		
		TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
		layout.setMargins(5, 5, 5, 5);
		
		
		
		TableRow tableRow;
		
		
		for(int i=1;i<=column_count;i++) {
			
			final int counter = i;
			//create table row with child view
			tableRow = new TableRow(this);
			tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
			
			//TextView
			  TextView tvLeft = new TextView(this);
			  //tvLeft.setBackgroundColor(Color.WHITE);
			  tvLeft.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1));
			  tvLeft.setText("Column "+i);
			  tvLeft.setId(i*23);
			  tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PT,8);
			  
			  
			  
			//DropDown of DataTypes  
			  final Spinner datatypeDropdown = new Spinner(this);
			  datatypeDropdown.setId(i*13);
			 // datatypeDropdown.setBackgroundColor(Color.YELLOW);
			  datatypeDropdown.setLayoutParams(new TableRow.LayoutParams(260,TableRow.LayoutParams.WRAP_CONTENT,1));
			  datatypeDropdown.setAdapter(adapter);
			  
			  datatypeDropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
				  
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
					String spinner_value = (String) parent.getItemAtPosition(position);
					
					EditText showSize = (EditText) findViewById(counter);
					
					//Show edit view only for character data types.
					for(TypeEnum e:TypeEnum.values()){
						if(e.isTypeof().equals("character") && e.name().equals(spinner_value)) {
							showSize.setVisibility(View.VISIBLE);
						} 
						if(e.isTypeof().equals("noncharacter") && e.name().equals(spinner_value) ) {
							showSize.setVisibility(View.INVISIBLE);
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			  });
			  
			//EditText for size of dataType  
			  int maxLength = 4;
			  EditText size = new EditText(this);
			  size.setId(i);
			  //size.setBackgroundColor(Color.GREEN);
			  size.setBackgroundResource(android.R.drawable.editbox_background);
			  size.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			  size.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
			  size.setHint("Size");
			  size.setLayoutParams(new TableRow.LayoutParams(260,TableRow.LayoutParams.WRAP_CONTENT,1));
			  size.setVisibility(View.INVISIBLE);
			  
			  //add components to table row
			  tableRow.addView(tvLeft,layout);
			  tableRow.addView(datatypeDropdown);
			  tableRow.addView(size);
			  
			  //add row to table
			  testLayout.addView(tableRow);
		}
		
		
	}
	
	public void collectViews(View v) {
		TableLayout layout_tbl = (TableLayout) findViewById(R.id.table1);
		List<Columns> allcolumns = new ArrayList<Columns>();
		
		for (int i = 0; i < layout_tbl.getChildCount(); i++) {
		     TableRow parentRow = (TableRow) layout_tbl.getChildAt(i);
		     
		     if(parentRow instanceof TableRow){
		                
			                   View view;
			                   String type = null;
			                   int size =0;
			                   			                   
			                   view = parentRow.getChildAt(1);
			                   type=String.valueOf(((Spinner) view).getSelectedItem());
			                   
			                   view = parentRow.getChildAt(2);
			                   if(((EditText) view).getText().toString().length() != 0) {
			                	   size = Integer.valueOf(((EditText) view).getText().toString());
			                   }
			                   
			                   allcolumns.add(new Columns(type,size));
			               
			                Log.d("View ","type="+type+" size="+size);
			            
		       }		
		}
		
		calculateDBSize(allcolumns);
	}



	private void calculateDBSize(List<Columns> allcolumns) {
		Spinner spinner = (Spinner) findViewById(R.id.blockEdit);
		blocksize = Integer.valueOf(String.valueOf(spinner.getSelectedItem()).split("K")[0]);
		
		spinner = (Spinner) findViewById(R.id.pct_spinner);
		pct_free = Integer.valueOf(String.valueOf(spinner.getSelectedItem()).split("\\(")[0]);
		
		edit = (EditText) findViewById(R.id.rowsEdit);
		numberofrows = Integer.valueOf(edit.getText().toString());
		
		Log.d("est pct",""+pct_free);		
		
		double block_size = blocksize*1024;
		
		// TotalBlockHeaderSize = DB_BLOCK_SIZE - KCBH - UB4 - KTBBH - ((INITRANS - 1) * KTBIT) - KDBH
		double total_block_header = (block_size) - 86;//20 - 4 - ((5-1)*24)-14;
		
		double pct_free_calc = (1 - pct_free/100);
		
		double average_space_per_block = (total_block_header * (1 - pct_free/100)) - 4;
		
		double total_row_size = 3 + getSumOfColumnSize(allcolumns);
		
		double no_rows_per_block = Math.ceil(average_space_per_block/total_row_size);
		
		double totalbytes = (numberofrows / no_rows_per_block )*(block_size);
		
		double totalsize_MB = totalbytes;
		
		//reduce 30% to have around exact size
		double adjusted_size = (double) (totalsize_MB - (totalsize_MB*0.3));
		
		Log.d("est blockheader",""+total_block_header);
		Log.d("est pctfreecalc",""+pct_free_calc);
		Log.d("est aver",""+average_space_per_block);
		Log.d("est no_rows",""+no_rows_per_block);
		Log.d("est blk",""+block_size);
		Log.d("est ts",""+totalsize_MB);
		Log.d("est as",""+adjusted_size);
		Toast.makeText(getApplicationContext(), "Total Size "+readableFileSize((long) adjusted_size), Toast.LENGTH_LONG).show();
	}



	private int getSumOfColumnSize(List<Columns> allcolumns) {
		
		int total_column_size=0;
		
		for(Columns c:allcolumns) {
			
			for(TypeEnum t : TypeEnum.values()){
				
				if(c.getType().equals(t.name()) && t.isTypeof().equals("noncharacter")){
					total_column_size  = total_column_size + t.getMaxsizeinBytes();
					Log.d("est Get",c.getType()+" "+t.getMaxsizeinBytes());
				}
				
				if(c.getType().equals(t.name()) && t.isTypeof().equals("character")){
					total_column_size = total_column_size + Math.min(c.getSize(),t.getMaxsizeinBytes())+1;
					Log.d("est Get",c.getType()+" "+Math.min(c.getSize(),t.getMaxsizeinBytes()));
				}
			}
			
			
		}
		Log.d("est totalC",""+total_column_size);
		return total_column_size;
	}
	
	public String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
