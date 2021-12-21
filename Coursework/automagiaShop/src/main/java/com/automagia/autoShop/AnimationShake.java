package com.automagia.autoShop;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationShake {
    private TranslateTransition transition;
    
    public AnimationShake(Node node) {
        transition = new TranslateTransition(Duration.millis(100), node);
        transition.setFromX(0f);
        transition.setByX(15f);
        transition.setCycleCount(4);
        transition.setAutoReverse(true);
        
    }
    
    public void play() {
        transition.playFromStart();
    }
}
