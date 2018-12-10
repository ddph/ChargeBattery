package com.clound.battery.model.datafactory.factory;


import com.clound.battery.model.datafactory.product.BaseProduct;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public abstract class BaseFactory {
    public abstract BaseProduct disposeData(int code);
}