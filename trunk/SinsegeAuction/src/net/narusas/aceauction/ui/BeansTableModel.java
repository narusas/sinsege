/*
 * 
 */
package net.narusas.aceauction.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.table.DefaultTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class BeansTableModel.
 */
public class BeansTableModel extends DefaultTableModel {
	
	/** The bean. */
	private Object bean;

	/**
	 * Instantiates a new beans table model.
	 */
	BeansTableModel() {
		super();
		addColumn("Key");
		addColumn("Value");
	}

	/**
	 * Clear.
	 */
	public void clear() {
		while (getRowCount() > 0) {
			removeRow(0);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 1;
	}

	/**
	 * Sets the beans.
	 * 
	 * @param bean the new beans
	 */
	public void setBeans(Object bean) {
		this.bean = bean;
		clear();
		Class clazz = bean.getClass();
		Method[] m = clazz.getMethods();

		for (Method method : m) {
			String name = method.getName();
			if (name.startsWith("get")) {
				try {
					addRow(new Object[] { name.substring(3), method.invoke(bean, new Object[] {}) });
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object data, int row, int col) {
		String name = "set" + (String) getValueAt(row, 0);
		System.out.println(name);
		Class clazz = bean.getClass();
		Method[] m = clazz.getMethods();

		for (Method method : m) {
			if (name.equals(method.getName())) {
				try {
					method.invoke(bean, new Object[] { data });
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	/**
	 * Checks if is type match.
	 * 
	 * @param method the method
	 * 
	 * @return true, if is type match
	 */
	private boolean isTypeMatch(Method method) {
		return method.getReturnType() == String.class || method.getReturnType() == Integer.class
				|| method.getReturnType() == Boolean.class;
	}
}
