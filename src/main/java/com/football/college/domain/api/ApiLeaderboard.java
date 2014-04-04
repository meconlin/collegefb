package com.football.college.domain.api;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mark
 *
 */
public class ApiLeaderboard 
{
   protected List<String> columnNames = new ArrayList<String>();
   protected List<Object> rows = new ArrayList<Object>();
   protected List<Integer> totals = new ArrayList<Integer>();
   protected List<String> averages = new ArrayList<String>();
   
   public ApiLeaderboard()
   {
      
   }
      
   public List<String> getColumnNames()
   {
      return columnNames;
   }

   public List<Object> getRows()
   {
      return rows;
   }

   public List<Integer> getTotals()
   {
      return totals;
   } 
   
   public void addTotals(List<Integer> totals)
   {
      this.totals = totals;
   }
   
   public List<String> getAverages()
   {
      return averages;
   }

   public void addAverages(List<String> averages)
   {
      this.averages = averages;
   }

   public void addRow(Integer weekNumber, List<String> rowOfValues)
   {
      List<Object> row = new ArrayList<Object>();
      row.add(weekNumber);
      row.addAll(1, rowOfValues);
      rows.add(row);
   }
   
   public void addColumn(String s)
   {
      columnNames.add(s);
   }
}