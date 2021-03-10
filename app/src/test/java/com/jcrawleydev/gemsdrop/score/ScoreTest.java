package com.jcrawleydev.gemsdrop.score;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoreTest {

    private Score score;
    private int BASE_SCORE = 100;


    @Before
    public void init(){
        score = new Score(BASE_SCORE);
    }


    @Test
    public void canAddPointsToScore(){
        assertEquals(0, score.get());
        int numberOfGems = 1;
        int multiplier = 1;
        //score.addPointsFor(numberOfGems, multiplier);
        assertEquals(BASE_SCORE, score.get());
        //score.addPointsFor(2, 2);
        assertEquals(BASE_SCORE * 5, score.get());
    }

    @Test
    public void canClearScore(){
        score.addPointsFor(12);
        score.addPointsFor(10);
        score.addPointsFor(4);
        score.clear();
        assertEquals(0, score.get());

    }

}
