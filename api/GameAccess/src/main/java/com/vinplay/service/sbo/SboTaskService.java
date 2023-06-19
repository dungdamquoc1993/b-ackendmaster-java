/**
 * Archie
 */
package com.vinplay.service.sbo;

import java.util.List;

import com.vinplay.dto.sbo.SboRecordDetail;

/**
 * @author Archie
 *
 */
public interface SboTaskService {
	public boolean createUser(String sboId) throws Exception;

	public List<SboRecordDetail> getBetLogDetail(String startDate, String endDate) throws Exception;
}
