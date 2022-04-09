package com.riggle.di.components;


import com.riggle.baseClass.RiggleApplication;/*
import com.riggle.di.modules.AppModule;
import com.riggle.di.modules.DataMangerModule;
import com.riggle.di.modules.NetworkModule;
import com.riggle.di.modules.UtilityModule;
import com.riggle.di.scopes.ApplicationScope;*/
import com.riggle.ui.base.activity.CustomAppActivityImpl;
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl;
import com.riggle.ui.bottomsheets.ProductVariantSheet;
import com.riggle.ui.dialogs.AddMemberDialog;
import com.riggle.ui.dialogs.ContactUsDialog;
import com.riggle.ui.dialogs.RemoveMemberDialog;
import com.riggle.ui.profile.MembersAdapter;
import com.riggle.utils.UserProfileSingleton;

import org.jetbrains.annotations.NotNull;

import dagger.Component;


//@ApplicationScope
//@Component(modules = {AppModule.class, NetworkModule.class, UtilityModule.class, DataMangerModule.class})
public interface ApplicationComponent {

    void inject(RiggleApplication riggleApplication);

    void inject(CustomAppActivityImpl customAppActivity);

    void inject(UserProfileSingleton userProfile);

    void inject(CustomAppFragmentViewImpl customAppFragmentView);

    void inject(AddMemberDialog addMemberDialog);

    void inject(@NotNull MembersAdapter membersAdapter);

    void inject(RemoveMemberDialog removeMemberDialog);

    void inject(ProductVariantSheet productVariantSheet);

    void inject(ContactUsDialog contactUsDialog);
}
