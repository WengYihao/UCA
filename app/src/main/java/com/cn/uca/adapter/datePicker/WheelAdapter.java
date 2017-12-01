package com.cn.uca.adapter.datePicker;

/**
 * Wheel adapter interface
 * 
 * @deprecated Use WheelViewAdapter
 */
public interface WheelAdapter {
	/**
	 * Gets items count
	 * @return the count of wheel2 items
	 */
	public int getItemsCount();
	
	/**
	 * Gets a wheel2 item by index.
	 * 
	 * @param index the item index
	 * @return the wheel2 item text or null
	 */
	public String getItem(int index);
	
	/**
	 * Gets maximum item length. It is used to determine the wheel2 width.
	 * If -1 is returned there will be used the default wheel2 width.
	 * 
	 * @return the maximum item length or -1
	 */
	public int getMaximumLength();
}
