/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.beans;

/**
 *
 * @author moth
 */
public interface Converter<F,T> {
    public T convert(F value);
}
