package game.ConfigFake;

public class UpdateFakeRoomTimer implements Runnable {

    @Override
    public void run() {
        RoomFakeConfig.getInstance().changeConfigFakeRoom();
    }
}
