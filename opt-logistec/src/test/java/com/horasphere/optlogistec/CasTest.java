package com.horasphere.optlogistec;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import com.horasphere.optlogistec.impl.OptimiseurSequentiel;

public class CasTest
{
    @Test
   //@Ignore
    public void testCas1a()
    {

        // Cas #1
        // Besoin
        // OGBOR rotation 8h00 � 12h00
        // OGBOR rotation 12h00 � 24h00
        // a) Si je suis pr�f�rence jour, je vais avoir 8h00 � 12h00 et 00:00 a
        // 12:00 le lendemain
        // b) Si je suis pr�f�rence nuit, je vais avoir 8h00 � 12h00 (parce
        // qu'il est rotation)

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("8:00", "12:00", 0), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("12:00", "00:00", 1), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("12:00", "0:00", 1), 1);
        mapPreferencesQuart.put(TypeQuart.Jour, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
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
    public void testCas1b()
    {
        // Cas #1
        // Besoin
        // OGBOR rotation 8h00 � 12h00
        // OGBOR rotation 12h00 � 24h00
        // a) Si je suis pr�f�rence jour, je vais avoir 12h00 � 24h00
        // b) Si je suis pr�f�rence nuit, je vais avoir 8h00 � 12h00 (parce
        // qu'il est rotation)

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("8:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("12:00", "00:00", 1), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");

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
    public void testCas2a()
    {
        // Cas #2
        // Besoin:
        // JOURN 8h00 � 22h00 (14 hrs)
        // OGBOR rotation 12h00 � 24h00
        // a) Si je suis pr�f�rence jour et que je pr�f�re OGBOR, je vais avoir
        // JOURN 8h00 � 22h00 (parce que plus long malgr� mes pr�f�rences)
        // b) Si je suis pr�f�rence nuit, je vais avoir rien. (parce qu�il y a
        // de la rotation)

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "22:00", 0), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("12:00", "0:00", 1), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "22:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("12:00", "0:00", 1), 1);
        mapPreferencesQuart.put(TypeQuart.Jour, 2);

        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
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
    public void testCas2b()
    {
        // Cas #2
        // Besoin:
        // JOURN 8h00 � 22h00 (14 hrs)
        // OGBOR rotation 12h00 � 24h00
        // a) Si je suis pr�f�rence jour et que je pr�f�re OGBOR, je vais avoir
        // JOURN 8h00 � 22h00 (parce que plus long malgr� mes pr�f�rences)
        // b) Si je suis pr�f�rence nuit, je vais avoir rien. (parce qu�il y a
        // de la rotation)

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "22:00", 0), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("12:00", "0:00", 1), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("0:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "2");
        besoinToEmployes.put("1", "1");

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
    public void testCas3()
    {
        // Cas #3
        // Besoin:
        // JOURN sans rotation 8h00 � 16h00 ce sont deux quarts de jour
        // JOURN sans rotation 8h00 � 12h00

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "16:00", 0), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Jour, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");

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
    public void testCas4a()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 8h00 � 12h00 ce sont deux quarts de jour
        // JOURN sans rotation 8h00 � 12h00

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

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
  // //@Ignore
    public void testCasNaimePasNuit()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 8h00 � 12h00 ce sont deux quarts de jour
        // JOURN sans rotation 8h00 � 12h00

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("00:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 1);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "1");
        besoinToEmployes.put("1", "2");

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
    // //@Ignore
      public void testCasNaimePasNuit2()
      {
          // Cas #3
          // Besoin:
          // JOURN avec rotation 8h00 � 12h00 ce sont deux quarts de jour
          // JOURN sans rotation 8h00 � 12h00

        System.out.println("PAS NUIT 2");
          List<Besoin> besoins = new ArrayList();
          Date date = new LocalDate(2014, 1, 1).toDate();
          besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("00:00", "12:00", 0), date, "0", "0",
                  false, true));
          besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                  false, true));

          List<Employe> employes = new ArrayList();
          QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
          PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
          Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
          mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
          mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
          Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

          Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
          // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
          // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
          mapPreferencesQuart.put(TypeQuart.Nuit, 1);
          Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
          Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                  preferenceAffectation);
          employes.add(employe);
          employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                  preferenceAffectation);
          employes.add(employe);


          Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
          List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                  new ArrayList());

          Map<String, String> besoinToEmployes = new HashMap();
          besoinToEmployes.put("0", "0");
          besoinToEmployes.put("1", "1");

          System.out.println("PAS NUIT 2 END");
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
    public void testCasRotation()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 8h00 � 12h00 ce sont deux quarts de jour
        // JOURN sans rotation 8h00 � 12h00

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("00:00", "12:00", 0), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

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
    public void testCasRotation2()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 8h00 � 12h00 ce sont deux quarts de jour
        // JOURN sans rotation 8h00 � 12h00

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "2", "0",
                false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "2");

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
    public void testCasRotation3()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 00h00 � 12h00
        // JOURN sans rotation 8h00 � 12h00
        // JOURN avec rotation 12h00 � 00h00

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("12:00", "00:00", 1), date, "2", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("4", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("5", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");

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
    public void testCasRotation4()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 00h00 � 12h00
        // JOURN sans rotation 8h00 � 12h00
        // JOURN avec rotation 12h00 � 00h00

        System.out.println("ROTATION 4");
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("12:00", "00:00", 1), date, "2", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Jour, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("4", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("5", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "2");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "0");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmployes, employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }
        System.out.println("ROTATION 4");

    }

    @Test
   //@Ignore
    public void testCasRotation5()
    {
        // Cas #3
        // Besoin:
        // JOURN avec rotation 00h00 � 12h00
        // JOURN sans rotation 8h00 � 12h00
        // JOURN avec rotation 12h00 � 00h00

        System.out.println("ROTATION 5");
        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("RN", "BJOUR"), new Quart("12:00", "00:00", 1), date, "2", "0",
                false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 2);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 2);
        mapPreferences.put(new CompagnieAffectation("RN", "BJOUR"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
        // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("4", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("5", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "2");

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
     public void testCasRotation6()
     {
         // Cas #3
         // Besoin:
         // JOURN avec rotation 8h00 � 12h00 ce sont deux quarts de jour
         // JOURN sans rotation 8h00 � 12h00

         List<Besoin> besoins = new ArrayList();
         Date date = new LocalDate(2014, 1, 1).toDate();
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "0", "0",
                 false, true));
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                 false, false));
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("0:00", "12:00", 0), date, "2", "0",
                 false, false));

         List<Employe> employes = new ArrayList();
         QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
         PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
         Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
         mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
         mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
         Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

         Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
         // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
         // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
         mapPreferencesQuart.put(TypeQuart.Jour, 2);
         Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         mapPreferencesQuart.put(TypeQuart.Nuit, 2);
         preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);

         Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
         List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                 new ArrayList());

         Map<String, String> besoinToEmployes = new HashMap();
         besoinToEmployes.put("0", "0");
         besoinToEmployes.put("1", "2");
         besoinToEmployes.put("2", "1");

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
     public void testCasRotation7()
     {
   
        System.out.println("TEST CAS ROTATION 7");
         List<Besoin> besoins = new ArrayList();
         Date date = new LocalDate(2014, 1, 1).toDate();
        
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "0", "0",
                 false, false));
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("12:00", "00:00", 1), date, "1", "0",
                 false, true));

         List<Employe> employes = new ArrayList();
         QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
         PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
         Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
         mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
         mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
         Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

         Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
         // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
         // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
         mapPreferencesQuart.put(TypeQuart.Nuit, 2);
         Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         mapPreferencesQuart.put(TypeQuart.Nuit, 2);
         preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);

         Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
         List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                 new ArrayList());

         Map<String, String> besoinToEmployes = new HashMap();
         besoinToEmployes.put("0", "0");
         besoinToEmployes.put("1", "1");

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
     public void testCasRotationJour()
     {
   
        System.out.println("CAS JOUR");
         List<Besoin> besoins = new ArrayList();
         Date date = new LocalDate(2014, 1, 1).toDate();
        
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "0", "0",
                 false, false));
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("12:00", "00:00", 1), date, "1", "0",
                 false, true));

         List<Employe> employes = new ArrayList();
         QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
         PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
         Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
         mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
         mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
         Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

         Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
         // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
         // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
         mapPreferencesQuart.put(TypeQuart.Jour, 2);
         Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         mapPreferencesQuart.put(TypeQuart.Jour, 2);
         preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);

         Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
         List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                 new ArrayList());

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
     public void testCasRotationJour2()
     {
   
        System.out.println("CAS JOUR");
         List<Besoin> besoins = new ArrayList();
         Date date = new LocalDate(2014, 1, 1).toDate();
        
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("8:00", "12:00", 0), date, "0", "0",
                 false, false));
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("12:00", "00:00", 1), date, "1", "0",
                 false, true));
        Date date2 = new LocalDate(2014, 1, 2).toDate();
         besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("00:00", "12:00", 0), date2, "2", "0",
                 false, false));

         List<Employe> employes = new ArrayList();
         QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
         PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
         Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
         mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
         mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 1);
         Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

         Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
         // mapPreferencesQuart.put(new Quart("8:00", "16:00", 0), 1);
         // mapPreferencesQuart.put(new Quart("8:00", "12:00", 0), 1);
         mapPreferencesQuart.put(TypeQuart.Jour, 2);
         Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         mapPreferencesQuart.put(TypeQuart.Jour, 2);
         preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
         employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);
         employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                 preferenceAffectation);
         employes.add(employe);

         Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
         List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                 new ArrayList());

         Map<String, String> besoinToEmployes = new HashMap();
         besoinToEmployes.put("0", "1");
         besoinToEmployes.put("1", "0");
         besoinToEmployes.put("2", "1");

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
    public void testCasRelaisNordik1()
    {

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("RN", "BJOUR"), new Quart("7:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("LA", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("LA", "JOURN"), new Quart("12:00", "00:00", 1), date, "2", "0",
                false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("RN", "BJOUR"), 1);
        mapPreferences.put(new CompagnieAffectation("LA", "JOURN"), 2);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();

        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("4", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("5", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "2");
        besoinToEmployes.put("1", "1");
        besoinToEmployes.put("2", "0");

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
    public void testCasRelaisNordik2()
    {

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("RN", "BJOUR"), new Quart("7:00", "12:00", 0), date, "0", "0",
                false, true));
        besoins.add(new Besoin(new CompagnieAffectation("LA", "JOURN"), new Quart("8:00", "12:00", 0), date, "1", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("LA", "JOURN"), new Quart("12:00", "00:00", 1), date, "2", "0",
                false, false));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("RN", "BJOUR"), 2);
        mapPreferences.put(new CompagnieAffectation("LA", "JOURN"), 1);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();

        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("3", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("4", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("5", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "0");
        besoinToEmployes.put("1", "2");
        besoinToEmployes.put("2", "1");

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
    public void testChangementOrdreObjectif()
    {
        // Cas #2
        // Besoin:
        // JOURN 8h00 � 22h00 (14 hrs)
        // OGBOR rotation 12h00 � 24h00
        // a) Si je suis pr�f�rence jour et que je pr�f�re OGBOR, je vais avoir
        // JOURN 8h00 � 22h00 (parce que plus long malgr� mes pr�f�rences)
        // b) Si je suis pr�f�rence nuit, je vais avoir rien. (parce qu�il y a
        // de la rotation)

        List<Besoin> besoins = new ArrayList();
        Date date = new LocalDate(2014, 1, 1).toDate();
        besoins.add(new Besoin(new CompagnieAffectation("A", "JOURN"), new Quart("12:00", "0:00", 1), date, "0", "0",
                false, false));
        besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("12:00", "0:00", 1), date, "1", "0",
                false, true));

        List<Employe> employes = new ArrayList();
        QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
        PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
        Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
        mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 1);
        mapPreferences.put(new CompagnieAffectation("A", "JOURN"), 2);
        Preference<CompagnieAffectation> preferenceAffectation = new Preference<CompagnieAffectation>(mapPreferences);

        Map<TypeQuart, Integer> mapPreferencesQuart = new HashMap();
        // mapPreferencesQuart.put(new Quart("0:00", "12:00", 0), 1);
        mapPreferencesQuart.put(TypeQuart.Nuit, 2);
        Preference<TypeQuart> preferencesQuart = new Preference<TypeQuart>(mapPreferencesQuart);
        Employe employe = new Employe("0", 0, quartDateDejaPlanifies, periodesNonDisponibles, preferencesQuart,
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("1", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);
        employe = new Employe("2", 0, quartDateDejaPlanifies, periodesNonDisponibles, new Preference(),
                preferenceAffectation);
        employes.add(employe);

        Optimiseur optimiseur = new OptimiseurSequentiel(true, 1);
        List<AssignationBesoinEmploye> assignationBesoinEmployes = optimiseur.optimise(besoins, employes,
                new ArrayList());

        Map<String, String> besoinToEmployes = new HashMap();
        besoinToEmployes.put("0", "2");
        besoinToEmployes.put("1", "1");

        Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                assignationBesoinEmployes.size());
        for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
        {
            String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
            Assert.assertEquals("Invalid assignation " + assignationBesoinEmployes, employeId,
                    assignationBesoinEmploye.getIdEmploye());
        }

    }

}
