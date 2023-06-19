package game.modules.quest;

import com.vinplay.dailyQuest.model.DailyGiftData;
import com.vinplay.dailyQuest.model.DailyQuestData;

public class DailyQuestSendData {
    public DailyQuestData dailyQuestData;
    public DailyGiftData dailyGiftData;
    public int index;

    public DailyQuestSendData(DailyQuestData dailyQuestData, DailyGiftData dailyGiftData, int index) {
        this.dailyQuestData = dailyQuestData;
        this.dailyGiftData = dailyGiftData;
        this.index = index;
    }
}
