package com.horasphere.optlogistec;

import java.util.Date;

public class Besoin
{
    private CompagnieAffectation compagnieAffectation;
    private QuartDate quartDate;
    private String id;
    private String idCommande;
    private Boolean pourUnNavire = false;
    private Boolean estDejaCouvert;
    private Boolean rotation;

    public Besoin(CompagnieAffectation compagnieAffectation, Quart quart, Date date, String id, String idCommande, Boolean estDejaCouvert, Boolean rotation)
    {
        super();
        this.compagnieAffectation = compagnieAffectation;
        this.quartDate = new QuartDate(quart, date);
        this.id = id;
        this.idCommande = idCommande;
        this.estDejaCouvert = estDejaCouvert;
        this.rotation = rotation;
    }
    
    public Besoin(CompagnieAffectation companyAffectation, QuartDate quartDate, String id, String idCommande, Boolean estDejaCouvert, Boolean rotation)
    {
        super();
        this.compagnieAffectation = companyAffectation;
        this.quartDate = quartDate;
        this.id = id;
        this.idCommande = idCommande;
        this.estDejaCouvert = estDejaCouvert;
        this.rotation = rotation;
    }
    
    public Besoin(CompagnieAffectation companyAffectation, QuartDate quartDate, String id, String idCommande, Boolean estDejaCouvert, Boolean rotation, Boolean pourUnNavire)
    {
        super();
        this.compagnieAffectation = companyAffectation;
        this.quartDate = quartDate;
        this.id = id;
        this.idCommande = idCommande;
        this.estDejaCouvert = estDejaCouvert;
        this.rotation = rotation;
        this.pourUnNavire = pourUnNavire;
    }
    

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Besoin other = (Besoin) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

   
    public CompagnieAffectation getCompagnieAffectation()
    {
        return compagnieAffectation;
    }

    public QuartDate getQuartDate()
    {
        return quartDate;
    }

    public String getId()
    {
        return id;
    }

    public Boolean getPourUnNavire()
    {
        return pourUnNavire;
    }

    public String getIdCommande()
    {
        return idCommande;
    }

    public Boolean getRotation()
    {
        return rotation;
    }


    public Boolean getEstDejaCouvert()
    {
        return estDejaCouvert;
    }

    @Override
    public String toString()
    {
        return "Besoin [compagnieAffectation=" + compagnieAffectation + ", quartDate=" + quartDate + ", id=" + id
                + ", idCommande=" + idCommande + ", estDejaCouvert=" + estDejaCouvert + ", rotation=" + rotation + "]";
    }

 

}
