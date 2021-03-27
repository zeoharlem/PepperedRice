package com.zeoharlem.gads.pepperedrice.dialog;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zeoharlem.gads.pepperedrice.models.CartItem;
import com.zeoharlem.gads.pepperedrice.utils.L;
import com.zeoharlem.gads.pepperedrice.viewmodels.MenuFoodListViewModel;
import com.zeoharlem.gads.pepperedrice.models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.databinding.MenuFoodItemBottomDialogSheetBinding;

import java.util.List;

public class MenuFoodListBottomSheet extends BottomSheetDialogFragment {

    private byte[] mBytesArray;
    private MenuFoodItemBottomDialogSheetBinding mDialogSheetBinding;
    private MenuFoodListViewModel mMenuFoodListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDialogSheetBinding         = MenuFoodItemBottomDialogSheetBinding.inflate(inflater, container, false);
        return mDialogSheetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenuFoodListViewModel  = new ViewModelProvider(requireActivity()).get(MenuFoodListViewModel.class);
        mDialogSheetBinding.setMMenuFoodListViewModel(mMenuFoodListViewModel);
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
