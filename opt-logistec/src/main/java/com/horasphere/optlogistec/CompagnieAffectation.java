package com.horasphere.optlogistec;

public class CompagnieAffectation
{
    private String compagnie;
    private String affectation;

    public CompagnieAffectation(String compagnie, String affectation)
    {
        super();
        this.compagnie = compagnie;
        this.affectation = affectation;
    }

    public String getCompagnie()
    {
        return compagnie;
    }

    public String getAffectation()
    {
        return affectation;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((affectation == null) ? 0 : affectation.hashCode());
        result = prime * result + ((compagnie == null) ? 0 : compagnie.hashCode());
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
        CompagnieAffectation other = (CompagnieAffectation) obj;
        if (affectation == null)
        {
            if (other.affectation != null)
                return false;
        }
        else if (!affectation.equals(other.affectation))
            return false;
        if (compagnie == null)
        {
            if (other.compagnie != null)
                return false;
        }
        else if (!compagnie.equals(other.compagnie))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "CompagnieAffectation [compagnie=" + compagnie + ", affectation=" + affectation + "]";
    }

}
