package com.football.college.domain.api;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mark
 *
 */
public class ApiScoreboard 
{
   protected List<String> columnNames = new ArrayList<String>();
   protected List<Object> rows = new ArrayList<Object>();
   protected List<Integer> totals = new ArrayList<Integer>();
   
   public ApiScoreboard()
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
   
   public void addRow(ApiGame g, List<String> rowOfValues)
   {
      List<Object> row = new ArrayList<Object>();
      row.add(g);
      row.addAll(1, rowOfValues);
      rows.add(row);
   }
   
   public void addColumn(String s)
   {
      columnNames.add(s);
   }
}