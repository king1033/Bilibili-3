package org.pqh.entity;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Created by 10295 on 2016/7/3.
 */
public class AvPlay{
    private String title;
    private Integer play;
    private Timestamp timestamp;
    private Integer ranking;

    public AvPlay(String title, Integer play, Timestamp timestamp, Integer ranking) {
        this.title = title;
        this.play = play;
        this.timestamp = timestamp;
        this.ranking = ranking;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPlay() {
        return play;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public AvPlay(String title, Integer play, Timestamp timestamp) {
        this.title = title;
        this.play = play;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AvPlay{" +
                "title='" + title + '\'' +
                ", play=" + play +
                ", timestamp=" + timestamp +
                ", ranking=" + ranking +
                '}';
    }
}
