package com.horasphere.optlogistec.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.horasphere.core.shared.Pair;
import com.horasphere.core.shared.io.MyFileUtils;
import com.horasphere.core.shared.opt.linear.glpk.GlpkCFixOptimizer;
import com.horasphere.engine.api.Affectation;
import com.horasphere.engine.api.AffectationType;
import com.horasphere.engine.api.DayOfWork;
import com.horasphere.engine.api.Division;
import com.horasphere.engine.api.DivisionAffectation;
import com.horasphere.engine.api.DivisionAffectationPeriod;
import com.horasphere.engine.api.Employee;
import com.horasphere.engine.api.EmployeeType;
import com.horasphere.engine.api.ExplicitSpecification;
import com.horasphere.engine.api.HorizonRequirement;
import com.horasphere.engine.api.Period;
import com.horasphere.engine.api.Schedule;
import com.horasphere.engine.api.SchedulingInstance;
import com.horasphere.engine.api.Shift;
import com.horasphere.engine.api.ShiftType;
import com.horasphere.engine.api.WorkingBlock;
import com.horasphere.engine.api.localcons.AvailabilityPeriods;
import com.horasphere.engine.api.localcons.LocalConstraint;
import com.horasphere.engine.api.localcons.MaxConsecutiveHours;
import com.horasphere.engine.api.localcons.MaxConsecutiveHoursBeforeStart;
import com.horasphere.engine.api.localcons.MaxNumberOfDayOfWorkOnADate;
import com.horasphere.engine.api.localcons.MustQualified;
import com.horasphere.engine.api.objective.Aggregator;
import com.horasphere.engine.api.objective.DayOfWorkTarget;
import com.horasphere.engine.api.objective.HierarchicalObjective;
import com.horasphere.engine.api.objective.MaximizeEmployeeAffectationSatisfaction;
import com.horasphere.engine.api.objective.MaximizeEmployeeDayOfWorkSatisfaction;
import com.horasphere.engine.api.objective.MaximizeEmployeeNShiftSatisfaction;
import com.horasphere.engine.api.objective.MaximizeEmployeeShiftSatisfaction;
import com.horasphere.engine.api.objective.MaximizeHourForEmployee;
import com.horasphere.engine.api.objective.MinimizeStaffingDeviation;
import com.horasphere.engine.api.objective.MinimizeStaffingDeviationByDivisionAffectation;
import com.horasphere.engine.api.objective.Objective;
import com.horasphere.engine.api.policy.NoOverstaff;
import com.horasphere.engine.api.solver.ScheduleGenerationEngine;
import com.horasphere.engine.lp.schedule.SequentialScheduleGenerationEngine;
import com.horasphere.engine.lp.schedule.SimpleScheduleGenerationEngine;
import com.horasphere.engine.lp.schedule.aggregation.BasicAggregatedSchedulingOptimizer;
import com.horasphere.engine.lp.schedule.aggregation.SimpleConverterAggregatedSchedulingInstance;
import com.horasphere.engine.lp.schedule.constraint.ScheduleObjectiveEvaluator;
import com.horasphere.engine.lp.schedule.generation.GenerateAllSchedulesWithCache;
import com.horasphere.optlogistec.AssignationBesoinEmploye;
import com.horasphere.optlogistec.Besoin;
import com.horasphere.optlogistec.CompagnieAffectation;
import com.horasphere.optlogistec.Employe;
import com.horasphere.optlogistec.Optimiseur;
import com.horasphere.optlogistec.Preference;
import com.horasphere.optlogistec.Quart;
import com.horasphere.optlogistec.QuartDate;
import com.horasphere.optlogistec.TypeQuart;
import com.thoughtworks.xstream.XStream;

public class OptimiseurSequentiel implements Optimiseur
{

    private static Logger logger = LoggerFactory.getLogger(OptimiseurSequentiel.class);
    private boolean useVirtualBesoins = false;
    private Integer maxNumberOfAffectations = 999;

    public OptimiseurSequentiel()
    {
        super();
    }

