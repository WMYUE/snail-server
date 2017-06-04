package com.snail.fitment.common.profile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.snail.fitment.common.config.FitmentConfig;

public class ProfileStatHolder {
	private static final Logger logger=Logger.getLogger(ProfileStatHolder.class);

	static final ConcurrentHashMap<String,ProfileStat> stats=new ConcurrentHashMap<String,ProfileStat>();
	
	private ProfileStatHolder() {
	}
	
	public static void saveOrUpdateCounter(String methodName, long cost) {
		ProfileStat stat=ProfileStatHolder.stats.get(methodName);
		if(stat!=null){
			stat.increase(cost);
		}else{
			stat=new ProfileStat(methodName,cost);
			
		}
		ProfileStatHolder.stats.put(methodName, stat);
	}
	
	public static void printPerformanceLog(String sumKeyword) {
		try{
			StringBuilder header=new StringBuilder();
			header.append(StringUtils.rightPad("method", 60))
			.append(" ").append(StringUtils.rightPad(String.valueOf("percent"),7))
			.append(" ").append(StringUtils.rightPad(String.valueOf("avg (ms)"),10))
			.append(" ").append(StringUtils.rightPad(String.valueOf("max (ms)"),10))
			.append(" ").append(StringUtils.rightPad(String.valueOf("exec times"),10)).append("\n");
			long sum=0L;
			List<ProfileStat> statList=new ArrayList<>(ProfileStatHolder.stats.size());
			for(ProfileStat stat:ProfileStatHolder.stats.values()){
				if(stat.getMethod().indexOf(sumKeyword)>=0){
					sum+=stat.getTotal();
				}
				statList.add(stat);
			}
			Collections.sort(statList);
			Collections.reverse(statList);
			if(CollectionUtils.isEmpty(statList)){
				return;
			}
			StringBuilder performanceLog=new StringBuilder("\n性能计数开始- ").append(header);
			for(ProfileStat stat:statList){
				long avg=stat.getAvg();
				if(avg>FitmentConfig.PERFORMANCE_THRESHOLD){
					performanceLog.append("性能计数　　- ")
					.append(StringUtils.rightPad(stat.getMethod(), 60))
					.append(" ").append(StringUtils.rightPad(getPercent(sum, stat.getTotal()),7))
					.append(" ").append(StringUtils.rightPad(String.valueOf(avg),10))
					.append(" ").append(StringUtils.rightPad(String.valueOf(stat.getMax()),10))
					.append(" ").append(StringUtils.rightPad(String.valueOf(stat.getCount()),10)).append("\n");
				}
			}
			performanceLog.append("性能计数结束- ").append(header);
			logger.info(performanceLog);
			//每次打印时清除原来的计数
			ProfileStatHolder.stats.clear();
		}catch(Throwable t){
			logger.error("性能计数打印失败",t);
		}
	}

	/**
	 * 返回百分比
	 * @param sum
	 * @param total
	 * @return
	 */
	private static String getPercent(long sum, long total) {
		float percent = sum == 0 ? 0 : ((float)total*100/sum);
		return String.format("%2.2f%%", percent);
	}
	
}
