package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.SoundPlayer;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gameState.dropcounter.DropCounter;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.speed.SpeedController;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.speed.VariableSpeedController;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import static com.jcrawleydev.gemsdrop.gameState.GameState.Type.*;

import java.util.HashMap;
import java.util.Map;

public class GameStateManagerImpl implements GameStateManager {

    private GameState currentGameState;

    private Map<GameState.Type, GameState> map;

    private Game game;
    private ScoreBoardLayer scoreBoardLayer;
    private GemGroupLayer gemGroupLayer;
    private GemGridLayer gemGridLayer;
    private GemControls gemControls;
    private Evaluator evaluator;
    private GemGroupFactory gemGroupFactory;
    private SoundPlayer soundPlayer;
    private int maxColumnHeight;
    private GemGroup gemGroup;
    private final DropCounter dropCounter;
    private SpeedController variableSpeedController;


    public GameStateManagerImpl(Builder builder) {
        assignFieldsFrom(builder);
        dropCounter = new DropCounter();

    }

    private void assignFieldsFrom(Builder builder){
        game = builder.game;
        gemGroupLayer = builder.gemGroupLayer;
        gemGridLayer = builder.gemGridView;
        gemControls = builder.gemControls;
        evaluator = builder.evaluator;
        gemGroupFactory = builder.gemGroupFactory;
        scoreBoardLayer = builder.scoreView;
        soundPlayer = builder.soundPlayer;
        maxColumnHeight = builder.maxColumnHeight;
    }

    @Override
    public void init() {
        initSpeedController();
        initStates();
    }


    private void initStates(){
        map = new HashMap<>();
        map.put(BEGIN_NEW_GAME,     new BeginNewGameState(this));
        map.put(CREATE_NEW_GEMS,    new CreateNewGemsState(this));
        map.put(DROP,               new DropState(this));
        map.put(EVALUATE_GRID,      new EvaluateGridState(this));
        map.put(FLICKER,            new FlickerState(this));
        map.put(FREE_FALL,          new FreeFallState(this));
        map.put(QUICK_DROP,         new QuickDropState(this));
        map.put(HEIGHT_EXCEEDED,    new HeightExceededState(this));
        map.put(GRID_GRAVITY,       new GridGravityState(this));

        map.put(GAME_OVER,          new GameState() {
            @Override
            public void start() {
                game.loadGameOverState();
            }
            public void stop() {}
        });
    }


    private void initSpeedController(){
        variableSpeedController = VariableSpeedController.Builder.newInstance()
                .baseInterval(700)
                .intervalMultiplier(40)
                .startingSpeed(1)
                .speedIncrease(1)
                .maxSpeed(20)
                .numberOfDropsToIncreaseSpeed(12).build();
    }


    @Override
    public void loadState(GameState.Type type) {
        if (currentGameState != null) {
            currentGameState.stop();
        }
        currentGameState = map.get(type);
        assert currentGameState != null;
        currentGameState.start();
    }


    @Override
    public DropCounter getDropCounter(){
        return dropCounter;
    }


    @Override
    public Evaluator getEvaluator(){
        return evaluator;
    }


    @Override
    public void loadState(GameState.Type type,GameState.Type source) {
        if (currentGameState != null) {
            currentGameState.stop();
        }
        currentGameState = map.get(type);
        assert currentGameState != null;
        currentGameState.start();
    }


    @Override
    public SpeedController getSpeedController() {
        return variableSpeedController;
    }


    @Override
    public GemGroup getGemGroup() {
        return gemGroup;
    }


    @Override
    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
    }

    @Override
    public GemGroupFactory getGemGroupFactory(){
        return this.gemGroupFactory;
    }

    @Override
    public GemGridLayer getGemGridLayer() {
        return gemGridLayer;
    }

    @Override
    public GemGroupLayer getGemGroupLayer() {
        return gemGroupLayer;
    }

    @Override
    public int getMaxColumnHeight(){
        return maxColumnHeight;
    }

    @Override
    public GemControls getControls(){
        return gemControls;
    }

    @Override
    public ScoreBoardLayer getScoreBoardLayer(){
        return scoreBoardLayer;
    }


    public static class Builder {
        private Game game;
        private Score score;
        private ScoreBoardLayer scoreView;
        private GemGroupLayer gemGroupLayer;
        private GemGridLayer gemGridView;
        private GemControls gemControls;
        private Evaluator evaluator;
        private GemGroupFactory gemGroupFactory;
        private GemCountTracker gemCountTracker;
        private SoundPlayer soundPlayer;
        private SpeedController speedController;
        private int gravityInterval;
        private int gridGravityDistanceFactor;
        private int flickerMarkedGemsTime;
        private int maxColumnHeight;


        private Builder(){}


        public static Builder newInstance(){
            return new Builder();
        }


        public GameStateManagerImpl.Builder game(Game game) {
            this.game = game;
            return this;
        }


        public GameStateManagerImpl.Builder gemGroupView(GemGroupLayer gemGroupView) {
            this.gemGroupLayer = gemGroupView;
            return this;
        }


        public GameStateManagerImpl.Builder gemControls(GemControls gemControls) {
            this.gemControls = gemControls;
            return this;
        }


        public GameStateManagerImpl.Builder evaluator(Evaluator evaluator) {
            this.evaluator = evaluator;
            return this;
        }


        public GameStateManagerImpl.Builder gemGroupFactory(GemGroupFactory gemGroupFactory) {
            this.gemGroupFactory = gemGroupFactory;
            return this;
        }


        public GameStateManagerImpl.Builder gridView(GemGridLayer gemGridView) {
            this.gemGridView = gemGridView;
            return this;
        }


        public GameStateManagerImpl.Builder scoreView(ScoreBoardLayer scoreView) {
            this.scoreView = scoreView;
            this.score = scoreView.getScore();
            return this;
        }


        public GameStateManagerImpl.Builder gridGravityDistanceFactor(int gridGravityDistanceFactor) {
            this.gridGravityDistanceFactor = gridGravityDistanceFactor;
            return this;
        }


        public GameStateManagerImpl.Builder gemCountTracker(GemCountTracker gemCountTracker) {
            this.gemCountTracker = gemCountTracker;
            return this;
        }


        public GameStateManagerImpl.Builder soundPlayer(SoundPlayer soundPlayer) {
            this.soundPlayer = soundPlayer;
            return this;
        }


        public GameStateManagerImpl.Builder speedController(SpeedController speedController) {
            this.speedController = speedController;
            return this;
        }


        public GameStateManagerImpl.Builder gravityInterval(int gravityInterval) {
            this.gravityInterval = gravityInterval;
            return this;
        }


        public GameStateManagerImpl.Builder flickerMarkedGemsTime(int flickerMarkedGemsTime) {
            this.flickerMarkedGemsTime = flickerMarkedGemsTime;
            return this;
        }


        public GameStateManagerImpl.Builder maxColumnHeight(int maxColumnHeight) {
            this.maxColumnHeight = maxColumnHeight;
            return this;
        }


        public GameStateManagerImpl build() {
            return new GameStateManagerImpl(this);
        }

    }
}
