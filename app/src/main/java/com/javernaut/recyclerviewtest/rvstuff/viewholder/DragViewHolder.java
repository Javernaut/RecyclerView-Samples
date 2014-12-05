package com.javernaut.recyclerviewtest.rvstuff.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.InjectView;
import com.javernaut.recyclerviewtest.R;

public class DragViewHolder extends ButterKnifeViewHolder {
    @InjectView(R.id.text)
    TextView text;

    public DragViewHolder(View itemView) {
        super(itemView);
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
