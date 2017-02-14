package asywalul.bubbleshoot.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SplashScreen extends Activity {


    ImageView splashImage , camFlash;
    public static int screenWidth,screenHeight;
    RelativeLayout relativeLayout;

    Thread t ;
    Handler h;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashh);
        loadDimension();

        relativeLayout = (RelativeLayout) findViewById(R.id.rell);


        splashImage = (ImageView) findViewById(R.id.splashPicture);
        camFlash = (ImageView) findViewById(R.id.camflash);
        setInvisible(splashImage, camFlash);

        h = new Handler();
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {

                        final float end;
                        ViewGroup.LayoutParams layoutParams = splashImage.getLayoutParams();
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            layoutParams.height = (int) (screenWidth * 0.5);
                            layoutParams.width = (int) (screenWidth * 0.5);
                            end = screenHeight/2.5F;
                        }else{
                            layoutParams.height = (int) (screenHeight * 0.5);
                            layoutParams.width = (int) (screenHeight * 0.5);
                            end = screenHeight/4.5F;
                        }


                        splashImage.setLayoutParams(layoutParams);



                        final ValueAnimator bounceAnim = ObjectAnimator.ofFloat(splashImage, "y",-screenHeight, end);
                        bounceAnim.setDuration(2000);
                        bounceAnim.setInterpolator(new BounceInterpolator());

                        final ValueAnimator rotate = ObjectAnimator.ofFloat(splashImage,"rotation",90,0);
                        rotate.setDuration(2000);
                        rotate.setInterpolator(new BounceInterpolator());

                        final ValueAnimator scaleX = ObjectAnimator.ofFloat(camFlash,"scaleX",0f,1.1f);
                        scaleX.setDuration(180);
                        scaleX.setInterpolator(new BounceInterpolator());
                        final ValueAnimator scaleY = ObjectAnimator.ofFloat(camFlash,"scaleY",0f,1.1f);
                        scaleY.setDuration(180);
                        scaleY.setInterpolator(new BounceInterpolator());

                        final ValueAnimator scaleDownX = ObjectAnimator.ofFloat(camFlash,"scaleX",1.1f,0f);
                        scaleX.setDuration(180);
                        scaleX.setInterpolator(new BounceInterpolator());
                        final ValueAnimator scaleDownY = ObjectAnimator.ofFloat(camFlash,"scaleY",1.1f,0f);
                        scaleY.setDuration(180);
                        scaleY.setInterpolator(new BounceInterpolator());

                        final AnimatorSet set[] = new AnimatorSet[4];
                        initializeAnimators(set);

                        set[0].playTogether(rotate, bounceAnim);
                        set[0].start();

                        splashImage.setVisibility(View.VISIBLE);
                        set[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                camFlash.setVisibility(View.VISIBLE);

                                set[1].playTogether(scaleX, scaleY);
                                set[1].start();
                                set[1].addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationStart(animation);
                                        set[1].removeAllListeners();set[1].end(); set[1].cancel();

                                        set[2].playTogether(scaleDownX, scaleDownY);
                                        set[2].start();

                                        set[2].addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                set[2].removeAllListeners();set[2].end(); set[2].cancel();
                                                init();
                                                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
                                                finish();
                                            }
                                        });

                                    }
                                });

                            }
                        });
                    }
                })     ;
            }
        });t.start();

   }

    private void init() {
            Intent i = new Intent(SplashScreen.this,Start_Activity.class);
            startActivity(i);
    }



    private void initializeAnimators(AnimatorSet[] set) {
        for(int i = 0 ; i < set.length;i++) {
            set[i] = new AnimatorSet();
        }
    }

    private void setInvisible(ImageView splashImage, ImageView camFlash) {
        splashImage.setVisibility(View.INVISIBLE);
        camFlash.setVisibility(View.INVISIBLE);
    }




    private  void loadDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

}
