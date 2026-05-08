package com.conghoan.sinhviencntt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.DashboardItem;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private List<DashboardItem> items;
    private Context context;
    private int lastAnimatedPosition = -1;

    public DashboardAdapter(Context context, List<DashboardItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = items.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvSubtitle.setText(item.getSubtitle());

        // Load icon - ưu tiên URL, fallback drawable
        if (item.getLinkAnh() != null && !item.getLinkAnh().isEmpty()) {
            com.bumptech.glide.Glide.with(context)
                    .load(item.getLinkAnh())
                    .placeholder(item.getFallbackIconRes())
                    .error(item.getFallbackIconRes())
                    .into(holder.ivIcon);
        } else {
            holder.ivIcon.setImageResource(item.getFallbackIconRes());
        }

        int bgColor = ContextCompat.getColor(context, item.getBgColorResId());
        int iconColor = ContextCompat.getColor(context, item.getIconColorResId());

        holder.cardView.setCardBackgroundColor(blendWithWhite(bgColor, 0.45f));

        GradientDrawable circleDrawable = new GradientDrawable();
        circleDrawable.setShape(GradientDrawable.OVAL);
        circleDrawable.setColor(bgColor);
        holder.iconCircle.setBackground(circleDrawable);

        GradientDrawable blobDrawable = new GradientDrawable();
        blobDrawable.setShape(GradientDrawable.OVAL);
        blobDrawable.setColor(bgColor);
        holder.decorBlob.setBackground(blobDrawable);

        GradientDrawable dotDrawable = new GradientDrawable();
        dotDrawable.setShape(GradientDrawable.OVAL);
        dotDrawable.setColor(bgColor);
        holder.decorDot.setBackground(dotDrawable);

        GradientDrawable lineDrawable = new GradientDrawable();
        lineDrawable.setShape(GradientDrawable.RECTANGLE);
        lineDrawable.setCornerRadius(4f);
        lineDrawable.setColor(iconColor);
        holder.accentLine.setBackground(lineDrawable);

        holder.tvArrow.setTextColor(iconColor);

        holder.itemView.setOnClickListener(v -> {
            String loai = item.getLoai();
            if ("BUILTIN".equalsIgnoreCase(loai)) {
                android.content.Intent i = com.conghoan.sinhviencntt.util.ScreenRouter.intentFor(context, item.getMaManHinh());
                if (i != null) {
                    i.putExtra("title", item.getTitle());
                    context.startActivity(i);
                } else {
                    android.widget.Toast.makeText(context, "Tính năng chưa khả dụng", android.widget.Toast.LENGTH_SHORT).show();
                }
            } else if ("WEBVIEW".equalsIgnoreCase(loai)) {
                String url = item.getLinkTruyCap();
                if (url != null && !url.isEmpty()) {
                    android.content.Intent i = new android.content.Intent(context,
                            com.conghoan.sinhviencntt.activity.WebViewActivity.class);
                    i.putExtra(com.conghoan.sinhviencntt.activity.WebViewActivity.EXTRA_URL, url);
                    i.putExtra(com.conghoan.sinhviencntt.activity.WebViewActivity.EXTRA_TITLE, item.getTitle());
                    context.startActivity(i);
                } else {
                    android.widget.Toast.makeText(context, "Danh mục chưa cấu hình link", android.widget.Toast.LENGTH_SHORT).show();
                }
            } else {
                android.widget.Toast.makeText(context, "Loại danh mục không hợp lệ", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        if (position > lastAnimatedPosition) {
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_in));
            holder.itemView.getAnimation().setStartOffset(position * 100L);
            lastAnimatedPosition = position;
        }
    }

    private int blendWithWhite(int color, float ratio) {
        int r = (int) (Color.red(color) + (255 - Color.red(color)) * ratio);
        int g = (int) (Color.green(color) + (255 - Color.green(color)) * ratio);
        int b = (int) (Color.blue(color) + (255 - Color.blue(color)) * ratio);
        return Color.rgb(r, g, b);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivIcon;
        TextView tvTitle, tvSubtitle, tvArrow;
        View iconCircle, decorBlob, decorDot, accentLine;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            tvArrow = itemView.findViewById(R.id.tv_arrow);
            iconCircle = itemView.findViewById(R.id.icon_circle);
            decorBlob = itemView.findViewById(R.id.decor_blob);
            decorDot = itemView.findViewById(R.id.decor_dot);
            accentLine = itemView.findViewById(R.id.accent_line);
        }
    }
}
