/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.vinplay.vbee.common.models.minigame.pokego.LSGDPokeGo
 *  com.vinplay.vbee.common.response.BaseResponseModel
 */
package com.vinplay.api.processors.giftCode;

import com.vinplay.vbee.common.response.BaseResponseModel;

public class GiftCodeResponse
extends BaseResponseModel {
    public GiftCodeResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }
    public long currentMoney;
}

