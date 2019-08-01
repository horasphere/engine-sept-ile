package com.horasphere.optlogistec;

import java.util.List;

import com.horasphere.engine.api.Period;

public class PeriodesNonDisponibles
{
    private List<QuartDate> quartDates;

    public PeriodesNonDisponibles(List<QuartDate> quartDates)
    {
        super();
        this.quartDates = quartDates;
    }
    
    public List<QuartDate> getQuartDates()
    {
        return quartDates;
    }

    public Boolean estNonDisponible(QuartDate quartDate)
    {
        Period current = quartDate.getQuart().generatePeriod(quartDate.getDate());
        for(QuartDate qd : quartDates)
        {
            Period period = qd.getQuart().generatePeriod(qd.getDate());
            if(period.intersects(current))
            {
                return true;
            }
        }
        return false;
        
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
        PeriodesNonDisponibles other = (PeriodesNonDisponibles) obj;
        if (quartDates == null)
        {
            if (other.quartDates != null)
                return false;
        }
        else if (!quartDates.equals(other.quartDates))
            return false;
        return true;
    }

}
