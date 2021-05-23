package pan.alexander.notes.presentation.animation;

import android.view.View;

public class ViewAnimation {

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
                .setDuration(200)
                .translationY(0)
                .alpha(1f)
                .start();
    }

    public static void fabShowOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .withEndAction(() -> v.setVisibility(View.GONE))
                .alpha(0f)
                .start();
    }

    public static boolean fabRotate(final View v, boolean rotate) {
        v.animate().setDuration(200)
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
                .setDuration(200)
                .alpha(0f)
                .withEndAction(() -> v.setVisibility(View.INVISIBLE))
                .start();
    }

    public static void bottomNavigationShow(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .alpha(1f)
                .start();
    }

    public static void bottomNavigationHide(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .withEndAction(() -> v.setVisibility(View.GONE))
                .alpha(0f)
                .start();
    }

    public static void fabParentLayoutUp(final View v, float height) {
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(-height)
                .start();
    }

    public static void fabParentLayoutDown(final View v, float height) {
        v.setTranslationY(-height);
        v.animate()
                .setDuration(200)
                .translationY(0)
                .start();
    }
}
