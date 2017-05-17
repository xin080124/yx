package model;
public class Count implements Comparable<Count> {
    public Long count;
    public String id;

    public Count(Long count, String id) {
        this.count = count;
        this.id = id;
    }

    @Override
    public int compareTo(Count另一个对象) {
        return Long.compare(count,另一个对象.count) < 0 ? 1 : Long.compare(count,另一个对象.count) > 0 ? -1 : 0;
        //return score <另一个对象.score ? -1 : score >另一个对象.score ? 1 : 0;
    }
}

public List<Count> getMostPopularVenue(int number, String fromTime, String toTime) {
		return getMostPopular("venuecheckin:*", number, fromTime, toTime);
	}

private List<Count> getMostPopular(String "venuecheckin:*", int number, String fromTime, String toTime) {
		
		List<Count> countObjList = new ArrayList<Count>();
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
			

			Set<String> dataset = jedis.keys("venuecheckin:*");
			
			for (String checkinID : dataset) {
				countObjList.add(new Count(jedis.zcount(checkinID, fromTime, toTime), 	eckinID.split(":")[1]));
			}
			
			Collections.sort(countObjList);
			
			countReturn = countObjList.subList(0, number > countObjList.size() ? countObjList.size() : number);
			
		} catch (JedisException e) {
			//if something wrong happen, return it back to the pool
			if (null != jedis) {
				jedis = null;
			}
		}		
		return countReturn;
	}