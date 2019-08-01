package com.horasphere.optlogistec;

import junit.framework.Assert;

import org.junit.Test;

import com.horasphere.engine.api.Shift;

public class TypeQuartTest
{
    @Test
   public void testQuartDeJour1()
   {
       Shift shift = new Shift("12:00", "00:00",1,"ROTATION_VIRTUAL");
       Assert.assertEquals(TypeQuart.Jour, TypeQuart.findTypeQuart(shift));
   }
    @Test
    public void testQuartDeJour2()
    {
        Shift shift = new Shift("8:00", "12:00",0,"VIRTUAL");
        Assert.assertEquals(TypeQuart.Jour, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeJour3()
    {
        Shift shift = new Shift("12:00", "16:00",0,"ROTATION_VIRTUAL");
        Assert.assertEquals(TypeQuart.Jour, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeNuit1()
    {
        Shift shift = new Shift("00:00", "12:00",0,"ROTATION_VIRTUAL");
        Assert.assertEquals(TypeQuart.Nuit, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeNuit2()
    {
        Shift shift = new Shift("8:00", "12:00",0,"ROTATION_VIRTUAL");
        Assert.assertEquals(TypeQuart.Nuit, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeNuit3()
    {
        Shift shift = new Shift("00:00", "08:00",0,"VIRTUAL");
        Assert.assertEquals(TypeQuart.Nuit, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeJourLogistecFer1()
    {
        Shift shift = new Shift("04:00", "16:00",0,"COMPAGNIE_LF");
        Assert.assertEquals(TypeQuart.Jour, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeJourLogistecFer2()
    {
        Shift shift = new Shift("15:59", "20:00",0,"COMPAGNIE_LF");
        Assert.assertEquals(TypeQuart.Jour, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeNuitLogistecFer1()
    {
        Shift shift = new Shift("16:00", "02:00",1,"COMPAGNIE_LF");
        Assert.assertEquals(TypeQuart.Nuit, TypeQuart.findTypeQuart(shift));
    }
  
    @Test
    public void testQuartDeNuitLogistecFer2()
    {
        Shift shift = new Shift("3:00", "12:00",0,"COMPAGNIE_LF");
        Assert.assertEquals(TypeQuart.Nuit, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeNuitLogistecFer3()
    {
        Shift shift = new Shift("2:00", "14:00",0,"COMPAGNIE_LF");
        Assert.assertEquals(TypeQuart.Nuit, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuartDeNuitLogistecFer4()
    {
        Shift shift = new Shift("16:00", "02:00",1,"A");
        Assert.assertEquals(TypeQuart.Autre, TypeQuart.findTypeQuart(shift));
    }
    @Test
    public void testQuart()
    {
        Shift shift = new Shift("18:00", "00:00",1,"ROTATION");
         Assert.assertEquals(TypeQuart.Jour, TypeQuart.findTypeQuart(shift));
        
    }
}
