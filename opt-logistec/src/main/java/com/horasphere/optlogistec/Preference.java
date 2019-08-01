package com.horasphere.optlogistec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preference<T>
{
    public Map<T, Integer> map = new HashMap();
    
   
    public Preference()
    {
        super();
    }


    public Preference(Map<T, Integer> map)
    {
        super();
        this.map = map;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((map == null) ? 0 : map.hashCode());
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
        Preference other = (Preference) obj;
        if (map == null)
        {
            if (other.map != null)
                return false;
        }
        else if (!map.equals(other.map))
            return false;
        return true;
    }


    public List<T> getItems()
    {
        return new ArrayList(map.keySet());        
    }

    public Integer getAppreciation(T object)
    {
      Integer  score = map.get(object);
        if(score == null)
        {
            return 0;
        }
        return score;
    }


    @Override
    public String toString()
    {
        return "Preference [map=" + map + "]";
    }

}
