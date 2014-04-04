package com.football.college.domain.api;

public class TimeLabelValue
{
   protected String label;
   protected String value;
   
   /*******************************/
   
   public TimeLabelValue()
   {
      
   }
   
   public TimeLabelValue(String label, String value)
   {
      this.label = label;
      this.value = value;
   }
   
   public String getLabel()
   {
      return label;
   }
   public void setLabel(String label)
   {
      this.label = label;
   }
   public String getValue()
   {
      return value;
   }
   public void setValue(String value)
   {
      this.value = value;
   }
}
