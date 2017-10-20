package com.gome.bottom.anim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gome.bottom.anim.widget.ZoomAnimView;
import com.gome.bottom.anim.widget.ZoomAnimationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ZoomAnimationView mFocusAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.gome_plus_bottom1).setOnClickListener(this);
        this.findViewById(R.id.gome_plus_bottom2).setOnClickListener(this);
        this.findViewById(R.id.gome_plus_bottom3).setOnClickListener(this);
        this.findViewById(R.id.gome_plus_bottom4).setOnClickListener(this);
        this.findViewById(R.id.gome_plus_bottom5).setOnClickListener(this);
        mFocusAnimView = (ZoomAnimationView) this.findViewById(R.id.gome_plus_bottom1);
        mFocusAnimView.showFocusView(false);
    }

    @Override
    public void onClick(View v) {

        if(v instanceof ZoomAnimationView && mFocusAnimView!= null && mFocusAnimView.isAnimFinish()){

            mFocusAnimView.showUnFocusView();
            mFocusAnimView = (ZoomAnimationView) v;
        }
    }
}
