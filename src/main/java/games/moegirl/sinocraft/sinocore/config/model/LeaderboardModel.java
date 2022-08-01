package games.moegirl.sinocraft.sinocore.config.model;

import java.util.UUID;

public class LeaderboardModel {
    public Leaderboard[] leaderboard;

    public class Leaderboard {
        public int rank;
        public PlayerData player;
        public int timeUsed;
        public int triedTimes;
    }

    public class PlayerData {
        public String id;
        public UUID uuid;
    }
}
