package com.horasphere.optlogistec;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.horasphere.optlogistec.impl.EvaluateurDisponibiliteDeBase;

public class EvaluateurDisponibiliteTest
{
    public Employe createEmploye()
    {
        List<QuartDate> quartDates = new ArrayList();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        quartDates.add(new QuartDate(new Quart("8:00", "23:00", 0), localDate.toDate()));
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(quartDates);
        quartDates = new ArrayList();
        localDate = new LocalDate(2014, 1, 2);
        quartDates.add(new QuartDate(new Quart("8:00", "23:00", 0), localDate.toDate()));
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(quartDates);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                new Preference());

        return employe;

    }

    public Employe changeRestForReset(Employe employe, Double restForReset)
    {
        return new Employe(employe.getId(),employe.getSeniorite(),employe.getQuartDejaPlanifies(), employe.getQuartNonDisponibles(), employe.getPreferenceQuarts(), employe.getPreferenceCompagnieAffectations(),employe.getMaxConsecutiveHours(),restForReset, employe.getMaxConsecutiveHoursToStartJob());
    }
    @Test
    public void testDisponibleCrossMidnight2()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("23:00", "02:00", 1), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 0.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertTrue(status);

    }

    @Test
    public void testDisponibleCrossMidnight3()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("23:00", "02:00", 1), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 0.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertTrue(status);

    }

    @Test
    public void testNonDisponibleCrossMidnight1()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 2);
        QuartDate candidate = new QuartDate(new Quart("00:00", "01:00", 0), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 2.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertFalse(status);

        evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        employe = changeRestForReset(employe, 1.0);
        status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertTrue(status);

    }

    @Test
    public void testNonDisponibleCrossMidnight2()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("22:00", "02:00", 1), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 0.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertFalse(status);

    }

    @Test
    public void testNonDisponibleCrossMidnight3()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("22:00", "23:00", 0), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 0.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertFalse(status);

    }

    @Test
    public void testNonDisponibleCrossMidnight4()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("23:30", "23:00", 1), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 1.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertFalse(status);

    }

    @Test
    public void testNonDisponibleBefore1()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("6:00", "7:30", 0), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 1.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertFalse(status);

        evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        employe = changeRestForReset(employe, 0.0);
        status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);
        Assert.assertTrue(status);

    }

    @Test
    public void testNonDisponibleBefore2()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("6:00", "8:00", 0), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 0.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertTrue(status);

        evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        employe = changeRestForReset(employe, 0.001);
        status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);
        Assert.assertFalse(status);

    }

    @Test
    public void testNonDisponibleConge1()
    {
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        LocalDate localDate = new LocalDate(2014, 1, 2);
        QuartDate candidate = new QuartDate(new Quart("8:00", "23:00", 0), localDate.toDate());
        Employe employe = createEmploye();
        employe = changeRestForReset(employe, 24.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);

        Assert.assertFalse(status);

    }

    @Test
    public void testAvecDeuxAffectation()
    {
        List<QuartDate> quartDates = new ArrayList();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        quartDates.add(new QuartDate(new Quart("8:00", "12:00", 0), localDate.toDate()));
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(quartDates);
        quartDates = new ArrayList();
    
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(quartDates);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                new Preference());
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase();
        employe = changeRestForReset(employe, 0.0);
        localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("13:00", "17:00", 0), localDate.toDate());

        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);
        Assert.assertTrue(status);

    }
    
    
    @Test
    public void testAvecUneAffectation()
    {
        List<QuartDate> quartDates = new ArrayList();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        quartDates.add(new QuartDate(new Quart("8:00", "12:00", 0), localDate.toDate()));
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(quartDates);
        quartDates = new ArrayList();
    
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(quartDates);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                new Preference());
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase(1);
        localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("13:00", "17:00", 0), localDate.toDate());
        employe = changeRestForReset(employe, 0.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);
        Assert.assertFalse(status);

    }
    
    @Test
    public void testAvecPlusieursQuarts1()
    {
        List<QuartDate> quartDates = new ArrayList();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        quartDates.add(new QuartDate(new Quart("8:00", "12:00", 0), localDate.toDate()));
        quartDates.add(new QuartDate(new Quart("14:00", "16:00", 0), localDate.toDate()));
        quartDates.add(new QuartDate(new Quart("16:00", "21:00", 0), localDate.toDate()));
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(quartDates);
        quartDates = new ArrayList();
    
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(quartDates);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                new Preference());
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase(10);
        localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("23:00", "10:00", 1), localDate.toDate());
        employe = changeRestForReset(employe, 2.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);
        Assert.assertTrue(status);

    }
    
    @Test
    public void testAvecPlusieursQuarts2()
    {
        List<QuartDate> quartDates = new ArrayList();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        quartDates.add(new QuartDate(new Quart("8:00", "12:00", 0), localDate.toDate()));
        quartDates.add(new QuartDate(new Quart("14:00", "16:00", 0), localDate.toDate()));
        quartDates.add(new QuartDate(new Quart("16:00", "21:00", 0), localDate.toDate()));
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(quartDates);
        quartDates = new ArrayList();
    
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(quartDates);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                new Preference());
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase(10);
        localDate = new LocalDate(2014, 1, 1);
        QuartDate candidate = new QuartDate(new Quart("23:00", "10:00", 1), localDate.toDate());
        employe = changeRestForReset(employe, 6.0);
        Boolean status = evaluateurDisponibilteDeBase.estDisponible(candidate, employe);
        Assert.assertFalse(status);

    }
    
    public Employe createEmployeInvalide()
    {
        List<QuartDate> quartDates = new ArrayList();
        LocalDate localDate = new LocalDate(2014, 1, 1);
        quartDates.add(new QuartDate(new Quart("4:00", "23:00", 0), localDate.toDate()));
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(quartDates);
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(quartDates);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                new Preference());

        return employe;

    }
    
    
    @Test
    public void testAvecEmployeInvalide1()
    {
        Employe employe = createEmployeInvalide();
        LocalDate localDate = new LocalDate(2014, 1, 2);
        QuartDate candidate = new QuartDate(new Quart("6:00", "10:00", 0), localDate.toDate());
       
        EvaluateurDisponibiliteDeBase evaluateurDisponibiliteDeBase = new EvaluateurDisponibiliteDeBase(1);
        Boolean status = evaluateurDisponibiliteDeBase.estDisponible(candidate, employe);
        Assert.assertTrue(status);

    }
    
    @Test
    public void testAvecEmployeInvalide2()
    {
        Employe employe = createEmployeInvalide();
        LocalDate localDate = new LocalDate(2014, 1, 2);
        QuartDate candidate = new QuartDate(new Quart("4:00", "10:00", 0), localDate.toDate());
       
        EvaluateurDisponibiliteDeBase evaluateurDisponibiliteDeBase = new EvaluateurDisponibiliteDeBase(1);
        Boolean status = evaluateurDisponibiliteDeBase.estDisponible(candidate, employe);
        Assert.assertFalse(status);

    }
    
  
    
    
}
