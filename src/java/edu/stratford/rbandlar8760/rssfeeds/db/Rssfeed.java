package edu.stratford.rbandlar8760.rssfeeds.db;

import edu.stratford.rbandlar8760.rssfeeds.db.auto._Rssfeed;

public class Rssfeed extends _Rssfeed {

    private static Rssfeed instance;

    private Rssfeed() {}

    public static Rssfeed getInstance() {
        if(instance == null) {
            instance = new Rssfeed();
        }

        return instance;
    }
}
