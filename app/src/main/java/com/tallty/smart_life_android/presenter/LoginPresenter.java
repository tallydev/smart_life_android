package com.tallty.smart_life_android.presenter;

import com.tallty.smart_life_android.model.IUser;
import com.tallty.smart_life_android.model.User;
import com.tallty.smart_life_android.view.ILoginView;

/**
 * Created by kang on 16/6/15.
 * ILoginPresenter 实现类
 */
public class LoginPresenter implements ILoginPresenter {
    ILoginView iLoginView;
    IUser user;

    public LoginPresenter(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        initUser();
    }

    private void initUser() {
        user = new User("chenbo");
    }

    @Override
    public void processName() {
        iLoginView.showName(user.getName());
    }
}
