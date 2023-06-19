/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.entities.taixiu.TransactionTaiXiuDetail
 */
package game.modules.minigame.entities;

import com.vinplay.dal.entities.taixiu.TransactionTaiXiuDetail;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import game.modules.minigame.entities.Pot;
import java.util.ArrayList;
import java.util.List;

public class PotTaiXiu
extends Pot {
    private long totalBotBet = 0L;
    private int numBot = 0;
    public List<TransactionTaiXiuDetail> contributors = new ArrayList<TransactionTaiXiuDetail>();
    public List<String> users = new ArrayList<String>();
    public List<String> realUsers = new ArrayList<String>();
    private UserService userService = new UserServiceImpl();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void bet(TransactionTaiXiuDetail trans, boolean isBot) {
        List<String> list;
        List<TransactionTaiXiuDetail> list2 = this.contributors;
        synchronized (list2) {
            this.contributors.add(trans);
        }
        list = users;
        synchronized (list) {
            if (!this.users.contains(trans.username)) {
                this.users.add(trans.username);
            }
        }
        this.totalValue += trans.betValue;
        if (isBot) {
            this.totalBotBet += trans.betValue;
            ++this.numBot;
        } else {
            this.realUsers.add(trans.username);
        }
    }

    @Override
    public void renew() {
        super.renew();
        this.contributors.clear();
        this.users.clear();
        this.realUsers.clear();
        this.totalBotBet = 0L;
        this.numBot = 0;
    }

    public long getTotalBetByUsername(String username) {
        long totalValue = 0L;
        for (TransactionTaiXiuDetail tran : this.contributors) {
            if (!tran.username.equals(username)) continue;
            totalValue += tran.betValue;
        }
        return totalValue;
    }

    public long getRealUserBet() {
        return this.getTotalValue() - this.getTotalBotBet();
    }

    public short getNumBet() {
        return (short)this.users.size();
    }

    public long getTotalBotBet() {
        return this.totalBotBet;
    }

    public int getNumBotBet() {
        return this.numBot;
    }

    public boolean hasBet(String userName){
        return this.realUsers.contains(userName);
    }

    public List<TransactionTaiXiuDetail> getRealContributors(){
        List<TransactionTaiXiuDetail> items = new ArrayList<>();
        for (TransactionTaiXiuDetail i: contributors) {
            if(!this.isBot(i.username)){
             items.add(i);
            }
        }
        return items;
    }

    public boolean isBot(String username) {
        UserCacheModel model = this.userService.getUser(username);
        return model.isBot();
    }
}

