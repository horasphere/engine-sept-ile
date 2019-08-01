package com.horasphere.optlogistec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.horasphere.engine.api.Employee;
import com.horasphere.optlogistec.impl.OptimiseurSequentiel;
import com.thoughtworks.xstream.XStream;


public  class OptimiseurSequentielTest
{
    @Test
 //@Ignore
    public void testDeBase()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "0");
        besoinToEmployes.put("3", "1");
        besoinToEmployes.put("4", "2");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmployes, employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

    @Test
 //@Ignore
    public void testDeBaseAvecDoublons()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "5", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "0");
        besoinToEmployes.put("3", "1");
        besoinToEmployes.put("4", "2");
        besoinToEmployes.put("5", "3");
        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

    @Test
 //@Ignore
    public void testAvecBesoinDejaCouvert()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", true, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "0");
        besoinToEmployes.put("3", "1");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation ", assignationBesoinEmploye.getIdEmploye(), employeId);
        }
    }

    @Test
 //@Ignore
    public void testAvecPeriodeDeNonDisponibilite()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        List<QuartDate> quartDates = new ArrayList();
        quartDates.add(new QuartDate(new Quart("0:00", "0:00", 1), date));
        PeriodesNonDisponibles periodesNonDisponiblesEmploye0 = new PeriodesNonDisponibles(quartDates);
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponiblesEmploye0, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation ", assignationBesoinEmploye.getIdEmploye(), employeId);
        }
    }

    @Test
 //@Ignore
    public void testAvecPeriodesNonDisponiblesEtQuartDejaPlanifie()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", true, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        List<QuartDate> quartDates = new ArrayList();
        quartDates.add(new QuartDate(new Quart("0:00", "0:00", 1), date));
        QuartDateDejaPlanifies quartDateDejaPlanifiesEmploye0 = new QuartDateDejaPlanifies(quartDates);
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifiesEmploye0, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation ", assignationBesoinEmploye.getIdEmploye(), employeId);
        }
    }

    @Test
 //@Ignore
    public void testPreference()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "21:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","grue"), new Quart("7:00", "12:00", 0), date, "5", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        mapPreferences.put(new CompagnieAffectation("A","grue"), 2);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "0");
        besoinToEmployes.put("3", "1");
        besoinToEmployes.put("4", "2");
        besoinToEmployes.put("5", "0");
        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

   

    @Test
 //@Ignore
    public void testPrioriteAffectations()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "21:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","grue"), new Quart("10:00", "14:00", 0), date, "5", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);

        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<CompagnieAffectation, Integer> mapPreferencesGrues = new HashMap();
        mapPreferencesGrues.put(new CompagnieAffectation("A","debardeur"), 1);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue"), 2);
        Preference<CompagnieAffectation> preferenceAffectationEmploye0 = new Preference<CompagnieAffectation>(mapPreferencesGrues);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectationEmploye0);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList(Arrays.asList("grue", "debardeur")));

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");
        besoinToEmployes.put("5", "0");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }
    
    @Test
 //@Ignore
    public void testPrioriteAffectations2()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();

        besoins.add(new Besoin(new CompagnieAffectation("A","grue1"), new Quart("10:00", "14:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","grue2"), new Quart("10:00", "14:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "2", "0", false, false));
        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);

        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<CompagnieAffectation, Integer> mapPreferencesGrues = new HashMap();
        mapPreferencesGrues.put(new CompagnieAffectation("A","debardeur"), 1);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue1"), 2);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue2"), 2);
        Preference<CompagnieAffectation> preferenceAffectationEmploye0 = new Preference<CompagnieAffectation>(mapPreferencesGrues);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectationEmploye0);
        employes.add(employe);
        mapPreferencesGrues = new HashMap();
        mapPreferencesGrues.put(new CompagnieAffectation("A","debardeur"), 2);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue1"), 2);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue2"), 2);
        Preference<CompagnieAffectation> preferenceAffectationEmploye1 = new Preference<CompagnieAffectation>(mapPreferencesGrues);
    
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectationEmploye1);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList(Arrays.asList("grue1","grue2", "debardeur")));

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "2");
       

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

    @Test
 //@Ignore
    public void testConsecutiveHoursActive()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "21:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "22:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","grue"), new Quart("4:00", "12:00", 0), date, "5", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);

        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<CompagnieAffectation, Integer> mapPreferencesGrues = new HashMap();
        mapPreferencesGrues.put(new CompagnieAffectation("A","debardeur"), 1);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue"), 2);
        Preference<CompagnieAffectation> preferenceAffectationEmploye0 = new Preference<CompagnieAffectation>(mapPreferencesGrues);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectationEmploye0);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList(Arrays.asList("grue", "debardeur")));

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");
        besoinToEmployes.put("4", "0");
        besoinToEmployes.put("5", "0");

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

    @Test
 //@Ignore
    public void testConsecutiveHoursNonActive()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "21:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","grue"), new Quart("4:00", "12:00", 0), date, "5", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);

        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<CompagnieAffectation, Integer> mapPreferencesGrues = new HashMap();
        mapPreferencesGrues.put(new CompagnieAffectation("A","debardeur"), 1);
        mapPreferencesGrues.put(new CompagnieAffectation("A","grue"), 2);
        Preference<CompagnieAffectation> preferenceAffectationEmploye0 = new Preference<CompagnieAffectation>(mapPreferencesGrues);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectationEmploye0);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList(Arrays.asList("grue","affectation")));

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "0");
        besoinToEmployes.put("4", "2");
        besoinToEmployes.put("5", "0");

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

    @Test
 //@Ignore //TODO FIXME
    public void testAvecBesoinRotation()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "13:00", 0), date, "0", "0", false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "1", "0", false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        mapPreferencesQuart.put(TypeQuart.Jour, 2);

        Preference<TypeQuart> preferenceQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferenceQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 999);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();

        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "0");


        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals(
                    "Invalid assignation " + assignationBesoinEmployes + " " + assignationBesoinEmploye.getIdBesoin(),
                    employeId, assignationBesoinEmploye.getIdEmploye());
        }

    }

   

   

    @Test
 //@Ignore
    public void testAvecUneAffectation()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("11:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(false, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();

        besoinToEmployes.put("2", "0");
        besoinToEmployes.put("3", "1");
        besoinToEmployes.put("4", "2");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmployes, employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }
    
    @Test
 //@Ignore
    public void testEquite()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "12:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("B","debardeur"), new Quart("8:00", "12:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("C","debardeur"), new Quart("9:00", "12:00", 0), date, "3", "0", false, false));



        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        mapPreferences.put(new CompagnieAffectation("B","debardeur"), 1);
        mapPreferences.put(new CompagnieAffectation("C","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
      

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");
  

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye + " " +  assignationBesoinEmployes, employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }

    @Test
   // @Ignore
    public void testAvecFichier()
    {
        System.out.println("Fichier");
        XStream xStream = new XStream();
        Vector<Employe> employes = (Vector) xStream.fromXML(FileUtils.toFile(this.getClass().getResource(
                "/employes.xml")));
        Vector<Besoin> besoins = (Vector) xStream.fromXML(FileUtils.toFile(this.getClass()
                .getResource("/besoins.xml")));
        Vector<Besoin> affectations = (Vector) xStream.fromXML(FileUtils.toFile(this.getClass()
                .getResource("/affectations.xml")));
        OptimiseurSequentiel optimiseurSequentiel = new OptimiseurSequentiel(true, 1);
       
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseurSequentiel.optimise(
                new ArrayList(besoins), new ArrayList(employes), new ArrayList(affectations));
        Set<String> visited = new HashSet();

        System.out.println("Result");
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String besoinString = "";
            for(Besoin besoin : besoins)
            {
                if(besoin.getId().equals(assignationBesoinEmploye.getIdBesoin()))
                {
                    besoinString = besoin.getIdCommande() + " " + besoin.getCompagnieAffectation().getCompagnie() + " " + besoin.getCompagnieAffectation().getAffectation();
                }
            }
            System.out.println(assignationBesoinEmploye.getIdEmploye() + " " + besoinString);
            for(Employe employe : employes)
            {
                if(employe.getId().equals(assignationBesoinEmploye.getIdEmploye()))
                {
                    System.out.println(employe);
                }
            }
            visited.add(assignationBesoinEmploye.getIdBesoin());
        }
          
        for(Besoin besoin : besoins)
        {
            
            if(!visited.contains(besoin.getId()))
            {
                System.out.println("Ce besoin ne peut etre couvert " + besoin);
            }
        }
         System.out.println("Fichier");
    }
    
    @Test
 //@Ignore
    public void testAvecPrioriteDeNavire()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new QuartDate(new Quart("8:00", "11:00", 0), date), "0", "0", false, false,true));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"),new QuartDate( new Quart("7:00", "12:00", 0), date), "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new QuartDate(new Quart("8:00", "12:00", 0), date), "2", "0", false, false));
       
        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList(Arrays.asList("debardeur")));

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "0");
       

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmployes, employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
    }
    
    @Test
 //@Ignore
    public void testAvecQuartDejaPlanifieEtDeuxAffectations()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "20:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        List<QuartDate> quartDates = new ArrayList();
        quartDates.add(new QuartDate(new Quart("0:00", "12:30", 0), date));
        QuartDateDejaPlanifies quartDateDejaPlanifiesEmploye0 = new QuartDateDejaPlanifies(quartDates);
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifiesEmploye0, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel();
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation ", assignationBesoinEmploye.getIdEmploye(), employeId);
        }
    }
    
    @Test
 //@Ignore
    public void testAvecQuartDejaPlanifieEtUneAffectation()
    {
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("7:00", "12:00", 0), date, "0", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("9:00", "13:00", 0), date, "1", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("12:00", "21:00", 0), date, "2", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "21:00", 0), date, "3", "0", false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A","debardeur"), new Quart("13:00", "20:00", 0), date, "4", "0", false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());

        List<QuartDate> quartDates = new ArrayList();
        quartDates.add(new QuartDate(new Quart("0:00", "12:30", 0), date));
        QuartDateDejaPlanifies quartDateDejaPlanifiesEmploye0 = new QuartDateDejaPlanifies(quartDates);
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A","debardeur"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifiesEmploye0, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 1, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 2, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true,1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
     //   besoinToEmployes.put("0", "1");
        besoinToEmployes.put("2", "1");
        besoinToEmployes.put("3", "2");


        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());

        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation ", assignationBesoinEmploye.getIdEmploye(), employeId);
        }
    }


}