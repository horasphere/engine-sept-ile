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

public class CasNew
{
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
         besoins.add(new Besoin(new CompagnieAffectation("A", "OGBOR"), new Quart("16:00", "0:00", 1), date, "1", "0",
                 false, false));

         List<Employe> employes = new ArrayList();
         QuartDateDejaPlanifies quartDateDejaPlanifies = new QuartDateDejaPlanifies(new ArrayList());
         PeriodesNonDisponibles periodesNonDisponibles = new PeriodesNonDisponibles(new ArrayList());
         Map<CompagnieAffectation, Integer> mapPreferences = new HashMap();
         mapPreferences.put(new CompagnieAffectation("A", "OGBOR"), 3);
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
         besoinToEmployes.put("0", "0");
         besoinToEmployes.put("1", "1");

         Assert.assertEquals("Invalid number of affectations ", besoinToEmployes.size(),
                 assignationBesoinEmployes.size());
         for (AssignationBesoinEmploye assignationBesoinEmploye : assignationBesoinEmployes)
         {
             String employeId = besoinToEmployes.get(assignationBesoinEmploye.getIdBesoin());
             Assert.assertEquals("Invalid assignation " + assignationBesoinEmploye.getIdBesoin(), employeId,
                     assignationBesoinEmploye.getIdEmploye());
         }

     }
}
