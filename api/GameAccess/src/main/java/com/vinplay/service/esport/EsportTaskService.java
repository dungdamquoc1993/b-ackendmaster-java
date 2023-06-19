/**
 * Archie
 */
package com.vinplay.service.esport;

import com.vinplay.dto.esport.EsportRecord;
import com.vinplay.dto.esport.EsportRespose;

/**
 * @author Archie
 *
 */
public interface EsportTaskService {
	public boolean isCreateUser(String esportId) throws Exception;

	public EsportRespose<EsportRecord> getBetLog(Integer page, Integer pagesize, String fromDate, String toDate)
			throws Exception;
}
