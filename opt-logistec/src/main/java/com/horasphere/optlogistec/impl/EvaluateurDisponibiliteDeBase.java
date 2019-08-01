package com.horasphere.optlogistec.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.LocalDate;

import com.horasphere.core.shared.NodeList;
import com.horasphere.core.shared.dynprog2.Bundle;
import com.horasphere.engine.api.DayOfWork;
import com.horasphere.engine.api.Period;
import com.horasphere.engine.lp.condition.MaxConsecutiveHoursCondition;
import com.horasphere.engine.lp.condition.MaxConsecutiveHoursToStartTaskCondition;
import com.horasphere.engine.lp.condition.RestConditionUtils;
import com.horasphere.optlogistec.Employe;
import com.horasphere.optlogistec.EvaluateurDisponibilite;
import com.horasphere.optlogistec.QuartDate;

public class EvaluateurDisponibiliteDeBase implements EvaluateurDisponibilite
{

    private Integer maxNumberOfAssignationsInOneDay = 999999;

    public EvaluateurDisponibiliteDeBase()
    {
        super();

    }

    public EvaluateurDisponibiliteDeBase(Integer maxNumberOfAssignationsInOneDay)
    {
        super();
        this.maxNumberOfAssignationsInOneDay = maxNumberOfAssignationsInOneDay;
    }

    public Boolean estDisponible(QuartDate quartDate, Employe employe)
    {
        Boolean enConge = employe.getQuartNonDisponibles().estNonDisponible(quartDate);
        if (enConge)
        {
            return false;
        }
        Long minDifference = Long.MAX_VALUE;
        Period candidatePeriod = quartDate.generatePeriod();
        int counter = 0;
        for (QuartDate quartDateDejaPlanifie : employe.getQuartDejaPlanifies().getQuartDates())
        {
            LocalDate start = new LocalDate(quartDate.getDate());
            LocalDate startQuartDejaPlanifie = new LocalDate(quartDateDejaPlanifie.getDate());

            if (start.equals(startQuartDejaPlanifie))
            {
                counter++;

            }
        }

        if (counter >= maxNumberOfAssignationsInOneDay)
        {
            return false;
        }
        for (QuartDate quartDateDejaPlanifie : employe.getQuartDejaPlanifies().getQuartDates())
        {
            Period period = quartDateDejaPlanifie.generatePeriod();
            if (period.intersects(candidatePeriod))
            {
                return false;
            }
        }
       
        Period period = quartDate.generatePeriod();
        DayOfWork dayOfWork = new DayOfWork(null, quartDate.getDate(), new ArrayList(), period.getStart(),
                period.getEnd());
        List<DayOfWork> dayOfWorks = new ArrayList();
        dayOfWorks.add(dayOfWork);
        for (QuartDate quartDateDejaPlanifie : employe.getQuartDejaPlanifies().getQuartDates())
        {
            period = quartDateDejaPlanifie.generatePeriod();
            dayOfWorks.add(new DayOfWork(null, quartDateDejaPlanifie.getDate(), new ArrayList(), period.getStart(),
                    period.getEnd()));
        }
        Collections.sort(dayOfWorks, new BeanComparator("start"));
        int index = RestConditionUtils.getDayOfWorkIndexForNewSequence(dayOfWorks, employe.getRestForReset());

        dayOfWorks = new ArrayList(dayOfWorks.subList(index, dayOfWorks.size()));
        Bundle<DayOfWork> bundle = new Bundle(new NodeList(),new ArrayList());
        for(DayOfWork d : dayOfWorks)
        {
            bundle.addElement(d);
        }
        MaxConsecutiveHoursCondition maxConsecutiveHoursCondition = new MaxConsecutiveHoursCondition(
                employe.getRestForReset(), employe.getMaxConsecutiveHours());
        
        if(maxConsecutiveHoursCondition.isViolated(bundle))
        {
            return false;
        }

        MaxConsecutiveHoursToStartTaskCondition maxConsecutiveHoursBeforeStart = new MaxConsecutiveHoursToStartTaskCondition(employe.getRestForReset(),employe.getMaxConsecutiveHoursToStartJob());
        
        if(maxConsecutiveHoursBeforeStart.isViolated(bundle))
        {
            return false;
        }
        return true;
    }

}
