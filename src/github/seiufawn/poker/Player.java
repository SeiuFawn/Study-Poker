package github.seiufawn.poker;

public class Player {

    public String name;
    public int count = -1;

    /**
     * 字符串转Player
     *
     * @param string 字符串
     * @return Player 失败则是null
     */
    public static Player FormString(String string) {
        Player player = new Player();
        for (String s : string.split(",")) {
            String[] args = s.split(":");
            if (args.length > 1) {
                switch (args[0]) {
                    case "name":
                        player.name = args[1];
                        break;
                    case "size":
                        player.count = Integer.parseInt(args[1]);
                        break;
                }
            }
        }

        if (player.name != null && player.count != -1) {
            return player;
        } else {
            return null;
        }
    }
}
