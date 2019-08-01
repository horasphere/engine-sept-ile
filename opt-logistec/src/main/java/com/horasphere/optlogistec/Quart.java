package com.horasphere.optlogistec;

import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import com.horasphere.core.shared.timeline.LocalDateTimeUtils;
import com.horasphere.engine.api.Period;

public class Quart
{
    private String startTime;// HH:mm
    private String endTime;// HH:mm
    private Integer dayDiff;

    public Quart(String startTime, String endTime, Integer dayDiff)
    {
        super();
        this.startTime = startTime;
        if (startTime.length() <= 4)
        {
            this.startTime = "0" + startTime;
        }
        this.endTime = endTime;
        if (endTime.length() <= 4)
        {
            this.endTime = "0" + endTime;
        }
        this.dayDiff = dayDiff;
        Period period = generatePeriod(new Date());
        if (period.getLenghtInHours() <= 0)
        {
            throw new IllegalArgumentException("Quart invalide " + startTime + " " + endTime + " " + dayDiff);
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dayDiff == null) ? 0 : dayDiff.hashCode());
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
        Quart other = (Quart) obj;
        if (dayDiff == null)
        {
            if (other.dayDiff != null)
                return false;
        }
        else if (!dayDiff.equals(other.dayDiff))
            return false;
        if (endTime == null)
        {
            if (other.endTime != null)
                return false;
        }
        else if (!endTime.equals(other.endTime))
            return false;
        if (startTime == null)
        {
            if (other.startTime != null)
                return false;
        }
        else if (!startTime.equals(other.startTime))
            return false;
        return true;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public Integer getDayDiff()
    {
        return dayDiff;
    }

    public Period generatePeriod(Date date)
    {
        LocalDateTime start = LocalDateTimeUtils.create(new LocalDate(date),
                LocalTime.parse(getStartTime(), DateTimeFormat.forPattern("HH:mm")));
        LocalDateTime end = LocalDateTimeUtils.create(new LocalDate(date),
                LocalTime.parse(getEndTime(), DateTimeFormat.forPattern("HH:mm")));
        end = end.plusDays(getDayDiff());
        Period period = new Period(start.toDate(), end.toDate());
        return period;
    }

    @Override
    public String toString()
    {
        return "Quart [startTime=" + startTime + ", endTime=" + endTime + ", dayDiff=" + dayDiff + "]";
    }

}
