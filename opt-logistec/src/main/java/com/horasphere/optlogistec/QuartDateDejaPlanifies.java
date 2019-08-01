package com.horasphere.optlogistec;

import java.util.List;

public class QuartDateDejaPlanifies
{
    private List<QuartDate> quartDates;

    public QuartDateDejaPlanifies(List<QuartDate> quartDates)
    {
        super();
        this.quartDates = quartDates;
    }

    public List<QuartDate> getQuartDates()
    {
        return quartDates;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quartDates == null) ? 0 : quartDates.hashCode());
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
        QuartDateDejaPlanifies other = (QuartDateDejaPlanifies) obj;
        if (quartDates == null)
        {
            if (other.quartDates != null)
                return false;
        }
        else if (!quartDates.equals(other.quartDates))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "QuartDateDejaPlanifies [quartDates=" + quartDates + "]";
    }

}
