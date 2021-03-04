package com.zeoharlem.gads.pepperedrice.Dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zeoharlem.gads.pepperedrice.Models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.databinding.MenuFoodItemBottomDialogSheetBinding;

public class MenuFoodListBottomSheet extends BottomSheetDialogFragment {

    private byte[] mBytesArray;
    MenuFoodItemBottomDialogSheetBinding mDialogSheetBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments            = getArguments();
        MenuFoodItem menuItemFood   = arguments.getParcelable("menuItemKey");
        mBytesArray                 = arguments.getByteArray("imageUri");
        mDialogSheetBinding         = MenuFoodItemBottomDialogSheetBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.menu_food_item_bottom_dialog_sheet, container, false);
        View view                   = mDialogSheetBinding.getRoot();
        setMenuItemDetail(menuItemFood, view);
        setImageViewBytesArray(view);
        return view;
    }

    private void setMenuItemDetail(MenuFoodItem menuFoodItem, View view){
        TextView textViewTitle  = view.findViewById(R.id.bottomSheetTitle);
        textViewTitle.setText(menuFoodItem.getTitle());
    }

    private void setImageViewBytesArray(View view){
        Bitmap bitmap   = BitmapFactory.decodeByteArray(mBytesArray, 0, mBytesArray.length);
        ImageView imageView = view.findViewById(R.id.imageView5);
        imageView.setImageBitmap(bitmap);
    }
}
