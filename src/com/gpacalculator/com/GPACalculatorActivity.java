package com.gpacalculator.com;

import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class GPACalculatorActivity extends Activity  {
    /** Called when the activity is first created. */
	
	private EditText credit1,credit2,credit3,credit4,credit5,credit6;
	private TextView gpa;
	private Button calculateGpa,saveGpa,retrieveGpa;
	private String logText = "GPA";
	private Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,termSpinner,yearSpinner,tab2TermSpinner,tab2YearSpinner;
	private String grade1 = null;
	private String grade2 = null;
	private String grade3 = null;
	private String grade4 = null;
	private String grade5 = null;
	private String grade6 = null;
	private String term;
	private String termYear;
	private String tab2Term;
	private String tab2Year;
	private int count = 0;
	private EditText coursetitle1,coursetitle2,coursetitle3,coursetitle4,coursetitle5,coursetitle6;
	double result = 0.0;
	double totalCredits = 0.0;
	double finalResult = 0.0;
	private TextView tab2course1,tab2course2,tab2course3,tab2course4,tab2course5,tab2course6;
	private TextView tab2credithrs1,tab2credithrs2,tab2credithrs3,tab2credithrs4,tab2credithrs5,tab2credithrs6;
	private TextView tab2grades1,tab2grades2,tab2grades3,tab2grades4,tab2grades5,tab2grades6;
	private TextView tab2gpa,tab2cgpa,tab2nogpa;
	
	//GPACalculatorDB db;
	SQLiteDatabase mysqlitedb;
	GPACalculatorContentProvider contentProvider;
	
	//private String course1,course2,course3,course4,course5,course6;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        credit1 = (EditText) findViewById(R.id.creditHrs1);
        credit2 = (EditText) findViewById(R.id.creditHrs2);
        credit3 = (EditText) findViewById(R.id.creditHrs3);
        credit4 = (EditText) findViewById(R.id.creditHrs4);
        credit5 = (EditText) findViewById(R.id.creditHrs5);
        credit6 = (EditText) findViewById(R.id.creditHrs6);
        
        coursetitle1 = (EditText) findViewById(R.id.coursetitle1);
        coursetitle2 = (EditText) findViewById(R.id.coursetitle2);
        coursetitle3 = (EditText) findViewById(R.id.coursetitle3);
        coursetitle4 = (EditText) findViewById(R.id.coursetitle4);
        coursetitle5 = (EditText) findViewById(R.id.coursetitle5);
        coursetitle6 = (EditText) findViewById(R.id.coursetitle6);
        
        //TAB 2 ENTRIES
        
        tab2course1 = (TextView) findViewById(R.id.tab2coursetitle1);
        tab2course2 = (TextView) findViewById(R.id.tab2coursetitle2);
        tab2course3 = (TextView) findViewById(R.id.tab2coursetitle3);
        tab2course4 = (TextView) findViewById(R.id.tab2coursetitle4);
        tab2course5 = (TextView) findViewById(R.id.tab2coursetitle5);
        tab2course6 = (TextView) findViewById(R.id.tab2coursetitle6);
        
        tab2credithrs1 = (TextView) findViewById(R.id.tab2credithrs1);
        tab2credithrs2 = (TextView) findViewById(R.id.tab2credithrs2);
        tab2credithrs3 = (TextView) findViewById(R.id.tab2credithrs3);
        tab2credithrs4 = (TextView) findViewById(R.id.tab2credithrs4);
        tab2credithrs5 = (TextView) findViewById(R.id.tab2credithrs5);
        tab2credithrs6 = (TextView) findViewById(R.id.tab2credithrs6);
        
        tab2grades1 = (TextView) findViewById(R.id.tab2grade1);
        tab2grades2 = (TextView) findViewById(R.id.tab2grade2);
        tab2grades3 = (TextView) findViewById(R.id.tab2grade3);
        tab2grades4 = (TextView) findViewById(R.id.tab2grade4);
        tab2grades5 = (TextView) findViewById(R.id.tab2grade5);
        tab2grades6 = (TextView) findViewById(R.id.tab2grade6);
        
        tab2gpa = (TextView) findViewById(R.id.tab2gpatext);
        tab2cgpa = (TextView) findViewById(R.id.tab2cgpatext);
        tab2nogpa = (TextView) findViewById(R.id.tab2nogpa);
      
 
        ArrayList<String> year = new ArrayList<String>();
        int startYear = 2010;
                  
        for(int i = 0;i<=40;i++){
        	year.add(Integer.toString(startYear));
        	startYear++;
        }
        
        ArrayList<String> tab2year = new ArrayList<String>();
        int startYearTab2 = 2010;

        for(int i = 0;i<=40;i++){
        	tab2year.add(Integer.toString(startYearTab2));
        	startYearTab2++;
        }
          
        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        
        TabSpec tabOne = tabs.newTabSpec("tab_one_btn_tab");
        tabOne.setContent(R.id.tab1Layout);
        tabOne.setIndicator("Find Your GPA");
        tabs.addTab(tabOne);
        
        TabSpec tabTwo = tabs.newTabSpec("tab_two_btn_tab");
        tabTwo.setIndicator("Previous Term");
        tabTwo.setContent(R.id.tab2Layout);
        tabs.addTab(tabTwo);
        
        tabs.setCurrentTab(0);
        
        //Year Spinner
        yearSpinner = (Spinner) findViewById(R.id.yearspinner);
        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(
                this, R.array.year, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapterYear);
        yearSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
       
        
        //Term Spinner
        termSpinner = (Spinner) findViewById(R.id.termspinner);
        ArrayAdapter<CharSequence> adapterTerm = ArrayAdapter.createFromResource(
                this, R.array.semester, android.R.layout.simple_spinner_item);
        adapterTerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(adapterTerm);
        termSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Tab 2 Term Spinner
       tab2TermSpinner = (Spinner) findViewById(R.id.tab2termspinner);
       ArrayAdapter<CharSequence> adapterTab2Term = ArrayAdapter.createFromResource(
    		   this, R.array.semester, android.R.layout.simple_spinner_dropdown_item);
       adapterTab2Term.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       tab2TermSpinner.setAdapter(adapterTab2Term);
       tab2TermSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
       // Tab 2 Year Spinner
       tab2YearSpinner = (Spinner) findViewById(R.id.tab2yearspinner);
       ArrayAdapter<CharSequence> adapterYearTab2 = ArrayAdapter.createFromResource(
               this, R.array.year, android.R.layout.simple_spinner_item);
       adapterYearTab2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       tab2YearSpinner.setAdapter(adapterYearTab2);
       tab2YearSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Spinner 1
        spinner1 = (Spinner) findViewById(R.id.grade1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.grades, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Spinner 2
        spinner2 = (Spinner) findViewById(R.id.grade2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.grades, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Spinner 3
        spinner3 = (Spinner) findViewById(R.id.grade3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.grades, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Spinner 4
        spinner4 = (Spinner) findViewById(R.id.grade4);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                this, R.array.grades, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new MyOnItemSelectedListener());
       
        //Spinner 5
        spinner5 = (Spinner) findViewById(R.id.grade5);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(
                this, R.array.grades, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Spinner 6
        spinner6 = (Spinner) findViewById(R.id.grade6);
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(
                this, R.array.grades, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter6);
        spinner6.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        calculateGpa = (Button) findViewById(R.id.calculate);
        gpa = (TextView) findViewById(R.id.result);
        
        saveGpa = (Button) findViewById(R.id.save);
        retrieveGpa = (Button) findViewById(R.id.retrieve);
        
        retrieveGpa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//RETRIEVING VALUES FROM DB
				
				Log.i(logText,"Entered retrievegpa button click event ");
				
				String year = tab2Year;
				String term1 = tab2Term;
				double cgpa = 0.0;
				
				Log.i(logText,"Selected year and Term "+ year + " - "+ term1);
				
				Log.i(logText, "Before opening db");
				mysqlitedb = openOrCreateDatabase("gpaCalculator",
						SQLiteDatabase.CREATE_IF_NECESSARY, null);
				Log.i(logText, "After opening db object");
				mysqlitedb.setVersion(1);
				mysqlitedb.setLocale(Locale.getDefault());
				mysqlitedb.setLockingEnabled(true);
				
				Log.i(logText,"After creating cursor");
				
				Cursor cur = mysqlitedb.query("gpa", new String[]{"courseTitle1","courseTitle2","courseTitle3",
						"courseTitle4","courseTitle5","courseTitle6","creditHrs1","creditHrs2","creditHrs3","creditHrs4","creditHrs5",
						"creditHrs6","grade1","grade2","grade3","grade4","grade5","grade6","gpa"},
						GPACalculatorDB.col2 +"= ? and "+GPACalculatorDB.col3+"= ?", new String[]{term1,year}, null,
						null, null);
				
				Cursor curgpa = mysqlitedb.query("gpa", new String[]{"gpa"}, null, null, null, null, null);
				
				 // TO FIND CGPA				
					
					int curgpaRows = curgpa.getCount();
					Log.i(logText,"No of rows with gpa "+curgpaRows);
					
					if(curgpaRows == 0){
						tab2cgpa.setText("-");
					}
					else{
						curgpa.moveToFirst();
						for(int i =0;i<curgpaRows;i++){
							
							cgpa += Double.parseDouble(curgpa.getString(0));
							curgpa.moveToNext();
						}
						curgpa.close();
						
						cgpa = cgpa/curgpaRows;
						cgpa = roundTwoDecimals(cgpa);
						
						tab2cgpa.setText(Double.toString(cgpa));
					}
					
					// TO FIND GPA
					
					int curRows = cur.getCount();
					Log.i(logText,"No of rows "+curRows);
					
				if (curRows == 0) {
					tab2nogpa.setText("Sorry No Results returned");
					tab2cgpa.setText("-");
					tab2gpa.setText("-");
					clearContents();
					
				} else {
					cur.moveToFirst();
					for (int i = 0; i < curRows; i++) {
						
						tab2nogpa.setText("");
						
						tab2course1.setText(cur.getString(0));
						tab2course2.setText(cur.getString(1));
						tab2course3.setText(cur.getString(2));
						tab2course4.setText(cur.getString(3));
						tab2course5.setText(cur.getString(4));
						tab2course6.setText(cur.getString(5));

						tab2credithrs1.setText(cur.getString(6));
						tab2credithrs2.setText(cur.getString(7));
						tab2credithrs3.setText(cur.getString(8));
						tab2credithrs4.setText(cur.getString(9));
						tab2credithrs5.setText(cur.getString(10));
						tab2credithrs6.setText(cur.getString(11));

						tab2grades1.setText(cur.getString(12));
						tab2grades2.setText(cur.getString(13));
						tab2grades3.setText(cur.getString(14));
						tab2grades4.setText(cur.getString(15));
						tab2grades5.setText(cur.getString(16));
						tab2grades6.setText(cur.getString(17));

						tab2gpa.setText(cur.getString(18));
						cur.moveToNext();
					}
				}
				cur.close();
	
			}
        });
        
        saveGpa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String gpaa = calculateGpa();
				gpa.setText(gpaa);
				String creditHrs1,creditHrs2,creditHrs3,creditHrs4,creditHrs5,creditHrs6;
				String course1,course2,course3,course4,course5,course6,year;
				Log.i(logText,"Entered the save procedure");
				
				Log.i(logText,"calculated gpa is "+gpaa);
				
				//FINDING COURSE TITLE
				  if(coursetitle1.getText().toString().isEmpty() == false){
			        	course1 = coursetitle1.getText().toString();
			        	Log.i(logText,"Course 1 "+ course1);
			        }
			        else{
			        	course1 = "course";
			        	Log.i(logText, "Course 1 "+course1);
			        }
			        
			        if(coursetitle2.getText().toString().isEmpty() == false){
			        	course2 = coursetitle2.getText().toString();
			        	Log.i(logText,"Course 2 "+ course2);
			        }
			        else{
			        	course2 = "course";
			        	Log.i(logText,"Course 2 "+ course2);
			        }
			        
			        if(coursetitle3.getText().toString().isEmpty() == false){
			        	course3 = coursetitle3.getText().toString();
			        	Log.i(logText,"course 3 "+ course3);
			        }
			        else{
			        	course3 = "course";
			        	Log.i(logText,"course 3 "+ course3);
			        }
			        if(coursetitle4.getText().toString().isEmpty() == false){
			        	 course4 = coursetitle4.getText().toString();
			        	 Log.i(logText,"course 4 "+ course4);
			        }
			        else{
			        	course4 = "course";
			        	Log.i(logText,"course 4 "+ course4);
			        }
			        if(coursetitle5.getText().toString().isEmpty() == false){
			        	course5 = coursetitle5.getText().toString();
			        	Log.i(logText,"course 5 "+course5);
			        }
			        else{
			        	course5 = "course";
			        	Log.i(logText,"course 5 "+course5);
			        }
			        if(coursetitle6.getText().toString().isEmpty() == false){
			        	  course6 = coursetitle6.getText().toString();
			        	  Log.i(logText,"course 6 "+course6);
			        }
			        else{
			        	course6 = "course";
			        	Log.i(logText,"course 6 "+course6);
			        }
				
				
				//FIND THE CREDIT HRS
				if(credit1.getText().toString().isEmpty() == false){
					creditHrs1 = credit1.getText().toString();
					Log.i(logText,"credit hrs 1 "+ creditHrs1);
				}
				else{
					creditHrs1 = "credit";
					Log.i(logText,"credit hrs 1 "+ creditHrs1);
				}
				if(credit2.getText().toString().isEmpty() == false){
					creditHrs2 = credit2.getText().toString();
					Log.i(logText,"credit hrs 2 "+ creditHrs2);
				}
				else{
					creditHrs2 = "credit";
					Log.i(logText,"credit hrs 2 "+ creditHrs2);
				}
				if(credit3.getText().toString().isEmpty() == false){
					creditHrs3 = credit3.getText().toString();
					Log.i(logText,"credit hrs 3 "+ creditHrs3);
				}
				else{
					creditHrs3 = "credit";
					Log.i(logText,"credit hrs 3 "+ creditHrs3);
				}
				if(credit4.getText().toString().isEmpty() == false){
					creditHrs4 = credit4.getText().toString();
					Log.i(logText,"credit hrs 4 "+ creditHrs4);
				}
				else{
					creditHrs4 = "credit";
					Log.i(logText,"credit hrs 4 "+ creditHrs4);
				}
				if(credit5.getText().toString().isEmpty() == false){
					creditHrs5 = credit5.getText().toString();
					Log.i(logText,"credit hrs 5 "+ creditHrs5);
				}
				else{
					creditHrs5 = "credit";
					Log.i(logText,"credit hrs 5 "+ creditHrs5);
				}
				if(credit6.getText().toString().isEmpty() == false){
					creditHrs6 = credit6.getText().toString();
					Log.i(logText,"credit hrs 6 "+ creditHrs6);
        		}
				else{
					creditHrs6 = "credit";
					Log.i(logText,"credit hrs 6 "+ creditHrs6);
				}
				if(grade1.isEmpty()){
					grade1 = "-";
					Log.i(logText,"grade 1 "+grade1);
				}
				if(grade2.isEmpty()){
					grade2 = "-";
					Log.i(logText,"grade 2 "+grade2);
				}
				if(grade3.isEmpty()){
					grade3 = "-";
					Log.i(logText,"grade 3 "+grade3);
				}
				if(grade4.isEmpty()){
					grade4 = "-";
					Log.i(logText,"grade 4 "+grade4);
				}
				if(grade5.isEmpty()){
					grade5 = "-";
					Log.i(logText,"grade 5 "+grade5);
				}
				if(grade6.isEmpty()){
					grade6 = "-";
					Log.i(logText,"grade 6 "+grade6);
				}
				
				Log.i(logText,"Before creating content values");
				ContentValues valueToInsert = new ContentValues();
				valueToInsert.put("term", term);
				valueToInsert.put("year",termYear);
				valueToInsert.put("courseTitle1", course1);
				valueToInsert.put("courseTitle2", course2);
				valueToInsert.put("courseTitle3", course3);
				valueToInsert.put("courseTitle4", course4);
				valueToInsert.put("courseTitle5", course5);
				valueToInsert.put("courseTitle6", course6);
				valueToInsert.put("creditHrs1", creditHrs1);
				valueToInsert.put("creditHrs2", creditHrs2);
				valueToInsert.put("creditHrs3", creditHrs3);
				valueToInsert.put("creditHrs4", creditHrs4);
				valueToInsert.put("creditHrs5", creditHrs5);
				valueToInsert.put("creditHrs6", creditHrs6);
				valueToInsert.put("grade1",grade1);
				valueToInsert.put("grade2",grade2);
				valueToInsert.put("grade3",grade3);
				valueToInsert.put("grade4",grade4);
				valueToInsert.put("grade5",grade5);
				valueToInsert.put("grade6",grade6);
				valueToInsert.put("gpa",gpaa);
				
				Log.i(logText,"After creating all values to insert");
				
				Log.i(logText,"Before entering all values into db");
				
				Uri uri = getContentResolver()
						.insert(GPACalculatorContentProvider.CONTENT_URI,
								valueToInsert);
				Log.i(logText,"After entering all values into db");
			}
        });
        
        
        calculateGpa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Log.i(logText,"Before calling calculateGpa() ");
				String finalgpa = calculateGpa();
				Log.i(logText,"After calling calculateGpa() "+ finalgpa);
				gpa.setText(finalgpa);
												
			}
        });
        
    }
    
    public void clearContents(){
    	tab2course1.setText("");
    	tab2course2.setText("");
    	tab2course3.setText("");
    	tab2course4.setText("");
    	tab2course5.setText("");
    	tab2course6.setText("");
    	
    	tab2credithrs1.setText("");
    	tab2credithrs2.setText("");
    	tab2credithrs3.setText("");
    	tab2credithrs4.setText("");
    	tab2credithrs5.setText("");
    	tab2credithrs6.setText("");
    	
    	tab2grades1.setText("");
    	tab2grades2.setText("");
    	tab2grades3.setText("");
    	tab2grades4.setText("");
    	tab2grades5.setText("");
    	tab2grades6.setText("");
    }
    
    public String calculateGpa(){
    	String gpa = "";
    	
    	double creditHrs1 =0.0,creditHrs2=0.0,creditHrs3=0.0,creditHrs4=0.0,creditHrs5=0.0,creditHrs6=0.0;
		
		Log.i(logText,"Before finding credit hrs");
		
		if(credit1.getText().toString().isEmpty() == false){
			creditHrs1 = Integer.parseInt(credit1.getText().toString());
			totalCredits += creditHrs1;
			Log.i(logText,"After calculating credit hrs1: "+creditHrs1);
		}
		
		if(credit2.getText().toString().isEmpty() == false){
			creditHrs2 = Integer.parseInt(credit2.getText().toString());
			totalCredits += creditHrs2;
		}
		
		if(credit3.getText().toString().isEmpty() == false){
			creditHrs3 = Integer.parseInt(credit3.getText().toString());
			totalCredits += creditHrs3;
		}
		
		if(credit4.getText().toString().isEmpty() == false){
			Log.i(logText,"value of credit 4 - " + credit4.getText().toString());
			creditHrs4 = Integer.parseInt(credit4.getText().toString());
			totalCredits += creditHrs4;
		}
		
		if(credit5.getText().toString().isEmpty() == false){
			Log.i(logText,"value of credit 5 - " + credit5.getText().toString());
			creditHrs5 = Integer.parseInt(credit5.getText().toString());
			totalCredits += creditHrs5;
		}
		
		if(credit6.getText().toString().isEmpty() == false){
			creditHrs6 = Integer.parseInt(credit6.getText().toString());
			totalCredits += creditHrs6;
		}
				
		
		//GRADE 1
		
		if(grade1.isEmpty() == false){
			result += creditHrs1*findGrade(grade1);
		}
						
		//GRADE 2
		
		if(grade2.isEmpty() == false){
			result += creditHrs2*findGrade(grade2);
			
		}
						
		//GRADE 3
		
		if(grade3.isEmpty() == false){
			result += creditHrs3*findGrade(grade3);
			
		}
	
		//GRADE 4
		
		if(grade4.isEmpty() == false){
			result += creditHrs4*findGrade(grade4);
			
		}
						
		//GRADE 5
		
		if(grade5.isEmpty() == false){
			result += creditHrs5*findGrade(grade5);
			
		} 
						
		//GRADE 6
		
		if(grade6.isEmpty() == false){
			result += creditHrs6*findGrade(grade6);
			
		}
	
		Log.i(logText,"Value of Count is "+ count);
		Log.i(logText,"Value of result is "+ result);
		
		if(totalCredits == 0.0){
			 finalResult =roundTwoDecimals(result / count);	
		}
		else{
			 finalResult =roundTwoDecimals(result / totalCredits);	
		}

		gpa = Double.toString(finalResult);
		
		totalCredits = 0.0;
		result = 0.0;
		count = 0;
    	
    	return gpa;
    }
    
    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) {
        	
        	switch(parent.getId()){
        	
        	case R.id.termspinner:
        		Log.i(logText,"Term Spinner selected");
        		term = parent.getItemAtPosition(pos).toString();
        		Log.i(logText,"Term selected is " + term);
        		break;
        	
        	case R.id.grade1:
        		Log.i(logText,"Spinner 1 selected");
        		grade1 = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.grade2:
        		Log.i(logText,"Spinner 2 selected");
        		grade2 = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.grade3:
        		Log.i(logText,"Spinner 3 selected");
        		grade3 = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.grade4:
        		Log.i(logText,"Spinner 4 selected");
        		grade4 = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.grade5:
        		Log.i(logText,"Spinner 5 selected");
        		grade5 = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.grade6:
        		Log.i(logText,"Spinner 6 selected");
        		grade6 = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.yearspinner:
        		Log.i(logText,"Year Spinner selected");
        		termYear = parent.getItemAtPosition(pos).toString();
        		Log.i(logText,"Year selected "+termYear);
        	case R.id.tab2termspinner:
        		Log.i(logText,"Tab 2 Term selected");
        		tab2Term = parent.getItemAtPosition(pos).toString();
        		break;
        	case R.id.tab2yearspinner:
        		Log.i(logText,"Tab 2 Year selected");
        		tab2Year = parent.getItemAtPosition(pos).toString();
        		break;

        	}
      	
        }

        public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }

			
    }

	double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
	return Double.valueOf(twoDForm.format(d));
}
    
    
    public double findGrade(String lettergrade){
    	double grade = 0;
    	
    	if(lettergrade.compareToIgnoreCase("A") == 0){
    		grade = 4.0;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("A-") == 0){
    		grade = 3.67;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("B+") == 0){
    		grade = 3.33;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("B") == 0){
    		grade = 3.0;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("B-") == 0){
    		grade = 2.67;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("C+") == 0){
    		grade = 2.33;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("C") == 0){
    		grade = 2.00;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("C-") == 0){
    		grade = 1.70;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("D+") == 0){
    		grade = 1.30;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("D") == 0){
    		grade = 1.00;
    		return grade;
    	}
    	if(lettergrade.compareToIgnoreCase("D-") == 0){
    		grade = 0.70;
    		return grade;
    	}
    	else{
    		grade = 0.0;
    	}
    	
    	return grade;
    }
}