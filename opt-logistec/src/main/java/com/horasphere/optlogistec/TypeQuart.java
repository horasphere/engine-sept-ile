package com.horasphere.optlogistec;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import com.horasphere.core.shared.timeline.LocalDateTimeUtils;
import com.horasphere.engine.api.Shift;

public enum TypeQuart {

    Nuit, Jour, Autre;

    public static boolean estUnQuartDeNuit(Shift shift)
    {
        Quart quart = new Quart(shift.getStart(), shift.getEnd(), shift.getDayDiff());
        LocalDateTime startTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getStartTime(), DateTimeFormat.forPattern("HH:mm")), 0);
        LocalDateTime endTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getEndTime(), DateTimeFormat.forPattern("HH:mm")), shift.getDayDiff());
        LocalDateTime quartDeNuitStartTime = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(0, 0), 0);
        LocalDateTime quartDeNuitExceptionStartTime = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(8, 0), 0);
        LocalDateTime quartDeNuitEndTime = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(12, 0), 0);
        if (startTime.compareTo(quartDeNuitStartTime) >= 0 && endTime.compareTo(quartDeNuitEndTime) <= 0)
        {
            if (startTime.equals(quartDeNuitExceptionStartTime) && !shift.getShiftTypeCode().contains("ROTATION"))
            {
                return false;
            }
            return true;
        }

        return false;
    }

    public static boolean estUnQuartDeJour(Shift shift)
    {
        Quart quart = new Quart(shift.getStart(), shift.getEnd(), shift.getDayDiff());
        LocalDateTime startTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getStartTime(), DateTimeFormat.forPattern("HH:mm")), 0);
        LocalDateTime endTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getEndTime(), DateTimeFormat.forPattern("HH:mm")), shift.getDayDiff());

        LocalDateTime quartDeJourStartTime = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(12, 0), 0);
        LocalDateTime quartDeJourEndTime = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(0, 0), 1);
        LocalDateTime quartDeNuitExceptionStartTime = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(8, 0), 0);
        if (startTime.compareTo(quartDeJourStartTime) >= 0 && endTime.compareTo(quartDeJourEndTime) <= 0)
        {
            return true;
        }
        if (startTime.equals(quartDeNuitExceptionStartTime) && !shift.getShiftTypeCode().contains("ROTATION"))
        {
            return true;
        }
        return false;

    }

    public static boolean estUnQuartDeNuitLogistecFer(Shift shift)
    {
        Quart quart = new Quart(shift.getStart(), shift.getEnd(), shift.getDayDiff());
        LocalDateTime startTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getStartTime(), DateTimeFormat.forPattern("HH:mm")), 0);
           LocalDateTime quartDeNuitStartLB = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(16, 0), 0);
        LocalDateTime quartDeNuitStartUB = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(0, 0), 1);
        if (startTime.compareTo(quartDeNuitStartLB) >= 0 && startTime.compareTo(quartDeNuitStartUB) < 0)
        {

            return true;
        }

        quartDeNuitStartLB = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(0, 0), 0);
        quartDeNuitStartUB = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(4, 0), 0);
        if (startTime.compareTo(quartDeNuitStartLB) >= 0 && startTime.compareTo(quartDeNuitStartUB) < 0)
        {

            return true;
        }

        return false;
    }

    public static boolean estUnQuartDeJourLogistecFer(Shift shift)
    {
        Quart quart = new Quart(shift.getStart(), shift.getEnd(), shift.getDayDiff());
        LocalDateTime startTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getStartTime(), DateTimeFormat.forPattern("HH:mm")), 0);
        LocalDateTime endTime = LocalDateTimeUtils.createVirtualDateTime(
                LocalTime.parse(quart.getEndTime(), DateTimeFormat.forPattern("HH:mm")), shift.getDayDiff());

        LocalDateTime quartDeJourStartLB = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(4, 0), 0);
        LocalDateTime quartDeJourStartUB = LocalDateTimeUtils.createVirtualDateTime(new LocalTime(16, 0), 1);

        if (startTime.compareTo(quartDeJourStartLB) >= 0 && startTime.compareTo(quartDeJourStartUB) < 0)
        {
            return true;
        }

        return false;

    }

    public static TypeQuart findTypeQuart(Shift shift)
    {
        if (shift.getShiftTypeCode().contains("COMPAGNIE_LF"))
        {
            if (estUnQuartDeNuitLogistecFer(shift))
            {
                return Nuit;
            }
            if (estUnQuartDeJourLogistecFer(shift))
            {
                return Jour;
            }
        }
        else
        {
            if (estUnQuartDeNuit(shift))
            {
                return Nuit;
            }
            if (estUnQuartDeJour(shift))
            {
                return Jour;
            }
        }
        return Autre;
    }

}
