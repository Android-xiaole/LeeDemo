
package com.le.base;

public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();

        void cancelAll();
    }

    interface BaseView {

        /**
         * @param flag 用于标记对应接口
         * @param e 错误信息
         */
        void showError(int flag,Throwable e);

        /**
         * @param flag 用于标记对应接口
         */
        void complete(int flag);

    }
}
