/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.usercore.dao.impl.SecurityDaoImpl
 *  com.vinplay.usercore.service.impl.UserServiceImpl
 *  com.vinplay.usercore.utils.GameCommon
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.models.cache.UserCacheModel
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
package com.vinplay.api.backend.processors.login;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.api.backend.report.utils.BackendUtils;
import com.vinplay.usercore.dao.impl.SecurityDaoImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ResetPasswordNoOtpProcessor
        implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String) "backend");

    public String execute(Param<HttpServletRequest> param) {
        String res;
        block14:
        {
            res = "1001";
            HttpServletRequest request = (HttpServletRequest) param.get();
            String nickname = request.getParameter("nn");
            if (nickname != null && !nickname.isEmpty()) {
                try {
                    UserServiceImpl userService = new UserServiceImpl();
                    UserModel userModel = userService.getUserByNickName(nickname);
                    if (userModel != null) {
                        String password = "12345678";
                        SecurityDaoImpl securityDao = new SecurityDaoImpl();
                        if (!securityDao.updateUserInfo(userModel.getId(), VinPlayUtils.getMD5Hash((String) password), 2))
                            break block14;
                        HazelcastInstance client = HazelcastClientFactory.getInstance();
                        IMap userMap = client.getMap("users");
                        if (userMap.containsKey((Object) userModel.getNickname())) {
                            block15:
                            {
                                try {
                                    userMap.lock((Object) userModel.getNickname());
                                    UserCacheModel user = (UserCacheModel) userMap.get((Object) userModel.getNickname());
                                    user.setPassword(VinPlayUtils.getMD5Hash((String) password));
                                    userMap.put((Object) userModel.getNickname(), (Object) user);
                                    res = "0";
                                } catch (Exception e) {
                                    logger.debug((Object) e);
                                    break block15;
                                }
                                try {
                                    userMap.unlock((Object) userModel.getNickname());
                                } catch (Exception e) {
                                    // empty catch block
                                }
                            }
                            res = "0";
                            break block14;
                        }
                        res = "0";
                        break block14;
                    }
                    res = "1035";
                    break block14;
                } catch (Exception e2) {
                    logger.debug((Object) e2);
                }
            } else {
                res = "1036";
                break block14;
            }
        }
        return res;
    }
}

