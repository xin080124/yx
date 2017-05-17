package model;
public class Count implements Comparable<Count> {
    public Long count;
    public String id;

    public Count(Long count, String id) {
        this.count = count;
        this.id = id;
    }

    @Override
    public int compareTo(Count o) {
        return Long.compare(count, o.count) < 0 ? 1 : Long.compare(count, o.count) > 0 ? -1 : 0;
        //return score < o.score ? -1 : score > o.score ? 1 : 0;
    }
}

public List<Count> getMostPopularVenue(int number, String fromTime, String toTime) {
        return getMostPopular("venuecheckin:*", number, fromTime, toTime);
    }

private List<Count> getMostPopular(String key, int number, String fromTime, String toTime) {
        
        List<Count> count = new ArrayList<Count>();
        List<Count> countReturn = new ArrayList<Count>();
        
        if(number < 1){
            number = 1;
        }
        if(fromTime.isEmpty())
            fromTime = "-inf";
        if(toTime.isEmpty())
            toTime = "+inf";
        
        Jedis jedis = pool.getResource();
        
        try {
            

            Set<String> dataset = jedis.keys(key);
            
            for (String id : dataset) {
                count.add(new Count(jedis.zcount(id, fromTime, toTime), id.split(":")[1]));
            }
            
            Collections.sort(count);
            
            countReturn = count.subList(0, number > count.size() ? count.size() : number);
            
        } catch (JedisException e) {
            //if something wrong happen, return it back to the pool
            if (null != jedis) {
                jedis = null;
            }
        }       
        return countReturn;
    }