/**
 * Archie
 */
package com.vinplay.service.esport;

import java.io.ObjectInputStream;
import java.util.Optional;

import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.esport.EsportLoginBase64;

/**
 * @author Archie
 *
 */
public interface EsportService {
	ResultFormat transfer(ObjectInputStream objInStream) throws Exception;

	ResultFormat getBalance(String loginname,boolean isCreate) throws Exception;

	ResultFormat login(String loginname) throws Exception;

	Optional<EsportLoginBase64> esportDecode(String token) throws Exception;
}
