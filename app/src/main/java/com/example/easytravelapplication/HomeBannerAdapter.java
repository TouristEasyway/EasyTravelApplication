package com.example.easytravelapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeBannerAdapter extends
        SliderViewAdapter<HomeBannerAdapter.SliderAdapterVH> {

    private Context context;
    private List<BannerList> bannerArrayList;

    public HomeBannerAdapter(Context introductionActivity, ArrayList<BannerList> arrayList) {
        this.context = introductionActivity;
        this.bannerArrayList = arrayList;
    }

    public void renewItems(List<BannerList> sliderItems) {
        this.bannerArrayList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.bannerArrayList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(BannerList sliderItem) {
        this.bannerArrayList.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_banner, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        //BannerList sliderItem = mSliderItems.get(position);
        //viewHolder.banner.setImageResource(sliderItem.getImage());
        Glide.with(context).load(bannerArrayList.get(position).getImage()).placeholder(R.drawable.banner1).into(viewHolder.banner);
        viewHolder.name.setText(bannerArrayList.get(position).getDescription());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return bannerArrayList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView banner;
        TextView name;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.custom_banner_image);
            name = itemView.findViewById(R.id.custom_banner_name);
        }
    }

}