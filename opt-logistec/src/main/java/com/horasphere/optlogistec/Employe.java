package com.horasphere.optlogistec;


public class Employe
{
    private String id;
    private Integer seniorite;
    private QuartDateDejaPlanifies quartDejaPlanifies;
    private PeriodesNonDisponibles quartNonDisponibles;
    private Preference<TypeQuart> preferenceQuarts;
    private Preference<CompagnieAffectation> preferenceCompagnieAffectations;
    private Double maxConsecutiveHours = 16.0;
    private Double restForReset = 6.0;
    private Double maxConsecutiveHoursToStartJob = 12.0;
    
    public Employe(String id, Integer seniorite,QuartDateDejaPlanifies quartDejaPlanifies, PeriodesNonDisponibles quartNonDisponibles, Preference<TypeQuart> preferenceQuarts, Preference<CompagnieAffectation> preferenceAffectations)
    {
        super();
        this.id = id;
        this.seniorite = seniorite;
        this.quartDejaPlanifies = quartDejaPlanifies;
        this.quartNonDisponibles = quartNonDisponibles;
        this.preferenceCompagnieAffectations = preferenceAffectations;
        this.preferenceQuarts = preferenceQuarts;
    }

    public Employe(String id, Integer seniorite, QuartDateDejaPlanifies quartDejaPlanifies,
            PeriodesNonDisponibles quartNonDisponibles, Preference<TypeQuart> preferenceQuarts,
            Preference<CompagnieAffectation> preferenceAffectations, Double maxConsecutiveHours, Double restForReset,
            Double maxConsecutiveHoursToStartJob)
    {
        this(id,seniorite,quartDejaPlanifies,quartNonDisponibles,preferenceQuarts, preferenceAffectations);
        this.maxConsecutiveHours = maxConsecutiveHours;
        this.restForReset = restForReset;
        this.maxConsecutiveHoursToStartJob = maxConsecutiveHoursToStartJob;
    }

    public Preference<TypeQuart> getPreferenceQuarts()
    {
        return preferenceQuarts;
    }

    public Preference<CompagnieAffectation> getPreferenceCompagnieAffectations()
    {
        return preferenceCompagnieAffectations;
    }

    public Integer getSeniorite()
    {
        return seniorite;
    }

    public String getId()
    {
        return id;
    }


    public QuartDateDejaPlanifies getQuartDejaPlanifies()
    {
        return quartDejaPlanifies;
    }

    public PeriodesNonDisponibles getQuartNonDisponibles()
    {
        return quartNonDisponibles;
    }

    public Double getMaxConsecutiveHours()
    {
        return maxConsecutiveHours;
    }

    public Double getRestForReset()
    {
        return restForReset;
    }

    public Double getMaxConsecutiveHoursToStartJob()
    {
        return maxConsecutiveHoursToStartJob;
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
        Employe other = (Employe) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Employe [id=" + id + ", seniorite=" + seniorite + "]";
    }

  
    
    
}
