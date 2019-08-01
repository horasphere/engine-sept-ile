package com.horasphere.optlogistec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.horasphere.optlogistec.impl.OptimiseurSequentiel;
import com.thoughtworks.xstream.XStream;

public class TestAvecFichier
{
    @Test
    public void testAvecFichier()
    {
        System.out.println("Fichier");
        XStream xStream = new XStream();
        Vector<Employe> employes = (Vector) xStream.fromXML(FileUtils.toFile(this.getClass().getResource(
                "/employes.txt")));
        Vector<Besoin> besoins = (Vector) xStream.fromXML(FileUtils.toFile(this.getClass()
                .getResource("/besoins.txt")));
        Vector<Besoin> affectations = (Vector) xStream.fromXML(FileUtils.toFile(this.getClass()
                .getResource("/affectations.txt")));
        for(Employe employe : employes)
        {
            if(employe.getId().equals("47"))
            {
                System.out.println("YE");
               // employe.getQuartNonDisponibles().getQuartDates().add(new QuartDate(new Quart("00:00", "12:00",0),new LocalDate(2015,3,26).toDate()));
                System.out.println(employe.getQuartNonDisponibles().getQuartDates());
            }
        }
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
}
