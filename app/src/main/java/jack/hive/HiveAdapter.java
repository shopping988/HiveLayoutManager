package jack.hive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjchai on 16/9/10.
 */
public class HiveAdapter extends RecyclerView.Adapter<ImageViewHolder>{


    List<Integer> resId = new ArrayList<>() ;
    Context context;

    HiveAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_img, parent,false);
        return new ImageViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        if(position == 0 || position== 2 || position == 3 || position == 5
                || position == 6 || position == 10 || position == 16) {
            holder.bind(resId.get(position),position) ;
        }
    }

    @Override
    public int getItemCount() {
        return resId.size();
    }

    public void addData(Integer data,int index) {
        resId.add(index,data) ;
    }

    public void addData(Integer data) {
        resId.add(data) ;
    }

    public void remove(int index){
        resId.remove(index) ;
    }

    public void move(int r, int r2) {
        Integer id = resId.remove(r);
        resId.add(r2,id) ;
    }
}
