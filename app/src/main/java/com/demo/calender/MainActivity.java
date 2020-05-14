package com.demo.calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int currentYear = 0;
    private int currentMonth = 0;
    private int currentDay = 0;

    private int index = 0;
    private List<String> calendarStrings;
    private int[] days;
    private int[] months;
    private int[] years;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CalendarView calendarView = findViewById(R.id.calendarView);


        calendarStrings = new ArrayList<>();
        final int numberOfDays = 2000;
        days = new int[numberOfDays];
        months = new int[numberOfDays];
        years = new int[numberOfDays];

        readInfo();

        final EditText textInput = findViewById(R.id.inputText);
        final View dayContent = findViewById(R.id.dayContent);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                currentDay = dayOfMonth;
                currentMonth = month;
                currentYear = year;

                if (dayContent.getVisibility() == View.GONE) {
                    dayContent.setVisibility(View.VISIBLE);
                }

                for (int h = 0; h < index; h++) {
                    if (years[h] == currentYear) {
                        for (int i = 0; i < index; i++) {
                            if (days[i] == currentDay) {
                                for (int j = 0; j < index; j++) {
                                    if (months[j] == currentMonth && days[j] == currentDay && years[j] == currentYear) {
                                        textInput.setText(calendarStrings.get(j));
                                        return;
                                    }
                                }

                            }
                        }
                    }
                }

                textInput.setText("");
            }

        });

        final Button saveTextButton = findViewById(R.id.saveTextButton);

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                days[index] = currentDay;
                months[index] = currentMonth;
                years[index] = currentYear;

                //calendarStrings.add(String.valueOf(calendarView.getDate()));  //secilen tarihi listeye ekledik
                calendarStrings.add(index, textInput.getText().toString());   //yazılan notu listeye ekledik

                textInput.setText("");
                index++;
                dayContent.setVisibility(View.GONE);

            }
        });
        final Button todayButton = findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setDate(calendarView.getDate()); //today butonu ile gnümüze set ettik
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInfo();
    }

    private void saveInfo() {
        File file=new File(this.getFilesDir(),"saved");
        File daysFile=new File(this.getFilesDir(),"days");
        File monthsFile=new File(this.getFilesDir(),"months");
        File yearsFile=new File(this.getFilesDir(),"years");

        try {
            FileOutputStream fOut=new FileOutputStream(file);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fOut));

            FileOutputStream fOutDays=new FileOutputStream(daysFile);
            BufferedWriter bwDays=new BufferedWriter(new OutputStreamWriter(fOutDays));

            FileOutputStream fOutMonths=new FileOutputStream(monthsFile);
            BufferedWriter bwMonths=new BufferedWriter(new OutputStreamWriter(fOutMonths));

            FileOutputStream fOutYears=new FileOutputStream(yearsFile);
            BufferedWriter bwYears=new BufferedWriter(new OutputStreamWriter(fOutYears));

            for (int i=0;i<index;i++){
                bw.write(calendarStrings.get(i));
                bw.newLine();
                bwDays.write(days[i]);
                bwMonths.write(months[i]);
                bwYears.write(years[i]);

            }
            bw.close();
            fOut.close();
            bwDays.close();
            fOutDays.close();
            bwMonths.close();
            fOutMonths.close();
            bwYears.close();
            fOutYears.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

     /*   try {
            FileWriter filewriter = new FileWriter("calendarStrings");
            final int calendarStringsCount = calendarStrings.size();
            for (int i = 0; i < calendarStringsCount; i++) {
                filewriter.write(calendarStrings.get(i));
            }
            filewriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private void readInfo() {

        File file=new File(this.getFilesDir(),"saved");
        File daysFile=new File(this.getFilesDir(),"days");
        File monthsFile=new File(this.getFilesDir(),"months");
        File yearsFile=new File(this.getFilesDir(),"years");

        if (!file.exists()){
            return;
        }
        try {
            FileInputStream is=new FileInputStream(file);
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));

            FileInputStream isDays=new FileInputStream(daysFile);
            BufferedReader readerDays=new BufferedReader(new InputStreamReader(isDays));

            FileInputStream isMonths=new FileInputStream(monthsFile);
            BufferedReader readerMonths=new BufferedReader(new InputStreamReader(isMonths));

            FileInputStream isYears=new FileInputStream(yearsFile);
            BufferedReader readerYears=new BufferedReader(new InputStreamReader(isYears));

            int i=0;
            String line=reader.readLine();

            while (line!=null){
                calendarStrings.add(line);
                line=reader.readLine();
                days[i]=readerDays.read();
                months[i]=readerMonths.read();
                years[i]=readerYears.read();
                i++;

            }
            index=i;
        }catch (Exception e){
            e.printStackTrace();
        }

        /*try {
            BufferedReader reader = new BufferedReader(new FileReader("calendarStrings"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                calendarStrings.add(line);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
