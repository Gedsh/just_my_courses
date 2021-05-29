package pan.alexander.notes.presentation.animation;

import android.view.View;

import static pan.alexander.notes.utils.AppConstants.DEFAULT_ANIMATION_DURATION;

public class ViewAnimation {

    private final static int ANIMATION_START_DELAY = 200;

    public static void fabInit(final View v) {
        v.setVisibility(View.INVISIBLE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }

    public static void fabShowIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(0)
                .alpha(1f)
                .start();
    }

    public static void fabShowOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(v.getHeight())
                .withEndAction(() -> v.setVisibility(View.GONE))
                .alpha(0f)
                .start();
    }

    public static boolean fabRotate(final View v, boolean rotate) {
        v.animate().setDuration(DEFAULT_ANIMATION_DURATION)
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }

    public static void fabLayoutShow(final View v) {
        v.setAlpha(0f);
        v.setVisibility(View.VISIBLE);
        v.animate()
                .setDuration(500)
                .alpha(1f)
                .start();
    }

    public static void fabLayoutHide(final View v) {
        v.setAlpha(1f);
        v.setVisibility(View.VISIBLE);
        v.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .alpha(0f)
                .withEndAction(() -> v.setVisibility(View.INVISIBLE))
                .start();
    }

    public static void bottomNavigationShow(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(0)
                .alpha(1f)
                .start();
    }

    public static void bottomNavigationHide(final View v, boolean withDelay) {

        int delay = 0;
        if (withDelay) {
            delay = ANIMATION_START_DELAY;
        }

        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setStartDelay(delay)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(v.getHeight())
                .withEndAction(() -> v.setVisibility(View.GONE))
                .alpha(0f)
                .start();
    }

    public static void fabParentLayoutUp(final View v, float height) {
        v.setTranslationY(0);
        v.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(-height)
                .start();
    }

    public static void fabParentLayoutDown(final View v, float height) {
        v.setTranslationY(-height);
        v.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(0)
                .start();
    }
}
