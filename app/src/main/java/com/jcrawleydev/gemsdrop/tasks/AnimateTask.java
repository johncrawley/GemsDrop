package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.view.UpdatableView;

public class AnimateTask implements Runnable {

    private final UpdatableView[] updatableViews;

    public AnimateTask(UpdatableView... views){
        this.updatableViews = views;
    }

    public void run() {
       for(UpdatableView uv : updatableViews){
           uv.drawIfUpdated();
       }
    }


}