package loop.theliwala.heartanimation;

import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;

/**
 * Created by LK on 8/26/2017.
 */
public class EasingInterpolator implements TimeInterpolator {

    private final Ease ease;

    public EasingInterpolator(@NonNull Ease ease) {
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float input) {
        return EasingProvider.get(this.ease, input);
    }

    public Ease getEase() {
        return ease;
    }
}