    public OptimiseurSequentiel(boolean useVirtualBesoins, Integer maxNumberOfAffectations)
    {
        super();
        this.useVirtualBesoins = useVirtualBesoins;
        if (false && logger.isWarnEnabled())
        {
            logger.warn("Override virtual besoins flag ");
            this.useVirtualBesoins = false;
        }
        this.maxNumberOfAffectations = maxNumberOfAffectations;

    }

    public boolean classificationIsTheHighest(Preference<CompagnieAffectation> preference,
            CompagnieAffectation compagnieAffectation)
    {
        Integer appreciation = preference.getAppreciation(compagnieAffectation);
        for (CompagnieAffectation o : preference.getItems())
        {
            if (!o.equals(compagnieAffectation) && appreciation <= preference.getAppreciation(o))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isAGoodShift(Preference<TypeQuart> preference, Shift shift)
    {
        TypeQuart typeQuart = TypeQuart.findTypeQuart(shift);
        Integer value = preference.getAppreciation(typeQuart);
        for (TypeQuart t : preference.getItems())
        {
            Integer v = preference.getAppreciation(t);
            if (v > value)
            {
                return false;
            }
        }
        return true;

    }

    public List<AssignationBesoinEmploye> optimise(List<Besoin> besoins, List<Employe> employes,
            List<String> prioriteAffectations)
    {

        if (true)
        {
            String directory = System.getProperty("java.io.tmpdir");

            XStream xtream = new XStream();
            try
            {
                xtream.toXML(besoins, new FileWriter(new File(directory, "besoins.xml")));
                xtream.toXML(employes, new FileWriter(new File(directory, "employes.xml")));
                xtream.toXML(prioriteAffectations, new FileWriter(new File(directory, "affectations.xml")));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        Map<DayOfWork, List<Besoin>> dayOfWorkToBesoinMapping = findDayOfWorks(besoins, "NORMAL");
        Map<Besoin, DayOfWork> besoinToDayOfWorkMapping = new HashMap();
        List<DayOfWork> dayOfWorks = new ArrayList();

        for (Map.Entry<DayOfWork, List<Besoin>> entry : dayOfWorkToBesoinMapping.entrySet())
        {
            for (Besoin besoin : entry.getValue())
            {
                dayOfWorks.add(entry.getKey());
                besoinToDayOfWorkMapping.put(besoin, entry.getKey());
            }
        }

        Set<QuartDate> quartDates = new LinkedHashSet();
        for (Besoin besoin : besoins)
        {
            quartDates.add(besoin.getQuartDate());
        }
        List<DayOfWork> virtualDayOfWorks = new ArrayList();
        Collection<String> companies = new HashSet();
        if (useVirtualBesoins)
        {
            List<Besoin> besoinsVirtuels = findBesoinsVirtuels(besoins);

            for (Besoin besoin : besoinsVirtuels)
            {
                quartDates.add(besoin.getQuartDate());
                companies.add(besoin.getCompagnieAffectation().getCompagnie());
            }
            Map<DayOfWork, List<Besoin>> dayOfWorkToVirtualBesoinMapping = findDayOfWorks(besoinsVirtuels, "VIRTUAL");
            dayOfWorkToBesoinMapping.putAll(dayOfWorkToVirtualBesoinMapping);
            for (Map.Entry<DayOfWork, List<Besoin>> entry : dayOfWorkToVirtualBesoinMapping.entrySet())
            {
                for (Besoin besoin : entry.getValue())
                {
                    virtualDayOfWorks.add(entry.getKey());
                }
            }

            dayOfWorks.addAll(virtualDayOfWorks);

        }

        List<Employee> employees = convertEmploye(employes, new ArrayList(quartDates));

        List<Employe> employeeSortedBySeniority = new ArrayList(employes);

        Collections.sort(employeeSortedBySeniority, new BeanComparator("seniorite"));
        HierarchicalObjective hierarchicalObjective = new HierarchicalObjective();

        Map<DayOfWork, Integer> mapDayOfWorkPreferences = new HashMap();
        com.horasphere.engine.api.Preference<DayOfWork> preferenceRotation = new com.horasphere.engine.api.Preference(
                mapDayOfWorkPreferences);

        List<DayOfWork> dayOfWorkTerminal = new ArrayList();
        for (DayOfWork dayOfWork : dayOfWorks)
        {
            if (dayOfWork.getShiftType().getCode().contains("__ROTATION"))
            {
                mapDayOfWorkPreferences.put(dayOfWork, 1);
            }

            if (!dayOfWork.getShiftType().getCode().contains("__NAVIRE"))
            {
                dayOfWorkTerminal.add(dayOfWork);
            }
        }

        mapDayOfWorkPreferences = new HashMap();
        com.horasphere.engine.api.Preference<DayOfWork> preferenceRelaisNordik = new com.horasphere.engine.api.Preference(
                mapDayOfWorkPreferences);

        for (DayOfWork dayOfWork : dayOfWorks)
        {
            if (dayOfWork.getShiftType().getCode().contains("COMPAGNIE_RN"))
            {
                mapDayOfWorkPreferences.put(dayOfWork, 1);
            }
            else
            {
                mapDayOfWorkPreferences.put(dayOfWork, 2);
            }
        }

        mapDayOfWorkPreferences = new HashMap();
        com.horasphere.engine.api.Preference<DayOfWork> preferenceNonVirtual = new com.horasphere.engine.api.Preference(
                mapDayOfWorkPreferences);

        for (DayOfWork dayOfWork : dayOfWorks)
        {
            if (!dayOfWork.getShiftType().getCode().contains("VIRTUAL"))
            {
                mapDayOfWorkPreferences.put(dayOfWork, 1);
            }

        }

        LinkedHashSet<String> set = new LinkedHashSet();
        for (String affectation : prioriteAffectations)
        {

            set.add(affectation);

        }

        LinkedHashSet<CompagnieAffectation> compagnieAffectations = new LinkedHashSet();
        for (Besoin besoin : besoins)
        {
            compagnieAffectations.add(besoin.getCompagnieAffectation());
        }
        List<DayOfWork> virtualDayOfWorksAndTerminalDayOfWorks = new ArrayList(virtualDayOfWorks);
        virtualDayOfWorksAndTerminalDayOfWorks.addAll(dayOfWorkTerminal);
        for (String realAffectation : set)
        {
            List<DivisionAffectation> validAffectations = new ArrayList();
            for (CompagnieAffectation compagnieAffectation : compagnieAffectations)
            {
                if (!realAffectation.contains("_"))
                {
                    if (compagnieAffectation.getAffectation().equals(realAffectation))
                    {
                        validAffectations.add(convert(compagnieAffectation));
                    }
                }
                else {
                    String[] tokens = realAffectation.split("_");
                    
                    if (compagnieAffectation.getAffectation().equals(tokens[1]) && compagnieAffectation.getCompagnie().equals(tokens[0]))
                    {
                        validAffectations.add(convert(compagnieAffectation));
                    }
                }
            }

            hierarchicalObjective.addObjective(new MinimizeStaffingDeviationByDivisionAffectation(1.0, 1.0, 1000000.0,
                    validAffectations, virtualDayOfWorksAndTerminalDayOfWorks, new ArrayList()), new ArrayList());
            hierarchicalObjective.addObjective(new MinimizeStaffingDeviationByDivisionAffectation(1.0, 1.0, 1000000.0,
                    validAffectations, virtualDayOfWorks, new ArrayList()), new ArrayList());
        }
        hierarchicalObjective.addObjective(new MinimizeStaffingDeviation(1.0, 1.0, 1000000.0, virtualDayOfWorks,
                new ArrayList()), new ArrayList());

        List<String> employeeIdToDisaggregate = new ArrayList();

        List<Shift> allShifts = new ArrayList();
        for (DayOfWork dayOfWork : dayOfWorks)
        {
            allShifts.add(Shift.createFromDayOfWork(dayOfWork));
        }
        for (Employe employe : employeeSortedBySeniority)
        {
            employeeIdToDisaggregate.add(employe.getId());
            hierarchicalObjective.addObjective(new MaximizeEmployeeShiftSatisfaction(employe.getId(),
                    convertNonPreferenceQuart(employe.getPreferenceQuarts(), allShifts), false),
                    employeeIdToDisaggregate);

            if (!classificationIsTheHighest(employe.getPreferenceCompagnieAffectations(), new CompagnieAffectation(
                    "RN", "BJOUR")))
            {
                hierarchicalObjective.addObjective(new MaximizeEmployeeDayOfWorkSatisfaction(employe.getId(),
                        preferenceRelaisNordik, false, Aggregator.Min), employeeIdToDisaggregate);

            }
            hierarchicalObjective.addObjective(new MaximizeEmployeeNShiftSatisfaction(employe.getId(),
                    convertPreferenceQuart(employe.getPreferenceQuarts(), allShifts), 0, false),
                    employeeIdToDisaggregate);

            hierarchicalObjective.addObjective(new MaximizeHourForEmployee(employe.getId()), employeeIdToDisaggregate);
            
            hierarchicalObjective.addObjective(new MaximizeEmployeeAffectationSatisfaction(employe.getId(),
                    convertPreferenceAffectation(employe.getPreferenceCompagnieAffectations(), prioriteAffectations),
                    false, new HashSet(virtualDayOfWorks)), employeeIdToDisaggregate);

            hierarchicalObjective.addObjective(new MaximizeEmployeeDayOfWorkSatisfaction(employe.getId(),
                    preferenceNonVirtual, false, Aggregator.Max), employeeIdToDisaggregate);

            hierarchicalObjective.addObjective(new MaximizeEmployeeDayOfWorkSatisfaction(employe.getId(),
                    preferenceRotation, false, Aggregator.Min), employeeIdToDisaggregate);

     
            hierarchicalObjective.addObjective(new MaximizeEmployeeNShiftSatisfaction(employe.getId(),
                    convertPreferenceQuart(employe.getPreferenceQuarts(), allShifts), 1, false),
                    employeeIdToDisaggregate);

        }

        Map<String, List<Pair<String, Integer>>> besoinsNestes = getBesoinsNestes(besoins);
        for (Map.Entry<String, List<Pair<String, Integer>>> entry : besoinsNestes.entrySet())
        {
            for (Pair<String, Integer> pair : entry.getValue())
            {
                hierarchicalObjective.addObjective(
                        new DayOfWorkTarget(pair.getSecond(), getDayOfWorkCompatibleWithCompanyAndAffectation(
                                besoinToDayOfWorkMapping, pair.getFirst(), entry.getKey())), employeeIdToDisaggregate);
            }
        }
        SchedulingInstance schedulingInstance = new SchedulingInstance(employees,
                new HorizonRequirement(new HashMap()), hierarchicalObjective, new NoOverstaff(), new ArrayList(),
                new HashMap());

        ScheduleGenerationEngine scheduleGenerationEngine = new SequentialScheduleGenerationEngine(
                new SimpleScheduleGenerationEngine(new BasicAggregatedSchedulingOptimizer(new GlpkCFixOptimizer(),
                        new GenerateAllSchedulesWithCache(), false), new SimpleConverterAggregatedSchedulingInstance()));

        List<Schedule> schedules = scheduleGenerationEngine.optimize(schedulingInstance, dayOfWorks);

        if (logger.isInfoEnabled())
        {
            for (Objective objective : hierarchicalObjective.getObjectives())
            {
                logger.info("OBJECTIVE VALUE : " + objective + " "
                        + new ScheduleObjectiveEvaluator().evaluate(objective, schedules, dayOfWorks));
            }
        }
        List<String> lines = new ArrayList();
        String directory = System.getProperty("java.io.tmpdir");
        for (Objective objective : hierarchicalObjective.getObjectives())
        {
            lines.add("OBJECTIVE VALUE : " + objective + " "
                    + new ScheduleObjectiveEvaluator().evaluate(objective, schedules, dayOfWorks));
        }

        Set<Besoin> visited = new HashSet();
        List<AssignationBesoinEmploye> result = new ArrayList();
        for (Schedule schedule : schedules)
        {
            for (DayOfWork dayOfWork : schedule.getDaysOfWork())
            {

                List<Besoin> list = dayOfWorkToBesoinMapping.get(dayOfWork);
                for (Besoin besoin : list)
                {
                    // Just return real besoin
                    if (!visited.contains(besoin) && besoins.contains(besoin))
                    {
                        result.add(new AssignationBesoinEmploye(besoin.getId(), schedule.getEmployeeId()));
                        visited.add(besoin);
                        break;
                    }
                    else
                    {
                        if (!visited.contains(besoin))
                        {
                            lines.add(besoin.getQuartDate().getDate() + " " + besoin.getId() + " "
                                    + besoin.getIdCommande() + " " + schedule.getEmployeeId() + " "
                                    + dayOfWork.getShiftType().getCode());
                            visited.add(besoin);
                            break;
                        }
                    }
                }
            }

        }

        for (AssignationBesoinEmploye assignationBesoinEmploye : result)
        {
            lines.add(assignationBesoinEmploye.getIdEmploye() + " " + assignationBesoinEmploye.getIdBesoin());
            for (Employe employe : employes)
            {
                if (employe.getId().equals(assignationBesoinEmploye.getIdEmploye()))
                {
                }
            }

        }

        MyFileUtils.writeLines(new File(directory, "result"), lines);

        return result;
    }

    public Map<DayOfWork, List<Besoin>> findDayOfWorks(List<Besoin> besoins, String type)
    {
        Map<DayOfWork, List<Besoin>> mapping = new LinkedHashMap();
        for (Besoin besoin : besoins)
        {

            ShiftType shiftType = null;
            String suffix = "";
            if (besoin.getRotation())
            {
                suffix += "__ROTATION";
            }
            if (besoin.getPourUnNavire())
            {
                suffix += "__NAVIRE";
            }
            suffix += "__COMPAGNIE_" + besoin.getCompagnieAffectation().getCompagnie();
            shiftType = new ShiftType(new ArrayList(), type + suffix);

            if (besoin.getEstDejaCouvert())
            {
                continue;
            }

            DivisionAffectation divisionAffectation = convert(besoin.getCompagnieAffectation());
            List<WorkingBlock> workingBlocks = new ArrayList();

            Period period = besoin.getQuartDate().generatePeriod();

            workingBlocks.add(new WorkingBlock(new ArrayList(Arrays.asList(new DivisionAffectationPeriod(
                    divisionAffectation, period)))));

            DayOfWork dayOfWork = new DayOfWork(shiftType, besoin.getQuartDate().getDate(), workingBlocks,
                    period.getStart(), period.getEnd());

            List<Besoin> list = mapping.get(dayOfWork);
            if (list == null)
            {
                list = new ArrayList();
                mapping.put(dayOfWork, list);
            }
            list.add(besoin);
        }
        return mapping;
    }

    public List<Besoin> findBesoinsVirtuels(List<Besoin> besoins)
    {
        List<Besoin> result = new ArrayList();
        for (Besoin besoin : besoins)
        {

            if (!besoin.getRotation())
            {
                continue;
            }
            QuartDate previousQuartDate = besoin.getQuartDate();
            Period previousPeriod = previousQuartDate.generatePeriod();

            LocalDateTime newEnd = new LocalDateTime(previousPeriod.getEnd());

            if (newEnd.toLocalTime().equals(new LocalTime(0, 0)))
            {
                Long minutes = 12L * 60;
                newEnd = newEnd.plusMinutes(minutes.intValue());
                Period period = new Period(previousPeriod.getEnd(), newEnd.toDate());

                int nbOfDays = Days.daysBetween(new LocalDateTime(period.getStart()).toLocalDate(),
                        new LocalDateTime(period.getEnd()).toLocalDate()).getDays();
                QuartDate quartDate = new QuartDate(new Quart(new LocalTime(period.getStart()).toString("HH:mm"),
                        new LocalTime(period.getEnd()).toString("HH:mm"), nbOfDays),
                        new LocalDate(period.getStart()).toDate());
                result.add(new Besoin(besoin.getCompagnieAffectation(), quartDate, "virtual_" + besoin.getId(), besoin
                        .getIdCommande(), false, besoin.getRotation()));
            }

        }

        return result;
    }

    public com.horasphere.engine.api.Preference convertPreferenceAffectation(
            Preference<CompagnieAffectation> preference, List<String> affectations)
    {
        Map<DivisionAffectation, Integer> scores = new HashMap();
        com.horasphere.engine.api.Preference enginePreference = new com.horasphere.engine.api.Preference(scores);
        Set<String> compagnies = new LinkedHashSet();
        for (CompagnieAffectation compagnieAffectation : preference.getItems())
        {
            compagnies.add(compagnieAffectation.getCompagnie());
        }
        for (CompagnieAffectation compagnieAffectation : preference.getItems())
        {
            Integer rank = affectations.indexOf(compagnieAffectation.getCompagnie() + "_"
                    + compagnieAffectation.getAffectation());

            if (rank < 0)
            {
                rank = affectations.indexOf(compagnieAffectation.getAffectation());
            }
            if (rank < 0)
            {
                affectations.size();
            }
            scores.put(convert(compagnieAffectation), preference.getAppreciation(compagnieAffectation) * (10000)
                    + affectations.size() - rank);
        }
        return enginePreference;

    }

    public com.horasphere.engine.api.Preference convertPreferenceQuart(Preference<TypeQuart> preference,
            List<Shift> shifts)
    {
        Map<Shift, Integer> scores = new HashMap();
        com.horasphere.engine.api.Preference enginePreference = new com.horasphere.engine.api.Preference(scores);
        for (Shift shift : shifts)
        {
            TypeQuart typeQuart = TypeQuart.findTypeQuart(shift);

            Integer appreciation = preference.getAppreciation(typeQuart);

            int prime = 0;
            if (shift.getShiftTypeCode().contains("ROTATION"))
            {
                prime = 5;
            }

            if (shift.getShiftTypeCode().contains("VIRTUAL"))
            {
                scores.put(shift, (90 * appreciation) + prime);
            }
            else
            {
                scores.put(shift, (100 * appreciation) + prime);
            }
        }

        return enginePreference;

    }

    public com.horasphere.engine.api.Preference convertNonPreferenceQuart(Preference<TypeQuart> preference,
            List<Shift> shifts)
    {
        Map<Shift, Integer> scores = new HashMap();
        com.horasphere.engine.api.Preference enginePreference = new com.horasphere.engine.api.Preference(scores);
        for (Shift shift : shifts)
        {
            TypeQuart typeQuart = TypeQuart.findTypeQuart(shift);

            Integer pref = preference.getAppreciation(typeQuart);
            if (pref == 1)
            {

                scores.put(shift, -9);
            }
            else
            {
                scores.put(shift, 0);
            }

        }

        return enginePreference;

    }

    public List<Employee> convertEmploye(List<Employe> employes, List<QuartDate> quartDates)
    {
        ShiftType normalShift = new ShiftType(new ArrayList(), "NORMAL");
        List<Employee> result = new ArrayList();

        Map<List<LocalConstraint>, EmployeeType> employeeTypes = new HashMap();
        for (Employe employe : employes)
        {
            List<LocalConstraint> scheduleConstraints = new ArrayList();
            scheduleConstraints.add(convert(employe, quartDates));
            List<DivisionAffectation> divisionAffectations = new ArrayList();
            for (CompagnieAffectation compagnieAffectation : employe.getPreferenceCompagnieAffectations().getItems())
            {

                divisionAffectations.add(convert(compagnieAffectation));

            }

            scheduleConstraints.add(new MustQualified(new ExplicitSpecification(divisionAffectations, true)));
            scheduleConstraints.add(new MaxConsecutiveHoursBeforeStart(12.0, 6.0));
            scheduleConstraints.add(new MaxConsecutiveHours(16.0, 6.0));
            scheduleConstraints.add(new MaxNumberOfDayOfWorkOnADate(maxNumberOfAffectations));
            EmployeeType employeeType = employeeTypes.get(scheduleConstraints);
            if (employeeType == null)
            {
                employeeType = new EmployeeType("__likeEmployee" + employe.getId(), normalShift, scheduleConstraints);
                employeeTypes.put(scheduleConstraints, employeeType);
            }

            Employee employee = new Employee(employe.getId(), employeeType);
            result.add(employee);
        }
        if (logger.isInfoEnabled())
        {
            logger.info("There is " + employeeTypes.size() + " distinct employee types");
        }
        return result;

    }

    public AvailabilityPeriods convert(Employe employe, List<QuartDate> quartDates)
    {
        List<Period> periods = new ArrayList();
        EvaluateurDisponibiliteDeBase evaluateurDisponibilteDeBase = new EvaluateurDisponibiliteDeBase(
                maxNumberOfAffectations);
        for (QuartDate quartDate : quartDates)
        {
            if (evaluateurDisponibilteDeBase.estDisponible(quartDate, employe))
            {
                Period period = quartDate.generatePeriod();
                periods.add(period);
            }

        }

        return new AvailabilityPeriods(periods);

    }

    public DivisionAffectation convert(CompagnieAffectation compagnieAffectation)
    {
        Division division = new Division(compagnieAffectation.getCompagnie());

        return new DivisionAffectation(new Affectation(compagnieAffectation.getAffectation()), division,
                AffectationType.WORK);
    }

    public Map<String, Integer> getBesoinsByCompagnie(String realAffectation, List<Besoin> besoins)
    {
        Map<String, Integer> map = new HashMap();
        for (Besoin besoin : besoins)
        {
            String companie = besoin.getCompagnieAffectation().getCompagnie();
            String ra = besoin.getCompagnieAffectation().getAffectation();
            if (ra.equals(realAffectation))
            {
                Integer count = map.get(companie);
                if (count == null)
                {
                    count = 0;
                }
                count++;
                map.put(companie, count);
            }
        }

        return map;

    }

    public Map<String, List<Pair<String, Integer>>> getBesoinsNestes(List<Besoin> besoins)
    {

        Map<String, List<Pair<String, Integer>>> map = new HashMap();

        LinkedHashSet<String> allRealAffectations = new LinkedHashSet();
        for (Besoin besoin : besoins)
        {
            allRealAffectations.add(besoin.getCompagnieAffectation().getAffectation());
        }

        for (String realAffectation : allRealAffectations)
        {
            Map<String, Integer> besoinsParCompagnie = getBesoinsByCompagnie(realAffectation, besoins);
            int quantity = 1;
            List<Pair<String, Integer>> besoinsNestes = new ArrayList();
            while (true)
            {
                boolean ok = false;
                for (Map.Entry<String, Integer> entry : besoinsParCompagnie.entrySet())
                {
                    if (entry.getValue() >= quantity)
                    {
                        besoinsNestes.add(new Pair<String, Integer>(entry.getKey(), quantity));
                        ok = true;
                    }

                }
                if (!ok)
                {
                    break;
                }
                quantity++;

            }
            map.put(realAffectation, besoinsNestes);

        }
        return map;
    }

    public List<DayOfWork> getDayOfWorkCompatibleWithCompanyAndAffectation(Map<Besoin, DayOfWork> mapping,
            String company, String realAffectation)
    {
        List<DayOfWork> dayOfWorks = new ArrayList();
        for (Map.Entry<Besoin, DayOfWork> entry : mapping.entrySet())
        {
            if (entry.getKey().getCompagnieAffectation().getAffectation().equals(realAffectation)
                    && entry.getKey().getCompagnieAffectation().getCompagnie().equals(company))
            {
                dayOfWorks.add(entry.getValue());
            }
        }
        return dayOfWorks;
    }
}
