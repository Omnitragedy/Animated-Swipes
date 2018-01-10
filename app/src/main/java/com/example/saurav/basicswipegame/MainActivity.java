package com.example.saurav.basicswipegame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.GestureDetector.*;

public class MainActivity extends AppCompatActivity {

    private GestureDetector mGesture;
    private RelativeLayout layout;
    private Button startBtn;
    private ImageView imageView;

    private TextView scoreView;
    private TextView failsView;

    private ArrayList<ArrowTextView> arrowTextViews = new ArrayList<>();
    private static int animationDur = 3000;

    private int numberOfArrowsGenerated;
    private int score;
    private int fails;

    private OnGestureListener mOnGesture = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("DEBUG:", "detected fling");

            Arrow.Direction swipeDirection = Arrow.getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY());

            Rect imageRect = new Rect();
            Rect arrowRect = new Rect();



            imageView.getGlobalVisibleRect(imageRect);
            ArrowTextView textViewOfInterest = getTextViewOfInterest(imageRect);

            textViewOfInterest.getGlobalVisibleRect(arrowRect);



            if(Rect.intersects(imageRect, arrowRect)) {
                if(textViewOfInterest.arrow.dir == swipeDirection)
                    score += 10;
                else if(score > 0) {
                    score -= 10;
                    fails++;
                }
            } else {
                score -= 10;
                fails++;
            }

            scoreView.setText(score + " points");
            failsView.setText(fails + "");

            if(fails > 4)
                onGameOver();


            Log.d("Fling Direction:", "Flung: " + swipeDirection.name() + ", Expected: " + textViewOfInterest.arrow.dir.name());
            Log.d("SCORE:", "Score: " + score);
            return true;
        }
    };

    private void onGameOver() {
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    /**
     * Determines which arrow is the one needed to check for intersection.
     * NOTE: It is given the first element of the array is the rightmost arrow on the screen and
     * that this arrow is still on the screen
     *
     * @return the rightmost arrow that is still left of the middle bar's right x-coordinate
     */
    private ArrowTextView getTextViewOfInterest(Rect rectForImageView) {
        boolean isOfInterestFound = false;

        int i = 0;
        float imgXCoor = rectForImageView.centerX() + rectForImageView.width()/2;

        while(!isOfInterestFound) {
            Rect arrowRect = new Rect();
            arrowTextViews.get(i).getGlobalVisibleRect(arrowRect);
            float arrowLeftCoor = arrowRect.centerX() - arrowRect.width()/2;

            if(arrowLeftCoor < imgXCoor) {
                isOfInterestFound = true;
            } else
                i++;
        }

        return arrowTextViews.get(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adding the horizontal pane (vertical linear layout) to contain the arrows onscreen
        layout = findViewById(R.id.layout);

        startBtn = findViewById(R.id.start);
        imageView = findViewById(R.id.imageView);

        scoreView = findViewById(R.id.score);
        scoreView.setText("0 points");
        failsView = findViewById(R.id.fails);
        failsView.setText("0");

        mGesture = new GestureDetector(this, mOnGesture);
        numberOfArrowsGenerated = 0;
        score = 0;
        fails = 0;
    }


    public void onStart(View v) {
        //TODO: Start game code
        startBtn.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);


        Log.e("DEBUG", "Started Generating ArrowTextViews");
        for(int i = 0; i < 4; i++) {
            addNewTextView(numberOfArrowsGenerated * animationDur/4);
        }
    }

    private void addNewTextView(int withDelay) {
        ArrowTextView atv = new ArrowTextView(this, generateNewArrow());
        arrowTextViews.add(atv);
        layout.addView(atv);

        initiateTextViewTranslate(atv, withDelay);
    }

    /**
     * Returns a new arrow object with random direction
     * @return new Arrow object with random direction
     */
    @NonNull
    private Arrow generateNewArrow() {
        int randInt = (int)(Math.random()*4);
        numberOfArrowsGenerated++;
        switch(randInt) {
            case 0:
                return new Arrow(Arrow.Direction.UP);
            case 1:
                return new Arrow(Arrow.Direction.DOWN);
            case 2:
                return new Arrow(Arrow.Direction.LEFT);
            case 3://you are the most nubbiest nub ther eis in java u no dat??
                return new Arrow(Arrow.Direction.RIGHT);
        }

        return new Arrow(Arrow.Direction.UP);
    }

    /**
     * Translate the TextView with the arrow across the width of the screen
     * @param a the TextView being translated
     */
    private void initiateTextViewTranslate(ArrowTextView a, int withDelay) {
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(a, "x", layout.getWidth() + a.getWidth());

        animatorX.setDuration(animationDur);
        animatorX.setStartDelay(withDelay);
        animatorX.setRepeatCount(0);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                layout.removeView(arrowTextViews.remove(0));
                Log.e("ARROWS ARRAY LENGTH", "Number of Arrows in array:" + arrowTextViews.size());
                addNewTextView(0);
//                TODO: experimentation
//              arrowTextViews.get(arrowTextViews.size()-1).getAnimation().cancel();
//                arrowTextViews.get(arrowTextViews.size()-1).getAnimation().setDuration(1000);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animatorX.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean handled = super.dispatchTouchEvent(event);
        handled = mGesture.onTouchEvent(event);
        return handled;
    }
}
