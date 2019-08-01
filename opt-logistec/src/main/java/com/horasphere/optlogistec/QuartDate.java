package com.horasphere.optlogistec;

import java.util.Date;

import com.horasphere.engine.api.Period;

public class QuartDate
{
    private Quart quart;
    private Date date;

    public QuartDate(Quart quart, Date date)
    {
        super();
        this.quart = quart;
        this.date = date;
    }

    public Quart getQuart()
    {
        return quart;
    }

    public Date getDate()
    {
        return date;
    }
    
    public Period generatePeriod()
    {
        return quart.generatePeriod(date);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((quart == null) ? 0 : quart.hashCode());
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
        QuartDate other = (QuartDate) obj;
        if (date == null)
        {
            if (other.date != null)
                return false;
        }
        else if (!date.equals(other.date))
            return false;
        if (quart == null)
        {
            if (other.quart != null)
                return false;
        }
        else if (!quart.equals(other.quart))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "QuartDate [quart=" + quart + ", date=" + date + "]";
    }

}
