package com.horasphere.optlogistec;

import java.util.List;

public interface Optimiseur
{
    List<AssignationBesoinEmploye> optimise(List<Besoin> besoins, List<Employe> employes, List<String> prioriteAffectations);

}
