package com.horasphere.optlogistec;

public class AssignationBesoinEmploye
{
    private String idBesoin;
    private String idEmploye;

    public AssignationBesoinEmploye(String idBesoin, String idEmploye)
    {
        super();
        this.idBesoin = idBesoin;
        this.idEmploye = idEmploye;
    }

    public String getIdBesoin()
    {
        return idBesoin;
    }

    public String getIdEmploye()
    {
        return idEmploye;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idBesoin == null) ? 0 : idBesoin.hashCode());
        result = prime * result + ((idEmploye == null) ? 0 : idEmploye.hashCode());
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
        AssignationBesoinEmploye other = (AssignationBesoinEmploye) obj;
        if (idBesoin == null)
        {
            if (other.idBesoin != null)
                return false;
        }
        else if (!idBesoin.equals(other.idBesoin))
            return false;
        if (idEmploye == null)
        {
            if (other.idEmploye != null)
                return false;
        }
        else if (!idEmploye.equals(other.idEmploye))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "AssignationBesoinEmploye [idBesoin=" + idBesoin + ", idEmploye=" + idEmploye + "]";
    }

}
