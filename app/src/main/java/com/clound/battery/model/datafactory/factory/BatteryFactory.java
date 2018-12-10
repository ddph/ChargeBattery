package com.clound.battery.model.datafactory.factory;


import com.clound.battery.model.datafactory.product.BaseProduct;
import com.clound.battery.model.datafactory.product.Product10;
import com.clound.battery.model.datafactory.product.Product15;
import com.clound.battery.model.datafactory.product.Product16;
import com.clound.battery.model.datafactory.product.Product20;
import com.clound.battery.model.datafactory.product.Product21;
import com.clound.battery.model.datafactory.product.Product22;
import com.clound.battery.model.datafactory.product.Product23;
import com.clound.battery.model.datafactory.product.Product24;
import com.clound.battery.model.datafactory.product.Product25;
import com.clound.battery.model.datafactory.product.Product26;
import com.clound.battery.model.datafactory.product.Product27;
import com.clound.battery.model.datafactory.product.Product28;
import com.clound.battery.model.datafactory.product.Product29;
import com.clound.battery.model.datafactory.product.Product30;
import com.clound.battery.model.datafactory.product.Product31;
import com.clound.battery.model.datafactory.product.Product32;
import com.clound.battery.model.datafactory.product.Product34;
import com.clound.battery.model.datafactory.product.Product35;
import com.clound.battery.model.datafactory.product.Product36;
import com.clound.battery.model.datafactory.product.Product38;
import com.clound.battery.model.datafactory.product.Product39;
import com.clound.battery.model.datafactory.product.Product40;
import com.clound.battery.model.datafactory.product.Product41;
import com.clound.battery.model.datafactory.product.Product42;
import com.clound.battery.model.datafactory.product.Product44;
import com.clound.battery.model.datafactory.product.Product45;
import com.clound.battery.model.datafactory.product.Product46;
import com.clound.battery.model.datafactory.product.Product47;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class BatteryFactory extends BaseFactory {

    /**
     * 创建不同的产品
     */
    @Override
    public BaseProduct disposeData(int code) {
        BaseProduct product = null;
        switch (code) {
            case 10:
                product = new Product10();
                break;
            case 15:
                product = new Product15();
                break;
            case 16:
                product = new Product16();
                break;
            case 20:
                product = new Product20();
                break;
            case 21:
                product = new Product21();
                break;
            case 22:
                product = new Product22();
                break;
            case 23:
                product = new Product23();
                break;
            case 24:
                product = new Product24();
                break;
            case 25:
                product = new Product25();
                break;
            case 26:
                product = new Product26();
                break;
            case 27:
                product = new Product27();
                break;
            case 28:
                product = new Product28();
                break;
            case 29:
                product = new Product29();
                break;
            case 30:
                product = new Product30();
                break;
            case 31:
                product = new Product31();
                break;
            case 32:
                product = new Product32();
                break;
            case 34:
                product = new Product34();
                break;
            case 35:
                product = new Product35();
                break;
            case 36:
                product = new Product36();
                break;
            case 38:
                product = new Product38();
                break;
            case 39:
                product = new Product39();
                break;
            case 40:
                product = new Product40();
                break;
            case 41:
                product = new Product41();
                break;
            case 42:
                product = new Product42();
                break;
            case 44:
                product = new Product44();
                break;
            case 45:
                product = new Product45();
                break;
            case 46:
                product = new Product46();
                break;
            case 47:
                product = new Product47();
                break;
            default:
                break;
        }
        return product;
    }
}