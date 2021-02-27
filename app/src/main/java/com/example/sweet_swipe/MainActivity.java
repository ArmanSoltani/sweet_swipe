package com.example.sweet_swipe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    static float hearthSize = 160;
    RelativeLayout rootLayout;
    FrameLayout mImagesContainer;
    ImageView mImage1;
    ImageView mImage2;
    TextView mDetails;
    TextView mName;
    int curProfil = 0;
    boolean detailsShow = false;
    boolean swipeAnimRunning = false;
    boolean swap = true;

    private int shortAnimationDuration;
    private int initalImageHeight;

    private GestureDetectorCompat mGestureDetector;
    private GestureDetectorCompat mDoubleTapDetecor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sweet Swipe");

        rootLayout = findViewById(R.id.main_activity_root);

        mImagesContainer = findViewById(R.id.fl_image_container);
        mImagesContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImagesContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initalImageHeight = mImagesContainer.getHeight(); // On attend que le FrameLayout soit créée pour récupérer sa taille
            }
        });

        // init image profil
        mImage1 = findViewById(R.id.iv_profil1);
        mImage2 = findViewById(R.id.iv_profil2);
        mImage1.setClipToOutline(true);
        mImage2.setClipToOutline(true);
        mImage2.setVisibility(View.GONE);
        mImage1.setImageResource(Data.profilePictures[curProfil]);

        // init description profil
        mDetails = findViewById(R.id.tv_profil_details);
        mName = findViewById(R.id.tv_profil_name);
        mDetails.setVisibility(View.GONE);
        mName.setVisibility(View.GONE);
        mDetails.setAlpha(0f);
        mName.setAlpha(0f);

        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mGestureDetector = new GestureDetectorCompat(this, new CustomGestureListener());
        mDoubleTapDetecor = new GestureDetectorCompat(this, new CustomDoubleTapListener());
        mImage1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDoubleTapDetecor.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        mImage2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDoubleTapDetecor.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    /**
     * Permet de faire apparaitre un coeur animé
     *
     * @param x la position X du coeur
     * @param y la position Y du coeur
     */
    public void popHearth(float x, float y) {
        // On crée une nouvelle ImageView au bon endroit et on la place dans le rootLayout
        final ImageView hearth = new ImageView(this);
        int size = (int) hearthSize;
        // Si on affiche pas les détails du profil alors la photo est plus grande et c'est plus
        // estétique d'augmenter la taille du coeur
        if (!detailsShow)
            size *= 2;
        hearth.setLayoutParams(new FrameLayout.LayoutParams(size, size));
        hearth.setImageResource(R.drawable.baseline_favorite_24);
        hearth.setX(x);
        hearth.setY(y);
        rootLayout.addView(hearth);

        // initialisation de l'animation
        Random rand = new Random();
        Animations.HearthAnimation hearthAnim = new Animations.HearthAnimation(hearth, 0f, rand.nextInt(1000) - 500, -(rand.nextInt(500) + 1000));
        hearthAnim.setDuration(1000);
        hearthAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rootLayout.removeView(hearth);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        hearth.startAnimation(hearthAnim);
    }

    /**
     * Affiche les détails du profil et diminue la taille de l'image de profil
     */
    private void showDetails() {
        if (detailsShow || swipeAnimRunning)
            return;

        mDetails.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);

        Animations.ResizeHeightAnimation imageResizeAnim = new Animations.ResizeHeightAnimation(mImagesContainer,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
        imageResizeAnim.setDuration(shortAnimationDuration);

        Animations.AlphaAnimation nameAlphaAnim = new Animations.AlphaAnimation(mName, 1);
        nameAlphaAnim.setDuration(shortAnimationDuration);
        Animations.AlphaAnimation detailsAlphaAnim = new Animations.AlphaAnimation(mDetails, 1);
        detailsAlphaAnim.setDuration(shortAnimationDuration);

        mImagesContainer.startAnimation(imageResizeAnim);
        mName.startAnimation(nameAlphaAnim);
        mDetails.startAnimation(detailsAlphaAnim);

        updateDetails();
        detailsShow = true;
    }

    /**
     * Cache les détails du profil et agrandit l'image de profil
     */
    private void hideDetails() {
        if (!detailsShow || swipeAnimRunning)
            return;

        mDetails.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);

        Animations.ResizeHeightAnimation imageResizeAnim = new Animations.ResizeHeightAnimation(mImagesContainer, initalImageHeight);
        imageResizeAnim.setDuration(shortAnimationDuration);

        Animations.AlphaAnimation nameAlphaAnim = new Animations.AlphaAnimation(mName, 0);
        nameAlphaAnim.setDuration(shortAnimationDuration);
        nameAlphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mName.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        Animations.AlphaAnimation detailsAlphaAnim = new Animations.AlphaAnimation(mDetails, 0);
        detailsAlphaAnim.setDuration(shortAnimationDuration);
        detailsAlphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDetails.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mImagesContainer.startAnimation(imageResizeAnim);
        mName.startAnimation(nameAlphaAnim);
        mDetails.startAnimation(detailsAlphaAnim);

        detailsShow = false;
    }

    /**
     * Change la photo de profil affichée.
     * L'App utilise une logique de swap buffer afin de n'utiliser que deux ImageView tout en
     * donnant l'illusion à l'utilisateur de parcourir une séquence continue d'image.
     *
     * @param rightTransition Indique si c'est une transition vers le prochain profil (vers la droite)
     *                        ou vers le profil précédent (vers la gauche)
     */
    private void updateProfil(boolean rightTransition) {
        swipeAnimRunning = true;

        // curImage pointe vers l'ImageView à l'écran
        // bufImage pointe vers l'ImageView qu'on va utilise pour afficher le nouveau profil
        final ImageView curImage;
        final ImageView bufImage;
        // Le booléen "swap" indique quelle ImageView est actuellement utilisée
        if (swap) {
            curImage = mImage1;
            bufImage = mImage2;
        } else {
            curImage = mImage2;
            bufImage = mImage1;
        }

        // On place bufImage hors de l'écran à droite ou à gauche
        bufImage.setImageResource(Data.profilePictures[curProfil]);
        int initialX = getResources().getDisplayMetrics().widthPixels / 2 + curImage.getWidth() / 2;
        if (!rightTransition)
            initialX *= -1;
        bufImage.setTranslationX(initialX);
        bufImage.setVisibility(View.VISIBLE);

        // Animation de bufImage entrant à l'écran
        Animations.TranslateXAnimation bufImageTranslationnim = new Animations.TranslateXAnimation(bufImage, 0f);
        bufImageTranslationnim.setDuration(shortAnimationDuration);

        // Animation de curImage sortant de l'écran
        Animations.TranslateXAnimation curImageTranslationAnim = new Animations.TranslateXAnimation(curImage, -initialX);
        curImageTranslationAnim.setDuration(shortAnimationDuration);
        curImageTranslationAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                curImage.setVisibility(View.GONE);
                curImage.setTranslationX(0f);
                swipeAnimRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        bufImage.startAnimation(bufImageTranslationnim);
        curImage.startAnimation(curImageTranslationAnim);

        if (detailsShow)
            updateDetails();

        swap = !swap;
    }

    /**
     * Update de text de description du profil à l'écran
     */
    private void updateDetails() {
        mDetails.setText(Data.profileDetails[curProfil]);
        mName.setText(Data.profileName[curProfil]);
    }

    /**
     * Afficher le profil suivant
     */
    private void nextProfile() {
        if (swipeAnimRunning)
            return;

        curProfil++;
        if (curProfil >= Data.nbProfil)
            curProfil = 0;
        updateProfil(true);
    }

    /**
     * Afficher le profil précédent
     */
    private void prevProfile() {
        if (swipeAnimRunning)
            return;

        curProfil--;
        if (curProfil < 0)
            curProfil = Data.nbProfil - 1;
        updateProfil(false);
    }

    /**
     * Gestionnaire des doubles clics
     */
    class CustomDoubleTapListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ImageView curImage;
            if (swap)
                curImage = mImage1;
            else
                curImage = mImage2;
            float size = hearthSize;
            if (!detailsShow)
                size *= 2;
            popHearth(e.getX() + curImage.getLeft() - (size / 2),
                    e.getY() + curImage.getTop() - (size / 2));

            return true;
        }
    }

    /**
     * Gestionnaire des swipes
     */
    class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                // c'est un swipe horizontal
                if (velocityX > 0) {
                    // c'est un swipe vers la droite
                    prevProfile();
                } else {
                    // c'est un swipe vers la gauche
                    nextProfile();
                }
            } else {
                // c'est un swipe vertical
                if (velocityY > 0) {
                    // c'est un swipe vers le bas
                    hideDetails();
                } else {
                    // c'est un swipe vers le haut
                    showDetails();
                }
            }
            return true;
        }
    }
}